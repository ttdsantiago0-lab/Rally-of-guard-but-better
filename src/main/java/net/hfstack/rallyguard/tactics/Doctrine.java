package net.hfstack.rallyguard.tactics;

import java.util.Locale;

/**
 * E2A block 4/5 - the playbook's doctrines ({@code docs/DECISIONES_TACTICAS.md} §4) as
 * WEIGHTS on the decision ladder and on terrain scoring, never new code paths: the Battle
 * Planner (E3) only has to pick one per squad. {@link #STANDARD} is today's behavior.
 *
 * @param retreatHealth  retreat below this share of health (rung 7)
 * @param preferRange    0 = close, 1 = medium, 2 = long - ranged guards keep that distance
 * @param avoidEngaging  refuse a melee fight in inferiority (more enemies than allies near)
 * @param terrainWeight  how much terrain matters (0 = ignored)
 * @param cover/height/chokepoint/defensible/forest  per-tag weights for {@link TerrainSense}
 */
public enum Doctrine {
    STANDARD(0.30D, 1, false, 1.0D, 1.0D, 1.0D, 0.0D, 0.5D, 0.0D),
    HARASS(0.70D, 2, true, 1.0D, 1.0D, 1.5D, -0.5D, 0.0D, 0.0D),
    AVOID_CASUALTIES(0.80D, 2, true, 1.2D, 1.5D, 0.5D, 1.0D, 1.0D, 0.0D),
    DISTRACT(0.50D, 1, true, 0.6D, 0.5D, 0.0D, -0.5D, 0.0D, 0.0D),
    ALL_OUT(0.15D, 0, false, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D),
    HOLD_POSITION(0.0D, 1, false, 1.2D, 1.0D, 1.0D, 1.5D, 1.5D, 0.0D),
    AMBUSH(0.40D, 0, true, 1.0D, 1.0D, 0.0D, 0.0D, 0.0D, 1.5D);

    public final double retreatHealth;
    public final int preferRange;
    public final boolean avoidEngaging;
    public final double terrainWeight, cover, height, chokepoint, defensible, forest;

    Doctrine(double retreatHealth, int preferRange, boolean avoidEngaging, double terrainWeight,
             double cover, double height, double chokepoint, double defensible, double forest) {
        this.retreatHealth = retreatHealth;
        this.preferRange = preferRange;
        this.avoidEngaging = avoidEngaging;
        this.terrainWeight = terrainWeight;
        this.cover = cover;
        this.height = height;
        this.chokepoint = chokepoint;
        this.defensible = defensible;
        this.forest = forest;
    }

    public static Doctrine parse(String s) {
        if (s == null || s.isEmpty()) return STANDARD;
        try {
            return valueOf(s.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return STANDARD;
        }
    }
}
