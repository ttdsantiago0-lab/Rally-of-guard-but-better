package net.hfstack.rallyguard.force;

import net.hfstack.rallyguard.tactics.Doctrine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * E3A A2 - force rules ({@code docs/ARQUITECTURA_VETERANIA.md} §8). Each rule is
 * {@code {id, scope, condition, action, priority, cooldown, hysteresis (enter / exit
 * thresholds), minimum duration}}:
 * <ul>
 *     <li>it activates when its metric crosses {@code enter} (and its cooldown is over), and
 *     deactivates only when the metric crosses back past {@code exit} AND it has been active
 *     for {@code minDuration} - a rule entering at 40 % casualties never flips back at 41 %;</li>
 *     <li>between active rules on the same unit, the highest priority wins (per rule, not per
 *     type); ties go to definition order;</li>
 *     <li>rules are evaluated by the force brain at most once a second, on the units' reports.</li>
 * </ul>
 * The default rules come from the force's doctrine; plans (A4) can add their own.
 */
public final class ForceRules {
    private ForceRules() {
    }

    public enum Scope { UNIT, FORCE }

    public enum Metric { CASUALTIES, HEALTH, AMMO, CONTACT_SECONDS, ENEMIES_SEEN }

    /** What a rule makes the affected units do (compiled to {@link UnitOrders}). */
    public enum Action { RETREAT_HQ, FALLBACK, HOLD, RESUPPLY, VOLLEY, RELEASE_RESERVE }

    /**
     * @param above      true: active when the metric is ≥ enter, off when ≤ exit; false: the reverse
     * @param roleFilter only units with this role ("" = any)
     */
    public record Rule(String id, Scope scope, Metric metric, double enter, double exit, boolean above, Action action,
                       int priority, int cooldownTicks, int minDurationTicks, String roleFilter) {
    }

    /** Runtime state of one rule on one target (unit or force). */
    public static final class State {
        boolean active;
        long since = Long.MIN_VALUE / 2;
        long lastChange = Long.MIN_VALUE / 2;
        int activations, deactivations;

        public boolean active() {
            return active;
        }
    }

    /** Hysteresis + cooldown + minimum duration; returns whether the state changed. */
    public static boolean evaluate(Rule rule, State state, double value, long now) {
        if (!state.active) {
            boolean crossed = rule.above() ? value >= rule.enter() : value <= rule.enter();
            if (crossed && now - state.lastChange >= rule.cooldownTicks()) {
                state.active = true;
                state.since = now;
                state.lastChange = now;
                state.activations++;
                return true;
            }
            return false;
        }
        boolean back = rule.above() ? value <= rule.exit() : value >= rule.exit();
        if (back && now - state.since >= rule.minDurationTicks()) {
            state.active = false;
            state.lastChange = now;
            state.deactivations++;
            return true;
        }
        return false;
    }

    /** The doctrine's default rules (playbook §4 retreat lines; K-02 ammo reaction by role). */
    public static List<Rule> defaults(Doctrine d) {
        List<Rule> rules = new ArrayList<>();
        double forceRetreat = d == Doctrine.ALL_OUT ? 0.85D : d == Doctrine.AVOID_CASUALTIES ? 0.25D
                : d == Doctrine.HOLD_POSITION ? 0.75D : 0.40D;
        rules.add(new Rule("force-retreat", Scope.FORCE, Metric.CASUALTIES, forceRetreat, Math.max(0, forceRetreat - 0.2D),
                true, Action.RETREAT_HQ, 100, 200, 600, ""));
        double unitLine = Math.max(0.1D, Math.min(0.6D, d.retreatHealth));
        rules.add(new Rule("unit-fallback", Scope.UNIT, Metric.HEALTH, unitLine, unitLine + 0.15D, false,
                Action.FALLBACK, 50, 200, 300, ""));
        // K-02: a rear (support) unit out of ammo goes to resupply; a front-line one switches to melee by itself.
        rules.add(new Rule("support-resupply", Scope.UNIT, Metric.AMMO, 0.15D, 0.5D, false, Action.RESUPPLY, 40, 400, 400, "SUPPORT"));
        rules.add(new Rule("anvil-reserve", Scope.UNIT, Metric.CASUALTIES, 0.40D, 0.20D, true, Action.RELEASE_RESERVE,
                30, 600, 600, "ANVIL"));
        return rules;
    }

    /**
     * The hysteresis check the prompt asks for, run without a world: a metric oscillating
     * 39 % / 41 % around a 40 % entry (exit 20 %) must activate ONCE and never flip. Returns a
     * one-line verdict for the simulation summary.
     */
    public static String selfTest() {
        Rule r = new Rule("test", Scope.UNIT, Metric.CASUALTIES, 0.40D, 0.20D, true, Action.FALLBACK, 1, 0, 100, "");
        State s = new State();
        double[] series = {0.38, 0.39, 0.41, 0.39, 0.41, 0.39, 0.41, 0.39, 0.41, 0.39, 0.30, 0.25, 0.21};
        long t = 0;
        for (double v : series) {
            evaluate(r, s, v, t);
            t += 20;
        }
        boolean ok = s.activations == 1 && s.deactivations == 0;
        return String.format(Locale.ROOT, "Reglas con histéresis (39↔41 %%, entrada 40 %%, salida 20 %%): %d activación, %d "
                + "desactivaciones -> %s", s.activations, s.deactivations, ok ? "sin alternancia ✓" : "ALTERNA ✗");
    }

    /** Rule states per "target#ruleId" (memory only; they settle again within seconds after a restart). */
    static final Map<String, State> STATES = new HashMap<>();

    static State state(String target, Rule rule) {
        return STATES.computeIfAbsent(target + "#" + rule.id(), k -> new State());
    }

    /** E3B R4 (B4): drops the rule states of a force and its units (keys "owner#squad#rule", "owner#squad/unit#rule"). */
    public static void forgetPrefix(String prefix) {
        STATES.keySet().removeIf(k -> k.startsWith(prefix + "#") || k.startsWith(prefix + "/"));
    }

    public static void clearAll() {
        STATES.clear();
    }
}
