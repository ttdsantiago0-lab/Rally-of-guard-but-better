package net.hfstack.rallyguard.debug.sim;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;

/**
 * F204 (O9, sim only, measure only): items on the ground during a fight.
 *
 * <ul>
 *   <li>Every run: once a second {@link #sample} counts the living {@link ItemEntity} inside the arena box;
 *       {@link #finish} writes {@code itemsMean}, {@code itemsMax}, {@code itemsEnd} (with {@code itemsN} samples).</li>
 *   <li>Tag {@code noloot}: {@link #begin} sets the game rules {@code doMobLoot} and {@code doEntityDrops} to false
 *       and remembers their old values; {@link #restore} (end of run, end of batch, error) puts them back. Off without
 *       the tag: the rules are never touched.</li>
 * </ul>
 */
public final class SimGround {
    private SimGround() {
    }

    private static long n, sum;
    private static int max, last;
    private static ServerLevel touched;
    private static boolean oldMobLoot, oldEntityDrops;

    /** Start of a run (after SimExtras.begin): counters to zero; {@code noloot} switches the drop rules off. */
    public static void begin(ServerLevel level, SimScenario s) {
        restore(); // a run that never ended cleanly leaves nothing behind
        n = sum = 0;
        max = last = 0;
        if (level == null || s == null || !s.tags().contains("noloot")) return;
        GameRules rules = level.getGameRules();
        oldMobLoot = rules.getBoolean(GameRules.RULE_DOMOBLOOT);
        oldEntityDrops = rules.getBoolean(GameRules.RULE_DOENTITYDROPS);
        rules.getRule(GameRules.RULE_DOMOBLOOT).set(false, level.getServer());
        rules.getRule(GameRules.RULE_DOENTITYDROPS).set(false, level.getServer());
        touched = level;
        SimExtras.put("noloot", 1.0D); // proof in the telemetry
    }

    /** Once a second during the fight: living items inside the arena box. */
    public static void sample(ServerLevel level, AABB box) {
        if (level == null || box == null) return;
        int c = level.getEntitiesOfClass(ItemEntity.class, box, e -> e.isAlive()).size();
        n++;
        sum += c;
        if (c > max) max = c;
        last = c;
    }

    /** End of a run, before SimExtras.finish: the item keys; then the rules back. */
    public static void finish() {
        if (n > 0) {
            SimExtras.put("itemsMean", (double) sum / n);
            SimExtras.put("itemsMax", max);
            SimExtras.put("itemsEnd", last);
            SimExtras.put("itemsN", n);
        }
        restore();
    }

    /** The drop rules back to what they were (no-op when {@code noloot} did not touch them). */
    public static void restore() {
        ServerLevel level = touched;
        if (level == null) return;
        touched = null;
        GameRules rules = level.getGameRules();
        rules.getRule(GameRules.RULE_DOMOBLOOT).set(oldMobLoot, level.getServer());
        rules.getRule(GameRules.RULE_DOENTITYDROPS).set(oldEntityDrops, level.getServer());
    }
}
