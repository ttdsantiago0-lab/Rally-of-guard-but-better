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

## Current status (as of 2026-09-29, Phase 3G)

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
