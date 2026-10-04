package net.hfstack.rallyguard.order;

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
 * An HQ whose banner is gone (checked only when its chunk is loaded - an unloaded HQ is
 * trusted) is dropped and the commander told: a raided HQ is lost until a new banner is
 * claimed.
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
        String dimension = level.dimension().location().toString();
        Map<SquadRegistry.Squad, List<Guard>> toHq = new LinkedHashMap<>();
        List<Guard> followers = new ArrayList<>();
        for (Guard guard : guards) {
            SquadRegistry.Squad squad = squads.squadOf(guard.getUUID()).orElse(null);
            if (squad != null && squad.hq != null && dimension.equals(squad.hqDimension) && hqStands(player, squads, squad)) {
                toHq.computeIfAbsent(squad, k -> new ArrayList<>()).add(guard);
            } else {
                followers.add(guard);
            }
        }

        for (Map.Entry<SquadRegistry.Squad, List<Guard>> e : toHq.entrySet()) {
            BlockPos hq = e.getKey().hq;
            List<Guard> group = e.getValue();
            // Formed up next to the banner, facing back the way they came.
            Vec3 center = Vec3.atBottomCenterOf(hq);
            Vec3 from = group.get(0).position();
            float facing = (float) Math.toDegrees(Math.atan2(-(from.x - center.x), from.z - center.z));
            new RetreatOrder(center, facing).execute(player, group);
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

    /** False (and the HQ cleared, commander told) if the HQ's banner is known to be gone. */
    private static boolean hqStands(ServerPlayer player, SquadRegistry squads, SquadRegistry.Squad squad) {
        ServerLevel level = player.serverLevel();
        if (!level.isLoaded(squad.hq)) return true;
        if (level.getBlockState(squad.hq).getBlock() instanceof AbstractBannerBlock) return true;
        squads.setHq(squad.owner, squad.name, "", null);
        player.sendSystemMessage(Component.translatable("gui.rallyguard.command.hq_lost", squad.name));
        return false;
    }
}
