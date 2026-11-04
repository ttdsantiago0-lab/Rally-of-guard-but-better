package net.hfstack.rallyguard.debug;

import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.slf4j.Logger;

import java.util.UUID;

/**
 * Headless simulation for CI (GitHub Actions): when the JVM is started with
 * {@code -Drallyguard.sim.autorun=<preset>}, a dedicated server runs that sim batch as soon as it has
 * started, commanded by a fake player at the world spawn, and shuts itself down when the batch ends.
 * Without the property this class does nothing. Results land in {@code rally-sim/} of the server's
 * working directory, exactly as in a normal game.
 */
public final class SimAutorun {
    private static final Logger LOG = LogUtils.getLogger();
    public static final String PROP = "rallyguard.sim.autorun";
    /** Fixed id so the CI commander owns the same guards in every run. */
    private static final UUID CI_COMMANDER = UUID.fromString("5a11e000-0000-4000-8000-00000000c1c1");
    private static boolean active;

    public static boolean active() {
        return active;
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        String preset = System.getProperty(PROP, "").trim();
        if (preset.isEmpty()) return;
        MinecraftServer server = event.getServer();
        ServerLevel level = server.overworld();
        FakePlayer commander = FakePlayerFactory.get(level, new GameProfile(CI_COMMANDER, "RallySimCI"));
        BlockPos spawn = level.getSharedSpawnPos();
        commander.moveTo(spawn.getX() + 0.5D, spawn.getY(), spawn.getZ() + 0.5D, 0.0F, 0.0F);
        active = true;
        String speed = System.getProperty("rallyguard.sim.speed", "").trim();
        Component refused = SimRunner.startBatch(commander, preset, null, 1L, 0, 0, speed);
        if (refused != null) {
            LOG.error("[rallyguard] sim autorun '{}' refused: {}", preset, refused.getString());
            active = false;
            server.halt(false);
        } else {
            LOG.info("[rallyguard] sim autorun '{}' started", preset);
        }
    }

    /** Called by {@link SimRunner} once a batch has written its summary. */
    static void batchFinished(MinecraftServer server) {
        if (!active) return;
        active = false;
        LOG.info("[rallyguard] sim autorun finished - stopping the server");
        server.halt(false);
    }
}
