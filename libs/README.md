# Guard Villagers dependency jar goes here

This Phase 1 skeleton does **not** ship the Guard Villagers jar (Phase 0 research confirmed it's
CurseForge/Modrinth-distributed and this build environment's network sandbox cannot reach either
service's API/CDN to fetch it automatically - see PORTING_PLAN.md, section 3).

**Do not use `github.com/ShackleSailorTrek/guard-villagers`** - Phase 0 found that repository hosts
trojanized jars disguised as Guard Villagers (see PORTING_PLAN.md, section 0, "Security finding").

## What to place here

Expected filename: **`guardvillagers-2.4.11-1.21.1.jar`**.

This is the current latest NeoForge build for Minecraft 1.21.1 on the official CurseForge page,
re-confirmed directly (not from memory) during the Phase 2 preflight on 2026-08-27: Release
status, uploaded Aug 23, 2026, 276.7 KB. It's also the closest published release to the exact
`seymourimadeit/guardvillagers` `1.21.1`-branch source (`mod_version=2.4.12` at that branch's
HEAD) that PORTING_PLAN.md section 4's ownership-API verification was checked against - so this
is the build most likely to match that verification exactly. It will still be independently
re-verified (decompiled/`javap`-inspected) against the actual jar once it's placed here, per
PORTING_PLAN.md section 4/12 - the source-level verification is not assumed to carry over
byte-for-byte without checking.

The `versionRange="[2.3.0,)"` in `neoforge.mods.toml` is intentionally loose and will also accept
an older 2.3.x/2.4.x build or a slightly newer patch release if `2.4.11` isn't available when you
read this - but name the file to match whatever you actually place, and update the
`guardvillagers_jar` property in `gradle.properties` to match if it's not `2.4.11`. Both need to
agree.

## Where to get the real jar

- Official CurseForge page: `curseforge.com/minecraft/mc-mods/guard-villagers` (author:
  `almightytallestred`) - the "Files" tab, filtered to Minecraft 1.21.1 and NeoForge, top entry.
- Or Modrinth project `H1sntfo8` (`modrinth.com/mod/guard-villagers` or similar - the CurseForge
  page links this as the project's Modrinth mirror).

## Why the build doesn't fail without it (yet)

`build.gradle` only adds this jar to the classpath `if (guardVillagersJar.exists())`. Phase 1's
skeleton has no gameplay code that references Guard Villagers classes, so `./gradlew build`
should succeed with this folder empty. **Phase 2, stage C (ownership/hiring) will need the real
jar present here** - the `contract/GuardOwnership` rewrite (PORTING_PLAN.md section 4) compiles
against `tallestegg.guardvillagers.common.entities.Guard` directly.
