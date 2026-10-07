package net.hfstack.rallyguard.compat;

import com.mojang.logging.LogUtils;
import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.event.InteractGuardHandler;
import net.hfstack.rallyguard.order.GuardModes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import tallestegg.guardvillagers.common.entities.Guard;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * E1 ARREGLO 2 - MCA: Mob Compat ({@code dev.otectus.mcamobcompat}, 0.2.1) bridges Guard
 * Villagers' guards into MCA through a hidden MCA villager ("GuardProxy") whose movement
 * and state it copies onto the guard every tick - writing the guard's Following /
 * Patrolling flags and owner directly. Facts from reading its jar (David, 2026-10-05).
 * <p>
 * David's decision: the guard STAYS an MCA character (name, personality, hearts, history),
 * but Rally owns hiring, movement and ownership. This bridge:
 * <ul>
 *     <li>listens to Mob Compat's own cancelable {@code api.event.GuardInteractEvent}
 *     (registered by reflection - no compile-time dependency; if the class is missing,
 *     nothing happens);</li>
 *     <li><b>unowned guard</b>: cancels it and opens Rally's hiring screen instead;</li>
 *     <li><b>owned guard, not the owner</b>: cancels it - nobody else can talk to or give
 *     MCA orders to your guard;</li>
 *     <li><b>the owner</b>: MCA's menu opens normally; the interaction is noted so
 *     {@link GuardModes} adopts a follow/stay chosen there as the owner's order for ~60 s,
 *     and reverts any other change (and any owner change) made behind Rally's back -
 *     checked in the stance loop, spread over 20 ticks.</li>
 * </ul>
 * E2A B9 (FEEDBACK_E1 B9, "MCA's menu still doesn't open on my own guard"): Rally's side
 * already lets the OWNER's interaction through, both here and in
 * {@link InteractGuardHandler}. Per {@code docs/API_MODS.md}, Mob Compat only opens MCA's
 * menu when its own config {@code guardMcaInteraction = true}; with it false the event is
 * never even posted, so nothing Rally does can open it. The log line at start-up reminds
 * of that key. Not verifiable from here: whether Guard Villagers' own owner GUI (normal
 * right-click in {@code Guard.mobInteract}) runs before Mob Compat's hook - if David still
 * sees GV's screen with the key on, that ordering is the cause (open question).
 * <p>
 * Not done (see the E1 report): a "Talk" button on the hiring screen (Mob Compat's
 * {@code GuardInteractions} methods' signatures aren't known without its jar), and hiring
 * from inside MCA's menu.
 */
public final class McaMobCompat {
    private McaMobCompat() {
    }

    private static final String EVENT_CLASS = "dev.otectus.mcamobcompat.api.event.GuardInteractEvent";
    private static final Logger LOGGER = LogUtils.getLogger();

    private static boolean active;
    private static Method getPlayer, getGuard, getHand, setResult;

    /** True when Mob Compat is installed and the bridge is listening. */
    public static boolean active() {
        return active && RallyConfig.on(RallyConfig.MCA_GUARD);
    }

    @SuppressWarnings("unchecked")
    public static void registerIfLoaded() {
        try {
            Class<?> eventClass = Class.forName(EVENT_CLASS);
            if (!Event.class.isAssignableFrom(eventClass)) return;
            getPlayer = eventClass.getMethod("getPlayer");
            getGuard = eventClass.getMethod("getGuard");
            getHand = eventClass.getMethod("getHand");
            setResult = eventClass.getMethod("setCancellationResult", InteractionResult.class);
            NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, true, (Class<Event>) eventClass, McaMobCompat::onGuardInteract);
            active = true;
            LOGGER.info("[rallyguard] MCA Mob Compat found: Rally keeps hiring, movement and ownership of guards. "
                    + "MCA's menu on your own guard needs guardMcaInteraction=true in Mob Compat's config");
        } catch (ClassNotFoundException e) {
            // Mob Compat isn't installed - nothing to do.
        } catch (ReflectiveOperationException | RuntimeException | LinkageError e) {
            LOGGER.warn("[rallyguard] MCA Mob Compat is present but its GuardInteractEvent API didn't match ({}); "
                    + "the bridge is off", e.toString());
        }
    }

    private static void onGuardInteract(Event event) {
        if (!RallyConfig.on(RallyConfig.MCA_GUARD)) return;
        try {
            if (!(getPlayer.invoke(event) instanceof Player player) || !(getGuard.invoke(event) instanceof Entity entity)) return;
            if (!(entity instanceof Guard guard)) return;
            UUID owner = GuardOwnership.getOwner(guard);
            if (owner == null) {
                cancel(event);
                if (getHand.invoke(event) == InteractionHand.MAIN_HAND && player instanceof ServerPlayer sp) {
                    InteractGuardHandler.openHire(sp, guard);
                }
            } else if (!owner.equals(player.getUUID())) {
                cancel(event);
            } else if (!player.level().isClientSide()) {
                GuardModes.noteOwnerInteraction(guard);
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            LOGGER.warn("[rallyguard] MCA Mob Compat bridge skipped an interaction ({})", e.toString());
        }
    }

    private static void cancel(Event event) throws ReflectiveOperationException {
        if (event instanceof ICancellableEvent cancellable) cancellable.setCanceled(true);
        setResult.invoke(event, InteractionResult.SUCCESS);
    }
}
