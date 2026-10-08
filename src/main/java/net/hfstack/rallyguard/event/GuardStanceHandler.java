package net.hfstack.rallyguard.event;

import net.hfstack.rallyguard.combat.Ammo;
import net.hfstack.rallyguard.combat.FriendIndex;
import net.hfstack.rallyguard.combat.GuardPack;
import net.hfstack.rallyguard.contract.HqRegistry;
import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.debug.Perf;
import net.hfstack.rallyguard.combat.RangedFire;
import net.hfstack.rallyguard.compat.GunAmmo;
import net.hfstack.rallyguard.compat.ScorchedGunsCompat;
import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.contract.Stance;
import net.hfstack.rallyguard.contract.StanceRegistry;
import net.hfstack.rallyguard.order.CommandTargeting;
import net.hfstack.rallyguard.order.CommanderMarks;
import net.hfstack.rallyguard.order.CommanderSelection;
import net.hfstack.rallyguard.order.Engagement;
import net.hfstack.rallyguard.order.MountDriver;
import net.hfstack.rallyguard.order.GuardPosts;
import net.hfstack.rallyguard.order.GuardModes;
import net.hfstack.rallyguard.order.KamikazeOrder;
import net.hfstack.rallyguard.order.WaterSafety;
import net.hfstack.rallyguard.order.HqLogistics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The owned guard's combat gate: stance, fire discipline, designated target and post
 * leash, all decided in ONE place.
 * <p>
 * <b>Phase 3E ("Commander's Intent") - why this handler changed shape.</b>
 * {@code LivingChangeTargetEvent} fires for every {@code Mob#setTarget} call, including
 * {@code setTarget(null)} (verified in NeoForge's own {@code Mob.java.patch}, 1.21.x
 * branch). The Phase 3C gate cancelled those null changes too: a DEFENSIVE guard (the
 * default for every owned guard) or a PASSIVE one could therefore never DROP a target -
 * not when it died, not when Guard Villagers' own target goals stopped, not when Hold,
 * Patrol, the Rally scroll or the Passive switch cleared it. A guard that fought once
 * kept a stale (even dead) target, which in turn blocked Guard Villagers' walk-back-to-
 * post and teleport-to-owner (both require {@code getTarget() == null}). The gate now:
 * <ol>
 *     <li>always allows disengaging ({@code null});</li>
 *     <li>always allows the commander's explicit orders ({@link CommandTargeting#issue});</li>
 *     <li>refuses everything while the guard is under HOLD FIRE;</li>
 *     <li>refuses targets outside a posted guard's leash ({@link
 *     CommandTargeting#POST_LEASH}) - a gate squad stays at the gate;</li>
 *     <li>always allows the guard's designated target (commander intent beats
 *     posture);</li>
 *     <li>then applies the stance rules below, unchanged.</li>
 * </ol>
 * The once-a-second scan keeps the standing instructions true between events: drops
 * dead targets and targets that lead a posted guard past its leash, re-acquires the
 * designated target when it comes back into reach, answers other commanders' guards that
 * are fighting us (Guard Villagers never lets guards retaliate against guards), and runs
 * the AGGRESSIVE/RUTHLESS scan. It also remembers who last hurt a commander's side, for
 * the Ledger's "Counterattack" target option.
 * <p>
 * Phase 3C: enforces the non-default {@link Stance} values for owned guards.
 * <p>
 * {@link Stance#DEFENSIVE} was ORIGINALLY assumed to need no code at all - the theory
 * being that Guard Villagers' own out-of-the-box AI already only reacts when hit. Real
 * in-game testing (2026-09-15) disproved that: {@code Guard#registerGoals()} always
 * registers {@code HeroHurtTargetGoal} (join whatever the OWNER is currently fighting,
 * unconditional on the guard itself ever being hit or on {@code isFollowing()}) and
 * several always-on {@code NearestAttackableTargetGoal}s for raid mobs (Ravager/Witch/
 * Raider/Zombie, confirmed by reading {@code Guard.java}'s real source directly) - so an
 * untouched Defensive guard looks exactly like Aggressive the moment its owner fights
 * anything (most visible during a Rally, since that's when the owner is usually
 * fighting near the group). Fixed the same way {@link Stance#PASSIVE} already works -
 * intercepting {@code LivingChangeTargetEvent} - but more narrowly: DEFENSIVE cancels
 * the change UNLESS the new target is literally {@code guard.getLastHurtByMob()}, i.e.
 * the entity that just damaged the GUARD ITSELF. This matches the user's own definition
 * verbatim ("solo atacan si los atacan" - only attack if attacked) and correctly lets
 * through genuine retaliation ({@code HurtByTargetGoal}/{@code HeroHurtByTargetGoal},
 * which set target from the guard's own attacker) while blocking the owner-fight-join,
 * raid-mob-scan, and (as a known, accepted simplification) delayed vanilla-anger-based
 * targeting if a different attacker intervened in between. A conscious side effect: this
 * also means a Defensive guard will no longer join Guard Villagers' own raid-defense
 * goal ({@code DefendVillageGuardGoal}) unless it personally gets hit during the raid -
 * accepted as the literal, correct reading of "only attack if attacked," not treated as
 * a regression.
 * <p>
 * {@code LivingChangeTargetEvent} confirmed real on this exact NeoForge/MC 1.21.1
 * version via TWO independent sources, per this project's established rule for a
 * first-time-used API: nekoyue's ForgeJavaDocs-NG mirror (pinned to
 * {@code 1.21.x-neoforge}) and the real NeoForge source at
 * {@code github.com/neoforged/NeoForge} on the {@code 1.21.x} branch. The two sources
 * actually DISAGREED on the method names - the javadoc mirror said
 * {@code getNewTarget()}/{@code setNewTarget()}/{@code getOriginalTarget()}, while the
 * raw source (more literal, and matching a known javadoc-accuracy issue reported against
 * this exact class - {@code neoforged/NeoForge#1996}) gives the real names:
 * {@code getNewAboutToBeSetTarget()}/{@code setNewAboutToBeSetTarget(LivingEntity)}/
 * {@code getOriginalAboutToBeSetTarget()}. The raw source wins per this project's
 * established two-source-conflict resolution (trust the more literal source - see the
 * {@code EditBox.setHint} lesson in PORTING_PLAN.md). The class extends
 * {@code LivingEvent} (so {@code getEntity()} is inherited) and implements
 * {@code ICancellableEvent}, same shape already proven working in this project for
 * {@code LivingIncomingDamageEvent} ({@code GuardFallDamageHandler}) - {@code
 * event.setCanceled(true)} blocks the target change entirely.
 * <p>
 * {@link Stance#PASSIVE} cancels every target-change attempt for the guard - this is
 * deliberately broader than "don't retaliate when hit": it also blocks
 * {@code HeroHurtTargetGoal} (joining the owner's fight), {@code DefendVillageGuardGoal},
 * and the vanilla anger-at-a-player goal, since "won't fight back" was the user's own
 * framing, not "won't retaliate specifically."
 * <p>
 * {@link Stance#AGGRESSIVE} and {@link Stance#RUTHLESS} both need a guard to acquire a
 * target it was never given by being hit or by its owner's fight - something none of
 * Guard Villagers' own {@code targetSelector} goals do without the global
 * {@code AttackAllMobs} config flag (which is not per-guard, and this project needs a
 * per-guard stance). Since {@code Mob#targetSelector} is a protected field (not
 * reachable from outside the class without extending it, and this project deliberately
 * avoids mixins - PORTING_PLAN.md section 4.2), a real AI Goal can't be added to an
 * existing {@code Guard} instance from here. Instead, {@link #onLevelTick} periodically
 * (once a second, not every tick - see {@link #SCAN_INTERVAL_TICKS}) scans for a nearby
 * valid target and calls {@code guard.setTarget(...)} directly when the guard currently
 * has none: this is the EXACT SAME mechanism Guard Villagers' own
 * {@code HurtByTargetGoal}/{@code HeroHurtTargetGoal} use to hand a guard a target, so
 * the existing, unmodified attack-execution goals ({@code GuardMeleeGoal},
 * {@code GuardBowAttack}, the crossbow goal) pick it up on their own very next
 * {@code canUse()} check with zero new combat code needed.
 * <p>
 * {@link Stance#AGGRESSIVE} engages nearby hostile mobs (and other commanders' guards that
 * are fighting our side); {@link Stance#RUTHLESS} ("modo asesinos") engages any other
 * living thing except players, the owner's own guards and pets.
 * <p>
 * <b>Correction (Phase 3E, from Guard Villagers' real source):</b> the Phase 3C notes
 * claimed RUTHLESS bypasses the {@code MobBlackList} (Creepers etc.) because a directly
 * set target skips {@code Guard#canAttack}. It does not: Guard Villagers overrides
 * {@code Guard#setTarget} itself and silently returns for blacklisted mobs (also for
 * team-allied entities, Hero of the Village players, the owner and the owner's pets), so
 * no outside code can hand a guard those targets without a mixin. Worse, the old scan
 * kept choosing the blacklisted mob as "nearest" every second and so ignored everything
 * else around it. Candidates are now filtered with {@link CommandTargeting#wouldAccept},
 * which mirrors that override exactly. Whether RUTHLESS should be able to hit
 * blacklisted mobs at all is a design decision left to the user (it needs a mixin, which
 * this project avoids).
 */
public final class GuardStanceHandler {

    private static final double AGGRO_RADIUS = 16.0D;

    /** Once a second (20 ticks), not every tick - see the class javadoc. */
    private static final int SCAN_INTERVAL_TICKS = 20;

    @SubscribeEvent
    public void onChangeTarget(LivingChangeTargetEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Guard guard)) return;
        long t0 = Perf.t0();
        try {
            gate(event, guard);
        } finally {
            Perf.add(Perf.Sys.COMBAT_GATE, t0);
        }
    }

    private void gate(LivingChangeTargetEvent event, Guard guard) {
        if (!GuardOwnership.hasOwner(guard)) return;
        if (!(guard.level() instanceof ServerLevel level)) return;

        LivingEntity newTarget = event.getNewAboutToBeSetTarget();
        if (newTarget == null) return;                    // 1. disengaging is always allowed
        if (GuardPack.isOutOfAmmo(guard)) {               // E2A B2: an empty ranged weapon doesn't fight
            event.setCanceled(true);
            return;
        }
        if (CommandTargeting.isIssuing()) return;         // 2. the commander's explicit order
        if (net.hfstack.rallyguard.tactics.Tactics.isRetreating(guard) && newTarget != guard.getLastHurtByMob()) {
            event.setCanceled(true);                      // E2A: falling back (self-defence still allowed)
            return;
        }
        if (KamikazeOrder.isRunning(guard)) {             // a kamikaze on its run never fights
            event.setCanceled(true);
            return;
        }

        DirectiveRegistry.Directive directive = DirectiveRegistry.get(level).directiveOf(guard.getUUID());
        if (directive.holdFire()) {                       // 3. hold fire
            event.setCanceled(true);
            return;
        }

        BlockPos post = CommandTargeting.postOf(guard);   // 4. post leash (weapon-aware since E1)
        double leash = CommandTargeting.leashFor(guard);
        if (post != null && newTarget.distanceToSqr(Vec3.atCenterOf(post)) > leash * leash
                && !net.hfstack.rallyguard.veterancy.Veterancy.isReckless(guard)) { // E2A: reckless ignores the leash
            event.setCanceled(true);
            return;
        }
        if (newTarget.getUUID().equals(directive.target())) return; // 5. designated target

        Stance stance = StanceRegistry.get(level).stanceOf(guard.getUUID());
        if (stance == Stance.PASSIVE) {
            // E3A F5 (David: "Pasiva debe intentar defenderse"): never takes the initiative,
            // but answers whoever just hit it.
            if (newTarget != guard.getLastHurtByMob()) event.setCanceled(true);
            return;
        }

        if (stance == Stance.DEFENSIVE) {
            // Only let a target through if it's the entity that just hurt this guard
            // directly - see the class javadoc for why this, and not getTargetType(),
            // is how retaliation-vs-proactive-join actually gets distinguished (the
            // event's target type is MOB_TARGET for every Guard Villagers goal alike,
            // it does not identify which goal fired).
            if (newTarget != guard.getLastHurtByMob()) {
                event.setCanceled(true);
                return;
            }
        }

        // 6. Phase 3H.1 engagement cap: no more melee guards on one target than fit
        // around it (see Engagement). Never applied to self-defense.
        if (newTarget != guard.getLastHurtByMob() && !Engagement.tryEngage(guard, newTarget)) {
            event.setCanceled(true);
        }
    }

    /** Remembers who last hurt a commander or one of their guards, for the Ledger's
     *  Counterattack target option. Read-only - never changes the damage. */
    @SubscribeEvent
    public void onIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity victim = event.getEntity();
        if (!(victim.level() instanceof ServerLevel level)) return;
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker) || attacker == victim) return;

        UUID commander;
        if (victim instanceof ServerPlayer player) {
            commander = player.getUUID();
        } else if (victim instanceof Guard guard && GuardOwnership.hasOwner(guard)) {
            commander = GuardOwnership.getOwner(guard);
        } else {
            return;
        }
        if (attacker.getUUID().equals(commander) || GuardOwnership.isOwnedBy(attacker, commander)) return;

        CommanderMarks.recordAggressor(commander, attacker, level.getGameTime());
    }

    /**
     * E2A B10 (FEEDBACK_E1: single spikes of 28 ms in combat_gate and others with 200
     * guards): every registry is SavedData loaded from disk on its FIRST {@code get}, which
     * used to happen inside the first combat event or tick that needed it. They're loaded
     * here once, before any guard ticks.
     */
    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        ServerLevel overworld = event.getServer().overworld();
        StanceRegistry.get(overworld);
        DirectiveRegistry.get(overworld);
        net.hfstack.rallyguard.contract.SquadRegistry.get(overworld);
        net.hfstack.rallyguard.contract.GuardRoster.get(overworld);
        net.hfstack.rallyguard.contract.FormationRegistry.get(overworld);
        net.hfstack.rallyguard.contract.PackRegistry.get(overworld);
        HqRegistry.get(overworld);
        net.hfstack.rallyguard.contract.VeterancyRegistry.get(overworld);
        net.hfstack.rallyguard.contract.ForceRegistry.get(overworld);
    }

    @SubscribeEvent
    public void onServerStopped(ServerStoppedEvent event) {
        CommanderMarks.clearAll();
        KamikazeOrder.clearAll();
        GuardModes.clearAll();
        CommanderSelection.clearAll();
        Engagement.clearAll();
        FriendIndex.clearAll();
        RangedFire.clearAll();
        Ammo.clearAll();
        GuardPack.clearAll();
        HqLogistics.clearAll();
        net.hfstack.rallyguard.command.SlotQueue.clearAll();
        net.hfstack.rallyguard.command.SquadDirectives.clearAll();
        net.hfstack.rallyguard.command.CaptainSuccession.clearAll();
        net.hfstack.rallyguard.veterancy.VeterancyEvents.clearAll();
        net.hfstack.rallyguard.tactics.Tactics.clearAll();
        net.hfstack.rallyguard.force.Leadership.clearAll();
        net.hfstack.rallyguard.force.ForceBrain.clearAll();
        net.hfstack.rallyguard.order.FormationFollow.clearAll();
        HqRegistry.clearSelections();
        SLICES.clear();
    }

    @SubscribeEvent
    public void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        long tk = Perf.t0();
        try {
            KamikazeOrder.tick(level); // every tick, but only touches the few active runs
        } catch (RuntimeException e) {
            // A modded explosive misbehaving must not crash the world (see SafeTick).
            KamikazeOrder.clearAll();
            com.mojang.logging.LogUtils.getLogger().warn("[rallyguard] Kamikaze runs cleared after an error", e);
        }
        Perf.add(Perf.Sys.KAMIKAZE, tk);
        if (level.getGameTime() % 1200L == 0L) net.hfstack.rallyguard.tactics.Tactics.purge(level.getGameTime());
        long tq = Perf.t0();
        net.hfstack.rallyguard.command.SlotQueue.drain(level); // E2A: formation slots, a few per tick
        net.hfstack.rallyguard.command.CaptainSuccession.tick(level); // E3A A1: delayed reliefs
        net.hfstack.rallyguard.force.ForceBrain.tick(level); // E3A A2: forces, ≤ 1 per second each, staggered
        net.hfstack.rallyguard.order.FormationFollow.tick(level); // E3A A5: follow in formation
        Perf.add(Perf.Sys.SLOTS, tq);
        if (level.getGameTime() % CommanderSelection.MARKER_INTERVAL_TICKS == 0) {
            long tm = Perf.t0();
            try {
                CommanderSelection.showMarkers(level);
            } catch (RuntimeException e) {
                com.mojang.logging.LogUtils.getLogger().warn("[rallyguard] Selection markers skipped after an error", e);
            }
            Perf.add(Perf.Sys.MARKERS, tm);
        }
        // Phase 3H.1 scale: the guard list is collected once a second, and each tick
        // handles 1/20 of it - every guard is still looked at once a second, but a big
        // army no longer lands its whole cost on one tick (no once-a-second lag spike).
        int phase = (int) (level.getGameTime() % SCAN_INTERVAL_TICKS);
        if (phase == 0) {
            List<Guard> collected = new ArrayList<>(level.getEntities(EntityTypeTest.forClass(Guard.class),
                    GuardOwnership::hasOwner));
            SLICES.put(level.dimension(), collected);
            long te = Perf.t0();
            Engagement.rebuild(level.dimension(), collected);
            Perf.add(Perf.Sys.ENGAGEMENT, te);
            long tf = Perf.t0();
            FriendIndex.rebuild(level.dimension(), collected);
            Perf.add(Perf.Sys.FRIEND_INDEX, tf);
        }
        List<Guard> guards = SLICES.get(level.dimension());
        if (guards == null || guards.isEmpty()) return;

        long ts = Perf.t0();
        StanceRegistry stances = StanceRegistry.get(level);
        DirectiveRegistry directives = DirectiveRegistry.get(level);
        // E1-E: never more than maxGuardsProcessedPerTick in one tick (the rest of this
        // slice waits for the next second) - a hard ceiling on Rally's per-tick cost.
        int budget = RallyConfig.i(RallyConfig.MAX_GUARDS_PER_TICK, 200);
        for (int i = phase; i < guards.size() && budget > 0; i += SCAN_INTERVAL_TICKS) {
            Guard guard = guards.get(i);
            if (guard.isRemoved() || !guard.isAlive()) continue;
            budget--;
            SafeTick.run(guard, () -> {
                GuardModes.normalize(guard);
                net.hfstack.rallyguard.force.UnitOrders.applyIfNewer(level, guard); // E3A A2: versioned unit directive
                WaterSafety.tick(level, guard); // E2A B1: no more drowning on posts / horses
                net.hfstack.rallyguard.veterancy.VeterancyEvents.second(level, guard); // E2A: fights, totems, history
                HqLogistics.tick(level, guard);  // E2A B6: doors, park / remount at the HQ
                // E1-A: war pack - consumables, second weapon, off-hand ammo, and (every
                // 5 s, idle) restocking from a nearby container.
                long tp = Perf.t0();
                GuardPack.tick(level, guard, (level.getGameTime() / SCAN_INTERVAL_TICKS + guard.getId()) % 5 == 0);
                Perf.add(Perf.Sys.PACK, tp);
                maintain(level, guard, stances.stanceOf(guard.getUUID()), directives.directiveOf(guard.getUUID()));
                // E2A block 4: the decision ladder, after the existing upkeep.
                long tl = Perf.t0();
                net.hfstack.rallyguard.tactics.Tactics.step(level, guard, directives.directiveOf(guard.getUUID()));
                Perf.add(Perf.Sys.LADDER, tl);
            });
        }
        Perf.add(Perf.Sys.STANCE_LOOP, ts);
    }

    /** The owned guards collected at the start of the current second, per dimension -
     *  shared with the mount driver so nothing else scans the entity list. */
    private static final Map<ResourceKey<Level>, List<Guard>> SLICES = new HashMap<>();

    public static List<Guard> loaded(ResourceKey<Level> dimension) {
        return SLICES.getOrDefault(dimension, List.of());
    }

    private void maintain(ServerLevel level, Guard guard, Stance stance, DirectiveRegistry.Directive directive) {
        if (KamikazeOrder.isRunning(guard)) return; // KamikazeOrder steers it alone
        LivingEntity current = guard.getTarget();
        if (GuardPack.isOutOfAmmo(guard)) { // E2A B2: falling back to restock
            if (current != null) CommandTargeting.disengage(guard);
            return;
        }

        // Phase 3H DEFEND: stay beside the ward (even under hold fire - that only stops
        // the shooting) and answer whatever threatens it before anything else.
        LivingEntity threat = directive.ward() == null ? null : tendWard(level, guard, directive.ward());
        if (threat != null && !directive.holdFire()) {
            if (current != threat) CommandTargeting.issue(guard, threat);
            return;
        }

        // Hygiene: a dead target is never worth keeping (it blocks walk-back/teleport).
        if (current != null && !current.isAlive()) {
            CommandTargeting.disengage(guard);
            current = null;
        }

        if (directive.holdFire()) {
            if (current != null) CommandTargeting.disengage(guard);
            return;
        }

        // A posted guard never follows a fight past its leash - drop it, and Guard
        // Villagers' WalkBackToCheckPointGoal walks it home.
        BlockPos post = CommandTargeting.postOf(guard);
        double leash = CommandTargeting.leashFor(guard);
        if (current != null && post != null && current.distanceToSqr(Vec3.atCenterOf(post)) > leash * leash) {
            CommandTargeting.disengage(guard);
            current = null;
        }

        // The designated target comes first whenever it is within reach.
        if (directive.target() != null) {
            Entity entity = level.getEntity(directive.target());
            if (entity instanceof LivingEntity designated
                    && CommandTargeting.wouldAccept(guard, designated)
                    && CommandTargeting.inReach(guard, designated, CommandTargeting.PURSUIT_RANGE)) {
                if (Engagement.tryEngage(guard, designated)) {
                    if (current != designated) CommandTargeting.issue(guard, designated);
                } else {
                    // Phase 3H.1: the target is full - hold the screen ring around it and
                    // step in when a place frees up (next second).
                    if (current == designated) CommandTargeting.disengage(guard);
                    if (post == null) MountDriver.moveTo(guard, Engagement.screenSpot(guard, designated), 1.0D);
                }
                return;
            }
        }

        // Phase 3H.1: a target GV's own alarm handed to this guard that already has its
        // fill of melee guards is dropped (unless it's hitting this guard) - the stance
        // below then picks a free enemy instead, spreading the fight.
        if (current != null && current != guard.getLastHurtByMob() && !Engagement.tryEngage(guard, current)) {
            CommandTargeting.disengage(guard);
            current = null;
        }

        if (current != null) return; // already fighting something acceptable
        if (stance == Stance.PASSIVE) return;

        // Answer another commander's guard that is fighting us - Guard Villagers' own
        // HurtByTargetGoal ignores Guard attackers, so without this two armies would
        // never fight back at each other.
        LivingEntity hurtBy = guard.getLastHurtByMob();
        if (hurtBy != null && CommandTargeting.isHostileGuard(guard, hurtBy)
                && CommandTargeting.wouldAccept(guard, hurtBy)
                && CommandTargeting.inReach(guard, hurtBy, AGGRO_RADIUS)) {
            guard.setTarget(hurtBy); // it is getLastHurtByMob(), so even DEFENSIVE allows it
            return;
        }

        if (stance == Stance.AGGRESSIVE || stance == Stance.RUTHLESS) {
            LivingEntity candidate = findTarget(level, guard, stance);
            if (candidate != null) {
                guard.setTarget(candidate);
            }
        }
    }

    /** How far the ward may get from a defender's post before the post moves to it. */
    private static final double WARD_RANGE = 5.0D;
    private static final double WARD_THREAT_RADIUS = 16.0D;

    /**
     * Phase 3H DEFEND: keeps the guard posted next to its ward (re-posting as the ward
     * moves - the ward is the guard's moving post) and returns what threatens the ward
     * right now: whoever hurt it in the last 5 seconds (vanilla forgets
     * {@code getLastHurtByMob} after 100 ticks), else the nearest mob hunting it. A ward
     * that isn't loaded here (another dimension, logged out) leaves the guard where it
     * is; a dead ward is cleared by {@code GuardRosterCleanupHandler}.
     */
    private LivingEntity tendWard(ServerLevel level, Guard guard, UUID wardId) {
        if (!(level.getEntity(wardId) instanceof LivingEntity ward) || !ward.isAlive()) return null;
        // Two escorts guarding each other would walk in circles: the one that still
        // has this guard as ITS ward wins, this one stops escorting.
        DirectiveRegistry directives = DirectiveRegistry.get(level);
        if (guard.getUUID().equals(directives.directiveOf(wardId).ward())) {
            directives.setWard(guard.getUUID(), null, "");
            return null;
        }

        BlockPos post = CommandTargeting.postOf(guard);
        if (post == null || Vec3.atCenterOf(post).distanceToSqr(ward.position()) > WARD_RANGE * WARD_RANGE) {
            GuardModes.postKeepingWard(guard, GuardPosts.besideWard(guard, ward));
        }

        LivingEntity hurtBy = ward.getLastHurtByMob();
        if (hurtBy != null && hurtBy.isAlive() && CommandTargeting.wouldAccept(guard, hurtBy)) return hurtBy;

        List<Mob> hunters = level.getEntitiesOfClass(Mob.class, ward.getBoundingBox().inflate(WARD_THREAT_RADIUS),
                m -> m.getTarget() == ward && CommandTargeting.wouldAccept(guard, m));
        Mob nearest = null;
        for (Mob m : hunters) {
            if (nearest == null || m.distanceToSqr(ward) < nearest.distanceToSqr(ward)) nearest = m;
        }
        return nearest;
    }

    private LivingEntity findTarget(ServerLevel level, Guard guard, Stance stance) {
        // A small, guard-centred box (post-centred for a posted guard) - nowhere near the
        // "abnormally large AABB" size Sable aborts (see PORTING_PLAN.md section 17.1),
        // and far cheaper than the old whole-level scan per guard per second.
        BlockPos post = CommandTargeting.postOf(guard);
        // E2A B4: a free archer/gunner looks as far as its weapon (and rank) reaches.
        net.hfstack.rallyguard.combat.WeaponProfile weapon = net.hfstack.rallyguard.combat.WeaponProfile.forGuard(guard);
        double freeRadius = weapon == null ? AGGRO_RADIUS : Math.max(AGGRO_RADIUS, Math.min(weapon.maxRange(), 64.0D));
        double radius = post != null ? CommandTargeting.leashFor(guard) : freeRadius;
        AABB box = post != null ? new AABB(post).inflate(radius) : guard.getBoundingBox().inflate(radius);

        List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class, box,
                candidate -> isEligible(candidate, guard, stance)
                        && CommandTargeting.inReach(guard, candidate, freeRadius));

        LivingEntity nearest = null;
        double nearestDistSq = Double.MAX_VALUE;
        for (LivingEntity candidate : nearby) {
            double distSq = candidate.distanceToSqr(guard);
            if (distSq < nearestDistSq) {
                nearest = candidate;
                nearestDistSq = distSq;
            }
        }
        return nearest;
    }

    private boolean isEligible(LivingEntity candidate, Guard guard, Stance stance) {
        if (!CommandTargeting.wouldAccept(guard, candidate)) return false;
        if (Engagement.isSaturated(guard, candidate)) return false; // spread the fight

        if (stance == Stance.AGGRESSIVE) {
            return candidate instanceof Monster || CommandTargeting.isHostileGuard(guard, candidate);
        }

        // RUTHLESS: any other living entity - Players are still excluded as a deliberate
        // safety choice; fighting a player is the commander's explicit decision (Attack
        // with a player target), never something a stance starts on its own.
        return !(candidate instanceof Player);
    }
}
