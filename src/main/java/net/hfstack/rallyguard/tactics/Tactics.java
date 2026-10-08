package net.hfstack.rallyguard.tactics;

import net.hfstack.rallyguard.combat.FireMode;
import net.hfstack.rallyguard.combat.GuardPack;
import net.hfstack.rallyguard.combat.WeaponProfile;
import net.hfstack.rallyguard.command.SquadDirectives;
import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.hfstack.rallyguard.debug.SimProbe;
import net.hfstack.rallyguard.order.CommandTargeting;
import net.hfstack.rallyguard.order.Engagement;
import net.hfstack.rallyguard.order.GuardPosts;
import net.hfstack.rallyguard.order.KamikazeOrder;
import net.hfstack.rallyguard.order.MountDriver;
import net.hfstack.rallyguard.veterancy.Affinity;
import net.hfstack.rallyguard.veterancy.Trait;
import net.hfstack.rallyguard.veterancy.Veterancy;
import net.hfstack.rallyguard.veterancy.VeterancyEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * E2A block 4 - runs {@link DecisionLadder} for every owned guard in its once-a-second turn
 * of the stance loop (after the existing maintenance), and carries out the decision with
 * the systems that already exist: navigation through {@link MountDriver} (horse or foot),
 * targets through the combat gate (so hold fire and the {@link Engagement} cap still apply),
 * {@code GuardModes} untouched - the ladder moves guards, it never changes their mode or post.
 * <p>
 * Squads share one {@link SquadPicture} (one entity query per squad per second, done by
 * whoever asks first) and one {@link TerrainSense} reading (only when the squad's captain is
 * a Veteran, rank ≥ 2). A squad is the guards sharing a pushed directive; a guard with none is
 * its own squad.
 */
public final class Tactics {

    /** How long a guard that decided to fall back keeps refusing new targets (self-defence aside). */
    static final long RETREAT_TICKS = 100L;
    /** A new move is not re-issued for the same rung more often than this. */
    static final long MOVE_EVERY_TICKS = 40L;
    static final double PICTURE_RADIUS = 24.0D;

    /** What a squad knows, refreshed at most once a second. */
    static final class SquadPicture {
        long tick = Long.MIN_VALUE;
        int enemies, allies;
        @Nullable Vec3 enemyCentre;
        @Nullable UUID priority;
        double lastMeanDist = -1;
        int growing;
    }

    private static final Map<String, SquadPicture> PICTURES = new HashMap<>();
    private static final Map<UUID, FireMode> AUTO_STYLE = new HashMap<>();
    private static final Map<UUID, Long> RETREATING_UNTIL = new HashMap<>();
    private static final Map<UUID, long[]> LAST_MOVE = new HashMap<>(); // {rung, tick}

    // ==== Public reads ====

    /** The fire style a guard uses: the player's order if any, else its own (veterans), else NORMAL. */
    public static FireMode effectiveFireMode(Guard guard, FireMode ordered) {
        if (ordered != FireMode.NORMAL) return ordered;
        return AUTO_STYLE.getOrDefault(guard.getUUID(), FireMode.NORMAL);
    }

    public static boolean isRetreating(Guard guard) {
        Long until = RETREATING_UNTIL.get(guard.getUUID());
        if (until == null) return false;
        if (guard.level().getGameTime() > until) {
            RETREATING_UNTIL.remove(guard.getUUID());
            return false;
        }
        return true;
    }

    /** The guard's doctrine: its force's when it belongs to one (A3: inside a group it follows
     *  the group's doctrine), else its own, saved in {@link DirectiveRegistry} (E3A F6). */
    public static Doctrine doctrineOf(Guard guard) {
        if (!(guard.level() instanceof ServerLevel level)) return Doctrine.STANDARD;
        Doctrine force = net.hfstack.rallyguard.force.Forces.doctrineOf(level, guard);
        if (force != null) return force;
        return Doctrine.parse(DirectiveRegistry.get(level).doctrineOf(guard.getUUID()));
    }

    /** Block 5: the commander sets a doctrine (ladder weights) for the selected guards. */
    public static void setDoctrine(ServerLevel level, UUID guardId, Doctrine doctrine) {
        DirectiveRegistry.get(level).setDoctrine(guardId, doctrine.name());
    }

    // ==== The turn ====

    public static void step(ServerLevel level, Guard guard, DirectiveRegistry.Directive playerDirective) {
        if (KamikazeOrder.isRunning(guard)) return;
        // Idle guards (no target, nowhere to go) cost nothing: no picture, no terrain, no ladder.
        if (guard.getTarget() == null && guard.getNavigation().isDone()) {
            Unstuck.forget(guard.getUUID());
            return;
        }
        long now = level.getGameTime();
        SquadDirectives.Directive squad = SquadDirectives.of(guard.getUUID());
        String key = squad != null ? "d" + System.identityHashCode(squad) : "g" + guard.getUUID();
        SquadPicture picture = picture(level, guard, squad, key, now);

        int rank = Veterancy.rank(guard);
        boolean ranged = Engagement.isRanged(guard);
        WeaponProfile weapon = WeaponProfile.forGuard(guard);
        Doctrine doctrine = doctrineOf(guard);
        LivingEntity target = guard.getTarget();
        boolean hasTarget = target != null && target.isAlive();
        BlockPos post = CommandTargeting.postOf(guard);
        double leash = CommandTargeting.leashFor(guard);

        TerrainSense.Reading terrain = null;
        if (picture.enemyCentre != null && captainRank(level, guard, squad) >= 2) {
            terrain = TerrainSense.read(level, key, guard.position(), picture.enemyCentre, ranged, doctrine, now);
        }
        double coverDist = -1;
        if (terrain != null && terrain.best() != null) {
            coverDist = Math.sqrt(guard.blockPosition().distSqr(terrain.best().pos()));
        }

        Situation s = new Situation(rank, Veterancy.has(guard, Trait.TANK), Veterancy.has(guard, Trait.STEADY),
                Veterancy.has(guard, Trait.SCOUT), Affinity.timid(guard.getUUID()), Veterancy.isReckless(guard),
                guard.getHealth() / Math.max(1.0F, guard.getMaxHealth()),
                hasTarget, hasTarget ? guard.distanceTo(target) : -1,
                ranged, weapon == null ? 0 : weapon.minRange(), weapon == null ? 2 : weapon.optimalRange(),
                weapon == null ? 3 : weapon.maxRange(), GuardPack.isOutOfAmmo(guard), playerDirective.holdFire(),
                squad == null ? null : squad.kind(), squad != null && now - squad.issuedTick() < 600,
                post == null ? -1 : Math.sqrt(guard.blockPosition().distSqr(post)),
                hasTarget && post != null && target.distanceToSqr(Vec3.atCenterOf(post)) > leash * leash,
                coverDist, picture.priority != null,
                hasTarget && picture.priority != null && picture.priority.equals(target.getUUID()),
                picture.enemies, picture.allies, picture.growing >= 3, Unstuck.check(guard), guard.isInWater(), doctrine);

        DecisionLadder.Decision d = DecisionLadder.decide(s, rank);
        SimProbe.ladder(guard, d.rung().ordinal());
        if (rank >= 2 && d.style() != null) AUTO_STYLE.put(guard.getUUID(), d.style());
        else if (rank < 2) AUTO_STYLE.remove(guard.getUUID());
        apply(level, guard, d.rung(), s, target, post, terrain, picture, squad, now);
    }

    private static void apply(ServerLevel level, Guard guard, DecisionLadder.Rung rung, Situation s,
                              @Nullable LivingEntity target, @Nullable BlockPos post, @Nullable TerrainSense.Reading terrain,
                              SquadPicture picture, @Nullable SquadDirectives.Directive squad, long now) {
        switch (rung) {
            case UNSTUCK -> {
                Vec3 goal = target != null ? target.position() : post != null ? Vec3.atBottomCenterOf(post) : null;
                Unstuck.attempt(level, guard, goal);
            }
            case RETREAT -> {
                boolean ordered = squad != null && (squad.kind() == SquadDirectives.Kind.RETREAT
                        || squad.kind() == SquadDirectives.Kind.ROUTED);
                if (!isRetreating(guard)) {
                    RETREATING_UNTIL.put(guard.getUUID(), now + RETREAT_TICKS);
                    if (target != null) CommandTargeting.disengage(guard);
                }
                if (ordered || !moveDue(guard, rung, now)) return; // an ordered retreat already has its path
                Vec3 from = target != null ? target.position() : picture.enemyCentre;
                BlockPos dest = post;
                if (from != null && (dest == null || Vec3.atCenterOf(dest).distanceToSqr(from) < guard.distanceToSqr(from))) {
                    Vec3 away = guard.position().subtract(from).multiply(1, 0, 1);
                    if (away.lengthSqr() < 1.0E-4) away = new Vec3(1, 0, 0);
                    dest = GuardPosts.standableNear(level, guard.position().add(away.normalize().scale(12.0D)));
                }
                if (dest != null) MountDriver.moveTo(guard, dest, 1.25D);
            }
            case RETURN_TO_SLOT -> {
                if (post == null || !moveDue(guard, rung, now)) return;
                if (target != null) CommandTargeting.disengage(guard);
                MountDriver.moveTo(guard, post, 1.0D);
            }
            case TAKE_COVER -> {
                if (terrain == null || terrain.best() == null || !moveDue(guard, rung, now)) return;
                MountDriver.moveTo(guard, terrain.best().pos(), 1.1D);
            }
            case REPOSITION_AWAY -> {
                if (target == null || !moveDue(guard, rung, now)) return;
                Vec3 away = guard.position().subtract(target.position()).multiply(1, 0, 1);
                if (away.lengthSqr() < 1.0E-4) return;
                Vec3 spot = target.position().add(away.normalize().scale(s.minRange() + 3.0D));
                MountDriver.moveTo(guard, GuardPosts.standableNear(level, spot), 1.2D);
            }
            case REPOSITION_CLOSER -> {
                if (target == null || !moveDue(guard, rung, now)) return;
                if (post != null && s.targetBeyondLeash()) return; // a posted guard doesn't leave its leash
                Vec3 toward = guard.position().subtract(target.position()).multiply(1, 0, 1);
                if (toward.lengthSqr() < 1.0E-4) return;
                Vec3 spot = target.position().add(toward.normalize().scale(Math.max(s.minRange() + 1.0D, s.optimalRange())));
                MountDriver.moveTo(guard, GuardPosts.standableNear(level, spot), 1.0D);
            }
            case SWITCH_PRIORITY -> {
                if (picture.priority == null) return;
                Entity e = level.getEntity(picture.priority);
                if (e instanceof LivingEntity p && p.isAlive() && CommandTargeting.wouldAccept(guard, p)) {
                    guard.setTarget(p); // through the gate: hold fire, leash and the engagement cap apply
                }
            }
            default -> {
                // OBEY_DIRECTIVE, SEEK_TARGET (the stance loop finds one), HOLD_FORMATION,
                // FIRE, ADVANCE, NONE: Guard Villagers' goals and Rally's fire already do it.
            }
        }
    }

    private static boolean moveDue(Guard guard, DecisionLadder.Rung rung, long now) {
        long[] last = LAST_MOVE.get(guard.getUUID());
        if (last != null && last[0] == rung.ordinal() && now - last[1] < MOVE_EVERY_TICKS) return false;
        LAST_MOVE.put(guard.getUUID(), new long[]{rung.ordinal(), now});
        return true;
    }

    private static int captainRank(ServerLevel level, Guard guard, @Nullable SquadDirectives.Directive squad) {
        if (squad == null || squad.captain() == null) return Veterancy.rank(guard);
        Entity captain = level.getEntity(squad.captain());
        return captain instanceof Guard c ? Veterancy.rank(c) : Veterancy.rank(guard);
    }

    /** The squad's picture: one entity query per squad per second, shared by its members. */
    private static SquadPicture picture(ServerLevel level, Guard guard, @Nullable SquadDirectives.Directive squad,
                                        String key, long now) {
        SquadPicture p = PICTURES.computeIfAbsent(key, k -> new SquadPicture());
        if (now - p.tick < 20) return p;
        p.tick = now;
        p.enemies = 0;
        p.allies = 0;
        p.priority = null;
        double cx = 0, cy = 0, cz = 0, distSum = 0, bestPriority = Double.MAX_VALUE;
        AABB box = guard.getBoundingBox().inflate(PICTURE_RADIUS);
        List<LivingEntity> near = level.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::isAlive);
        for (LivingEntity e : near) {
            if (e instanceof Guard g && (g == guard || (squad != null && SquadDirectives.of(g.getUUID()) == squad))) {
                p.allies++;
                continue;
            }
            if (!VeterancyEvents.realEnemy(guard, e)) continue;
            p.enemies++;
            cx += e.getX();
            cy += e.getY();
            cz += e.getZ();
            double d2 = e.distanceToSqr(guard);
            distSum += Math.sqrt(d2);
            if (isPriority(e) && d2 < bestPriority) {
                bestPriority = d2;
                p.priority = e.getUUID();
            }
        }
        if (p.enemies == 0) {
            p.enemyCentre = null;
            p.lastMeanDist = -1;
            p.growing = 0;
            return p;
        }
        p.enemyCentre = new Vec3(cx / p.enemies, cy / p.enemies, cz / p.enemies);
        double mean = distSum / p.enemies;
        p.growing = p.lastMeanDist >= 0 && mean > p.lastMeanDist + 0.5D ? p.growing + 1 : 0;
        p.lastMeanDist = mean;
        return p;
    }

    /** Flyer, ranged, explosive or "boss" (≥ 60 max health) - playbook rung 6. */
    static boolean isPriority(LivingEntity e) {
        return e instanceof FlyingMob || (e.isNoGravity() && !e.onGround()) || e instanceof RangedAttackMob
                || e instanceof Creeper || e.getMaxHealth() >= 60.0F;
    }

    // ==== Lifecycle ====

    @SubscribeEvent
    public void onExplosion(ExplosionEvent.Detonate event) {
        if (event.getLevel().isClientSide() || event.getAffectedBlocks().isEmpty()) return;
        TerrainSense.invalidateNear(Vec3.atCenterOf(event.getAffectedBlocks().get(0)));
    }

    /** Once a minute: drop squad pictures and readings nobody asked for in a while. */
    public static void purge(long now) {
        PICTURES.values().removeIf(p -> now - p.tick > 20L * 30L);
        LAST_MOVE.values().removeIf(m -> now - m[1] > 20L * 60L);
        TerrainSense.purge(now);
    }

    public static void forget(UUID guardId) {
        AUTO_STYLE.remove(guardId);
        RETREATING_UNTIL.remove(guardId);
        LAST_MOVE.remove(guardId);
        Unstuck.forget(guardId);
    }

    public static void clearAll() {
        PICTURES.clear();
        AUTO_STYLE.clear();
        RETREATING_UNTIL.clear();
        LAST_MOVE.clear();
        TerrainSense.clearAll();
        Unstuck.clearAll();
    }
}
