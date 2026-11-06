package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.combat.WeaponProfile;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Escort v1 (David 2026-11-06: "quiero que tengan la habilidad de escoltar a alguien"; design in
 * {@code docs/ESCOLTA_Y_TESTUDO.md} §3). DEFEND (phase 3H) already made the ward each defender's moving post; this
 * turns a handful of defenders into one escort:
 * <ul>
 *     <li><b>an even ring</b> around the ward (each escort its own share of the circle, instead of an angle drawn
 *     from its UUID - eight escorts used to bunch on two or three sides), <b>led ahead</b> of the ward by about
 *     1.5 s of its walk, re-posted as soon as the wanted spot moves 1.5 blocks (it used to trail 5 blocks behind)
 *     and walked there at a march instead of Guard Villagers' slow walk-back;</li>
 *     <li><b>a leash to the ward</b>: a melee escort never fights farther than {@link #LEASH} blocks from it (it
 *     used to chase 16 blocks from its post) - a shooter keeps its weapon's reach;</li>
 *     <li><b>one threat each</b>: melee escorts take the nearest enemy that hunts the ward or the escort, within
 *     the leash and the per-target melee cap ({@link Engagement}); shooters take the one closest to the ward.
 *     Before, every escort answered the same mob;</li>
 *     <li><b>the ward is ours</b>: our guards' arrows and blows pass through the entity they escort (an MCA
 *     villager, a friend, a trader) the way they pass through our own guards.</li>
 * </ul>
 * Sim switch {@code off:escort} ({@link #simOld}) brings back phase 3H's DEFEND for comparison.
 */
public final class Escort {
    private Escort() {
    }

    /** Simulation only: phase 3H's DEFEND (tag {@code off:escort}). */
    public static boolean simOld;

    /** Phase 3H's DEFEND instead of this escort: the sim switch or {@code fieldManual.escortV1 = false}. */
    public static boolean old() {
        return simOld || !net.hfstack.rallyguard.config.RallyConfig.on(net.hfstack.rallyguard.config.RallyConfig.ESCORT_V1);
    }

    private static boolean screenOff() {
        return simNoScreen || !net.hfstack.rallyguard.config.RallyConfig.on(net.hfstack.rallyguard.config.RallyConfig.ESCORT_SCREEN);
    }
    /** Simulation only: no shield screen against shooters (tag {@code off:screen}). */
    public static boolean simNoScreen;

    /** A melee escort never fights farther than this from the ward. */
    public static final double LEASH = 10.0D;
    /** How far from a hunter a mob counts as hunting the ward. */
    public static final double THREAT_RADIUS = 16.0D;
    private static final double RING = 3.5D;
    private static final double REPOST = 1.5D;
    private static final double MARCH = 0.9D;
    private static final int LEAD_TICKS = 30;
    private static final double MAX_LEAD = 4.0D;
    /** An escort's mark (refreshed every second by the stance loop) lasts this long. */
    private static final long FRESH = 60L;

    private record Mark(UUID ward, UUID owner, long time, boolean shield, boolean ranged) {
    }

    /** Where the ward is being shot from: the mean direction of the shooters hunting it (NaN: nobody). */
    private record Fire(long time, double angle, int nearest) {
    }

    private static final double SHOOTER_RADIUS = 32.0D;
    private static final double SCREEN_RADIUS = 2.0D;
    private static final double SCREEN_ROW = 1.0D;
    private static final double SCREEN_SPREAD = 0.36D;
    private static final int SCREEN_WIDTH = 5;
    /** With no shooters of its own, an escort sends its other shields after the shooters, this far from the ward. */
    private static final double HUNT_RADIUS = 24.0D;
    /** A melee enemy this close to the ward or to a screening shield is fought, screen or not. */
    private static final double SCREEN_MELEE = 3.0D;
    private static final Map<UUID, Fire> FIRE = new HashMap<>();
    /** Screening shield -> entity id of the shooter it faces, and until when (applied every tick). */
    private record Facing(int shooter, long until) {
    }

    private static final Map<UUID, Facing> FACING = new HashMap<>();

    private record Track(Vec3 pos, long time, Vec3 velocity) {
    }

    /** Escort guard -> its ward, commander and last refresh. */
    private static final Map<UUID, Mark> ESCORTS = new HashMap<>();
    /** Ward -> where it was and how fast it walks (blocks per tick). */
    private static final Map<UUID, Track> TRACKS = new HashMap<>();

    /** Called every second for each defender by the stance loop. */
    public static void mark(Guard guard, LivingEntity ward, long now) {
        UUID owner = GuardOwnership.getOwner(guard);
        ESCORTS.put(guard.getUUID(), new Mark(ward.getUUID(), owner, now, hasShield(guard), Engagement.isRanged(guard)));
        if (ESCORTS.size() > 512) prune(now);
    }

    private static void prune(long now) {
        ESCORTS.values().removeIf(m -> now - m.time() > FRESH);
        TRACKS.values().removeIf(t -> now - t.time() > 200L);
        FIRE.values().removeIf(f -> now - f.time() > 200L);
        FACING.values().removeIf(f -> now > f.until());
    }

    private static boolean hasShield(Guard guard) {
        return guard.getOffhandItem().canPerformAction(net.neoforged.neoforge.common.ItemAbilities.SHIELD_BLOCK);
    }

    /** Is this guard escorting someone right now? */
    public static boolean escorting(Guard guard) {
        Mark m = ESCORTS.get(guard.getUUID());
        return m != null && guard.level().getGameTime() - m.time() <= FRESH;
    }

    /** Is {@code victim} the ward of one of commander {@code owner}'s guards (for friendly fire)? */
    public static boolean isWardOf(Entity victim, @Nullable UUID owner) {
        if (old() || owner == null || ESCORTS.isEmpty()) return false;
        long now = victim.level().getGameTime();
        UUID id = victim.getUUID();
        for (Mark m : ESCORTS.values()) {
            if (id.equals(m.ward()) && owner.equals(m.owner()) && now - m.time() <= FRESH) return true;
        }
        return false;
    }

    /** This commander's escorts of {@code ward}, in a stable order (their share of the ring). */
    private static List<UUID> escortsOf(UUID ward, @Nullable UUID owner, long now) {
        return escortsOf(ward, owner, now, false);
    }

    private static List<UUID> escortsOf(UUID ward, @Nullable UUID owner, long now, boolean shieldsOnly) {
        List<UUID> out = new ArrayList<>();
        for (Iterator<Map.Entry<UUID, Mark>> it = ESCORTS.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<UUID, Mark> e = it.next();
            Mark m = e.getValue();
            if (now - m.time() > FRESH) {
                it.remove();
                continue;
            }
            if (ward.equals(m.ward()) && (owner == null || owner.equals(m.owner())) && (!shieldsOnly || m.shield())) {
                out.add(e.getKey());
            }
        }
        out.sort(null);
        return out;
    }

    /** The ward's walking velocity in blocks per tick, measured over about a second. */
    private static Vec3 velocity(LivingEntity ward, long now) {
        Track t = TRACKS.get(ward.getUUID());
        if (t == null || now - t.time() > 100L) {
            TRACKS.put(ward.getUUID(), new Track(ward.position(), now, Vec3.ZERO));
            return Vec3.ZERO;
        }
        if (now - t.time() >= 20L) {
            Vec3 v = ward.position().subtract(t.pos()).scale(1.0D / (now - t.time()));
            v = new Vec3(v.x, 0.0D, v.z);
            TRACKS.put(ward.getUUID(), new Track(ward.position(), now, v));
            return v;
        }
        return t.velocity();
    }

    /**
     * Where the ward is being shot from, measured at most once a second per ward: the mean direction (closer
     * shooters weigh more) of every ranged mob within {@link #SHOOTER_RADIUS} hunting the ward or one of its escorts.
     */
    private static Fire fire(ServerLevel level, LivingEntity ward, List<UUID> escorts, long now) {
        Fire f = FIRE.get(ward.getUUID());
        if (f != null && now - f.time() < 20L) return f;
        double sx = 0, sz = 0, best = Double.MAX_VALUE;
        int nearest = -1;
        for (Mob m : level.getEntitiesOfClass(Mob.class, ward.getBoundingBox().inflate(SHOOTER_RADIUS),
                m -> m.isAlive() && m instanceof net.minecraft.world.entity.monster.RangedAttackMob && m.getTarget() != null)) {
            LivingEntity t = m.getTarget();
            if (t != ward && !escorts.contains(t.getUUID())) continue;
            double dx = m.getX() - ward.getX(), dz = m.getZ() - ward.getZ(), d = Math.sqrt(dx * dx + dz * dz);
            if (d < 1.0E-3) continue;
            sx += dx / (d * d);
            sz += dz / (d * d);
            if (d < best) {
                best = d;
                nearest = m.getId();
            }
        }
        f = new Fire(now, nearest < 0 || sx * sx + sz * sz < 1.0E-9 ? Double.NaN : Math.atan2(sz, sx), nearest);
        FIRE.put(ward.getUUID(), f);
        return f;
    }

    /** Does this commander's escort of {@code ward} have a shooter of its own? */
    private static boolean escortHasRanged(UUID ward, @Nullable UUID owner, long now) {
        for (Mark m : ESCORTS.values()) {
            if (m.ranged() && ward.equals(m.ward()) && (owner == null || owner.equals(m.owner())) && now - m.time() <= FRESH) return true;
        }
        return false;
    }

    /** How many shields stand in the screen: up to five if the escort has its own shooters to kill the enemy's; if
     *  not, half of them (up to four) - the others go after the shooters (measured: eight shields that all screen
     *  never kill a skeleton, and the ward still falls). */
    private static int screenSize(int shields, boolean ranged) {
        return ranged ? Math.min(shields, SCREEN_WIDTH * 2) : Math.min(4, (shields + 1) / 2);
    }

    /** True while this shield-bearer stands in its ward's screen against shooters. */
    public static boolean screening(Guard guard) {
        Facing f = FACING.get(guard.getUUID());
        return f != null && guard.level().getGameTime() <= f.until();
    }

    /** Where this escort should stand: its share of a ring around the spot the ward is walking to - or, for a
     *  shield-bearer while shooters hunt the ward, its place in the screen between them and the ward. */
    public static Vec3 spot(Guard guard, LivingEntity ward, long now) {
        List<UUID> escorts = escortsOf(ward.getUUID(), GuardOwnership.getOwner(guard), now);
        int n = Math.max(1, escorts.size());
        int i = Math.max(0, escorts.indexOf(guard.getUUID()));
        Vec3 lead = velocity(ward, now).scale(LEAD_TICKS);
        if (lead.lengthSqr() > MAX_LEAD * MAX_LEAD) lead = lead.normalize().scale(MAX_LEAD);
        if (!screenOff() && hasShield(guard) && guard.level() instanceof ServerLevel level) {
            Fire f = fire(level, ward, escorts, now);
            if (!Double.isNaN(f.angle())) {
                // 2026-11-06 escort v1 screen: shields in an arc between the shooters and the ward, rows of five,
                // the second row behind the gaps of the first. An arrow aimed at the ward meets a raised shield.
                List<UUID> shields = escortsOf(ward.getUUID(), GuardOwnership.getOwner(guard), now, true);
                int j = Math.max(0, shields.indexOf(guard.getUUID()));
                int size = screenSize(shields.size(), escortHasRanged(ward.getUUID(), GuardOwnership.getOwner(guard), now));
                if (j >= size) {
                    FACING.remove(guard.getUUID());
                    return ring(ward, lead, i, n);
                }
                int row = j / SCREEN_WIDTH, inRow = Math.min(SCREEN_WIDTH, size - row * SCREEN_WIDTH);
                int k = j % SCREEN_WIDTH;
                double angle = f.angle() + (k - (inRow - 1) / 2.0D) * SCREEN_SPREAD + (row % 2) * SCREEN_SPREAD / 2.0D;
                double radius = SCREEN_RADIUS + row * SCREEN_ROW;
                FACING.put(guard.getUUID(), new Facing(f.nearest(), now + FRESH));
                net.hfstack.rallyguard.debug.SimProbe.count(guard, "act:escortScreen");
                return ward.position().add(lead).add(Math.cos(angle) * radius, 0.0D, Math.sin(angle) * radius);
            }
        }
        FACING.remove(guard.getUUID());
        return ring(ward, lead, i, n);
    }

    private static Vec3 ring(LivingEntity ward, Vec3 lead, int i, int n) {
        double radius = RING + Math.max(0, n - 8) * 0.25D;
        double angle = Math.PI * 2.0D * i / n;
        return ward.position().add(lead).add(Math.cos(angle) * radius, 0.0D, Math.sin(angle) * radius);
    }

    /**
     * A screening shield keeps its head (what a shield's block test reads: {@code LivingEntity#isDamageSourceBlocked})
     * turned to the shooter, set after the guard's own tick so its look goals can't turn it away before the arrows
     * of this tick fly.
     */
    @net.neoforged.bus.api.SubscribeEvent
    public static void onEntityTick(net.neoforged.neoforge.event.tick.EntityTickEvent.Post event) {
        if (FACING.isEmpty() || !(event.getEntity() instanceof Guard guard) || guard.level().isClientSide()) return;
        Facing f = FACING.get(guard.getUUID());
        if (f == null || guard.getTarget() != null) return;
        if (guard.level().getGameTime() > f.until()) {
            FACING.remove(guard.getUUID());
            return;
        }
        Entity shooter = guard.level().getEntity(f.shooter());
        if (shooter == null || !shooter.isAlive()) return;
        float yaw = (float) (net.minecraft.util.Mth.atan2(shooter.getZ() - guard.getZ(), shooter.getX() - guard.getX())
                * (180.0D / Math.PI)) - 90.0F;
        guard.setYRot(yaw);
        guard.setYHeadRot(yaw);
        guard.setYBodyRot(yaw);
    }

    /** Re-posts the escort when its spot moved, and marches it there when it has nothing to fight. */
    public static void keepUp(ServerLevel level, Guard guard, LivingEntity ward, long now) {
        Vec3 want = spot(guard, ward, now);
        net.minecraft.core.BlockPos post = CommandTargeting.postOf(guard);
        if (post == null || Vec3.atBottomCenterOf(post).distanceToSqr(want.x, post.getY(), want.z) > REPOST * REPOST) {
            post = GuardPosts.standableNear(level, want);
            GuardModes.postKeepingWard(guard, post);
        }
        if (guard.getTarget() == null && guard.distanceToSqr(Vec3.atBottomCenterOf(post)) > 2.0D * 2.0D) {
            MountDriver.moveTo(guard, post, MARCH);
        }
    }

    /**
     * What this escort should fight now, or null. Candidates: whoever hurt the ward lately, and every mob within
     * {@link #THREAT_RADIUS} of the ward that hunts the ward or one of its escorts. A shooter takes the one closest
     * to the ward; a melee escort the one closest to itself among those within {@link #LEASH} of the ward (or the
     * one hitting it), when the melee cap on that mob leaves it room.
     */
    @Nullable
    public static LivingEntity threat(ServerLevel level, Guard guard, LivingEntity ward) {
        long now = level.getGameTime();
        UUID owner = GuardOwnership.getOwner(guard);
        List<UUID> escorts = escortsOf(ward.getUUID(), owner, now);
        // a shield left out of the screen of an escort without shooters hunts the enemy's shooters
        boolean hunter = !screenOff() && hasShield(guard) && !screening(guard) && !Engagement.isRanged(guard)
                && !escortHasRanged(ward.getUUID(), owner, now)
                && !Double.isNaN(fire(level, ward, escorts, now).angle());
        List<LivingEntity> candidates = new ArrayList<>();
        LivingEntity hurtBy = ward.getLastHurtByMob();
        if (hurtBy != null && hurtBy.isAlive() && CommandTargeting.wouldAccept(guard, hurtBy)) candidates.add(hurtBy);
        for (Mob m : level.getEntitiesOfClass(Mob.class, ward.getBoundingBox().inflate(hunter ? HUNT_RADIUS : THREAT_RADIUS),
                m -> m.isAlive() && m.getTarget() != null)) {
            LivingEntity t = m.getTarget();
            boolean hunting = t == ward || t == guard || escorts.contains(t.getUUID());
            if (hunting && !candidates.contains(m) && CommandTargeting.wouldAccept(guard, m)) candidates.add(m);
        }
        if (candidates.isEmpty()) return null;

        if (screening(guard)) {
            // a screening shield stays in the screen: it only fights what reaches the ward or itself up close
            LivingEntity close = null;
            for (LivingEntity c : candidates) {
                if (c instanceof net.minecraft.world.entity.monster.RangedAttackMob) continue;
                boolean near = c.distanceToSqr(ward) <= SCREEN_MELEE * SCREEN_MELEE || c.distanceToSqr(guard) <= SCREEN_MELEE * SCREEN_MELEE;
                if (near && (close == null || c.distanceToSqr(guard) < close.distanceToSqr(guard))) close = c;
            }
            return close;
        }
        if (Engagement.isRanged(guard)) {
            LivingEntity best = null;
            for (LivingEntity c : candidates) {
                if (best == null || c.distanceToSqr(ward) < best.distanceToSqr(ward)) best = c;
            }
            WeaponProfile weapon = WeaponProfile.forGuard(guard);
            double reach = weapon == null ? THREAT_RADIUS : weapon.maxRange();
            return best != null && best.distanceToSqr(guard) <= reach * reach ? best : null;
        }
        LivingEntity mine = guard.getLastHurtByMob();
        candidates.sort((a, b) -> Double.compare(a.distanceToSqr(guard), b.distanceToSqr(guard)));
        for (LivingEntity c : candidates) {
            boolean hittingMe = c == mine;
            double leash = hittingMe ? LEASH + 6.0D : LEASH; // never off after a far archer
            if (hunter && c instanceof net.minecraft.world.entity.monster.RangedAttackMob) leash = HUNT_RADIUS;
            if (c.distanceToSqr(ward) > leash * leash) continue;
            if (hittingMe || Engagement.tryEngage(guard, c)) return c;
        }
        return null;
    }
}
