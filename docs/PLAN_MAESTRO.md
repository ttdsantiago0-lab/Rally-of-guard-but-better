# Plan maestro: cómo se construye Rally de principio a fin

Versión 1 · 2026-11-07 · reemplaza a la sección "De horas a calendario" de `docs/campana-1000-horas.html` (v1, 6-nov).
Fuente de verdad del plan. El mapa visual es `docs/campana-1000-horas.html` (v2). El tributo tiene su propio documento:
`docs/TRIBUTO.md`. La evaluación de la conversación con ChatGPT del 2026-11-07 está en §7.

Regla del documento: cada etapa tiene una **puerta** (qué tiene que quedar probado), una **alarma** (qué nos dice que
algo va mal) y un **plan B** (qué hacemos si la puerta no se pasa). Cuando algo cambia, se cambia aquí primero.

---

## 1. La meta, en una frase

Que tus amigos no puedan ignorar el mod: un jugador con guardias siempre supera a uno sin ellos, una base sin guardias
es mucho más vulnerable, y jugar sin Rally deja de ser lo mismo (BACKLOG, meta del 2026-11-07). "Terminado" =
combate y coordinación excelentes, Battle Planner con misiones, tributo que mueve la economía y un mundo con facciones
que no espera al jugador. Todo lo demás (palomas, chamanes, invocar jefes) es aspiración y entra si sobra capacidad.

## 2. Los recursos, y cuál es el freno

| Recurso | Cuánto | Para qué | ¿Es el freno? |
|---|---|---|---|
| 2 portátiles | runners de GitHub (Linux o WSL2), 1–2 servidores de simulación cada uno según RAM (≈2 GB por servidor) | barridos de simulación de noche y mientras trabajas | **No.** Con dos portátiles 12 h/día salen ~24–48 h de simulación al día. Las 1000 h caben en 1–2 meses de máquina |
| Espacio de trabajo de Claude | 2 núcleos; ~1 s por corrida corta a velocidad sprint (medido 2026-11-06) | compilar y correr rondas pequeñas dentro de cada sesión | No |
| GitHub Actions (repositorio privado) | plan gratuito: los minutos incluidos se acabaron el 2026-11-06 | compilar; ya no para simular | Sí, si no se usan los portátiles |
| Claude Pro | unas pocas sesiones largas al día, con topes | diseñar, programar, leer resultados, auditar | **Sí: es el freno principal.** Cada idea es un ciclo diseñar → programar → correr → leer → quedarse o quitar, y cada ciclo es una sesión |
| Tu hora diaria | 1 h, no más | probar en el juego, estética, dirección, decisiones | **Sí, el segundo freno.** La simulación no sabe cómo se ve ni si es divertido |

Consecuencia: el plan no se organiza por horas de máquina sino por **ciclos de Claude** y **pruebas tuyas**. Las horas de
simulación son un presupuesto de evidencia: lo que hace falta para que una decisión sea confiable, no un calendario.

### Cómo ahorrar sesiones de Claude (sin perder calidad)
1. Barridos grandes definidos de una vez: una sesión deja en cola los presets de toda una pregunta (todas las
   composiciones × amenazas × semillas); los portátiles los corren de noche; la sesión siguiente solo lee.
2. Leer `summary.md` (≤150 líneas), nunca los `runs.jsonl` enteros. `tools/torneo.py report/confirm` hace las cuentas.
3. Una pregunta por ronda, con su criterio de aceptación escrito antes de correr (el protocolo de `TORNEO_DOCTRINAS.md`).
4. Una suite de regresión (un preset por comportamiento confirmado) que corre sola tras cada cambio: así no se gasta una
   sesión en descubrir que algo se rompió.
5. Dos *skills* pequeñas en `.claude/skills/` (ronda de torneo, guía de prueba en el juego) que apunten a los
   documentos en vez de repetirlos: cada sesión arranca más rápido.

## 3. El ciclo semanal

**Cada día (tu hora):**
- 10 min: leer el parte (las filas nuevas de `docs/AVANCE.md` y la sección "Alarmas" de §5).
- 40 min: **una** prueba de la cola de pruebas en el juego (guía corta con casillas, como la Guía de Pruebas E3B).
- 10 min: anotar lo que viste con la plantilla: qué hiciste · qué esperabas · qué pasó · se ve bien/mal · decisión.

**Cada semana (uno de esos días):** revisión de puertas. Claude trae: puertas pasadas, alarmas encendidas, qué propone
cambiar del plan. Tú decides. Claude actualiza este documento y el mapa.

**Las máquinas:** los portátiles corren la cola de barridos de noche; Claude deja la cola llena al cerrar cada sesión.

**Regla de la cola de pruebas:** nunca más de 5 pruebas tuyas pendientes. Si se llena, Claude deja de añadir funciones
nuevas y dedica las sesiones a medir, auditar y pulir lo que ya hay hasta que la cola baje.

## 4. Las etapas, con su puerta

Horas = horas de simulación acumuladas (hoy ~30 h, unas 18 450 corridas). Las semanas son estimaciones con Claude Pro.

### Etapa 0 · Infraestructura (ahora, 1–2 semanas)
- Los dos portátiles como runners (`docs/SIMULACIONES.md`, "Correr en un PC propio"; una variable `RALLY_RUNNER`).
- Los mods del modpack en los portátiles (no en GitHub) para que la simulación tenga sus mobs.
- Subir NeoForge a ≥ 21.1.230 (lo piden Ice and Fire y Scorched Guns; seguimos en Minecraft 1.21.1).
- Suite de regresión: un preset por fila confirmada del Manual de campo.
- Spark en tu perfil para medir fps y tick cuando pruebas.
- **Puerta G0:** los dos portátiles corren un lote completo solos; con el NeoForge nuevo la regresión da lo mismo.
- **Plan B:** si un portátil no aguanta (calor, suspensión, RAM), uno solo + el espacio de Claude; si el NeoForge nuevo
  rompe algo de Guard Villagers, nos quedamos en 21.1.228 y dejamos Ice and Fire para después.

### Etapa 1 · Límites del guardia (30 → 150 h, 3–5 semanas)
- Bestiario del modpack: hordas, tiradores, explosivos, voladores, dragones jóvenes, jefes de Cataclysm, pistoleros
  de Scorched Guns. Formaciones por amenaza, terrenos, estructuras, caballería.
- Cada regla del manual encendida y apagada (la que no gana, se quita).
- **Puerta G1:** para cada familia de mobs: "se puede con lo que hay", "se puede con código nuevo (cuál)" o "techo".
  Y la decisión de cuánto hay que intervenir Guard Villagers (§6).
- **Tú:** un volador, un dragón joven y un jefe en el juego (¿se parece a lo que dice la simulación?).

### Etapa 2 · Amenaza y personalidad (150 → 250 h, 3–4 semanas)
- Amenaza **contextual** (tu ejemplo: un zombi vale 1, un jugador armado puede valer 10; ganarle al 1, sobrevivir y
  dañar al 10 todo lo posible). Se construye sobre lo que ya existe: `veterancy/Threat` (hoy solo da XP),
  `tactics/Situation` y `DecisionLadder`, y los rasgos de `veterancy/Trait` + `Genetics` como personalidad.
- Lógica pura en Java (sin mundo) con pruebas unitarias, y una capa fina que la conecta a los guardias.
- **Puerta G2:** dos personalidades (prudente / agresiva) eligen distinto ante la misma amenaza y **ninguna es
  catastrófica** (ninguna pierde por más de 2 errores estándar contra la base en ninguna fila). El "jugador 10" se
  prueba con un jugador falso armado (el `FakePlayer` de la simulación) y contigo en el juego.
- **Plan B:** si la personalidad solo empeora, queda como estilo visible (distancia, gritos, postura) sin cambiar
  decisiones de vida o muerte.

### Etapa 3 · Escala y mando (250 → 400 h, 4–6 semanas)
- 10, 25, 50, 100, 200, 300, 400 guardias; general, capitanes, unidades de 10; sucesión cuando cae un capitán.
- Hoy: 400 guardias peleando = 12,5 ms por tick, 1,1 ms de Rally (ronda 21). El reto es que se sigan portando bien.
- **Puerta G3:** 300 guardias de un comandante pelean con mando claro, sin atascos ni órdenes perdidas, Rally < 5 ms por
  tick y sin picos > 20 ms; lo que ganaba con 8 sigue ganando con 300.
- **Tú:** una batalla de 100–300 en tu PC: fps del cliente (la simulación no lo mide) y si el Libro sigue usable.
- **Plan B:** si el cliente no aguanta 300, el tope físico por batalla baja (100–200) y lo demás vive como población
  estratégica (§6 de `TRIBUTO.md` y facciones): el ejército es más grande que lo que se ve a la vez.

### Etapa 4 · Misiones y Battle Planner (400 → 550 h, 4–6 semanas)
- Un motor de misiones común, no una IA por misión: objetivo, condición de éxito, de fracaso y de abandono, y "volver a
  casa" al cumplirla. Se construye sobre `force/Operations`, `PlanV2` y `command/InstructionRunner`.
- Misiones de ejército y de un solo guardia (tu anotación): cargar cañones, cerrar una puerta, abrir el portón para que
  salga un dragón. Primeras misiones: defender zona, escoltar (ya hay escolta v1), interceptar, destruir/quemar una
  estructura, reconocer, recuperar un objeto.
- Contingencias que el jugador predice (bloque C): "si cae el capitán…", "si aparece un volador…".
- **Puerta G4:** una fuerza coordinada con plan gana a la misma cantidad de guardias sin plan por más de 2 errores
  estándar, en varios terrenos y semillas; cada tipo de misión se cumple ≥ 80 % en su escenario y abandona cuando debe.
- **Tú:** diriges una batalla grande con el Battle Planner (tu ejemplo: tortuga al frente, arqueros hostigando,
  caballería rodeando, contra una horda con jefes).

### Etapa 5 · Tributo (550 → 650 h, 3–5 semanas) — diseño en `docs/TRIBUTO.md`
- Corte vertical completo antes de conectar todo: aldea protegida produce → se sella en carga → se transporta → se
  registra en el mercado → se consagra un objeto → un arma consagrada gasta tributo por disparo.
- **Puerta G5:** en una campaña simulada, la producción responde a la protección (aldea defendida > aldea saqueada por
  más de 2 errores estándar); auditoría sin forma de fabricarlo ni duplicarlo (Create incluido); y **tu prueba de
  decisión**: mover tributo te obligó a decidir algo (ruta, escolta, cuánto llevar). Si no, se rediseña antes de seguir.

### Etapa 6 · Caravanas y combate fuera de pantalla (650 → 750 h, 3–5 semanas)
- La caravana es un registro (origen, destino, ruta de puntos, progreso, carga, escolta, dueño, riesgo); su posición se
  calcula con el tiempo. Se materializa cuando un jugador se acerca a su posición y vuelve a ser registro cuando se va.
  Sin tickets de chunk.
- Ventaja que nadie más tiene: **el combate fuera de pantalla se resuelve con nuestras tablas de simulación**
  (composición × amenaza → probabilidad de ganar y bajas esperadas). Las 1000 h no solo enseñan a los guardias:
  también son el motor de lo que pasa cuando nadie mira.
- **Puerta G6:** 1000 ciclos automáticos de materializar/desmaterializar sin duplicar ni perder carga; el resultado
  abstracto y el físico del mismo encuentro coinciden dentro de ±10 puntos.
- **Plan B:** si la materialización da problemas en terreno difícil, la caravana aparece solo en caminos marcados
  (estandartes de ruta) y se resuelve abstracta en el resto.

### Etapa 7 · Facciones y campaña (750 → 900 h, 2 meses o más)
- Un motor de facciones con datos (doctrina, cultura, territorio, economía, diplomacia); las guerrillas son una clase de
  facción. Contratos con duración, pago y consecuencias si se rompen. Población estratégica / desplegada / física
  separadas: una facción puede tener miles registrados y pocos cargados.
- **Puerta G7:** en campañas largas simuladas, las facciones crecen, pelean entre ellas y contra el jugador con un coste
  de servidor estable; un mundo sin jugador activo puede terminar "subyugado" por una facción (tu ejemplo del granjero).
- **Plan B:** facciones más simples (3–4 plantillas fijas) si el motor configurable se come las sesiones.

### Etapa 8 · Memoria, rumores y cultura (900 → 1000 h, en paralelo con 7)
- Memoria por acontecimientos (batallas, muertes de compañeros, ascensos, rescates), nunca golpe a golpe: chismes de
  MCA, bitácoras, títulos. Señales y grafitis como acontecimientos puntuales. Rumores que viajan por caravanas,
  mensajeros o aldeas conectadas, no omniscientes.
- **Puerta G8:** cero coste medible en el tick (se escribe solo en acontecimientos); tú lo encuentras inmersivo.
- Experimentales, solo si sobra: palomas mensajeras, chamanes nigromantes avanzados, invocar un jefe de Cataclysm.

**Siempre, fuera de la simulación:** estética (texturas, modelo del escudo romano, uniformes, interfaz) — tu dirección,
casi sin horas de máquina.

## 5. Alarmas: cómo nos enteramos de que algo cambió

Cada `summary.md` y cada parte las revisa. Una alarma encendida se trata antes que cualquier función nueva.

| Alarma | Umbral | Reacción |
|---|---|---|
| Regresión | una fila confirmada del manual cae > 2 errores estándar | se arregla o se revierte el cambio que la rompió |
| Coste de Rally | > 5 ms medio o picos > 20 ms por tick | se perfila (`Perf` por sistema) antes de seguir |
| Corridas atascadas | > 10 % de un lote | se revisa la arena o el comportamiento |
| Actualización de Guard Villagers o NeoForge | cualquiera | los mixins con `require = 1` hacen fallar el arranque a propósito; la CI lo detecta; seguimos en 2.4.11 hasta adaptar |
| Dependencia nueva de un mod | p. ej. Ice and Fire pide otro NeoForge | se decide en la revisión semanal |
| Presupuesto de sesiones de Claude | dos semanas seguidas topando el límite | se recorta alcance de la etapa o se sube de plan (§8) |
| Cola de pruebas tuyas | > 5 pendientes | se congelan funciones nuevas |
| Puerta no pasada | tras 2 rondas de arreglo | plan B de la etapa |

## 6. ¿Hasta dónde con Guard Villagers?

Guard Villagers es el cuerpo de nuestros guardias (modelo, inventario, contratar, seguir). Su código es MIT; sus
texturas y modelos, todos los derechos reservados (leído en su jar). Escalera de intervención, de menos a más:

| Nivel | Qué es | Estado |
|---|---|---|
| N0 | Su configuración (`GuardVillagersConfigPatcher`) | hecho |
| N1 | Mixins pequeños que solo **añaden** un sí (facciones, creepers, escudo arriba, héroe de la aldea) | hecho |
| N2 | Objetivos (goals) de Rally al lado de los suyos: disparo, escolta, tortuga, comer, manual de campo | hecho; aquí vive casi todo |
| N3 | Rally toma el mando: para guardias con dueño se quitan los goals de combate de Guard Villagers y decide el núcleo de Rally; GV sigue como cuerpo | **se decide en la puerta G1** |
| N4 | Entidad propia (soldado de Rally) | último recurso |

- Pasamos a **N3** si en la etapa 1 dos o más familias del bestiario chocan con un techo causado por los goals de
  Guard Villagers peleando contra los nuestros (lo vimos con el escudo: lo bajaba sin blanco).
- Pasamos a **N4** solo si Guard Villagers deja de mantenerse o una versión nueva lo hace incompatible y N3 no basta.
  Coste: arte propio (sus texturas no se pueden copiar), migrar los guardias existentes y perder la compatibilidad
  gratuita que trae (MCA, otros mods). Su código MIT sí se puede estudiar y adaptar con atribución.
- Regla que no cambia: nunca usar `github.com/ShackleSailorTrek/guard-villagers` (troyanizado).

## 7. Evaluación de la conversación con ChatGPT (2026-11-07)

Tus decisiones van a `TRIBUTO.md` y a las etapas. Las propuestas de ChatGPT, contra lo que hay en el repositorio:

| Propuesta | Veredicto | Por qué |
|---|---|---|
| Cortes verticales (una función completa de punta a punta) | **Aceptada** | Es como ya trabajamos (escolta, tortuga) |
| Separar la lógica pura de Minecraft | **Adaptada** | Solo para lo nuevo (amenaza, economía, caravanas, facciones). No se reescriben 28 600 líneas que funcionan |
| Amenaza en cuatro capas (percepción, amenaza, acción, personalidad) | **Adaptada** | Ya existen `Threat`, `Situation`, `DecisionLadder` (con escalones de misión y moral) y rasgos genéticos. Se amplía; no es un motor nuevo |
| Simulaciones → parámetros deterministas y versionados | **Aceptada** | Es el Manual de campo; falta pasarlo a un archivo de datos con el número de ronda que lo respalda |
| Motor de misiones común con éxito/fracaso/abandono | **Aceptada** | Etapa 4, sobre `Operations`/`PlanV2`/`InstructionRunner` |
| Tributo en 4 estados | **Adaptada** | 2 estados físicos + 1 registro (ver `TRIBUTO.md`); ChatGPT mismo sugería empezar con dos |
| Subunidades enteras para gastar fracciones por disparo | **Aceptada** | Evita errores de redondeo |
| Caravanas abstractas sin cargar chunks | **Aceptada y ampliada** | Con el combate fuera de pantalla calibrado con nuestras tablas. Ojo: la arena de simulación sí fuerza chunks; el juego no lo hará |
| Población estratégica / desplegada / física | **Aceptada** | Encaja con la medición de 400 guardias |
| Motor común de facciones, culturas por datos | **Aceptada, para la etapa 7** | Hoy `Factions` es el UUID del dueño; se amplía a registros de facción |
| Memoria por acontecimientos, MCA, títulos | **Aceptada** | Etapa 8; investigar la API de chismes de MCA |
| Rumores sobre MCA Crime / Reputation | **Investigar** | Ambos están en tu modpack; no sabemos aún qué exponen |
| Sistema genérico de tareas para otros oficios | **Aceptada como interfaz** | Escolta, patrulla, exploración, transporte primero; minería, construcción y aviación solo por integración |
| SmartBrainLib | **Rechazada para los guardias de GV** | GV usa objetivos (goals); SmartBrainLib usa el sistema de cerebro. Mezclarlos en la misma entidad es riesgo alto. Se reconsidera solo en N4 |
| NeoForge GameTests | **Adaptada** | Para mecánicas exactas (tributo, duplicados de caravana, recetas). Las estadísticas de batalla siguen en `SimRunner` |
| Spark | **Aceptada** | En tu perfil, para tus pruebas (fps y tick); no está en el modpack |
| Context7 | **No por ahora** | Leemos los jars directamente (así encontramos lo del escudo); poco valor extra |
| "Minecraft Agent Skills" de terceros | **Rechazada salvo revisión** | Origen desconocido; ya tuvimos un repositorio troyanizado |
| `.claude/skills/` propias | **Aceptada, mínima** | Dos skills que apunten a los documentos (§2) para ahorrar sesiones |
| Su orden de prioridades | **Adaptado** | Igual en espíritu; amenaza/personalidad va antes de escala porque cambia cómo se portan 300 |
| No cambiar de versión de Minecraft ni de loader | **Aceptada** | Solo un parche de NeoForge dentro de 1.21.1, justificado por Ice and Fire y Scorched Guns |
| No fijar números de balance sin simular | **Aceptada** | 7 por stack y 50 por guardia son hipótesis de prueba |

Contradicciones encontradas y cómo quedan:
- "La veteranía solo cambia decisiones" vs. "estadísticas semi rotas por maestría": resuelto el 2026-11-06 (las cifras
  únicas viven en el objeto consagrado con tributo, no en el guardia).
- Mochilas de 200–300 tributos vs. Sophisticated Backpacks: con sus mejoras de stack una mochila guardaría miles.
  Propuesta en `TRIBUTO.md` §3 (pendiente de tu decisión).
- "Sin chunks cargados" vs. marchas: las caravanas pueden ser abstractas; un ejército **físico** que marcha lejos del
  jugador se congela igual. Las marchas lejanas serán abstractas o con el jugador.

## 8. Dinero: qué compra tiempo de verdad

Ordenado por lo que acelera por cada dólar. Nada de esto es necesario para empezar.

| Opción | Coste | Qué acelera | Cuándo |
|---|---|---|---|
| Claude Max 5× | 100 USD/mes | El freno principal: más ciclos por día. Esperable ~1,5–2× más rápido (no 5×: tu hora y la estética no cambian) | cuando la puerta G0 esté pasada y veas avance semanal |
| Repositorio público | 0 USD | Actions gratis en máquinas de GitHub para repos públicos (compilar y simular sin portátiles) | si no te importa que el código sea visible; Guard Villagers no se sube nunca (lo descarga la CI) |
| Pago por uso en Actions con tope | p. ej. 10–20 USD/mes | Minutos de GitHub cuando los portátiles están apagados | si los portátiles no dan |
| Más RAM en un portátil (16 GB) | ~30–60 USD | 2 servidores en vez de 1 | si la RAM limita |
| Arte por encargo (texturas, modelo del escudo, uniformes) | variable | Lo que la simulación no hace | etapas 3–5 |

No recomendado: máquinas en la nube (los portátiles bastan) ni la API de pago por token para este trabajo (sale más
caro que Max con este patrón de uso).

Aviso de precios: GitHub anunció un cobro por minuto también para runners propios en repositorios privados y luego lo
pospuso; antes de depender de los portátiles con el repositorio privado hay que mirar la página de facturación.

## 9. Calendario honesto

| Hito | Con Claude Pro | Con Max 5× |
|---|---|---|
| G0–G1: límites del guardia conocidos | 1–1,5 meses | ~1 mes |
| G2–G4: combate, escala y Battle Planner (el mod "que no se puede ignorar") | 4–6 meses | 3–4 meses |
| G5–G6: tributo y caravanas | +2–3 meses | +1,5–2 meses |
| G7–G8: facciones, campaña, memoria | +3–5 meses | +2–3 meses |

Son estimaciones, no promesas; se corrigen en cada revisión semanal con el avance real.

Qué parte de la visión creo alcanzable con esta arquitectura y estos recursos:
- **Muy probable:** combate y coordinación excelentes contra el bestiario, misiones y Battle Planner, escala de 100–300
  guardias en una batalla, tributo con consagración y consumo.
- **Probable:** caravanas abstractas con encuentros físicos, facciones que crecen y conquistan en versión simplificada.
- **Posible, con recorte:** culturas con unidades especiales, rumores que viajan, contratos con traiciones.
- **No en física:** miles de guardias peleando a la vez en el mismo lugar. Sí miles registrados en una campaña.
- **Experimental:** palomas, invocar jefes de Cataclysm, oficios de aviación o minería propios (llegan por
  integración con otros mods si llegan).

## 10. Las cinco tareas siguientes

1. Etapa 0: los dos portátiles como runners (te dejo los pasos en tu cola de pruebas: es tu hora de mañana).
2. Suite de regresión con las filas confirmadas del manual y subir NeoForge a ≥ 21.1.230.
3. Las dos skills de `.claude/skills/` (ronda de torneo, guía de prueba).
4. Arrancar la etapa 1 con el bestiario vanilla restante y los primeros mobs del modpack.
5. Prototipo de amenaza contextual en Java puro (etapa 2), sin conectarlo todavía.
