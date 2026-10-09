package net.hfstack.rallyguard.combat;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import net.hfstack.rallyguard.veterancy.WeaponMastery;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;
import tallestegg.guardvillagers.common.entities.Guard;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * E3A A8 (H-06) - what weapon mastery ({@link WeaponMastery#level}, 0..1) DOES. Each weapon
 * archetype has a BUDGET spread over five effects, in fractions at full mastery:
 * <ul>
 *     <li>{@code cadence} - shorter time between shots (Rally's bow/crossbow fire);</li>
 *     <li>{@code reload} - shorter crossbow reload (added to the cadence for crossbows);</li>
 *     <li>{@code spread} - tighter aim (bow/crossbow spread; Scorched Guns: a better
 *     {@code aiDifficulty} for the gun goal, applied when the guard joins the level);</li>
 *     <li>{@code effectDuration} - special effects last longer: the burn of a Flame arrow or
 *     a Fire Aspect blade (any weapon whose hit sets the target on fire, any mod);</li>
 *     <li>{@code effectRange} - reaches further (the volley / shot reach).</li>
 * </ul>
 * Raw damage is never raised (David: the effects before the damage). Every effect is capped
 * at {@code maxPerEffect} and an archetype's effects are scaled down together when they add
 * up to more than its {@code budget}. Table in {@code config/rallyguard-weapon-mastery.json}
 * (written with the defaults when missing, read once per server start), with per-item
 * overrides by id ({@code "overrides": {"modid:item": {...}}}) - no recompiling. Weapons of
 * other mods fall in a generic archetype by their class ({@link WeaponMastery#archetype}).
 */
public final class MasteryEffects {

    /**
     * E3B V6 appends three non-damage effects so every weapon reads differently: {@code composure}
     * (retreats later - the axe and the shield), {@code focus} (keeps its target until it falls - the
     * sword and the gun) and {@code discipline} (chases less, holds its slot tighter - the spear and the
     * shield). Append only (ordinals are not saved, but keep the order stable).
     */
    public enum Effect { CADENCE, RELOAD, SPREAD, EFFECT_DURATION, EFFECT_RANGE, COMPOSURE, FOCUS, DISCIPLINE }

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String FILE = "rallyguard-weapon-mastery.json";

    private static Map<String, Map<Effect, Double>> archetypes;
    private static Map<String, Map<Effect, Double>> overrides;
    private static double maxPerEffect = 0.5D;
    private static double gunDifficultyBonus = 1.0D;

    /** Hits whose fire must be stretched once the hit (and its post-attack ignition) is done. */
    private static final List<Burn> PENDING = new ArrayList<>();

    private record Burn(LivingEntity target, int ticksBefore, double factor) {
    }

    /** E3B R7: the configured ceiling of any one mastery effect (for the anti-inflation report). */
    public static double maxPerEffect() {
        return maxPerEffect;
    }

    /** The fraction ({@code 0..maxPerEffect}) {@code effect} improves for this guard and weapon now. */
    public static double bonus(Guard guard, ItemStack weapon, Effect effect) {
        double m = WeaponMastery.level(guard);
        if (m <= 0.0D) return 0.0D;
        Map<Effect, Double> row = row(weapon);
        return row == null ? 0.0D : Math.min(maxPerEffect, row.getOrDefault(effect, 0.0D)) * m;
    }

    /** Multiplier for the time between shots (cadence, plus reload for crossbows). */
    public static double cooldownFactor(Guard guard, ItemStack weapon) {
        double b = bonus(guard, weapon, Effect.CADENCE);
        if ("crossbow".equals(WeaponMastery.archetype(weapon))) b += bonus(guard, weapon, Effect.RELOAD);
        return Math.max(1.0D - maxPerEffect, 1.0D - b);
    }

    public static double spreadFactor(Guard guard, ItemStack weapon) {
        return 1.0D - bonus(guard, weapon, Effect.SPREAD);
    }

    public static double reachFactor(Guard guard, ItemStack weapon) {
        return 1.0D + bonus(guard, weapon, Effect.EFFECT_RANGE);
    }

    /** E3B V6: the held weapon's bonus for one of the behaviour effects (composure, focus, discipline). */
    public static double held(Guard guard, Effect effect) {
        return bonus(guard, guard.getMainHandItem(), effect);
    }

    /** E3B V6: what mastery of this weapon changes, strongest first (effects with a share ≥ 0.1). */
    public static List<Effect> effectsOf(ItemStack weapon) {
        Map<Effect, Double> row = row(weapon);
        List<Effect> out = new ArrayList<>();
        if (row == null) return out;
        for (Effect f : Effect.values()) if (row.getOrDefault(f, 0.0D) >= 0.1D) out.add(f);
        out.sort((a, b) -> Double.compare(row.getOrDefault(b, 0.0D), row.getOrDefault(a, 0.0D)));
        return out;
    }

    /** E3B V6: the effects of an archetype by name ("bow", "sword"...), for the card. */
    public static List<Effect> effectsOf(String archetype) {
        ensureLoaded();
        Map<Effect, Double> row = archetypes.get(archetype);
        List<Effect> out = new ArrayList<>();
        if (row == null) return out;
        for (Effect f : Effect.values()) if (row.getOrDefault(f, 0.0D) >= 0.1D) out.add(f);
        out.sort((a, b) -> Double.compare(row.getOrDefault(b, 0.0D), row.getOrDefault(a, 0.0D)));
        return out;
    }

    /** Scorched Guns' {@code aiDifficulty} bonus for a gunner (0 or more whole steps). */
    public static int gunDifficultyBonus(Guard guard) {
        ensureLoaded();
        return (int) Math.floor(gunDifficultyBonus * WeaponMastery.level(guard) + 1.0E-6D);
    }

    /** E3A A8: a Flame arrow / Fire Aspect hit by a master burns longer. */
    @SubscribeEvent
    public void onHit(LivingDamageEvent.Post event) {
        if (!(event.getEntity().level() instanceof ServerLevel)) return;
        Entity src = event.getSource().getEntity();
        Entity direct = event.getSource().getDirectEntity();
        if (!(src instanceof Guard guard) || !net.hfstack.rallyguard.contract.GuardOwnership.hasOwner(guard)) return;
        ItemStack weapon = guard.getMainHandItem();
        double b = bonus(guard, weapon, Effect.EFFECT_DURATION);
        if (b <= 0.0D) return;
        LivingEntity target = event.getEntity();
        if (direct instanceof Projectile p) {
            // A burning arrow already set its 5 s burn before the damage: stretch that base (no compounding).
            if (p.isOnFire() && target.getRemainingFireTicks() > 0) {
                target.setRemainingFireTicks(Math.max(target.getRemainingFireTicks(), (int) Math.round(100 * (1.0D + b))));
            }
            return;
        }
        // Melee: Fire Aspect-like effects ignite AFTER the damage, so look again next tick.
        if (PENDING.size() < 512) PENDING.add(new Burn(target, target.getRemainingFireTicks(), 1.0D + b));
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        if (PENDING.isEmpty()) return;
        for (Burn b : PENDING) {
            int now = b.target().getRemainingFireTicks();
            // Only a burn this hit started or renewed is stretched (never one the target already had).
            if (b.target().isAlive() && now > 0 && now > b.ticksBefore()) {
                b.target().setRemainingFireTicks((int) Math.round(now * b.factor()));
            }
        }
        PENDING.clear();
    }

    private static Map<Effect, Double> row(ItemStack weapon) {
        ensureLoaded();
        Map<Effect, Double> o = overrides.get(BuiltInRegistries.ITEM.getKey(weapon.getItem()).toString());
        return o != null ? o : archetypes.get(WeaponMastery.archetype(weapon));
    }

    private static synchronized void ensureLoaded() {
        if (archetypes != null) return;
        archetypes = new HashMap<>();
        overrides = new HashMap<>();
        JsonObject root = defaults();
        Path file = FMLPaths.CONFIGDIR.get().resolve(FILE);
        try {
            if (Files.exists(file)) {
                root = JsonParser.parseString(Files.readString(file, StandardCharsets.UTF_8)).getAsJsonObject();
            } else {
                Files.writeString(file, new GsonBuilder().setPrettyPrinting().create().toJson(root), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            LOGGER.warn("[rallyguard] {} unreadable, using the defaults ({})", FILE, e.toString());
            root = defaults();
        }
        // E3B V6: a table written before the behaviour effects gets their defaults merged in memory (the file is
        // left as David edited it; a value he sets wins).
        if (num(root, "version", 1) < 2) {
            JsonObject defs = defaults().getAsJsonObject("archetypes");
            if (root.has("archetypes") && root.get("archetypes").isJsonObject()) {
                JsonObject mine = root.getAsJsonObject("archetypes");
                for (Map.Entry<String, JsonElement> d : defs.entrySet()) {
                    if (!mine.has(d.getKey()) || !mine.get(d.getKey()).isJsonObject()) {
                        mine.add(d.getKey(), d.getValue());
                        continue;
                    }
                    JsonObject row = mine.getAsJsonObject(d.getKey());
                    for (String k : new String[]{"composure", "focus", "discipline"}) {
                        if (!row.has(k) && d.getValue().getAsJsonObject().has(k)) row.add(k, d.getValue().getAsJsonObject().get(k));
                    }
                    // the melee rows used to be one shared burn effect: the new default spread replaces an untouched one
                    if (row.has("effectDuration") && Math.abs(num(row, "effectDuration", 0) - 0.8D) < 1.0E-9D
                            && d.getValue().getAsJsonObject().has("effectDuration")) {
                        row.add("effectDuration", d.getValue().getAsJsonObject().get("effectDuration"));
                    }
                }
            }
        }
        maxPerEffect = num(root, "maxPerEffect", 0.5D);
        gunDifficultyBonus = num(root, "gunDifficultyBonus", 1.0D);
        double budget = num(root, "budget", 1.0D);
        readTable(root, "archetypes", archetypes, budget);
        readTable(root, "overrides", overrides, budget);
    }

    private static void readTable(JsonObject root, String key, Map<String, Map<Effect, Double>> into, double defBudget) {
        if (!root.has(key) || !root.get(key).isJsonObject()) return;
        for (Map.Entry<String, JsonElement> e : root.getAsJsonObject(key).entrySet()) {
            if (!e.getValue().isJsonObject()) continue;
            JsonObject o = e.getValue().getAsJsonObject();
            Map<Effect, Double> row = new EnumMap<>(Effect.class);
            double sum = 0.0D;
            for (Effect f : Effect.values()) {
                double v = Math.max(0.0D, num(o, camel(f), 0.0D));
                row.put(f, v);
                sum += v;
            }
            double budget = num(o, "budget", defBudget);
            final double total = sum;
            if (total > budget && total > 0.0D) row.replaceAll((eff, v) -> v * budget / total); // over budget: scaled down together
            into.put(e.getKey().toLowerCase(Locale.ROOT), row);
        }
    }

    private static String camel(Effect f) {
        return switch (f) {
            case CADENCE -> "cadence";
            case RELOAD -> "reload";
            case SPREAD -> "spread";
            case EFFECT_DURATION -> "effectDuration";
            case EFFECT_RANGE -> "effectRange";
            case COMPOSURE -> "composure";
            case FOCUS -> "focus";
            case DISCIPLINE -> "discipline";
        };
    }

    private static double num(JsonObject o, String key, double def) {
        try {
            return o.has(key) ? o.get(key).getAsDouble() : def;
        } catch (RuntimeException e) {
            return def;
        }
    }

    private static JsonObject defaults() {
        JsonObject root = new JsonObject();
        root.addProperty("_doc", "E3A A8 weapon mastery: fractions gained at FULL mastery per archetype; each capped at "
                + "maxPerEffect, an archetype's sum scaled down to its budget. Overrides by item id. Raw damage is never raised.");
        root.addProperty("version", 2);
        root.addProperty("budget", 1.0D);
        root.addProperty("maxPerEffect", 0.5D);
        root.addProperty("gunDifficultyBonus", 1.0D);
        JsonObject a = new JsonObject();
        // E3B V6: every archetype with its own recognizable profile (budget 1.0; damage never).
        a.add("bow", row(0.25, 0.0, 0.35, 0.25, 0.15, 0.0, 0.0, 0.0));        // fast, accurate, far
        a.add("crossbow", row(0.10, 0.30, 0.30, 0.10, 0.05, 0.0, 0.15, 0.0)); // reload, aim, picks a target
        a.add("ranged", row(0.15, 0.10, 0.25, 0.20, 0.10, 0.0, 0.0, 0.0));
        a.add("gun", row(0.0, 0.0, 0.40, 0.10, 0.10, 0.0, 0.30, 0.0));        // aim and focus
        a.add("sword", row(0.0, 0.0, 0.0, 0.30, 0.0, 0.20, 0.50, 0.0));       // duelist: finishes its opponent
        a.add("axe", row(0.0, 0.0, 0.0, 0.30, 0.0, 0.50, 0.10, 0.0));         // holds on when hurt
        a.add("trident", row(0.0, 0.0, 0.0, 0.20, 0.0, 0.20, 0.0, 0.50));     // spear line: keeps formation
        a.add("shield", row(0.0, 0.0, 0.0, 0.0, 0.0, 0.50, 0.0, 0.40));       // stands and holds the line
        a.add("melee", row(0.0, 0.0, 0.0, 0.30, 0.0, 0.30, 0.0, 0.0));
        root.add("archetypes", a);
        JsonObject o = new JsonObject();
        o.add("minecraft:example_item", row(0.0, 0.0, 0.0, 0.0, 0.0));
        root.add("overrides", o);
        return root;
    }

    private static JsonObject row(double cadence, double reload, double spread, double duration, double range) {
        return row(cadence, reload, spread, duration, range, 0, 0, 0);
    }

    private static JsonObject row(double cadence, double reload, double spread, double duration, double range,
                                  double composure, double focus, double discipline) {
        JsonObject r = new JsonObject();
        r.addProperty("composure", composure);
        r.addProperty("focus", focus);
        r.addProperty("discipline", discipline);
        r.addProperty("cadence", cadence);
        r.addProperty("reload", reload);
        r.addProperty("spread", spread);
        r.addProperty("effectDuration", duration);
        r.addProperty("effectRange", range);
        return r;
    }

    /** Re-read on the next use (server stop). */
    public static void clearAll() {
        archetypes = null;
        overrides = null;
        PENDING.clear();
    }
}
