# Guard Villagers dependency jar goes here

This Phase 1 skeleton does **not** ship the Guard Villagers jar (Phase 0 research confirmed it's
CurseForge/Modrinth-distributed and this build environment's network sandbox cannot reach either
service's API/CDN to fetch it automatically - see PORTING_PLAN.md, section 3).

**Do not use `github.com/ShackleSailorTrek/guard-villagers`** - Phase 0 found that repository hosts
trojanized jars disguised as Guard Villagers (see PORTING_PLAN.md, section 0, "Security finding").

## What to place here

Expected filename: **`guardvillagers-2.3.2-1.21.1.jar`** (or a newer 2.4.x build for 1.21.1 - the
`versionRange="[2.3.0,)"` in `neoforge.mods.toml` accepts anything 2.3.0 or later).

If you download a different exact filename/version, either rename it to match the
`guardvillagers_jar` property in `gradle.properties`, or update that property to match the file
you actually have - both need to agree.

## Where to get the real jar

- Official CurseForge page: `curseforge.com/minecraft/mc-mods/guard-villagers` (author:
  `almightytallestred`) - pick a file built for **NeoForge, Minecraft 1.21.1**.
- Or Modrinth project `H1sntfo8` (`modrinth.com/mod/guard-villagers` or similar - the CurseForge
  page links this as the project's Modrinth mirror).

## Why the build doesn't fail without it (yet)

`build.gradle` only adds this jar to the classpath `if (guardVillagersJar.exists())`. Phase 1's
skeleton has no gameplay code that references Guard Villagers classes, so `./gradlew build`
should succeed with this folder empty. **Phase 2, stage C (ownership/hiring) will need the real
jar present here** - the `contract/GuardOwnership` rewrite (PORTING_PLAN.md section 4) compiles
against `tallestegg.guardvillagers.common.entities.Guard` directly.
