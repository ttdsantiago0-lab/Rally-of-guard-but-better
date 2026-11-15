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

- **F11a, veredicto con métricas continuas (analista 2026-11-15, mismos datos r54, n = 30-31 por celda, 100 % victorias en las 12 celdas).** Retroceso II frente a base, media ± error típico:

  | Celda | vivos de 8 | vida restante | segundos | daño recibido |
  |---|---|---|---|---|
  | zombis60 arco | 8,0 / 8,0 | 160 / 160 | 29,8±0,1 → 28,8±0,2 (−3 %) | 0 / 0 |
  | zombis60 línea | 7,93 → 7,77 | 136 / 136 | 38,5±0,4 → 37,4±0,5 | 56 → 55 |
  | devastadores4 arco | 8,0 / 8,0 | 160 / 160 | 15,1 → 14,8 | 0 / 0 |
  | devastadores4 línea | 7,43 → 7,77 | 140±2 → 144±2 | 18,3±0,3 → 17,3±0,3 (−5 %) | 30 → 23 |
  | vindicadores32 arco | 7,94 → 8,0 | 158 → 160 | 21,7±0,5 → 25,5±0,7 (+18 %, peor) | 11 → 14 |
  | vindicadores32 línea | 3,50 → 3,73 | 69±2 → 73±2 | 35,7±0,8 → 37,0±0,7 | 122 → 125 |

  - El arco está en el techo también en métricas continuas (0 de daño recibido contra zombis y devastadores): escenario sin
    discriminación para el arco.
  - Donde la base sufre (línea): el único efecto que supera ~2 errores típicos es devastadores4 línea (+4 de vida, −1 s,
    −7 de daño: mejora real pero pequeña, ~+2,5 puntos de vida). En vindicadores32 línea la diferencia (+0,23 vivos,
    +4 de vida) está dentro del ruido y el daño recibido no baja.
  - Contra vindicadores el arco con retroceso tarda más (+3,8 s) y gasta más flechas (125 → 138): empujarlos los aleja.
  - Criterio (+10 puntos de vivos sin perder victorias): **no cumple** (máximo +4 puntos, devastadores línea). Sigue en
    NO; ya no por techo sino porque el efecto medido es pequeño. No cambia valores por defecto ni objetivos (O7 sigue 1 de 7
    por indicación de David; supresión: NO con métricas continuas).
  - Para el orquestador: no hace falta repetir F11a. Si se quiere, el escenario donde la línea sufre de verdad
    (vindicadores32 línea, base 44 % vivos) ya existe y Retroceso II no lo mueve.

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
- Veredicto (analista, 2026-11-14, lote `20261114-192328-t56b-retira-vindicadores32`, commit `befe90c`): **capacidad rota
  en RETIRARSE por unidad** (no cumple el criterio). n = 8 (+1 corrida extra, no contada); victoria 100 %, vivos 11,5/16
  (72 %), 17 s. La orden SÍ se registra: `u1OrderAt:RETIRARSE` = `u2OrderAt:RETIRARSE` = 5 s en 8/8. Pero no cambia
  ninguna acción: ni U1 ni U2 tienen claves `act:*` de retirada (solo kite/shot/sec*, igual que U3-U4; kite medio 3,9 y
  3,6 contra 3,5 y 3,4), y `MeanEnemyDist` de U1-U2 (5,7 y 5,4) NO es mayor que el de U3-U4 (7,1 y 7,9); solo 2 de 8
  corridas tienen U1+U2 más lejos que U3+U4. Las unidades "en retirada" siguen peleando a distancia media menor.
  Con el mismo resultado que F16 (@15): la orden se anota pero el intérprete/planificador no la ejecuta por unidad.
  Motivos de decisión del lote: INSTRUCTION 71, LOW_HEALTH_RETREAT 18, UNIT_BROKEN 4 (retiradas por vida baja, no por la
  orden). No mueve ningún KPI (O7 sigue en 1 de 7). Vuelve al ejecutor: revisar en el código por qué
  `InstructionRunner` no convierte RETIRARSE por unidad en movimiento (¿el 100 % de victoria cierra la regla por
  histéresis, o la unidad sin amenaza propia cancela la retirada?) y añadir una clave `act:retreat` para verlo.

### F17 [EJECUTOR] Calibrar la horda para el paso 2 de F12 (base cerca del 50 %) → O7
- Hecho: con 16 de línea en `unit:4` contra 32 vindicadores, la base gana el 100 % (ronda 56, 13 vivos). El paso 2
  pide hordas que la base gane alrededor del 50 %.
- Un solo brazo (el de control del paso 2): todos MANTENER_LINEA, 16 de línea, `unit:4`, `mix:shield=2,archer=2`, como
  en la ronda 56. Barrido de tamaño: 48, 64 y 80 vindicadores. n = 8 por tamaño (exploración), seed 5701.
- Métrica: % victorias y vivos; secundaria: tiempo.
- Salida: el tamaño con victorias entre 30 y 70 % (si hay dos, el más cercano a 50). Si los tres quedan fuera de ese
  rango, anotar la curva y proponer el siguiente tamaño (interpolar); no lanzar una segunda ronda sin una ficha nueva.
- Se pueden lanzar F16b y F17 en el mismo commit y con presets distintos.
- Estado: **lanzado 2026-11-14** · preset `t57-horda-vindicadores` (`gen-horde57`, ids
  `t57-horda-vindicadores{48,64,80}-linea-todos`), `mods=""`, `seed=5701`, n = 8 por tamaño · commit `a131236` · run
  43631765667. Lectura del ejecutor: montaje idéntico al brazo (a) `todos` de `gen-units56` (distancia 30, arena 160,
  2 min) salvo el número de rojos; se mantienen los 2 min de la ronda 56 (un empate por tiempo cuenta como no victoria
  y se anota aparte). Sin cambios de Java.
- Veredicto (analista, 2026-11-14, lote `20261114-192512-t57-horda-vindicadores`, commit `a131236`; n = 8 por tamaño,
  seed 5701; 26 filas: 1 repetición por `stuck` en 64 y 1 fila extra en 48, no contadas):

  | vindicadores | n | victoria | vivos | segundos | empates por tiempo |
  |---|---|---|---|---|---|
  | 48 | 8 | 100 % | 47 % | 36 | 0 |
  | 64 | 8 (+1 repetida) | 62,5 % (5/8; 67 % con la repetida) | 46 % | 97 | 3 |
  | 80 | 8 | 62,5 % (5/8) | 33 % | 109 | 3 |

  Cumple el criterio parcialmente: 64 y 80 caen en 30-70 %, pero con el mismo % y 80 con mucha más presión (vivos 33 %,
  1 corrida con 2/16 vivos y 39 rojos vivos). Tamaño propuesto para el paso 2 de F12: **80** (el más difícil dentro del
  rango; 64 queda como segundo). Matiz importante: **la base nunca pierde** (0 derrotas en 24 corridas); todo lo que no
  es victoria es un empate por llegar a los 120 s con azules vivos (3 en 64, 3 en 80), y 4 de los 6 conservan ≥ 4
  azules. Por eso "50 %" aquí mide lentitud (97-109 s de media), no mortalidad. Para que el paso 2 discrimine, las
  métricas buenas son vivos, vida restante y segundos, no el % de victoria; si el orquestador quiere un 50 % real de
  derrotas hace falta más de 80 o un límite fijo de tiempo distinto (no cambiar sin ficha). n = 8 es exploración: no
  se fija ningún valor por defecto. No mueve ningún KPI de `OBJETIVOS.md`.

## Orquestador 2026-11-14 (turno 6/6; modelo Opus, no Fable: solo fichas técnicas)

Veredicto leído: F15 (capacidad de montar aprobada, 8/8; la huida con relevo da 4,6 % de vivos frente al 50 % de
pelear) → O3 sin avance, intentos 2 (uno más y queda estancado). F16b y F17 siguen despachadas, sin veredicto. Siguen
pendientes para el analista: F11a con métricas continuas. Una sola ficha hoy: el defecto técnico que señala F15, antes
de dar la estrategia del relevo por perdida.

### F15b [EJECUTOR] Relevo sin carga: el jinete que huye no persigue blancos → O3
- Hecho (ronda 55): al montar durante `retreat:flee`, el conductor de monturas carga contra el blanco del guardia
  (`refugeLeaveCause` dominado por `SEEK_TARGET` y `target:vindicator`; `reachedRefuge` ≈ 0,08).
- Cambio (solo sim, con el interruptor del relevo): mientras la huida esté activa y la orden dada, un guardia montado
  por relevo suelta el blanco (`setTarget(null)`, por la puerta normal) y el conductor lo lleva a su puesto de FALLBACK
  sin cargar. Fuera de la sim no cambia nada. Una sola cosa cambia respecto a F15.
- Revisar de paso el aviso "sin enfrentamiento" del resumen en las corridas de huida/relevo (la columna `outOfContact`
  es 0): anotar qué criterio lo dispara; no cambiar la conducta por eso.
- Prueba: mismo preset que F15 (`t55-relevo-vindicadores32`: 8 de línea contra 32 vindicadores, refugio a 50, orden a
  los 10 s, `mods=bestiario`), brazos pelear / huida a pie / relevo:8:20 sin carga. n = 30, seed 5501 (las mismas
  semillas que la ronda 55).
- Métrica principal: % vivos al final; secundarias `atRefuge`, `reachedRefuge`, `relayMounted`, `refugeLeaveCause`.
- Aprueba la capacidad si `relayMounted` ≥ 6 y `reachedRefuge` ≥ 50 % en el brazo de relevo. Cuenta para O3 si el
  relevo supera a pelear en vivos por > 2,5 EE y ≥ 50 % en el refugio. Si los jinetes llegan al refugio pero mueren
  igual (vivos ≤ huida a pie), el veredicto es "el relevo no salva contra vindicadores" → O3 estancado y a la lista
  de David con F5 v3.
- Estado: **lanzado 2026-11-14** · preset `t55b-relevo-vindicadores32` (`gen-relay55b`, brazos pelear / huida /
  `relevonocarga`), `mods=bestiario`, `seed=5501`, n = 30 · commit `b41cb52` · run 43632029511. Lecturas del ejecutor:
  interruptor propio `relayNoCharge` (con `relay:8:20`); en `MountDriver.drive`, si el guardia montó un caballo de relevo,
  sigue montado, `retreat:flee` y la orden ya dada, `setTarget(null)` antes de decidir, así que el conductor va al puesto
  de FALLBACK (rama del puesto). Se repite en cada paso del conductor, porque la puerta de combate sigue dejando la
  defensa propia (`lastHurtByMob`) y el guardia podría volver a fijar blanco. Telemetría nueva: `relayDropTarget` (veces
  que se soltó un blanco). Prefijo `t55b-` para no pisar la ronda 55.
  Aviso "sin enfrentamiento": lo pone `SimRunner` (línea ~346) cuando pasan `simulation.stuckSeconds` (45 s por defecto)
  sin daño entre azul y rojo después de formar; la corrida termina antes de tiempo, se marca `stuck` y se repite UNA vez
  con semilla + 7777 (la fila original queda con la nota). No tiene relación con `outOfContact` (que mide, al final,
  cuántos azules vivos no son blanco de ningún rojo). En la huida es esperable: si los que quedan escapan o se esconden,
  45 s sin daño cortan la corrida. Ojo para el analista: los reintentos usan otras semillas (no pareadas); leer las
  corridas con la semilla original. Conducta sin cambios.

- **Veredicto F15b (analista, ronda t55b, commit b41cb52, semilla 5501, n=30 originales por brazo; los reintentos con
  otra semilla se excluyeron):**

  | brazo | n | victoria | vivos | vida restante (media) | s | reachedRefuge | relayMounted |
  |---|---|---|---|---|---|---|---|
  | pelear | 30 | 100 % | 50,0 % | 79,6 | 37 | 0,11 | - |
  | huida a pie | 30 | 0 % | 27,1 % | 43,1 | 92 | 0,17 | - |
  | relevo sin carga | 30 | 0 % | 4,6 % | 7,3 | 41 | 0,04 | 3,6 |

  `relayDropTarget` ≈ 9,7 por corrida: el blanco sí se soltó. Aun así el relevo monta solo 3,6 de 8 (< 6) y llega al
  refugio el 4 % (< 50 %). Es el mismo 4,6 % de F15: quitar la carga no cambió nada, los vivos siguen muy por debajo de
  pelear (-45 EE) y de la huida a pie. **No cumple ni la capacidad ni O3.** Veredicto: "el relevo no salva contra
  vindicadores" → O3 estancado (3 intentos) y a la lista de David junto con F5 v3. Nota: "sin enfrentamiento" cortó
  26/46 corridas de huida y 13/40 de relevo; los reintentos no son pareados. Sin cambio de defecto; regresión no
  necesaria (no hay valor por defecto).

## Orquestador 2026-11-15 (turno 1/6; modelo Opus, no Fable: solo fichas técnicas)

Veredictos leídos: F16b (RETIRARSE por unidad se anota pero no mueve a nadie), F17 (horda de 80 para el paso 2 de
F12; la base nunca pierde, medir vivos/vida/segundos) y F15b (relevo sin carga no salva). **O3 estancado** (3 intentos:
F5 v2, F15, F15b; mejor cifra: pelear 50 % vivos, huida a pie 27 %, relevo 4,6 %) → a la lista del próximo issue de
David junto con F5 v3; no abre issue solo (1 estancado de 7). O7 sigue en 1 de 7, intentos 0 (F16b/F17 son capacidad,
no estrategias medidas). Una sola ficha: RETIRARSE por unidad es el cuello de botella de F12 paso 2 (cebo que
retrocede) y de F14 (ciclo mongol = `U1=HOSTIGAR@0,U1=RETIRARSE@T,U1=HOSTIGAR@T2`; `unitInstr` ya admite varias
entradas por unidad). Se despacha además al analista el pendiente F11a (supresión con métricas continuas, ANALISTA 3b).

### F16c [EJECUTOR] Arreglar RETIRARSE por unidad (capacidad) → O7
- Hecho (F16 y F16b): `u1/u2OrderAt:RETIRARSE` se registra en 8/8, pero U1-U2 no tienen `act:*` de retirada y su
  `MeanEnemyDist` (5,5) es menor que la de U3-U4 (7,5). Motivos del lote: INSTRUCTION 71, LOW_HEALTH_RETREAT 18.
- Tarea: leer `debug/sim/SimExtras.unitTick` (rama no-ESPERAR) y `force/InstructionRunner`/`InstructionInterpreter`
  para RETIRARSE (incluida la celda nueva de "retirada combatiente" y `simFlee`/`simOldRetreat`) y encontrar por qué la
  orden por unidad no produce movimiento: ¿la general MANTENER_LINEA de las otras unidades la pisa cada ciclo?, ¿la
  retirada se cierra al no haber amenaza "propia" de la unidad?, ¿se aplica a la escuadra entera y no a la unidad?
  Anotar la causa en esta ficha. Arreglo mínimo, **solo en la ruta sim de `unitInstr`** salvo que la causa esté en
  código común y el arreglo no cambie nada fuera de las unidades (decirlo explícitamente). Añadir `u<k>:act:retreat`
  (pasos de retirada por unidad).
- Prueba: el mismo preset `t56b-retira-vindicadores32` (o `t56c-` si hay que regenerarlo), `mods=""`, seed 5601, n = 8.
- Aprueba si en ≥ 6 de 8 corridas U1-U2 tienen `u<k>:act:retreat` > 0 y `MeanEnemyDist` de U1+U2 mayor que U3+U4.
  Si no, veredicto "sigue rota" con la causa encontrada, y vuelve al orquestador. Regresión: `python3 tools/regresion.py
  check` si se tocó código común. Modelo: ejecutor.
- Causa (ejecutor, 2026-11-15, leída en código): la orden por unidad SÍ arranca su `Unit` (ni la general la pisa ni se
  cierra sola), pero `RETREAT_FIGHTING` da sus saltos con `UnitOrders.Kind.MOVE`, y `kindFor(MOVE)` = directiva HOLD. En
  `DecisionLadder` un guardia con blanco solo vuelve a su casilla (rung 3) con rango ≥ 2 o rasgos; la sim usa rango 1,
  así que con vindicadores encima la casilla nueva se ignora y la unidad pelea donde está. Es la misma retirada
  combatiente del juego (también afecta a RETIRARSE general); no se toca fuera de la sim.
- Arreglo (solo ruta sim de `unitInstr`): `InstructionRunner.SIM_REAL_BOUNDS` (vacío en el juego; lo llena
  `SimExtras.unitTick` con la unidad que recibe RETIRARSE propio, se vacía al empezar y al terminar cada corrida): sus
  saltos pasan a FALLBACK (directiva RETREAT, que la escalera obedece primero); la fase de aguante sigue en HOLD y
  pelea. Nueva clave `act:retreat` (+1 por guardia en cada salto emitido por RETREAT_FIGHTING; solo telemetría,
  no-op fuera de la sim) -> `u<k>:act:retreat`. Se tocó `InstructionRunner` (código común) sin cambio de conducta fuera
  de las unidades sim; por si acaso va el lote `regresion`.
- Estado: **lanzado 2026-11-15** · presets `t56b-retira-vindicadores32` (mismo preset, sin regenerar) + `regresion`,
  `mods=""`, `seed=5601`, n = 8 · commit `46d9869` · run 43669899409.
- Veredicto (analista, 2026-11-15, lote `20261115-025646-t56b-retira-vindicadores32`, commit `46d9869`): **sigue rota**
  (no cumple el criterio). n = 8 (+1 extra, no contada): victoria 100 %, vivos 72 %, 16 s. `u<k>:act:retreat` > 0 solo en
  U2 y en 3 de 8 corridas (4 pasos cada una); U1 nunca (0 de 8); U3-U4 0. Criterio pedía U1 y U2 > 0 en >= 6 de 8: 0 de 8.
  `MeanEnemyDist` medio U1+U2 = 9,8 contra U3+U4 = 11,4: U1-U2 NO quedan más lejos (en la mayoría de corridas están
  igual o más cerca). El arreglo FALLBACK produce algo de retirada en U2 pero casi no hay efecto; causa residual no
  mostrada por los datos (¿el salto de retirada se emite pocas veces o se cancela al pasar a la fase de aguante?).
  Regresión: `python3 tools/regresion.py check` -> "Ninguna fila empeora más que el ruido" (verde). No mueve ningún KPI
  (O7 sigue 1 de 7). Vuelve al orquestador: F16c no cierra; decidir si seguir con RETIRARSE por unidad (2.º intento,
  ver por qué U1 no emite saltos) o dejar F12 paso 2 con otro cebo.

## Orquestador 2026-11-15 (turno 2/6; modelo Opus, no Fable: solo fichas técnicas)

Veredicto nuevo: F11a con métricas continuas → supresión **NO** (efecto máximo +4 puntos, devastadores línea; el arco
sigue en techo). O7 sin cambio (1 de 7 por indicación de David), intentos 0: F16c (capacidad) sigue en curso, sin
veredicto. O3 sigue estancado y en la lista del próximo issue (1 de 7 estancados: no se abre issue). Para alternar
objetivos, una ficha técnica de diagnóstico en O6 (intentos 0, nunca trabajado desde que existe el equipo), que da a
Fable los datos que F4 pedía antes de diseñar posición contra Ignis.

### F18 [EJECUTOR] Ignis: ¿dónde se pierde el daño? (telemetría, sin cambiar reglas) → O6
- Hecho (F4, código de Cataclysm): Ignis anula el daño de proyectil a más de 22,5 bloques del tirador, tiene escudo
  frontal y tope de daño por segundo compartido. Hoy 8 guardias netherita+ = 44 % vivos; no sabemos qué parte del
  esfuerzo azul se pierde en cada uno de esos tres sumideros.
- Tarea: en `debug/sim/SimCombatStats`, solo en la sim y solo cuando el rojo sea Ignis (o cualquier jefe de Cataclysm,
  por id), contar por corrida: `ignis:far` (impactos de proyectil desde > 22,5 bloques), `ignis:shield` (golpes
  bloqueados por el escudo frontal: evento de daño cancelado o reducido a 0 con el atacante en el cono frontal),
  `ignis:capped` (daño recortado por el tope por segundo: daño propuesto − daño aplicado, si se puede leer del evento
  pre/post), `ignis:dmgIn` (daño aplicado total), y el reparto de golpes por tipo de arma (`ignis:hits:melee|bow|gun`).
  Verificar en el código fuente de Cataclysm 1.21.1 los nombres y el orden de los eventos antes de usarlos (regla de
  CLAUDE.md); si una medida no se puede obtener sin mixin, decirlo y omitirla.
- Prueba: el escenario que dio el 44 % (8 guardias `tier:netherite+` contra Ignis, `off:morale`; buscar el preset
  `t30-gigante-ignis-*`/`t28b-jefe-ignis-g8` que corresponde; si no existe exacto, `t58-ignis-g8-telemetria`),
  `mods` como en la ronda 30, seed 5801, n = 8 (exploración; no cambia ningún valor por defecto).
- Aprueba (capacidad) si en ≥ 6 de 8 corridas las medidas salen no nulas y coherentes (`far+shield+capped` ≤ golpes
  totales). Veredicto del analista: tabla de % del esfuerzo perdido por sumidero y % vivos; el mayor sumidero es la
  primera ficha de diseño de Fable para O6. Regresión: no hace falta si solo se añade telemetría sim. Modelo: ejecutor.
- Lectura del código (ejecutor, Cataclysm `lender544/new1.20.1` rama `1.21`; el modpack usa 3.33, así que todo va por
  reflexión y falla suave): `Ignis_Entity.hurt` devuelve `false` ANTES de `super.hurt` por animación inmune
  (contraataque, fases) o por escudo (`getIsShield` y fuente delante: vector plano fuente→Ignis · vista < 0). Luego
  `LLibrary_Boss_Monster.hurt`: tope por golpe, caída por distancia del TIRADOR (completo hasta `RangeLimit`, `false`
  desde 1,5×), y un cubo con fuga (`damageBucket`, se llena hasta `DamageCap`, pierde `DpsCap`/20 por tick; el golpe
  que lo desborda se corta a lo que cabe o a 0,1). Solo después llega `LivingEntity.hurt`, donde NeoForge lanza
  `LivingIncomingDamageEvent`. Por eso escudo y distancia no llegan a ningún evento de daño: se clasifican en
  `ProjectileImpactEvent` repitiendo la prueba de Cataclysm (primero escudo, luego distancia).
- Medidas (extras por corrida, todas presentes si hay jefe): `ignis:hits:bow|gun` (impactos de proyectil, intentos),
  `ignis:shield`, `ignis:far` (tirador ≥ 1,5 × `RangeLimit` = 22,5: daño cero), `ignis:falloff` (entre 15 y 22,5,
  daño reducido), `ignis:reached` (golpes que llegan al evento de entrada), `ignis:hits:melee` (cuerpo a cuerpo que
  llega), `ignis:capped` (cortados por el cubo; `ignis:cappedFull` = reducidos a 0,1), `ignis:amountIn` (daño que
  entra, antes de armadura), `ignis:dmgIn` (vida que pierde), `ignis:bossSec`, `ignis:shieldUpSec`.
  **Omitido (necesita mixin):** golpes cuerpo a cuerpo parados por el escudo (no lanzan ningún evento). Proxy:
  `ignis:frontMeleeSec` (segundos de guardias de cuerpo a cuerpo a ≤ 5 bloques delante del escudo alzado).
  **`capped` es un recuento, no "propuesto − aplicado"**: el evento de entrada ya trae el daño recortado y no hay
  evento antes. Las inmunidades por animación tampoco se ven (quedan dentro de `hits − shield − far − reached`).
  Coherencia esperada: `shield + far ≤ hits:bow + hits:gun` y `capped ≤ reached`.
- Estado: **lanzado 2026-11-15** · preset `t58-ignis-g8-telemetria` (`gen-ignis58`, copia de
  `t38-coord-ignis-g8-base`), `mods=bestiario`, `seed=5801`, n = 8 · commit `1fcee20` · run 43670298466.
- Veredicto (analista, 2026-11-15; n = 8 semillas 5801-5808 + 1 control a velocidad normal, datos en `tools/torneo-data/t58/`):
  **cumple (capacidad)**: medidas no nulas y coherentes en 8/8 (`shield+far` ≤ `hits:bow`; `capped` ≤ `reached`).
  Resultado del escenario: 100 % victorias, 3,5 de 8 vivos (44 %, igual que la ronda 30), 104 ± 8 s.
  Reparto (sumas de 8 corridas): impactos de flecha 1117 (gun 0), cuerpo a cuerpo que llega 1023.
  | Sumidero | Cifra | % |
  |---|---|---|
  | Escudo frontal (flechas paradas) | 261 | 23 % de las flechas |
  | Distancia ≥ 22,5 (daño 0) | 30 | 3 % de las flechas |
  | Caída 15-22,5 (reducido, sí entra) | 328 | 29 % de las flechas |
  | Cubo de tope por segundo: golpes cortados | 1206 de 1749 que llegan | **69 %** (cortados a 0,1: 40) |
  | Vida que pierde Ignis / daño que entra | 6905 / 12454 | 55 % |
  Inmunidad por animación ≈ 100 golpes (5 %). Escudo arriba 42 s de ~99 s de combate (≈ 43 %); `frontMeleeSec` ≈ 81 s/corrida
  (melee delante del escudo alzado, no medible como golpes parados). Melee ≈ 55 % de los golpes que llegan y el arco 45 %.
  **Mayor sumidero: el tope por segundo (cubo con fuga)** — 7 de cada 10 golpes que llegan salen recortados, así que más
  golpes por segundo no suben el daño; el segundo es el escudo frontal (23 % de las flechas, melee parado sin medir).
  Primera ficha de diseño para Fable (O6): el límite es dps compartido, no puntería (precisión 89 %); la vía es subir el
  daño por golpe (calidad) o repartir el esfuerzo fuera del escudo (flanqueo), no más cadencia/guardias. Con esto, 8
  guardias ya ganan al 100 %: O6 se mide en bajas (3,5 vivos) y tiempo, no en victoria. Mueve O6: sin cambio de cifra
  (telemetría, no regla). Sin valor por defecto; regresión no necesaria.

## Orquestador 2026-11-15 (turno 3/6; modelo Opus, no Fable: solo fichas técnicas)

Veredicto nuevo: F16c → RETIRARSE por unidad **sigue rota** (`u<k>:act:retreat` solo en U2, 3 de 8; U1 nunca; U1+U2
no quedan más lejos), regresión verde. Es capacidad (como F16/F16b): O7 sigue 1 de 7, intentos 0; R1 en verde. F18 (O6)
en curso, sin veredicto. O3 sigue estancado y en la lista del próximo issue (1 de 7 estancados: no se abre issue).
Dos fichas: F16d (2.º intento de la capacidad, que bloquea F12 paso 2 y F14) y F19 (O1, intentos 0, sin tocar desde
la ronda 48: medir dónde se pierde la pelea con las medidas continuas, sin cambiar reglas, como F18 para O6).

### F16d [EJECUTOR] RETIRARSE por unidad: por qué U1 no emite saltos (capacidad, 2.º intento) → O7
- Hecho (F16c, commit `46d9869`): con los saltos en FALLBACK, U2 emite `act:retreat` en 3 de 8 corridas (4 pasos) y U1
  en 0 de 8. La orden se registra en 8/8 a los 5 s. Lo que no se sabe: si RETREAT_FIGHTING llega a la fase de salto,
  cuántas veces pasa a aguante, y si algo la cancela.
- Tarea: telemetría por unidad solo en la sim (`u<k>:rf:start`, `u<k>:rf:jump`, `u<k>:rf:hold`, `u<k>:rf:end` con
  el motivo como sufijo, p. ej. `u<k>:rf:end:noThreat|done|override|broken`), y leer con ella la causa. Mirar en
  concreto: si la unidad 1 (o el orden U1/U2 en `SIM_REAL_BOUNDS`) se pierde al llenarse o vaciarse el conjunto; si la
  condición para saltar (amenaza a < X bloques, vida, cadencia) casi nunca se cumple con vindicadores pegados; si otra
  regla (LOW_HEALTH_RETREAT, Engagement, rung 1-2 de `DecisionLadder`) vuelve a poner HOLD/ATTACK encima. Anotar la
  causa aquí. Arreglo mínimo, **solo en la ruta sim de `unitInstr`** (mismo límite que F16c).
- Prueba: preset `t56b-retira-vindicadores32`, `mods=""`, seed 5601, n = 8; lote `regresion` si se toca código común.
- Aprueba si en ≥ 6 de 8 corridas U1 y U2 tienen `u<k>:act:retreat` > 0 y `MeanEnemyDist` de U1+U2 > U3+U4. Si no,
  veredicto "sigue rota" **con la causa leída en la telemetría `rf:*`**; un 3.er fallo pasa la capacidad a la lista de
  David (F12 paso 2 y F14 quedarían con otro cebo: MANTENER + FLANQUEAR sin retroceso). Modelo: ejecutor.
- Lectura (ejecutor, 2026-11-15; código y datos de F16c, sin causa cerrada): (1) `act:retreat` suma +1 **por guardia**
  en cada salto emitido: los "4 pasos" de U2 son UN solo salto de 4 guardias; ninguna unidad emitió un 2.º salto.
  (2) La pelea dura 13-19 s (contacto a los 3-4 s) y la orden llega a los 5 s: con `cycle` = 4 s caben ~2 saltos como
  mucho. (3) En el código no aparece quién corta la unidad (no hay `UnitOrders.issue` de otra fuente en este
  escenario, sin `force`; ni excepción en `latest.log`). Por eso no se arregla nada a ciegas: solo telemetría.
- Telemetría (solo sim, sin cambio de conducta; `InstructionRunner` común pero no-op fuera de `unitInstr`), por unidad
  en modo RETREAT_FIGHTING: `u<k>:rf:start`, `rf:step` (evaluaciones, ~1/s), `rf:phaseFlip`, `rf:noThreat`,
  `rf:hold`, `rf:jump:far|near|phase` (salto elegido), `rf:same` (misma acción/casilla: no se emite), `rf:issued:<KIND>`,
  `rf:realBounds` (la clave de F16c casó), `rf:dX10`/`rf:farX10`/`rf:nearX10` (distancia al centro enemigo y umbrales
  ×10, sumados en los pasos con enemigo: dividir por `step − noThreat`), `rf:end:empty|released|broken|
  override:<KIND>:<SOURCE>:<why>` y `rf:running` (seguía activa al acabar). Lectura sugerida al analista: si
  `step` ≈ 0 → la unidad no se evalúa; si `end:override:*` → quién la pisa; si `hold` domina con d entre near y far →
  la condición de salto casi no se cumple; si `issued:FALLBACK` > 0 sin distancia → la escalera la anula.
- Estado: **lanzado 2026-11-15** · presets `t56b-retira-vindicadores32` + `regresion`, `mods=""`, `seed=5601`,
  n = 8 · commit `e3e0fc6` · run 43670902967.
- Veredicto (analista, 2026-11-15, lote `20261115-030952-t56b-retira-vindicadores32`, commit `e3e0fc6`; regresión `check`
  verde): **sigue rota, con la causa leída en `rf:*`**. n = 9 corridas (rep 0-8; la ficha decía 8), 100 % victorias,
  73 % vivos, 17 s. Criterio (U1 y U2 con `act:retreat` > 0 en ≥ 6): U1 0/9, U2 1/9 -> NO. `MeanEnemyDist` U1+U2 ≈
  U3+U4 (sin retroceso real).
  Causa: cada unidad hace **UN solo paso** (`rf:step` = 1 en 9/9 y en U1 y U2) y en el siguiente `InstructionRunner.step`
  `UnitOrders.members(...)` devuelve vacío (`rf:end:empty` 9/9 en ambas). No hay `end:override`, `end:broken` ni
  `rf:phaseFlip`; no es muerte (73 % siguen vivos y ganan). En ese único paso la distancia al centro enemigo es de
  7-18 m (U1 media 12,6; U2 8,5) frente a near 7,4 y far 29,4: cae en la zona intermedia -> `issued:HOLD` (8/9 en
  U1 y U2; solo U2 en la corrida 1 salta `near`, FALLBACK, 4 guardias). Así que la condición de salto casi nunca se
  cumple en el único paso que hay, y la unidad se pierde un segundo después. Sospecha (no medida): el conjunto ad hoc
  (`ADHOC_MEMBERS`/`bindAdhoc`) o `GuardStanceHandler.loaded()` se vacía tras emitir el HOLD; falta telemetría
  del tamaño de `members` y de `ADHOC_MEMBERS` en cada paso. Mueve objetivos: no (O7 sigue 1 de 7, intentos 0).
  Por la regla de la ficha, un 3.er fallo pasa la capacidad RETIRARSE-por-unidad **a la lista de David** (F12 paso 2 y
  F14 con otro cebo: MANTENER + FLANQUEAR sin retroceso). Arreglo mínimo sugerido si David prefiere seguir: mirar por
  qué `members()` se vacía tras el 1.er paso (tamaño de `ADHOC_MEMBERS[key]` y `loaded(level)` antes/después de
  `UnitOrders.issue`).

### F19 [EJECUTOR corre, ANALISTA lee] O1: ¿dónde se pierde la pelea contra 32 Pax en llano? (telemetría) → O1
- Hecho (ronda 48, n = 30, semillas 2001): 8 arqueros contra 32 Pax en llano, 57 % victorias (cifra de O1). Desde
  entonces existen las medidas `SimCombatStats` (dps, guardSeconds, hurt:*, wastedHits, iframeHits/iframeDmg, nnDist,
  aggro*) y `act:*`, que ese lote no tenía o no se leyeron.
- Tarea: sin cambiar reglas ni Java, relanzar el preset `t48-distancia-pillagerpax32-flat` (6 brazos: arco/línea/
  línea+scutum × con/sin regla) con `mods` igual que la ronda 48 (mirar el lote original; si no consta, `bestiario`),
  seed 5901, n = 30 por brazo (mismas semillas en los 6). Si el preset ya no genera, regenerarlo con el mismo
  generador y decirlo.
- Veredicto del analista: por brazo, % victorias con EE, vivos, segundos hasta ganar/perder, vida restante de los
  Pax, flechas gastadas, `wastedHits`/`iframeHits` (% de impactos perdidos), `hurt:*` (de qué mueren los guardias:
  bala, cuerpo a cuerpo, otro) y `nnDist`. Señalar el mayor sumidero (impactos perdidos, distancia de tiro, muertes por
  bala a distancia X) como primera ficha de diseño de Fable para O1. Comprobar que el 57 % se reproduce (± 2,5 EE); si
  no, la cifra de O1 se corrige con la de este lote. No cambia ningún valor por defecto. Modelo: ejecutor (lanzar) +
  analista.

## Orquestador 2026-11-15 (turno 4/6; modelo Opus, no Fable: solo fichas técnicas)

Veredicto nuevo: F18 (O6) **cumple la capacidad**; 8 guardias netherita+ ganan a Ignis al 100 %, 3,5 de 8 vivos (44 %,
igual que antes). El mayor sumidero es el tope de daño por segundo (69 % de los golpes que llegan salen recortados) y
el segundo es el escudo frontal (23 % de las flechas). Es telemetría y no una regla, así que O6 no cambia de cifra y
sigue con intentos 0. Diseñar la primera regla para O6 (más daño por golpe o flanquear el escudo) toca a Fable: queda
apuntado para el próximo turno de Fable, no se diseña aquí. F16d y F19 siguen en curso. O3 sigue estancado
(1 objetivo de 7, no se abre issue). Ficha nueva: F20 para O5, que no se ha tocado desde la ronda 30 y cuya cifra es un
rango (20-50) en vez de una medida. F20 no cambia Java.

### F20 [EJECUTOR corre, ANALISTA lee] O5: cifra de partida con n = 30 y dónde se pierde contra Tremorzilla → O5
- Hecho: O5 = "20-50 % victorias" (rondas 30-31, n pequeño, con mezclas de equipo). No hay cifra con n ≥ 30 ni
  lectura de las medidas `SimCombatStats` (dps, guardSeconds, hurt:*, aoeHits, wastedHits, iframeHits/iframeDmg,
  nnDist, aggro*).
- Tarea: sin cambiar reglas ni Java, lanzar el preset `t30-gigante-tremorzilla-netheritamas` (16 guardias
  `tier:netherite+` contra Tremorzilla, `off:morale`), `mods` igual que la ronda 30 (mirar el lote original; si no
  consta, `bestiario`), seed 6001, n = 30. Si el preset ya no genera, regenerarlo con el mismo generador y decirlo.
- Veredicto del analista: % victorias con EE, vivos al final, segundos hasta ganar/perder, **vida restante de
  Tremorzilla** (métrica continua principal), dps azul, `hurt:*` (de qué mueren: aoe/rayo, cuerpo a cuerpo, otro),
  `aoeHits`, `wastedHits` + `iframeHits` (% de golpes perdidos) y `nnDist`. Señalar el mayor sumidero como primera
  ficha de diseño de Fable para O5. La cifra de O5 pasa a ser la de este lote (una medida, no un rango). No cambia
  ningún valor por defecto; no hace falta regresión. Modelo: ejecutor (lanzar) + analista.
- Lectura (ejecutor): `t30-gigante-tremorzilla-netheritamas` es un escenario, no un preset, y no llevaba `off:morale`.
  Lectura más simple: ronda nueva `t60` (`gen-tremor60`, copia exacta del escenario + `off:morale`, 30 semillas, 5 min);
  la ronda 30 no deja constancia de `mods`, así que `bestiario` (el mismo que F18).
- Estado: **lanzado 2026-11-15** · preset `t60-tremorzilla-g16-netheritamas`, `mods=bestiario`, `seed=6001`, n = 30 ·
  commit `7f8ede8` · run 43671090649.

## Orquestador 2026-11-15 (turno 6/6; modelo Opus, no Fable: solo fichas técnicas)

Veredicto nuevo: F16d → RETIRARSE por unidad **sigue rota** (2.º fallo), pero ahora con causa: cada unidad se evalúa
UNA vez y en el paso siguiente `UnitOrders.members(...)` sale vacío (`rf:end:empty` 9/9, sin override). Es capacidad:
O7 sigue 1 de 7, intentos 0; regresión verde. F18 cerrada (pendiente Fable para O6). F20 (O5) en curso. F19 **no llegó
a correr**: su despacho de ejecutor (run 43670493902) se canceló sin jobs, porque la concurrencia `equipo-ejecutor`
solo guarda un pendiente. Se vuelve a despachar tal cual. O3 sigue estancado (1 de 7: no se abre issue).
Ficha nueva: F16e (3.er y último intento, arreglo de la causa leída). Aviso a los turnos: no despachar más de un
ejecutor pendiente a la vez (el tercero cancela al segundo).

### F16e [EJECUTOR] RETIRARSE por unidad: que la unidad no se vacíe tras el 1.er paso (capacidad, 3.er intento) → O7
- Hecho (F16d, commit `e3e0fc6`, n = 9): `rf:step` = 1 y `rf:end:empty` en 9/9 para U1 y U2; ni `end:override`, ni
  `end:broken`, ni muertes que lo expliquen (73 % vivos). En el único paso la distancia (U1 12,6 m, U2 8,5 m) cae entre
  near 7,4 y far 29,4 → `issued:HOLD`.
- Tarea: (1) telemetría solo sim en cada paso de RETREAT_FIGHTING: `u<k>:rf:members` (tamaño de `members()`),
  `u<k>:rf:adhoc` (tamaño de `ADHOC_MEMBERS[key]`), `u<k>:rf:loaded` (cuántos de esos están en
  `GuardStanceHandler.loaded(level)`), antes y después de `UnitOrders.issue`. (2) Leer el código de `bindAdhoc`,
  `ADHOC_MEMBERS`, `UnitOrders.issue`/`members` y encontrar quién vacía el conjunto (p. ej. el HOLD reasigna o
  reconstruye la unidad, una clave que cambia, un `remove` al emitir). (3) Arreglo mínimo **solo en la ruta sim de
  `unitInstr`** (mismo límite que F16c/F16d). **No** tocar near/far ni la cadencia: una sola cosa por ficha.
- Prueba: preset `t56b-retira-vindicadores32`, `mods=""`, seed 5601, n = 8; lote `regresion` si se toca código común.
- Aprueba si en ≥ 6 de 8 corridas U1 y U2 tienen `rf:step` ≥ 5 **y** `u<k>:act:retreat` > 0, y `MeanEnemyDist` de
  U1+U2 > U3+U4. Si la unidad ya persiste (`rf:step` ≥ 5) pero no salta: veredicto "persistencia arreglada, salto
  por calibrar" con la distribución de distancias por paso (abre F16f de umbrales, no cuenta como 3.er fallo). Si
  `members` sigue vaciándose: 3.er fallo → la capacidad pasa a la lista de David (F12 paso 2 y F14 con el cebo
  MANTENER + FLANQUEAR sin retroceso). Modelo: ejecutor + analista.
- Lectura (ejecutor, 2026-11-15): leyendo el código no aparece quién vacía la unidad. U1-U2 son unidades de escuadra
  (`SimBlue/U<k>`, no ad hoc: `rf:adhoc` debería salir 0), `members()` = guardias de `GuardStanceHandler.loaded()`
  (todos los guardias con dueño, se rehace cada 1 s) con dueño + ruta `SimBlue/U<k>`. Nadie llama a `assign`/`unassign`
  ni cambia el dueño en este escenario (sin `force`, `split` ni `mods`), y no hay `end:released` ni `end:override`.
  Por eso este turno añade, solo para la sim: (1) telemetría por paso `rf:members|adhoc|loaded` (sumas: dividir por
  `step`) y `rf:membersPost|adhocPost|loadedPost` tras la directiva (dividir por `rf:post`); (2) al salir vacío,
  `rf:emptySeen` y el motivo contado por guardia inicial: `rf:why:alive|loaded|ownerMatch|inSquad|pathMatch` (el
  primero que caiga por debajo de `alive` es la causa); (3) arreglo: si la unidad es RETIRARSE de la sim
  (`SIM_REAL_BOUNDS`), sus guardias vivos se vuelven a atar como unidad ad hoc **con la misma clave** (`rf:rebound`),
  así que versión y directivas siguen y `unitKeyOf` llega a los guardias. Fuera de `unitInstr` no cambia nada.
- Estado: **lanzado 2026-11-15** · presets `t56b-retira-vindicadores32` + `regresion`, `mods=""`, `seed=5601`,
  n = 8 · commit COMMIT_F16E.
