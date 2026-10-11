package net.hfstack.rallyguard.command;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.force.UnitOrders;
import net.hfstack.rallyguard.formation.FormationLayout;
import net.hfstack.rallyguard.network.payload.GroupCardS2CPayload;
import net.hfstack.rallyguard.order.CommanderSelection;
import net.hfstack.rallyguard.order.MusterOrder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * E3B G4 - the group card ({@code docs/GRUPOS_Y_SUBGRUPOS.md} §3 "Dónde se ve"): for the
 * commander's current selection of 2+ guards, its profile at a glance (identity, consistency,
 * second archetype, morale and cohesion, up to 3 warnings) plus what the card's own buttons set
 * (instruction, density, load). Sent after every selection change and every order; the Ledger
 * draws it above its bars and the planner shows the same card for a unit.
 */
public final class GroupCards {
    private GroupCards() {
    }

    /** The selected, loaded, owned guards of this commander. */
    public static List<Guard> selection(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        List<Guard> out = new ArrayList<>();
        for (UUID id : CommanderSelection.of(player.getUUID())) {
            if (level.getEntity(id) instanceof Guard g && g.isAlive() && GuardOwnership.isOwnedBy(g, player.getUUID())) out.add(g);
        }
        return out;
    }

    /** The card line for these guards ("" for fewer than 2). */
    public static String card(ServerLevel level, String cacheKey, List<Guard> guards) {
        if (guards.size() < 2) return "";
        GroupProfile.Profile p = GroupProfile.cached(cacheKey, guards, level.getGameTime());
        String unit = UnitOrders.unitKeyOf(level, guards.get(0));
        InstructionInterpreter.Instruction instr = unit == null ? null : InstructionRunner.instructionOf(unit);
        FormationLayout layout = MusterOrder.layoutFor(level, guards, null);
        return p.card() + "|" + (instr == null ? "" : instr.name()) + "|" + layout.density().name() + "|" + layout.load().name()
                + "|" + moraleState(level, guards) + "|" + moraleCause(level, guards);
    }

    /** Filled by the morale system (E3B M): the unit's morale state name, "" if unmeasured. */
    static String moraleState(ServerLevel level, List<Guard> guards) {
        return net.hfstack.rallyguard.morale.Morale.enabled() ? net.hfstack.rallyguard.morale.Morale.worst(guards).name() : "";
    }

    /** Filled by the morale system (E3B M): the main cause lang key, "" if none. */
    static String moraleCause(ServerLevel level, List<Guard> guards) {
        // The cause of the lowest morale in the group (what the commander should fix first).
        Guard worst = null;
        for (Guard g : guards) if (worst == null || net.hfstack.rallyguard.morale.Morale.value(g) < net.hfstack.rallyguard.morale.Morale.value(worst)) worst = g;
        return worst == null ? "" : net.hfstack.rallyguard.morale.Morale.cause(worst);
    }

    public static void send(ServerPlayer player) {
        String line = card(player.serverLevel(), "sel#" + player.getUUID(), selection(player));
        if (line.length() > GroupCardS2CPayload.MAX) line = line.substring(0, GroupCardS2CPayload.MAX);
        PacketDistributor.sendToPlayer(player, new GroupCardS2CPayload(line));
    }
}
