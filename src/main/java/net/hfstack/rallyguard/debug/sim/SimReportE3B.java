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
        profiles(md, findings, by, matrix);
        instructions(md, findings, by, matrix);
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
