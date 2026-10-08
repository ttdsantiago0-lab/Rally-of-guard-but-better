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
        Map<String, Integer> noContact = new LinkedHashMap<>();
        for (SimRun r : all) {
            if (r.control) controls.add(r);
            else if (r.stuck) noContact.merge(r.scenario, 1, Integer::sum); // E3A F7: own finding, not a draw
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

        List<String> findings = new ArrayList<>();
        for (Map.Entry<String, Integer> e : noContact.entrySet()) {
            findings.add(String.format(Locale.ROOT, "SIN ENFRENTAMIENTO: %s - %d corrida(s) sin daño entre bandos "
                    + "(excluidas de las tasas; revisar hostilidad/objetivos)", e.getKey(), e.getValue()));
        }
        findings.addAll(findings(by, controls, matrix));
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
        md.add("| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |");
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
                    stat(rs, "cohesion").fmt("%.1f"), noContact.getOrDefault(e.getKey(), 0)));
        }
        boolean anyFormation = false;
        for (Map.Entry<String, List<SimRun>> e : by.entrySet()) {
            Stat f = stat(e.getValue(), "formationKept");
            if (f.n() == 0) continue;
            if (!anyFormation) {
                md.add("");
                md.add("## Formaciones (guardias en su hueco a los 30 s)");
                anyFormation = true;
            }
            md.add(String.format(Locale.ROOT, "- %s: %s", e.getKey(), pct(f)));
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
        md.add("## Jerarquía de órdenes");
        String rulesTest = net.hfstack.rallyguard.force.ForceRules.selfTest();
        md.add("- " + rulesTest);
        if (!rulesTest.contains("✓")) findings.add("Reglas de fuerza: alternan alrededor del umbral");
        for (Map.Entry<String, List<SimRun>> e : by.entrySet()) {
            Stat p = stat(e.getValue(), "precedenceOk");
            if (p.n() == 0) continue;
            md.add(String.format(Locale.ROOT, "- %s: precedencia correcta en %s de las corridas", e.getKey(), pct(p)));
            if (p.mean() < 1.0D) findings.add(e.getKey() + ": la orden del jugador no siempre prevalece sobre la directiva de fuerza");
        }
        excellence(md, findings, by, matrix);
        pace(md, by, matrix);
        reasons(md, all);
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

    /** E3A V9: the seven excellence metrics; true = higher is better. */
    static final String[] EXCELLENCE = {"blueCasualties", "survivalSeconds", "cohesion", "pursuit", "efficiency",
            "orderSeconds", "friendlyFire"};
    static final boolean[] HIGHER_BETTER = {false, true, false, false, true, false, false};

    /** Weapon efficiency: hits per shot for archers, damage per second for melee. */
    static double metricFor(List<SimRun> rs, String metric, boolean archer) {
        if (metric.equals("efficiency")) return stat(rs, archer ? "blueAccuracy" : "damagePerSecond").mean();
        return stat(rs, metric).mean();
    }

    /**
     * E3A V9 - "Techo de excelencia" ({@code docs/ARQUITECTURA_VETERANIA.md} §7): per family
     * (tag {@code family:x}, ranks from {@code rank:n}) R5 vs R0 on the 7 metrics (≥ 6 better),
     * a composite index per rank that must not go down from R0 to R5, the captain comparison
     * ({@code captain:high/low}), the outnumbered run (may lose, must retreat) and the duel
     * alarm (R5 winning > 85 % 1 v 1 = possible stat inflation).
     */
    private static void excellence(List<String> md, List<String> findings, Map<String, List<SimRun>> by,
                                   SimScenario.Matrix matrix) {
        Map<String, Map<Integer, List<SimRun>>> families = new LinkedHashMap<>();
        List<SimRun> capHigh = new ArrayList<>(), capLow = new ArrayList<>(), outnumbered = new ArrayList<>(), duel = new ArrayList<>();
        for (Map.Entry<String, List<SimRun>> e : by.entrySet()) {
            SimScenario s = matrix.scenarios().get(e.getKey());
            if (s == null || !s.tags().contains("ceiling")) continue;
            String fam = s.tag("family"), rank = s.tag("rank");
            if (!fam.isEmpty() && !rank.isEmpty()) {
                families.computeIfAbsent(fam, k -> new java.util.TreeMap<>()).put(Integer.parseInt(rank), e.getValue());
            }
            if (s.tag("captain").equals("high")) capHigh.addAll(e.getValue());
            if (s.tag("captain").equals("low")) capLow.addAll(e.getValue());
            if (s.tags().contains("outnumbered")) outnumbered.addAll(e.getValue());
            if (s.tags().contains("duel")) duel.addAll(e.getValue());
        }
        if (families.isEmpty() && capHigh.isEmpty() && duel.isEmpty()) return;
        md.add("");
        md.add("## Techo de excelencia (mismo equipo; vida, daño y velocidad no cambian con el rango)");
        for (Map.Entry<String, Map<Integer, List<SimRun>>> f : families.entrySet()) {
            boolean archer = f.getKey().contains("archer");
            Map<Integer, List<SimRun>> ranks = f.getValue();
            List<SimRun> r0 = ranks.get(0), r5 = ranks.get(5);
            if (r0 != null && r5 != null) {
                int better = 0;
                StringBuilder detail = new StringBuilder();
                for (int i = 0; i < EXCELLENCE.length; i++) {
                    double a = metricFor(r0, EXCELLENCE[i], archer), b = metricFor(r5, EXCELLENCE[i], archer);
                    boolean ok = !Double.isNaN(a) && !Double.isNaN(b) && (HIGHER_BETTER[i] ? b > a : b < a);
                    if (ok) better++;
                    detail.append(String.format(Locale.ROOT, " %s %s%.2f→%.2f", ok ? "✓" : "✗", EXCELLENCE[i] + " ", a, b));
                }
                md.add(String.format(Locale.ROOT, "- %s: R5 mejora a R0 en %d/7 (n=%d/%d):%s", f.getKey(), better,
                        r0.size(), r5.size(), detail));
                if (better < 6) findings.add(String.format(Locale.ROOT, "Techo %s: R5 mejora a R0 solo en %d/7 métricas "
                        + "(criterio ≥ 6; ajustar configuración, no el criterio)", f.getKey(), better));
                if (r0.size() < 10 || r5.size() < 10) findings.add("Techo " + f.getKey() + ": menos de 10 repeticiones por rango");
            }
            // Composite index: per metric, the rank's mean scaled 0..1 between worst and best rank, averaged.
            StringBuilder idx = new StringBuilder();
            double prev = -1;
            boolean monotonic = true;
            for (Map.Entry<Integer, List<SimRun>> r : ranks.entrySet()) {
                double sum = 0;
                int n = 0;
                for (int i = 0; i < EXCELLENCE.length; i++) {
                    double lo = Double.MAX_VALUE, hi = -Double.MAX_VALUE;
                    for (List<SimRun> rr : ranks.values()) {
                        double v = metricFor(rr, EXCELLENCE[i], archer);
                        if (Double.isNaN(v)) continue;
                        lo = Math.min(lo, v);
                        hi = Math.max(hi, v);
                    }
                    double v = metricFor(r.getValue(), EXCELLENCE[i], archer);
                    if (Double.isNaN(v) || hi <= lo) continue;
                    double norm = (v - lo) / (hi - lo);
                    sum += HIGHER_BETTER[i] ? norm : 1.0D - norm;
                    n++;
                }
                double index = n == 0 ? 0 : sum / n;
                idx.append(String.format(Locale.ROOT, " R%d %.2f", r.getKey(), index));
                if (prev >= 0 && index < prev - 1.0E-9) monotonic = false;
                prev = index;
            }
            md.add("  - índice compuesto:" + idx + (monotonic ? " (sube sin retroceder)" : " (RETROCEDE)"));
            if (!monotonic) findings.add("Techo " + f.getKey() + ": el índice compuesto no sube de forma monótona R0→R5");
        }
        if (!capHigh.isEmpty() && !capLow.isEmpty()) {
            double ch = stat(capHigh, "cohesion").mean(), cl = stat(capLow, "cohesion").mean();
            double bh = stat(capHigh, "blueCasualties").mean(), bl = stat(capLow, "blueCasualties").mean();
            md.add(String.format(Locale.ROOT, "- Capitán alto vs bajo: cohesión %.1f vs %.1f, bajas %.1f vs %.1f", ch, cl, bh, bl));
            if (!(ch < cl && bh < bl)) findings.add("Capitán de rango alto no supera al bajo en cohesión y bajas");
        }
        if (!outnumbered.isEmpty()) {
            Stat win = stat(outnumbered, "blueWin"), ret = stat(outnumbered, "retreats");
            md.add(String.format(Locale.ROOT, "- Desventaja ≥ 3 a 1 (R5): victoria %s, retiradas %.1f por corrida", pct(win), ret.mean()));
            if (ret.mean() <= 0) findings.add("Desventaja 3 a 1: la unidad R5 no se retira en orden");
        }
        if (!duel.isEmpty()) {
            Stat win = stat(duel, "blueWin");
            md.add(String.format(Locale.ROOT, "- Alarma 1 contra 1 (R5 vs R0): R5 gana %s", pct(win)));
            if (win.mean() > 0.85D) findings.add("POSIBLE INFLADO DE ESTADÍSTICAS: R5 gana > 85 % en 1 contra 1");
        }
    }

    /** E3A V8 - "Ritmo de veteranía": XP per guard per combat minute in pace scenarios -> time to each rank. */
    private static void pace(List<String> md, Map<String, List<SimRun>> by, SimScenario.Matrix matrix) {
        List<SimRun> runs = new ArrayList<>();
        for (Map.Entry<String, List<SimRun>> e : by.entrySet()) {
            SimScenario s = matrix.scenarios().get(e.getKey());
            if (s != null && s.pace()) runs.addAll(e.getValue());
        }
        if (runs.isEmpty()) return;
        Stat rate = stat(runs, "xpPerGuardMinute");
        int perDay = net.hfstack.rallyguard.config.RallyConfig.i(net.hfstack.rallyguard.config.RallyConfig.COMBAT_MINUTES_PER_DAY, 10);
        md.add("");
        md.add(String.format(Locale.ROOT, "## Ritmo de veteranía (%.1f ± %.1f XP por guardia y minuto de combate; %d min de combate = 1 día)",
                rate.mean(), rate.se(), perDay));
        if (rate.mean() <= 0) {
            md.add("- Sin XP medida.");
            return;
        }
        StringBuilder line = new StringBuilder("-");
        for (int r = 1; r <= 5; r++) {
            double minutes = net.hfstack.rallyguard.veterancy.Veterancy.xpFor(r) / rate.mean();
            line.append(String.format(Locale.ROOT, " R%d ≈ %.0f min (%.1f días)", r, minutes, minutes / perDay));
        }
        md.add(line.toString());
        md.add("- Objetivo: R2 ≈ 1–2 días, R3 ≈ 1 semana, R4 ≈ varias semanas, R5 raro (subestima: sin decaimiento por tipo acumulado)");
    }

    /** E3A V11 - decision reasons counted over the batch. */
    private static void reasons(List<String> md, List<SimRun> all) {
        Map<String, Long> total = new java.util.TreeMap<>();
        for (SimRun r : all) for (Map.Entry<String, Long> e : r.reasons.entrySet()) total.merge(e.getKey(), e.getValue(), Long::sum);
        if (total.isEmpty()) return;
        md.add("");
        StringBuilder line = new StringBuilder("## Motivos de decisión (cambios registrados): ");
        boolean first = true;
        for (Map.Entry<String, Long> e : total.entrySet()) {
            if (!first) line.append(", ");
            line.append(e.getKey()).append(' ').append(e.getValue());
            first = false;
        }
        md.add(line.toString());
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
            long timeouts = rs.stream().filter(r -> r.timeout).count();
            if (timeouts * 2 > rs.size()) out.add(id + ": " + timeouts + "/" + rs.size() + " corridas llegaron al tope de tiempo");
            long drowned = rs.stream().mapToLong(r -> r.drowned[0] + r.drowned[1]).sum();
            if (drowned > 0) out.add(id + ": " + drowned + " ahogado(s) en una arena sin agua - revisar");
            Stat secs = stat(rs, "seconds");
            if (secs.n() > 0 && secs.mean() < 3) out.add(id + ": se resuelve en < 3 s (¿aparición mal colocada?)");
            Stat kept = stat(rs, "formationKept");
            if (kept.n() > 0 && kept.mean() < 0.8D) {
                out.add(String.format(Locale.ROOT, "%s: solo %.0f %% de los guardias en su hueco a los 30 s", id, kept.mean() * 100));
            }
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
