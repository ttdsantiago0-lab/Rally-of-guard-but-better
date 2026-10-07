# Datos de APIs de otros mods (leídos con `javap` por Cowork, 2026-10-07)

Claude Code en la nube **no tiene** los jar de otros mods. Esta hoja sustituye al `javap`. Si falta algo, pregúntalo como "pregunta abierta" en el informe; David o Cowork lo leen.

## B3 · Arco de dragón (Ice and Fire 2.0-beta.17)
- `com.iafenvoy.iceandfire.item.tool.DragonBowItem extends BowItem`; flechas `DragonArrowItem extends ArrowItem` → entidad `DragonArrowEntity extends AbstractArrow`.
- Lo que hace al soltar (`releaseUsing`, solo con `Player`): crea la flecha con `ArrowItem.createArrow(level, ammoStack, shooter, bowStack)`; `shootFromRotation(shooter, xRot, yRot, 0, power*3.0f, 1.0f)`; **`setCritArrow(true)` siempre**; daño base `getBaseDamage()` + (nivel de Potencia × 0,5 + 0,5); Fuego → `igniteForSeconds(100)`; Retroceso (Punch) por encantamiento; desgaste del arco `hurtAndBreak`.
- **Por qué el guardia pega menos:** el código de disparo genérico del guardia no aplica crítico siempre ni los encantamientos del arma (Potencia, Fuego, Retroceso). Solución: tras `createArrow` con la munición real, aplicar sobre la flecha los efectos del `ItemStack` del arma: `setCritArrow(true)` si el item es de tipo "siempre crítico" (o si la carga es completa, como el arco vanilla), `setBaseDamage(base + 0.5*nivelPotencia + 0.5)`, `igniteForSeconds` con Fuego, Retroceso con `setKnockback`/equivalente de 1.21.1. Implementación general: leer encantamientos con `EnchantmentHelper.getItemEnchantmentLevel(holder, stack)`; no codificar nada específico de Ice and Fire. La lista de ítems "siempre crítico" va por configuración/tag.

## B7 · Diálogo de crimen (MCA Crime 0.7.5)
- Origen: `dev.otectus.mcacrime.enforcement.GuardChallengeService.challenge(ServerLevel, LivingEntity guard, ServerPlayer)` (público, estático). Valida con un método privado `validGuard`. Los textos "Stop where you stand…", "You are holding someone unlawfully" (`mcacrime.challenge.reason.captive`, ¡se dispara si el jugador lleva a un cautivo!) y "You refused a lawful order" son de ese servicio.
- Config de MCA Crime (servidor): `enableGuardChallenge` (activa el desafío de guardias), `guardChallengeRadius`, `guardChallengeWindowTicks`. Dice que un guardia "puenteado" de Guard Villagers usa un MCA oculto (de Mob Compat), por eso nuestros guardias cuentan como autoridad.
- Mob Compat 0.2.1: `GuardBridge.isBridgedGuard(Entity)` (estático, público) decide si una entidad está puenteada. Config de Mob Compat: `guardMcaInteraction`, `recruitMcaVillagersAsGuards`, `treatGuardsAsMcaGuards`, `guardShowNameTag`.
- **Opciones de solución, de menos a más invasiva:** (a) que Rally marque sus guardias para que Mob Compat no los puentee (revisar `ClassificationProvider`/`MobCompatibilityAPI` y la config `treatGuardsAsMcaGuards`); (b) un mixin pequeño y apuntado que cancele `GuardChallengeService.challenge` cuando `guard` es un guardia de Rally (con dueño); (c) documentar para David el ajuste de config (`enableGuardChallenge=false`) como último recurso. Elige (a) o (b) y registra en el informe cuál y por qué.
- Importante: en la prueba el jugador sostenía a un cautivo/prisionero; ese motivo explica el diálogo "holding someone unlawfully". Considera marcar a los cautivos de Rally para que MCA Crime no los trate como secuestro.

## B9 · Menú de MCA sobre un guardia propio
- `GuardInteractEvent` (API de Mob Compat) ya se usa por reflexión en `McaMobCompat`. El menú MCA de un guardia propio solo se abre si no cancelamos el evento y si `guardMcaInteraction=true` en la config de Mob Compat. Implementa la lógica de nuestro lado; documenta que la clave de config debe estar en `true`.

## Reputación (MCA Reputation 0.6.1)
- `McaReputationApi`; `CoreIncidentExemptions.register(ResourceLocation id, Set<CoreIncidentKind>, Query)` permite eximir incidentes (la `Query` devuelve `Decision.PASS` o `EXEMPT`). Útil para que daño entre guardias propios o a cautivos de Rally no genere incidentes de reputación.
