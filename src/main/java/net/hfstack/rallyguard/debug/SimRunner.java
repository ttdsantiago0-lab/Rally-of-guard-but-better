package net.hfstack.rallyguard.debug;

import net.hfstack.rallyguard.combat.GuardPack;
import net.hfstack.rallyguard.command.CommandStructure;
import net.hfstack.rallyguard.command.SquadDirectives;
import net.hfstack.rallyguard.compat.GunAmmo;
import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.hfstack.rallyguard.contract.ForceRegistry;
import net.hfstack.rallyguard.contract.FormationRegistry;
import net.hfstack.rallyguard.contract.SquadRegistry;
import net.hfstack.rallyguard.contract.PackRegistry;
import net.hfstack.rallyguard.contract.Stance;
import net.hfstack.rallyguard.contract.StanceRegistry;
import net.hfstack.rallyguard.contract.VeterancyRegistry;
import net.hfstack.rallyguard.debug.sim.SimArena;
import net.hfstack.rallyguard.debug.sim.SimReport;
import net.hfstack.rallyguard.debug.sim.SimRun;
import net.hfstack.rallyguard.debug.sim.SimScenario;
import net.hfstack.rallyguard.formation.Formation;
import net.hfstack.rallyguard.formation.FormationShape;
import net.hfstack.rallyguard.order.CommandTargeting;
import net.hfstack.rallyguard.order.Engagement;
import net.hfstack.rallyguard.order.GuardModes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import tallestegg.guardvillagers.GuardEntityType;
import tallestegg.guardvillagers.common.entities.Guard;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * E2A block 3 - simulation v2, a learning platform that runs unattended
 * ({@code docs/SIMULACIONES.md}):
 * <pre>
 *   /rally sim batch &lt;preset|all&gt; [--minutes N] [--seeds N] [--tickrate sprint|normal]
 *   /rally sim stop
 *   /rally sim &lt;A|B|C|D|ALL|scenario id&gt; [seed] [repetitions]   (E1 aliases)
 * </pre>
 * The scenario matrix is data ({@link SimScenario}). Runs go one after another on a ground
 * arena ({@link SimArena}) built once per batch, away from the player and never over a
 * build. Each run: terrain, two sides spawned alternately in formation with IDENTICAL kit
 * and health (blue = simulation commander, red = a second simulation commander, or mobs
 * holding the same equipment), captains computed and directives pushed (block 1), 3 s to
 * settle, then both sides engage; a light driver only re-targets units left with no living
 * enemy, so the guards' own AI (Guard Villagers + Rally's gate, engagement cap, ladder)
 * decides the fight. A run ends when a side is gone, at the time cap, or when no damage
 * happens for {@code simulation.stuckSeconds} (stuck: retried once with another seed).
 * Everything spawned is removed after each run, and veterancy XP is never earned by
 * simulation units (their ranks stay as set). Output: {@link SimReport}.
 */
public final class SimRunner {

    public static final UUID SIM_OWNER = UUID.fromString("5a1d0000-0000-4000-8000-000000000001");
    public static final UUID SIM_RED = UUID.fromString("5a1d0000-0000-4000-8000-000000000002");
    private static final int FORM_TICKS = 60;
    private static final int BUILD_ROWS_PER_TICK = 4;

    private enum Phase { IDLE, BUILD, SETUP, FORM, FIGHT }

    private record Job(String scenario, long seed, int rep, boolean control, boolean retry) {
    }

    private static Phase phase = Phase.IDLE;
    private static ServerPlayer player;
    private static ServerLevel level;
    private static SimArena arena;
    private static SimScenario.Matrix matrix;
    private static final Deque<Job> QUEUE = new ArrayDeque<>();
    private static final List<SimRun> RUNS = new ArrayList<>();
    private static final List<String> NOTES = new ArrayList<>();
    private static String batchId, presetName, tickrate;
    private static int presetMinutes, minutesOverride;
    private static Path dir;
    private static boolean sprinting;

    // ==== Current run ====
    private static Job job;
    private static SimScenario scenario;
    private static int tick, capTicks, lastDamageTick;
    private static final List<List<LivingEntity>> UNITS = List.of(new ArrayList<>(), new ArrayList<>());
    private static final List<Entity> MOUNTS = new ArrayList<>();
    private static final List<Set<UUID>> LEADERS = List.of(new HashSet<>(), new HashSet<>());
    private static final int[] LEADER_DEATHS = new int[2];
    private static double cohesionSum, distSum, perfPeakMs;
    private static int cohesionN, distN;
    private static long perfStart;

    public static boolean running() {
        return phase != Phase.IDLE;
    }

    // ==== Starting ====

    /**
     * Starts a batch. {@code scenarioIds} non-null = exactly those scenarios (aliases);
     * otherwise the preset's. {@code minutes}/{@code seeds} 0 = the preset's. Returns an error
     * message, or null when started.
     */
    public static Component startBatch(ServerPlayer p, String preset, List<String> scenarioIds, long seedBase,
                                       int seeds, int minutes, String speed) {
        if (running()) return Component.translatable("gui.rallyguard.sim.busy");
        try {
            matrix = SimScenario.load(FMLPaths.CONFIGDIR.get().resolve("rallyguard-sim-scenarios.json"));
        } catch (IOException | RuntimeException e) {
            return Component.translatable("gui.rallyguard.sim.matrix_error", e.toString());
        }
        NOTES.clear();
        STRAYS.clear();
        RUNS.clear();
        QUEUE.clear();
        List<String> ids;
        int presetSeeds;
        if (scenarioIds != null) {
            ids = new ArrayList<>();
            for (String id : scenarioIds) {
                String key = id.toUpperCase(Locale.ROOT);
                if (key.equals("ALL")) ids.addAll(List.of("A", "B", "C", "D"));
                else if (matrix.scenarios().containsKey(key)) ids.add(key);
                else NOTES.add("Escenario desconocido: " + id);
            }
            presetName = "alias";
            presetSeeds = 1;
            presetMinutes = 2;
        } else {
            SimScenario.Preset pr = matrix.presets().get(preset.toLowerCase(Locale.ROOT));
            if (pr == null && preset.equalsIgnoreCase("all")) {
                pr = new SimScenario.Preset("all", new ArrayList<>(matrix.scenarios().keySet()), 2, 2);
            }
            if (pr == null) return Component.translatable("gui.rallyguard.sim.unknown_preset", preset,
                    String.join(", ", matrix.presets().keySet()));
            ids = new ArrayList<>();
            for (String id : pr.scenarios()) {
                if (matrix.scenarios().containsKey(id)) ids.add(id);
                else NOTES.add("El preset nombra un escenario que no existe: " + id);
            }
            presetName = pr.name();
            presetSeeds = pr.seeds();
            presetMinutes = pr.minutes();
        }
        if (ids.isEmpty()) return Component.translatable("gui.rallyguard.sim.bad_scenario");
        int nSeeds = seeds > 0 ? seeds : presetSeeds;
        minutesOverride = minutes;
        tickrate = speed == null || speed.isEmpty()
                ? RallyConfig.get(RallyConfig.SIM_TICKRATE::get, "normal") : speed.toLowerCase(Locale.ROOT);
        for (int s = 0; s < nSeeds; s++) {
            for (String id : ids) QUEUE.add(new Job(id, seedBase + s, s, false, false));
        }
        if ("sprint".equals(tickrate)) {
            Job first = QUEUE.peekFirst();
            QUEUE.addLast(new Job(first.scenario(), first.seed(), first.rep(), true, false));
        }

        player = p;
        level = p.serverLevel();
        arena = SimArena.find(level, p.blockPosition(), RallyConfig.i(RallyConfig.SIM_ARENA_SIZE, 96),
                RallyConfig.i(RallyConfig.SIM_ARENA_DISTANCE, 128), RallyConfig.get(RallyConfig.SIM_FLOOR_BLOCK::get, "minecraft:stone"));
        if (arena == null) {
            QUEUE.clear();
            return Component.translatable("gui.rallyguard.sim.no_site");
        }
        arena.forceChunks(true);
        batchId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + "-" + presetName;
        dir = Paths.get("rally-sim", batchId);
        if ("sprint".equals(tickrate)) {
            sprinting = level.getServer().tickRateManager().requestGameToSprint(20 * 60 * 60 * 12);
        }
        phase = Phase.BUILD;
        net.hfstack.rallyguard.tactics.DecisionTrace.forced = true; // V11: reasons are counted during batches
        say("gui.rallyguard.sim.batch_started", presetName, QUEUE.size(), arena.cx, arena.y, arena.cz, tickrate);
        return null;
    }

    public static void stop(String reason) {
        if (!running()) return;
        if (job != null) endRun(false, reason);
        QUEUE.clear();
        finishBatch();
    }

    // ==== Tick ====

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        if (phase == Phase.IDLE) return;
        long t0 = Perf.t0();
        try {
            if (player != null && player.isRemoved()) {
                // Respawn replaces the ServerPlayer object: follow the same player, stop only if they left.
                player = level.getServer().getPlayerList().getPlayer(player.getUUID());
            }
            if (player == null || player.hasDisconnected()) {
                stop("player left");
                return;
            }
            switch (phase) {
                case BUILD -> {
                    if (arena.buildStep(BUILD_ROWS_PER_TICK)) phase = Phase.SETUP;
                }
                case SETUP -> {
                    job = QUEUE.poll();
                    if (job == null) {
                        finishBatch();
                        return;
                    }
                    if (job.control() && sprinting) {
                        level.getServer().tickRateManager().stopSprinting();
                        sprinting = false;
                    }
                    setupRun();
                    phase = Phase.FORM;
                }
                case FORM -> {
                    tick++;
                    if (tick >= FORM_TICKS) {
                        kitCheck(); // what they really carry when the fight starts (fightTick begins at FORM_TICKS + 1)
                        engage();
                        phase = Phase.FIGHT;
                    }
                }
                case FIGHT -> fightTick();
                default -> {
                }
            }
        } catch (RuntimeException e) {
            com.mojang.logging.LogUtils.getLogger().warn("[rallyguard] simulation stopped after an error", e);
            NOTES.add("Error: " + e);
            stop("error");
        } finally {
            Perf.add(Perf.Sys.SIM, t0);
        }
    }

    /** E3A F9: a formation run is measured this long after the Muster order. */
    private static final int FORMATION_MEASURE_TICKS = 20 * 30;

    private static void fightTick() {
        tick++;
        if (net.hfstack.rallyguard.debug.sim.SimExtras.profileOnly(scenario)) { // E3B G2: measured at spawn
            endRun(false, "");
            return;
        }
        perfPeakMs = Math.max(perfPeakMs, Perf.lastTickNanos() / 1.0e6);
        if (scenario.tags().contains("precedence")) {
            precedenceTick();
            if (job == null) return; // precedenceTick already ended the run (was a NPE in endRun: job == null)
        }
        if (net.hfstack.rallyguard.debug.sim.SimExtras.tick(level, player, tick, FORM_TICKS, guardsOf(0))) { // E3B block S checks
            endRun(false, "");
            return;
        }
        if (scenario.formationTest()) {
            if (tick % 20 == 0) sample();
            if (tick >= FORM_TICKS + FORMATION_MEASURE_TICKS) {
                formationKept = formationKept();
                endRun(false, "");
            }
            return;
        }
        if (tick % 20 == 0) {
            adoptConverted();
            drive();
            sample();
        }
        if (!improv.isEmpty() && tick > FORM_TICKS && tick % 100 == 0) improvise();
        int blue = alive(0), red = alive(1);
        int stuckTicks = 20 * RallyConfig.i(RallyConfig.SIM_STUCK_SECONDS, 45);
        if (blue == 0 || red == 0 || tick >= capTicks) {
            endRun(false, "");
        } else if (tick - Math.max(lastDamageTick, FORM_TICKS) > stuckTicks && !scenario.tags().contains("nostuck")) {
            endRun(true, "");
        }
    }

    // ==== Run setup ====

    private static void setupRun() {
        scenario = matrix.scenarios().get(job.scenario());
        int minutes = minutesOverride > 0 ? minutesOverride : scenario.minutes() > 0 ? scenario.minutes() : presetMinutes;
        capTicks = 20 * 60 * Math.max(1, minutes);
        tick = 0;
        lastDamageTick = 0;
        cohesionSum = distSum = perfPeakMs = 0;
        cohesionN = distN = 0;
        LEADER_DEATHS[0] = LEADER_DEATHS[1] = 0;
        for (List<LivingEntity> u : UNITS) u.clear();
        for (Set<UUID> l : LEADERS) l.clear();
        MOUNTS.clear();
        SimProbe.begin();
        net.hfstack.rallyguard.debug.sim.SimExtras.begin(scenario); // E3B block S
        SimProbe.allowXp = scenario.pace(); // E3A V8: only pace scenarios earn XP
        String gvNote = net.hfstack.rallyguard.debug.sim.SimGvConfig.apply(scenario); // torneo: gv:creepers
        if (!gvNote.isEmpty() && !NOTES.contains(gvNote)) NOTES.add(gvNote);
        net.hfstack.rallyguard.tactics.DecisionTrace.resetCounts();
        DEATH_TICK.clear();
        firstBlueDamage = -1;
        pursuitSum = 0;
        pursuitN = 0;
        perfStart = Perf.allNanos();

        int half = arena.half();
        int d = Math.max(6, Math.min(scenario.distance(), arena.size - 16));
        int bx = arena.cx, bz = arena.cz + d / 2;
        float blueYaw = 180.0F;
        int rx, rz;
        float redYaw;
        if ("flank".equals(scenario.position())) {
            rx = arena.cx + Math.min(d / 2 + 8, half - 4);
            rz = bz;
            redYaw = 90.0F;
        } else if ("rear".equals(scenario.position())) { // E3B M: red behind blue (blue faces away from it)
            rx = arena.cx;
            rz = Math.min(bz + 12, arena.cz + half - 4);
            redYaw = 180.0F;
        } else {
            rx = arena.cx;
            rz = arena.cz - d / 2;
            redYaw = 0.0F;
        }
        String terrain = "height".equals(scenario.position()) ? "hill" : scenario.terrain();
        arena.terrain(terrain, bx, bz, rx, rz, job.seed());

        List<Vec3> bluePos = slots(scenario.blue(), new Vec3(bx + 0.5, arena.y, bz + 0.5), blueYaw);
        List<Vec3> redPos = slots(scenario.red(), new Vec3(rx + 0.5, arena.y, rz + 0.5), redYaw);
        int n = Math.max(bluePos.size(), redPos.size());
        for (int i = 0; i < n; i++) { // alternate sides AND who goes first: no side gets the earlier AI turns
            int first = (i + (int) (job.seed() & 1L)) % 2;
            for (int k = 0; k < 2; k++) {
                int side = (first + k) % 2;
                List<Vec3> pos = side == 0 ? bluePos : redPos;
                if (i < pos.size()) spawnUnit(side, side == 0 ? scenario.blue() : scenario.red(), i, pos.get(i),
                        side == 0 ? blueYaw : redYaw);
            }
        }
        if (scenario.formationTest()) { // E3A F9: scattered blue, then one Muster in the scenario's shape
            for (LivingEntity e : UNITS.get(0)) {
                double a = level.getRandom().nextDouble() * Math.PI * 2, r = 4 + level.getRandom().nextDouble() * 14;
                int x = arena.clampX(bx + Math.cos(a) * r), z = arena.clampZ(bz + Math.sin(a) * r);
                e.teleportTo(x + 0.5, arena.standY(x, z), z + 0.5);
            }
            FormationShape shape;
            try {
                shape = FormationShape.valueOf(scenario.blue().formation().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                shape = FormationShape.V;
            }
            new net.hfstack.rallyguard.order.MusterOrder(new Vec3(arena.cx + 0.5, arena.y, arena.cz + 0.5), 180.0F, shape)
                    .execute(player, guardsOf(0));
            return;
        }
        if (scenario.blue().leader()) spawnLeader(0, scenario.blue(), bx, bz + 4, blueYaw);
        if (scenario.red().leader()) spawnLeader(1, scenario.red(), rx, rz - 4, redYaw);
        for (int side = 0; side < 2; side++) organize(side);
        if (scenario.tags().contains("force")) {
            setupForces(new Vec3(bx + 0.5, arena.y, bz + 0.5), blueYaw, new Vec3(rx + 0.5, arena.y, rz + 0.5), redYaw);
        }
        // E3B block S: group profile, instructions (and later systems) on the organized sides.
        net.hfstack.rallyguard.debug.sim.SimExtras.setup(level, SIM_OWNER, guardsOf(0), new Vec3(bx + 0.5, arena.y, bz + 0.5), blueYaw);
        // E3A A9: the improvisation lab - half the runs of a "lab" scenario try one off-script action.
        java.util.Random labRnd = new java.util.Random(job.seed() * 7919L + RUNS.size());
        improv = scenario.tags().contains("lab") && labRnd.nextBoolean() ? LAB_ACTIONS[labRnd.nextInt(LAB_ACTIONS.length)] : "";
        improvCount = 0;
    }

    private static List<Vec3> slots(SimScenario.Side cfg, Vec3 anchor, float yaw) {
        int max = RallyConfig.i(RallyConfig.SIM_MAX_PER_SIDE, 100);
        int count = cfg.count();
        if (count > max) {
            NOTES.add(scenario.id() + ": bando recortado de " + count + " a " + max + " (simulation.maxUnitsPerSide)");
            count = max;
        }
        FormationShape shape;
        try {
            shape = FormationShape.valueOf(cfg.formation().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            shape = FormationShape.LINE;
        }
        return new Formation(shape, Formation.DEFAULT_SPACING).positions(anchor, yaw, count);
    }

    private static void spawnLeader(int side, SimScenario.Side cfg, int x, int z, float yaw) {
        int rank = cfg.leaderRank() >= 0 ? Math.min(5, cfg.leaderRank()) : Math.min(5, Math.max(cfg.rank() + 2, 4));
        SimScenario.Side leader = new SimScenario.Side("guard", 1, "melee".equals(cfg.role()) || "mixed".equals(cfg.role())
                ? "melee" : cfg.role(), rank, false, cfg.formation(), cfg.mounted(), cfg.fireMode(), -1);
        LivingEntity e = spawnUnit(side, leader, 0, new Vec3(arena.clampX(x) + 0.5, arena.y, arena.clampZ(z) + 0.5), yaw);
        if (e != null) LEADERS.get(side).add(e.getUUID());
    }

    private static LivingEntity spawnUnit(int side, SimScenario.Side cfg, int index, Vec3 at, float yaw) {
        int x = arena.clampX(at.x), z = arena.clampZ(at.z);
        BlockPos pos = new BlockPos(x, arena.standY(x, z), z);
        // E3B G2: "random" = a scrambled group (random kit, 1 in 3 mounted) for the group-profile tests.
        String[] kit = "random".equals(cfg.role()) ? net.hfstack.rallyguard.debug.sim.SimExtras.randomKit(job.seed(), index) : null;
        String[] mix = cfg.role().startsWith("mix:") ? mixRole(cfg.role(), index) : null; // E3-COORD torneo
        if (mix != null) kit = mix;
        String role = kit != null ? kit[0] : "mixed".equals(cfg.role()) ? (index % 2 == 0 ? "melee" : "archer") : cfg.role();
        ItemStack weapon = weaponFor(role);
        LivingEntity unit;
        if (cfg.guards()) {
            // 2026-11-04: NOT EntityType#spawn. Guard Villagers' finalizeSpawn sets spawnWithArmor and the guard's
            // first aiStep then rolls its loot table (iron sword OR crossbow in the main hand, a random armour set,
            // a shield ~47 % of the time) over whatever kit we give it here - every torneo composition up to now
            // was that random mix ("archers" with no bow at all). Created without finalizeSpawn, the kit stays.
            Guard g = GuardEntityType.GUARD.get().create(level);
            if (g == null) return null;
            g.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, yaw, 0.0F);
            g.setYHeadRot(yaw);
            if (!level.addFreshEntity(g)) return null;
            equip(g, weapon);
            if ("shield".equals(role) || "heavy".equals(role)) { // E3B: heavy infantry kit (full iron)
                // torneo 2026-11-04: "heavy" = the same armour WITHOUT the shield, so a duel can tell what the
                // shield itself is worth (before, shield vs melee also changed legs and boots).
                if ("shield".equals(role)) g.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
                g.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                g.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
            }
            g.setOwnerId(side == 0 ? SIM_OWNER : SIM_RED);
            g.setPersistenceRequired();
            StanceRegistry.get(level).setStance(g.getUUID(), Stance.AGGRESSIVE);
            DebugRanks.set(g.getUUID(), cfg.rank());
            if (!"NORMAL".equalsIgnoreCase(cfg.fireMode())) {
                DirectiveRegistry.get(level).setFireMode(g.getUUID(), cfg.fireMode().toUpperCase(Locale.ROOT));
            }
            Item ammo = ammoFor(weapon);
            if (ammo != null) {
                GuardPack.of(level, g.getUUID()).setItem(0, new ItemStack(ammo, 64));
                GuardPack.of(level, g.getUUID()).setItem(1, new ItemStack(ammo, 64));
            }
            if (cfg.mounted() || (kit != null && "mounted".equals(kit[1]))) mount(side, g, pos);
            // Torneo, comer y rotar (David 2026-11-04): blue may carry food. "food:offhand" = bread in the off hand,
            // which Guard Villagers' own GuardRunToEatGoal/GuardEatFoodGoal use (step away from the attacker, eat,
            // heal) - only where the off hand is free (no shield); "food:pack" = bread in Rally's pack, eaten in place
            // below 40 % health (GuardPack.consume).
            if (side == 0 && scenario.tags().contains("food:offhand") && g.getOffhandItem().isEmpty()) {
                g.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.BREAD, 16));
            }
            if (side == 0 && scenario.tags().contains("food:pack")) {
                GuardPack.of(level, g.getUUID()).setItem(2, new ItemStack(Items.BREAD, 16));
            }
            unit = g;
        } else {
            String id = cfg.kind().contains(":") ? cfg.kind() : "minecraft:" + cfg.kind();
            EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.tryParse(id));
            Entity e = type.spawn(level, pos, MobSpawnType.COMMAND);
            if (!(e instanceof Mob mob)) {
                if (e != null) e.discard();
                NOTES.add(scenario.id() + ": no se pudo crear " + id);
                return null;
            }
            boolean boss = id.endsWith("iron_golem") || id.endsWith("ravager");
            // E3-COORD torneo: role "native" = the mob exactly as the game spawns it (its own gear, health, damage)
            if (!boss && !"native".equals(role)) {
                equip(mob, weapon);
                matchGuardBase(mob); // E3A F7: same base health, punch and armor as a guard
                mob.setHealth(mob.getMaxHealth());
            }
            if (mob instanceof Zombie zombie) zombie.setBaby(false);
            mob.setPersistenceRequired();
            if (cfg.mounted() && index == 0) NOTES.add(scenario.id() + ": los mobs no se montan (solo guardias)");
            unit = mob;
        }
        unit.setYRot(yaw);
        unit.setYHeadRot(yaw);
        UNITS.get(side).add(unit);
        SimProbe.SIDE.put(unit.getUUID(), side);
        return unit;
    }

    /**
     * E3A F7 (David: 5 zombies killed 5 guards "with the same kit"): the kit was the same but
     * the BASE attributes were not - a zombie hits for 3 + weapon against a guard's 1 +
     * weapon, has 2 natural armor and can call reinforcements. Each non-boss mob now takes
     * the guard's base max health, attack damage, armor, toughness and knockback resistance
     * (from Guard Villagers' default attributes), and zombie reinforcements are off. Speed is
     * left as each mob's own (noted in the summary).
     */
    private static void matchGuardBase(Mob mob) {
        net.minecraft.world.entity.ai.attributes.AttributeSupplier guardBase =
                net.minecraft.world.entity.ai.attributes.DefaultAttributes.getSupplier(GuardEntityType.GUARD.get());
        for (net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> a : List.of(
                Attributes.MAX_HEALTH, Attributes.ATTACK_DAMAGE, Attributes.ARMOR, Attributes.ARMOR_TOUGHNESS,
                Attributes.KNOCKBACK_RESISTANCE)) {
            AttributeInstance inst = mob.getAttribute(a);
            if (inst != null && guardBase.hasAttribute(a)) inst.setBaseValue(guardBase.getBaseValue(a));
            else if (inst != null && a != Attributes.MAX_HEALTH) inst.setBaseValue(0.0D);
        }
        AttributeInstance reinforcements = mob.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
        if (reinforcements != null) reinforcements.setBaseValue(0.0D);
    }

    /**
     * E3-COORD torneo: a composition {@code mix:shield=4,archer=4,melee@m=2} (counts are proportions; {@code @m} =
     * mounted). Guard {@code index} gets the entry its position falls in, cycling over the total, so any side size
     * keeps the proportions. Returns {role, "mounted"|"foot"}.
     */
    private static String[] mixRole(String spec, int index) {
        String[] parts = spec.substring(4).split(",");
        int total = 0;
        int[] counts = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String[] kv = parts[i].trim().split("=");
            try {
                counts[i] = kv.length > 1 ? Math.max(1, Integer.parseInt(kv[1].trim())) : 1;
            } catch (NumberFormatException e) {
                counts[i] = 1;
            }
            total += counts[i];
        }
        int k = index % Math.max(1, total);
        for (int i = 0; i < parts.length; i++) {
            if (k < counts[i]) {
                String r = parts[i].trim().split("=")[0].trim();
                boolean mounted = r.endsWith("@m");
                return new String[]{mounted ? r.substring(0, r.length() - 2) : r, mounted ? "mounted" : "foot"};
            }
            k -= counts[i];
        }
        return new String[]{"melee", "foot"};
    }

    private static ItemStack weaponFor(String role) {
        return switch (role) {
            case "archer" -> new ItemStack(Items.BOW);
            case "crossbow" -> new ItemStack(Items.CROSSBOW);
            case "axe" -> new ItemStack(Items.IRON_AXE);
            case "spear" -> new ItemStack(Items.TRIDENT);
            case "fist" -> ItemStack.EMPTY;
            case "gunner" -> {
                String id = RallyConfig.get(RallyConfig.SIM_GUN_ITEM::get, "");
                Item gun = id.isEmpty() ? Items.AIR : BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(id));
                if (gun == Items.AIR) {
                    if (!NOTES.contains(NO_GUN)) NOTES.add(NO_GUN);
                    yield new ItemStack(Items.CROSSBOW);
                }
                yield new ItemStack(gun);
            }
            default -> new ItemStack(Items.IRON_SWORD);
        };
    }

    private static final String NO_GUN = "Rol 'gunner': simulation.gunItem vacío o desconocido; se usó ballesta.";

    private static Item ammoFor(ItemStack weapon) {
        if (weapon.is(Items.BOW) || weapon.is(Items.CROSSBOW)) return Items.ARROW;
        try {
            return GunAmmo.ammoFor(weapon);
        } catch (RuntimeException e) {
            return null;
        }
    }

    /** Same kit on both sides: the weapon, an iron helmet and chestplate, nothing dropped. */
    private static void equip(Mob mob, ItemStack weapon) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            mob.setItemSlot(slot, ItemStack.EMPTY);
            mob.setDropChance(slot, 0.0F);
        }
        mob.setItemSlot(EquipmentSlot.MAINHAND, weapon);
        mob.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        mob.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
    }

    private static void mount(int side, Guard guard, BlockPos pos) {
        Horse horse = EntityType.HORSE.spawn(level, pos, MobSpawnType.COMMAND);
        if (horse == null) return;
        horse.setTamed(true);
        horse.equipSaddle(new ItemStack(Items.SADDLE), null);
        horse.setPersistenceRequired();
        guard.startRiding(horse, true);
        MOUNTS.add(horse);
    }

    /** Block 1 on each side: subunits, computed captains (counted as leaders), MUSTER directives; posted where they stand. */
    private static void organize(int side) {
        List<Guard> guards = guardsOf(side);
        if (guards.isEmpty()) return;
        long now = level.getGameTime();
        List<CommandStructure.Subunit> units = CommandStructure.partition(guards);
        for (int k = 0; k < units.size(); k++) {
            CommandStructure.Subunit u = units.get(k);
            LEADERS.get(side).add(u.captain().getUUID());
            SquadDirectives.push(u.members(), new SquadDirectives.Directive(SquadDirectives.Kind.MUSTER,
                    u.captain().position(), u.captain().getYRot(), u.captain().getUUID(), now, k, units.size()));
        }
        for (Guard g : guards) GuardModes.postHere(g);
    }

    private static List<Guard> guardsOf(int side) {
        List<Guard> out = new ArrayList<>();
        for (LivingEntity e : UNITS.get(side)) if (e instanceof Guard g && g.isAlive()) out.add(g);
        return out;
    }

    // ==== Fight ====

    /** Share of blue guards within 2.5 blocks of the position of their formation slot. */
    private static double formationKept() {
        FormationRegistry formations = FormationRegistry.get(level);
        int n = 0, kept = 0;
        for (Guard g : guardsOf(0)) {
            FormationRegistry.Slot slot = formations.slotOf(g.getUUID());
            if (slot == null) continue;
            n++;
            List<Vec3> pos = slot.formation().positions(slot.anchor(), slot.facingYaw(), slot.count());
            if (slot.index() < pos.size() && g.position().distanceToSqr(pos.get(slot.index())) <= 2.5 * 2.5) kept++;
        }
        return n == 0 ? 0 : (double) kept / n;
    }

    private static double formationKept = -1;

    // ==== E3A A2: precedence scenario (player order vs force directive) ====
    public static final String SIM_SQUAD = "SimBlue";
    private static final List<Guard> PLAYER_HALF = new ArrayList<>(), FORCE_HALF = new ArrayList<>();
    private static double precedenceOk = -1;

    /**
     * At +2 s of the fight, half of blue gets an explicit player order (Hold); then the force
     * issues one versioned unit directive (Hold at a far point) to the whole unit. At +7 s:
     * the player's half must not have carried out the force directive, the other half must.
     */
    private static void precedenceTick() {
        int start = FORM_TICKS + 40;
        if (tick == start) {
            List<Guard> blue = guardsOf(0);
            SquadRegistry squads = SquadRegistry.get(level);
            ForceRegistry.get(level).create(SIM_OWNER, SIM_SQUAD);
            PLAYER_HALF.clear();
            FORCE_HALF.clear();
            for (int i = 0; i < blue.size(); i++) {
                squads.assign(blue.get(i).getUUID(), SIM_OWNER, SIM_SQUAD);
                (i % 2 == 0 ? PLAYER_HALF : FORCE_HALF).add(blue.get(i));
            }
            // E3-COORD M2: engage() gave the whole side an ATTACK through the player's channel at the start, which
            // muted the force directive for EVERYONE for playerOverrideSeconds - the test then measured that, not
            // precedence. Only PLAYER_HALF may carry a fresh player order here.
            net.hfstack.rallyguard.force.UnitOrders.forgetPlayerOrder(FORCE_HALF);
            SquadDirectives.pushOrder(SquadDirectives.Kind.HOLD, PLAYER_HALF, level.getGameTime());
            Vec3 far = new Vec3(arena.cx + 0.5, arena.y, arena.cz + arena.half() - 6);
            net.hfstack.rallyguard.force.UnitOrders.issue(level, net.hfstack.rallyguard.force.UnitOrders.unitKey(SIM_OWNER, SIM_SQUAD),
                    net.hfstack.rallyguard.force.UnitOrders.Kind.HOLD, net.hfstack.rallyguard.force.UnitOrders.Source.FORCE,
                    far, 180.0F, FormationShape.LINE, null, 0, "sim precedence");
        } else if (tick == start + 140) {
            int wrong = 0, reached = 0;
            for (Guard g : PLAYER_HALF) if (net.hfstack.rallyguard.force.UnitOrders.appliedVersion(g.getUUID()) > 0) wrong++;
            for (Guard g : FORCE_HALF) if (!g.isAlive() || net.hfstack.rallyguard.force.UnitOrders.appliedVersion(g.getUUID()) > 0) reached++;
            precedenceOk = wrong == 0 && reached >= FORCE_HALF.size() * 0.8D ? 1 : 0;
            if (precedenceOk < 1) { // E3-COORD M2: say WHY the directive didn't reach them, guard by guard
                String expected = net.hfstack.rallyguard.force.UnitOrders.unitKey(SIM_OWNER, SIM_SQUAD);
                java.util.Map<String, Integer> why = new java.util.TreeMap<>();
                for (Guard g : FORCE_HALF) {
                    if (!g.isAlive()) continue;
                    String key = net.hfstack.rallyguard.force.UnitOrders.unitKeyOf(level, g);
                    String r;
                    if (key == null) r = "sin-unidad(owner=" + (net.hfstack.rallyguard.contract.GuardOwnership.getOwner(g) == null ? "null" : "ok") + ")";
                    else if (!key.equals(expected)) r = "otra-unidad:" + key.substring(key.indexOf('#') + 1);
                    else if (net.hfstack.rallyguard.force.UnitOrders.current(key) == null) r = "sin-directiva";
                    else if (net.hfstack.rallyguard.force.UnitOrders.underPlayerOrder(g)) r = "bajo-orden-del-jugador";
                    else r = "aplicada-v" + net.hfstack.rallyguard.force.UnitOrders.appliedVersion(g.getUUID());
                    why.merge(r, 1, Integer::sum);
                }
                NOTES.add("Precedencia, por que no llego la directiva al resto: " + why);
            }
            NOTES.add(String.format(Locale.ROOT, "Precedencia: %d/%d con orden del jugador obedecieron la directiva de fuerza (debe ser 0); "
                    + "%d/%d del resto la recibieron", wrong, PLAYER_HALF.size(), reached, FORCE_HALF.size()));
            endRun(false, "");
        }
    }

    // ==== E3A A9: force scenarios and the improvisation lab ====
    private static final String SIM_RED_SQUAD = "SimRed";
    /** Provisional names of the lab's off-script actions (never promoted automatically). */
    private static final String[] LAB_ACTIONS = {"flanco-corto", "retirada-fingida", "foco-mas-debil", "dispersarse"};
    private static String improv = "";
    private static int improvCount;

    /**
     * A "force" scenario: each guard side becomes a squad of units of {@code unit:N} guards
     * (default 8) under a real force (ForceRegistry) - doctrine from the tag {@code doctrine:X}
     * (red: {@code rdoctrine:X}), an operation from {@code op:TYPE} (red: {@code rop:TYPE}). The
     * force brain then runs it exactly as in play (roles, rules, operation, captains).
     */
    private static void setupForces(Vec3 blueAt, float blueYaw, Vec3 redAt, float redYaw) {
        int size = 8;
        try {
            if (!scenario.tag("unit").isEmpty()) size = Math.max(2, Integer.parseInt(scenario.tag("unit")));
        } catch (NumberFormatException ignored) {
        }
        SquadRegistry squads = SquadRegistry.get(level);
        ForceRegistry registry = ForceRegistry.get(level);
        for (int side = 0; side < 2; side++) {
            List<Guard> guards = guardsOf(side);
            if (guards.isEmpty()) continue;
            UUID owner = side == 0 ? SIM_OWNER : SIM_RED;
            String squad = side == 0 ? SIM_SQUAD : SIM_RED_SQUAD;
            ForceRegistry.Force force = registry.create(owner, squad);
            String doctrine = scenario.tag(side == 0 ? "doctrine" : "rdoctrine");
            if (!doctrine.isEmpty()) force.doctrine = doctrine.toUpperCase(Locale.ROOT);
            for (int i = 0; i < guards.size(); i++) {
                squads.assign(guards.get(i).getUUID(), owner, squad + SquadRegistry.PATH_SEPARATOR + "U" + (i / size + 1));
            }
            registry.changed();
            String op = scenario.tag(side == 0 ? "op" : "rop");
            if (op.isEmpty()) continue;
            try {
                net.hfstack.rallyguard.force.Operations.start(level, force,
                        net.hfstack.rallyguard.force.Operations.Type.valueOf(op.toUpperCase(Locale.ROOT)),
                        side == 0 ? blueAt : redAt, side == 0 ? blueYaw : redYaw);
            } catch (IllegalArgumentException e) {
                NOTES.add(scenario.id() + ": operación desconocida " + op);
            }
        }
    }

    /** Every 5 s each blue guard tries the run's off-script action with the configured chance. */
    private static void improvise() {
        double chance = RallyConfig.d(RallyConfig.SIM_LAB_CHANCE, 0.1D);
        List<LivingEntity> enemies = new ArrayList<>();
        double ex = 0, ez = 0;
        for (LivingEntity e : UNITS.get(1)) {
            if (!e.isAlive()) continue;
            enemies.add(e);
            ex += e.getX();
            ez += e.getZ();
        }
        if (enemies.isEmpty()) return;
        Vec3 enemy = new Vec3(ex / enemies.size(), arena.y, ez / enemies.size());
        for (Guard g : guardsOf(0)) {
            if (level.getRandom().nextDouble() >= chance) continue;
            Vec3 away = g.position().subtract(enemy).multiply(1, 0, 1);
            if (away.lengthSqr() < 1.0E-4) continue;
            away = away.normalize();
            Vec3 to = null;
            switch (improv) {
                case "flanco-corto" -> to = g.position().add(new Vec3(-away.z, 0, away.x).scale(6));
                case "retirada-fingida" -> to = g.position().add(away.scale(8));
                case "dispersarse" -> to = g.position().add((level.getRandom().nextDouble() - 0.5) * 6, 0,
                        (level.getRandom().nextDouble() - 0.5) * 6);
                case "foco-mas-debil" -> {
                    LivingEntity weakest = null;
                    for (LivingEntity e : enemies) {
                        if (e.distanceToSqr(g) <= 24 * 24 && (weakest == null || e.getHealth() < weakest.getHealth())) weakest = e;
                    }
                    if (weakest != null && (Engagement.isRanged(g) || Engagement.tryEngage(g, weakest))) {
                        CommandTargeting.issue(g, weakest);
                        improvCount++;
                    }
                }
                default -> {
                }
            }
            if (to != null) {
                int x = arena.clampX(to.x), z = arena.clampZ(to.z);
                g.getNavigation().moveTo(x + 0.5, arena.standY(x, z), z + 0.5, 1.1D);
                improvCount++;
            }
        }
    }

    private static void engage() {
        if (scenario.formationTest()) return;
        long now = level.getGameTime();
        for (int side = 0; side < 2; side++) {
            if (side == 0 && net.hfstack.rallyguard.debug.sim.SimExtras.blueInstructed()) continue; // E3B G3: the instruction decides
            List<Guard> guards = guardsOf(side);
            for (Guard g : guards) GuardModes.free(g); // both sides advance on the enemy
            if (!guards.isEmpty()) SquadDirectives.pushOrder(SquadDirectives.Kind.ATTACK, guards, now);
        }
        drive();
    }

    /** Hands a living enemy to every unit left without one (never overrides a retreat or a dry gunner). */
    private static void drive() {
        for (int side = 0; side < 2; side++) {
            if (side == 0 && net.hfstack.rallyguard.debug.sim.SimExtras.blueInstructed()) continue; // E3B G3
            if (side == 1 && net.hfstack.rallyguard.debug.sim.SimExtras.redHeld()) continue; // E3B P2: an ambush waits
            List<LivingEntity> enemies = UNITS.get(1 - side);
            for (LivingEntity u : UNITS.get(side)) {
                if (!u.isAlive() || !(u instanceof Mob mob)) continue;
                LivingEntity current = mob.getTarget();
                if (current != null && current.isAlive() && SimProbe.sideOf(current) == 1 - side) continue;
                if (u instanceof Guard g) {
                    SquadDirectives.Directive d = SquadDirectives.of(g.getUUID());
                    if (d != null && (d.kind() == SquadDirectives.Kind.RETREAT || d.kind() == SquadDirectives.Kind.ROUTED)) continue;
                    if (GuardPack.isOutOfAmmo(g) || net.hfstack.rallyguard.tactics.Tactics.isRetreating(g)) continue;
                    List<LivingEntity> near = nearest(u, enemies, 3);
                    for (LivingEntity c : near) {
                        if (Engagement.isRanged(g) || Engagement.tryEngage(g, c)) {
                            CommandTargeting.issue(g, c);
                            break;
                        }
                    }
                } else {
                    List<LivingEntity> near = nearest(u, enemies, 1);
                    if (!near.isEmpty()) mob.setTarget(near.get(0));
                }
            }
        }
    }

    private static List<LivingEntity> nearest(LivingEntity from, List<LivingEntity> pool, int k) {
        List<LivingEntity> alive = new ArrayList<>();
        for (LivingEntity e : pool) if (e.isAlive()) alive.add(e);
        alive.sort((a, b) -> Double.compare(a.distanceToSqr(from), b.distanceToSqr(from)));
        return alive.size() > k ? alive.subList(0, k) : alive;
    }

    /** E3A V9 run state: death tick per unit, first blue hit, pursuit beyond the leash. */
    private static final java.util.Map<UUID, Integer> DEATH_TICK = new java.util.HashMap<>();
    private static int firstBlueDamage = -1;
    private static double pursuitSum;
    private static int pursuitN;

    /** 2026-11-04: what each blue guard really carries once the fight starts ("kit:" counters in every run), so a
     *  kit silently replaced by another mod can never again pass for the composition the scenario asked for. */
    private static void kitCheck() {
        for (Guard g : guardsOf(0)) {
            SimProbe.count(g, "kit:" + BuiltInRegistries.ITEM.getKey(g.getMainHandItem().getItem()).getPath());
            if (g.getOffhandItem().is(Items.SHIELD)) SimProbe.count(g, "kit:shield");
            if (g.getOffhandItem().is(Items.BREAD)) SimProbe.count(g, "kit:foodOffhand");
            if (g.getItemBySlot(EquipmentSlot.LEGS).is(Items.IRON_LEGGINGS)) SimProbe.count(g, "kit:ironLegs");
        }
    }

    private static void sample() {
        List<LivingEntity> blue = new ArrayList<>();
        for (LivingEntity e : UNITS.get(0)) if (e.isAlive()) blue.add(e);
        if (blue.isEmpty()) return;
        for (LivingEntity e : blue) { // V9: unnecessary pursuit = metres beyond the post leash
            if (!(e instanceof Guard g)) continue;
            BlockPos post = CommandTargeting.postOf(g);
            if (post == null) continue;
            double over = Math.sqrt(g.distanceToSqr(Vec3.atCenterOf(post))) - CommandTargeting.leashFor(g);
            pursuitSum += Math.max(0.0D, over);
            pursuitN++;
        }
        double cx = 0, cz = 0;
        for (LivingEntity e : blue) {
            cx += e.getX();
            cz += e.getZ();
        }
        cx /= blue.size();
        cz /= blue.size();
        double spread = 0;
        for (LivingEntity e : blue) {
            spread += Math.sqrt((e.getX() - cx) * (e.getX() - cx) + (e.getZ() - cz) * (e.getZ() - cz));
            double best = Double.MAX_VALUE;
            for (LivingEntity r : UNITS.get(1)) if (r.isAlive()) best = Math.min(best, e.distanceToSqr(r));
            if (best < Double.MAX_VALUE) {
                distSum += Math.sqrt(best);
                distN++;
            }
        }
        cohesionSum += spread / blue.size();
        cohesionN++;
        net.hfstack.rallyguard.debug.sim.SimExtras.sample(UNITS.get(0), UNITS.get(1)); // E3B block S
    }

    private static int alive(int side) {
        int n = 0;
        for (LivingEntity e : UNITS.get(side)) if (e.isAlive() && !e.isRemoved()) n++;
        return n;
    }

    // ==== Ending a run ====

    private static void endRun(boolean stuck, String reason) {
        if (job == null) return; // ended earlier this tick - never record a run twice
        SimRun r = new SimRun();
        r.batch = batchId;
        r.preset = presetName;
        r.scenario = scenario == null ? job.scenario() : scenario.id();
        r.seed = job.seed();
        r.rep = job.rep();
        r.control = job.control();
        r.retried = job.retry();
        r.tickrate = job.control() ? "normal" : tickrate;
        r.capTicks = capTicks;
        r.ticks = tick;
        r.stuck = stuck;
        r.timeout = tick >= capTicks;
        for (int s = 0; s < 2; s++) {
            r.start[s] = UNITS.get(s).size();
            r.survivors[s] = alive(s);
            double h = 0;
            for (LivingEntity e : UNITS.get(s)) if (e.isAlive()) h += e.getHealth();
            r.healthLeft[s] = h;
            r.damage[s] = SimProbe.damage[s];
            r.shots[s] = SimProbe.shots[s];
            r.hits[s] = SimProbe.hits[s];
            r.ammo[s] = SimProbe.ammoItems[s];
            r.swaps[s] = SimProbe.weaponSwaps[s];
            r.orders[s] = SimProbe.orders[s];
            r.retreats[s] = SimProbe.retreats[s];
            r.drowned[s] = SimProbe.drowned[s];
        }
        r.leaderDeathsBlue = LEADER_DEATHS[0];
        r.leaderDeathsRed = LEADER_DEATHS[1];
        r.winner = r.survivors[0] > 0 && r.survivors[1] == 0 ? "blue"
                : r.survivors[1] > 0 && r.survivors[0] == 0 ? "red" : "draw";
        r.ladder = SimProbe.ladder.clone();
        r.cohesionBlue = cohesionN == 0 ? 0 : cohesionSum / cohesionN;
        r.meanEnemyDist = distN == 0 ? 0 : distSum / distN;
        r.perfMsPerTick = (Perf.allNanos() - perfStart) / 1.0e6 / Math.max(1, tick);
        r.perfPeakMs = perfPeakMs;
        r.formationKept = formationKept;
        r.precedenceOk = precedenceOk;
        precedenceOk = -1;
        r.improv = improv;
        r.improvCount = improvCount;
        double alive = 0;
        for (LivingEntity e : UNITS.get(0)) alive += DEATH_TICK.getOrDefault(e.getUUID(), tick) / 20.0D;
        r.survivalSeconds = UNITS.get(0).isEmpty() ? 0 : alive / UNITS.get(0).size();
        r.pursuitMeters = pursuitN == 0 ? 0 : pursuitSum / pursuitN;
        r.orderSeconds = firstBlueDamage < 0 ? -1 : Math.max(0, firstBlueDamage - FORM_TICKS) / 20.0D;
        r.friendlyFire[0] = SimProbe.friendlyFire[0];
        r.friendlyFire[1] = SimProbe.friendlyFire[1];
        double minutes = Math.max(1, tick) / 1200.0D;
        r.xpPerGuardMinute = r.start[0] == 0 ? 0 : SimProbe.xpGained[0] / r.start[0] / minutes;
        r.commandXp = SimProbe.commandXpGained[0];
        for (java.util.Map.Entry<net.hfstack.rallyguard.tactics.DecisionReason, Long> e
                : net.hfstack.rallyguard.tactics.DecisionTrace.counts().entrySet()) {
            r.reasons.put(e.getKey().name(), e.getValue());
        }
        formationKept = -1;
        if (!reason.isEmpty()) r.notes = reason;
        net.hfstack.rallyguard.debug.sim.SimExtras.attributesCheck(guardsOf(0)); // E3B S: no stat ever changes
        net.hfstack.rallyguard.debug.sim.SimExtras.finish(r); // E3B block S
        net.hfstack.rallyguard.debug.sim.SimGvConfig.restore(); // torneo: GV's own config back as it was

        cleanup();
        Job done = job;
        job = null;
        phase = Phase.SETUP;

        // E3A F7: a run without contact is recorded (it is a finding of its own, never hidden
        // by its retry); the retry, with another seed, is queued once.
        if (stuck) r.notes = (r.notes.isEmpty() ? "" : r.notes + "; ") + "sin enfrentamiento";
        if (stuck && !done.retry()) {
            QUEUE.addFirst(new Job(done.scenario(), done.seed() + 7777L, done.rep(), done.control(), true));
        }
        RUNS.add(r);
        try {
            SimReport.appendRun(dir, r);
            // Rewritten after every run: an aborted or crashed batch still leaves its summary.
            SimReport.writeSummary(dir, batchId, presetName, tickrate, RUNS, matrix, NOTES);
        } catch (IOException e) {
            say("gui.rallyguard.sim.save_failed", e.getMessage());
        }
        say("gui.rallyguard.sim.run", r.scenario, r.seed, r.winner, r.survivors[0], r.start[0], r.survivors[1],
                r.start[1], Math.round(r.ticks / 20.0D), QUEUE.size());
    }

    private static void cleanup() {
        VeterancyRegistry vet = VeterancyRegistry.get(level);
        List<Guard> simGuards = new ArrayList<>(guardsOf(0));
        simGuards.addAll(guardsOf(1));
        net.hfstack.rallyguard.command.InstructionRunner.release(simGuards); // E3B G3
        net.hfstack.rallyguard.force.ForceBrain.forget(SIM_OWNER, SIM_SQUAD); // E3B R4: no state carried between runs
        net.hfstack.rallyguard.force.ForceBrain.forget(SIM_RED, SIM_RED_SQUAD);
        if (ForceRegistry.get(level).dissolve(SIM_OWNER, SIM_SQUAD)) {
            SquadRegistry.get(level).disband(SIM_OWNER, SIM_SQUAD);
        }
        if (ForceRegistry.get(level).dissolve(SIM_RED, SIM_RED_SQUAD)) {
            SquadRegistry.get(level).disband(SIM_RED, SIM_RED_SQUAD);
        }
        for (List<LivingEntity> side : UNITS) {
            for (LivingEntity e : side) {
                UUID id = e.getUUID();
                PackRegistry.get(level).remove(id);
                DebugRanks.forget(id);
                StanceRegistry.get(level).remove(id);
                DirectiveRegistry.get(level).remove(id);
                FormationRegistry.get(level).remove(id);
                vet.remove(id);
                SquadDirectives.forget(id);
                GuardModes.forget(id);
                if (!e.isRemoved()) e.discard();
            }
            side.clear();
        }
        for (Entity m : MOUNTS) if (!m.isRemoved()) m.discard();
        MOUNTS.clear();
        SimProbe.end();
        arena.clearTerrain();
        int h = arena.half() + 2;
        AABB box = new AABB(arena.cx - h, arena.y - 3, arena.cz - h, arena.cx + h, arena.y + 16, arena.cz + h);
        for (Entity e : level.getEntitiesOfClass(Entity.class, box,
                e -> e instanceof ItemEntity || e instanceof Projectile || e instanceof ExperienceOrb)) {
            e.discard();
        }
        // E3-COORD M2: the arena is the simulation's alone - any mob left on it that the run did not spawn
        // (a converted victim, a reinforcement, a natural spawn...) would fight the NEXT run as an unseen third
        // side. Long batches showed exactly that (blue killed by mobs of no side). Remove and count them.
        AABB wide = box.inflate(8, 32, 8);
        for (Mob m : level.getEntitiesOfClass(Mob.class, wide, m -> m.isAlive() && (m instanceof net.minecraft.world.entity.monster.Enemy
                || (m instanceof Guard g && (SIM_OWNER.equals(g.getOwnerId()) || SIM_RED.equals(g.getOwnerId())))))) {
            STRAYS.merge(EntityType.getKey(m.getType()).toString(), 1, Integer::sum);
            m.discard();
        }
    }

    /**
     * E3-COORD (David 2026-11-04: a guard killed by a zombie stays a zombie and keeps fighting): a hostile mob on
     * the arena that no side spawned - typically a guard turned into a zombie villager - joins RED for the rest of
     * the run, so the fight doesn't end while it is still alive and the report counts it ("convertedToRed").
     */
    private static void adoptConverted() {
        int h = arena.half() + 2;
        AABB box = new AABB(arena.cx - h, arena.y - 3, arena.cz - h, arena.cx + h, arena.y + 16, arena.cz + h).inflate(8, 32, 8);
        for (Mob m : level.getEntitiesOfClass(Mob.class, box, m -> m.isAlive() && m instanceof net.minecraft.world.entity.monster.Enemy
                && !SimProbe.SIDE.containsKey(m.getUUID()))) {
            m.setPersistenceRequired();
            UNITS.get(1).add(m);
            SimProbe.SIDE.put(m.getUUID(), 1);
            SimProbe.count(m, "converted");
        }
    }

    /** E3-COORD M2: mobs no run spawned, found on the arena after runs (type -> count), reported per batch. */
    private static final java.util.Map<String, Integer> STRAYS = new java.util.TreeMap<>();

    private static void finishBatch() {
        net.hfstack.rallyguard.combat.FieldManual.simNoClearance = false; // field manual: back to the real rules
        net.hfstack.rallyguard.combat.FieldManual.simNoCreepers = false;
        net.hfstack.rallyguard.combat.FieldManual.simNoShieldUp = false;
        net.hfstack.rallyguard.combat.FieldManual.simNoLeftHand = false;
        net.hfstack.rallyguard.debug.sim.SimGvConfig.restore();
        net.hfstack.rallyguard.tactics.DecisionTrace.forced = false;
        SimProbe.allowXp = false;
        if (sprinting) {
            level.getServer().tickRateManager().stopSprinting();
            sprinting = false;
        }
        if (arena != null) arena.forceChunks(false);
        phase = Phase.IDLE;
        if (dir == null) {
            if (level != null) SimAutorun.batchFinished(level.getServer());
            return;
        }
        try {
            if (!STRAYS.isEmpty()) NOTES.add("Mobs ajenos retirados del campo entre corridas (tercer bando oculto): " + STRAYS);
            List<String> findings = SimReport.writeSummary(dir, batchId, presetName, tickrate, RUNS, matrix, NOTES);
            say("gui.rallyguard.sim.batch_done", RUNS.size(), dir.toAbsolutePath().toString());
            for (int i = 0; i < Math.min(5, findings.size()); i++) {
                if (player != null) player.sendSystemMessage(Component.literal("• " + findings.get(i)));
            }
        } catch (IOException e) {
            say("gui.rallyguard.sim.save_failed", e.getMessage());
        }
        if (level != null) SimAutorun.batchFinished(level.getServer()); // CI: headless batch -> stop the server
    }

    // ==== Events (only while a run is live) ====

    @SubscribeEvent
    public void onDamage(LivingDamageEvent.Post event) {
        if (!SimProbe.active) return;
        int v = SimProbe.sideOf(event.getEntity());
        Entity by = event.getSource().getEntity();
        if (by == null && event.getSource().getDirectEntity() instanceof Projectile p) by = p.getOwner();
        int a = SimProbe.sideOf(by);
        if (v >= 0 && a >= 0 && v != a) {
            if (event.getEntity().getOffhandItem().is(Items.SHIELD)) { // torneo: what the shield let through
                SimProbe.count(event.getEntity(), event.getEntity().isBlocking() ? "act:hitThroughRaisedShield" : "act:hitShieldDown");
            }
            SimProbe.damage(a, event.getNewDamage());
            lastDamageTick = tick;
            if (a == 0 && firstBlueDamage < 0) firstBlueDamage = tick;
        }
    }

    /** Torneo 2026-11-04: every hit a simulation unit's shield stopped, and how much damage it stopped. */
    @SubscribeEvent(priority = net.neoforged.bus.api.EventPriority.LOWEST)
    public void onShieldBlock(net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent event) {
        if (!SimProbe.active || event.isCanceled() || !event.getBlocked()) return;
        if (SimProbe.sideOf(event.getEntity()) < 0) return;
        SimProbe.count(event.getEntity(), "act:shieldBlocks");
        SimProbe.add(event.getEntity(), "act:shieldBlockedDmg", Math.round(event.getBlockedDamage()));
    }

    /** Torneo, comer y rotar: a blue guard finished eating something from its hand (Guard Villagers' eat goals). */
    @SubscribeEvent
    public void onAte(net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent.Finish event) {
        if (!SimProbe.active || SimProbe.sideOf(event.getEntity()) != 0) return;
        if (event.getItem().get(net.minecraft.core.component.DataComponents.FOOD) != null) SimProbe.count(event.getEntity(), "act:ateHand");
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (!SimProbe.active) return;
        int v = SimProbe.sideOf(event.getEntity());
        if (v < 0) return;
        lastDamageTick = tick;
        DEATH_TICK.put(event.getEntity().getUUID(), tick);
        if (event.getSource().is(DamageTypes.DROWN)) SimProbe.drowned(v);
        if (LEADERS.get(v).contains(event.getEntity().getUUID())) LEADER_DEATHS[v]++;
        // E3-COORD M2: what killed them - an enemy, a friend, or the arena (fall, suffocation, void...)
        Entity killer = event.getSource().getEntity();
        String by = killer == null ? "env" : SimProbe.sideOf(killer) == v ? "friend" : SimProbe.sideOf(killer) >= 0 ? "enemy"
                : "other(" + net.minecraft.world.entity.EntityType.getKey(killer.getType()) + ")";
        SimProbe.count(event.getEntity(), "death:" + by + ":" + event.getSource().getMsgId());
    }

    private static void say(String key, Object... args) {
        if (player != null) player.sendSystemMessage(Component.translatable(key, args));
        if (SimAutorun.active()) { // CI: nobody reads the chat, so progress goes to the server log
            com.mojang.logging.LogUtils.getLogger().info("[rallyguard] sim {} {}", key, java.util.Arrays.toString(args));
        }
    }
}
