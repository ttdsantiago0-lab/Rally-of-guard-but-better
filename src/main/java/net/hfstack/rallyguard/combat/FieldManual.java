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
 * </ul>
 * Only owned guards; every guard is looked at once a second in its slice (cheap: one small entity query).
 */
public final class FieldManual {
    private FieldManual() {
    }

    /** Simulation switches (set per run by SimExtras; false = the field manual rule is on). */
    public static boolean simNoClearance, simNoCreepers, simNoShieldUp, simNoLeftHand;

    private static final Set<UUID> SHIELD_UP = new HashSet<>();
    private static final int CREEPER_RANGE_SHOOTER = 16, CREEPER_RANGE_SHIELD = 8;

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
        // shield up: target shoots, and not surrounded (<2 melee enemies within 3 blocks)
        boolean up = false;
        final LivingEntity aim = g.getTarget();
        if (!simNoShieldUp && RallyConfig.on(RallyConfig.SHIELD_DOCTRINE) && shieldBearer(g)
                && aim != null && aim.isAlive() && aim instanceof RangedAttackMob) {
            List<LivingEntity> near = level.getEntitiesOfClass(LivingEntity.class, g.getBoundingBox().inflate(3),
                    e -> e != aim && e.isAlive() && e instanceof Enemy && !(e instanceof RangedAttackMob));
            up = near.size() < 2;
        }
        if (shieldBearer(g)) { // telemetry, once a second per shield-bearer: why the shield is (not) up
            net.hfstack.rallyguard.debug.SimProbe.count(g, aim == null ? "act:secTargetNone"
                    : aim instanceof RangedAttackMob ? "act:secTargetRanged" : "act:secTargetMelee");
            if (up) net.hfstack.rallyguard.debug.SimProbe.count(g, "act:secWantsUp");
            if (g.isBlocking()) net.hfstack.rallyguard.debug.SimProbe.count(g, "act:secBlocking");
            if (g.isUsingItem()) net.hfstack.rallyguard.debug.SimProbe.count(g, "act:secUsingItem");
        }
        if (up) {
            if (SHIELD_UP.add(g.getUUID())) net.hfstack.rallyguard.debug.SimProbe.count(g, "act:shieldUpDoctrine");
        } else {
            SHIELD_UP.remove(g.getUUID());
        }
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
