package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.command.InstructionInterpreter;
import net.hfstack.rallyguard.command.InstructionRunner;
import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.force.UnitOrders;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

/**
 * E3B G3 - a general instruction to the selected group(s) (order ids {@code INSTR_<NAME>},
 * {@code INSTR_NONE}; the group card's instruction button and the planner send them). The
 * group's anchor is the commander's marked point, else the group's centre; it faces the enemy
 * the group can see, else the commander's facing. Each subgroup then interprets it with its own
 * profile ({@link InstructionRunner}). A group of one guard gets no instruction (G0: individual
 * guards receive intentions, not group instructions).
 */
public final class InstructionOrder implements OrderExecutor {

    @Nullable
    private final InstructionInterpreter.Instruction instruction;

    public InstructionOrder(@Nullable InstructionInterpreter.Instruction instruction) {
        this.instruction = instruction;
    }

    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        UnitOrders.notePlayerOrder(guards); // ends whatever instruction they had (and counts as the player's word)
        if (instruction == null) {
            player.displayClientMessage(Component.translatable("gui.rallyguard.instr.stopped", guards.size()), true);
            return;
        }
        if (!RallyConfig.on(RallyConfig.INSTRUCTIONS_ENABLED)) {
            player.displayClientMessage(Component.translatable("gui.rallyguard.instr.disabled"), true);
            return;
        }
        if (guards.size() < 2) {
            player.displayClientMessage(Component.translatable("gui.rallyguard.instr.needs_group"), true);
            return;
        }
        ServerLevel level = player.serverLevel();
        Vec3 centre = InstructionRunner.centre(guards);
        CommanderMarks.MarkedPosition mark = CommanderMarks.markedPosition(player.getUUID(), level);
        Vec3 anchor = mark != null ? Vec3.atBottomCenterOf(mark.pos()) : centre;
        float facing = mark != null ? mark.facingYaw() : player.getYRot();
        Vec3 enemy = InstructionRunner.enemyCentre(level, guards.get(0), centre);
        if (enemy != null) facing = InstructionRunner.yaw(anchor, enemy);
        List<String> parts = InstructionRunner.start(level, player.getUUID(), guards, instruction, anchor, facing);
        player.displayClientMessage(Component.translatable("gui.rallyguard.instr.started",
                Component.translatable("gui.rallyguard.instr." + instruction.name().toLowerCase(Locale.ROOT)),
                guards.size(), String.join(", ", parts)), true);
    }

    /** INSTR_NAME / INSTR_NONE -> executor (null for an unknown name). */
    @Nullable
    public static OrderExecutor forId(String id) {
        String u = id.toUpperCase(Locale.ROOT);
        if (!u.startsWith("INSTR_")) return null;
        String name = u.substring("INSTR_".length());
        if (name.equals("NONE")) return new InstructionOrder(null);
        InstructionInterpreter.Instruction i = InstructionInterpreter.parse(name);
        return i == null ? null : new InstructionOrder(i);
    }
}
