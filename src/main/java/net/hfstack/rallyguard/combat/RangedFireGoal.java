package net.hfstack.rallyguard.combat;

import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.contract.SquadRegistry;
import net.hfstack.rallyguard.order.CommandTargeting;
import net.hfstack.rallyguard.order.KamikazeOrder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.EnumSet;
import java.util.UUID;

/**
 * E1-G: Rally's own archery for owned guards with a bow or crossbow - the shots Guard
 * Villagers' goals can't make. GV's {@code GuardBowAttack} only shoots within 15 blocks
 * (its attack radius, read from its source) and its crossbow goal within its config
 * radius; this goal covers:
 * <ul>
 *     <li><b>long shots</b>: a target beyond that radius but within the weapon's
 *     {@code maxRange}, aimed on the flat arc, leading a moving target;</li>
 *     <li><b>volleys</b>: posted guards (Hold Ground / a formation) or guards in VOLLEY
 *     mode shoot as far as the weapon physically throws ({@link WeaponProfile#volleyReach}),
 *     on the low arc while they see the target (it climbs to the max-range angle by itself)
 *     and on the steep arc only over what hides it (a target the guard knows of: it's its
 *     target, which an order or a squadmate's sighting gave it);</li>
 *     <li><b>fire modes</b> ({@link FireMode}): how the guard moves while shooting.</li>
 * </ul>
 * Shots use the same vanilla calls Guard Villagers uses ({@code ProjectileUtil.getMobArrow},
 * {@code BowItem#customArrow}, {@code AbstractArrow#shoot}); the pitch comes from
 * {@link Ballistics}. Mandatory limits: at least 30 ticks between shots per archer,
 * a per-squad volley budget per second (config), never if a friend stands within 5 blocks
 * of the landing point ({@link FriendIndex}, no entity scans), every arrow paid from the
 * guard's ammo ({@link Ammo}), arrows can't be picked up. Firearms never volley.
 * <p>
 * The goal holds only the LOOK flag: while it runs, Guard Villagers' bow/melee goals
 * (MOVE + LOOK) wait, and the movement each fire mode wants is driven here.
 */
public final class RangedFireGoal extends Goal {

    private static final double FRIEND_CLEARANCE = 5.0D;

    private final Guard guard;
    private int cooldown;
    private int repath;

    public RangedFireGoal(Guard guard) {
        this.guard = guard;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    private record Plan(LivingEntity target, WeaponProfile weapon, FireMode mode, double dist,
                        boolean los, boolean volleyAllowed, BlockPos post) {
    }

    private Plan plan() {
        if (!RallyConfig.on(RallyConfig.RANGED_FIRE)) return null;
        if (!(guard.level() instanceof ServerLevel level) || !GuardOwnership.hasOwner(guard)) return null;
        LivingEntity target = guard.getTarget();
        if (target == null || !target.isAlive() || KamikazeOrder.isRunning(guard)) return null;
        WeaponProfile weapon = WeaponProfile.forGuard(guard);
        if (weapon == null || !weapon.firesArrows()) return null;
        if (RallyConfig.on(RallyConfig.FINITE_ARROWS) && !Ammo.available(guard, Ammo.ARROWS)) return null;

        // E2A: the player's ordered style wins; otherwise a veteran's own choice (Tactics).
        FireMode mode = net.hfstack.rallyguard.tactics.Tactics.effectiveFireMode(guard,
                FireMode.parse(DirectiveRegistry.get(level).directiveOf(guard.getUUID()).fireMode()));
        BlockPos post = CommandTargeting.postOf(guard);
        boolean volleyAllowed = mode == FireMode.VOLLEY || (post != null && mode != FireMode.ADVANCING);
        double dist = guard.distanceTo(target);
        // E3A F3: a volley reaches as far as the weapon physically throws (Ballistics), 5 % short.
        double reach = volleyAllowed ? weapon.volleyReach() : weapon.maxRange();
        reach *= MasteryEffects.reachFactor(guard, guard.getMainHandItem()); // E3A A8
        if (dist > reach) return null;
        boolean los = guard.getSensing().hasLineOfSight(target);
        // E2A B3: Rally fires EVERY bow/crossbow shot of an owned guard (also inside Guard
        // Villagers' 15-block radius), so all shots use the real ammo, the weapon's
        // properties and the guard's rank; GV's own bow goal waits while this one runs.
        return new Plan(target, weapon, mode, dist, los, volleyAllowed, post);
    }

    @Override
    public boolean canUse() {
        return plan() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return plan() != null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        cooldown = 10;
        repath = 0;
    }

    @Override
    public void tick() {
        long t0 = net.hfstack.rallyguard.debug.Perf.t0();
        try {
            tickInner();
        } finally {
            net.hfstack.rallyguard.debug.Perf.add(net.hfstack.rallyguard.debug.Perf.Sys.RANGED_GOAL, t0);
        }
    }

    private void tickInner() {
        Plan p = plan();
        if (p == null) return;
        guard.getLookControl().setLookAt(p.target(), 30.0F, 30.0F);
        if (--repath <= 0) {
            repath = 10;
            move(p);
        }
        if (--cooldown > 0) return;
        cooldown = fire(p) ? (int) Math.max(5, Math.round(RallyConfig.i(RallyConfig.RANGED_COOLDOWN_TICKS, 30)
                * MasteryEffects.cooldownFactor(guard, guard.getMainHandItem()))) : 10; // E3A A8: cadence/reload
    }

    private void move(Plan p) {
        if (guard.isPassenger()) return; // the mount driver steers riders
        var nav = guard.getNavigation();
        Vec3 me = guard.position();
        Vec3 them = p.target().position();
        Vec3 away = me.subtract(them);
        away = away.lengthSqr() < 1.0E-4 ? new Vec3(1, 0, 0) : away.normalize();
        switch (p.mode()) {
            case STILL, VOLLEY -> nav.stop();
            case ADVANCING -> {
                if (p.dist() > p.weapon().minRange() + 2.0D) go(clampToLeash(p, them), 0.7D);
                else nav.stop();
            }
            case RETREATING -> {
                if (p.dist() < p.weapon().minRange() + 3.0D) go(clampToLeash(p, me.add(away.scale(6.0D))), 1.0D);
                else nav.stop();
            }
            case NORMAL -> {
                if (p.dist() > p.weapon().optimalRange() && p.los()) {
                    go(clampToLeash(p, them.add(away.scale(p.weapon().optimalRange()))), 0.8D);
                } else if (!p.los() && !p.volleyAllowed()) {
                    go(clampToLeash(p, them), 0.8D); // find a line of sight
                } else {
                    nav.stop();
                }
            }
        }
    }

    private void go(Vec3 dest, double speed) {
        guard.getNavigation().moveTo(dest.x, dest.y, dest.z, speed);
    }

    /** A posted guard never walks further from its post than its (weapon) leash. */
    private Vec3 clampToLeash(Plan p, Vec3 dest) {
        if (p.post() == null) return dest;
        Vec3 post = Vec3.atBottomCenterOf(p.post());
        double leash = CommandTargeting.leashFor(guard);
        Vec3 off = dest.subtract(post);
        return off.length() <= leash ? dest : post.add(off.normalize().scale(leash));
    }

    /** Fires one arrow if every limit allows it; returns true if it did. */
    private boolean fire(Plan p) {
        // 2026-11-04 (torneo ronda 1): the steep arc is ONLY for targets the archer can't see (over a wall or a
        // hill). In the open the low solution reaches just as far - Ballistics' low arc climbs up to the
        // max-range angle (~42 deg) on its own - and it lands in a fraction of the time and much tighter. The
        // old rule (VOLLEY = steep arc beyond 15 blocks) lobbed near-vertical arrows that flew ~5 s while the
        // target walked away: 8-22 % hits against 85-99 % for every other fire mode (descarte, 6 runs per row).
        boolean high = !p.los();
        if (high && !p.volleyAllowed()) return noShot("noLos");
        ServerLevel level = (ServerLevel) guard.level();
        UUID owner = GuardOwnership.getOwner(guard);
        ItemStack weapon = guard.getMainHandItem();
        // E2A B3: the arrow is made from the REAL ammo item, the way a player's shot is.
        AbstractArrow arrow = ArrowFactory.create(level, guard, weapon, Ammo.nextRound(guard, Ammo.ARROWS));

        // Lead the target by the flight time: estimate it, solve, then lead again with the solved flight time
        // (a long or steep shot flies many times longer than the straight-line estimate).
        LivingEntity target = p.target();
        double speed = p.weapon().arrowSpeed();
        Vec3 centre = new Vec3(target.getX(), target.getY(0.5D), target.getZ());
        Vec3 walk = target.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D);
        double flightTicks = Math.max(1.0D, p.dist() / (speed * 0.9D));
        Vec3 aim = centre;
        double hx = 0, hz = 0, hd = 0;
        Ballistics.Shot shot = null;
        for (int pass = 0; pass < 2; pass++) {
            aim = centre.add(walk.scale(flightTicks));
            hx = aim.x - arrow.getX();
            hz = aim.z - arrow.getZ();
            hd = Math.sqrt(hx * hx + hz * hz);
            shot = Ballistics.solve(hd, aim.y - arrow.getY(), speed, high);
            if (shot == null) break;
            flightTicks = shot.ticks();
        }
        if (shot == null) return noShot("noSolution");
        double clearance = net.hfstack.rallyguard.debug.sim.SimGvConfig.clearance(FieldManual.friendClearance()); // torneo: measured per run; field manual default 1
        if (owner != null && clearance > 0.0D && FriendIndex.friendNear(level, owner, aim, clearance)) return noShot("friendNearAim");
        if (high && !RangedFire.tryVolley(owner, squadKey(level))) return noShot("volleyBudget");
        if (RallyConfig.on(RallyConfig.FINITE_ARROWS) && Ammo.consume(guard, Ammo.ARROWS, 1) == 0) return noShot("noAmmo");
        net.hfstack.rallyguard.debug.SimProbe.count(guard, high ? "act:shotHigh" : p.dist() > p.weapon().directRange()
                ? "act:shotLong" : "act:shotNear");

        // E2A B5: spread grows with distance and shrinks with the archer's rank.
        int rank = net.hfstack.rallyguard.veterancy.Veterancy.rank(guard);
        double tighter = Math.max(RallyConfig.d(RallyConfig.SPREAD_RANK_FLOOR, 0.3D), // E3B R7 (C1): floor configurable
                1.0D - rank * RallyConfig.d(RallyConfig.VOLLEY_SPREAD_PER_RANK, 0.12D));
        float spread = (float) ((high ? 1.5D + p.dist() * 0.05D : 0.5D + p.dist() * 0.02D) * tighter
                * MasteryEffects.spreadFactor(guard, guard.getMainHandItem())); // E3A A8
        arrow.shoot(hx, hd * Math.tan(shot.pitch()), hz, (float) speed, spread);
        RangedFire.spawnRallyArrow(level, arrow);
        net.hfstack.rallyguard.debug.SimProbe.shot(guard);
        guard.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (guard.getRandom().nextFloat() * 0.4F + 0.8F));
        return true;
    }

    /** A shot the archer wanted but didn't take, counted by reason in the simulation (nothing outside one). */
    private boolean noShot(String reason) {
        net.hfstack.rallyguard.debug.SimProbe.count(guard, "act:noShot:" + reason);
        return false;
    }

    private String squadKey(ServerLevel level) {
        return SquadRegistry.get(level).squadOf(guard.getUUID()).map(s -> s.id.toString())
                .orElse(guard.getUUID().toString());
    }
}
