# ARQUITECTURA_VETERANIA — decisiones firmes (David, 2026-10-08)

Este documento fija cómo se organiza la veteranía, el mando y las reglas de fuerza. Va **antes** que cualquier criterio de equilibrio. Si algo de otro documento lo contradice, manda este (salvo `DECISIONES_DAVID_2026-10-08.md`, que manda sobre todos).

## 1. Rango y maestría son cosas distintas (firme)
- **Rango = calidad de decisión**, de 0 a 5. No hay rango 6.
- Rangos y nombres: **0 Recluta · 1 Entrenado · 2 Experimentado · 3 Veterano · 4 Élite · 5 Leyenda** (nombres configurables en `lang`). Los **umbrales por número de rango del código no cambian**; solo cambian los nombres. Donde un documento o comentario diga "Veterano" pensando en el rango 2, ahora es "Experimentado" (rango 2). Revisa `DecisionLadder`, `TerrainSense` y los textos.
- **Después del rango 5 el XP no sube estadísticas.** Se reparte en cinco vías de progreso, cada una con rendimientos decrecientes y tope: **maestría de arma** (por tipo de arma), **supervivencia**, **mando**, **vínculo con la montura**, **historia** (registro de hazañas, base del valor de contratación).
- Nada escala sin límite: `legendBonus` (capacidad de mando que crece por cada duplicación de XP) deja de escalar la capacidad; el mando lo decide el Puntaje de liderazgo (§4). Por defecto +0 escalones; configurable.
- Un guardia de rango 5 con el mismo equipo tiene **la misma vida, daño y velocidad** que uno de rango 0. La diferencia está en lo que decide y en cómo ejecuta.

## 2. Qué cambia en cada rango (sin volver tontos a los bajos)
| Rango | Qué decide mejor |
|---|---|
| 0 Recluta | Cumple la orden: ataca el objetivo, sigue la formación con holgura, defiende lo básico. |
| 1 Entrenado | Mantiene la formación sin tropezar, retrocede con poca vida, usa escudo y munición con sentido. |
| 2 Experimentado | Aguanta la formación bajo presión, usa cobertura simple, evita fuego amigo evidente. |
| 3 Veterano | Prioriza objetivos, usa `TerrainSense`, concentra el fuego con sus vecinos, reacciona a amenazas a distancia. |
| 4 Élite | Elige el arma adecuada a cada amenaza, se reposiciona tras disparar, ve flancos, puede ser capitán. |
| 5 Leyenda | Usa el **núcleo de decisión avanzado** (§3) y sus preferencias de rasgo (§6). |
Los rangos bajos conservan comportamiento competente; la tabla dice qué se **añade**, no qué se quita. Reconcilia con la escalera `DecisionLadder` que ya existe; si un umbral actual contradice la tabla, deja el actual y anótalo en el informe.

## 3. Núcleo de decisión avanzado (solo rango 5, nativo de Rally, sin otros mods)
Diez pasos, en este orden, sobre datos que Rally ya calcula:
1. misión u orden vigente · 2. supervivencia propia · 3. protegido o formación (obligaciones) · 4. valoración de amenazas · 5. prioridad de objetivo · 6. idoneidad del arma para esa amenaza · 7. terreno (`TerrainSense`) · 8. formación · 9. elegir acción (reposicionar / atacar / retirarse / apoyar) · 10. retomar la misión.
Se evalúa **escalonado y con presupuesto** (no cada tick, solo con enemigo conocido); si el presupuesto de milisegundos por tick de `Perf` se excede, los guardias degradan a la escalera normal en vez de lagear. Cada decisión deja un código de motivo (§9).

## 4. Capitán automático y escalable: Puntaje de liderazgo
- `LeadershipScore` = suma ponderada (pesos en config) de: rango, rasgo de liderazgo, XP de mando propio, XP/experiencia de la unidad (supervivencia, cohesión) y la vía de mando de la maestría. Máximos acotados.
- Se **recalcula solo por eventos** (cambia la composición de la unidad, muere el líder, sube un rango), con caché. Nunca por tick.
- **Capitán** de una unidad = mayor puntaje entre sus miembros. **General** de una fuerza = mayor puntaje entre los **capitanes** de esa fuerza. Nada se nombra a mano; nada es una entidad nueva.
- Alcance de mando acotado (`maxDirectSubordinates`, configurable): si una unidad lo excede, se crea una subunidad cuyo capitán es el mejor puntaje de entre los que quedan. Así escala a cientos sin que un solo capitán mande a 200.

## 5. El liderazgo mejora la ejecución, no las estadísticas (firme)
La misma orden con otro líder se ejecuta con otra calidad. El puntaje del líder modula, con multiplicadores **acotados y configurables**: retraso de respuesta a una directiva, tolerancia de la formación antes de romperse, exceso de persecución, tiempo de reorganización tras bajas, y cuánto tarda en reasignarse un relevo. **Nunca** da daño, vida ni velocidad.

## 6. Rasgos = preferencias, no bonos
Un rasgo cambia **qué prefiere hacer** (pesos en la decisión), no cuánto daño hace. Ejemplos: Tirador prefiere altura, línea de vista y voladores; Duelista prefiere contraatacar cuerpo a cuerpo; Jinete prefiere mantener formación montado y cargar; Asalto prefiere abrir el encuentro; Tanque prefiere sostener la línea; Firme prefiere no romper la unidad; Explorador prefiere ver y señalar. Capstone en rango 5 = **la preferencia se aplica con mejor ejecución**, no más números.

## 7. Cómo se mide la excelencia (reemplaza criterios de 1 contra 1)
**No se usa la tasa de victoria en 1 contra 1 como objetivo**: empuja a inflar estadísticas. Se miden, con el mismo equipo y semillas distintas (≥10 repeticiones): **bajas, tiempo de supervivencia, cohesión de formación, persecución innecesaria (metros fuera de la correa), eficiencia de arma (impactos/disparos y daño por flecha), tiempo de ejecución de órdenes, fuego amigo**.
- Aceptación: en escenarios `ceiling-*`, la unidad de rango 5 mejora a la de rango 0 en **al menos 6 de las 7 métricas** y el índice compuesto sube **monótonamente de R0 a R5**; la misma unidad con capitán de rango alto supera a la del capitán de rango bajo en cohesión y bajas.
- **Alarma anti-inflado** (no objetivo): si en 1 contra 1 R5 vence a R0 en más del 85 % o los valores de vida/daño/velocidad difieren entre rangos, el informe lo marca como "posible inflado de estadísticas".
- **Nadie es inmortal**: en escenarios de desventaja abrumadora (≥3 a 1 con equipo igual) la unidad de rango 5 puede perder y debe hacerlo con retirada ordenada.

## 8. Reglas de fuerza (vínculos y disparadores)
Cada regla: `{id, ámbito (escuadrón | subgrupo | fuerza), condición, acción, prioridad, enfriamiento, histéresis (umbral de entrada y de salida), duración mínima}`.
- **Prioridad por regla**, no por tipo de regla; empate = orden de definición.
- **Histéresis**: una regla que se activa al 40 % de bajas no se revierte al 41 %; sale con su propio umbral de salida y respeta la duración mínima.
- Se evalúan **por eventos y como máximo 1 vez por segundo**, con directivas versionadas; no un mensaje por guardia.
- Los vínculos se expresan en **capacidades**: `MELEE_LINE`, `RANGED_SUPPORT`, `MOUNTED_CHARGE`, `SCREEN`, `ESCORT`, `HOLD_POSITION`, `RETREAT_COVER` (y futuras: `RECON`, `ENGINEER`, `LOGISTICS`, `ASSASSIN`). Si falta una capacidad que el plan necesita, **se informa** ("faltan capacidades: X") en el planner y en el log, nunca falla en silencio.

## 9. Telemetría de decisiones (solo depuración)
Enum `DecisionReason` (p. ej. `COVER_AVAILABLE`, `HIGH_GROUND`, `LOW_HEALTH_RETREAT`, `PROTECT_ALLY`, `AMMO_LOW`, `WEAPON_MISMATCH`, `TARGET_PRIORITY_AIR`, `FORMATION_HOLD`, `ORDER_OVERRIDE`, `LEADER_DIRECTIVE`, `UNIT_BROKEN`, `DEGRADED_BUDGET`). Un anillo de las últimas 8 decisiones por guardia, **solo si el modo depuración está activo**, y solo se escribe cuando el código **cambia** (no cada tick). Comando `/rally debug why <guardia>`; la simulación agrega conteos por código en `summary.md`.

## 10. Semántica por nivel (no se mezclan en el código)
Atacar / defender significan cosas distintas en cada nivel y se implementan por separado: **individual** (intención del guardia), **unidad** (maniobra con formación), **fuerza** (reparto de papeles entre unidades), **plan** (secuencia de fases del planner). Un nivel superior compila directivas para el inferior; nunca lo salta.

## 11. Rendimiento
Todo lo anterior: por eventos, escalonado, en caché y con presupuesto medido por `Perf`. Objetivo: 200 guardias sin que Rally pase de 0,5 ms/tick de media.

## 12. Interfaz
**No se añaden botones nuevos** para nada de esto. Rango, puntaje, historia y "por qué decidió" viven en la ficha existente del guardia y en el planner.
