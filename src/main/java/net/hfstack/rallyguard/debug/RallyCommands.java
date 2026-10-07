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
 *     <li>{@code /rally debug rank <n>} - test rank for the guards in command (the higher of
 *     it and the earned rank applies);</li>
 *     <li>{@code /rally debug vet} - rank, XP, traits and history of the guards in command;</li>
 *     <li>{@code /rally sim batch <preset|all> [--minutes N] [--seeds N] [--tickrate sprint|normal]},
 *     {@code /rally sim stop}, and {@code /rally sim <A|B|C|D|ALL|scenario> [seed] [repetitions]}
 *     ({@link SimRunner}, E2A simulation v2).</li>
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
                        .then(Commands.literal("vet").executes(RallyCommands::vet))
                        .then(Commands.literal("rank")
                                .then(Commands.argument("n", IntegerArgumentType.integer(0, 10)).executes(RallyCommands::rank))))
                .then(Commands.literal("sim")
                        .then(Commands.literal("batch")
                                .then(Commands.argument("preset", StringArgumentType.word())
                                        .executes(c -> batch(c, ""))
                                        .then(Commands.argument("options", StringArgumentType.greedyString())
                                                .executes(c -> batch(c, StringArgumentType.getString(c, "options"))))))
                        .then(Commands.literal("stop").executes(c -> {
                            SimRunner.stop("stopped by command");
                            return 1;
                        }))
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
            if (GuardPosts.isWet(level, pos)) continue; // E2A B1: never spawn guards into water
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

    /** E2A: rank, XP, traits and history of the guards in command (selection, else followers). */
    private static int vet(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
        ServerPlayer player = c.getSource().getPlayerOrException();
        List<Guard> guards = CommanderHotkeys.scope(player);
        int shown = 0;
        for (Guard g : guards) {
            if (shown++ >= 20) break;
            net.hfstack.rallyguard.contract.VeterancyRegistry.Record r = net.hfstack.rallyguard.veterancy.Veterancy.record(g);
            int rank = net.hfstack.rallyguard.veterancy.Veterancy.rank(g);
            String line = r == null
                    ? String.format(Locale.ROOT, "%s: rank %d, no combat record", g.getName().getString(), rank)
                    : String.format(Locale.ROOT, "%s: rank %d, %.0f xp, traits %s, kills m/r/g %d/%d/%d, near death %d, battles %d, totems %d, enemy types %d, genes %s",
                    g.getName().getString(), rank, r.xp, r.traits, r.killsMelee, r.killsRanged, r.killsGun,
                    r.nearDeath, r.battles, r.totems, r.enemyTypes.size(),
                    net.hfstack.rallyguard.veterancy.Affinity.of(g.getUUID()));
            c.getSource().sendSuccess(() -> Component.literal(line), false);
        }
        return guards.size();
    }

    /** E1 aliases (A, B, C, D, ALL) and any scenario id of the matrix, run as a small batch. */
    private static int sim(CommandContext<CommandSourceStack> c, long seed, int reps) throws CommandSyntaxException {
        ServerPlayer player = c.getSource().getPlayerOrException();
        String s = StringArgumentType.getString(c, "scenario");
        Component error = SimRunner.startBatch(player, "", List.of(s), seed, reps, 0, null);
        if (error != null) {
            c.getSource().sendFailure(error);
            return 0;
        }
        return 1;
    }

    /** {@code /rally sim batch <preset|all> [--minutes N] [--seeds N] [--tickrate sprint|normal]}. */
    private static int batch(CommandContext<CommandSourceStack> c, String options) throws CommandSyntaxException {
        ServerPlayer player = c.getSource().getPlayerOrException();
        String preset = StringArgumentType.getString(c, "preset");
        int minutes = 0, seeds = 0;
        String speed = null;
        String[] t = options.trim().isEmpty() ? new String[0] : options.trim().split("\\s+");
        for (int i = 0; i < t.length; i++) {
            String key = t[i].toLowerCase(Locale.ROOT);
            String val = i + 1 < t.length ? t[i + 1] : "";
            try {
                switch (key) {
                    case "--minutes" -> { minutes = Math.max(1, Math.min(60, Integer.parseInt(val))); i++; }
                    case "--seeds" -> { seeds = Math.max(1, Math.min(50, Integer.parseInt(val))); i++; }
                    case "--tickrate" -> {
                        if (!val.equalsIgnoreCase("sprint") && !val.equalsIgnoreCase("normal")) throw new NumberFormatException(val);
                        speed = val.toLowerCase(Locale.ROOT);
                        i++;
                    }
                    default -> throw new NumberFormatException(key);
                }
            } catch (NumberFormatException e) {
                c.getSource().sendFailure(Component.translatable("gui.rallyguard.sim.bad_option", t[i]));
                return 0;
            }
        }
        Component error = SimRunner.startBatch(player, preset, null, 1L, seeds, minutes, speed);
        if (error != null) {
            c.getSource().sendFailure(error);
            return 0;
        }
        return 1;
    }
}
