package net.hfstack.rallyguard.debug.sim;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * E2A block 3 - the scenario matrix, defined in DATA ({@code docs/SIMULACIONES.md}): the
 * default {@code /rallyguard_sim/scenarios.json} in the mod jar is copied to
 * {@code config/rallyguard-sim-scenarios.json} on first use, and that file is what runs -
 * David (or Claude Code) adds a scenario by editing JSON, no code.
 */
public record SimScenario(String id, String terrain, int distance, int minutes, String position,
                          List<String> tags, Map<String, Double> expect, Side blue, Side red) {

    /**
     * One side. {@code kind}: "guard" or a mob id ("zombie", "minecraft:skeleton"...);
     * {@code role}: melee / archer / gunner / mixed (also the weapon a mob is handed);
     * {@code leader}: one extra guard of rank max(rank + 2, 4) leads the side.
     */
    public record Side(String kind, int count, String role, int rank, boolean leader, String formation,
                       boolean mounted, String fireMode) {
        public boolean guards() {
            return "guard".equals(kind);
        }
    }

    public record Preset(String name, List<String> scenarios, int seeds, int minutes) {
    }

    /** A cross-scenario expectation: {@code metric} of scenario {@code a} {@code op} (">", ">=", "<=") that of {@code b}. */
    public record Check(String a, String op, String b, String metric) {
    }

    public record Matrix(Map<String, SimScenario> scenarios, Map<String, Preset> presets, List<Check> checks) {
    }

    public boolean mirror() {
        return tags.contains("mirror");
    }

    private static final String RESOURCE = "/rallyguard_sim/scenarios.json";

    /** Reads the matrix from {@code file}, creating it from the jar's default first if missing. */
    public static Matrix load(Path file) throws IOException {
        if (!Files.exists(file)) {
            try (InputStream in = SimScenario.class.getResourceAsStream(RESOURCE)) {
                if (in == null) throw new IOException("missing " + RESOURCE + " in the mod jar");
                Files.createDirectories(file.getParent());
                Files.write(file, in.readAllBytes());
            }
        }
        JsonObject root = JsonParser.parseString(Files.readString(file, StandardCharsets.UTF_8)).getAsJsonObject();
        Map<String, SimScenario> scenarios = new LinkedHashMap<>();
        for (JsonElement e : root.getAsJsonArray("scenarios")) {
            SimScenario s = parse(e.getAsJsonObject());
            scenarios.put(s.id().toUpperCase(Locale.ROOT), s);
        }
        Map<String, Preset> presets = new LinkedHashMap<>();
        JsonObject p = root.has("presets") ? root.getAsJsonObject("presets") : new JsonObject();
        for (Map.Entry<String, JsonElement> e : p.entrySet()) {
            JsonObject o = e.getValue().getAsJsonObject();
            List<String> ids = new ArrayList<>();
            for (JsonElement id : o.getAsJsonArray("scenarios")) ids.add(id.getAsString().toUpperCase(Locale.ROOT));
            presets.put(e.getKey().toLowerCase(Locale.ROOT), new Preset(e.getKey(), ids,
                    integer(o, "seeds", 1), integer(o, "minutes", 2)));
        }
        List<Check> checks = new ArrayList<>();
        if (root.has("checks")) {
            for (JsonElement e : root.getAsJsonArray("checks")) {
                JsonObject o = e.getAsJsonObject();
                checks.add(new Check(o.get("a").getAsString().toUpperCase(Locale.ROOT), o.get("op").getAsString(),
                        o.get("b").getAsString().toUpperCase(Locale.ROOT), o.get("metric").getAsString()));
            }
        }
        return new Matrix(scenarios, presets, checks);
    }

    private static SimScenario parse(JsonObject o) {
        List<String> tags = new ArrayList<>();
        if (o.has("tags")) for (JsonElement t : o.getAsJsonArray("tags")) tags.add(t.getAsString());
        Map<String, Double> expect = new LinkedHashMap<>();
        if (o.has("expect")) {
            for (Map.Entry<String, JsonElement> e : o.getAsJsonObject("expect").entrySet()) {
                expect.put(e.getKey(), e.getValue().getAsDouble());
            }
        }
        Side blue = side(o.getAsJsonObject("blue"), null);
        Side red = side(o.getAsJsonObject("red"), blue);
        return new SimScenario(o.get("id").getAsString(), string(o, "terrain", "flat"), integer(o, "distance", 20),
                integer(o, "minutes", 0), string(o, "position", "front"), tags, expect, blue, red);
    }

    /** A side; any field the red side leaves out is copied from blue (mirror by default). */
    private static Side side(JsonObject o, Side like) {
        if (o == null) o = new JsonObject();
        return new Side(
                string(o, "kind", like == null ? "guard" : like.kind()),
                integer(o, "count", like == null ? 5 : like.count()),
                string(o, "role", like == null ? "melee" : like.role()),
                integer(o, "rank", like == null ? 0 : like.rank()),
                bool(o, "leader", like != null && like.leader()),
                string(o, "formation", like == null ? "LINE" : like.formation()),
                bool(o, "mounted", like != null && like.mounted()),
                string(o, "fireMode", like == null ? "NORMAL" : like.fireMode()));
    }

    private static String string(JsonObject o, String k, String d) {
        return o.has(k) ? o.get(k).getAsString() : d;
    }

    private static int integer(JsonObject o, String k, int d) {
        return o.has(k) ? o.get(k).getAsInt() : d;
    }

    private static boolean bool(JsonObject o, String k, boolean d) {
        return o.has(k) ? o.get(k).getAsBoolean() : d;
    }
}
