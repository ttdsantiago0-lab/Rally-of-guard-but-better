package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.hfstack.rallyguard.contract.HqRegistry;
import net.hfstack.rallyguard.contract.SquadRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;

/**
 * Phase 3H.1: WHERE a retreat goes (user decision, 2026-10-04 - "deben tener sus
 * headquarters... 3 opciones"). For each guard, in this order:
 * <ol>
 *     <li><b>The temporary retreat point</b> - the commander's marked position (sneak +
 *     Command on a block, or sneak-click with the Ledger). It is the decision for THIS
 *     fight, so it outranks the permanent HQ while it is set.</li>
 *     <li><b>Its squad's headquarters</b> - the banner the commander claimed for that
 *     squad (sneak + Command on a banner). Different squads fall back to different HQs
 *     with one key press.</li>
 *     <li><b>Neither</b> - they disengage and FOLLOW the commander.</li>
 * </ol>
 * Since E1 an HQ is a record of its own ({@link HqRegistry}): permanent, shareable, and
 * it keeps its last position if its banner is broken.
 */
public final class RetreatPlanner {
    private RetreatPlanner() {
    }

    /** Retreats {@code guards}; returns a one-line summary for the commander. */
    public static Component retreat(ServerPlayer player, List<Guard> guards) {
        ServerLevel level = player.serverLevel();
        CommanderMarks.MarkedPosition mark = CommanderMarks.markedPosition(player.getUUID(), level);
        if (mark != null) {
            new RetreatOrder(Vec3.atBottomCenterOf(mark.pos()), mark.facingYaw()).execute(player, guards);
            return Component.translatable("gui.rallyguard.command.retreat_point", guards.size());
        }

        SquadRegistry squads = SquadRegistry.get(level);
        HqRegistry hqs = HqRegistry.get(level);
        String dimension = level.dimension().location().toString();
        Map<UUID, List<Guard>> toHq = new LinkedHashMap<>();
        List<Guard> followers = new ArrayList<>();
        for (Guard guard : guards) {
            SquadRegistry.Squad squad = squads.squadOf(guard.getUUID()).orElse(null);
            HqRegistry.Hq hq = squad == null ? null : hqOf(squads, hqs, squad);
            if (hq != null && dimension.equals(hq.dimension)) {
                toHq.computeIfAbsent(hq.id, k -> new ArrayList<>()).add(guard);
            } else {
                followers.add(guard);
            }
        }

        for (Map.Entry<UUID, List<Guard>> e : toHq.entrySet()) {
            HqRegistry.Hq hq = hqs.byId(e.getKey());
            List<Guard> group = e.getValue();
            // E2A B6: an HQ is usually a building with one door - formation slots there
            // landed inside walls. Each guard gets a spot REACHABLE from the HQ block
            // (flood fill: the room first, then out through the door), nearest first.
            List<BlockPos> spots = GuardPosts.reachableSpots(level, hq.pos, group.size());
            DirectiveRegistry directives = DirectiveRegistry.get(level);
            group.sort(java.util.Comparator.comparingDouble(g -> g.distanceToSqr(Vec3.atCenterOf(hq.pos))));
            for (int i = 0; i < group.size(); i++) {
                Guard g = group.get(i);
                directives.setTarget(g.getUUID(), null, "");
                CommandTargeting.disengage(g);
                BlockPos spot = i < spots.size() ? spots.get(i) : hq.pos;
                GuardModes.postAndMove(g, Vec3.atBottomCenterOf(spot), 1.0D);
            }
        }
        if (!followers.isEmpty()) {
            for (Guard guard : followers) CommandTargeting.disengage(guard);
            new FollowOrder().execute(player, followers);
        }

        int atHq = guards.size() - followers.size();
        if (atHq == 0) return Component.translatable("gui.rallyguard.command.retreat_follow", followers.size());
        if (followers.isEmpty()) return Component.translatable("gui.rallyguard.command.retreat_hq", atHq);
        return Component.translatable("gui.rallyguard.command.retreat_mixed", atHq, followers.size());
    }

    /**
     * E1-C: the squad's HQ record. A Phase 3H.1 squad that still has its HQ inline (a
     * position on the squad) is migrated here, once, into its own {@link HqRegistry}
     * record named after the squad. HQs keep their last position if the banner breaks
     * (E1 decision: an HQ is permanent until the commander deletes it).
     */
    @Nullable
    public static HqRegistry.Hq hqOf(SquadRegistry squads, HqRegistry hqs, SquadRegistry.Squad squad) {
        if (squad.hqId == null && squad.hq != null) {
            HqRegistry.Hq migrated = hqs.create(squad.owner, squad.name, squad.hqDimension, squad.hq);
            squads.setHqId(squad.owner, squad.name, migrated.id);
        }
        return hqs.byId(squad.hqId);
    }
}
