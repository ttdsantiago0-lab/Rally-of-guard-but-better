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
    public record Report(String unit, int alive, int total, double health, double ammo, int enemiesSeen,
                         long contactSince, @Nullable Vec3 centre, @Nullable Vec3 enemyCentre, long tick) {
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
    private static final Map<String, List<KnownEnemy>> KNOWN = new HashMap<>(); // force key -> sightings
    private static long published;

    /** Builds the unit's report from its members (cheap facts + the squad picture) and publishes it if it changed. */
    public static Report update(String forceKey, String unit, List<Guard> members, long now) {
        Report prev = LAST.get(unit);
        int alive = members.size();
        int total = Math.max(alive, prev == null ? alive : prev.total());
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
        Report r = new Report(unit, alive, total, health, ammoShare, enemies, contactSince, centre, enemyCentre, now);
        if (changed(prev, r)) {
            LAST.put(unit, r);
            published++;
            if (enemyCentre != null) {
                KNOWN.computeIfAbsent(forceKey, k -> new ArrayList<>()).add(new KnownEnemy(unit, enemyCentre, enemies, now));
            }
            return r;
        }
        return prev;
    }

    private static boolean changed(@Nullable Report a, Report b) {
        if (a == null) return true;
        return a.alive() != b.alive() || Math.abs(a.health() - b.health()) > 0.1D || Math.abs(a.ammo() - b.ammo()) > 0.15D
                || bucket(a.enemiesSeen()) != bucket(b.enemiesSeen()) || (a.contactSince() < 0) != (b.contactSince() < 0);
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

    public static void clearAll() {
        LAST.clear();
        KNOWN.clear();
        published = 0;
    }
}
