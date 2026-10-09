package net.hfstack.rallyguard.screen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;
import net.hfstack.rallyguard.formation.Formation;
import net.hfstack.rallyguard.formation.FormationLayout;
import net.hfstack.rallyguard.formation.FormationShape;
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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * E3A A4 / E3B P3 - the Battle Planner (client), drawn as a battle TABLE: dark wood frame,
 * parchment panels, the map in the middle. It only DRAWS the state the server sends
 * ({@link PlannerStateS2CPayload}) and SENDS actions ({@link PlannerActionC2SPayload});
 * authority, validation and execution are server side ({@code force.PlannerService}).
 * <ul>
 *     <li><b>Map</b>: top-down, drawn by code from the CLIENT's loaded chunks only, built
 *     {@link #COLUMNS_PER_FRAME} columns per frame into a 128×128 texture (cached; rebuilt only
 *     on zoom). Each unit is a TOKEN: its members drawn as dots in the rows of its real
 *     formation and density ({@link Formation#localOffsets}, cached per state), an identity
 *     badge (archetype colour + initial), count, health and morale bars, and the captain's
 *     command MEDALLION (click = the unit's card). Known enemies with their composition, HQs,
 *     plan points A (left click) / B (right click), and the plan's links as coloured arrows.</li>
 *     <li><b>Drag and drop</b>: drag a token onto another = a link of the selected type (the
 *     dropped-on unit is "from", the dragged one acts); drag a token onto empty ground = point A
 *     there, and the dragged unit's AMBUSH wait point / RENDEZVOUS point moves there.</li>
 *     <li><b>Left panel</b> (tabs): Units (role per unit, the selected unit's group card),
 *     Scenarios (on/off and priority of each response), Importance / Fill (importance and
 *     first substitute per unit).</li>
 *     <li><b>Right panel</b>: the plan - phases (objective, trigger, tempo, instruction) as a
 *     timeline with the running phase marked, links (all v2 types), signals, retreat line,
 *     Execute / Cancel / Save / Load / Delete.</li>
 * </ul>
 * Widgets are rebuilt only when the server state or the plan changes; token geometry is cached
 * per state, so a frame only blits the map and fills small rectangles.
 */
public class BattlePlannerScreen extends Screen {

    private static final int MAP_PX = 128;
    private static final int COLUMNS_PER_FRAME = 1024;
    private static final ResourceLocation MAP_ID = ResourceLocation.fromNamespaceAndPath("rallyguard", "planner_map");
    private static final String[] OBJECTIVES = {"HOLD", "ATTACK_ZONE", "ATTACK_GROUP", "ESCORT", "PATROL", "RETREAT", "RALLY",
            "INSTRUCTION"};
    private static final String[] TRIGGERS = {"NONE", "TIME", "CASUALTIES", "ENEMY_SEEN", "CONTACT", "SIGNAL"};
    private static final String[] LINKS = {"RELIEF", "COVER", "REINFORCE", "RENDEZVOUS", "SCREEN", "ESCORT",
            "HOLD_UNTIL_RELIEVED", "STAGGERED_RETREAT", "AMBUSH", "OPEN_LANE", "COUNTERATTACK", "DELAY", "PINCER",
            "MOVING_SCREEN", "SIGNAL_RESERVE"};
    private static final String[] DOCTRINES = {"STANDARD", "HARASS", "AVOID_CASUALTIES", "DISTRACT", "ALL_OUT",
            "HOLD_POSITION", "AMBUSH"};
    private static final String[] ROLES = {"", "ANVIL", "HAMMER", "SCREEN", "SUPPORT", "FLANK", "RESERVE"};
    private static final String[] OPS = {"HOLD_LINE", "FALLBACK_FIGHTING", "ADVANCE_BY_TURNS", "FLANK", "ORDERLY_RETREAT", "STOP"};
    private static final String[] TEMPOS = {"NORMAL", "LENTO", "RAPIDO", "ASALTO"};
    private static final String[] INSTRUCTIONS = {"HOSTIGAR", "MANTENER_LINEA", "AVANZAR", "RETIRARSE", "FLANQUEAR"};
    private static final int[] IMPORTANCE = {-1, 0, 25, 50, 75, 100};
    private static final double[] RETREAT_AT = {0, 0.25, 0.4, 0.6};

    // Table colours (wood frame, parchment panels, ink).
    private static final int WOOD_DARK = 0xFF2E1E10, WOOD = 0xFF5A3A1E, WOOD_LIGHT = 0xFF7A5230;
    private static final int PARCHMENT = 0xFFE6D3A3, PARCHMENT_DARK = 0xFFC9B27E, INK = 0x3A2A18, INK_SOFT = 0x6A5236;

    private enum Tab { UNITS, SCENARIOS, FILL }

    private JsonObject state = new JsonObject();
    private String selectedForce = "";
    private String selectedUnit = "";
    private String message = "";
    private Tab tab = Tab.UNITS;

    // Plan being edited
    private final List<JsonObject> phases = new ArrayList<>();
    private final List<JsonObject> links = new ArrayList<>();
    private final Map<String, Integer> importance = new LinkedHashMap<>();
    private final Map<String, String> fillSub = new LinkedHashMap<>();
    private final Map<String, JsonObject> scenarioEdits = new LinkedHashMap<>();
    private int objective, trigger, linkType, linkTrigger, linkFrom, linkTo, retreatAt, loadIndex, tempo, instruction;
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

    // Tokens: cached local offsets (blocks, already rotated) per unit path; rebuilt on each state.
    private final Map<String, List<double[]>> tokenDots = new HashMap<>();
    // Drag and drop
    @Nullable
    private String dragFrom;
    private double dragStartX, dragStartY;
    private int mouseNowX, mouseNowY;

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
        cacheTokens();
        if (this.minecraft != null) rebuild();
    }

    /** Each unit's dots: the rows of its formation and density, rotated by its facing (blocks). */
    private void cacheTokens() {
        tokenDots.clear();
        for (JsonObject u : units()) {
            int count = Math.min(64, intOf(u, "count", 0));
            if (count <= 0) continue;
            FormationShape shape;
            try {
                shape = FormationShape.valueOf(strOf(u, "shape", "LINE"));
            } catch (IllegalArgumentException e) {
                shape = FormationShape.LINE;
            }
            FormationLayout layout = FormationLayout.parse(strOf(u, "layout", ""));
            List<double[]> local;
            try {
                local = new Formation(shape, Formation.DEFAULT_SPACING, layout).localOffsets(count);
            } catch (RuntimeException e) {
                local = List.of(new double[]{0, 0});
            }
            double yaw = Math.toRadians(u.has("yaw") ? u.get("yaw").getAsDouble() : 0);
            double fx = -Math.sin(yaw), fz = Math.cos(yaw), rx = -fz, rz = fx;
            List<double[]> world = new ArrayList<>(local.size());
            for (double[] o : local) world.add(new double[]{rx * o[0] + fx * o[1], rz * o[0] + fz * o[1]});
            tokenDots.put(strOf(u, "path", ""), world);
        }
    }

    private List<JsonObject> forces() {
        List<JsonObject> out = new ArrayList<>();
        if (state.has("forces")) for (JsonElement e : state.getAsJsonArray("forces")) out.add(e.getAsJsonObject());
        return out;
    }

    @Nullable
    private JsonObject force() {
        for (JsonObject f : forces()) if (f.get("squad").getAsString().equalsIgnoreCase(selectedForce)) return f;
        return null;
    }

    private List<JsonObject> units() {
        List<JsonObject> out = new ArrayList<>();
        JsonObject f = force();
        if (f != null && f.has("units")) for (JsonElement e : f.getAsJsonArray("units")) out.add(e.getAsJsonObject());
        return out;
    }

    @Nullable
    private JsonObject unit(String path) {
        for (JsonObject u : units()) if (strOf(u, "path", "").equalsIgnoreCase(path)) return u;
        return null;
    }

    private List<JsonObject> scenarioLib() {
        List<JsonObject> out = new ArrayList<>();
        if (state.has("scenarioLib")) for (JsonElement e : state.getAsJsonArray("scenarioLib")) out.add(e.getAsJsonObject());
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

    private static String strOf(JsonObject o, String key, String fallback) {
        return o.has(key) && !o.get(key).isJsonNull() ? o.get(key).getAsString() : fallback;
    }

    private static int intOf(JsonObject o, String key, int fallback) {
        try {
            return o.has(key) ? o.get(key).getAsInt() : fallback;
        } catch (RuntimeException e) {
            return fallback;
        }
    }

    // ==== Layout ====

    private int left() {
        return 8;
    }

    private int mapSize() {
        return Math.max(96, Math.min(this.width - 2 * 166 - 16, this.height - 64));
    }

    private int mapX() {
        return 170 + (this.width - 2 * 170 - mapSize()) / 2;
    }

    private int mapY() {
        return 24;
    }

    private int right() {
        return this.width - 164;
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
        int y = 24;
        // Forces
        for (JsonObject f : forces()) {
            String squad = f.get("squad").getAsString();
            boolean sel = squad.equalsIgnoreCase(selectedForce);
            addRenderableWidget(Button.builder(Component.literal((sel ? "▶ " : "") + squad + " (" + f.getAsJsonArray("units").size() + ")"), b -> {
                selectedForce = squad;
                selectedUnit = "";
                send("select", forceArgs());
                cacheTokens();
                rebuild();
            }).bounds(left(), y, 154, 14).build());
            y += 16;
            if (y > 80) break;
        }
        JsonObject f = force();
        if (f != null) {
            String doctrine = f.get("doctrine").getAsString();
            addRenderableWidget(Button.builder(Component.translatable("gui.rallyguard.style.doctrine",
                    Component.translatable("gui.rallyguard.doctrine." + doctrine.toLowerCase(Locale.ROOT))), b -> {
                JsonObject a = forceArgs();
                a.addProperty("doctrine", next(DOCTRINES, doctrine));
                send("doctrine", a);
            }).bounds(left(), y + 2, 154, 14).build());
            y += 18;
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
                }).bounds(left() + (i % 2) * 78, y + (i / 2) * 16, 76, 14).build());
            }
            y += 50;
            // Tabs
            Tab[] tabs = Tab.values();
            for (int i = 0; i < tabs.length; i++) {
                Tab t = tabs[i];
                addRenderableWidget(Button.builder(Component.translatable("gui.rallyguard.planner.tab." + t.name().toLowerCase(Locale.ROOT))
                        .withStyle(t == tab ? net.minecraft.ChatFormatting.UNDERLINE : net.minecraft.ChatFormatting.RESET), b -> {
                    tab = t;
                    rebuild();
                }).bounds(left() + i * 52, y, 50, 14).build());
            }
            y += 18;
            switch (tab) {
                case UNITS -> buildUnitsTab(y);
                case SCENARIOS -> buildScenariosTab(y);
                case FILL -> buildFillTab(y);
            }
        }
        // Map zoom
        addRenderableWidget(Button.builder(Component.literal("+"), b -> zoom(-1)).bounds(mapX(), mapY() + mapSize() + 4, 16, 14).build());
        addRenderableWidget(Button.builder(Component.literal("-"), b -> zoom(1)).bounds(mapX() + 18, mapY() + mapSize() + 4, 16, 14).build());
        buildPlanPanel();
    }

    /** Units tab: one row per unit (click cycles its declared role); the selected unit's card is drawn below. */
    private void buildUnitsTab(int y0) {
        int y = y0;
        for (JsonObject u : units()) {
            if (y > this.height - 96) break;
            String path = strOf(u, "path", "");
            String declared = strOf(u, "declared", "");
            String label = shortPath(path) + " · " + strOf(u, "role", "").toLowerCase(Locale.ROOT);
            addRenderableWidget(Button.builder(Component.literal((path.equalsIgnoreCase(selectedUnit) ? "● " : "") + label), b -> {
                if (!path.equalsIgnoreCase(selectedUnit)) {
                    selectedUnit = path; // first click selects (card), the next cycles the role
                    rebuild();
                    return;
                }
                JsonObject a = forceArgs();
                a.addProperty("unit", path);
                a.addProperty("role", next(ROLES, declared));
                send("role", a);
            }).bounds(left() + 18, y, 136, 13).build());
            y += 14;
        }
    }

    /** Scenarios tab: each response on/off and its priority (the edit is saved with the plan). */
    private void buildScenariosTab(int y0) {
        int y = y0;
        for (JsonObject def : scenarioLib()) {
            if (y > this.height - 30) break;
            String kind = strOf(def, "kind", "");
            JsonObject edit = scenarioEdits.get(kind);
            boolean active = edit != null && edit.has("active") ? edit.get("active").getAsBoolean() : def.get("active").getAsBoolean();
            int priority = edit != null && edit.has("priority") ? edit.get("priority").getAsInt() : def.get("priority").getAsInt();
            Component name = Component.translatable("gui.rallyguard.scenario." + kind.toLowerCase(Locale.ROOT));
            addRenderableWidget(Button.builder(Component.literal((active ? "✓ " : "✗ ") + fit(name.getString(), 92) + " " + priority), b -> {
                scenarioEdit(kind).addProperty("active", !active);
                rebuild();
            }).bounds(left(), y, 122, 13).build());
            addRenderableWidget(Button.builder(Component.literal("-"), b -> {
                scenarioEdit(kind).addProperty("priority", Math.max(0, priority - 5));
                rebuild();
            }).bounds(left() + 124, y, 14, 13).build());
            addRenderableWidget(Button.builder(Component.literal("+"), b -> {
                scenarioEdit(kind).addProperty("priority", Math.min(100, priority + 5));
                rebuild();
            }).bounds(left() + 140, y, 14, 13).build());
            y += 14;
        }
    }

    private JsonObject scenarioEdit(String kind) {
        return scenarioEdits.computeIfAbsent(kind, k -> {
            JsonObject o = new JsonObject();
            o.addProperty("kind", k);
            return o;
        });
    }

    /** Importance / Fill tab: per unit, its importance (auto = by role) and its first substitute. */
    private void buildFillTab(int y0) {
        int y = y0;
        List<JsonObject> us = units();
        for (JsonObject u : us) {
            if (y > this.height - 30) break;
            String path = strOf(u, "path", "");
            int imp = importance.getOrDefault(path, -1);
            String sub = fillSub.getOrDefault(path, "");
            addRenderableWidget(Button.builder(Component.literal(fit(shortPath(path), 34) + " " + (imp < 0
                    ? Component.translatable("gui.rallyguard.planner.auto").getString() : String.valueOf(imp))), b -> {
                int i = 0;
                while (i < IMPORTANCE.length && IMPORTANCE[i] != imp) i++;
                int nextImp = IMPORTANCE[(i + 1) % IMPORTANCE.length];
                if (nextImp < 0) importance.remove(path);
                else importance.put(path, nextImp);
                rebuild();
            }).bounds(left(), y, 70, 13).build());
            Component subLabel = sub.isEmpty() ? Component.translatable("gui.rallyguard.planner.fill_none")
                    : Component.literal("→ " + shortPath(sub));
            addRenderableWidget(Button.builder(subLabel, b -> {
                List<String> options = new ArrayList<>();
                options.add("");
                for (JsonObject o : us) if (!strOf(o, "path", "").equalsIgnoreCase(path)) options.add(strOf(o, "path", ""));
                int i = options.indexOf(sub);
                String nextSub = options.get((i + 1) % options.size());
                if (nextSub.isEmpty()) fillSub.remove(path);
                else fillSub.put(path, nextSub);
                rebuild();
            }).bounds(left() + 72, y, 82, 13).build());
            y += 14;
        }
    }

    /** Right panel: phases (timeline), links, signals, retreat, execute / save / load. */
    private void buildPlanPanel() {
        int x = right();
        int y = 24;
        nameBox = new EditBox(this.font, x, y, 156, 14, Component.literal("name"));
        nameBox.setMaxLength(40);
        nameBox.setValue(nameDraft);
        addRenderableWidget(nameBox);
        y += 17;
        addRenderableWidget(cycle(x, y, 156, "gui.rallyguard.planner.objective", OBJECTIVES[objective], b -> {
            objective = (objective + 1) % OBJECTIVES.length;
            rebuild();
        }));
        y += 16;
        if (OBJECTIVES[objective].equals("INSTRUCTION")) {
            addRenderableWidget(cycle(x, y, 156, "gui.rallyguard.planner.instruction", INSTRUCTIONS[instruction], b -> {
                instruction = (instruction + 1) % INSTRUCTIONS.length;
                rebuild();
            }));
            y += 16;
        }
        addRenderableWidget(cycle(x, y, 100, "gui.rallyguard.planner.trigger", TRIGGERS[trigger], b -> {
            trigger = (trigger + 1) % TRIGGERS.length;
            rebuild();
        }));
        addRenderableWidget(Button.builder(Component.literal("-"), b -> {
            value = Math.max(0, value - step(TRIGGERS[trigger]));
            rebuild();
        }).bounds(x + 102, y, 14, 14).build());
        addRenderableWidget(Button.builder(Component.literal("+"), b -> {
            value += step(TRIGGERS[trigger]);
            rebuild();
        }).bounds(x + 142, y, 14, 14).build());
        y += 16;
        addRenderableWidget(cycle(x, y, 156, "gui.rallyguard.planner.tempo", TEMPOS[tempo], b -> {
            tempo = (tempo + 1) % TEMPOS.length;
            rebuild();
        }));
        y += 16;
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
            if (!TEMPOS[tempo].equals("NORMAL")) p.addProperty("tempo", TEMPOS[tempo]);
            if (OBJECTIVES[objective].equals("INSTRUCTION")) p.addProperty("instruction", INSTRUCTIONS[instruction]);
            phases.add(p);
            rebuild();
        }).bounds(x, y, 156, 14).build());
        y += 16;
        for (int i = 0; i < phases.size(); i++) {
            int idx = i;
            addRenderableWidget(Button.builder(Component.literal("x"), b -> {
                phases.remove(idx);
                rebuild();
            }).bounds(x + 146, y + i * 11, 10, 10).build());
        }
        y += Math.max(1, phases.size()) * 11 + 4;
        List<JsonObject> us = units();
        addRenderableWidget(cycle(x, y, 156, "gui.rallyguard.planner.link", LINKS[linkType], b -> {
            linkType = (linkType + 1) % LINKS.length;
            rebuild();
        }));
        y += 16;
        String from = us.isEmpty() ? "-" : shortPath(strOf(us.get(linkFrom % us.size()), "path", "-"));
        String to = us.isEmpty() ? "-" : shortPath(strOf(us.get(linkTo % us.size()), "path", "-"));
        addRenderableWidget(Button.builder(Component.literal(fit(from, 60) + " →"), b -> {
            linkFrom++;
            rebuild();
        }).bounds(x, y, 77, 14).build());
        addRenderableWidget(Button.builder(Component.literal("→ " + fit(to, 60)), b -> {
            linkTo++;
            rebuild();
        }).bounds(x + 79, y, 77, 14).build());
        y += 16;
        addRenderableWidget(cycle(x, y, 100, "gui.rallyguard.planner.trigger", TRIGGERS[linkTrigger], b -> {
            linkTrigger = (linkTrigger + 1) % TRIGGERS.length;
            rebuild();
        }));
        addRenderableWidget(Button.builder(Component.literal("-"), b -> {
            linkValue = Math.max(0, linkValue - linkStep());
            rebuild();
        }).bounds(x + 102, y, 14, 14).build());
        addRenderableWidget(Button.builder(Component.literal("+"), b -> {
            linkValue += linkStep();
            rebuild();
        }).bounds(x + 142, y, 14, 14).build());
        y += 16;
        addRenderableWidget(Button.builder(Component.translatable("gui.rallyguard.planner.add_link"), b -> {
            if (us.isEmpty()) return;
            addLink(strOf(us.get(linkFrom % us.size()), "path", ""), strOf(us.get(linkTo % us.size()), "path", ""));
            rebuild();
        }).bounds(x, y, 156, 14).build());
        y += 16;
        for (int i = 0; i < links.size(); i++) {
            int idx = i;
            addRenderableWidget(Button.builder(Component.literal("x"), b -> {
                links.remove(idx);
                rebuild();
            }).bounds(x + 146, y + i * 11, 10, 10).build());
        }
        y += Math.max(1, links.size()) * 11 + 4;
        // Signals (only meaningful with a running plan; the server answers if none)
        for (int i = 1; i <= 3; i++) {
            int id = i;
            addRenderableWidget(Button.builder(Component.translatable("gui.rallyguard.planner.signal", id), b -> {
                JsonObject a = forceArgs();
                a.addProperty("id", id);
                send("signal", a);
            }).bounds(x + (i - 1) * 53, y, 50, 14).build());
        }
        y += 16;
        addRenderableWidget(Button.builder(Component.translatable("gui.rallyguard.planner.retreat_at",
                (int) (RETREAT_AT[retreatAt] * 100)), b -> {
            retreatAt = (retreatAt + 1) % RETREAT_AT.length;
            rebuild();
        }).bounds(x, y, 156, 14).build());
        y += 18;
        addRenderableWidget(Button.builder(Component.translatable("gui.rallyguard.planner.execute"), b -> {
            JsonObject a = new JsonObject();
            a.add("plan", buildPlan());
            send("execute", a);
        }).bounds(x, y, 77, 14).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.rallyguard.planner.cancel"), b -> send("cancel", forceArgs()))
                .bounds(x + 79, y, 77, 14).build());
        y += 16;
        addRenderableWidget(Button.builder(Component.translatable("gui.rallyguard.planner.save"), b -> {
            JsonObject a = new JsonObject();
            a.add("plan", buildPlan());
            send("save", a);
        }).bounds(x, y, 50, 14).build());
        List<String> saved = new ArrayList<>();
        if (state.has("plans")) for (JsonElement e : state.getAsJsonArray("plans")) saved.add(e.getAsString());
        String shown = saved.isEmpty() ? "-" : saved.get(loadIndex % saved.size());
        addRenderableWidget(Button.builder(Component.literal("⟲ " + fit(shown, 52)), b -> {
            if (saved.isEmpty()) return;
            JsonObject a = new JsonObject();
            a.addProperty("name", saved.get(loadIndex % saved.size()));
            send("load", a);
            loadIndex++;
        }).bounds(x + 52, y, 70, 14).build());
        addRenderableWidget(Button.builder(Component.literal("✕"), b -> {
            if (saved.isEmpty()) return;
            JsonObject a = new JsonObject();
            a.addProperty("name", saved.get(loadIndex % saved.size()));
            send("delete", a);
        }).bounds(x + 124, y, 32, 14).build());
    }

    private double linkStep() {
        return switch (TRIGGERS[linkTrigger]) {
            case "CASUALTIES" -> 0.05;
            case "SIGNAL" -> 1;
            default -> 5;
        };
    }

    /** A link of the selected type: {@code from} is the reference unit, {@code to} the one that acts. */
    private void addLink(String from, String to) {
        if (links.size() >= 16 || from.isEmpty() || to.isEmpty()) return;
        JsonObject l = new JsonObject();
        l.addProperty("type", LINKS[linkType]);
        l.addProperty("from", from);
        l.addProperty("to", to);
        l.addProperty("trigger", TRIGGERS[linkTrigger]);
        l.addProperty("value", linkValue);
        l.addProperty("x", Double.isNaN(ax) ? centreX : ax);
        l.addProperty("z", Double.isNaN(ax) ? centreZ : az);
        links.add(l);
    }

    private Button cycle(int x, int y, int w, String key, String value, Button.OnPress press) {
        return Button.builder(Component.translatable(key, Component.translatable("gui.rallyguard.planner.v." + value.toLowerCase(Locale.ROOT))),
                press).bounds(x, y, w, 14).build();
    }

    private static double step(String trigger) {
        return trigger.equals("SIGNAL") ? 1 : 5;
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

    /** Cuts {@code text} to {@code maxWidth} pixels with an ellipsis (no font helper assumed). */
    private String fit(String text, int maxWidth) {
        if (this.font == null || this.font.width(text) <= maxWidth) return text;
        String t = text;
        while (!t.isEmpty() && this.font.width(t + "…") > maxWidth) t = t.substring(0, t.length() - 1);
        return t + "…";
    }

    private JsonObject buildPlan() {
        JsonObject p = new JsonObject();
        p.addProperty("name", nameBox == null || nameBox.getValue().isBlank() ? "plan" : nameBox.getValue().trim());
        p.addProperty("force", selectedForce);
        JsonObject f = force();
        p.addProperty("doctrine", f == null ? "STANDARD" : f.get("doctrine").getAsString());
        p.addProperty("retreatAt", RETREAT_AT[retreatAt]);
        JsonObject roles = new JsonObject();
        for (JsonObject u : units()) {
            if (!strOf(u, "declared", "").isEmpty()) roles.addProperty(strOf(u, "path", ""), strOf(u, "declared", ""));
        }
        p.add("roles", roles);
        JsonArray ph = new JsonArray();
        phases.forEach(ph::add);
        p.add("phases", ph);
        JsonArray ln = new JsonArray();
        links.forEach(ln::add);
        p.add("links", ln);
        JsonObject imp = new JsonObject();
        importance.forEach(imp::addProperty);
        p.add("importance", imp);
        JsonObject fill = new JsonObject();
        for (Map.Entry<String, String> e : fillSub.entrySet()) {
            JsonArray chain = new JsonArray();
            chain.add(e.getValue());
            fill.add(e.getKey(), chain);
        }
        p.add("fill", fill);
        JsonArray scn = new JsonArray();
        scenarioEdits.values().forEach(scn::add);
        p.add("scenarios", scn);
        return p;
    }

    private void loadPlan(JsonObject p) {
        nameDraft = p.has("name") ? p.get("name").getAsString() : "plan";
        if (p.has("force")) selectedForce = p.get("force").getAsString();
        phases.clear();
        links.clear();
        importance.clear();
        fillSub.clear();
        scenarioEdits.clear();
        if (p.has("phases")) for (JsonElement e : p.getAsJsonArray("phases")) phases.add(e.getAsJsonObject());
        if (p.has("links")) for (JsonElement e : p.getAsJsonArray("links")) links.add(e.getAsJsonObject());
        if (p.has("importance")) {
            for (Map.Entry<String, JsonElement> e : p.getAsJsonObject("importance").entrySet()) importance.put(e.getKey(), e.getValue().getAsInt());
        }
        if (p.has("fill")) {
            for (Map.Entry<String, JsonElement> e : p.getAsJsonObject("fill").entrySet()) {
                JsonArray chain = e.getValue().getAsJsonArray();
                if (!chain.isEmpty()) fillSub.put(e.getKey(), chain.get(0).getAsString());
            }
        }
        if (p.has("scenarios")) {
            for (JsonElement e : p.getAsJsonArray("scenarios")) {
                JsonObject o = e.getAsJsonObject();
                JsonObject edit = new JsonObject();
                edit.addProperty("kind", strOf(o, "kind", ""));
                if (o.has("active")) edit.add("active", o.get("active"));
                if (o.has("priority")) edit.add("priority", o.get("priority"));
                scenarioEdits.put(strOf(o, "kind", ""), edit);
            }
        }
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

    /** Builds the next batch of map columns from the client's loaded chunks (nothing once complete). */
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
                // Parchment wash: a little of the table's colour over the terrain.
                int r = Math.min(255, (int) ((((rgb >> 16) & 0xFF) * shade) * 0.8 + 0xE6 * 0.2));
                int g = Math.min(255, (int) ((((rgb >> 8) & 0xFF) * shade) * 0.8 + 0xD3 * 0.2));
                int b = Math.min(255, (int) (((rgb & 0xFF) * shade) * 0.8 + 0xA3 * 0.2));
                abgr = 0xFF000000 | (b << 16) | (g << 8) | r;
                lastHeight[px] = h;
            }
            image.setPixelRGBA(px, pz, abgr);
        }
        texture.upload();
    }

    private double pxPerBlock() {
        return mapSize() / (double) (MAP_PX * scale);
    }

    private int toScreenX(double wx) {
        return mapX() + (int) ((wx - (centreX - MAP_PX * scale / 2.0)) * pxPerBlock());
    }

    private int toScreenZ(double wz) {
        return mapY() + (int) ((wz - (centreZ - MAP_PX * scale / 2.0)) * pxPerBlock());
    }

    private double toWorldX(double sx) {
        return centreX - MAP_PX * scale / 2.0 + (sx - mapX()) / pxPerBlock();
    }

    private double toWorldZ(double sy) {
        return centreZ - MAP_PX * scale / 2.0 + (sy - mapY()) / pxPerBlock();
    }

    /** The unit whose token is under the mouse (within 7 px of its centre), or null. */
    @Nullable
    private String tokenAt(double mx, double my) {
        String best = null;
        double bestD = 49;
        for (JsonObject u : units()) {
            if (!u.has("x")) continue;
            double dx = toScreenX(u.get("x").getAsDouble()) - mx, dz = toScreenZ(u.get("z").getAsDouble()) - my;
            double d = dx * dx + dz * dz;
            if (d < bestD) {
                bestD = d;
                best = strOf(u, "path", "");
            }
        }
        return best;
    }

    private boolean overMap(double mx, double my) {
        return mx >= mapX() && mx < mapX() + mapSize() && my >= mapY() && my < mapY() + mapSize();
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (super.mouseClicked(mx, my, button)) return true;
        if (overMap(mx, my)) {
            String token = button == 0 ? tokenAt(mx, my) : null;
            if (token != null) {
                dragFrom = token;
                dragStartX = mx;
                dragStartY = my;
                return true;
            }
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
    public boolean mouseReleased(double mx, double my, int button) {
        if (dragFrom != null && button == 0) {
            String dragged = dragFrom;
            dragFrom = null;
            boolean moved = Math.abs(mx - dragStartX) + Math.abs(my - dragStartY) > 4;
            if (!moved) {
                selectedUnit = dragged; // a click on a token (its medallion) opens its card
                tab = Tab.UNITS;
                rebuild();
                return true;
            }
            if (!overMap(mx, my)) return true;
            String target = tokenAt(mx, my);
            if (target != null && !target.equalsIgnoreCase(dragged)) {
                addLink(target, dragged); // dropped on a unit: "from" = that unit, the dragged one acts
                message = Component.translatable("gui.rallyguard.planner.link_made",
                        Component.translatable("gui.rallyguard.planner.v." + LINKS[linkType].toLowerCase(Locale.ROOT)),
                        shortPath(target), shortPath(dragged)).getString();
            } else {
                ax = toWorldX(mx);
                az = toWorldZ(my);
                int moved2 = 0;
                for (JsonObject l : links) {
                    if (!strOf(l, "to", "").equalsIgnoreCase(dragged)) continue;
                    String type = strOf(l, "type", "");
                    if (type.equals("AMBUSH")) {
                        JsonObject params = l.has("params") ? l.getAsJsonObject("params") : new JsonObject();
                        params.addProperty("wx", String.format(Locale.ROOT, "%.1f", ax));
                        params.addProperty("wz", String.format(Locale.ROOT, "%.1f", az));
                        l.add("params", params);
                        moved2++;
                    } else if (type.equals("RENDEZVOUS") || type.equals("PINCER") || type.equals("SIGNAL_RESERVE")) {
                        l.addProperty("x", ax);
                        l.addProperty("z", az);
                        moved2++;
                    }
                }
                message = Component.translatable("gui.rallyguard.planner.point_set", shortPath(dragged), moved2).getString();
            }
            rebuild();
            return true;
        }
        return super.mouseReleased(mx, my, button);
    }

    @Override
    public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        // The table: dark wood, a lighter rim, parchment side panels.
        g.fill(0, 0, this.width, this.height, WOOD_DARK);
        g.fill(2, 2, this.width - 2, this.height - 2, WOOD);
        for (int y = 6; y < this.height; y += 9) g.fill(2, y, this.width - 2, y + 1, WOOD_LIGHT); // grain
        g.fill(left() - 3, 20, left() + 157, this.height - 6, PARCHMENT_DARK);
        g.fill(left() - 2, 21, left() + 156, this.height - 7, PARCHMENT);
        g.fill(right() - 3, 20, right() + 159, this.height - 6, PARCHMENT_DARK);
        g.fill(right() - 2, 21, right() + 158, this.height - 7, PARCHMENT);
        int ms = mapSize();
        g.fill(mapX() - 4, mapY() - 4, mapX() + ms + 4, mapY() + ms + 4, WOOD_DARK);
        g.fill(mapX() - 2, mapY() - 2, mapX() + ms + 2, mapY() + ms + 2, PARCHMENT_DARK);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        buildMap();
        mouseNowX = mouseX;
        mouseNowY = mouseY;
        super.render(g, mouseX, mouseY, delta);
        int ms = mapSize();
        g.blit(MAP_ID, mapX(), mapY(), 0, 0, ms, ms, ms, ms);
        g.drawString(this.font, this.title, mapX(), 8, 0xF0E0C0, true);
        JsonObject f = force();
        if (f != null) {
            drawLinks(g);
            for (JsonElement e : f.getAsJsonArray("units")) drawToken(g, e.getAsJsonObject());
            for (JsonElement e : f.getAsJsonArray("enemies")) drawEnemy(g, e.getAsJsonObject());
            String scn = strOf(f, "scenario", "");
            Component status = Component.translatable("gui.rallyguard.planner.status", strOf(f, "general", ""),
                    strOf(f, "operation", ""), strOf(f, "plan", ""), intOf(f, "phase", -1) + 1);
            g.drawString(this.font, status, mapX() + 40, mapY() + ms + 7, 0xF0E0C0, true);
            if (!scn.isEmpty()) {
                g.drawString(this.font, Component.translatable("gui.rallyguard.planner.scenario_running",
                        Component.translatable("gui.rallyguard.scenario." + scn.toLowerCase(Locale.ROOT))),
                        mapX() + 4, mapY() + 4, 0xFFFF6040, true);
            }
        }
        if (state.has("hqs")) {
            for (JsonElement e : state.getAsJsonArray("hqs")) {
                JsonObject h = e.getAsJsonObject();
                int sx = toScreenX(h.get("x").getAsDouble()), sz = toScreenZ(h.get("z").getAsDouble());
                if (!inMap(sx, sz)) continue;
                g.fill(sx - 3, sz - 3, sx + 4, sz + 4, 0xFF8A1A1A);
                g.fill(sx - 2, sz - 2, sx + 3, sz + 3, 0xFFFFD700);
                g.drawString(this.font, h.get("name").getAsString(), sx + 5, sz - 4, 0xFFD700, true);
            }
        }
        if (!Double.isNaN(ax)) drawPoint(g, ax, az, "A", 0xFFFFFFFF);
        if (!Double.isNaN(bx)) drawPoint(g, bx, bz, "B", 0xFFFFFF55);
        if (dragFrom != null) {
            JsonObject u = unit(dragFrom);
            if (u != null && u.has("x")) {
                line(g, toScreenX(u.get("x").getAsDouble()), toScreenZ(u.get("z").getAsDouble()), mouseNowX, mouseNowY,
                        linkColor(LINKS[linkType]));
            }
        }
        drawPanels(g, f);
    }

    /** A unit token: its rows of dots, the identity badge, count, health and morale bars, the captain's medallion. */
    private void drawToken(GuiGraphics g, JsonObject u) {
        if (!u.has("x")) return;
        double ux = u.get("x").getAsDouble(), uz = u.get("z").getAsDouble();
        int sx = toScreenX(ux), sz = toScreenZ(uz);
        if (!inMap(sx, sz)) return;
        String path = strOf(u, "path", "");
        String arch = strOf(u, "arch", "");
        int color = arch.isEmpty() ? 0xFF5599FF : GuardCommandScreen.archetypeColor(arch);
        boolean selected = path.equalsIgnoreCase(selectedUnit);
        List<double[]> dots = tokenDots.get(path);
        double ppb = pxPerBlock();
        if (dots != null) {
            for (double[] d : dots) {
                int px = sx + (int) Math.round(d[0] * ppb), pz = sz + (int) Math.round(d[1] * ppb);
                if (!inMap(px, pz)) continue;
                g.fill(px - 1, pz - 1, px + 1, pz + 1, selected ? 0xFFFFFFFF : color);
            }
        }
        // Identity badge (archetype initial; a mixed group gets a "?" corner).
        int bx = sx + 6, by = sz - 10;
        g.fill(bx - 1, by - 1, bx + 10, by + 10, selected ? 0xFFFFFFFF : 0xFF1A120A);
        g.fill(bx, by, bx + 9, by + 9, color);
        String initial = arch.isEmpty() ? "?" : arch.substring(0, 1);
        g.drawString(this.font, initial, bx + 5 - this.font.width(initial) / 2, by + 1, 0xFFFFFF, false);
        if (u.has("identity") && !u.get("identity").getAsBoolean()) g.fill(bx + 7, by, bx + 9, by + 2, 0xFFFFFF55);
        // Count, health and morale.
        g.drawString(this.font, String.valueOf(intOf(u, "count", 0)), bx + 12, by + 1, 0xFFFFFF, true);
        int health = intOf(u, "health", -1), morale = intOf(u, "morale", -1);
        if (health >= 0) {
            g.fill(bx, by + 11, bx + 20, by + 13, 0xFF301010);
            g.fill(bx, by + 11, bx + 20 * Math.min(100, health) / 100, by + 13, 0xFFCC3333);
        }
        if (morale >= 0) {
            g.fill(bx, by + 14, bx + 20, by + 16, 0xFF202020);
            g.fill(bx, by + 14, bx + 20 * Math.min(100, morale) / 100, by + 16, GuardCommandScreen.moraleColor(morale));
        }
        // Command medallion (the captain): click opens the card.
        if (!strOf(u, "captain", "").isEmpty()) {
            g.fill(sx - 3, sz - 3, sx + 4, sz + 4, 0xFF6A4A10);
            g.fill(sx - 2, sz - 2, sx + 3, sz + 3, 0xFFE8C04A);
        } else {
            g.fill(sx - 2, sz - 2, sx + 3, sz + 3, 0xFF8A7A6A);
        }
        g.drawString(this.font, fit(shortPath(path), 60), sx + 6, sz + 8, selected ? 0xFFFFFF : 0xF0E6D0, true);
    }

    private void drawEnemy(GuiGraphics g, JsonObject en) {
        int sx = toScreenX(en.get("x").getAsDouble()), sz = toScreenZ(en.get("z").getAsDouble());
        if (!inMap(sx, sz)) return;
        g.fill(sx - 3, sz - 3, sx + 4, sz + 4, 0xFF400000);
        g.fill(sx - 2, sz - 2, sx + 3, sz + 3, 0xFFFF3333);
        StringBuilder label = new StringBuilder(String.valueOf(intOf(en, "count", 0)));
        int ranged = intOf(en, "ranged", 0), flyers = intOf(en, "flyers", 0), bosses = intOf(en, "bosses", 0);
        if (ranged > 0) label.append(" ➶").append(ranged);
        if (flyers > 0) label.append(" ✈").append(flyers);
        if (bosses > 0) label.append(" ☠").append(bosses);
        if (en.has("fleeing") && en.get("fleeing").getAsBoolean()) label.append(" ⇢");
        g.drawString(this.font, label.toString(), sx + 5, sz - 4, 0xFF7070, true);
    }

    /** The plan's links as arrows from the reference unit to the acting one (AMBUSH: to its wait point). */
    private void drawLinks(GuiGraphics g) {
        for (JsonObject l : links) {
            JsonObject from = unit(strOf(l, "from", "")), to = unit(strOf(l, "to", ""));
            if (from == null || to == null || !from.has("x") || !to.has("x")) continue;
            int x1 = toScreenX(from.get("x").getAsDouble()), z1 = toScreenZ(from.get("z").getAsDouble());
            int x2 = toScreenX(to.get("x").getAsDouble()), z2 = toScreenZ(to.get("z").getAsDouble());
            String type = strOf(l, "type", "");
            int color = linkColor(type);
            line(g, x2, z2, x1, z1, color);
            arrowHead(g, x2, z2, x1, z1, color);
            if (type.equals("AMBUSH") && l.has("params") && l.getAsJsonObject("params").has("wx")) {
                JsonObject p = l.getAsJsonObject("params");
                try {
                    int wx = toScreenX(Double.parseDouble(p.get("wx").getAsString()));
                    int wz = toScreenZ(Double.parseDouble(p.get("wz").getAsString()));
                    dashed(g, x2, z2, wx, wz, color);
                    g.fill(wx - 2, wz - 2, wx + 3, wz + 3, color);
                } catch (RuntimeException ignored) {
                    // a hand-edited plan with a bad point: no arrow
                }
            }
        }
    }

    private static int linkColor(String type) {
        return switch (type) {
            case "AMBUSH", "PINCER" -> 0xFFB03030;
            case "OPEN_LANE" -> 0xFF30A0B0;
            case "COUNTERATTACK", "REINFORCE", "SIGNAL_RESERVE" -> 0xFFD07020;
            case "DELAY", "STAGGERED_RETREAT", "COVER" -> 0xFF6A8A30;
            case "RELIEF", "HOLD_UNTIL_RELIEVED" -> 0xFF4060C0;
            default -> 0xFF3A2A18;
        };
    }

    private void line(GuiGraphics g, int x1, int y1, int x2, int y2, int color) {
        int steps = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        if (steps == 0) return;
        for (int i = 0; i <= steps; i += 1) {
            int x = x1 + (x2 - x1) * i / steps, y = y1 + (y2 - y1) * i / steps;
            if (inMap(x, y)) g.fill(x, y, x + 1, y + 1, color);
        }
    }

    private void dashed(GuiGraphics g, int x1, int y1, int x2, int y2, int color) {
        int steps = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        if (steps == 0) return;
        for (int i = 0; i <= steps; i++) {
            if ((i / 3) % 2 == 1) continue;
            int x = x1 + (x2 - x1) * i / steps, y = y1 + (y2 - y1) * i / steps;
            if (inMap(x, y)) g.fill(x, y, x + 1, y + 1, color);
        }
    }

    /** Two short strokes at (x2, y2) pointing back along the line. */
    private void arrowHead(GuiGraphics g, int x1, int y1, int x2, int y2, int color) {
        double dx = x2 - x1, dy = y2 - y1, len = Math.sqrt(dx * dx + dy * dy);
        if (len < 8) return;
        double ux = dx / len, uy = dy / len;
        int bx = (int) (x2 - ux * 9), by = (int) (y2 - uy * 9);
        line(g, x2 - (int) (ux * 4), y2 - (int) (uy * 4), bx + (int) (-uy * 4), by + (int) (ux * 4), color);
        line(g, x2 - (int) (ux * 4), y2 - (int) (uy * 4), bx - (int) (-uy * 4), by - (int) (ux * 4), color);
    }

    /** Text on the parchment panels: the selected unit's card, the phases timeline, links, messages. */
    private void drawPanels(GuiGraphics g, @Nullable JsonObject f) {
        if (f != null && tab == Tab.UNITS) {
            int y = 24 + 16 * Math.min(4, forces().size()) + 18 + 50 + 18;
            for (JsonObject u : units()) {
                if (y > this.height - 96) break;
                String arch = strOf(u, "arch", "");
                int color = arch.isEmpty() ? 0xFF808080 : GuardCommandScreen.archetypeColor(arch);
                g.fill(left(), y + 1, left() + 14, y + 12, color);
                int morale = intOf(u, "morale", -1);
                g.fill(left() + 15, y + 1, left() + 17, y + 12, GuardCommandScreen.moraleColor(morale));
                y += 14;
            }
            JsonObject sel = selectedUnit.isEmpty() ? null : unit(selectedUnit);
            if (sel != null) drawCard(g, sel, this.height - 92);
        }
        if (f != null && tab == Tab.SCENARIOS) {
            String running = strOf(f, "scenario", "");
            if (!running.isEmpty()) {
                g.drawString(this.font, Component.translatable("gui.rallyguard.planner.scenario_running",
                        Component.translatable("gui.rallyguard.scenario." + running.toLowerCase(Locale.ROOT))),
                        left(), this.height - 20, 0xB03020, false);
            }
        }
        // Phases timeline (the running phase in red ink) and links.
        int px = right();
        int py = phaseListY();
        int current = f != null && strOf(f, "plan", "").equalsIgnoreCase(nameBox == null ? "" : nameBox.getValue().trim())
                ? intOf(f, "phase", -1) : -1;
        for (int i = 0; i < phases.size(); i++) {
            JsonObject p = phases.get(i);
            String obj = strOf(p, "objective", "").toLowerCase(Locale.ROOT);
            String extra = p.has("tempo") ? " · " + strOf(p, "tempo", "").toLowerCase(Locale.ROOT) : "";
            if (p.has("instruction") && !strOf(p, "instruction", "").isEmpty()) extra += " · " + strOf(p, "instruction", "").toLowerCase(Locale.ROOT);
            String text = fit((i + 1) + ". " + obj + " ⟶ " + strOf(p, "trigger", "").toLowerCase(Locale.ROOT) + extra, 142);
            g.drawString(this.font, text, px, py + i * 11, i == current ? 0xB03020 : INK, false);
            if (i < phases.size() - 1) g.fill(px + 2, py + i * 11 + 9, px + 3, py + i * 11 + 11, INK_SOFT | 0xFF000000);
        }
        int ly = linkListY();
        for (int i = 0; i < links.size(); i++) {
            JsonObject l = links.get(i);
            int color = linkColor(strOf(l, "type", ""));
            g.fill(px, ly + i * 11 + 2, px + 4, ly + i * 11 + 6, color);
            g.drawString(this.font, fit(strOf(l, "type", "").toLowerCase(Locale.ROOT) + " " + shortPath(strOf(l, "from", ""))
                    + "→" + shortPath(strOf(l, "to", "")), 136), px + 6, ly + i * 11, INK, false);
        }
        g.drawString(this.font, Component.translatable("gui.rallyguard.planner.value", fmt(TRIGGERS[trigger], value)),
                right() + 118, valueY(), INK, false);
        // Fills made by the running plan
        if (f != null && f.has("fills") && !f.getAsJsonObject("fills").entrySet().isEmpty()) {
            int fy = mapY() + mapSize() - 10;
            for (Map.Entry<String, JsonElement> e : f.getAsJsonObject("fills").entrySet()) {
                g.drawString(this.font, "⇄ " + shortPath(e.getKey()) + " → " + shortPath(e.getValue().getAsString()),
                        mapX() + 4, fy, 0xFFE0C060, true);
                fy -= 10;
                if (fy < mapY() + 16) break;
            }
        }
        // Warnings and messages
        int wy = this.height - 14;
        if (!message.isEmpty()) {
            g.drawString(this.font, fit(message, mapSize()), mapX(), wy, 0xFFFF55, true);
            wy -= 10;
        }
        if (state.has("warnings")) {
            for (JsonElement e : state.getAsJsonArray("warnings")) {
                if (wy < mapY() + mapSize() + 20) break;
                g.drawString(this.font, fit(e.getAsString(), mapSize()), mapX(), wy, 0xFF8855, true);
                wy -= 10;
            }
        }
    }

    /** The selected unit's group card on the parchment: identity, count, consistency, health, morale, cohesion, layout. */
    private void drawCard(GuiGraphics g, JsonObject u, int y) {
        int x = left();
        String arch = strOf(u, "arch", "");
        int color = arch.isEmpty() ? 0xFF808080 : GuardCommandScreen.archetypeColor(arch);
        g.fill(x, y, x + 152, y + 1, INK_SOFT | 0xFF000000);
        g.fill(x, y + 4, x + 14, y + 18, color);
        Component name = arch.isEmpty() ? Component.literal("?")
                : Component.translatable("gui.rallyguard.archetype." + arch.toLowerCase(Locale.ROOT));
        g.drawString(this.font, fit(name.getString() + " · " + intOf(u, "count", 0), 134), x + 18, y + 4, INK, false);
        g.drawString(this.font, fit(shortPath(strOf(u, "path", "")) + " · " + strOf(u, "captain", "-"), 134), x + 18, y + 14, INK_SOFT, false);
        int row = y + 26;
        row = bar(g, x, row, "gui.rallyguard.planner.card.consistency", intOf(u, "consistency", -1), 0xFF6A8A30);
        row = bar(g, x, row, "gui.rallyguard.planner.card.health", intOf(u, "health", -1), 0xFFB03030);
        int morale = intOf(u, "morale", -1);
        row = bar(g, x, row, "gui.rallyguard.planner.card.morale", morale, GuardCommandScreen.moraleColor(morale));
        row = bar(g, x, row, "gui.rallyguard.planner.card.cohesion", intOf(u, "cohesion", -1), 0xFF4060C0);
        String layout = strOf(u, "layout", "");
        g.drawString(this.font, fit(Component.translatable("gui.rallyguard.planner.card.layout",
                strOf(u, "shape", "LINE").toLowerCase(Locale.ROOT), layout.toLowerCase(Locale.ROOT)).getString(), 152), x, row, INK_SOFT, false);
        // E3B C1: what the unit can do (RECON for scouts), as the planner's links and scenarios need it.
        if (u.has("caps")) {
            StringBuilder caps = new StringBuilder();
            for (JsonElement c : u.getAsJsonArray("caps")) {
                if (caps.length() > 0) caps.append(", ");
                caps.append(Component.translatableWithFallback("gui.rallyguard.capability." + c.getAsString().toLowerCase(Locale.ROOT),
                        c.getAsString()).getString());
            }
            g.drawString(this.font, fit(caps.toString(), 152), x, row + 10, INK_SOFT, false);
        }
    }

    private int bar(GuiGraphics g, int x, int y, String key, int value, int color) {
        g.drawString(this.font, Component.translatable(key), x, y, INK, false);
        g.fill(x + 70, y + 2, x + 150, y + 7, 0xFF8A7A5A);
        if (value >= 0) g.fill(x + 70, y + 2, x + 70 + 80 * Math.min(100, value) / 100, y + 7, color);
        else g.drawString(this.font, "-", x + 106, y, INK_SOFT, false);
        return y + 10;
    }

    private int valueY() {
        return 24 + 17 + 16 + (OBJECTIVES[objective].equals("INSTRUCTION") ? 16 : 0) + 3;
    }

    private int phaseListY() {
        return 24 + 17 + 16 * 4 + (OBJECTIVES[objective].equals("INSTRUCTION") ? 16 : 0);
    }

    private int linkListY() {
        return phaseListY() + Math.max(1, phases.size()) * 11 + 4 + 16 * 4;
    }

    private static String fmt(String trigger, double v) {
        return switch (trigger) {
            case "CASUALTIES" -> (int) v + " %";
            case "TIME", "CONTACT" -> (int) v + " s";
            case "SIGNAL" -> "#" + (int) v;
            default -> "-";
        };
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
