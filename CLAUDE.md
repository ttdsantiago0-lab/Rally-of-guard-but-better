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

## Current status (as of 2026-09-28, Phase 3E.1)

- Phase 3E.1 (PORTING_PLAN.md §35): Hero of the Village mixin, Kamikaze order, Ledger
  clarity pass (row-click selection, chips, tooltips, distance), research report
  `docs/INVESTIGACION_CBC_MONTURAS.md`. NOT compiled yet.
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
- A guard's "post" IS Guard Villagers' patrol position (`GuardPosts`). Formations,
  Hold Ground and Retreat work by setting it; don't add parallel movement AI.
- Guard Villagers' real 1.21.1 source is readable at
  `raw.githubusercontent.com/seymourimadeit/guardvillagers/1.21.1/src/main/java/tallestegg/guardvillagers/common/entities/Guard.java`
  (all goals are inner classes of that file) - check it before assuming GV behavior.

## Deploying a new build (until the user sets up their own automation)

1. User runs `.\gradlew.bat build` on their own machine.
2. Copy `build/libs/rallyguard-0.1.0-neoforge-skeleton.jar` over the stale
   copy in the mods folder (Modrinth "veamos" profile's `mods/` dir).
3. Verify with an md5/sha256 checksum that the copied jar matches the
   freshly-built one (don't just trust the copy succeeded).
4. `git add` + `git commit` with the attribution footer above — a build
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
