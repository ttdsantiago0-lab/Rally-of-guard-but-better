package net.hfstack.rallyguard.debug.sim;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
 *     with a 4-wide gap, a simple forest; E3A A9: plain, mountain, river, lava with a rim,
 *     cliff; 2026-11-08 structures: village, corridors, fort, hall), removed again by {@link #clearTerrain}.</li>
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
    /** E3A A9: floor blocks swapped for water/lava, restored by {@link #clearTerrain}. */
    private final Map<BlockPos, BlockState> replaced = new LinkedHashMap<>();
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

    /**
     * 2026-11-05: puts the floor, the edge wall and the air above back as {@link #buildStep} laid them, in one go;
     * returns how many blocks it had to fix. The arena used to be built once per batch, so creeper craters piled up
     * run after run in mixed waves: a zombie down a hole no archer could see and no posted shield would fetch kept
     * runs going until the "stuck" rule ended them (6-9 of 13-18 line runs in rondas 10 and 13). Craters made during
     * a run still count - only earlier runs' damage is undone.
     */
    public int repair() {
        int h = half(), fixed = 0;
        for (int x = cx - h - 1; x <= cx + h + 1; x++) {
            for (int z = cz - h - 1; z <= cz + h + 1; z++) {
                boolean edge = x == cx - h - 1 || x == cx + h + 1 || z == cz - h - 1 || z == cz + h + 1;
                fixed += fix(new BlockPos(x, y - 1, z), floor) + fix(new BlockPos(x, y - 2, z), floor);
                for (int yy = y; yy <= clearTop; yy++) {
                    fixed += fix(new BlockPos(x, yy, z), edge && yy < y + 4 ? floor : Blocks.AIR.defaultBlockState());
                }
            }
        }
        return fixed;
    }

    private int fix(BlockPos pos, BlockState state) {
        if (level.getBlockState(pos) == state) return 0;
        level.setBlock(pos, state, Block.UPDATE_CLIENTS);
        return 1;
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
            case "mountain" -> {
                // E3A A9: a broad mountain between the sides, 5 up at its peak, 16 blocks in radius:
                // both sides must climb; its crest blocks the line of sight.
                int mz = (blueZ + redZ) / 2;
                for (int dx = -16; dx <= 16; dx++) {
                    for (int dz = -16; dz <= 16; dz++) {
                        int height = Math.min(5, (int) Math.floor(5.0D - Math.sqrt(dx * dx + dz * dz) / 3.2D));
                        for (int k = 0; k < height; k++) place(new BlockPos(cx + dx, y + k, mz + dz), floor);
                    }
                }
            }
            case "river" -> {
                // E3A A9: a river across the field, 5 wide and ONE block deep - guards wade or swim
                // across and can always stand with their heads out (no drowning).
                int mz = (blueZ + redZ) / 2;
                for (int x = cx - half(); x <= cx + half(); x++) {
                    for (int dz = -2; dz <= 2; dz++) replace(new BlockPos(x, y - 1, mz + dz), Blocks.WATER.defaultBlockState());
                }
            }
            case "lava" -> {
                // E3A A9: lava only as an OBSTACLE: a 3-wide channel one block deep with a 1-high
                // rim on both banks, crossed by a 5-wide stone bridge in the middle.
                int mz = (blueZ + redZ) / 2;
                for (int x = cx - half(); x <= cx + half(); x++) {
                    if (Math.abs(x - cx) <= 2) continue; // the bridge
                    for (int dz = -1; dz <= 1; dz++) replace(new BlockPos(x, y - 1, mz + dz), Blocks.LAVA.defaultBlockState());
                    place(new BlockPos(x, y, mz - 2), floor);
                    place(new BlockPos(x, y, mz + 2), floor);
                }
            }
            case "cliff" -> {
                // E3A A9: red stands on a plateau 5 high; the cliff face runs across the field, with a
                // ramp along the east wall (rising 1 per block) so melee can climb.
                int mz = (blueZ + redZ) / 2;
                int dir = redZ < blueZ ? -1 : 1;
                int far = dir < 0 ? cz - half() : cz + half();
                int rampX = cx + half() - 4;
                for (int x = cx - half(); x <= cx + half(); x++) {
                    for (int z = mz; dir < 0 ? z >= far : z <= far; z += dir) {
                        int height = x >= rampX ? Math.min(5, Math.abs(z - mz)) : 5;
                        for (int k = 0; k < height; k++) place(new BlockPos(x, y + k, z), floor);
                    }
                }
            }
            // ==== 2026-11-08: structures (David: "que no sean inútiles ahí") ====
            case "village" -> {
                // Two staggered rows of small stone houses (7 x 7, walls 3 high, plank roof, an oak door facing blue).
                // Row A straddles red's start line: some reds start indoors, some in the streets, and the ones that
                // land on a wall start on a roof. Row B stands halfway, so blue weaves through streets. Guard
                // Villagers' guards open doors (GuardsOpenDoors, default true); a zombie can't.
                int toBlue = blueZ > redZ ? 1 : -1;
                for (int k = -2; k <= 2; k++) house(cx + k * 12, redZ, toBlue);
                for (int k = -2; k <= 1; k++) house(cx + 6 + k * 12, (blueZ + redZ) / 2, toBlue);
            }
            case "corridors" -> {
                // A roofed stone block across the whole field between the sides, carved into 3-wide, 3-high
                // corridors: one from blue's side to the middle, a cross corridor, and two on to red's side 16 blocks
                // out each way. Nobody can walk around it: the fight happens in the corridors.
                int toBlue = blueZ > redZ ? 1 : -1;
                int zb = blueZ - 4 * toBlue, zr = redZ + 4 * toBlue, mid = (blueZ + redZ) / 2;
                for (int x = cx - half(); x <= cx + half(); x++) {
                    for (int z = Math.min(zb, zr); z <= Math.max(zb, zr); z++) {
                        boolean open = Math.abs(x - cx) <= 1 && (z - mid) * toBlue >= -1
                                || Math.abs(z - mid) <= 1 && Math.abs(x - cx) <= 17
                                || Math.abs(Math.abs(x - cx) - 16) <= 1 && (z - mid) * toBlue <= 1;
                        if (!open) for (int k = 0; k < 3; k++) place(new BlockPos(x, y + k, z), floor);
                        place(new BlockPos(x, y + 3, z), floor);
                    }
                }
            }
            case "fort" -> {
                // A walled yard around red's start (29 x 15, walls 4 high) with one 3-wide gate facing blue.
                int toBlue = blueZ > redZ ? 1 : -1;
                int zGate = redZ + 6 * toBlue, zBack = redZ - 8 * toBlue;
                for (int x = cx - 14; x <= cx + 14; x++) {
                    for (int z = Math.min(zGate, zBack); z <= Math.max(zGate, zBack); z++) {
                        boolean edge = Math.abs(x - cx) == 14 || z == zGate || z == zBack;
                        if (!edge || z == zGate && Math.abs(x - cx) <= 1) continue;
                        for (int k = 0; k < 4; k++) place(new BlockPos(x, y + k, z), floor);
                    }
                }
            }
            case "hall" -> {
                // A great hall holding both sides (41 wide, walls and roof 5 up, pillars every 6 blocks): fighting
                // indoors, where a high arrow arc hits the ceiling.
                int z0 = Math.min(blueZ, redZ) - 6, z1 = Math.max(blueZ, redZ) + 6, zm = (z0 + z1) / 2;
                for (int x = cx - 20; x <= cx + 20; x++) {
                    for (int z = z0; z <= z1; z++) {
                        boolean edge = Math.abs(x - cx) == 20 || z == z0 || z == z1;
                        boolean pillar = !edge && Math.floorMod(x - cx, 6) == 3 && Math.floorMod(z - zm, 6) == 3
                                && z != blueZ && z != redZ;
                        if (edge || pillar) for (int k = 0; k < 5; k++) place(new BlockPos(x, y + k, z), floor);
                        place(new BlockPos(x, y + 5, z), floor);
                    }
                }
            }
            default -> { // "flat" / "plain": the bare floor
            }
        }
    }

    /** One village house centred on (hx, hz); its door faces +Z when {@code toBlue} is 1, -Z when -1. */
    private void house(int hx, int hz, int toBlue) {
        BlockState wall = Blocks.COBBLESTONE.defaultBlockState(), roof = Blocks.OAK_PLANKS.defaultBlockState();
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                int x = hx + dx, z = hz + dz;
                if (Math.abs(x - cx) >= half() || Math.abs(z - cz) >= half()) continue;
                boolean edge = Math.abs(dx) == 3 || Math.abs(dz) == 3;
                if (edge && !(dx == 0 && dz == 3 * toBlue)) for (int k = 0; k < 3; k++) place(new BlockPos(x, y + k, z), wall);
                place(new BlockPos(x, y + 3, z), roof);
            }
        }
        BlockPos door = new BlockPos(hx, y, hz + 3 * toBlue);
        if (Math.abs(door.getZ() - cz) >= half()) return;
        BlockState d = Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.FACING, toBlue > 0 ? Direction.SOUTH : Direction.NORTH);
        place(door, d.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
        place(door.above(), d.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
        place(door.above(2), wall); // the lintel
    }

    private void replace(BlockPos pos, BlockState state) {
        replaced.putIfAbsent(pos, level.getBlockState(pos));
        level.setBlock(pos, state, Block.UPDATE_ALL);
    }

    private void place(BlockPos pos, BlockState state) {
        if (!level.getBlockState(pos).isAir()) return;
        level.setBlock(pos, state, Block.UPDATE_CLIENTS);
        placed.add(pos);
    }

    /** Blocks the current terrain placed or swapped (telemetry: a structure that silently failed to build shows 0). */
    public int terrainBlocks() {
        return placed.size() + replaced.size();
    }

    public void clearTerrain() {
        for (BlockPos p : placed) level.setBlock(p, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
        placed.clear();
        for (Map.Entry<BlockPos, BlockState> e : replaced.entrySet()) level.setBlock(e.getKey(), e.getValue(), Block.UPDATE_ALL);
        replaced.clear();
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
