package net.hfstack.rallyguard.combat;

import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * E1-G glue:
 * <ul>
 *     <li>gives every Guard a {@link RangedFireGoal} (priority 2) when it joins a level;</li>
 *     <li><b>finite arrows for Guard Villagers' own fire too</b>: GV's bow and crossbow
 *     goals create arrows out of nothing (a mob's {@code getProjectile} is a fresh arrow
 *     stack). Any arrow an owned guard puts in the world that Rally didn't fire is paid
 *     here from the guard's ammo ({@link Ammo}); with none, the arrow is cancelled.
 *     Push-based (one event per arrow), nothing scanned.</li>
 *     <li>the per-squad volley budget.</li>
 * </ul>
 */
public final class RangedFire {

    private static boolean spawningRallyArrow;

    /** squad key -> {second, arrows this second} */
    private static final Map<String, long[]> BUDGET = new HashMap<>();

    static void spawnRallyArrow(ServerLevel level, AbstractArrow arrow) {
        spawningRallyArrow = true;
        try {
            level.addFreshEntity(arrow);
        } finally {
            spawningRallyArrow = false;
        }
    }

    static boolean tryVolley(UUID owner, String squadKey) {
        long second = System.currentTimeMillis() / 1000L;
        long[] b = BUDGET.computeIfAbsent(owner + "/" + squadKey, k -> new long[]{second, 0});
        if (b[0] != second) {
            b[0] = second;
            b[1] = 0;
        }
        if (b[1] >= RallyConfig.i(RallyConfig.VOLLEY_ARROWS_PER_SQUAD_PER_SECOND, 10)) return false;
        b[1]++;
        return true;
    }

    @SubscribeEvent
    public void onJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getEntity() instanceof Guard guard) {
            boolean present = guard.goalSelector.getAvailableGoals().stream()
                    .anyMatch(w -> w.getGoal() instanceof RangedFireGoal);
            if (!present) guard.goalSelector.addGoal(2, new RangedFireGoal(guard));
            return;
        }
        if (spawningRallyArrow || event.loadedFromDisk()) return;
        if (!(event.getEntity() instanceof AbstractArrow arrow)) return;
        if (!(arrow.getOwner() instanceof Guard shooter) || !GuardOwnership.hasOwner(shooter)) return;
        if (!RallyConfig.on(RallyConfig.FINITE_ARROWS)) return;
        if (Ammo.consume(shooter, Ammo.ARROWS, 1) == 0) {
            event.setCanceled(true); // no arrows: no shot
        } else {
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
        }
    }

    public static void clearAll() {
        BUDGET.clear();
    }
}
