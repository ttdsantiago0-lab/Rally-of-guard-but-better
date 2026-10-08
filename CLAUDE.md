# Rally of the Guard — NeoForge 1.21.1 port

Private Minecraft mod. Not for public release. Single-player / small modpack
("veamos" Modrinth profile) use only. This file is what a fresh Claude Code
session reads automatically on startup — it exists so a new session (run by
the user directly in their own terminal, with their own Gradle/JDK 17+
toolchain) has the essential context immediately, without reading the whole
of `PORTING_PLAN.md` cold. `PORTING_PLAN.md` is the full, numbered running
design log — read it for the "why" behind any decision; this file is only
the "don't break these things" summary.

## Standing rules (do not violate)

- **Never use `github.com/ShackleSailorTrek/guard-villagers`** — it's a
  known trojanized fork. The real upstream is the official Guard Villagers
  mod; if in doubt, ask the user before adding any dependency on a
  "guard-villagers"-named repo.
- **Git commit messages** must end with:
  ```
  Co-Authored-By: Claude Sonnet 5 <noreply@anthropic.com>
  Claude-Session: https://claude.ai/code/session_016MsxyJgX8Ni6kFosnnWk4F
  ```
  (A genuinely new Claude Code session should use its own session's
  attribution footer per its own system prompt, not necessarily copy this
  literal line — but keep *some* clear co-authorship trailer, and never
  silently drop attribution.)
- **Never force-push, never skip git hooks (`--no-verify`), never use `-i`
  git flags** (interactive rebase/add — no interactive input is available).
- **Never claim a build succeeded, a bug is fixed, or a web/API claim is
  true without real, direct confirmation** — either the command's own
  output in front of you, or the user's own in-game/gameplay confirmation.
  Optimistic guessing has caused real confusion in this project before.
- **Before any first-time use of a NeoForge/Minecraft API**, verify against
  real source or two independent sources (javadoc, decompiled NeoForge
  source, or the NeoForge/Minecraft wiki) rather than assuming Forge 1.20.1
  knowledge carries over unchanged — many APIs were renamed or restructured
  in the Forge→NeoForge and 1.20.1→1.21.1 transition.
- **Prefer composition over mixins.** Use public NeoForge APIs/events first. Since
  2026-09-28 the user allows small, targeted mixins when no public API can do the job
  ("no tan rígido, confío en ti"): keep each one narrow, owned-guard-only where it
  touches Guard Villagers, and documented with why no API sufficed. Current list:
  `mixin/GuardHeroOfTheVillageMixin` (Hero of the Village no longer shields anyone
  from owned guards).
- Only delete files after explicit permission in-session; this repo has no
  standing blanket delete permission.

## Working economically (user budget is limited)

- Do NOT read `PORTING_PLAN.md` whole (2200+ lines): read its last section (`tail -n 120`) and `grep` for the rest.
- Current delivery plan and the prompt to execute: `docs/PLAN_ENTREGAS.md`, `docs/PROMPT_E1.md`, `docs/DECISIONES_TACTICAS.md`.
- Add at most ~40 lines per phase to `PORTING_PLAN.md`; update only "Current status" here. No unrequested new docs.
- `octetus/` holds third-party jars (MCA addons) on the user's machine only; it is git-ignored and absent in the cloud.

## What this project is

A port of a private Forge 1.20.1 mod to NeoForge 1.21.1. The mod adds a
"Rally of the Guard" command layer on top of the Guard Villagers mod:
players can select their owned guards and issue orders (Recall, Hold,
Muster, Attack, Reform, Retreat) through a custom screen
(`GuardCommandScreen`), including grouping guards into named squads and
mustering them into geometric formations (Line, Column, V, Circle) anchored
on a chosen point/facing.

Key packages under `src/main/java/net/hfstack/rallyguard/`:
- `formation/` — `Formation`, `FormationShape` (LINE/COLUMN/V/CIRCLE — pure
  geometry, computes per-member local offsets from anchor+yaw+spacing),
  `contract/FormationRegistry` (per-level state: last-mustered formation per
  owner, used by Reform).
- `order/` — `OrderExecutor` interface + concrete orders (`RecallOrder`,
  `HoldOrder`, `MusterOrder`, `AttackOrder`, `ReformOrder`, `RetreatOrder`),
  `OrderRegistry`.
- `network/` — C2S payloads (`IssueMusterOrderC2SPayload`,
  `IssueRetreatOrderC2SPayload`, etc.), `NetworkBootstrap`,
  `GuardCommandNetworking` (server-side resolution: `resolveOwnedGuards`,
  `musterOrder`, `retreatOrder`).
- `screen/GuardCommandScreen.java` — the in-game UI: guard roster ("the
  Ledger"), squad bar (with a "▾" quick-select button that cycles through
  known squad names AND replaces the current selection with every online
  member of that squad), Stance bar, Order bar (Recall/Hold Ground/Reform/
  Break Ranks), Target bar (target selector + Attack/Hold Fire/Release Fire),
  Muster bar (LINE/COLUMN/V/CIRCLE + Retreat).
- `order/` since 3E also holds `CommandTargeting` (shared targeting rules),
  `GuardPosts`, `TargetSpec`, `CommanderMarks` (the Ledger's sneak-click
  pointer); `contract/DirectiveRegistry` stores hold-fire + designated target.
- `event/GuardRosterCleanupHandler.java` — cleans up
  `FormationRegistry`/roster state when a guard entity is removed.

## Current status (as of 2026-10-08, E3A)

- E3A (PORTING_PLAN.md §42, spec `docs/PROMPT_E3A.md`; `docs/DECISIONES_DAVID_2026-10-08.md` and
  `docs/ARQUITECTURA_VETERANIA.md` override older docs): eleven commits, NOT compiled.
  - F: faction hostility mixin (`mixin/compat/GuardFactionMixin`), approach/volley reach, HQ parking, sim fixes, mount button;
  - V: veterancy v2 (ranks 0-5 renamed, paths past 5, threat XP + anti-abuse, story, `DecisionTrace`, rank-5 `AdvancedCore`);
  - A1-A4: `force/` (ForceRegistry, Leadership, UnitOrders precedence, ForceBrain + rules, Operations, Battle Planner, protocol 8);
  - A5 `order/FormationFollow`; A6 `order/HqRelax` (HQ zone, relaxed state); A7 Ledger by selection composition;
  - A8 `combat/MasteryEffects` + `config/rallyguard-weapon-mastery.json`; A9 sim v3 (terrains, forces, scale, lab).
- Next: David builds, then `/rally sim batch quick`, `v3` and `lab` in a flat world.

### Earlier (E2A, 2026-10-07)

- E2A (PORTING_PLAN.md §41, spec `docs/PROMPT_E2A.md`): six commits, NOT compiled.
  - 0: E1 fixes (water safety, dry-ammo fallback, `combat/ArrowFactory`, range/accuracy by rank, `order/HqLogistics`,
    optional MCA Crime mixin `rallyguard.compat.mixins.json` + descriptor-checking plugin, Guards tab, perf);
  - 1: `command/` (CommandStructure capacity + computed captain + subunits, SquadDirectives, SlotQueue, CaptainSuccession);
  - 2: `veterancy/` + `contract/VeterancyRegistry` (ranks 0-5, traits, totem delay, reckless);
  - 3: simulation v2 (`debug/sim/`, `/rally sim batch ...`, matrix `config/rallyguard-sim-scenarios.json`);
  - 4: `tactics/` (pure `DecisionLadder`, `TerrainSense`, squad picture, `Unstuck`, `Doctrine`);
  - 5: `order/OrderSemantics`, doctrines, group attack, Ledger bars grouped (protocol 7).
  Every rank read goes through `veterancy/Veterancy.rank`; every formation move through `command/SlotQueue`.
- Next: David builds, tests and runs `/rally sim batch quick` in a flat world; then E2b (logistics, dummy).

### Earlier (E1, 2026-10-05)

- E1 "foundation + ranged fire" (PORTING_PLAN.md §40; spec `docs/PROMPT_E1.md`, plan
  `docs/PLAN_ENTREGAS.md`): three commits - friendly-fire layers + MCA Mob Compat bridge
  + ranged fire (`combat/`: WeaponProfile, Ammo, Ballistics, FireMode, RangedFireGoal,
  volleys); war pack + backpacks (`PackRegistry`, `GuardPack`, `PackMenu`), HQ registry
  (`HqRegistry`, Ledger HQ tab), mount gallop + tag `rallyguard:guard_mounts`; scale tools
  (`/rally debug ...`, `Perf`, Ledger batches) + simulation harness (`/rally sim`,
  `docs/SIMULACIONES.md`). Config: `config/rallyguard-common.toml` (`RallyConfig`).
  NOT compiled. Next: E2 (leaders + veterancy + terrain), after E1 builds and is tested.
- Ammo rule: every shot is paid through `combat/Ammo` (1 item per 4 shots, one constant).

### Earlier (Phase 3H.1, 2026-10-04)

- Phase 3H.1 (PORTING_PLAN.md §39, ARQUITECTURA D14-D20):
  - hold fire absolute;
  - Attack/Defend exclusive;
  - squad HQ banners + `RetreatPlanner`;
  - `FormationJoin`;
  - commander-death handler;
  - `Engagement` melee cap;
  - sliced stance loop;
  - Ledger row cap (protocol 5);
  - finite SG ammo (`compat/GunAmmo`).
  - NOT compiled.
- Next phase (user decision): veterancy before classes - design proposal in
  `docs/VETERANIA.md`. The visual map of systems/ideas is the "Rally Map" artifact (source `docs/rally-map.html`, republish it when statuses change).

### Earlier (Phase 3H, 2026-09-30)

- Phase 3H (PORTING_PLAN.md §38, `docs/ARQUITECTURA.md` D7-D13):
  - command hotkeys G/H/J (`order/CommanderHotkeys`);
  - one server-side selection (`order/CommanderSelection`);
  - DEFEND (ward);
  - squad units "Squad/Unit";
  - Forget/Rename/hover details;
  - loss notices;
  - Scorched Guns gun AI (`compat/ScorchedGunsCompat`).
  - NOT compiled.
- Every idea from the user's 2026-09-30 review lives in `docs/BACKLOG.md`. Add new
  ideas there; never drop one.

### Earlier (Phase 3G)

- Phase 3G (PORTING_PLAN.md §37, `docs/ARQUITECTURA.md`): one movement-mode authority
  (`GuardModes`), Summon/Recall = come and HOLD, Follow order, scalable formations +
  Advance, squad management tab, SafeTick isolation, one guard per mount. NOT compiled.
  User's standing development philosophy (2026-09-29): keep advancing the roadmap and
  fix inconsistencies in the systems each increment touches; no giant refactors; check
  every feature against the compatibility matrix in `docs/ARQUITECTURA.md`.

### Earlier (Phase 3F)

- Phase 3F (PORTING_PLAN.md §36): mount driver for horses/ground mounts
  (`order/MountDriver`, loop in `GuardMountDisciplineHandler`), Kamikaze friend safety
  + softened (not cancelled) own-side explosion damage, automatic deploy to the mods
  folder. NOT compiled yet. Hippogryph and CBC gunner are ON HOLD until horses are
  tested in-game (user decision).

- Phase 3E.1 (PORTING_PLAN.md §35): Hero of the Village mixin, Kamikaze order, Ledger
  clarity pass (row-click selection, chips, tooltips, distance), research report
  `docs/INVESTIGACION_CBC_MONTURAS.md`. User reported `BUILD SUCCESSFUL` (2026-09-28).
- User design decisions (2026-09-28): the Rally scroll must keep ignoring posted guards
  (villages thousands of blocks apart - orders must never hit every guard at once);
  the mounts datapack stays outside git; CBC gunner and mount driver are researched,
  not yet approved for implementation.
- Phase 3E "Commander's Intent" (PORTING_PLAN.md §34) plus the audit in
  `docs/AUDITORIA.md` (Spanish): user reported `BUILD SUCCESSFUL` on 2026-09-28
  (not yet confirmed the checkout was on the Claude branch). Not gameplay-tested.
- Nothing from §29 onward has been gameplay-confirmed by the user yet.
- Yaw/facing convention (`forward = (-sin(yaw), cos(yaw))` as (x,z),
  yaw 0°=South/+Z, increasing counterclockwise viewed from above) has been
  independently re-verified against two sources (minecraft.wiki) and is
  correct - don't "fix" it without a concretely reproduced facing bug.

## Combat rules that are easy to break (read before touching targeting)

- NeoForge posts `LivingChangeTargetEvent` for EVERY `Mob#setTarget`, including
  `null`. `GuardStanceHandler#onChangeTarget` is the single combat gate for owned
  guards: it must **always allow null** (cancelling it leaves guards stuck on stale or
  dead targets - the Phase 3C bug fixed in 3E) and must let
  `CommandTargeting.issue(...)` (explicit commander orders) through.
- Guard Villagers' own `Guard#setTarget` silently refuses blacklisted mobs, team
  allies, the owner and the owner's pets (and Hero of the Village - except for owned
  guards, via `GuardHeroOfTheVillageMixin`). Filter candidates with
  `CommandTargeting.wouldAccept` before picking "the nearest" anything.
- A guard's movement MODE (FREE / FOLLOW / POST) IS Guard Villagers' native
  `Following`/`Patrolling` flags. Change it ONLY through `order/GuardModes`
  (`free/follow/post/postHere`) - never call `setFollowing/setPatrolling` directly.
  Stance, directives, formation slot and squad are orthogonal and must never change
  the mode. A "post" is GV's patrol position (`GuardPosts`); don't add parallel
  movement AI.
- Any new periodic loop over guards must run each guard's step through
  `event/SafeTick.run(...)` so another mod's entity can't crash the level tick.
- Posting a guard for an order goes through `GuardModes` (`post`, `postHere`,
  `postAndMove`), which also ends a DEFEND ward; only the ward loop uses
  `postKeepingWard`.
- Roles come from equipment, never new buttons. Other-mod integrations live in
  `compat/`, load by reflection only when that mod is present, and fail soft.
  `Mob.goalSelector` is public on NeoForge (its access transformer), so a compat can add
  real goals.
- HOLD FIRE is absolute: only H / Release Fire lift it; Attack under hold fire only
  designates. Attack and Defend are mutually exclusive directives.
- Melee engagement is capped per target (`order/Engagement`); any new code that sets a
  melee guard's target outside `CommandTargeting.issue` goes through the gate, which
  applies the cap - explicit `issue` calls must check `Engagement.tryEngage` first.
- Payload lists must stay within their codec caps (Ledger rows: `GuardListS2CPayload.MAX_ROWS`).
- Hotkeys never act on "every guard": the scope is the selection, or else the guards
  following within 64 blocks (villages thousands of blocks apart must stay untouched).
- Guard Villagers' real 1.21.1 source is readable at
  `raw.githubusercontent.com/seymourimadeit/guardvillagers/1.21.1/src/main/java/tallestegg/guardvillagers/common/entities/Guard.java`
  (all goals are inner classes of that file) - check it before assuming GV behavior.

## Deploying a new build (automatic since 2026-09-28)

1. User runs `.\gradlew.bat build` on their own machine.
2. `gradle/deploy.gradle` (applied from `build.gradle`) then runs `deployToMods`
   automatically: removes any other `rallyguard-*.jar` from `rally_mods_dir`
   (`gradle.properties`, the Modrinth "veamos" profile's `mods/`), copies the fresh
   jar there, and verifies it with SHA-256 - failing loudly if the copy can't happen
   (usually: Minecraft still running and locking the old jar). `.\gradlew.bat
   deployToMods` re-runs just that step. Ask the user for the printed
   `[rallyguard] SHA-256 verified` line when confirming a deploy.
3. `git add` + `git commit` with the attribution footer above — a build
   that isn't committed is easy to lose track of, as happened once already
   in this project's history.

## Full history / design rationale

See `PORTING_PLAN.md` in the repo root — a chronological, numbered log
(`## N. Title (date)`) of every phase, including the reasoning behind
design choices (e.g. why Reform/Attack are registered zero-parameter orders
while Muster/Retreat are constructor-parameterized; why there's no separate
AdvanceOrder; deliberately-deferred features like Battle Planner, Kamikaze,
Extended Mounts). Read the relevant section before changing anything in an
area you haven't touched before.
