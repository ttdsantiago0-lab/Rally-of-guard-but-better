package net.hfstack.rallyguard.debug.sim;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * Ficha F1 (2026-11-11, cola de Fable): "tabla de atracción". Does this mob turn on whoever hit it last, how far does it
 * keep chasing, and how long until it lets go? Base of the bait / ping-pong / boss-ring manoeuvres (F3, F4, F6).
 * Simulation only, scenario tag {@code probe:attraction}: 2 invulnerable, motionless guards 8 blocks either side of
 * one mob; every 2 s one of them (alternating) hits it for 1; 1 s later the probe looks at the mob's target. After 10
 * hits both guards are moved out past the mob's follow range and the probe counts the ticks until it drops them.
 * Extras: {@code attrSwitch} (share of hits after which the mob targets the last hitter), {@code attrFollowRange}
 * (the attribute), {@code attrDropTicks} (ticks to let go once out of range; -1 = kept them the whole time).
 */
public final class AttractionProbe {
    private AttractionProbe() {
    }

    public static boolean on;
    private static int t0 = -1, hits, checks, switches, dropStart = -1, dropTicks = -2;
    private static Guard lastHitter;

    public static void begin(SimScenario s) {
        on = s.tags().contains("probe:attraction");
        t0 = -1;
        hits = checks = switches = 0;
        dropStart = -1;
        dropTicks = -2;
        lastHitter = null;
    }

    public static void tick(ServerLevel level, int tick, List<Guard> blue, List<LivingEntity> red) {
        if (!on || blue.size() < 2 || red.isEmpty() || !(red.get(0) instanceof Mob mob) || !mob.isAlive()) return;
        Guard a = blue.get(0), b = blue.get(1);
        if (t0 < 0) {
            t0 = tick;
            for (Guard g : blue) {
                g.setNoAi(true);
                g.setInvulnerable(true);
            }
            a.teleportTo(mob.getX() + 8, mob.getY(), mob.getZ());
            b.teleportTo(mob.getX() - 8, mob.getY(), mob.getZ());
            return;
        }
        int t = tick - t0;
        if (hits < 10) {
            if (t > 0 && t % 40 == 0) {
                lastHitter = hits % 2 == 0 ? a : b;
                mob.hurt(level.damageSources().mobAttack(lastHitter), 1.0F);
                hits++;
            } else if (t > 0 && t % 40 == 20 && lastHitter != null) {
                checks++;
                if (mob.getTarget() == lastHitter) switches++;
            }
            return;
        }
        if (dropStart < 0) {
            double range = mob.getAttributeValue(Attributes.FOLLOW_RANGE);
            dropStart = tick;
            a.teleportTo(mob.getX() + range + 4, mob.getY(), mob.getZ());
            b.teleportTo(mob.getX() - range - 4, mob.getY(), mob.getZ());
            return;
        }
        if (dropTicks == -2) {
            LivingEntity tgt = mob.getTarget();
            if (tgt != a && tgt != b) dropTicks = tick - dropStart;
        }
    }

    public static void end(Mob mob) {
        if (!on) return;
        SimExtras.put("attrSwitch", checks == 0 ? -1 : Math.round(100.0D * switches / checks) / 100.0D);
        SimExtras.put("attrChecks", checks);
        if (mob != null) SimExtras.put("attrFollowRange", mob.getAttributeValue(Attributes.FOLLOW_RANGE));
        SimExtras.put("attrDropTicks", dropTicks == -2 ? -1 : dropTicks);
        on = false;
    }
}
