package net.hfstack.rallyguard.mixin.compat;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;

import java.util.List;
import java.util.Set;

/**
 * E2A B7: decides at load time whether the optional MCA Crime mixins apply - only if
 * {@code GuardChallengeService} exists AND its {@code challenge(ServerLevel, LivingEntity,
 * ServerPlayer)} has the return type the mixin was written for (void or boolean). Its
 * signature comes from {@code docs/API_MODS.md} (read with javap by Cowork) except the
 * return type, which isn't recorded there - hence two variants and this check, so a
 * different MCA Crime version just leaves the mixin out instead of crashing the game.
 */
public final class CompatMixinPlugin implements IMixinConfigPlugin {

    private static final String CHALLENGE_ARGS =
            "(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/server/level/ServerPlayer;)";

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.endsWith("GuardFactionMixin")) return hasBlacklistCheck(targetClassName);
        String ret = challengeReturnType(targetClassName);
        if (mixinClassName.endsWith("CrimeChallengeVoidMixin")) return "V".equals(ret);
        if (mixinClassName.endsWith("CrimeChallengeBoolMixin")) return "Z".equals(ret);
        return false;
    }

    /**
     * E3A F1: true when both {@code setTarget} and {@code canAttack(LivingEntity)} of Guard
     * Villagers' Guard still contain a {@code List.contains} call (the blacklist check the
     * faction mixin redirects); otherwise the mixin is skipped and a warning is logged.
     */
    private static boolean hasBlacklistCheck(String targetClassName) {
        try {
            ClassNode node = MixinService.getService().getBytecodeProvider().getClassNode(targetClassName);
            boolean set = false, can = false;
            for (MethodNode m : node.methods) {
                int contains = 0; // E3B R9 (B13): EXACTLY one blacklist check per method, or the mixin is skipped
                for (org.objectweb.asm.tree.AbstractInsnNode insn : m.instructions) {
                    if (insn instanceof org.objectweb.asm.tree.MethodInsnNode call && call.owner.equals("java/util/List")
                            && call.name.equals("contains") && call.desc.equals("(Ljava/lang/Object;)Z")) {
                        contains++;
                    }
                }
                if (m.name.equals("setTarget") && m.desc.equals("(Lnet/minecraft/world/entity/LivingEntity;)V")) set |= contains == 1;
                if (m.name.equals("canAttack") && m.desc.equals("(Lnet/minecraft/world/entity/LivingEntity;)Z")) can |= contains == 1;
            }
            if (!(set && can)) {
                org.slf4j.LoggerFactory.getLogger("rallyguard").warn("[rallyguard] Guard Villagers' blacklist check "
                        + "not found as expected; enemy-faction guards may not fight (remove guardvillagers:guard from its Mob Blacklist)");
            }
            return set && can;
        } catch (Throwable t) {
            return false;
        }
    }

    /** "V", "Z", another descriptor, or null if the class/method can't be found. */
    private static String challengeReturnType(String targetClassName) {
        try {
            ClassNode node = MixinService.getService().getBytecodeProvider().getClassNode(targetClassName);
            for (MethodNode m : node.methods) {
                if (m.name.equals("challenge") && m.desc.startsWith(CHALLENGE_ARGS)) {
                    return m.desc.substring(CHALLENGE_ARGS.length());
                }
            }
        } catch (Throwable ignored) {
            // MCA Crime not installed (or unreadable): no mixin.
        }
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
