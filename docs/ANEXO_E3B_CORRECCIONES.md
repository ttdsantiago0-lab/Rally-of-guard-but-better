# ANEXO_E3B_CORRECCIONES — Bloque R (se añade a `docs/PROMPT_E3B.md`, 2026-10-09)

**Léelo si ya estás ejecutando E3B.** Una revisión del código de E3A encontró fallos de comportamiento reales, uno de ellos crítico y confirmado por lectura directa. Detalle completo, líneas y escenarios en **`docs/REVISION_E3A.md`**. **Haz el Bloque R ahora, antes de seguir con el bloque de E3B que estés tocando** (si estás a mitad de un bloque, termínalo con su commit y sigue con R). Es la única excepción a "correcciones ≤ 15 %": aquí pueden llegar al **25 %** del esfuerzo total, porque sin R los bloques G, M y P se apoyan en una táctica que hoy está apagada.

**Reglas del bloque:** comprueba cada fallo en el código antes de cambiarlo (los marcados ✔ ya están verificados; los demás, no); un commit por grupo; cada arreglo con su escenario de simulación o autoprueba cuando sea posible; **no cambies comportamiento que no esté en la lista**; si algo de la lista resulta ser falso, anótalo en el informe.

## R1 · Imagen de escuadrón (A1 ✔) + lo que despierta (A2–A6) — **un solo grupo de commits, en este orden**
1. `tactics/Tactics.java`: `SquadPicture.tick` inicial = `-1000L` (o centinela explícito) y revisa **todos** los usos de `now - p.tick` (`picture()`, `odds()`, `enemyCentre()` y similares).
2. En el mismo grupo, **antes de dar el grupo por cerrado**, corrige lo que el arreglo activa: A2 (exclusión mutua prioridad/foco + enfriamiento de recambio de objetivo ≥ 3 s), A3 (aliados cercanos reales y enemigos relevantes; exentar `ALL_OUT`), A4 (`TAKE_COVER`↔`RETREAT`), A5 (histéresis de reentrada de la retirada), A6 (enfriamiento de `SWAP_WEAPON`).
3. Escenarios: `picture-live` (en `cover-archer` y `height-archer` deben aparecer decisiones `TAKE_COVER`/`TAKE_POSITION` y `TerrainSense` consultado); `rank5-core-live` (con rango 5 y un enemigo, `AdvancedCore` decide ≥ 1 vez, y no huye en igualdad); `no-pingpong` (ningún guardia cambia de peldaño más de 2 veces en 10 s en una pelea estable). Métrica nueva en `summary.md`: **cambios de peldaño por guardia y minuto** (alarma si > 8).
4. Con la imagen viva, comprueba `Tactics.odds` con null correcto (el `UnitFights.closed` no debe conceder "victoria" sin enemigos conocidos).

## R2 · Atascado falso (A7 ✔)
`tactics/Unstuck.java`: atascado = hay ruta pendiente y no avanza; **nunca** con mantener fuego, correa, `STILL`, puesto o fuego en curso. Prueba `still-archer-not-stuck`: un arquero en `STILL` con objetivo a 12 bloques no recibe `UNSTUCK` en 30 s.

## R3 · Informes y bajas (A8 ✔, A9 ✔)
`force/SituationReports`: guardar siempre `centre`/`enemyCentre`/`tick` frescos (publicar eventos solo por cambios significativos, como ahora); contar bajas por **muerte** (evento) y reducir `total` cuando la pertenencia cambia sin muerte. Prueba `split-no-casualties` (dividir una unidad de 10 en 5+5 → 0 % de bajas, sin retirada de fuerza) y `report-fresh-centre` (una unidad que avanza 80 bloques sin cambiar de vida publica el centro nuevo).

## R4 · Órdenes y mando (A10 ✔, A11 ✔, B3–B7)
- `force/UnitOrders`: versión por `(unitKey, guardia)`; ignorar órdenes de más de ~60 s para un guardia que no aplicó ninguna en esa unidad.
- `command/CaptainSuccession`: clave por dueño + ruta de unidad; el relevo retrasado debe comprobar que la directiva vigente del guardia es la que originó el relevo antes de empujarla.
- `force/ForceBrain.forget(forceKey)` en disolver, renombrar, cancelar plan; borrar estados de `ForceRules` por prefijo; `/rallyforce dissolve` limpia `OPS`, `ACTIVE`, `STATES`, `LAST_ACTION`, `ORDERS` y los informes de esa fuerza.
- La retirada de fuerza recuerda y **restaura** la operación o fase anterior, y una orden explícita del jugador la cancela. `HOLD_UNTIL_RELIEVED` con tiempo límite y limpieza si el relevo desaparece; los retenidos quedan fuera de `ORDERLY_RETREAT`.
- Disparadores de plan: `CONTACT` ≤ 0 = "cualquier contacto"; trigger `NONE` con valor por defecto; `ENEMY_SEEN` ignora avistamientos anteriores al inicio del plan; `ATTACK_ZONE` se detiene en la zona; `ADVANCE_BY_TURNS`, el que "mantiene", se queda en su sitio. `FLANK`: marcar `issued` solo si se emitió.
- Prueba `retreat-restores-op` y `plan-cancel-clears-state` (cancelar y ejecutar otro plan con `retreatAt=0.5` y bajas 25 % **no** dispara retirada).

## R5 · HQ y seguimiento (B1 ✔, B2 ✔)
`order/HqLogistics`: el temporizador de olvido de monturas aparcadas cuenta desde que `leaving` pasa a verdadero (y solo expira si el caballo no existe o está lejos). `order/FormationFollow.order`/`postForFollow`: llamar también a `HqRelax.leave` y `SlotQueue.cancel` (y desde `GuardModes.normalize` cuando cambian los flags nativos de GV fuera de Rally). Escenarios: `hq-remount-after-rest` (aparcado 3 min → sale → remonta) y `relaxed-then-follow` (un Relajado que recibe Seguir no vuelve al HQ).

## R6 · Seguridad y red (B8, B9)
Servidor: coordenadas **finitas** y a ≤ 512 bloques del jugador (y dentro de la frontera); ignorar el `Destination` del cliente donde baste `player.position()`; no cargar chunks por posiciones del cliente; topes (≤ 48 roles por plan, claves ≤ 64, nombres de escuadrón ≤ 32 y rutas ≤ 65, HQs por jugador acotados); comprobar zona de HQ/mesa en **cada** acción del planner; limitar paquetes por segundo y por jugador; deduplicar ids de guardias (`LinkedHashSet`). Libro: `openFromPayload` solo reemplaza si el jugador tiene el Libro abierto o hay una petición pendiente; `ledgerPageSize` por defecto 500 y límite por bytes; "Olvidar" solo si el guardia está confirmado ausente en **todas** las dimensiones; `client/CommanderKeys`: limpiar `orderPending` sin enviar si hay pantalla abierta o se pierde el foco, y reiniciar al desconectar.

## R7 · XP y veteranía (B10, decisión C1/C2)
`VeterancyEvents`: `markCombat` solo para golpes válidos (no inmóvil, dentro de la ventana de repetición); `XP_LONG_FIGHT`, `XP_NEAR_DEATH_SURVIVED` y XP de mando pasan por el tope por minuto; el fuego aliado o de otro jugador **no** da XP. **Decisiones C1 y C2 de `REVISION_E3A.md`**: déjalas **como están** (alcance/puntería por rango y maestría desde rango 0) pero con **tope configurable** (`ranged.rangePerRank`, `ranged.spreadPerRank`) y regístralas en la alarma anti-inflado de `summary.md` (David decidirá con las cifras).

## R8 · Rendimiento (B11, B12)
`SquadPicture` por escuadrón **o por celda de 32×32 bloques** para guardias sin escuadrón (sin una consulta 48³ por guardia y segundo); que `ADVANCED_BUDGET_MS` mida de verdad el coste total del paso del rango 5 (imagen + terreno + decisión) y degrade; `ForceBrain.tick` solo en la dimensión que corresponde; `UnitOrders.apply` con lista de miembros cacheada por `(clave, versión)`; `SlotQueue.enqueueFollow` no reordena lo ya encolado y omite guardias cuyo hueco se movió menos del umbral. Medir con `/rally debug perf` a 200 guardias y reportar ms/tick.

## R9 · Endurecimiento (B13, B14, resto)
`GuardFactionMixin`: limpiar el `ThreadLocal` en `RETURN` y exigir exactamente un `List.contains` por método (el plugin ya lo comprueba; el mixin debe usar `require = 1` dentro de esa condición). `GuardHeroOfTheVillageMixin` → `require = 0` o vía el mismo plugin. Persistir operaciones y fases de plan activas (o documentar que se pierden); `ForceRegistry.changed()` tras `RELIEF`; `PlanRegistry` con nombres insensibles a mayúsculas en `put`/`remove`; `Plans.warnings` y `ForceBrain.step` sin spam de log (una vez por cambio / por minuto); `/rallyforce doctrine <nombre inválido>` se rechaza.

**Informe:** en el informe final añade una sección "R" de ≤ 10 líneas: qué se confirmó, qué resultó falso, qué quedó sin hacer.
