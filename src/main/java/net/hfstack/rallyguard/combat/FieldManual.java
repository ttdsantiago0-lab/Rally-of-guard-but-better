package net.hfstack.rallyguard.combat;

import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.component.DataComponents;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * What the doctrine tournament PROVED (docs/MANUAL_DE_CAMPO.md), applied to a player's guards in play.
 * Decided by David 2026-11-04; every rule has its switch in {@code [fieldManual]} (off = old behaviour) and a
 * simulation override ({@code off:clearance}, {@code off:creepers}, {@code off:shieldUp}, {@code off:leftHand}) so the
 * tournament can keep measuring each one against the old behaviour.
 * <ul>
 *   <li><b>Point-blank fire</b> - {@link #friendClearance()}.</li>
 *   <li><b>Creepers</b> - shooters and shield-bearers may target them ({@link #mayTargetCreeper}; read by
 *   {@code mixin.compat.GuardFactionMixin}) and acquire one nearby when they have no target.</li>
 *   <li><b>Shield up against shooters</b> - {@link #wantsShieldUp}, decided once a second here, read every tick by the
 *   mixin on Guard Villagers' {@code RaiseShieldGoal}.</li>
 *   <li><b>Left hand</b> - shield stays; else a pack totem goes there; else food (Guard Villagers' own
 *   "step away and eat" goal only uses the left hand).</li>
 *   <li><b>Shoot from where they stand against shooters</b> - {@link #fireMode}: an archer facing a shooter fires
 *   STILL instead of walking in to its optimal range (sim override {@code off:hold}).</li>
 * </ul>
 * Only owned guards; every guard is looked at once a second in its slice (cheap: one small entity query).
 */
public final class FieldManual {
    private FieldManual() {
    }

    /** Simulation switches (set per run by SimExtras; false = the field manual rule is on). */
    public static boolean simNoClearance, simNoCreepers, simNoShieldUp, simNoLeftHand, simNoHold;

    private static final Set<UUID> SHIELD_UP = new HashSet<>();
    private static final int CREEPER_RANGE_SHOOTER = 16, CREEPER_RANGE_SHIELD = 8;

    // ==== Shoot from where they stand against shooters ====
    /**
     * Ronda 3: against skeletons, archers and lines that fired from where they stood (STILL or the fixed VOLLEY) won
     * 97-100 % where the base won ~52 % - the base walks in to its optimal range (15 blocks), inside the skeletons'.
     * Only replaces NORMAL (no explicit order, no veteran's own choice), only for a player's shooters whose target shoots.
     */
    public static net.hfstack.rallyguard.combat.FireMode fireMode(Guard g, net.hfstack.rallyguard.combat.FireMode mode) {
        if (mode != net.hfstack.rallyguard.combat.FireMode.NORMAL || !holdsAgainst(g)) return mode;
        return net.hfstack.rallyguard.combat.FireMode.STILL;
    }

    private static boolean holdsAgainst(Guard g) {
        if (simNoHold || !RallyConfig.on(RallyConfig.HOLD_AGAINST_SHOOTERS)) return false;
        LivingEntity t = g.getTarget();
        return t != null && t.isAlive() && t instanceof RangedAttackMob && GuardOwnership.getOwner(g) != null && shooter(g);
    }

    // ==== Point-blank fire ====
    public static double friendClearance() {
        return simNoClearance ? 5.0D : RallyConfig.d(RallyConfig.FRIEND_CLEARANCE, 1.0D);
    }

    // ==== Creepers ====
    private static boolean shooter(Guard g) {
        return net.hfstack.rallyguard.order.Engagement.isRanged(g);
    }

    private static boolean shieldBearer(Guard g) {
        return g.getOffhandItem().is(Items.SHIELD);
    }

    /** Guard Villagers' blacklist keeps creepers away from guards; ours may attack them if they shoot or carry a shield. */
    public static boolean mayTargetCreeper(Guard g) {
        if (simNoCreepers || !RallyConfig.on(RallyConfig.CREEPER_TARGETING)) return false;
        return GuardOwnership.getOwner(g) != null && (shooter(g) || shieldBearer(g));
    }

    // ==== Shield up ====
    /** How long a shooter's hit keeps the shield up after the guard has lost (or never had) that shooter as target. */
    private static final int SHOT_MEMORY_TICKS = 100;
    /** An enemy shooter this close that can see the guard keeps its shield up (a skeleton shoots from ~15 blocks). */
    private static final double SHOOTER_NEAR = 24.0D;

    /** The nearest living enemy shooter within {@link #SHOOTER_NEAR} blocks that has line of sight to the guard. */
    private static LivingEntity nearestShooter(ServerLevel level, Guard g) {
        LivingEntity best = null;
        double bestD = SHOOTER_NEAR * SHOOTER_NEAR;
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, g.getBoundingBox().inflate(SHOOTER_NEAR),
                e -> e.isAlive() && e instanceof Enemy && e instanceof RangedAttackMob)) {
            double d = e.distanceToSqr(g);
            if (d < bestD && e.hasLineOfSight(g)) {
                bestD = d;
                best = e;
            }
        }
        return best;
    }

    public static boolean wantsShieldUp(Guard g) {
        return SHIELD_UP.contains(g.getUUID());
    }

    // ==== Once a second ====
    public static void second(ServerLevel level, Guard g) {
        if (GuardOwnership.getOwner(g) == null) return;
        LivingEntity target = g.getTarget();
        // creepers: acquire one if idle
        if ((target == null || !target.isAlive()) && mayTargetCreeper(g)) {
            int r = shooter(g) ? CREEPER_RANGE_SHOOTER : CREEPER_RANGE_SHIELD;
            Creeper best = null;
            double bestD = Double.MAX_VALUE;
            for (Creeper c : level.getEntitiesOfClass(Creeper.class, g.getBoundingBox().inflate(r), c -> c.isAlive() && g.hasLineOfSight(c))) {
                double d = c.distanceToSqr(g);
                if (d < bestD) {
                    bestD = d;
                    best = c;
                }
            }
            if (best != null) {
                net.hfstack.rallyguard.order.CommandTargeting.issue(g, best);
                net.hfstack.rallyguard.debug.SimProbe.count(g, "act:creeperTargeted");
            }
        }
        // shield up: UNDER FIRE from a shooter, and not surrounded (<2 melee enemies within 3 blocks).
        // Under fire = its target shoots, OR a shooter hit it in the last 5 s. Round-5 probe (t5-shield-probe):
        // against 28 skeletons a shield-bearer spends about HALF its time with no target while still being shot, and
        // "target shoots" alone is what Guard Villagers already does (raiseShield: shooter >= 5 blocks away), so the
        // first version changed nothing (blocking 43 % of the time, same as before). GV's global "always up" switch
        // that won the tournament kept it up then too (blocking 89 %).
        boolean up = false;
        final LivingEntity aim = g.getTarget();
        LivingEntity shooter = null;
        String why = "act:secWantsUp";
        if (aim != null && aim.isAlive() && aim instanceof RangedAttackMob) {
            shooter = aim;
        } else {
            LivingEntity by = g.getLastHurtByMob();
            if (by != null && by.isAlive() && by instanceof RangedAttackMob
                    && g.tickCount - g.getLastHurtByMobTimestamp() < SHOT_MEMORY_TICKS) {
                shooter = by;
                why = "act:secWantsUpUnderFire";
            } else if (!simNoShieldUp && RallyConfig.on(RallyConfig.SHIELD_DOCTRINE) && shieldBearer(g)) {
                // 2026-11-05 (David: against shooters, surviving comes first): also while an enemy shooter that can
                // see the guard is near. "5 s after a hit" raised blocking from 41 % to 60 % of the time but the
                // survivors didn't follow in the repeat; GV's always-up switch (89 %) did.
                shooter = nearestShooter(level, g);
                why = "act:secWantsUpShooterNear";
            }
        }
        final LivingEntity firing = shooter;
        if (!simNoShieldUp && RallyConfig.on(RallyConfig.SHIELD_DOCTRINE) && shieldBearer(g) && firing != null) {
            List<LivingEntity> near = level.getEntitiesOfClass(LivingEntity.class, g.getBoundingBox().inflate(3),
                    e -> e != firing && e.isAlive() && e instanceof Enemy && !(e instanceof RangedAttackMob));
            up = near.size() < 2;
        }
        if (shieldBearer(g)) { // telemetry, once a second per shield-bearer: why the shield is (not) up
            net.hfstack.rallyguard.debug.SimProbe.count(g, aim == null ? "act:secTargetNone"
                    : aim instanceof RangedAttackMob ? "act:secTargetRanged" : "act:secTargetMelee");
            if (up) net.hfstack.rallyguard.debug.SimProbe.count(g, why);
            if (g.isBlocking()) net.hfstack.rallyguard.debug.SimProbe.count(g, "act:secBlocking");
            if (g.isUsingItem()) net.hfstack.rallyguard.debug.SimProbe.count(g, "act:secUsingItem");
        }
        if (up) {
            if (SHIELD_UP.add(g.getUUID())) net.hfstack.rallyguard.debug.SimProbe.count(g, "act:shieldUpDoctrine");
        } else {
            SHIELD_UP.remove(g.getUUID());
        }
        if (holdsAgainst(g)) net.hfstack.rallyguard.debug.SimProbe.count(g, "act:secHoldVsShooter");
        leftHand(level, g);
    }

    /** David: shield stays; else a totem in the left hand; else food there (Guard Villagers eats from the left hand). */
    private static void leftHand(ServerLevel level, Guard g) {
        if (simNoLeftHand || !RallyConfig.on(RallyConfig.LEFT_HAND_POLICY)) return;
        if (g.isUsingItem()) return; // eating or blocking right now
        ItemStack off = g.getOffhandItem();
        if (off.is(Items.SHIELD) || off.is(Items.TOTEM_OF_UNDYING)) return;
        SimpleContainer pack = GuardPack.of(level, g.getUUID());
        int totem = find(pack, s -> s.is(Items.TOTEM_OF_UNDYING));
        if (totem >= 0) {
            swapIntoLeftHand(g, pack, totem);
            net.hfstack.rallyguard.debug.SimProbe.count(g, "act:totemToHand");
            return;
        }
        if (!off.isEmpty()) return; // something else there (food already, a torch, an arrow stack...): leave it
        int food = find(pack, s -> s.get(DataComponents.FOOD) != null && !s.is(Items.GOLDEN_APPLE) && !s.is(Items.ENCHANTED_GOLDEN_APPLE));
        if (food >= 0) {
            swapIntoLeftHand(g, pack, food);
            net.hfstack.rallyguard.debug.SimProbe.count(g, "act:foodToHand");
        }
    }

    private static int find(SimpleContainer pack, java.util.function.Predicate<ItemStack> what) {
        for (int i = 0; i < pack.getContainerSize(); i++) {
            ItemStack s = pack.getItem(i);
            if (!s.isEmpty() && what.test(s)) return i;
        }
        return -1;
    }

    /** The pack stack goes to the left hand; whatever was there goes into that pack slot (nothing is lost). */
    private static void swapIntoLeftHand(Guard g, SimpleContainer pack, int slot) {
        ItemStack fromPack = pack.getItem(slot);
        pack.setItem(slot, g.getOffhandItem().copy());
        g.setItemSlot(EquipmentSlot.OFFHAND, fromPack);
    }

    public static void forget(UUID id) {
        SHIELD_UP.remove(id);
    }
}
