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
 *   <li><b>Get away from a swelling creeper</b> - {@link #dodge}, every 5 ticks (sim override {@code off:dodge}).</li>
 *   <li><b>Shoot from where they stand against shooters</b> - {@link #fireMode}: an archer facing a shooter fires
 *   STILL instead of walking in to its optimal range (sim override {@code off:hold}).</li>
 * </ul>
 * Only owned guards; every guard is looked at once a second in its slice (cheap: one small entity query).
 */
public final class FieldManual {
    private FieldManual() {
    }

    /** Simulation switches (set per run by SimExtras; false = the field manual rule is on). */
    public static boolean simNoClearance, simNoCreepers, simNoShieldUp, simNoLeftHand, simNoHold, simNoDodge;

    private static final Set<UUID> SHIELD_UP = new HashSet<>();
    private static final int CREEPER_RANGE_SHOOTER = 16, CREEPER_RANGE_SHIELD = 8;

    // ==== Shoot from where they stand against shooters ====
    /**
     * Ronda 3: against skeletons, archers and lines that fired from where they stood (STILL or the fixed VOLLEY) won
     * 97-100 % where the base won ~52 % - the base walks in to its optimal range (15 blocks), inside the skeletons'.
     * Only replaces NORMAL (no explicit order, no veteran's own choice), only for a player's shooters whose target shoots.
     */
    public static net.hfstack.rallyguard.combat.FireMode fireMode(Guard g, net.hfstack.rallyguard.combat.FireMode mode) {
        if (mode == net.hfstack.rallyguard.combat.FireMode.NORMAL && standOffActive(g)) return net.hfstack.rallyguard.combat.FireMode.VOLLEY;
        if (mode != net.hfstack.rallyguard.combat.FireMode.NORMAL || !holdsAgainst(g)) return mode;
        return net.hfstack.rallyguard.combat.FireMode.STILL;
    }

    /**
     * Only while the shooter is within the archer's own reach (its weapon's max range, 40 for a bow) and in sight.
     * Ronda 6 (waves): holding against ANY shooter left archers standing still in front of the last 1-4 skeletons of a
     * wave that stayed out of reach or out of sight - nobody hurt anybody for 45 s (13 of 28 mixed-wave archer runs
     * stuck). A 24-block limit fixed that but cost a lot against skeletons (archers 100 % -> 80-87 %, line 100 % ->
     * 67-80 %): between 24 and 40 the archer already hits them. Out of reach or hidden, it closes in as before.
     */
    private static boolean holdsAgainst(Guard g) {
        if (simNoHold || !RallyConfig.on(RallyConfig.HOLD_AGAINST_SHOOTERS)) return false;
        LivingEntity t = g.getTarget();
        if (t == null || !t.isAlive() || !(t instanceof RangedAttackMob) || GuardOwnership.getOwner(g) == null || !shooter(g)) return false;
        WeaponProfile w = WeaponProfile.forGuard(g);
        double reach = w == null ? 24.0D : w.maxRange();
        return g.distanceToSqr(t) <= reach * reach && g.hasLineOfSight(t);
    }

    // ==== Point-blank fire ====
    public static double friendClearance() {
        return simNoClearance ? 5.0D : RallyConfig.d(RallyConfig.FRIEND_CLEARANCE, 1.0D);
    }

    // ==== Creepers ====
    private static boolean shooter(Guard g) {
        return net.hfstack.rallyguard.order.Engagement.isRanged(g);
    }

    /** Any shield in the left hand - vanilla, another mod's, or our scutum: whatever Guard Villagers raises. */
    public static boolean shieldBearer(Guard g) {
        return g.getOffhandItem().canPerformAction(net.neoforged.neoforge.common.ItemAbilities.SHIELD_BLOCK);
    }

    /** Guard Villagers' blacklist keeps creepers away from guards; ours may attack them if they shoot or carry a shield. */
    public static boolean mayTargetCreeper(Guard g) {
        if (simNoCreepers || !RallyConfig.on(RallyConfig.CREEPER_TARGETING) || dodging(g)) return false;
        return GuardOwnership.getOwner(g) != null && (shooter(g) || shieldBearer(g));
    }

    /** Simulation only: guards never fight back against endermen (tag {@code off:enderman}). */
    public static boolean simNoEnderman;

    /**
     * Guard Villagers' blacklist keeps every guard off endermen (read in its config: the default list has
     * {@code minecraft:enderman}). An angry enderman then kills guards that never strike back (round 25: 3 endermen
     * against a line of 8, 0 % wins, 0 endermen killed). Ours fight back once it is angry ({@code isCreepy}: it has a
     * target), with a melee weapon; a shooter doesn't (an arrow only makes it teleport) and steps back instead.
     */
    public static boolean mayTargetEnderman(Guard g, LivingEntity target) {
        if (simNoEnderman || !RallyConfig.on(RallyConfig.ENDERMAN_FIGHT_BACK)) return false;
        return GuardOwnership.getOwner(g) != null && !shooter(g)
                && target instanceof net.minecraft.world.entity.monster.EnderMan em && em.isCreepy();
    }

    // ==== Shield up ====
    /** How long a shooter's hit keeps the shield up after the guard has lost (or never had) that shooter as target. */
    private static final int SHOT_MEMORY_TICKS = 100;
    /** An enemy shooter this close that can see the guard keeps its shield up (a skeleton shoots from ~15 blocks). */
    private static final double SHOOTER_NEAR = 24.0D;

    /** The nearest living enemy shooter within {@link #SHOOTER_NEAR} blocks that has line of sight to the guard. */
    private static LivingEntity nearestShooter(ServerLevel level, Guard g) {
        LivingEntity best = null;
        double bestD = Double.MAX_VALUE;
        double scan = Math.max(SHOOTER_NEAR, THREAT_FAR);
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, g.getBoundingBox().inflate(scan),
                e -> e.isAlive() && e != g)) {
            double d = e.distanceToSqr(g);
            if (d < bestD && fires(g, e, d) && e.hasLineOfSight(g)) {
                bestD = d;
                best = e;
            }
        }
        return best;
    }

    // ==== Shield threats (2026-11-06, docs/ESCOLTA_Y_TESTUDO.md §6) ====
    /** Mobs that breathe or throw at a guard without being a {@code RangedAttackMob}: blazes, ghasts, Ice and Fire's
     *  dragons (verified: none of them is one, so neither Guard Villagers nor this manual raised a shield at them). */
    public static final net.minecraft.tags.TagKey<net.minecraft.world.entity.EntityType<?>> SHIELD_THREATS =
            net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE,
                    net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("rallyguard", "shield_threats"));
    private static final double THREAT_NEAR = 16.0D, THREAT_FAR = 32.0D;
    /** Simulation only: the shield-threat rule off (tag {@code off:threats}). */
    public static boolean simNoThreats;

    /**
     * Does {@code e}, {@code d2} blocks squared away, fire at this guard's side? Any enemy {@code RangedAttackMob} within
     * 24 blocks (all ranks, as before). A shield threat only for a guard of rank {@code fieldManual.shieldThreatMinRank}
     * (1) or more - veterancy decides what a guard knows to fear, never how much a shield stops: within 16 blocks, and
     * within 32 two ranks above that - and only while it hunts this guard, a guard of the same commander or the
     * commander (a tamed dragon is not a threat).
     */
    private static boolean fires(Guard g, LivingEntity e, double d2) {
        if (e instanceof RangedAttackMob) return e instanceof Enemy && d2 <= SHOOTER_NEAR * SHOOTER_NEAR;
        if (simNoThreats || !e.getType().is(SHIELD_THREATS) || !(e instanceof net.minecraft.world.entity.Mob m)) return false;
        int min = RallyConfig.i(RallyConfig.SHIELD_THREAT_MIN_RANK, 1);
        int rank = net.hfstack.rallyguard.veterancy.Veterancy.rank(g);
        if (rank < min) return false;
        double reach = rank >= min + 2 ? THREAT_FAR : THREAT_NEAR;
        if (d2 > reach * reach) return false;
        LivingEntity t = m.getTarget();
        if (t == null) return false;
        UUID owner = GuardOwnership.getOwner(g);
        return t == g || (owner != null && (owner.equals(t.getUUID())
                || (t instanceof Guard tg && owner.equals(GuardOwnership.getOwner(tg)))));
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
        if (aim != null && aim.isAlive() && (aim instanceof RangedAttackMob || fires(g, aim, aim.distanceToSqr(g)))) {
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
        if (!up && Testudo.wantsUp(level, g)) { // 2026-11-06: in a testudo the shields stay up while an enemy is near
            up = true;
            why = "act:secWantsUpTestudo";
        }
        if (shieldBearer(g)) { // telemetry, once a second per shield-bearer: why the shield is (not) up
            net.hfstack.rallyguard.debug.SimProbe.count(g, aim == null ? "act:secTargetNone"
                    : aim instanceof RangedAttackMob ? "act:secTargetRanged" : "act:secTargetMelee");
            if (up) net.hfstack.rallyguard.debug.SimProbe.count(g, why);
            if (g.isBlocking()) net.hfstack.rallyguard.debug.SimProbe.count(g, "act:secBlocking");
            if (g.isUsingItem()) net.hfstack.rallyguard.debug.SimProbe.count(g, "act:secUsingItem");
        }
        if (up && firing != null && firing != g.getTarget()) { // face what fires at it (a shield guards its front only)
            net.hfstack.rallyguard.order.Escort.face(g, firing.getId(), level.getGameTime() + 30L);
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
        if (shieldBearer(g) || off.is(Items.TOTEM_OF_UNDYING)) return;
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
        DODGE_UNTIL.remove(id);
    }

    // ==== Get away from a swelling creeper ====
    // Ronda 6 (waves): explosions were THE killer of the new manual's groups in mixed waves (shields with food: 58 of
    // 58 deaths in 10 runs; archers and line 23-34). A creeper lights its 1.5 s fuse with its target within 3 blocks and
    // calls it off once that target is more than 7 away (vanilla SwellGoal) - the classic answer is to step back.
    private static final java.util.Map<UUID, Long> DODGE_UNTIL = new java.util.HashMap<>();
    private static final double DODGE_SEE = 7.0D, DODGE_RUN = 9.0D;
    private static final int DODGE_TICKS = 30;

    /** True while this guard is getting away from a creeper (its movement and creeper targeting are paused). */
    public static boolean dodging(Guard g) {
        Long until = DODGE_UNTIL.get(g.getUUID());
        if (until == null) return false;
        if (g.level().getGameTime() >= until) {
            DODGE_UNTIL.remove(g.getUUID());
            return false;
        }
        return true;
    }

    /** Every 5 ticks per guard: a creeper swelling within 7 blocks -> run 9 blocks straight away from it. */
    public static void dodge(ServerLevel level, Guard g) {
        // Riders don't: galloping away (ronda 11) left cavalry's survivors vs creepers unchanged and its wins lower
        // (36-45 % instead of 54-71 %, n = 30, two seed sets) - reverted.
        if (simNoDodge || !RallyConfig.on(RallyConfig.CREEPER_DODGE) || g.isPassenger()) return;
        if (GuardOwnership.getOwner(g) == null) return;
        Creeper near = null;
        double best = DODGE_SEE * DODGE_SEE;
        for (Creeper c : level.getEntitiesOfClass(Creeper.class, g.getBoundingBox().inflate(DODGE_SEE),
                c -> c.isAlive() && c.getSwellDir() > 0)) { // lit by a player too: its tick then sets swellDir 1
            double d = c.distanceToSqr(g);
            if (d < best) {
                best = d;
                near = c;
            }
        }
        if (near == null) return;
        net.minecraft.world.phys.Vec3 away = g.position().subtract(near.position()).multiply(1, 0, 1);
        away = away.lengthSqr() < 1.0E-4 ? new net.minecraft.world.phys.Vec3(1, 0, 0) : away.normalize();
        net.minecraft.world.phys.Vec3 dest = g.position().add(away.scale(DODGE_RUN));
        if (g.getTarget() == near) g.setTarget(null);
        g.getNavigation().moveTo(dest.x, dest.y, dest.z, 1.4D);
        if (!dodging(g)) net.hfstack.rallyguard.debug.SimProbe.count(g, "act:creeperDodge");
        long now = level.getGameTime();
        if (DODGE_UNTIL.size() > 256) DODGE_UNTIL.values().removeIf(t -> t <= now); // guards that died mid-dodge
        DODGE_UNTIL.put(g.getUUID(), now + DODGE_TICKS);
    }

    // ==== Step back from melee (2026-11-08; sonda de techo, rondas 23-24) ====
    // The oracle's "kite" was the biggest gain against hordes: 8 archers vs 60 zombies 30 % -> 100 % wins, a line of 4+4 vs
    // 55 zombies 5 % -> 100 %. It cheats in nothing (it only looks at enemies within 6 blocks), so it becomes a rule: a
    // shooter on foot with a ground melee enemy within KITE_NEAR steps straight back to KITE_FAR from it, then shoots
    // again. Not from flyers (against phantoms the oracle's kite turned fights into stand-offs: 81 % -> 54 % wins).
    private static final double KITE_NEAR = 6.0D, KITE_FAR = 11.0D;
    private static final long KITE_TICKS = 40L;
    private static final java.util.Map<java.util.UUID, Long> KITE_UNTIL = new java.util.HashMap<>();
    /** Simulation only: no stepping back (tag {@code off:kite}). */
    public static boolean simNoKite;

    /** While true, the archery movement leaves this guard's navigation alone. */
    public static boolean kiting(Guard g) {
        Long until = KITE_UNTIL.get(g.getUUID());
        if (until == null) return false;
        if (g.level().getGameTime() >= until) {
            KITE_UNTIL.remove(g.getUUID());
            return false;
        }
        return true;
    }

    // ==== Stand off from gunners (2026-11-11; rounds 36-37) ====
    // A pillager with a Pax kills archers that walk in to 40 blocks (0 % wins) and loses to the same archers firing volleys
    // from 50-80 (100 %, 67-91 % alive); "fuego normal desde 60" was 0 %: the range is what wins, the volley is what keeps
    // it. So a shooter whose target holds a firearm fires in volleys and, if it is closer than STANDOFF_MIN, walks back to
    // STANDOFF_FAR. Only firearms (Scorched Guns items): against bows and crossbows "fire from where they stand" already won.
    private static final double STANDOFF_MIN = 48.0D, STANDOFF_FAR = 56.0D;
    /** Simulation only: no stand-off (tag {@code off:standoff}). */
    public static boolean simNoStandOff;

    public static boolean gunner(LivingEntity e) {
        if (e == null) return false;
        net.minecraft.world.item.ItemStack h = e.getMainHandItem();
        return !h.isEmpty() && "scguns".equals(net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(h.getItem()).getNamespace());
    }

    private static boolean standOffActive(Guard g) {
        if (simNoStandOff || !RallyConfig.on(RallyConfig.STAND_OFF_VS_GUNS) || g.isPassenger()) return false;
        if (GuardOwnership.getOwner(g) == null || !shooter(g) || net.hfstack.rallyguard.combat.Testudo.member(g)) return false;
        LivingEntity t = g.getTarget();
        return t != null && t.isAlive() && gunner(t);
    }

    private static void standOff(ServerLevel level, Guard g) {
        if (!standOffActive(g)) return;
        LivingEntity t = g.getTarget();
        double d = g.distanceTo(t);
        if (d >= STANDOFF_MIN) return;
        net.minecraft.world.phys.Vec3 away = g.position().subtract(t.position()).multiply(1, 0, 1);
        away = away.lengthSqr() < 1.0E-4 ? new net.minecraft.world.phys.Vec3(1, 0, 0) : away.normalize();
        net.minecraft.world.phys.Vec3 dest = g.position().add(away.scale(STANDOFF_FAR - d));
        if (!g.getNavigation().moveTo(dest.x, dest.y, dest.z, 1.1D)) return; // cornered: stand and fight
        if (!kiting(g)) net.hfstack.rallyguard.debug.SimProbe.count(g, "act:standOff");
        long now = level.getGameTime();
        if (KITE_UNTIL.size() > 256) KITE_UNTIL.values().removeIf(x -> x <= now);
        KITE_UNTIL.put(g.getUUID(), now + 20L);
    }

    /** Every 5 ticks per guard (with the creeper dodge). */
    public static void kite(ServerLevel level, Guard g) {
        kiteFromMelee(level, g);
        if (!kiting(g) && !dodging(g)) standOff(level, g);
    }

    private static void kiteFromMelee(ServerLevel level, Guard g) {
        if (simNoKite || !RallyConfig.on(RallyConfig.KITE_FROM_MELEE) || g.isPassenger() || dodging(g)) return;
        if (GuardOwnership.getOwner(g) == null || net.hfstack.rallyguard.combat.Testudo.member(g)) return;
        WeaponProfile w = WeaponProfile.forGuard(g);
        if (w == null || !w.firesArrows()) return;
        LivingEntity near = null;
        double best = KITE_NEAR * KITE_NEAR;
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, g.getBoundingBox().inflate(KITE_NEAR),
                e -> e.isAlive() && e instanceof Enemy && !(e instanceof RangedAttackMob) && !e.isNoGravity()
                        && !(e instanceof net.minecraft.world.entity.FlyingMob))) {
            double d = e.distanceToSqr(g);
            if (d < best) {
                best = d;
                near = e;
            }
        }
        if (near == null) return;
        net.minecraft.world.phys.Vec3 away = g.position().subtract(near.position()).multiply(1, 0, 1);
        away = away.lengthSqr() < 1.0E-4 ? new net.minecraft.world.phys.Vec3(1, 0, 0) : away.normalize();
        net.minecraft.world.phys.Vec3 dest = g.position().add(away.scale(KITE_FAR - Math.sqrt(best)));
        if (!g.getNavigation().moveTo(dest.x, dest.y, dest.z, 1.2D)) return; // no way back there: stand and fight
        if (!kiting(g)) net.hfstack.rallyguard.debug.SimProbe.count(g, "act:kite");
        long now = level.getGameTime();
        if (KITE_UNTIL.size() > 256) KITE_UNTIL.values().removeIf(t -> t <= now); // guards that died mid-step
        KITE_UNTIL.put(g.getUUID(), now + KITE_TICKS);
    }
}

