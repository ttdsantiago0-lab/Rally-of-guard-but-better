package net.hfstack.rallyguard.contract;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.UUID;

/**
 * Guard ownership facade used by the rest of Rally of the Guard (kept as its own class,
 * matching the Forge 1.20.1 architecture, so later stages can keep calling
 * {@code GuardOwnership.xxx(...)} unchanged).
 * <p>
 * The Forge 1.20.1 implementation manipulated ownership by serializing the whole guard
 * entity to NBT ({@code saveWithoutId}/{@code load}) and reading/writing the flat
 * "Owner"/"rallyguard:Owner" tags by hand. Per PORTING_PLAN.md section 4 (confirmed in
 * section 4.2 against Guard Villagers' real source, and section 12.6 against the actual
 * compiled {@code guardvillagers-2.4.11-1.21.1.jar} binary), that round-trip is replaced
 * here with Guard Villagers' own verified public typed API
 * ({@code getOwnerId()}/{@code setOwnerId(UUID)}) on
 * {@code tallestegg.guardvillagers.common.entities.Guard}. This is not just an internal
 * implementation swap - it changes persistence to go through
 * {@code SynchedEntityData}, which live-syncs the change to tracking clients (see section
 * 4.2 point 3), unlike the old {@code Entity.load()} round-trip.
 * <p>
 * The redundant {@code rallyguard:Owner} NBT key the Forge port also wrote is dropped -
 * Guard Villagers' own "Owner" persistence is the single source of truth now, so a second
 * copy under a different key serves no purpose.
 * <p>
 * "Is this entity a Guard?" is answered with {@code instanceof Guard} rather than the
 * Forge version's registry lookup by {@code ResourceLocation("guardvillagers", "guard")} -
 * behaviorally identical (an entity is the Guard Villagers guard type if and only if it is
 * an instance of this class), and simpler now that the real class is a verified compile
 * dependency instead of something only reachable by registry string lookup.
 */
public final class GuardOwnership {
    private GuardOwnership() {
    }

    /** The gold name color every freshly-hired guard gets (see {@link #applyGoldName}) - exposed so Phase 3A.1's squad name coloring can restore it when a guard is cleared from a squad, instead of leaving it uncolored. */
    public static final int GOLD_NAME_COLOR = 0xFFD700;

    /**
     * Returns true if the entity is a Guard Villagers guard.
     */
    public static boolean isGuard(Entity e) {
        return e instanceof Guard;
    }

    /**
     * Reads the owner via Guard Villagers' typed API. Returns null if the entity isn't a
     * Guard, or has no owner set.
     */
    public static UUID getOwner(Entity guard) {
        if (!(guard instanceof Guard g)) return null;
        return g.getOwnerId();
    }

    public static boolean hasOwner(Entity guard) {
        return getOwner(guard) != null;
    }

    public static boolean isOwnedBy(Entity guard, UUID player) {
        UUID owner = getOwner(guard);
        return owner != null && owner.equals(player);
    }

    /**
     * Sets the owner via Guard Villagers' typed {@code setOwnerId(UUID)}; applies the same
     * gold custom name and chat message the Forge port used.
     */
    public static void setOwner(Entity guard, ServerPlayer player) {
        if (!(guard instanceof Guard g)) return;

        g.setOwnerId(player.getUUID());

        applyGoldName(g);

        String display = g.getName().getString();
        player.sendSystemMessage(Component.translatable("message.rallyguard.guard_presenting", display));
    }

    private static void applyGoldName(Entity guard) {
        if (!(guard instanceof LivingEntity le)) return;

        String base = le.getName().getString();
        Component golden = Component.literal(base).withStyle(s -> s.withColor(GOLD_NAME_COLOR));
        le.setCustomName(golden);
        le.setCustomNameVisible(true);
    }

    /**
     * Phase 3B: this guard's current mount, as a plain display string ("" = not
     * mounted). Deliberately reuses {@code Entity#getName()} (already verified/used in
     * this project for a guard's OWN name - see {@link #applyGoldName}) rather than
     * introducing a new {@code EntityType#getDescription()} call, so no new vanilla API
     * needs verifying just for this. {@code Entity#getVehicle()} is this method's one
     * genuinely new API - confirmed real on 1.21.1 via a version-pinned NeoForge javadoc
     * mirror before first use here (mappings.dev's own page for {@code Entity} was
     * truncated and didn't show it, so a second, more literal source was used instead,
     * per this project's two-source rule for a first-time-used API).
     */
    public static String describeMount(Entity guard) {
        Entity vehicle = guard.getVehicle();
        return vehicle == null ? "" : vehicle.getName().getString();
    }

    private static final EquipmentSlot[] ARMOR_SLOTS_IN_WIRE_ORDER = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    /**
     * Phase 3B: this guard's armor condition as a compact, unlocalized wire string -
     * see {@link GuardRoster.Entry#armor} for the exact format. {@code
     * LivingEntity#getItemBySlot(EquipmentSlot)}, {@code ItemStack#isDamageableItem()}/
     * {@code getDamageValue()}/{@code getMaxDamage()} are all first-time uses in this
     * project - confirmed real on 1.21.1 via mappings.dev ({@code ItemStack}'s four
     * methods, {@code EquipmentSlot}'s enum constants) plus a second, independent
     * javadoc mirror for {@code getItemBySlot} specifically (mappings.dev's own
     * {@code LivingEntity} page was truncated and didn't show it), per this project's
     * two-source rule.
     */
    public static String describeArmor(LivingEntity guard) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ARMOR_SLOTS_IN_WIRE_ORDER.length; i++) {
            if (i > 0) sb.append(',');
            ItemStack stack = guard.getItemBySlot(ARMOR_SLOTS_IN_WIRE_ORDER[i]);
            if (stack.isEmpty()) {
                sb.append('-');
            } else if (!stack.isDamageableItem()) {
                sb.append(100);
            } else {
                int percent = 100 - (stack.getDamageValue() * 100 / Math.max(1, stack.getMaxDamage()));
                sb.append(Math.max(0, Math.min(100, percent)));
            }
        }
        return sb.toString();
    }
}
