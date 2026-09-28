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
- **This project deliberately avoids mixins.** Stick to public NeoForge
  APIs/events even when a mixin would be simpler.
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
  member of that squad), Order bar (Recall/Hold/Attack/Reform), Muster bar
  (LINE/COLUMN/V/CIRCLE + Retreat).
- `event/GuardRosterCleanupHandler.java` — cleans up
  `FormationRegistry`/roster state when a guard entity is removed.

## Current status (as of 2026-09-28)

- Latest git commits: `d99a91b` (Circle formation shape + squad
  quick-select, "Phase 3D.1") on top of `89a8328` (the main Phase 3D
  Extended pass: Formation engine + Muster/Attack/Reform/Retreat orders).
- **The jar currently deployed to the user's mods folder
  (`rallyguard-0.1.0-neoforge-skeleton.jar`, matches
  `build/libs/rallyguard-0.1.0-neoforge-skeleton.jar` built 2026-09-15) does
  NOT yet include the `d99a91b` Circle/squad-select changes.** The user
  needs to run `.\gradlew.bat build` (on their own Windows machine — this
  cannot be run from a Cowork device-bash sandbox: wrong OS shell, wrong
  JDK version, no populated Gradle cache there) and redeploy the new jar
  before Circle formations or squad quick-select are testable in-game.
- Muster/Attack/Reform/Retreat (Line/Column/V shapes only, pre-Circle) were
  previously reported as "not visible in-game" by the user — root cause was
  NOT a code bug: the built jar had never actually been copied into the
  mods folder, and the code had never been committed. Both are now fixed
  (see git log above and the mods-folder jar timestamp). **Not yet
  gameplay-confirmed by the user as of this writing.**
- Yaw/facing convention (`forward = (-sin(yaw), cos(yaw))` as (x,z),
  yaw 0°=South/+Z, increasing counterclockwise viewed from above) has been
  independently re-verified against two sources (minecraft.wiki) and is
  correct — don't "fix" it without a concretely reproduced facing bug.

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
