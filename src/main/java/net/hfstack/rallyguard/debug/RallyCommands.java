package net.hfstack.rallyguard.debug;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.hfstack.rallyguard.contract.SquadRegistry;
import net.hfstack.rallyguard.order.CommanderHotkeys;
import net.hfstack.rallyguard.order.GuardModes;
import net.hfstack.rallyguard.order.GuardPosts;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import tallestegg.guardvillagers.GuardEntityType;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;
import java.util.Locale;

/**
 * E1-E / E1-F admin commands (permission level 2):
 * <ul>
 *     <li>{@code /rally debug spawn <n> [squad]} - n guards (max 2000) owned by you, posted
 *     in a grid in front of you, optionally put in a squad;</li>
 *     <li>{@code /rally debug perf} - Rally's own ms/tick per system since the last call
 *     ({@link Perf});</li>
 *     <li>{@code /rally debug rank <n>} - test rank for the guards in command (no effect on
 *     behavior until E2);</li>
 *     <li>{@code /rally sim <A|B|C|D|ALL> [seed] [repetitions]} - the baseline simulation
 *     ({@link SimRunner}).</li>
 * </ul>
 */
public final class RallyCommands {

    @SubscribeEvent
    public void onRegister(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    private static void register(CommandDispatcher<CommandSourceStack> d) {
        d.register(Commands.literal("rally").requires(s -> s.hasPermission(2))
                .then(Commands.literal("debug")
                        .then(Commands.literal("spawn")
                                .then(Commands.argument("n", IntegerArgumentType.integer(1, 2000))
                                        .executes(c -> spawn(c, ""))
                                        .then(Commands.argument("squad", StringArgumentType.string())
                                                .executes(c -> spawn(c, StringArgumentType.getString(c, "squad"))))))
                        .then(Commands.literal("perf").executes(RallyCommands::perf))
                        .then(Commands.literal("rank")
                                .then(Commands.argument("n", IntegerArgumentType.integer(0, 10)).executes(RallyCommands::rank))))
                .then(Commands.literal("sim")
                        .then(Commands.argument("scenario", StringArgumentType.word())
                                .executes(c -> sim(c, 1L, 3))
                                .then(Commands.argument("seed", LongArgumentType.longArg())
                                        .executes(c -> sim(c, LongArgumentType.getLong(c, "seed"), 3))
                                        .then(Commands.argument("repetitions", IntegerArgumentType.integer(1, 20))
                                                .executes(c -> sim(c, LongArgumentType.getLong(c, "seed"),
                                                        IntegerArgumentType.getInteger(c, "repetitions"))))))));
    }

    private static int spawn(CommandContext<CommandSourceStack> c, String squad) throws CommandSyntaxException {
        ServerPlayer player = c.getSource().getPlayerOrException();
        ServerLevel level = player.serverLevel();
        int n = IntegerArgumentType.getInteger(c, "n");
        int side = (int) Math.ceil(Math.sqrt(n));
        int made = 0;
        for (int i = 0; i < n; i++) {
            Vec3 spot = player.position().add((i % side - side / 2.0) * 2.0, 0, 4 + (i / side) * 2.0);
            BlockPos pos = GuardPosts.standableNear(level, spot);
            Guard g = GuardEntityType.GUARD.get().spawn(level, pos, MobSpawnType.COMMAND);
            if (g == null) continue;
            g.setOwnerId(player.getUUID());
            g.setPersistenceRequired();
            GuardModes.postHere(g);
            if (!squad.isEmpty()) SquadRegistry.get(level).assign(g.getUUID(), player.getUUID(), squad);
            made++;
        }
        int count = made;
        c.getSource().sendSuccess(() -> Component.translatable("gui.rallyguard.debug.spawned", count), true);
        return made;
    }

    private static int perf(CommandContext<CommandSourceStack> c) {
        for (String line : Perf.reportAndReset()) {
            c.getSource().sendSuccess(() -> Component.literal(line), false);
        }
        return 1;
    }

    private static int rank(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
        ServerPlayer player = c.getSource().getPlayerOrException();
        int n = IntegerArgumentType.getInteger(c, "n");
        List<Guard> guards = CommanderHotkeys.scope(player);
        for (Guard g : guards) DebugRanks.set(g.getUUID(), n);
        c.getSource().sendSuccess(() -> Component.translatable("gui.rallyguard.debug.ranked", guards.size(), n), false);
        return guards.size();
    }

    private static int sim(CommandContext<CommandSourceStack> c, long seed, int reps) throws CommandSyntaxException {
        ServerPlayer player = c.getSource().getPlayerOrException();
        String s = StringArgumentType.getString(c, "scenario").toUpperCase(Locale.ROOT);
        List<String> scenarios = switch (s) {
            case "A", "B", "C", "D" -> List.of(s);
            case "ALL" -> List.of("A", "B", "C", "D");
            default -> null;
        };
        if (scenarios == null) {
            c.getSource().sendFailure(Component.translatable("gui.rallyguard.sim.bad_scenario"));
            return 0;
        }
        if (SimRunner.running()) {
            c.getSource().sendFailure(Component.translatable("gui.rallyguard.sim.busy"));
            return 0;
        }
        SimRunner.start(player, scenarios, seed, reps);
        return 1;
    }
}
