# PROMPT_E2A para Claude Code (v2, 2026-10-07)

**Cupo:** David autoriza gastar **todo el que haga falta** (incluso agotar su sesión). Prioriza **hacer mucho y bien**, no recortar. Lo que no cabe se recorta por la línea de corte (`docs/PLAN_ENTREGAS.md` §2) y se dice en el informe.

Reglas:
1. Lee `docs/PLAN_ENTREGAS.md`, `docs/DECISIONES_TACTICAS.md`, `docs/VETERANIA.md` §0–4, `docs/FEEDBACK_E1.md` (secciones A, B, C), `CLAUDE.md` y solo los archivos que vayas a modificar. No re-explores el repo. No leas `PORTING_PLAN.md` entero (usa `tail -n 120` y `grep`).
2. Sin investigación web. Para APIs de otros mods usa `javap` sobre los jar de `octetus/` (git-ignorado) y los de `veamos--mods`. Si falta un dato, anótalo como pregunta; no lo inventes.
3. Escribe cada archivo completo de una vez; evita reescrituras innecesarias.
4. **No puedes compilar el juego.** Al terminar cada bloque haz parse + chequeo de tipos de lo que tocaste y añade a `PORTING_PLAN.md` (máx. ~40 líneas por bloque) las APIs usadas por primera vez, para que un fallo de build se arregle barato.
5. **Un commit y `git push` al terminar CADA bloque** (mensaje claro, trailers de atribución del repo). Así nada se pierde si se agota el cupo.
6. No pidas confirmaciones ni plantees preguntas: decide con el plan, anótalo en el informe y sigue.
7. Informe final ≤30 líneas: qué quedó, qué NO quedó, qué debe probar David.

**Contexto.** Rally of the Guard (NeoForge 1.21.1). E1 está compilado y probado por David; su feedback y datos medidos están en `docs/FEEDBACK_E1.md`. Haz los bloques **en este orden**:

## Bloque 0 · ARREGLOS de E1 (`FEEDBACK_E1.md` §B, todos)
Resuélvelos todos o anota por qué no. Notas: (1) ahogamiento: que naden y salgan; que la orden "quedarse" no los deje bajo el agua; que `/rally debug spawn` no coloque sobre agua. (2) sin munición disponible (incluida la mochila) cambiar a otra arma; si solo hay arma a distancia vacía, retirarse. (3) daño del arco/flechas de dragón y de otros ítems modded: usa la lógica real del ítem (revisa con `javap` cómo crea el proyectil), no un daño base; haz que el guardia dispare con las propiedades del arma/munición. (4) alcance de apuntado/detección: que dependa del arma (`WeaponProfile`, configuración de Scorched Guns) y de la veteranía; un francotirador veterano debe intentar blancos lejanos. (5) volea más precisa con veteranía. (6) HQ: aparcar la montura al entrar, volver a montar al salir; entrar por una puerta única; un guardia en HQ debe salir con la orden. (7) el diálogo de crimen de MCA no debe dispararse por guardias de Rally (revisa los eventos de Mob Compat/Crime con `javap`). (8) un guardia que está en escuadrón no aparece en la lista de guardias, solo en escuadrones. (9) coexistencia con el menú de MCA sobre guardias propios: implementa lo que sea viable; si no, documenta la causa. (10) los tres picos de rendimiento `markers`, `mounts`, `combat_gate`: reparte el trabajo en el tiempo.

## Bloque 1 · E2-A capacidad de mando y capitán calculado
- `commandCapacity` por veteranía (tabla del playbook, configurable). **Capitán = guardia con mayor capacidad de su unidad; se calcula, no se crea.**
- Jerarquía: el líder decide → emite una **directiva** de escuadra (push, una vez) → cada soldado la resuelve con información local. Nunca una decisión pesada por guardia por tick.
- **Escala:** un grupo mayor que la capacidad se divide en subunidades, cada una con su capitán, y las formaciones se calculan **por subunidad** (arregla la V de 200, `FEEDBACK_E1.md` B10). Asigna huecos de formación repartidos en el tiempo, no todos en un tick.
- Sucesión: máx. 3 relevos (4 en ejércitos grandes); luego se desarticula (moral, huidas).

## Bloque 2 · E2-B veteranía = calidad de decisión
Rangos 0–5 (nombres ilustrativos y configurables). XP **solo en combate real** (daño causado, pelea > N s, variedad de enemigos, rendimientos decrecientes por tipo repetido). Cada rango desbloquea comportamiento de la escalera de decisión, **no daño bruto**. Un rasgo por rango según genética (MCA si está; si no, afinidad al azar) e historia. Maestro no es inmortal. Tótems con retraso (rasgo Tanque lo reduce); revivir varias veces puede volverlo kamikaze. Contadores por UUID guardados como el roster.

## Bloque 3 · SIMULACIÓN v2 (plataforma de aprendizaje) — prioridad alta
Objetivo: David abre Minecraft (en un **mundo plano**), lanza un lote **sin atenderlo** y tú aprendes de los resultados. No gastes tokens en rondas de revisión: construye la plataforma una vez, completa.
- **Arena en tierra:** `SimRunner` construye su propia plataforma plana y sólida (config: tamaño por defecto 96×96, bloque del suelo, altura), sin agua en su zona, con terreno por escenario (llano, colina con ventaja de altura para un bando, bloques de cobertura, muro con estrechamiento, bosque simple). Nada de arena en el aire.
- **Bandos simétricos:** cada escenario define dos bandos `{cantidad, rol/arma, rango, líder sí/no, formación, montados sí/no, posición}`. Para medir **sin ruido**, ambos bandos reciben **equipo, armas, munición y vida idénticos**. Los bandos pueden ser guardias de Rally contra guardias de Rally (un bando "rojo" sin dueño jugador, hostil al "azul"), o guardias contra mobs que sostengan el mismo equipo (zombi, esqueleto, husk, saqueador, vindicador; añade golem de hierro/ravager como jefes). Incluye **partidas espejo** (A contra A idéntico) como control: debe dar ≈50 %; si no, es un fallo de la simulación y se informa.
- **Matriz de escenarios** definida en datos (JSON en `config` o recursos), no en código: tamaños 2v2, 5v5, 20v20, 50v50, 100v100 (con tope por rendimiento); composición (cuerpo a cuerpo, arqueros, tiradores con Scorched Guns, mixta); rangos 0–5 por bando (¡rango alto contra rango bajo con el mismo equipo!); con/sin líder y capitán; distancia inicial 20/40/80; posiciones (frente a frente, flanqueo, uno en altura); a pie y montados; con distintas doctrinas/estilos de fuego cuando existan; enemigos de mob; cobertura y terreno.
- **Comando:** `/rally sim batch <preset|all> [--minutes N] [--seeds N] [--tickrate sprint|normal]`. Presets: `quick` (≈15 min), `standard` (≈60 min), `long` (≈2 h), `all`. Ejecuta las corridas **en secuencia**, cada una con tope de tiempo, limpieza total de entidades al terminar y semilla propia; reintenta una vez si una corrida se atasca. Admite `/tick sprint`/`tick rate` de vanilla para ir más rápido; verifica que el resultado no cambia con la velocidad (corrida de control) y deja la opción en configuración. Sigue aceptando `/rally sim ALL`, A, B, C, D como alias.
- **Métricas por corrida:** supervivientes y bajas por bando, vida total restante, tiempo hasta resolver, daño hecho, tiros/aciertos/munición, formación, distancia media, cuántas veces se usó cada peldaño de la escalera de decisión, órdenes ejecutadas, retiradas, muertes de líder, y costo medio por tick (`Perf`).
- **Salida** en `rally-sim/<id-lote>/`: una línea JSON por corrida (`runs.jsonl`), `summary.json` y un **`summary.md` compacto (≤150 líneas)** pensado para que se copie al repo: tabla por escenario con media ± error estándar y tasa de victoria por bando, comparación espejo, y una sección **"Hallazgos"** que detecta automáticamente expectativas incumplidas (p. ej. rango 3 contra rango 0 con igual equipo debería ganar >60 %; D>B>A; C≥A en distancia y bajas) y anomalías (corridas atascadas, ahogados, tiempos extremos, picos de Perf).
- **Registro de esquema** en `docs/SIMULACIONES.md` (escenarios, métricas, cómo añadir uno) y deja el comando exacto para David.
- El juego sigue funcionando en un mundo normal: la plataforma de arena se coloca lejos del jugador en una zona de comprobación configurable y no destruye construcciones existentes (si no hay sitio libre, aborta con mensaje).

## Bloque 4 · E2-C valoración de terreno + E2-D escalera de decisión
- `TerrainSense` exactamente como el playbook §3: ≤8 candidatas, 1 vez por segundo por escuadra, ≤12 lecturas de bloque por candidata, caché 2–4 s; etiquetas `COVER_GOOD/BAD`, `HEIGHT_GOOD/BAD`, `DEFENSIBLE/OPEN`, `NEAR_WATER`, `IN_FOREST`, `CHOKEPOINT`. Solo líderes ≥ Veterano. Invalida la caché con explosiones (los cráteres cuentan como cobertura).
- `DecisionLadder.decide(situation, rank)` función pura, llamada desde el bucle de 20 ticks. Reutiliza `Engagement`, `RetreatPlanner`, `ScorchedGunsCompat`. **Los veteranos con libertad eligen solos su estilo según el arma y la situación; si hay una orden del jugador, solo usan esa.**
- Escape de atrapados v1 (barato): si no avanza y está encerrado, saltar/nadar/salir; si tiene pico y `mobGriefing` lo permite, picar hacia el aire libre con tope de bloques.

## Bloque 5 · Órdenes universales y limpieza de interfaz (`FEEDBACK_E1.md` §C)
- `OrderSemantics`: define para **cada rol** (cuerpo a cuerpo, arquero, tirador de arma de fuego, montado) qué significa cada orden (avanzar, mantener, replegarse, atacar, seguir, formación). Un grupo mixto que recibe una orden que a un rol no le aplica la **degrada de forma sensata** (el cuerpo a cuerpo avanza en lugar de "fuego a distancia"). Sin excepciones silenciosas: si no aplica, se registra en el log de depuración.
- Las **opciones de estilo de fuego solo aparecen si hay armas a distancia en la selección**. Reorganiza los botones en grupos (Posición · Ataque · Estilo · Doctrina) con disponibilidad calculada por composición; sin botones muertos. La interfaz debe ser limpia y escalable.
- **Atacar con un escuadrón entero** en una orden (seleccionar escuadrón + objetivo).
- Deja enganchada la lista de doctrinas del playbook (§4) como pesos de la escalera, para que el Battle Planner (E3) solo tenga que elegirlas.

## Bloque 6 · Verificación
Revisa tu propio trabajo por cada bloque (nombres de API, ciclos de vida, hilos del servidor, límites por tick), actualiza `PORTING_PLAN.md` (§ nuevas APIs) y `CLAUDE.md` ("Current status") y deja el informe.

**Corte si el cupo se agota** (de lo más sacrificable a lo intocable): picos de rendimiento B10 → escape de atrapados → doctrinas como pesos → atacar con escuadrón → partidas de mobs (dejar solo guardias espejo) → presets `long` → … Intocables: Bloques 0 (1–8), 1, 2, 3 y `DecisionLadder`.

**Entrega:** un commit y push por bloque, informe ≤30 líneas con qué NO quedó.
