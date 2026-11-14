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
- Estado: generado (commit local), sin lanzar.
- **Veredicto:** pendiente.

### F5 v2 [EJECUTOR] Escape contra lo que no se gana
- Presets `t52-escape-vindicadores80`, `t52-escape-devastadores8` (más los anteriores), con la huida como FALLBACK.
- Estado: generado (commit local), sin lanzar.
- **Veredicto:** pendiente.

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
