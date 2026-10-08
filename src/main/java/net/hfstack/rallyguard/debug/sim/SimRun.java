package net.hfstack.rallyguard.debug.sim;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/** E2A block 3 - one finished simulation run: everything one line of {@code runs.jsonl} holds. */
public final class SimRun {
    public String batch = "", preset = "", scenario = "", tickrate = "normal", winner = "draw", notes = "";
    public long seed;
    public int rep, capTicks, ticks, leaderDeathsBlue, leaderDeathsRed;
    public boolean control, retried, stuck, timeout;
    public final int[] start = new int[2], survivors = new int[2];
    public final double[] healthLeft = new double[2], damage = new double[2];
    public final long[] shots = new long[2], hits = new long[2], ammo = new long[2], swaps = new long[2],
            orders = new long[2], retreats = new long[2], drowned = new long[2];
    public long[] ladder = new long[0];
    public double cohesionBlue, meanEnemyDist, perfMsPerTick, perfPeakMs;
    /** E3A F9: share of guards within 2.5 blocks of their formation slot 30 s after the order (-1 = n/a). */
    public double formationKept = -1;
    /** E3A V9: mean seconds blue units stayed alive; mean metres beyond the leash per sample; seconds from the
     *  attack order to blue's first damage (-1 = never); blocked friendly hits; XP per guard per combat minute (V8). */
    public double survivalSeconds, pursuitMeters, orderSeconds = -1, xpPerGuardMinute, commandXp;
    public final long[] friendlyFire = new long[2];
    public final java.util.Map<String, Long> reasons = new java.util.LinkedHashMap<>();

    /** A named metric for summaries and checks ({@code blueWin} counts a draw as 0.5). */
    public double metric(String name) {
        return switch (name) {
            case "blueWin" -> "blue".equals(winner) ? 1.0D : "draw".equals(winner) ? 0.5D : 0.0D;
            case "redWin" -> "red".equals(winner) ? 1.0D : "draw".equals(winner) ? 0.5D : 0.0D;
            case "blueSurvivors" -> survivors[0];
            case "redSurvivors" -> survivors[1];
            case "blueCasualties" -> start[0] - survivors[0];
            case "redCasualties" -> start[1] - survivors[1];
            case "blueHealth" -> healthLeft[0];
            case "redHealth" -> healthLeft[1];
            case "seconds" -> ticks / 20.0D;
            case "blueDamage" -> damage[0];
            case "redDamage" -> damage[1];
            case "blueShots" -> shots[0];
            case "blueHits" -> hits[0];
            case "blueAccuracy" -> shots[0] == 0 ? 0.0D : (double) hits[0] / shots[0];
            case "meanEnemyDist" -> meanEnemyDist;
            case "cohesion" -> cohesionBlue;
            case "retreats" -> retreats[0] + retreats[1];
            case "perfMs" -> perfMsPerTick;
            case "perfPeakMs" -> perfPeakMs;
            case "formationKept" -> formationKept < 0 ? Double.NaN : formationKept;
            case "survivalSeconds" -> survivalSeconds;
            case "pursuit" -> pursuitMeters;
            case "orderSeconds" -> orderSeconds < 0 ? Double.NaN : orderSeconds;
            case "friendlyFire" -> friendlyFire[0];
            case "damagePerSecond" -> ticks <= 0 ? 0.0D : damage[0] / (ticks / 20.0D);
            case "xpPerGuardMinute" -> xpPerGuardMinute;
            default -> Double.NaN;
        };
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("batch", batch);
        o.addProperty("preset", preset);
        o.addProperty("scenario", scenario);
        o.addProperty("seed", seed);
        o.addProperty("rep", rep);
        o.addProperty("tickrate", tickrate);
        o.addProperty("control", control);
        o.addProperty("winner", winner);
        o.addProperty("timeout", timeout);
        o.addProperty("stuck", stuck);
        o.addProperty("retried", retried);
        o.addProperty("seconds", ticks / 20.0D);
        o.addProperty("capSeconds", capTicks / 20.0D);
        o.add("blue", side(0, leaderDeathsBlue));
        o.add("red", side(1, leaderDeathsRed));
        o.addProperty("cohesionBlue", round(cohesionBlue));
        o.addProperty("meanEnemyDist", round(meanEnemyDist));
        JsonArray l = new JsonArray();
        for (long v : ladder) l.add(v);
        o.add("ladder", l);
        o.addProperty("perfMsPerTick", round(perfMsPerTick));
        o.addProperty("perfPeakMs", round(perfPeakMs));
        if (formationKept >= 0) o.addProperty("formationKept", round(formationKept));
        o.addProperty("survivalSeconds", round(survivalSeconds));
        o.addProperty("pursuitMeters", round(pursuitMeters));
        o.addProperty("orderSeconds", round(orderSeconds));
        o.addProperty("friendlyFireBlue", friendlyFire[0]);
        o.addProperty("friendlyFireRed", friendlyFire[1]);
        if (xpPerGuardMinute > 0) o.addProperty("xpPerGuardMinute", round(xpPerGuardMinute));
        if (commandXp > 0) o.addProperty("commandXp", round(commandXp));
        JsonObject rs = new JsonObject();
        for (java.util.Map.Entry<String, Long> e : reasons.entrySet()) rs.addProperty(e.getKey(), e.getValue());
        o.add("reasons", rs);
        if (!notes.isEmpty()) o.addProperty("notes", notes);
        return o;
    }

    private JsonObject side(int s, int leaderDeaths) {
        JsonObject o = new JsonObject();
        o.addProperty("start", start[s]);
        o.addProperty("survivors", survivors[s]);
        o.addProperty("healthLeft", round(healthLeft[s]));
        o.addProperty("damage", round(damage[s]));
        o.addProperty("shots", shots[s]);
        o.addProperty("hits", hits[s]);
        o.addProperty("ammo", ammo[s]);
        o.addProperty("weaponSwaps", swaps[s]);
        o.addProperty("orders", orders[s]);
        o.addProperty("retreats", retreats[s]);
        o.addProperty("drowned", drowned[s]);
        o.addProperty("leaderDeaths", leaderDeaths);
        return o;
    }

    static double round(double v) {
        return Math.round(v * 100.0D) / 100.0D;
    }
}
