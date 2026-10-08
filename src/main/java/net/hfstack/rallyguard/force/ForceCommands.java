package net.hfstack.rallyguard.force;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.hfstack.rallyguard.contract.ForceRegistry;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.contract.SquadRegistry;
import net.hfstack.rallyguard.order.CommanderHotkeys;
import net.hfstack.rallyguard.tactics.Doctrine;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * E3A A1 - managing forces without new Ledger buttons (the Battle Planner, A4, is their
 * screen). Any player, on their own squads:
 * <pre>
 *   /rallyforce create &lt;squad&gt;            the squad becomes a force (its units are its "Squad/Unit" groups)
 *   /rallyforce dissolve &lt;squad&gt;
 *   /rallyforce doctrine &lt;squad&gt; &lt;doctrine&gt;
 *   /rallyforce unit add &lt;squad&gt; &lt;unit&gt;    declares the unit and moves the guards in command into it
 *   /rallyforce unit remove &lt;squad&gt; &lt;unit&gt; its guards go back to the squad itself
 *   /rallyforce role &lt;squad&gt; &lt;unit&gt; &lt;role|auto&gt;  anvil, hammer, screen, support, flank, reserve
 *   /rallyforce info &lt;squad&gt;              general, units, captains, capabilities, roles
 * </pre>
 */
public final class ForceCommands {

    @SubscribeEvent
    public void onRegister(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    private static void register(CommandDispatcher<CommandSourceStack> d) {
        d.register(Commands.literal("rallyforce")
                .then(Commands.literal("create").then(squadArg().executes(c -> create(c, true))))
                .then(Commands.literal("dissolve").then(squadArg().executes(c -> create(c, false))))
                .then(Commands.literal("doctrine").then(squadArg()
                        .then(Commands.argument("doctrine", StringArgumentType.word()).executes(ForceCommands::doctrine))))
                .then(Commands.literal("unit")
                        .then(Commands.literal("add").then(squadArg()
                                .then(Commands.argument("unit", StringArgumentType.word()).executes(c -> unit(c, true)))))
                        .then(Commands.literal("remove").then(squadArg()
                                .then(Commands.argument("unit", StringArgumentType.word()).executes(c -> unit(c, false))))))
                .then(Commands.literal("role").then(squadArg()
                        .then(Commands.argument("unit", StringArgumentType.word())
                                .then(Commands.argument("role", StringArgumentType.word()).executes(ForceCommands::role)))))
                .then(Commands.literal("info").then(squadArg().executes(ForceCommands::info))));
    }

    private static com.mojang.brigadier.builder.RequiredArgumentBuilder<CommandSourceStack, String> squadArg() {
        return Commands.argument("squad", StringArgumentType.string());
    }

    private static Optional<SquadRegistry.Squad> squad(ServerPlayer p, String name) {
        for (SquadRegistry.Squad s : SquadRegistry.get(p.serverLevel()).squadsOwnedBy(p.getUUID())) {
            if (s.name.equalsIgnoreCase(name)) return Optional.of(s);
        }
        return Optional.empty();
    }

    private static int create(CommandContext<CommandSourceStack> c, boolean create) throws CommandSyntaxException {
        ServerPlayer p = c.getSource().getPlayerOrException();
        String name = StringArgumentType.getString(c, "squad");
        Optional<SquadRegistry.Squad> s = squad(p, name);
        if (s.isEmpty()) return fail(c, "gui.rallyguard.force.no_squad", name);
        ForceRegistry forces = ForceRegistry.get(p.serverLevel());
        if (create) forces.create(p.getUUID(), s.get().name);
        else if (!forces.dissolve(p.getUUID(), s.get().name)) return fail(c, "gui.rallyguard.force.not_force", name);
        Leadership.invalidate();
        ok(c, create ? "gui.rallyguard.force.created" : "gui.rallyguard.force.dissolved", s.get().name);
        return 1;
    }

    private static int doctrine(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
        ServerPlayer p = c.getSource().getPlayerOrException();
        ForceRegistry.Force f = force(p, StringArgumentType.getString(c, "squad"));
        if (f == null) return fail(c, "gui.rallyguard.force.not_force", StringArgumentType.getString(c, "squad"));
        Doctrine doc = Doctrine.parse(StringArgumentType.getString(c, "doctrine"));
        f.doctrine = doc.name();
        ForceRegistry.get(p.serverLevel()).changed();
        ok(c, "gui.rallyguard.force.doctrine", f.squad,
                Component.translatable("gui.rallyguard.doctrine." + doc.name().toLowerCase(Locale.ROOT)));
        return 1;
    }

    private static int unit(CommandContext<CommandSourceStack> c, boolean add) throws CommandSyntaxException {
        ServerPlayer p = c.getSource().getPlayerOrException();
        ForceRegistry.Force f = force(p, StringArgumentType.getString(c, "squad"));
        if (f == null) return fail(c, "gui.rallyguard.force.not_force", StringArgumentType.getString(c, "squad"));
        String unit = StringArgumentType.getString(c, "unit");
        SquadRegistry squads = SquadRegistry.get(p.serverLevel());
        if (add) {
            f.units.putIfAbsent(unit, "");
            int moved = 0;
            for (Guard g : CommanderHotkeys.scope(p)) {
                if (!GuardOwnership.isOwnedBy(g, p.getUUID())) continue;
                squads.assign(g.getUUID(), p.getUUID(), f.squad + SquadRegistry.PATH_SEPARATOR + unit);
                moved++;
            }
            ok(c, "gui.rallyguard.force.unit_added", unit, f.squad, moved);
        } else {
            f.units.keySet().removeIf(u -> u.equalsIgnoreCase(unit));
            for (java.util.UUID id : squads.membersOf(p.getUUID(), f.squad + SquadRegistry.PATH_SEPARATOR + unit)) {
                squads.assign(id, p.getUUID(), f.squad);
            }
            ok(c, "gui.rallyguard.force.unit_removed", unit, f.squad);
        }
        ForceRegistry.get(p.serverLevel()).changed();
        Leadership.invalidate();
        return 1;
    }

    private static int role(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
        ServerPlayer p = c.getSource().getPlayerOrException();
        ForceRegistry.Force f = force(p, StringArgumentType.getString(c, "squad"));
        if (f == null) return fail(c, "gui.rallyguard.force.not_force", StringArgumentType.getString(c, "squad"));
        String unit = StringArgumentType.getString(c, "unit");
        String role = StringArgumentType.getString(c, "role").toUpperCase(Locale.ROOT);
        if (!role.equals("AUTO") && !Roles.isRole(role)) return fail(c, "gui.rallyguard.force.bad_role", role);
        f.units.put(unit.equalsIgnoreCase(f.squad) ? "" : unit, role.equals("AUTO") ? "" : role);
        ForceRegistry.get(p.serverLevel()).changed();
        ok(c, "gui.rallyguard.force.role", unit, role);
        return 1;
    }

    private static int info(CommandContext<CommandSourceStack> c) throws CommandSyntaxException {
        ServerPlayer p = c.getSource().getPlayerOrException();
        ForceRegistry.Force f = force(p, StringArgumentType.getString(c, "squad"));
        if (f == null) return fail(c, "gui.rallyguard.force.not_force", StringArgumentType.getString(c, "squad"));
        ServerLevel level = p.serverLevel();
        Guard general = Forces.generalOf(level, f);
        c.getSource().sendSuccess(() -> Component.translatable("gui.rallyguard.force.info", f.squad,
                Component.translatable("gui.rallyguard.doctrine." + f.doctrine.toLowerCase(Locale.ROOT)),
                general == null ? "-" : general.getName().getString() + " (" + Leadership.ledgerScore(general) + ")")
                .withStyle(ChatFormatting.GOLD), false);
        List<Forces.UnitView> units = Forces.units(level, f);
        java.util.Map<String, String> roles = Roles.assign(level, f, units);
        for (Forces.UnitView u : units) {
            String captain = u.captain() == null ? "-" : u.captain().getName().getString() + " (" + Leadership.ledgerScore(u.captain()) + ")";
            c.getSource().sendSuccess(() -> Component.literal(String.format(Locale.ROOT, "  %s [%s] %d - %s - %s - %s",
                    u.path(), Forces.typeOf(u), u.members().size(), captain, roles.getOrDefault(u.path(), "-"),
                    u.capabilities())), false);
        }
        return 1;
    }

    private static ForceRegistry.Force force(ServerPlayer p, String name) {
        Optional<SquadRegistry.Squad> s = squad(p, name);
        return s.isEmpty() ? null : ForceRegistry.get(p.serverLevel()).find(p.getUUID(), s.get().name);
    }

    private static void ok(CommandContext<CommandSourceStack> c, String key, Object... args) {
        c.getSource().sendSuccess(() -> Component.translatable(key, args), false);
    }

    private static int fail(CommandContext<CommandSourceStack> c, String key, Object... args) {
        c.getSource().sendFailure(Component.translatable(key, args));
        return 0;
    }
}
