package net.hfstack.rallyguard.debug.sim;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.damagesource.DamageSource;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 2026-11-09 (David): per-battle measures that explain WHY a fight goes the way it goes, not only who won. Written into
 * each run's "extra":
 * <ul>
 *     <li>{@code dmgDealt} - health the blue side really took off the red side (after armour, caps, resistances);
 *     {@code guardSeconds} - seconds each blue guard stayed alive, summed; {@code dps} = dmgDealt / guardSeconds, the
 *     damage one guard deals per second alive. Against the training dummy (round 33) it is the guard's ceiling; against a
 *     monster, the ratio to the dummy says how much the monster's own defences take away;</li>
 *     <li>{@code hurt:<type>} - damage the blue side took by damage type (breath, laser, stomp, arrow...): which attack
 *     does the killing;</li>
 *     <li>{@code aoeHits} - times one attacker hurt 3 or more guards in the same tick (bunched up under an area attack);</li>
 *     <li>{@code hitsOnRed}, {@code wastedHits} - blue attacks that reached a red unit, and those its own rules cancelled
 *     (immune phase, shield, invulnerability): attacking when it can't be hurt;</li>
 *     <li>{@code iframeHits}, {@code iframeDmg} (2026-11-11) - hits that landed while the target was still invulnerable from
 *     the previous one: the waste that coordinated fire ({@code combat.FireWindows}) tries to remove;</li>
 *     <li>{@code nnDist} - mean distance from each guard to its nearest comrade (sampled each second): how spread out;</li>
 *     <li>{@code aggroShield}, {@code aggroOther}, {@code aggroSwitches} - seconds the red units targeted a shield-bearer
 *     or another guard, and how often they changed target: who draws the boss.</li>
 *     <li>{@code ignis:*} (F18, 2026-11-15) - only when a red unit is a Cataclysm boss: where the blue effort against it is
 *     lost. See {@link #bossProjectile}, {@link #bossIncoming} and {@link #end}.</li>
 *     <li>{@code boss:*}, {@code stuck:*} (F26, 2026-11-17) - only when a red unit has 100 or more max health (any mod):
 *     does the giant heal, how long nobody hurts it, and where it is relative to the guards whose last decision reason is
 *     STUCK. See {@link #giantTick}.</li>
 * </ul>
 */
public final class SimCombatStats {
    private SimCombatStats() {
    }

    private static double dmgDealt, guardTicks, nnSum, aggroShield, aggroOther, iframeDmg, mountDmg, mountedTicks;
    private static int shotsMoving, shotsStill, hitsMoving, hitsStill, mountDeaths, mountsN, horseShieldBlocks;
    private static int nnN, aoeHits, hitsOnRed, wastedHits, iframeHits, aggroSwitches, hitTick = -1;
    private static int hitsOnBlue, iframeHitsRed; // F85: the same i-frame count for red's attacks on blue
    private static final Map<String, Double> HURT = new HashMap<>();
    private static final Map<UUID, Set<UUID>> VICTIMS = new HashMap<>();
    private static final Map<UUID, UUID> LAST_TARGET = new HashMap<>();
    // ==== F186 (2026-11-25, sim only, telemetry): overkill and how many blue guards share one red target ====
    /** Red victim's health right before the blue hit now being applied (LivingIncomingDamageEvent, same hit). */
    private static final Map<UUID, Float> HP_BEFORE = new HashMap<>();
    private static double overkillDmg, tgtMaxSum, tgtTopSum, tgtNoneSum;
    private static int overkillKills, tgtSamples, tgtTopN;
    // ==== F187 (2026-11-25, sim only, telemetry): who lands the i-frame hits - the guard that opened the window or another ====
    private static final Map<UUID, UUID> OPENER = new HashMap<>();
    private static double iframeSameDmg, iframeOtherDmg;
    private static int iframeSame, iframeOther, iframeUnk;
    /** F18: Cataclysm boss sinks, per run. Empty (and nothing written) unless a red unit is a Cataclysm boss. */
    private static final Map<String, Double> BOSS = new HashMap<>();
    private static final Map<Class<?>, BossReflect> BOSS_REFLECT = new HashMap<>();

    // ==== F26 (2026-11-17): a giant that heals while the guards are stuck (telemetry only) ====
    private static final double GIANT_HEALTH = 100.0D;
    private static final int[] GIANT_MARKS = {60, 120, 180, 240};
    private static LivingEntity giant;
    private static double giantLast, giantHealed, stuckDy, stuckDist;
    private static int giantHitTick, giantNoHitSec, giantGap, giantMaxGap, stuckSec;
    private static final double[] GIANT_AT = new double[GIANT_MARKS.length];
    // ==== F33 (2026-11-18): what every guard does in the seconds the giant takes no damage (telemetry only) ====
    /** Sums over "gap seconds" (the giant alive and 5 s or more without blue damage); written as means in {@link #end}. */
    private static final double[] GAP = new double[13];
    private static final String[] GAP_KEYS = {"alive", "dist", "inReach", "far", "noTarget", "targetBoss", "blocking",
            "navIdle", "below", "bossDy", "bossMoved", "bossNoTarget", "distN"};
    private static int gapSec, gapFirstAt, gapAliveAtFirst;
    private static double giantFloorY, giantPrevX, giantPrevZ;

    /**
     * Every fight tick. The giant = the red unit with the most max health, if that is at least {@link #GIANT_HEALTH}
     * (picked once, on the first tick). {@code boss:healed} sums every rise of its health between two ticks (so a heal
     * and a hit inside the same tick only count their net). Once a second while it lives: a second with no blue damage
     * on it in the last 20 ticks counts in {@code boss:secNoHit} (longest run: {@code boss:maxGapSec}); every living
     * guard whose last decision reason is STUCK adds one to {@code stuck:sec}, and {@code stuck:dy} (giant's feet minus
     * the guard's feet, so positive = the giant is higher) and {@code stuck:dist} (flat distance) are their means.
     */
    private static void giantTick(int tick, List<Guard> blue, List<LivingEntity> red) {
        if (giant == null) {
            for (LivingEntity e : red) {
                if (e.isAlive() && e.getMaxHealth() >= GIANT_HEALTH && (giant == null || e.getMaxHealth() > giant.getMaxHealth()))
                    giant = e;
            }
            if (giant == null) return;
            giantLast = giant.getHealth();
            giantHitTick = tick;
            giantFloorY = giant.getY(); // F33: the giant spawns standing on the arena floor
            giantPrevX = giant.getX();
            giantPrevZ = giant.getZ();
        }
        double h = giant.isAlive() ? giant.getHealth() : 0.0D;
        if (h > giantLast) giantHealed += h - giantLast;
        giantLast = h;
        if (tick % 20 != 0) return;
        for (int i = 0; i < GIANT_MARKS.length; i++) if (tick == GIANT_MARKS[i] * 20) GIANT_AT[i] = h;
        if (!giant.isAlive()) return;
        if (tick - giantHitTick >= 20) {
            giantNoHitSec++;
            giantGap++;
            giantMaxGap = Math.max(giantMaxGap, giantGap);
        } else {
            giantGap = 0;
        }
        for (Guard g : blue) {
            if (!g.isAlive() || net.hfstack.rallyguard.tactics.DecisionTrace.last(g.getUUID())
                    != net.hfstack.rallyguard.tactics.DecisionReason.STUCK) continue;
            stuckSec++;
            stuckDy += giant.getY() - g.getY();
            double dx = giant.getX() - g.getX(), dz = giant.getZ() - g.getZ();
            stuckDist += Math.sqrt(dx * dx + dz * dz);
        }
        gapTick(tick, blue);
    }

    /**
     * F33: once a second while the giant lives. A "gap second" = 5 s or more since the last blue damage on it; each
     * one samples the living blue guards: flat distance to the edge of the giant's box ({@code dist}, mean per guard),
     * how many are within 3 ({@code inReach}) or past 6 ({@code far}), without a target, targeting the giant, with the
     * shield up, with an idle navigation, and with their feet 1 block or more under the arena floor (a crater); and the
     * giant: feet minus floor, flat blocks moved since the last second, and whether it has no target.
     */
    private static void gapTick(int tick, List<Guard> blue) {
        double moved = Math.sqrt((giant.getX() - giantPrevX) * (giant.getX() - giantPrevX)
                + (giant.getZ() - giantPrevZ) * (giant.getZ() - giantPrevZ));
        giantPrevX = giant.getX();
        giantPrevZ = giant.getZ();
        if (tick - giantHitTick < 100) return;
        gapSec++;
        net.minecraft.world.phys.AABB box = giant.getBoundingBox();
        int alive = 0;
        for (Guard g : blue) {
            if (!g.isAlive()) continue;
            alive++;
            double dx = Math.max(Math.max(box.minX - g.getX(), g.getX() - box.maxX), 0.0D);
            double dz = Math.max(Math.max(box.minZ - g.getZ(), g.getZ() - box.maxZ), 0.0D);
            double d = Math.sqrt(dx * dx + dz * dz);
            GAP[1] += d;
            GAP[12]++;
            if (d <= 3.0D) GAP[2]++;
            if (d > 6.0D) GAP[3]++;
            LivingEntity t = g.getTarget();
            if (t == null) GAP[4]++;
            else if (t == giant) GAP[5]++;
            if (g.isBlocking()) GAP[6]++;
            if (g.getNavigation().isDone()) GAP[7]++;
            if (g.getY() <= giantFloorY - 1.0D) GAP[8]++;
        }
        GAP[0] += alive;
        GAP[9] += giant.getY() - giantFloorY;
        GAP[10] += moved;
        if (giant instanceof Mob m && m.getTarget() == null) GAP[11]++;
        if (gapFirstAt < 0 && giantGap >= 10) { // the first gap of 10 s or more: the second it started
            gapFirstAt = tick / 20 - giantGap;
            gapAliveAtFirst = alive;
        }
    }

    public static void begin() {
        giant = null;
        giantLast = giantHealed = stuckDy = stuckDist = 0;
        giantHitTick = giantNoHitSec = giantGap = giantMaxGap = stuckSec = 0;
        java.util.Arrays.fill(GIANT_AT, -1.0D);
        java.util.Arrays.fill(GAP, 0.0D); // F33
        gapSec = 0;
        gapFirstAt = gapAliveAtFirst = -1;
        giantFloorY = giantPrevX = giantPrevZ = 0;
        dmgDealt = guardTicks = nnSum = aggroShield = aggroOther = iframeDmg = mountDmg = mountedTicks = 0;
        shotsMoving = shotsStill = hitsMoving = hitsStill = mountDeaths = mountsN = horseShieldBlocks = 0;
        nnN = aoeHits = hitsOnRed = wastedHits = iframeHits = aggroSwitches = 0;
        hitsOnBlue = iframeHitsRed = 0; // F85
        HP_BEFORE.clear(); // F186
        OPENER.clear(); // F187
        iframeSameDmg = iframeOtherDmg = 0;
        iframeSame = iframeOther = iframeUnk = 0;
        overkillDmg = tgtMaxSum = tgtTopSum = tgtNoneSum = 0;
        overkillKills = tgtSamples = tgtTopN = 0;
        hitTick = -1;
        HURT.clear();
        VICTIMS.clear();
        LAST_TARGET.clear();
        BOSS.clear();
    }

    // ==== F18 (2026-11-15): Cataclysm bosses - where the blue damage is lost ====
    // Read in Cataclysm's source (lender544/new1.20.1, branch 1.21; the modpack runs 3.33, so everything below is looked
    // up by reflection and fails soft): Ignis_Entity.hurt returns false BEFORE calling super.hurt when (a) a counter/
    // phase animation makes it immune, (b) its shield is up (getIsShield) and the damage source position is in front
    // (canBlockDamageSource: flat vector source->Ignis dotted with its view vector < 0). Then LLibrary_Boss_Monster.hurt:
    // per-hit cap min(DamageCap, amount); fall-off by the SHOOTER's distance (calculateRange = distanceToSqr(source
    // .getEntity())): full up to RangeLimit, false from 1.5 x RangeLimit; then a leaky bucket (private float
    // damageBucket, filled up to DamageCap, drained DpsCap/20 per tick): a hit that would overflow it is cut to the room
    // left, or to 0.1 if it is full. Only then LivingEntity.hurt, where NeoForge posts LivingIncomingDamageEvent.
    // So shield and range losses never reach any damage event: they are classified at ProjectileImpactEvent by
    // repeating Cataclysm's own test. A melee blow the shield stops posts no event at all (no mixin -> not counted;
    // the proxy ignis:frontMeleeSec says how long melee guards stood in front of the raised shield).

    private static final class BossReflect {
        java.lang.reflect.Method rangeLimit, damageCap, isShield;
        java.lang.reflect.Field bucket;
        boolean boss;
    }

    private static BossReflect reflect(LivingEntity e) {
        return BOSS_REFLECT.computeIfAbsent(e.getClass(), c -> {
            BossReflect r = new BossReflect();
            if (!"cataclysm".equals(net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getKey(e.getType()).getNamespace()))
                return r;
            try {
                r.rangeLimit = c.getMethod("RangeLimit");
                r.damageCap = c.getMethod("DamageCap");
                r.boss = true;
            } catch (ReflectiveOperationException | RuntimeException ex) {
                return r; // not one of the capped bosses
            }
            try {
                r.isShield = c.getMethod("getIsShield");
            } catch (ReflectiveOperationException | RuntimeException ignored) {
                // only Ignis has the frontal shield
            }
            for (Class<?> k = c; k != null && r.bucket == null; k = k.getSuperclass()) {
                try {
                    java.lang.reflect.Field f = k.getDeclaredField("damageBucket");
                    f.setAccessible(true);
                    r.bucket = f;
                } catch (ReflectiveOperationException | RuntimeException ignored) {
                    // keep walking up
                }
            }
            return r;
        });
    }

    private static boolean isBoss(LivingEntity e) {
        return e != null && reflect(e).boss;
    }

    private static double num(java.lang.reflect.Method m, Object o, double fallback) {
        if (m == null) return fallback;
        try {
            return ((Number) m.invoke(o)).doubleValue();
        } catch (ReflectiveOperationException | RuntimeException ex) {
            return fallback;
        }
    }

    private static boolean shieldUp(LivingEntity boss) {
        BossReflect r = reflect(boss);
        if (r.isShield == null) return false;
        try {
            return Boolean.TRUE.equals(r.isShield.invoke(boss));
        } catch (ReflectiveOperationException | RuntimeException ex) {
            return false;
        }
    }

    /** Cataclysm's canBlockDamageSource geometry: flat direction source->boss against the boss's view vector. */
    private static boolean inFront(LivingEntity boss, net.minecraft.world.phys.Vec3 from) {
        net.minecraft.world.phys.Vec3 d = from.vectorTo(boss.position()).normalize();
        d = new net.minecraft.world.phys.Vec3(d.x, 0.0D, d.z);
        return d.dot(boss.getViewVector(1.0F)) < 0.0D;
    }

    private static void bump(String k, double v) {
        BOSS.merge(k, v, Double::sum);
    }

    /** A blue projectile struck a red Cataclysm boss (ProjectileImpactEvent, before the boss's hurt runs). */
    public static void bossProjectile(net.minecraft.world.entity.projectile.Projectile p, LivingEntity boss, Entity shooter) {
        if (!isBoss(boss)) return;
        boolean arrow = p instanceof net.minecraft.world.entity.projectile.AbstractArrow;
        bump(arrow ? "ignis:hits:bow" : "ignis:hits:gun", 1);
        if (shieldUp(boss) && inFront(boss, p.position())) { // Ignis checks the shield first
            bump("ignis:shield", 1);
            return;
        }
        if (shooter == null) return;
        double limit = num(reflect(boss).rangeLimit, boss, 15.0D), d = boss.distanceTo(shooter);
        if (d >= limit * 1.5D) bump("ignis:far", 1); // zero damage: hurt() returns false
        else if (d > limit) bump("ignis:falloff", 1); // reduced, (1.5L - d) / 0.5L
    }

    /** A blue hit reached the boss's LivingEntity.hurt (LivingIncomingDamageEvent: after shield, range and bucket). */
    public static void bossIncoming(LivingEntity boss, DamageSource src, float amount, boolean cancelled) {
        if (!isBoss(boss)) return;
        bump("ignis:reached", 1);
        if (!(src.getDirectEntity() instanceof net.minecraft.world.entity.projectile.Projectile)) bump("ignis:hits:melee", 1);
        if (cancelled) return;
        BossReflect r = reflect(boss);
        if (amount <= 0.1001F) {
            bump("ignis:capped", 1); // bucket full: the hit was cut to 0.1
            bump("ignis:cappedFull", 1);
            return;
        }
        if (r.bucket == null) return;
        try {
            double cap = num(r.damageCap, boss, Double.MAX_VALUE);
            if (r.bucket.getFloat(boss) >= cap - 1.0E-3D) bump("ignis:capped", 1); // cut to the room left in the bucket
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            // bucket unreadable in this Cataclysm build: capped stays partial (only the 0.1 hits)
        }
        bump("ignis:amountIn", amount);
    }

    /** Every blue-vs-red damage that landed (LivingDamageEvent.Post): v = victim side, a = attacker side. */
    public static void damage(LivingEntity victim, Entity attacker, DamageSource src, float amount, int v, int a, int tick) {
        if (a == 0 && v == 1) {
            dmgDealt += amount;
            if (lureN > 0 && attacker != null && LURE.contains(attacker.getUUID())) LURE_BAIT_HIT.add(victim.getUUID()); // F128
            if (victim == giant && amount > 0.0F) giantHitTick = tick; // F26
            if (isBoss(victim)) bump("ignis:dmgIn", amount); // F18: health the boss really lost (after armour)
            Float before = HP_BEFORE.remove(victim.getUUID()); // F186: the killing blow's damage past the health it had
            if (before != null && victim.getHealth() <= 0.0F) {
                overkillKills++;
                overkillDmg += Math.max(0.0D, amount - before);
            }
        }
        if (v == 0 && a == 1) {
            HURT.merge("hurt:" + src.getMsgId(), (double) amount, Double::sum);
            if (attacker != null) {
                if (tick != hitTick) {
                    VICTIMS.clear();
                    hitTick = tick;
                }
                Set<UUID> s = VICTIMS.computeIfAbsent(attacker.getUUID(), k -> new HashSet<>());
                if (s.add(victim.getUUID()) && s.size() == 3) aoeHits++;
            }
        }
    }

    /** F85: a red attack that reached a blue unit (not cancelled); counts those inside blue's half-second of i-frames. */
    public static void incomingRed(boolean inIframes) {
        hitsOnBlue++;
        if (inIframes) iframeHitsRed++;
    }

    /** F186: a blue attack is about to hit a red unit (LivingIncomingDamageEvent): its health before this hit. */
    public static void incomingHp(LivingEntity victim) {
        HP_BEFORE.put(victim.getUUID(), victim.getHealth());
    }

    /** A blue attack that reached a red unit (LivingIncomingDamageEvent, cancelled ones included). */
    public static void incoming(LivingEntity victim, Entity attacker, boolean cancelled, boolean inIframes, float amount) {
        hitsOnRed++;
        if (cancelled) wastedHits++;
        else if (inIframes) { // 2026-11-11: landed inside the half-second of invulnerability (only the excess counts)
            iframeHits++;
            iframeDmg += amount;
            // F187: same guard that opened the window, another blue, or unknown (no attacker or no recorded opener)
            UUID opener = victim == null ? null : OPENER.get(victim.getUUID());
            if (attacker == null || opener == null) iframeUnk++;
            else if (opener.equals(attacker.getUUID())) { iframeSame++; iframeSameDmg += amount; }
            else { iframeOther++; iframeOtherDmg += amount; }
        } else if (victim != null) { // F187: this hit opens a new i-frame window on the red unit
            if (attacker != null) OPENER.put(victim.getUUID(), attacker.getUUID());
            else OPENER.remove(victim.getUUID());
        }
    }

    /** F2 (2026-11-11): cavalry diagnosis - horse damage and deaths, time in the saddle, accuracy moving vs still. */
    public static void horseShieldBlock() {
        horseShieldBlocks++;
    }

    public static void mountHurt(float amount) {
        mountDmg += amount;
    }

    public static void shot(boolean moving) {
        if (moving) shotsMoving++;
        else shotsStill++;
    }

    public static void arrowHit(boolean moving) {
        if (moving) hitsMoving++;
        else hitsStill++;
    }

    public static void mountsEnd(List<? extends Entity> mounts) {
        mountsN = mounts.size();
        for (Entity m : mounts) if (!m.isAlive()) mountDeaths++;
    }

    // ==== F128 (O7): a bait unit pulls part of the enemy away - accounting only, behaviour unchanged ====
    private static int lureN, lureSec, lureContactAt, lurePulledAtContact, lurePulledMax, lureMainPeak, lureGapS,
            lureRedAtReturn, lureBaitDeadAt;
    private static double lureContactRedS;
    private static final java.util.List<UUID> LURE = new java.util.ArrayList<>();
    private static final Set<UUID> LURE_PULLED = new HashSet<>(), LURE_BAIT_HIT = new HashSet<>();

    /** {@code lure:N} = the last N blue guards in spawn order count as "bait" (no tag: every guard is "main"). */
    public static void lureBegin(String spec) {
        lureN = 0;
        try {
            if (!spec.isEmpty()) lureN = Math.max(0, Integer.parseInt(spec.trim()));
        } catch (NumberFormatException e) {
            SimExtras.put("lure:bad", 1.0D);
        }
        lureSec = 0;
        lureContactAt = lurePulledAtContact = lureGapS = lureRedAtReturn = lureBaitDeadAt = -1;
        lurePulledMax = lureMainPeak = 0;
        lureContactRedS = 0;
        LURE.clear();
        LURE_PULLED.clear();
        LURE_BAIT_HIT.clear();
    }

    /** Once a second: pulled = alive reds targeting a bait guard and > 8 from every main guard; onMain = alive reds
     *  within 6 of a main guard. {@code blue} holds the ALIVE guards in spawn order (SimRunner.guardsOf). */
    private static void lureSample(List<Guard> blue, List<LivingEntity> red) {
        if (lureSec == 0 && lureN > 0) { // first fight second: every guard still alive, spawn order intact
            for (int i = Math.max(0, blue.size() - lureN); i < blue.size(); i++) LURE.add(blue.get(i).getUUID());
        }
        lureSec++;
        List<Guard> main = new java.util.ArrayList<>();
        int baitAlive = 0;
        for (Guard g : blue) {
            if (LURE.contains(g.getUUID())) baitAlive++;
            else main.add(g);
        }
        if (lureN > 0 && lureBaitDeadAt < 0 && baitAlive < LURE.size()) lureBaitDeadAt = lureSec;
        int pulled = 0, onMain = 0, neverPulledAlive = 0;
        boolean returned = false;
        for (LivingEntity e : red) {
            if (!e.isAlive()) continue;
            if (!LURE_PULLED.contains(e.getUUID())) neverPulledAlive++;
            double best = Double.MAX_VALUE;
            for (Guard g : main) best = Math.min(best, g.distanceToSqr(e));
            if (best <= 36.0D) {
                onMain++;
                if (LURE_PULLED.contains(e.getUUID())) returned = true;
            } else if (best > 64.0D && e instanceof Mob m && m.getTarget() != null && LURE.contains(m.getTarget().getUUID())) {
                pulled++;
                LURE_PULLED.add(e.getUUID());
            }
        }
        if (lureContactAt < 0 && onMain >= 1) {
            lureContactAt = lureSec;
            lurePulledAtContact = pulled;
        }
        if (lureContactAt >= 0 && lureGapS < 0 && returned) {
            lureGapS = lureSec - lureContactAt;
            lureRedAtReturn = neverPulledAlive;
        }
        lurePulledMax = Math.max(lurePulledMax, pulled);
        lureMainPeak = Math.max(lureMainPeak, onMain);
        lureContactRedS += onMain;
        lureLastBaitAlive = baitAlive;
        lureLastNeverPulled = neverPulledAlive;
    }

    private static int lureLastBaitAlive, lureLastNeverPulled;

    private static void lureEnd() {
        SimExtras.put("lure:contactRedS", lureContactRedS); // every arm
        SimExtras.put("lure:mainPeak", lureMainPeak);
        if (lureContactAt >= 0) SimExtras.put("lure:contactAt", lureContactAt);
        if (lureN <= 0) return;
        SimExtras.put("lure:n", LURE.size());
        if (lureContactAt >= 0) {
            SimExtras.put("lure:pulledAtContact", lurePulledAtContact);
            SimExtras.put("lure:gapS", lureGapS >= 0 ? lureGapS : lureSec - lureContactAt);
            SimExtras.put("lure:returned", lureGapS >= 0 ? 1.0D : 0.0D);
            SimExtras.put("lure:redAliveAtReturn", lureGapS >= 0 ? lureRedAtReturn : lureLastNeverPulled);
        }
        SimExtras.put("lure:pulledMax", lurePulledMax);
        SimExtras.put("lure:pulledEver", LURE_PULLED.size());
        SimExtras.put("lure:baitHitRed", LURE_BAIT_HIT.size()); // distinct reds the bait hurt (verdict "physical limit")
        SimExtras.put("lure:baitAlive", lureLastBaitAlive);
        if (lureBaitDeadAt >= 0) SimExtras.put("lure:baitDeadAt", lureBaitDeadAt);
    }

    /** Each fight tick. */
    public static void tick(int tick, List<Guard> blue, List<LivingEntity> red) {
        giantTick(tick, blue, red); // F26
        guardTicks += blue.size();
        for (Guard g : blue) if (g.isPassenger()) mountedTicks++;
        if (tick % 20 != 0) return;
        lureSample(blue, red); // F128
        targetShare(blue, red); // F186
        for (Guard g : blue) {
            double best = Double.MAX_VALUE;
            for (Guard o : blue) if (o != g) best = Math.min(best, g.distanceToSqr(o));
            if (best < Double.MAX_VALUE) {
                nnSum += Math.sqrt(best);
                nnN++;
            }
        }
        for (LivingEntity e : red) {
            if (e.isAlive() && isBoss(e)) { // F18: shield up, and melee guards standing in front of it
                bump("ignis:bossSec", 1);
                if (shieldUp(e)) {
                    bump("ignis:shieldUpSec", 1);
                    for (Guard g : blue) {
                        if (g.getMainHandItem().getItem() instanceof net.minecraft.world.item.ProjectileWeaponItem) continue;
                        if (g.distanceTo(e) <= 5.0D && inFront(e, g.position())) bump("ignis:frontMeleeSec", 1);
                    }
                }
            }
            if (!(e instanceof Mob m) || !m.isAlive()) continue;
            LivingEntity t = m.getTarget();
            if (!(t instanceof Guard)) continue;
            if (net.hfstack.rallyguard.combat.FieldManual.shieldBearer((Guard) t)) aggroShield++;
            else aggroOther++;
            UUID before = LAST_TARGET.put(m.getUUID(), t.getUUID());
            if (before != null && !before.equals(t.getUUID())) aggroSwitches++;
        }
    }

    /**
     * F186: once a second with some red alive. For each living red, the living blue guards whose getTarget() is it;
     * tgtMax = mean of the largest count, tgtTop = mean of (largest count / blue guards with a living red target, only
     * seconds with one), tgtNone = mean share of living blue guards without a living red target.
     */
    private static void targetShare(List<Guard> blue, List<LivingEntity> red) {
        Set<LivingEntity> reds = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());
        for (LivingEntity r : red) if (r.isAlive()) reds.add(r);
        if (reds.isEmpty()) return;
        Map<LivingEntity, Integer> on = new java.util.IdentityHashMap<>();
        int alive = 0, aiming = 0, max = 0;
        for (Guard g : blue) {
            if (!g.isAlive()) continue;
            alive++;
            LivingEntity t = g.getTarget();
            if (t == null || !reds.contains(t)) continue;
            aiming++;
            max = Math.max(max, on.merge(t, 1, Integer::sum));
        }
        if (alive == 0) return;
        tgtSamples++;
        tgtMaxSum += max;
        tgtNoneSum += (double) (alive - aiming) / alive;
        if (aiming > 0) {
            tgtTopSum += (double) max / aiming;
            tgtTopN++;
        }
    }

    public static void end() {
        lureEnd(); // F128
        if (giant != null) { // F26: every key present; a mark the fight never reached is left out
            SimExtras.put("boss:maxHealth", Math.round(giant.getMaxHealth() * 10) / 10.0D);
            SimExtras.put("boss:healed", Math.round(giantHealed * 10) / 10.0D);
            SimExtras.put("boss:secNoHit", giantNoHitSec);
            SimExtras.put("boss:maxGapSec", giantMaxGap);
            SimExtras.put("stuck:sec", stuckSec);
            SimExtras.put("stuck:dy", stuckSec == 0 ? 0 : Math.round(stuckDy / stuckSec * 10) / 10.0D);
            SimExtras.put("stuck:dist", stuckSec == 0 ? 0 : Math.round(stuckDist / stuckSec * 10) / 10.0D);
            for (int i = 0; i < GIANT_MARKS.length; i++)
                if (GIANT_AT[i] >= 0) SimExtras.put("boss:healthAt" + GIANT_MARKS[i], Math.round(GIANT_AT[i] * 10) / 10.0D);
            SimExtras.put("gap:sec", gapSec); // F33: means per gap second (dist: per guard sample)
            for (int i = 0; i < 12; i++) {
                double div = i == 1 ? GAP[12] : gapSec;
                SimExtras.put("gap:" + GAP_KEYS[i], div <= 0 ? 0 : Math.round(GAP[i] / div * 100) / 100.0D);
            }
            SimExtras.put("gap:firstAt", gapFirstAt);
            SimExtras.put("gap:aliveAtFirst", gapAliveAtFirst);
            giant = null; // no entity kept after the run
        }
        double gs = guardTicks / 20.0D;
        SimExtras.put("dmgDealt", Math.round(dmgDealt * 10) / 10.0D);
        SimExtras.put("guardSeconds", Math.round(gs * 10) / 10.0D);
        SimExtras.put("dps", gs <= 0 ? 0 : Math.round(dmgDealt / gs * 100) / 100.0D);
        SimExtras.put("aoeHits", aoeHits);
        SimExtras.put("hitsOnRed", hitsOnRed);
        SimExtras.put("wastedHits", wastedHits);
        if (mountsN > 0 || shotsMoving + shotsStill > 0) {
            SimExtras.put("mountDmg", Math.round(mountDmg * 10) / 10.0D);
            SimExtras.put("mountDeaths", mountDeaths);
            SimExtras.put("horseShieldBlocks", horseShieldBlocks);
            SimExtras.put("mountedShare", guardTicks <= 0 ? 0 : Math.round(mountedTicks / guardTicks * 100) / 100.0D);
            SimExtras.put("shotsMoving", shotsMoving);
            SimExtras.put("shotsStill", shotsStill);
            SimExtras.put("accMoving", shotsMoving == 0 ? -1 : Math.round(100.0D * hitsMoving / shotsMoving) / 100.0D);
            SimExtras.put("accStill", shotsStill == 0 ? -1 : Math.round(100.0D * hitsStill / shotsStill) / 100.0D);
        }
        SimExtras.put("iframeHitsRed", iframeHitsRed); // F85: red hits on blue inside blue's i-frames (mirror)
        SimExtras.put("hitsOnBlue", hitsOnBlue);
        SimExtras.put("iframeHits", iframeHits); // hits that landed inside the target's invulnerability half-second
        SimExtras.put("iframeDmg", Math.round(iframeDmg * 10) / 10.0D); // their raw damage (only the excess over the last hit counted)
        SimExtras.put("overkillDmg", Math.round(overkillDmg * 10) / 10.0D); // F186: every run
        SimExtras.put("iframeSame", iframeSame); // F187: i-frame hits by the guard that opened the window
        SimExtras.put("iframeOther", iframeOther); // F187: i-frame hits by another blue
        SimExtras.put("iframeUnk", iframeUnk); // F187: no attacker or no recorded opener
        SimExtras.put("iframeSameDmg", Math.round(iframeSameDmg * 10) / 10.0D);
        SimExtras.put("iframeOtherDmg", Math.round(iframeOtherDmg * 10) / 10.0D);
        SimExtras.put("overkillKills", overkillKills);
        SimExtras.put("tgtN", tgtSamples);
        SimExtras.put("tgtMax", tgtSamples == 0 ? -1 : Math.round(tgtMaxSum / tgtSamples * 100) / 100.0D);
        SimExtras.put("tgtTop", tgtTopN == 0 ? -1 : Math.round(tgtTopSum / tgtTopN * 1000) / 1000.0D);
        SimExtras.put("tgtNone", tgtSamples == 0 ? -1 : Math.round(tgtNoneSum / tgtSamples * 1000) / 1000.0D);
        SimExtras.put("nnDist", nnN == 0 ? -1 : Math.round(nnSum / nnN * 10) / 10.0D);
        SimExtras.put("aggroShield", aggroShield);
        SimExtras.put("aggroOther", aggroOther);
        SimExtras.put("aggroSwitches", aggroSwitches);
        for (Map.Entry<String, Double> e : HURT.entrySet()) SimExtras.put(e.getKey(), Math.round(e.getValue() * 10) / 10.0D);
        if (!BOSS.isEmpty()) { // F18: every key present (0 if never seen) so the analyst's table has no holes
            for (String k : new String[]{"ignis:hits:bow", "ignis:hits:gun", "ignis:hits:melee", "ignis:reached", "ignis:shield",
                    "ignis:far", "ignis:falloff", "ignis:capped", "ignis:cappedFull", "ignis:amountIn", "ignis:dmgIn",
                    "ignis:bossSec", "ignis:shieldUpSec", "ignis:frontMeleeSec"})
                SimExtras.put(k, Math.round(BOSS.getOrDefault(k, 0.0D) * 10) / 10.0D);
        }
    }
}
