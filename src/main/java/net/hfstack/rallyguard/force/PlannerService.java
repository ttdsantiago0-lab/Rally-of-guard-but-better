package net.hfstack.rallyguard.force;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.ForceRegistry;
import net.hfstack.rallyguard.contract.HqRegistry;
import net.hfstack.rallyguard.contract.PlanRegistry;
import net.hfstack.rallyguard.network.payload.PlannerActionC2SPayload;
import net.hfstack.rallyguard.network.payload.PlannerStateS2CPayload;
import net.hfstack.rallyguard.order.CommanderSelection;
import net.hfstack.rallyguard.tactics.Doctrine;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * E3A A4 - the Battle Planner's server side. Authority is here: every action is validated
 * against the sender (it must own the force; plans are per player), then the state is sent
 * back. The client only draws and asks.
 * <p>
 * Also the group keys: {@code G + number} selects the player's force N (its loaded guards
 * become the selection and the "active force"), {@code G + sneak + number} runs an operation
 * shortcut on the active force (1 hold the line, 2 fall back fighting, 3 advance by turns,
 * 4 flank, 5 orderly retreat) anchored where the commander stands.
 */
public final class PlannerService {
    private PlannerService() {
    }

    private static final Map<UUID, String> ACTIVE_FORCE = new HashMap<>();

    /** Inside one of the player's HQ zones ({@code hq.radius} around its banner, same dimension)? */
    public static boolean inHqZone(ServerPlayer player, BlockPos pos) {
        double r = RallyConfig.i(RallyConfig.HQ_RADIUS, 16);
        String dim = player.serverLevel().dimension().location().toString();
        for (HqRegistry.Hq hq : HqRegistry.get(player.serverLevel()).ownedBy(player.getUUID())) {
            if (dim.equals(hq.dimension) && hq.pos.distSqr(pos) <= r * r) return true;
        }
        return false;
    }

    public static void open(ServerPlayer player) {
        send(player, true, null, null);
    }

    public static void handle(PlannerActionC2SPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)
                    || !net.hfstack.rallyguard.network.NetSafety.allow(player)) return; // E3B R6: packet budget
            String message = null;
            JsonObject loaded = null;
            try {
                JsonObject args = payload.json().isBlank() ? new JsonObject() : JsonParser.parseString(payload.json()).getAsJsonObject();
                ServerLevel level = player.serverLevel();
                ForceRegistry forces = ForceRegistry.get(level);
                ForceRegistry.Force force = args.has("force") ? forces.find(player.getUUID(), args.get("force").getAsString()) : null;
                // E3B R6 (B8): the HQ zone (where the planning table is) is checked on EVERY action, not only on opening.
                if (!inHqZone(player, player.blockPosition())) throw new IllegalArgumentException("hq");
                switch (payload.action()) {
                    case "refresh" -> {
                    }
                    case "select" -> {
                        if (force == null) throw new IllegalArgumentException("force");
                        select(player, force);
                    }
                    case "doctrine" -> {
                        if (force == null) throw new IllegalArgumentException("force");
                        force.doctrine = Doctrine.parse(args.get("doctrine").getAsString()).name();
                        forces.changed();
                    }
                    case "role" -> {
                        if (force == null) throw new IllegalArgumentException("force");
                        String unit = args.get("unit").getAsString();
                        String role = args.get("role").getAsString().toUpperCase(Locale.ROOT);
                        int slash = unit.indexOf('/');
                        String unitName = slash >= 0 ? unit.substring(slash + 1) : "";
                        if (unitName.length() > 64 || (!force.units.containsKey(unitName) && force.units.size() >= Plans.MAX_ROLES)) {
                            throw new IllegalArgumentException("limit");
                        }
                        force.units.put(unitName, Roles.isRole(role) ? role : "");
                        forces.changed();
                    }
                    case "op" -> {
                        if (force == null) throw new IllegalArgumentException("force");
                        String op = args.get("op").getAsString().toUpperCase(Locale.ROOT);
                        if (op.equals("STOP")) Operations.stop(force);
                        else {
                            Vec3 at = args.has("x") ? net.hfstack.rallyguard.network.NetSafety.point(player,
                                    args.get("x").getAsDouble(), player.getY(), args.get("z").getAsDouble()) : player.position();
                            if (at == null) throw new IllegalArgumentException("far"); // E3B R6: finite, ≤ 512 blocks
                            Operations.start(level, force, Operations.Type.valueOf(op), at,
                                    args.has("yaw") ? args.get("yaw").getAsFloat() : player.getYRot());
                        }
                    }
                    case "execute" -> {
                        Plans.Plan plan = Plans.parse(args.get("plan").toString());
                        Plans.checkNear(plan, player); // E3B R6: every point finite and ≤ 512 blocks away
                        ForceRegistry.Force f = forces.find(player.getUUID(), plan.force());
                        if (f == null) throw new IllegalArgumentException("force");
                        Plans.execute(level, f, plan);
                        List<String> warnings = Plans.warnings(level, f, plan);
                        message = warnings.isEmpty() ? "executed" : "executed with warnings";
                    }
                    case "signal" -> { // E3B P1: the commander's manual signal (SIGNAL triggers, SIGNAL_RESERVE)
                        if (force == null) throw new IllegalArgumentException("force");
                        int id = args.get("id").getAsInt();
                        if (id < 1 || id > 9) throw new IllegalArgumentException("signal");
                        if (!Plans.signal(force, id)) throw new IllegalArgumentException("plan");
                        message = "signal";
                    }
                    case "cancel" -> {
                        if (force == null) throw new IllegalArgumentException("force");
                        Plans.cancel(force);
                    }
                    case "save" -> {
                        Plans.Plan plan = Plans.parse(args.get("plan").toString());
                        Plans.checkNear(plan, player);
                        if (!PlanRegistry.get(level).put(player.getUUID(), plan.name(), Plans.toJson(plan).toString())) {
                            throw new IllegalArgumentException("limit");
                        }
                        message = "saved";
                    }
                    case "load" -> {
                        String json = PlanRegistry.get(level).find(player.getUUID(), args.get("name").getAsString());
                        if (json == null) throw new IllegalArgumentException("plan");
                        loaded = Plans.toJson(Plans.parse(json));
                    }
                    case "delete" -> PlanRegistry.get(level).remove(player.getUUID(), args.get("name").getAsString());
                    default -> throw new IllegalArgumentException("action");
                }
            } catch (RuntimeException e) {
                message = "error: " + e.getMessage();
            }
            send(player, false, message, loaded);
        });
    }

    /** Selects a force: its loaded guards become the commander's selection (G orders reach it). */
    public static void select(ServerPlayer player, ForceRegistry.Force force) {
        ACTIVE_FORCE.put(player.getUUID(), force.squad);
        List<UUID> ids = new ArrayList<>();
        for (Forces.UnitView u : Forces.units(player.serverLevel(), force)) for (Guard g : u.members()) ids.add(g.getUUID());
        CommanderSelection.set(player.getUUID(), ids);
        player.displayClientMessage(Component.translatable("gui.rallyguard.planner.selected", force.squad, ids.size()), true);
    }

    /** G + number (1-9): select the player's force N. */
    public static void selectByNumber(ServerPlayer player, int n) {
        List<ForceRegistry.Force> mine = ForceRegistry.get(player.serverLevel()).ownedBy(player.getUUID());
        if (n < 1 || n > mine.size()) {
            player.displayClientMessage(Component.translatable("gui.rallyguard.planner.no_force", n), true);
            return;
        }
        select(player, mine.get(n - 1));
    }

    /** G + sneak + number: operation shortcut on the active force. */
    public static void operationShortcut(ServerPlayer player, int n) {
        ForceRegistry.Force force = activeForce(player);
        if (force == null) {
            player.displayClientMessage(Component.translatable("gui.rallyguard.planner.no_active"), true);
            return;
        }
        Operations.Type[] ops = {Operations.Type.HOLD_LINE, Operations.Type.FALLBACK_FIGHTING, Operations.Type.ADVANCE_BY_TURNS,
                Operations.Type.FLANK, Operations.Type.ORDERLY_RETREAT};
        if (n < 1 || n > ops.length) return;
        Operations.start(player.serverLevel(), force, ops[n - 1], player.position(), player.getYRot());
        player.displayClientMessage(Component.translatable("gui.rallyguard.force.op_started", force.squad,
                Component.translatable("gui.rallyguard.op." + ops[n - 1].name().toLowerCase(Locale.ROOT))), true);
    }

    @Nullable
    private static ForceRegistry.Force activeForce(ServerPlayer player) {
        String squad = ACTIVE_FORCE.get(player.getUUID());
        return squad == null ? null : ForceRegistry.get(player.serverLevel()).find(player.getUUID(), squad);
    }

    private static void send(ServerPlayer player, boolean open, @Nullable String message, @Nullable JsonObject loaded) {
        ServerLevel level = player.serverLevel();
        long now = level.getGameTime();
        JsonObject state = new JsonObject();
        JsonArray forcesJson = new JsonArray();
        JsonArray warnings = new JsonArray();
        for (ForceRegistry.Force f : ForceRegistry.get(level).ownedBy(player.getUUID())) {
            JsonObject fj = new JsonObject();
            fj.addProperty("squad", f.squad);
            fj.addProperty("doctrine", f.doctrine);
            Operations.Type op = Operations.current(f);
            fj.addProperty("operation", op == null ? "" : op.name());
            Plans.Plan plan = Plans.active(f);
            fj.addProperty("plan", plan == null ? "" : plan.name());
            fj.addProperty("phase", Plans.activePhase(f));
            Guard general = Forces.generalOf(level, f);
            fj.addProperty("general", general == null ? "" : general.getName().getString() + " (" + Leadership.ledgerScore(general) + ")");
            List<Forces.UnitView> units = Forces.units(level, f);
            Map<String, String> roles = Roles.assign(level, f, units);
            JsonArray uj = new JsonArray();
            for (Forces.UnitView u : units) {
                if (uj.size() >= 48) break;
                JsonObject o = new JsonObject();
                o.addProperty("path", u.path());
                o.addProperty("type", Forces.typeOf(u));
                o.addProperty("role", roles.getOrDefault(u.path(), u.role()));
                o.addProperty("declared", u.role());
                o.addProperty("count", u.members().size());
                o.addProperty("captain", u.captain() == null ? "" : u.captain().getName().getString());
                o.addProperty("score", u.captain() == null ? 0 : Math.round(Leadership.score(u.captain())));
                double x = 0, z = 0;
                for (Guard g : u.members()) {
                    x += g.getX();
                    z += g.getZ();
                }
                int n = Math.max(1, u.members().size());
                o.addProperty("x", x / n);
                o.addProperty("z", z / n);
                JsonArray caps = new JsonArray();
                u.capabilities().forEach(c -> caps.add(c.name()));
                o.add("caps", caps);
                // E3B P3: what the battle table draws on the unit's token (identity, rows, health, morale).
                String ukey = UnitOrders.unitKey(f.owner, u.path());
                if (!u.members().isEmpty()) {
                    net.hfstack.rallyguard.command.GroupProfile.Profile prof =
                            net.hfstack.rallyguard.command.GroupProfile.cached(ukey, u.members(), now);
                    o.addProperty("arch", prof.dominant().name());
                    o.addProperty("identity", prof.identity());
                    o.addProperty("consistency", prof.consistency());
                }
                SituationReports.Report rep = SituationReports.last(ukey);
                if (rep != null) {
                    o.addProperty("health", Math.round(rep.health() * 100));
                    o.addProperty("morale", Math.round(rep.morale()));
                    o.addProperty("cohesion", Math.round(rep.cohesion()));
                    o.addProperty("routed", rep.routed());
                }
                o.addProperty("layout", net.hfstack.rallyguard.contract.FormationRegistry.get(level).unitLayout(f.owner, u.path()).encode());
                UnitOrders.UnitOrder uo = UnitOrders.current(ukey);
                o.addProperty("shape", uo == null ? "LINE" : uo.shape().name());
                o.addProperty("yaw", uo == null ? 0 : uo.facingYaw());
                uj.add(o);
            }
            fj.add("units", uj);
            JsonArray enemies = new JsonArray();
            for (SituationReports.KnownEnemy e : SituationReports.knownEnemies(ForceBrain.forceKey(f), now)) {
                if (enemies.size() >= 32) break;
                JsonObject ej = new JsonObject();
                ej.addProperty("x", e.centre().x);
                ej.addProperty("z", e.centre().z);
                ej.addProperty("count", e.count());
                ej.addProperty("ranged", e.ranged()); // E3B P2: composition
                ej.addProperty("flyers", e.flyers());
                ej.addProperty("bosses", e.bosses());
                ej.addProperty("fleeing", e.fleeing());
                enemies.add(ej);
            }
            fj.add("enemies", enemies);
            // E3B P1/P2: fills made, the scenario running and how often each one fired.
            JsonObject fills = new JsonObject();
            Plans.fills(f).forEach(fills::addProperty);
            fj.add("fills", fills);
            Scenarios.Kind scn = Scenarios.current(f);
            fj.addProperty("scenario", scn == null ? "" : scn.name());
            JsonObject fired = new JsonObject();
            Scenarios.activations(f).forEach((k, v) -> fired.addProperty(k.name(), v));
            fj.add("scenarioCount", fired);
            if (plan != null) {
                for (String w : Plans.warnings(level, f, plan)) warnings.add(f.squad + ": " + w);
                for (String w : Scenarios.warnings(plan.scenarios(), units)) warnings.add(f.squad + ": scenario " + w);
            }
            forcesJson.add(fj);
        }
        state.add("forces", forcesJson);
        JsonArray hqs = new JsonArray();
        for (HqRegistry.Hq hq : HqRegistry.get(level).ownedBy(player.getUUID())) {
            if (!hq.dimension.equals(level.dimension().location().toString())) continue;
            JsonObject h = new JsonObject();
            h.addProperty("name", hq.name);
            h.addProperty("x", hq.pos.getX() + 0.5);
            h.addProperty("z", hq.pos.getZ() + 0.5);
            hqs.add(h);
        }
        state.add("hqs", hqs);
        JsonArray plans = new JsonArray();
        PlanRegistry.get(level).names(player.getUUID()).forEach(plans::add);
        state.add("plans", plans);
        state.add("warnings", warnings);
        state.add("scenarioLib", Scenarios.toJson(Scenarios.defaults())); // E3B P3: the Scenarios tab's defaults
        state.addProperty("active", ACTIVE_FORCE.getOrDefault(player.getUUID(), ""));
        state.addProperty("px", player.getX());
        state.addProperty("pz", player.getZ());
        if (message != null) state.addProperty("message", message);
        if (loaded != null) state.add("loaded", loaded);
        String json = state.toString();
        if (json.length() > PlannerStateS2CPayload.MAX_JSON) json = "{\"message\":\"state too large\"}";
        PacketDistributor.sendToPlayer(player, new PlannerStateS2CPayload(json, open));
    }

    public static void forget(UUID player) {
        ACTIVE_FORCE.remove(player);
    }
}
