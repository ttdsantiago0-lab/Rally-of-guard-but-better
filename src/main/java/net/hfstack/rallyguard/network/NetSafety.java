package net.hfstack.rallyguard.network;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * E3B R6 (B8) - what the server accepts from a client: a packet budget per player (token bucket,
 * {@link #BURST} packets, refilled at {@link #PER_SECOND}/s; the rest are dropped and logged once
 * every 10 s), coordinates that are finite, inside the world border and within
 * {@link #MAX_DISTANCE} blocks of the sender (a client can never make the server load or
 * generate far chunks), and text caps. Everything a client sends goes through here before it
 * touches the world.
 */
public final class NetSafety {
    private NetSafety() {
    }

    public static final int BURST = 40;
    public static final double PER_SECOND = 20.0D;
    public static final double MAX_DISTANCE = 512.0D;

    private static final class Bucket {
        double tokens = BURST;
        long last;
        long warned = Long.MIN_VALUE / 2;
    }

    private static final Map<UUID, Bucket> BUCKETS = new HashMap<>();

    /** True if this player may send one more packet now. */
    public static boolean allow(ServerPlayer player) {
        long now = player.serverLevel().getGameTime();
        Bucket b = BUCKETS.computeIfAbsent(player.getUUID(), k -> new Bucket());
        if (b.last == 0) b.last = now;
        b.tokens = Math.min(BURST, b.tokens + (now - b.last) * PER_SECOND / 20.0D);
        b.last = now;
        if (b.tokens >= 1.0D) {
            b.tokens -= 1.0D;
            return true;
        }
        if (now - b.warned > 200) {
            b.warned = now;
            LogUtils.getLogger().warn("[rallyguard] {} sends Rally packets too fast; extra ones are dropped", player.getName().getString());
        }
        return false;
    }

    /** The point if it is finite, inside the border and within {@link #MAX_DISTANCE} of the player; else null. */
    @Nullable
    public static Vec3 point(ServerPlayer player, double x, double y, double z) {
        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)) return null;
        double dx = x - player.getX(), dz = z - player.getZ();
        if (dx * dx + dz * dz > MAX_DISTANCE * MAX_DISTANCE) return null;
        if (!player.serverLevel().getWorldBorder().isWithinBounds(x, z)) return null;
        if (y < player.serverLevel().getMinBuildHeight() - 16 || y > player.serverLevel().getMaxBuildHeight() + 16) return null;
        return new Vec3(x, y, z);
    }

    /** True for a point the player could legitimately mean (see {@link #point}). */
    public static boolean near(ServerPlayer player, double x, double z) {
        return point(player, x, player.getY(), z) != null;
    }

    public static void forget(UUID player) {
        BUCKETS.remove(player);
    }

    public static void clearAll() {
        BUCKETS.clear();
    }
}
