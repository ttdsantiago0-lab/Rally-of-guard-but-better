package net.hfstack.rallyguard.screen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;
import net.hfstack.rallyguard.network.payload.PlannerActionC2SPayload;
import net.hfstack.rallyguard.network.payload.PlannerStateS2CPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * E3A A4 - the Battle Planner (client). It only DRAWS the state the server sends
 * ({@link PlannerStateS2CPayload}) and SENDS actions ({@link PlannerActionC2SPayload});
 * authority, validation and execution are server side ({@code force.PlannerService}).
 * <ul>
 *     <li><b>Map</b>: a top-down map drawn by code from the CLIENT's loaded chunks only -
 *     one {@code MapColor} per column with height shading like vanilla maps, built
 *     {@link #COLUMNS_PER_FRAME} columns per frame into a 128×128 dynamic texture (never the
 *     whole world; unloaded columns stay dark). Zoom 1/2/4 blocks per pixel. Markers: units
 *     (by type), HQs, known enemies, and the two plan points (left click = A, right click = B).</li>
 *     <li><b>Left</b>: forces (select = its guards become the selection; G + number does the
 *     same), doctrine, operation shortcuts, units with their role (click cycles it).</li>
 *     <li><b>Right</b>: the plan - name, linear phases (objective + trigger + value), links
 *     between units, retreat line; Execute / Cancel / Save / Load / Delete.</li>
 * </ul>
 * No external map mod is needed (an optional Xaero integration could come later).
 */
public class BattlePlannerScreen extends Screen {

    private static final int MAP_PX = 128;
    private static final int COLUMNS_PER_FRAME = 1024;
    private static final ResourceLocation MAP_ID = ResourceLocation.fromNamespaceAndPath("rallyguard", "planner_map");
    private static final String[] OBJECTIVES = {"HOLD", "ATTACK_ZONE", "ATTACK_GROUP", "ESCORT", "PATROL", "RETREAT", "RALLY"};
    private static final String[] TRIGGERS = {"NONE", "TIME", "CASUALTIES", "ENEMY_SEEN", "CONTACT"};
    private static final String[] LINKS = {"RELIEF", "COVER", "REINFORCE", "RENDEZVOUS", "SCREEN", "ESCORT",
            "HOLD_UNTIL_RELIEVED", "STAGGERED_RETREAT"};
    private static final String[] DOCTRINES = {"STANDARD", "HARASS", "AVOID_CASUALTIES", "DISTRACT", "ALL_OUT",
            "HOLD_POSITION", "AMBUSH"};
    private static final String[] ROLES = {"", "ANVIL", "HAMMER", "SCREEN", "SUPPORT", "FLANK", "RESERVE"};
    private static final String[] OPS = {"HOLD_LINE", "FALLBACK_FIGHTING", "ADVANCE_BY_TURNS", "FLANK", "ORDERLY_RETREAT", "STOP"};
    private static final double[] RETREAT_AT = {0, 0.25, 0.4, 0.6};

    private JsonObject state = new JsonObject();
    private String selectedForce = "";
    private String message = "";

    // Plan being edited
    private final List<JsonObject> phases = new ArrayList<>();
    private final List<JsonObject> links = new ArrayList<>();
    private int objective, trigger, linkType, linkTrigger, linkFrom, linkTo, retreatAt, loadIndex;
    private double value = 30, linkValue = 0.4;
    private double ax = Double.NaN, az, bx = Double.NaN, bz;
    private EditBox nameBox;
    private String nameDraft = "plan";

    // Map
    private DynamicTexture texture;
    private NativeImage image;
    private double centreX, centreZ;
    private int scale = 2;
    private int cursor;
    private int[] lastHeight = new int[MAP_PX];

    public BattlePlannerScreen() {
        super(Component.translatable("gui.rallyguard.planner.title"));
    }

    // ==== State from the server ====

    public static void onState(PlannerStateS2CPayload payload) {
        Minecraft mc = Minecraft.getInstance();
        JsonObject s;
        try {
            s = JsonParser.parseString(payload.json()).getAsJsonObject();
        } catch (RuntimeException e) {
            return;
        }
        if (mc.screen instanceof BattlePlannerScreen open) {
            open.accept(s);
        } else if (payload.open()) {
            BattlePlannerScreen screen = new BattlePlannerScreen();
            screen.accept(s);
            screen.centreX = s.has("px") ? s.get("px").getAsDouble() : 0;
            screen.centreZ = s.has("pz") ? s.get("pz").getAsDouble() : 0;
            mc.setScreen(screen);
        }
    }

    private void accept(JsonObject s) {
        state = s;
        if (s.has("message")) message = s.get("message").getAsString();
        if (selectedForce.isEmpty() && s.has("active")) selectedForce = s.get("active").getAsString();
        if (selectedForce.isEmpty() && !forces().isEmpty()) selectedForce = forces().get(0).get("squad").getAsString();
        if (s.has("loaded")) loadPlan(s.getAsJsonObject("loaded"));
        if (this.minecraft != null) rebuild();
    }

    private List<JsonObject> forces() {
        List<JsonObject> out = new ArrayList<>();
        if (state.has("forces")) for (JsonElement e : state.getAsJsonArray("forces")) out.add(e.getAsJsonObject());
        return out;
    }

    private JsonObject force() {
        for (JsonObject f : forces()) if (f.get("squad").getAsString().equalsIgnoreCase(selectedForce)) return f;
        return null;
    }

    private List<JsonObject> units() {
        List<JsonObject> out = new ArrayList<>();
        JsonObject f = force();
        if (f != null) for (JsonElement e : f.getAsJsonArray("units")) out.add(e.getAsJsonObject());
        return out;
    }

    private static void send(String action, JsonObject args) {
        PacketDistributor.sendToServer(new PlannerActionC2SPayload(action, args.toString()));
    }

    private JsonObject forceArgs() {
        JsonObject o = new JsonObject();
        o.addProperty("force", selectedForce);
        return o;
    }

    // ==== Layout ====

    private int left() {
        return 6;
    }

    private int mapSize() {
        return Math.max(96, Math.min(this.width - 2 * 160 - 24, this.height - 60));
    }

    private int mapX() {
        return 166 + (this.width - 2 * 166 - mapSize()) / 2;
    }

    private int mapY() {
        return 22;
    }

    private int right() {
        return this.width - 162;
    }

    @Override
    protected void init() {
        if (texture == null) {
            image = new NativeImage(MAP_PX, MAP_PX, false);
            image.fillRect(0, 0, MAP_PX, MAP_PX, 0xFF202020);
            texture = new DynamicTexture(image);
            Minecraft.getInstance().getTextureManager().register(MAP_ID, texture);
        }
        rebuild();
    }

    private void rebuild() {
        if (nameBox != null) nameDraft = nameBox.getValue();
        this.clearWidgets();
        int y = 22;
        // Forces
        for (JsonObject f : forces()) {
            String squad = f.get("squad").getAsString();
            boolean sel = squad.equalsIgnoreCase(selectedForce);
            addRenderableWidget(Button.builder(Component.literal((sel ? "▶ " : "") + squad + " (" + f.getAsJsonArray("units").size() + ")"), b -> {
                selectedForce = squad;
                send("select", forceArgs());
                rebuild();
            }).bounds(left(), y, 154, 16).build());
            y += 18;
            if (y > 120) break;
        }
        JsonObject f = force();
        if (f != null) {
            String doctrine = f.get("doctrine").getAsString();
            addRenderableWidget(Button.builder(Component.translatable("gui.rallyguard.style.doctrine",
                    Component.translatable("gui.rallyguard.doctrine." + doctrine.toLowerCase(Locale.ROOT))), b -> {
                JsonObject a = forceArgs();
                a.addProperty("doctrine", next(DOCTRINES, doctrine));
                send("doctrine", a);
            }).bounds(left(), y + 2, 154, 16).build());
            y += 20;
            for (int i = 0; i < OPS.length; i++) {
                String op = OPS[i];
                addRenderableWidget(Button.builder(Component.translatable("gui.rallyguard.op." + op.toLowerCase(Locale.ROOT)), b -> {
                    JsonObject a = forceArgs();
                    a.addProperty("op", op);
                    if (!Double.isNaN(ax)) {
                        a.addProperty("x", ax);
                        a.addProperty("z", az);
                        if (!Double.isNaN(bx)) a.addProperty("yaw", yaw(ax, az, bx, bz));
                    }
                    send("op", a);
                }).bounds(left() + (i % 2) * 78, y + (i / 2) * 18, 76, 16).build());
            }
            y += 56;
            for (JsonObject u : units()) {
                if (y > this.height - 30) break;
                String path = u.get("path").getAsString();
                String declared = u.get("declared").getAsString();
                String label = shortPath(path) + " [" + u.get("type").getAsString() + "] " + u.get("role").getAsString().toLowerCase(Locale.ROOT);
                addRenderableWidget(Button.builder(Component.literal(label), b -> {
                    JsonObject a = forceArgs();
                    a.addProperty("unit", path);
                    a.addProperty("role", next(ROLES, declared));
                    send("role", a);
                }).bounds(left(), y, 154, 14).build());
                y += 16;
            }
        }
        // Map zoom
        addRenderableWidget(Button.builder(Component.literal("+"), b -> zoom(-1)).bounds(mapX(), mapY() + mapSize() + 2, 16, 14).build());
        addRenderableWidget(Button.builder(Component.literal("-"), b -> zoom(1)).bounds(mapX() + 18, mapY() + mapSize() + 2, 16, 14).build());

        // Plan editor (right)
        int x = right();
        y = 22;
        nameBox = new EditBox(this.font, x, y, 156, 14, Component.literal("name"));
        nameBox.setMaxLength(40);
        nameBox.setValue(nameDraft);
        addRenderableWidget(nameBox);
        y += 18;
        addRenderableWidget(cycle(x, y, 156, "gui.rallyguard.planner.objective", OBJECTIVES[objective], b -> {
            objective = (objective + 1) % OBJECTIVES.length;
            rebuild();
        }));
        y += 18;
        addRenderableWidget(cycle(x, y, 100, "gui.rallyguard.planner.trigger", TRIGGERS[trigger], b -> {
            trigger = (trigger + 1) % TRIGGERS.length;
            rebuild();
        }));
        addRenderableWidget(Button.builder(Component.literal("-"), b -> {
            value = Math.max(0, value - step(TRIGGERS[trigger]));
            rebuild();
        }).bounds(x + 102, y, 16, 16).build());
        addRenderableWidget(Button.builder(Component.literal("+"), b -> {
            value += step(TRIGGERS[trigger]);
            rebuild();
        }).bounds(x + 140, y, 16, 16).build());
        y += 18;
        addRenderableWidget(Button.builder(Component.translatable("gui.rallyguard.planner.add_phase"), b -> {
            if (phases.size() >= 8) return;
            JsonObject p = new JsonObject();
            p.addProperty("objective", OBJECTIVES[objective]);
            p.addProperty("x", Double.isNaN(ax) ? centreX : ax);
            p.addProperty("z", Double.isNaN(ax) ? centreZ : az);
            p.addProperty("x2", Double.isNaN(bx) ? centreX : bx);
            p.addProperty("z2", Double.isNaN(bx) ? centreZ : bz);
            p.addProperty("radius", 16);
            p.addProperty("yaw", Double.isNaN(ax) || Double.isNaN(bx) ? 0 : yaw(ax, az, bx, bz));
            p.addProperty("trigger", TRIGGERS[trigger]);
            p.addProperty("value", TRIGGERS[trigger].equals("CASUALTIES") ? Math.min(1, value / 100.0) : value);
            phases.add(p);
            rebuild();
        }).bounds(x, y, 156, 16).build());
        y += 18;
        for (int i = 0; i < phases.size(); i++) {
            int idx = i;
            addRenderableWidget(Button.builder(Component.literal("x"), b -> {
                phases.remove(idx);
                rebuild();
            }).bounds(x + 144, y + i * 11, 12, 10).build());
        }
        y += Math.max(1, phases.size()) * 11 + 4;
        List<JsonObject> us = units();
        addRenderableWidget(cycle(x, y, 156, "gui.rallyguard.planner.link", LINKS[linkType], b -> {
            linkType = (linkType + 1) % LINKS.length;
            rebuild();
        }));
        y += 18;
        String from = us.isEmpty() ? "-" : shortPath(us.get(linkFrom % us.size()).get("path").getAsString());
        String to = us.isEmpty() ? "-" : shortPath(us.get(linkTo % us.size()).get("path").getAsString());
        addRenderableWidget(Button.builder(Component.literal(from + " →"), b -> {
            linkFrom++;
            rebuild();
        }).bounds(x, y, 77, 16).build());
        addRenderableWidget(Button.builder(Component.literal("→ " + to), b -> {
            linkTo++;
            rebuild();
        }).bounds(x + 79, y, 77, 16).build());
        y += 18;
        addRenderableWidget(cycle(x, y, 100, "gui.rallyguard.planner.trigger", TRIGGERS[linkTrigger], b -> {
            linkTrigger = (linkTrigger + 1) % TRIGGERS.length;
            rebuild();
        }));
        addRenderableWidget(Button.builder(Component.literal("-"), b -> {
            linkValue = Math.max(0, linkValue - (TRIGGERS[linkTrigger].equals("CASUALTIES") ? 0.05 : 5));
            rebuild();
        }).bounds(x + 102, y, 16, 16).build());
        addRenderableWidget(Button.builder(Component.literal("+"), b -> {
            linkValue += TRIGGERS[linkTrigger].equals("CASUALTIES") ? 0.05 : 5;
            rebuild();
        }).bounds(x + 140, y, 16, 16).build());
        y += 18;
        addRenderableWidget(Button.builder(Component.translatable("gui.rallyguard.planner.add_link"), b -> {
            if (us.isEmpty() || links.size() >= 8) return;
            JsonObject l = new JsonObject();
            l.addProperty("type", LINKS[linkType]);
            l.addProperty("from", us.get(linkFrom % us.size()).get("path").getAsString());
            l.addProperty("to", us.get(linkTo % us.size()).get("path").getAsString());
            l.addProperty("trigger", TRIGGERS[linkTrigger]);
            l.addProperty("value", linkValue);
            l.addProperty("x", Double.isNaN(ax) ? centreX : ax);
            l.addProperty("z", Double.isNaN(ax) ? centreZ : az);
            links.add(l);
            rebuild();
        }).bounds(x, y, 156, 16).build());
        y += 18;
        for (int i = 0; i < links.size(); i++) {
            int idx = i;
            addRenderableWidget(Button.builder(Component.literal("x"), b -> {
                links.remove(idx);
                rebuild();
            }).bounds(x + 144, y + i * 11, 12, 10).build());
        }
        y += Math.max(1, links.size()) * 11 + 4;
        addRenderableWidget(Button.builder(Component.translatable("gui.rallyguard.planner.retreat_at",
                (int) (RETREAT_AT[retreatAt] * 100)), b -> {
            retreatAt = (retreatAt + 1) % RETREAT_AT.length;
            rebuild();
        }).bounds(x, y, 156, 16).build());
        y += 20;
        addRenderableWidget(Button.builder(Component.translatable("gui.rallyguard.planner.execute"), b -> {
            JsonObject a = new JsonObject();
            a.add("plan", buildPlan());
            send("execute", a);
        }).bounds(x, y, 77, 16).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.rallyguard.planner.cancel"), b -> send("cancel", forceArgs()))
                .bounds(x + 79, y, 77, 16).build());
        y += 18;
        addRenderableWidget(Button.builder(Component.translatable("gui.rallyguard.planner.save"), b -> {
            JsonObject a = new JsonObject();
            a.add("plan", buildPlan());
            send("save", a);
        }).bounds(x, y, 50, 16).build());
        List<String> saved = new ArrayList<>();
        if (state.has("plans")) for (JsonElement e : state.getAsJsonArray("plans")) saved.add(e.getAsString());
        String shown = saved.isEmpty() ? "-" : saved.get(loadIndex % saved.size());
        addRenderableWidget(Button.builder(Component.literal("⟲ " + shown), b -> {
            if (saved.isEmpty()) return;
            JsonObject a = new JsonObject();
            a.addProperty("name", saved.get(loadIndex % saved.size()));
            send("load", a);
            loadIndex++;
        }).bounds(x + 52, y, 70, 16).build());
        addRenderableWidget(Button.builder(Component.literal("✕"), b -> {
            if (saved.isEmpty()) return;
            JsonObject a = new JsonObject();
            a.addProperty("name", saved.get(loadIndex % saved.size()));
            send("delete", a);
        }).bounds(x + 124, y, 32, 16).build());
    }

    private Button cycle(int x, int y, int w, String key, String value, Button.OnPress press) {
        return Button.builder(Component.translatable(key, Component.translatable("gui.rallyguard.planner.v." + value.toLowerCase(Locale.ROOT))),
                press).bounds(x, y, w, 16).build();
    }

    private static double step(String trigger) {
        return trigger.equals("CASUALTIES") ? 5 : 5;
    }

    private static String next(String[] values, String current) {
        for (int i = 0; i < values.length; i++) if (values[i].equalsIgnoreCase(current)) return values[(i + 1) % values.length];
        return values[0];
    }

    private static String shortPath(String path) {
        int slash = path.indexOf('/');
        return slash >= 0 ? path.substring(slash + 1) : path;
    }

    private static float yaw(double fromX, double fromZ, double toX, double toZ) {
        double dx = toX - fromX, dz = toZ - fromZ;
        return (float) Math.toDegrees(Math.atan2(-dx, dz));
    }

    private JsonObject buildPlan() {
        JsonObject p = new JsonObject();
        p.addProperty("name", nameBox == null || nameBox.getValue().isBlank() ? "plan" : nameBox.getValue().trim());
        p.addProperty("force", selectedForce);
        JsonObject f = force();
        p.addProperty("doctrine", f == null ? "STANDARD" : f.get("doctrine").getAsString());
        p.addProperty("retreatAt", RETREAT_AT[retreatAt]);
        JsonObject roles = new JsonObject();
        for (JsonObject u : units()) if (!u.get("declared").getAsString().isEmpty()) roles.addProperty(u.get("path").getAsString(), u.get("declared").getAsString());
        p.add("roles", roles);
        JsonArray ph = new JsonArray();
        phases.forEach(ph::add);
        p.add("phases", ph);
        JsonArray ln = new JsonArray();
        links.forEach(ln::add);
        p.add("links", ln);
        return p;
    }

    private void loadPlan(JsonObject p) {
        nameDraft = p.has("name") ? p.get("name").getAsString() : "plan";
        if (p.has("force")) selectedForce = p.get("force").getAsString();
        phases.clear();
        links.clear();
        if (p.has("phases")) for (JsonElement e : p.getAsJsonArray("phases")) phases.add(e.getAsJsonObject());
        if (p.has("links")) for (JsonElement e : p.getAsJsonArray("links")) links.add(e.getAsJsonObject());
        double r = p.has("retreatAt") ? p.get("retreatAt").getAsDouble() : 0;
        for (int i = 0; i < RETREAT_AT.length; i++) if (Math.abs(RETREAT_AT[i] - r) < 0.01) retreatAt = i;
    }

    private void zoom(int dir) {
        int[] scales = {1, 2, 4, 8};
        int i = 0;
        while (i < scales.length && scales[i] != scale) i++;
        scale = scales[Math.max(0, Math.min(scales.length - 1, i + dir))];
        cursor = 0;
        image.fillRect(0, 0, MAP_PX, MAP_PX, 0xFF202020);
        texture.upload();
    }

    // ==== Map ====

    /** Builds the next batch of map columns from the client's loaded chunks. */
    private void buildMap() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || cursor >= MAP_PX * MAP_PX) return;
        int end = Math.min(MAP_PX * MAP_PX, cursor + COLUMNS_PER_FRAME);
        double x0 = centreX - MAP_PX * scale / 2.0, z0 = centreZ - MAP_PX * scale / 2.0;
        for (; cursor < end; cursor++) {
            int px = cursor % MAP_PX, pz = cursor / MAP_PX;
            int wx = (int) Math.floor(x0 + px * scale), wz = (int) Math.floor(z0 + pz * scale);
            int abgr = 0xFF202020;
            if (level.hasChunk(wx >> 4, wz >> 4)) {
                int h = level.getHeight(Heightmap.Types.WORLD_SURFACE, wx, wz);
                BlockPos pos = new BlockPos(wx, h - 1, wz);
                BlockState bs = level.getBlockState(pos);
                MapColor mc = bs.getMapColor(level, pos);
                int rgb = mc == MapColor.NONE ? 0x707070 : mc.col;
                int north = pz == 0 ? h : lastHeight[px];
                double shade = h > north ? 1.1 : h < north ? 0.82 : 1.0;
                int r = Math.min(255, (int) (((rgb >> 16) & 0xFF) * shade));
                int g = Math.min(255, (int) (((rgb >> 8) & 0xFF) * shade));
                int b = Math.min(255, (int) ((rgb & 0xFF) * shade));
                abgr = 0xFF000000 | (b << 16) | (g << 8) | r;
                lastHeight[px] = h;
            }
            image.setPixelRGBA(px, pz, abgr);
        }
        texture.upload();
    }

    private int toScreenX(double wx) {
        return mapX() + (int) ((wx - (centreX - MAP_PX * scale / 2.0)) / scale * mapSize() / MAP_PX);
    }

    private int toScreenZ(double wz) {
        return mapY() + (int) ((wz - (centreZ - MAP_PX * scale / 2.0)) / scale * mapSize() / MAP_PX);
    }

    private double toWorldX(double sx) {
        return centreX - MAP_PX * scale / 2.0 + (sx - mapX()) * MAP_PX / (double) mapSize() * scale;
    }

    private double toWorldZ(double sy) {
        return centreZ - MAP_PX * scale / 2.0 + (sy - mapY()) * MAP_PX / (double) mapSize() * scale;
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (super.mouseClicked(mx, my, button)) return true;
        if (mx >= mapX() && mx < mapX() + mapSize() && my >= mapY() && my < mapY() + mapSize()) {
            if (button == 0) {
                ax = toWorldX(mx);
                az = toWorldZ(my);
            } else if (button == 1) {
                bx = toWorldX(mx);
                bz = toWorldZ(my);
            }
            return true;
        }
        return false;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        buildMap();
        super.render(g, mouseX, mouseY, delta);
        int ms = mapSize();
        g.blit(MAP_ID, mapX(), mapY(), 0, 0, ms, ms, ms, ms);
        g.drawString(this.font, this.title, mapX(), 8, 0xFFFFFF, true);
        JsonObject f = force();
        if (f != null) {
            for (JsonElement e : f.getAsJsonArray("units")) {
                JsonObject u = e.getAsJsonObject();
                int sx = toScreenX(u.get("x").getAsDouble()), sz = toScreenZ(u.get("z").getAsDouble());
                if (!inMap(sx, sz)) continue;
                int color = switch (u.get("type").getAsString()) {
                    case "archers" -> 0xFF55FF55;
                    case "cavalry" -> 0xFFFFAA00;
                    case "reserve" -> 0xFFAAAAAA;
                    default -> 0xFF5599FF;
                };
                g.fill(sx - 2, sz - 2, sx + 3, sz + 3, color);
                g.drawString(this.font, shortPath(u.get("path").getAsString()), sx + 4, sz - 4, 0xFFFFFF, true);
            }
            for (JsonElement e : f.getAsJsonArray("enemies")) {
                JsonObject en = e.getAsJsonObject();
                int sx = toScreenX(en.get("x").getAsDouble()), sz = toScreenZ(en.get("z").getAsDouble());
                if (!inMap(sx, sz)) continue;
                g.fill(sx - 2, sz - 2, sx + 3, sz + 3, 0xFFFF3333);
                g.drawString(this.font, String.valueOf(en.get("count").getAsInt()), sx + 4, sz - 4, 0xFF5555, true);
            }
            g.drawString(this.font, Component.translatable("gui.rallyguard.planner.status", f.get("general").getAsString(),
                    f.get("operation").getAsString(), f.get("plan").getAsString(), f.get("phase").getAsInt() + 1),
                    mapX(), mapY() + ms + 20, 0xDDDDDD, true);
        }
        if (state.has("hqs")) {
            for (JsonElement e : state.getAsJsonArray("hqs")) {
                JsonObject h = e.getAsJsonObject();
                int sx = toScreenX(h.get("x").getAsDouble()), sz = toScreenZ(h.get("z").getAsDouble());
                if (!inMap(sx, sz)) continue;
                g.fill(sx - 3, sz - 3, sx + 4, sz + 4, 0xFFFFD700);
                g.drawString(this.font, h.get("name").getAsString(), sx + 5, sz - 4, 0xFFD700, true);
            }
        }
        if (!Double.isNaN(ax)) drawPoint(g, ax, az, "A", 0xFFFFFFFF);
        if (!Double.isNaN(bx)) drawPoint(g, bx, bz, "B", 0xFFFFFF55);
        // Phases and links of the plan being edited
        int y = phaseListY();
        for (int i = 0; i < phases.size(); i++) {
            JsonObject p = phases.get(i);
            g.drawString(this.font, (i + 1) + ". " + p.get("objective").getAsString().toLowerCase(Locale.ROOT) + " ⟶ "
                    + p.get("trigger").getAsString().toLowerCase(Locale.ROOT), right(), y + i * 11, 0xCCCCCC, false);
        }
        int ly = linkListY();
        for (int i = 0; i < links.size(); i++) {
            JsonObject l = links.get(i);
            g.drawString(this.font, l.get("type").getAsString().toLowerCase(Locale.ROOT) + " " + shortPath(l.get("from").getAsString())
                    + "→" + shortPath(l.get("to").getAsString()), right(), ly + i * 11, 0xCCCCCC, false);
        }
        g.drawString(this.font, Component.translatable("gui.rallyguard.planner.value", fmt(TRIGGERS[trigger], value)),
                right() + 104, 22 + 36 + 4, 0xFFFFFF, false);
        // Warnings and messages
        int wy = this.height - 12;
        if (!message.isEmpty()) {
            g.drawString(this.font, message, mapX(), wy, 0xFFFF55, true);
            wy -= 10;
        }
        if (state.has("warnings")) {
            for (JsonElement e : state.getAsJsonArray("warnings")) {
                if (wy < mapY() + ms + 32) break;
                g.drawString(this.font, e.getAsString(), mapX(), wy, 0xFF8855, true);
                wy -= 10;
            }
        }
    }

    private int phaseListY() {
        return 22 + 18 * 3 + 18;
    }

    private int linkListY() {
        return phaseListY() + Math.max(1, phases.size()) * 11 + 4 + 18 * 4;
    }

    private static String fmt(String trigger, double v) {
        return trigger.equals("CASUALTIES") ? (int) v + " %" : trigger.equals("TIME") || trigger.equals("CONTACT") ? (int) v + " s" : "-";
    }

    private boolean inMap(int sx, int sz) {
        return sx >= mapX() && sx < mapX() + mapSize() && sz >= mapY() && sz < mapY() + mapSize();
    }

    private void drawPoint(GuiGraphics g, double wx, double wz, String label, int color) {
        int sx = toScreenX(wx), sz = toScreenZ(wz);
        if (!inMap(sx, sz)) return;
        g.fill(sx - 3, sz, sx + 4, sz + 1, color);
        g.fill(sx, sz - 3, sx + 1, sz + 4, color);
        g.drawString(this.font, label, sx + 4, sz + 2, color, true);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void removed() {
        super.removed();
        if (texture != null) {
            Minecraft.getInstance().getTextureManager().release(MAP_ID);
            texture = null;
            image = null;
        }
    }
}
