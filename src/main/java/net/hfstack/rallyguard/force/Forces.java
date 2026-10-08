package net.hfstack.rallyguard.force;

import net.hfstack.rallyguard.tactics.Doctrine;
import net.minecraft.server.level.ServerLevel;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;

/** E3A A1 - forces (filled in by block A1). */
public final class Forces {
    private Forces() {
    }

    /** The doctrine of the force the guard belongs to, or null if it is in none. */
    @Nullable
    public static Doctrine doctrineOf(ServerLevel level, Guard guard) {
        return null;
    }
}
