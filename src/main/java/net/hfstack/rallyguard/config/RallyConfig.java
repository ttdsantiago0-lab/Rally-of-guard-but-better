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

    // Veterancy (E2A block 2)
    public static final ModConfigSpec.ConfigValue<List<? extends String>> RANK_NAMES;
    public static final ModConfigSpec.ConfigValue<List<? extends Integer>> RANK_XP;
    public static final ModConfigSpec.DoubleValue XP_PER_DAMAGE;
    public static final ModConfigSpec.IntValue XP_LONG_FIGHT_SECONDS, XP_LONG_FIGHT;
    public static final ModConfigSpec.IntValue XP_NEW_ENEMY_TYPE, XP_KILL;
    public static final ModConfigSpec.DoubleValue XP_REPEAT_DECAY;
    public static final ModConfigSpec.IntValue TOTEM_DELAY_TICKS, TOTEM_DELAY_TANK_TICKS;
    public static final ModConfigSpec.IntValue KAMIKAZE_REVIVES, KAMIKAZE_WINDOW_SECONDS;
    public static final ModConfigSpec.DoubleValue KAMIKAZE_CHANCE;

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

        b.comment("Veterancy: ranks 0..5 earned only in real combat (E2A)").push("veterancy");
        RANK_NAMES = b.comment("Optional rank names 0..5; empty = the translated defaults (Recruit, Trained, Veteran, Elite, Master, Legend)")
                .defineListAllowEmpty("rankNames", List.of(), o -> o instanceof String);
        RANK_XP = b.comment("XP needed for ranks 1..5")
                .defineList("xpForRank", List.of(100, 350, 900, 2000, 4500), o -> o instanceof Integer i && i > 0);
        XP_PER_DAMAGE = b.comment("XP per point of damage dealt to a real enemy (diminished for repeated enemy types)")
                .defineInRange("xpPerDamage", 1.0D, 0.0D, 100.0D);
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
