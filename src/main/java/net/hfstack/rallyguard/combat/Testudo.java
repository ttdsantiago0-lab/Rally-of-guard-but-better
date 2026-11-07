package net.hfstack.rallyguard.combat;

import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.FormationRegistry;
import net.hfstack.rallyguard.formation.FormationShape;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * Testudo (David 2026-11-06; design {@code docs/ESCOLTA_Y_TESTUDO.md} §4): shield-bearers mustered in
 * {@link FormationShape#TESTUDO} - a tight block, one block between guards - keep their shields raised while an enemy
 * is near, do not break ranks to chase (they fight what reaches their place), and, with a shield made for it, block
 * ARROWS AND BULLETS FROM ANY SIDE while a testudo comrade stands next to them: the roof and the flanks of the block
 * cover what a single shield, which only guards its front half, cannot. Blows keep the vanilla rule (front only): the
 * testudo is for marching under fire, not for a brawl (measured: shields always up against a zombie horde are worse).
 * <p>
 * Which shields cover all round: the scutum ({@code item/ScutumItem}); any shield with {@code fieldManual.testudoAllShields}
 * (default false) or the sim tag {@code testudo:anyShield}.
 */
public final class Testudo {
    private Testudo() {
    }

    /** Simulation only: every shield covers all round in a testudo (tag {@code testudo:anyShield}). */
    public static boolean simAnyShield;
    /** Simulation only: no testudo rules at all, only the shape (tag {@code off:testudo}). */
    public static boolean simOff;

    /** A testudo comrade this close covers the side the shield doesn't. */
    private static final double COVER = 1.6D;
    /** An enemy this close keeps the shields up. */
    public static final double ALERT = 24.0D;
    /** A guard in the testudo never fights farther than this from its place. */
    public static final double LEASH = 3.0D;

    private record Seen(long tick, boolean member) {
    }

    /** Membership, worked out at most once per tick per guard (a projectile hit asks for every neighbour). */
    private static final java.util.Map<java.util.UUID, Seen> SEEN = new java.util.HashMap<>();

    /**
     * Is this guard a shield-bearer of a testudo: posted on its TESTUDO formation slot? Posted ON THE SLOT, not "standing
     * in its place": the first version asked for that, and a guard that stepped 2.5 blocks out to hit something stopped
     * being a member - lost the testudo's leash, chased, and never came back (round 18: 9-15 s a run in the testudo).
     * Being tight enough to be covered is the neighbour test of {@link #onShieldBlock}. A guard freed or following keeps
     * its remembered slot (Reform uses it) but has no post there, so it is no longer in the testudo.
     */
    public static boolean member(Guard guard) {
        if (simOff || !(guard.level() instanceof ServerLevel level) || !FieldManual.shieldBearer(guard)) return false;
        long now = level.getGameTime();
        Seen seen = SEEN.get(guard.getUUID());
        if (seen != null && seen.tick() == now) return seen.member();
        boolean in = false;
        FormationRegistry.Slot slot = FormationRegistry.get(level).slotOf(guard.getUUID());
        net.minecraft.core.BlockPos post = net.hfstack.rallyguard.order.CommandTargeting.postOf(guard);
        if (slot != null && slot.shape() == FormationShape.TESTUDO && post != null) {
            List<net.minecraft.world.phys.Vec3> pos = slot.formation().positions(slot.anchor(), slot.facingYaw(), slot.count());
            if (slot.index() < pos.size()) {
                net.minecraft.world.phys.Vec3 p = pos.get(slot.index());
                double dx = post.getX() + 0.5D - p.x, dz = post.getZ() + 0.5D - p.z;
                in = dx * dx + dz * dz <= 3.0D * 3.0D;
            }
        }
        if (SEEN.size() > 1024) SEEN.clear();
        SEEN.put(guard.getUUID(), new Seen(now, in));
        return in;
    }

    /** Shields up in a testudo whenever an enemy is within {@link #ALERT} blocks (checked once a second). */
    public static boolean wantsUp(ServerLevel level, Guard guard) {
        if (!member(guard)) return false;
        return !level.getEntitiesOfClass(LivingEntity.class, guard.getBoundingBox().inflate(ALERT),
                e -> e.isAlive() && e instanceof Enemy).isEmpty();
    }

    /** The scutum covers all round; any other shield only with the switch (or the sim tag). */
    private static boolean coversAllRound(Guard guard) {
        return Scutum.is(guard.getUseItem()) || simAnyShield || RallyConfig.on(RallyConfig.TESTUDO_ALL_SHIELDS);
    }

    /** The roof and the flanks: a projectile from any side is blocked when a testudo comrade stands next to this one. */
    @SubscribeEvent
    public static void onShieldBlock(LivingShieldBlockEvent event) {
        if (event.getBlocked() || !(event.getEntity() instanceof Guard guard) || guard.level().isClientSide()) return;
        if (!guard.isBlocking() || !event.getDamageSource().is(DamageTypeTags.IS_PROJECTILE)) return;
        if (event.getDamageSource().is(DamageTypeTags.BYPASSES_SHIELD) || !coversAllRound(guard) || !member(guard)) return;
        List<Guard> near = guard.level().getEntitiesOfClass(Guard.class, guard.getBoundingBox().inflate(COVER),
                g -> g != guard && g.isAlive() && member(g));
        if (near.isEmpty()) return;
        event.setBlocked(true);
        net.hfstack.rallyguard.debug.SimProbe.count(guard, "act:testudoCover");
    }
}
