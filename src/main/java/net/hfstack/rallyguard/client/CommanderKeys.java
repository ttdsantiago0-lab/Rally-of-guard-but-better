package net.hfstack.rallyguard.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.hfstack.rallyguard.RallyOfTheGuard;
import net.hfstack.rallyguard.network.payload.CommandKeyC2SPayload;
import net.hfstack.rallyguard.network.payload.OpenGuardCommandC2SPayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

/**
 * Phase 3H: the commander's hotkeys (rebindable in Controls, category "Rally of the
 * Guard"). The client only reports "key X pressed, sneaking or not"; the server decides
 * what it means from what the player is looking at ({@code order.CommanderHotkeys}).
 * <ul>
 *     <li><b>G - Command:</b> attack / defend / move / follow / select, by context.</li>
 *     <li><b>H - Fire:</b> hold fire / fire at will. Sneak + H: retreat.</li>
 *     <li><b>J - Ledger:</b> opens the Commander's Ledger without the item in hand.</li>
 * </ul>
 * Registration: {@code RegisterKeyMappingsEvent} is a mod-bus event (it implements
 * {@code IModBusEvent}). E1: the explicit {@code bus = MOD} was dropped - the build
 * warned it is deprecated, because FML routes each {@code @SubscribeEvent} to the right
 * bus by its event type (as this mod's {@code ModClient} already relies on). The key
 * polling runs on the game bus in {@link Ticker}. {@code KeyMapping(String, IKeyConflictContext,
 * InputConstants.Type, int, String)} and {@code KeyConflictContext.IN_GAME} verified in
 * NeoForge's 1.21.1 {@code KeyMapping.java.patch} / {@code KeyConflictContext.java}.
 */
@EventBusSubscriber(modid = RallyOfTheGuard.MOD_ID, value = Dist.CLIENT)
public final class CommanderKeys {
    private CommanderKeys() {
    }

    private static final String CATEGORY = "key.categories.rallyguard";

    public static final KeyMapping COMMAND = new KeyMapping("key.rallyguard.command",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, CATEGORY);
    public static final KeyMapping FIRE = new KeyMapping("key.rallyguard.fire",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, CATEGORY);
    public static final KeyMapping LEDGER = new KeyMapping("key.rallyguard.ledger",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_J, CATEGORY);

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(COMMAND);
        event.register(FIRE);
        event.register(LEDGER);
    }

    /** Game-bus half: turns key presses into payloads once per client tick. */
    @EventBusSubscriber(modid = RallyOfTheGuard.MOD_ID, value = Dist.CLIENT)
    public static final class Ticker {
        private Ticker() {
        }

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.getConnection() == null) return;
            boolean sneaking = mc.player.isShiftKeyDown();
            while (COMMAND.consumeClick()) {
                PacketDistributor.sendToServer(new CommandKeyC2SPayload("ORDER", sneaking));
            }
            while (FIRE.consumeClick()) {
                PacketDistributor.sendToServer(new CommandKeyC2SPayload("FIRE", sneaking));
            }
            while (LEDGER.consumeClick()) {
                PacketDistributor.sendToServer(new OpenGuardCommandC2SPayload());
            }
        }
    }
}
