package net.hfstack.rallyguard.debug;

import net.hfstack.rallyguard.combat.GuardPack;
import net.hfstack.rallyguard.contract.PackRegistry;
import net.hfstack.rallyguard.contract.Stance;
import net.hfstack.rallyguard.contract.StanceRegistry;
import net.hfstack.rallyguard.formation.FormationShape;
import net.hfstack.rallyguard.order.MusterOrder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import tallestegg.guardvillagers.GuardEntityType;
import tallestegg.guardvillagers.common.entities.Guard;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * E1-F: {@code /rally sim <A|B|C|D|ALL> [seed] [repetitions=3]} - a baseline combat test
 * that runs in real time on a flat stone arena built 200 blocks up, 64 blocks east of the
 * player (so terrain, light and other mobs don't interfere):
 * <ul>
 *     <li>A: 20 recruits (iron swords) · B: 20 "veterans" (same kit, debug rank 3) ·
 *     C: 20 archers (bows, 64 arrows in the pack) · D: C + 1 captain (rank 5, sword).</li>
 *     <li>20 zombies (helmets, so daylight doesn't burn them) start 30 blocks away, each
 *     sent at a guard. Guards stand in a LINE, AGGRESSIVE stance, owned by a fixed
 *     simulation owner (so no player gets chat spam and nothing enters anyone's roster).</li>
 *     <li>A run ends when one side is gone or after 2 minutes. Metrics come from
 *     {@link SimProbe} plus a once-a-second sample: survivors, combat ticks, casualties,
 *     % formation kept, ammo spent, weapon swaps, mean distance to the nearest enemy,
 *     orders executed, shots and hits.</li>
 * </ul>
 * Repetitions are averaged (the AI isn't deterministic). Output: chat and
 * {@code rally-sim/<scenario>-<seed>.json} in the game directory; after the queue, a
 * comparison of every scenario run this session (A vs C vs B vs D). Until E2 the rank
 * changes nothing - that is the point of the baseline.
 */
public final class SimRunner {

    public static final UUID SIM_OWNER = UUID.fromString("5a1d0000-0000-4000-8000-000000000001");
    private static final int MAX_TICKS = 20 * 120;
    private static final int GUARDS = 20;
    private static final int ENEMIES = 20;
    private static final int W = 48;
    private static final int L = 48;

    private record Job(String scenario, long seed, int rep) {
    }

    private static final Deque<Job> QUEUE = new ArrayDeque<>();
    private static final Map<String, List<double[]>> RUNS = new LinkedHashMap<>();
    private static final Map<String, double[]> SESSION = new LinkedHashMap<>();
    static final String[] METRICS = {"survivors", "combatTicks", "casualties", "formationPct", "ammoSpent",
            "weaponSwaps", "meanEnemyDist", "orders", "shots", "hits"};

    private static ServerPlayer player;
    private static ServerLevel level;
    private static BlockPos origin;
    private static Job job;
    private static int tick;
    private static int combatStart = -1;
    private static double formationSum, distSum;
    private static int formationN, distN;
    private static final List<Guard> guards = new ArrayList<>();
    private static final List<Zombie> zombies = new ArrayList<>();

    public static boolean running() {
        return job != null || !QUEUE.isEmpty();
    }

    public static void start(ServerPlayer p, List<String> scenarios, long seed, int reps) {
        player = p;
        level = p.serverLevel();
        int y = Math.min(level.getMaxBuildHeight() - 8, 200);
        origin = new BlockPos(p.getBlockX() + 64, y, p.getBlockZ() - L / 2);
        buildArena();
        for (String s : scenarios) {
            RUNS.remove(s);
            for (int r = 0; r < reps; r++) QUEUE.add(new Job(s, seed, r));
        }
        say("gui.rallyguard.sim.started", String.join(", ", scenarios), reps, seed);
    }

    private static void buildArena() {
        for (int x = -1; x <= W; x++) {
            for (int z = -1; z <= L; z++) {
                boolean edge = x == -1 || z == -1 || x == W || z == L;
                level.setBlockAndUpdate(origin.offset(x, -1, z), Blocks.STONE.defaultBlockState());
                for (int h = 0; h < 4; h++) {
                    level.setBlockAndUpdate(origin.offset(x, h, z),
                            edge && h < 2 ? Blocks.STONE.defaultBlockState() : Blocks.AIR.defaultBlockState());
                }
            }
        }
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        if (job == null && QUEUE.isEmpty()) return;
        long t0 = Perf.t0();
        try {
            if (player == null || player.isRemoved()) {
                abort();
                return;
            }
            if (job == null) {
                job = QUEUE.poll();
                setup();
                return;
            }
            tick++;
            if (tick % 20 == 0) sample();
            long alive = guards.stream().filter(Entity::isAlive).count();
            long enemies = zombies.stream().filter(Entity::isAlive).count();
            if (alive == 0 || enemies == 0 || tick >= MAX_TICKS) finishRun(alive);
        } finally {
            Perf.add(Perf.Sys.SIM, t0);
        }
    }

    private static void setup() {
        Random rnd = new Random(job.seed() * 31L + job.rep());
        tick = 0;
        combatStart = -1;
        formationSum = distSum = 0;
        formationN = distN = 0;
        guards.clear();
        zombies.clear();
        SimProbe.begin();
        boolean archers = job.scenario().equals("C") || job.scenario().equals("D");
        int rank = job.scenario().equals("B") ? 3 : 0;
        for (int i = 0; i < GUARDS; i++) {
            Guard g = spawnGuard(new BlockPos(origin.getX() + 4 + i * 2, origin.getY(), origin.getZ() + 6),
                    archers ? new ItemStack(Items.BOW) : new ItemStack(Items.IRON_SWORD), rank);
            if (g != null && archers) GuardPack.of(level, g.getUUID()).setItem(0, new ItemStack(Items.ARROW, 64));
        }
        if (job.scenario().equals("D")) {
            spawnGuard(new BlockPos(origin.getX() + W / 2, origin.getY(), origin.getZ() + 3), new ItemStack(Items.IRON_SWORD), 5);
        }
        Vec3 anchor = new Vec3(origin.getX() + W / 2.0, origin.getY(), origin.getZ() + 6);
        new MusterOrder(anchor, 0.0F, FormationShape.LINE).execute(player, new ArrayList<>(guards));

        for (int i = 0; i < ENEMIES; i++) {
            BlockPos pos = new BlockPos(origin.getX() + 4 + i * 2 + rnd.nextInt(2), origin.getY(), origin.getZ() + 36 + rnd.nextInt(3));
            Zombie z = EntityType.ZOMBIE.spawn(level, pos, MobSpawnType.COMMAND);
            if (z == null) continue;
            z.setBaby(false);
            for (EquipmentSlot slot : EquipmentSlot.values()) z.setItemSlot(slot, ItemStack.EMPTY);
            z.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
            z.setDropChance(EquipmentSlot.HEAD, 0.0F);
            z.setPersistenceRequired();
            if (!guards.isEmpty()) z.setTarget(guards.get(i % guards.size()));
            zombies.add(z);
            SimProbe.ENEMIES.add(z.getUUID());
        }
    }

    private static Guard spawnGuard(BlockPos pos, ItemStack weapon, int rank) {
        Guard g = GuardEntityType.GUARD.get().spawn(level, pos, MobSpawnType.COMMAND);
        if (g == null) return null;
        for (EquipmentSlot slot : EquipmentSlot.values()) g.setItemSlot(slot, ItemStack.EMPTY);
        g.setItemSlot(EquipmentSlot.MAINHAND, weapon);
        g.setOwnerId(SIM_OWNER);
        g.setPersistenceRequired();
        StanceRegistry.get(level).setStance(g.getUUID(), Stance.AGGRESSIVE);
        DebugRanks.set(g.getUUID(), rank);
        guards.add(g);
        SimProbe.GUARDS.add(g.getUUID());
        return g;
    }

    private static void sample() {
        int kept = 0, alive = 0;
        for (Guard g : guards) {
            if (!g.isAlive()) continue;
            alive++;
            BlockPos post = g.isPatrolling() ? g.getPatrolPos() : null;
            if (post != null && g.position().distanceToSqr(Vec3.atBottomCenterOf(post)) <= 2.5 * 2.5) kept++;
            double best = Double.MAX_VALUE;
            for (Zombie z : zombies) if (z.isAlive()) best = Math.min(best, g.distanceToSqr(z));
            if (best < Double.MAX_VALUE) {
                distSum += Math.sqrt(best);
                distN++;
            }
            if (combatStart < 0 && g.getTarget() != null) combatStart = tick;
        }
        if (alive > 0) {
            formationSum += (double) kept / alive;
            formationN++;
        }
    }

    private static void finishRun(long survivors) {
        double[] m = new double[METRICS.length];
        m[0] = survivors;
        m[1] = combatStart < 0 ? 0 : tick - combatStart;
        m[2] = guards.size() - survivors;
        m[3] = formationN == 0 ? 0 : 100.0 * formationSum / formationN;
        m[4] = SimProbe.ammoItems;
        m[5] = SimProbe.weaponSwaps;
        m[6] = distN == 0 ? 0 : distSum / distN;
        m[7] = SimProbe.orders;
        m[8] = SimProbe.shots;
        m[9] = SimProbe.hits;
        RUNS.computeIfAbsent(job.scenario(), k -> new ArrayList<>()).add(m);
        say("gui.rallyguard.sim.run", job.scenario(), job.rep() + 1, (int) m[0], (int) m[2], (int) m[1], (int) m[8], (int) m[9]);
        cleanup();
        Job done = job;
        job = null;
        boolean lastOfScenario = QUEUE.stream().noneMatch(j -> j.scenario().equals(done.scenario()));
        if (lastOfScenario) writeScenario(done.scenario(), done.seed());
        if (QUEUE.isEmpty()) printComparison();
    }

    private static void cleanup() {
        SimProbe.end();
        for (Guard g : guards) {
            PackRegistry.get(level).remove(g.getUUID());
            DebugRanks.forget(g.getUUID());
            if (!g.isRemoved()) g.discard();
        }
        for (Zombie z : zombies) if (!z.isRemoved()) z.discard();
        AABB box = new AABB(origin.getX() - 2, origin.getY() - 2, origin.getZ() - 2,
                origin.getX() + W + 2, origin.getY() + 8, origin.getZ() + L + 2);
        for (Entity e : level.getEntitiesOfClass(Entity.class, box, e -> e instanceof ItemEntity || e instanceof AbstractArrow)) {
            e.discard();
        }
        guards.clear();
        zombies.clear();
    }

    private static void abort() {
        QUEUE.clear();
        if (job != null && level != null) cleanup();
        job = null;
    }

    private static double[] average(List<double[]> runs) {
        double[] avg = new double[METRICS.length];
        for (double[] r : runs) for (int i = 0; i < avg.length; i++) avg[i] += r[i] / runs.size();
        return avg;
    }

    private static void writeScenario(String scenario, long seed) {
        List<double[]> runs = RUNS.get(scenario);
        if (runs == null || runs.isEmpty()) return;
        double[] avg = average(runs);
        SESSION.put(scenario, avg);
        StringBuilder sb = new StringBuilder("{\n  \"scenario\": \"").append(scenario).append("\",\n  \"seed\": ")
                .append(seed).append(",\n  \"repetitions\": ").append(runs.size()).append(",\n  \"average\": {");
        for (int i = 0; i < METRICS.length; i++) {
            sb.append(i == 0 ? "\n" : ",\n").append("    \"").append(METRICS[i]).append("\": ")
                    .append(String.format(Locale.ROOT, "%.2f", avg[i]));
        }
        sb.append("\n  },\n  \"runs\": [");
        for (int r = 0; r < runs.size(); r++) {
            sb.append(r == 0 ? "\n    [" : ",\n    [");
            for (int i = 0; i < METRICS.length; i++) {
                sb.append(i == 0 ? "" : ", ").append(String.format(Locale.ROOT, "%.2f", runs.get(r)[i]));
            }
            sb.append("]");
        }
        sb.append("\n  ]\n}\n");
        try {
            Path dir = Paths.get("rally-sim");
            Files.createDirectories(dir);
            Path file = dir.resolve(scenario + "-" + seed + ".json");
            Files.writeString(file, sb.toString(), StandardCharsets.UTF_8);
            say("gui.rallyguard.sim.saved", file.toAbsolutePath().toString());
        } catch (IOException e) {
            say("gui.rallyguard.sim.save_failed", e.getMessage());
        }
    }

    private static void printComparison() {
        if (player == null) return;
        player.sendSystemMessage(Component.translatable("gui.rallyguard.sim.compare"));
        for (String s : new String[]{"A", "C", "B", "D"}) {
            double[] m = SESSION.get(s);
            if (m == null) continue;
            player.sendSystemMessage(Component.literal(String.format(Locale.ROOT,
                    "%s  surv %.1f  dead %.1f  combat %.0ft  form %.0f%%  dist %.1f  shots %.0f  hits %.0f  ammo %.0f",
                    s, m[0], m[2], m[1], m[3], m[6], m[8], m[9], m[4])));
        }
    }

    private static void say(String key, Object... args) {
        if (player != null) player.sendSystemMessage(Component.translatable(key, args));
    }
}
