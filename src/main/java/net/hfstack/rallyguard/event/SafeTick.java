package net.hfstack.rallyguard.event;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Phase 3G robustness rule: Rally's periodic per-guard work must never be the thing that
 * crashes a server. Every loop over guards runs each guard's step through {@link #run}:
 * if something throws - typically another mod's entity or item behaving unexpectedly
 * (a modded mount, target, weapon or explosive) - that one guard is skipped for this
 * tick and the problem is logged ONCE per exception type, instead of taking the whole
 * level tick (and the world) down with it.
 */
public final class SafeTick {
    private SafeTick() {
    }

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Set<String> REPORTED = new HashSet<>();

    public static void run(Entity guard, Runnable step) {
        try {
            step.run();
        } catch (RuntimeException e) {
            String key = e.getClass().getName() + "@" + (e.getStackTrace().length > 0 ? e.getStackTrace()[0] : "?");
            if (REPORTED.add(key)) {
                LOGGER.warn("[rallyguard] Skipped a guard update after an error (reported once): guard={} ({})",
                        guard.getUUID(), guard.getType(), e);
            }
        }
    }
}
