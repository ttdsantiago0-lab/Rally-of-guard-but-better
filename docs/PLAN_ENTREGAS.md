# Rally of the Guard — Plan de entregas v2 (cupo ≈ US$40 ≈ 4 prompts)

Fecha: 2026-10-05. Documento para David y para Claude Code: los dos trabajan sobre el mismo plan.
**Claude Code: lee este archivo completo y `docs/DECISIONES_TACTICAS.md` antes de tocar nada. No re-explores lo que ya está en `docs/`.**

---

## 1. Decisiones fijadas (David, 2026-10-05)

| Tema | Decisión |
|---|---|
| Inventario | Cada guardia tiene **6 espacios propios** además de manos y armadura. |
| Munición | Sale **solo del inventario** (nada virtual). 1 ítem cada 4 disparos, constante en un único sitio. |
| Maletas | **Sophisticated Backpacks** (ya instalado). Un guardia debe poder usar una maleta como almacenamiento extra, sin dependencia dura. |
| Escala | **10 000 es un objetivo de arquitectura, no una promesa de 10 000 mobs activos a 60 FPS.** 10 000 guardias *registrados*; cientos *distribuidos*; solo los relevantes corren IA intensiva; las escuadras usan estado agregado; el líder decide → emite una directiva → cada soldado resuelve su situación local. Todo lo nuevo es O(n) o mejor y repartido en ticks. |
| Cuarteles | Cada escuadra tiene el suyo (compartible, nunca todos en uno por defecto). Cuartel **permanente**, puntos de retirada **temporales**. La bandera pertenece al cuartel seleccionado en el Libro. |
| Caballos | Con G, el guardia montado va lo más rápido posible. |
| Si muere el jugador | Se queda como está. |
| Veteranía | **Los líderes van primero.** La veteranía mejora **la calidad de las decisiones, no el daño bruto** (ver `DECISIONES_TACTICAS.md`). |
| Capitán | **No es una entidad especial**: es una **propiedad calculada** de una unidad/subgrupo (el guardia con más capacidad de mando). Valores ilustrativos de capacidad: ~5–10 normal, ~20–30 veterano, ~50 muy veterano, 100+ legendario. |
| Terreno | Los veteranos valoran altura, cobertura, agua, puertas, muros, bosque, edificios, estrechamientos y puentes **como conceptos simples** (buena/mala cobertura, altura favorable/desfavorable, posición defendible/abierta). No es un simulador militar. |
| Doctrinas | El Battle Planner trae doctrinas básicas: hostigar, evitar bajas, distraer, ataque total… |
| Guerrillas | Addon aparte, v1 recortada. |
| Nigromante | Solo se activa si se usa la **Parca de MCA Reborn**. (Fuera de las 4 entregas; queda en backlog.) |
| Uniformes | Basados en **la bandera única** del jugador. Solo estética. |
| Presupuesto | ≈ US$40. David acepta que un prompt llegue a ≈ US$20 si eso adelanta más. Plan realista: E1 + E2 + E3; E4 solo si queda cupo. Los fallos de las pruebas viajan **dentro del siguiente prompt**. |

---

## 2. Orden de entregas y línea de corte

| # | Nombre | Contenido | Se manda cuando |
|---|---|---|---|
| E0 | Verificar | `.\gradlew.bat build` de la rama + checklist de 11 pruebas de 3H.1 | ya (gratis) |
| **E1** | **Fundación + fuego de tiradores** | Mochila 6 + maleta; cuarteles por escuadra; galope; monturas no-caballo (genéricas); escala y medición; **arnés de simulación A–D**; **fuego de tiradores** (alcance real, fuego en movimiento, volea); arreglos de pruebas (fuego amigo a caballos, contratar con MCA) | E0 verificado ✔ (12 pruebas, 2026-10-05) |
| **E2** | **Mando** | Líderes (capacidad de mando calculada) + veteranía como calidad de decisión + valoración de terreno + escalera de decisión + muñeco de práctica en el cuartel + saqueo + **mago de apoyo v1** + **logística de cuartel** | E1 compila y se prueba |
| **E3** | **Planner** | **Battle Planner** con doctrinas y grupos G+número · uniformes de bandera · lanza y escudo grandes | E2 compila y se prueba |
| **E4** | **Mundo** | Guerrillas v1 (addon) + compat MCA suave + arreglos acumulados | E3 compila y se prueba |

**Línea de corte** (qué se sacrifica primero si el cupo se acaba, de abajo hacia arriba):
1. Campamentos de guerrilla (queda la banda errante).
2. Lanza y escudo grandes (queda la lógica de veteranía, sin ítems).
3. Uniformes.
4. Compat MCA (queda solo la detección).
5. Saqueo y muñeco de práctica.
**Intocables:** E1 completo, líderes+veteranía, valoración de terreno, Battle Planner con doctrinas, arnés de simulación.

Entre prompts: **build → 10–15 min de pruebas → pegarme lo que falló**. Yo lo convierto en el bloque “arreglos” del siguiente prompt. **No se manda el siguiente prompt hasta que el anterior compile.**

---

## 3. Bloque de ahorro (pegar al inicio de cada prompt)

> Presupuesto ajustado. Reglas de este prompt:
> 1. Lee `docs/PLAN_ENTREGAS.md`, `docs/DECISIONES_TACTICAS.md`, `CLAUDE.md` y solo los archivos que vayas a modificar. No re-explores el repo.
> 2. Sin investigación web: lo investigado está en `docs/BACKLOG.md`, `docs/ARQUITECTURA.md`, `docs/VETERANIA.md`. Si falta un dato, anótalo como pregunta; no lo busques.
> 3. Escribe cada archivo completo en una sola pasada; no lo reescribas más de dos veces.
> 4. No compiles intentos parciales: un solo parse + chequeo de tipos al final.
> 5. Haz los commits en los puntos de control que indique el prompt (si no indica ninguno, uno al terminar). Actualiza la lista de “APIs usadas por primera vez” en `PORTING_PLAN.md`, para que un fallo de build se arregle barato.
> 6. Informe final de máximo 25 líneas: qué quedó, qué NO quedó, qué debe probar David.
> 7. **No leas `PORTING_PLAN.md` entero** (más de 2200 líneas): lee solo su última sección (`tail -n 120`) y usa `grep` para lo demás. En `PORTING_PLAN.md` añade como máximo ~40 líneas por fase; en `CLAUDE.md` actualiza solo la sección “Current status”. No crees documentos nuevos que no se pidan.
> 8. No pidas confirmaciones ni plantees preguntas a David durante el trabajo: decide con el plan, anótalo en el informe y sigue.
> 9. Si algo de lo pedido no cabe, **recorta siguiendo la línea de corte de `docs/PLAN_ENTREGAS.md` §2** y dilo en el informe. No dejes nada a medias sin avisar.

---

## 4. Prompt E1 — Fundación

> (Pega primero el bloque de §3.)
>
> **Contexto.** Eres Claude Code en el repo de Rally of the Guard (NeoForge 1.21.1). Lee `docs/PLAN_ENTREGAS.md` §1 (decisiones) y `docs/DECISIONES_TACTICAS.md` §0 (principios; el resto es para E2).
>
> **Antes de nada:** corrige cualquier aviso de compilación pendiente (p. ej. `@EventBusSubscriber(bus=…)` obsoleto en `CommanderKeys`) y todo lo que David reporte en el bloque ARREGLOS al final. No toques más código existente que lo necesario para los puntos siguientes.
>
> **E1-A · Mochila de guerra (6 espacios).**
> - Inventario propio de 6 espacios por guardia, *data attachment* (o el mecanismo persistente del roster); sobrevive a recargar el mundo y a cambios de dimensión.
> - Munición y consumibles (manzana dorada, poción, dinamita, segunda arma) se toman de ahí. La mano secundaria se rellena desde la mochila cuando no está en combate. Sin munición → cuerpo a cuerpo (ya existe).
> - Consumo 1 ítem/4 disparos, en una sola constante. Reabastecimiento desde cofre/barril a ≤4 bloques, ahora rellenando la mochila.
> - UI mínima: en la pestaña Guardias del Libro, botón **Mochila** que abre un menú de contenedor estándar de 6 espacios. El hover de la ficha muestra espacios usados y munición total. Sin pantallas nuevas complejas.
> - Al morir (incluido “se vuelve zombi”, ya resuelto en `GuardRosterCleanupHandler`) suelta la mochila como botín.
>
> **E1-B · Maletas.** Si un espacio contiene un ítem que exponga `Capabilities.ItemHandler.ITEM` con más de 6 espacios (Sophisticated Backpacks), el guardia toma munición/comida de él. **Sin importar clases de ese mod**; sin dependencia dura; se reconoce por la capacidad, no por id. Apúntalo como “API de primer uso”. Sin maleta, todo igual.
>
> **E1-C · Cuarteles por escuadra.** Registro persistente (id, nombre, posición, dimensión), permanente hasta que David lo borre. Cada escuadra tiene `hqId` (propio por defecto; compartible; nunca uno común automático). Desde el Libro: crear/renombrar/borrar, asignar la escuadra seleccionada, y **agachado + G sobre un estandarte lo vincula al cuartel seleccionado**. Si el estandarte se rompe, el cuartel conserva su última posición. Retirada: punto temporal → cuartel de cada escuadra → te siguen. Migra `RetreatPlanner`, no lo dupliques.
>
> **E1-D · Monturas.** (1) Con G, el guardia montado va a galope máximo hasta ~3 bloques del destino, en `MountDriver`. (2) **Generaliza el conductor a cualquier montura terrestre**, no solo caballos: configurable por *entity type tag* `rallyguard:guard_mounts` (default: caballo, mula, burro, llama, camello, cerdo ensillado, strider en lava). Voladoras e hipogrifos siguen en espera.
>
> **E1-E · Escala.**
> - `/rally debug spawn <n> [escuadra]` y `/rally debug perf` (ms/tick de **Rally** por sistema, medido con `nanoTime`).
> - Ningún sistema de Rally es O(n²) ni escanea a toda la escuadra cada tick. Lo periódico se reparte (`tickCount % 20 == id % 20`); todo evento es push.
> - Paginación del Libro más allá de las 2000 filas. Config: `maxGuardsProcessedPerTick`, `ledgerPageSize`, interruptores de sistemas opcionales.
> - `docs/ESCALA.md` (≤40 líneas): reglas y una tabla vacía “n → ms/tick” para David.
>
> **E1-F · Arnés de simulación (línea base).** Comando `/rally sim <A|B|C|D> [semilla] [repeticiones=3]`. Cada escenario genera en terreno plano una fila de guardias y 20 zombis a 30 bloques, y deja correr el combate en tiempo real:
> - **A** 20 reclutas · **B** 20 veteranos · **C** 20 arqueros · **D** 20 arqueros + 1 capitán veterano.
> - Para B y D, usa `/rally debug rank <n>` (nuevo, solo admin) que fija rango a los guardias de prueba; **hasta E2 el rango no cambia nada**: eso es esperado y la línea base lo documenta.
> - Métricas por escenario (un contador barato `SimProbe`, activo **solo** durante una simulación): supervivientes, ticks de combate, bajas, % de formación mantenida (muestreo 1/s), munición gastada, cambios de arma, distancia media al enemigo más cercano, órdenes ejecutadas.
> - Salida: chat + `rally-sim/<escenario>-<semilla>.json`. Promedia las repeticiones (la IA no es determinista). Al final imprime la comparación A vs C vs B vs D.
> - `docs/SIMULACIONES.md` (≤30 líneas) explica cómo correrlo y qué orden de resultados se espera tras E2 (D > B > A; C ≥ A en distancia y bajas).
>
> **E1-G · Fuego de tiradores (arqueros, ballesteros, armas de fuego).** Hallazgo de las pruebas de David: con G sobre un zombi lejano, los guardias lo marcan pero no disparan hasta que está cerca, mientras el enemigo sí les dispara desde más lejos. Causa probable: `CommandTargeting.inReach` mide la correa del puesto (`POST_LEASH` = 16) hasta el **objetivo**, y la ignora el alcance real del arma. Se pide:
> 1. **Perfil de arma** (`WeaponProfile`, una sola tabla): arco, ballesta, arma de Scorched Guns → `maxRange`, `optimalRange`, `minRange`. Para armas de fuego léelo de los datos del arma si es barato; si no, config.
> 2. **Alcance real:** para un guardia con arma a distancia, la correa del puesto es `max(POST_LEASH, 0.8 × maxRange)`, y si el objetivo designado está fuera de `optimalRange` pero dentro de `maxRange`, **se acerca hasta su rango óptimo** (sin salirse de la correa ampliada) y dispara. Un guardia libre persigue hasta el rango óptimo, no hasta el cuerpo a cuerpo.
> 3. **Modos de fuego** (por escuadra, orden de movimiento/combate nueva y barata, botones en el Libro; las doctrinas del Planner los usarán después): **Quieto** (dispara sin moverse), **Avanzando** (dispara mientras avanza hacia el objetivo/destino), **Retrocediendo** (mantiene distancia ≥ `minRange` y se aleja a pasos mientras dispara cuando el enemigo se acerca), **Volea**.
> 4. **Volea (lluvia de flechas):** con la escuadra apostada (Hold Ground / modo Volea) y un objetivo conocido —lo ve un miembro o lo marcó el comandante con G— **fuera del alcance directo o sin línea de visión**, cada arquero lanza flechas **en parábola** con el ángulo que maximiza el alcance: resuelve el ángulo con la balística de la flecha (gravedad 0.05 por tick, rozamiento 0.99, velocidad máxima de arco/ballesta) con ≤ 8 iteraciones por disparo, anticipando la posición del objetivo. Dispersión proporcional a la distancia (rangos altos la reducirán en E2). Límites obligatorios: cadencia por arquero ≥ 1.5 s, tope de flechas por escuadra por segundo en config, nunca si hay un aliado a ≤ 5 bloques del punto de impacto (usa el estado ya conocido, sin escanear), gasta munición de la mochila, flechas sin recogida. Las armas de fuego no hacen volea (solo 1–3).
> 5. Las flechas de guardias propios **no dañan a los nuestros ni a sus monturas** (ver ARREGLOS 1).
> 6. Añade al arnés de simulación las métricas “disparos”, “impactos” y “distancia media al enemigo” del escenario C.
>
> **Fuera de alcance:** líderes, veteranía, terreno, Planner, guerrillas, uniformes. No los empieces.
>
> **ARREGLOS DE PRUEBAS 3H.1 (David probó las 12 pruebas: todo bien salvo esto):**
> 1. **Fuego amigo a caballos.** Los guardias siguen pudiendo dispararse y hacer daño entre sí a los **caballos de otros guardias**. Ya existe protección en `RallyFriendlyFireHandler` (arcos) pero no basta; probablemente las balas de Scorched Guns o las flechas llegan con `attacker` nulo o con otro tipo de daño. Haz dos cosas: (a) **registro de diagnóstico** (nivel INFO, solo cuando se daña una montura que lleva un guardia con dueño) con tipo de daño, clase de la entidad directa y del atacante, para ver en el log qué fuente se escapa; (b) **bloqueo más robusto**: cancela el impacto (`ProjectileImpactEvent`) y el daño cuando el proyectil lo disparó un guardia o el jugador dueño de esa montura/escuadra, y cuando el origen sea desconocido y la víctima sea una montura de guardia propio y haya un guardia con dueño igual a ≤ 64 bloques. Documenta qué cubre cada capa.
> 2. **Contratar guardias con MCA: Mob Compat instalado, y el “seguir” de MCA que secuestra guardias. Decisión de David: el guardia SIGUE siendo parte de MCA (nombre, personalidad, corazones, historia; `treatGuardsAsMcaGuards` y `guardShowNameTag` se quedan activados) y MCA no debe estorbar a Rally.** Hechos verificados leyendo el jar de Mob Compat 0.2.1 (no está en el repo; `octetus/` está en `.gitignore`, no lo añadas a git): con `guardMcaInteraction=true`, un clic derecho en un guardia abre la interacción de MCA a través de un **aldeano MCA oculto** (`GuardProxy`) cuyo movimiento y estado se copian al guardia cada tick (`GuardProxySync.relayNavigation` y `mirrorMoveState`; `GuardReflection.setFollowing/setPatrolling/setOwnerId`). Consecuencias: (a) tapa la contratación de Rally; (b) **cualquier jugador puede darle “seguir” a un guardia ajeno**, saltándose `GuardModes` y la propiedad. Mob Compat publica un evento cancelable en el bus de NeoForge: `dev.otectus.mcamobcompat.api.event.GuardInteractEvent` (`getPlayer()`, `getGuard()`, `getHand()`, `setCancellationResult(InteractionResult)`, implementa `ICancellableEvent`) y una API estática `dev.otectus.mcamobcompat.api.MobCompatibilityAPI` (`isBridgedGuard(Entity)`, `getOwner(Entity)`). **Sin dependencia dura ni compilar contra ese jar:** registra el oyente por reflexión en `compat/` (p. ej. `NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, true, claseResuelta, consumidor)` si la clase existe) y falla en silencio si no está. Reglas:
>    - **Guardia sin dueño:** el clic derecho abre la **contratación de Rally** (cancela el evento de Mob Compat). En esa pantalla, un botón **“Hablar”** abre la interacción de MCA de ese guardia por reflexión (`GuardInteractions.interactAfterHook`/`delegateToGuardVillagers`); si no es viable, anótalo en el informe y sigue.
>    - **Guardia con dueño, jugador que NO es el dueño:** se cancela el evento: no puede hablarle ni darle órdenes de MCA.
>    - **Guardia con dueño, el dueño:** el menú de MCA **sí se abre** (conversar, corazones, historia). Pero **Rally manda en movimiento y propiedad**: (i) si tras una interacción de MCA cambian `Following`/`Patrolling`/`ownerId` del guardia sin pasar por `GuardModes`/`GuardOwnership`, Rally lo **reconcilia**: si el dueño interactuó con MCA en los últimos ~60 s, **adopta** el cambio como orden suya (MCA “seguir” → `GuardModes.follow`, “quedarse” → `GuardModes.postHere`), y si no, lo **revierte**; (ii) el `ownerId` de un guardia contratado **nunca** cambia sin pasar por Rally (si cambia, se revierte y se registra). Haz la comprobación en el bucle repartido en 20 ticks, sin escanear a toda la escuadra por tick.
>    - Si una vía simple lo permite, y solo entonces, ofrece **contratar dentro del menú de MCA**; no investigues en la web para eso, anótalo como pregunta.
> 3. Aviso de compilación: `@EventBusSubscriber(bus=…)` obsoleto en `CommanderKeys`.
>
> **Orden de trabajo y puntos de control (este prompt es grande y el cupo es limitado):** 1) ARREGLOS y E1-G (fuego de tiradores) → **commit 1**; 2) E1-A, B, C, D → **commit 2**; 3) E1-E y E1-F (escala y simulación) → **commit 3**. Si el cupo se agota, lo ya comprometido queda a salvo. Prioridad si hay que recortar: ARREGLOS > E1-G > E1-A/B > E1-C > E1-D > E1-E > E1-F.
>
> **Entrega:** `PORTING_PLAN.md` y `CLAUDE.md` actualizados, informe corto. Di explícitamente qué NO quedó.

---

## 5. Prompt E2 — Mando (líderes + veteranía + terreno)

> (Bloque de §3. Después, el **ARREGLOS** de E1.)
>
> **Contexto.** Lee `docs/DECISIONES_TACTICAS.md` completo: es la **especificación** de este prompt. Aquí se implementa, no se rediseña. Lee `docs/VETERANIA.md` §0–4.
>
> **E2-A · Capacidad de mando y capitán calculado.**
> - Cada guardia tiene `commandCapacity` derivada de su veteranía (tabla en el playbook; ilustrativa y configurable).
> - **Capitán = el guardia con mayor capacidad dentro de una unidad/escuadra; se calcula, no se crea.** Si no hay líder veterano, la escuadra se comporta como hoy.
> - Jerarquía: el líder decide → emite una **directiva** de escuadra (push, una vez) → cada soldado la resuelve con información local. **Nunca** una decisión por guardia por tick.
> - Sucesión: si cae el líder, manda el siguiente; máximo 3 relevos (4 en ejércitos grandes); después la escuadra se desarticula (moral, huidas).
>
> **E2-B · Veteranía = calidad de decisión.** Rango 0–5 (nombres: Recluta, Entrenado, Veterano, Élite, Maestro; solo ilustrativos, configurables). XP **solo en combate real** (daño causado, pelea de más de N s, variedad de enemigos, rendimientos decrecientes por tipo repetido). Cada rango desbloquea comportamiento de la escalera de decisión del playbook, **no daño bruto**. Un rasgo por rango según genética (MCA si está; si no, afinidad al azar) e historia. Maestro **no** es inmortal. Los tótems no se usan al instante (retraso; el rasgo Tanque lo reduce); revivir varias veces seguidas puede volverlo kamikaze. Contadores por UUID, guardados como el roster.
>
> **E2-C · Valoración de terreno (conceptos simples).** Implementa `TerrainSense` exactamente como lo define el playbook §3: puntúa **como máximo 8 posiciones candidatas** alrededor del líder, **una vez por segundo por escuadra**, con muestreo barato (≤12 lecturas de bloque por candidata; sin raycasts masivos), caché de 2–4 s. Salida: una etiqueta por candidata (`COVER_GOOD/BAD`, `HEIGHT_GOOD/BAD`, `DEFENSIBLE/OPEN`, más banderas `NEAR_WATER`, `IN_FOREST`, `CHOKEPOINT`). Solo líderes de rango ≥ Veterano la usan; el resto sigue como hoy.
>
> **E2-D · Escalera de decisión** del playbook §2 (objetivo → formación → cobertura → rango óptimo → amenaza prioritaria → avanzar/disparar/cubrirse/retirarse). Una función pura `DecisionLadder.decide(situation, rank)` fácil de probar, llamada desde el bucle ya repartido en 20 ticks. Reutiliza `Engagement`, `RetreatPlanner`, `ScorchedGunsCompat`.
>
> **E2-E · Entrenamiento y saqueo.** Muñeco de práctica junto al cuartel (bloque o entidad simple): descubre afinidades y lleva a rango 1, **nunca más**. **Saqueo:** al morir un enemigo en combate de la escuadra, un guardia libre con espacio en la mochila recoge el botín cercano (≤8 bloques, tras terminar la pelea, política configurable: todo / solo munición y comida / nada).
>
> **E2-F · Simulación.** Conecta `/rally debug rank` al sistema real y **corre los escenarios A–D**; escribe los resultados (promedios) en `docs/SIMULACIONES.md` con una tabla antes/después. Si el orden esperado no se cumple, **dilo**; no lo maquilles.
>
> **E2-G · Mago de apoyo v1 (nativo, sin dependencias; clase = ítem).** Un guardia con un **foco de mago** en la mano (etiqueta `rallyguard:mage_focus`; por defecto: el libro de hechizos de Ars Nouveau si el mod está, y el bastón/vara de blaze como alternativa vanilla) se comporta como **sanador de apoyo**: cada 6 s (configurable) cura al aliado más herido a ≤ 12 bloques (vida o efecto de regeneración), con partículas y sonido propios; no ataca cuerpo a cuerpo, se mantiene detrás de la formación y huye antes que un tirador. La veteranía mejora **decisión** (elige antes a quién curar, prioriza al capitán), no la cantidad bruta. Una sola función `SupportDecision` pura, repartida en el bucle de 20 ticks y sin escanear a toda la escuadra (usa el estado de escuadra que ya mantiene el líder). **No** integres hechizos de Ars Nouveau todavía (anótalo como siguiente paso; la nigromancia sigue condicionada a la Parca de MCA Reborn).
>
> **E2-0 · Inventario (corrección de David, 2026-10-05; hazlo primero, es pequeño).** Los **6 espacios extra** actuales (además de manos y armadura) se quedan, con una regla nueva: **como máximo 1 maleta** (ítem que exponga `Capabilities.ItemHandler.ITEM` con más de 6 espacios, p. ej. Sophisticated Backpacks) y **5 espacios libres** para objetos (armas, flechas, munición, comida, tótems). La maleta guarda muchísimos ítems. El guardia **gestiona su inventario solo**: coloca lo que recoge donde corresponde (munición/comida/tótems a la maleta si hay hueco; armas útiles a los 5 libres), descarta lo inútil dejándolo en el cuartel, y busca primero en los 5 espacios y luego en la maleta. El menú ▤ muestra la maleta como espacio especial y rechaza una segunda. Migración: si una mochila guardada tiene 2+ maletas, deja una y mueve el resto a los espacios libres o al suelo del cuartel. Sin dependencia dura de Sophisticated Backpacks.
>
> **E2-H · Logística y entrenamiento en el cuartel (la base de un sistema eventual de logística).**
> 1. **Reabastecer en el cuartel:** los guardias dentro del radio del cuartel (config, por defecto 12 bloques) con la escuadra en reposo (no en combate) revisan —repartido en el bucle de 20 ticks, **un guardia por tick de la escuadra, nunca todos a la vez**— los **contenedores cercanos** (cofres, barriles, **maletas colocadas en el suelo** de Sophisticated Backpacks por `Capabilities.ItemHandler.BLOCK`, y maletas soltas como ítems en el suelo) y **toman**: comida, munición que usan, y **armas solo del mismo tipo que la suya** (misma clase: espada↔espada, arco↔arco; solo si es mejor que la actual). Respeta cupos (no vaciar el cofre: máx. N ítems por visita) y deja los sobrantes.
> 2. **Entregar el botín:** al volver, depositan en un contenedor del cuartel el **botín de guerra** que lleven (todo lo que no sea su equipo ni su munición), de modo que haya material para el futuro sistema de saqueo (que busquen mejor armadura, etc.). No equipan armaduras de ahí todavía.
> 3. **Punto de suministro temporal:** el comandante puede **designar un cofre o maleta en el terreno** (agachado + G sobre él, como el punto de retirada temporal; mismo mecanismo y caducidad que `CommanderMarks`) y la escuadra seleccionada va a abastecerse allí con las mismas reglas, una vez, y vuelve a su puesto o a seguirte. Si es una maleta en el suelo o un cofre sin nada útil, lo dice con un mensaje corto.
> 4. **Entrenamiento:** el muñeco de práctica de E2-E se coloca junto al cuartel y los guardias del cuartel en reposo lo golpean (sin gastar durabilidad ni munición) hasta rango 1, **nunca más** (ver E2-E).
> 5. Todo con tope de config (`supplyRadius`, `supplyItemsPerVisit`, `supplyIntervalTicks`), sin escanear bloques por tick (cachea los contenedores del cuartel y refréscalos cada ~5 s).
>
> **ARREGLOS DE E1:** _(David los pega aquí)_
>
> **Entrega:** un commit, docs al día, informe ≤25 líneas con la tabla de simulación.

---

## 6. Prompt E3 — Planner, uniformes, lanza y escudo

> (Bloque de §3 + **ARREGLOS** de E2.)
>
> **E3-A · Battle Planner (prioridad máxima).** Un plan = lista de **grupos** (escuadras/unidades, numerados 1–9) y, por grupo, un **objetivo/zona**, una **doctrina** y una **condición de activación** opcional (momento + condición). Doctrinas v1 (cada una es una **configuración de la escalera de decisión**, no código nuevo por doctrina — ver playbook §4): **Hostigar**, **Evitar bajas**, **Distraer**, **Ataque total**, **Defender posición**, **Emboscar** (si cabe). UI: una pestaña del Libro con la lista de grupos del plan, selector de doctrina y botones Activar/Pausar; **mantener G + número** selecciona el grupo N del plan activo (G suelto sigue siendo Ordenar). El plan se guarda por UUID del comandante y es persistente. Ejecución: el plan **solo emite directivas a líderes** (uno por grupo, una vez por cambio); el resto es la escalera de E2. Sin lista de pasos libre ni editor gráfico en v1.
>
> **E3-B · Uniformes.** Capa/tabardo **solo visual**, teñido con los patrones de la bandera del cuartel del guardia (los de su estandarte vinculado; si no hay, la bandera por defecto del jugador). Lee los patrones del componente de ítem de banner de NeoForge 1.21.1 (APIs de primer uso: anótalas). Diferencia visual mínima entre soldado y capitán.
>
> **E3-C · Lanza grande y escudo grande** (customizables con estandarte).
> - Ítems nuevos, texturas propias (no copiar de otros mods). El escudo toma patrones de banner igual que el escudo vanilla.
> - Guardia apostado y quieto con ambos: pose **escudo arriba / lanza al frente**.
> - Solo a **rango máximo**: el escudo desvía flechas y balas **no explosivas**; con él, sobrevive a explosiones grandes (reducción, nunca inmunidad). El daño de la lanza **escala con la veteranía** (curva suave, tope en la config).
> - Si la pose o el renderizado no caben en el cupo, entrega los ítems y los efectos y deja la pose en la lista “NO quedó”.
>
> **E3-D · Simulación.** Añade un escenario **E** (“Doctrina”): 20 guardias vs 20 zombis con cada doctrina. Registra en `docs/SIMULACIONES.md`.
>
> **ARREGLOS DE E2:** _(David los pega aquí)_
>
> **Entrega:** un commit, docs, informe ≤25 líneas.

---

## 7. Prompt E4 — Mundo (guerrillas v1, MCA, arreglos)

> (Bloque de §3 + **ARREGLOS** de E3.)
>
> **E4-A · Guerrillas v1 (addon).** Subproyecto en el **mismo repo**, jar aparte que depende de la API de Rally, con interruptor para apagarlo y **spawns apagados por defecto** en el primer test. Contenido v1: **banda errante nivel 1–10** con formación, posturas y rango de Rally (las líderes usan la escalera de E2; nivel → rango); botín propio incluida la **caja de munición**; **un** tipo de campamento oculto con cofre (si cabe, ver línea de corte). Fuera de v1: Nether, asaltos a aldeas, mercenarios, monturas voladoras, dimensión de hielo, jefe estratégico. Nombres de líder: adjetivo + animal/color.
>
> **E4-B · Compat MCA (suave, por reflexión, sin dependencia dura).** Detección de MCA Reborn, Crime, Conversations, Reputation, Mob Compat (solo “está cargado”). Si MCA está: personalidad y rasgos → genética de veteranía (ya diseñado en `VETERANIA.md` §1). Una frase de miedo ocasional cuando la moral baja. **No** implementes prisioneros ni nigromancia; anótalos en `BACKLOG.md` con su condición (la nigromancia solo se activa con la Parca de MCA Reborn).
>
> **E4-C · Arreglos acumulados** de E3 y de este prompt, y documentación final: `docs/ARQUITECTURA.md`, `docs/rally-map.html` (republicar), `CLAUDE.md`.
>
> **ARREGLOS DE E3:** _(David los pega aquí)_
>
> **Entrega:** un commit, informe ≤25 líneas, y la lista final de lo que quedó fuera para el backlog.

---

## 8. Mods del paquete (estado 2026-10-05)

| Mod | Estado |
|---|---|
| MCA Reborn 7.7.36-beta.3 | instalado |
| MCA: Conversations 1.8.0 | **colocado, pero exige NeoForge ≥ 21.1.234 y el pack corre 21.1.231** → actualizar el loader o usar una versión anterior |
| MCA: Crime 0.7.4, Reputation 0.6.1, Mob Compat 0.2.1 | por instalar (NeoForge 1.21.1; requieren MCA Reborn; Crime pide GeckoLib, ya está) |
| Banderas libres (editor) | no existe un mod NeoForge 1.21.1 con dibujo libre; opciones: Banner Tweaks (+ChimericLib, CurseForge), Banner Text (Modrinth) |
| Sophisticated Backpacks 3.25.41 | instalado (E1-B) |
| Magic NPCs | solo Forge 1.20.1: inspiración |
