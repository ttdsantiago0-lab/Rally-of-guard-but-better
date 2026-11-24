package net.hfstack.rallyguard.contract;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * E3A F1 - who is on whose side, decided by Rally (not by Guard Villagers' config).
 * <p>
 * A guard's FACTION is its owner's UUID (the simulation's two sides are two owner UUIDs,
 * so they are factions too); an unowned village guard has no faction. Two guards are
 * enemy factions when both are owned, by different owners, are not on allied scoreboard
 * teams, and their owners (when online) are not allied players. Guard Villagers' default
 * {@code Mob Blacklist} contains {@code guardvillagers:guard}, so without this two
 * players' armies could never fight; {@code GuardFactionMixin} lets enemy-faction guards
 * past that one list check - everything else in Guard Villagers' rules still applies.
 */
public final class Factions {
    private Factions() {
    }

    /**
     * F139 (sim only, tag {@code norally:red}): the red side is plain Guard Villagers - no owner, so no Rally handler
     * touches it - but it still needs a side, or GV's blacklist ({@code guardvillagers:guard}) keeps both sides from
     * ever hitting each other. The simulation writes the red owner's UUID under this persistent key; nothing else does.
     */
    public static final String SIM_FACTION_KEY = "rallyguard:simFaction";

    @Nullable
    public static UUID of(Entity e) {
        if (!(e instanceof Guard g)) return null;
        UUID owner = GuardOwnership.getOwner(g);
        if (owner == null && g.getPersistentData().hasUUID(SIM_FACTION_KEY)) return g.getPersistentData().getUUID(SIM_FACTION_KEY);
        return owner;
    }

    /** True when {@code a} and {@code b} are guards of two different, non-allied factions. */
    public static boolean enemies(Guard a, Entity b) {
        if (!(b instanceof Guard other) || other == a) return false;
        UUID fa = of(a), fb = of(other);
        if (fa == null || fb == null || fa.equals(fb)) return false;
        if (a.getTeam() != null && other.getTeam() != null && a.getTeam().isAlliedTo(other.getTeam())) return false;
        Player pa = a.level().getPlayerByUUID(fa), pb = a.level().getPlayerByUUID(fb);
        return pa == null || pb == null || !pa.isAlliedTo(pb);
    }
}
