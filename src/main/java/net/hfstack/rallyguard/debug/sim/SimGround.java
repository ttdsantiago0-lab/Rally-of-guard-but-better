package net.hfstack.rallyguard.debug.sim;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * F204 (O9, sim only, measure only): items on the ground during a fight.
 *
 * <ul>
 *   <li>Every run: once a second {@link #sample} counts the living {@link ItemEntity} inside the arena box;
 *       {@link #finish} writes {@code itemsMean}, {@code itemsMax}, {@code itemsEnd} (with {@code itemsN} samples).</li>
 *   <li>Tag {@code noloot}: {@link #begin} sets the game rules {@code doMobLoot} and {@code doEntityDrops} to false
 *       and remembers their old values; {@link #restore} (end of run, end of batch, error) puts them back. Off without
 *       the tag: the rules are never touched.</li>
 *   <li>F210, every run: the same once-a-second count per item id; {@link #finish} writes the 5 ids with the highest
 *       per-second maximum as {@code itemTop:<id>} (value = that maximum).</li>
 *   <li>F210, tag {@code nodrop}: while the run lasts, {@link #onJoin} cancels the {@link EntityJoinLevelEvent} (a
 *       NeoForge {@code ICancellableEvent}, posted from {@code ServerLevel#addFreshEntity}) of every new
 *       {@link ItemEntity} inside the arena box; {@code nodrop} = 1 as proof, {@code nodropCancelled} = items
 *       cancelled. Off at {@link #finish} and {@link #restore} (end of batch / error), like {@code noloot}.</li>
 * </ul>
 */
public final class SimGround {
    private SimGround() {
    }

    private static long n, sum;
    private static int max, last;
    private static ServerLevel touched;
    private static boolean oldMobLoot, oldEntityDrops;
    private static final Map<String, Integer> TOP = new HashMap<>(); // F210: per id, max items seen in one sample
    private static boolean nodrop;
    private static long nodropCancelled;

    /** Start of a run (after SimExtras.begin): counters to zero; {@code noloot} switches the drop rules off. */
    public static void begin(ServerLevel level, SimScenario s) {
        restore(); // a run that never ended cleanly leaves nothing behind
        n = sum = 0;
        max = last = 0;
        TOP.clear();
        nodropCancelled = 0;
        if (s != null && s.tags().contains("nodrop")) { // F210: no new item enters the arena box this run
            nodrop = true;
            SimExtras.put("nodrop", 1.0D);
        }
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
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, box, e -> e.isAlive());
        int c = items.size();
        Map<String, Integer> now = new HashMap<>(); // F210: this second's count per item id
        for (ItemEntity it : items)
            now.merge(BuiltInRegistries.ITEM.getKey(it.getItem().getItem()).toString(), 1, Integer::sum);
        for (Map.Entry<String, Integer> e : now.entrySet()) TOP.merge(e.getKey(), e.getValue(), Math::max);
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
        List<Map.Entry<String, Integer>> top = new ArrayList<>(TOP.entrySet()); // F210: the 5 biggest ids
        top.sort((a, b) -> b.getValue() - a.getValue());
        for (int i = 0; i < Math.min(5, top.size()); i++) SimExtras.put("itemTop:" + top.get(i).getKey(), top.get(i).getValue());
        if (nodrop) SimExtras.put("nodropCancelled", nodropCancelled);
        restore();
    }

    /** The drop rules back to what they were (no-op when {@code noloot} did not touch them). */
    public static void restore() {
        nodrop = false; // F210: the cancel never outlives a run
        ServerLevel level = touched;
        if (level == null) return;
        touched = null;
        GameRules rules = level.getGameRules();
        rules.getRule(GameRules.RULE_DOMOBLOOT).set(oldMobLoot, level.getServer());
        rules.getRule(GameRules.RULE_DOENTITYDROPS).set(oldEntityDrops, level.getServer());
    }

    /**
     * F210, tag {@code nodrop}: called by the sim's join listener with the arena box (null when no arena); cancels a
     * new item that would land inside it. Loaded-from-disk entities and other levels are left alone.
     */
    public static void onJoin(EntityJoinLevelEvent event, ServerLevel level, AABB box) {
        if (!nodrop || box == null || level == null || event.getLevel() != level || event.loadedFromDisk()) return;
        if (!(event.getEntity() instanceof ItemEntity it) || !box.contains(it.position())) return;
        event.setCanceled(true);
        nodropCancelled++;
    }
}
