# Prompt E1 para Claude Code

Presupuesto ajustado. Reglas de este prompt:
1. Lee `docs/PLAN_ENTREGAS.md`, `docs/DECISIONES_TACTICAS.md`, `CLAUDE.md` y solo los archivos que vayas a modificar. No re-explores el repo.
2. Sin investigación web: lo investigado está en `docs/BACKLOG.md`, `docs/ARQUITECTURA.md`, `docs/VETERANIA.md`. Si falta un dato, anótalo como pregunta; no lo busques.
3. Escribe cada archivo completo en una sola pasada; no lo reescribas más de dos veces.
4. No compiles intentos parciales: un solo parse + chequeo de tipos al final.
5. Haz los commits en los puntos de control que indique el prompt (si no indica ninguno, uno al terminar). Actualiza la lista de “APIs usadas por primera vez” en `PORTING_PLAN.md`, para que un fallo de build se arregle barato.
6. Informe final de máximo 25 líneas: qué quedó, qué NO quedó, qué debe probar David.
7. **No leas `PORTING_PLAN.md` entero** (más de 2200 líneas): lee solo su última sección (`tail -n 120`) y usa `grep` para lo demás. En `PORTING_PLAN.md` añade como máximo ~40 líneas por fase; en `CLAUDE.md` actualiza solo la sección “Current status”. No crees documentos nuevos que no se pidan.
8. No pidas confirmaciones ni plantees preguntas a David durante el trabajo: decide con el plan, anótalo en el informe y sigue.
9. Si algo de lo pedido no cabe, **recorta siguiendo la línea de corte de `docs/PLAN_ENTREGAS.md` §2** y dilo en el informe. No dejes nada a medias sin avisar.

---

**Contexto.** Eres Claude Code en el repo de Rally of the Guard (NeoForge 1.21.1). Lee `docs/PLAN_ENTREGAS.md` §1 (decisiones) y `docs/DECISIONES_TACTICAS.md` §0 (principios; el resto es para E2).

**Antes de nada:** corrige cualquier aviso de compilación pendiente (p. ej. `@EventBusSubscriber(bus=…)` obsoleto en `CommanderKeys`) y todo lo que David reporte en el bloque ARREGLOS al final. No toques más código existente que lo necesario para los puntos siguientes.

**E1-A · Mochila de guerra (6 espacios).**
- Inventario propio de 6 espacios por guardia, *data attachment* (o el mecanismo persistente del roster); sobrevive a recargar el mundo y a cambios de dimensión.
- Munición y consumibles (manzana dorada, poción, dinamita, segunda arma) se toman de ahí. La mano secundaria se rellena desde la mochila cuando no está en combate. Sin munición → cuerpo a cuerpo (ya existe).
- Consumo 1 ítem/4 disparos, en una sola constante. Reabastecimiento desde cofre/barril a ≤4 bloques, ahora rellenando la mochila.
- UI mínima: en la pestaña Guardias del Libro, botón **Mochila** que abre un menú de contenedor estándar de 6 espacios. El hover de la ficha muestra espacios usados y munición total. Sin pantallas nuevas complejas.
- Al morir (incluido “se vuelve zombi”, ya resuelto en `GuardRosterCleanupHandler`) suelta la mochila como botín.

**E1-B · Maletas.** Si un espacio contiene un ítem que exponga `Capabilities.ItemHandler.ITEM` con más de 6 espacios (Sophisticated Backpacks), el guardia toma munición/comida de él. **Sin importar clases de ese mod**; sin dependencia dura; se reconoce por la capacidad, no por id. Apúntalo como “API de primer uso”. Sin maleta, todo igual.

**E1-C · Cuarteles por escuadra.** Registro persistente (id, nombre, posición, dimensión), permanente hasta que David lo borre. Cada escuadra tiene `hqId` (propio por defecto; compartible; nunca uno común automático). Desde el Libro: crear/renombrar/borrar, asignar la escuadra seleccionada, y **agachado + G sobre un estandarte lo vincula al cuartel seleccionado**. Si el estandarte se rompe, el cuartel conserva su última posición. Retirada: punto temporal → cuartel de cada escuadra → te siguen. Migra `RetreatPlanner`, no lo dupliques.

**E1-D · Monturas.** (1) Con G, el guardia montado va a galope máximo hasta ~3 bloques del destino, en `MountDriver`. (2) **Generaliza el conductor a cualquier montura terrestre**, no solo caballos: configurable por *entity type tag* `rallyguard:guard_mounts` (default: caballo, mula, burro, llama, camello, cerdo ensillado, strider en lava). Voladoras e hipogrifos siguen en espera.

**E1-E · Escala.**
- `/rally debug spawn <n> [escuadra]` y `/rally debug perf` (ms/tick de **Rally** por sistema, medido con `nanoTime`).
- Ningún sistema de Rally es O(n²) ni escanea a toda la escuadra cada tick. Lo periódico se reparte (`tickCount % 20 == id % 20`); todo evento es push.
- Paginación del Libro más allá de las 2000 filas. Config: `maxGuardsProcessedPerTick`, `ledgerPageSize`, interruptores de sistemas opcionales.
- `docs/ESCALA.md` (≤40 líneas): reglas y una tabla vacía “n → ms/tick” para David.

**E1-F · Arnés de simulación (línea base).** Comando `/rally sim <A|B|C|D> [semilla] [repeticiones=3]`. Cada escenario genera en terreno plano una fila de guardias y 20 zombis a 30 bloques, y deja correr el combate en tiempo real:
- **A** 20 reclutas · **B** 20 veteranos · **C** 20 arqueros · **D** 20 arqueros + 1 capitán veterano.
- Para B y D, usa `/rally debug rank <n>` (nuevo, solo admin) que fija rango a los guardias de prueba; **hasta E2 el rango no cambia nada**: eso es esperado y la línea base lo documenta.
- Métricas por escenario (un contador barato `SimProbe`, activo **solo** durante una simulación): supervivientes, ticks de combate, bajas, % de formación mantenida (muestreo 1/s), munición gastada, cambios de arma, distancia media al enemigo más cercano, órdenes ejecutadas.
- Salida: chat + `rally-sim/<escenario>-<semilla>.json`. Promedia las repeticiones (la IA no es determinista). Al final imprime la comparación A vs C vs B vs D.
- `docs/SIMULACIONES.md` (≤30 líneas) explica cómo correrlo y qué orden de resultados se espera tras E2 (D > B > A; C ≥ A en distancia y bajas).

**E1-G · Fuego de tiradores (arqueros, ballesteros, armas de fuego).** Hallazgo de las pruebas de David: con G sobre un zombi lejano, los guardias lo marcan pero no disparan hasta que está cerca, mientras el enemigo sí les dispara desde más lejos. Causa probable: `CommandTargeting.inReach` mide la correa del puesto (`POST_LEASH` = 16) hasta el **objetivo**, y la ignora el alcance real del arma. Se pide:
1. **Perfil de arma** (`WeaponProfile`, una sola tabla): arco, ballesta, arma de Scorched Guns → `maxRange`, `optimalRange`, `minRange`. Para armas de fuego léelo de los datos del arma si es barato; si no, config.
2. **Alcance real:** para un guardia con arma a distancia, la correa del puesto es `max(POST_LEASH, 0.8 × maxRange)`, y si el objetivo designado está fuera de `optimalRange` pero dentro de `maxRange`, **se acerca hasta su rango óptimo** (sin salirse de la correa ampliada) y dispara. Un guardia libre persigue hasta el rango óptimo, no hasta el cuerpo a cuerpo.
3. **Modos de fuego** (por escuadra, orden de movimiento/combate nueva y barata, botones en el Libro; las doctrinas del Planner los usarán después): **Quieto** (dispara sin moverse), **Avanzando** (dispara mientras avanza hacia el objetivo/destino), **Retrocediendo** (mantiene distancia ≥ `minRange` y se aleja a pasos mientras dispara cuando el enemigo se acerca), **Volea**.
4. **Volea (lluvia de flechas):** con la escuadra apostada (Hold Ground / modo Volea) y un objetivo conocido —lo ve un miembro o lo marcó el comandante con G— **fuera del alcance directo o sin línea de visión**, cada arquero lanza flechas **en parábola** con el ángulo que maximiza el alcance: resuelve el ángulo con la balística de la flecha (gravedad 0.05 por tick, rozamiento 0.99, velocidad máxima de arco/ballesta) con ≤ 8 iteraciones por disparo, anticipando la posición del objetivo. Dispersión proporcional a la distancia (rangos altos la reducirán en E2). Límites obligatorios: cadencia por arquero ≥ 1.5 s, tope de flechas por escuadra por segundo en config, nunca si hay un aliado a ≤ 5 bloques del punto de impacto (usa el estado ya conocido, sin escanear), gasta munición de la mochila, flechas sin recogida. Las armas de fuego no hacen volea (solo 1–3).
5. Las flechas de guardias propios **no dañan a los nuestros ni a sus monturas** (ver ARREGLOS 1).
6. Añade al arnés de simulación las métricas “disparos”, “impactos” y “distancia media al enemigo” del escenario C.

**Fuera de alcance:** líderes, veteranía, terreno, Planner, guerrillas, uniformes. No los empieces.

**ARREGLOS DE PRUEBAS 3H.1 (David probó las 12 pruebas: todo bien salvo esto):**
1. **Fuego amigo a caballos.** Los guardias siguen pudiendo dispararse y hacer daño entre sí a los **caballos de otros guardias**. Ya existe protección en `RallyFriendlyFireHandler` (arcos) pero no basta; probablemente las balas de Scorched Guns o las flechas llegan con `attacker` nulo o con otro tipo de daño. Haz dos cosas: (a) **registro de diagnóstico** (nivel INFO, solo cuando se daña una montura que lleva un guardia con dueño) con tipo de daño, clase de la entidad directa y del atacante, para ver en el log qué fuente se escapa; (b) **bloqueo más robusto**: cancela el impacto (`ProjectileImpactEvent`) y el daño cuando el proyectil lo disparó un guardia o el jugador dueño de esa montura/escuadra, y cuando el origen sea desconocido y la víctima sea una montura de guardia propio y haya un guardia con dueño igual a ≤ 64 bloques. Documenta qué cubre cada capa.
2. **Contratar guardias con MCA: Mob Compat instalado, y el “follow” de MCA que secuestra guardias.** Hechos verificados leyendo el jar de Mob Compat 0.2.1 (no está en el repo; `octetus/` está en `.gitignore`, no lo añadas a git): con la opción por defecto `guardMcaInteraction=true`, un clic derecho en un guardia abre la pantalla de interacción de MCA a través de un **aldeano MCA oculto** (`GuardProxy`) que copia el movimiento del guardia (`GuardProxySync.relayNavigation` y `mirrorMoveState`, que mueven y fijan seguir/patrullar del guardia con `GuardReflection.setFollowing/setPatrolling/setOwnerId`). Consecuencias: (a) tapa la contratación de Rally; (b) **cualquier jugador puede darle “seguir” a un guardia ajeno**, saltándose `GuardModes`, la propiedad y el Libro. Mob Compat publica un evento cancelable en el bus de NeoForge: `dev.otectus.mcamobcompat.api.event.GuardInteractEvent` (`getPlayer()`, `getGuard()`, `getHand()`, `setCancellationResult(InteractionResult)`, implementa `ICancellableEvent`) y una API estática `dev.otectus.mcamobcompat.api.MobCompatibilityAPI` (`isBridgedGuard(Entity)`, `getOwner(Entity)`). **Sin dependencia dura ni compilar contra ese jar**: registra el oyente por reflexión en `compat/` (p. ej. `NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, true, claseResuelta, consumidor)` si la clase existe), falla en silencio si no está. Reglas:
   - **Guardia sin dueño:** el clic derecho abre la **contratación de Rally** (cancela el evento de Mob Compat). Añade en la pantalla de contratación un botón **“Hablar”** que abre la interacción de MCA para ese guardia por reflexión (`GuardInteractions.interactAfterHook`/`delegateToGuardVillagers` si sirven; si no es viable, déjalo anotado en el informe y sigue).
   - **Guardia con dueño:** **se cancela siempre** el evento de MCA, tanto para extraños como para el dueño, de modo que ni el “seguir” ni el “quedarse” de MCA puedan mover guardias contratados; la única autoridad de movimiento sigue siendo `GuardModes`. Un extraño que hace clic derecho en un guardia ajeno no obtiene nada (mensaje corto opcional).
   - **Red de seguridad:** `GuardModes` ya es la única autoridad; si detectas una forma barata de que un cambio de seguir/patrullar de un guardia con dueño que venga de otra fuente sea revertido, añádela; si no, no inventes nada.
   - Contratar **dentro del menú de MCA** (un botón nuevo ahí) solo si MCA Reborn ofrece una vía sencilla y estable; no investigues en la web para eso, anótalo como pregunta.
3. Aviso de compilación: `@EventBusSubscriber(bus=…)` obsoleto en `CommanderKeys`.

**Orden de trabajo y puntos de control (este prompt es grande y el cupo es limitado):** 1) ARREGLOS y E1-G (fuego de tiradores) → **commit 1**; 2) E1-A, B, C, D → **commit 2**; 3) E1-E y E1-F (escala y simulación) → **commit 3**. Si el cupo se agota, lo ya comprometido queda a salvo. Prioridad si hay que recortar: ARREGLOS > E1-G > E1-A/B > E1-C > E1-D > E1-E > E1-F.

**Entrega:** `PORTING_PLAN.md` y `CLAUDE.md` actualizados, informe corto. Di explícitamente qué NO quedó.
