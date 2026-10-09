package net.hfstack.rallyguard.force;

import net.hfstack.rallyguard.combat.Ammo;
import net.hfstack.rallyguard.combat.WeaponProfile;
import net.hfstack.rallyguard.tactics.Tactics;
import net.hfstack.rallyguard.veterancy.VeterancyEvents;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * E3A A2 - the report bus UP the chain. Each unit publishes a {@link Report} (state,
 * casualties, health, ammo, enemies seen, contact) ONLY WHEN IT CHANGES meaningfully; the
 * force's general sums them into {@link KnownEnemy} sightings (time-stamped, expiring after
 * {@link #ENEMY_TTL_TICKS}). This is the base of fog of information and future scouts:
 * nothing above a unit ever scans the world.
 */
public final class SituationReports {
    private SituationReports() {
    }

    static final long ENEMY_TTL_TICKS = 20L * 60L;

    /**
     * @param total        biggest size the unit had since it was first reported
     * @param contactSince tick contact began (-1 = no contact)
     */
    /**
     * @param morale   E3B M: mean morale 0..100 (100 when morale is off)
     * @param cohesion E3B M: the unit's cohesion 0..100
     * @param routed   E3B M: members broken (ROTO / DESTROZADO)
     */
    public record Report(String unit, int alive, int total, double health, double ammo, int enemiesSeen,
                         long contactSince, @Nullable Vec3 centre, @Nullable Vec3 enemyCentre, long tick,
                         double morale, double cohesion, int routed) {
        /** Share of the living members that are broken. */
        public double routedShare() {
            return alive <= 0 ? 0 : (double) routed / alive;
        }

        public double casualties() {
            return total <= 0 ? 0 : 1.0D - (double) alive / total;
        }

        public double contactSeconds(long now) {
            return contactSince < 0 ? 0 : (now - contactSince) / 20.0D;
        }
    }

    public record KnownEnemy(String reportedBy, Vec3 centre, int count, long tick) {
    }

    private static final Map<String, Report> LAST = new HashMap<>();
    /** E3B 0: the last report that counted as a change (LAST keeps fresh centres every second). */
    private static final Map<String, Report> PUBLISHED = new HashMap<>();
    private static final Map<String, List<KnownEnemy>> KNOWN = new HashMap<>(); // force key -> sightings
    /** E3B R3 (A8): per unit, the members last seen and how many of them DIED since it was first reported. */
    private static final Map<String, java.util.Set<java.util.UUID>> SEEN = new HashMap<>();
    private static final Map<String, Integer> DEATHS = new HashMap<>();
    /** Guards that died (death event or destroyed), until a report consumes them (bounded). */
    private static final java.util.Set<java.util.UUID> DEAD = new java.util.LinkedHashSet<>();

    /** E3B R3: a guard died - the next report of its unit counts it as a casualty (not a guard that just left). */
    public static void noteDeath(java.util.UUID guardId) {
        DEAD.add(guardId);
        if (DEAD.size() > 4096) {
            java.util.Iterator<java.util.UUID> it = DEAD.iterator();
            it.next();
            it.remove();
        }
    }
    private static long published;

    /** Builds the unit's report from its members (cheap facts + the squad picture) and publishes it if it changed. */
    public static Report update(String forceKey, String unit, List<Guard> members, long now) {
        Report prev = LAST.get(unit);
        int alive = members.size();
        // E3B R3 (A8): casualties = deaths counted by event. A member that left (moved to another unit,
        // unloaded, another dimension) is simply no longer in the unit: total = alive + dead, so splitting
        // a unit of 10 into 5 + 5 is 0 % casualties (it used to be 50 %, a high-water mark).
        java.util.Set<java.util.UUID> present = new java.util.HashSet<>();
        for (Guard g : members) present.add(g.getUUID());
        java.util.Set<java.util.UUID> before = SEEN.put(unit, present);
        int deaths = DEATHS.getOrDefault(unit, 0);
        if (before != null) {
            for (java.util.UUID id : before) {
                if (!present.contains(id) && DEAD.remove(id)) deaths++;
            }
        }
        DEATHS.put(unit, deaths);
        int total = alive + deaths;
        double health = 0, ammoShare = 0;
        int ranged = 0, withAmmo = 0, enemies = 0;
        double cx = 0, cy = 0, cz = 0;
        boolean contact = false;
        for (Guard g : members) {
            health += g.getHealth() / Math.max(1.0F, g.getMaxHealth());
            cx += g.getX();
            cy += g.getY();
            cz += g.getZ();
            WeaponProfile w = WeaponProfile.of(g.getMainHandItem());
            if (w != null && w.firesArrows()) {
                ranged++;
                if (Ammo.available(g, Ammo.ARROWS)) withAmmo++;
            }
            int[] odds = Tactics.odds(g);
            if (odds != null) enemies = Math.max(enemies, odds[0]);
            if (VeterancyEvents.inFight(g)) contact = true;
        }
        health = alive == 0 ? 0 : health / alive;
        ammoShare = ranged == 0 ? 1.0D : (double) withAmmo / ranged;
        Vec3 centre = alive == 0 ? null : new Vec3(cx / alive, cy / alive, cz / alive);
        long contactSince = contact ? (prev != null && prev.contactSince() >= 0 ? prev.contactSince() : now) : -1;
        Vec3 enemyCentre = enemies > 0 && !members.isEmpty() ? Tactics.enemyCentre(members.get(0)) : null;
        // E3B M: morale, cohesion and broken count travel with the report (published only when they change).
        double morale = net.hfstack.rallyguard.morale.Morale.mean(members);
        if (morale < 0) morale = 100.0D;
        double cohesion = members.isEmpty() || !(members.get(0).level() instanceof net.minecraft.server.level.ServerLevel lvl) ? 100.0D
                : net.hfstack.rallyguard.morale.Cohesion.of(lvl, members.get(0));
        int routed = net.hfstack.rallyguard.morale.Morale.broken(members);
        Report r = new Report(unit, alive, total, health, ammoShare, enemies, contactSince, centre, enemyCentre, now,
                morale, cohesion, routed);
        // E3B 0: the latest numbers are always kept (a moving unit's centre used to stay where
        // it was last "published"); only a meaningful change counts as a publication.
        LAST.put(unit, r);
        if (changed(PUBLISHED.get(unit), r)) {
            PUBLISHED.put(unit, r);
            published++;
            if (enemyCentre != null) {
                KNOWN.computeIfAbsent(forceKey, k -> new ArrayList<>()).add(new KnownEnemy(unit, enemyCentre, enemies, now));
            }
        }
        return r;
    }

    private static boolean changed(@Nullable Report a, Report b) {
        if (a == null) return true;
        return a.alive() != b.alive() || Math.abs(a.health() - b.health()) > 0.1D || Math.abs(a.ammo() - b.ammo()) > 0.15D
                || bucket(a.enemiesSeen()) != bucket(b.enemiesSeen()) || (a.contactSince() < 0) != (b.contactSince() < 0)
                || (int) (a.morale() / 10) != (int) (b.morale() / 10) || (int) (a.cohesion() / 20) != (int) (b.cohesion() / 20)
                || a.routed() != b.routed();
    }

    private static int bucket(int n) {
        return n == 0 ? 0 : n <= 2 ? 1 : n <= 5 ? 2 : n <= 12 ? 3 : 4;
    }

    @Nullable
    public static Report last(String unit) {
        return LAST.get(unit);
    }

    /** The force's known enemies (expired sightings dropped). */
    public static List<KnownEnemy> knownEnemies(String forceKey, long now) {
        List<KnownEnemy> list = KNOWN.get(forceKey);
        if (list == null) return List.of();
        Iterator<KnownEnemy> it = list.iterator();
        while (it.hasNext()) if (now - it.next().tick() > ENEMY_TTL_TICKS) it.remove();
        if (list.size() > 64) list.subList(0, list.size() - 64).clear();
        return new ArrayList<>(list);
    }

    /** How many reports were published (changes) - for the simulation's "one per change" check. */
    public static long published() {
        return published;
    }

    /** E3B R4: drops every report of one force's units ("owner#squad" prefix). */
    public static void forgetForce(String forceKey, String unitPrefix) {
        java.util.function.Predicate<String> mine = k -> k.equals(unitPrefix) || k.startsWith(unitPrefix + "/");
        LAST.keySet().removeIf(mine);
        PUBLISHED.keySet().removeIf(mine);
        SEEN.keySet().removeIf(mine);
        DEATHS.keySet().removeIf(mine);
        KNOWN.remove(forceKey);
    }

    public static void clearAll() {
        LAST.clear();
        PUBLISHED.clear();
        SEEN.clear();
        DEATHS.clear();
        DEAD.clear();
        KNOWN.clear();
        published = 0;
    }
}
