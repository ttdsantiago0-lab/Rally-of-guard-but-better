package net.hfstack.rallyguard.veterancy;

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
 * E2A block 2 - where veterancy XP comes from: REAL combat only ({@code docs/VETERANIA.md}
 * §4 anti-abuse). A "real enemy" is a monster ({@link Enemy}), another player, another
 * commander's guard, or a mob currently targeting our side - never a cow, a villager, an
 * armor stand or a friend.
 * <ul>
 *     <li>damage dealt to a real enemy: {@code xpPerDamage} per point, damage received from
 *     one: half that; both × {@code repeatDecay}^(kills of that enemy type) - fighting the
 *     same mob over and over stops paying (diminishing returns per type);</li>
 *     <li>the first fight against a new enemy type: {@code newEnemyTypeXp} (variety);</li>
 *     <li>a kill: {@code killXp} (same decay), counted by weapon class for traits;</li>
 *     <li>a fight (combat events less than 10 s apart) lasting more than
 *     {@code longFightSeconds}, survived: {@code longFightXp} and one battle;</li>
 *     <li>dropping under 3 hearts and living: one "near death" (the Tank trait's history).</li>
 * </ul>
 * Totems ({@link LivingUseTotemEvent}): a guard's totem only saves it once it has been in
 * hand for {@code totemDelayTicks} (Tank: {@code totemDelayTankTicks}) - a chest of totems
 * handed over mid-fight doesn't make anyone immortal. {@code kamikazeRevives} revives within
 * {@code kamikazeWindowSeconds} may (chance {@code kamikazeChance}) make the guard reckless
 * for a minute - and if it carries an explosive, it charges its target as a kamikaze.
 */
public final class VeterancyEvents {

    private static final long FIGHT_GAP_TICKS = 200L;
    private static final float NEAR_DEATH_HEALTH = 6.0F;

    /** guard -> {fight start, last combat tick} */
    private static final Map<UUID, long[]> FIGHT = new HashMap<>();
    /** guard -> tick it was last counted near death (one count per fight). */
    private static final Map<UUID, Long> NEAR_DEATH = new HashMap<>();
    /** guard -> tick a totem was first seen in its hands. */
    private static final Map<UUID, Long> TOTEM_SINCE = new HashMap<>();
    /** guard -> recent revive ticks. */
    private static final Map<UUID, Deque<Long>> REVIVES = new HashMap<>();

    // ==== Damage ====

    @SubscribeEvent
    public void onDamage(LivingDamageEvent.Post event) {
        LivingEntity victim = event.getEntity();
        if (!(victim.level() instanceof ServerLevel level)) return;
        float amount = event.getNewDamage();
        if (amount <= 0.0F) return;
        DamageSource source = event.getSource();
        Guard attacker = guardOf(source);

        if (attacker != null && realEnemy(attacker, victim)) {
            String type = typeOf(victim);
            VeterancyRegistry.Record r = Veterancy.edit(level, attacker.getUUID());
            double xp = amount * RallyConfig.d(RallyConfig.XP_PER_DAMAGE, 1.0D) * decay(r, type);
            if (!r.enemyTypes.containsKey(type) && r.enemyTypes.size() < VeterancyRegistry.MAX_TYPES) {
                r.enemyTypes.put(type, 0);
                xp += RallyConfig.i(RallyConfig.XP_NEW_ENEMY_TYPE, 25);
            }
            markCombat(level, attacker);
            Veterancy.addXp(level, attacker, xp);
        }

        if (victim instanceof Guard guard && GuardOwnership.hasOwner(guard)) {
            Entity by = source.getEntity();
            if (by instanceof LivingEntity enemy && realEnemy(guard, enemy)) {
                VeterancyRegistry.Record r = Veterancy.edit(level, guard.getUUID());
                markCombat(level, guard);
                Veterancy.addXp(level, guard, 0.5D * amount * RallyConfig.d(RallyConfig.XP_PER_DAMAGE, 1.0D)
                        * decay(r, typeOf(enemy)));
            }
            if (guard.isAlive() && guard.getHealth() < NEAR_DEATH_HEALTH) {
                long now = level.getGameTime();
                Long last = NEAR_DEATH.get(guard.getUUID());
                if (last == null || now - last > FIGHT_GAP_TICKS * 3) {
                    NEAR_DEATH.put(guard.getUUID(), now);
                    Veterancy.edit(level, guard.getUUID()).nearDeath++;
                }
            }
        }
    }

    // ==== Kills ====

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        if (!(victim.level() instanceof ServerLevel level)) return;
        if (victim instanceof Guard dead) forgetFight(dead.getUUID());
        Guard killer = guardOf(event.getSource());
        if (killer == null || !realEnemy(killer, victim)) return;
        String type = typeOf(victim);
        VeterancyRegistry.Record r = Veterancy.edit(level, killer.getUUID());
        double xp = RallyConfig.i(RallyConfig.XP_KILL, 10) * decay(r, type);
        if (r.enemyTypes.containsKey(type) || r.enemyTypes.size() < VeterancyRegistry.MAX_TYPES) {
            r.enemyTypes.merge(type, 1, Integer::sum);
        }
        ItemStack hand = killer.getMainHandItem();
        if (ScorchedGunsCompat.isGun(hand)) r.killsGun++;
        else if (hand.getItem() instanceof ProjectileWeaponItem || event.getSource().getDirectEntity() instanceof Projectile) r.killsRanged++;
        else r.killsMelee++;
        Veterancy.addXp(level, killer, xp);
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
        Veterancy.edit(level, guard.getUUID()).totems++;

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
            if (mounted) r.secondsMounted++;
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
        if (fight == null) FIGHT.put(guard.getUUID(), new long[]{now, now});
        else fight[1] = now;
    }

    private static void closeFight(ServerLevel level, Guard guard, long[] fight) {
        FIGHT.remove(guard.getUUID());
        if (!guard.isAlive()) return;
        long seconds = (fight[1] - fight[0]) / 20L;
        if (seconds >= RallyConfig.i(RallyConfig.XP_LONG_FIGHT_SECONDS, 12)) {
            Veterancy.edit(level, guard.getUUID()).battles++;
            Veterancy.addXp(level, guard, RallyConfig.i(RallyConfig.XP_LONG_FIGHT, 15));
        }
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
        if (other instanceof Player p) return !p.getUUID().equals(owner) && !p.isSpectator();
        if (other instanceof Guard g) {
            UUID o = GuardOwnership.getOwner(g);
            return o != null && !o.equals(owner);
        }
        if (other instanceof Enemy) return true;
        if (other instanceof Mob mob && mob.getTarget() != null) {
            LivingEntity t = mob.getTarget();
            return t.getUUID().equals(owner) || (t instanceof Guard tg && owner.equals(GuardOwnership.getOwner(tg)));
        }
        return false;
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
    }

    public static void forget(UUID guardId) {
        forgetFight(guardId);
        Veterancy.forget(guardId);
    }

    public static void clearAll() {
        FIGHT.clear();
        NEAR_DEATH.clear();
        TOTEM_SINCE.clear();
        REVIVES.clear();
        Veterancy.clearCache();
    }
}
