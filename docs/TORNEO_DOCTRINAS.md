# TORNEO_DOCTRINAS — protocolo para descubrir el oficio de los guardias (David + Cowork, 2026-11-04)

> Objetivo: que un grupo de veteranos **maneje objetivamente mejor cada tipo de amenaza**, no solo que cada uno sea
> más hábil. Los guardias no entienden; **nosotros** aprendemos con la simulación y escribimos ese saber en un
> **Manual de campo** (datos). La veteranía decide quién lo aplica y con qué calidad. Nada de esto da vida, daño ni
> velocidad: solo decisiones. Primero contra **mobs** (vanilla, luego las raids de Scorched Guns), después jugadores.

## 1. Los cuatro ejes de cada prueba
| Eje | Qué es | Valores iniciales |
|---|---|---|
| **Amenaza** | contra qué se pelea, por su naturaleza (no por su nombre) | ver §2 |
| **Composición** | qué tiene nuestro grupo: roles, armas, monturas, proporciones | ver §3 |
| **Objetivo** (la *doctrina*) | qué priorizamos | `MATAR_RAPIDO`, `SOBREVIVIR`, `MANTENER_POSICION`, `GANAR_TIEMPO` |
| **Respuesta** | qué combinación de comportamientos que **ya existen** usamos | ver §4 |

La pregunta del torneo siempre es: **para (amenaza, composición, objetivo), ¿qué respuesta rinde mejor?**
Una *doctrina* no es un comportamiento: es "jalar la mejor respuesta del manual según la amenaza y priorizando X".

## 2. Amenazas, clasificadas por su naturaleza
Cada amenaza se describe con rasgos que la IA del capitán sí puede observar (lo que ha visto: `SituationReport`,
`ThreatMemory`): **alcance** (mele / medio / largo), **movilidad** (lenta / normal / rápida / voladora),
**daño** (bajo / alto / explosivo / de área), **resistencia** (frágil / blindado / jefe), **número** (solo / grupo / horda),
**especial** (explota, invoca, cura, salta, se teletransporta, convierte a nuestros caídos).

Orden de trabajo:
1. **Vanilla, uno a uno** (ronda 1): zombi (horda mele lenta, convierte caídos), esqueleto (rango medio), creeper
   (explosivo, lento), araña (rápida, salta), bruja (pociones de área, cura), saqueador (ballesta), vindicador (mele
   alto daño), evocador (invoca vexes voladores), fantasma/blaze (voladores, el segundo a distancia).
2. **Mezclas vanilla** (zombis + esqueletos, la patrulla de saqueadores).
3. **Raids de Scorched Guns** (`data/scguns/raids/*.json`, 12 raids: antique, frontier, copper, iron, wrecker,
   ocean, sculk, piglin, diamond_steel, treated_brass, blood_moon…): zombis, esqueletos y husks **con armas de fuego**,
   armadura propia, multiplicadores de vida, efectos (velocidad) y un **jefe** de 65–250 de vida. Requisitos: Scorched
   Guns 1.5 pide NeoForge ≥ 21.1.228 (hoy compilamos con 21.1.215), `framework`, `curios`, `geckolib`.
4. **Jugador simulado** (bloque A de E3-COORD): volador con proyectiles, vehículo, asesino, mago de área.

**Mobs nativos:** en el torneo el rojo aparece **como en el juego** (rol `native`: su equipo, vida y daño reales). Las
pruebas antiguas igualaban al mob con un guardia (misma vida, arma y armadura) para medir decisiones; eso sigue
disponible, pero no sirve para aprender a pelear contra el mob real.

## 3. Composiciones (cómo se evalúa nuestro grupo)
Rol `mix:` en el escenario: `mix:shield=4,archer=4,melee@m=2` (`@m` = montado). Roles de hoy: `melee` (espada),
`shield` (espada + escudo + hierro completo), `axe`, `spear` (tridente, hasta tener la lanza propia), `archer`,
`crossbow`, `gunner` (arma de Scorched Guns), `fist`. Familias de composición:
- **Pura**: solo mele, solo escudos, solo arqueros, solo ballesteros, solo caballería, solo caballería de rango.
- **Línea + apoyo**: frente (escudo o mele) + tiradores detrás, en proporciones 2:1, 1:1, 1:2.
- **Con montura**: la misma composición con parte montada (velocidad para esquivar o golpear y salir).
- **Con armas de fuego**: cuando haya CI con Scorched Guns, cada arma con su valor (alcance, cadencia, daño, munición,
  dispersión) leído de `data/scguns/guns/*.json`; ver §8.

Cada composición recibe una **firma** (lo mismo que `GroupProfile`: arquetipos y proporciones). El manual se indexa
por firma, así una unidad real del jugador encuentra su fila más cercana.

## 4. Respuestas (comportamientos que ya existen; el torneo los combina, no inventa)
| Pieza | Valores |
|---|---|
| Postura / operación | mantener línea (HOLD_LINE), avanzar por turnos, flanquear, retroceder disparando, emboscada |
| Instrucción del intérprete | MANTENER, HOSTIGAR, FLANQUEAR (y las que se agreguen) |
| Formación | línea, columna, V, círculo; densidad |
| Modo de fuego | normal, quieto, avanzando, retrocediendo, volea |
| Prioridad de objetivos | el más peligroso, el más cercano, el más débil |
| Distancia | pegarse, distancia media, máxima distancia (kiting) |

Cuando una respuesta gana, se **aterriza a un nombre** legible ("Contra creeper: frente con escudos arriba
manteniendo la línea, arqueros quietos con fuego constante"; "a caballo: usar la diferencia de velocidad para no
dejarse golpear"). Si ninguna pieza existente sirve, eso es un hallazgo: falta un comportamiento (se propone, no se
inventa en silencio).

### 4b. Comer y rotar (David, 2026-11-04)
Los guardias, a diferencia de los mobs, **comen**. Hoy (`GuardPack.consume`) un guardia por debajo del 40 % de vida come
o bebe de su mochila **en el sitio**, cada 5 s como máximo; en las pruebas no llevaba comida, así que nunca se midió.
Plan: (1) la comida pasa a ser parte de la composición (`+comida`: pan ×16, manzana dorada ×2) y se mide cuánto cambia
sobrevivir; (2) respuesta nueva **"rotar a comer"**: el herido de mele se retira detrás de la línea o de un tirador que lo
cubra, come y vuelve a su puesto, y otro ocupa el hueco mientras tanto (piezas existentes: retirada por vida baja,
cobertura, relleno de huecos, reagrupar). Si gana, entra al manual como una respuesta más; es la primera "habilidad de
grupo" que los mobs no tienen. Comprobado en el jar de Guard Villagers 2.4.11: el `Guard` ya trae `GuardRunToEatGoal` y `GuardEatFoodGoal`
(**se aleja a comer** la comida que lleva y se cura según su `nutrition`) y `convertGuardOnDeath` (la conversión en
zombi). O sea: el "irse a comer" ya existe en Guard Villagers; lo que falta es **coordinarlo** (que alguien lo cubra y
otro tape su hueco) y medirlo, porque en las pruebas nunca llevaron comida.

## 5. Cómo se mide cada objetivo
| Objetivo | Métrica principal | Secundarias |
|---|---|---|
| MATAR_RAPIDO | tiempo hasta matar al rojo (con victoria) | daño rojo recibido por segundo |
| SOBREVIVIR | supervivientes azules y vida restante | bajas evitables (fuera de su rol) |
| MANTENER_POSICION | % del tiempo con el puesto cubierto, sin ceder > N bloques | supervivientes |
| GANAR_TIEMPO | segundos hasta perder el puesto o caer | bajas por segundo ganado |
Siempre también: victoria, conversiones (caídos que se vuelven zombi), coste (ms/tick), ping-pong de órdenes.

## 6. Búsqueda y estadística (para no engañarnos)
1. **Calibrar**: por (amenaza, composición) se busca el número de enemigos con el que la respuesta *base* gana ≈ 50 %.
   Sin esto todo da 100 % o 0 % y nada se distingue (ya nos pasó).
2. **Ronda de descarte**: todas las respuestas, n = 6. Pasa el tercio superior.
3. **Ronda final**: las finalistas y la base, n ≥ 30. Una respuesta "gana" solo si supera a la base por más de 2
   errores estándar en su métrica.
4. **Confirmación**: la ganadora se repite en otro lote (otras semillas). Si se invierte, no se adopta (ya vimos
   inversiones con n = 10).
5. **Mismo campo limpio**: entre corridas se retira todo mob ajeno (el "tercer bando").

## 7. El Manual de campo y la veteranía
- Archivo `config/rallyguard-field-manual.json` (y su copia por defecto en el jar), **generado por el torneo**, una fila
  por (rasgos de amenaza, firma de composición, objetivo) → respuesta, con su evidencia (lote, n, ventaja).
- **Quién lo usa:** el capitán reconoce la amenaza (con lo visto) y busca su fila. Un recluta R0 pelea con la doctrina
  genérica. La veteranía cambia **qué tan bien y qué tan pronto** se aplica: un capitán que ya enfrentó ese tipo de
  enemigo (tipos vistos, memoria de amenazas) lo reconoce antes y ejecuta con su `Leadership.quality`; uno que no, tarda
  o cae a la genérica. Una unidad de veteranos aplica el manual como unidad; eso es lo que la hace mejor que la suma.
- **El Battle Planner** queda encima: el manual es el oficio (lo que saben); la contingencia es tu previsión (lo que tú
  sabes y ellos no). Las respuestas de las contingencias se eligen de entre las del manual.
- **El Ledger se depura con esto**: una orden que no gana en ninguna fila del manual es candidata a quitarse.

## 8. Protocolo para cada rol, arma o amenaza nueva (se repite siempre igual)
1. Ficha del rol/arma/amenaza: rasgos (§2) o valores (alcance, cadencia, daño, munición, dispersión, peso).
2. Escenarios generados con `tools/torneo.py` (amenaza × composiciones que la incluyen × respuestas).
3. Calibración → descarte → final → confirmación (§6).
4. Filas nuevas en el manual + nombre legible + informe en `RALLY_SESIONES.md`.
Pendientes de este protocolo: **lanza y escudo propios** (cuando existan los ítems), **asesino**, **mago de
proyectiles**, **mago de área**, **armas de Scorched Guns** (una por familia: pistola, rifle, escopeta, ametralladora,
lanzador), **raids de Scorched Guns**.

## 9. Moral visible (David, 2026-11-04)
Para distinguir "huye por miedo" de "el mod no funciona": encima del guardia aparece una señal según su moral
(partículas desde el servidor, sin código de cliente): **VACILANTE** = gotas de sudor; **ROTO / DESTROZADO** =
humo gris mientras huye; **reagrupado** = destello verde una vez. Interruptor `morale.showIcons` (activado).

## 10. Ronda 2: primero medir qué funciona, después ponerle nombre (David, 2026-11-04)
Las doctrinas de hoy **no son el punto de partida**: son etiquetas. Lo que buscamos es el comportamiento que gana en la
simulación; cuando lo encontramos, lo programamos (si falta) y le ponemos nombre. Por eso cada corrida guarda ahora lo
que los guardias **hicieron** (`extra.act:*` en `runs.jsonl`, `python3 tools/torneo.py groups <jsonl…>`):
`shotNear` / `shotLong` / `shotHigh` (disparos por tipo de arco), `noShot:<motivo>` (disparos que quiso y no hizo:
`friendNearAim`, `noSolution`, `noLos`, `volleyBudget`, `noAmmo`), `shieldBlocks` y `shieldBlockedDmg` (golpes y daño
que paró el escudo), `hitShieldDown` / `hitThroughRaisedShield` (lo que le entró a quien lleva escudo).

**Lo que el descarte de la ronda 1 ya mostró (antes de la final):**
- **La ronda 1 entera no vale para comparar composiciones.** Guard Villagers (2.4.11, comprobado en el jar) arma a cada
  guardia en su **primer tick** con su tabla de botín `entities/guard_armor`: espada de hierro **o** ballesta (50/50),
  un juego de armadura al azar y escudo en la mano izquierda ~47 % de las veces, **encima** del kit que acababa de
  poner la simulación. Los "arqueros" de la ronda 1 no tenían ni un arco; los "mele" llevaban ballestas y escudos. Lo
  delató la telemetría nueva (guardias "mele" parando golpes con escudo). Corregido: la simulación crea a sus guardias
  sin `finalizeSpawn` (no se activa ese reparto) y cada corrida guarda el kit real de cada guardia azul al empezar la
  pelea (`extra.kit:*`: `kit:bow`, `kit:iron_sword`, `kit:shield`, `kit:ironLegs`…). Los datos de `tools/torneo-data/t1-*`
  quedan como historia; la **ronda 3** es la ronda 1 rehecha (`gen-cal 3`, abanico de rojos más ancho).
- **La volea estaba mal programada.** Acierto 8–22 % contra 85–99 % de cualquier otro modo de fuego. Causa: en modo
  VOLLEY, más allá de 15 bloques tiraba siempre el arco **alto** (≈ 80°), que vuela ≈ 105 ticks (5 s), pero la
  anticipación se calculaba con el tiempo del tiro recto (7–15 ticks): la flecha caía donde el blanco ya no estaba.
  Corregido: el arco alto solo sobre lo que tapa la vista; con el blanco a la vista, el arco bajo (que sube solo hasta el
  ángulo de alcance máximo, ≈ 42°) y la anticipación se recalcula con el tiempo de vuelo real. La volea queda como
  **"alcance máximo"**: un tirador en VOLLEY busca blancos hasta lo que su arma tira (tope 64 bloques), no hasta 40.
  Como VOLLEY también lo eligen solos los veteranos (`DecisionLadder`), HOSTIGAR y las operaciones de apoyo, el arreglo
  les llega a todos.
- **Los creepers nunca fueron atacados.** La lista negra de Guard Villagers (`MobBlackList`) trae `minecraft:creeper`:
  0 disparos y 0 de daño en las 59 filas de creeper. "Victoria" solo significaba que ya habían explotado todos. Ronda 1
  de creeper: **anulada**. Nueva prueba (`gen-creeper`): por composición, *ignorarlos* (lo de hoy) frente a *atacarlos*
  (etiqueta `gv:creepers`, que quita al creeper de esa lista solo durante esa corrida).
- **El escudo estaba confundido con la armadura**: el kit `shield` trae además grebas y botas de hierro. Nuevo kit
  `heavy` (mismo hierro, sin escudo) para medir lo que vale el escudo solo.

**Ronda 2 y 3 (presets):** `t3-cal-zombie`, `t3-cal-skeleton` (calibración rehecha, 8–40 zombis / 6–32 esqueletos,
n = 6) y, con los kits ya de verdad: `t2-duel-zombie`, `t2-duel-sc` (1 guardia espada / pesado / escudo contra 1–4 zombis, 1–3
esqueletos, 1–2 creepers, n = 30: la hipótesis de David de que el escudo gana casi siempre el 1 contra 1);
`t2-volea-30`, `t2-volea-70` (arco y línea, base / quietos / volea, a 30 y a 70 bloques, n = 30); `t2-creeper`
(5 composiciones × ignorar/atacar × 12/24/40 creepers, n = 10). La volea se regenera con la calibración de la
ronda 3 (`gen-volea 2 <t3-cal.json>`); la final de la ronda 1 ya no se corre (sus finalistas salieron de kits al azar).
Guard Villagers ya hace algo parecido a "escudo-paso-golpe": sube el escudo con el enemigo a ≤ 4 bloques (y siempre
contra creepers), lo baja 8 ticks para golpear y retrocede de lado a ≤ 3 bloques. El duelo mide eso tal cual; si gana,
el nombre es el de ese comportamiento, y si se puede mejorar (cadencia, paso adelante) será otra pieza medida igual.

### 10c. Manual de campo v0
Ronda 3 completa (calibración, descarte, final y confirmación con otras semillas): **`docs/MANUAL_DE_CAMPO.md`**.

### 10b. Primeros resultados con kits reales (2026-11-04, n por fila entre paréntesis)
- **Capacidad (8 guardias, la base gana ≈ 50 %)**: contra zombis escudo 30, espada 24, línea 21, arco 13, caballería
  12. Contra esqueletos espada y escudo ≈ 28; arco, línea y caballería ganan todavía el 100 % con 32 (el arco de verdad
  les saca 25 bloques de alcance): calibración ampliada en curso (40–80).
- **Creepers, atacarlos o no (n = 10 por fila, supervivientes azules con 12 / 24 / 40 creepers)**: arqueros 95/79/50 %
  atacando frente a 48/40/16 % ignorándolos; línea 89/72/66 frente a 54/49/30; escudos 78/68/52 frente a 70/44/27;
  caballería 62/36/31 frente a 34/32/7. **Espadas sin escudo, al revés**: 39/21/10 atacando frente a 51/39/12
  ignorándolos. Lo que de verdad funciona contra creepers: que los tiradores y los escudos los ataquen y que la espada
  sin escudo no se les acerque (pendiente de confirmación con otras semillas).
- **Duelos (n = 30)**: 1 contra 1 cualquier guardia gana a un zombi el 100 % sin perder vida; el escudo **no** marca la
  diferencia que esperábamos porque Guard Villagers lo baja demasiado: para 2–4 golpes por duelo y recibe 5–15 con el
  escudo bajo. Contra zombis el escudo ≈ el mismo hierro sin escudo (1 vs 3: 50 % frente a 57 %); contra esqueletos
  es **peor** (1 vs 2: 70 % frente a 100 %; avanza encogido y tarda el doble); contra creepers es el **mejor** (1 vs 2:
  83 % y 9 de vida frente a 72 % y 1,7). Siguiente: `escudoarriba` (escudo arriba mientras haya blanco).
- **Arqueros que no tiran**: con kits reales, por cada disparo dejaron de tirar 2–5 veces porque un amigo estaba a
  < 5 bloques del punto de caída, aunque nuestras flechas ya atraviesan a los nuestros. Siguiente: `quemarropa`
  (distancia de seguridad 1 bloque).
