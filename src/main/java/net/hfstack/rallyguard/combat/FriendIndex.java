package net.hfstack.rallyguard.combat;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * E1-G: "where are our own people" - a coarse grid (16-block cells) of every owned
 * guard's position per commander, rebuilt once a second from the guard list the stance
 * loop already collects (one pass, no entity scans). A volley asks it whether a friend
 * stands within 5 blocks of where the arrows will land; the commander's live position is
 * checked too. Positions are at most one second old - good enough for a safety margin.
 */
public final class FriendIndex {
    private FriendIndex() {
    }

    private record Cell(UUID owner, int cx, int cz) {
    }

    private static final Map<ResourceKey<Level>, Map<Cell, List<Vec3>>> INDEX = new HashMap<>();

    public static void rebuild(ResourceKey<Level> dimension, List<? extends Guard> guards) {
        Map<Cell, List<Vec3>> map = new HashMap<>();
        for (Guard g : guards) {
            UUID owner = GuardOwnership.getOwner(g);
            if (owner == null) continue;
            map.computeIfAbsent(new Cell(owner, g.getBlockX() >> 4, g.getBlockZ() >> 4), k -> new ArrayList<>())
                    .add(g.position());
        }
        INDEX.put(dimension, map);
    }

    /** True if the commander or one of their guards is within {@code radius} (≤ 16) of {@code p}. */
    public static boolean friendNear(ServerLevel level, UUID owner, Vec3 p, double radius) {
        Player commander = level.getPlayerByUUID(owner);
        if (commander != null && commander.position().distanceToSqr(p) <= radius * radius) return true;
        Map<Cell, List<Vec3>> map = INDEX.get(level.dimension());
        if (map == null) return false;
        int cx = ((int) Math.floor(p.x)) >> 4, cz = ((int) Math.floor(p.z)) >> 4;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                List<Vec3> list = map.get(new Cell(owner, cx + dx, cz + dz));
                if (list == null) continue;
                for (Vec3 v : list) {
                    if (v.distanceToSqr(p) <= radius * radius) return true;
                }
            }
        }
        return false;
    }

    public static void clearAll() {
        INDEX.clear();
    }
}
