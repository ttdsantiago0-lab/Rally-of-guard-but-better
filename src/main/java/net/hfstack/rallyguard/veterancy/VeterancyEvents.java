package net.hfstack.rallyguard.veterancy;

import net.hfstack.rallyguard.command.SquadDirectives;
import net.hfstack.rallyguard.compat.ScorchedGunsCompat;
import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.contract.VeterancyRegistry;
import net.hfstack.rallyguard.order.KamikazeOrder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingUseTotemEvent;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * E2A block 2 / E3A V1-V2-V7-V8 - where veterancy XP comes from: REAL combat only, valued by
 * the THREAT of the enemy and each guard's CONTRIBUTION, never by raw damage
 * ({@code docs/VETERANIA_ESTADO.md} §1 listed the old model's distortions).
 * <p>
 * A "real enemy" is a monster ({@link Enemy}), another player, another commander's guard,
 * or a mob currently targeting our side - never a cow, a villager, an armor stand or a friend.
 * <ul>
 *     <li><b>Damage share (V1):</b> each hit is worth {@code xpPerThreat × Threat(enemy) ×
 *     effective / maxHealth}, where {@code effective} is capped by the health the victim
 *     actually had (no XP for overkill), × the repeat decay of that enemy type, × an
 *     outnumbered bonus (enemies / allies near, ≤ {@code inferiorityBonusMax}), plus
 *     {@code coverBonus} when the enemy was attacking a comrade (covering it). Half for damage
 *     received.</li>
 *     <li><b>Kill / assists:</b> when the enemy dies, a kill bonus (Threat × {@code killXp})
 *     is split by damage share among every guard that hurt it in the last 15 s.</li>
 *     <li><b>Anti-abuse (V8):</b> nothing from an enemy that hasn't moved nor fought for
 *     {@code immobileSeconds}; nothing once the same guard has been hitting the same enemy
 *     for over {@code repeatVictimSeconds}; per-minute cap {@code xpPerMinuteCap}; one enemy
 *     can't give a guard more than {@code maxXpPerVictimShare} of its current rank band
 *     (bosses give a lot, never a rank skip).</li>
 *     <li><b>Fights:</b> combat events less than 10 s apart; a fight longer than
 *     {@code longFightSeconds}, survived: {@code longFightXp} + a battle; survived after
 *     dropping under 3 hearts: {@code nearDeathSurvivedXp}.</li>
 *     <li><b>Command XP (V2, leaders rise first):</b> when a captain's fight closes, it earns
 *     command XP for the share of its unit alive, the unit left holding the field and the unit
 *     kept together - apart from combat XP, also counted for rank.</li>
 *     <li><b>Story (V7):</b> notable kills, long battles, near deaths, reliefs.</li>
 * </ul>
 * Totems ({@link LivingUseTotemEvent}): a totem saves an owned guard only after
 * {@code totemDelayTicks} in hand (Tank: shorter); repeated revives may make it reckless.
 */
public final class VeterancyEvents {

    private static final long FIGHT_GAP_TICKS = 200L;
    private static final float NEAR_DEATH_HEALTH = 6.0F;
    private static final long ASSIST_WINDOW_TICKS = 300L;

    /** guard -> {fight start, last combat tick, near death in this fight (0/1)} */
    private static final Map<UUID, long[]> FIGHT = new HashMap<>();
    /** guard -> tick it was last counted near death (one count per fight). */
    private static final Map<UUID, Long> NEAR_DEATH = new HashMap<>();
    /** guard -> tick a totem was first seen in its hands. */
    private static final Map<UUID, Long> TOTEM_SINCE = new HashMap<>();
    /** guard -> recent revive ticks. */
    private static final Map<UUID, Deque<Long>> REVIVES = new HashMap<>();
    /** victim -> health right before the hit being processed (Pre). */
    private static final Map<UUID, Float> PRE_HEALTH = new HashMap<>();
    /** victim -> guard -> {damage, first hit tick, last hit tick, xp taken} */
    private static final Map<UUID, Map<UUID, double[]>> CONTRIB = new HashMap<>();
    /** guard -> {window start tick, xp in window} */
    private static final Map<UUID, double[]> MINUTE = new HashMap<>();
    /** victim -> {last tick it moved or fought, x, z} */
    private static final Map<UUID, double[]> ACTIVE = new HashMap<>();

    // ==== Damage ====

    @SubscribeEvent
    public void onDamagePre(LivingDamageEvent.Pre event) {
        LivingEntity v = event.getEntity();
        if (v.level().isClientSide()) return;
        PRE_HEALTH.put(v.getUUID(), v.getHealth() + v.getAbsorptionAmount());
    }

    @SubscribeEvent
    public void onDamage(LivingDamageEvent.Post event) {
        LivingEntity victim = event.getEntity();
        if (!(victim.level() instanceof ServerLevel level)) return;
        float amount = event.getNewDamage();
        Float before = PRE_HEALTH.remove(victim.getUUID());
        if (amount <= 0.0F) return;
        float effective = before == null ? amount : Math.min(amount, Math.max(0.0F, before)); // no overkill XP
        DamageSource source = event.getSource();
        Guard attacker = guardOf(source);
        long now = level.getGameTime();

        if (attacker != null && realEnemy(attacker, victim)) {
            if (!immobile(victim, now)) {
                double[] c = CONTRIB.computeIfAbsent(victim.getUUID(), k -> new HashMap<>())
                        .computeIfAbsent(attacker.getUUID(), k -> new double[]{0, now, now, 0});
                c[0] += effective;
                c[2] = now;
                if (now - (long) c[1] <= 20L * RallyConfig.i(RallyConfig.XP_REPEAT_VICTIM_SECONDS, 45)) {
                    // E3B R7 (B10): only a VALID hit (moving enemy, inside the repeat window) opens or extends a
                    // fight - a farm or a trapped mob no longer makes a "long battle" or command XP.
                    markCombat(level, attacker);
                    VeterancyRegistry.Record r = Veterancy.edit(level, attacker.getUUID());
                    String type = typeOf(victim);
                    double xp = RallyConfig.d(RallyConfig.XP_PER_THREAT, 20.0D) * Threat.of(victim)
                            * effective / Math.max(1.0F, victim.getMaxHealth()) * decay(r, type) * outnumbered(attacker);
                    if (covering(attacker, victim)) xp *= 1.0D + RallyConfig.d(RallyConfig.XP_COVER_BONUS, 0.5D);
                    if (!r.enemyTypes.containsKey(type) && r.enemyTypes.size() < VeterancyRegistry.MAX_TYPES) {
                        r.enemyTypes.put(type, 0);
                        xp += RallyConfig.i(RallyConfig.XP_NEW_ENEMY_TYPE, 25);
                    }
                    grant(level, attacker, victim, c, xp, VeterancyRegistry.Path.SURVIVAL);
                }
            }
        }

        if (victim instanceof Guard guard && GuardOwnership.hasOwner(guard)) {
            Entity by = source.getEntity();
            if (by == null && source.getDirectEntity() instanceof Projectile p) by = p.getOwner();
            if (by instanceof LivingEntity enemy && realEnemy(guard, enemy)) {
                markCombat(level, guard);
                VeterancyRegistry.Record r = Veterancy.edit(level, guard.getUUID());
                double xp = 0.5D * RallyConfig.d(RallyConfig.XP_PER_THREAT, 20.0D) * Threat.of(enemy)
                        * effective / Math.max(1.0F, guard.getMaxHealth()) * decay(r, typeOf(enemy));
                capAndAdd(level, guard, xp, VeterancyRegistry.Path.SURVIVAL);
            }
            if (guard.isAlive() && guard.getHealth() < NEAR_DEATH_HEALTH) {
                Long last = NEAR_DEATH.get(guard.getUUID());
                if (last == null || now - last > FIGHT_GAP_TICKS * 3) {
                    NEAR_DEATH.put(guard.getUUID(), now);
                    VeterancyRegistry.Record r = Veterancy.edit(level, guard.getUUID());
                    r.nearDeath++;
                    long[] fight = FIGHT.get(guard.getUUID());
                    if (fight != null) fight[2] = 1;
                }
            }
        }
        // Whoever deals or takes damage is not "immobile".
        if (source.getEntity() instanceof LivingEntity hitter) noteActive(hitter, now);
    }

    /** Grants damage-share XP respecting the per-enemy cap (a share of the guard's current rank band). */
    private static void grant(ServerLevel level, Guard guard, LivingEntity victim, double[] contrib, double xp,
                              VeterancyRegistry.Path path) {
        int rank = Veterancy.rank(guard);
        double cap = RallyConfig.d(RallyConfig.XP_MAX_VICTIM_SHARE, 0.5D) * Veterancy.band(rank);
        double room = Math.max(0.0D, cap - contrib[3]);
        double given = capAndAdd(level, guard, Math.min(xp, room), path);
        contrib[3] += given;
    }

    /**
     * E3B R7 (B10): long-fight, near-death and command XP go through the same per-minute cap as combat XP
     * (they used to call addXp directly); no weapon mastery from them. Returns what was added.
     */
    static double capped(ServerLevel level, Guard guard, double xp, VeterancyRegistry.Path path, boolean command) {
        if (xp <= 0.0D) return 0.0D;
        long now = level.getGameTime();
        double[] w = MINUTE.computeIfAbsent(guard.getUUID(), k -> new double[]{now, 0});
        if (now - (long) w[0] >= 1200L) {
            w[0] = now;
            w[1] = 0;
        }
        double add = Math.min(xp, Math.max(0.0D, RallyConfig.i(RallyConfig.XP_PER_MINUTE_CAP, 80) - w[1]));
        if (add <= 0.0D) return 0.0D;
        w[1] += add;
        Veterancy.addXp(level, guard, add, path, command);
        return add;
    }

    /** Applies the per-minute cap; returns what was actually added. */
    private static double capAndAdd(ServerLevel level, Guard guard, double xp, VeterancyRegistry.Path path) {
        if (xp <= 0.0D) return 0.0D;
        long now = level.getGameTime();
        double[] w = MINUTE.computeIfAbsent(guard.getUUID(), k -> new double[]{now, 0});
        if (now - (long) w[0] >= 1200L) {
            w[0] = now;
            w[1] = 0;
        }
        double room = Math.max(0.0D, RallyConfig.i(RallyConfig.XP_PER_MINUTE_CAP, 80) - w[1]);
        double add = Math.min(xp, room);
        if (add <= 0.0D) return 0.0D;
        w[1] += add;
        Veterancy.addXp(level, guard, add, path, false);
        WeaponMastery.gain(level, guard, add);
        return add;
    }

    // ==== Kills and assists ====

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        if (!(victim.level() instanceof ServerLevel level)) return;
        if (victim instanceof Guard dead) forgetFight(dead.getUUID());
        Map<UUID, double[]> contributors = CONTRIB.remove(victim.getUUID());
        ACTIVE.remove(victim.getUUID());
        Guard killer = guardOf(event.getSource());
        if (contributors == null || contributors.isEmpty()) return;

        long now = level.getGameTime();
        double total = 0;
        for (double[] c : contributors.values()) if (now - (long) c[2] <= ASSIST_WINDOW_TICKS) total += c[0];
        if (total <= 0) return;
        double threat = Threat.of(victim);
        String type = typeOf(victim);
        for (Map.Entry<UUID, double[]> e : contributors.entrySet()) {
            double[] c = e.getValue();
            if (now - (long) c[2] > ASSIST_WINDOW_TICKS) continue;
            if (!(level.getEntity(e.getKey()) instanceof Guard g) || !g.isAlive() || !realEnemy(g, victim)) continue;
            VeterancyRegistry.Record r = Veterancy.edit(level, g.getUUID());
            double share = c[0] / total;
            double xp = RallyConfig.i(RallyConfig.XP_KILL, 10) * threat * share * decay(r, type);
            grant(level, g, victim, c, xp, VeterancyRegistry.Path.SURVIVAL);
            if (g == killer) {
                if (r.enemyTypes.containsKey(type) || r.enemyTypes.size() < VeterancyRegistry.MAX_TYPES) {
                    r.enemyTypes.merge(type, 1, Integer::sum);
                }
                ItemStack hand = g.getMainHandItem();
                if (ScorchedGunsCompat.isGun(hand)) r.killsGun++;
                else if (hand.getItem() instanceof ProjectileWeaponItem || event.getSource().getDirectEntity() instanceof Projectile) r.killsRanged++;
                else r.killsMelee++;
                if (threat >= 3.0D || victim instanceof Player) {
                    r.addStory(level.getDayTime() / 24000L, "kill", victim.getName().getString());
                }
            }
        }
    }

    // ==== Totems ====

    @SubscribeEvent
    public void onTotem(LivingUseTotemEvent event) {
        if (!(event.getEntity() instanceof Guard guard) || !GuardOwnership.hasOwner(guard)) return;
        if (!(guard.level() instanceof ServerLevel level)) return;
        long now = level.getGameTime();
        Long since = TOTEM_SINCE.get(guard.getUUID());
        int delay = Veterancy.has(guard, Trait.TANK)
                ? RallyConfig.i(RallyConfig.TOTEM_DELAY_TANK_TICKS, 20)
                : RallyConfig.i(RallyConfig.TOTEM_DELAY_TICKS, 60);
        if (since == null || now - since < delay) {
            event.setCanceled(true); // not in hand long enough: it doesn't save the guard
            return;
        }
        TOTEM_SINCE.remove(guard.getUUID());
        VeterancyRegistry.Record r = Veterancy.edit(level, guard.getUUID());
        r.totems++;
        r.addStory(level.getDayTime() / 24000L, "revived", "");

        Deque<Long> revives = REVIVES.computeIfAbsent(guard.getUUID(), k -> new ArrayDeque<>());
        revives.addLast(now);
        long window = 20L * RallyConfig.i(RallyConfig.KAMIKAZE_WINDOW_SECONDS, 600);
        while (!revives.isEmpty() && now - revives.peekFirst() > window) revives.removeFirst();
        if (revives.size() >= RallyConfig.i(RallyConfig.KAMIKAZE_REVIVES, 3)
                && level.getRandom().nextDouble() < RallyConfig.d(RallyConfig.KAMIKAZE_CHANCE, 0.35D)) {
            revives.clear();
            goReckless(level, guard, now);
        }
    }

    private static void goReckless(ServerLevel level, Guard guard, long now) {
        Veterancy.makeReckless(guard, now + 20L * 60L);
        UUID owner = GuardOwnership.getOwner(guard);
        ServerPlayer commander = owner == null ? null : level.getServer().getPlayerList().getPlayer(owner);
        LivingEntity target = guard.getTarget();
        if (commander != null && target != null && target.isAlive() && KamikazeOrder.isArmed(guard)) {
            new KamikazeOrder(target).execute(commander, List.of(guard));
        }
        if (commander != null) {
            commander.sendSystemMessage(Component.translatable("gui.rallyguard.vet.reckless", guard.getName())
                    .withStyle(ChatFormatting.DARK_RED));
        }
    }

    // ==== Once a second per guard (stance loop) ====

    /** Closes finished fights, counts mounted time / time near the commander, tracks totems in hand. */
    public static void second(ServerLevel level, Guard guard) {
        long now = level.getGameTime();
        UUID id = guard.getUUID();
        long[] fight = FIGHT.get(id);
        if (fight != null && now - fight[1] > FIGHT_GAP_TICKS) closeFight(level, guard, fight);

        boolean totem = guard.getOffhandItem().is(Items.TOTEM_OF_UNDYING) || guard.getMainHandItem().is(Items.TOTEM_OF_UNDYING);
        if (totem) TOTEM_SINCE.putIfAbsent(id, now);
        else TOTEM_SINCE.remove(id);

        boolean mounted = guard.isPassenger();
        UUID owner = GuardOwnership.getOwner(guard);
        Player commander = owner == null ? null : level.getPlayerByUUID(owner);
        boolean near = commander != null && commander.distanceToSqr(guard) < 24.0D * 24.0D;
        if (mounted || near) {
            VeterancyRegistry.Record r = Veterancy.edit(level, id);
            if (mounted) {
                r.secondsMounted++;
                if (r.rank >= Veterancy.MAX_RANK) r.paths[VeterancyRegistry.Path.MOUNT.ordinal()] += 0.05D; // V0: mount bond
            }
            if (near) r.secondsWithCommander++;
        }
    }

    private static void markCombat(ServerLevel level, Guard guard) {
        long now = level.getGameTime();
        long[] fight = FIGHT.get(guard.getUUID());
        if (fight != null && now - fight[1] > FIGHT_GAP_TICKS) {
            closeFight(level, guard, fight);
            fight = null;
        }
        if (fight == null) {
            FIGHT.put(guard.getUUID(), new long[]{now, now, 0});
            UnitFights.opened(level, guard);
        } else {
            fight[1] = now;
        }
        noteActive(guard, now);
    }

    private static void closeFight(ServerLevel level, Guard guard, long[] fight) {
        FIGHT.remove(guard.getUUID());
        if (!guard.isAlive()) return;
        long seconds = (fight[1] - fight[0]) / 20L;
        long day = level.getDayTime() / 24000L;
        if (seconds >= RallyConfig.i(RallyConfig.XP_LONG_FIGHT_SECONDS, 12)) {
            VeterancyRegistry.Record r = Veterancy.edit(level, guard.getUUID());
            r.battles++;
            if (seconds >= 60) r.addStory(day, "battle", String.valueOf(seconds));
            capped(level, guard, RallyConfig.i(RallyConfig.XP_LONG_FIGHT, 15), VeterancyRegistry.Path.HISTORY, false);
        }
        if (fight[2] == 1) {
            Veterancy.edit(level, guard.getUUID()).addStory(day, "near_death", "");
            capped(level, guard, RallyConfig.i(RallyConfig.XP_NEAR_DEATH_SURVIVED, 15), VeterancyRegistry.Path.SURVIVAL, false);
        }
        UnitFights.closed(level, guard);
    }

    /** True while the guard has been in combat within the last 10 s. */
    public static boolean inFight(Guard guard) {
        long[] fight = FIGHT.get(guard.getUUID());
        return fight != null && guard.level().getGameTime() - fight[1] <= FIGHT_GAP_TICKS;
    }

    // ==== Helpers ====

    /** The owned guard behind a damage source (directly, or as a projectile's owner). */
    @Nullable
    private static Guard guardOf(DamageSource source) {
        Entity e = source.getEntity();
        if (e == null && source.getDirectEntity() instanceof Projectile p) e = p.getOwner();
        return e instanceof Guard g && GuardOwnership.hasOwner(g) ? g : null;
    }

    /** See the class javadoc: monsters, other players, other commanders' guards, mobs fighting our side. */
    public static boolean realEnemy(Guard guard, LivingEntity other) {
        if (other == guard) return false;
        UUID owner = GuardOwnership.getOwner(guard);
        if (owner == null) return false;
        // E3B R7 (B10): an ALLIED player (same team) and that player's guards are friends, not combat.
        if (other instanceof Player p) return !p.getUUID().equals(owner) && !p.isSpectator() && !allied(guard, owner, p.getUUID());
        if (other instanceof Guard g) {
            UUID o = GuardOwnership.getOwner(g);
            return o != null && !o.equals(owner) && !allied(guard, owner, o);
        }
        if (other instanceof Enemy) return true;
        if (other instanceof Mob mob && mob.getTarget() != null) {
            LivingEntity t = mob.getTarget();
            return t.getUUID().equals(owner) || (t instanceof Guard tg && owner.equals(GuardOwnership.getOwner(tg)));
        }
        return false;
    }

    /** True if both commanders are online and on the same scoreboard team. */
    static boolean allied(Guard guard, UUID a, UUID b) {
        Player pa = guard.level().getPlayerByUUID(a), pb = guard.level().getPlayerByUUID(b);
        return pa != null && pb != null && pa.isAlliedTo(pb);
    }

    /** V8: an enemy that hasn't moved nor fought for {@code immobileSeconds} (a farm, a trapped mob). */
    private static boolean immobile(LivingEntity victim, long now) {
        double[] a = ACTIVE.get(victim.getUUID());
        boolean fighting = victim instanceof Mob m && m.getTarget() != null;
        if (a == null || fighting || Math.abs(victim.getX() - a[1]) + Math.abs(victim.getZ() - a[2]) > 0.5D) {
            noteActive(victim, now);
            return false;
        }
        return now - (long) a[0] > 20L * RallyConfig.i(RallyConfig.XP_IMMOBILE_SECONDS, 5);
    }

    private static void noteActive(LivingEntity e, long now) {
        ACTIVE.put(e.getUUID(), new double[]{now, e.getX(), e.getZ()});
        if (ACTIVE.size() > 4096) ACTIVE.clear();
    }

    /** V1: fighting outnumbered pays more - enemies / allies the guard's squad knows (≤ cap). */
    private static double outnumbered(Guard guard) {
        int[] odds = net.hfstack.rallyguard.tactics.Tactics.odds(guard);
        if (odds == null || odds[1] <= 0) return 1.0D;
        return Math.max(1.0D, Math.min(RallyConfig.d(RallyConfig.XP_INFERIORITY_MAX, 2.0D), (double) odds[0] / odds[1]));
    }

    /** V1: the enemy was attacking one of this guard's comrades (not the guard itself). */
    private static boolean covering(Guard guard, LivingEntity victim) {
        if (!(victim instanceof Mob mob) || mob.getTarget() == null || mob.getTarget() == guard) return false;
        UUID owner = GuardOwnership.getOwner(guard);
        LivingEntity t = mob.getTarget();
        return owner != null && (t.getUUID().equals(owner) || GuardOwnership.isOwnedBy(t, owner));
    }

    private static String typeOf(LivingEntity e) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(e.getType()).toString();
    }

    private static double decay(VeterancyRegistry.Record r, String type) {
        int repeats = Math.min(50, r.enemyTypes.getOrDefault(type, 0));
        return Math.pow(RallyConfig.d(RallyConfig.XP_REPEAT_DECAY, 0.9D), repeats);
    }

    private static void forgetFight(UUID guardId) {
        FIGHT.remove(guardId);
        NEAR_DEATH.remove(guardId);
        TOTEM_SINCE.remove(guardId);
        REVIVES.remove(guardId);
        MINUTE.remove(guardId);
    }

    public static void forget(UUID guardId) {
        forgetFight(guardId);
        Veterancy.forget(guardId);
    }

    /** Once a minute: drop contribution and activity records nobody updated in 2 minutes. */
    public static void purge(long now) {
        CONTRIB.values().removeIf(m -> {
            m.values().removeIf(c -> now - (long) c[2] > 2400L);
            return m.isEmpty();
        });
        ACTIVE.values().removeIf(a -> now - (long) a[0] > 2400L);
        PRE_HEALTH.clear();
    }

    public static void clearAll() {
        FIGHT.clear();
        NEAR_DEATH.clear();
        TOTEM_SINCE.clear();
        REVIVES.clear();
        CONTRIB.clear();
        MINUTE.clear();
        ACTIVE.clear();
        PRE_HEALTH.clear();
        UnitFights.clearAll();
        Veterancy.clearCache();
    }

    /**
     * E3A V2 - command XP. When the first member of a unit (guards sharing a pushed
     * directive) enters a fight, the unit's size is noted; when its CAPTAIN's fight closes,
     * the captain earns command XP: {@code commandSurvivalXp × alive / size}, +
     * {@code commandWinXp} if no enemy is left near, + {@code commandFormationXp} if the unit
     * stayed together (mean distance to its centre ≤ 8). Also a relief story line.
     */
    static final class UnitFights {
        private static final Map<SquadDirectives.Directive, int[]> SIZE = new java.util.IdentityHashMap<>();

        static void opened(ServerLevel level, Guard guard) {
            SquadDirectives.Directive d = SquadDirectives.of(guard.getUUID());
            if (d == null || SIZE.containsKey(d)) return;
            SIZE.put(d, new int[]{members(level, guard, d).size()});
            if (SIZE.size() > 2048) SIZE.clear();
        }

        static void closed(ServerLevel level, Guard guard) {
            SquadDirectives.Directive d = SquadDirectives.of(guard.getUUID());
            if (d == null || d.captain() == null || !d.captain().equals(guard.getUUID())) return;
            int[] size = SIZE.remove(d);
            if (size == null || size[0] <= 1) return;
            List<Guard> unit = members(level, guard, d);
            double xp = RallyConfig.i(RallyConfig.CMD_XP_SURVIVAL, 20) * Math.min(1.0D, (double) unit.size() / size[0]);
            // E3B R1: "held the field" = no real enemy within 40 blocks NOW (the squad picture is stale by
            // the time a fight closes, 10 s after the last blow - a stale or missing picture grants nothing).
            if (net.hfstack.rallyguard.command.InstructionRunner.enemyCentre(level, guard, guard.position()) == null) {
                xp += RallyConfig.i(RallyConfig.CMD_XP_WIN, 15);
            }
            double cx = 0, cz = 0;
            for (Guard g : unit) {
                cx += g.getX();
                cz += g.getZ();
            }
            cx /= Math.max(1, unit.size());
            cz /= Math.max(1, unit.size());
            double spread = 0;
            for (Guard g : unit) spread += Math.sqrt((g.getX() - cx) * (g.getX() - cx) + (g.getZ() - cz) * (g.getZ() - cz));
            if (!unit.isEmpty() && spread / unit.size() <= 8.0D) xp += RallyConfig.i(RallyConfig.CMD_XP_FORMATION, 10);
            double given = capped(level, guard, xp, VeterancyRegistry.Path.COMMAND, true); // E3B R7: per-minute cap too
            net.hfstack.rallyguard.debug.SimProbe.commandXp(guard, given);
        }

        private static List<Guard> members(ServerLevel level, Guard guard, SquadDirectives.Directive d) {
            List<Guard> out = new java.util.ArrayList<>();
            for (Guard g : net.hfstack.rallyguard.event.GuardStanceHandler.loaded(level.dimension())) {
                if (g.isAlive() && SquadDirectives.of(g.getUUID()) == d) out.add(g);
            }
            if (!out.contains(guard) && guard.isAlive()) out.add(guard);
            return out;
        }

        static void clearAll() {
            SIZE.clear();
        }
    }
}
