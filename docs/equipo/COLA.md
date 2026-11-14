# Cola de fichas del equipo (creada 2026-11-11; en el repo desde 2026-11-13)

Origen: segunda opinión sobre el motor táctico (sesión Fable, 2026-11-11). Regla de reparto en `CLAUDE.md` del repo,
sección "Which model does what". El traspaso es este archivo (David permite subagentes dentro de una sesión desde 2026-11-11).

- **[FABLE]** = diagnóstico, diseño de experimento, arquitectura, auditoría. Escribe la ficha y termina; no espera la CI.
- **[EJECUTOR]** = Sonnet u Opus. Implementa la ficha tal cual, lanza lotes, resume con `tools/resumen.py`, escribe la
  línea "Veredicto" de la ficha.

Formato de ficha: hipótesis · brazos · métrica principal · n y criterio de parada · veredicto (lo llena el ejecutor).

Norma estadística para todas (**aprobada por David el 2026-11-11**: "usemos todas las corridas necesarias"): semillas pareadas entre brazos; métrica principal
continua cuando exista (vida restante del jefe, guardias-segundo vivos, tiempo hasta ganar) en vez de solo victoria
sí/no; n ≥ 30 por brazo para decisiones sobre valores por defecto; n = 5–8 solo para explorar.

## Cola

### F0 [FABLE] Auditoría de lo ya confirmado
Revisar qué filas del manual se decidieron con n < 20 (ronda 35, 40, 42, 45: n = 5–8) y cuáles dependen de la arena
(distancia 48–56 dentro de 96 bloques). Salida: lista de filas "exploratorias" a repetir con n = 30 y en arenas
`village`, `corridors`, `fort`.

### F1 [EJECUTOR] Tabla de atracción por tipo de mob
Hipótesis: casi todos los mobs cambian de blanco hacia quien les pegó último. Brazos: por cada mob del bestiario, 2
guardias a lados opuestos alternando golpes. Métrica: probabilidad de cambio de blanco tras golpe, alcance de
seguimiento (distancia a la que suelta el blanco), tiempo hasta soltarlo. n = 10 por mob. Es la base de F3, F4 y F6.
- *Ejecutor 2026-11-11:* implementada como `probe:attraction` (`debug/sim/AttractionProbe`): 2 guardias inmóviles e
  invulnerables a 8 bloques a cada lado; golpe alterno de 1 cada 2 s, se mira el blanco 1 s después; tras 10 golpes se
  alejan más allá del alcance de seguimiento y se cuentan los ticks hasta que lo suelta. Extras `attrSwitch`,
  `attrFollowRange`, `attrDropTicks`. Ronda 47, presets `t47-atraccion-vanilla1|vanilla2|grandes` (20 mobs).
- **Veredicto (ronda 47, n = 10 × 20 mobs):** ningún mob cambia hacia el último que le pegó (`attrSwitch` 0,4–0,6 con
  golpes alternos = se queda con el primero: el HurtByTargetGoal que corre no se reinicia). Sueltan al blanco 3–4
  ticks después de salir del alcance de seguimiento. Alcances: 12 (vindicador), 16 (esqueleto, araña, bruja, piglin),
  32–36 (zombi, saqueador, devastador), 40–64 (Cataclysm, blaze, enderman), 128 (dragón, Tremorzilla).
  → F3(a) ping-pong por golpes alternos queda descartado; el cebo (quien llega primero) sí retiene. Falta medir si
  cambian por cercanía o por daño grande (otra sonda).

### F2 [FABLE diagnostica, EJECUTOR corre] Caballería: ¿cuerpo o táctica?
Hecho: caballería contra 32 vindicadores 0 % donde la línea gana 100 %; no es solo un problema con armas de fuego.
Brazos (contra Pax ×16 y vindicadores ×32): (a) base; (b) los mismos 8 a pie; (c) caballos invulnerables;
(d) 8 arqueros montados, sin espadas; (e) montados y quietos a 48–56. Métricas nuevas: muertes de monturas frente a
jinetes, % del tiempo montado, acierto en movimiento frente a quieto, daño recibido por caballo frente a jinete.
Después, solo si (c) o (e) ganan: (f) círculo tangencial a 48–56; (g) caracola (mitad rodea, mitad dispara quieta,
relevo cada 5 s). Métrica: % de aciertos enemigos frente a velocidad angular del jinete.
- *Ejecutor 2026-11-11:* hallazgo en código antes de correr: `MountDriver.drive` lleva a todo jinete arquero hasta 12
  bloques del blanco y ahí para el caballo; no hay ninguna lógica de alejarse ni contra cuerpo a cuerpo ni contra armas
  de fuego (la regla `standOffVsGuns` excluye a los montados). Brazos (a)–(e) implementados más (f) `rider:kite`
  (pistolero: 48–56; cuerpo a cuerpo a menos de 14: galopar hasta 24) y (g) solo arqueros con kite, como exploración.
  Medidas nuevas: `mountDmg`, `mountDeaths`, `mountedShare`, `shotsMoving/Still`, `accMoving/Still`. Etiquetas
  `mount:invulnerable`, `rider:standoff|kite`. Ronda 46, n = 30 por brazo, presets `t46-caballeria-<enemigo>-ad|eg`.
- **Veredicto (rondas 46 y 49, n = 30):** contra armas de fuego el lastre es el caballo (inmortal 30 → 97 %); contra
  cuerpo a cuerpo es la táctica (inmortal sigue en 0 %; alejarse 0 → 83–87 %). La regla (f) "alejarse" quedó **por
  defecto** (`riderKite`): 16 Pax 47 → 100 %, 32 vindicadores 0 → 87 %, 40 zombis 73 → 100 %, esqueletos igual.
  Arqueros montados solos con la regla: 100 % con 96–98 % vivos. Siguiente: los jinetes con espada (siguen cargando);
  el círculo y la caracola ya no hacen falta para arqueros.

### F3 [FABLE especifica, EJECUTOR corre] Maniobra títere antes del planificador
Guionar a mano (sin planificador) y medir contra las reglas individuales actuales, capacidad = N de mobs con 50 % de
victorias: (a) ping-pong de fuego: dos grupos de arqueros a lados opuestos, dispara solo el grupo lejano; (b) relevo
de línea: el herido sale a comer, la reserva tapa el hueco; (c) cebo montado más arqueros quietos. Criterio: la
maniobra que suba la capacidad ≥ 25 % en dos lotes es la primera del planificador. Si ninguna gana guionada, no se
construye planificador automático.

### F4 [FABLE especifica, EJECUTOR corre] Jefes: ventanas por daño y radio
(a) Solo los K golpes más fuertes ocupan las ventanas de invulnerabilidad (K = 2–4); el resto no dispara al jefe.
(b) Anillo con separación angular; solo el cebo y K golpeadores dentro del radio de ataque. Métricas: dps del jefe
recibido frente al techo teórico (golpe máximo × 2/s), guardias-segundo dentro del radio, máximo de guardias
alcanzados por ataque (`aoeHits`), % del tiempo que el jefe camina sin atacar. Primero verificar en el código de
Cataclysm y Alex's Caves si el jefe tiene tope de daño o invulnerabilidad propia.
- *Ejecutor 2026-11-11, verificado en el código fuente* (detalle en `docs/MANUAL_DE_CAMPO.md`, "Mecánicas de los jefes"):
  - **Cataclysm tiene tope de daño POR SEGUNDO compartido** (`dps_cap` 13–20 antes de armadura) además del tope por
    golpe (20–25), y **caída por distancia del tirador** (Ignis cero a 22,5, Guardián a 18, Monstruosidad a 27,
    Harbinger a 52,5).
  - Harbinger inmune a flechas bajo la mitad de vida; Guardián inmune a flechas con casco (sobre la mitad); escudo
    frontal de Ignis.
  - Tremorzilla: flechas al 35 %, partes inmunes a mobs, regeneración hasta 10/s bajo el 20 %.
  - Luxtructosaurio: flechas al 55 %, × 0,35 durante 10 ticks tras cada golpe, se cura 12/s sin jugadores cerca.
  - Consecuencia para la ficha: la métrica "dps frente al techo teórico" debe usar `min(dps_cap, 2 × golpe máximo)`;
    el brazo (a) "K golpes más fuertes" tiene sentido sobre todo para Tremorzilla; para Cataclysm el juego es
    posición (dentro del `range_cap`, de lado contra Ignis) y cambio de rol por fase.

### F5 [EJECUTOR construye arena, FABLE diseña decisión] Laboratorio de retirada real
Arena abierta (sin muro) con refugio a distancia D (techo de 2 de alto; variante agua). Brazos: pelear, retirada
actual, dispersión, refugio, retaguardia. Barrido: razón de velocidades (zombi, vindicador, devastador, dragón) × D.
Métrica: % vivos y % fuera de contacto (ningún enemigo con ellos de blanco) a los 60 s de la orden. Salida: tabla
frontera para el motor.

- *Ejecutor 2026-11-12, primera versión (ronda 52, n = 30):* arena de 160 (`arena:N`), refugio 50 bloques detrás
  (`refuge:D`, `InstructionRunner.retreatPoint`), orden a los 10 s (`instructionAt:S`), brazos pelear / retirada que
  pelea hacia el refugio / huida sin pelear (`retreat:flee`, modo `RETREAT_TO_POINT`). Medidas `atRefuge`,
  `reachedRefuge`, `outOfContact`, `refugeSeconds`.
- **Veredicto parcial:** huir sin pelear es lo peor siempre (32 vindicadores, línea: 49 % vivos peleando, 25 % huyendo;
  jinetes 43 → 0). La retirada que pelea iguala a quedarse. Contra 3 dragones adultos nadie se salva con ninguna
  opción (0–22 %). **Casi nadie llega al refugio (10–40 %)**: en la huida pasan ~290 guardia-segundos sin blanco pero
  con 74 "alejarse del cuerpo a cuerpo" y 12 "buscar enemigo" de la escalera por pelea. Sospecha: la escalera
  (SEEK_ENEMY) y la regla de alejarse pelean contra la orden de ir al refugio. **Para Fable:** diagnóstico y diseño de
  la v2 (enemigos de tierra que no se puedan ganar: 80 vindicadores, 8 devastadores; cómo debe mandar la orden de
  huida sobre la escalera).
- *Ejecutor 2026-11-12, causa encontrada en el código:* la huida iba como orden MOVE (directiva HOLD) y la escalera de
  decisiones, sin blanco y con enemigos cerca, elige SEEK_TARGET antes que la orden. Arreglo (commit local `5047b0e`,
  **sin subir**): la huida va como FALLBACK, que crea una directiva RETREAT, la primera que la escalera obedece.
  Escenarios v2 agregados (80 vindicadores, 8 devastadores). Falta correrlo.

### F6 [FABLE] Modelo de decisión retirada + moral
Un solo estimador (tiempo hasta ganar frente a tiempo hasta perder, con tasas medidas en los últimos 10 s) leído a
tres escalas: novato = su escuadra; experimentado = su batallón; veterano = la misión. Criterio de aceptación de la
moral: huidas falsas (huir en batallas que el brazo sin moral gana) < 5 %; en batallas con < 10 % de victoria, más
vivos que sin moral.

### F7 [EJECUTOR] Valor de la decisión del jugador
Por escenario de referencia: victoria con la mejor configuración que el jugador puede elegir (composición, formación,
plan, compromiso), con la de por defecto y con la peor. Métrica: diferencia mejor − por defecto. Si es ~0, el mod
juega solo en ese escenario.

### F8 [FABLE] Coste a 1000 guardias
Con el dato 400 guardias = 12,5 ms por tick (1,1 de Rally): perfilar qué parte es Guard Villagers, búsqueda de caminos
y empujones entre entidades. Diseñar: una exploración espacial por escuadra, camino único por escuadra, metas del
cuerpo espaciadas fuera de combate, reserva quieta cuando el blanco ya tiene sus ventanas llenas.

### F2b/F2c [EJECUTOR] Supervivencia del caballo y jinetes con espada: hechos (rondas 50–51, n = 30, 2026-11-12)
Con `riderKite` el caballo casi no importa; solo contra 32 Pax (arqueros montados): vivos 65 % sin armadura, 77 % con
diamante + 50 % de vida, 85 % inmortal. Escudo frontal inútil (el caballo da la espalda al alejarse). En la mixta
mueren los jinetes con espada, no los caballos. "Golpear y salir" con espada: peor (97 → 77 % contra 32
vindicadores), descartado. Propuesta abierta para David: vínculo jinete-caballo → hasta +50 % de vida al caballo.
Pregunta para Fable: ¿qué rol tiene la caballería con espada si no puede cargar contra hordas ni contra armas?

### F9 [FABLE diseña, EJECUTOR corre] Pistoleros en terreno cerrado (nueva, 2026-11-11)
Hecho (ronda 48, n = 30): en la aldea la regla de distancia evita las muertes (arqueros 100 % vivos frente a 44 %)
pero no gana: los pistoleros quedan escondidos entre casas y la pelea se estanca. En llano y bosque gana.
Pregunta: ¿qué hace un guardia cuando no hay línea de visión larga? Ideas por medir: acercarse por cobertura de a
pares, escudos delante (paran balas), tiro alto sobre las casas (la volea sin vista), rodear.
- *Ejecutor 2026-11-12, ronda 53 (n = 30):* brazo `standoff:losRelease` (sin línea de visión la regla suelta).
  16 Pax, % victorias / % vivos: arcos 0/80 con regla → 45/60 buscando vista; línea 50/90 → 62/82. 32 Pax y Callwell:
  nadie gana de verdad en la aldea (≤ 23 %). La regla salva vidas, no gana; buscar vista gana algo a cambio de vidas.
  **Para Fable:** diseñar la táctica de terreno cerrado.

### F10 [EJECUTOR] Reconfirmación con n = 30: hechos (ronda 48, semillas 2001)
Distancia contra pistoleros: sí en llano y bosque, no en aldea (ver F9). Fuego sincronizado: no sube victorias (95
esqueletos 67 frente a 70 %); queda por defecto solo como ahorro de flechas. Retirada nueva: igual a pelear a pie,
peor a caballo (69 frente a 84 % vivos contra 1 dragón adulto); muy superior a la vieja.

### Prueba de David (1 hora)
Vídeo A/B a ciegas: dos peleas grabadas, una con la regla y otra sin ella. Si no distingues cuál es cuál en 60 s, la
regla necesita una señal visible o no existe para el jugador.

### F11 [EJECUTOR] Laboratorio de estrategias reales (David 2026-11-13; `docs/LAB_ESTRATEGIAS_REALES.md`)
- F11a supresión: arcos con Retroceso II (`ench:punch`) contra 60 zombis, 32 vindicadores, 4 devastadores; arcos y
  línea; base frente a retroceso. Métrica: % vivos. Criterio: +10 puntos de vivos sin perder victorias. Ronda 54,
  presets `t54-supresion-*`, n = 30.
- F11b pinza (Cannas): 16 guardias en unidades de 4 (`unit:4`), instrucción FLANQUEAR frente a MANTENER_LINEA y la
  base, contra 40 zombis, 32 vindicadores, 16 Pax; línea y caballería. Métrica: % victorias y % vivos. Ronda 54,
  presets `t54-pinza-*`, n = 30.
- Estado: corrido (run 43596825440, seed 5401, n = 30, commit cc101f8).
- **Veredicto (analista 2026-11-14): NO en las dos estrategias.** Datos en `tools/torneo-data/r54/`. Casi todo es techo
  (100 % victorias en las 24 celdas), así que la métrica es vivos (media de supervivientes de 16, o de 8 en supresión).
  - Supresión (Retroceso II): base → retroceso, arco 8,0 → 8,0 (60 zombis y 4 devastadores); línea 7,9 → 7,8 (zombis
    60), 7,4 → 7,8 (devastadores 4, +5 puntos de vivos). Contra 32 vindicadores: arco 7,9 → 8,0; línea 3,5 → 3,7 (+2,5 puntos). Criterio +10 puntos: **no cumple**. Los escenarios no discriminan (los arcos ya no pierden a nadie).
  - Pinza (unit:4): línea contra 32 vindicadores, vivos base 9,2 / mantener 11,6 / pinza 11,8 (±0,3): el salto
    (+16 puntos) lo da la estructura de unidades, no FLANQUEAR (pinza − mantener = +0,2, dentro del error). Caballería:
    8,0 / 8,2 / 8,0, nada. Contra 16 Pax en caballería, base 11,3 supera a pinza 9,8 y mantener 9,1. Contra 40 zombis,
    sin diferencia (16,0 en todo). Criterio (pinza > mantener y base): **no cumple**.
  - O7: 3 de 7 con veredicto (Partos sí; supresión no; pinza no). No cambia ningún valor por defecto.
  - Para Fable: si se quiere probar de verdad la supresión y la pinza, hace falta un escenario donde la base no llegue
    al techo (más enemigos o equipo peor); y `unit:4` solo, sin instrucción, merece su propia ficha (+16 puntos de
    vivos contra vindicadores en línea).

### F5 v2 [EJECUTOR] Escape contra lo que no se gana
- Presets `t52-escape-vindicadores80`, `t52-escape-devastadores8` (más los anteriores), con la huida como FALLBACK.
- Estado: corrido (run 43596851492, seed 5201, n = 30, commit cc101f8).
- **Veredicto (analista 2026-11-14): el arreglo FALLBACK no basta; O3 no se cumple → F5 v3 para Fable.**
  Datos en `tools/torneo-data/r52v2/`. % vivos al final (n = 30 por brazo; los brazos de huida tienen más filas por
  reintentos y excluyen corridas sin enfrentamiento en las tasas de victoria):

  | Escenario | brazo | pelear | retirada | huida | llegada al refugio (`atRefuge`, huida) |
  |---|---|---|---|---|---|
  | 80 vindicadores | línea | 13 | 20 | 20 | 0 % |
  | 80 vindicadores | arco | 75 | 41 | 23 | 1 % |
  | 80 vindicadores | caballería | 2 | 0 | 0 | 0 % |
  | 8 devastadores | línea | 68 | 43 | 31 | 2 % |
  | 8 devastadores | arco | 100 | 96 | 73 | 5 % |
  | 32 vindicadores | línea | 50 | 45 | 26 | 0 % |
  | 32 vindicadores | arco | 100 | 99 | 62 | 4 % |
  | 32 vindicadores | caballería | 44 | 25 | 0 | 1 % |

  - Criterio de O3 (huida/retirada > pelear por > 2,5 EE y ≥ 50 % en refugio): **no**. Solo la línea contra 80
    vindicadores muestra retirada/huida (20 %) por encima de pelear (13 %), con ~0 % en el refugio; es ruido de
    suelo, no ventaja. Contra todo lo demás pelear es igual o mejor.
  - Control 32 vindicadores: `atRefuge` ≤ 4 % (umbral 40 %) → el arreglo no bastó. `reachedRefuge` (haber tocado el
    refugio alguna vez) sí sube algo (arco-huida 42 % en 32 vind., 37 % en 80) pero no se quedan.
  - Los brazos de huida acaban casi siempre en el tope de 120 s y muchas corridas sin daño entre bandos ("sin
    enfrentamiento": línea-huida 24 de 46 con 80 vind.). Con los reintentos, parte de la huida acaba en vagar, no en
    llegar. Sin telemetría de qué hace el guardia tras tocar el refugio no puedo decir por qué sale.
  - Caballería: huir es lo peor (0 % vivos en 32 y 80 vind.).
  - **Para Fable (F5 v3):** ¿qué significa "estar a salvo" en el refugio (el sim lo cuenta cuando 'atRefuge' pero
    casi nadie permanece)? Mirar por qué salen del refugio (escalera/SEEK_ENEMY otra vez o el enemigo los sigue hasta
    allí; contra vindicadores a pie que persiguen no hay refugio posible sin techo). Probable conclusión: contra
    enemigos de tierra más rápidos o iguales, huir no gana nunca; el motor debería elegir pelear y reservar la
    retirada a dragones/jinetes.

## Orquestador 2026-11-14 (turno 3/3; los turnos 1 y 2 fallaron en la CI sin escribir nada)

Sin veredictos nuevos. Despachado al ejecutor, solo lanzar (código y presets ya en la rama desde `5047b0e`/`6352f9f`):

- **F5 v2 → O3.** Presets `t52-escape-vindicadores80,t52-escape-devastadores8,t52-escape-vindicadores32` (el último
  repite la ronda 52 con la huida como FALLBACK, para ver si el arreglo sube la llegada al refugio), `mods=""`,
  `seed=5201`, n = 30, mismas semillas en todos los brazos. Métrica principal: % vivos al final; secundaria
  `atRefuge`. Aprueba O3 si en 80 vindicadores u 8 devastadores algún brazo de huida/retirada tiene más vivos que
  `pelear` por > 2,5 EE **y** ≥ 50 % en el refugio. Si en 32 vindicadores `atRefuge` no pasa de 40 %, el arreglo no
  bastó → vuelve a Fable (F5 v3). Modelo: ejecutor.
- **F11 → O7.** Presets `t54-supresion-zombis60,t54-supresion-vindicadores32,t54-supresion-devastadores4,
  t54-pinza-zombis40,t54-pinza-vindicadores32,t54-pinza-pax16`, `mods=bestiario` si Pax lo necesita (si no, `""`),
  `seed=5401`, n = 30. Criterios: los de F11 arriba (supresión +10 puntos de vivos sin perder victorias; pinza frente
  a MANTENER y base en victorias y vivos). Cada estrategia con veredicto (sí o no) cuenta para O7. Modelo: ejecutor.
- *Corrección del mismo turno:* el ejecutor no arrancó: `claude-code-action` rechaza runs iniciados por el bot
  `github-actions` (falta `allowed_bots` en `equipo.yml`, que es de David) → issue #2. Para no perder el día, el
  orquestador lanzó él mismo los lotes con `build.yml` (los presets ya estaban subidos): F5 v2 run 43596851492 y
  F11 run 43596825440, los dos con `mods=bestiario` (igual que la ronda 52), semillas 5201 y 5401, n = 30.
  Estado F5 v2 y F11: **lanzado 2026-11-14**, veredicto pendiente (el analista no puede arrancar hasta que se
  arregle #2).


## Indicaciones de David — 2026-11-14 (para el orquestador; prioridad sobre lo anterior)

**Sobre F11.** "Ganamos el 100 %" no significa que no haya nada que medir. Rehacer el veredicto de supresión con las
métricas continuas (vivos, vida restante, tiempo, flechas; ver ANALISTA 3b) y, si sigue en el techo, en un
escenario más duro. La pinza **no** está pensada para todos los escenarios: su veredicto en un panel genérico no
cuenta como "no sirve"; se prueba en su contexto (F12, F13).

### F12 [FABLE diseña, EJECUTOR corre] Cebo y pinza contra mobs
Idea de David: un grupo de tanques (escudos) aguanta una línea y retrocede un poco; los mobs los persiguen (ronda 47:
el primero que entra se queda con el mob); otro grupo, fuera de la vista de los mobs, cierra por los flancos y aplica
superioridad numérica rápida. Hipótesis: menos bajas que el choque frontal con los mismos guardias.
Paso 1 (técnico): ¿puede la simulación dar órdenes distintas a dos grupos azules (cebo con RETIRARSE / MANTENER, flanco
oculto con FLANQUEAR o esperando y luego atacando)? Si no, esa es la primera ficha. Paso 2: brazos choque frontal /
cebo sin pinza / cebo + pinza, con 16 guardias, contra hordas que la base gana al ~50 %. Métrica: vivos y tiempo.

### F13 [FABLE diseña, EJECUTOR corre] Guardias contra guardias: pinza contra carga
Hipótesis de David: un ejército de guardias mejor equipado que solo "ataca" no siempre gana contra uno peor equipado
que lo envuelve con una pinza, guardando las proporciones. Laboratorio exploratorio (David no está seguro de que
guardias contra guardias haga avanzar el mod: medir primero si la pinza cambia algo). Brazos: hierro carga contra
cuero/cuero+hierro con pinza; y la misma pelea con ambos cargando, como control. n = 30.

### F14 [FABLE diseña, EJECUTOR corre] Ciclo mongol (atacar, irse, volver)
A nivel de grupo, no de jinete: los arqueros montados cargan a tiro, disparan unas rondas, se van fuera del alcance
de seguimiento del enemigo (tabla F1), y vuelven. Comparar con la regla actual (`riderKite`, individual). Métrica:
vivos, tiempo y bajas enemigas; en escenarios donde la base no esté en el techo.

### F5 v3 [FABLE diseña] La retirada sigue siendo prioridad
David: un jugador que empieza un mundo sobrevive a los mobs huyendo; tras una misión, el grupo puede quedar mal parado
y huir ser lo mejor; pasar de 0 a 10 % ya vale. Huir de alguien más rápido no sirve. Ideas por medir:
- huir de enemigos más lentos o con poco alcance de seguimiento (zombis, esqueletos, vindicadores 12 bloques);
- escenarios donde pelear da 0 % (grupo herido y sin comida tras una misión);
- **caballos dejados en un punto** (relevos): los guardias llegan, montan y escapan. Paso 1 técnico: ¿un guardia sabe
  montar un caballo cercano sin jinete? Si no, es la primera ficha.
- qué hace el guardia al llegar al refugio (por qué sale).

## Orquestador 2026-11-14 (turno 4/6; modelo Opus, no Fable: solo fichas técnicas de capacidad, sin diseño táctico)

Veredictos leídos: F5 v2 (O3 sin avance, intentos 1) y F11. Por indicación de David, el "no" de la pinza en panel
genérico no cuenta y la supresión se rehace con métricas continuas: O7 queda en 1 de 7 (Partos) e intentos 0.
**Pendiente para el analista:** rehacer el veredicto F11a (supresión) con vivos, vida restante, tiempo y flechas sobre
`tools/torneo-data/r54/` (ANALISTA 3b); si sigue en el techo, proponer escenario más duro. Issue #2 resuelto (900f040).
Principio técnico primero: las dos fichas de hoy son el paso 1 de F5 v3 y F12.

### F15 [EJECUTOR] F5 v3 paso 1: caballos de relevo (¿un guardia sabe montar uno libre al huir?) → O3
Hecho en código: solo `order/GuardMounts.toggle` monta, por orden del jugador, al más cercano a ≤ 12 bloques
(`FREE_RADIUS`) y sin caminar hasta él. Nadie monta solo durante una huida.
- Capacidad (solo sim, etiqueta `relay:N:D`): N caballos sin jinete, mansos y ensillados, dejados D bloques detrás de
  azul, en el camino al refugio. Con la huida (FALLBACK) activa, un guardia a pie que pase a ≤ 12 de un caballo libre
  lo monta (reusar la lógica de `GuardMounts`, sin teletransporte nuevo) y sigue la huida montado. Interruptor
  propio; fuera de la sim no cambia nada.
- Telemetría nueva (no cambia conducta): `relayMounted` (guardias que montaron), `refugeLeft` (veces que un guardia
  en el refugio sale de él) y `refugeLeaveCause:<acción de la escalera o blanco>` para saber por qué salen.
- Prueba: 8 guardias de línea contra 32 vindicadores (ronda 52 v2: pelear 50 % vivos, huida 26 %), refugio a 50,
  orden a los 10 s. Brazos: pelear / huida a pie / huida con `relay:8:20`. n = 30, semillas pareadas (seed 5501).
- Métrica principal: % vivos al final; secundarias `atRefuge`, `relayMounted`, `refugeLeaveCause`.
- Aprueba la capacidad si `relayMounted` ≥ 6 de 8 de media. El brazo cuenta para O3 si huida con relevo supera a
  pelear por > 2,5 EE. Si no monta nadie, el veredicto es "capacidad rota" y vuelve al ejecutor, no a Fable.
- Estado: **lanzado 2026-11-14** · preset `t55-relevo-vindicadores32` (`gen-relay55`, brazos pelear / huida / relevo),
  `mods=bestiario` (como la ronda 52 v2), `seed=5501`, n = 30 · commit `564c0b8` · run 43629983034. Lecturas del
  ejecutor: "huida activa" = `retreat:flee` y la orden ya dada (comprobado una vez por segundo); el guardia monta al
  caballo libre más cercano a ≤ 12 con las reglas del botón del Ledger (`GuardMounts.mountNearestFree`) y el conductor de
  monturas lo lleva a su puesto de FALLBACK; si tiene blanco, el conductor carga contra él (conducta actual, sin
  cambiar). Telemetría extra: `relayHorses`; la causa de salida es `target:<mob>` si tiene blanco, si no el último
  peldaño de la escalera. Verificar `relayHorses` = 8 antes de leer el resto.

- Veredicto (analista, 2026-11-14, ronda 55, commit 564c0b8, `tools/torneo-data/ronda55/relevo.jsonl`; n = 30 por brazo,
  semillas pareadas, más reintentos que no cambian la lectura): **capacidad aprobada, estrategia NO.**
  | brazo | n | victoria | guardias vivos (de 8) | rojos vivos | tope de tiempo |
  |---|---|---|---|---|---|
  | pelear | 30 | 100 % | 3,97 (50 %) | 0 | 0 |
  | huida a pie | 30 | 0 % | 1,93 (24 %) | 20,8 | 12/30 |
  | huida + relevo:8:20 | 30 | 0 % | 0,37 (4,6 %) | 19,0 | 0 |
  `relayHorses` = 8,0 en todas las corridas del brazo (criterio ≥ 6: cumplido). Pero la huida con relevo queda
  45 puntos por debajo de pelear (O3 pedía > +2,5 EE: NO cumple) y 19 puntos por debajo de huir a pie. `atRefuge` ≈ 0 y
  `reachedRefuge` ≈ 0,08 en relevo: montados no llegan al refugio. Causas de salida del refugio (`refugeLeaveCause`) dominadas
  por `SEEK_TARGET` y `target:vindicator`: el conductor de monturas carga contra el blanco en vez de huir (lo avisó el
  ejecutor). Hipótesis, no probada: al montar, el guardia con blanco pasa de huida a carga. Aviso: 13 corridas de relevo y
  22 de huida marcadas "sin enfrentamiento" por el resumen; la columna `outOfContact` de las filas es 0, así que
  el aviso parece venir de otro criterio y conviene que el ejecutor lo revise.
  No mueve ningún objetivo. Siguiente (decide el orquestador): variante donde el jinete ignora blancos mientras la huida esté
  activa (`relay` sin carga) antes de descartar F5 v3.

### F16 [EJECUTOR] F12 paso 1: dos grupos azules con instrucciones distintas → O7
Hecho en código: `debug/sim/SimExtras` lee una sola `instruction:NAME` para todo el lado azul; no hay forma de dar
al cebo una orden y al flanco otra.
- Capacidad (solo sim): etiqueta `unitInstr:U1=NAME@T,U2=NAME@T,...` que da a cada unidad (`unit:N`) su instrucción
  de `InstructionInterpreter` a los T s; una unidad sin entrada sigue la `instruction` general. Añadir "esperar"
  (quieta, sin disparar ni ser conducida) si no existe, para el flanco oculto. Telemetría por unidad: `u<k>Alive`,
  `u<k>FirstContactS` (primer daño dado o recibido).
- Prueba de humo (exploración, n = 8, seed 5601): 16 guardias de línea en 4 unidades contra 32 vindicadores;
  brazos (a) todos MANTENER_LINEA; (b) U1–U2 RETIRARSE@15, U3–U4 MANTENER_LINEA; (c) U1–U2 MANTENER_LINEA,
  U3–U4 esperar y FLANQUEAR@20.
- Aprueba la capacidad si en (b) y (c) los `u<k>FirstContactS` de cada par difieren de forma visible (≥ 5 s) y las
  acciones de `extra.act:*` por unidad corresponden a su instrucción. No es aún el veredicto de la pinza: con la
  capacidad lista, Fable diseña el paso 2 (n = 30, hordas que la base gana al ~50 %).
- Estado: **lanzado 2026-11-14** · preset `t56-unidades-vindicadores32` (`gen-units56`, brazos todos / retira12 /
  flanco34), `mods=""`, `seed=5601`, n = 8 · commit `de4f438` · run 43630691393. Lecturas del ejecutor: unidades de 4
  en orden de aparición (`unit:4`) con `mix:shield=2,archer=2` (cada unidad 2 escudos + 2 arqueros, misma proporción
  que `linea`); en los tres brazos la general es `instruction:MANTENER_LINEA` y el brazo (a) repite MANTENER_LINEA por
  unidad para tener la misma telemetría. ESPERAR = solo sim (no se añade al juego): apostada, sin blanco, solo
  responde a quien la golpea (el holdBack de AMBUSH). Telemetría: `u<k>FirstContactS` (-1 = nunca), `u<k>Alive`,
  `u<k>DmgDealt/DmgTaken`, `u<k>MeanEnemyDist`, `u<k>Moved`, `u<k>OrderAt:NAME` y `u<k>:act:*` por unidad.
- Veredicto (analista, 2026-11-14, ronda 56, n = 8 por brazo, seed 5601, datos `tools/torneo-data/56/`): **capacidad
  aprobada a medias**. (c) flanco34 SÍ cumple: U1-U2 contacto a 0 s; U3 a 6-8 s y U4 a 10-23 s (diferencia >= 6 s en 8/8);
  `u3/u4OrderAt:ESPERAR` y `FLANQUEAR` registrados y las acciones por unidad existen. (b) retira12 NO verificada: el
  lote termina en ~16 s, antes de los 15 s + reacción, y no hay `u1/u2OrderAt:RETIRARSE`; todos los FirstContact = 0.
  Es defecto de la prueba (RETIRARSE@15 demasiado tarde), no de la capacidad.

| brazo | n | victoria | vivos (de 16) | s | daño rojo |
|---|---|---|---|---|---|
| todos MANTENER_LINEA | 9* | 100 % | 13,1 | 14-21 | ~130 |
| retira12 (sin efecto real) | 8 | 100 % | 12,8 | 15-18 | ~150 |
| flanco34 | 8 | 100 % | 7,8 | 28-80 | ~270 |

  *n=9 en `todos` (una repetición de más). Lectura continua (3b): el flanco oculto, tal como está dado, es PEOR que la
  línea (bajas ~+5, tiempo x4): U1-U2 pelean solas contra 32 (daño recibido por unidad ~50) y U3-U4 llegan sueltas y
  reciben más (~75-110) con poco daño dado (12-40). No es veredicto de la pinza (no era el objetivo); sugiere que
  FLANQUEAR@20 con ESPERAR no coordinado no ayuda contra esta horda. No mueve ningún KPI.
- Petición al orquestador: repetir solo el brazo (b) con RETIRARSE@5 (n = 8) para cerrar la capacidad; luego paso 2 con
  hordas que la base gane ~50 %. Los tres brazos están en el techo (100 %), así que allí la métrica es vivos/tiempo.

## Orquestador 2026-11-14 (turno 5/6; modelo Opus, no Fable: solo fichas técnicas)

Veredicto leído: F16 (capacidad a medias; no mueve O7, que sigue en 1 de 7 con intentos 0). F15 sigue sin veredicto
(su lote ya terminó: run 43629983034 en verde, espera al analista). Siguen pendientes para el analista: el veredicto de
F15 y el nuevo veredicto de F11a con métricas continuas. Las dos fichas de hoy son lo que falta antes de que Fable diseñe
el paso 2 de F12; el diseño de la pinza no se toca en este turno.

### F16b [EJECUTOR] Cerrar la capacidad: retirada por unidad a tiempo → O7
- Mismo preset y montaje que F16 (16 de línea, `unit:4`, 32 vindicadores, `mods=""`), solo el brazo (b) con
  `unitInstr:U1=RETIRARSE@5,U2=RETIRARSE@5` (U3–U4 MANTENER_LINEA). n = 8, seed 5601 (las mismas semillas que la ronda 56).
- Aprueba si en ≥ 6 de 8 corridas aparecen `u1OrderAt:RETIRARSE` y `u2OrderAt:RETIRARSE`, y U1–U2 tienen
  `u<k>:act:*` de retirada y `u<k>MeanEnemyDist` mayor que U3–U4. Si la orden se registra pero no cambia ninguna
  acción, el veredicto es "capacidad rota en RETIRARSE por unidad" y vuelve al ejecutor.
- Estado: **lanzado 2026-11-14** · preset `t56b-retira-vindicadores32` (`gen-units56b`, un brazo, id
  `t56b-unidades-vindicadores32-linea-retira12`), `mods=""`, `seed=5601`, n = 8 · commit `befe90c` · run 43631592975.
  Lectura del ejecutor: ronda nueva con prefijo `t56b-` para no pisar los datos de la ronda 56; montaje idéntico al brazo
  (b) de `gen-units56` salvo `@15` -> `@5`. Sin cambios de Java.

### F17 [EJECUTOR] Calibrar la horda para el paso 2 de F12 (base cerca del 50 %) → O7
- Hecho: con 16 de línea en `unit:4` contra 32 vindicadores, la base gana el 100 % (ronda 56, 13 vivos). El paso 2
  pide hordas que la base gane alrededor del 50 %.
- Un solo brazo (el de control del paso 2): todos MANTENER_LINEA, 16 de línea, `unit:4`, `mix:shield=2,archer=2`, como
  en la ronda 56. Barrido de tamaño: 48, 64 y 80 vindicadores. n = 8 por tamaño (exploración), seed 5701.
- Métrica: % victorias y vivos; secundaria: tiempo.
- Salida: el tamaño con victorias entre 30 y 70 % (si hay dos, el más cercano a 50). Si los tres quedan fuera de ese
  rango, anotar la curva y proponer el siguiente tamaño (interpolar); no lanzar una segunda ronda sin una ficha nueva.
- Se pueden lanzar F16b y F17 en el mismo commit y con presets distintos.
