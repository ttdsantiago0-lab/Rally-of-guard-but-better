package net.hfstack.rallyguard.debug.sim;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * E2A block 3 - the batch's output in {@code rally-sim/<batch>/}: {@code runs.jsonl} (one
 * line per run, appended as each finishes, so a crash keeps what ran), {@code summary.json}
 * and a compact {@code summary.md} (≤ 150 lines, meant to be copied into the repo) with
 * mean ± standard error and win rates per scenario, the mirror (control) comparison, and
 * "Hallazgos": expectations not met and anomalies, detected automatically.
 */
public final class SimReport {
    private SimReport() {
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Gson LINE = new Gson();
    private static final int MAX_MD_LINES = 150;

    public static void appendRun(Path dir, SimRun run) throws IOException {
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("runs.jsonl"), LINE.toJson(run.toJson()) + "\n", StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    /** Mean and standard error of a metric over runs. */
    record Stat(double mean, double se, int n) {
        String fmt(String pattern) {
            return String.format(Locale.ROOT, pattern + " ± " + pattern, mean, se);
        }
    }

    static Stat stat(List<SimRun> runs, String metric) {
        int n = 0;
        double sum = 0, sq = 0;
        for (SimRun r : runs) {
            double v = r.metric(metric);
            if (Double.isNaN(v)) continue;
            n++;
            sum += v;
            sq += v * v;
        }
        if (n == 0) return new Stat(Double.NaN, 0, 0);
        double mean = sum / n;
        double var = n > 1 ? Math.max(0, (sq - n * mean * mean) / (n - 1)) : 0;
        return new Stat(mean, Math.sqrt(var / n), n);
    }

    /** Writes summary.json and summary.md; returns the findings (also in the .md). */
    public static List<String> writeSummary(Path dir, String batch, String preset, String tickrate,
                                            List<SimRun> all, SimScenario.Matrix matrix, List<String> batchNotes)
            throws IOException {
        Map<String, List<SimRun>> by = new LinkedHashMap<>();
        List<SimRun> controls = new ArrayList<>();
        for (SimRun r : all) {
            if (r.control) controls.add(r);
            else by.computeIfAbsent(r.scenario, k -> new ArrayList<>()).add(r);
        }
        String[] metrics = {"blueWin", "redWin", "blueSurvivors", "redSurvivors", "blueCasualties", "seconds",
                "blueDamage", "redDamage", "blueAccuracy", "meanEnemyDist", "cohesion", "retreats", "perfMs", "perfPeakMs"};

        JsonObject json = new JsonObject();
        json.addProperty("batch", batch);
        json.addProperty("preset", preset);
        json.addProperty("tickrate", tickrate);
        json.addProperty("runs", all.size());
        JsonObject scen = new JsonObject();
        for (Map.Entry<String, List<SimRun>> e : by.entrySet()) {
            JsonObject s = new JsonObject();
            s.addProperty("n", e.getValue().size());
            for (String m : metrics) {
                Stat st = stat(e.getValue(), m);
                JsonObject v = new JsonObject();
                v.addProperty("mean", SimRun.round(st.mean()));
                v.addProperty("se", SimRun.round(st.se()));
                s.add(m, v);
            }
            s.addProperty("stuck", e.getValue().stream().filter(r -> r.stuck).count());
            s.addProperty("timeouts", e.getValue().stream().filter(r -> r.timeout).count());
            scen.add(e.getKey(), s);
        }
        json.add("scenarios", scen);

        List<String> findings = findings(by, controls, matrix);
        JsonArray f = new JsonArray();
        findings.forEach(f::add);
        json.add("findings", f);
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("summary.json"), GSON.toJson(json), StandardCharsets.UTF_8);

        List<String> md = new ArrayList<>();
        md.add("# Simulación Rally - lote " + batch);
        md.add("");
        md.add(String.format(Locale.ROOT, "Preset `%s` · velocidad `%s` · %d corridas · %d escenarios.",
                preset, tickrate, all.size(), by.size()));
        for (String note : batchNotes) md.add("> " + note);
        md.add("");
        md.add("| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Atasc. |");
        md.add("|---|---|---|---|---|---|---|---|---|---|---|---|---|");
        int rowsLeft = 60;
        for (Map.Entry<String, List<SimRun>> e : by.entrySet()) {
            if (rowsLeft-- <= 0) {
                md.add("| … | (más escenarios en summary.json) |");
                break;
            }
            List<SimRun> rs = e.getValue();
            md.add(String.format(Locale.ROOT, "| %s | %d | %s | %s | %s | %s | %s | %s | %s | %s | %s | %s | %d |",
                    e.getKey(), rs.size(),
                    pct(stat(rs, "blueWin")), pct(stat(rs, "redWin")),
                    stat(rs, "blueSurvivors").fmt("%.1f"), stat(rs, "redSurvivors").fmt("%.1f"),
                    stat(rs, "seconds").fmt("%.0f"), stat(rs, "blueDamage").fmt("%.0f"), stat(rs, "redDamage").fmt("%.0f"),
                    pct(stat(rs, "blueAccuracy")), stat(rs, "meanEnemyDist").fmt("%.1f"),
                    stat(rs, "cohesion").fmt("%.1f"), rs.stream().filter(r -> r.stuck).count()));
        }
        md.add("");
        md.add("## Espejo (control: debe dar ≈50 %)");
        for (Map.Entry<String, List<SimRun>> e : by.entrySet()) {
            SimScenario s = matrix.scenarios().get(e.getKey());
            if (s == null || !s.mirror()) continue;
            Stat w = stat(e.getValue(), "blueWin");
            md.add(String.format(Locale.ROOT, "- %s: azul %s (n=%d)", e.getKey(), pct(w), w.n()));
        }
        if (!controls.isEmpty()) {
            md.add("");
            md.add("## Control de velocidad (misma semilla a velocidad normal)");
            for (SimRun c : controls) {
                SimRun twin = all.stream().filter(r -> !r.control && r.scenario.equals(c.scenario) && r.seed == c.seed)
                        .findFirst().orElse(null);
                md.add(String.format(Locale.ROOT, "- %s semilla %d: sprint → %s (%d/%d vivos), normal → %s (%d/%d vivos)",
                        c.scenario, c.seed, twin == null ? "?" : twin.winner, twin == null ? 0 : twin.survivors[0],
                        twin == null ? 0 : twin.survivors[1], c.winner, c.survivors[0], c.survivors[1]));
            }
        }
        md.add("");
        md.add("## Hallazgos");
        if (findings.isEmpty()) md.add("- Ninguna expectativa incumplida ni anomalía detectada.");
        for (String s : findings) md.add("- " + s);
        if (md.size() > MAX_MD_LINES) {
            md = new ArrayList<>(md.subList(0, MAX_MD_LINES - 1));
            md.add("- … (recortado a 150 líneas; ver summary.json)");
        }
        Files.writeString(dir.resolve("summary.md"), String.join("\n", md) + "\n", StandardCharsets.UTF_8);
        return findings;
    }

    private static String pct(Stat s) {
        return s.n() == 0 ? "-" : String.format(Locale.ROOT, "%.0f %% ± %.0f", s.mean() * 100.0D, s.se() * 100.0D);
    }

    private static List<String> findings(Map<String, List<SimRun>> by, List<SimRun> controls, SimScenario.Matrix matrix) {
        List<String> out = new ArrayList<>();
        for (Map.Entry<String, List<SimRun>> e : by.entrySet()) {
            String id = e.getKey();
            List<SimRun> rs = e.getValue();
            SimScenario s = matrix.scenarios().get(id);
            Stat win = stat(rs, "blueWin");
            if (s != null) {
                Double min = s.expect().get("blueWinMin"), max = s.expect().get("blueWinMax");
                if (min != null && win.mean() < min) {
                    out.add(String.format(Locale.ROOT, "%s: victoria azul %.0f %% < esperado %.0f %%%s", id,
                            win.mean() * 100, min * 100, win.n() < 3 ? " (n<3, poco fiable)" : ""));
                }
                if (max != null && win.mean() > max) {
                    out.add(String.format(Locale.ROOT, "%s: victoria azul %.0f %% > esperado máx. %.0f %%%s", id,
                            win.mean() * 100, max * 100, win.n() < 3 ? " (n<3, poco fiable)" : ""));
                }
                if (s.mirror() && win.n() >= 4 && Math.abs(win.mean() - 0.5D) > Math.max(0.2D, 2 * win.se())) {
                    out.add(String.format(Locale.ROOT, "%s (espejo): azul gana %.0f %% - posible sesgo de la simulación "
                            + "(orden de aparición, terreno, lado)", id, win.mean() * 100));
                }
            }
            long stuck = rs.stream().filter(r -> r.stuck).count();
            if (stuck > 0) out.add(id + ": " + stuck + " corrida(s) atascada(s) (sin daño durante el límite)");
            long timeouts = rs.stream().filter(r -> r.timeout).count();
            if (timeouts * 2 > rs.size()) out.add(id + ": " + timeouts + "/" + rs.size() + " corridas llegaron al tope de tiempo");
            long drowned = rs.stream().mapToLong(r -> r.drowned[0] + r.drowned[1]).sum();
            if (drowned > 0) out.add(id + ": " + drowned + " ahogado(s) en una arena sin agua - revisar");
            Stat secs = stat(rs, "seconds");
            if (secs.n() > 0 && secs.mean() < 3) out.add(id + ": se resuelve en < 3 s (¿aparición mal colocada?)");
            Stat peak = stat(rs, "perfPeakMs");
            if (peak.n() > 0 && peak.mean() > 20) {
                out.add(String.format(Locale.ROOT, "%s: pico de Rally %.0f ms en un tick (media de picos)", id, peak.mean()));
            }
            Stat perf = stat(rs, "perfMs");
            if (perf.n() > 0 && perf.mean() > 2) {
                out.add(String.format(Locale.ROOT, "%s: Rally cuesta %.2f ms/tick de media", id, perf.mean()));
            }
        }
        for (SimScenario.Check c : matrix.checks()) {
            List<SimRun> a = by.get(c.a()), b = by.get(c.b());
            if (a == null || b == null) continue;
            Stat sa = stat(a, c.metric()), sb = stat(b, c.metric());
            boolean ok = switch (c.op()) {
                case ">" -> sa.mean() > sb.mean();
                case ">=" -> sa.mean() >= sb.mean();
                case "<" -> sa.mean() < sb.mean();
                case "<=" -> sa.mean() <= sb.mean();
                default -> true;
            };
            if (!ok) {
                out.add(String.format(Locale.ROOT, "Esperado %s %s %s en %s: %.2f vs %.2f%s", c.a(), c.op(), c.b(),
                        c.metric(), sa.mean(), sb.mean(),
                        Math.abs(sa.mean() - sb.mean()) <= sa.se() + sb.se() ? " (dentro del ruido)" : ""));
            }
        }
        for (SimRun c : controls) {
            for (List<SimRun> rs : by.values()) {
                for (SimRun r : rs) {
                    if (r.scenario.equals(c.scenario) && r.seed == c.seed && !r.winner.equals(c.winner)) {
                        out.add(c.scenario + ": el ganador cambia entre sprint y velocidad normal con la misma semilla "
                                + "(la IA no es determinista; si se repite en varios lotes, revisar)");
                    }
                }
            }
        }
        return out;
    }
}
