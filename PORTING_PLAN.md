# PORTING_PLAN.md — Rally of the Guard: Forge 1.20.1 → NeoForge 1.21.1

Status: **Phase 0 complete (§4.2). Phase 1 complete and VERIFIED (§11.2). Phase 2 preflight complete (§12.6) — `guardvillagers-2.4.11-1.21.1.jar` in `libs/`, no drift found. Phase 2A complete and VERIFIED (§13). Phase 2B complete and VERIFIED (§14). Phase 2C (guard ownership/hiring) code complete, build-VERIFIED, and VALIDATED in-game by the user (§15.1). Phase 2D (Commander's Ledger networking) code complete and build-VERIFIED (§16). Phase 2E (Commander's Ledger item + GUI) code complete, build-VERIFIED, and VALIDATED in-game after the Sable-AABB fix (§17/§17.1) — mounts, bows, guard listing, and summon-while-unmounted all confirmed working by the user. Mounted-summon fix build-VERIFIED and deployed (§17.2), awaiting in-game retest. **Phase 2F (patrol), Phase 2G (Scroll of Rallying), and a minimal Phase 2I (followHero config patch) are all code complete, build-VERIFIED (one real Holder<MobEffect> compile error found and fixed along the way, §19), and deployed to the mods folder with bytecode spot-checks. NONE of 2F/2G/2I has been in-game tested yet — that's next.** Phase 2H (friendly-fire/neutrality) code complete, `BUILD SUCCESSFUL`, deployed, bytecode-verified, and VALIDATED in-game by the user (§20) - `RallyFriendlyFireHandler` and `HiredGuardsNeutralityHandler` both confirmed present with the correct NeoForge 1.21.1 event types in the actual jar the game loads, and the user confirmed both behaviors work correctly in-game (no friendly-fire damage to a following guard; hitting an unrelated guard does not turn the player's own guards hostile). **The entire original porting plan (Phase 0 through 2H) is now complete, built, deployed, and validated.** Phase 3A (squads + Ledger multi-select assignment) is code complete, NOT YET BUILT (§21).
Scope: private, single-player/small-modpack experimental build. Not for public release.

---

## 0. Sources inspected

| Repo | Loader / MC version | Commit inspected | Role in this port |
|---|---|---|---|
| `HyanFerreira/rally-of-the-guard-forge` | Forge 47.4.10 / **1.20.1** | `main` (shallow) | **Feature-of-record.** Defines the MVP feature set we must preserve. |
| `HyanFerreira/rally-of-the-guard` | Fabric 0.116 / **1.21.1** | `main` (shallow) | **Primary code donor.** Already targets our exact Minecraft version, so its vanilla-API usage (data components, `StreamCodec` networking, 1.21.1 GUI classes) is directly relevant. Fabric-specific glue is discarded; the game-logic bodies are close to what NeoForge needs. |
| `seymourimadeit/guardvillagers` (branch `1.21.1`) | **NeoForge 21.1.215 / 1.21.1** | `1.21.1` branch head | **The real dependency.** This is the actual upstream source of `tallestegg.guardvillagers` for our target version (`mod_version=2.4.12`, per its `gradle.properties`; the CurseForge listing shows a slightly newer `2.4.11`/`2.4.12` build — close enough that the branch head is a reliable stand-in, but see §9 caveat). MIT-licensed code. |

**Security finding — do not use:** a GitHub repo (`ShackleSailorTrek/guard-villagers`) surfaced high in search results claiming to host "GuardVillagers-Modern.jar" (Forge/Fabric/NeoForge 1.18–1.21+). Its jars are **not** Guard Villagers — `META-INF/mods.toml` inside identifies them as unrelated mods ("Lush Utils" / `rubypatch`, "Keen Utils", "SkylineMod"), with obfuscated single-file class names (`dev/pixelcraft/minilib/DnbnpwXhazc.class`), an embedded multi-megabyte binary disguised as a texture resource, and an "All Rights Reserved" license contradicting an open mirror. This has the shape of a trojanized mod jar. It was extracted read-only in this sandbox for inspection, never executed, and has been deleted from the workspace. **Do not download or run anything from that repository.** Get the real jar from the official CurseForge page (`curseforge.com/minecraft/mc-mods/guard-villagers`, file `guardvillagers-2.3.2-1.21.1.jar` or newer) or Modrinth, the same way the Forge port already does it (see §3).

---

## 1. Executive summary

- **Difficulty: Moderate.** The mod is small (~20–25 gameplay files) and self-contained. The hard parts are not the gameplay logic — they're the four loader-API surfaces that changed shape between Forge 1.20.1 and NeoForge 1.21.1: registries, networking, config, and events. All four are already solved for us in reference form, either by the Fabric 1.21.1 repo (same MC version, so its vanilla-API calls mostly transliterate 1:1) or by Guard Villagers' own NeoForge 1.21.1 source (which is a real, compiling example of every loader idiom this port needs).
- **Files requiring changes: ~24 new/ported Java files**, 2 resource configs (`neoforge.mods.toml`, optionally a mixins.json), plus the existing lang/model/texture/recipe assets which need **no changes** (item/recipe JSON and lang files are loader-agnostic and can be copied verbatim from either source repo).
- **Highest risk area, by far: the guard ownership/state model.** Both existing ports read and write guard state (`Owner`, `Following`, `Patrolling`, `PatrolPosX/Y/Z`) by serializing the whole entity to NBT (`saveWithoutId`/`writeNbt`) and reading/writing flat tags — a loose, reflection-flavored contract that never touches Guard Villagers' actual Java API. This was probably done to avoid a hard compile dependency. **On NeoForge 1.21.1 this input is provable to be unnecessary and worse:** the real `Guard` entity (`tallestegg.guardvillagers.common.entities.Guard`) exposes exactly the public methods we need — `getOwnerId()/setOwnerId(UUID)`, `isFollowing()/setFollowing(boolean)`, `isPatrolling()/setPatrolling(boolean)`, `getPatrolPos()/setPatrolPos(BlockPos)` — and still serializes to the *same* NBT keys (`Owner`, `Following`, `Patrolling`, `PatrolPosX/Y/Z`) under the hood, so nothing about the save format needs to be reverse-engineered. §4 recommends switching to the typed API, and this has now been verified against the actual `Guard.java` source and confirmed (§4.2) — adopted as the Phase 2C plan.
- **Dependencies: NeoForge 21.1.215 (MC 1.21.1), Java 21, Guard Villagers `guardvillagers-2.3.2-1.21.1.jar` (or newer 2.4.x) as a local, compile+runtime `implementation files(...)` dependency** — same pattern the Forge port already uses for its local jar.
- **MVP achievability: Yes, realistically achievable.** Every feature in the Forge MVP list has a direct, verified NeoForge 1.21.1 equivalent with no missing platform capability. No feature requires anything Guard Villagers 2.x doesn't expose.

---

## 2. Architecture

### 2.1 Project shape

Single-module Gradle project (no split client/main source sets — Forge/NeoForge don't need Fabric's `splitEnvironmentSourceSets`; client-only code goes in a `client` sub-package guarded by `@Mod(value = MODID, dist = Dist.CLIENT)` or `DistExecutor`-free `FMLClientSetupEvent`, matching the pattern the Forge port already uses).

```
rally-of-the-guard-neoforge/
├── build.gradle                      (NeoGradle userdev, mirrors GV's own build.gradle)
├── gradle.properties
├── settings.gradle
├── libs/guardvillagers-2.3.2-1.21.1.jar   (user-supplied, gitignored)
├── src/main/java/net/hfstack/rallyguard/
│   ├── RallyOfTheGuard.java                 (@Mod entry point, NeoForge constructor style)
│   ├── client/
│   │   ├── ModClient.java                   (FMLClientSetupEvent: register MenuScreens)
│   │   └── ClientHooks.java
│   ├── config/RallyConfig.java              (ModConfigSpec, or drop — see §6)
│   ├── contract/GuardOwnership.java         (rewritten against typed Guard API)
│   ├── effect/ (ModEffects.java, RallyCommanderEffect.java)
│   ├── event/ (InteractGuardHandler, RallyFriendlyFireHandler, HiredGuardsNeutralityHandler)
│   ├── item/ (ModItems.java, ScrollOfRallyingItem.java, CommandersLedgerItem.java)
│   ├── network/
│   │   ├── NetworkBootstrap.java            (RegisterPayloadHandlersEvent)
│   │   └── payload/ (GuardActionC2SPayload, GuardListS2CPayload, OpenGuardCommandC2SPayload — records + StreamCodec)
│   └── screen/ (ModMenus, HireGuardMenu, HireGuardScreen, GuardCommandScreen)
└── src/main/resources/
    ├── META-INF/neoforge.mods.toml
    ├── assets/rallyguard/...   (copy verbatim from either source repo — identical art/lang)
    └── data/rallyguard/recipes/...  (copy verbatim — recipe JSON is loader-agnostic)
```

No mixins are planned by default (see §6.4) — if Phase 2I proves the `GuardInventoryScreen` hook is still needed, add a single `rallyguard.mixins.json` declared via `[[mixins]]` in `neoforge.mods.toml`, exactly like Guard Villagers' own mixin config, which confirms this mechanism works on NeoForge 1.21.1.

### 2.2 Source-of-truth policy per subsystem

| Subsystem | Primary donor | Why |
|---|---|---|
| Feature list / behavior | Forge repo | It's the MVP spec (per user instructions) |
| Vanilla API calls (Component/Text, ItemStack, InteractionResult, BlockPos, etc.) | Fabric-1.21.1 repo | Same MC version as target — Yarn names differ from Mojmap but the *shape* of vanilla 1.21.1 API (data components, `use()` signatures, `InteractionResult`, `AABB`/`Box`) is what we need, and Fabric already made those breaking changes for us |
| Loader glue (registries, networking, events, config, menus) | Guard Villagers NeoForge 1.21.1 source | It's a real, compiling NeoForge 21.1.215 mod — every idiom in it is proven, not guessed |
| Guard entity contract (ownership, following, patrol) | Guard Villagers NeoForge 1.21.1 source (`Guard.java`) | Ground truth — see §4 |

---

## 3. Dependency plan

- **NeoForge:** `21.1.215` (matches `neo_version` in Guard Villagers' own `1.21.1` branch `gradle.properties` — using the exact version GV itself builds against removes a whole class of ABI-mismatch risk).
- **Minecraft:** `1.21.1`, Java **21** toolchain (`net.neoforged.gradle.userdev` plugin, version `7.0.165` per GV's build, or latest 7.x).
- **Guard Villagers:** not resolvable from a public Maven repo in this research sandbox (CurseForge/Modrinth API and CDN hosts are outside this environment's network allowlist — `api.modrinth.com` and `cdn.modrinth.com` both returned proxy-level 403s here). The Forge port already solves this the same way: a **local jar file dependency**. Plan:
  1. User downloads `guardvillagers-2.3.2-1.21.1.jar` (or current 2.4.x) from the **official** CurseForge page (`curseforge.com/minecraft/mc-mods/guard-villagers`, author `almightytallestred`) or Modrinth project `H1sntfo8`, and drops it in `libs/`.
  2. `build.gradle`: `implementation files("libs/${guardvillagers_jar}")` — compile + runtime, unlike the Forge port's `modImplementation` (that's a Forge-Loom-specific remapping helper; plain NeoForge doesn't need remapping since NeoForge ships in official Mojang mappings already, so a plain `files()`/`implementation` dependency is correct and simpler).
  3. This also gives us the *typed* `Guard` class at compile time (see §4), which the Forge/Fabric ports never had.
- No other third-party dependencies. No Architectury, no multi-loader plumbing — single NeoForge target only, per the user's explicit scope.

---

## 4. The ownership/state contract — verified Guard Villagers 1.21.1 API

This is the single most important finding of Phase 0, so it gets its own section.

`tallestegg.guardvillagers.common.entities.Guard` (NeoForge 1.21.1 branch) stores its state in `SynchedEntityData` accessors, **not** ad-hoc NBT:

```java
protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = ...;
private static final EntityDataAccessor<Optional<BlockPos>> GUARD_POS = ...;
private static final EntityDataAccessor<Boolean> PATROLLING = ...;
private static final EntityDataAccessor<Boolean> FOLLOWING = ...;
```

But it exposes clean public accessors, and its `addAdditionalSaveData`/`readAdditionalSaveData` still read/write the **same flat NBT tag names** the Forge/Fabric ports already assume:

| Concept | Public API (verified in source) | NBT tag on disk (verified in source) |
|---|---|---|
| Owner UUID | `UUID getOwnerId()` / `void setOwnerId(@Nullable UUID)` / `LivingEntity getOwner()` (returns `null` if the follow-hero gate isn't satisfied — see below) / `boolean isOwner(LivingEntity)` | `Owner` (UUID) |
| Following | `boolean isFollowing()` / `void setFollowing(boolean)` | `Following` (bool) |
| Patrolling | `boolean isPatrolling()` / `void setPatrolling(boolean)` | `Patrolling` (bool) |
| Patrol point | `BlockPos getPatrolPos()` / `void setPatrolPos(BlockPos)` | `PatrolPosX`/`PatrolPosY`/`PatrolPosZ` (int) |

**Decision: CONFIRMED.** Per your request, this was verified against the actual source (not assumed) and the direct typed-API approach is adopted as the implementation plan for Phase 2C. Full verification in §4.2 below. Summary of the recommendation: rewrite `GuardOwnership` (and the networking handlers that touch guard state) to cast to `tallestegg.guardvillagers.common.entities.Guard` and call its public methods directly, instead of the existing ports' pattern of `CompoundTag n = new CompoundTag(); guard.saveWithoutId(n); ...; guard.load(n);` on every read and write.

### 4.2 Verification (requested by user, answered against `Guard.java` source directly — `seymourimadeit/guardvillagers`, branch `1.21.1`, file `src/main/java/tallestegg/guardvillagers/common/entities/Guard.java`)

**1. Exact class and location.** `tallestegg.guardvillagers.common.entities.Guard` (`public class Guard extends PathfinderMob implements CrossbowAttackMob, RangedAttackMob, NeutralMob, ContainerListener, ReputationEventHandler`, line 92). All four methods (and their siblings) live directly on this class, not on an interface or superclass:
- `public UUID getOwnerId()` — line 383
- `public void setOwnerId(@Nullable UUID p_184754_1_)` — line 387
- `public boolean isFollowing()` — line 691
- `public void setFollowing(boolean following)` — line 695
- (also verified while there, since the plan uses them too: `public LivingEntity getOwner()` line 368, `public boolean isOwner(LivingEntity entityIn)` line 378, `public boolean isPatrolling()` line 833, `public void setPatrolling(boolean patrolling)` line 837, `public BlockPos getPatrolPos()` line 171, `public void setPatrolPos(BlockPos position)` line 175)

**2. Public and intended for external use?** Yes, on both counts, with direct evidence rather than inference:
- All ten methods are declared `public` on a `public class` — no package-private, no `@ApiStatus.Internal`, no `@Deprecated`, no restrictive Javadoc anywhere in the file (checked — there is none of that annotation family anywhere in `Guard.java`).
- The mod's **own** internal features are built on exactly this surface: `networking/GuardFollowPacket.java` (`guard.setFollowing(!guard.isFollowing()); guard.setOwnerId(player.getUUID());`), `HandlerEvents.java`, and `client/gui/GuardInventoryScreen.java` all call these same methods to implement Guard Villagers' own hire/follow UI — this is not a side door, it's the mod's primary internal contract for this data.
- The author also ships `ModCompat.java`, a dedicated compatibility-shim class written specifically so *other mods* (a musket mod, in that case) can interoperate with `Guard` through its public API — direct evidence the author designs this class's public surface to be used by outside code, not just internally.

**3. Does it preserve the same persisted NBT/data behavior as the Forge 1.20.1 version relies on?** Yes, confirmed by reading `addAdditionalSaveData`/`readAdditionalSaveData` in full (lines 205–241 and 278–312):
- `addAdditionalSaveData`: `compound.putBoolean("Following", this.isFollowing())`, `compound.putBoolean("Patrolling", this.isPatrolling())`, `compound.putUUID("Owner", this.getOwnerId())` (only if non-null), `compound.putInt("PatrolPosX/Y/Z", ...)` (only if a patrol pos is set) — the exact same four tag names (`Owner`, `Following`, `Patrolling`, `PatrolPosX/Y/Z`) both existing ports already read/write by hand.
- `readAdditionalSaveData` reads those same keys back through `setOwnerId`/`setFollowing`/`setPatrolling`/direct `entityData.set(GUARD_POS, ...)`. So the on-disk format is identical across all four inspected versions of this mod (1.6.18 Forge/1.20.1 through 2.4.12 NeoForge/1.21.1) — the internal storage moved from (presumably) plain fields to `SynchedEntityData`, but the serialized shape never changed.
- A genuine improvement, not just parity: `setOwnerId`/`setFollowing`/`setPatrolling`/`setPatrolPos` write through `SynchedEntityData.set(...)`, which is Minecraft's live client-sync mechanism — a change is automatically queued for the next tracker update to every client that has the entity loaded. The existing ports' `guard.load(n)` round-trip calls `readAdditionalSaveData`, which is designed for one-time deserialization at chunk/entity load, **not** as a live-sync path — it does not push the changed value to already-tracking clients. This is a plausible source of a real (if minor) client desync bug in the existing Forge/Fabric mods — e.g. a nearby player's client not immediately seeing a guard's follow icon/name-color update — that switching to the typed setters removes by construction. (Framed as a risk category inherent to calling `Entity.load()` repeatedly on a live entity, a well-documented modding anti-pattern — not as a confirmed observed bug, since nothing has been run yet.)

**4. Does any existing Forge functionality rely on behavior that would be lost?** No — and Phase 0 found something worth flagging in the other direction: **one existing behavior already appears to be a latent no-op**, not something the typed-API switch would newly break.
- Both the Forge and Fabric `RallyFriendlyFireHandler`/`ScrollOfRallyingItem` write a *custom*, Rally-of-the-Guard-only NBT key, `rallyguard:in_rally`, using the same `saveWithoutId()` → mutate → `load()` pattern, then read it back later (in a different method call, often ticks or seconds later — e.g. on the next attack event) via a fresh `saveWithoutId()`.
- Vanilla `Entity`/`Mob`/`PathfinderMob`/`Guard` NBT (de)serialization has no generic "preserve unrecognized tags" mechanism — only tags an entity's own `addAdditionalSaveData` chain explicitly writes come back out of a fresh `saveWithoutId()` call. Since `rallyguard:in_rally` (and the redundant `rallyguard:Owner`) are not part of Guard Villagers' own save schema, and Rally of the Guard doesn't subclass `Guard` to add them to it, a freshly-populated `saveWithoutId()` compound should **not** actually contain `rallyguard:in_rally` on any call after the one that set it — meaning the flag most likely reads back as `false` every time it's checked from a separate call, regardless of actual rally state. (This is exactly the class of problem NeoForge's Data Attachments and Forge's `getPersistentData()` exist to solve — bare custom NBT keys on someone else's entity do not persist on their own.)
- The Fabric port's own code comment on the `Following`-based check (`"fallback robusto: no rali você seta Following=true; fora, você seta false"` — "robust fallback: during rally you set Following=true, outside you set it false") reads like the author already suspected `in_rally` wasn't reliable and added the fallback for that reason. If so, `Following` (a real, persisted Guard Villagers field) is very likely already doing all the actual work for the friendly-fire-during-rally check in both shipped mods today.
- **Conclusion:** nothing about the MVP's actual observed behavior depends on `rallyguard:in_rally` persisting, because — on this reading of vanilla NBT semantics — it likely never did. The typed-API port should drop that dead key rather than reproduce it, and rely on `isFollowing()` (now read via the typed getter) as the single source of truth for "is this guard currently rallied," matching what the existing mods' fallback logic already does in practice. If a genuinely separate "in rally, but not just generically following" state is ever wanted, the correct 1.21.1 NeoForge mechanism is a `AttachmentType` (§6.6), not a bare custom NBT key — but the MVP doesn't need this; `Following` is enough, exactly as today's fallback already assumes. **This should be sanity-checked empirically in Phase 3 testing** (rally a guard, attack it, confirm friendly-fire protection still works) rather than taken purely on this static analysis.
- Everything else the existing ports do around ownership/follow/patrol (the manual `mob.setTarget(null); mob.setAggressive(false); mob.getNavigation().stop();` calls after every state change) is independent, general-purpose `Mob`/`Mob.getNavigation()` API, untouched by this decision — those calls carry over unchanged.

**Net result:** the typed API is confirmed public, intended for external use, behaviorally equivalent on the persisted-data front, and adopted as the Phase 2C implementation plan. No Forge functionality is lost; one latent likely-no-op is identified and will be dropped rather than ported forward.

### 4.1 The `followHero` gate (why hired guards can follow without Hero of the Village)

`Guard.getOwner()` (real source):
```java
public LivingEntity getOwner() {
    UUID uuid = this.getOwnerId();
    boolean heroOfTheVillage = uuid != null && level().getPlayerByUUID(uuid) != null
        && level().getPlayerByUUID(uuid).hasEffect(MobEffects.HERO_OF_THE_VILLAGE);
    return uuid == null || (playerOnline && (!heroOfTheVillage && followHeroConfig)) || (!followHeroConfig && !playerOnline)
        ? null : level().getPlayerByUUID(uuid);
}
```
In plain terms: if the `followHero` config (`GuardConfig.COMMON.followHero`, a `ModConfigSpec.BooleanValue`, default `true`) is `true`, the guard only "sees" its owner (for `FollowHeroGoal` purposes) while that owner has the Hero of the Village effect. If `followHero` is `false`, the guard always sees its online owner. **This is the exact same mechanism the Forge port patches via reflection/config-file rewriting.** With a compile dependency we can do this the direct way at mod setup:
```java
GuardConfig.COMMON.followHero.set(false);
GuardConfig.COMMON.followHero.save();
```
No JSON/TOML file patching, no reflection, no mixin into `GuardInventoryScreen` needed *unless* the inventory screen caches this value independently at screen-open time (Phase 0 found no such cache in `GuardInventoryScreen.java` beyond re-reading `GuardConfig.COMMON.followHero.get()` live — see the grep results in §9 raw notes) — this should be re-verified once the project builds, in Phase 2I, and the mixin re-added only if a real caching problem is observed (rule: no unnecessary mixins).

---

## 5. File-by-file migration plan

Legend: **Port** = adapt from Fabric-1.21.1 body, retarget loader glue to NeoForge · **Rewrite** = loader glue changes enough that it's faster to write fresh against GV's NeoForge examples · **Copy** = no changes needed.

| Target file | Source(s) | Strategy | Notes |
|---|---|---|---|
| `RallyOfTheGuard.java` | Forge `RallyOfTheGuard.java` (structure) + GV `GuardVillagers.java` (constructor idiom) | Rewrite | NeoForge 1.21.1 mod entry point takes `(ModContainer container, IEventBus modEventBus)` in the constructor — **not** `FMLJavaModLoadingContext.get().getModEventBus()` (that's a Forge-only static; still exists as compat but GV's own source uses the modern constructor-injection form, so we should too). |
| `client/ModClient.java` | Forge `client/ModClient.java` | Port | `@Mod.EventBusSubscriber` → `@EventBusSubscriber` (package moved to `net.neoforged.fml.common`). `Dist`/`FMLClientSetupEvent` package moves from `net.minecraftforge.*` → `net.neoforged.*`/`net.neoforged.fml.*`. Drop the `GuardVillagersConfigPatcher` calls if §4.1's direct-config-set approach is adopted. |
| `client/ClientHooks.java` | Both | Port | Trivial — just sends a packet via the new payload API. |
| `contract/GuardOwnership.java` | Both (behavior) + GV `Guard.java` (API) | Rewrite | See §4. `ForgeRegistries.ENTITY_TYPES` → `BuiltInRegistries.ENTITY_TYPE` or the registry-access pattern GV itself uses (`Registries.ENTITY_TYPE` via `RegistryAccess`, or simply `e.getType() == GuardEntityType.GUARD.get()` if we take the compile dependency — cleaner and avoids a `ResourceLocation` lookup entirely). |
| `effect/ModEffects.java`, `effect/RallyCommanderEffect.java` | Forge (structure) | Port | `DeferredRegister<MobEffect>` still exists in NeoForge, just from `net.neoforged.neoforge.registries.DeferredRegister` + `net.neoforged.neoforge.registries.NeoForgeRegistries`/`BuiltInRegistries`. Mostly mechanical package renames. |
| `event/InteractGuardHandler.java` | Forge (structure) + GV `HandlerEvents.java` (proof `PlayerInteractEvent` exists on NeoForge) | Port | `net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific` → `net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific` (class name preserved, package moved — verified via GV's own import list). `NetworkHooks.openScreen` is Forge-only; NeoForge equivalent is `IMenuProvider`/`player.openMenu(MenuProvider, Consumer<RegistryFriendlyByteBuf>)` — confirm exact overload in Phase 2E, it changed shape again in later 1.21.x NeoForge versions (§6.3). |
| `event/RallyFriendlyFireHandler.java` | Forge (structure) | Port | `LivingAttackEvent` was **removed** in 1.21.x NeoForge/Forge — replaced by `LivingIncomingDamageEvent` (cancellable, fires before damage is applied) or `LivingDamageEvent.Pre` (can zero the amount). GV's own `HandlerEvents.java` imports `net.neoforged.neoforge.event.entity.living.LivingDamageEvent`, confirming this event exists and is the modern hook point — use its `Pre` variant. `AttackEntityEvent` (direct player-hit) still exists under `net.neoforged.neoforge.event.entity.player.AttackEntityEvent`. |
| `event/HiredGuardsNeutralityHandler.java` | Forge (structure) | Port | `TickEvent.LevelTickEvent`/`TickEvent.Phase` (Forge 1.20.1) **no longer exists** in this form on NeoForge 1.21.1 — replaced by `net.neoforged.neoforge.event.tick.LevelTickEvent.Post` (verified package via GV's `EntityTickEvent` import in the same file; `LevelTickEvent` is the level-scoped sibling). No more `.phase == Phase.END` check — `Post` *is* the end-of-tick event. |
| `item/ModItems.java` | Forge (structure) | Port | `DeferredRegister<Item>` from NeoForge package; `BuildCreativeModeTabContentsEvent` still exists (GV's own `GuardVillagers.java` uses it verbatim, same signature) — near copy-paste. |
| `item/ScrollOfRallyingItem.java` | Forge (behavior/NBT keys) + Fabric-1.21.1 (data-component pattern for the `isFoil`/glint state, 1.21.1 `Item.use()` signature) | Port | Fabric version's `ModComponents.ACTIVE` (a `DataComponentType<Boolean>`) is the *correct* 1.21.1-idiomatic replacement for the Forge version's raw NBT tag (`rallyguard:active`) — 1.20.5+ deprecated free-form item NBT in favor of data components on both loaders. Port the Fabric component approach, renamed to NeoForge's `DeferredRegister<DataComponentType<?>>` / `DataComponentType.builder(...)`. |
| `item/CommandersLedgerItem.java` | Forge (behavior) + Fabric (1.21.1 `use()` signature) | Port | Straightforward. |
| `network/NetworkBootstrap.java` | GV `GuardVillagers.java`'s `register(RegisterPayloadHandlersEvent)` method | Rewrite | Forge's `SimpleChannel`/`NetworkRegistry.ChannelBuilder` is **fully replaced** on NeoForge 1.21.1 by `RegisterPayloadHandlersEvent` + `PayloadRegistrar` + `CustomPacketPayload` + `StreamCodec`. GV's own `GuardVillagers.java` (`private void register(final RegisterPayloadHandlersEvent event) { PayloadRegistrar reg = event.registrar(MODID).versioned("2.0.2"); reg.playToServer(...); ... }`) is a verified, compiling template for exactly this. |
| `network/payload/*.java` | Fabric-1.21.1 payload records (near-identical shape) + GV's payload classes (NeoForge `StreamCodec` idiom) | Port | Fabric's `record ... implements CustomPayload` with `PacketCodec` is structurally the same concept as NeoForge's `record ... implements CustomPacketPayload` with `StreamCodec` — Mojang unified this API across loaders in 1.20.5+, so the Fabric-1.21.1 payload records port with mostly mechanical renames (`PacketCodec`→`StreamCodec`, `Identifier`→`ResourceLocation`, `RegistryByteBuf`→`RegistryFriendlyByteBuf`). GV's `GuardFollowPacket.java`/`GuardSetPatrolPosPacket.java` confirm the exact NeoForge incantation, including `IPayloadContext` server-thread dispatch (`ctx.enqueueWork(...)`). |
| `screen/ModMenus.java`, `HireGuardMenu.java`, `HireGuardScreen.java` | Forge (structure — `AbstractContainerMenu`/`clickMenuButton` id==0 pattern) + Fabric (1.21.1 `ScreenHandlerType`/menu registration idiom, ported to NeoForge's `MenuType`) | Port | `IForgeMenuType.create(...)` is Forge-only; NeoForge equivalent is `IMenuTypeExtension.create(...)` or the vanilla `MenuType<T>` constructor taking a `MenuSupplier` (register via `DeferredRegister<MenuType<?>>` from NeoForge registries) — confirm exact factory helper name in Phase 2E, this is a small, well-scoped unknown (see §7). |
| `screen/GuardCommandScreen.java` | Forge or Fabric (near-identical `Screen`/`GuiGraphics` code, 1.21.1 vanilla client API is loader-agnostic here) | Port | This file barely touches loader APIs at all — mostly vanilla `Screen`/`Button`/`GuiGraphics`. Nearly copy-paste from either source. |
| `META-INF/neoforge.mods.toml` | Forge `mods.toml` (structure) + GV's own `neoforge.mods.toml` (exact 1.21.1 syntax, verified) | Rewrite | Format changed: `modLoader="javafml"` stays the same string(!), but the file is renamed `neoforge.mods.toml`, and NeoForge dependency block uses `modId="neoforge"` (not `"forge"`) with `type='required'` (not `mandatory=true`) — both syntaxes confirmed from GV's real file. |
| `assets/rallyguard/**`, `data/rallyguard/recipes/**` | Either repo | Copy | Identical between Forge and Fabric repos already (same PNGs/JSON, checked). No loader-specific content. |
| `rallyguard.mixins.json` + `mixin/client/GuardInventoryScreenMixin.java` | Forge | **Deferred / conditional** | Only add back if Phase 2I finds `followHero` isn't fully respected by direct config write — see §4.1, §6.4. |

---

## 6. Breaking changes: Forge 1.20.1 → NeoForge 1.21.1 (verified against GV's real 1.21.1 source + both port repos)

### 6.1 Registries
- `net.minecraftforge.registries.DeferredRegister`/`RegistryObject`/`ForgeRegistries` → `net.neoforged.neoforge.registries.DeferredRegister`/`DeferredHolder`/`NeoForgeRegistries` (GV uses `DeferredHolder`, not `RegistryObject` — the type was renamed).
- Mod entry point registration moved from event-bus-listener style (`modBus.addListener(...)`) for *registries specifically* to still using `DeferredRegister.register(modEventBus)` — unchanged pattern, just package renames.
- `BuildCreativeModeTabContentsEvent` — same name, same shape, package moved to `net.neoforged.neoforge.event`. GV uses it identically to the Forge port.

### 6.2 Networking
- `SimpleChannel`/`NetworkRegistry.ChannelBuilder`/`NetworkDirection` (Forge) — **gone**. Replaced by the vanilla-since-1.20.5 `CustomPacketPayload` + `StreamCodec` system, wired up on NeoForge via `RegisterPayloadHandlersEvent` + `PayloadRegistrar` (`registrar(modid).versioned("...")`, `.playToServer(...)`, `.playToClient(...)`). Verified end-to-end in GV's `GuardVillagers.java`/`networking/*.java`.
- Packet handler signature changed from `Supplier<NetworkEvent.Context>` to `IPayloadContext`; `context.enqueueWork(...)` still exists with the same purpose (marshal back to the main thread).

### 6.3 Menu/Screen
- `IForgeMenuType.create(...)` (Forge) has no direct NeoForge equivalent under that name — NeoForge exposes menu-type creation through `IMenuTypeExtension.create(...)` (via `net.neoforged.neoforge.common.extensions`) for menus needing extra client data, or a plain `new MenuType<>(factory, FeatureFlags.VANILLA_FEATURES)` for simple cases (our `HireGuardMenu` needs no extra data — the Fabric port already proves a data-less 2-arg client factory is sufficient). Since `HireGuardMenu`'s client-side constructor takes no extra fields, the plain `MenuType<>` constructor (matching what the Fabric `ModScreenHandlers.java` does with `ScreenHandlerType<>`) is almost certainly right; confirm against NeoForge 1.21.1 javadoc/decompiled sources in Phase 1.
- `NetworkHooks.openScreen(player, provider, extraDataWriter)` (Forge) → NeoForge exposes menu-opening through `player.openMenu(MenuProvider)` (vanilla) or `player.openMenu(MenuProvider, Consumer<RegistryFriendlyByteBuf>)` (extra data variant) — both are vanilla `ServerPlayer` methods on 1.20.5+, no loader-specific hook needed anymore. This is a simplification, not a blocker.
- `MenuScreens.register(...)` (client-side menu→screen binding) is vanilla and unchanged.

### 6.4 Config
- `ForgeConfigSpec` → `net.neoforged.neoforge.common.ModConfigSpec` (same builder API shape — `.comment(...).defineInRange(...)`/`.define(...)`, verified against GV's `GuardConfig.java`). Registration changed from an event-listener pattern (`ModLoadingContext.get().registerConfig(...)`) to constructor-injected `ModContainer.registerConfig(ModConfig.Type.COMMON, spec)` (verified in GV's constructor).
- `RallyConfig`'s single `PRICE` value: keep as a trivial `ModConfigSpec` (parity with Forge) or drop entirely and hardcode `3` (parity with the Fabric port, which just hardcodes it) — low-stakes, either is fine; defaulting to keeping the config for parity with the Forge MVP spec unless you'd rather simplify.
- GV's own config is exposed as a public static `GuardConfig.COMMON.followHero` (a `ModConfigSpec.BooleanValue`) once we have the compile dependency — see §4.1. This replaces the Forge port's `GuardVillagersConfigPatcher` (JSON/TOML file surgery + reflection) entirely, and removes the only reason that mod currently ships a mixin.

### 6.5 Events
- `net.minecraftforge.event.*` → `net.neoforged.neoforge.event.*` (near-total 1:1 class renames, verified against GV's imports for `PlayerInteractEvent`, `LivingDamageEvent`, `LivingChangeTargetEvent`, `EntityJoinLevelEvent`).
- `LivingAttackEvent` (used by the Forge port's `RallyFriendlyFireHandler` for "any damage" interception, e.g. sweep/projectiles/trident) **does not exist** in 1.21.1 NeoForge. Its closest modern replacement is `LivingIncomingDamageEvent` (fires before amount/type is finalized, cancellable) — needs confirmation against the actual NeoForge 1.21.1 event API during Phase 2H, but GV's own damage-related hook (`LivingDamageEvent`) confirms the general family of events still exists and is subscribed to the same way.
- `TickEvent.ClientTickEvent`/`TickEvent.LevelTickEvent` with a `.phase` field → split into distinct `Pre`/`Post` event classes under `net.neoforged.neoforge.event.tick.*` (e.g. `LevelTickEvent.Post`, `PlayerTickEvent.Post`) — verified via GV's `EntityTickEvent` import (same package family).
- `Mod.EventBusSubscriber` → `net.neoforged.fml.common.EventBusSubscriber` (same annotation shape/attributes — `modid=`, `bus=` — verified via GV's `HandlerEvents.java`).

### 6.6 Data storage / NBT
- No breaking change to the flat-NBT save format for guard state (`Owner`, `Following`, `Patrolling`, `PatrolPosX/Y/Z` all still present, per §4). Internally GV moved from (presumed) plain fields to `SynchedEntityData`, which is itself a networking/sync mechanism, not a storage-format change — the NBT keys are stable.
- NeoForge's own new data-storage feature, **Data Attachments** (`net.neoforged.neoforge.attachment.AttachmentType`, replacing Forge `Capability`), is used internally by GV for a few unrelated counters (`times_thrown_potion`, `times_repaired_golem`, etc. — see `GuardDataAttachments.java`). **Rally of the Guard does not need to touch attachments** — none of the MVP features require attaching new persistent data to the guard entity; the item-side "is this scroll active" flag is better served by a **Data Component** on the `ItemStack` (see §5, `ScrollOfRallyingItem.java` row), not an attachment (attachments are for entities/block entities/levels, components are for item stacks — different mechanisms for a similar problem, and the Fabric-1.21.1 repo already made the entity-vs-item distinction correctly for us).

### 6.7 Mixins
- Confirmed working on NeoForge 1.21.1: GV itself ships `[[mixins]] config="guardvillagers.mixins.json"` inside its `neoforge.mods.toml`, with `"compatibilityLevel": "JAVA_21"` — same declaration mechanism the Forge port uses (`loom { forge { mixinConfig '...' } }` is Loom-specific config-file plumbing; the actual runtime declaration NeoForge reads is the `[[mixins]]` toml block, which GV's build confirms). If Phase 2I determines the `GuardInventoryScreenMixin` is still needed, this is a known-working mechanism, not a research risk.

### 6.8 Java version
- Java 21 (NeoForge 1.21.1 / GV's own toolchain declaration). Matches the Fabric port's Java 21 target; up from the Forge port's Java 17.

---

## 7. Risky areas (ranked)

1. **Ownership contract migration (§4).** Not risky in the sense of "might not work" (the API is verified) — risky in the sense of "easy to get subtly wrong if the typed-API recommendation is adopted and a call site is missed, silently falling back to old NBT-poking behavior in one spot." Mitigate by doing this as a single, reviewable pass through `GuardOwnership` first, before touching any event handler that calls it (matches the requested MVP order — ownership/hiring is stage C, before rally/patrol).
2. **`RallyFriendlyFireHandler`'s "any damage" hook (§6.5).** `LivingAttackEvent` is gone; the replacement event's exact cancellation semantics (does cancelling `LivingIncomingDamageEvent` fully suppress knockback/sound/hurt-animation the same way the old event did?) needs empirical verification in-game, not just a compile-time swap.
3. **`HireGuardMenu`'s NeoForge `MenuType` factory (§6.3).** The exact factory method name for a client constructor taking `(int, Inventory, RegistryFriendlyByteBuf)` vs a data-less `(int, Inventory)` needs confirming against decompiled/actual NeoForge 1.21.1 sources in Phase 1 — low risk (well-trodden path, every NeoForge mod with a custom menu solves this), but it's the one loader idiom this plan couldn't verify against a real compiling example (GV doesn't have a custom menu of its own — it reuses `GuardContainer`/a hand-rolled screen without going through `MenuType` the same way).
4. **Exact Guard Villagers jar version drift.** This plan verified against the `1.21.1` branch head of the real upstream repo, whose `gradle.properties` declares `mod_version=2.4.12`, and cross-referenced the CurseForge listing (`2.3.2`–`2.4.11`/`2.4.12` builds exist for 1.21.1). The core API in §4 (five methods, four NBT keys) has been stable in this mod across every version this plan could inspect, and is exactly the surface the Forge 1.20.1 port already relies on (same NBT keys, four MC major versions apart) — but the *exact* jar the user ends up building against should be decompiled and spot-checked against this table in Phase 1, since Phase 0 could not download the actual `.jar` binary (network sandboxing — see §3).
5. **Unloaded/far guards.** Both existing ports already document this as a known limitation ("Guards in unloaded chunks may not pathfind until their chunk is loaded"), not something to newly solve — carried over as-is per "preserve original Forge behavior."

## 8. Expected compilation blockers (first `./gradlew build` after gameplay code lands)

- Missing/renamed imports for every class listed in §6 (registries, networking, events, config) — expected, mechanical, fixed file-by-file per the migration table.
- `Item.Properties`/`Item.use()` signature drift between 1.20.1 and 1.21.1 vanilla (independent of loader) — e.g., `InteractionResultHolder<ItemStack>` return type may have changed shape; the Fabric-1.21.1 repo's `ScrollOfRallyingItem.use()` signature is the correct 1.21.1 vanilla shape to copy.
- `CompoundTag`/`FriendlyByteBuf` method renames are unlikely (Mojmap has been fairly stable there since 1.20), but `RegistryFriendlyByteBuf` now shows up in more networking signatures than `FriendlyByteBuf` alone (registry-aware codecs) — expect a few codec type-parameter mismatches on first compile.
- The local `libs/guardvillagers-*.jar` not being present will fail the build immediately and loudly (`implementation files(...)` on a missing path) — this is the Phase 1 gate: get a real jar in place before attempting the skeleton build.

## 9. Expected runtime blockers (first launch / manual test pass)

- `followHero` gate not actually flipping if the direct `GuardConfig.COMMON.followHero.set(false)` call races GV's own config load order (mod load order/`ordering="AFTER"` dependency needs to be correct in our `neoforge.mods.toml`, mirroring the Forge port's `ordering = "AFTER"` on its `guardvillagers` dependency).
- `RallyFriendlyFireHandler`'s replacement event (§6.5, §7.2) not fully suppressing damage the way `LivingAttackEvent` used to (visual hit reaction, sound, or knockback might slip through even if HP damage is blocked) — needs an in-game check, not just "does it compile."
- Networking payload registration ordering (`versioned("...")` version string mismatches between our mod's client/server, or forgetting to register a payload type before a menu tries to open) — classic first-launch NPE/desync source for new NeoForge networking code; mitigated by following GV's own `RegisterPayloadHandlersEvent` pattern exactly.
- GUI text (`GuardCommandScreen`) using `Component.translatable(...)` keys that don't exist yet in `en_us.json` for this new project — trivial to catch (renders the raw key), but worth a checklist item in `TESTING.md`.

---

## 10. MVP implementation order (Phase 2, staged per your instructions)

Matches your requested lettering; each stage compiles and is committed before the next starts.

- **A. Mod metadata/resources** — `neoforge.mods.toml`, copy `assets/`/`data/` verbatim, `pack.mcmeta`. No Java yet. Build should succeed with zero gameplay classes (empty `@Mod` main class).
- **B. Registries/items/recipes** — `ModItems`, `ModEffects`, `RallyCommanderEffect`, item models (already have art). Creative tab entries. Recipes copy verbatim. No behavior yet (items do nothing on use).
- **C. Ownership/hiring** — `GuardOwnership` (typed-API rewrite per §4, confirmed §4.2), `InteractGuardHandler`, `HireGuardMenu`/`HireGuardScreen`/`ModMenus`. This is the riskiest stage (§7.1) — isolate it and get it fully working/tested before touching networking for the Ledger.
- **D. Networking** — `NetworkBootstrap`, all three payload records, `GuardCommandNetworking` server-side handlers. No UI consumes them yet beyond what C already needs.
- **E. Commander's Ledger** — `CommandersLedgerItem`, `GuardCommandScreen`, wire up summon/patrol-toggle actions client→server.
- **F. Patrol** — patrol-position storage/read via the typed API (§4), the `Mob.getNavigation()` reset dance the Forge port already does on toggle.
- **G. Scroll of Rallying** — `ScrollOfRallyingItem` with the 1.21.1 data-component "active/glint" flag (port from Fabric repo), `ModEffects.RALLY_COMMANDER` gating, rally/dismiss radius logic.
- **H. Friendly-fire/neutrality** — `RallyFriendlyFireHandler` (§6.5/§7.2 — the one stage with a genuinely unverified event API swap), `HiredGuardsNeutralityHandler` (§6.5 tick-event rewrite).
- **I. Guard Villagers integration/mixins** — final pass confirming `followHero` behaves correctly end-to-end (§4.1); add the mixin back only if empirically needed (§6.7).

---

## 11. Phase 1 status — VERIFIED: `.\gradlew.bat build` succeeds

### 11.1 Update: `clientData` run-type fix (2026-08-27, second iteration)

You ran `.\gradlew.bat build` on your own Windows machine (Temurin 21.0.12.1, `JAVA_HOME` set correctly). Gradle 8.9 downloaded and NeoGradle/NeoForge resolved fine — confirming the network blocker in §11 above is specific to Claude sessions, not your machine. Configuration then failed with:
```
(The run type 'clientData' was not found) null.
```

**Exact cause:** the original `build.gradle`'s `runs {}` block declared a run named `clientData`, copied by mistake from a *different, newer/future* branch of the Guard Villagers repo (`main`, which targets a much later Minecraft version and a newer NeoGradle release: `net.neoforged.gradle.userdev` `7.1.38` there vs. `7.0.165` on the actual `1.21.1` branch) — an artifact of an earlier git-checkout error during Phase 0 research where that branch's `build.gradle` got read instead of the intended `1.21.1` branch's. NeoGradle 7.0.165 (what our `neo_version`/`build.gradle` actually target) does not recognize `clientData` as a run type at all.

**Fix — re-verified directly against the real `seymourimadeit/guardvillagers` `1.21.1` branch `build.gradle` (re-cloned and re-read in full, this time confirming the branch with `git branch --show-current` before trusting the content):** its data-generation run block is named **`data`**, and uses **`programArguments.addAll(...)`**, not `arguments.addAll(...)`:
```groovy
data {
    programArguments.addAll '--mod', project.mod_id, '--all', '--output', file('src/generated/resources/').getAbsolutePath(), '--existing', file('src/main/resources/').getAbsolutePath()
}
```
While re-verifying, two more small mismatches from the same wrong-branch copy were caught and fixed in the same pass (same root cause, same fix — not scope creep):
- `client`/`server`/`gameTestServer` blocks used a guessed property name `neoforge.enabledGameTestNamespaces`; the verified real source uses `forge.enabledGameTestNamespaces` (NeoForge 21.1.215 still uses the legacy `forge.*` system-property namespace for this particular flag — verified, not assumed).
- `server { argument '--nogui' }` used a singular `argument(String)` DSL method that doesn't appear anywhere in Guard Villagers' own verified build files and isn't confirmed to exist on this NeoGradle version — removed rather than left in unverified, since it wasn't needed for the "build succeeds" milestone.

**Files changed:** `build.gradle` only — the `runs {}` block (see §5/§11 above for the rest of the skeleton, unchanged). No dependency, `gradle.properties`, `settings.gradle`, or gameplay changes.

**Verification:** attempted `gradle help` in this session's own sandbox to check the fix compiles/configures — it still cannot get past the same network-allowlist block documented in §11 above (Gradle Plugin Portal unreachable from here), so **this fix has not been executed successfully by Claude**. It is verified by direct comparison against Guard Villagers' real, confirmed-correct `1.21.1`-branch `build.gradle`, not by a passing build. Please re-run `.\gradlew.bat build` and report the result.

The skeleton project (`build.gradle`, `gradle.properties`, `settings.gradle`, the Gradle wrapper copied verbatim from Guard Villagers' own working `1.21.1` branch, a no-op `RallyOfTheGuard` `@Mod` entry point, `neoforge.mods.toml`, `pack.mcmeta`, `libs/README.md`) has been written to this project folder. **`./gradlew build` has been attempted twice in this session's available shells and has not succeeded** — this is reported honestly rather than assumed, per your instructions.

**Exact failure, reproduced with both the project's own Gradle wrapper and this environment's system-installed Gradle 8.14.3:**
```
* What went wrong:
Plugin [id: 'org.gradle.toolchains.foojay-resolver-convention', version: '0.5.0'] was not found in any of the following sources:
- Gradle Core Plugins (plugin is not in 'org.gradle' namespace)
- Included Builds (No included builds contain this plugin)
- Plugin Repositories (could not resolve plugin artifact '...')
```
The wrapper's own first step (downloading the Gradle 8.9 distribution from `services.gradle.org`) failed identically with `HTTP/1.1 403 Forbidden` from an intermediate proxy.

**Root cause:** this Claude session's outbound network access — in *both* its own cloud sandbox and the bridged shell on your machine (`device_bash`, which was also tested and hits the same restriction) — is limited to an allowlist that does not include the Gradle Plugin Portal, `services.gradle.org`, `maven.neoforged.net`, `maven.parchmentmc.org`, or Mojang's asset/library servers (`piston-meta.mojang.com`, `libraries.minecraft.net`) — all tested directly and all returned `403`/`blocked-by-allowlist`. A NeoForge Gradle build fundamentally needs several of these to do its first-run toolchain setup (resolve NeoGradle's own plugins, download NeoForge itself, download and patch/decompile against Minecraft). There is no workaround available from inside this session — this isn't a project misconfiguration, it's a network boundary neither shell here can cross.

**What this means for you:** the skeleton is ready, but its first real build needs to happen in a normal terminal on your machine (outside this session's sandboxed bridge — e.g. your regular terminal, IDE, or a shell you open yourself, which has your computer's normal internet access). Once you run `./gradlew build` there and it either succeeds or fails, paste the output back here (or just tell me it succeeded) and I'll either make the commit-and-report you asked for, or fix whatever it reports — I can still fix compile errors from the error text alone even though I can't execute the build myself in this session.

**Where the files are:** `C:\Proyectos personales\rally-of-the-guard-neoforge\` (already written there).

**Guard Villagers jar:** also not resolvable automatically for the same networking reason (§3). It's wired as optional for this Phase 1 skeleton specifically so that absence doesn't block the build milestone — but you'll still need to supply it before Phase 2C. See `libs/README.md` in the project folder for the exact expected filename (`guardvillagers-2.3.2-1.21.1.jar`, or any 2.3.x/2.4.x build for NeoForge 1.21.1) and where to get it (official CurseForge page, author `almightytallestred` — **not** the `ShackleSailorTrek` mirror flagged in §0).

### 11.2 VERIFIED — `.\gradlew.bat build` succeeds (2026-08-27, third iteration)

You re-ran the build on your own Windows machine after the §11.1 fix and it succeeded:

```
BUILD SUCCESSFUL in 8m 20s
32 actionable tasks: 10 executed, 22 up-to-date
```

**Environment this was verified against:**
- Minecraft 1.21.1
- NeoForge 21.1.215
- Java 21 (Eclipse Temurin 21.0.12.1)
- Gradle 8.9 (via the project's own wrapper)
- Guard Villagers dependency: intentionally absent (as planned for Phase 1 — see §3, §11 "Guard Villagers jar")

**Noted but non-blocking:** a NeoGradle deprecation warning for `Run.getProgramArguments()` — the `data` run config's method NeoGradle itself is nudging toward a newer accessor, but the old one still works on NeoGradle 7.0.165 and the build completes. Worth a one-line look whenever this project's NeoGradle version is bumped in the future; not a Phase 1 blocker.

**This is the authoritative verification.** Claude has not run this build successfully in either shell available to it (its own cloud sandbox or the `device_bash` bridge) — both remain blocked from the Gradle Plugin Portal / NeoForge / Mojang infrastructure this build needs, as documented in §11 above and re-confirmed during the §11.1 fix. The build.gradle changes in §11.1 are correct because they match Guard Villagers' own verified real 1.21.1-branch source *and* because you've now confirmed they compile and configure successfully end-to-end on real hardware — not because Claude executed them.

**Phase 1 is complete.**

## 12. Phase 2 preflight (2026-08-27) — no gameplay code touched

Run before starting Phase 2, per your request. Findings only; nothing in `src/` changed.

**1. HEAD check:** confirmed. `git rev-parse HEAD` on the project folder returns `e3b1f0e85a289b47b73af3b91bf7f333c11f3354` — the Phase 1 commit remains current HEAD, no other commits landed since.

**2. Working tree:** confirmed clean at that commit (`git status --short` empty; `.gradle/`, `build/`, `runs/` present on disk from your successful build but correctly untracked/ignored). Note: this preflight's own changes (item 4 below) leave three files modified after this check — see that item.

**3. Plan-vs-actual discrepancies found:**
- No functional discrepancies. The one real one from earlier (the `clientData`/`forge.enabledGameTestNamespaces` mismatch) was already found and fixed during Phase 1 itself (§11.1) and is reflected in both the plan and the committed `build.gradle`.
- One accuracy gap, now corrected as part of this preflight: `gradle.properties` and `libs/README.md` pointed at `guardvillagers-2.3.2-1.21.1.jar` as the expected filename — that was simply the first NeoForge/1.21.1 file Phase 0's search happened to surface, not necessarily the best choice. Re-checked directly against the current CurseForge listing just now (see item 5) and updated both files to point at the actual current latest release instead. This is a documentation precision fix, not a sign anything was broken.
- The architecture tree in §2.1 (client/, config/, contract/, effect/, event/, item/, network/, screen/ packages) is aspirational/Phase 2 target, not yet built — that's expected and already labeled as such in the plan, not a discrepancy.

**4. Project prepared for the real dependency (no jar downloaded or invented):**
- `build.gradle`'s guarded `if (guardVillagersJar.exists())` local-file dependency wiring (already in place since Phase 1) needs no changes — it will pick up the jar automatically once it's placed at the path `gradle.properties` names.
- Updated `gradle.properties` (`guardvillagers_jar=...`) and `libs/README.md` to name the exact recommended file (item 5) instead of the earlier placeholder. These two files are now modified but **uncommitted** — left for you to review/commit, since you didn't ask for a commit this round.
- Did not touch `neoforge.mods.toml`'s dependency block — its `versionRange="[2.3.0,)"` was already loose enough to cover this file and is still correct.

**5. Exact file to place at `libs/`:**
```
guardvillagers-2.4.11-1.21.1.jar
```
Re-confirmed directly against the official CurseForge page (not from Phase 0 memory) during this preflight: **NeoForge**, **Minecraft 1.21.1**, Release status, uploaded Aug 23, 2026, 276.7 KB — the current latest published build for this exact target. It's also the closest published release to the `seymourimadeit/guardvillagers` `1.21.1`-branch source (`mod_version=2.4.12` at that branch's HEAD) that this plan's §4.2 ownership-API verification was checked against, so it's the build most likely to match that verification without drift.

Get it from the official CurseForge page (`curseforge.com/minecraft/mc-mods/guard-villagers`, author `almightytallestred`) or Modrinth project `H1sntfo8`. **Do not use `github.com/ShackleSailorTrek/guard-villagers`** — flagged in §0 as hosting trojanized jars disguised as this mod. Place the file at:
```
C:\Proyectos personales\rally-of-the-guard-neoforge\libs\guardvillagers-2.4.11-1.21.1.jar
```

**6. Binary verification against the actual jar — COMPLETE.** You placed `guardvillagers-2.4.11-1.21.1.jar` (283,387 bytes) at `libs/`. It was staged into the working environment and inspected directly with `javap` (no source, no assumptions) — the real compiled classes, not the `1.21.1`-branch source §4.2 was originally checked against:

- **Class identity:** `tallestegg.guardvillagers.common.entities.Guard` — `public class Guard extends PathfinderMob implements CrossbowAttackMob, RangedAttackMob, NeutralMob, ContainerListener, ReputationEventHandler`. Matches §4.2 point 1 exactly.
- **Ownership/following/patrol API — all present, public, identical signatures to §4.2:**
  ```
  public java.util.UUID getOwnerId();
  public void setOwnerId(java.util.UUID);
  public boolean isFollowing();
  public void setFollowing(boolean);
  public boolean isPatrolling();
  public void setPatrolling(boolean);
  public net.minecraft.core.BlockPos getPatrolPos();
  public void setPatrolPos(net.minecraft.core.BlockPos);
  ```
  Also confirmed: `getOwner()`, `isOwner(LivingEntity)`.
- **NBT persistence keys — verified in the bytecode, not just by method presence.** Traced the constant pool and the `ldc` instructions around `addAdditionalSaveData`/`readAdditionalSaveData`: the literal strings `Owner`, `Following`, `Patrolling`, `PatrolPosX`, `PatrolPosY`, `PatrolPosZ` are present and used in both a write path and a read path — confirms §4.2 point 3's persisted-data claim against the real binary.
- **`GuardConfig.COMMON.followHero` (§4.1) — verified.** `GuardConfig.COMMON` is `public static final GuardConfig$CommonConfig`; `CommonConfig` declares `public final ModConfigSpec.BooleanValue followHero` (alongside `multiFollow`). Matches exactly.
- **Jar's own metadata — verified.** Its `META-INF/neoforge.mods.toml` self-reports `modId="guardvillagers"`, `version="2.4.11"`, and a `minecraft` dependency of `versionRange="[1.21.1, 1.22)"` — consistent with this project's target.
- **Drift check: none found.** Package, class declaration, all eight method signatures, all six NBT key strings, and the config field all match the §4.2/§4.1 source-level verification exactly. No signature changes, no renamed/removed methods, no changed NBT keys between the `1.21.1`-branch source and the actual `2.4.11` binary.
- **Dependency configuration — confirmed already correct, no changes needed:** `gradle.properties`'s `guardvillagers_jar` names this exact file; `build.gradle`'s `file("libs/${guardvillagers_jar}")` now resolves to a jar that exists, so the `implementation files(...)` branch will activate on the next build; `neoforge.mods.toml`'s `versionRange="[2.3.0,)"` for `modId="guardvillagers"` is satisfied by `2.4.11`.

**Net result:** the Phase 2C plan (§4, §4.2) is now verified against the real dependency binary, not just its source. No blockers, no plan changes required.

**7. Phase 2A/B/C:** not started. No gameplay code modified.

## 13. Phase 2A status (2026-08-27) — VERIFIED: `.\gradlew.bat build` succeeds

Stage A per §10: "Mod metadata/resources — neoforge.mods.toml, copy assets/data verbatim, pack.mcmeta. No Java yet. Build should succeed with zero gameplay classes (empty `@Mod` main class)."

**Scope decision (recorded for future reference):** §10's Stage A text ("copy assets/data verbatim") and Stage B text ("item models... Recipes copy verbatim") overlap. Resolved as: Stage A = mod-level metadata not tied to any registered item/effect — `neoforge.mods.toml`, `pack.mcmeta`, `icon.png`, the two lang files. Item models, item textures, mob-effect textures, and recipes are Stage B's job (tied to `ModItems`/`ModEffects` registration, explicitly named in §10's Stage B bullet) and were deliberately left uncopied this stage. `rallyguard.mixins.json` stays deferred to Phase 2I per §5 — untouched.

**What was implemented:**
- `src/main/resources/assets/rallyguard/icon.png` — added, byte-identical to the Forge source (SHA-256 verified).
- `src/main/resources/assets/rallyguard/lang/en_us.json` and `lang/pt_br.json` — added, byte-identical to the Forge source (SHA-256 verified). All translation keys preserved verbatim, including keys for items/effects/GUI screens that don't exist as Java yet (Stages B–E) — inert until then, not an error.
- `neoforge.mods.toml` and `pack.mcmeta` — no changes; both were already correct as of Phase 1. `pack_format_number=18` matches Guard Villagers' own verified `1.21.1`-branch `gradle.properties` exactly. Deliberately did not add a `logoFile` entry — Forge's own `mods.toml` leaves that line commented out, so that (non-)behavior was preserved rather than "improved."
- No Java files touched. `RallyOfTheGuard.java` unchanged — still the no-op `@Mod` entry point, zero gameplay classes, matching Stage A's "no Java yet" requirement.

**Build result — real, user-executed, not simulated:** you ran `.\gradlew.bat build` on the actual Windows machine (same environment as Phase 1: Temurin 21, Gradle 8.9) and reported:
```
BUILD SUCCESSFUL in 33s
32 actionable tasks: 2 executed, 30 up-to-date
```
Only output of note: the same pre-existing non-blocking NeoGradle deprecation warning about `Run.getProgramArguments()` already noted in §11.2 — not new, not related to this stage's changes.

**Commit:** "Phase 2A: mod metadata/resources (icon, lang files)" (see `git log` for the exact hash). Contains only the 3 new resource files (`icon.png`, `lang/en_us.json`, `lang/pt_br.json`) plus this PORTING_PLAN.md update. No `src/main/java` changes, no registries, no items, no recipes, no networking, no menus, no entities.

**Phase 2B:** not started.

## 14. Phase 2B status (2026-08-27) — VERIFIED: `.\gradlew.bat build` succeeds

Stage B per §10: "Registries/items/recipes — ModItems, ModEffects, RallyCommanderEffect, item models (already have art). Creative tab entries. Recipes copy verbatim. No behavior yet (items do nothing on use)."

**What was implemented:**
- `item/ModItems.java` — `DeferredRegister<Item>` keyed by `Registries.ITEM`, registers `scroll_of_rallying` and `commanders_ledger` (registry names, `stacksTo(1)` property preserved exactly from Forge), plus `registerCreativeTabContents(...)` adding both to `CreativeModeTabs.TOOLS_AND_UTILITIES` (same tab as Forge).
- `item/ScrollOfRallyingItem.java`, `item/CommandersLedgerItem.java` — thin `Item` subclasses: constructor + `appendHoverText(...)` only (static tooltip text, ported verbatim). The Forge `use(...)` overrides (rally logic, ownership checks, the C2S packet that opens the command GUI) and the "active"/glint indicator are explicitly Phase 2C/2D/2E/2G territory and were **not** ported — these items do nothing on use, per Stage B's own requirement.
- `effect/ModEffects.java` — `DeferredRegister<MobEffect>` keyed by `Registries.MOB_EFFECT`, registers `rally_commander`.
- `effect/RallyCommanderEffect.java` — ported verbatim; the Forge version already had zero behavior (no tick/duration overrides), just `super(MobEffectCategory.NEUTRAL, 0xFFD700)`.
- `RallyOfTheGuard.java` — constructor now calls `ModItems.REGISTER.register(modEventBus)`, `ModEffects.REGISTER.register(modEventBus)`, and adds the creative-tab listener. Everything else from the Forge constructor (menus, networking, the `MinecraftForge.EVENT_BUS` gameplay handlers, the Guard Villagers config patcher) is untouched/not added — those are later stages.
- Resources copied byte-for-byte (SHA-256 verified) from the Forge source: both item model JSONs, both item textures, both mob-effect textures, both recipe JSONs (`commanders_ledger`, `scroll_of_rallying` shaped crafting recipes, unchanged).

**Forge → NeoForge / vanilla-API changes, each verified before use (not guessed):**
- `net.minecraftforge.registries.DeferredRegister`/`RegistryObject` → `net.neoforged.neoforge.registries.DeferredRegister`/`DeferredHolder`, keyed by the vanilla `ResourceKey<Registry<T>>` constants on `net.minecraft.core.registries.Registries` (`Registries.ITEM`, `Registries.MOB_EFFECT`) instead of `ForgeRegistries.ITEMS`/`ForgeRegistries.MOB_EFFECTS`. Verified two ways: (1) directly against Guard Villagers' own real, compiled 1.21.1 source (`GuardItems.java` registers items this exact way against `Registries.ITEM`); (2) `Registries.MOB_EFFECT` specifically (no precedent in the GV jar, which doesn't register any mob effects) was independently confirmed via the official 1.21.1 Mojang-mapped class listing at mappings.dev: `public static final ResourceKey<Registry<MobEffect>> MOB_EFFECT`.
- `net.minecraftforge.event.BuildCreativeModeTabContentsEvent` → `net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent` (package moved). Verified against Guard Villagers' own `addCreativeTabs` listener in `GuardVillagers.java`, including the exact `event.accept(HOLDER.get())` call shape (not `event.accept(HOLDER)`).
- `Item.appendHoverText(ItemStack, Level, List<Component>, TooltipFlag)` → `Item.appendHoverText(ItemStack, Item.TooltipContext, List<Component>, TooltipFlag)`. This is a genuine vanilla signature change between MC 1.20.1 and 1.21.1 (not a Forge/NeoForge difference) that was **not** already listed in section 6 - found and confirmed via mappings.dev's 1.21.1 `Item` and `Item$TooltipContext` listings (second parameter type changed; our override doesn't need to read from the context, so no behavioral adaptation was needed beyond the parameter type).
- `MobEffect(MobEffectCategory, int)` constructor and `CreativeModeTabs.TOOLS_AND_UTILITIES` — both confirmed unchanged in 1.21.1 via mappings.dev, so `RallyCommanderEffect` and the creative-tab target needed no changes at all.
- `Item.Properties().stacksTo(int)` — confirmed unchanged in 1.21.1 via mappings.dev.

**Scope note:** `event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES` compares a `ResourceKey<CreativeModeTab>` (unchanged pattern from Forge). No entity code was touched - registration did not require it.

**Build result — real, user-executed, not simulated:**
```
BUILD SUCCESSFUL in 23s
32 actionable tasks: 3 executed, 29 up-to-date
```
Only output of note: the same pre-existing non-blocking NeoGradle deprecation warning (`Run.getProgramArguments()`) already seen on every build since Phase 1 - not new, not related to this stage.

**Commit:** "Phase 2B: registries/items/recipes" (see `git log` for the exact hash). Contains only `ModItems`, `ModEffects`, `RallyCommanderEffect`, `ScrollOfRallyingItem`, `CommandersLedgerItem`, the `RallyOfTheGuard.java` registration wiring, the 8 copied resource files, and this PORTING_PLAN.md update. No ownership/hiring, networking, menus/screens, patrol, rally logic, friendly-fire, entity, or mixin code.

**Phase 2C:** not started.

## 15. Phase 2C status (2026-09-12) — VERIFIED: `.\gradlew.bat build` succeeds; AWAITING IN-GAME TEST

Primary objective per your Phase 2C authorization: port the Guard Villagers hiring/ownership integration so Rally of the Guard can correctly establish ownership of hired guards while preserving Guard Villagers' existing behavior. Code/build phase is complete; **this does not mean Phase 2C is validated** - that requires your manual in-game test (see below).

**Hiring flow traced in full before writing anything:** `Guard.mobInteract` (read directly in the real `guardvillagers-2.4.11-1.21.1.jar` decompiled source) only opens Guard Villagers' own inventory GUI for a player who already has Hero of the Village (with config gates), sufficient reputation, or is already the owner - an ordinary player interacting with an unowned guard gets `InteractionResult.CONSUME` and nothing happens. Rally of the Guard's `InteractGuardHandler` intercepts `PlayerInteractEvent.EntityInteractSpecific`, which fires *before* `mobInteract` in the vanilla interaction pipeline, and for an unowned guard opens `HireGuardMenu` instead (an alternate, emerald-cost hire path). If the guard already has an owner, the handler returns early and lets Guard Villagers' own interaction run normally. The "Hire" button in `HireGuardScreen` reaches `HireGuardMenu.clickMenuButton` entirely through vanilla's built-in `ServerboundContainerButtonClickPacket` mechanism - confirmed there is no Rally-of-the-Guard custom networking anywhere in this flow, so the whole thing is genuinely Phase 2C, not entangled with Phase 2D.

**What was implemented:**
- `contract/GuardOwnership.java` - rewritten to use Guard Villagers' verified typed API (`getOwnerId()`/`setOwnerId(UUID)`) instead of the old `saveWithoutId`/`load` NBT round-trip. The redundant `rallyguard:Owner` NBT key is dropped entirely (Guard Villagers' own "Owner" persistence is now the single source of truth). `isGuard(Entity)` uses `instanceof Guard` instead of a `ResourceLocation`-based registry lookup - behaviorally identical, simpler now that the real class is a verified compile dependency. Public method names/signatures (`isGuard`, `getOwner`, `hasOwner`, `isOwnedBy`, `setOwner`) preserved exactly so later stages calling `GuardOwnership.xxx(...)` don't need to change.
- `event/InteractGuardHandler.java` - ported; detects unowned guards and opens the hire menu.
- `screen/HireGuardMenu.java` - ported; emerald counting/deduction unchanged, ownership now set via `GuardOwnership.setOwner(...)`.
- `screen/HireGuardScreen.java` - ported (client-side GUI only, no ownership/networking logic).
- `screen/ModMenus.java` - ported; registers the `hire_menu` `MenuType`.
- `client/ModClient.java` - **new file**, not explicitly named in your Phase 2C file list but judged squarely inside your own stated allowance ("GUI/menu classes... insofar as required to complete the hiring flow") - registers `HireGuardScreen` for the menu type. Without it the menu opens server-side with nothing to render client-side. Deliberately excludes the Forge original's `GuardVillagersConfigPatcher` calls (Phase 2I).
- `RallyOfTheGuard.java` - now also calls `ModMenus.register(modEventBus)` and `NeoForge.EVENT_BUS.register(new InteractGuardHandler())`. Networking, the Ledger, patrol, rally, friendly-fire, and the config patcher remain unregistered.

**No entity code was touched** - registration/interaction/ownership did not require it.

**Forge → NeoForge / vanilla API changes, each verified before use:**
- `NetworkHooks.openScreen(player, provider, writer)` → `ServerPlayer.openMenu(MenuProvider, Consumer<RegistryFriendlyByteBuf>)` - verified against the official NeoForged 1.21.1 docs and the real `IPlayerExtension`/`IContainerFactory` javadoc (`aldak`/`lexxie.dev` NeoForge 1.21.1 mirrors).
- `IForgeMenuType.create(...)` → `IMenuTypeExtension.create(...)`, keyed by `Registries.MENU` (confirmed present, unchanged, via mappings.dev); the client-side menu constructor's buffer parameter changes from `FriendlyByteBuf` to `RegistryFriendlyByteBuf`, matching the real `IContainerFactory<T>.create(int, Inventory, RegistryFriendlyByteBuf)` signature (not the simplified `FriendlyByteBuf` shown in NeoForge's own introductory doc example - the actual interface was trusted over the simplified prose).
- `Screen.renderBackground(GuiGraphics)` → `renderBackground(GuiGraphics, int, int, float)` - genuine 1.20.1→1.21.1 vanilla signature change (mouseX/mouseY added), verified via mappings.dev.
- `@Mod.EventBusSubscriber` → `net.neoforged.fml.common.EventBusSubscriber` - confirmed by pulling NeoForge's own FancyModLoader source directly (not just docs prose).
- `PlayerInteractEvent.EntityInteractSpecific` - package moved to `net.neoforged.neoforge.event.entity.player`; all method names unchanged (verified via NeoForge 1.21.1 javadoc).
- `InteractionResult` - confirmed still a plain enum with `SUCCESS`/`CONSUME`/`PASS` in 1.21.1 (gained new variants, kept the old ones) - no change needed.
- **`MenuScreens.register(MenuType, ScreenConstructor)` - the one adaptation NOT caught by pre-build verification, found only by the real compiler.** The first build attempt failed: `<M,U>register(...) has private access in MenuScreens`. That overload, callable directly by mods in Forge 1.20.1, is now `private` in vanilla 1.21.1. The real fix, confirmed by reading NeoForge's own source (`net.neoforged.neoforge.client.event.RegisterMenuScreensEvent`, which wraps that same private method behind a public `register(...)` call) and the official NeoForged 1.21.1 "Screens" documentation: subscribe to `RegisterMenuScreensEvent` (a mod-bus, `IModBusEvent`-implementing event fired specifically for this) instead of calling `MenuScreens.register` inside `FMLClientSetupEvent`. `ModClient.java` was corrected accordingly. This is a reminder that a "verified" API can still miss something the compiler alone catches - the actual `.\gradlew.bat build` remains the final authority, exactly as the standing rules require.

**How ownership is established and persisted:** `HireGuardMenu.clickMenuButton` (server-side, id 0) calls `GuardOwnership.setOwner(guard, player)`, which casts to `tallestegg.guardvillagers.common.entities.Guard` and calls its own `setOwnerId(player.getUUID())`. This writes through Guard Villagers' `SynchedEntityData`, which both persists to the "Owner" NBT key on save (verified in section 4.2/12.6) and live-syncs the change to tracking clients immediately - an improvement over the old NBT round-trip, not just parity.

**Build result — real, user-executed, not simulated. Two attempts:**
1. First attempt: `BUILD FAILED` - the `MenuScreens.register` private-access error above.
2. After the fix:
```
BUILD SUCCESSFUL in 45s
32 actionable tasks: 3 executed, 29 up-to-date
```
Only remaining output: the same pre-existing non-blocking NeoGradle deprecation warning (`Run.getProgramArguments()`) seen since Phase 1.

**Unresolved limitations / things that must be tested in-game (compilation cannot prove these):**
- Whether `InteractGuardHandler` actually fires before Guard Villagers' `mobInteract` in practice, and an unowned guard's right-click genuinely opens the hire menu instead of silently consuming the click.
- Whether the emerald count/deduction and the "not enough" / "already owned" messages behave correctly with real inventories.
- Whether `GuardOwnership.setOwner` visibly applies the gold custom name and chat message, and whether the guard's ownership is then correctly recognized by Guard Villagers' own systems (e.g. re-interacting opens GV's inventory GUI instead of our hire menu).
- Whether `RegisterMenuScreensEvent` genuinely renders `HireGuardScreen` correctly (layout, button, text wrap) given the vanilla API drift already found once in this stage.
- General runtime stability - no crash/exception on interaction, menu open/close, or hiring.

### 15.1 In-game validation (2026-09-13)

You tested Phase 2C's hiring/ownership flow manually, as required before Phase 2D could even be discussed. Reported, confirmed-working behaviors:
- Right-clicking an unowned guard opens Rally of the Guard's `HireGuardScreen` (not Guard Villagers' own inventory GUI, and not a silently-consumed click) - confirms `InteractGuardHandler` genuinely intercepts before `mobInteract`, and confirms `RegisterMenuScreensEvent` correctly renders the screen (both were unresolved/only-compile-tested items above).
- Hiring charges the correct emerald amount, and re-interacting with an already-hired guard does **not** re-open the hire menu (it lets Guard Villagers' own systems take over instead) - confirms `GuardOwnership.setOwner`'s `setOwnerId(UUID)` call is genuinely recognized by Guard Villagers' own ownership checks, not just by our own code.
- Guard Villagers' own native follow toggle (opened via its own inventory GUI once a guard is owned) works correctly against a Rally-of-the-Guard-hired guard: toggling it on makes the guard stop/stand still, toggling it off makes it move/follow again - confirms our ownership write doesn't fight or desync from Guard Villagers' own `isFollowing()`/`setFollowing(boolean)` state.
- Ownership and follow state both survive closing and reopening the world - confirms the typed-API persistence path (`SynchedEntityData` -> the same `Owner`/`Following` NBT keys, per §4.2) round-trips correctly through a real save/load, not just in a single play session.
- No crash or exception was reported at any point in this flow (menu open, hire, re-interact, toggle, world close/reopen) - general runtime stability confirmed.

Not separately called out by the user one way or the other, so not independently confirmed (only static-verified in §4.2/§15): the gold custom name and the `message.rallyguard.guard_presenting` chat message `GuardOwnership.setOwner` applies on hire. Worth a quick look next time a guard is hired, but nothing suggests it's broken.

**Net result: Phase 2C is validated in-game, not just build-verified.** All five items listed as "cannot be proven by compilation alone" in §15 above are now confirmed, except the cosmetic gold-name/message detail, which is low-risk and unconfirmed rather than failing.

## 16. Phase 2D status (2026-09-13) — VERIFIED: `.\gradlew.bat build` succeeds

Stage D per §10: "Networking — NetworkBootstrap, all three payload records, GuardCommandNetworking server-side handlers. No UI consumes them yet beyond what C already needs."

**What was implemented:**
- `network/NetworkConstants.java` — new. Just the two action codes (`ACTION_SUMMON=1`, `ACTION_TOGGLE_PATROL=2`), matching the Forge/Fabric ports' numbering exactly.
- `network/payload/OpenGuardCommandC2SPayload.java`, `GuardActionC2SPayload.java`, `GuardListS2CPayload.java` — new. Three `CustomPacketPayload` records (the architecture named in §2.1), verified field-by-field against real, compiling NeoForge 1.21.1 source: the `Type<T>`/`StreamCodec` shape and the `ByteBufCodecs.INT`/`STRING_UTF8`/`BOOL`/`StreamCodec.composite` calls all match Guard Villagers' own `GuardFollowPacket`/`GuardSetPatrolPosPacket`/`GuardOpenInventoryPacket` exactly (same repo §4.2/§12.6 already verified against). Two things Guard Villagers itself has no example of, verified instead against the official NeoForged 1.21.x networking docs: `StreamCodec.unit(...)` for `OpenGuardCommandC2SPayload`'s zero fields, and `Entry.STREAM_CODEC.apply(ByteBufCodecs.list(4096))` for `GuardListS2CPayload`'s `List<Entry>` field.
- `network/NetworkBootstrap.java` — new. `RegisterPayloadHandlersEvent`/`PayloadRegistrar`/`registrar(MOD_ID).versioned("1")`/`playToServer`/`playToClient` — the exact mechanism verified against Guard Villagers' own `GuardVillagers.java#register(RegisterPayloadHandlersEvent)` (§6.2). `SimpleChannel`/`NetworkRegistry.ChannelBuilder`, what the Forge port used here, does not exist on NeoForge 1.21.1.
- `network/GuardCommandNetworking.java` — new. Server-side logic for all three payloads, ported from the Forge/Fabric `GuardCommandNetworking`, with two deliberate, documented departures from the donor code (both in the file's own javadoc, not hidden):
  1. **Typed API throughout, not NBT-poking.** Following/patrolling/patrol-pos are now read and written via Guard Villagers' own `isPatrolling()`/`setPatrolling(boolean)`/`setFollowing(boolean)`/`setPatrolPos(BlockPos)` (confirmed null-safe by reading `Guard.java`'s own `Optional.ofNullable(position)` implementation), instead of the donor ports' `saveWithoutId`/`load` round-trip — the same policy §4/§4.2 already established for ownership in Phase 2C, now extended to the rest of the guard-state contract.
  2. **Dropped the dead `rallyguard:in_rally` NBT key** on the summon action, rather than porting it forward — §4.2 point 4 already found this key can never actually be read back on a fresh `saveWithoutId()` call (Rally of the Guard doesn't subclass `Guard` to declare it), so it was very likely always a no-op in both existing ports. Nothing in the MVP's observed behavior depends on it.
  - Messages use `ServerPlayer.sendSystemMessage(Component)` (chat), matching the exact overload already verified compiling in `GuardOwnership` during Phase 2C, instead of the donor ports' `displayClientMessage(Component, true)` (action bar) — a deliberate choice to reuse a proven call shape instead of assuming an unverified one, flagged here for a UI-polish look once the Ledger GUI exists.
  - Server→client send uses `player.connection.send(payload)`, the same call shape Guard Villagers' own `Guard.java` (`openGui()`) uses to push its own inventory-open packet to a specific client.
  - The guard-list lookup sweeps a giant `AABB` filtered by `GuardOwnership.isOwnedBy(...)`, typed to the compile-time `Guard` class instead of a `ForgeRegistries`/`EntityType` lookup by `ResourceLocation` — the same simplification `GuardOwnership.isGuard(Entity)` already made in Phase 2C, applied here for consistency.
- `RallyOfTheGuard.java` — now also calls `modEventBus.addListener(NetworkBootstrap::register)`, mirroring Guard Villagers' own `modEventBus.addListener(this::register)`. The Commander's Ledger item, patrol UI, rally logic, and friendly-fire/neutrality handlers remain unregistered.

**No entity code was touched** — this stage is pure networking plumbing plus the server-side handlers named in §10; no new gameplay behavior is reachable by a player yet, since nothing sends `OpenGuardCommandC2SPayload` from a UI (that's Phase 2E's Commander's Ledger item/screen).

**Build result — real, user-executed, not simulated:** you ran `.\gradlew.bat build` on your own Windows machine and reported `BUILD SUCCESSFUL`. As with every prior stage, this was not executed by Claude — this session's own shells (cloud sandbox and the `device_bash` bridge alike) remain blocked from the Gradle Plugin Portal/NeoForge/Mojang infrastructure this build needs (§11). This is the authoritative verification for Phase 2D's compile-time correctness: the three payload records, `NetworkBootstrap`'s `RegisterPayloadHandlersEvent` registration, and `GuardCommandNetworking`'s typed-API server-side handlers all compile cleanly against the real `guardvillagers-2.4.11-1.21.1.jar` and NeoForge 21.1.215.

**Compilation cannot prove the following — deferred to Phase 2E, when a UI can actually trigger these packets (no in-game test is possible for Phase 2D on its own, since nothing sends `OpenGuardCommandC2SPayload` yet):**
- Whether the giant-`AABB` guard sweep actually finds a player's owned guards in a real world (no reason to expect not — same pattern as the already-tested Forge donor — but untested here).
- Whether the patrol toggle's `setPatrolPos`/`setPatrolling`/`setFollowing` sequence drives the guard to the given `BlockPos` the way Guard Villagers' own `GuardVillagerPatrolGoal` (visible in its source, § Appendix) expects.
- Whether the summon teleport (`guard.moveTo(...)`) looks/feels right next to the player.
- General runtime stability of the new payload registrations (no version-string mismatch, no missed registration) — the classic first-launch networking failure mode per §9.

## 17. Phase 2E status (2026-09-13) — VERIFIED: `.\gradlew.bat build` succeeds; AWAITING IN-GAME TEST

Stage E per §10: "Commander's Ledger — CommandersLedgerItem, GuardCommandScreen, wire up summon/patrol-toggle actions client→server."

**What was implemented:**
- `client/ClientHooks.java` — **new**, named in the original architecture plan (§2.1/§5: "just sends a packet via the new payload API") but not created until now, since nothing needed it before this stage. One method, `requestOpenGuardPanel()`, sends `OpenGuardCommandC2SPayload` to the server via `PacketDistributor.sendToServer(...)`.
- `item/CommandersLedgerItem.java` — `use(...)` now ported from the Forge 1.20.1 version: on the client side, calls `ClientHooks.requestOpenGuardPanel()` and returns `InteractionResultHolder.success(...)`; on the server side, `InteractionResultHolder.pass(...)` (unchanged from Forge). The Fabric donor reached its client-only hook through reflection (`Class.forName(...)`) — not needed here, since the Forge donor itself proves a direct call under a `level.isClientSide()` guard is safe on Forge/NeoForge (this project doesn't split client/common source sets, §2.1), so `ClientHooks` is called directly.
- `screen/GuardCommandScreen.java` — **new**. The Commander's Ledger panel: lists a player's owned guards (paginated, 4 per page) with a Summon and a Patrol/Stop button per row, ported from the Forge 1.20.1 `GuardCommandScreen`. Two changes applied: `renderBackground(GuiGraphics)` → `renderBackground(GuiGraphics, int, int, float)` (the same MC 1.20.1→1.21.1 signature change already found and fixed in `HireGuardScreen` during Phase 2C, §6.3), and `NetworkBootstrap.CHANNEL.sendToServer(...)` (Forge `SimpleChannel`) → `PacketDistributor.sendToServer(...)` (the same call shape Guard Villagers' own `GuardInventoryScreen.java` uses for its follow/patrol buttons — real, verified source, not guessed). `openFromPayload(...)` calls `Minecraft.getInstance().setScreen(...)` directly, with no extra thread-marshaling wrapper — unlike the Fabric donor's `MinecraftClient.getInstance().execute(...)`, this isn't needed because `IPayloadContext.enqueueWork(...)` (see below) already guarantees the handler runs on the client main thread.
- `network/GuardCommandNetworking.java` — `handleGuardList(...)` (previously a Phase 2D no-op placeholder) now calls `GuardCommandScreen.openFromPayload(payload)` inside `context.enqueueWork(...)`, matching the same enqueue-then-act pattern Guard Villagers' own `GuardOpenInventoryPacket.handle(...)` uses for its S2C inventory-open packet.

**No entity, ownership, or menu-registration code was touched** — this stage only makes the Phase 2D networking reachable from an actual item a player can hold and use.

**One verification gap, noted rather than hidden:** `Item.use(Level, Player, InteractionHand)`'s signature was not independently re-checked against mappings.dev this stage (unlike `appendHoverText`'s `Item.TooltipContext` change, caught in Phase 2B) — it is trusted on the strength of the Forge 1.20.1 donor already compiling against this exact signature shape, combined with no prior stage's verification work (Phase 2B/2C/2D, all of which touched adjacent vanilla `Item`/`Screen` APIs) ever flagging `Item.use` as changed. If the build below disagrees, this is the first place to look.

**Build result — real, user-executed, not simulated:** `BUILD SUCCESSFUL`, reported directly by the user. As with every prior stage, Claude did not and cannot execute this build itself (§11's network-allowlist limitation).

**Compilation cannot prove the following — this is the actual in-game test checklist for Phase 2E, still pending:**
- Using the Commander's Ledger item actually opens the panel, and it lists guards you own (not guards owned by nobody or by someone else).
- The Summon button teleports the correct guard next to you; the Patrol/Stop button toggles patrol and the button's own label updates (optimistic local update in `GuardCommandScreen.rebuildButtons()`) to match.
- Pagination (`<`/`>` buttons) behaves correctly with 5+ owned guards; the empty-list message shows correctly with 0 owned guards.
- No crash on open/close/repeated use, and the panel doesn't darken the background oddly (the `renderBackground` override).

### 17.1 In-game test result and real bug found (2026-09-13)

You tested Phase 2E: the Commander's Ledger panel opens correctly, but a freshly-hired guard did not appear in its list, even though the "Guard %s reporting for duty" chat message confirmed `GuardOwnership.setOwner(...)` had run.

**Root cause, found via `logs/latest.log`, not guessed:** the Sable mod (installed in this modpack for its airship "sub-levels") wraps the level's entity-getter and aborts any query whose `AABB` it considers "abnormally large," logging `[Sable/ERROR]: Aborting entity get for abnormally large AABB` and returning nothing. `GuardCommandNetworking.sendGuardList(...)`'s world-wide sweep (`new AABB(-3.0e7, ..., 3.0e7, ...)`, copied from the Forge/Fabric donors as noted in the original Phase 2D writeup) tripped this guard on every single Commander's Ledger open — 57 occurrences in the log, one per open. Ownership was never the problem; the guard-list query was silently emptied by another mod before it ever reached `GuardOwnership.isOwnedBy(...)`.

**Fix:** `sendGuardList` now uses `ServerLevel#getEntities(EntityTypeTest<Entity, T>, Predicate<? super T>)` (confirmed via the official 1.21.1 Mojang mappings) instead of `getEntitiesOfClass(Class, AABB, Predicate)` - no bounding box at all, so Sable's guard never triggers, and no arbitrary box size to get wrong for a large world in the first place. `EntityTypeTest.forClass(Guard.class)` builds the type test from the compile-time `Guard` class, consistent with the project's existing preference (`GuardOwnership.isGuard(Entity)`, Phase 2C) for the typed class over a registry/`ResourceLocation` lookup.

This is a real, environment-specific mod interaction (Sable + a naive world-wide AABB sweep), not a Rally of the Guard logic error and not something any amount of re-reading the ownership code would have found - it only showed up by reading the actual server log from a real test.

**Build result — real, user-executed, not simulated. Two attempts:**
1. First attempt: `BUILD FAILED` - `List<Guard> guards = level.getEntities(...)` doesn't compile, because that overload returns `List<? extends T>`, not `List<T>` (Java generics invariance - a compiler-caught mistake, not a wrong API choice).
2. After changing the declared type to `List<? extends Guard> guards = ...` (no other change - the `for (Guard guard : guards)` loop below already worked fine against a wildcard list): `BUILD SUCCESSFUL`, reported directly by the user.

**Phase 2F:** not started. Per the standing rule, Phase 2E is not to be claimed as validated until the guard list actually shows owned guards and the rest of the checklist passes - this section will be updated with results before Phase 2F is even discussed.

### 17.2 Second in-game test round: three real questions and one more real bug (2026-09-13)

You confirmed horse mounting, bow usage, Ledger guard listing, and summon-while-unmounted are all working well after the Sable fix. This round raised four points; each is answered below with what was actually checked in the real source (not guessed), consistent with the project's standing evidence-first approach.

**1. Bug: guards can't be Summoned via the Commander's Ledger while mounted on a horse.** Confirmed as a real bug in `GuardCommandNetworking.summon(...)`: a vehicle repositions its passenger every tick (vanilla `Entity#positionRider` behavior), so calling `guard.moveTo(...)` on a guard that is still a passenger of a horse has no lasting effect — the horse simply puts the guard back on itself the next tick. **Fix applied:** `summon(...)` now calls `guard.stopRiding()` (a plain vanilla `Entity` method, not Guard Villagers-specific) before `moveTo(...)` whenever `guard.isPassenger()` is true. **Build result — real, user-executed:** `BUILD SUCCESSFUL`, reported directly by the user. The fresh jar has been redeployed to the mods folder and the deployed jar's bytecode was spot-checked (`unzip -p ... GuardCommandNetworking.class | strings | grep`) to confirm `isPassenger`/`stopRiding` are actually present in what the game loads, not just in source. **Still pending: an actual in-game retest** — summon a horse-mounted guard and confirm it dismounts and teleports correctly; this fix is build-verified but not yet in-game validated.

**2. Question: can hired guards damage/attack each other?** Checked directly against Guard Villagers' own `Guard.java` target-selection code (not guessed): the one goal that could make a guard retaliate against whatever hurt it, `HurtByTargetGoal(this, Guard.class, IronGolem.class)` (line 597), explicitly excludes `Guard` and `IronGolem` from that retaliation — a guard hit by another guard (including accidental arrow crossfire) will not target it back. Separately, none of the guard's other targeting goals (`NearestAttackableTargetGoal` variants for hostile mobs/raiders/players, `DefendVillageGuardGoal`, `HeroHurtByTargetGoal`/`HeroHurtTargetGoal`) ever add `Guard` as an attackable type — `HeroHurtByTargetGoal.canAttack`/`HeroHurtTargetGoal.canAttack` (lines 1666, 1704) both explicitly filter out `Guard` too. **Answer: no, by Guard Villagers' own design, hired guards will not attack or retaliate against each other**, independent of anything Rally of the Guard adds. This is native behavior, not something this port's friendly-fire system (Phase 2H, not started) needs to provide.

**3.1 Real bug found and fixed 2026-09-13, in the mounts datapack itself (not this mod's Java code):** after starting a Rally, guards later auto-mounted onto a horse by `mount_logic.mcfunction` stopped following entirely - both guards alone on a horse and guards sharing one. Root cause: the datapack's periodic auto-mount rule only checked whether a guard was *already* mounted (`nbt=!{RootVehicle:{}}`), not whether it was actively following or patrolling - so a rallied (or patrolling) guard could get swept onto a horse well after the rally/patrol command ran, and once mounted, `FollowHeroGoal`/`WalkBackToCheckPointGoal` can't drive it (a vehicle controls its passenger's position every tick, the same root cause behind the mounted-summon and mounted-patrol-order bugs already fixed in `GuardCommandNetworking`/`ScrollOfRallyingItem` - this is the same issue at a different trigger point, one this mod's Java code has no visibility into since the datapack mounts guards on its own schedule). **Fixed** by adding `nbt=!{Following:1b}, nbt=!{Patrolling:1b}` to the auto-mount rule's guard-side selector in `GuardVillagersMounts-v1.2.zip` (edited directly in the world's `datapacks` folder, not the mod's git repo - this datapack is separate content, not shipped by this mod). **Requires `/reload` in-game (or reopening the world) to take effect - not yet confirmed working**, since datapack function files are only read at world/datapack load time.

**3. Curiosity (not a fix request — explicitly not asking to remove this): why can multiple guards mount the same horse, and could this extend to something like a Leviathan?** The mounts datapack's `mount_logic.mcfunction` uses a plain vanilla `/ride ... mount` command (`ride @s mount @e[type=minecraft:horse, nbt=!{Passengers:[{},{}]}, distance=..4, limit=1, sort=nearest]`) to place a guard on the nearest horse. Two things likely combine to allow more than one guard onto the same horse: (a) `/ride ... mount` is a command-driven force-mount — unlike a player right-clicking a horse, it does not go through the game's normal single-rider capacity check for that entity type; (b) the pack's own "already occupied" filter (`nbt=!{Passengers:[{},{}]}`) is meant to exclude horses that already have a rider, but this is the pack's own custom logic layered on top of a vanilla command, not a Guard Villagers or Rally of the Guard mechanic. I want to flag that, unlike the Sable bug, **I have not proven this against an actual test the way I did with the real server log** — this is the most likely mechanism reading the actual command, not a confirmed root cause. Since you said you're not sure you want to remove this, no change was made. On the Leviathan idea: this mechanism is generic to any entity a similar `/ride ... mount` selector targets — right now `mount_logic.mcfunction` only selects `minecraft:horse` by type, so guards will not auto-mount a Leviathan (or anything else) out of the box. Extending it would mean writing an equivalent selector for that entity type in the datapack — happy to do that if/when you actually want it, but it's not started and wasn't assumed from this message.

**4. Question: guards only fight hostile mobs, not a target I personally attack — plus, is there a way to make them follow me?** Both trace back to the same root cause, found in Guard Villagers' own source: `HeroHurtTargetGoal`/`HeroHurtByTargetGoal` (the goals that would make a guard join in on whatever its owner is fighting, or retaliate for its owner) only activate when `Guard#getOwner()` returns non-null, and that getter itself returns `null` whenever GuardConfig's `followHero` setting (default `true`, confirmed at `GuardConfig.java` line 140) is on and the player lacks the vanilla **Hero of the Village** status effect. The Forge donor mod ships a whole `GuardVillagersConfigPatcher` utility (reflection + on-disk config-file surgery) specifically to force `followHero` to `false` at runtime so none of this requires that effect — and our own `ModClient.java` already has a comment flagging this as explicitly deferred to **Phase 2I**, not yet started. So: **combat backup and continuous following are both currently gated behind the same unported `followHero` patch**, not a bug in anything built so far. The other route to "guards follow me" — the **Scroll of Rallying** (Phase 2G, not started) — is the mass version of this (gathers all nearby non-patrolling owned guards and sets them following, with its own friendly-fire protection) and would need Phase 2I's `followHero` fix underneath it anyway to actually walk after you continuously. In the meantime, Guard Villagers' own native per-guard follow toggle (right-click an owned guard **without** sneaking to open its own inventory GUI, then toggle follow) already works today per the Phase 2C validation (§15.1) — worth checking whether that toggle actually keeps the guard walking with you right now despite `followHero`, since if GV's own UI already flips `isFollowing()` directly it may not depend on `getOwner()` the same way `FollowHeroGoal` reads it; this hasn't been re-tested against the `followHero` finding above and would be worth confirming next time you're in-game.

None of Phase 2F/2G/2H/2I is authorized or started by this section — this is research plus one targeted bug fix (point 1), per the standing rule that real bugs in already-shipped phases get fixed without needing new phase authorization, while new phases still need an explicit go-ahead.

### 17.3 Backlog / future ideas (not started, not authorized) — noted 2026-09-13

Two ideas the user wants to remember for later, not requested for implementation yet:
- **Guard inventory for ammo/arrows.** The user's guards appear to run out of ammunition (mentioned as "pistolas" - likely a ranged weapon from another installed mod being used via Guard Villagers' item-in-hand system, or possibly the crossbow/bow itself). Would need to look at how Guard Villagers manages a guard's held/offhand items and whether it exposes any inventory slot(s) for ammo restocking (Guard Villagers does have its own inventory GUI already, opened by right-clicking an owned guard - worth checking first whether ammo can already be added there before assuming new code is needed).
- **Mounting other creature types.** Currently the mounts datapack (`GuardVillagersMounts-v1.2.zip`, `mount_logic.mcfunction`) only targets `minecraft:horse`. Extending to other mountable/rideable entities (including modded ones) would mean writing additional `/ride ... mount` selectors per entity type in the datapack - same mechanism, not a Java/mod-code change.

Neither of these is part of the original Forge donor's feature set (§0/README) - both are new ideas beyond the porting plan's original scope. If pursued, they'd need their own numbered phase(s) after 2F-2I.

**Expanded 2026-09-13 (same day, after liking the "Recall All"/health-bar/status ideas from the previous round):** the user wants a way to see their total guard count and names, ideally with guards separable by armor/weapon loadout (Guard Villagers guards do carry a `GUARD_VARIANT` string field and equipment, both readable per-entity), plus a Commander's Ledger view that can handle hundreds of guards at once rather than the current 4-per-page list. This is a real scaling concern for the existing `GuardCommandScreen`/`GuardListS2CPayload`: right now every owned guard within the world-wide sweep is sent over the network and rendered as a flat paginated list every single time the Ledger opens - fine for a handful of guards, but not designed for hundreds (payload size, per-open server-side sweep cost, and a flat list UI). If pursued, this would likely need: a payload/UI redesign (grouping by variant/loadout, a count summary, possibly a search/filter instead of pure pagination) - a bigger, separate piece of work from anything in 2F-2I, not started.

## 18. Phase 2F status (2026-09-13) — analysis complete, one defensive fix added, AWAITING BUILD

Stage F per §10: patrol - largely already implemented via the typed API in Phase 2D; this stage is about confirming that's actually true and tuning/fixing anything real.

**Analysis, read directly from Guard Villagers' own `Guard.java` (not guessed):** the AI goal that actually walks a guard to its patrol position, `WalkBackToCheckPointGoal` (registered at goal priority 3, ahead of `FollowHeroGoal` at priority 4 - patrol correctly takes precedence over following), consumes exactly the four pieces of state `GuardCommandNetworking.togglePatrol(...)` already sets: `isPatrolling()`, `getPatrolPos()`, `isFollowing()`, and `getTarget()` (cleared by `resetNavigation(...)`). Its only other gate is a 200-game-tick cooldown (`delayTime`) that only gets set when a previous patrol attempt gave up on an unreachable path - it starts at `0`, so a guard's first patrol order is never blocked by it. **Conclusion: Phase 2D's typed-API wiring is sufficient on its own; no new patrol-walking code was needed.**

**One real gap found and fixed anyway, by direct analogy with the summon bug (§17.2 point 1):** `WalkBackToCheckPointGoal` walks the guard via `Mob#getNavigation()`, which cannot move an entity that is currently a vehicle's passenger - so ordering a horse-mounted guard to patrol would silently never start walking, the same underlying cause as the mounted-summon bug. `togglePatrol(...)`'s patrol-on branch now also calls `guard.stopRiding()` first when `guard.isPassenger()`, mirroring `summon(...)`.

**Not changed:** the patrol-off branch, `resetNavigation(...)`, and everything else - no reason found to touch them.

**Build result:** pending - this fix has not been compiled yet. **Next step: rebuild, then in-game test the full patrol loop** (order patrol on a guard, confirm it walks to and holds the given position; order patrol on a horse-mounted guard, confirm it dismounts and walks; toggle patrol off, confirm it stops holding position and can resume following/normal behavior).

## 19. Phase 2G + minimal Phase 2I status (2026-09-13) — code complete, AWAITING BUILD

You asked to proceed with 2F (done, §18) and, since that turned out to be small, go ahead into 2G as well. Doing 2G alone would not have actually produced working "follow" behavior, for a reason found during the §17.2 research pass, so a minimal Phase 2I was implemented alongside it - explained below rather than silently expanding scope.

**Phase 2G - Scroll of Rallying (`item/ScrollOfRallyingItem.java`):** ported from the Forge 1.20.1 donor's `use(...)`. Shift + right-click toggles a rally: gathers nearby (100-block range) owned, non-patrolling guards into a fan formation around the player and sets them following; using it again while active releases them (`setFollowing(false)` on nearby owned guards) and removes the `RALLY_COMMANDER` effect (already registered since Phase 2B). Two departures from the donor, consistent with this project's standing policy:
- Guard state read/written through the typed API (`isFollowing`/`setFollowing`/`isPatrolling`) instead of the donor's `saveWithoutId`/`load` NBT round-trip, and the dead `rallyguard:in_rally` key dropped (same policy as Phase 2C/2D/2E/2F).
- The owned-guard lookup uses the same bounds-free `ServerLevel#getEntities(EntityTypeTest, Predicate)` overload the Phase 2E Sable fix already proved safe, with the 100-block range folded into the predicate as a `distanceToSqr` check, instead of `getEntitiesOfClass(..., AABB, ...)`. (A 100-block box is nowhere near what tripped Sable's "abnormally large AABB" guard - this choice was about reusing an already-verified overload, not about a second Sable risk.)
- Each rallied guard is dismounted first if `isPassenger()`, the same mounted-guard fix already applied to `summon(...)` and `togglePatrol(...)` (§17.2/§18) - a horse-mounted guard can't be usefully repositioned or driven by AI without dismounting.
- The "active" glint (`isFoil`) flag is stored via 1.21.1's data component API (`DataComponents.CUSTOM_DATA` / `CustomData.update(...)`), not raw NBT (`stack.getOrCreateTag()` no longer exists post-1.20.5) - confirmed via mappings.dev. **This is a real verification gap, like `appendHoverText`'s `TooltipContext` change in Phase 2B**: first use of a data component for custom mod data in this project, not proven by any prior stage.

**Minimal Phase 2I - `util/GuardVillagersConfigPatcher.java`:** forces Guard Villagers' `GuardConfig.COMMON.followHero` config to `false` from an `FMLCommonSetupEvent` handler. Why this couldn't wait: `Guard#getOwner()` (read directly from Guard Villagers' own source) returns `null` whenever `followHero` is `true` (its default) and the player lacks the vanilla Hero of the Village effect - and `getOwner()` gates not just `FollowHeroGoal` (continuous following) but `HeroHurtByTargetGoal`/`HeroHurtTargetGoal` (combat backup) too. Without this, the Scroll of Rallying's `setFollowing(true)` would have no visible effect beyond the initial teleport - guards would gather once and then just stand there, which is exactly the "guards don't really follow me" symptom already reported. A real, deliberate departure from the Forge donor's own `GuardVillagersConfigPatcher`: that version used reflection plus on-disk config-file surgery because Forge's mod load order didn't guarantee direct compile-time access to Guard Villagers' classes; this project already depends on `tallestegg.guardvillagers` at compile time (same as `Guard`/`GuardOwnership` since Phase 2C), so `GuardConfig.COMMON.followHero` is reached directly as a public field, no reflection, no file parsing. Wrapped in a try/catch that only logs on failure, so a future Guard Villagers update changing this config's shape degrades to "the old Hero-of-the-Village requirement still applies" rather than crashing startup.

**Not touched:** friendly-fire/neutrality (the broader "don't help attack a guard I don't own" handler, Phase 2H) - the Scroll of Rallying's own effect-based protection is part of 2G's ported behavior, but the separate `HiredGuardsNeutralityHandler`-equivalent is still not started.

**Build result — real, user-executed. First attempt:** `BUILD FAILED` - a real compiler error, not the two flagged verification points. `LivingEntity#hasEffect`/`#removeEffect` and the `MobEffectInstance` constructor all take a `Holder<MobEffect>` in 1.21.1, not the raw `MobEffect` that `ModEffects.RALLY_COMMANDER.get()` returns - three call sites in `ScrollOfRallyingItem` failed to compile. **Fixed** by dropping `.get()` at all three sites: confirmed via NeoForge's own real source (`DeferredHolder<R, T extends R> implements Holder<R>, Supplier<T>`) that the holder itself already satisfies a `Holder<MobEffect>`-typed parameter directly. Also confirmed the exact 1.21.1 signatures via mappings.dev before making this change, rather than guessing from the compiler error alone. **Second attempt: `BUILD SUCCESSFUL`**, reported directly by the user. Both originally-flagged verification points (`DataComponents.CUSTOM_DATA`/`CustomData` and `GuardConfig.COMMON.followHero.set(...)`) compiled cleanly - confirmed real, not just assumed, since they were the only remaining unknowns after the Holder<MobEffect> fix above.

Jar redeployed to the mods folder and spot-checked directly in the deployed bytecode (`unzip -l`/`unzip -p ... | strings | grep`): `ScrollOfRallyingItem.class` and `GuardVillagersConfigPatcher.class` are both present; `ScrollOfRallyingItem.class` contains `stopRiding`/`isPassenger`/`setFollowing`/`hasEffect`/`removeEffect`; `GuardVillagersConfigPatcher.class` contains the real `tallestegg/guardvillagers/configuration/GuardConfig$CommonConfig` reference, the `followHero` field name, and all three log message strings. Same for the Phase 2F patrol dismount fix in `GuardCommandNetworking.class` (`stopRiding`/`isPassenger` present).

**Everything from Phase 2F, Phase 2G, and the minimal Phase 2I is now build-verified and deployed. None of it has been in-game tested yet** - that's the next step, per the checklist above (rally formation/dismount/follow, patrol on a mounted guard, and whether combat backup now works).

**In-game test checklist once built and deployed:**
- Shift + right-click the Scroll of Rallying: nearby owned, non-patrolling guards teleport into a fan formation around you (dismounting first if they were on a horse), the scroll gets an enchant-style glint, and you get the gold "strength gained" message.
- The rallied guards should now actually follow you as you walk away - the real point of the followHero patch. If they still just stand there, the patch likely didn't take (check the log line at startup: `"Guard Villagers followHero forced to false"` vs. an error).
- Guards currently patrolling should NOT be pulled into the rally.
- Shift + right-click again: guards stop following (the "strength lost" message, red), effect removed, glint gone.
- Bonus: with the followHero patch active, check whether hitting a hostile mob now pulls nearby owned guards into the fight even without a rally active (`HeroHurtTargetGoal`) - this was the "guards don't help me attack a target I attack" question from §17.2 point 4.

### 17.4 Minor issue noted, not investigated yet (2026-09-13)

User reports guards' own arrows/bolts sometimes hit horses (their own mount, or a nearby one) - explicitly flagged as low priority ("no me parece tan grave"), not urgent. Not yet investigated with real evidence (no log/source read done for this one). Likely candidate explanation, NOT confirmed: vanilla arrow/crossbow-bolt collision typically only excludes the shooter itself from self-hits for a few ticks after firing, not the shooter's own vehicle - so a mounted guard's projectile can still clip the horse it (or a nearby guard) is riding depending on firing angle/geometry, independent of anything this port added. Needs a real look (Guard Villagers' arrow-firing code, or vanilla `AbstractArrow`/`Projectile` collision-exclusion logic) before any fix is attempted - logged here so it isn't lost, not acted on yet.

## Appendix: raw grep evidence for key claims (for future reference / re-verification)

- `Guard.java` public API: `getOwnerId`/`setOwnerId`/`getOwner`/`isOwner`/`isFollowing`/`setFollowing`/`isPatrolling`/`setPatrolling`/`getPatrolPos`/`setPatrolPos` — all found via direct source read of `common/entities/Guard.java`, lines ~171–388, `1.21.1` branch, `seymourimadeit/guardvillagers`.
- `addAdditionalSaveData`/`readAdditionalSaveData` NBT keys (`Owner`, `Following`, `Patrolling`, `PatrolPosX/Y/Z`) — same file, lines ~205–308.
- `followHero` config key text: `"Have guards only follow the player if they have hero of the village?"`, default `true` — `configuration/GuardConfig.java` line 140. Identical key text to the Forge port's own reflection/TOML-patch target, confirming NBT-era config-key stability across major GV versions.
- Modern mod entry constructor `(ModContainer, IEventBus)`, `RegisterPayloadHandlersEvent`/`PayloadRegistrar`, `ModConfigSpec`, `DeferredRegister` from `net.neoforged.neoforge.registries` — `GuardVillagers.java`, full file.
- `[[mixins]]` toml block + `guardvillagers.mixins.json` (`compatibilityLevel: JAVA_21`) — `src/main/resources/META-INF/neoforge.mods.toml` and `src/main/resources/guardvillagers.mixins.json`.
- NeoForge build coordinates: `neo_version=21.1.215`, `neo_version_range=[20.4,)`, Java toolchain `21`, NeoGradle userdev `7.0.165` — `gradle.properties`/`build.gradle`, `1.21.1` branch.


## 20. Phase 2H status (2026-09-14)

Ported the Forge donor's two friendly-fire/neutrality handlers, `RallyFriendlyFireHandler`
and `HiredGuardsNeutralityHandler`, into `src/main/java/net/hfstack/rallyguard/event/`.

**RallyFriendlyFireHandler** - stops an owner from accidentally damaging their own
following/rallied guard. Two hooks, both cancellable:
- `AttackEntityEvent` (package moved from `net.minecraftforge...` to
  `net.neoforged.neoforge.event.entity.player...`, same shape) for a direct melee hit.
- `LivingIncomingDamageEvent` for everything else (sweep, projectiles, tridents). The
  donor's `LivingAttackEvent` does not exist in NeoForge 1.21.1 (confirmed: no such class
  anywhere under `net.neoforged.neoforge.event.entity.living` in the real NeoForge source
  tree) - `LivingIncomingDamageEvent` is the real replacement, firing from
  `LivingEntity#hurt(...)` "after invulnerability checks but before any damage
  processing" per its own javadoc, same interception point the donor relied on. Attacker
  identification (`DamageSource#getEntity()`/`getDirectEntity()`) is unchanged - both
  confirmed still present on `DamageSource` via mappings.dev for 1.21.1.

Whether a guard is "protected" uses `Guard#isFollowing()` alone (typed API, section 4.2),
not the donor's two-flag NBT check - `rallyguard:in_rally` was already established as
dead/no-op (section 4.2 point 4), so `isFollowing()` is the complete equivalent.

**HiredGuardsNeutralityHandler** - when a player hits a guard that ISN'T theirs, their own
nearby hired guards get a 10-second (`IGNORE_TICKS = 200`) grace window during which any
target-lock on the owner is cleared. Root cause this protects against: Guard Villagers'
`HurtByTargetGoal(this, Guard.class, IronGolem.class).setAlertOthers()` alerts nearby
allied guards whenever ANY guard is hurt, without scoping that by ownership - so a player
hitting an unrelated guard can cause their own hired guards to get alerted and target the
player (their own owner). Two hooks:
- `AttackEntityEvent` - on hitting an unowned/other guard, sweep the attacker's own nearby
  guards (24-block radius) via this project's established bounds-free
  `ServerLevel#getEntities(EntityTypeTest.forClass(Guard.class), Predicate)` pattern
  (first used in the Phase 2E Sable-AABB fix, reused by `ScrollOfRallyingItem`) - replaces
  the donor's registry lookup + `AABB` + `getEntitiesOfClass(Entity.class, ...)` +
  `instanceof Mob` cast. Results come back already typed as `Guard`.
- `LevelTickEvent.Post` (NeoForge 1.21.1's dedicated end-of-tick event, replacing the
  donor's `TickEvent.LevelTickEvent` + manual `phase == END` check) - decrements the
  in-memory tracking map every tick and re-clears any target that re-locks onto the owner
  during the grace window.

Both handlers registered on `NeoForge.EVENT_BUS` in `RallyOfTheGuard`'s constructor, same
pattern as `InteractGuardHandler`.

**Status: `BUILD SUCCESSFUL` (user-confirmed), deployed to the mods folder,
bytecode-verified, and IN-GAME VALIDATED** - `unzip -p ... | strings` on the actual
deployed jar confirms both handler classes present, referencing the correct NeoForge
1.21.1 event types (`AttackEntityEvent`, `LivingIncomingDamageEvent`,
`LevelTickEvent$Post`) and the typed `isFollowing`/`getEntities` calls, and both
registered in `RallyOfTheGuard.class`. **In-game test result (user-confirmed,
2026-09-14): "no reciben daño, ni tampoco reaccionan si le pego a un guardia aparte"** -
both behaviors confirmed working: the owner's own following/rallied guard takes no
friendly-fire damage, and hitting an unrelated guard does not cause the player's own
guards to turn hostile. This completes every phase of the original porting plan -
everything after this is user-directed expansion (Phase 3 Command System, Escort
Formation/Patrol Routes, Renegade Guard Squads; tracked in the published roadmap
Artifact and in project memory, not in this document).


## 21. Phase 3A status (2026-09-14)

First stage of the user-directed Phase 3 Command System (see the published roadmap
Artifact and project memory for the full five-stage plan: 3A here, then 3B Ledger
persistent roster, 3C Stance, 3D Orders, 3E Home point).

**Squad membership**, a completely new concept this port didn't have before, added as
`contract/SquadRegistry.java`: a `SavedData` subclass storing named squads (id, owning
player UUID, member guard UUIDs) keyed by squad id, attached to the OVERWORLD's data
storage (`server.overworld().getDataStorage()`) so squads are one world-global registry
regardless of which dimension a guard or its owner is in - not per-dimension state.
Members are stored by guard UUID rather than runtime entity id, since UUIDs are the only
identifier that survives a guard unloading and reloading; this is deliberately the same
approach Phase 3B's persistent roster will need, just scoped down to membership only for
this stage.

**Real API verification note, important for anyone touching `SavedData` in this project
again:** the locally cloned `/tmp/neoforge-src` used to verify Phase 2H's event classes
turned out to be on NeoForge's `26.2.x` branch (confirmed via `git branch --show-current`
and `gradle.properties`' `minecraft_version=26.2`) - several Minecraft versions AHEAD of
this project's actual 1.21.1/NeoForge 21.1.215 target. Its `SavedData`-related classes
(`LevelAttachmentsSavedData`, using a Codec-based `SavedDataType` and the newer
`Identifier` class) reflect a LATER Mojang refactor of `SavedData` that does not apply to
1.21.1. Caught before writing any code by checking the clone's own branch/version instead
of assuming it still matched - `SquadRegistry` was written against mappings.dev's
1.21.1-pinned pages instead, which confirmed the real 1.21.1 shape: abstract
`SavedData#save(CompoundTag, HolderLookup.Provider)`, the
`SavedData.Factory<T>(Supplier<T> constructor, BiFunction<CompoundTag, HolderLookup.Provider, T> deserializer, DataFixTypes type)`
record, and `DimensionDataStorage#computeIfAbsent(Factory<T>, String)`. **`/tmp/neoforge-src`
should not be trusted again for anything beyond what's already been cross-checked
in-game (the Phase 2H event classes) - re-clone the correct 1.21.1-era branch/tag before
relying on it for a new API.**

**Networking**: one new C2S payload, `AssignSquadC2SPayload(List<Integer> entityIds,
String squadName)` - a blank `squadName` after trimming means "remove from squad,"
matching the same blank-means-none convention added to `GuardListS2CPayload.Entry`'s new
`squadName` field (no extra optional-string codec needed either way).
`GuardCommandNetworking.assignSquad(...)` re-checks ownership per guard exactly like
every other action on this channel before touching `SquadRegistry`, then re-sends the
full guard list so the client's squad labels resync from the server in one shot (a
deliberate difference from `summon`/`togglePatrol`, which mutate the client's local copy
optimistically - a multi-entity batch is simpler to just let the server resolve
authoritatively). `sendGuardList` now also looks up each guard's current squad via
`SquadRegistry#squadOf(UUID)` and includes it in the entry sent to the client. The
`StreamCodec.composite` arity for `GuardListS2CPayload.Entry` went from 3 fields to 4 -
confirmed via mappings.dev that 1.21.1's `StreamCodec.composite` supports up to 6 fields,
not assumed.

**Commander's Ledger GUI**: `GuardCommandScreen` gets a per-row select toggle (state kept
in a `Set<Integer>` of entity ids, surviving page changes since it's a screen field, not
rebuilt from the entry list), a squad name `EditBox` (this project's first use of that
widget - constructor shape confirmed via mappings.dev: `EditBox(Font, x, y, width,
height, Component)`), and Assign/Clear buttons that send `AssignSquadC2SPayload` for
every currently-selected entity id at once. Panel height grew from 200 to 232px to fit
the new bottom control row without touching the existing row/pagination layout math.
Each guard's squad, when set, is appended after its name in the list ("Aldric — Norte"),
reusing the row's existing text-truncation logic unchanged since it already worked on any
string, not just a bare name.

**First build attempt: `BUILD FAILED`, real compiler error, not the SavedData risk
flagged above.** `GuardCommandScreen.java:195: cannot find symbol: method
setPlaceholder(MutableComponent)` on the new `EditBox` - the earlier mappings.dev
verification for this class invented a `setPlaceholder(Component)` method that doesn't
exist in 1.21.1's real `EditBox`. **Fixed** by re-verifying against a second, more
literal source (a version-pinned NeoForge javadoc mirror, nekoyue's ForgeJavaDocs-NG,
`.../javadoc/1.21.x-neoforge/...`) instead of trusting the first summary - the real
method is `setHint(Component)`. Swapped the one call site
(`squadNameBox.setPlaceholder(...)` -> `squadNameBox.setHint(...)`); `setValue`,
`setResponder`, `setMaxLength` were all confirmed correct by the same second source, no
other changes needed.

**New general lesson for this project: a `WebFetch`-summarized page (including
mappings.dev) can still invent a plausible-sounding method name that doesn't exist -
summarization is a lossy, LLM-mediated step, not a direct read of the source.** When a
brand-new API is being used for the first time (like `EditBox` here) and the build fails
on it, re-verify against a second, independent, more literal source (a javadoc mirror
listing real signatures directly, or the actual decompiled source if reachable) rather
than assuming the first fetch was authoritative just because it named a real, versioned
URL.

**Status: `BUILD SUCCESSFUL` (confirmed by the user, full PowerShell output showing
`BUILD SUCCESSFUL in 1m 1s`, 32 actionable tasks). Jar redeployed to the mods folder
(`cp` to `veamos--mods`) and bytecode-verified: `SquadRegistry.class`,
`SquadRegistry$Squad.class`, `AssignSquadC2SPayload.class`, and the rebuilt
`GuardCommandScreen.class` are all present in the jar; `strings` on
`GuardCommandScreen.class` confirms `setHint` is present and `setPlaceholder` is gone.**
Phase 3A is now build-verified and deployed - next is the user's in-game test (assign a
few guards to a squad via the Commander's Ledger's new multi-select + Assign button,
confirm the squad name shows next to each guard's name, confirm Clear removes it) before
calling Phase 3A fully validated and moving on to Phase 3B.

## 22. Phase 3A.1 - post in-game-test refinements (2026-09-14)

Phase 3A passed real in-game testing with no bugs found. While reviewing it, three real,
concrete follow-ups came up and were built in the same sitting, all as small additions on
top of already-validated code rather than new phases of their own:

**1. Squad picker button (`GuardCommandScreen`).** Typing a squad name by hand risks a
silent typo landing a guard in a brand-new squad instead of the intended one
(`SquadRegistry.assign` matches names case-insensitively, but still needs an exact
match otherwise). Added a small "▾" button next to the squad name field that cycles
through every distinct squad name already visible in the current guard list - reusing
data the client already has (`Entry.squadName()`), no new network payload. Verified
`AbstractWidget#active` (used to disable the button when the player has no squads yet)
against mappings.dev AND a second, independent javadoc mirror before using it, per the
`EditBox.setPlaceholder` lesson.

**2. Squad-colored name tags (`GuardCommandNetworking.applySquadNameColor`).** Requested
directly: "que en los name tags algo cambiara para saber que son de una legion o de
otra." On every assign/clear, a guard's EXISTING name tag (if it has one) gets recolored
- never given a name it didn't have, never has its text changed, just its `Style` -
using a color picked deterministically from the squad name's hash into a fixed 10-color
palette, so every guard in a squad renders the same color with no new state to track.
Confirmed `Entity#getCustomName()`/`setCustomName(Component)` are real by finding them
already in use in Guard Villagers' own source (`Guard.java` line ~780, its
witch-conversion code); confirmed `MutableComponent#withStyle(ChatFormatting)` against
mappings.dev before first use.

**3. Real bug found and fixed: releasing an active Rally used to un-follow the WRONG
guards.** User question that led to this: "cuando no estoy en modo rally, no les logro
hacer daño, pero cuando los suelto de rally se activa friendly fire de nuevo, ¿esta
hecho a proposito?" Investigated by reading Guard Villagers' real source
(`Guard.FOLLOWING`, `GuardFollowPacket.java`): `Following` is the SAME flag Guard
Villagers' own inventory-GUI "follow me" toggle uses - it is not exclusive to this
mod's Rally. Two real consequences, confirmed from source rather than guessed:
- `RallyFriendlyFireHandler` blocking friendly fire whenever `isFollowing()` is true
  (rally or not) is **working exactly as designed** - its own javadoc says "following/
  rallied" together, and protecting any actively-following companion (not just a
  rallied one) is the intended scope. Not a bug.
- `ScrollOfRallyingItem`'s release path calling `guard.setFollowing(false)` on every
  OWNED GUARD CURRENTLY WITHIN 100 blocks of the player (not just the guards that
  particular rally gathered) **was** a real bug: releasing a rally could silently turn
  off a companion guard's independently-set "follow me" preference, and a rallied guard
  that wandered more than 100 blocks away would never get released by that query at all.
  **Fixed** by having the rally remember exactly which guard UUIDs it gathered (stored
  in the same `CustomData` tag the "active" glint flag already lives in, as a
  `ListTag`/`StringTag` of UUID strings - same serialization shape `SquadRegistry`
  already uses) and releasing only those, looked up by UUID rather than by a fresh
  distance query.

**4. Real bug found and fixed: guards falling to their death while following a flying
player.** User report: "cuando vuelo se intentan teletransportar a lugares altos y se
caen." Confirmed the mechanism by reading Guard Villagers' real source
(`Guard#shouldTryTeleportToOwner`/`#tryToTeleportToOwner`/`#teleportToAroundBlockPos`,
`Guard.java`): a following guard more than 12 blocks from its owner (config permitting)
teleports to a random spot near the owner, filtered by `canTeleportTo`'s own
`PathType.WALKABLE` check - which already tries to avoid an obviously unsafe landing, but
a player flying at height can still leave the only "walkable" spot nearby somewhere
precarious (a mountain peak, a tree canopy, a floating build's edge), and a follow-up AI
step off that spot is lethal fall damage for a grounded mob at that height. That
placement logic lives entirely inside Guard Villagers' own compiled class - fixing it
directly would need a mixin, which this project has deliberately avoided everywhere else.
**Fixed** with the narrower, safer option instead: new `GuardFallDamageHandler` (Phase
2H's `LivingIncomingDamageEvent` pattern, `DamageTypeTags.IS_FALL`) makes an owned guard
immune to fall damage outright - same shape as the existing friendly-fire immunity for a
following/rallied guard, and the follow/teleport-to-catch-up AI itself (explicitly liked
- "la ia de seguirme me encanta") is completely untouched. Trade-off stated plainly to
the user: this is blanket fall-damage immunity for any owned guard, not scoped to only
right after a teleport.

**Status: all four changes made. BUILD CONFIRMED SUCCESSFUL by the user** (`BUILD
SUCCESSFUL in 52s`, `32 actionable tasks: 3 executed, 29 up-to-date`) **on 2026-09-14**.
Deployed to `veamos--mods/rallyguard-0.1.0-neoforge-skeleton.jar` and bytecode-verified:
all four classes present with the fresh build timestamp, and each feature's specific
compiled symbol confirmed present via `strings` on the extracted `.class` (`IS_FALL`/
`setCanceled` for the fall-damage handler, `cycleSquadPicker`/`knownSquadNames` for the
picker, `applySquadNameColor`/`colorForSquad` for the name-tag coloring,
`RalliedGuards`/`getRalliedGuardIds`/`setRalliedGuardIds` for the rally-release fix).
In-game testing of items 1-3 is still pending; see section 23 for a same-day refinement
to item 4 made just after this build, before in-game testing of that item happened.

## 23. Phase 3A.1 refinement - fall damage resistance, not immunity (2026-09-14)

Right after confirming the section 22 build, before in-game-testing item 4, the user
asked for a design change to the fall-damage fix: "diría que simplemente, que no sean
inmunes, pero que tengan cierta resistencia, casi como si tuvieran caida de pluma" (not
immune, but resistant, almost like Feather Falling). Changed `GuardFallDamageHandler`
from `event.setCanceled(true)` to `event.setAmount(event.getAmount() * 0.10f)` - keeps
10% of incoming fall damage instead of canceling it outright. Verified
`LivingIncomingDamageEvent#getAmount()`/`#setAmount(float)` are real on this NeoForge
version via two independent sources (the NeoForge 1.21.1 source itself and
CraftTweaker's NeoForge event docs) before using them, per this project's two-source
rule for a first-time-used API. Trade-off unchanged from before except the model: a
short teleport-catch-up fall becomes a non-issue, an extreme fall from flight height can
still do noticeable, rarely lethal, damage - deliberately not a hard cap like vanilla
Feather Falling, just a flat 90% reduction. **Status: BUILD CONFIRMED SUCCESSFUL by
the user** (`BUILD SUCCESSFUL in 40s`, `32 actionable tasks: 2 executed, 30 up-to-date`)
**on 2026-09-14**, committed as `3b31c0d`. Deployed to
`veamos--mods/rallyguard-0.1.0-neoforge-skeleton.jar` and bytecode-verified: the
recompiled `GuardFallDamageHandler.class` contains `getAmount`/`setAmount`/
`RETAINED_FRACTION` and no longer contains `setCanceled`. **All of Phase 3A.1 (four
original items plus this refinement) is now build-confirmed.** In-game testing of the
new resistance feel, along with the other three 3A.1 items, is not yet scheduled to a
specific date - see section 25: the user wants to batch upcoming work into larger
chunks between build/test check-ins rather than testing after every small change.

## 24. Design notes filed, not yet implemented (2026-09-14)

- **Kamikaze guard variant, refined.** Confirmed TNT mod: **Lucky TNT**. New detail from
  the user: a Kamikaze-type guard should have a chance to appear specifically among
  Phase 5's renegade squads ("guerrillas"), and should render with a distinct skin/
  texture so it's visually identifiable before it closes the distance - the user
  flagged this second part themselves as possibly tricky ("puede ser problematico
  siento") since a stance is just a behavior mode on the existing `Guard` entity, not a
  separate entity type; a reskin would need either a resource-pack-level texture swap
  keyed off stance/squad-role, or promoting "kamikaze" from a stance to something the
  renderer can key off (e.g. a small marker on the entity data). Left as an open
  implementation question for when Phase 3C (Stance) and Phase 5 (renegade squads) are
  actually built - no code yet, on purpose, since a specific Lucky TNT item still needs
  to be identified before the stance's "use item in hand" behavior can be written.
- **Flying mounts (Phase 7), narrowed.** Confirmed: Hippogryph only, from Ice and Fire -
  Dragons are explicitly reserved for players only, not guard mounts. The user also
  flagged two more mods worth checking later, unconfirmed: Alex's Caves may have a
  Pteranodon-like flyer, and Alex's Mobs likely has some flying creature - neither
  investigated yet, both lower priority than getting a Hippogryph mount working first.

## 25. Renegade squad skins chosen; workflow pacing change; new future phase idea (2026-09-14)

- **Renegade squad skins picked, scoped to Phase 5 only.** The user picked two specific
  skins from minecraftskins.com for the Phase 5 renegade ("guerrilla") squads: a
  "Bandit" skin (https://www.minecraftskins.com/skin/24298218/bandit/) for the ordinary
  renegade guard, and an "Arab Garfield" skin (https://www.minecraftskins.com/skin/
  24134506/arab-garfield/) for the Kamikaze variant specifically - explicitly confirmed
  these skins apply ONLY to renegade/guerrilla-squad guards, not to the player's owned
  guards. Since Phase 5's renegade guard is planned as its own entity type (see section
  "Phase 5 - Renegade guard squads" above: a new class extending `Guard`, registered
  separately so it never touches the Ledger/Rally/Squad logic), giving it its own
  default skin is straightforward - a dedicated renderer/texture for that entity type,
  no resource-pack trickery needed for the base case. The earlier open question (section
  24) about how the Kamikaze VARIANT specifically gets a different look now has a
  concrete direction: since renegades are already their own entity type, the Kamikaze
  variant can most simply be a boolean/stance flag on that SAME renegade entity, with
  the renderer picking between the two skins based on that flag - simpler than the
  "kamikaze as a stance on an owned Guard" case, which would still need the promotion-
  to-synced-data approach discussed in section 24 if a kamikaze OWNED guard is ever
  wanted too. Not implemented yet - recorded here so the two skin URLs and the intended
  scope aren't lost before Phase 5 is actually built.
- **Workflow pacing preference, stated directly by the user.** "No creo que sea tan
  importante correrlo ya mismo, quiero hacerlo de a pedazos de desarrollo más grandes" -
  the user wants to batch upcoming work into larger development chunks rather than
  building/testing after every small change. Does not change the core discipline (still
  build-verify and bytecode-verify everything, still never claim success without the
  user's own real confirmation) - just means Claude should feel free to keep stacking
  well-scoped, real changes across a longer stretch before prompting for the next
  `.\gradlew.bat build`, rather than asking for a rebuild after each individual item.
- **New future phase idea floated: UI/interface tuning at scale.** The user wants to
  eventually evaluate and tune how well the Commander's Ledger and related screens hold
  up once there are a LOT of guards to manage (the ~10-village, 100+-guard vision from
  the original Phase 3 design conversation) - i.e. whether today's UI (a flat scrollable
  guard list, per-row selection, etc.) is actually convenient at that scale, and what
  needs to change if not. Not scoped or numbered yet - explicitly meant to come near the
  END of the existing phase plan, once there's enough real functionality (squads,
  stances, orders, renegades, logistics) to know what the UI actually needs to
  accommodate, rather than guessing ahead of time.


## 26. Phase 3B implemented - Ledger persistent roster (offline guard visibility) (2026-09-14)

Implemented in full, following the "Dale sigue" instruction to proceed after Phase
3A.1's build was confirmed. Not yet build-verified by the user (per their own stated
pacing preference in section 25, this is being batched with other well-scoped work
rather than prompting for a rebuild immediately) - written up here as code-complete and
internally consistent, pending the next real `.\gradlew.bat build`.

- **`GuardRoster` (new `SavedData`, `contract/GuardRoster.java`).** A durable,
  UUID-keyed "last known state" cache per guard (owner, name, patrolling, mount, armor,
  last-seen tick), following the exact same `SavedData` pattern already established by
  `SquadRegistry` (Phase 3A) - `LinkedHashMap`, `static get(ServerLevel)` resolved
  against the OVERWORLD's data storage, `SavedData.Factory<>(ctor, loader, null)`. Only
  ever refreshed opportunistically - whenever a guard happens to be found loaded during
  `sendGuardList`, or at hire time - never via a periodic background scan. This is a
  deliberate, documented trade-off (see the class's own javadoc): a cached mount/armor
  value can go stale between Ledger opens, which is an accepted cost for "can I see
  this guard at all" versus "is this number live," which was never the ask.
- **`GuardOwnership.describeMount(Entity)` / `describeArmor(LivingEntity)`.** Two new
  helpers. `describeMount` reuses the already-verified `Entity#getName()` pattern on
  `Entity#getVehicle()` (a genuinely new API for this project - `Entity#getVehicle()`
  was confirmed real on 1.21.1 via a version-pinned NeoForge javadoc mirror, since
  mappings.dev's own `Entity` page was truncated). `describeArmor` builds a compact,
  deliberately unlocalized wire string - four comma-separated values in fixed order
  HEAD,CHEST,LEGS,FEET, each `"-"` (empty) or a 0-100 durability-remaining percent -
  using `LivingEntity#getItemBySlot(EquipmentSlot)` and `ItemStack#isDamageableItem()`/
  `getDamageValue()`/`getMaxDamage()` (all first-time uses, confirmed via mappings.dev
  plus a second javadoc-mirror source for `getItemBySlot` specifically, whose
  mappings.dev page was also truncated).
- **`GuardListS2CPayload.Entry` grew from 4 to 6 fields** (added `mount`, `armor`) -
  the ceiling this version's `StreamCodec.composite` supports, per the limit already
  found during Phase 3A. Added `OFFLINE_ENTITY_ID = -1` as the sentinel `entityId` for a
  row backed only by a roster snapshot, not a live entity (a real entity id is never
  negative).
- **`GuardCommandNetworking#sendGuardList` rewritten** to build the guard list from TWO
  sources: the existing live-entity scan (each hit also calls
  `roster.recordSnapshot(...)` to refresh the cache), plus every OTHER guard the roster
  remembers for this owner that wasn't found loaded just now
  (`roster.entriesOwnedBy(player.getUUID())`, minus anything already online), sent with
  `OFFLINE_ENTITY_ID` and the cached name/patrolling/mount/armor. Squad name is still
  looked up live from `SquadRegistry` even for offline rows, since that registry already
  works for unloaded guards by its own Phase 3A design.
- **`GuardCommandScreen` (client) updated end to end**, not just its data model: the
  `Entry` record grew to 6 fields with an `isOnline()` helper; `rebuildButtons()` now
  skips creating the Select/Summon/Patrol buttons entirely for an offline row (a click
  on any of those would otherwise silently fall through to the server's existing
  "guard not found" message, which is a worse experience than the row visibly having no
  actions); `drawRows()` now appends a bracketed mount name and a compact averaged-
  armor-percent summary (`formatArmorSummary`, parsing the wire string client-side per
  the format's own design goal of staying unlocalized on the wire) to each row, and
  renders an offline row in a dimmer gray with an "(away)" tag so the online/offline
  distinction is visible at a glance, not just functional. Also fixed a real bug this
  same rewrite surfaced: the Patrol-toggle button's local optimistic-update callback
  still constructed `Entry` with the old 4-argument constructor, which would not have
  compiled once `Entry` grew to 6 fields - caught and fixed as part of this same pass by
  re-reading the file's actual current content before patching, not just recalling
  yesterday's version of it from memory.
- **`HireGuardMenu#clickMenuButton` now seeds the roster immediately at hire time**
  (right after `GuardOwnership.setOwner(...)`), closing the narrow gap where a guard
  hired and then immediately unloaded (owner walks away right after hiring) would
  otherwise be invisible on the Ledger until the next time it happened to load while its
  owner had the Ledger open.
- **New `GuardRosterCleanupHandler` (`event/GuardRosterCleanupHandler.java`), registered
  in `RallyOfTheGuard.java`.** Subscribes to `LivingDeathEvent` (confirmed real on this
  NeoForge/MC 1.21.1 version via nekoyue's ForgeJavaDocs-NG mirror pinned to
  `1.21.x-neoforge`); on an owned guard's death, removes its `GuardRoster` entry AND its
  `SquadRegistry` membership in the same handler. Without this, a dead guard's cached
  roster snapshot would never be removed by anything else (no periodic scan exists, by
  design), so the Ledger's guard count would slowly drift wrong as guards died - this
  was treated as a required part of Phase 3B, not deferred. Folding in the squad
  cleanup here also fixes a pre-existing gap noted since Phase 3A (squads never dropped
  dead members either) at zero extra cost, since both caches need the identical
  "this guard is gone for good" signal.
- **`gui.rallyguard.command.empty` reworded** from "No hired guards loaded in this
  world." to "You haven't hired any guards yet." - the old wording was written before
  Phase 3B and is no longer accurate now that a hired guard can appear on the Ledger
  even while genuinely unloaded.
- Not yet build-verified - next step per the user's own stated pacing preference is
  likely to keep batching further well-scoped work before asking for the next
  `.\gradlew.bat build`, rather than requesting one immediately after this phase alone.


## 27. Phase 3C implemented (first pass) - Stance: Passive/Defensive/Aggressive/Ruthless (2026-09-14)

Implemented immediately after Phase 3B (section 26), following the user's second "Dale
sigue" instruction given right after 3B was written up as code-complete-but-unbuilt -
continuing to batch work per their own stated pacing preference (section 25) rather than
pausing for a build after every stage. Not yet build-verified, same status as 3B.

- **New `contract/Stance.java`** (enum: `PASSIVE`, `DEFENSIVE`, `AGGRESSIVE`, `RUTHLESS`).
  Kamikaze, the fifth stance from the original Phase 3 design conversation, is
  deliberately NOT part of this pass - it needs the specific Lucky TNT throwable item
  identified first (see section 24). `DEFENSIVE` needs zero enforcement code - it IS
  Guard Villagers' own out-of-the-box AI, untouched.
- **New `contract/StanceRegistry.java`** (`SavedData`, identical shape to
  `SquadRegistry`/`GuardRoster`): durable per-guard-UUID `Stance`, resolved as
  `DEFENSIVE` when absent rather than persisting an explicit entry for every guard.
- **New `event/GuardStanceHandler.java`**, registered in `RallyOfTheGuard.java`:
  - `PASSIVE` is enforced by canceling every `LivingChangeTargetEvent` for the guard -
    blocks retaliation, joining the owner's fight, village defense, and player-anger
    alike, matching "won't fight back" literally rather than narrowly.
    `LivingChangeTargetEvent` is a genuinely new API for this project, confirmed via TWO
    independent sources that, notably, DISAGREED with each other: nekoyue's
    ForgeJavaDocs-NG mirror (`getNewTarget()`/`setNewTarget()`/`getOriginalTarget()`) vs.
    the real NeoForge source on the `1.21.x` GitHub branch
    (`getNewAboutToBeSetTarget()`/`setNewAboutToBeSetTarget(LivingEntity)`/
    `getOriginalAboutToBeSetTarget()`) - the raw source was trusted as the more literal
    of the two (consistent with a known javadoc-accuracy issue already filed upstream
    against this exact class, `neoforged/NeoForge#1996`), per this project's established
    rule for resolving a two-source disagreement.
  - `AGGRESSIVE`/`RUTHLESS` proactively hand a guard a target it would otherwise never
    get (Guard Villagers' own `targetSelector` goals only react to being hit or to the
    owner's fight, unless the GLOBAL, not-per-guard `AttackAllMobs` config is on). Since
    `Mob#targetSelector` is a protected field and this project avoids mixins (section
    4.2), a real AI Goal can't be added from outside the class - instead, a periodic
    (once-a-second, not every tick, for scale) scan directly calls
    `guard.setTarget(candidate)` when the guard has none, reusing the EXACT mechanism
    Guard Villagers' own `HurtByTargetGoal`/`HeroHurtTargetGoal` already use to hand a
    guard a target - the existing, unmodified attack-execution goals then pick it up
    with zero new combat code.
  - `AGGRESSIVE` still respects `GuardConfig.COMMON.MobBlackList` (only engages a real
    `Monster` not on that list). `RUTHLESS` ("modo asesinos") deliberately does NOT
    check the blacklist - confirmed feasible by reading `Guard.java`'s real source
    directly: `Guard#canAttack(LivingEntity)` (the method that enforces the blacklist)
    is consulted only by the target-ACQUISITION goals (`NearestAttackableTargetGoal`'s
    own `TargetingConditions`), never by the attack-EXECUTION goals
    (`GuardMeleeGoal extends MeleeAttackGoal`, whose `checkAndPerformAttack`/
    `canPerformAttack`/`doHurtTarget` chain never re-checks `canAttack`) - so a target
    set directly, bypassing acquisition, is never blacklist-checked. **Not yet in-game
    confirmed** that a Ruthless guard actually LANDS a hit on an otherwise-blacklisted
    mob (e.g. a Creeper), as opposed to merely walking at it - open item.
  - Both stances exclude the guard's own owner and every other guard owned by that same
    owner (reusing `GuardOwnership.isOwnedBy`, no separate squad-membership check
    needed since squadmates are, by definition, guards owned by the same player).
    `RUTHLESS` additionally excludes `Player`s outright as a deliberate first-pass
    safety choice - unprovoked player-vs-player combat is intentionally left to Phase
    3D's future "attack a specific player" ORDER (which the owner deliberately issues),
    not something a stance should trigger automatically on a bystander player.
- **`GuardListS2CPayload.Entry` nested `mount`/`armor` into a new `Condition` sub-record**
  to make room for a `stance` field within the version's confirmed 6-field
  `StreamCodec.composite` ceiling (4 base fields + `condition` + `stance` = 6 again,
  fully spent - any future new per-guard field needs another nesting pass). This
  changes `GuardCommandNetworking`'s and `GuardCommandScreen`'s accessors from
  `e.mount()`/`e.armor()` to `e.condition().mount()`/`e.condition().armor()` on the WIRE
  record specifically - the CLIENT-side `GuardCommandScreen.Entry` display record stays
  flat (7 plain fields, `mount`/`armor`/`stance` included) since it has no codec
  constraint of its own.
- **New C2S `AssignStanceC2SPayload(List<Integer> entityIds, String stance)`**,
  structurally identical to `AssignSquadC2SPayload` - registered in `NetworkBootstrap`,
  handled by new `GuardCommandNetworking.handleAssignStance`/`assignStance` (batch,
  per-guard ownership re-check, re-sends the guard list afterward to resync).
- **`GuardCommandScreen` (client) gets a new fourth control row** (panel height grown
  232 -&gt; 260 to fit it): four buttons, Passive/Defensive/Aggressive/Ruthless, acting on
  every currently-selected guard, same multi-select batch pattern as the squad bar.
  `drawRows()` now also appends the guard's current stance (localized, in parentheses)
  to each row's display text.
- **`GuardRosterCleanupHandler` also now removes a dead guard's `StanceRegistry` entry**
  (if any), same reasoning as its existing roster/squad cleanup - one more cache that
  would otherwise never be swept.
- New lang keys: `gui.rallyguard.stance.{passive,defensive,aggressive,ruthless}`,
  `gui.rallyguard.command.stance_set`.
- **Not yet build-verified.** Two specific behaviors need real in-game confirmation once
  built: (1) does `AGGRESSIVE`/`RUTHLESS`'s once-a-second target-acquisition scan feel
  responsive, or does it need a shorter interval; (2) does `RUTHLESS` actually damage an
  otherwise-blacklisted mob once targeted this way, confirming the `canAttack`-bypass
  analysis above holds in practice and not just on paper.

## 28. Phase 3B + 3C real BUILD SUCCESSFUL, deployed, bytecode-verified (2026-09-15)

The user ran `.\gradlew.bat build` for the first time since Phase 3B/3C landed. First
attempt failed with a real compile error:

```
GuardStanceHandler.java:138: error: incompatible types: no instance(s) of type
variable(s) T#1,B,T#2 exist so that List<? extends T#1> conforms to List<LivingEntity>
        List<LivingEntity> nearby = level.getEntities(EntityTypeTest.forClass(LivingEntity.class),
```

**Root cause:** `ServerLevel#getEntities(EntityTypeTest<Entity,T>, Predicate<? super T>)`
returns `List<? extends T>`, never `List<T>` - a generic-wildcard detail this project
already knew about and handled correctly at every OTHER call site
(`List<? extends Guard>` in `GuardStanceHandler#onLevelTick`,
`HiredGuardsNeutralityHandler`, `ScrollOfRallyingItem` x2, and
`GuardCommandNetworking`), but `GuardStanceHandler#findTarget`'s own nearby-scan
(added in the same Phase 3C pass) declared its result as the plain
`List<LivingEntity>`, which the compiler correctly rejects even when `T` resolves to
exactly `LivingEntity`, since `List<? extends LivingEntity>` is not assignable to
`List<LivingEntity>`.

**Fix:** changed the declaration to `List<? extends LivingEntity> nearby = ...` - the
existing enhanced-for loop over it (`for (LivingEntity candidate : nearby)`) already
works unchanged with a covariant list, no other code needed to change. Grepped every
other `getEntities(EntityTypeTest` call site in the codebase afterward to confirm none
had the same mistake - none did.

Rebuilt: real `BUILD SUCCESSFUL in 19s, 32 actionable tasks: 3 executed, 29 up-to-date`
(only the routine `Run.getProgramArguments()` deprecation notice, no errors) - **this
is the first real build confirmation for BOTH Phase 3B and Phase 3C.**

Deployed to the mods folder and bytecode-verified: `GuardRoster.class`,
`GuardRoster$Entry.class`, `GuardRosterCleanupHandler.class`, `Stance.class`,
`StanceRegistry.class`, `GuardStanceHandler.class`, `AssignStanceC2SPayload.class`, and
`GuardListS2CPayload$Entry$Condition.class` are all present in the deployed jar with
real, non-trivial sizes; `strings` on `GuardRoster.class` confirms `recordSnapshot`;
`strings` on `GuardStanceHandler.class` confirms `onChangeTarget`, `onLevelTick`,
`findTarget`, `isEligible`, and `setTarget` are all compiled in as expected.

**Phase 3B (Ledger persistent roster) and Phase 3C first pass (Stance) are now both
real, build-confirmed, deployed features - only in-game testing remains** (bundled
with the still-pending Phase 3A.1 in-game test round, per the user's own batching
preference). Specific things to check in-game once loaded:
- **3B:** open the Commander's Ledger, walk far enough away from a hired guard (or
  change dimension) that it unloads, and confirm it still shows up in the list -
  dimmed, tagged "(away)", with its last-known mount/armor, and with
  Select/Summon/Patrol disabled. Confirm armor condition (a rough %) shows for a guard
  wearing damaged armor.
- **3C:** select one or more guards, try each of the four stance buttons
  (Passive/Defensive/Aggressive/Ruthless) and confirm the row updates to show the new
  stance. Passive: guard should not retaliate or join a fight at all. Aggressive: guard
  should proactively walk toward and engage a nearby hostile mob (not a creeper/
  villager/etc - those stay blacklisted) without being hit first. Ruthless: guard
  should proactively engage literally anything living nearby INCLUDING an otherwise-
  protected mob like a Creeper (the specific bypass this stance exists for) - watch
  whether it actually lands damage on it, not just walks toward it. Also worth judging
  whether the once-a-second target-acquisition scan feels responsive enough, or too
  slow, in practice.
- **3A.1 (still pending from before):** squad picker button, squad-colored name tags,
  the rally-release fix (releasing a rally shouldn't un-follow unrelated guards
  outside it), and the 10%-fall-damage-resistance behavior.

## 29. First real in-game test round for Phase 3A.1/3B/3C - three bugs fixed (2026-09-15)

The user finally loaded up the world with Phase 3A.1/3B/3C all deployed together and
reported back. Three real bugs found and fixed; several other reports resolved as
by-design or filed as open investigation items (not fixed this pass - see below).

**Bug 1 - fixed: switching a guard TO Passive didn't take effect until its current
fight ended.** `GuardStanceHandler`'s `LivingChangeTargetEvent` cancel only blocks
*future* target changes - a guard already mid-fight (e.g. Ruthless vs. a Creeper) kept
swinging until it killed or lost that target, only going quiet afterward. Fixed in
`GuardCommandNetworking#assignStance`: when the new stance is `PASSIVE`, immediately
call `guard.setTarget(null)` for any currently-loaded guard in the batch, on top of the
existing registry write. An unloaded guard has nothing to clear and just picks up the
new stance normally once loaded.

**Bug 2 - fixed: Defensive behaved like Aggressive, especially during a Rally.** Root
cause found by re-reading `Guard.java#registerGoals()` directly (not guessed):
`HeroHurtTargetGoal` (join whatever the OWNER is currently fighting) is registered
UNCONDITIONALLY - not gated on the guard itself ever being hit, and not gated on
`isFollowing()` either - and several `NearestAttackableTargetGoal`s for raid mobs
(Ravager/Witch/Raider/Zombie) are *also* always-on regardless of the `AttackAllMobs`
config. `GuardStanceHandler` originally assumed `DEFENSIVE` needed zero code because
Guard Villagers' "default" AI would already be defensive-only - that assumption was
wrong. Fixed by giving `DEFENSIVE` its own `LivingChangeTargetEvent` handling: cancel
the change UNLESS the new target is literally `guard.getLastHurtByMob()` (the entity
that just hit the GUARD itself, not the owner). This is the literal reading of the
user's own definition ("solo atacan si los atacan"). Verified via the real NeoForge
1.21.1 event source (`raw.githubusercontent.com/neoforged/NeoForge/1.21.x/...`) that
`getTargetType()` can NOT be used to distinguish retaliation from a proactive
owner-fight-join - every Guard Villagers goal sets target via `Mob#setTarget(...)`,
which always reports as `MOB_TARGET` regardless of which goal called it; only
comparing against `getLastHurtByMob()` actually works. **Known, accepted side effect**:
a Defensive guard will no longer join Guard Villagers' own raid-defense goal
(`DefendVillageGuardGoal`) unless it personally takes a hit during the raid - this is
the correct literal behavior per the user's own definition, not a regression.

**Bug 3 - fixed: two of the same owner's guards could damage each other outside a
Rally.** `RallyFriendlyFireHandler` only ever protected a *following/rallied* guard
from the *owner's own* attacks - it never addressed guard-vs-guard damage at all.
Guard Villagers' `HurtByTargetGoal` already stops a hit guard from *retaliating*
against another Guard, but that never stopped the original stray hit (an arrow
overshooting its target, a melee sweep catching an ally standing nearby) from landing
in the first place, and no stance ever deliberately targets a squadmate either. Added
an unconditional (not gated on `isFollowing`) check to `RallyFriendlyFireHandler`:
cancel any `LivingIncomingDamageEvent` where the attacker (direct or true source) is
another Guard sharing the same owner as the victim.

**Also fixed (separate, not-git-tracked file): guards were mounting baby horses.**
The `GuardVillagersMounts-v1.2.zip` datapack's auto-mount selector
(`mount_logic.mcfunction`) had no age check at all. Since target selectors can't do
numeric NBT ranges, added a new datapack predicate
(`data/guard_villagers_mounts/predicate/not_baby_horse.json`,
`minecraft:entity_properties` condition with `flags.is_baby: false`) and referenced it
from the selector (`predicate=guard_villagers_mounts:not_baby_horse`). Requires
`/reload` or a world restart, same as every other datapack change this project has
made.

**Confirmed by design, no change needed:** a guard hired/created while a Rally is
already active does not auto-join it - this was already established during the
2026-09-13 test round (a Rally is a one-time gather of guards present at cast time) and
the user confirmed they're fine leaving it as-is.

**Filed as open items, NOT fixed this pass (need real investigation before touching
code, per this project's established discipline):**
- Mounted guards suffocating under low-ceiling blocks the underlying pathfinding
  doesn't seem to account for the rider's extra height.
- Guards struggling to path through water and getting stuck/nearly drowning (recoverable
  via Summon, which teleports them out).
- Combining Rally with individually-assigned mounts (the user wants to be able to
  rally a group where guards keep/receive their own mounts, even if that means
  one-guard-per-horse as a first-pass restriction) - this is a real feature request tied
  to the master vision brief's Transport abstraction, not a bug; needs actual design
  work, most naturally scoped as (or alongside) Phase 7.
- The guard-vs-guard friendly-fire root cause (Bug 3 above) is fixed defensively (block
  the damage), but WHY it happens at all (stray arrows? melee sweep radius?) was not
  root-caused via source reading this pass - if it turns out to still happen in ways the
  blanket damage-cancel doesn't cover, that needs a closer look.

User design decisions recorded (2026-09-15, in response to being asked): Stance stays
per-guard, with group/batch orders (3D) applying it across a whole selection - not
promoted to a stored Group-level property. Orders (3D) should be explicitly designed
with the future Battle Planner in mind from the start. See
`rally-of-the-guard-vision.md` (project memory) for the full master design brief this
answers into.

Not yet build-verified - awaiting the user's next `.\gradlew.bat build`.

## 30. Fourth bug from the same test round: Guard Villagers' own native menu didn't dismount mounted guards (2026-09-15)

Right after section 29's fixes were written up, the user reported a related but
distinct problem: toggling Follow/Patrol through Guard Villagers' OWN native
inventory-GUI menu (the basic right-click screen, not this mod's Ledger) on a mounted
guard doesn't work properly - the horse keeps moving on its own regardless ("el
caballo aun domina al guardia"), telling it to stay still doesn't stop it, and telling
it to patrol "no puede con el caballo."

**Root cause (confirmed by reading Guard Villagers' real source, not guessed):**
`GuardInventoryScreen.java` has its own two buttons that send `GuardFollowPacket`/
`GuardSetPatrolPosPacket` directly to Guard Villagers' own server-side handlers - code
this project has never touched. This project already fixed the identical "a vehicle
drives its passenger's position every tick, so a guard that's still mounted can't be
moved/reassigned" problem at three of its OWN trigger points (Ledger Summon, Ledger
Patrol-toggle, Rally cast - all via `guard.stopRiding()` before acting), but Guard
Villagers' native menu bypasses all three entirely.

**Fix:** rather than duplicate the dismount check at a fourth trigger point, or try to
intercept Guard Villagers' own packets (ruled out by this project's mixin-avoidance
policy), generalized it into one small periodic sweep - new
`event/GuardMountDisciplineHandler.java`, registered in `RallyOfTheGuard.java`: once a
second (same interval as `GuardStanceHandler`'s own scan), for every owned guard that
is both currently mounted AND either following or patrolling, call
`guard.stopRiding()`. This covers every current AND future trigger point uniformly
with zero new state and zero NBT/mixin use. The mounts datapack already excludes
`Following:1b`/`Patrolling:1b` guards from its own auto-mount selector, so a guard
dismounted this way will not be immediately re-mounted.

Explicitly documented as a STOPGAP, not real mounted-movement support - neither
vanilla horses nor Guard Villagers' own `FollowHeroGoal`/`WalkBackToCheckPointGoal`
know how to move a mount a non-player rider is sitting on. Real "guard stays mounted
and its horse actually follows/patrols with it" support belongs to the master vision
brief's Transport abstraction and lands with (or alongside) Phase 7, not here.

**Also clarified (no bug, no code change):** the user believed a guard needs "Follow"
already active before a Rally is cast to be included in it. Re-read
`ScrollOfRallyingItem`'s actual candidate-selection predicate to check: it only
requires ownership, not-currently-patrolling, and physical proximity
(`RALLY_RANGE` = 100 blocks) - it does NOT check `isFollowing()` at all. The much
simpler explanation: a guard that isn't following the player typically isn't standing
near them, so by the time the Scroll is used it's just outside `RALLY_RANGE` - nothing
to do with the Follow flag specifically. Told the user this instead of guessing a
fix for a bug that isn't actually in the candidate-selection code.

Not yet build-verified - part of the same not-yet-built batch as section 29's three
fixes.

## 31. Phase 3D (first pass) - the Order abstraction, plus Recall and Hold (2026-09-15)

**Objective (user-directed, "CONTINUE DEVELOPMENT" brief):** design the smallest
reusable Order abstraction ("what should this guard/group do right now?"), verify it
can later support batch/group orders and a future Battle Planner, then implement only
as many concrete orders as are actually needed right now - explicitly NOT the whole
order vocabulary discussed (Muster, Attack, Advance, Defend, Follow, Retreat, Reform,
Escort, Guard Post, Resupply, Kamikaze), and explicitly NOT Kamikaze, per the user's own
"MOST IMPORTANT RULE": build the Order system clean first, make Kamikaze one of its
first meaningful consumers later, once that system is proven.

**Required process followed before writing any code**, per the user's own explicit
instructions:
1. Inspected the current repository state rather than assuming it matched an older
   design conversation - read `GuardCommandNetworking.java`'s full method list,
   `resetNavigation(Guard)`'s body, `NetworkBootstrap.java` in full, and
   `AssignStanceC2SPayload.java` in full (as the direct template for the new payload) -
   confirming the Phase 3A/3C multi-select batch pattern (Squad/Stance bars: a
   `List<Integer> entityIds` payload, a per-guard ownership re-check server-side, a
   guard-list resync afterward) was still the right shape to extend.
2. Read `GuardCommandScreen.java` in full (514 lines) to understand the existing
   Squad-bar/Stance-bar row layout (`SQUAD_BAR_OFFSET`/`STANCE_BAR_OFFSET`, both defined
   as `PANEL_H - N` with N shrinking by 28 per row added, matching a consistent 28px
   row rhythm already established when the Stance bar was added in Phase 3C) before
   adding a fifth row.
3. Confirmed no existing system already covered this (Squad/Stance registries are
   persistent `SavedData`; an Order is a one-shot dispatch action, not persistent
   state - deliberately NOT modeled the same way).

**What was implemented:**

- New package `net.hfstack.rallyguard.order`, kept separate from `contract` (which
  holds the persistent `SavedData` registries - `StanceRegistry`/`SquadRegistry`/
  `GuardRoster`) precisely because Orders are one-shot stateless dispatch, not
  persisted state:
  - `OrderExecutor` - a `@FunctionalInterface` with exactly one method,
    `void execute(ServerPlayer player, List<Guard> guards)`. This is the entire
    abstraction; nothing else was added speculatively (no target field, no "can this
    guard receive this order" predicate, no priority/queueing) since neither of the
    two orders implemented needs any of that yet.
  - `OrderRegistry` - a plain static `Map<String, OrderExecutor>` (NOT a `SavedData`),
    string-keyed exactly like the existing Stance/squad-name convention (an
    unrecognized id fails soft instead of desyncing an enum ordinal across versions).
    Registers `RECALL` and `HOLD` in a static initializer.
  - `RecallOrder` - generalizes `GuardCommandNetworking#summon`'s existing
    dismount-then-moveTo-near-player logic (mounted-guard dismount fix from Phase 2F)
    to a batch of guards, mirrored line for line rather than reimplemented.
  - `HoldOrder` - generalizes `GuardCommandNetworking#resetNavigation`'s existing
    clear-target/un-aggressive/stop-navigation logic to a batch, also mirrored
    directly. Deliberately does NOT touch patrol/following flags - Hold is a "stand
    down right now" order, not a mode switch, so a patrolling guard is still marked
    patrolling after a Hold, just stopped for this instant.
- New payload `IssueOrderC2SPayload(List<Integer> entityIds, String orderId)` -
  structurally identical to `AssignStanceC2SPayload` (same field shapes, same 4096-cap
  int-list codec, same `STRING_UTF8` id). Deliberately has NO target field in this
  first pass; the established "nest into a sub-record" escape hatch (already used by
  `GuardListS2CPayload.Entry.Condition`) is the documented path for adding one later,
  once an order that actually needs a target (Attack, Muster, Kamikaze) is built.
- `NetworkBootstrap.java` - registered `IssueOrderC2SPayload` via
  `registrar.playToServer(...)`, same pattern as every other C2S payload here.
- `GuardCommandNetworking.java` - added `handleIssueOrder` (enqueues work, resolves the
  player) and `issueOrder` (resolves `entityIds` to live, owned guards with the same
  stale-id/not-owned tolerance `assignSquad`/`assignStance` already have, looks up the
  `OrderExecutor` in `OrderRegistry`, silently no-ops on an unrecognized order id or an
  empty resolved guard list, otherwise runs the executor, sends a confirmation chat
  message, and resyncs the guard list).
- `GuardCommandScreen.java` - added a fifth Ledger row, the Order bar, above the
  existing Stance bar: `PANEL_H` grew from 260 to 288 (the same +28-per-row rhythm
  already used when the Stance bar itself was added), with `ORDER_BAR_OFFSET =
  PANEL_H - 112` and two centered buttons (Recall, Hold), each sending an
  `IssueOrderC2SPayload` for every currently-selected guard - same multi-select-batch
  UI pattern already used for Squad/Stance.
- `contract/Stance.java` - updated its javadoc to record the reclassification: Kamikaze
  will NOT be added as a fifth `Stance` enum value. Per the user's explicit direction,
  it is now planned as a special Order (a temporary override, not a persistent
  posture), so it belongs in the `order` package later, not here. No enum values or
  wire format changed - this is a documentation-only clarification.
- `en_us.json` - added `gui.rallyguard.order.recall`, `gui.rallyguard.order.hold`, and
  `gui.rallyguard.command.order_issued`. (`pt_br.json` was NOT updated for these keys,
  matching this project's existing state - it was already missing the Phase 3A/3C
  squad/stance keys before this change, so this keeps it consistently behind rather
  than translating some newer strings but not others.)

**Deliberately NOT implemented in this pass** (per the user's own explicit
instructions not to build everything now):

- Every other order from the brief's list: Muster, Move, Attack, Advance, Defend,
  Follow, Retreat/Fallback, Reform, Escort, Guard Post, Resupply.
- Kamikaze itself, in any form - no detonation code, no target-selection code, no new
  guard state. Preliminary *research only* was done (see below) so the eventual
  implementation is better-scoped, not to get ahead of the Order system being proven
  first.
- Any target/position field on `IssueOrderC2SPayload` - not needed by Recall or Hold,
  deliberately deferred until an order that needs one is actually built.
- The Battle Planner itself (manual "COMMAND NEXT" progression or automatic triggers) -
  only verified that nothing in this pass's shape (a string order id, a flat guard
  batch, a stateless executor) would block building it later.
- Formation engine, Reform, Guard Post vs Patrol distinction, Transport-agnosticism
  abstraction - all still pending, unaffected by this pass.

**Preliminary Kamikaze research (no code written), kept for the next pass:** read
vanilla `TntBlock`'s real 1.21.1 public/protected API via WebFetch (mappings.dev) to
check for a "generic, whitelist-free detonate this TNT-family item" hook. Found none -
`TntBlock`'s own primed-entity spawn is a `private static` method, and the public
`static void explode(Level, BlockPos)` always spawns vanilla `PrimedTnt`/
`EntityType.TNT` and can't be overridden polymorphically. This means "read whatever
item the player gave the guard and detonate its own real explosion, no whitelist" is
technically harder than first assumed. A `DispenserBlock` dispense-behavior-registry
reuse approach (since any TNT-variant mod wanting dispenser compatibility must already
register one) is a candidate worth verifying next, once Kamikaze's turn actually comes.

**How to test in-game:** open the Commander's Ledger, select one or more guards with
the row checkbox, then:
- **Recall** - guard(s) teleport to just beside you (identical feel to the existing
  per-guard Summon button, now batchable); a mounted guard dismounts first.
- **Hold** - guard(s) immediately stop fighting/moving (clears target, un-aggros, halts
  navigation) without changing their Follow/Patrol mode.
Both send a chat confirmation ("N guard(s) given the Recall/Hold order.") and refresh
the Ledger's list. An order sent with nothing selected is a silent no-op (matches the
existing Squad/Stance bars' behavior for an empty selection).

**Known limitations:**
- Hold does not persist - a guard with an active target-acquisition goal (e.g.
  Ruthless stance, or Aggressive scanning) can reacquire a target on its own shortly
  after a Hold, since Hold only clears the current instant's state, it doesn't install
  any ongoing "stand down" flag. This is consistent with Hold's documented intent
  ("stop right now," not "stop forever") but worth knowing before relying on it as a
  cease-fire.
- Recall/Hold apply to whatever is currently `selected` in the Ledger, exactly like
  Squad/Stance - there is still no "select by squad name" shortcut.
- Real `BUILD SUCCESSFUL in 1m` confirmed by the user (2026-09-15), 32 actionable
  tasks (3 executed, 29 up-to-date). The only warning was the pre-existing, explicitly
  ignored `Run.getProgramArguments()` NeoGradle deprecation notice, unrelated to this
  change. This section is now build-confirmed.

## 32. Phase 3D Extended - the Formation engine, plus Muster/Attack/Reform/Retreat (2026-09-15) — AWAITING BUILD

Following on from section 31's first pass (Order abstraction + Recall/Hold), this
pass implements the primary objective the design brief laid out: Muster (bring
selected guards to a position and arrange them into a formation), plus Attack,
Reform, and Retreat. Per the brief's own explicit scope limits, the full Battle
Planner, Kamikaze, and Extended Mounts are NOT part of this pass.

**Inspection done before changing anything** (per the brief's own instruction):
re-read `OrderExecutor.java`, `OrderRegistry.java`, `RecallOrder.java`,
`HoldOrder.java`, `IssueOrderC2SPayload.java`, `GuardCommandNetworking.java`,
`GuardCommandScreen.java`, `SquadRegistry.java` (confirmed no formation/home-point
data on `Squad` yet), `GuardStanceHandler.java` (confirmed its exact
`guard.setTarget(candidate)` direct-bypass targeting mechanism and nearest-entity
scan, both reused below), `GuardRosterCleanupHandler.java`, `GuardListS2CPayload.java`
(confirmed the nested-sub-record pattern used below for `Destination`), and
`ScrollOfRallyingItem.java` (its own fan-placement math doesn't use player facing, so
it wasn't reusable as a yaw reference - see the Formation math note below). No
existing formation/AI code was found anywhere in the repo; this is new ground.

**The Order abstraction philosophy, unchanged from section 31, now proven with
parameterized orders too:** `Guard -> Order -> Target/Position/Formation ->
Execution`. `OrderExecutor` itself is untouched (`execute(ServerPlayer,
List<Guard>)`, no new method, no growing interface). Orders that need more than "which
guards" (Muster: anchor + facing + shape; Retreat: a destination) get that data via
**constructor parameters** instead - `GuardCommandNetworking` builds a fresh
`new MusterOrder(anchor, facingYaw, shape)` / `new RetreatOrder(destination)` per
network call and calls `.execute(...)` directly, bypassing `OrderRegistry`'s bare-id
lookup entirely for just those two. `OrderRegistry` itself now also carries `ATTACK`
and `REFORM` (zero-parameter, looked up by id like `RECALL`/`HOLD`). This is "one
Order system, different scopes and parameters," not two systems.

**Hold vs. Stance, reaffirmed (not re-implemented this pass):** Hold remains "stay
where you are, right now" - it does not become a mode. Stance (Passive/Defensive/
Aggressive/Ruthless) still separately governs ongoing target-acquisition behavior.
A future "Stand Down"/"Disengage" concept remains explicitly out of scope.

**What was implemented:**

- New package `net.hfstack.rallyguard.formation` - the generic formation engine, NOT
  one class per shape:
  - `FormationShape` - an enum (`LINE`, `COLUMN`, `V`) where each constant implements
    one method, `List<double[]> localOffsets(int count, double spacing)`, returning
    local `{right, forward}` offset pairs. A shape never sees world coordinates.
    Adding a future shape (Wedge, Circle, U, Row, shield-wall, escort, cavalry wedge -
    all explicitly deferred) means one more enum constant with its own
    `localOffsets`, never a new AI system. Only three shapes are implemented, the
    minimum needed to prove a side-by-side shape (Line), a front-to-back shape
    (Column), and an asymmetric pointed shape (V) all go through the identical
    rotation/translation math below.
  - `Formation` - a record `(FormationShape shape, double spacing)` with one method,
    `positions(Vec3 anchor, float facingYawDegrees, int count)`, that rotates/
    translates a shape's local offsets into real world positions. This is the entire
    engine; `FormationShape` only ever hands back local offsets.
  - **Yaw-to-direction math**, independently derived and cross-verified (no reliable
    single existing source was found in-repo or via WebFetch, and the session's
    WebFetch quota ran out mid-verification - see below): yaw 0° = south (+Z), yaw
    increases counter-clockwise viewed from above. `forward = (-sin(yaw), cos(yaw))`
    as `(x, z)`; `right = (-forward.z, forward.x)` (forward rotated 90° clockwise
    viewed from above). Verified two independent ways: it reproduces the known
    cardinal mapping at yaw 0/90/180/270 (south/west/north/east), and it matches
    vanilla's own well-established horizontal view-vector formula. `Formation`'s
    positions all share the anchor's Y - per the brief's own instruction, no custom
    pathfinding or terrain-following was built; ordinary Minecraft navigation (see
    below) handles the rest, with some imperfection accepted.
- New `contract/FormationRegistry.java` - a `SavedData` registry, same shape as
  `StanceRegistry`/`SquadRegistry` (world-global, overworld-backed), remembering per
  guard: anchor, facing, shape, spacing, its own slot index, and the formation's
  member count at Muster time (a `Slot` record). This is what makes Reform possible:
  each guard remembers the WHOLE formation it was part of, not just its own point,
  since recomputing any one slot's position needs the full shape/count/spacing. This
  does NOT make `OrderExecutor` itself stateful (still one-shot, per its own
  javadoc) - it is specifically Muster's *result* that's worth remembering, the same
  established pattern as Squad/Stance assignment already being a persistent side
  effect of a network handler.
- New `network/payload/Destination.java` - `Destination(int x, int y, int z, int
  facingYawDegrees)`, nested (not inlined) into the two payloads below so each stays
  comfortably under the known 6-field `StreamCodec.composite` ceiling (the same
  nesting escape hatch `GuardListS2CPayload.Entry.Condition` already uses and proved
  compiling). Deliberately **integer-only** precision (`ByteBufCodecs.INT` only, no
  `FLOAT`/`DOUBLE`) - a decision forced by the session's WebFetch quota running out
  mid-verification of the float/double codecs' exact behavior; block/degree precision
  is more than sufficient for formation muster/retreat, so this is a documented,
  low-cost simplification, not a compromise on anything that mattered.
- New `network/payload/IssueMusterOrderC2SPayload.java` -
  `(List<Integer> entityIds, Destination destination, String shape)` - three
  top-level fields (comfortably under the ceiling since `Destination` nests its own
  four). `shape` is a `FormationShape` enum name as a string, same soft-fail
  convention as every other order/stance id in this project (an unrecognized value
  falls back to `LINE` server-side rather than desyncing an enum ordinal).
- New `network/payload/IssueRetreatOrderC2SPayload.java` -
  `(List<Integer> entityIds, Destination destination)` - reuses `Destination` purely
  for its position; the facing field is unused by Retreat but not worth a second,
  position-only record for one unused field.
- New `order/MusterOrder.java` - "converge into this formation, here." Computes
  `Formation.positions(anchor, facingYaw, guards.size())`, then for each guard:
  dismounts it if it's a passenger (same fix as `summon`/patrol from Phase 2F/3D
  first pass), calls `guard.getNavigation().moveTo(target.x, target.y, target.z,
  1.0D)` (real pathfinding, per the brief's explicit "do NOT build custom
  pathfinding" instruction - guards visibly walk into position, unlike Recall's
  instant teleport), and records the guard's slot into `FormationRegistry` as a side
  effect. Constructor-parameterized (`anchor`, `facingYaw`, `shape`), not registered
  in `OrderRegistry` - see the Order abstraction note above.
- New `order/RetreatOrder.java` - "fall back to this position," deliberately the
  simplest new order: no formation math at all, each guard just
  `getNavigation().moveTo(destination, 1.0D)` individually after dismounting if
  needed. Constructor-parameterized (`destination`), not registered in
  `OrderRegistry`, same reasoning as Muster.
- New `order/ReformOrder.java` - "get back into formation." For each guard, looks up
  its `FormationRegistry.Slot`, recomputes that *same* formation's positions via
  `Formation.positions(...)`, and sends the guard back to its own slot. Reuses the
  identical position calculation Muster uses; adds no formation math of its own. A
  guard with no remembered slot (never Mustered, or cleared on death - see below) is
  silently skipped, same tolerance every other batch order has. Registered as a
  bare, zero-parameter `OrderRegistry` entry (`REFORM`) - unlike Muster/Retreat, it
  needs no data beyond "which guards," since everything else already lives in
  `FormationRegistry`. This is the brief's own reading realized directly: "MUSTER =
  establish formation around a destination; REFORM = restore the existing formation
  around the group's current/known anchor" - no separate formation system for Reform.
- New `order/AttackOrder.java` - "engage the nearest threat to me, right now," a
  **deliberately simplified first pass**, registered in `OrderRegistry` as `ATTACK`.
  The brief's ideal (select a specific target entity, then Attack) doesn't map onto
  how this order is actually issued: the Ledger is an open GUI when Attack is
  clicked, so "whatever's under the player's crosshair" is meaningless at that
  moment. Real click/mark-a-target selection would need either a server-side world
  raycast or a separate "mark a target first" interaction - both more machinery than
  this pass calls for, and both would have needed fresh API verification the
  session's WebFetch quota no longer allowed (see below). Instead: the target is
  resolved deterministically server-side as the nearest real vanilla `Monster` within
  32 blocks of the **player** (not the guards, who may be scattered), excluding the
  player's own guards, found via the exact nearest-entity-scan pattern already
  proven in `GuardStanceHandler`, then `guard.setTarget(target)` for every guard in
  the batch - the same direct-bypass mechanism Ruthless already uses (no mob
  blacklist consulted, mirroring that stance's already-proven behavior). Fully
  server-validated (only currently-loaded, currently-alive entities are ever found;
  nothing client-supplied is trusted). Sends
  `gui.rallyguard.command.attack_no_target` if no monster is found nearby. True
  click/mark-target selection is real future work, explicitly not built here.
- `GuardCommandNetworking.java` - extracted `resolveOwnedGuards(ServerLevel,
  ServerPlayer, List<Integer>)` as a shared private helper (deduplicating the
  ownership-check loop that was previously inline only in `issueOrder`, now reused by
  `issueOrder`, the new `musterOrder`, and the new `retreatOrder` - justified since
  three callers now need the identical logic). Added `handleIssueMusterOrder`/
  `musterOrder` (parses the shape name, converts `Destination` to a block-centered
  `Vec3` anchor, constructs and runs `MusterOrder`, sends a confirmation message
  naming the formation, resyncs the guard list) and `handleIssueRetreatOrder`/
  `retreatOrder` (same shape, no formation shape to parse).
- `GuardCommandScreen.java` - `PANEL_H` grew from 288 to 316 (a second +28 row,
  continuing the established per-row rhythm). The existing Order bar
  (`ORDER_BAR_OFFSET`) expanded from 2 buttons (Recall/Hold) to 4 (Recall/Hold/
  Attack/Reform), `ORDER_BTN_W` shrunk from 130 to 95 to fit four. A new Muster bar
  (`MUSTER_BAR_OFFSET`, above the Order bar) holds 4 buttons: Line, Column, V (each
  issuing Muster with that shape), and Retreat - matching the brief's own sketch
  layout (`[Recall][Hold] / [Muster shapes][Attack] / [Retreat] / [Reform]`,
  rearranged slightly to fit two clean rows of same-sized buttons rather than
  mismatched groups, without changing which actions exist). New
  `currentPlayerDestination()` floors the local player's own position/yaw into a
  `Destination` - see the destination-selection limitation below.
- `GuardRosterCleanupHandler.java` - added a `FormationRegistry.get(level).remove(
  guard.getUUID())` call alongside the existing Roster/Squad/Stance cleanup on guard
  death, so a dead guard's remembered Muster slot doesn't sit in the registry
  forever with no other removal path (same reasoning as the other three).
- `en_us.json` - added `gui.rallyguard.order.attack`, `gui.rallyguard.order.reform`,
  `gui.rallyguard.order.retreat`, `gui.rallyguard.formation.line`,
  `gui.rallyguard.formation.column`, `gui.rallyguard.formation.v`,
  `gui.rallyguard.command.attack_no_target`, `gui.rallyguard.command.muster_issued`,
  `gui.rallyguard.command.retreat_issued`.

**Destination-selection v1 limitation (deliberate, per the brief's own fallback
instruction - "if a clean mechanism can't be implemented yet, establish the
underlying server-side architecture and document the UI limitation"):** for this
pass, every Muster and Retreat button always uses the sending player's own current
position/facing at the moment of the click - there is no in-world "mark a
destination elsewhere" interaction yet, since the Ledger is an open GUI with no view
of the world to pick a point from, and this session's WebFetch quota ran out before
a raycast-based marker approach could be verified against real 1.21.1 API behavior.
The wire format (`Destination`'s full x/y/z/facingYaw fields) already supports an
arbitrary point though, so a future destination-marking UI is a client-only addition
- nothing here or in any payload needs to change for it.

**Deliberate decision NOT to build a separate `AdvanceOrder` this pass:** since
Muster's v1 destination is always "the player's own current position," a distinct
Advance order/payload/button would be functionally identical to Muster with no way
for a player to ever tell them apart - building it anyway would create real,
UI-unreachable dead code, directly conflicting with the brief's own quality-check
instruction to look for exactly that. Muster's move-into-formation logic already IS
the reusable infrastructure a real, distinguishable Advance would need; building
Advance for real is trivial once destination-marking exists, and is left for that
future pass.

**Deliberately NOT implemented in this pass** (per the brief's own Tier list and
explicit deferrals):
- The Battle Planner UI (Groups with Formation/Order/Trigger, "COMMAND NEXT" manual
  progression) - only verified that nothing built here would block it; Muster/
  Attack/Reform/Retreat are all things a future Battle Plan step could invoke
  directly through the same Order system.
- Kamikaze - no implementation, per the brief's own "unless truly trivial" bar; with
  the Order system only now getting its second real pass, and the session's WebFetch
  quota exhausted (so `TntBlock`'s real detonation API from section 31's preliminary
  research couldn't be re-verified further this session), it was not remotely
  trivial and was not attempted. It remains a future Order, once its detonation
  mechanism is properly researched.
- Extended Mounts (Candicorn, Komodo Dragon, Hippogryph-only - Ice & Fire dragons are
  explicitly reserved for players, never guard mounts) - a future mount phase, not
  touched here. Orders remain transport-independent (every order that moves a guard
  dismounts it first if needed, exactly like the pre-existing Summon/Patrol did).
- **The generic "Operator" concept is confirmed REMOVED from the roadmap entirely**,
  per the user's explicit direction - it is not part of Rally of the Guard's future
  plans in any form, superseded by Extended Mounts above.
- Any additional formation shape beyond Line/Column/V (Wedge, Circle, U, Row,
  shield-wall, escort, cavalry wedge) - all are future `FormationShape.localOffsets`
  implementations, an architecture-free addition once actually wanted.
- Advanced target prioritization, anti-mage behavior, boss-specific logic, or any
  other sophisticated Attack targeting beyond "nearest monster to the player" - the
  brief's own instruction was an explicit, simple first version only.
- Group-level (whole-squad-by-name, not just currently-selected) order issuing - the
  Ledger's existing multi-select already lets a player select an entire squad's
  guards manually and issue any of these orders to them at once, which satisfies
  "different selection scopes" for this pass without a new selection mechanism; a
  "select by squad name" shortcut remains a future Ledger UI convenience, not an
  Order-system change.

**WebFetch session-limit impact on this pass (for the record):** partway through
this pass, `WebFetch` hit its session limit (resets 8:30am UTC), immediately after
starting to verify a possible `Minecraft.crosshairPickEntity`-based raycast approach
for Attack targeting. Rather than guess at an unverified API, that approach was
abandoned in favor of the already-proven `GuardStanceHandler` nearest-scan pattern
(see `AttackOrder` above), and `Destination`'s wire format was deliberately restricted
to already-proven `ByteBufCodecs.INT` rather than an unverified `FLOAT`/`DOUBLE`
codec, for the same reason. Both are documented, low-cost, and revisitable once
verification is available again - not silent corner-cutting.

**How to test in-game (manual test plan - nothing below is build- or
gameplay-confirmed yet; this section is AWAITING BUILD):**
- **Recall / Hold** - unchanged from section 31; retest only if section 31's own
  behavior seems to have regressed.
- **Muster** - select 3+ guards, stand somewhere with open space, face a cardinal-ish
  direction, click Line. Guards should walk (not teleport) into a side-by-side row
  centered on your position, perpendicular to your facing. Turn 90° and click Column
  - guards should walk into a front-to-back line along your new facing. Click V -
  one guard should end up at your exact position/facing (the apex), others trailing
  behind in a chevron. **Facing check (at least two orientations, per the brief):**
  repeat Line or Column facing two different directions (e.g. facing north, then
  facing east) and confirm the formation's orientation visibly rotates with you both
  times, not just the first.
- **Attack** - stand within 32 blocks of a hostile mob (a zombie/skeleton is enough),
  select guards, click Attack. Guards should immediately target and move to engage
  that mob (or the nearest one, if several are in range). With no hostile mob within
  32 blocks, expect the chat message "No enemy found nearby to attack." and no guard
  behavior change.
- **Reform** - after a successful Muster, walk several guards away from their
  formation spot (or let them chase something), then select the same guards and
  click Reform. They should walk back to their own individual slot in the
  last-Mustered formation (same shape, same anchor/facing) - not to a new formation
  around wherever they currently are.
- **Retreat** - select guards, move some distance away from them, click Retreat.
  Guards should walk toward your position at the moment you clicked (no formation
  shape, just individual pathing toward one point).
- What remains explicitly untested/unconfirmed until the user reports back: whether
  `.\gradlew.bat build` actually succeeds with these 17 changed/new files (it has
  NOT been run yet as of writing this section); all five behaviors above in a real
  game session; whether guards navigate around obstacles/terrain acceptably given no
  custom pathfinding was added (per the brief's own "some navigation imperfection
  acceptable" allowance).

**Known limitations (carried into this section, not yet resolved):**
- Destination v1 (Muster/Retreat) always uses the player's own position - see above.
- Attack always targets "nearest monster to the player," never a specific
  player-chosen entity - see above.
- Reform restores *exactly* what Muster last recorded (same shape/anchor/facing) -
  there is no way yet to reform into a different anchor without a fresh Muster.
- `Destination`'s block/degree-only integer precision (a WebFetch-quota-driven
  choice, not a hard limitation - sub-block/sub-degree precision was never actually
  needed for formation muster/retreat).
- No AdvanceOrder yet (see the dead-code reasoning above) - functionally, Muster
  already serves "move to my position" until destination-marking exists.
- No group-by-squad-name order shortcut yet - manual multi-select still required.

## 33. Phase 3D.1 - Circle formation, squad quick-select, and the deploy-gap fix (2026-09-25 / 2026-09-28)

**Context.** The user tested after Phase 3D Extended (section 32) and
reported "no he podido usar formaciones aún" / "no vi los botones de
Muster/Retreat" - i.e. the new UI wasn't visible in-game at all. Before
assuming a code bug, root-caused it directly:
- `git log` showed the last commit was still "Add Phase 3D Order
  abstraction with Recall and Hold orders" - all of section 32's 17
  files (Formation/FormationShape/FormationRegistry/the four new
  orders/the two new payloads/GuardCommandScreen's new bars/etc.) were
  sitting uncommitted. A stale `.git/index.lock` was also found and
  cleared (from an earlier interrupted session).
- The built jar (`build/libs/rallyguard-0.1.0-neoforge-skeleton.jar`,
  built 2026-09-15 23:03, verified via `unzip -l` to contain all the
  Phase 3D Extended classes) had never actually been copied into the
  mods folder. The mods folder still had a stale jar from 2026-09-15
  04:43 - built *before* Phase 3D Extended was even written.
- **This was not a functional bug** - it was purely an undeployed
  build + uncommitted code. Historically the jar copy into the mods
  folder was done by Claude directly via `device_bash`; once that tool
  broke for a stretch, the user was never explicitly told they'd need
  to do that copy step manually, so it silently never happened.

**Fix applied.** Copied the existing built jar over the stale one in the
mods folder, verified identical via md5sum on both copies
(`94faeaa45667aaaaa58df1ca7b0da153`), then made two clean commits:
`89a8328` (the full section-32 Phase 3D Extended changeset) and `d99a91b`
(this section's Circle + squad-select changes, below). **This means
Muster/Attack/Reform/Retreat, as they existed on 2026-09-15 (Line/Column/V
shapes, no Circle, no squad quick-select), should now actually be visible
and testable in-game - but this has not yet been gameplay-confirmed by the
user.**

**New: `FormationShape.CIRCLE`.** A ring centered on the anchor. Member 0
sits directly forward of the anchor (`{0, radius}` in local right/forward
space - the same forward-of-anchor convention V's apex already used);
remaining members are spaced evenly by angle
`theta_i = 2*PI*i/count`, using `offset = {radius*sin(theta),
radius*cos(theta)}`. Radius: `Math.max(spacing / 2.0, spacing * count /
(2 * Math.PI))` - the `spacing/2` floor keeps small groups (2-3 guards)
from collapsing toward the center, while the `spacing*count/(2*PI)` term
keeps larger groups' adjacent-member spacing roughly consistent with the
other shapes. `count == 0` returns an empty list; `count == 1` places that
one guard exactly at the anchor rather than drawing a meaningless
ring-of-one.

`GuardCommandScreen.buildMusterBar` already computed `totalWidth`/`startX`
dynamically from `shapeNames.length`, so adding "CIRCLE" as a 4th shape
(5 buttons total counting Retreat) only required shrinking `MUSTER_BTN_W`
from 95 to 78 (`5*78 + 4*BTN_GAP(2) = 398`, under `PANEL_W=440`) - the same
"shrink to fit" pattern already used once before when the Order bar grew
from 2 to 4 buttons (`ORDER_BTN_W` 130→95).

**New: squad quick-select.** The existing "▾" squad-picker button in
`buildSquadBar` previously only cycled `squadNameDraft` through known squad
names, as a typing shortcut. `cycleSquadPicker` now *also* replaces the
`selected` set with every currently-online guard in that squad (a
deliberate replace, not an add-to-selection). No new UI space or buttons
were needed - this reuses the existing picker button.

**Also added this section:** a `CLAUDE.md` file at the repo root (commit
`4a75fd1`), so a fresh Claude Code session (run directly by the user on
their own machine, outside Cowork) has the essential standing rules and
current status immediately on startup, without reading this whole file
cold. `CLAUDE.md` is deliberately short; this file remains the source of
truth for full rationale.

**Still not yet done as of 2026-09-28:**
- The user has not yet run a fresh `.\gradlew.bat build` that would
  include this section's Circle/squad-select changes - the jar currently
  deployed in the mods folder only has the section-32 feature set
  (Line/Column/V, no Circle, no squad quick-select).
- Nothing in section 32 or this section has been gameplay-confirmed by the
  user yet.

## 34. Phase 3E - "Commander's Intent": combat gate fix, posts, explicit targets, fire control (2026-09-28) — AWAITING BUILD

**Context.** New session (cloud, repo pushed by the user) asked for a full architectural
audit plus whatever implementation the audit justified - see `docs/AUDITORIA.md`
(Spanish, for the user) for the audit itself. This section records the code changes.
**This environment cannot reach NeoForge/Mojang Maven either, so nothing here has been
compiled** - only a `javac` parse-only pass (syntax) and a manual import/type review.
Guard Villagers' real 1.21.1 source WAS readable here (via raw.githubusercontent.com),
and every Guard Villagers behavior this section relies on was read from it.

**Real bugs found (source-verified) and fixed:**
1. **Critical - the Phase 3C/§29 target gate vetoed `setTarget(null)`.** NeoForge's
   `Mob.java.patch` (1.21.x) posts `LivingChangeTargetEvent` for EVERY `setTarget`,
   null included. The DEFENSIVE branch cancelled whenever `newTarget != lastHurtByMob`
   (true for null) and PASSIVE cancelled everything, so DEFENSIVE (the default for every
   owned guard) and PASSIVE guards could never drop a target: not when it died, not
   when Guard Villagers' target goals stopped, not on Hold / Patrol toggle / Rally
   release / the §29 "Passive disengages immediately" fix (which therefore never
   worked). A stale target also blocks `WalkBackToCheckPointGoal` and the owner
   teleport (both require `getTarget() == null`). Gate now always allows null.
2. **Attack did nothing to DEFENSIVE/PASSIVE guards** - the same gate vetoed the
   order's own `setTarget`. Commander-issued targets now go through
   `CommandTargeting.issue(...)`, which the gate always lets through.
3. **RUTHLESS cannot target blacklisted mobs, and got stuck on them.** Guard
   Villagers overrides `Guard#setTarget` and silently returns for blacklisted mobs,
   Hero of the Village, team allies, the owner and the owner's pets - before any event.
   The §27 "bypass" premise was wrong, and the nearest-candidate scan kept picking the
   creeper every second while ignoring every other mob. Candidates are now filtered by
   `CommandTargeting.wouldAccept`, a line-for-line mirror of that override.
4. **Formations dissolved.** Muster/Reform/Retreat were a single `navigation.moveTo`,
   overridden by the next goal touching navigation; a patrolling guard walked back to
   its OLD post. Fixed by posting (below).
5. **Every order reset the Ledger's selection** (the server's refresh opened a brand
   new screen). The screen now carries selection/page/drafts/target over.
6. **The Ledger overflowed** at GUI scale 4 on 1080p (fixed 316px). It now sizes rows
   per page to the screen.
7. AGGRESSIVE/RUTHLESS scanned every entity in the level per guard per second; now a
   16-block AABB (far below Sable's "abnormally large" abort size, §17.1).
8. `strength_gained` text said 50 blocks; `RALLY_RANGE` is 100.

**New design (all composition over Guard Villagers' own AI, no mixins):**
- **Posts** (`order/GuardPosts`): Muster/Reform/Retreat/Hold Ground make each slot the
  guard's Guard Villagers patrol post (`setPatrolPos` + `setPatrolling(true)` +
  `setFollowing(false)`), snapped to standable ground. GV's `WalkBackToCheckPointGoal`
  + `GuardGroundPathNavigation#isDone` then hold the formation and return guards to it
  after each fight. **Post leash** (`CommandTargeting.POST_LEASH` = 16): posted guards
  refuse/drop targets further than that from their post (gate + scan). This applies to
  guards posted via GV's own native patrol button too.
- **Order semantics changed:** Hold = *Hold Ground* (disengage + post where standing);
  Recall = teleport + un-post + follow; new Break Ranks (un-post, keep fire/target);
  Retreat = drop fight AND designated target, then Muster at the destination in the
  squad's last shape.
- **Directives** (`contract/DirectiveRegistry`, SavedData `rallyguard_directives`):
  per-guard `holdFire` + designated target UUID/label. Cleared on the guard's death;
  a dead non-player target is cleared from everyone (players are never auto-cleared).
- **Combat gate** (`GuardStanceHandler#onChangeTarget`, order matters): null → allow;
  commander-issued → allow; hold fire → cancel; beyond post leash → cancel; designated
  target → allow; then the unchanged PASSIVE/DEFENSIVE rules.
- **Once-a-second maintenance** (same handler): drop dead targets; enforce hold fire;
  drop targets past the leash; (re)acquire the designated target when in reach (leash
  if posted, `PURSUIT_RANGE` 32 if free); answer another commander's guard that is
  fighting our side (`CommandTargeting.isHostileGuard` - GV's `HurtByTargetGoal`
  ignores Guard attackers, so two players' armies never fought back; requiring the
  enemy guard to be targeting our side prevents stray-arrow wars); then the
  AGGRESSIVE/RUTHLESS scan. AGGRESSIVE also considers hostile guards.
- **Explicit targets** (`order/TargetSpec`): NEAREST / ATTACKER (last entity that hurt
  the commander or their guards, remembered 60 s) / MARKED / PLAYER:<uuid> / NONE.
  `AttackOrder` now takes a resolved target, stores it as the designated target and
  lifts hold fire. New `IssueAttackOrderC2SPayload`.
- **Fire control:** `HOLD_FIRE` / `RELEASE_FIRE` zero-arg orders.
- **Commander's pointer** (`order/CommanderMarks`, in-memory): sneak + right-click an
  entity with the Ledger = mark target; a block = mark formation point (+ facing),
  used by Muster/Retreat instead of the player's position; the air = clear. Feedback
  (sound/particles/action bar) goes to the commander only.
- **Payload/protocol:** `GuardListS2CPayload` gained `targets` + `anchor`; `Entry`'s 6th
  field became `Status(stance, holdFire, target)`. `PROTOCOL_VERSION` 1 → 2.
- **Ledger:** new target row (selector, Attack, Hold Fire, Release Fire); order row is
  Recall / Hold Ground / Reform / Break Ranks; squad names drawn in their squad color
  (shared `util/SquadColors`, also used for world name tags); HOLD FIRE and the
  designated target shown per row; selected count and formation point in the title
  line.
- **Lang:** new keys in `en_us`; new complete `es_es` and `es_mx`.

**New first-time API uses to check if the build fails** (all believed correct for
1.21.1, none compiled yet): `Item#useOn(UseOnContext)`,
`Item#interactLivingEntity(ItemStack, Player, LivingEntity, InteractionHand)`,
`InteractionResult.sidedSuccess(boolean)`, `InteractionResultHolder.sidedSuccess(...)`,
`ServerPlayer#playNotifySound(SoundEvent, SoundSource, float, float)` with
`SoundEvents.BOOK_PAGE_TURN`, `ServerLevel#sendParticles(ServerPlayer, T, boolean, ...)`,
`ServerLevel#getEntity(UUID)`, `ServerLevel#players()`, `Level#getEntitiesOfClass(Class,
AABB, Predicate)`, `new AABB(BlockPos)`, `BlockPos.containing(Position)`,
`BlockState#getCollisionShape(BlockGetter, BlockPos)`, `Entity#isAlliedTo(Entity)`,
`TamableAnimal#getOwnerUUID()`, `CompoundTag#hasUUID`, `ServerStoppedEvent`.

**Open design questions put to the user (not decided here):** Hero of the Village makes
a PvP enemy untargetable by every guard (GV's `setTarget` override); RUTHLESS vs.
blacklisted mobs needs a mixin; whether the no-mixin rule is philosophy or technical
preference; whether the Rally scroll should gather posted guards; Kamikaze and CBC
gunner designs (see the audit, §14); versioning the mounts datapack in this repo.

## 35. Phase 3E.1 - user decisions applied: Hero of the Village mixin, Kamikaze, Ledger clarity, research (2026-09-28) — AWAITING BUILD

The user built §34 (`BUILD SUCCESSFUL`, 2026-09-28 - not yet confirmed that the local
checkout was on this branch at the time) and answered the audit's open questions:
1. Hero of the Village must NOT stop guards from targeting someone.
2. The no-mixin rule is not rigid ("confío en ti") - small targeted mixins are OK.
3. The Rally scroll ignoring posted guards is correct (some guards must keep guarding
   walls/houses; the user will run several villages thousands of blocks apart and an
   order must never hit all of them). UI must be "extremely useful, not confusing".
4. Kamikaze design approved. 5. CBC gunner and mounts: study their code first.
6. The mounts datapack stays outside git.

**Implemented (not compiled - same environment limits as §34):**
- **First mixin** (`mixin/GuardHeroOfTheVillageMixin`, `rallyguard.mixins.json`,
  `[[mixins]]` in `neoforge.mods.toml` - same declaration Guard Villagers uses; no
  refmap needed on NeoForge 1.21.1/Mojmap): `@Redirect` of the single `hasEffect` call
  inside `Guard#setTarget` and `Guard#canAttack`, returning false for Hero of the
  Village when the guard has an owner. Unowned village guards unchanged.
  `CommandTargeting.wouldAccept` no longer filters Hero of the Village.
- **Kamikaze** (`order/KamikazeOrder`, `IssueKamikazeOrderC2SPayload`, Ledger button in
  the Order row using the target row's selection): only guards with a `TntBlock` item
  in the off hand go; Speed II + Resistance II; ignite at 2.5 blocks via
  `Block#onCaughtFire` (the generic NeoForge ignition hook - Lucky TNT's `LTNTBlock
  extends TntBlock` overrides it, read from Neo-LuckyTNTLib source), fuse forced to 10
  ticks on the spawned `PrimedTnt`, guard `kill()`ed when the fuse ends; aborts after
  30 s or if the target disappears. In-memory runs, ticked from `GuardStanceHandler`.
- **Friendly fire additions** (`RallyFriendlyFireHandler`): a commander is never hurt by
  their own guards (kamikaze blast, stray arrows); a guard's shot never hurts a mount
  ridden by a guard of the same commander (fixes the §17.4 "arrows hit horses" report).
- **Ledger clarity pass** (`GuardCommandScreen`): row click selects (check box + tint;
  the "x" button is gone); header chips All / None / per-squad; hover tooltip on every
  button (`gui.rallyguard.tip.*`); selection-dependent buttons disabled while nothing
  is selected; rows sorted (loaded first, by squad, by name) and showing distance or
  dimension; per-row buttons read Post here / Release. Order row = Recall / Hold Ground
  / Reform / Break Ranks / Kamikaze (78px each).
- **Whereabouts**: `GuardRoster.Entry` now stores dimension + last block position
  (older saves load with "" = unknown); `GuardListS2CPayload.Entry.Status` gained
  `distance` and `dimension`.
- **Command feedback** moved from chat to the action bar (`GuardCommandNetworking#feedback`).
- Lang: tooltips and new labels in `en_us`, `es_es`, `es_mx`.

**Research only** (`docs/INVESTIGACION_CBC_MONTURAS.md`, Spanish): CBC's hand-held
autocannon only accepts a Player as aimer, but `CannonMountBlockEntity#setYaw/setPitch`
and `PitchOrientedContraptionEntity#tryFiringShot` are public - a posted guard can crew
a mount through an optional compat module with rate-limited, target-leading aim.
Horses and Ice and Fire CE hippogryphs only let a Player control them; the proposed
"mount driver" forwards the guard's navigation destination and target to the mount's
own (ground or flying) navigation instead of writing flight AI.

**New first-time APIs to check if the build fails:** `@Redirect` mixin on
`Guard#setTarget/canAttack`; `Block#onCaughtFire(BlockState, Level, BlockPos, Direction,
LivingEntity)`; `PrimedTnt#getOwner/getFuse/setFuse`; `LivingEntity#getOffhandItem`,
`#addEffect`, `#kill`; `MobEffects.MOVEMENT_SPEED/DAMAGE_RESISTANCE`;
`Button.Builder#tooltip(Tooltip)`, `Tooltip.create`, `AbstractWidget#setTooltip`;
`SimpleSoundInstance.forUI(Holder<SoundEvent>, float)` with `SoundEvents.UI_BUTTON_CLICK`;
`BlockPos#distSqr`, `ResourceKey#location`.

## 36. Deploy automation, Phase 3F mount driver (horses), Kamikaze friend safety (2026-09-28) — AWAITING BUILD

User report: `.\gradlew.bat build` → `BUILD SUCCESSFUL in 44s` for §35 (commit
`62fe9ff`). User decisions: approve the horse mount driver first; CBC gunner and
hippogryph on hold until horses are tested in-game; Kamikaze must NOT make the own side
immune ("muy roto") but must avoid catastrophic self-damage - "un kamikaze jamás se
explotaría cerca a un amigo"; implement automatic jar deploy BEFORE more features.

**Deploy automation (commit `619f0ba`, done first as asked):** `gradle/deploy.gradle`,
applied from `build.gradle`, adds `deployToMods`, finalizing `build`: deletes other
`rallyguard-*.jar` in `rally_mods_dir` (gradle.properties, forward-slash path to the
Modrinth "veamos" profile), copies the jar, verifies SHA-256, fails loudly on IO error
(e.g. the game locking the old jar), warns and skips if the folder doesn't exist. This
one WAS executed here: tested in a scratch Gradle 8.9 project (deploy + stale-jar
removal with another mod left untouched; missing folder → warning, build OK; compile
error → no deploy).

**Mount driver (`order/MountDriver`, `GuardMountDisciplineHandler` rewritten as its
loop, every 10 ticks):** any owned guard riding a `PathfinderMob` that no player
controls has its intent handed to the MOUNT's own navigation - target (ranged riders
stop within 12 blocks to shoot), else post (arrive within 1.5 blocks), else follow the
commander (start > 7, stop < 4, teleport mount+rider past 48 blocks when the commander
is on the ground). Other vehicles (boat, minecart) still dismount on follow/post.
Orders no longer dismount riders of drivable mounts: `GuardPosts.post` uses
`dismountIfUndrivable`; Recall, the per-row Summon and the Rally scroll use
`MountDriver#relocate` (teleports the mount with its rider); per-row Post here uses
`dismountIfUndrivable`. Mounted guards' formation slots need 3 blocks of headroom
(§29 suffocation report). The mounts datapack (outside git) still refuses to mount
following/patrolling guards - the user can drop those two selector filters now if they
want following cavalry to be mounted automatically.

**Kamikaze:** no ignition with a friend (commander, the commander's other guards except
fellow kamikazes on a run, players on the commander's team) within 6 blocks - the guard
shadows the target and tells the commander once; aborts at the 30 s timeout.
`RallyFriendlyFireHandler`: own-side EXPLOSIONS (`DamageTypeTags.IS_EXPLOSION`) now hit
the commander, their guards and their guards' mounts for 50% of the damage capped at 8
(4 hearts) instead of being cancelled; non-explosion accidents stay cancelled.

**New first-time APIs to check if the build fails:** `Entity#teleportTo(double,double,
double)`, `Entity#getControllingPassenger()`, `PathNavigation#moveTo(Entity, double)`,
`ProjectileWeaponItem`, `DamageTypeTags.IS_EXPLOSION`, `Entity#isAlliedTo(Entity)` on
players, `Player#onGround()`.

**In-game test (horses):** get a guard mounted (datapack) and then: Recall (arrives on
its horse and follows you mounted); Muster Line (rides to its slot and stays); Attack a
mob (charges on horseback; a bow rider stops ~12 blocks away and shoots); Break Ranks
(horse is free again); a mounted guard in a boat/minecart still dismounts on Recall.

## 37. Phase 3G - one mode authority, Summon = hold, scalable formations, squads tab (2026-09-29) — AWAITING BUILD

**Brief (user, 2026-09-29):** keep advancing the roadmap while fixing architectural
inconsistencies as they're touched; establish a clear authority hierarchy between native
Guard Villagers behavior, Rally, stance, orders, formations and execution; Summon should
leave a guard under command, not wandering; one guard per mount; formations must be a
persistent tactical state and scale to hundreds of guards; squads/factions need group
management (create/rename/delete/select, orders to whole squads); robustness so new
mods and future roles (gunner, dynamiter) don't break things. The full decision record,
authority diagram and compatibility matrix live in `docs/ARQUITECTURA.md` (Spanish).

**Finding from Guard Villagers' real source:** GV has exactly two native movement flags,
`Following` and `Patrolling` (+ position). `GuardFollowPacket` only flips following;
`GuardSetPatrolPosPacket` only sets patrolling/position (and its GUI keeps the patrol
button's pressed state client-side, so the first click on a non-patrolling guard is a
no-op - a GV bug left alone). The user's "Stop" was Rally's own label for un-patrol.

**Changes (commits `375124c`, `4e0bd0d`; not compiled here):**
- `order/GuardModes`: FREE / FOLLOW / POST stored only in the native flags; every order
  writes modes through it; `normalize()` (once a second per owned guard) resolves
  following+patrolling in favor of the flag turned on last (tracked in memory).
- Summon (row) and Recall teleport in front of the commander (riders with their mounts)
  and POST the guards; Recall does it in formation (last shape or LINE, 3 blocks in
  front) and records slots. New `FOLLOW` order. The Rally scroll's release posts only
  the rallied guards still following. Per-row Post here / Release write modes too.
- `CommandTargeting.wouldAccept` excludes the guard's own vehicle and any entity
  carrying the commander or their guards; the mount driver keeps one guard per mount.
- `event/SafeTick`: every per-guard periodic step is isolated (skip + log once), the
  Kamikaze tick is wrapped too.
- `FormationShape` scales: LINE rear ranks (min width 16, ~3:1), COLUMN more files,
  V filled wedge > 15, CIRCLE concentric rings (outermost first). Unit-tested here with
  0-1000 guards (exact counts, min gap = spacing). Groups at or under the old limits
  keep their previous layout. New `ADVANCE` order shifts each guard's remembered
  formation 8 blocks along its facing and re-posts it on the same slot.
- Squads: `SquadRegistry.rename/disband`, `SquadActionC2SPayload` (RENAME/DISBAND by
  name - covers unloaded members), protocol version 3. Ledger tabs Guards / Squads; the
  Squads tab lists squads (total / nearby / nearest distance) with Select / Rename /
  Disband (two-click confirm); command rows reorganized (Follow, Recall, Hold, Advance,
  Reform, Break Ranks / target, Attack, Hold Fire, Release Fire, Kamikaze).

**New first-time APIs to check if the build fails:** `Entity#getFirstPassenger()`,
`Entity#getPassengers()` on LivingEntity targets, `ServerLevel#getEntity(UUID)` pattern
match in squad disband, `Set.of()` return in `SquadRegistry#disband`.

## 38. Phase 3H - command without the item, shared selection, Defend, units, identity, guns (2026-09-30) — AWAITING BUILD

**Brief (user review, 2026-09-30):**
- The owner could shoot and kill their own guards.
- The kamikaze fought on its way in.
- Needs:
  - weapon-aware guards (a Scorched Guns gun used the way SG's own mobs use it);
  - selecting and renaming one guard easily, and more info per guard;
  - death visibility, and an end to the "cemetery" of names;
  - sub-groups inside squads;
  - small circles that stop relocating;
  - a less odd Advance;
  - picking attack AND defend targets mid-battle without the item ("debes ingeniarte
    algo");
  - no creation-order list, and a visual mark on selected guards;
  - newcomers vs. squad formation (user undecided).
- A long list of ideas that must not be lost is kept in `docs/BACKLOG.md` (Spanish),
  triaged, with design advice.

**Changes (not compiled here - parse + partial type check only):**
- `RallyFriendlyFireHandler`: the owner can never damage their own guards (the old
  "only while following" condition is gone).
- Kamikaze:
  - disengages at start;
  - the combat gate refuses it any target while running (`KamikazeOrder.isRunning`);
  - it is steered by navigation every 5 ticks, mount included;
  - the mount driver skips it.
- `FormationShape` CIRCLE: single-ring radius is at least the spacing (3-guard circles
  no longer overlap and relocate).
- `AdvanceOrder`: 16 blocks, or to the marked point facing it.
- Hotkeys:
  - Keys: `client/CommanderKeys` (G Command, H Fire, J Ledger; mod-bus
    `RegisterKeyMappingsEvent` with explicit `bus = MOD`, as in GV's `GuardClientEvents`).
  - Server logic: `order/CommanderHotkeys` (server ray cast).
  - Payload: `CommandKeyC2SPayload`.
- `order/CommanderSelection` + `SetSelectionC2SPayload`:
  - one server-side selection per commander;
  - sent by the Ledger, returned in `GuardListS2CPayload.selectedIds`;
  - happy-villager particles over selected guards, for their commander only.
- DEFEND:
  - `DirectiveRegistry` ward (saved "Ward"/"WardLabel");
  - maintained in `GuardStanceHandler.tendWard` (re-post near the ward, and the
    ward's threat first);
  - `GuardModes.postKeepingWard`;
  - every other mode write clears the ward.
  - New `GuardModes.postAndMove` is used by Muster/Reform/Advance, so no order writes
    `GuardPosts` directly any more.
- Units:
  - `SquadRegistry` "Squad/Unit" paths (`units` map saved as "Units", `pathOf`,
    `membersOf`, unit-aware rename/disband);
  - Ledger Squads tab lists units indented, with **View ›** (filtered Guards tab +
    "✕" chip).
- Identity:
  - `GuardManageC2SPayload` FORGET (offline rows only, two-click) / RENAME (✎ with one
    guard selected);
  - the guard list carries the guard UUID and the ward label;
  - hover a row for the full details;
  - rows are sorted by squad, then nearest.
- Losses:
  - `GuardRosterCleanupHandler` adds a "↳ squad: N left · army: M" line after GV's own
    death message;
  - it also cleans up on `EntityLeaveLevelEvent` with a destroying removal reason
    (GV's zombie-villager conversion returns before NeoForge's death hook), saying the
    guard was lost.
- `ReformOrder`: if some selected guards have no slot, the whole selection is re-mustered
  on the formation the others remember (newcomers join only by an explicit Reform).
- `compat/ScorchedGunsCompat`:
  - if `scguns` is loaded, adds SG's own
    `top.ribs.scguns.entity.ai.GunAttackGoal(PathfinderMob, int)` (difficulty 4) to
    every Guard at priority 2 by reflection;
  - the mount driver treats SG guns as ranged.
  - Read from `sadeast69/ScorchedGunsNeoforge` (mod 1.5, MC 1.21.1).
- Protocol version 4.

**New first-time APIs to check if the build fails:**
- NeoForge / client:
  - `RegisterKeyMappingsEvent`, `ClientTickEvent.Post`;
  - `KeyMapping(String, IKeyConflictContext, InputConstants.Type, int, String)` (all
    verified in NeoForge 1.21.1 source);
  - `GuiGraphics#renderComponentTooltip(Font, List<Component>, int, int)` (verified in
    NeoForge's GuiGraphics patch).
- Vanilla world and entities:
  - `ServerLevel#sendParticles(ServerPlayer, T, boolean, double...)`;
  - `Entity#pick(double, float, boolean)`, `ProjectileUtil.getEntityHitResult(...)`;
  - `AbstractHorse#getOwnerUUID/isTamed`, `Entity#startRiding(Entity, boolean)`;
  - `Entity#getRemovalReason()` / `RemovalReason#shouldDestroy()`, `EntityLeaveLevelEvent`;
  - `GoalSelector#getAvailableGoals()`.

## 39. Phase 3H.1 - review fixes, user decisions, engagement cap, scale, finite ammo (2026-10-04) — AWAITING BUILD

**User test of 3H (2026-10-04):**
- Hotkeys G/H/J work; selection, defend, mounting, retreat point, rename, loss notices,
  Forget and Reform-with-newcomers all work.
- Problems found:
  - G-attack overrode hold fire;
  - the owner could still hurt their guards' horses;
  - formations ordered while flying went wrong;
  - a mounted guard called to the commander "doesn't run";
  - units worked but weren't intuitive;
  - the G-selected guard was hard to find in the Ledger.
- New decisions:
  - newcomers join formations;
  - squad HQs (banners) with the order temporary point > HQ > follow;
  - guerrillas as an addon;
  - veterancy before classes, item = class.
- 28 numbered ideas plus closing notes, all recorded in `docs/BACKLOG.md` §7. The
  veterancy design proposal is in `docs/VETERANIA.md`.

**Changes (not compiled here - parse + partial type check only):** see
`docs/ARQUITECTURA.md` D14-D20.
- `AttackOrder` keeps hold fire (it only designates while holding), clears the ward.
- Defend: root-of-chain, no self, cycles flip.
- Friendly fire: the owner can't hurt a mount ridden by their guard.
- `GuardPosts.groundAnchor` / `groundBelow` (64-block drop): Muster and Recall anchors
  reach the ground.
- `MountDriver` gallops (x2.6) beyond 14 blocks. Not reproduced here - the user's case
  ("no corre") may be a different command; ask.
- Loss lines name the guard; the Ledger opens with the selected guards first.
- Unit UX: inside a squad's view the field is a unit name, chips show units, and Clear
  only removes from the unit.
- `FormationJoin` (assign → join the majority formation within 128 blocks).
- `SquadRegistry` HQ + member index; `RetreatPlanner`; HQ claim by sneak + G on a banner;
  unmark by sneak + G on the marked spot; `CommanderDeathHandler` (followers hold +
  vengeance).
- `Engagement`: melee cap per target `clamp(round(width*4)+2, 3, 8)`, screen ring at 4.5
  blocks, saturated targets skipped by stance scans; the stance loop is sliced over 20
  ticks.
- `GuardListS2CPayload`: `MAX_ROWS` 2000, server `SquadInfo` totals + HQ, `omitted`
  count; client lists capped at 4096; protocol version 5.
- `compat/GunAmmo`:
  - `Rationed` wraps SG's `GunAttackGoal`;
  - a reload costs 1 off-hand ammo item per 4 rounds (ammo item via SG's
    `GunItem#getModifiedGun -> Gun#getProjectile -> Projectile#getItem`, `AmmoCount`
    custom data);
  - idle restock from a `Container` within 4 blocks, every 5 s.
- The Ledger's Muster buttons draw formation icons from Rally's own geometry. Villager
  Recruits is "All Rights Reserved" (its README), so its textures are not used.

**Scale answer (user question 8):**
- Rally's own per-guard work is O(guards) once a second, spread over 20 ticks. Squad
  lookups are O(1), the engagement table is one pass, and the Ledger is capped.
- Unloaded guards cost nothing (UUID registries).
- The real ceiling is Guard Villagers' own per-entity AI (goals, pathfinding) and
  vanilla entity ticking for LOADED guards: a few hundred in one place is a realistic
  heavy load for any server. Thousands are fine spread across distant villages.
- Not measured here (no game runtime); a profiling run (`/spark` or similar) in the
  user's world is the way to confirm.

**New first-time APIs to check if the build fails:**
- `Level#getMinBuildHeight()`, `BlockPos.betweenClosed`, `AbstractBannerBlock`;
- `Goal#getFlags()` / `setFlags`, `Container#removeItem`;
- `DataComponents.CUSTOM_DATA` / `CustomData`, `EventPriority.LOWEST` on
  `@SubscribeEvent`.


## 40. E1 - foundation + ranged fire (2026-10-05) — AWAITING BUILD

Spec: `docs/PROMPT_E1.md` (plan `docs/PLAN_ENTREGAS.md`). Not compiled here (parse + partial type check).
- Commit 1 (ARREGLOS + E1-G): `RallyFriendlyFireHandler` rewritten in layers (melee / `ProjectileImpactEvent` /
  damage with owner-of-projectile fallback / INFO log of every hit on a mount ridden by an owned guard);
  `compat/McaMobCompat` (reflection listener on Mob Compat's `GuardInteractEvent`; `GuardModes` reconciles
  flag/owner changes made outside Rally: adopt within 60 s of an owner interaction, else revert);
  `CommanderKeys` without the deprecated `bus=`; `config/RallyConfig`; `combat/` package: `WeaponProfile`,
  `Ammo` (single 1-item-per-4-shots constant, off hand + pack), `Ballistics`, `FireMode` (+ FIRE_* orders,
  Ledger button), `FriendIndex`, `RangedFireGoal` (long shots beyond GV's 15-block bow radius, volleys,
  fire-mode movement), `RangedFire` (installs the goal; GV's own arrows now cost real arrows);
  ranged post leash = max(16, 0.8 x maxRange); protocol 6.

**First-use APIs (check here first if the build fails):** `ModConfigSpec` builder + `ModContainer#registerConfig`,
`ModConfig.Type`; `ProjectileImpactEvent` (`getProjectile`, `getRayTraceResult`, cancelable);
`EntityJoinLevelEvent#loadedFromDisk`; `IEventBus#addListener(EventPriority, boolean, Class, Consumer)`;
`ICancellableEvent#setCanceled`; `OwnableEntity#getOwnerUUID`; `DamageTypeTags.IS_PROJECTILE`;
`DamageSource#getMsgId`; `AbstractArrow.pickup` / `Pickup.DISALLOWED`; `Mob#getSensing().hasLineOfSight`;
`Goal#setFlags(EnumSet)`.
- Commit 2 (E1-A..D): `contract/PackRegistry` (6-slot war pack per guard UUID, world data) + `combat/GuardPack`
  (ammo/consumables from pack and backpacks, off-hand refill, second-weapon swap, resupply into the pack,
  drops on death/zombie, goes to the commander on Forget) + `screen/PackMenu`/`PackScreen` (Ledger ▤);
  backpacks = any pack item exposing `Capabilities.ItemHandler.ITEM` with > 6 slots; `contract/HqRegistry`
  (HQ records; squads point by `hqId`; 3H.1 inline HQs migrate on first use; banner breaks keep the HQ),
  Ledger HQ tab + `HqActionC2SPayload`, sneak+G on a banner links the picked HQ; `MountDriver` gallops (x3.0)
  until 3 blocks out, tag `rallyguard:guard_mounts` (+ config `mountsFromTagOnly`, default false).
  First-use APIs: `ContainerHelper.saveAllItems/loadAllItems(tag, list, provider)`, `SimpleContainer#addItem`
  /`addListener`, `IMenuTypeExtension.create` (already used), `AbstractContainerScreen` labels,
  `ItemStack#getCapability(Capabilities.ItemHandler.ITEM)`, `IItemHandler#extractItem`, `PotionContents#forEachEffect`,
  `DataComponents.FOOD/POTION_CONTENTS`, `Guard#eat(Level, ItemStack, FoodProperties)`, `Pig#isSaddled`,
  `TagKey.create(Registries.ENTITY_TYPE, ...)`, `Containers.dropItemStack`.
- Commit 3 (E1-E/F): `debug/Perf` (Rally ms/tick per system, `/rally debug perf`), `/rally debug spawn <n> [squad]`,
  `/rally debug rank <n>` (`DebugRanks`, no behavior until E2), `/rally sim <A|B|C|D|ALL> [seed] [reps]`
  (`SimRunner` arena 64 E / y 200, `SimProbe` counters active only during a run, JSON in `rally-sim/`);
  stance loop capped by `maxGuardsProcessedPerTick`, mount driver reuses the stance list (no entity scan);
  Ledger batches (`ledgerPageSize`, `LedgerPageC2SPayload`, « » buttons). Docs: `docs/ESCALA.md`, `docs/SIMULACIONES.md`.
  First-use APIs: `RegisterCommandsEvent`, Brigadier `Commands.literal/argument`, `LongArgumentType`,
  `CommandSourceStack#getPlayerOrException/sendSuccess(Supplier, boolean)`, `ServerTickEvent.Post`,
  `EntityType#spawn(ServerLevel, BlockPos, MobSpawnType)`, `tallestegg.guardvillagers.GuardEntityType.GUARD`,
  `Zombie#setBaby`, `Mob#setDropChance`, `Level#setBlockAndUpdate`, `Files.writeString`.
- Decisions taken without asking (prompt rule 8): "Si muere el jugador: se queda como está" read as "keep the
  current behavior" (followers hold + vengeance unchanged); default mounts tag + `mountsFromTagOnly=false` so
  datapack mounts keep working; finite arrows apply to Guard Villagers' own bow/crossbow fire too
  (`ranged.finiteArrows`, default true); pack opens within 16 blocks.

## 41. E2A - spec `docs/PROMPT_E2A.md` (2026-10-07) — AWAITING BUILD

### Block 0 - E1 fixes (`docs/FEEDBACK_E1.md` §B)
- B1 drowning: `GuardPosts.isStandable` rejects fluid in the headroom, `groundBelow` stops at fluid, `standableNear`
  falls back to `dryNear` (r 12); `order/WaterSafety` (stance loop): underwater riders dismount, wet posts move to dry
  land, swimmers get an upward push + jump + path out; `/rally debug spawn` skips wet cells.
- B2 dry ammo: `GuardPack.checkDry` after the weapon swap - a guard whose only weapon is an empty ranged one is
  `isOutOfAmmo`: the gate refuses its targets, it disengages and goes to its squad HQ (else follows its commander).
- B3 real arrows: `combat/ArrowFactory` builds every Rally arrow through the AMMO item's own `createArrow` and the
  bow's `customArrow` (modded bows/arrows keep their entity and damage), crit, Flame, and the legacy IaF dragon
  bow's Power formula for classes in `ranged.legacyPowerWeapons`. Rally now fires ALL guard bow/crossbow shots.
- B4/B5: `WeaponProfile.forGuard` - SG range from the gun's projectile speed×life (`GunAmmo.rangeOf`), × (1 + rank ×
  `ranged.rangePerRank`); volley spread × max(0.3, 1 − rank × `ranged.volleySpreadPerRank`). Free guards scan up to
  their weapon's reach (16..64).
- B6 HQ: `RetreatPlanner` sends HQ groups to `GuardPosts.reachableSpots` (BFS flood from the HQ, Manhattan ≤ 24,
  ≤ 800 cells - spreads inside the building via its doors); `order/HqLogistics`: door opening (nav flag + vanilla
  `OpenDoorGoal`), park the mount within 8 blocks of the HQ, call it back and remount on leaving.
- B7 MCA Crime: optional mixin config `rallyguard.compat.mixins.json` (`required:false`) +
  `mixin/compat/CompatMixinPlugin` that reads `GuardChallengeService` bytecode and applies the Void or Bool variant
  only if `challenge(ServerLevel, LivingEntity, ServerPlayer)` exists with that return type; cancels owned guards'
  challenges (option b of `docs/API_MODS.md`; (a) needs Mob Compat API not documented there).
- B8 the Guards tab lists only unsquadded guards unless a squad/unit filter is open. B9: Rally already lets the owner
  through; MCA's menu needs Mob Compat `guardMcaInteraction=true` (logged at start-up); GV-GUI ordering = open question.
- B10 spikes: registries preloaded on `ServerStartedEvent`; mount re-pathing kept when the running path ends near
  the spot, else ≤ 6 new paths/tick; selection markers look at ≤ 96 ids per pass with a rotating cursor.
- First-use APIs: `ArrowItem#createArrow(Level, ItemStack, LivingEntity, ItemStack)`, `ProjectileWeaponItem#customArrow
  (AbstractArrow, ItemStack, ItemStack)`, `AbstractArrow#setCritArrow/igniteForSeconds(float)/setBaseDamage/pickup`,
  `EnchantmentHelper.getItemEnchantmentLevel(Holder, ItemStack)`, `RegistryAccess#registryOrThrow(Registries.ENCHANTMENT)
  .getHolderOrThrow(Enchantments.FLAME)`, `ModConfigSpec.Builder#defineListAllowEmpty`, `Level#getFluidState`,
  `FluidState#isEmpty`, `Entity#isUnderWater/isInWater`, `Mob#getJumpControl().jump()`, `OpenDoorGoal(Mob, boolean)`,
  `GroundPathNavigation#setCanOpenDoors`, `GoalSelector#getAvailableGoals`, `PathNavigation#getTargetPos/
  setSpeedModifier/isDone`, `Vec3i#distToCenterSqr(double,double,double)`, `ServerStartedEvent`, Mixin
  `IMixinConfigPlugin`, `MixinService.getService().getBytecodeProvider().getClassNode(String)`, `@Pseudo`,
  `@Mixin(targets=..., remap=false)`, `CallbackInfoReturnable#setReturnValue`.

### Block 1 - command capacity, computed captain, subunits, succession
- `command/CommandStructure`: `capacity(rank)` from `command.capacityByRank` (4/10/25/45/80/120), `captainOf(unit)`
  (most capacity, then rank, then lowest UUID - computed, never stored), `partition(guards)` into subunits led by the
  best remaining guard + its nearest guards, size clamped to `minSubunitSize..maxSubunitSize` (8..120).
- `command/SquadDirectives`: one shared `Directive` (kind, anchor, facing, captain, tick, unit index/count) pushed once
  per order to each member (`Muster` per subunit; registered orders, Attack, Retreat, hotkeys via `pushOrder`, which
  keeps the previous subunit grouping). The decision ladder's rung 1 reads it (block 4).
- `command/SlotQueue`: Muster/Advance record slots at once and hand the posts+paths out `command.slotsPerTick` (12)
  per tick; every `GuardModes` mode change cancels a queued slot (newer order wins). Perf system `slots`.
- `formation/UnitLayout`: subunit anchors laid out with the same shape at macro scale (spacing = biggest subunit
  footprint + 2 slots); Advance moves the army centre and rotates each subunit around it (no collapse onto the mark).
- `command/CaptainSuccession` (LivingDeathEvent, HIGH): captain of the dead guard's directive group or squad path
  falls -> next captain, directive re-pushed, commander told; > `maxReliefs` (3, 4 when the unit ≥ `largeArmySize`)
  -> ROUTED: `routFleeShare` flee via `RetreatPlanner`, the rest break ranks. Count resets after 5 min.
- Ledger: Condition gains `command` ("rank|captain|name"), protocol "7"; ★ captain + rank numeral + tooltip.
  `veterancy/Veterancy` is the single rank facade (test ranks now, earned ranks in block 2).
- First-use APIs: `ModConfigSpec.Builder#defineList(String, List, Predicate)`, `@SubscribeEvent(priority = ...)`,
  `PlayerList#getPlayer(UUID)`, `ServerPlayer#sendSystemMessage`, `StreamCodec.composite` with 6 fields (Condition).

### Block 2 - veterancy = decision quality
- `contract/VeterancyRegistry` (SavedData `rallyguard_veterancy`, per guard UUID): xp, rank, kills melee/ranged/gun,
  near-death, battles, totems, seconds mounted / near the commander, fights per enemy type (≤ 64), traits. Removed on
  death/Forget with the rest of the guard's records. `veterancy/Veterancy` = facade (cached registry; rank = max(earned,
  `/rally debug rank`)); `legendBonus` keeps adding capacity past rank 5 (+10 per doubling of the extra XP, max 10).
- `veterancy/VeterancyEvents`: XP only vs real enemies (Enemy, other players, other commanders' guards, mobs targeting
  our side): damage dealt ×`xpPerDamage`, received ×0.5, kills `killXp`, all × `repeatDecay`^(kills of that type);
  first fight vs a new type `newEnemyTypeXp`; fights > `longFightSeconds` survived `longFightXp`; < 3 hearts = near death.
  One `Trait` per rank (TANK/MARKSMAN/DUELIST/RIDER/STEADY/ASSAULT/SCOUT) scored from history + `Affinity` (rolled
  from the UUID - MCA personality accessor not documented in API_MODS: open question). Rank-up chat message.
- Totems: `LivingUseTotemEvent` cancelled unless the totem was in hand ≥ `totemDelayTicks` (Tank: `totemDelayTankTicks`);
  ≥ `kamikazeRevives` revives within the window -> chance of 60 s reckless (no post leash; kamikaze if armed).
- Ranks now drive range/accuracy (`WeaponProfile`, `RangedFireGoal`) and command capacity. `/rally debug vet`.
- First-use APIs: `LivingDamageEvent.Post#getNewDamage`, `LivingUseTotemEvent` (cancellable; if this class name is wrong
  on 21.1, delete `onTotem` - nothing else depends on it), `BuiltInRegistries.ENTITY_TYPE.getKey`, `CompoundTag#putIntArray`
  /`getAllKeys`, `StringTag.valueOf`, `ItemStack#is(Item)`, `RandomSource#nextDouble`.

### Block 3 - simulation v2 (`docs/SIMULACIONES.md`)
- `debug/sim/SimScenario` (matrix as JSON: `/rallyguard_sim/scenarios.json` in the jar, copied to
  `config/rallyguard-sim-scenarios.json`; 41 scenarios, presets quick/standard/long/all, cross-scenario checks),
  `SimArena` (ground site search that refuses builds/water/slopes, staged floor + wall build, per-scenario terrain,
  force-loaded chunks), `SimRun` (one jsonl line), `SimReport` (runs.jsonl, summary.json, summary.md ≤ 150 lines
  with mean ± SE, mirror check, speed control, automatic "Hallazgos"). `SimRunner` rewritten as a batch queue:
  build → setup (alternate spawns, identical kit/health, captains + directives) → 3 s form → fight (driver only
  re-targets idle units) → end (side gone / cap / stuck → one retry) → full cleanup. `SimProbe` counts per side;
  sim units never earn XP. `/rally sim batch <preset|all> [--minutes N] [--seeds N] [--tickrate sprint|normal]`,
  `/rally sim stop`, aliases A–D/ALL/any id. Config `simulation.*`.
- First-use APIs: Gson `JsonParser.parseString`/`JsonObject`/`GsonBuilder`, `FMLPaths.CONFIGDIR`, `Files.write`/
  `StandardOpenOption.APPEND`, `ServerLevel#setChunkForced`, `Level#getChunk(int,int)` + `LevelChunk#getBlockEntities`,
  `Level#getHeight(Heightmap.Types, int, int)`, `BlockTags.PLANKS/WOOL/STAIRS/SLABS/DOORS/BEDS/FENCES/WALLS`,
  `Level#setBlock(pos, state, Block.UPDATE_CLIENTS)`, `BuiltInRegistries.BLOCK/ITEM/ENTITY_TYPE.get(ResourceLocation)`,
  `MinecraftServer#tickRateManager().requestGameToSprint(int)/stopSprinting()`, `Horse#setTamed`,
  `AbstractHorse#equipSaddle(ItemStack, SoundSource)`, `Entity#startRiding(Entity, boolean)`, `Mob#getAttribute(
  Attributes.MAX_HEALTH).setBaseValue`, `DamageSource#is(DamageTypes.DROWN)`, `ServerPlayer#hasDisconnected`,
  `StringArgumentType.greedyString`.

### Block 4 - TerrainSense + DecisionLadder + unstuck (package `tactics/`)
- `DecisionLadder.decide(Situation, rank)`: pure, rungs 0-7 of the playbook (+ UNSTUCK); rank gates rungs (cover 2,
  band 1, priority 3 / Scout 2, slot 2 / Steady 1); veterans (≥ 2) pick their own `FireMode` when the player ordered
  none (`Tactics.effectiveFireMode`, read by `RangedFireGoal`); an ordered style always wins.
- `Situation` (cheap signals only) built by `Tactics.step`, called after `maintain` in the stance loop's 1 Hz turn
  (Perf `ladder`); idle guards (no target, path done) skip everything. Decisions use MountDriver navigation and the
  gate (`setTarget`) - never `GuardModes`. Retreating guards refuse new targets for 5 s (gate), self-defence allowed.
- `SquadPicture`: one `getEntitiesOfClass` (r 24) per squad per second: enemies (`VeterancyEvents.realEnemy`), allies,
  centre, nearest priority threat (FlyingMob / no-gravity airborne / RangedAttackMob / Creeper / ≥ 60 HP), fleeing.
- `TerrainSense`: 8 candidates r 6, exactly 12 block reads each, 3 s cache per squad, only with a captain ≥ rank 2;
  tags COVER_GOOD/BAD, HEIGHT_GOOD/BAD, DEFENSIBLE/OPEN, NEAR_WATER, IN_FOREST, CHOKEPOINT; score −2..+2 weighted by
  `Doctrine` (7 doctrines of playbook §4 as weights). `ExplosionEvent.Detonate` drops readings within 24 blocks.
- `Unstuck` v1: < 0.3 blocks moved in 4 s while it wants to move -> jump+push, then re-path, then (pickaxe +
  `mobGriefing` + boxed in) dig feet/head toward the goal, ≤ 6 blocks per episode, never block entities/unbreakable.
- First-use APIs: `ExplosionEvent.Detonate#getAffectedBlocks`, `Level#canSeeSky`, `BlockState#blocksMotion/
  getDestroySpeed/hasBlockEntity`, `Level#destroyBlock(pos, drop, entity)`, `GameRules.RULE_MOBGRIEFING`,
  `Direction.getNearest(double,double,double)`, `ItemTags.PICKAXES`, `BlockPos.containing`, `BlockTags.LOGS/LEAVES`,
  `FlyingMob`, `RangedAttackMob`, `Entity#isNoGravity`, `PathNavigation#isDone`.

### Block 5 - universal orders + Ledger cleanup (FEEDBACK_E1 §C)
- `order/OrderSemantics`: role per guard (MELEE / ARCHER / GUNNER / MOUNTED melee) and the order × role table
  (javadoc). Movement orders are universal; fire styles degrade for melee/riders (ADVANCING -> ADVANCE, STILL/
  VOLLEY/RETREATING -> HOLD, NORMAL -> nothing). `issueOrder` executes per part, logs degradations at DEBUG and tells
  the commander in chat.
- Doctrines: `DOCTRINE_<NAME>` orders (`DoctrineOrder` -> `Tactics.setDoctrine`, memory only) for the 7 playbook
  doctrines; Ledger "Doctrine" cycle button.
- Attack a whole group: target option `GROUP` (marked, else nearest enemy + its squad if it is another commander's
  guard in a squad, else every accepted enemy within 8 blocks, ≤ 64) - guards spread over members, melee capped by
  `Engagement.capFor`. Attack WITH a whole squad: ⚔ button on each Squads-tab row.
- Ledger bars regrouped with captions: Position (Follow/Recall/Hold/Break ranks/Retreat), Formation (4 shapes +
  Advance + Reform), Attack (selector/Attack/Hold fire/Release fire/Kamikaze), Style (stance, fire style - only
  if the selection has a bow/crossbow/gun -, doctrine; one cycle button each). Row `command` tag gains role and
  doctrine (no protocol change: same field).
- First-use APIs: none new beyond earlier blocks (`Button.builder`, `GuiGraphics#drawString` already in use).

### Block 6 - verification, decisions, open questions
- Self-review fixes: sim spawn order alternates which side spawns first (mirror bias); dead Ledger constants removed.
- Decisions taken without asking (rule 6): rank-0 guards also retreat below the doctrine line (playbook rung 7 is rank
  0) - tune with the sims; subunit floor 8 (`minSubunitSize`) so recruits don't form 50 groups of 4; doctrines and
  veterans' own fire styles are memory-only; B7 = option (b) (mixin), (a) needs Mob Compat API not in API_MODS.md;
  sim units never earn XP; the arena stays built after a batch (it is the check zone).
- Open questions: return type of `GuardChallengeService.challenge` (the plugin handles void/boolean only); MCA
  personality accessor for a bridged guard (genetics use the UUID); whether GV's owner GUI runs before Mob Compat's
  menu (B9); Scorched Guns item ids for the sim `gunner` role (`simulation.gunItem`); `LivingUseTotemEvent` and
  `AbstractHorse#equipSaddle(ItemStack, SoundSource)` names on 21.1 (first build will tell).

## 42. E3A - forces, leadership, Battle Planner (spec `docs/PROMPT_E3A.md`, 2026-10-08) — AWAITING BUILD

### Block F - fixes (≤ 20 %)
- F1: `contract/Factions` (faction = owner UUID; sim sides = 2 owners) + `mixin/compat/GuardFactionMixin`: in GV's
  `setTarget`/`canAttack(LivingEntity)` the single `List.contains` (the `Mob Blacklist` check, default includes
  `guardvillagers:guard`) answers false for an enemy-faction guard (target captured at HEAD in a ThreadLocal).
  `CompatMixinPlugin` applies it only if both methods still contain `List.contains(Object)`. `wouldAccept` agrees.
- F2: `AttackOrder` - a target beyond reach makes the guard re-post at its weapon's optimal range on the line to
  the target (≤ 160 blocks) and walk/ride there; chat says how many are closing in.
- F3: `Ballistics.maxRange(speed)` (APEX arc back to launch height, cached); volleys reach 95 % of it; a posted
  archer whose fire mode is VOLLEY gets that as its leash.
- F4: `HqLogistics` parks a rider only within 3 blocks of its own spot; a guard sent home that stopped > 1.2
  blocks short (GV walk-back gives up for 10 s after a failed path) is sent again each second.
- F5: PASSIVE answers whoever just hit it (still never takes the initiative).
- F6: doctrines saved per guard in `DirectiveRegistry` ("Doctrines" list); fire mode already was.
- F7: sim - stuck runs are kept and reported as "SIN ENFRENTAMIENTO" (excluded from win rates), retry still
  queued; summary rewritten after every run; non-boss mobs take the guard's base health/attack/armor/toughness/
  knockback resistance (zombies hit 3 vs a guard's 1 and had 2 armor + reinforcements = the zombie-5 result).
- F8: Ledger row ♞/⇩ button -> `GuardManageC2SPayload "MOUNT"` -> `order/GuardMounts.toggle` (own mount first).
- F9: scenarios `formation-v-100/200` (tag `formation`: scattered, one V muster, `formationKept` at 30 s).
- First-use APIs: Mixin `@Redirect` on `java/util/List.contains` + `MethodInsnNode` scan, `DefaultAttributes.getSupplier`,
  `AttributeSupplier#hasAttribute/getBaseValue`, `Attributes.SPAWN_REINFORCEMENTS_CHANCE/ARMOR_TOUGHNESS/KNOCKBACK_RESISTANCE`.

### Block V - veterancy v2 (`docs/ARQUITECTURA_VETERANIA.md`)
- V0: rank 0-5 only; past rank 5 XP feeds paths (SURVIVAL / COMMAND / MOUNT / HISTORY, `pathLevel` = cap·(1−e^(−xp/scale)))
  + weapon mastery per archetype (`veterancy/WeaponMastery`); `legendBonus` 0 unless `veterancy.legendCapacitySteps`.
  Rank names 0 Recruit · 1 Trained · 2 Experienced · 3 Veteran · 4 Elite · 5 Legend (en/es/es_mx/pt_br).
- V1: `veterancy/Threat` (zombie = 1, clamp 0.25..8). XP per hit = xpPerThreat × threat × min(damage, health it had)/maxHealth ×
  type decay × outnumbered (≤ 2) × cover bonus; kill bonus split by damage share among guards hitting it in 15 s;
  anti-abuse: immobile enemy (5 s), same victim > 45 s, ≤ 80 XP/min, ≤ 50 % of the rank band per victim.
  `LivingDamageEvent.Pre` records health before the hit. V2: captains get command XP at fight close (unit survival,
  field held, cohesion) - `Record.commandXp`, also counts for rank. V7: `Record.story` (≤ 12) + reliefs.
- V3: traits as preference bits in `Situation` (Marksman height/flyers/longer band, Duelist counter, Rider slot+charge,
  Assault opens, Tank holds, Steady never flees a rout, Scout wider picture); new trait LEADER (for A1).
- V5: `tactics/AdvancedCore` (10 steps, rank 5, enemy known, `advancedBudgetMs` per tick; else DEGRADED_BUDGET);
  new rungs FOCUS_FIRE, COUNTER, SWAP_WEAPON (`GuardPack.chooseWeapon`), SCOOT, TAKE_POSITION, PAIR_BOUND.
- V6: `veterancy/Genetics` - reflection chains `veterancy.mcaPersonalityAccessors` ("a.b" or "Class#static.b"), else UUID.
- V8/V9/V11 sim: `pace-*` (XP on) -> "Ritmo de veteranía"; `ceiling-*` (preset `ceiling`, 10 seeds) -> "Techo de
  excelencia" (7 metrics R0 vs R5, composite index, captain high/low, 3:1, duel alarm); `DecisionReason` + `DecisionTrace`
  (last reason always; ring of 8 when `debug.decisionTrace` or a sim runs) + `/rally debug why <guard>`; reason counts.
  The sim matrix in config now gets missing default scenarios/presets merged in.
- Ledger card (no new buttons): rank description, traits, leadership, last 3 story lines, last decision.
- First-use APIs: `LivingDamageEvent.Pre`, `LivingEntity#getAbsorptionAmount/getArmorValue/getLastHurtByMobTimestamp`,
  `AttributeMap#hasAttribute`, `Component.translatableWithFallback`, `EntityArgument.entity/getEntity`, `Level#getDayTime`.

### Block A1 - forces and leadership
- `contract/ForceRegistry` (SavedData `rallyguard_forces`): a force = one squad + doctrine + declared units with roles;
  members stay in `SquadRegistry` ("Squad/Unit"). `force/Forces`: force of a guard, units (members, computed captain,
  `Capability` set, type infantry/archers/cavalry/reserve), computed general. `force/ForceCommands`: `/rallyforce
  create|dissolve|doctrine|unit add|unit remove|role|info` (no new Ledger buttons). `force/Roles` (A3 roles).
- `force/Leadership`: score 0..100 = weights (`leadership.scoreWeights`) × rank, LEADER trait, command XP (1−e^(−x/300)),
  unit mean rank, COMMAND path; cache invalidated only by events (rank-up, `SquadRegistry` mutators, death, relief).
  Captain = best score (`CommandStructure.BEST_FIRST`), capacity = rank capacity × (0.75 + 0.5·q), subunits capped by
  `maxDirectSubordinates`. Execution only: SlotQueue response delay (≤ `maxResponseDelayTicks`), formation tolerance
  (`Situation.leaderTolerance`), leash × pursuit factor, relief delay (`CaptainSuccession.tick`). Never health/damage/speed.
- Force doctrine overrides a member's own (`Tactics.doctrineOf`). Ledger card shows the score.
- First-use APIs: none new (Brigadier `StringArgumentType.string`, SavedData as before).

### Block A2 - versioned directives, reports, force rules, precedence
- `force/UnitOrders`: one versioned record per unit ("owner#squad/unit"); kinds HOLD/MOVE/FALLBACK/RETREAT_HQ/ATTACK_AREA/
  FIRE_STYLE/FOLLOW/RELAX/FREE; each guard compares versions in its 1 Hz turn (`applyIfNewer`, stance loop) and takes
  its own slot (UUID order) through `SlotQueue`; a player's explicit order (`SquadDirectives.pushOrder`, `MusterOrder`)
  beats automatic directives for `leadership.playerOverrideSeconds` (60). Doc: `docs/ORDENES_JERARQUIA.md`.
- `force/SituationReports`: unit report (alive/total, health, ammo, enemies seen, contact) published only on change;
  per-force `KnownEnemy` sightings (60 s TTL). `force/ForceRules`: {id, scope, metric, enter/exit (hysteresis), action,
  priority, cooldown, min duration, role filter}; doctrine defaults (force retreat, unit fallback, support resupply
  (K-02), anvil -> reserve relief); `selfTest()` 39↔41 % printed in every summary. `force/ForceBrain`: per force ≤ 1/s,
  staggered (Perf `forces`): reports -> roles -> rules -> unit directives on change -> operation (A3) -> plan (A4).
- Sim: `precedence-player-vs-force` (half the unit under a player order must ignore a force directive).

### Block A3 - force roles and maneuvers
- `force/Roles`: ANVIL / HAMMER / SCREEN / SUPPORT / FLANK / RESERVE - declared roles kept, the rest proposed from
  composition + doctrine and recomputed every evaluation (fallen captains -> re-planned roles -> new unit orders).
- `force/Operations`: HOLD_LINE, FALLBACK_FIGHTING (6 blocks every 6 s, ≤ 5 steps), ADVANCE_BY_TURNS (front/fire groups
  alternate every 8 s; HOLD_LINE when the enemy is within 12), FLANK (around 15 blocks, then ATTACK_AREA),
  ORDERLY_RETREAT (support/reserve to HQ, anvil/screen cover, everyone after 10 s). One versioned unit directive per
  unit and phase; captains' translation: shape by role (LINE / V / COLUMN), support style VOLLEY (4+ shooters) /
  RETREATING (Harass) / STILL. A force-scope retreat rule starts ORDERLY_RETREAT. `/rallyforce op <squad> <op|stop>`.
- Levels per ARQUITECTURA §10 documented in `docs/ORDENES_JERARQUIA.md` (guard / unit / force / plan).

### Block A4 - Battle Planner v1
- Item/placement: the Scroll of Rallying used on a block of tag `rallyguard:planner_tables` inside one of the player's HQ
  zones (`hq.radius`, 16) lays it there as an invisible `ItemFrame` (custom data `RallyPlanner`) and opens the planner;
  clicking that frame reopens it (`event/PlannerTableHandler`). Its old sneak-use rally stays = planner template RALLY.
- `screen/BattlePlannerScreen` (client): top-down map from the client's loaded chunks, 1024 columns per frame into a
  128² `DynamicTexture` (MapColor + height shading, zoom 1/2/4/8), markers (units by type, HQs, known enemies, points
  A/B), forces, doctrine, 6 operation buttons, unit roles, plan editor (phases objective+trigger+value, 8 link types,
  retreat line), execute/cancel/save/load/delete. Payloads `PlannerStateS2CPayload`/`PlannerActionC2SPayload` (JSON,
  protocol "8"); `force/PlannerService` validates ownership and replies; `contract/PlanRegistry` saves plans per player.
- `force/Plans`: phases (HOLD, ATTACK_ZONE, ATTACK_GROUP, ESCORT, PATROL, RETREAT, RALLY) with triggers (TIME, CASUALTIES,
  ENEMY_SEEN, CONTACT); links RELIEF, COVER, REINFORCE, RENDEZVOUS, SCREEN, ESCORT, HOLD_UNTIL_RELIEVED, STAGGERED_RETREAT
  with capability checks ("faltan capacidades" in the planner + log); plan retreat rule (priority 120).
- Keys: G is a modifier - `ClientTickEvent.Pre` consumes hotbar clicks while G is down: G+N selects force N, G+sneak+N
  runs operation N; the plain G order is now sent on release. `docs/CONTROLES.md` updated.
- First-use APIs: `NativeImage(int,int,boolean)/setPixelRGBA (ABGR)/fillRect`, `DynamicTexture#upload`, `TextureManager#
  register/release`, `GuiGraphics#blit(ResourceLocation, x, y, u, v, w, h, texW, texH)`, `BlockState#getMapColor`,
  `MapColor.col/NONE`, `Level#hasChunk`, `ItemFrame(Level, BlockPos, Direction)/setItem/survives`, `Item#useOn
  (UseOnContext)`, `PlayerInteractEvent.EntityInteract#setCancellationResult`, `ByteBufCodecs.stringUtf8(int)`,
  `ClientTickEvent.Pre`, `Options#keyHotbarSlots`, block tag JSON `data/rallyguard/tags/block/`.
