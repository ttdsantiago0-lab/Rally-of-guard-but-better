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
}
