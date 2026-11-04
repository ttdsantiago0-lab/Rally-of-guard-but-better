package net.hfstack.rallyguard.mixin;

import net.hfstack.rallyguard.combat.FieldManual;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * Field manual, creepers (David 2026-11-04, docs/MANUAL_DE_CAMPO.md): a player's shooters and shield-bearers may
 * attack creepers. Guard Villagers keeps {@code minecraft:creeper} in its config {@code MobBlackList}, checked inside
 * {@code Guard#setTarget} and {@code Guard#canAttack} with ONE {@code List.contains} each (read from the 2.4.11 bytecode);
 * changing the config would let every guard in the world (swordsmen too, which the tournament showed is worse) chase
 * them. No API reaches a check inside another mod's method, so this narrow redirect: only that contains call, only
 * for the creeper id, only for guards {@link FieldManual#mayTargetCreeper} allows. Everything else is unchanged.
 */
@Mixin(Guard.class)
public abstract class GuardFieldManualMixin {

    @Redirect(method = {"setTarget", "canAttack"},
            at = @At(value = "INVOKE", target = "Ljava/util/List;contains(Ljava/lang/Object;)Z"), require = 0)
    private boolean rallyguard$creepersForShootersAndShields(List<?> blackList, Object id) {
        if ("minecraft:creeper".equals(id) && FieldManual.mayTargetCreeper((Guard) (Object) this)) return false;
        return blackList.contains(id);
    }
}
