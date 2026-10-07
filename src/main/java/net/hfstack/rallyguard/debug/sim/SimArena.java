package net.hfstack.rallyguard.debug.sim;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * E2A block 3 - the simulation's ground arena (no more arena in the sky).
 * <ul>
 *     <li>{@link #find}: looks for a site {@code arenaDistance} blocks from the player (8
 *     directions, then 1.5× further): every chunk is checked for block entities (chests,
 *     furnaces... = someone's build) and the surface, sampled every 4 blocks, must be dry,
 *     within 10 blocks of height, and free of planks, wool, stairs, slabs, doors, beds, fences
 *     and walls. No site = the batch aborts with a message; nothing is touched.</li>
 *     <li>{@link #buildStep}: a solid flat floor of {@code floorBlock}, air above it, a 4-high
 *     perimeter wall - a few rows per tick, never one huge tick. The chunks stay
 *     force-loaded while the batch runs so the fight ticks wherever the player stands.</li>
 *     <li>{@link #terrain}: per-scenario features (hill under one side, cover pillars, a wall
 *     with a 4-wide gap, a simple forest), removed again by {@link #clearTerrain}.</li>
 * </ul>
 * Blue starts on the +Z side facing north, red on the -Z side facing south (or on blue's
 * +X flank facing west for "flank").
 */
public final class SimArena {

    public final ServerLevel level;
    public final int size;
    /** Centre of the floor's top face (units stand at y). */
    public final int cx, cz, y;
    private final int clearTop;
    private final BlockState floor;
    private int buildRow = -1;
    private final List<BlockPos> placed = new ArrayList<>();
    private final List<ChunkPos> forced = new ArrayList<>();

    private SimArena(ServerLevel level, int size, int cx, int cz, int y, int clearTop, BlockState floor) {
        this.level = level;
        this.size = size;
        this.cx = cx;
        this.cz = cz;
        this.y = y;
        this.clearTop = clearTop;
        this.floor = floor;
    }

    public int half() {
        return size / 2;
    }

    /** A free site, or null (see the class javadoc). */
    @Nullable
    public static SimArena find(ServerLevel level, BlockPos player, int size, int distance, String floorId) {
        Block floorBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(floorId == null ? "" : floorId));
        BlockState floor = (floorBlock == null || floorBlock == Blocks.AIR ? Blocks.STONE : floorBlock).defaultBlockState();
        for (double scale : new double[]{1.0D, 1.5D}) {
            for (int dir = 0; dir < 8; dir++) {
                double a = Math.PI / 4.0D * dir;
                int cx = player.getX() + (int) Math.round(Math.cos(a) * distance * scale);
                int cz = player.getZ() + (int) Math.round(Math.sin(a) * distance * scale);
                SimArena arena = check(level, cx, cz, size, floor);
                if (arena != null) return arena;
            }
        }
        return null;
    }

    @Nullable
    private static SimArena check(ServerLevel level, int cx, int cz, int size, BlockState floor) {
        int h = size / 2 + 1;
        for (int chx = (cx - h) >> 4; chx <= (cx + h) >> 4; chx++) {
            for (int chz = (cz - h) >> 4; chz <= (cz + h) >> 4; chz++) {
                LevelChunk chunk = level.getChunk(chx, chz);
                if (!chunk.getBlockEntities().isEmpty()) return null; // someone's build
            }
        }
        List<Integer> heights = new ArrayList<>();
        int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
        for (int x = cx - h; x <= cx + h; x += 4) {
            for (int z = cz - h; z <= cz + h; z += 4) {
                int top = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                BlockPos surface = new BlockPos(x, top - 1, z);
                BlockState state = level.getBlockState(surface);
                if (!state.getFluidState().isEmpty() || !level.getFluidState(surface.above()).isEmpty()) return null;
                if (built(state)) return null;
                heights.add(top);
                max = Math.max(max, top);
                min = Math.min(min, top);
            }
        }
        if (heights.isEmpty() || max - min > 10) return null;
        int[] sorted = heights.stream().mapToInt(Integer::intValue).toArray();
        Arrays.sort(sorted);
        int y = sorted[sorted.length / 2];
        if (y <= level.getMinBuildHeight() + 2 || max + 12 >= level.getMaxBuildHeight()) return null;
        return new SimArena(level, size, cx, cz, y, Math.min(level.getMaxBuildHeight() - 1, max + 8), floor);
    }

    private static boolean built(BlockState s) {
        return s.is(BlockTags.PLANKS) || s.is(BlockTags.WOOL) || s.is(BlockTags.STAIRS) || s.is(BlockTags.SLABS)
                || s.is(BlockTags.DOORS) || s.is(BlockTags.BEDS) || s.is(BlockTags.FENCES) || s.is(BlockTags.WALLS);
    }

    public void forceChunks(boolean on) {
        if (on) {
            int h = half() + 2;
            for (int chx = (cx - h) >> 4; chx <= (cx + h) >> 4; chx++) {
                for (int chz = (cz - h) >> 4; chz <= (cz + h) >> 4; chz++) {
                    level.setChunkForced(chx, chz, true);
                    forced.add(new ChunkPos(chx, chz));
                }
            }
        } else {
            for (ChunkPos p : forced) level.setChunkForced(p.x, p.z, false);
            forced.clear();
        }
    }

    /** Builds a few rows; true when the arena is complete. */
    public boolean buildStep(int rows) {
        int h = half();
        for (int r = 0; r < rows; r++) {
            buildRow++;
            int x = cx - h - 1 + buildRow;
            if (x > cx + h + 1) return true;
            for (int z = cz - h - 1; z <= cz + h + 1; z++) {
                boolean edge = x == cx - h - 1 || x == cx + h + 1 || z == cz - h - 1 || z == cz + h + 1;
                set(new BlockPos(x, y - 1, z), floor);
                set(new BlockPos(x, y - 2, z), floor);
                for (int yy = y; yy <= clearTop; yy++) {
                    set(new BlockPos(x, yy, z), edge && yy < y + 4 ? floor : Blocks.AIR.defaultBlockState());
                }
            }
        }
        return false;
    }

    private void set(BlockPos pos, BlockState state) {
        if (level.getBlockState(pos) != state) level.setBlock(pos, state, Block.UPDATE_CLIENTS);
    }

    // ==== Terrain per scenario ====

    /** Places the scenario's terrain; {@code blueZ}/{@code redZ} are the sides' start lines. */
    public void terrain(String kind, int blueX, int blueZ, int redX, int redZ, long seed) {
        Random rnd = new Random(seed);
        switch (kind) {
            case "hill" -> {
                // A stepped mound under BLUE: +4 at its centre, 10 blocks across.
                for (int dx = -10; dx <= 10; dx++) {
                    for (int dz = -10; dz <= 10; dz++) {
                        int height = Math.min(4, (int) Math.floor(4.0D - Math.sqrt(dx * dx + dz * dz) / 2.5D));
                        for (int k = 0; k < height; k++) place(new BlockPos(blueX + dx, y + k, blueZ + dz), floor);
                    }
                }
            }
            case "cover" -> {
                int mid = (blueZ + redZ) / 2;
                for (int x = cx - half() + 3; x <= cx + half() - 3; x += 4) {
                    for (int row = -1; row <= 1; row++) {
                        int z = mid + row * 6 + ((x / 4) % 2 == 0 ? 0 : 2);
                        place(new BlockPos(x, y, z), floor);
                        place(new BlockPos(x, y + 1, z), floor);
                    }
                }
            }
            case "wall" -> {
                int mid = (blueZ + redZ) / 2;
                for (int x = cx - half(); x <= cx + half(); x++) {
                    if (Math.abs(x - cx) <= 2) continue; // the 4-wide chokepoint
                    for (int k = 0; k < 3; k++) place(new BlockPos(x, y + k, mid), floor);
                }
            }
            case "forest" -> {
                int z0 = Math.min(blueZ, redZ) + 3, z1 = Math.max(blueZ, redZ) - 3;
                for (int x = cx - half() + 2; x <= cx + half() - 2; x++) {
                    for (int z = z0; z <= z1; z++) {
                        if (rnd.nextInt(25) != 0) continue;
                        for (int k = 0; k < 4; k++) place(new BlockPos(x, y + k, z), Blocks.OAK_LOG.defaultBlockState());
                        place(new BlockPos(x, y + 4, z), Blocks.OAK_LEAVES.defaultBlockState());
                    }
                }
            }
            default -> {
            }
        }
    }

    private void place(BlockPos pos, BlockState state) {
        if (!level.getBlockState(pos).isAir()) return;
        level.setBlock(pos, state, Block.UPDATE_CLIENTS);
        placed.add(pos);
    }

    public void clearTerrain() {
        for (BlockPos p : placed) level.setBlock(p, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
        placed.clear();
    }

    /** The standing height at (x, z): the floor, or the top of a terrain feature (≤ 6 up). */
    public int standY(int x, int z) {
        int yy = y;
        while (yy < y + 6 && !level.getBlockState(new BlockPos(x, yy, z)).isAir()) yy++;
        return yy;
    }

    /** Clamps a coordinate inside the walls. */
    public int clampX(double x) {
        return (int) Math.max(cx - half() + 1, Math.min(cx + half() - 1, Math.floor(x)));
    }

    public int clampZ(double z) {
        return (int) Math.max(cz - half() + 1, Math.min(cz + half() - 1, Math.floor(z)));
    }

    public boolean inside(double x, double z) {
        return Math.abs(x - cx) <= half() + 1 && Math.abs(z - cz) <= half() + 1;
    }
}
