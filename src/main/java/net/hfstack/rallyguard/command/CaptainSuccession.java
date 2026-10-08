package net.hfstack.rallyguard.command;

import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.contract.SquadRegistry;
import net.hfstack.rallyguard.event.GuardStanceHandler;
import net.hfstack.rallyguard.order.GuardModes;
import net.hfstack.rallyguard.order.RetreatPlanner;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * E2A block 1 - succession of command (playbook §2 "Líder muerto", VETERANIA §3.4).
 * <p>
 * When a guard dies, its unit is either the guards sharing its pushed
 * {@link SquadDirectives.Directive} (the subunit of the last order) or, without one, the
 * loaded guards of its squad/unit path. If the dead guard was that unit's computed captain:
 * <ul>
 *     <li>up to {@code command.maxReliefs} times (3; {@code maxReliefsLargeArmy} = 4 when
 *     the unit has at least {@code command.largeArmySize} guards) the next best guard takes
 *     over - the directive is re-pushed with the new captain and the commander is told;</li>
 *     <li>past that, the unit ROUTS: {@code command.routFleeShare} of it flees
 *     ({@link RetreatPlanner}, as a Retreat order would) and the rest break ranks; all of it
 *     gets a {@link SquadDirectives.Kind#ROUTED} directive (low morale for the decision
 *     ladder).</li>
 * </ul>
 * The relief count resets after 5 minutes without a captain falling (the battle is over;
 * the next captain is simply computed again). Memory only. Runs at HIGH priority so the
 * squad membership is still there (the roster cleanup forgets the guard on the same event).
 */
public final class CaptainSuccession {

    private static final long RESET_AFTER_TICKS = 20L * 60L * 5L;

    private record Reliefs(int count, long lastTick) {
    }

    private static final Map<String, Reliefs> RELIEFS = new HashMap<>();

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Guard dead)) return;
        if (!(dead.level() instanceof ServerLevel level)) return;
        if (!GuardOwnership.hasOwner(dead)) return;
        UUID owner = GuardOwnership.getOwner(dead);

        SquadDirectives.Directive directive = SquadDirectives.of(dead.getUUID());
        String key;
        List<Guard> others = new ArrayList<>();
        if (directive != null && dead.getUUID().equals(directive.captain())) {
            key = owner + "#d" + System.identityHashCode(directive);
            for (Guard g : GuardStanceHandler.loaded(level.dimension())) {
                if (g != dead && g.isAlive() && SquadDirectives.of(g.getUUID()) == directive) others.add(g);
            }
        } else {
            SquadRegistry squads = SquadRegistry.get(level);
            Optional<String> path = squads.pathOf(dead.getUUID());
            if (path.isEmpty()) return;
            key = owner + "#s" + path.get();
            for (Guard g : GuardStanceHandler.loaded(level.dimension())) {
                if (g != dead && g.isAlive() && owner.equals(GuardOwnership.getOwner(g))
                        && path.equals(squads.pathOf(g.getUUID()))) {
                    others.add(g);
                }
            }
            Guard rival = CommandStructure.captainOf(others);
            if (rival != null && CommandStructure.BEST_FIRST.compare(dead, rival) > 0) return; // wasn't the captain
        }
        if (others.isEmpty()) return;

        long now = level.getGameTime();
        Reliefs previous = RELIEFS.get(key);
        int count = previous == null || now - previous.lastTick() > RESET_AFTER_TICKS ? 1 : previous.count() + 1;
        RELIEFS.put(key, new Reliefs(count, now));
        if (RELIEFS.size() > 4096) RELIEFS.clear();

        int unitSize = others.size() + 1;
        int max = unitSize >= RallyConfig.i(RallyConfig.LARGE_ARMY, 100)
                ? RallyConfig.i(RallyConfig.MAX_RELIEFS_LARGE, 4)
                : RallyConfig.i(RallyConfig.MAX_RELIEFS, 3);
        ServerPlayer commander = level.getServer().getPlayerList().getPlayer(owner);

        if (count <= max) {
            Guard next = CommandStructure.captainOf(others);
            if (next == null) return;
            // E3A A1: the unit reorganizes under the new captain after a delay set by ITS
            // leadership score (a good relief takes over at once, a poor one fumbles a while).
            SquadDirectives.Directive relieved = new SquadDirectives.Directive(
                    directive != null ? directive.kind() : SquadDirectives.Kind.FREE,
                    directive != null ? directive.anchor() : null,
                    directive != null ? directive.facingYaw() : 0.0F,
                    next.getUUID(), now,
                    directive != null ? directive.unitIndex() : 0,
                    directive != null ? directive.units() : 1);
            net.hfstack.rallyguard.force.Leadership.invalidate();
            int delay = net.hfstack.rallyguard.force.Leadership.reliefDelay(next);
            if (delay <= 0) SquadDirectives.push(others, relieved);
            else PENDING.add(new Pending(new ArrayList<>(others), relieved, now + delay));
            // E3A V7: the relief is part of the new captain's story (and of its leadership trait).
            net.hfstack.rallyguard.contract.VeterancyRegistry.Record rec =
                    net.hfstack.rallyguard.veterancy.Veterancy.edit(level, next.getUUID());
            rec.reliefsTaken++;
            rec.addStory(level.getDayTime() / 24000L, "relief", dead.getName().getString());
            if (commander != null) {
                commander.sendSystemMessage(Component.translatable("gui.rallyguard.command.captain_relief",
                        dead.getName(), next.getName(), count, max).withStyle(ChatFormatting.GOLD));
            }
            return;
        }

        rout(level, commander, others, now);
        RELIEFS.remove(key);
    }

    private static void rout(ServerLevel level, ServerPlayer commander, List<Guard> unit, long now) {
        SquadDirectives.push(unit, new SquadDirectives.Directive(SquadDirectives.Kind.ROUTED, null, 0.0F,
                null, now, 0, 1));
        RandomSource random = level.getRandom();
        double share = RallyConfig.d(RallyConfig.ROUT_FLEE_SHARE, 0.35D);
        List<Guard> fleeing = new ArrayList<>();
        for (Guard g : unit) {
            g.setTarget(null);
            // E3A V3: a Steady guard never leaves its unit, even when it routs.
            if (!net.hfstack.rallyguard.veterancy.Veterancy.has(g, net.hfstack.rallyguard.veterancy.Trait.STEADY)
                    && random.nextDouble() < share) fleeing.add(g);
            else GuardModes.free(g); // break ranks
        }
        if (!fleeing.isEmpty()) {
            if (commander != null) RetreatPlanner.retreat(commander, fleeing);
            else for (Guard g : fleeing) GuardModes.free(g);
        }
        if (commander != null) {
            commander.sendSystemMessage(Component.translatable("gui.rallyguard.command.unit_routed",
                    unit.size(), fleeing.size()).withStyle(ChatFormatting.RED));
        }
    }

    private record Pending(List<Guard> unit, SquadDirectives.Directive directive, long readyTick) {
    }

    private static final List<Pending> PENDING = new ArrayList<>();

    /** Every tick: pushes relieved directives whose reorganization time is over. */
    public static void tick(ServerLevel level) {
        if (PENDING.isEmpty()) return;
        long now = level.getGameTime();
        PENDING.removeIf(p -> {
            if (p.readyTick() > now) return false;
            List<Guard> alive = new ArrayList<>();
            for (Guard g : p.unit()) if (g.isAlive() && !g.isRemoved()) alive.add(g);
            SquadDirectives.push(alive, p.directive());
            return true;
        });
    }

    public static void clearAll() {
        RELIEFS.clear();
        PENDING.clear();
    }
}
