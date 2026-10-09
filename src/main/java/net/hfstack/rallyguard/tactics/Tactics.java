package net.hfstack.rallyguard.tactics;

import net.hfstack.rallyguard.combat.FireMode;
import net.hfstack.rallyguard.combat.GuardPack;
import net.hfstack.rallyguard.combat.WeaponProfile;
import net.hfstack.rallyguard.command.SquadDirectives;
import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.hfstack.rallyguard.debug.SimProbe;
import net.hfstack.rallyguard.order.CommandTargeting;
import net.hfstack.rallyguard.order.Engagement;
import net.hfstack.rallyguard.order.GuardPosts;
import net.hfstack.rallyguard.order.KamikazeOrder;
import net.hfstack.rallyguard.order.MountDriver;
import net.hfstack.rallyguard.veterancy.Genetics;
import net.hfstack.rallyguard.veterancy.Trait;
import net.hfstack.rallyguard.veterancy.Veterancy;
import net.hfstack.rallyguard.veterancy.VeterancyEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
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
 * E2A block 4 / E3A V - runs {@link DecisionLadder} for every owned guard in its
 * once-a-second turn of the stance loop (after the existing maintenance), and carries out
 * the decision with the systems that already exist: navigation through {@link MountDriver}
 * (horse or foot), targets through the combat gate (so hold fire and the {@link Engagement}
 * cap still apply), weapons through the pack - {@code GuardModes} untouched: the ladder moves
 * guards, it never changes their mode or post.
 * <p>
 * Squads share one {@link SquadPicture} (one entity query per squad per second) and one
 * {@link TerrainSense} reading (only when the squad's captain is rank ≥ 2, Experienced). A
 * squad is the guards sharing a pushed directive; a guard with none is its own squad.
 * Rank-5 guards use {@link AdvancedCore} while the per-tick Perf budget
 * ({@code veterancy.advancedBudgetMs}) allows and an enemy is known; past the budget they
 * fall back to the ladder (reason {@link DecisionReason#DEGRADED_BUDGET}).
 */
public final class Tactics {

    /** How long a guard that decided to fall back keeps refusing new targets (self-defence aside). */
    static final long RETREAT_TICKS = 100L;
    /** A new move is not re-issued for the same rung more often than this. */
    static final long MOVE_EVERY_TICKS = 40L;
    static final double PICTURE_RADIUS = 24.0D;
    static final double SCOUT_PICTURE_RADIUS = 32.0D;

    /** What a squad knows, refreshed at most once a second. */
    static final class SquadPicture {
        // E3B R1 (A1): was Long.MIN_VALUE - "now - tick" overflowed negative, so the picture was never
        // filled (no enemies, no terrain, no priority, no rank-5 core). Any value well in the past works.
        long tick = -1000L;
        int enemies, allies, threats;
        @Nullable Vec3 enemyCentre;
        @Nullable UUID priority, focus;
        boolean priorityAir;
        double lastMeanDist = -1;
        int growing;
    }

    private static final Map<String, SquadPicture> PICTURES = new HashMap<>();
    private static final Map<UUID, FireMode> AUTO_STYLE = new HashMap<>();
    private static final Map<UUID, Long> RETREATING_UNTIL = new HashMap<>();
    private static final Map<UUID, long[]> LAST_MOVE = new HashMap<>(); // {rung, tick}
    private static final Map<UUID, String> SQUAD_KEY = new HashMap<>();
    /** E3B R1 (A2): tick the ladder last changed a guard's target; (A6) last weapon swap; (A5) latched retreats. */
    private static final Map<UUID, Long> RETARGET_AT = new HashMap<>();
    private static final Map<UUID, Long> SWAP_AT = new HashMap<>();
    private static final java.util.Set<UUID> RETREAT_LATCH = new java.util.HashSet<>();
    /** E3B R1: last rung per guard (rung changes are counted in simulations: ping-pong alarm). */
    private static final Map<UUID, Integer> LAST_RUNG = new HashMap<>();
    static final long RETARGET_COOLDOWN = 60L;
    static final long SWAP_COOLDOWN = 100L;
    /** V5 budget: {tick, nanos spent in the advanced core this tick}. */
    private static final long[] ADVANCED = new long[2];

    // ==== Public reads ====

    /** The fire style a guard uses: the player's order if any, else its own (alone, rank ≥ 2), else NORMAL. */
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
        // E3B G3: an instruction in force sets the doctrine its subgroup interpreted.
        Doctrine instructed = net.hfstack.rallyguard.command.InstructionRunner.doctrineOverride(guard.getUUID());
        if (instructed != null) return instructed;
        Doctrine force = net.hfstack.rallyguard.force.Forces.doctrineOf(level, guard);
        if (force != null) return force;
        return Doctrine.parse(DirectiveRegistry.get(level).doctrineOf(guard.getUUID()));
    }

    /** Block 5: the commander sets a doctrine (ladder weights) for the selected guards. */
    public static void setDoctrine(ServerLevel level, UUID guardId, Doctrine doctrine) {
        DirectiveRegistry.get(level).setDoctrine(guardId, doctrine.name());
    }

    /** V1/V2: {enemies, allies} the guard's squad saw in the last 2 s, or null. */
    @Nullable
    public static int[] odds(Guard guard) {
        String key = SQUAD_KEY.get(guard.getUUID());
        SquadPicture p = key == null ? null : PICTURES.get(key);
        if (p == null || guard.level().getGameTime() - p.tick > 40) return null;
        return new int[]{p.enemies, Math.max(1, p.allies)};
    }

    /** A2: where the guard's squad last saw the enemy (null = none in the last 2 s). */
    @Nullable
    public static Vec3 enemyCentre(Guard guard) {
        String key = SQUAD_KEY.get(guard.getUUID());
        SquadPicture p = key == null ? null : PICTURES.get(key);
        if (p == null || guard.level().getGameTime() - p.tick > 40) return null;
        return p.enemyCentre;
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
        int rankEarly = Veterancy.rank(guard);
        // E3B R8 (B12): a rank-5 guard's WHOLE step (picture + terrain + decision) is measured against the
        // budget; once this tick's budget is spent, rank-5 guards degrade to the normal ladder.
        long stepStart = rankEarly >= 5 ? System.nanoTime() : 0L;
        if (rankEarly >= 5 && ADVANCED[0] != now) {
            ADVANCED[0] = now;
            ADVANCED[1] = 0;
        }
        SquadDirectives.Directive squad = SquadDirectives.of(guard.getUUID());
        // E3B R8 (B11): guards in no squad share one picture per commander and 32×32 cell (was one 48³ query
        // per guard per second).
        java.util.UUID cellOwner = net.hfstack.rallyguard.contract.GuardOwnership.getOwner(guard);
        String key = squad != null ? "d" + System.identityHashCode(squad)
                : "c" + cellOwner + "#" + (guard.getBlockX() >> 5) + "," + (guard.getBlockZ() >> 5);
        SQUAD_KEY.put(guard.getUUID(), key);
        int rank = Veterancy.rank(guard);
        SquadPicture picture = picture(level, guard, squad, key, now,
                Veterancy.has(guard, Trait.SCOUT) ? SCOUT_PICTURE_RADIUS : PICTURE_RADIUS);

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
            SimProbe.count(guard, "terrainReads");
        }
        double coverDist = -1;
        boolean highGround = false;
        if (terrain != null && terrain.best() != null) {
            coverDist = Math.sqrt(guard.blockPosition().distSqr(terrain.best().pos()));
            highGround = terrain.best().tags().contains(TerrainSense.Tag.HEIGHT_GOOD);
        }
        LivingEntity hurtBy = guard.getLastHurtByMob();
        boolean attackerNotTarget = hurtBy != null && hurtBy.isAlive() && hurtBy != target
                && guard.tickCount - guard.getLastHurtByMobTimestamp() < 60 && hurtBy.distanceToSqr(guard) < 16.0D
                && VeterancyEvents.realEnemy(guard, hurtBy);
        boolean focusAvailable = hasTarget && picture.focus != null && !picture.focus.equals(target.getUUID());

        // V5 budget: the advanced core only with a known enemy and while this tick's budget lasts.
        boolean advanced = rank >= 5 && picture.enemies > 0
                && ADVANCED[1] / 1.0e6 < RallyConfig.d(RallyConfig.ADVANCED_BUDGET_MS, 0.25D);

        // E3B R1 (A3): allies = own guards really near (not only those sharing the directive).
        java.util.UUID owner = net.hfstack.rallyguard.contract.GuardOwnership.getOwner(guard);
        int allies = owner == null ? picture.allies
                : Math.max(picture.allies, net.hfstack.rallyguard.combat.FriendIndex.countNear(level, owner, guard.position(), 16.0D));
        boolean retargetReady = now - RETARGET_AT.getOrDefault(guard.getUUID(), -1000L) >= RETARGET_COOLDOWN;
        boolean swapReady = now - SWAP_AT.getOrDefault(guard.getUUID(), -1000L) >= SWAP_COOLDOWN;
        boolean retreating = isRetreating(guard) || RETREAT_LATCH.contains(guard.getUUID());

        Situation s = new Situation(rank, Veterancy.traitBits(guard), Genetics.timid(guard),
                Veterancy.isReckless(guard), guard.isPassenger(),
                guard.getHealth() / Math.max(1.0F, guard.getMaxHealth()),
                hasTarget, hasTarget ? guard.distanceTo(target) : -1,
                ranged, weapon == null ? 0 : weapon.minRange(), weapon == null ? 2 : weapon.optimalRange(),
                weapon == null ? 3 : weapon.maxRange(), GuardPack.isOutOfAmmo(guard), playerDirective.holdFire(),
                squad == null ? null : squad.kind(), squad != null && now - squad.issuedTick() < 600,
                squad != null && picture.allies > 1,
                post == null ? -1 : Math.sqrt(guard.blockPosition().distSqr(post)),
                hasTarget && post != null && target.distanceToSqr(Vec3.atCenterOf(post)) > leash * leash,
                coverDist, highGround, picture.priority != null, picture.priorityAir,
                hasTarget && picture.priority != null && picture.priority.equals(target.getUUID()), focusAvailable,
                attackerNotTarget, picture.enemies, allies, picture.growing >= 3,
                swapReady && rank >= 1 && GuardPack.hasMelee(guard), swapReady && rank >= 4 && GuardPack.hasRangedWithAmmo(guard),
                Unstuck.check(guard), guard.isInWater(), now / 20L,
                (guard.getUUID().getLeastSignificantBits() & 1L) == 0L, advanced,
                squad == null ? 1.0D : net.hfstack.rallyguard.force.Leadership.toleranceFactor(guard), doctrine,
                picture.threats, retargetReady, retreating);
        if (RETREAT_LATCH.contains(guard.getUUID())
                && s.health() >= DecisionLadder.retreatHealth(s) + DecisionLadder.REENTRY_MARGIN) {
            RETREAT_LATCH.remove(guard.getUUID()); // E3B R1 (A5): healed enough to fight again
        }

        DecisionLadder.Decision d = DecisionLadder.decide(s, rank);
        if (rankEarly >= 5) ADVANCED[1] += System.nanoTime() - stepStart; // E3B R8: the whole step counts
        DecisionReason reason = rank >= 5 && picture.enemies > 0 && !advanced ? DecisionReason.DEGRADED_BUDGET : d.reason();
        DecisionTrace.record(guard, d.rung(), reason);
        SimProbe.ladder(guard, d.rung().ordinal());
        if (advanced) SimProbe.count(guard, "coreDecisions");
        if (d.reason() == DecisionReason.OUTNUMBERED && d.rung() == DecisionLadder.Rung.RETREAT) SimProbe.count(guard, "outnumberedRetreats");
        Integer lastRung = LAST_RUNG.put(guard.getUUID(), d.rung().ordinal());
        if (lastRung != null && lastRung != d.rung().ordinal()) SimProbe.count(guard, "rungChanges");
        if (rank >= 2 && d.style() != null) AUTO_STYLE.put(guard.getUUID(), d.style());
        else if (rank < 2 || s.inGroup()) AUTO_STYLE.remove(guard.getUUID()); // in a group the group's style rules
        apply(level, guard, d.rung(), d.reason(), s, target, post, terrain, picture, squad, hurtBy, now);
    }

    private static void apply(ServerLevel level, Guard guard, DecisionLadder.Rung rung, DecisionReason why, Situation s,
                              @Nullable LivingEntity target, @Nullable BlockPos post, @Nullable TerrainSense.Reading terrain,
                              SquadPicture picture, @Nullable SquadDirectives.Directive squad,
                              @Nullable LivingEntity hurtBy, long now) {
        switch (rung) {
            case UNSTUCK -> {
                Vec3 goal = target != null ? target.position() : post != null ? Vec3.atBottomCenterOf(post) : null;
                Unstuck.attempt(level, guard, goal);
            }
            case RETREAT -> {
                boolean ordered = squad != null && (squad.kind() == SquadDirectives.Kind.RETREAT
                        || squad.kind() == SquadDirectives.Kind.ROUTED);
                if (why == DecisionReason.LOW_HEALTH_RETREAT) RETREAT_LATCH.add(guard.getUUID()); // E3B R1 (A5)
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
                if (target != null && s.targetBeyondLeash()) CommandTargeting.disengage(guard);
                MountDriver.moveTo(guard, post, 1.0D);
            }
            case TAKE_COVER, TAKE_POSITION -> {
                if (why == DecisionReason.LOW_HEALTH_RETREAT) {
                    // E3B R1 (A4): cover chosen as the retreat's destination - it IS a retreat.
                    if (!isRetreating(guard)) RETREATING_UNTIL.put(guard.getUUID(), now + RETREAT_TICKS);
                    RETREAT_LATCH.add(guard.getUUID());
                    if (target != null) CommandTargeting.disengage(guard);
                }
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
            case SCOOT, PAIR_BOUND -> {
                // Step sideways 3-4 blocks across the line to the target, keeping the target.
                if (target == null || !moveDue(guard, rung, now)) return;
                if (post != null && guard.distanceToSqr(Vec3.atCenterOf(post)) > 36.0D) return;
                Vec3 line = target.position().subtract(guard.position()).multiply(1, 0, 1);
                if (line.lengthSqr() < 1.0E-4) return;
                Vec3 side = new Vec3(-line.z, 0, line.x).normalize().scale(((now / 40L) % 2 == 0 ? 1 : -1) * 3.5D);
                MountDriver.moveTo(guard, GuardPosts.standableNear(level, guard.position().add(side)), 1.15D);
            }
            case SWITCH_PRIORITY -> retarget(level, guard, picture.priority);
            case FOCUS_FIRE -> retarget(level, guard, picture.focus);
            case COUNTER -> {
                if (hurtBy != null && hurtBy.isAlive() && CommandTargeting.wouldAccept(guard, hurtBy)) guard.setTarget(hurtBy);
            }
            case SWAP_WEAPON -> {
                SWAP_AT.put(guard.getUUID(), now); // E3B R1 (A6): ≥ 5 s between swaps
                GuardPack.chooseWeapon(guard, s.ranged() || s.outOfAmmo()); // ranged in hand -> melee, and back
            }
            default -> {
                // OBEY_DIRECTIVE, SEEK_TARGET (the stance loop finds one), HOLD_FORMATION,
                // FIRE, ADVANCE, NONE: Guard Villagers' goals and Rally's fire already do it.
            }
        }
    }

    /** Through the gate: hold fire, leash and the engagement cap apply. */
    private static void retarget(ServerLevel level, Guard guard, @Nullable UUID id) {
        if (id == null) return;
        Entity e = level.getEntity(id);
        if (e instanceof LivingEntity p && p.isAlive() && CommandTargeting.wouldAccept(guard, p)) {
            guard.setTarget(p);
            RETARGET_AT.put(guard.getUUID(), level.getGameTime()); // E3B R1 (A2)
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
                                        String key, long now, double radius) {
        SquadPicture p = PICTURES.computeIfAbsent(key, k -> new SquadPicture());
        if (now - p.tick < 20) return p;
        p.tick = now;
        p.enemies = 0;
        p.allies = 0;
        p.threats = 0;
        java.util.UUID owner = net.hfstack.rallyguard.contract.GuardOwnership.getOwner(guard);
        p.priority = null;
        p.focus = null;
        p.priorityAir = false;
        double cx = 0, cy = 0, cz = 0, distSum = 0, bestPriority = Double.MAX_VALUE;
        Map<UUID, Integer> targeted = new HashMap<>();
        AABB box = guard.getBoundingBox().inflate(radius);
        List<LivingEntity> near = level.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::isAlive);
        for (LivingEntity e : near) {
            if (e instanceof Guard g && (g == guard || (squad != null && SquadDirectives.of(g.getUUID()) == squad))) {
                p.allies++;
                if (g.getTarget() != null && g.getTarget().isAlive()) targeted.merge(g.getTarget().getUUID(), 1, Integer::sum);
                continue;
            }
            if (!VeterancyEvents.realEnemy(guard, e)) continue;
            p.enemies++;
            // E3B R1 (A3): a threat = hunting our side, or close enough to matter (12 blocks).
            boolean hunting = e instanceof net.minecraft.world.entity.Mob mob && mob.getTarget() != null && owner != null
                    && (mob.getTarget().getUUID().equals(owner) || net.hfstack.rallyguard.contract.GuardOwnership.isOwnedBy(mob.getTarget(), owner));
            if (hunting || e.distanceToSqr(guard) <= 144.0D) p.threats++;
            cx += e.getX();
            cy += e.getY();
            cz += e.getZ();
            double d2 = e.distanceToSqr(guard);
            distSum += Math.sqrt(d2);
            if (isPriority(e) && d2 < bestPriority) {
                bestPriority = d2;
                p.priority = e.getUUID();
                p.priorityAir = flies(e);
            }
        }
        int best = 1;
        for (Map.Entry<UUID, Integer> t : targeted.entrySet()) {
            if (t.getValue() > best) {
                best = t.getValue();
                p.focus = t.getKey();
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

    static boolean flies(LivingEntity e) {
        return e instanceof FlyingMob || (e.isNoGravity() && !e.onGround());
    }

    /** Flyer, ranged, explosive or "boss" (≥ 60 max health) - playbook rung 6. */
    static boolean isPriority(LivingEntity e) {
        return flies(e) || e instanceof RangedAttackMob || e instanceof Creeper || e.getMaxHealth() >= 60.0F;
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
        RETARGET_AT.values().removeIf(t -> now - t > 20L * 60L);
        SWAP_AT.values().removeIf(t -> now - t > 20L * 60L);
        TerrainSense.purge(now);
        VeterancyEvents.purge(now);
    }

    public static void forget(UUID guardId) {
        RETARGET_AT.remove(guardId);
        SWAP_AT.remove(guardId);
        RETREAT_LATCH.remove(guardId);
        LAST_RUNG.remove(guardId);
        AUTO_STYLE.remove(guardId);
        RETREATING_UNTIL.remove(guardId);
        LAST_MOVE.remove(guardId);
        SQUAD_KEY.remove(guardId);
        Unstuck.forget(guardId);
        DecisionTrace.forget(guardId);
    }

    public static void clearAll() {
        RETARGET_AT.clear();
        SWAP_AT.clear();
        RETREAT_LATCH.clear();
        LAST_RUNG.clear();
        PICTURES.clear();
        AUTO_STYLE.clear();
        RETREATING_UNTIL.clear();
        LAST_MOVE.clear();
        SQUAD_KEY.clear();
        TerrainSense.clearAll();
        Unstuck.clearAll();
        DecisionTrace.clearAll();
    }
}
