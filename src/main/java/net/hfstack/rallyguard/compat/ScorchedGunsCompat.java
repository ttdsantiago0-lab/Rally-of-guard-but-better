package net.hfstack.rallyguard.compat;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.slf4j.Logger;
import tallestegg.guardvillagers.common.entities.Guard;

import java.lang.reflect.Constructor;

/**
 * Phase 3H "weapon-aware guards" (user review: "¿qué tiene el guardia en la mano? ...si
 * tiene una pistola, que la use muy bien... revisa la manera en que los mobs de Scorched
 * Guns usan sus armas"). Role = equipment, never a new button (docs/ARQUITECTURA.md §4.2).
 * <p>
 * Bows and crossbows already work: Guard Villagers' own goals use them. A Scorched Guns
 * gun does not - Guard Villagers' melee goal takes over and the guard clubs people with
 * it. Scorched Guns ships the exact AI its own gunner mobs (and flare-summoned raiders)
 * use: {@code top.ribs.scguns.entity.ai.GunAttackGoal<T extends PathfinderMob>(T shooter,
 * int aiDifficulty)} - read from its real NeoForge 1.21.1 source
 * ({@code sadeast69/ScorchedGunsNeoforge}, mod id {@code scguns}): flags MOVE + LOOK,
 * {@code canUse()} = a live target AND a {@code GunItem} in the main hand, handles aim,
 * bursts, reload and range itself, and refills the mob's magazine on reload (so a guard,
 * like their mobs, needs no ammo in its inventory).
 * <p>
 * So Rally just adds that goal to every Guard, at priority 2 - above Guard Villagers'
 * melee/bow/crossbow goals (priority 3) - by reflection, only when Scorched Guns is
 * loaded. With no gun in hand it never starts, so nothing else changes. Everything Rally
 * already controls still applies because it all works through the guard's TARGET (hold
 * fire, post leash, stances, designated target, defend, kamikaze). Mob.goalSelector is
 * public on NeoForge (its access transformer), which is how Guard Villagers' own
 * {@code ModCompat.UseMusketGoal} is added too.
 * <p>
 * Fail-soft: if the class or constructor is missing (another Scorched Guns version),
 * this logs once and does nothing.
 */
public final class ScorchedGunsCompat {

    public static final String MOD_ID = "scguns";
    private static final String GOAL_CLASS = "top.ribs.scguns.entity.ai.GunAttackGoal";
    private static final String GUN_ITEM_CLASS = "top.ribs.scguns.item.GunItem";
    /** Scorched Guns' own scale: spread = max(0.02, 0.35 - 0.08 x difficulty). 4 is its
     *  near-best aim - trained soldiers, not bandits. */
    private static final int AI_DIFFICULTY = 4;
    private static final int PRIORITY = 2;

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Constructor<?> goalConstructor;
    private final Class<?> goalClass;

    private static Class<?> gunItemClass;

    private ScorchedGunsCompat(Class<?> goalClass, Constructor<?> goalConstructor) {
        this.goalClass = goalClass;
        this.goalConstructor = goalConstructor;
    }

    /** Returns the listener to register, or null when Scorched Guns isn't usable. */
    public static ScorchedGunsCompat createIfLoaded() {
        if (!ModList.get().isLoaded(MOD_ID)) return null;
        try {
            Class<?> goal = Class.forName(GOAL_CLASS);
            Constructor<?> ctor = goal.getConstructor(PathfinderMob.class, int.class);
            try {
                gunItemClass = Class.forName(GUN_ITEM_CLASS);
            } catch (ClassNotFoundException ignored) {
                gunItemClass = null; // only used for the mounted stand-off distance
            }
            LOGGER.info("[rallyguard] Scorched Guns found: guards holding a gun will use it");
            return new ScorchedGunsCompat(goal, ctor);
        } catch (ReflectiveOperationException | LinkageError e) {
            LOGGER.warn("[rallyguard] Scorched Guns is loaded but its GunAttackGoal wasn't found; "
                    + "guards won't use guns ({})", e.toString());
            return null;
        }
    }

    /** True for a Scorched Guns gun (false when the mod isn't there). */
    public static boolean isGun(ItemStack stack) {
        return gunItemClass != null && gunItemClass.isInstance(stack.getItem());
    }

    @SubscribeEvent
    public void onJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof Guard guard)) return;
        try {
            boolean present = guard.goalSelector.getAvailableGoals().stream()
                    .anyMatch(wrapped -> goalClass.isInstance(wrapped.getGoal()));
            if (!present) {
                guard.goalSelector.addGoal(PRIORITY, (Goal) goalConstructor.newInstance(guard, AI_DIFFICULTY));
            }
        } catch (ReflectiveOperationException | RuntimeException | LinkageError e) {
            LOGGER.warn("[rallyguard] Could not give guard {} the Scorched Guns gun goal ({})",
                    guard.getUUID(), e.toString());
        }
    }
}
