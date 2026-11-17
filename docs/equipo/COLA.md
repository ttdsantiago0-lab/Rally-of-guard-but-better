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
  n = 8 · commit `5bb8417` · run 43671772119.

## Analista 2026-11-15 (F16e + F20)

**F20 — Veredicto (O5, ronda t60, commit `7f8ede8`, run 43671090649).** El lote trae 36 filas (30 semillas 6001-6030 + 6
reintentos con otras semillas, 1 control repetido): se cuentan las 36 no-control. Sin cambios de reglas ni de valores por defecto.
| n | victorias | vivos (media) | segundos | vida restante Tremorzilla |
|---|---|---|---|---|
| 36 | 8 = **22 % (IC95 ±14)**, 21 derrotas, 7 empates (tope 300 s / "stuck") | 3,3 de 16 (ganadas 11,6; perdidas 0,5) | ganadas 64, perdidas 142 | media 141, mediana 156; ganadas 0, perdidas 181 |
Solo las 24 corridas sin `stuck` ni reintento: 6 gana, 17 pierde, 1 empate (25 %), así que el 22 % no es un efecto de los atascos.
Dónde se pierde (medias por corrida): `hurt:tremorzilla_beam` 235 (frente a `hurt:mob` 107): **el rayo causa el 69 % del daño recibido**;
`aoeHits` 14,3; `wastedHits` 0; `iframeHits` 193 de ~321 golpes a Tremorzilla = **~60 % de los golpes caen en i-frames**
(`iframeDmg` 1935 frente a `dmgDealt` 919: el daño perdido por i-frames es ~2 veces el dado); `nnDist` 7,4 (guardias dispersos,
no apiñados); `dps` 0,9; `aggroShield` 70 / `aggroOther` 49 (el jefe cambia de objetivo 7,4 veces).
- Mayor sumidero → primera ficha de diseño de Fable para O5: **i-frames** (60 % de golpes sin efecto: la ficha `FireWindows`,
  fuego sincronizado/en ventana, ya existe solo en sim `fire:sync`) y, segundo, el rayo (235 de daño; dispersión `nnDist` 7,4 ya ayuda).
- **O5 pasa a una medida: 22 % victorias (n = 36, 16 guardias netherita+, `off:morale`, mods `bestiario`), vida restante de Tremorzilla 141 de media.** Cumple lo pedido; no mueve el KPI hacia arriba (cifra de partida).

**F16e — Veredicto: falló la capacidad (3.er fallo; commit `5bb8417`, run 43671772119, n = 8, 100 % victorias, 77 % vivos).**
`rf:step` = 1 en 8/8 para U1 y 7/8 para U2 (U2 ni siquiera hace el paso en 1 corrida); `end:empty` + `emptySeen` en 8/8;
`rebound` 0 en 8/8. Salto: U1 0/8, U2 2/8 (`jump:near`, `issued:FALLBACK`); `act:retreat` U1 0 / U2 0,5. Criterio (≥ 6/8 con `rf:step` ≥ 5) incumplido; `MeanEnemyDist` 8,5-11,5 m.
Dato nuevo: antes y después de `issue`, `members`=`loaded`=4 (la unidad existe y está completa); la vacía algo entre el paso 1 y el 2.
Y en `simKeep` NO sale ningún `rf:why:alive` (ni `loaded`, ni `ownerMatch`): ninguno de `u.simIds` es un `Guard` vivo según
`level.getEntity(id)` justo en el 2.º paso, pese a que sobrevive el 77 %. Es decir: la causa no es el dueño/escuadra/ruta (los `why:*` siguientes ni se evalúan)
sino que `simIds` (InstructionRunner.java:146, 629) apunta a algo no resoluble por `getEntity` (o está vacío). Pista para quien retome, no arreglo.
Según la regla de la ficha: **3.er fallo → la capacidad RETIRARSE por unidad pasa a la lista de David** (F12 paso 2 y F14 con el cebo MANTENER + FLANQUEAR sin retroceso). Sigue sin cumplir el criterio y no cuenta como fallo del lote (el lote corrió bien).
`regresion`: este lote no se ha leído aquí (no se cambió ningún valor por defecto).


## Indicaciones de David — 2026-11-15 (para el orquestador)

**Revisión en dos días (2026-11-17).** David quiere saber **qué estamos haciendo bien y qué estamos haciendo mal**,
como equipo y como método, no solo cifras. Antes del 2026-11-17 a las 12:00 UTC el orquestador escribe
`docs/equipo/REVISION_2026-11-17.md`, corto:
1. **Bien**: qué fichas dieron un resultado útil (mejoró un objetivo, confirmó o descartó algo con datos) y por qué
   funcionaron (diseño, métrica, escenario).
2. **Mal**: dónde se gastaron turnos sin avanzar (capacidades rotas repetidas, escenarios en el techo, fichas que
   no podían discriminar, turnos fallidos de la CI) y qué regla del método lo habría evitado.
3. **Por objetivo** (O1–O7): cifra de partida, cifra hoy, intentos, si vale la pena seguir.
4. **Coste**: turnos usados por rol y cuántos fallaron sin hacer nada.
5. **Tres cambios propuestos** al método o a los objetivos, para que David decida.
El plan de Claude de David sube en una semana; hasta entonces, priorizar fichas baratas y que discriminen.

## Orquestador 2026-11-16 (turno 1/6; modelo Fable: diagnóstico y diseño)

Veredictos leídos: F20 (O5 pasa de un rango a una medida, 22 %, n = 36; cifra de partida, intentos 0) y F16e (3.er
fallo de la capacidad RETIRARSE por unidad). F19 (O1) sigue sin correr: queda escrita y se despacha en el próximo turno.
Estancados: O3 (1 de 7; no toca abrir issue). Dos fichas, en UN solo turno de ejecutor (la espera mínima de 20 min
descarta un segundo despacho seguido): primero F21 (sin Java, sobre código que ya compila) y después F16f.

**Diagnóstico de Fable sobre F16c-e (leído en el código, sin corrida que lo confirme todavía).**
`GuardStanceHandler.onLevelTick` llama a `InstructionRunner.tick(level)` una vez **por cada dimensión** (overworld,
nether, end) y `UNITS` es un mapa global sin dimensión. En el mismo tick, tras el paso bueno en el overworld, el tick del
nether evalúa la misma unidad: `UnitOrders.members(nether, key)` sale vacío, `step` devuelve `false` y la unidad se
borra. Encaja con todo lo medido: `rf:step` = 1, `end:empty` 8/8, `members` = 4 antes y después de `issue`, y ningún
`simIds` resoluble con `level.getEntity` (se preguntaba al nivel equivocado). No es un fallo de la sim: **en el juego
toda instrucción de grupo moriría tras su primer segundo**. Si F16f lo confirma, las medidas de retirada con
instrucción (rondas 39, 52, 55: O3) midieron una instrucción de un solo paso, y O3 se vuelve a abrir en vez de ir a
la lista de David. Por eso se hace un 4.º intento pese a la regla de F16e: ahora hay causa concreta y es un arreglo
de 3 líneas, no otra ronda de telemetría.

### F21 [EJECUTOR lanza, ANALISTA lee] Jefes sin moral: ¿qué composición? (sin Java) → O5 y O6
- Hecho: la tabla de composiciones contra jefes (ronda 32) se midió CON moral, que rompía al 79-90 % de los guardias;
  desde la ronda 34 los jefes se prueban con `off:morale` y esa tabla no se repitió. F20: contra Tremorzilla el 60 % de
  los golpes cae en i-frames (el fuego sincronizado ya es el valor por defecto, así que son sobre todo espadas: el
  cuerpo a cuerpo ya está saturado con 8) y las flechas le hacen el 35 %; el rayo es el 69 % del daño recibido. F18:
  a Ignis le entra 15,7 de daño por segundo antes de armadura con un tope de 14: el ataque **ya está en el tope**, el
  tiempo (~100 s) casi no puede bajar, y los guardias mueren por golpes cuerpo a cuerpo (`hurt:mob` 316 de ~340 por
  corrida). La lectura "subir el daño por golpe" del veredicto de F18 no vale: el tope es de daño total por segundo.
- Hipótesis. Tremorzilla: los arqueros aportan poco daño y son blanco del rayo; más escudos deja menos vida al jefe.
  Ignis: con el ataque en el tope, sobran cuerpos delante; menos guardias en cuerpo a cuerpo = más vivos con el mismo
  tiempo. Riesgo que mide el brazo `arcos`: Ignis se cura 25/s si pasa 10 s sin blanco y las flechas no cuentan.
- Brazos (generador `gen-jefes61`; solo cambia `blue.role`; todo lo demás copia exacta):
  - de `t60-tremorzilla-g16-netheritamas` (16 guardias, 5 min): `t61-tremor-linea` (`mix:shield=1,archer=1`, control),
    `t61-tremor-escudos` (`shield`), `t61-tremor-espadas` (`melee`), `t61-tremor-3a1` (`mix:shield=3,archer=1` = 12 + 4);
  - de `t58-ignis-g8-telemetria` (8 guardias, 4 min): `t61-ignis-linea` (control), `t61-ignis-arcos` (`archer`),
    `t61-ignis-1a3` (`mix:shield=1,archer=3` = 2 + 6), `t61-ignis-3a1` (`mix:shield=3,archer=1` = 6 + 2).
  Comprobar en el código de la sim cómo reparte `mix:` (si 3:1 no da 12 + 4 y 6 + 2, usar la forma que sí lo dé y
  anotarlo); el analista lo verifica con `kit:*`.
- Lotes: presets `t61-tremor-comp` y `t61-ignis-comp` (4 escenarios cada uno), n = 30 por brazo, **mismas semillas en
  los 4 brazos**, `seed=6101`, `mods=bestiario`. 240 corridas. No cambia ningún valor por defecto: sin regresión.
- Métrica principal (diferencia pareada por semilla contra `linea`): Tremorzilla = vida restante del jefe
  (`red.healthLeft`); Ignis = guardias vivos (`blue.survivors`). Secundarias: % victorias, segundos,
  `hurt:tremorzilla_beam`, `iframeHits`, `ignis:capped`, `ignis:amountIn`/`bossSec`, `death:*`.
- Aprueba un brazo si mejora la métrica principal en más de 2,5 errores estándar de la diferencia pareada y,
  Tremorzilla: no baja las victorias; Ignis: victorias ≥ 90 %. Un brazo aprobado pide lote de confirmación (semillas
  nuevas) antes de cambiar la cifra de O5/O6. Si ninguno mejora: la composición no es la palanca (O5 y O6 intentos
  +1) y la siguiente ficha va al rayo (Tremorzilla) y a quién se pone delante del escudo (Ignis). El resultado es una
  entrada del manual y un dato para el Battle Planner, no un valor por defecto. Modelo: ejecutor + analista.
- Estado: lanzado 2026-11-16 · `t61-tremor-comp,t61-ignis-comp` (mods=bestiario, seed=6101, n = 30) · commit f11d741
  (run 43795318814). `mixRole` reparte por proporción cíclica: `mix:shield=3,archer=1` da 12 + 4 con 16 y 6 + 2 con 8;
  `mix:shield=1,archer=3` da 2 + 6. `melee` = espada (nivel netherita+ por la etiqueta `tier:`). Etiqueta `comp:linea`
  copiada sin cambiar en los 8 brazos (copia exacta); distinguir los brazos por el id del escenario.

### F16f [EJECUTOR] Instrucciones: cada unidad se evalúa solo en su dimensión (arreglo de causa) → O7 (y O3)
- Hecho: ver el diagnóstico de arriba (`InstructionRunner.tick` por dimensión, `UNITS` global).
- Tarea (una sola cosa): `Unit` guarda la dimensión donde se creó (`level.dimension()` en `start`) y `tick(level)`
  salta las unidades de otra dimensión **antes** de llamar a `step`. Nada más: no tocar near/far, cadencia ni
  `simKeep` (queda como está; `rf:emptySeen` y `rf:rebound` deben salir 0). Antes de escribirlo, comprobar con `grep`
  si otro bucle por nivel con estado global tiene el mismo patrón (`ForceBrain.tick`, `FormationFollow.tick`,
  `CaptainSuccession.tick`, `SlotQueue.drain`) y **solo anotarlo** en esta ficha, sin arreglarlo.
- Prueba: preset `t56b-retira-vindicadores32`, `mods=""`, seed 5601, n = 8, más el lote `regresion` (es código común).
- Aprueba si en ≥ 6 de 8 corridas U1 y U2 tienen `rf:step` ≥ 5 y `rf:end:empty` = 0 mientras quedan guardias vivos.
  Si además `u<k>:act:retreat` > 0 y `MeanEnemyDist` de U1+U2 > U3+U4: capacidad cerrada (F12 paso 2 y F14 se
  desbloquean). Si persiste pero no salta: "persistencia arreglada, salto por calibrar" (umbrales, ficha aparte). Si
  `rf:step` sigue en 1: diagnóstico de Fable refutado → la capacidad va a la lista de David como decía F16e.
  El analista comprueba la regresión (`python3 tools/regresion.py check`): si alguna fila con instrucción cambia, lo
  dice (es esperable: antes las instrucciones duraban un paso) y no lo trata como fallo sin mirar el sentido.
- Orden en el turno del ejecutor: 1.º F21 (generar, commit, lanzar), 2.º F16f (arreglo, commit, lanzar). Así los
  lotes de F21 corren sobre un commit que ya compila aunque F16f rompiera el build.
- Estado: lanzado 2026-11-16 · `t56b-retira-vindicadores32,regresion` (mods="", seed=5601) · commit 0242c3a (run
  43795379095). Arreglo: `Unit.dimension` (de `level.dimension()` en `start`) y `tick` hace `continue` antes de `step`
  si no coincide; nada más. Otros bucles (solo anotado, sin tocar): `SlotQueue.drain` ya filtra por dimensión;
  `FormationFollow.tick` es seguro (`level.getPlayerByUUID` solo halla jugadores de ese nivel); `CaptainSuccession.tick`
  tiene `PENDING` global y lo aplica en el primer nivel que pasa, pero usa referencias a `Guard`, no búsquedas por
  nivel: inocuo; `ForceBrain.tick` usa `ForceRegistry` global (del overworld) y solo salta niveles sin guardias propios
  cargados: con guardias en dos dimensiones cada fuerza se evalúa dos veces por segundo, una en el nivel equivocado
  (`step` sale pronto con unidades vacías, sin borrar nada; posible coste doble y ruido, sin daño visto).
- Veredicto (analista, 2026-11-16, lote `20261116-030150-t56b-retira-vindicadores32`, commit `0242c3a`; regresión `check`
  verde, 0 filas peor que el ruido; datos en `tools/torneo-data/t56b-f16f/`): **persistencia arreglada, salto por
  calibrar**. n = 9, 100 % victorias, 11,6 de 13 vivos (antes 73 %), 16-30 s. `rf:step` ≥ 9 en U1 y U2 en 9/9 (9-22;
  antes 1), sin ningún `rf:end:empty` (ni `emptySeen`/`rebound`): el diagnóstico de Fable (dimensión) se confirma.
  `u1:act:retreat` > 0 y `u2:act:retreat` > 0 en 9/9 (antes 0/9 y 1/9); saltos `near` en 9/9, `far` en 4/9, `phase`
  en 9/9. Pero el segundo criterio falla: `MeanEnemyDist` medio de U1+U2 < U3+U4 en 9/9 corridas (p. ej. 8,4 vs 12,4
  m de media; U1 ≈ 5 m casi siempre): saltan, pero no se separan del enemigo más que las unidades que se mantienen
  (`hold` ≈ 40 % de los pasos). Capacidad **no cerrada**: F12 paso 2 y F14 siguen sin desbloquear; falta calibrar
  umbrales near/far y cadencia (ficha aparte para el orquestador). Regresión: ninguna fila con instrucción cambió de
  forma relevante (4 filas «sin datos» ya lo estaban). Mueve objetivos: no (O7 sigue 1 de 7; intentos sin contar).

### F19 (O1): pendiente de despacho en el próximo turno del orquestador (escrita arriba, nunca corrió).

- Veredicto F21 (analista, 2026-11-16, lotes `20261116-030127-t61-tremor-comp` y `20261116-030109-t61-ignis-comp`, commit
  `f11d741`; datos en `tools/torneo-data/t61/`; 30 semillas pareadas por brazo; las filas sobrantes son reintentos, los %
  son sobre todas las filas, así que ±1 corrida): **aprueban `escudos` (Tremorzilla) y `1a3` + `arcos` (Ignis); `3a1` y
  `espadas` empeoran**. Kit verificado con `kit:shield` (16 escudos en `escudos`, 12 en `3a1`; el control `linea` = 8+8).
  Tremorzilla (vida restante del jefe, dif. pareada vs línea; línea 131,6, 22 % victorias, 2,9 vivos): escudos 33,9
  (−104,2, z −5,6; 84 % victorias; 10,1 vivos); 3a1 59,9 (−78,5, z −3,5; 65 %; 7,3 vivos); espadas 139,5 (−2,4, z −0,1;
  3 %; 0,4 vivos). Ignis (guardias vivos, dif. vs línea 3,03 / 97 % / 99 s): 1a3 4,83 (+1,80, z +4,9; 97 %; 82 s);
  arcos 4,45 (+1,43, z +3,7; 97 %; 81 s); 3a1 1,07 (−1,97, z −5,6; 67 %; 110 s). El riesgo de curación de Ignis sin
  flechas que cuenten no se ve en `arcos` (97 % y más rápido). Lectura: contra Tremorzilla la palanca es la composición
  (todo escudo: el jefe pierde casi toda su vida y las bajas caen; las espadas son el peor brazo); contra Ignis, menos
  cuerpos delante y más arcos sube los vivos de 3,0 a 4,8 sin perder victorias. No se tocó ningún valor por defecto.
  Mueve objetivos: candidatos, no confirmados (hace falta lote de confirmación con semillas nuevas): O5 22 % -> 84 %
  (escudos, n=30); O6 44 % -> 60 % vivos (1a3, 4,83 de 8). Pedir al orquestador: ficha de confirmación (semilla nueva,
  mismos 2 brazos aprobados por jefe) y, ya que todo escudo gana, probar 16 escudos en Tremorzilla con otro nivel de equipo
  (¿depende de netherita+?) antes de darlo como regla del Battle Planner.


## Indicaciones de David — 2026-11-16 (para el orquestador)

**Hasta el 17 a las 12:00 UTC: usar Fable todo lo posible** (presupuesto temporal en OBJETIVOS.md; los tres roles con
Fable). Aprovecharlo para lo que más pide pensar: diagnósticos, diseño de fichas en contexto, y la revisión del 17.

**Retirada con retaguardia (O3, nuevo brazo, decide David).** Escenario a tener en cuenta: **algunos guardias se quedan
peleando y el resto huye**. La retaguardia se sacrifica o aguanta lo suficiente para que el resto rompa el contacto.
Medir cuántos salen vivos del total frente a pelear todos y huir todos, contra vindicadores (a pie) y en el caso del
grupo herido tras una misión. Paso 1 técnico: dar la orden de huida solo a una parte del bando azul (si las órdenes por
unidad ya funcionan tras F16f, usarlas; si no, una etiqueta de simulación que deje K guardias fuera de la orden).
Elegir qué guardias se quedan (escudos, los más sanos, o los más heridos que no llegarían) es parte de la ficha.
Con esto O3 deja de estar estancado: es una idea nueva de David, no un 4.º intento de lo mismo.

## Orquestador 2026-11-16 (turno 2; modelo Fable)

Veredictos leídos: **F16f** (el diagnóstico de dimensión se confirma: `rf:step` 9-22 donde antes era 1, regresión en
verde; las unidades ya saltan a RETIRARSE pero no se alejan más que las que aguantan: capacidad no cerrada) y **F21**
(candidatos sin confirmar: Tremorzilla con 16 escudos 84 % de victorias frente a 22 %; Ignis con 2 escudos + 6 arcos
4,8 vivos de 8 frente a 3,0). Consecuencias:
- **O3 se reabre** (intentos 0, estancado no). Las rondas 52, 55 y 55b dieron la orden con `instruction:RETIRARSE`
  por `InstructionRunner.start`, el camino que moría tras un paso: midieron una retirada de un segundo. No valen como
  intentos. Además David aportó una idea nueva (retaguardia, 2026-11-16).
- O7 sigue en 1 de 7, intentos 1 (F16f avanzó la capacidad; no cuenta como intento fallido).
- O5 y O6: cifras sin cambiar hasta la confirmación (F22). Ningún objetivo estancado: no toca abrir issue.
- `REVISION_2026-11-17.md` actualizada con F21 y F16f.

Dos fichas, las dos sin Java, en UN turno de ejecutor (1.º F22, 2.º F19): la confirmación de mayor valor (O5, O6) y la
de O1, que lleva dos días escrita sin correr.

**Plan del próximo turno de Fable (no despachado hoy; orden "de lo técnico al escenario"):**
1. O3 paso 1, sin Java: repetir `t52-escape-vindicadores80`, `t52-escape-devastadores8` y `t52-escape-vindicadores32`
   (pelear / retirada / huida; n = 30, seed 5201, las mismas semillas que la ronda 52 v2) ahora que la instrucción
   sigue viva. Es la línea de base real de O3; el analista mira además `rf:step` para probar que la orden duró toda
   la pelea. Sin esa base, la retaguardia no tiene con qué compararse.
2. O3 paso 2 (idea de David): retaguardia. Etiqueta de sim `rearguard:K:<quién>` que deja K guardias fuera de la
   orden de huida (`quién` = `shield` | `healthy` | `hurt`); brazos K = 2 y 4 de 8 contra pelear todos y huir todos;
   métrica: vivos del total y vivos entre los que huyen; casos: 80 vindicadores y grupo ya herido (`blueHealth:`).
3. O7: calibrar el salto de RETIRARSE por unidad (F16f: saltan pero quedan a 5-8 m del enemigo). Antes de otra ficha,
   leer `InstructionInterpreter` (umbrales near/far, a dónde manda el paso de retirada): regla del 2.º fallo.

### F22 [EJECUTOR lanza, ANALISTA lee] Jefes: confirmar la composición y ver si depende del equipo (sin Java) → O5 y O6
- Hecho (F21, n = 30 pareado, seed 6101): Tremorzilla, 16 guardias netherita+: `escudos` deja al jefe con 33,9 de
  vida frente a 131,6 de la línea 8+8 (z −5,6), 84 % de victorias frente a 22 %, 10,1 vivos frente a 2,9. Ignis, 8
  guardias: `1a3` (2 escudos + 6 arcos) 4,83 vivos frente a 3,03 (z +4,9); `arcos` 4,45 (z +3,7); ambos 97 %.
- Hipótesis: (a) el efecto se repite con semillas nuevas; (b) contra Tremorzilla el muro de escudos funciona porque
  el escudo para el rayo, no por la armadura: con netherita o diamante debería seguir muy por encima de la línea
  netherita+ (si cae al nivel de la línea, la regla del manual es "escudos **y** netherita+", no "escudos").
- Brazos (generador nuevo `gen-jefes62`, copia exacta de los escenarios `t61-*` cambiando solo lo indicado):
  - preset `t62-tremor-conf`: `t62-tremor-linea` (control, = `t61-tremor-linea`), `t62-tremor-escudos`
    (= `t61-tremor-escudos`), `t62-tremor-escudos-netherita` y `t62-tremor-escudos-diamante` (como `escudos`, cambiando
    solo la etiqueta `tier:` a `tier:netherite` y `tier:diamond`);
  - preset `t62-ignis-conf`: `t62-ignis-linea` (control), `t62-ignis-1a3`, `t62-ignis-arcos` (= los `t61-ignis-*`).
- Lote: n = 30 por brazo, mismas semillas en todos los brazos de cada preset, **`seed=6201`** (nuevas),
  `mods=bestiario`. 210 corridas. No cambia ningún valor por defecto: sin regresión.
- Métrica principal (diferencia pareada por semilla contra el control, **solo filas con la semilla original**; los
  reintentos fuera): Tremorzilla = `red.healthLeft`; Ignis = `blue.survivors`. Secundarias: % victorias con EE,
  vivos, segundos, `hurt:tremorzilla_beam`, `kit:*` (comprobar el nivel de equipo de cada brazo), `death:*`.
- Veredicto, dicho antes:
  - **Confirma** un brazo si repite la mejora por > 2,5 EE de la diferencia pareada y (Tremorzilla) victorias ≥ las
    del control, (Ignis) victorias ≥ 90 %. Entonces la cifra del objetivo es la del brazo sobre los dos lotes juntos
    (n = 60): O5 = % victorias de `escudos`; O6 = % vivos del mejor brazo de Ignis. Intentos 0.
  - Si no se repite: F21 fue ruido de semilla; cifras sin cambiar, O5/O6 intentos +1.
  - Nivel de equipo: dar la curva (diamante, netherita, netherita+) de vida restante, victorias y vivos; decir si
    `escudos` con netherita o con diamante sigue > 2,5 EE mejor que la línea netherita+. Va al manual tal cual.
  - Aunque O5 llegue a ≥ 80 %, la pregunta no se cierra con las victorias: anotar vivos y segundos, que son lo que
    comparará la siguiente ficha (16 escudos no hacen daño a distancia: ¿cuánto tarda y cuántos caen?).
- Modelo: ejecutor (generar, commit, lanzar) + analista.

### F19: se despacha ahora (texto arriba, sin cambios). Va 2.º en el mismo turno del ejecutor que F22.
- Estado F22: lanzado 2026-11-16 · `t62-tremor-conf,t62-ignis-conf` (mods=bestiario, seed=6201, n = 30; 7 brazos,
  210 corridas) · commit cd3156d · run 43957324219. Generador `gen-jefes62` (copia exacta de los `t61-*`; los brazos
  `-netherita` y `-diamante` cambian solo `tier:`). Nota: la etiqueta `resp:netherita+` queda igual en esos dos
  brazos; es un rótulo que el Java no lee (el equipo lo fija `tier:`; el analista lo comprueba con `kit:tier:*`).
- Estado F19: lanzado 2026-11-16 · `t48-distancia-pillagerpax32-flat` (mods=bestiario, igual que el lote original
  `20261111-215324`, según su `mods.txt`; seed=5901, n = 30, 6 brazos) · commit cd3156d · run 43957328647. El preset
  seguía generado: no se regeneró. Turno de ejecutor con modelo Fable (no Sonnet/Opus): solo lanzó, no esperó lotes.

### F19: Veredicto (analista, 2026-11-16; lote `20261116-211426-t48-distancia-pillagerpax32-flat`, commit cd3156d)
Datos: `tools/torneo-data/t48b/pillagerpax32-flat.jsonl`. n = 30 por brazo, semillas 5901–5930 iguales en los 6
brazos, 0 corridas por tiempo, 0 reintentos. 8 guardias contra 32 Pax (768 de vida en total), llano.

| Brazo | Victorias ± EE (r48) | Vivos de 8 | s ganar / perder | Vida Pax restante | Flechas | Puntería | Tiros por guardia-s | Daño recibido (por s) | Dist. al enemigo |
|---|---|---|---|---|---|---|---|---|---|
| arco, con regla (**O1**) | **77 ± 8** (57 ± 9) | 2,5 | 38 / 30 | 22 (en derrotas 92; 8,4 Pax) | 138 | 73 % | 0,73 | 197 (5,4) | 38,9 |
| arco, sin regla | 0 (0) | 0 | – / 7 | 447 | 32 | 91 % | 1,49 | 198 (27) | 27,7 |
| línea, con regla | 97 ± 3 (97) | 6,0 | 26 / 30 | 6 | 68 | 65 % | 0,42 | 71 (2,7) | 31,5 |
| línea, sin regla | 33 ± 9 (43 ± 9) | 1,3 | 28 / 24 | 62 | 22 | 91 % | 0,23 | 197 (7,8) | 16,4 |
| línea + scutum, con regla | 97 ± 3 (100) | 6,7 | 26 / 37 | 0 | 66 | 67 % | 0,41 | 58 (2,2) | 31,1 |
| línea + scutum, sin regla | 87 ± 6 (87) | 2,8 | 32 / 31 | 7 | 22 | 92 % | 0,19 | 179 (5,7) | 13,8 |

- **¿Se reproduce el 57 %?** Sí por el criterio de la ficha: 77 ± 8 está dentro de 57 ± 2,5 EE (34–80; diferencia
  +20, 1,7 EE de la diferencia). **La cifra de O1 no se corrige** (sigue 57); los dos lotes juntos dan 67 ± 6 %
  (n = 60, semillas distintas y builds distintos: 11-11 y cd3156d). Los otros cinco brazos repiten la ronda 48 dentro
  de 1 EE. Intentos de O1: 0 (ficha de telemetría, no cuenta). No cambia ningún valor por defecto; sin regresión.
- **Impactos perdidos: no son el sumidero.** `wastedHits` 0 % en los 6 brazos; `iframeHits` 2–3 % de los impactos
  (arco con regla: 3,0 de 101; 23 de 760 de daño).
- **De qué mueren:** 100 % balas de Scorched Guns (`hurt:scguns.bullet.*`), en todos los brazos; ningún cuerpo a
  cuerpo, ningún otro tipo. Moral: 0 rotos, 0 retiradas.
- **Brazo de O1 (arco con regla), dónde se va la pelea:**
  1. **Daño recibido a 39 bloques.** La regla baja el daño recibido de 27 a 5,4 de vida por segundo, pero no lo
     corta: los 8 arqueros reciben los mismos ~197 en total, solo que en 36 s en vez de 7. Es lo que más separa
     victoria de derrota: daño recibido por segundo contra vida Pax restante, r = +0,64 (ganadas 187 en 38 s,
     perdidas 227 en 30 s). Mantener la distancia (38,7 contra 39,0) y la puntería (r = 0,0) no separan nada.
  2. **Cadencia a la mitad.** 0,73 tiros por guardia-segundo con la regla contra 1,49 sin ella; 57 % de los tiros
     se hacen en movimiento. `act:noShot:iframeHold` = 93 por corrida (3,7 sin regla): el fuego sincronizado retiene
     el tiro mientras el blanco está en ventana. Unidad = intentos, no tiros perdidos: no se puede convertir a daño.
  3. **Puntería 73 % contra 91 %** (37 flechas de 138 no dan). De esas, 18 por corrida son flechas paradas porque
     iban a dar a un compañero (`friendlyFireBlue` = 18,1; 0,4 sin regla): la mitad de los fallos.
  4. **Las derrotas son por poco:** quedan 8,4 Pax de 32 (92 de vida, 12 % del total = unos 12 impactos, 2–3 s de
     fuego de 8 arqueros). Dispersión: `nnDist` 7,2 en derrotas contra 5,1 en victorias (r = +0,28, débil).
- **Por qué gana la línea:** los 4 escudos se llevan el 77 % de la atención enemiga (`aggroShield` 215 s contra 65)
  y paran 963–1079 de daño por corrida (`act:shieldBlockedDmg`), el 93 % de lo que llega; el daño recibido cae a
  58–71. Con la regla además: 6,0–6,7 vivos contra 1,3–2,8.
- **Mayor sumidero → primera ficha de diseño de Fable para O1:** las balas que siguen llegando a 39 bloques a
  arqueros sin nada que las pare (no los impactos perdidos ni la distancia media). Dos palancas que los datos
  señalan, sin decidir cuál: (a) cortar daño recibido (más distancia que 39, o algo que pare balas); (b) recuperar
  cadencia y línea de tiro (18 flechas paradas por compañero + tiros retenidos por `iframeHold`). Las derrotas son
  por ~12 impactos: cualquiera de las dos podría bastar, hay que medirlo.
- **Lo que este lote NO puede decir (falta la medida):** a qué distancia se recibe cada bala y cuántas dispara el
  enemigo (`red.shots` y `red.hits` salen 0: las armas de fuego no se cuentan); "muertes por bala a distancia X" no
  se puede calcular. En los brazos de línea el bando azul hace 196–359 de daño pero los Pax pierden 706–768: el resto
  (~400) no está atribuido en `dmgDealt`/`blue.damage` (en los brazos de arco sí cuadra: 760 contra 746); no sé de
  dónde sale, pide una medida antes de leer `dps` en brazos con espadas.
- Control de velocidad: la semilla 5901 de arco con regla la gana el rojo en sprint y el azul a velocidad normal
  (1 corrida; anotado, no concluye nada).
- Mueve objetivos: no. O1 sigue 57 % (conjunto 67 ± 6, n = 60), meta ≥ 85.

## Orquestador 2026-11-16 (turno 3; modelo Fable)

Veredicto leído: **F19** (O1, telemetría; no cuenta como intento). 77 ± 8 % con semillas nuevas, dentro de 2,5 EE del
57 %: la cifra de O1 no se corrige (conjunto 67 ± 6, n = 60). F22 (O5/O6) sigue corriendo (run 43957324219): cifras
sin cambiar. Ningún objetivo estancado; no toca abrir issue.

**Lectura de Fable sobre F19 (código + filas, antes de diseñar):** la regla de distancia pide 48–56 bloques
(`FieldManual.STANDOFF_MIN/FAR`; la aprendida está apagada), pero los arqueros pelean a 38,9 de media y repiten
`act:standOff` 146 veces por corrida (4 por guardia y segundo: no dejan de retroceder nunca). No llegan a 48 porque no
caben: la arena por defecto mide 96 (`simArenaSize`), los azules salen a 15 del centro y tienen 33 bloques hasta el
muro; con los Pax avanzando, 48 solo existe con la espalda en la pared. Es la duda que Fable anotó en la ronda 48
("48-56 puede ser un efecto de la arena") y que el brazo de terreno no contestó porque todos usan la misma arena de
96. Consecuencias que cuadran con las filas: 57 % de los tiros en movimiento, grupo apelotonado contra el muro (18
flechas por corrida paradas por un compañero) y balas que llegan igual. Segunda palanca, aparte: `iframeHold` = 93
reintentos por corrida; cada tiro retenido cuesta 10 ticks (`RangedFireGoal.tickInner`), unos 46 guardia-segundos de
~190, cuando las derrotas son por ~12 impactos. `fire:sync` se hizo valor por defecto midiendo contra esqueletos y
hordas, no contra pistoleros a los que hay que matar antes de que maten.

Dos fichas, las dos sin Java, en UN turno de ejecutor (1.º F23, 2.º F24), de objetivos distintos (O3 y O1).

### F23 [EJECUTOR lanza, ANALISTA lee] O3 paso 1: la base real de la retirada, con la instrucción viva (sin Java) → O3
- Hecho: las rondas 52 v2, 55 y 55b dieron `instruction:RETIRARSE` por el camino que moría tras un paso (arreglado en
  F16f, commit 0242c3a). O3 no tiene cifra válida.
- Hipótesis: con la orden viva toda la pelea, `retirada` y `huida` llegan al refugio (antes `atRefuge` ≤ 5 %) y, contra
  lo que no se gana (80 vindicadores con línea o caballería), dejan más vivos que pelear.
- Tarea: sin cambiar nada, relanzar `t52-escape-vindicadores80`, `t52-escape-devastadores8` y
  `t52-escape-vindicadores32` (9 brazos cada uno: línea/arco/caballería × pelear/retirada/huida), **seed 5201** (las
  mismas semillas que la ronda 52 v2: se compara pareado con `tools/torneo-data/r52v2/`), n = 30, `mods` igual que el
  lote original (run 43596851492; si no consta, `""`: son mobs vanilla). 810 corridas. Si un preset ya no está
  generado, regenerarlo con `gen-escape52` y decirlo. No cambia valores por defecto: sin regresión.
- Métrica principal: `blue.survivors` (% vivos de 8), diferencia pareada por semilla de `retirada` y de `huida` contra
  `pelear` dentro de este lote, solo filas con la semilla original (reintentos y "sin enfrentamiento" aparte, contados).
  Secundarias: `atRefuge`, `reachedRefuge`, fuera de contacto, segundos, `meanEnemyDist`, y las claves `rf:*`
  (pasos de la instrucción por corrida).
- Veredicto, dicho antes:
  - **Prueba de vida** (va primero): la instrucción da pasos durante toda la pelea (`rf:*` con ≥ 5 pasos en ≥ 90 % de
    las corridas de `retirada`/`huida`). Si no, la ficha no mide nada: decirlo y parar ahí (regla del 2.º fallo: turno
    de lectura de Fable, no otra ficha).
  - Tabla nueva de % vivos y % en el refugio al lado de la de r52v2 (mismas semillas): qué cambió al arreglar la orden.
  - O3 **se cumple** en un caso si retirada o huida > pelear por > 2,5 EE de la diferencia pareada **y** `atRefuge`
    ≥ 50 %. Se fija la cifra de O3 con lo que salga (aunque sea "no"): es la base contra la que se medirá la
    retaguardia de David (paso 2). Como es base, intentos de O3 = 0 salga lo que salga.
  - Si llegan al refugio pero mueren allí (vindicadores a pie los siguen): decirlo con `atRefuge` frente a vivos; es
    justo el caso para el que David propuso la retaguardia.
- Modelo: ejecutor (lanzar) + analista.
- Estado: lanzado 2026-11-16 · presets `t52-escape-vindicadores80`, `t52-escape-devastadores8`,
  `t52-escape-vindicadores32` · seed 5201 · `mods=bestiario` (el lote original, run 43596851492, usó `bestiario`:
  comprobado en su registro) · commit 1450d14 · run 43961664012. Los tres presets seguían generados: no hizo falta
  `gen-escape52`. Sin cambios de código en la ronda 52.
- Veredicto (2026-11-17): escrito abajo en «F23: Veredicto». O3 no se cumple; `retirada` viva, `huida` sin prueba de vida posible.

### F24 [EJECUTOR genera y lanza, ANALISTA lee] O1: ¿el 57 % es el muro de la arena? Sitio para la distancia y fuego sin retener (sin Java) → O1
- Hecho: ver la lectura de arriba (F19 + código): la regla quiere 48–56 y la arena de 96 solo deja ~39.
- Hipótesis: (a) con sitio para retroceder (arena de 160; "en abierto" es justo eso) los arqueros llegan a 48+,
  reciben menos daño por segundo y ganan bastante más; (b) contra pistoleros, soltar el tiro en vez de retenerlo
  (`fire:spread`) o no mirar ventanas (`fire:off`) da los ~12 impactos que faltan en las derrotas.
- Brazos (generador nuevo `gen-o1-63`; cada escenario es copia exacta de `t48-distancia-pillagerpax32-flat-arco-regla`
  cambiando solo lo indicado). El tamaño de arena se fija por lote (máximo de las etiquetas `arena:` del lote,
  `SimRunner`), así que van en **dos presets**:
  - `t63-o1-arena96`: `t63-o1-a96-sync` (control, sin cambios), `t63-o1-a96-spread` (+ `fire:spread`),
    `t63-o1-a96-off` (+ `fire:off`);
  - `t63-o1-arena160`: los mismos tres con `arena:160` (`t63-o1-a160-sync`, `-spread`, `-off`).
  Todos con `measuredist`. n = 30, **seed 6301** en los dos presets (mismas semillas en los 6 brazos),
  `mods=bestiario`. 180 corridas. Lanzar los dos presets como lotes separados si un solo `build.yml` con los dos
  mezclara el tamaño de arena (comprobarlo en `SimRunner`: el tamaño se calcula por lote; si los presets de un mismo
  run comparten lote, lanzar dos runs).
- Métrica principal: `red.healthLeft` (vida Pax restante; 0 = victoria), diferencia pareada por semilla contra
  `a96-sync`. Secundarias: % victorias con EE, vivos, segundos, `meanEnemyDist`, `act:standOff`, daño recibido por
  segundo (`red.damage` / segundos), `shotsMoving`/`shotsStill`, `friendlyFireBlue`, `act:noShot:*`, flechas,
  `iframeHits`, y comprobar el tamaño real de arena del lote si consta en el informe.
- Veredicto, dicho antes:
  - **Arena**: si `a160-sync` mejora a `a96-sync` por > 2,5 EE de la diferencia pareada y `meanEnemyDist` sube a
    ≥ 46, el 57 % era en buena parte el muro: dar la cifra en 160 (n = 30, candidata; confirmar con semillas nuevas
    antes de fijarla). Si `meanEnemyDist` no sube aunque haya sitio, el freno no es el muro sino que los Pax avanzan
    tan rápido como los arqueros retroceden: decirlo (la siguiente ficha sería de la regla, no de la arena).
  - **Fuego**: `spread` y `off` contra `sync` dentro de cada arena, mismo criterio (> 2,5 EE pareado). Un brazo que
    gane en las dos arenas es candidato a regla "contra pistoleros no se retiene el tiro" (necesitaría Java mínimo y
    n = 30 de confirmación + regresión antes de ser valor por defecto). Si empeora o no se distingue: palanca
    descartada, anotarlo.
  - Ningún brazo cambia valores por defecto. Intentos de O1: 0 si algún brazo es candidato (> 2,5 EE); si ninguno,
    intentos +1.
- Nota para David (se anota en decisiones pendientes, no bloquea): si la arena explica la cifra, O1 "en abierto"
  debería medirse en la arena de 160; cambiar la medida de un objetivo lo decide David.
- Modelo: ejecutor (generar, commit, lanzar) + analista.
- Estado: lanzado 2026-11-16 · presets `t63-o1-arena96`, `t63-o1-arena160` · seed 6301 · `mods=bestiario` · commit
  1450d14 · run 43961690967. Generador `gen-o1-63` en `tools/torneo.py`. Un solo run con los dos presets: `build.yml`
  da una máquina por preset y `SimRunner` calcula la arena por lote (un lote = un preset), así que no se mezclan
  los tamaños. Lectura elegida (la más simple): el control `sync` va sin etiqueta `fire:` (copia exacta; `syncFire`
  = true es el valor del juego) y los brazos de 96 van sin etiqueta `arena:` (96 es `simArenaSize` por defecto).

**Sigue pendiente para Fable** (no despachado): O3 paso 2 (retaguardia, tras F23); O7 (leer `InstructionInterpreter`
antes de calibrar el salto de RETIRARSE por unidad); cifras de O5/O6 al llegar F22.

### F23: Veredicto (analista, 2026-11-17; lotes `20261116-213852-t52-escape-vindicadores80`, `20261116-213838-t52-escape-devastadores8`, `20261116-213840-t52-escape-vindicadores32`, commit 1450d14)
Datos: `tools/torneo-data/f23/` (al lado, `r52v2/` con las mismas semillas). n = 30 por brazo, semillas 5201–5230; todas
las cifras son de las **filas con la semilla original**; reintentos aparte (0–2 por brazo; en `huida` a pie 8–22). 8 guardias.

**Prueba de vida: `retirada` SÍ; `huida` no se puede comprobar con lo que hay.** Las claves `rf:*` que pedía la ficha
**no salen en estos presets**: solo se escriben con unidades de `unitInstr` (`SimExtras.unitRf` sale si `UNIT_OF` está
vacío), y `t52-escape` usa la instrucción general. Se usa lo que sí consta por corrida: decisiones con motivo
`INSTRUCTION` (`reasons`) y `act:retreat`.
- `retirada` (9 brazos): `INSTRUCTION` ≥ 5 en 270 de 270 corridas (mínimo 16, medianas 40–194; en r52v2 eran 4–8, una
  por guardia) y `act:retreat` ≥ 5 en 270 de 270 (en r52v2: 0). La orden ahora vive toda la pelea. Cumple (≥ 90 %).
- `huida` (`retreat:flee`): `INSTRUCTION` sigue en 4–8 por corrida (una por guardia, igual que en r52v2) y `act:retreat`
  = 0. Es un modo de una sola orden (`RETREAT_TO_POINT`), así que estas claves no distinguen "viva" de "un paso". Lo
  que sí cambió: ahora algún guardia pisa el refugio en casi todas las corridas (ver `llegó`), el doble que antes.

| Contra | Comp. | Brazo | % victorias | % vivos (r52v2) | dif. vs pelear (EE; z) | % en refugio al final (r52v2) | % llegó al refugio (r52v2) | s | cortadas |
|---|---|---|---|---|---|---|---|---|---|
| 80 vind. | línea | pelear | 13 | 13,8 (13,3) | – | 0 (0) | 10 (11) | 108 | 0 |
| | | retirada | 13 | 8,8 (20,0) | −5,0 (3,1; −1,6) | 0 (0) | 17 (15) | 86 | 2 |
| | | huida | 0 | 15,8 (19,6) | +2,1 (2,9; +0,7) | 0 (0) | 40 (20) | 89 | 22 |
| 80 vind. | arco | pelear | 97 | 67,1 (75,4) | – | 0 (0) | 30 (30) | 56 | 0 |
| | | retirada | 93 | 49,2 (41,2) | −17,9 (6,3; −2,8) | 3 (0) | 45 (35) | 69 | 0 |
| | | huida | 0 | 18,3 (26,2) | −48,8 (5,1; −9,6) | 1 (0) | 75 (35) | 103 | 15 |
| 80 vind. | cab. | pelear | 3 | 3,3 (1,7) | – | 0 (0) | 9 (9) | 71 | 0 |
| | | retirada | 0 | 0,4 (0,0) | −2,9 (1,5; −1,9) | 0 (0) | 12 (12) | 46 | 0 |
| | | huida | 0 | 0,0 (0,4) | −3,3 (1,5; −2,3) | 0 (0) | 8 (2) | 20 | 0 |
| 8 devast. | línea | pelear | 100 | 69,2 (67,9) | – | 0 (2) | 6 (9) | 33 | 0 |
| | | retirada | 93 | 58,8 (43,3) | −10,4 (3,7; −2,8) | 10 (0) | 20 (8) | 54 | 2 |
| | | huida | 0 | 24,6 (30,8) | −44,6 (4,8; −9,2) | 9 (3) | 43 (10) | 104 | 8 |
| 8 devast. | arco | pelear | 100 | 100,0 (100,0) | – | 2 (6) | 8 (9) | 22 | 0 |
| | | retirada | 97 | 99,6 (95,4) | −0,4 (0,4; −1,0) | 6 (1) | 17 (11) | 26 | 1 |
| | | huida | 0 | 57,9 (73,8) | −42,1 (4,3; −9,9) | 29 (4) | 71 (19) | 113 | 10 |
| 8 devast. | cab. | pelear | 97 | 44,6 (48,8) | – | 0 (0) | 16 (19) | 41 | 0 |
| | | retirada | 100 | 47,9 (37,1) | +3,3 (2,3; +1,4) | 8 (0) | 30 (14) | 39 | 0 |
| | | huida | 0 | 0,0 (0,4) | −44,6 (1,9; −23,9) | 0 (0) | 47 (11) | 36 | 0 |
| 32 vind. | línea | pelear | 100 | 49,6 (50,0) | – | 1 (3) | 9 (16) | 39 | 0 |
| | | retirada | 100 | 49,6 (44,2) | 0,0 (0,6; 0,0) | 2 (0) | 21 (13) | 39 | 0 |
| | | huida | 0 | 20,0 (26,7) | −29,6 (1,9; −15,2) | 0 (0) | 38 (19) | 92 | 22 |
| 32 vind. | arco | pelear | 100 | 99,6 (99,6) | – | 8 (7) | 21 (16) | 24 | 0 |
| | | retirada | 100 | 100,0 (98,8) | +0,4 (0,4; +1,0) | 11 (4) | 14 (13) | 22 | 0 |
| | | huida | 0 | 47,5 (59,2) | −52,1 (4,7; −11,1) | 8 (3) | 76 (42) | 106 | 12 |
| 32 vind. | cab. | pelear | 100 | 40,0 (44,2) | – | 0 (1) | 9 (10) | 45 | 0 |
| | | retirada | 93 | 36,2 (25,0) | −3,8 (3,7; −1,0) | 3 (0) | 11 (8) | 42 | 0 |
| | | huida | 0 | 0,0 (0,0) | −40,0 (2,2; −18,2) | 0 (0) | 19 (3) | 27 | 0 |

"Cortadas" = corridas que la sim paró por "sin enfrentamiento" (`stuck`), contadas dentro del n = 30.

- **O3 no se cumple en ningún caso** (0 de 18 brazos con orden). Ninguno supera a `pelear` por > 2,5 EE (el mejor:
  caballería en retirada contra 8 devastadores, +3,3 puntos, z +1,4) y ninguno llega a 50 % en el refugio (máximo 29 %,
  arco en huida contra devastadores, que pierde 42 puntos de vivos). **Cifra base de O3: contra 80 vindicadores, el
  mejor brazo con orden deja 49 % vivos (arco, retirada) frente a 67 % peleando, y 3 % en el refugio; con línea 9–16 %
  vivos frente a 14 % peleando, 0 % en el refugio.** Intentos de O3 = 0 (es la base).
- **Qué cambió al arreglar la orden (frente a r52v2, mismas semillas).** `retirada`: sube en 6 de 9 casos (+4 a +16
  puntos de vivos; p. ej. línea contra devastadores 43 → 59, caballería contra 32 vindicadores 25 → 36) no cambia en dos (≤ +1) y baja en
  línea contra 80 (20 → 9); queda igual que pelear donde ya se gana y peor donde el enemigo es mucho (arco contra 80: −18,
  z −2,8; línea contra devastadores: −10, z −2,8). `huida`: llega al refugio el doble o más (38–76 % a pie; antes
  10–42 %), pero deja **menos** vivos que en r52v2 en los 6 casos a pie (−4 a −16 puntos) y sigue en 0 % con caballería.
  Aviso de ruido: `pelear` no cambió de código y aun así difiere de r52v2 hasta 8 puntos (arco contra 80: 67 frente a
  75): las mismas semillas no dan la misma pelea, así que diferencias de ese tamaño entre lotes no cuentan.
- **Llegan y no se quedan (el caso de la retaguardia de David).** En `huida` algún guardia llega al refugio en 30 de 30
  corridas a pie (media 17–28 s; la orden sale a los 10 s) y al final hay 0–9 % dentro (29 % el mejor) y 0–40 % fuera
  de contacto. Salidas del refugio por brazo de 30 corridas: 73–197 con último peldaño `RETREAT` y sin objetivo
  (huida a pie), frente a 3–17 por tener objetivo. En `retirada` es al revés: salen con objetivo (`target:vindicator`
  / `target:ravager`, 15–115) o por `HOLD_FORMATION` (línea y arco contra devastadores, 37 y 20). Los datos no dicen
  **por qué** el peldaño `RETREAT` los saca del refugio en huida (¿el enemigo llega detrás y retroceden de nuevo?, ¿el
  punto de repliegue se mueve?): eso es lectura de código, no de este lote.
- **Las corridas cortadas pesan en `huida` a pie** (8–22 de 30 por brazo): la pelea se para por "sin enfrentamiento"
  con enemigos y guardias aún vivos; el % de vivos de esos brazos es, si acaso, optimista (los que quedan no
  habían terminado de morir ni de salvarse).
- Mueve objetivos: **O3 pasa de "sin base válida" a base medida: no se cumple** (retirada ≤ pelear, refugio ≤ 29 %).
  Nada cambia de valor por defecto; sin regresión (la ficha no la pedía).
- Pido al orquestador (Fable): (1) antes del paso 2, leer por qué en `RETREAT_TO_POINT` los guardias salen del refugio
  con peldaño `RETREAT` (3–7 salidas por corrida) y si `refuge:50` en arena de 160 da sitio para perder a un vindicador
  que corre igual que un guardia; (2) la retaguardia de David se mide contra esta tabla (base: arco contra 80, 49 %
  vivos y 3 % en refugio; línea contra 80, 9–16 % y 0 %); (3) una prueba de vida para la instrucción general: las
  `rf:*` solo existen con `unitInstr`, haría falta el mismo contador para el grupo entero (Java mínimo, solo sim).

### F22: Veredicto (analista, 2026-11-16; lotes `20261116-211351-t62-tremor-conf` y `20261116-211345-t62-ignis-conf`, commit cd3156d)
Datos: `tools/torneo-data/t62/`. n = 30 por brazo, semillas 6201–6230 iguales en los brazos de cada preset. Cifras de
las **filas con la semilla original** (regla de la ficha); entre paréntesis, lo que sale si el reintento sustituye a
la corrida cortada por "sin enfrentamiento". Entre el lote de F21 y este el Java solo cambia `InstructionRunner`.

**Tremorzilla, 16 guardias** (métrica principal: vida del jefe al acabar, diferencia pareada contra `linea`):

| Brazo | Victorias ± EE | Vivos de 16 | Vida jefe restante | Dif. pareada (z) | s (en victorias) | Daño hecho | Daño recibido: rayo / golpes | Muertes por rayo |
|---|---|---|---|---|---|---|---|---|
| `linea` 8+8, netherita+ (control) | 17 ± 7 (23) | 2,7 | 160 (130) | – | 130 (69) | 893 | 244 / 112 | 10,1 |
| `escudos`, netherita+ | **70 ± 8** (77) | **10,0** | **69** (49) | **−91,0 (z −3,3)** (−81,5; z −3,2) | 136 (94) | 1045 | 114 / 115 | 3,4 |
| `escudos`, netherita sin encantar | 0 (0) | 0,5 (0) | 151 (121) | −9,1 (z −0,4) | 125 | 658 | 235 / 329 | 13,1 |
| `escudos`, diamante | 0 (0) | 0,3 (0) | 178 (165) | +18,5 (z +0,9) | 109 | 496 | 251 / 311 | 13,3 |

**Ignis, 8 guardias** (métrica principal: guardias vivos, diferencia pareada contra `linea`; 0 reintentos):

| Brazo | Victorias | Vivos de 8 ± EE | Dif. pareada (z) | Vida azul restante | s | Dif. s (z) | Muertes: llamarada / golpe |
|---|---|---|---|---|---|---|---|
| `linea` 4+4 (control) | 100 % | 3,27 ± 0,15 | – | 56 | 88 | – | 2,7 / 2,0 |
| `1a3` (2 escudos + 6 arcos) | 100 % | **4,73 ± 0,18** | **+1,47 (z +7,7)** | 78 | 82 | −5,7 (z −1,2) | 1,1 / 2,1 |
| `arcos` (8 arcos) | 100 % | **4,83 ± 0,26** | **+1,57 (z +5,3)** | 80 | 77 | −10,9 (z −3,0) | 0,7 / 2,4 |

- **(a) ¿Se repite con semillas nuevas? Sí, los tres brazos confirman** (> 2,5 EE pareado y condición de victorias):
  `escudos` z −3,3 (F21: −5,6) con 70 % frente a 17 % del control; `1a3` z +7,7 y `arcos` z +5,3 con 100 % de victorias.
  F21 no fue ruido de semilla. Intentos de O5 y O6: 0.
- **Cifras sobre los dos lotes juntos (n = 60, semillas originales):**
  - **O5 = 77 ± 5 % victorias** con `escudos` (46 de 60; control 18 ± 5 %, 11 de 60). Con el reintento en lugar de las
    cortadas: 83 ± 5 % (50 de 60). **Con la regla de la ficha no llega a la meta (≥ 80); con la otra lectura sí.**
    Qué lectura vale para O5 lo decide el orquestador / David; yo fijo 77 % porque es la que la ficha dijo antes.
    Vivos 10,0 de 16 (62 %; control 2,6); en victorias 12,0 vivos y 94 s (control: 12,1 vivos y 70 s cuando gana).
  - **O6 = 60 % vivos** con `1a3` (4,78 ± 0,16 de 8; 98 % victorias, 82 s); `arcos` 58 % (4,65 ± 0,20; 98 %, 79 s);
    control 39 % (3,13). Los dos brazos no se distinguen entre sí en vivos; `arcos` es más rápido (z −3,0 en este
    lote). Meta ≥ 70: no llega.
- **(b) ¿Depende del equipo? Sí, del todo: la hipótesis (b) queda refutada.** Con netherita sin encantar o con
  diamante, 16 escudos ganan 0 de 30 (0 de 30 también con los reintentos) y no dejan al jefe peor que la línea
  netherita+ (z −0,4 y +0,9); en vivos quedan por debajo de ella (z −2,4 y −2,7). **La regla para el manual es
  "escudos y netherita+", no "escudos".** Lo que cambia entre brazos, medido: el escudo para lo mismo
  (`act:shieldBlockedDmg` 550 / 590 / 512), pero lo que pasa hace el doble o el triple de daño (rayo 114 → 235–251;
  golpes 115 → 329–311) y mueren por rayo 13 de 16 en vez de 3,4; el daño hecho baja de 1045 a 658 y 496. Netherita
  y diamante sin encantar casi no se separan: el salto está en los encantamientos. `tier:netherite+` pone a la vez
  Protección IV y Filo V: **este lote no dice cuál de los dos lleva el efecto** (haría falta `ench:protection` /
  `ench:sharpness` por separado, como la ronda 31).
- **Comprobación del equipo:** `kit:shield` 16 y espada 16 en los tres brazos de escudos (`diamond_sword` en
  diamante, `netherite_sword` en los otros dos); control 8 + 8. **`kit:tier:*` no sale en ninguna fila** (el contador
  de `SimRunner.applyTier` no llega al informe), así que netherita frente a netherita+ solo se comprueba por la
  etiqueta del escenario (`tier:netherite` / `tier:netherite+`, leída en `scenarios.json`) y por el efecto.
- **Corridas cortadas ("sin enfrentamiento"): más que en F21 y pesan en la cifra.** Con Tremorzilla hay 4–7 por brazo
  (F21: 1–2). En todas, el corte llega tras una pelea real (500–1350 de daño hecho, 140–290 s), con 1–8 guardias
  vivos con poca vida y el jefe con 236–279 de vida; cuentan como no victoria. En `escudos` son 5 cortadas + 2 por
  tiempo (300 s, 5 y 8 vivos, jefe a 260–269). No sé por qué el jefe acaba con tanta vida después de recibir más de
  1000 de daño (no hay medida de curación del jefe): pide una antes de leer más estas filas. De los 5 reintentos de
  `escudos`: 2 victorias, 1 derrota, 2 cortadas otra vez.
- **Lo que queda para la siguiente ficha (la ficha pidió anotarlo):** 16 escudos sin arcos ganan en 94 s con 12,0
  vivos; la línea, cuando gana, lo hace en 70 s con 12,1 vivos. Las no victorias de `escudos` (14 de 60) son sobre
  todo peleas que se alargan y se cortan (9 empates), no derrotas (5).
- Control de velocidad: Tremorzilla, semilla 6201, empate en sprint y derrota a velocidad normal; Ignis, victoria en
  las dos (4 vivos en sprint, 1 a velocidad normal). 1 corrida cada uno: anotado, no concluye nada.
- No cambia ningún valor por defecto; sin regresión. Mueve objetivos: **O5 22 → 77 % (n = 60; 83 % con reintentos;
  solo con netherita+)**; **O6 44 → 60 % vivos (n = 60)**. Pedir al orquestador: (1) decidir la lectura de O5 y, si
  hace falta, medir qué pasa en las peleas cortadas (vida del jefe en el tiempo); (2) separar Protección IV de Filo V
  con 16 escudos; (3) O6: los dos brazos confirmados se quedan en 60 %, falta otra palanca para el 70 %.

## Orquestador 2026-11-17 (turno 1; modelo Fable)

**CI:** los lotes de F23 y F24 terminaron bien el 16 a las 21:39 (datos en `sim-results`, commit 1450d14: 3 carpetas
`t52-escape-*` y 2 `t63-o1-*`), pero los tres turnos de analista que debían leerlos fallaron a los 9 s
(`is_error: true` al arrancar Claude; runs 43963492013, 43963672610, 43974589590) y nadie los relanzó en 7 horas.
Relanzados ahora, uno por ficha: runs 44022078405 (F23) y 44022090610 (F24). O1 y O3 siguen sin veredicto: cifras
e intentos sin tocar.

**Veredicto leído: F22.** O5 = **77 %** (46 de 60): vale la lectura que la ficha dijo antes (semillas originales); el
83 % con reintentos no se usa. Avance confirmado (22 → 77), intentos 0; meta ≥ 80 sin cumplir. O6 = 60 % vivos,
confirmado, intentos 0; meta ≥ 70 sin cumplir. Ningún objetivo estancado: no toca abrir issue.

**Lectura de Fable 1 (O5, filas de `t62-tremor-escudos`, 35 filas):** las no victorias no son derrotas, son peleas
que se atascan. Victorias (23): 93 s, 955 de daño hecho, motivo `STUCK` 28 veces en total. Empates (9): 228–300 s,
1093–1664 de daño hecho y el jefe acaba con **264 de vida**, motivo `STUCK` 572 veces, 3–6 guardias vivos con 23–42
de vida. Es decir: el jefe recibe más daño que en una victoria y termina casi entero → **se cura**, y a la vez los
guardias (solo espadas) no lo alcanzan. Hay además `death:env:radiation` y la clave `arenaRepaired`: Tremorzilla
rompe el suelo. Hipótesis: tras el rayo queda terreno roto o el jefe queda fuera de alcance, 16 escudos sin un solo
arco no pueden pegarle, y él se cura. Si es así, el 77 % no es el techo de la composición sino un fallo de alcance
con remedio (2 arcos, o desatascar). → F26.

**Lectura de Fable 2 (O7, `InstructionRunner` + filas de F16f; regla del 2.º fallo):** RETIRARSE por unidad **hace lo
que dice su tabla**; lo que estaba mal era el criterio de F16f. (1) Los umbrales son fracciones del alcance medio de
la unidad; con 2 escudos + 2 arcos el alcance medio es ~33: `near` ≈ 10 bloques y `far` ≈ 40 (medido: `nearX10` 1338
y `farX10` 5341 sobre 13 pasos). Con el enemigo entre 10 y 40 la regla es *aguantar y disparar* un ciclo de 4 s y
luego un salto de 6 bloques: 40 % de pasos en `hold` es el diseño, no un fallo. (2) La pelea dura 19 s, la orden
llega a los 5 s y los azules ganan el 100 %: en 14 s caben 6 saltos de 6 bloques contra vindicadores que corren. (3)
"Estar más lejos del enemigo que las unidades que aguantan" no puede cumplirse cuando el enemigo persigue a quien
retrocede: eso es justo lo que quiere un cebo. No hace falta otra ficha de telemetría sobre la capacidad: se pasa al
contexto (F12 paso 2), con la prueba de vida dentro del mismo lote. → F25.

Dos fichas, objetivos distintos (O7 y O5), en UN turno de ejecutor: 1.º F25 (sin Java), 2.º F26 (Java solo de sim).

### F25 [EJECUTOR genera y lanza, ANALISTA lee] F12 paso 2: cebo y pinza contra 80 vindicadores (sin Java) → O7
- Hecho: las instrucciones por unidad persisten (F16f) y RETIRARSE salta según su tabla (lectura de arriba). F17: 16
  de línea contra 80 vindicadores nunca pierden en 2 min, pero 3 de 8 no acaban a tiempo; discriminan vivos y segundos.
- Hipótesis (idea de David, F12): los escudos aguantan y ceden terreno, la horda los sigue, y los arqueros desde el
  flanco disparan a una masa que no los mira → más vivos y menos tiempo que el choque frontal con los mismos 16.
- Brazos (generador nuevo `gen-cebo64`, copia de `gen-horde57` con 80 vindicadores; distancia 30, arena 160,
  `unit:4`, 16 guardias; **solo cambia `unitInstr`**; **4 minutos** en todos, también el control, para que las peleas
  acaben). Composición: U1 y U2 = 8 escudos (cebo), U3 y U4 = 8 arqueros. Comprobar en la sim cómo reparte `mix:` por
  orden de aparición y usar la forma que dé eso (verificar con `kit:*` por unidad si sale; si no se puede separar por
  unidad sin Java, usar `mix:shield=2,archer=2` como en la ronda 57 y decirlo):
  - `t64-cebo-todos` (control): U1–U4 `MANTENER_LINEA@0`;
  - `t64-cebo-cebo`: U1, U2 `RETIRARSE@5`; U3, U4 `MANTENER_LINEA@0`;
  - `t64-cebo-pinza`: U1, U2 `MANTENER_LINEA@0`; U3, U4 `ESPERAR@0` y `FLANQUEAR@12`;
  - `t64-cebo-cebopinza`: U1, U2 `RETIRARSE@5`; U3, U4 `ESPERAR@0` y `FLANQUEAR@12`.
- Lote: preset `t64-cebo-vindicadores80`, n = 30 por brazo, mismas semillas en los 4, **seed 6401**, `mods=""`
  (vanilla, como la ronda 57). 120 corridas. No cambia valores por defecto: sin regresión.
- Métrica principal: `blue.survivors` (de 16), diferencia pareada por semilla contra `todos`, solo semillas
  originales. Secundarias: segundos, % victorias, `blue.healthLeft`, `red.damage`, `friendlyFireBlue`, y por unidad
  `u<k>:rf:*`, `u<k>:act:*`.
- Veredicto, dicho antes:
  - **Prueba de vida** (primero): en ≥ 90 % de las corridas, U1 y U2 con `rf:step` ≥ 5 y `rf:issued:FALLBACK` ≥ 2
    en los brazos con cebo; U3 y U4 con algún `rf:issued:*` tras el segundo 12 en los brazos con pinza. Si la pinza
    no emite nada, decir qué claves salen y parar esa mitad (lectura de Fable de FLANQUEAR, no otra ficha a ciegas).
  - Un brazo **aprueba** si mejora los vivos por > 2,5 EE de la diferencia pareada sin bajar las victorias; pide
    confirmación con semillas nuevas antes de contar en O7 (entonces 2 de 7). Si `cebopinza` aprueba y `cebo` y
    `pinza` solos no, decirlo: es la combinación. Si ninguno mejora o empeoran: "cebo y pinza contra vindicadores:
    NO en este montaje", con la tabla; O7 intentos +1.
  - Si todo queda en el techo (vivos ≥ 14 de 16 en el control): "escenario sin discriminación", sin contar intento.
- Modelo: ejecutor (generar, commit, lanzar) + analista.
- **Estado: lanzado 2026-11-17 · preset `t64-cebo-vindicadores80` (seed 6401, `mods=""`) · commit 13d48a4 · run
  44022728126.** Notas del ejecutor: (1) composición separada por unidad SIN Java: `mix:shield=8,archer=8` (mixRole
  reparte por índice de aparición % 16 y `unitSetup` hace unidad = índice / 4 + 1), así U1 y U2 = 8 escudos y U3 y U4 =
  8 arqueros en los 4 brazos, también el control (no es la `mix:shield=2,archer=2` de la ronda 57: el control de F17
  no es comparable fila a fila). (2) `kit:*` sale por corrida, no por unidad (debe dar `kit:shield` 8 y `kit:bow` 8);
  la separación por unidad se comprueba con `u<k>:act:*` (disparos solo en U3 y U4). (3) Por el mismo orden de
  aparición, en la línea inicial los escudos ocupan los 8 primeros puestos y los arqueros los 8 últimos (dos mitades
  de la línea, no escudos delante y arcos detrás): tenerlo en cuenta al leer la pinza. (4) Este turno corrió con
  Fable aunque el rol es de ejecutor.

### F26 [EJECUTOR mide y lanza, ANALISTA lee] O5: por qué se atascan las peleas contra Tremorzilla (telemetría + 1 brazo) → O5
- Hecho: lectura de arriba. En `t62-tremor-escudos` los empates (9 de 35 filas) acaban con el jefe a ~264 de vida tras
  recibir más daño que en una victoria, con `STUCK` 20 veces más frecuente.
- Hipótesis: (a) el jefe se cura cuando nadie le pega; (b) nadie le pega porque los 16 escudos quedan sin camino
  (suelo roto por el rayo, desnivel) o él queda fuera de alcance; (c) con 2 arcos entre los 16 el jefe sigue recibiendo
  daño y no llega a curarse.
- Tarea 1 (Java solo en `debug/sim/`, sin tocar reglas del juego): por corrida, en `extra`: `boss:healed` (suma de
  subidas de vida del jefe), `boss:maxHealth`, `boss:secNoHit` (segundos sin recibir daño, total y racha más larga
  `boss:maxGapSec`), `stuck:sec` (guardia-segundos con motivo `STUCK`), `stuck:dy` y `stuck:dist` (desnivel y
  distancia horizontal medias al jefe de los guardias atascados), `boss:healthAt60/120/180/240`. Si `arenaRepaired`
  ya dice cuánto suelo se rompió, dejarlo; si no, no añadir más.
- Tarea 2 (generador `gen-tremor65`, copia exacta de `t62-tremor-escudos`): preset `t65-tremor-atasco` con
  `t65-tremor-escudos` (control, sin cambios) y `t65-tremor-14a2` (`blue.role` = 14 escudos + 2 arqueros; comprobar
  con `kit:*`). n = 30 por brazo, **seed 6201** (las mismas de F22: el control se compara fila a fila con
  `tools/torneo-data/t62/`), `mods=bestiario`. 60 corridas. Sin regresión (solo sim); el build debe compilar.
- Métrica principal: `red.healthLeft`, diferencia pareada `14a2` contra `escudos`. Secundarias: % victorias,
  vivos, segundos, y las claves nuevas separadas por resultado (victoria / derrota / empate).
- Veredicto, dicho antes:
  - **Causa**: tabla de las claves nuevas por resultado. (a) se confirma si en los empates `boss:healed` ≥ 300 y en
    las victorias < 100; (b) si `stuck:sec` de los empates ≥ 5 veces el de las victorias, con `stuck:dy` o
    `stuck:dist` que diga dónde está el jefe respecto a ellos. Si ninguna se confirma: decir qué muestran las
    claves; lectura de Fable antes de otra ficha.
  - **Brazo**: `14a2` es candidato si deja menos vida al jefe por > 2,5 EE pareado y gana ≥ que el control; pide
    confirmación (semillas nuevas) antes de cambiar la cifra de O5. Si no mejora: O5 intentos +1 y la siguiente
    palanca sale de la causa medida (desatasco o terreno), no de la composición.
- Pendiente aparte (no en esta ficha): separar Protección IV de Filo V; `kit:tier:*` no sale en el informe.
- Modelo: ejecutor (Java de sim, generar, commit, lanzar) + analista.
- **Estado: lanzado 2026-11-17 · preset `t65-tremor-atasco` (seed 6201, `mods=bestiario`) · commit 13d48a4 · run
  44022731023.** El build de ese run es la prueba de que compila (aquí no hay Java). Notas del ejecutor: (1) Tremorzilla
  es de Alex's Caves, no de Cataclysm, así que las claves `ignis:*` no lo ven; las nuevas (`SimCombatStats.giantTick`)
  siguen al rojo de más vida máxima si tiene ≥ 100, de cualquier mod. (2) `boss:healed` = suma de subidas de vida
  entre dos ticks (cura y golpe en el mismo tick cuentan su neto). (3) `boss:secNoHit` / `boss:maxGapSec` = segundos,
  con el jefe vivo, sin daño AZUL en los 20 ticks anteriores (total y racha más larga). (4) `stuck:sec` =
  guardia-segundos cuyo ÚLTIMO motivo de decisión es `STUCK` (`DecisionTrace.last`; no es el contador de cambios de
  motivo `STUCK` del informe); `stuck:dy` = Y del jefe menos Y del guardia (positivo = jefe más alto) y `stuck:dist`
  = distancia horizontal, medias sobre esos guardia-segundos (0 si no hubo). (5) `boss:healthAt60/120/180/240`: solo
  salen las marcas que la pelea alcanzó (jefe muerto = 0); una clave ausente = la corrida acabó antes. (6)
  `arenaRepaired` ya existía: sin cambios. (7) Brazo: `mix:shield=14,archer=2` (índices 0-13 escudo, 14-15 arco;
  comprobar `kit:bow` = 2).

## Orquestador 2026-11-17 (turno 2; modelo Fable)

**CI:** este turno arrancó 25 s después del turno 1 (dos despachos casi a la vez). El ejecutor de F25+F26 está en
curso (run 44022425714), el analista de F24 en cola (run 44022090610). No se repite nada de eso.

**Veredicto leído: F23 (O3).** La base ya es válida: `retirada` vive toda la pelea (270 de 270) y **no salva más que
pelear** en ningún caso; `huida` llega al refugio (38–76 % a pie lo pisan) pero acaba con 0–9 % dentro y pierde
30–52 puntos de vivos. Cifra base de O3 contra 80 vindicadores: arco 49 % vivos con orden frente a 67 % peleando,
3 % en refugio; línea 9–16 % frente a 14 %, 0 % en refugio. Es la base: intentos de O3 = 0. Nada estancado: sin issue.

**Lectura de Fable (pedida por el analista: por qué "llegan y no se quedan"), en `InstructionRunner`, `UnitOrders` y
`Tactics`:**
1. La causa `RETREAT` de `refugeLeaveCause` no dice nada: en huida todo guardia sin blanco tiene `RETREAT` como último
   peldaño siempre. Solo significa "salió sin blanco".
2. `RETREAT_TO_POINT` emite `FALLBACK` con ancla = refugio y forma **COLUMNA** (`UnitOrders`, rama `HOLD, MOVE,
   FALLBACK`): 8 guardias × 2,5 de separación = columna de **17,5 bloques**. La medida "en el refugio" es ≤ 8 bloques
   del punto. Media columna tiene su puesto **fuera** del refugio medido: el guardia pasa por el punto (cuenta
   "llegó") y sigue hasta su puesto (cuenta "salió").
3. Los puestos se reparten por orden de dureza dentro de la unidad y con `unit.size()`: **cada baja reordena** la
   columna y mueve a los vivos de puesto, otra vez a través del radio de 8.
4. En `Tactics` un repliegue ordenado no añade movimiento propio (`if (ordered ...) return`): no hay un segundo
   sistema que los saque. Y el guardia que huye no pelea (solo responde a quien le pega): el vindicador corre igual
   que él, llega detrás, y en el refugio no hay nada que lo pare. Eso explica los vivos (−30 a −52), no las "salidas".
Conclusión: parte del "0–9 % en refugio" es de la **medida y la geometría** (columna más larga que el refugio), y la
parte real es que nadie frena al perseguidor → eso es justo la retaguardia de David. Se mide todo en una ficha.

Una ficha este turno (O3). O4 sigue sin ficha: el ejecutor ya lleva F25+F26 y esta va detrás; O4 va en el siguiente.

### F27 [EJECUTOR genera y lanza, ANALISTA lee] O3 paso 2: retaguardia (idea de David del 16) contra 80 vindicadores → O3
- Hecho: F23 (base) y la lectura de arriba. Las instrucciones por unidad viven (F16f) y traen `rf:*` (prueba de vida).
- Hipótesis (David): si K guardias se quedan a pelear mientras el resto huye al refugio, el perseguidor se queda con
  la retaguardia y los que huyen llegan y se quedan → más vivos en total que pelear todos y que huir todos.
- Java mínimo, **solo `debug/sim/`** (sin reglas del juego): en `refugeEnd`, además de lo que hay, `refugeIn20` (vivos
  a ≤ 20 bloques del punto al final; el radio de 8 es más corto que la columna de 17,5) y vivos al final por unidad
  (`u<k>:alive`) si no sale ya. Nada más.
- Brazos (generador nuevo `gen-retaguardia66`, copia de `t52-escape-vindicadores80`: 8 guardias, 80 vindicadores,
  misma arena, `refuge:50`, misma duración; se añade `unit:4` = 4 unidades de 2; **solo cambia `unitInstr`** y
  `retreat:flee`), para dos composiciones, **arco** y **línea** (las de F23):
  - `pelear` (control): U1–U4 `MANTENER_LINEA@0`, sin `retreat:flee`;
  - `huida`: U1–U4 `RETIRARSE@10` con `retreat:flee`;
  - `reta2`: U1 `MANTENER_LINEA@0`; U2–U4 `RETIRARSE@10` con `retreat:flee`;
  - `reta4`: U1, U2 `MANTENER_LINEA@0`; U3, U4 `RETIRARSE@10` con `retreat:flee`.
  `retreat:flee` es global para RETIRARSE: comprobar en `InstructionInterpreter` que no cambia MANTENER_LINEA (se lee
  que solo toca `Instruction.RETIRARSE`). En línea (mixta), decir con `kit:*` qué arma queda en U1 y U2; si se puede
  elegir sin Java, la retaguardia son los **escudos/espadas**; si no, lo que dé el orden de aparición, dicho.
- Lote: preset `t66-retaguardia-vindicadores80`, 8 brazos, n = 30, **seed 5201** (las de F23: `pelear` y `huida` se
  comparan además con `tools/torneo-data/f23/`), mismos mods que `t52-escape`. 240 corridas. Sin regresión (solo
  sim); el build debe compilar.
- Métrica principal: % vivos al final (de 8), diferencia pareada por semilla de `reta2` y `reta4` contra `pelear`
  (y contra `huida`), solo semillas originales. Secundarias: `refugeIn20` y % en refugio (≤ 8), vivos por unidad
  (¿muere la retaguardia entera?), segundos, corridas cortadas por "sin enfrentamiento" (contarlas aparte).
- Veredicto, dicho antes:
  - **Prueba de vida**: en ≥ 90 % de las corridas, las unidades que huyen tienen `rf:issued:FALLBACK` ≥ 1 tras el
    segundo 10 y las de retaguardia `act:*` de pelea. Si no, decir qué claves salen y parar.
  - **Medida**: si en `huida` `refugeIn20` ≥ 2 × el % a ≤ 8, la geometría era parte del "no se quedan": decirlo.
  - Un brazo **aprueba O3** si deja más vivos que `pelear` por > 2,5 EE pareado **y** ≥ 50 % de los vivos en
    `refugeIn20`; pide confirmación con semillas nuevas. Si mejora a `huida` pero no a `pelear`: "la retaguardia
    ayuda a huir, pero pelear sigue siendo mejor", O3 intentos +1. Si nada mejora: O3 intentos +1, y la siguiente
    palanca es de terreno (un refugio que pare al perseguidor: puerta o muro), que es decisión de escenario.
  - Si `pelear` con arco sigue cerca de 67 % y la línea en 14 %: la respuesta puede diferir por composición; dar
    las dos tablas por separado, no la media.
- Modelo: ejecutor (Java de sim, generar, commit, lanzar) + analista.
- **Estado: lanzado 2026-11-17 · preset `t66-retaguardia-vindicadores80` (8 brazos, n = 30, seed 5201,
  `mods=bestiario` como el lote de F23) · commit 2f8d62e · run 44023205679.** El build de ese run es la prueba de
  compilación (no hay Java aquí). Notas del ejecutor:
  1. **Un cambio fuera de `debug/sim/`, solo telemetría** (`command/InstructionRunner.rfAdd`, 4 líneas): `rf:*` solo
     se archivaba para unidades en modo `RETREAT_FIGHTING`, y con `retreat:flee` RETIRARSE es `RETREAT_TO_POINT`
     (`InstructionInterpreter.resolve`): la prueba de vida de esta ficha (`rf:issued:FALLBACK`) no podía salir nunca.
     Ahora `rfAdd` archiva también en `RETREAT_TO_POINT`. No cambia ninguna decisión: `rfOn` (re-atado `simKeep`,
     tamaños `members/adhoc/loaded`) sigue solo en `RETREAT_FIGHTING`, y fuera de una corrida `unitInstr` es un no-op.
     En la huida saldrán `u<k>:rf:start`, `rf:issued:FALLBACK`, `rf:same`, `rf:end:*`; **no** `rf:step`, `rf:jump:*`
     ni `rf:realBounds` (son de la rama que pelea): no pedirlos. El segundo de la orden está en `u<k>OrderAt:RETIRARSE`.
  2. `refugeIn20` añadido en `SimExtras.refugeEnd`: fracción de los 8 iniciales vivos a ≤ 20 bloques del punto (misma
     base que `atRefuge`, no fracción de los vivos). Vivos por unidad ya salían: `u<k>Alive` y `u<k>Start` (no
     `u<k>:alive`), con `u<k>DmgDealt/DmgTaken/Moved/MeanEnemyDist/FirstContactS`.
  3. `retreat:flee` comprobado: solo cambia `Instruction.RETIRARSE` (`InstructionInterpreter.resolve`); MANTENER_LINEA
     sale de la tabla normal.
  4. Línea, sin Java: `mix:shield=4,archer=4` reparte por orden de aparición y `unit:2` hace unidad = índice / 2 + 1,
     así **U1 y U2 = 4 escudos, U3 y U4 = 4 arqueros**: la retaguardia son escudos (`reta2` = 2 escudos, `reta4` =
     los 4). `kit:*` sale por corrida, no por unidad; se comprueba con `u<k>:act:*` (disparos solo en U3 y U4).
  5. Lectura más simple de «solo cambia `unitInstr`»: **sin etiqueta `instruction` general** (como `t52`). Una unidad
     con `RETIRARSE@10` pelea como la base hasta el segundo 10. Ojo al comparar con F23: `pelear` de F23 no tenía
     instrucción y aquí es `MANTENER_LINEA@0` en 4 unidades de 2; `huida` de F23 era una orden a los 8 juntos (una
     columna) y aquí son 4 unidades (4 columnas de 2 ancladas en el mismo punto). No son filas idénticas.
  6. Escenarios: `t66-retaguardia-{arco,linea}-{pelear,huida,reta2,reta4}` (`gen-retaguardia66`).

### F24: Veredicto (analista, 2026-11-17; lotes `20261116-213936-t63-o1-arena96`, `20261116-213910-t63-o1-arena160`, commit 1450d14)
Datos: `tools/torneo-data/f24/`. n = 30 por brazo, semillas 6301–6330, las mismas en los 6 brazos; 0 reintentos, 0
cortadas por tiempo, 0 atascadas. 8 arqueros contra 32 Pax, llano. El tamaño de arena **no consta** en el informe; se
deduce de que son dos lotes distintos y de la distancia medida. La separación inicial es la misma en las dos arenas
(`distance` 30; `SimRunner` coloca los bandos a ±15 del centro): lo único que cambia es el sitio detrás de los
arqueros (33 bloques hasta el muro en 96, 65 en 160).

| Brazo | % victorias | vida Pax restante | vivos de 8 | vida guardias | daño recibido (por s) | s | dist. media | flechas / impactos | impactos en invulnerabilidad | paradas por compañero |
|---|---|---|---|---|---|---|---|---|---|---|
| 96 `sync` (control) | 57 ± 9 (17) | 34,9 ± 9,5 | 1,8 | 33 | 200 (6,0) | 34 | 39,2 | 134 / 98 | 2,8 | 16 |
| 96 `spread` | 70 ± 8 (21) | 32,6 ± 11,3 | 3,0 | 57 | 161 (5,3) | 31 | 39,8 | 174 / 122 | 24,6 | 25 |
| 96 `off` | 47 ± 9 (14) | 60,0 ± 15,8 | 1,6 | 27 | 201 (6,7) | 31 | 38,0 | 174 / 122 | 36,1 | 24 |
| 160 `sync` | 100 (30) | 0 | 6,6 | 128 | 65 (1,6) | 39 | 44,3 | 169 / 114 | 3,9 | 11 |
| 160 `spread` | 100 (30) | 0 | 7,0 | 139 | 43 (1,3) | 33 | 44,8 | 220 / 133 | 24,6 | 11 |
| 160 `off` | 100 (30) | 0 | 6,0 | 117 | 83 (2,2) | 37 | 44,1 | 241 / 144 | 40,9 | 16 |

El control repite la cifra de O1 (57 %, igual que r48) y la distancia de F19 (39,2).

**Arena: el efecto es claro; el criterio escrito se cumple a medias.**
- `a160-sync` contra `a96-sync`, pareado: vida Pax −34,9 (EE 9,5; **z −3,7**, pasa el 2,5); victorias 17 → 30 de 30
  (13 semillas pasan de derrota a victoria, 0 al revés); vivos +4,7 de 8 (z +10,5); daño recibido −135 (z −12,4), por
  segundo 6,0 → 1,6 (z −12,0); tarda 4,7 s más (z +3,4). Lo mismo con los otros dos modos de fuego (z −2,9 y −3,8).
- `meanEnemyDist` sube 39,2 → 44,3 (+5,1; z +13,9) pero **no llega al ≥ 46** que pedía la ficha, ni a los 48–56 de la
  regla. Con sitio de sobra los arqueros siguen 4–12 bloques más cerca de lo que la regla quiere: los Pax avanzan
  casi tan rápido como ellos retroceden (`act:standOff` 131 → 163 por corrida). Aun así esos 5 bloques bastan para
  dividir entre 3,7 el daño recibido por segundo.
- Lectura: el 57 % **es en buena parte el muro de la arena de 96** (hipótesis (a) confirmada en el resultado), con la
  salvedad de la distancia. Cifra en 160: **100 % (30 de 30), 6,6 de 8 vivos**, n = 30: candidata, falta confirmar
  con semillas nuevas. Coste medido: 26 % más flechas y pelea 14 % más larga.

**Fuego: ningún brazo pasa el criterio; `off` descartado, `spread` solo da indicios.**
- `spread` contra `sync` en 96: vida Pax −2,2 (EE 15,4; z −0,1): **no se distingue** en la métrica principal. En 160
  la métrica principal está en el techo (0 en todos), así que por la regla 3b se miran las continuas: gana 5,9 s
  antes (z −4,3) y recibe −21 de daño (z −2,3); vivos +0,5 (z +1,4). En 96 las secundarias van igual: daño recibido
  −39 (z −3,0), vivos +1,2 (z +2,0), 3,1 s menos (z −2,4), victorias +4 (9 semillas a favor, 5 en contra). Signo
  igual en las dos arenas, pero son secundarias (varias miradas) y la principal no se mueve: **no es candidato** con el
  criterio dicho antes.
- Por qué no rinde más: soltar el tiro da +24 impactos por corrida en 96, pero +22 caen en invulnerabilidad
  (`iframeHits` 2,8 → 24,6; ~194 de daño perdido). Los "~12 impactos que faltan" (hipótesis (b)) no salen de aquí:
  quedan ~2 impactos útiles más. Además sube el tiro parado por compañero en 96 (16 → 25).
- `off` contra `sync`: en 96 vida Pax +25 (z +1,5), 14 victorias frente a 17; en 160 daño recibido por segundo +0,54
  (z +2,1), vivos −0,6 (z −1,7); 36–41 impactos en invulnerabilidad. Igual o peor en todo: **palanca descartada**.
  Mirar ventanas (`sync`/`spread`) es mejor que no mirarlas.

**Qué cambia.** Ningún valor por defecto; sin regresión (solo lectura). **O1 oficial sigue en 57 %** (la medida del
objetivo es la arena de 96 y cambiarla lo decide David). O1 intentos = 0 (la arena pasa el 2,5 EE). Pedir al
orquestador: (1) ficha de confirmación de arena 160 con semillas nuevas (basta `sync`; `spread` al lado sale gratis y
diría si sus indicios se repiten); (2) llevar a David la decisión ya anotada: medir O1 "en abierto" en 160; (3) si se
quiere seguir en 96, la palanca que señalan los datos no es el fuego sino la distancia (los arqueros no llegan a 48
ni con sitio: velocidad de retroceso frente a la de avance de los Pax); (4) añadir el tamaño real de arena al informe.

## Orquestador 2026-11-17 (turno 3; modelo Fable)

**CI:** tercer turno seguido en 10 minutos (el analista de F24 lo despertó). Ejecutor de F27 en curso (run
44022785245); lotes de F25 y F26 corriendo (runs 44022728126 y 44022731023). Hay otro turno de orquestador en cola
detrás de este (run 44022950961): no tendrá veredictos nuevos.

**Veredicto leído: F24 (O1).** La arena de 160 lleva el 57 % a **100 % (30 de 30), 6,6 de 8 vivos** (vida Pax
z −3,7; daño recibido por segundo 6,0 → 1,6); candidata, falta el segundo lote. `fire:off` descartado; `fire:spread`
no pasa el criterio (los impactos de más caen en invulnerabilidad). O1 oficial sigue en 57 % (la medida en 96 la
cambia David; ya está en decisiones pendientes). Avance candidato: O1 intentos = 0. Nada estancado: sin issue.

**Lectura de Fable:** (1) 5 bloques más de distancia (39 → 44) dividen entre 3,7 el daño por segundo: contra Pax la
distancia es casi todo, y el muro la quita. (2) O4 se mide en la misma arena de 96 con los mismos 32 Pax, y la regla
de jinete por defecto (`riderKite`, F2) es alejarse a 48–56: es muy probable que el 54 % de O4 tenga el mismo muro.
F2c ya dijo que en la mixta mueren los jinetes con espada. Una sola ronda responde las dos cosas: ¿O4 también es el
muro?, y si no, ¿son las espadas? (3) El 100 % no cierra O1 (principio de David): se añade un brazo más duro (48 Pax).

Una ficha, dos objetivos (O1 y O4), sin Java. O2 queda para el turno siguiente (pide leer la aldea antes).

### F28 [EJECUTOR genera y lanza, ANALISTA lee] O1: confirmar la arena de 160; O4: ¿la caballería choca con el mismo muro? (sin Java) → O1 y O4
- Hecho: F24 (arriba). Base de O4: `t49-jinete-pax32-kite` (4 espadas + 4 arcos montados contra 32 Pax, 54 %; como
  `riderKite` es valor por defecto, la etiqueta `rider:kite` sobra pero se deja: copia exacta).
- Hipótesis: (a) la cifra de O1 en 160 se repite con semillas nuevas; (b) la caballería mixta en 160 gana bastante
  más que en 96 (mismo muro); (c) si no, el lastre son los 4 jinetes con espada: 8 arqueros montados ganan donde la
  mixta no.
- Brazos (generador nuevo `gen-o1o4-67`; cada escenario es copia exacta del indicado, cambiando solo lo dicho; todos
  con `measuredist`; un preset por arena, como en F24):
  - `t67-arena96`:
    - `t67-a96-o1` = copia de `t63-o1-a96-sync` (control de O1);
    - `t67-a96-cab` = copia de `t49-jinete-pax32-kite` (control de O4);
    - `t67-a96-cabarcos` = la misma con `blue.role` = `mix:archer@m=1` (8 arqueros montados);
  - `t67-arena160` (todos con `arena:160`):
    - `t67-a160-o1` = copia de `t63-o1-a160-sync`;
    - `t67-a160-o1spread` = la misma + `fire:spread`;
    - `t67-a160-o1pax48` = la misma con `red.count` = 48 (el escenario más duro: el 100 % no cierra la pregunta);
    - `t67-a160-cab`, `t67-a160-cabarcos` = los dos de caballería de arriba con `arena:160`.
  n = 30, **seed 6701** en los dos presets (semillas nuevas, las mismas en los 8 brazos), `mods=bestiario`.
  240 corridas. Sin regresión (no se toca Java ni valores por defecto).
- Métrica principal: `red.healthLeft` (vida Pax restante), diferencia pareada por semilla, solo semillas originales
  (sin reintentos). Secundarias: % victorias con EE, vivos de 8, vida de guardias, segundos, `meanEnemyDist`, daño
  recibido por segundo, flechas, `iframeHits`; en caballería además muertes por arma (espada / arco, con `kit:*` o
  el desglose que haya), `mountDeaths`, `mountedShare`.
- Veredicto, dicho antes:
  - **O1 confirmación**: si `a160-o1` gana ≥ 85 % y mejora a `a96-o1` por > 2,5 EE pareado, la cifra en 160 queda
    **confirmada** (dos lotes): darla junto con F24 (n = 60). La cifra oficial de O1 no cambia hasta que David decida
    la medida; se escribe en decisiones pendientes como "listo para decidir". Si no se repite: decir cuánto y O1
    intentos +1.
  - **O1 más duro**: `a160-o1pax48` da la cifra de "cuánto aguanta" (victorias, vivos, vida Pax). Solo lectura.
  - **`spread`**: contra `a160-o1`, segundos y daño recibido (las dos que dieron indicios en F24, dichas antes ahora):
    si las dos repiten con > 2,5 EE, pasa a candidato con criterio válido; si no, cerrado.
  - **O4**: si `a160-cab` mejora a `a96-cab` por > 2,5 EE pareado → el muro también frena a la caballería: candidato,
    dar la cifra en 160 y O4 intentos = 0. Si no mejora y `cabarcos` gana a `cab` por > 2,5 EE en la misma arena →
    el lastre son las espadas (la siguiente ficha es qué hacen los jinetes con espada contra armas de fuego: quedarse
    atrás de escolta o desmontar; regla táctica, Fable la diseña). Si nada se distingue: O4 intentos +1.
  - Si el control `a96-cab` no repite ~54 % (fuera de 2,5 EE): decirlo antes que nada.
- Modelo: ejecutor (generar, commit, lanzar los dos presets; un solo run si `build.yml` da una máquina por preset,
  como en F24) + analista.
<<<<<<< Updated upstream
- Estado: lanzado 2026-11-17 · presets `t67-arena96`, `t67-arena160` · seed 6701 · `mods=bestiario` · commit df25e23 ·
  run 44023416099 (un solo run: una máquina por preset, la arena se calcula por lote). Generador `gen-o1o4-67` en
  `tools/torneo.py`. Sin Java. Lecturas elegidas (las más simples): las copias de O1 parten de `t63-o1-a96-sync` y se
  les añade `arena:160` (idéntico a `t63-o1-a160-sync`); a las de caballería se les añade `measuredist` (la base
  `t49-jinete-pax32-kite` no lo lleva) y conservan `rider:kite`, también `cabarcos` (solo cambia `blue.role`).
=======

## Orquestador 2026-11-17 (turno 4; modelo Fable)

**CI:** cuarto turno en 10 minutos (venía en cola detrás del turno 3). Sin veredictos nuevos. Ejecutor: F27 en curso
(run 44022785245) y F28 en cola (run 44023142184): la cola del ejecutor está llena, **no se despacha nada** (un
tercer despacho cancelaría F28). Nada estancado, sin issue. El turno se usa en lo pendiente: leer O2 y dejar su ficha.

**Lectura de Fable sobre O2 (datos ya guardados, `tools/torneo-data/t52-53/`, ronda 53, semillas originales):** el
brazo que da la cifra de O2 (línea 4 + 4, `standoff:losRelease`, 16 Pax en la aldea) **no pierde ni una pelea**: 18
victorias, 0 derrotas y **12 de 30 cortadas por "sin enfrentamiento"** (45 s sin daño), con 2,8 Pax vivos, 6,5 de 8
guardias vivos y a 10 bloques de media. Con 32 Pax pasa lo mismo: 18 de 30 cortadas (7,9 Pax vivos), 8 victorias, 4
derrotas. O sea: el 62 % no es "los pistoleros ganan entre casas", es "quedan 2-3 pistoleros a los que nadie hace
daño". La arena dice por qué puede ser (`SimArena`, caso `village`): la hilera A de casas está **sobre la línea de
salida roja**: unos rojos nacen dentro de una casa (puerta cerrada: un saqueador no la abre y un guardia no ve a
través de la pared, así que no lo toma por blanco) y otros nacen en el tejado (4 de alto, sin camino). Eso es en
buena parte un efecto del escenario, no una táctica de armas de fuego. Antes de diseñar "terreno cerrado" hay que
saber dónde están esos 2-3 Pax (principio de David: de lo técnico al escenario; regla del 2.º fallo: leer antes de
medir).

### F29 [EJECUTOR mide y lanza, ANALISTA lee] O2: ¿dónde están los Pax que quedan en la aldea? (telemetría de sim + 2 brazos de diagnóstico) → O2
- Hecho: lo de arriba. Base: `t53-aldea-pillagerpax16-linea-buscarvista` (62 % / 82 % vivos) y
  `t53-aldea-pillagerpax32-linea-buscarvista` (21 % / 44 %).
- Hipótesis: (a) los Pax vivos al cortarse la pelea están dentro de una casa o en un tejado (≥ 70 % de ellos);
  (b) con los rojos naciendo en la calle (sin la hilera A) la misma línea gana ≥ 85 % con ≥ 70 % vivos; (c) dejar
  correr la pelea (sin corte por atasco) no la resuelve sola.
- Java, **solo de sim** (nada cambia en el juego; sin regresión de valores, pero sí `tools/regresion.py check` si el
  ejecutor toca algo fuera de `debug/`):
  1. Telemetría `aldea:*` al terminar cada corrida con `terrain` = `village` (en `SimCombatStats` o `SimExtras`, como
     `boss:*` de F26), sobre los rojos vivos al final: `aldea:redIn` (bajo techo: bloque sólido encima a ≤ 4),
     `aldea:redRoof` (pies ≥ 3 sobre el suelo de la arena), `aldea:redStreet` (el resto), `aldea:redNoTarget` (sin
     blanco), y sobre los guardias vivos: `aldea:blueNoTarget`, `aldea:blueNoLos` (con blanco pero sin línea de
     visión), `aldea:nnDist` (distancia media del rojo vivo al guardia más cercano). Además, al nacer:
     `aldea:redStartIn`, `aldea:redStartRoof` (cuántos rojos empiezan dentro o en tejado).
  2. Etiqueta de sim `village:rowB`: la aldea se construye **sin la hilera A** (solo la hilera B, a medio camino):
     los rojos nacen en abierto y los azules siguen cruzando calles. Una línea en `SimArena`.
- Brazos (generador nuevo `gen-o2-68`, preset `t68-aldea`; copias exactas cambiando solo lo dicho):
  - `t68-pax16` = copia de `t53-aldea-pillagerpax16-linea-buscarvista` (control, cifra de O2);
  - `t68-pax16-nostuck` = la misma + `nostuck` (la pelea corre hasta los 180 s);
  - `t68-pax16-rowB` = la misma + `village:rowB`;
  - `t68-pax32`, `t68-pax32-rowB` = lo mismo sobre `t53-aldea-pillagerpax32-linea-buscarvista` (control y `rowB`).
  n = 30, **seed 6801**, las mismas semillas en los 5 brazos, `mods=bestiario`. 150 corridas (más reintentos por
  atasco, que **no** se cuentan: solo semillas originales, `retried` = false).
- Métrica principal: `red.survivors` al final (Pax vivos), diferencia pareada por semilla. Secundarias: % victorias
  con EE, % cortadas por atasco, % derrotas, vivos de 8, segundos, y el reparto `aldea:*` **separado por desenlace**
  (victoria / cortada / derrota).
- Veredicto, dicho antes:
  - Si el control no repite ~62 % (16) y ~21 % (32) dentro de 2,5 EE: decirlo antes que nada.
  - **Dónde están**: en las cortadas del control, si `redIn + redRoof` ≥ 70 % de los rojos vivos → causa = escondidos
    o inalcanzables (decir cuántos dentro y cuántos en tejado, y si los guardias tienen blanco). Si < 30 % → están en
    la calle y nadie los ataca: es un fallo de blanco/visión de los guardias, y la siguiente ficha lee ese código.
  - **`rowB`**: si gana ≥ 85 % con ≥ 70 % vivos contra 16 → **candidata** (2.º lote con semillas nuevas) y queda
    para David decidir si O2 se mide con los rojos naciendo fuera de las casas (decisión de escenario, no la toma el
    equipo). Si mejora > 2,5 EE sin llegar a la meta → dar la cifra; lo que falta es táctica de calle. Si no mejora →
    las casas de salida no son la causa: O2 intentos +1.
  - **`nostuck`**: si las cortadas pasan a victorias (≥ 85 % en total) → era el corte, no la pelea (la cifra buena
    es la de `nostuck`, candidata). Si siguen en empate a los 180 s → confirmado que nadie los alcanza.
  - Siguiente ficha según el caso (Fable la diseña): dentro de casa → capacidad "registrar la casa" (un guardia
    cuerpo a cuerpo entra por la puerta hacia el último lugar visto); tejado → tiro alto de los arqueros a un blanco
    sin camino. Las dos son reglas tácticas y se miden antes de ser valor por defecto.
- Modelo: ejecutor (telemetría + etiqueta + generador + lanzar; el build del lote es la prueba de compilación) +
  analista. Despachada al final de este turno: F27 terminó, F28 pasó a correr y la cola quedó libre (F29 espera detrás de F28).
>>>>>>> Stashed changes
