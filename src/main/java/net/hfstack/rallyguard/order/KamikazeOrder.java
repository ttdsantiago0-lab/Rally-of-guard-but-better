package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * KAMIKAZE - a deliberate, one-shot sacrifice (approved design, 2026-09-28). Not a
 * stance: the commander picks the guards and the target explicitly, and only guards
 * the commander has armed can take part.
 * <p>
 * <b>The explosive is whatever the commander put in the guard's OFF hand</b> (through
 * Guard Villagers' own inventory screen), as long as it is a TNT-like block: any
 * {@code BlockItem} whose block extends vanilla {@link TntBlock}. That covers vanilla
 * TNT and every Lucky TNT block with no whitelist - read from Lucky TNT Lib's NeoForge
 * source ({@code SlimingHD/Neo-LuckyTNTLib}): {@code LTNTBlock extends TntBlock} and
 * overrides {@code onCaughtFire} to spawn its own {@code PrimedLTNT extends PrimedTnt},
 * owned by the igniter. Detonation goes through that same generic NeoForge hook
 * ({@code IBlockExtension#onCaughtFire}, what fire or flint and steel use), so each mod's
 * own explosion runs exactly as designed; Rally only shortens the fuse.
 * <p>
 * Sequence: the guard leaves its post/formation, gets a short Speed + Resistance boost,
 * runs at the target, and within {@link #TRIGGER_DISTANCE} lights the charge at its feet
 * with a {@link #FUSE_TICKS} fuse and stops. When the fuse ends the guard dies - always,
 * even if the explosion itself wouldn't have killed it. The run is aborted (guard keeps
 * the target as a normal designated target) if the target disappears or after
 * {@link #MAX_RUN_TICKS}. Allies: the explosion's damage source names the guard as its
 * cause, and {@code RallyFriendlyFireHandler} already blocks damage between guards of the
 * same commander and from a guard to its own commander.
 * <p>
 * Runs are kept in memory only (a restart mid-run simply leaves the guard with a
 * designated target) and driven by {@link #tick(ServerLevel)}, called every tick from
 * {@code event.GuardStanceHandler} - there are only ever a handful active.
 */
public final class KamikazeOrder implements OrderExecutor {

    public static final double TRIGGER_DISTANCE = 2.5D;
    public static final int FUSE_TICKS = 10;
    public static final int MAX_RUN_TICKS = 20 * 30;
    private static final int BOOST_TICKS = 20 * 30;

    private record Run(UUID target, long startTick, long igniteTick) {
    }

    private static final Map<UUID, Run> RUNS = new HashMap<>();

    private final LivingEntity target;
    private int started;

    public KamikazeOrder(LivingEntity target) {
        this.target = target;
    }

    /** True if the guard is carrying something this order can detonate. */
    public static boolean isArmed(Guard guard) {
        ItemStack charge = guard.getOffhandItem();
        return charge.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof TntBlock;
    }

    /** How many guards actually set off on the last {@link #execute} (armed ones). */
    public int started() {
        return started;
    }

    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        ServerLevel level = player.serverLevel();
        DirectiveRegistry directives = DirectiveRegistry.get(level);
        started = 0;
        for (Guard guard : guards) {
            if (!isArmed(guard) || !CommandTargeting.wouldAccept(guard, target)) continue;

            GuardPosts.unpost(guard);
            guard.setFollowing(false);
            directives.setTarget(guard.getUUID(), target.getUUID(), target.getName().getString());
            directives.setHoldFire(guard.getUUID(), false);
            guard.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, BOOST_TICKS, 1));
            guard.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, BOOST_TICKS, 1));
            CommandTargeting.issue(guard, target);
            guard.getNavigation().moveTo(target, 1.2D);

            RUNS.put(guard.getUUID(), new Run(target.getUUID(), level.getGameTime(), -1L));
            started++;
        }
    }

    /** Drives every active run whose guard is in {@code level}. */
    public static void tick(ServerLevel level) {
        if (RUNS.isEmpty()) return;
        long now = level.getGameTime();

        Iterator<Map.Entry<UUID, Run>> it = RUNS.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, Run> entry = it.next();
            Run run = entry.getValue();
            Entity entity = level.getEntity(entry.getKey());
            if (!(entity instanceof Guard guard)) {
                // Not in this level (or unloaded/gone) - forget it once it's long overdue.
                if (now - run.startTick() > MAX_RUN_TICKS * 4L) it.remove();
                continue;
            }
            if (!guard.isAlive()) {
                it.remove();
                continue;
            }

            if (run.igniteTick() >= 0) {
                guard.getNavigation().stop(); // hold the lit charge
                if (now - run.igniteTick() >= FUSE_TICKS) {
                    guard.kill();
                    it.remove();
                }
                continue;
            }

            Entity targetEntity = level.getEntity(run.target());
            if (!(targetEntity instanceof LivingEntity target) || !target.isAlive()
                    || now - run.startTick() > MAX_RUN_TICKS || !isArmed(guard)) {
                it.remove(); // abort: it stays a normal guard with a designated target
                continue;
            }

            if (guard.distanceToSqr(target) <= TRIGGER_DISTANCE * TRIGGER_DISTANCE) {
                ignite(level, guard);
                entry.setValue(new Run(run.target(), run.startTick(), now));
            } else if (now % 10 == 0) {
                if (guard.getTarget() != target) CommandTargeting.issue(guard, target);
                guard.getNavigation().moveTo(target, 1.2D);
            }
        }
    }

    private static void ignite(ServerLevel level, Guard guard) {
        ItemStack charge = guard.getOffhandItem();
        BlockState state = ((BlockItem) charge.getItem()).getBlock().defaultBlockState();
        BlockPos pos = guard.blockPosition();

        state.getBlock().onCaughtFire(state, level, pos, null, guard);
        charge.shrink(1);
        guard.getNavigation().stop();

        // Every TNT mod picks its own fuse; a kamikaze needs a short, predictable one.
        List<PrimedTnt> lit = level.getEntitiesOfClass(PrimedTnt.class, new AABB(pos).inflate(1.5D),
                primed -> primed.getOwner() == guard);
        for (PrimedTnt primed : lit) {
            primed.setFuse(Math.min(primed.getFuse(), FUSE_TICKS));
        }
    }

    /** Forgets every run - on server stop, like the other in-memory command state. */
    public static void clearAll() {
        RUNS.clear();
    }
}
