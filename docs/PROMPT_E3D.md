# PROMPT_E3D para Claude Code — Mando profundo: lectura de la batalla, estilos, coordinación y resoluciones (2026-10-09)

**Orden de ejecución:** E3D va **después** de E3C (que va después de E3B y del Bloque R). Si algo de lo anterior no está terminado, **no lo rehagas**: termina lo pendiente de R primero. **Motivo (David):** "los roles de mando son los que más aportan al mod". Este bloque es el corazón de la jerarquía.

**Cupo:** aprovecha **todo el cupo** de forma útil; no te detengas al acabar un bloque. **Correcciones ≤ 15 % del esfuerzo; el resto, avance.** Todo aditivo y configurable (`command.styles.enabled`, defecto `true`; si se apaga, el mando se comporta como en E3C: umbrales de `ForceRules`). **Complejidad bienvenida (David: "entre más complejo mejor mientras no rompa el código")**: pero cada pieza con caducidad, histéresis y límites, sin estados colgados.

**Reglas de siempre:** lee solo lo necesario; sin web; los jar de otros mods NO están en tu sesión (`docs/API_MODS.md`; no inventes ids); anota dudas en el informe; no puedes compilar → parse + chequeo de tipos por bloque; `PORTING_PLAN.md` ≤ 40 líneas por bloque (§45); un commit y `git push` por bloque; sin preguntas a David; informe final ≤ 40 líneas con **qué NO quedó** y **qué probar**; trailers como indica `CLAUDE.md`. **Sin botones nuevos fuera del Battle Planner.**

## Bloque 0 · Preparación (≤ 5 %)
1. `git pull` de `claude/dreamy-ride-iy0g3q`; el historial contiene `b14df21`, E3B y E3C. **Lista de compilación** (aplícala al diff entero al cerrar cada bloque): (a) lambda captura solo `final`/efectivamente final; (b) parámetros de lambda y variables de `for` no repiten nombres de locales del alcance (`f`, `e`, `v`, `i`, `r`, `s`); (c) imports; (d) `switch` exhaustivo; (e) genéricos inferidos; (f) métodos privados desde otra clase; (g) `record` con `x()`; (h) `lang` en los 4 idiomas (`en_us`, `es_es`, `es_mx`, `pt_br`); (i) toda API nueva de NeoForge/Minecraft en `PORTING_PLAN.md` con la firma asumida.
2. Lee en orden: `docs/ESTILOS_DE_MANDO.md` (**la especificación**), `docs/DECISIONES_DAVID_2026-10-09.md` (D4, D9), `docs/ARQUITECTURA_VETERANIA.md`, `docs/MORAL_Y_COHESION.md`, `docs/BATTLE_PLANNER_V2.md`, `docs/VISION_ARQUITECTURA.md` (capas de mando); y solo el código que toques: `force/ForceRules`, `force/Leadership`, `force/Operations`, `force/UnitOrders`, `force/Plans`, `force/PlannerService`, `force/SituationReports`, `tactics/Tactics` (SquadPicture), `tactics/Situation`, `tactics/DecisionReason`, `contract/Factions`, `contract/GuardOwnership`, `screen/HireGuardMenu`, `veterancy/Trait`.
3. Verifica con `grep` (no lo supongas) cómo `ForceRules` decide hoy la retirada y la persecución: esos son los **umbrales fijos que este prompt reemplaza** por el `outlook` cuando `command.styles.enabled` esté activo.

## Bloque O · `BattleOutlook`: la lectura de la batalla (prioridad alta)
- **O1:** `outlook ∈ [−1, +1]` por líder (capitán y general) a 1 Hz, escalonado, solo con enemigo conocido, cacheado. Entradas (ya existentes): relación de fuerzas (ponderada por rango/arma/formación), **impulso** (bajas causadas vs sufridas ~20 s), progreso del objetivo de la fase, cohesión y moral propias y enemigas (`M`), terreno, refuerzos conocidos. Pesos en `config/rallyguard-command-styles.json`.
- **O2:** la **calidad de la lectura** depende del puntaje de liderazgo (ruido y retraso acotados, nunca estadísticas). El **estilo** infla ciertos pesos (§3.1 del documento).
- **O3:** el planner muestra **una barra de "lectura de la batalla"** del capitán en la tarjeta de la unidad (David lo pidió; es una tarjeta del planner, no un botón) y, en `/rally debug why`, el desglose de sus entradas.
- **Aceptación:** sobrecoste ≤ 0,03 ms/tick con 200 guardias (solo líderes); sin NPE al morir el líder.

## Bloque E · Estilos (ejes y catálogo)
- **E1:** seis ejes −1..+1 (**riesgo, disciplina, mando, ritmo, fuerzas, honor**), `config/rallyguard-command-styles.json` (se escribe con defectos si falta): catálogo de estilos con nombre (`Prudente`, `Audaz`, `Metódico`, `Oportunista`, `Estratega`, `Martillo`, `Escudero`, …) como **combinaciones de ejes**, más pesos de `outlook`. Añadir un estilo = una fila.
- **E2:** derivación **sin botón** (§7 del documento): semilla por rasgos, desplazada por historial y XP de mando, recalculada por eventos con tope de variación por batalla. Se guarda en el registro de veteranía del guardia (versión de formato; migración de registros viejos = ejes en 0).
- **E3:** el estilo se ve en la tarjeta del planner ("Capitán Marta · Prudente/Estratega") y cambia con la sucesión (aviso en el planner).

## Bloque D · Decisión: seguir, ajustar o romper contacto
- **D1:** `seguir = outlook + valorObjetivo + empujeMoral − aversiónAlRiesgo(estilo, intención) > umbralSalida`, con **histéresis** y duración mínima (la maquinaria de `ForceRules`). `valorObjetivo` sale de la **intención de la fase** del Battle Planner (mantener, destruir, ganar tiempo, romper línea); las instrucciones del general ("resistir a toda costa"/"evitar bajas") la desplazan; el estilo del capitán decide cuánto las interpreta literalmente.
- **D2:** **sin porcentajes fijos de bajas**: si el `outlook` es alto, hasta el Prudente continúa con 50 % de bajas; con emboscada, hasta el Audaz rompe contacto.
- **D3:** ante amenazas clasificadas (E3C, T1) el estilo decide cómo se resuelven (§4 del documento).
- **Aceptación:** `batalla-ganada-con-bajas-altas` (los 5 estilos continúan), `emboscada-sorpresa` (ninguno se queda a morir), 0 oscilaciones seguir↔retirar en 60 s.

## Bloque C · Coordinación y estrategia (donde más se nota)
Implementa la tabla del §4 del documento como **parámetros de ejecución acotados**, no como código aparte por estilo: **reserva** (cuánta y para qué), **inicio de fase** (señal de supresión, condición escrita, solape), **delegación vs sincronía** (cuánto detalla cada orden y si espera confirmación), **uso de capacidades** (preferencias entre `HOLD_POSITION`, `SCREEN`, `MOUNTED_CHARGE`, `MELEE_LINE`, `LONG_RANGE_COUNTER`…), **persecución** (correa), **retirada por escalones con retaguardia** o **contraataque de salida**, **reacción a la caída del capitán**. Todo pasa por las directivas versionadas de `UnitOrders` y las reglas de fuerza; nada salta la puerta de combate.
- **Aceptación:** escenario `style-compare` (mismo caso, 5 estilos): los resultados **difieren** en orden de fases, uso de reserva y capacidades, y **ningún estilo es estrictamente mejor en todos los escenarios**.

## Bloque R2 · Resoluciones de batalla y rendiciones (núcleo, para guardias de facciones enemigas)
Sigue `docs/ESTILOS_DE_MANDO.md` §10–11.
- **R1:** `BattleResolution { CONTINUE, ADJUST, DISENGAGE, FIGHT_TO_DEATH, SURRENDER, FEIGNED_SURRENDER, SCORCHED_EARTH }`. Selección por estilo + honor + **movilidad** de la fuerza (`ligera|normal|asentada`; una asentada no puede `DISENGAGE`) + moral + facción. Toda resolución tiene **caducidad**.
- **R2:** **`SURRENDER` real:** estado `SURRENDERED` para la unidad (no ataca, no huye). El jugador puede **contratar** a sus miembros (reutiliza la pantalla de contratación `HireGuardMenu`, precio por valoración), **traspasarlos** a su bando (`GuardOwnership`; empiezan en el rango que tuvieran y con su historia) o dejarlos ir. Conservan lo puesto; el botín restante va al jugador igual que el saqueo de E3C. Se rompe si el jugador ataca o se aleja (caducidad).
- **R3:** **`FEIGNED_SURRENDER`:** igual a la rendición real pero con bandera `trap`; un líder o explorador del jugador con visión/rango suficiente puede marcar `TRAP_SUSPECTED` y avisar en la tarjeta ("rendición dudosa"); si nadie lo detecta y el jugador/guardias entran en el radio de la trampa, se detona.
- **R4:** **`SCORCHED_EARTH`:** destruye contenido de depósitos y provoca una explosión que daña entidades **con daño a bloques APAGADO por defecto** (`resolution.scorched.blockDamage=false`; David lo activa si quiere). **Nunca** rompas bloques con la config por defecto.
- **R5:** `FIGHT_TO_DEATH`: sin retirada ni rendición; solo para estilos temerarios/fanáticos y sin escapatoria.
- **No implementes el addon de guerrillas**: deja el catálogo de estilos de guerrilla (Acosador, Emboscador, Saqueador, Fantasma…) como **filas de JSON de ejemplo desactivadas** (`"enabled": false`) y la interfaz de movilidad lista.
- **Aceptación:** `surrender-hire`, `feigned-surrender`, `fight-to-death`, `scorched-earth` (con `blockDamage=false` no cambia un solo bloque), toda resolución caduca, objetos conservados.

## Bloque S · Simulación v6 (en cada bloque; cierre aquí)
Añade `style-compare`, `batalla-ganada-con-bajas-altas`, `emboscada-sorpresa`, `surrender-hire`, `feigned-surrender`, `fight-to-death`, `scorched-earth`, y el preset `v6`; `summary.md` con comparación con el lote anterior y "Qué aprendimos". Comprobaciones globales: igualdad de atributos entre rangos; estilo/outlook no alteran vida/daño/velocidad; sobrecoste ≤ 0,03 ms/tick con 200 guardias; 0 bucles de estado.

## Si sobra cupo
Revisión de compilación del diff entero; segunda lectura de O/D/R2 buscando estados sin limpiar por UUID (`forget`/`clearAll`); `lang` en los 4 idiomas; docs: `docs/CONTROLES.md`, `docs/MATRIZ_FEATURES.md`, `CLAUDE.md` (Current status ≤ 12 líneas), `PORTING_PLAN.md` (§45).

## Corte si el cupo se agota (sacrificable → intocable)
R5 → R3 → R4 → ejes `honor` y `fuerzas` → E3 (aviso de sucesión) → C (detalles finos) → R2. **Intocables:** Bloque 0, O1, O2, E1, E2, D1, D2, R1, R2 (rendición real y contratación), S.

**Entrega:** un commit y push por bloque; informe ≤ 40 líneas: qué NO quedó, qué probar (comandos `/rally sim batch ...`), parámetros que David debería ajustar, preguntas abiertas.
