package net.hfstack.rallyguard.combat;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * The scutum's answer to dragon breath (David 2026-11-06: "debería proteger contra aliento de dragón"; verified in Ice
 * and Fire 2.0-beta.17, {@code IafDragonDestructionManager}): the breath and the charges call {@code hurt} with the
 * dragon as the source - a raised shield facing the dragon stops that whole hit - and THEN, whatever {@code hurt}
 * returned, set the target on fire, freeze it ({@code iceandfire:frozen}) or knock it back. A blocked hit of a damage
 * type in {@code #rallyguard:scutum_quenches} (the three breaths, optional entries: nothing breaks without Ice and Fire)
 * marks the scutum's bearer; before its next tick the fire is put out and the frost lifted, so the burn's first point of
 * damage never lands. Any bearer (a guard or a player), any formation.
 */
public final class Scutum {
    private Scutum() {
    }

    public static final TagKey<DamageType> QUENCHES =
            TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("rallyguard", "scutum_quenches"));
    private static final ResourceLocation FROZEN = ResourceLocation.fromNamespaceAndPath("iceandfire", "frozen");
    /** Bearer -> game time of the blocked breath. */
    private static final Map<UUID, Long> QUENCH = new HashMap<>();

    /** Any tier of scutum. */
    public static boolean is(ItemStack stack) {
        return stack.getItem() instanceof net.hfstack.rallyguard.item.ScutumItem;
    }

    /** Is {@code entity} blocking with a scutum right now? */
    public static boolean blockingWith(LivingEntity entity) {
        return entity.isBlocking() && is(entity.getUseItem());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onShieldBlock(LivingShieldBlockEvent event) {
        LivingEntity e = event.getEntity();
        if (e.level().isClientSide() || !event.getBlocked() || !blockingWith(e)) return;
        if (!event.getDamageSource().is(QUENCHES)) return;
        if (QUENCH.size() > 256) QUENCH.clear();
        QUENCH.put(e.getUUID(), e.level().getGameTime());
        net.hfstack.rallyguard.debug.SimProbe.count(e, "act:scutumQuench");
    }

    /** Before the bearer's own tick, so the burn set right after the blocked hit never deals its first damage. */
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (QUENCH.isEmpty() || !(event.getEntity() instanceof LivingEntity e) || e.level().isClientSide()) return;
        Long at = QUENCH.get(e.getUUID());
        if (at == null) return;
        if (e.level().getGameTime() - at > 2L) {
            QUENCH.remove(e.getUUID());
            return;
        }
        e.clearFire();
        Optional<Holder.Reference<MobEffect>> frozen = BuiltInRegistries.MOB_EFFECT.getHolder(FROZEN);
        frozen.ifPresent(e::removeEffect);
    }
}
