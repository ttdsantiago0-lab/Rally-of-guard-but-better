package net.hfstack.rallyguard.debug.sim;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static net.hfstack.rallyguard.debug.sim.SimReport.stat;

/**
 * E3B block S - the E3B sections of {@code summary.md} (called by {@link SimReport}): group
 * profile, instructions (and, as the blocks land, morale, planner v2, scenarios, recon and the
 * global checks). Every section only appears when the batch ran scenarios with its tags.
 */
final class SimReportE3B {
    private SimReportE3B() {
    }

    static void write(List<String> md, List<String> findings, Map<String, List<SimRun>> by, SimScenario.Matrix matrix) {
        liveTactics(md, findings, by, matrix);
        profiles(md, findings, by, matrix);
        instructions(md, findings, by, matrix);
    }

    /**
     * E3B R1: the squad picture is alive. {@code picturelive} scenarios must consult TerrainSense
     * and take cover/positions; {@code corelive} ones must use the rank-5 core at least once and
     * never flee an even fight; every scenario reports rung changes per guard and minute (alarm > 8).
     */
    static void liveTactics(List<String> md, List<String> findings, Map<String, List<SimRun>> by, SimScenario.Matrix matrix) {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<String, List<SimRun>> e : by.entrySet()) {
            SimScenario s = matrix.scenarios().get(e.getKey());
            if (s == null) continue;
            List<SimRun> rs = e.getValue();
            SimReport.Stat churn = stat(rs, "rungChangesPerGuardMinute");
            if (churn.n() > 0 && churn.mean() > 8.0D) {
                findings.add(String.format(Locale.ROOT, "%s: %.1f cambios de peldaño por guardia y minuto (alarma > 8: posible ir y venir)",
                        e.getKey(), churn.mean()));
            }
            if (s.tags().contains("picturelive")) {
                double reads = stat(rs, "terrainReads").mean(), cover = stat(rs, "coverDecisions").mean();
                lines.add(String.format(Locale.ROOT, "- %s: TerrainSense consultado %.0f veces, cobertura/posición %.0f decisiones, "
                        + "cambios de peldaño %.1f/guardia·min %s", e.getKey(), reads, cover, churn.mean(), reads > 0 && cover > 0 ? "✓" : "✗"));
                if (!(reads > 0 && cover > 0)) findings.add(e.getKey() + ": la imagen de escuadrón no activa terreno/cobertura");
            }
            if (s.tags().contains("corelive")) {
                double core = stat(rs, "coreDecisions").mean(), fled = stat(rs, "outnumberedRetreats").mean();
                lines.add(String.format(Locale.ROOT, "- %s: núcleo rango 5 decidió %.0f veces, retiradas por inferioridad %.1f %s",
                        e.getKey(), core, fled, core >= 1 && fled == 0 ? "✓" : "✗"));
                if (core < 1) findings.add(e.getKey() + ": el núcleo de rango 5 no decidió nunca");
                if (fled > 0) findings.add(e.getKey() + ": el rango 5 huye de una pelea pareja");
            }
            if (s.tags().contains("stillcheck")) { // E3B R2
                double un = stat(rs, "unstuckDecisions").mean();
                lines.add(String.format(Locale.ROOT, "- %s: decisiones UNSTUCK de guardias quietos a propósito: %.1f %s",
                        e.getKey(), un, un == 0 ? "✓" : "✗"));
                if (un > 0) findings.add(e.getKey() + ": un guardia quieto a propósito (STILL/puesto) se marcó como atascado");
            }
            if (s.tags().contains("pingpong")) {
                lines.add(String.format(Locale.ROOT, "- %s: %.1f cambios de peldaño por guardia y minuto (máx. 12 = 2 cada 10 s) %s",
                        e.getKey(), churn.mean(), churn.mean() <= 12.0D ? "✓" : "✗"));
                if (churn.mean() > 12.0D) findings.add(e.getKey() + ": ida y vuelta entre peldaños en una pelea estable");
            }
        }
        if (lines.isEmpty()) return;
        md.add("");
        md.add("## Táctica viva (imagen de escuadrón, núcleo de rango 5, sin ida y vuelta)");
        md.addAll(lines);
    }

    /** groupprofile-*: consistency measured at spawn against the scenario's expect (consistencyMin / Max). */
    static void profiles(List<String> md, List<String> findings, Map<String, List<SimRun>> by, SimScenario.Matrix matrix) {
        boolean header = false;
        for (Map.Entry<String, List<SimRun>> e : by.entrySet()) {
            SimScenario s = matrix.scenarios().get(e.getKey());
            if (s == null || !s.tags().contains("profile")) continue;
            if (!header) {
                md.add("");
                md.add("## Perfil de grupo (consistencia 0-100 al aparecer)");
                header = true;
            }
            SimReport.Stat c = stat(e.getValue(), "consistency");
            Double min = s.expect().get("consistencyMin"), max = s.expect().get("consistencyMax");
            boolean ok = (min == null || c.mean() >= min) && (max == null || c.mean() <= max);
            md.add(String.format(Locale.ROOT, "- %s: consistencia %.0f ± %.0f (n=%d)%s%s %s", e.getKey(), c.mean(), c.se(), c.n(),
                    min == null ? "" : " · esperado ≥ " + min.intValue(), max == null ? "" : " · esperado ≤ " + max.intValue(),
                    ok ? "✓" : "✗"));
            if (!ok) findings.add(String.format(Locale.ROOT, "%s: consistencia %.0f fuera de lo esperado (revisar groups.profileWeights)",
                    e.getKey(), c.mean()));
        }
    }

    /**
     * instruction-*: one row per cell (mean range, mean closest distance, seconds in contact); within
     * one instruction every pair of cells must differ clearly in at least one of the three.
     */
    static void instructions(List<String> md, List<String> findings, Map<String, List<SimRun>> by, SimScenario.Matrix matrix) {
        Map<String, List<String>> byInstruction = new LinkedHashMap<>();
        for (Map.Entry<String, List<SimRun>> e : by.entrySet()) {
            SimScenario s = matrix.scenarios().get(e.getKey());
            if (s == null || s.tag("instruction").isEmpty()) continue;
            byInstruction.computeIfAbsent(s.tag("instruction").toUpperCase(Locale.ROOT), k -> new ArrayList<>()).add(e.getKey());
        }
        if (byInstruction.isEmpty()) return;
        md.add("");
        md.add("## Instrucciones interpretadas por especialidad (alcance medio · distancia mínima · s en contacto)");
        String[] metrics = {"meanEnemyDist", "minEnemyDist", "contactSeconds"};
        double[] floor = {1.5D, 1.0D, 2.0D};
        for (Map.Entry<String, List<String>> inst : byInstruction.entrySet()) {
            List<String> cells = inst.getValue();
            for (String id : cells) {
                List<SimRun> rs = by.get(id);
                md.add(String.format(Locale.ROOT, "- %s / %s: %.1f · %.1f · %.0f s (n=%d)", inst.getKey(), id,
                        stat(rs, metrics[0]).mean(), stat(rs, metrics[1]).mean(), stat(rs, metrics[2]).mean(), rs.size()));
            }
            int pairs = 0, same = 0;
            for (int i = 0; i < cells.size(); i++) {
                for (int j = i + 1; j < cells.size(); j++) {
                    pairs++;
                    boolean distinct = false;
                    for (int m = 0; m < metrics.length; m++) {
                        SimReport.Stat a = stat(by.get(cells.get(i)), metrics[m]), b = stat(by.get(cells.get(j)), metrics[m]);
                        if (a.n() == 0 || b.n() == 0) continue;
                        if (Math.abs(a.mean() - b.mean()) > Math.max(floor[m], 2 * (a.se() + b.se()))) distinct = true;
                    }
                    if (!distinct) {
                        same++;
                        findings.add(String.format(Locale.ROOT, "Instrucción %s: %s y %s se comportan igual (ajustar "
                                + "config/rallyguard-instructions.json)", inst.getKey(), cells.get(i), cells.get(j)));
                    }
                }
            }
            md.add(String.format(Locale.ROOT, "  - %s: %d de %d pares de especialidades se distinguen", inst.getKey(), pairs - same, pairs));
        }
    }
}
