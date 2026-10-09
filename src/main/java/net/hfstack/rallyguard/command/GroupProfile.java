package net.hfstack.rallyguard.command;

import net.hfstack.rallyguard.combat.Ammo;
import net.hfstack.rallyguard.combat.WeaponProfile;
import net.hfstack.rallyguard.compat.GunAmmo;
import net.hfstack.rallyguard.compat.ScorchedGunsCompat;
import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.force.Capability;
import net.hfstack.rallyguard.force.UnitOrders;
import net.hfstack.rallyguard.veterancy.Trait;
import net.hfstack.rallyguard.veterancy.Veterancy;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TridentItem;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * E3B G2 ({@code docs/GRUPOS_Y_SUBGRUPOS.md} §3) - what a group or subgroup IS, from its real
 * equipment and mounts (generic properties only - no other mod's item lists):
 * <ul>
 *     <li>composition by role ARCHETYPE ({@link Archetype});</li>
 *     <li>aggregated attributes: rank mean and spread, armor, reach, expected speed, dominant
 *     mount, health and ammo (morale and cohesion are filled by the morale system);</li>
 *     <li>CONSISTENCY 0-100 = 100 × (1 − Σ weight × penalty), penalties = normalized entropy of
 *     the archetypes, normalized entropy of the mount types, rank spread (weights in config
 *     {@code groups.profileWeights}; nothing hidden);</li>
 *     <li>IDENTITY = the dominant archetype when consistency ≥ {@code groups.minIdentityScore},
 *     else "Mixed (consistency N)"; plus the second archetype and up to 3 warnings;</li>
 *     <li>CAPABILITIES (the one source of truth since E3B; {@code force/Forces} reads them here).</li>
 * </ul>
 * Computed on demand and cached per group key; a cached profile is reused while the group's
 * fingerprint (members + equipment + mounts) is unchanged, and recomputed at most once a second.
 * Its only effect on play is a BOUNDED nudge on the leader's execution quality
 * ({@link #qualityFactor}, ±{@code groups.qualityCap}): a player may put anything in a group and
 * the profile only REFLECTS it - never stats, never a refusal.
 */
public final class GroupProfile {
    private GroupProfile() {
    }

    public enum Archetype {
        INFANTERIA_LINEA, INFANTERIA_PESADA, LANCEROS, ARQUEROS, BALLESTEROS, TIRADORES,
        CABALLERIA_LIGERA, CABALLERIA_PESADA, CABALLERIA_DE_RANGO, OTROS;

        public boolean ranged() {
            return this == ARQUEROS || this == BALLESTEROS || this == TIRADORES || this == CABALLERIA_DE_RANGO;
        }

        public boolean mounted() {
            return this == CABALLERIA_LIGERA || this == CABALLERIA_PESADA || this == CABALLERIA_DE_RANGO;
        }

        /** The column of the instruction table this archetype reads (§4). */
        public Family family() {
            return switch (this) {
                case ARQUEROS, BALLESTEROS, TIRADORES -> Family.RANGED;
                case CABALLERIA_DE_RANGO -> Family.RANGED_CAVALRY;
                case CABALLERIA_LIGERA, CABALLERIA_PESADA -> Family.CAVALRY;
                default -> Family.INFANTRY;
            };
        }

        /** Infantry as a first-class category (planner palette, P1 point 7). */
        public boolean infantry() {
            return this == INFANTERIA_LINEA || this == INFANTERIA_PESADA || this == LANCEROS;
        }
    }

    /** Instruction-table columns: archers/crossbows/guns, ranged cavalry, heavy infantry/spears, cavalry. */
    public enum Family { RANGED, RANGED_CAVALRY, INFANTRY, CAVALRY }

    /**
     * @param consistency 0..100
     * @param identity    true when consistency ≥ minIdentityScore (the dominant archetype names the group)
     * @param morale      mean morale 0..100 (-1 = not measured)
     * @param cohesion    unit cohesion 0..100 (-1 = not measured)
     * @param warnings    "lang.key^arg^arg" (at most 3)
     */
    public record Profile(int size, Map<Archetype, Integer> counts, Archetype dominant, @Nullable Archetype second,
                          int consistency, boolean identity, double rankMean, double rankSpread, double armorMean,
                          double reachMean, double speed, String mount, double healthMean, double ammoMean,
                          double morale, double cohesion, List<String> warnings, EnumSet<Capability> capabilities) {

        public Family family() {
            return dominant.family();
        }

        public int count(Archetype a) {
            return counts.getOrDefault(a, 0);
        }

        /** "identityKey|consistency|secondKey|morale|cohesion|warn~warn~warn" for the group card. */
        public String card() {
            String id = identity ? "gui.rallyguard.archetype." + dominant.name().toLowerCase(Locale.ROOT) : "gui.rallyguard.profile.mixed";
            String sec = second == null ? "" : "gui.rallyguard.archetype." + second.name().toLowerCase(Locale.ROOT);
            return id + "|" + consistency + "|" + sec + "|" + Math.round(morale) + "|" + Math.round(cohesion) + "|"
                    + String.join("~", warnings) + "|" + dominant.name() + "|" + size;
        }
    }

    public static final Profile EMPTY = new Profile(0, Map.of(), Archetype.OTROS, null, 0, false, 0, 0, 0, 0, 0, "",
            0, 0, -1, -1, List.of(), EnumSet.noneOf(Capability.class));

    // ==== Per guard ====

    public static Archetype archetypeOf(Guard g) {
        ItemStack hand = g.getMainHandItem();
        boolean mounted = g.isPassenger();
        boolean gun = ScorchedGunsCompat.isGun(hand);
        WeaponProfile w = WeaponProfile.of(hand);
        boolean arrows = w != null && w.firesArrows();
        boolean heavy = g.getArmorValue() >= RallyConfig.i(RallyConfig.PROFILE_HIGH_ARMOR, 12);
        if (mounted) {
            if (gun || arrows) return Archetype.CABALLERIA_DE_RANGO;
            return heavy ? Archetype.CABALLERIA_PESADA : Archetype.CABALLERIA_LIGERA;
        }
        if (gun) return Archetype.TIRADORES;
        if (arrows) return hand.getItem() instanceof CrossbowItem ? Archetype.BALLESTEROS : Archetype.ARQUEROS;
        if (hand.isEmpty()) return Archetype.OTROS;
        if (isSpear(hand)) return Archetype.LANCEROS;
        boolean shield = g.getOffhandItem().getItem() instanceof ShieldItem;
        return shield && heavy ? Archetype.INFANTERIA_PESADA : Archetype.INFANTERIA_LINEA;
    }

    /** Trident or any item whose id names a spear/lance/pike/halberd (generic, no mod lists). */
    static boolean isSpear(ItemStack stack) {
        if (stack.getItem() instanceof TridentItem) return true;
        String path = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        return path.contains("spear") || path.contains("lance") || path.contains("pike") || path.contains("halberd")
                || path.contains("glaive");
    }

    /** The mount's entity type id, or "foot". */
    static String mountOf(Guard g) {
        Entity v = g.getVehicle();
        return v == null ? "foot" : BuiltInRegistries.ENTITY_TYPE.getKey(v.getType()).toString();
    }

    /**
     * E3B G1 - how much a guard should stand in an exposed (front, loaded) slot: armor, a shield
     * and a melee weapon push it forward; a bow, crossbow or gun pulls it behind.
     */
    public static double toughness(Guard g) {
        double t = g.getArmorValue() + g.getMaxHealth() / 4.0D;
        if (g.getOffhandItem().getItem() instanceof ShieldItem) t += 6.0D;
        Archetype a = archetypeOf(g);
        if (a.ranged()) t -= 12.0D;
        else if (a != Archetype.OTROS) t += 4.0D;
        return t;
    }

    // ==== Computing ====

    public static Profile of(List<Guard> members) {
        if (members.isEmpty()) return EMPTY;
        Map<Archetype, Integer> counts = new EnumMap<>(Archetype.class);
        Map<String, Integer> mounts = new HashMap<>();
        double rankSum = 0, rankSq = 0, armor = 0, reach = 0, speed = 0, health = 0, ammoShare = 0;
        int shooters = 0, shootersDry = 0, gunners = 0, gunnersDry = 0, mounted = 0, scouts = 0;
        for (Guard g : members) {
            Archetype a = archetypeOf(g);
            counts.merge(a, 1, Integer::sum);
            String m = mountOf(g);
            mounts.merge(m, 1, Integer::sum);
            if (!"foot".equals(m)) mounted++;
            int rank = Veterancy.rank(g);
            rankSum += rank;
            rankSq += rank * rank;
            armor += g.getArmorValue();
            WeaponProfile w = WeaponProfile.forGuard(g);
            reach += w == null ? 3.0D : w.maxRange();
            Entity v = g.getVehicle();
            LivingEntity mover = v instanceof LivingEntity lv ? lv : g;
            speed += mover.getAttributes().hasAttribute(Attributes.MOVEMENT_SPEED) ? mover.getAttributeValue(Attributes.MOVEMENT_SPEED) : 0.0D;
            health += g.getHealth() / Math.max(1.0F, g.getMaxHealth());
            if (a.ranged()) {
                shooters++;
                boolean dry;
                if (ScorchedGunsCompat.isGun(g.getMainHandItem())) {
                    gunners++;
                    dry = !GunAmmo.canFight(g);
                    if (dry) gunnersDry++;
                } else {
                    dry = !Ammo.available(g, Ammo.ARROWS);
                }
                if (dry) shootersDry++;
            }
            if (Veterancy.has(g, Trait.SCOUT)) scouts++;
        }
        int n = members.size();
        ammoShare = shooters == 0 ? 1.0D : 1.0D - (double) shootersDry / shooters;
        double rankMean = rankSum / n;
        double rankSpread = Math.sqrt(Math.max(0.0D, rankSq / n - rankMean * rankMean));

        Archetype dominant = Archetype.OTROS, second = null;
        int best = -1, secondBest = -1;
        for (Map.Entry<Archetype, Integer> e : counts.entrySet()) {
            if (e.getValue() > best) {
                second = best >= 0 ? dominant : second;
                secondBest = best;
                best = e.getValue();
                dominant = e.getKey();
            } else if (e.getValue() > secondBest) {
                secondBest = e.getValue();
                second = e.getKey();
            }
        }
        String mount = "foot";
        int bestMount = -1;
        for (Map.Entry<String, Integer> e : mounts.entrySet()) {
            if (e.getValue() > bestMount) {
                bestMount = e.getValue();
                mount = e.getKey();
            }
        }

        int consistency = consistency(counts.values(), mounts.values(), n, rankSpread, (double) best / n);
        boolean identity = consistency >= RallyConfig.i(RallyConfig.PROFILE_MIN_IDENTITY, 65);

        List<String> warnings = new ArrayList<>();
        int ranged = 0, footMelee = 0;
        for (Map.Entry<Archetype, Integer> e : counts.entrySet()) {
            if (e.getKey().ranged()) ranged += e.getValue();
            else if (!e.getKey().mounted()) footMelee += e.getValue();
        }
        if (!dominant.ranged() && ranged > 0 && ranged * 3 <= n && footMelee * 2 >= n) {
            warnings.add("gui.rallyguard.profile.warn.uncovered^" + ranged);
        }
        if (mounts.size() > 1 && mounted > 0) warnings.add("gui.rallyguard.profile.warn.mounts^" + mounts.size());
        if (gunnersDry > 0) warnings.add("gui.rallyguard.profile.warn.gun_ammo^" + gunnersDry + "^" + gunners);
        else if (shooters > 0 && ammoShare < 0.5D) warnings.add("gui.rallyguard.profile.warn.ammo^" + Math.round(ammoShare * 100));
        if (!identity && warnings.size() < 3) {
            warnings.add("gui.rallyguard.profile.warn.mixed^gui.rallyguard.archetype." + dominant.name().toLowerCase(Locale.ROOT));
        }
        while (warnings.size() > 3) warnings.remove(warnings.size() - 1);

        EnumSet<Capability> caps = capabilities(counts, n, scouts, dominant);
        return new Profile(n, counts, dominant, secondBest > 0 ? second : null, consistency, identity, rankMean, rankSpread,
                armor / n, reach / n, speed / n, mount, health / n, ammoShare, -1, -1, warnings, caps);
    }

    /**
     * 100 × (1 − Σ weight × penalty). The archetype penalty is half the normalized entropy and half
     * the dominance gap {@code min(1, 2 × (1 − dominant share))} - so two kinds half and half read
     * as mixed (~53), one odd archer among eight swords stays consistent (~85).
     */
    static int consistency(java.util.Collection<Integer> archetypes, java.util.Collection<Integer> mounts, int n, double rankSpread,
                           double dominantShare) {
        if (n <= 1) return 100;
        double wA = weight(0, 0.7D), wM = weight(1, 0.2D), wR = weight(2, 0.1D);
        double pA = 0.5D * normalizedEntropy(archetypes, n, Archetype.values().length)
                + 0.5D * Math.min(1.0D, 2.0D * (1.0D - dominantShare));
        double pM = normalizedEntropy(mounts, n, 4);
        double pR = Math.min(1.0D, rankSpread / 2.5D);
        double c = 100.0D * (1.0D - Math.min(1.0D, wA * pA + wM * pM + wR * pR));
        return (int) Math.round(Math.max(0, Math.min(100, c)));
    }

    /** Shannon entropy of the counts divided by ln(min(n, kinds)) - 0 = all alike, 1 = as mixed as possible. */
    static double normalizedEntropy(java.util.Collection<Integer> counts, int n, int kinds) {
        double h = 0;
        for (int c : counts) {
            if (c <= 0) continue;
            double p = (double) c / n;
            h -= p * Math.log(p);
        }
        double max = Math.log(Math.max(2, Math.min(n, kinds)));
        return max <= 0 ? 0 : Math.min(1.0D, h / max);
    }

    private static double weight(int i, double fallback) {
        List<? extends Double> w = RallyConfig.get(RallyConfig.PROFILE_WEIGHTS::get, List.of());
        return i < w.size() ? w.get(i) : fallback;
    }

    /**
     * The group's capabilities (E3A A4's rules, now from the profile): half or more melee on
     * foot = MELEE_LINE (and SCREEN from 4); half or more shooters = RANGED_SUPPORT (and
     * RETREAT_COVER from 2); half or more mounted = MOUNTED_CHARGE; 2+ = ESCORT and
     * HOLD_POSITION; E3B C1: RECON for light cavalry or archers with a Scout among them.
     */
    static EnumSet<Capability> capabilities(Map<Archetype, Integer> counts, int n, int scouts, Archetype dominant) {
        EnumSet<Capability> caps = EnumSet.noneOf(Capability.class);
        if (n == 0) return caps;
        int melee = 0, ranged = 0, mounted = 0;
        for (Map.Entry<Archetype, Integer> e : counts.entrySet()) {
            Archetype a = e.getKey();
            if (a.ranged()) ranged += e.getValue();
            if (a.mounted()) mounted += e.getValue();
            if (!a.ranged() && !a.mounted()) melee += e.getValue();
        }
        if (melee * 2 >= n) {
            caps.add(Capability.MELEE_LINE);
            if (melee >= 4) caps.add(Capability.SCREEN);
        }
        if (ranged * 2 >= n) {
            caps.add(Capability.RANGED_SUPPORT);
            if (ranged >= 2) caps.add(Capability.RETREAT_COVER);
        }
        if (mounted * 2 >= n) caps.add(Capability.MOUNTED_CHARGE);
        if (n >= 2) {
            caps.add(Capability.ESCORT);
            caps.add(Capability.HOLD_POSITION);
        }
        if (RallyConfig.on(RallyConfig.CLASSES_ENABLED) && scouts > 0
                && (dominant == Archetype.CABALLERIA_LIGERA || dominant == Archetype.ARQUEROS)) {
            caps.add(Capability.RECON);
        }
        return caps;
    }

    // ==== Cache (≤ 1 recompute per second per group, only when the group changed) ====

    private record Cached(Profile profile, long tick, int fingerprint) {
    }

    private static final Map<String, Cached> CACHE = new HashMap<>();
    private static long recomputes;

    /** Cheap: members, hands, armor and mounts - what the profile depends on. */
    static int fingerprint(List<Guard> members) {
        int h = members.size();
        for (Guard g : members) {
            h = h * 31 + g.getUUID().hashCode();
            h = h * 31 + g.getMainHandItem().getItem().hashCode();
            h = h * 31 + g.getOffhandItem().getItem().hashCode();
            h = h * 31 + g.getArmorValue();
            h = h * 31 + (g.getVehicle() == null ? 0 : g.getVehicle().getType().hashCode());
        }
        return h;
    }

    /** The cached profile of group {@code key}, recomputed when its fingerprint changed (at most once a second). */
    public static Profile cached(String key, List<Guard> members, long now) {
        Cached c = CACHE.get(key);
        if (c != null && now - c.tick() < 20) return c.profile();
        int fp = fingerprint(members);
        if (c != null && c.fingerprint() == fp) {
            Profile fresh = withMorale(c.profile(), members); // E3B M: morale and cohesion move without a recompute
            CACHE.put(key, new Cached(fresh, now, fp));
            return fresh;
        }
        Profile p = withMorale(of(members), members);
        recomputes++;
        if (CACHE.size() > 4096) CACHE.clear();
        CACHE.put(key, new Cached(p, now, fp));
        return p;
    }

    /** E3B M: fills mean morale and the unit's cohesion (-1 when morale is off). */
    static Profile withMorale(Profile p, List<Guard> members) {
        if (members.isEmpty() || !net.hfstack.rallyguard.morale.Morale.enabled()) return p;
        double morale = net.hfstack.rallyguard.morale.Morale.mean(members);
        double cohesion = members.get(0).level() instanceof ServerLevel lvl
                ? net.hfstack.rallyguard.morale.Cohesion.of(lvl, members.get(0)) : -1;
        return new Profile(p.size(), p.counts(), p.dominant(), p.second(), p.consistency(), p.identity(), p.rankMean(),
                p.rankSpread(), p.armorMean(), p.reachMean(), p.speed(), p.mount(), p.healthMean(), p.ammoMean(), morale,
                cohesion, p.warnings(), p.capabilities());
    }

    /** The profile of the unit (squad path) a guard belongs to, or of the guard alone. */
    public static Profile forGuard(ServerLevel level, Guard guard) {
        String key = UnitOrders.unitKeyOf(level, guard);
        if (key == null) return cached("g" + guard.getUUID(), List.of(guard), level.getGameTime());
        Cached c = CACHE.get(key);
        if (c != null && level.getGameTime() - c.tick() < 20) return c.profile();
        return cached(key, UnitOrders.members(level, key), level.getGameTime());
    }

    /**
     * Bounded effect on execution: a consistent group executes its specialty a little better,
     * an incoherent one a little worse. 1 ± {@code groups.qualityCap}, neutral at consistency 60.
     */
    public static double qualityFactor(Profile p) {
        if (!RallyConfig.on(RallyConfig.PROFILE_ENABLED) || p.size() < 2) return 1.0D;
        double cap = Math.min(0.15D, RallyConfig.d(RallyConfig.PROFILE_QUALITY_CAP, 0.15D));
        double x = Math.max(-1.0D, Math.min(1.0D, (p.consistency() - 60.0D) / 40.0D));
        return 1.0D + cap * x;
    }

    public static long recomputes() {
        return recomputes;
    }

    /** Distinct "kinds" present (for tests and the card). */
    public static Set<Archetype> kinds(Profile p) {
        return new HashSet<>(p.counts().keySet());
    }

    public static void clearAll() {
        CACHE.clear();
        recomputes = 0;
    }
}
