package net.hfstack.rallyguard.order;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Phase 3E: the commander's "pointer" - what they last pointed at in the world with the
 * Commander's Ledger, plus who last attacked their side. This is how the Ledger (an open
 * GUI with no view of the world) gets an explicit target or destination without any
 * client raycast: the player points first (sneak + right-click an entity or a block with
 * the Ledger), then opens the Ledger and orders against the mark.
 * <p>
 * Deliberately in-memory only (cleared on server stop): a mark is a momentary tactical
 * pointer, not army state - nothing about a squad's standing orders depends on it after
 * the order is given (the designated target itself is persisted in
 * {@code DirectiveRegistry}, the destination as Guard Villagers' patrol posts).
 */
public final class CommanderMarks {
    private CommanderMarks() {
    }

    /** How long "who attacked us" stays relevant for the Counterattack target option. */
    private static final long AGGRESSOR_MEMORY_TICKS = 20L * 60L;

    public record MarkedEntity(UUID id, String name) {
    }

    public record MarkedPosition(ResourceKey<Level> dimension, BlockPos pos, float facingYaw) {
    }

    public record Aggressor(UUID id, String name, long gameTime) {
    }

    private static final Map<UUID, MarkedEntity> ENTITY_MARKS = new HashMap<>();
    private static final Map<UUID, MarkedPosition> POSITION_MARKS = new HashMap<>();
    private static final Map<UUID, Aggressor> AGGRESSORS = new HashMap<>();

    public static void markEntity(UUID commander, Entity target) {
        ENTITY_MARKS.put(commander, new MarkedEntity(target.getUUID(), target.getName().getString()));
    }

    public static void markPosition(UUID commander, ResourceKey<Level> dimension, BlockPos pos, float facingYaw) {
        POSITION_MARKS.put(commander, new MarkedPosition(dimension, pos, facingYaw));
    }

    public static void clearMarks(UUID commander) {
        ENTITY_MARKS.remove(commander);
        POSITION_MARKS.remove(commander);
    }

    /** The marked entity, if it is still alive and loaded in {@code level}. */
    @Nullable
    public static LivingEntity markedEntity(UUID commander, ServerLevel level) {
        MarkedEntity mark = ENTITY_MARKS.get(commander);
        if (mark == null) return null;
        Entity e = level.getEntity(mark.id());
        return e instanceof LivingEntity living && living.isAlive() ? living : null;
    }

    /** The marked position, if it is in {@code level}'s dimension. */
    @Nullable
    public static MarkedPosition markedPosition(UUID commander, ServerLevel level) {
        MarkedPosition mark = POSITION_MARKS.get(commander);
        return mark != null && mark.dimension().equals(level.dimension()) ? mark : null;
    }

    public static void recordAggressor(UUID commander, LivingEntity attacker, long gameTime) {
        AGGRESSORS.put(commander, new Aggressor(attacker.getUUID(), attacker.getName().getString(), gameTime));
    }

    /** Whoever most recently hurt the commander or one of their guards, if still alive,
     *  loaded in {@code level}, and recent enough to still count. */
    @Nullable
    public static LivingEntity recentAggressor(UUID commander, ServerLevel level) {
        Aggressor a = AGGRESSORS.get(commander);
        if (a == null || level.getGameTime() - a.gameTime() > AGGRESSOR_MEMORY_TICKS) return null;
        Entity e = level.getEntity(a.id());
        return e instanceof LivingEntity living && living.isAlive() ? living : null;
    }

    public static void clearAll() {
        ENTITY_MARKS.clear();
        POSITION_MARKS.clear();
        AGGRESSORS.clear();
    }
}
