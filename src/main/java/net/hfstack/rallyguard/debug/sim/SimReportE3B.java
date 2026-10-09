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
        inflationKnobs(md);
        liveTactics(md, findings, by, matrix);
        morale(md, findings, by, matrix);
        globalChecks(md, findings, by);
        rankAttributes(md, findings, by, matrix);
        perf(md, findings, by, matrix);
        profiles(md, findings, by, matrix);
        instructions(md, findings, by, matrix);
        planner(md, findings, by, matrix);
    }

    /**
     * E3B P: planner v2 and scenarios. {@code scenario-*} (tag {@code expectscn:KIND}) - the detector fired
     * (share of runs) and survivors against the same fight with no plan ({@code <id>-noplan}); {@code planner2-*} -
     * the link under test did its job (fills, ambush sprung, lane passed, delay steps) and survivors against
     * the plan without it ({@code <id>-base}).
     */
    static void planner(List<String> md, List<String> findings, Map<String, List<SimRun>> by, SimScenario.Matrix matrix) {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<String, List<SimRun>> e : by.entrySet()) {
            SimScenario s = matrix.scenarios().get(e.getKey());
            if (s == null || s.tag("plan").isEmpty()) continue;
            List<SimRun> rs = e.getValue();
            double surv = stat(rs, "blueSurvivors").mean();
            String controlId = e.getKey().startsWith("scenario-") ? e.getKey() + "-noplan" : e.getKey() + "-base";
            List<SimRun> control = by.get(controlId);
            String vs = control == null ? "" : String.format(Locale.ROOT, " (sin %s: %.1f)",
                    e.getKey().startsWith("scenario-") ? "plan" : "el vínculo", stat(control, "blueSurvivors").mean());
            if (!s.tag("expectscn").isEmpty()) {
                double det = stat(rs, "scnDetected").mean();
                lines.add(String.format(Locale.ROOT, "- %s: %s detectado en %.0f %% de %d repeticiones, supervivientes %.1f%s %s",
                        e.getKey(), s.tag("expectscn").toUpperCase(Locale.ROOT), det * 100, rs.size(), surv, vs, det >= 0.5D ? "✓" : "✗"));
                if (det < 0.5D) findings.add(e.getKey() + ": el detector de " + s.tag("expectscn") + " no se activa (≥ 50 % esperado)");
            } else if (e.getKey().startsWith("planner2-")) {
                String kind = s.tag("plan").toLowerCase(Locale.ROOT);
                String metric = switch (kind) {
                    case "fill" -> "fills";
                    case "ambush" -> "ambushSprung";
                    case "lane" -> "lanePassed";
                    case "delay" -> "delaySteps";
                    default -> "";
                };
                double v = metric.isEmpty() ? Double.NaN : stat(rs, metric).mean();
                lines.add(String.format(Locale.ROOT, "- %s: %s %.1f, supervivientes %.1f%s %s", e.getKey(),
                        metric.isEmpty() ? "-" : metric, Double.isNaN(v) ? 0 : v, surv, vs,
                        metric.isEmpty() || v > 0 ? "✓" : "✗"));
                if (!metric.isEmpty() && !(v > 0) && !e.getKey().endsWith("-base")) {
                    findings.add(e.getKey() + ": el vínculo " + kind + " no actuó (" + metric + " = 0)");
                }
            }
        }
        if (lines.isEmpty()) return;
        md.add("");
        md.add("## Planner v2 y escenarios (E3B P)");
        md.addAll(lines);
    }

    /**
     * E3B R7 (decisions C1/C2 of REVISION_E3A, kept as they were but capped): what rank and mastery may add to
     * aim and reach - David decides with these numbers next to the 1 v 1 alarm.
     */
    static void inflationKnobs(List<String> md) {
        double perRank = net.hfstack.rallyguard.config.RallyConfig.d(net.hfstack.rallyguard.config.RallyConfig.RANGE_PER_RANK, 0.15D);
        double cap = net.hfstack.rallyguard.config.RallyConfig.d(net.hfstack.rallyguard.config.RallyConfig.RANGE_RANK_CAP, 0.75D);
        double spread = net.hfstack.rallyguard.config.RallyConfig.d(net.hfstack.rallyguard.config.RallyConfig.VOLLEY_SPREAD_PER_RANK, 0.12D);
        double floor = net.hfstack.rallyguard.config.RallyConfig.d(net.hfstack.rallyguard.config.RallyConfig.SPREAD_RANK_FLOOR, 0.3D);
        md.add("");
        md.add(String.format(Locale.ROOT, "## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)"));
        md.add(String.format(Locale.ROOT, "- Alcance por rango +%.0f %% (tope +%.0f %%, R5 = +%.0f %%); dispersión −%.0f %% por rango (suelo %.0f %%, R5 = %.0f %%); "
                        + "maestría: cada efecto ≤ %.0f %% (desde rango 0)", perRank * 100, cap * 100, Math.min(cap, 5 * perRank) * 100,
                spread * 100, floor * 100, Math.max(floor, 1 - 5 * spread) * 100,
                net.hfstack.rallyguard.combat.MasteryEffects.maxPerEffect() * 100));
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
            if (s.tags().contains("split")) { // E3B R3
                double c = stat(rs, "splitCasualties").mean(), fr = stat(rs, "forceRetreat").mean();
                lines.add(String.format(Locale.ROOT, "- %s: bajas tras dividir la unidad %.0f %%, retirada de fuerza %.0f %% %s",
                        e.getKey(), c * 100, fr * 100, c == 0 && fr == 0 ? "✓" : "✗"));
                if (c > 0 || fr > 0) findings.add(e.getKey() + ": dividir una unidad cuenta bajas o dispara la retirada");
            }
            if (s.tags().contains("freshcentre")) { // E3B R3
                double err = stat(rs, "reportCentreError").mean(), moved = stat(rs, "unitMoved").mean();
                lines.add(String.format(Locale.ROOT, "- %s: la unidad avanzó %.0f bloques; error del centro informado %.1f %s",
                        e.getKey(), moved, err, err < 4 ? "✓" : "✗"));
                if (err >= 4) findings.add(e.getKey() + ": el informe de unidad no sigue su posición");
            }
            if (s.tags().contains("retreatop")) { // E3B R4
                double st = stat(rs, "retreatStarted").mean(), back = stat(rs, "opRestored").mean();
                lines.add(String.format(Locale.ROOT, "- %s: retirada de fuerza iniciada %.0f %%, operación restaurada tras la orden del "
                        + "jugador %.0f %% %s", e.getKey(), st * 100, back * 100, st == 1 && back == 1 ? "✓" : "✗"));
                if (st < 1 || back < 1) findings.add(e.getKey() + ": la retirada de fuerza no se inicia o no devuelve la operación");
            }
            if (s.tags().contains("plancancel")) { // E3B R4
                double first = stat(rs, "firstRetreat").mean(), leak = stat(rs, "inheritedRetreat").mean();
                lines.add(String.format(Locale.ROOT, "- %s: primer plan se retira %.0f %%, el plan nuevo hereda la retirada %.0f %% %s",
                        e.getKey(), first * 100, leak * 100, leak == 0 ? "✓" : "✗"));
                if (leak > 0) findings.add(e.getKey() + ": un plan nuevo hereda el estado de reglas del plan cancelado");
            }
            if (s.tags().contains("hqremount")) { // E3B R5
                double parked = stat(rs, "parkedShare").mean(), back = stat(rs, "remountedShare").mean();
                lines.add(String.format(Locale.ROOT, "- %s: aparcados tras 3 min %.0f %%, vuelven a montar al salir %.0f %% %s",
                        e.getKey(), parked * 100, back * 100, back >= 1 ? "✓" : "✗"));
                if (back < 1) findings.add(e.getKey() + ": tras descansar en el HQ no todos vuelven a montar");
            }
            if (s.tags().contains("relaxfollow")) { // E3B R5
                double relaxed = stat(rs, "relaxedShare").mean(), away = stat(rs, "stayedWithCommander").mean();
                lines.add(String.format(Locale.ROOT, "- %s: relajados %.0f %%, siguen al comandante tras 70 s %.0f %% %s",
                        e.getKey(), relaxed * 100, away * 100, away >= 1 ? "✓" : "✗"));
                if (away < 1) findings.add(e.getKey() + ": un guardia relajado que recibe Seguir vuelve al HQ");
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

    /** E3B M §8: morale scenarios - time to the first break, broken share at 60 s, rallies, deaths in flight. */
    static void morale(List<String> md, List<String> findings, Map<String, List<SimRun>> by, SimScenario.Matrix matrix) {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<String, List<SimRun>> e : by.entrySet()) {
            SimScenario s = matrix.scenarios().get(e.getKey());
            if (s == null || !s.tags().contains("morale")) continue;
            List<SimRun> rs = e.getValue();
            SimReport.Stat first = stat(rs, "firstBrokenSeconds");
            lines.add(String.format(Locale.ROOT, "- %s: primer ROTO %s s · rotos a los 60 s %.0f %% · máx. %.0f %% · reagrupados %.1f · "
                            + "muertos huyendo %.1f · destrozados que volvieron sin descanso %.0f",
                    e.getKey(), first.n() == 0 ? "-" : String.format(Locale.ROOT, "%.0f", first.mean()),
                    stat(rs, "brokenShareAt60").mean() * 100, stat(rs, "brokenShareMax").mean() * 100,
                    stat(rs, "rallies").mean(), stat(rs, "pursuitDeaths").mean(), stat(rs, "shatteredReturned").mean()));
            if (stat(rs, "shatteredReturned").mean() > 0) findings.add(e.getKey() + ": un DESTROZADO volvió al combate sin descansar");
            if (s.tags().contains("sensitivity") && stat(rs, "brokenShareMax").mean() > 0.20D) {
                findings.add(String.format(Locale.ROOT, "MORAL DEMASIADO SENSIBLE: %s (pareja, rango 3+ con capitán) rompe al %.0f %% "
                        + "(> 20 %%; bajar morale.weights o subir morale.thresholds)", e.getKey(), stat(rs, "brokenShareMax").mean() * 100));
            }
        }
        if (lines.isEmpty()) return;
        md.add("");
        md.add("## Moral y cohesión (morale.strength " + net.hfstack.rallyguard.config.RallyConfig.d(
                net.hfstack.rallyguard.config.RallyConfig.MORALE_STRENGTH, 0.6D) + ")");
        md.addAll(lines);
    }

    /** E3B S global checks: (2) no system changes health, damage or speed (attributes at spawn = at the end). */
    static void globalChecks(List<String> md, List<String> findings, Map<String, List<SimRun>> by) {
        int runs = 0, changed = 0;
        for (List<SimRun> rs : by.values()) {
            for (SimRun r : rs) {
                double v = r.metric("attributesUnchanged");
                if (Double.isNaN(v)) continue;
                runs++;
                if (v < 1) changed++;
            }
        }
        if (runs == 0) return;
        md.add("");
        md.add(String.format(Locale.ROOT, "## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en %d de %d corridas %s",
                runs - changed, runs, changed == 0 ? "✓" : "✗"));
        if (changed > 0) findings.add("ALGÚN SISTEMA CAMBIÓ VIDA, DAÑO O VELOCIDAD en " + changed + " corridas (moral/consistencia/maestría deben ser solo decisiones)");
    }

    /**
     * E3B S global check (1): the same kit spawns with the same health, damage and speed at every rank -
     * blue's mean spawn attributes grouped by the scenario's blue rank must be equal (rank buys decisions).
     */
    static void rankAttributes(List<String> md, List<String> findings, Map<String, List<SimRun>> by, SimScenario.Matrix matrix) {
        Map<String, double[]> byKitRank = new java.util.TreeMap<>(); // "role|mounted|rank" -> {health, damage, speed, n}
        for (Map.Entry<String, List<SimRun>> e : by.entrySet()) {
            SimScenario s = matrix.scenarios().get(e.getKey());
            if (s == null || !s.blue().guards() || "random".equals(s.blue().role())) continue;
            String key = s.blue().role() + "|" + s.blue().mounted() + "|" + s.blue().rank();
            for (SimRun r : e.getValue()) {
                double h = r.metric("attrHealth");
                if (Double.isNaN(h)) continue;
                double[] acc = byKitRank.computeIfAbsent(key, k -> new double[4]);
                acc[0] += h;
                acc[1] += r.metric("attrDamage");
                acc[2] += r.metric("attrSpeed");
                acc[3]++;
            }
        }
        if (byKitRank.isEmpty()) return;
        Map<String, double[]> byKit = new LinkedHashMap<>(); // "role|mounted" -> {min/max per attribute}
        for (Map.Entry<String, double[]> e : byKitRank.entrySet()) {
            String kit = e.getKey().substring(0, e.getKey().lastIndexOf('|'));
            double[] acc = e.getValue();
            double[] mm = byKit.computeIfAbsent(kit, k -> new double[]{Double.MAX_VALUE, -Double.MAX_VALUE,
                    Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, 0});
            for (int i = 0; i < 3; i++) {
                double v = acc[i] / acc[3];
                mm[2 * i] = Math.min(mm[2 * i], v);
                mm[2 * i + 1] = Math.max(mm[2 * i + 1], v);
            }
            mm[6]++;
        }
        int compared = 0, unequal = 0;
        for (Map.Entry<String, double[]> e : byKit.entrySet()) {
            double[] mm = e.getValue();
            if (mm[6] < 2) continue; // only one rank of this kit in the batch
            compared++;
            boolean equal = true;
            for (int i = 0; i < 3; i++) if (mm[2 * i + 1] - mm[2 * i] > 1.0E-6D) equal = false;
            if (!equal) {
                unequal++;
                findings.add(String.format(Locale.ROOT, "Atributos distintos entre rangos para el equipo %s: vida %.2f–%.2f, daño %.2f–%.2f, "
                        + "velocidad %.3f–%.3f", e.getKey(), mm[0], mm[1], mm[2], mm[3], mm[4], mm[5]));
            }
        }
        if (compared == 0) return;
        md.add(String.format(Locale.ROOT, "## Comprobación global: mismos atributos en todos los rangos (%d equipos comparados) %s",
                compared, unequal == 0 ? "✓" : "✗ (" + unequal + ")"));
    }

    /**
     * E3B S global check (3): Rally's cost per tick in the big runs (≥ 200 guards in all) and per system,
     * the new ones (forces/plans/scenarios, morale, instructions) with an alarm above 0.5 ms/tick each.
     */
    static void perf(List<String> md, List<String> findings, Map<String, List<SimRun>> by, SimScenario.Matrix matrix) {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<String, List<SimRun>> e : by.entrySet()) {
            SimScenario s = matrix.scenarios().get(e.getKey());
            if (s == null) continue;
            int guards = s.blue().count() + (s.mirror() ? s.blue().count() : s.red() == null ? 0 : s.red().count());
            if (guards < 200) continue;
            List<SimRun> rs = e.getValue();
            StringBuilder per = new StringBuilder();
            for (net.hfstack.rallyguard.debug.Perf.Sys sys : net.hfstack.rallyguard.debug.Perf.Sys.values()) {
                double ms = stat(rs, "perf_" + sys.name()).mean();
                if (Double.isNaN(ms) || ms < 0.005D) continue;
                per.append(String.format(Locale.ROOT, " %s %.2f", sys.name().toLowerCase(Locale.ROOT), ms));
                boolean isNew = sys == net.hfstack.rallyguard.debug.Perf.Sys.MORALE || sys == net.hfstack.rallyguard.debug.Perf.Sys.INSTRUCTIONS
                        || sys == net.hfstack.rallyguard.debug.Perf.Sys.FORCES;
                if (isNew && ms > 0.5D) {
                    findings.add(String.format(Locale.ROOT, "%s: %s cuesta %.2f ms/tick con %d guardias (> 0,5)", e.getKey(),
                            sys.name().toLowerCase(Locale.ROOT), ms, guards));
                }
            }
            lines.add(String.format(Locale.ROOT, "- %s (%d guardias): Rally %.2f ms/tick, pico %.1f ms; por sistema (anidados, ms/tick):%s",
                    e.getKey(), guards, stat(rs, "perfMs").mean(), stat(rs, "perfPeakMs").mean(), per));
        }
        if (lines.isEmpty()) return;
        md.add("");
        md.add("## Rendimiento con ≥ 200 guardias");
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
