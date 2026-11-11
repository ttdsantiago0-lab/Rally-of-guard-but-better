package net.hfstack.rallyguard.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import net.hfstack.rallyguard.combat.FireMode;
import net.hfstack.rallyguard.formation.FormationLayout;
import net.hfstack.rallyguard.formation.FormationShape;
import net.hfstack.rallyguard.tactics.Doctrine;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

/**
 * E3B G3 ({@code docs/GRUPOS_Y_SUBGRUPOS.md} §4) - the same general instruction means different
 * things to different specialties. {@link #resolve} reads the instruction table
 * ({@code config/rallyguard-instructions.json}, written with the defaults below if missing, read
 * once per server start) for the subgroup's {@link GroupProfile.Family} and returns a
 * {@link Behavior}: doctrine (ladder weights), fire style, shape and density, a distance band,
 * a cycle, a tempo and a maneuver {@link Mode}.
 * <p>
 * The interpreter only COMPOSES what exists - {@link Doctrine}, {@link FireMode},
 * {@link FormationShape} + {@link FormationLayout}, unit directives ({@code force/UnitOrders}
 * kinds HOLD / MOVE / FALLBACK / ATTACK_AREA / RETREAT_HQ) - carried out by
 * {@link InstructionRunner}; no parallel system. A new instruction = a new row in the table
 * (and an enum constant); a heterogeneous group reads the column of its DOMINANT archetype and
 * says so in its warnings.
 */
public final class InstructionInterpreter {
    private InstructionInterpreter() {
    }

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String FILE = "rallyguard-instructions.json";

    /** General instructions (lang: gui.rallyguard.instr.*). */
    public enum Instruction { HOSTIGAR, MANTENER_LINEA, AVANZAR, RETIRARSE, FLANQUEAR }

    /**
     * What the unit does with the band and the cycle:
     * <ul>
     *     <li>KEEP_DISTANCE - stay between {@code near}×reach and {@code far}×reach of the enemy
     *     (fall back when closer, close in when farther, hold and shoot in between);</li>
     *     <li>THREATEN_FLANK - stand {@code offset} blocks off the enemy's side, shields up, no charge;</li>
     *     <li>HIT_AND_RUN - attack the enemy's side for {@code cycle} s, fall back {@code offset} for {@code cycle} s;</li>
     *     <li>HOLD - hold the group's anchor ({@code offset} ahead, negative = behind);</li>
     *     <li>RESERVE - hold {@code offset} behind the anchor; attack if the enemy comes within {@code counter};</li>
     *     <li>ADVANCE - the group anchor advances {@code offset} every {@code cycle} s; contact within {@code counter} = attack;</li>
     *     <li>ADVANCE_TURNS - like ADVANCE but halting to fire every other cycle;</li>
     *     <li>FLANK_ADVANCE - advance level with the anchor, {@code offset} out on a flank, at reach;</li>
     *     <li>RETREAT_STAGGERED - fall back {@code offset} every {@code cycle} s shooting, then to the HQ;</li>
     *     <li>RETREAT_FAST - straight to the HQ;</li>
     *     <li>RETREAT_LAST - hold {@code cycle} s facing the enemy, then staggered, then the HQ;</li>
     *     <li>COVER_FLANKS - hold {@code offset} out on both flanks for {@code cycle} s, then the HQ;</li>
     *     <li>FLANK_POSITION - take a lateral spot at {@code far}×reach from the enemy and shoot;</li>
     *     <li>ENCIRCLE - circle the enemy at {@code far}×reach, 30° every {@code cycle} s, shooting;</li>
     *     <li>FIX_FRONT - hold {@code offset} blocks in front of the enemy, facing it;</li>
     *     <li>FLANK_CHARGE - go round to the enemy's rear, then charge.</li>
     *     <li>RETREAT_FIGHTING (2026-11-11) - fighting withdrawal: never stops fighting; holds and fires for {@code cycle} s,
     *     falls back {@code offset} blocks still firing, again and again; with the enemy closer than {@code near}×reach it
     *     falls back at once; with the enemy out past {@code far}×reach it keeps going (contact broken).</li>
     * </ul>
     */
    public enum Mode {
        KEEP_DISTANCE, THREATEN_FLANK, HIT_AND_RUN, HOLD, RESERVE, ADVANCE, ADVANCE_TURNS, FLANK_ADVANCE,
        RETREAT_STAGGERED, RETREAT_FAST, RETREAT_LAST, COVER_FLANKS, FLANK_POSITION, ENCIRCLE, FIX_FRONT, FLANK_CHARGE,
        RETREAT_FIGHTING
    }

    /**
     * One cell of the table. {@code near}/{@code far} are fractions of the subgroup's mean
     * weapon reach; {@code offset} and {@code counter} are blocks; {@code cycle} seconds;
     * {@code tempo} multiplies the navigation speed requested (never the entity's speed).
     */
    public record Behavior(Mode mode, Doctrine doctrine, FireMode fire, FormationShape shape,
                           FormationLayout.Density density, double near, double far, double offset, double counter,
                           int cycle, double tempo, boolean engage) {
    }

    @Nullable
    private static Map<Instruction, Map<GroupProfile.Family, Behavior>> table;

    /** The behavior of {@code instruction} for a subgroup with this profile. */
    public static Behavior resolve(Instruction instruction, GroupProfile.Profile profile) {
        return resolve(instruction, profile.family());
    }

    public static Behavior resolve(Instruction instruction, GroupProfile.Family family) {
        if (simOldRetreat && instruction == Instruction.RETIRARSE) return oldRetreat().get(family);
        return table().get(instruction).get(family);
    }

    /** Simulation only (tag {@code retreat:old}): the withdrawal before 2026-11-11, to measure against. */
    public static boolean simOldRetreat;

    /** Before 2026-11-11: shields and riders stopped fighting while they withdrew (round 39: worse than fighting). */
    static Map<GroupProfile.Family, Behavior> oldRetreat() {
        FormationLayout.Density open = FormationLayout.Density.OPEN, normal = FormationLayout.Density.NORMAL,
                closed = FormationLayout.Density.CLOSED;
        Map<GroupProfile.Family, Behavior> retreat = new EnumMap<>(GroupProfile.Family.class);
        retreat.put(GroupProfile.Family.RANGED, b(Mode.RETREAT_STAGGERED, Doctrine.AVOID_CASUALTIES, FireMode.RETREATING,
                FormationShape.LINE, open, 0, 0, 8, 0, 6, 1.0));
        retreat.put(GroupProfile.Family.RANGED_CAVALRY, b(Mode.RETREAT_FAST, Doctrine.AVOID_CASUALTIES, FireMode.RETREATING,
                FormationShape.COLUMN, normal, 0, 0, 0, 0, 0, 1.3));
        retreat.put(GroupProfile.Family.INFANTRY, b(Mode.RETREAT_LAST, Doctrine.HOLD_POSITION, FireMode.NORMAL,
                FormationShape.LINE, closed, 0, 0, 8, 0, 10, 1.0));
        retreat.put(GroupProfile.Family.CAVALRY, b(Mode.COVER_FLANKS, Doctrine.STANDARD, FireMode.NORMAL, FormationShape.COLUMN,
                normal, 0, 0, 10, 0, 12, 1.1));
        return retreat;
    }

    @Nullable
    public static Instruction parse(String s) {
        try {
            return Instruction.valueOf(s.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    // ==== The table ====

    static Map<Instruction, Map<GroupProfile.Family, Behavior>> table() {
        Map<Instruction, Map<GroupProfile.Family, Behavior>> t = table;
        if (t == null) {
            t = load();
            table = t;
        }
        return t;
    }

    /** Server stop: the next start reads the file again. */
    public static void reset() {
        table = null;
    }

    /**
     * {@code engage} = the engagement rule: true = the guards take targets as their stance allows;
     * false = they only answer whoever hits them (a harassing shield line that must not charge,
     * a withdrawal) - applied through the stance gate, never a new stance.
     */
    private static Behavior b(Mode m, Doctrine d, FireMode f, FormationShape s, FormationLayout.Density den, double near,
                              double far, double offset, double counter, int cycle, double tempo) {
        boolean engage = m != Mode.THREATEN_FLANK && m != Mode.RETREAT_FAST && m != Mode.RETREAT_LAST && m != Mode.COVER_FLANKS; // RETREAT_FIGHTING engages
        return new Behavior(m, d, f, s, den, near, far, offset, counter, cycle, tempo, engage);
    }

    /** The draft table of the design doc (numbers are a first draft; the file is the place to tune them). */
    static Map<Instruction, Map<GroupProfile.Family, Behavior>> defaults() {
        Map<Instruction, Map<GroupProfile.Family, Behavior>> t = new EnumMap<>(Instruction.class);
        FormationLayout.Density open = FormationLayout.Density.ABIERTA, closed = FormationLayout.Density.CERRADA,
                normal = FormationLayout.Density.NORMAL;
        Map<GroupProfile.Family, Behavior> harass = new EnumMap<>(GroupProfile.Family.class);
        harass.put(GroupProfile.Family.RANGED, b(Mode.KEEP_DISTANCE, Doctrine.HARASS, FireMode.VOLLEY, FormationShape.LINE, open,
                0.75, 0.95, 0, 0, 6, 1.0));
        harass.put(GroupProfile.Family.RANGED_CAVALRY, b(Mode.KEEP_DISTANCE, Doctrine.HARASS, FireMode.RETREATING,
                FormationShape.LINE, open, 0.55, 0.8, 0, 0, 4, 1.25));
        harass.put(GroupProfile.Family.INFANTRY, b(Mode.THREATEN_FLANK, Doctrine.HOLD_POSITION, FireMode.NORMAL,
                FormationShape.LINE, closed, 0, 0, 7, 0, 6, 0.9));
        harass.put(GroupProfile.Family.CAVALRY, b(Mode.HIT_AND_RUN, Doctrine.ALL_OUT, FireMode.NORMAL, FormationShape.V,
                normal, 0, 0, 20, 0, 6, 1.3));
        t.put(Instruction.HOSTIGAR, harass);

        Map<GroupProfile.Family, Behavior> hold = new EnumMap<>(GroupProfile.Family.class);
        hold.put(GroupProfile.Family.RANGED, b(Mode.HOLD, Doctrine.HOLD_POSITION, FireMode.STILL, FormationShape.LINE, normal,
                0, 0, -6, 0, 0, 1.0));
        hold.put(GroupProfile.Family.RANGED_CAVALRY, b(Mode.RESERVE, Doctrine.STANDARD, FireMode.STILL, FormationShape.LINE,
                normal, 0, 0, 14, 0, 0, 1.0));
        hold.put(GroupProfile.Family.INFANTRY, b(Mode.HOLD, Doctrine.HOLD_POSITION, FireMode.NORMAL, FormationShape.LINE, closed,
                0, 0, 0, 0, 0, 1.0));
        hold.put(GroupProfile.Family.CAVALRY, b(Mode.RESERVE, Doctrine.STANDARD, FireMode.NORMAL, FormationShape.V, normal,
                0, 0, 12, 10, 0, 1.0));
        t.put(Instruction.MANTENER_LINEA, hold);

        Map<GroupProfile.Family, Behavior> advance = new EnumMap<>(GroupProfile.Family.class);
        advance.put(GroupProfile.Family.RANGED, b(Mode.ADVANCE_TURNS, Doctrine.STANDARD, FireMode.ADVANCING, FormationShape.LINE,
                normal, 0, 0.8, -5, 0, 6, 1.0));
        advance.put(GroupProfile.Family.RANGED_CAVALRY, b(Mode.FLANK_ADVANCE, Doctrine.HARASS, FireMode.ADVANCING,
                FormationShape.COLUMN, open, 0, 0.9, 12, 0, 6, 1.1));
        advance.put(GroupProfile.Family.INFANTRY, b(Mode.ADVANCE, Doctrine.STANDARD, FireMode.NORMAL, FormationShape.LINE, closed,
                0, 0, 6, 6, 8, 0.9));
        advance.put(GroupProfile.Family.CAVALRY, b(Mode.ADVANCE, Doctrine.ALL_OUT, FireMode.NORMAL, FormationShape.V, normal,
                0, 0, -8, 12, 8, 1.0));
        t.put(Instruction.AVANZAR, advance);

        // 2026-11-11 (David: "una retirada exitosa es importante, con tanta supervivencia como sea posible"): round 39
        // measured the old withdrawal worse than fighting in every case (adult dragon: 81 % alive fighting, 15 %
        // withdrawing) - shields and riders stopped fighting and were bitten in the back. Now every specialty fights
        // while it falls back (RETREAT_FIGHTING); riders take longer bounds.
        Map<GroupProfile.Family, Behavior> retreat = new EnumMap<>(GroupProfile.Family.class);
        retreat.put(GroupProfile.Family.RANGED, b(Mode.RETREAT_FIGHTING, Doctrine.STANDARD, FireMode.STILL,
                FormationShape.LINE, normal, 0.3, 1.2, 6, 0, 4, 1.0));
        retreat.put(GroupProfile.Family.RANGED_CAVALRY, b(Mode.RETREAT_FIGHTING, Doctrine.STANDARD, FireMode.RETREATING,
                FormationShape.COLUMN, normal, 0.3, 1.2, 12, 0, 3, 1.3));
        retreat.put(GroupProfile.Family.INFANTRY, b(Mode.RETREAT_FIGHTING, Doctrine.STANDARD, FireMode.STILL,
                FormationShape.LINE, closed, 0.3, 1.2, 5, 0, 4, 1.0));
        retreat.put(GroupProfile.Family.CAVALRY, b(Mode.RETREAT_FIGHTING, Doctrine.STANDARD, FireMode.NORMAL, FormationShape.COLUMN,
                normal, 0.3, 1.2, 12, 0, 3, 1.3));
        t.put(Instruction.RETIRARSE, retreat);

        Map<GroupProfile.Family, Behavior> flank = new EnumMap<>(GroupProfile.Family.class);
        flank.put(GroupProfile.Family.RANGED, b(Mode.FLANK_POSITION, Doctrine.STANDARD, FireMode.STILL, FormationShape.LINE, open,
                0, 0.7, 0, 0, 0, 1.0));
        flank.put(GroupProfile.Family.RANGED_CAVALRY, b(Mode.ENCIRCLE, Doctrine.HARASS, FireMode.ADVANCING, FormationShape.COLUMN,
                open, 0, 0.8, 0, 0, 4, 1.2));
        flank.put(GroupProfile.Family.INFANTRY, b(Mode.FIX_FRONT, Doctrine.HOLD_POSITION, FireMode.NORMAL, FormationShape.LINE,
                closed, 0, 0, 6, 0, 0, 1.0));
        flank.put(GroupProfile.Family.CAVALRY, b(Mode.FLANK_CHARGE, Doctrine.ALL_OUT, FireMode.NORMAL, FormationShape.V, normal,
                0, 0, 10, 6, 15, 1.3));
        t.put(Instruction.FLANQUEAR, flank);
        return t;
    }

    /** Reads the file (writing the defaults first if it is missing); any broken cell keeps its default. */
    static Map<Instruction, Map<GroupProfile.Family, Behavior>> load() {
        Map<Instruction, Map<GroupProfile.Family, Behavior>> t = defaults();
        Path file;
        try {
            file = FMLPaths.CONFIGDIR.get().resolve(FILE);
        } catch (RuntimeException e) {
            return t; // no config dir (tests): defaults
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            if (!Files.exists(file)) {
                Files.writeString(file, gson.toJson(toJson(t)), StandardCharsets.UTF_8);
                return t;
            }
            JsonObject root = JsonParser.parseString(Files.readString(file, StandardCharsets.UTF_8)).getAsJsonObject();
            boolean migrate = !root.has("_retreatV2"); // 2026-11-11: files from before the fighting withdrawal
            for (Instruction i : Instruction.values()) {
                if (!root.has(i.name())) continue;
                if (migrate && i == Instruction.RETIRARSE) continue;
                JsonObject row = root.getAsJsonObject(i.name());
                for (GroupProfile.Family f : GroupProfile.Family.values()) {
                    if (!row.has(f.name())) continue;
                    try {
                        t.get(i).put(f, cell(row.getAsJsonObject(f.name()), t.get(i).get(f)));
                    } catch (RuntimeException e) {
                        LOGGER.warn("[rallyguard] {}: {}.{} unreadable, default kept ({})", FILE, i, f, e.toString());
                    }
                }
            }
            if (migrate) {
                Files.writeString(file, gson.toJson(toJson(t)), StandardCharsets.UTF_8);
                LOGGER.info("[rallyguard] {}: RETIRARSE updated to the fighting withdrawal (other cells kept)", FILE);
            }
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("[rallyguard] {} unreadable, defaults used ({})", FILE, e.toString());
        }
        return t;
    }

    private static Behavior cell(JsonObject o, Behavior d) {
        return new Behavior(
                o.has("mode") ? Mode.valueOf(o.get("mode").getAsString().toUpperCase(Locale.ROOT)) : d.mode(),
                o.has("doctrine") ? Doctrine.parse(o.get("doctrine").getAsString()) : d.doctrine(),
                o.has("fire") ? FireMode.parse(o.get("fire").getAsString()) : d.fire(),
                o.has("shape") ? FormationShape.valueOf(o.get("shape").getAsString().toUpperCase(Locale.ROOT)) : d.shape(),
                o.has("density") ? FormationLayout.Density.valueOf(o.get("density").getAsString().toUpperCase(Locale.ROOT)) : d.density(),
                num(o, "near", d.near()), num(o, "far", d.far()), num(o, "offset", d.offset()), num(o, "counter", d.counter()),
                (int) Math.max(0, Math.min(60, num(o, "cycle", d.cycle()))), Math.max(0.5D, Math.min(1.6D, num(o, "tempo", d.tempo()))),
                o.has("engage") ? o.get("engage").getAsBoolean() : d.engage());
    }

    private static double num(JsonObject o, String k, double d) {
        JsonElement e = o.get(k);
        return e == null || e.isJsonNull() ? d : e.getAsDouble();
    }

    static JsonObject toJson(Map<Instruction, Map<GroupProfile.Family, Behavior>> t) {
        JsonObject root = new JsonObject();
        root.addProperty("_doc", "E3B G3: same instruction, different meaning per specialty (docs/GRUPOS_Y_SUBGRUPOS.md §4). "
                + "Columns: RANGED (archers, crossbows, guns), RANGED_CAVALRY, INFANTRY (line, heavy, spears), CAVALRY. "
                + "near/far = fractions of the subgroup's weapon reach; offset/counter = blocks; cycle = seconds; "
                + "tempo multiplies the navigation speed requested (0.5-1.6); engage=false: only answer whoever hits them. "
                + "Read once per server start.");
        root.addProperty("_retreatV2", true);
        for (Map.Entry<Instruction, Map<GroupProfile.Family, Behavior>> row : t.entrySet()) {
            JsonObject r = new JsonObject();
            for (Map.Entry<GroupProfile.Family, Behavior> c : row.getValue().entrySet()) {
                Behavior v = c.getValue();
                JsonObject o = new JsonObject();
                o.addProperty("mode", v.mode().name());
                o.addProperty("doctrine", v.doctrine().name());
                o.addProperty("fire", v.fire().name());
                o.addProperty("shape", v.shape().name());
                o.addProperty("density", v.density().name());
                o.addProperty("near", v.near());
                o.addProperty("far", v.far());
                o.addProperty("offset", v.offset());
                o.addProperty("counter", v.counter());
                o.addProperty("cycle", v.cycle());
                o.addProperty("tempo", v.tempo());
                o.addProperty("engage", v.engage());
                r.add(c.getKey().name(), o);
            }
            root.add(row.getKey().name(), r);
        }
        return root;
    }
}
