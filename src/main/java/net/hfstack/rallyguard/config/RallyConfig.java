package net.hfstack.rallyguard.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.function.Supplier;

/**
 * E1: Rally's own settings (config/rallyguard-common.toml). Every value is read through
 * {@link #get}, which falls back to the default if the file isn't loaded yet - a
 * config read can never crash a tick. Uses NeoForge's {@code ModConfigSpec}, the same
 * mechanism Guard Villagers' {@code GuardConfig} uses.
 */
public final class RallyConfig {
    private RallyConfig() {
    }

    public static final ModConfigSpec SPEC;

    // Ranged fire (E1-G)
    public static final ModConfigSpec.BooleanValue FINITE_ARROWS;
    public static final ModConfigSpec.IntValue BOW_MAX, BOW_OPTIMAL, BOW_MIN;
    public static final ModConfigSpec.IntValue CROSSBOW_MAX, CROSSBOW_OPTIMAL, CROSSBOW_MIN;
    public static final ModConfigSpec.IntValue GUN_MAX, GUN_OPTIMAL, GUN_MIN;
    public static final ModConfigSpec.IntValue VOLLEY_MAX_RANGE;
    public static final ModConfigSpec.IntValue VOLLEY_ARROWS_PER_SQUAD_PER_SECOND;
    public static final ModConfigSpec.IntValue RANGED_COOLDOWN_TICKS;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> LEGACY_POWER_WEAPONS;
    public static final ModConfigSpec.DoubleValue RANGE_PER_RANK;
    public static final ModConfigSpec.DoubleValue VOLLEY_SPREAD_PER_RANK;

    // Command (E2A block 1)
    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> COMMAND_CAPACITY;
    public static final ModConfigSpec.IntValue MIN_SUBUNIT, MAX_SUBUNIT;
    public static final ModConfigSpec.IntValue SLOTS_PER_TICK;
    public static final ModConfigSpec.IntValue MAX_RELIEFS, MAX_RELIEFS_LARGE, LARGE_ARMY;
    public static final ModConfigSpec.DoubleValue ROUT_FLEE_SHARE;

    // Leadership (E3A A1)
    public static final ModConfigSpec.ConfigValue<List<? extends Double>> LEADERSHIP_WEIGHTS;
    public static final ModConfigSpec.IntValue MAX_DIRECT_SUBORDINATES, MAX_RESPONSE_DELAY, MAX_RELIEF_DELAY;
    public static final ModConfigSpec.DoubleValue PURSUIT_BEST, PURSUIT_WORST, TOLERANCE_BEST, TOLERANCE_WORST;
    public static final ModConfigSpec.IntValue PLAYER_OVERRIDE_SECONDS;

    // HQ zone (E3A A4/A6)
    public static final ModConfigSpec.IntValue HQ_RADIUS;
    public static final ModConfigSpec.BooleanValue HQ_RELAX;

    // Veterancy (E2A block 2)
    public static final ModConfigSpec.ConfigValue<List<? extends String>> RANK_NAMES;
    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> RANK_XP;
    public static final ModConfigSpec.DoubleValue XP_PER_THREAT, XP_MAX_VICTIM_SHARE, XP_INFERIORITY_MAX, XP_COVER_BONUS;
    public static final ModConfigSpec.IntValue XP_PER_MINUTE_CAP, XP_NEAR_DEATH_SURVIVED, XP_IMMOBILE_SECONDS, XP_REPEAT_VICTIM_SECONDS;
    public static final ModConfigSpec.IntValue CMD_XP_SURVIVAL, CMD_XP_WIN, CMD_XP_FORMATION, LEGEND_CAPACITY_STEPS;
    public static final ModConfigSpec.DoubleValue PATH_CAP, PATH_SCALE, ADVANCED_BUDGET_MS;
    public static final ModConfigSpec.IntValue COMBAT_MINUTES_PER_DAY;
    public static final ModConfigSpec.BooleanValue DECISION_TRACE;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> MCA_ACCESSORS;
    public static final ModConfigSpec.IntValue XP_LONG_FIGHT_SECONDS, XP_LONG_FIGHT;
    public static final ModConfigSpec.IntValue XP_NEW_ENEMY_TYPE, XP_KILL;
    public static final ModConfigSpec.DoubleValue XP_REPEAT_DECAY;
    public static final ModConfigSpec.IntValue TOTEM_DELAY_TICKS, TOTEM_DELAY_TANK_TICKS;
    public static final ModConfigSpec.IntValue KAMIKAZE_REVIVES, KAMIKAZE_WINDOW_SECONDS;
    public static final ModConfigSpec.DoubleValue KAMIKAZE_CHANCE;

    // Simulation v2 (E2A block 3)
    public static final ModConfigSpec.IntValue SIM_ARENA_SIZE, SIM_ARENA_DISTANCE, SIM_MAX_PER_SIDE, SIM_STUCK_SECONDS;
    public static final ModConfigSpec.ConfigValue<String> SIM_FLOOR_BLOCK, SIM_TICKRATE, SIM_GUN_ITEM;

    // Scale (E1-E)
    public static final ModConfigSpec.IntValue MAX_GUARDS_PER_TICK;
    public static final ModConfigSpec.IntValue LEDGER_PAGE_SIZE;

    // Optional systems
    public static final ModConfigSpec.BooleanValue ENGAGEMENT_CAP;
    public static final ModConfigSpec.BooleanValue RANGED_FIRE;
    public static final ModConfigSpec.BooleanValue MOUNT_DRIVER;
    public static final ModConfigSpec.BooleanValue MOUNTS_FROM_TAG_ONLY;
    public static final ModConfigSpec.BooleanValue PACK_RESUPPLY;
    public static final ModConfigSpec.BooleanValue MCA_GUARD;

    // Diagnostics
    public static final ModConfigSpec.BooleanValue LOG_MOUNT_DAMAGE;

    static {
        ModConfigSpec.Builder b = new ModConfigSpec.Builder();

        b.comment("Ranged fire: weapon ranges in blocks, volley limits").push("ranged");
        FINITE_ARROWS = b.comment("Guards need real arrows (pack / off hand) for every bow or crossbow shot, 1 item per 4 shots")
                .define("finiteArrows", true);
        BOW_MAX = b.defineInRange("bowMaxRange", 40, 8, 128);
        BOW_OPTIMAL = b.defineInRange("bowOptimalRange", 15, 4, 64);
        BOW_MIN = b.defineInRange("bowMinRange", 5, 0, 32);
        CROSSBOW_MAX = b.defineInRange("crossbowMaxRange", 44, 8, 128);
        CROSSBOW_OPTIMAL = b.defineInRange("crossbowOptimalRange", 16, 4, 64);
        CROSSBOW_MIN = b.defineInRange("crossbowMinRange", 5, 0, 32);
        GUN_MAX = b.comment("Scorched Guns weapons (their own AI fires them; these set the post leash and approach)")
                .defineInRange("gunMaxRange", 40, 8, 128);
        GUN_OPTIMAL = b.defineInRange("gunOptimalRange", 20, 4, 64);
        GUN_MIN = b.defineInRange("gunMinRange", 6, 0, 32);
        VOLLEY_MAX_RANGE = b.defineInRange("volleyMaxRange", 64, 16, 160);
        VOLLEY_ARROWS_PER_SQUAD_PER_SECOND = b.defineInRange("volleyArrowsPerSquadPerSecond", 10, 1, 200);
        RANGED_COOLDOWN_TICKS = b.comment("Rally-fired shots per archer: at least 30 ticks (1.5 s) apart")
                .defineInRange("rangedCooldownTicks", 30, 30, 200);
        LEGACY_POWER_WEAPONS = b.comment("Weapon classes whose own release code adds Power to the arrow's base damage "
                        + "(guards then add base + 0.5 x Power + 0.5, like that weapon's player shot)")
                .defineListAllowEmpty("legacyPowerWeaponClasses",
                        List.of("com.iafenvoy.iceandfire.item.tool.DragonBowItem"), o -> o instanceof String);
        RANGE_PER_RANK = b.comment("Extra reach per veterancy rank, as a fraction of the weapon's range (0.15 = +15% per rank)")
                .defineInRange("rangePerRank", 0.15D, 0.0D, 1.0D);
        VOLLEY_SPREAD_PER_RANK = b.comment("Volley/long-shot spread reduction per veterancy rank (0.12 = 12% tighter per rank)")
                .defineInRange("volleySpreadPerRank", 0.12D, 0.0D, 0.2D);
        b.pop();

        b.comment("Command: capacity per veterancy rank, subunits, succession (E2A)").push("command");
        COMMAND_CAPACITY = b.comment("How many guards a guard of rank 0..5 can coordinate (the unit's captain is the one with the most)")
                .defineList("capacityByRank", List.of(4, 10, 25, 45, 80, 120), o -> o instanceof Integer i && i > 0);
        MIN_SUBUNIT = b.comment("Smallest subunit a large group is split into, even under a low-capacity captain")
                .defineInRange("minSubunitSize", 8, 2, 200);
        MAX_SUBUNIT = b.comment("Largest subunit, whatever the captain's capacity")
                .defineInRange("maxSubunitSize", 120, 4, 1000);
        SLOTS_PER_TICK = b.comment("Formation slots handed out per tick (a 200-guard muster takes 200 / this ticks)")
                .defineInRange("slotsPerTick", 12, 1, 500);
        MAX_RELIEFS = b.comment("Times a unit's command passes to the next captain before the unit routs")
                .defineInRange("maxReliefs", 3, 0, 10);
        MAX_RELIEFS_LARGE = b.defineInRange("maxReliefsLargeArmy", 4, 0, 10);
        LARGE_ARMY = b.comment("A unit at least this big counts as a large army (uses maxReliefsLargeArmy)")
                .defineInRange("largeArmySize", 100, 2, 100000);
        ROUT_FLEE_SHARE = b.comment("Share of a routed unit that flees (the rest break ranks)")
                .defineInRange("routFleeShare", 0.35D, 0.0D, 1.0D);
        b.pop();

        b.comment("Leadership score and how a leader changes EXECUTION - never health, damage or speed (E3A A1)").push("leadership");
        LEADERSHIP_WEIGHTS = b.comment("Score weights (0..100 total): rank, leader trait, command XP, unit experience, command mastery path")
                .defineList("scoreWeights", List.of(40.0D, 15.0D, 20.0D, 15.0D, 10.0D), o -> o instanceof Number n && n.doubleValue() >= 0);
        MAX_DIRECT_SUBORDINATES = b.comment("Most guards one captain commands directly; bigger units split into subunits under the next best score")
                .defineInRange("maxDirectSubordinates", 24, 2, 500);
        MAX_RESPONSE_DELAY = b.comment("Ticks a unit under the WORST leader waits before carrying out a new directive (best leader: 0)")
                .defineInRange("maxResponseDelayTicks", 40, 0, 400);
        MAX_RELIEF_DELAY = b.comment("Ticks a unit needs to reorganize after losing its captain under the worst next leader (best: 0)")
                .defineInRange("maxReliefDelayTicks", 100, 0, 1200);
        PURSUIT_BEST = b.comment("Post-leash multiplier under the best leader (less over-pursuit)").defineInRange("pursuitBest", 0.85D, 0.3D, 1.5D);
        PURSUIT_WORST = b.defineInRange("pursuitWorst", 1.15D, 0.3D, 2.0D);
        TOLERANCE_BEST = b.comment("Formation tolerance multiplier before breaking (best leader keeps it tighter)")
                .defineInRange("formationToleranceBest", 0.7D, 0.2D, 2.0D);
        TOLERANCE_WORST = b.defineInRange("formationToleranceWorst", 1.3D, 0.2D, 3.0D);
        PLAYER_OVERRIDE_SECONDS = b.comment("E3A A2: seconds a player's explicit order beats automatic force/plan/captain directives")
                .defineInRange("playerOverrideSeconds", 60, 0, 3600);
        b.pop();

        b.comment("HQ zones: the area around an HQ banner (E3A A4/A6)").push("hq");
        HQ_RADIUS = b.comment("Radius of an HQ zone in blocks: arriving inside relaxes guards; the Battle Planner table must be inside one")
                .defineInRange("radius", 16, 4, 128);
        HQ_RELAX = b.comment("Guards that arrive at their HQ break ranks, park their mounts and relax until the next order")
                .define("relaxOnArrival", true);
        b.pop();

        b.comment("Veterancy: ranks 0..5 earned only in real combat (E2A)").push("veterancy");
        RANK_NAMES = b.comment("Optional rank names 0..5; empty = the translated defaults (Recruit, Trained, Experienced, Veteran, Elite, Legend)")
                .defineListAllowEmpty("rankNames", List.of(), o -> o instanceof String);
        RANK_XP = b.comment("XP needed for ranks 1..5")
                .defineList("xpForRank", List.of(100, 350, 900, 2000, 4500), o -> o instanceof Integer i && i > 0);
        XP_PER_THREAT = b.comment("E3A V1: XP for taking a whole threat-1 enemy (a zombie) from full health to dead, "
                        + "shared by contribution; scaled by the enemy's threat (health, damage, flying, ranged, boss)")
                .defineInRange("xpPerThreat", 20.0D, 0.0D, 1000.0D);
        XP_MAX_VICTIM_SHARE = b.comment("Most XP one guard takes from one enemy, as a share of its current rank band (bosses can't skip ranks)")
                .defineInRange("maxXpPerVictimShare", 0.5D, 0.05D, 1.0D);
        XP_PER_MINUTE_CAP = b.comment("Most combat XP a guard earns per minute").defineInRange("xpPerMinuteCap", 80, 1, 100000);
        XP_INFERIORITY_MAX = b.comment("XP multiplier cap when fighting outnumbered (enemies / allies nearby)")
                .defineInRange("inferiorityBonusMax", 2.0D, 1.0D, 5.0D);
        XP_COVER_BONUS = b.comment("Extra share for hitting an enemy that was attacking a comrade (covering it)")
                .defineInRange("coverBonus", 0.5D, 0.0D, 5.0D);
        XP_NEAR_DEATH_SURVIVED = b.comment("XP for a fight survived after dropping under 3 hearts")
                .defineInRange("nearDeathSurvivedXp", 15, 0, 10000);
        XP_IMMOBILE_SECONDS = b.comment("An enemy that hasn't moved or fought for this long gives no XP (farms)")
                .defineInRange("immobileSeconds", 5, 1, 600);
        XP_REPEAT_VICTIM_SECONDS = b.comment("Hitting the SAME enemy for longer than this stops paying (a stuck mob)")
                .defineInRange("repeatVictimSeconds", 45, 5, 3600);
        CMD_XP_SURVIVAL = b.comment("E3A V2: command XP for a captain per fight, times the share of its unit that survived")
                .defineInRange("commandSurvivalXp", 20, 0, 10000);
        CMD_XP_WIN = b.comment("Command XP when the captain's unit is left holding the field (no enemies near)")
                .defineInRange("commandWinXp", 15, 0, 10000);
        CMD_XP_FORMATION = b.comment("Command XP when the unit kept together (cohesion) through the fight")
                .defineInRange("commandFormationXp", 10, 0, 10000);
        LEGEND_CAPACITY_STEPS = b.comment("E3A V0: command-capacity steps a rank-5 guard gains from extra XP (0 = none; capacity comes from leadership)")
                .defineInRange("legendCapacitySteps", 0, 0, 10);
        PATH_CAP = b.comment("Ceiling of each post-rank-5 path (survival, command, mount bond, history)")
                .defineInRange("pathCap", 100.0D, 1.0D, 10000.0D);
        PATH_SCALE = b.comment("XP for a path to reach ~63 % of its cap (diminishing returns)")
                .defineInRange("pathScale", 2000.0D, 10.0D, 1.0E7D);
        ADVANCED_BUDGET_MS = b.comment("E3A V5: ms per tick the rank-5 advanced decision core may use; above it guards fall back to the normal ladder")
                .defineInRange("advancedBudgetMs", 0.25D, 0.01D, 50.0D);
        COMBAT_MINUTES_PER_DAY = b.comment("Minutes of combat in a 'day of regular play' (only for the sim's veterancy-pace estimate)")
                .defineInRange("combatMinutesPerDay", 10, 1, 1440);
        MCA_ACCESSORS = b.comment("E3A V6: no-argument method chains tried by reflection to read an MCA personality / traits from a guard "
                        + "(each 'a.b.c'); unknown = genetics from the UUID")
                .defineListAllowEmpty("mcaPersonalityAccessors",
                        List.of("getVillagerBrain.getPersonality", "getPersonality"), o -> o instanceof String);
        XP_LONG_FIGHT_SECONDS = b.defineInRange("longFightSeconds", 12, 1, 600);
        XP_LONG_FIGHT = b.comment("XP for surviving a fight longer than longFightSeconds")
                .defineInRange("longFightXp", 15, 0, 10000);
        XP_NEW_ENEMY_TYPE = b.comment("XP the first time a guard fights a new enemy type")
                .defineInRange("newEnemyTypeXp", 25, 0, 10000);
        XP_KILL = b.defineInRange("killXp", 10, 0, 10000);
        XP_REPEAT_DECAY = b.comment("Each repeat against the same enemy type multiplies its XP by this (diminishing returns)")
                .defineInRange("repeatDecay", 0.9D, 0.1D, 1.0D);
        TOTEM_DELAY_TICKS = b.comment("Ticks a guard needs to bring a totem to hand after taking it from its pack")
                .defineInRange("totemDelayTicks", 60, 0, 1200);
        TOTEM_DELAY_TANK_TICKS = b.defineInRange("totemDelayTankTicks", 20, 0, 1200);
        KAMIKAZE_REVIVES = b.comment("Totem revives within kamikazeWindowSeconds that can turn a guard reckless")
                .defineInRange("kamikazeRevives", 3, 1, 50);
        KAMIKAZE_WINDOW_SECONDS = b.defineInRange("kamikazeWindowSeconds", 600, 10, 86400);
        KAMIKAZE_CHANCE = b.defineInRange("kamikazeChance", 0.35D, 0.0D, 1.0D);
        b.pop();

        b.comment("Simulation v2 (/rally sim batch): ground arena, speed, units (E2A)").push("simulation");
        SIM_ARENA_SIZE = b.comment("Arena side in blocks (square, flat, solid floor)")
                .defineInRange("arenaSize", 96, 48, 256);
        SIM_ARENA_DISTANCE = b.comment("How far from the player the arena is placed (blocks); its chunks are force-loaded while a batch runs")
                .defineInRange("arenaDistance", 128, 48, 2048);
        SIM_FLOOR_BLOCK = b.comment("Floor block id").define("floorBlock", "minecraft:stone");
        SIM_TICKRATE = b.comment("Default speed: normal or sprint (vanilla /tick sprint)").define("tickrate", "normal");
        SIM_GUN_ITEM = b.comment("Item id handed to the 'gunner' role (a Scorched Guns gun); empty = crossbow stands in")
                .define("gunItem", "");
        SIM_MAX_PER_SIDE = b.comment("Most units per side (performance cap; bigger scenarios are clipped and say so)")
                .defineInRange("maxUnitsPerSide", 400, 1, 1000);
        SIM_STUCK_SECONDS = b.comment("A run with no damage for this long while both sides live counts as stuck (retried once)")
                .defineInRange("stuckSeconds", 45, 10, 600);
        b.pop();

        b.comment("Scale").push("scale");
        MAX_GUARDS_PER_TICK = b.comment("Most guards the stance loop processes in one tick (the rest wait for the next pass)")
                .defineInRange("maxGuardsProcessedPerTick", 200, 10, 10000);
        LEDGER_PAGE_SIZE = b.comment("Rows per Ledger page sent by the server (at most 2000)")
                .defineInRange("ledgerPageSize", 2000, 50, 2000);
        b.pop();

        b.comment("Optional systems (turn off to isolate a problem)").push("systems");
        ENGAGEMENT_CAP = b.define("engagementCap", true);
        RANGED_FIRE = b.define("rangedFire", true);
        MOUNT_DRIVER = b.define("mountDriver", true);
        MOUNTS_FROM_TAG_ONLY = b.comment("Only mobs in the entity type tag rallyguard:guard_mounts can be steered as mounts")
                .define("mountsFromTagOnly", false);
        PACK_RESUPPLY = b.define("packResupply", true);
        MCA_GUARD = b.comment("Keep MCA Mob Compat from taking over hiring, movement or ownership of guards")
                .define("mcaGuard", true);
        b.pop();

        b.comment("Diagnostics").push("debug");
        LOG_MOUNT_DAMAGE = b.comment("Log (INFO) every hit on a mount ridden by an owned guard")
                .define("logMountDamage", true);
        DECISION_TRACE = b.comment("E3A V11: keep each guard's last 8 decision reasons (/rally debug why); on during simulations")
                .define("decisionTrace", false);
        b.pop();

        SPEC = b.build();
    }

    /** A config value, or {@code fallback} if the config isn't loaded (or the read fails). */
    public static <T> T get(Supplier<T> value, T fallback) {
        try {
            T v = value.get();
            return v == null ? fallback : v;
        } catch (RuntimeException e) {
            return fallback;
        }
    }

    public static boolean on(ModConfigSpec.BooleanValue flag) {
        return get(flag::get, Boolean.TRUE);
    }

    public static int i(ModConfigSpec.IntValue value, int fallback) {
        return get(value::get, fallback);
    }

    public static double d(ModConfigSpec.DoubleValue value, double fallback) {
        return get(value::get, fallback);
    }

    /** Element {@code index} of an int list setting, the list's last element past its end,
     *  or {@code fallback[index]} (clamped) if the config isn't readable. */
    public static int at(ModConfigSpec.ConfigValue<List<? extends Integer>> list, int index, int[] fallback) {
        List<? extends Integer> values = get(list::get, List.of());
        if (values.isEmpty()) return fallback[Math.max(0, Math.min(index, fallback.length - 1))];
        return values.get(Math.max(0, Math.min(index, values.size() - 1)));
    }
}
