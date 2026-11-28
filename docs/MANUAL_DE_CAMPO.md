# Manual de campo v0 (torneo de doctrinas, 2026-11-04; filas de esqueleto con rango confirmadas el mismo día, lote 20261104-202223)

Lo que la simulación **demostró** que funciona, con su evidencia. Cada fila salió de: calibración (la respuesta base
gana ≈ 50 %), descarte (n = 6), final (n = 30) y **confirmación con otras semillas** (n = 30, semillas 1001+). Una
fila entra como **CONFIRMADA** solo si gana a la base por más de 2 errores estándar, en victoria o en supervivientes,
en la final **y** en la confirmación. 8 guardias de rango 1, mobs nativos (su equipo, vida y daño reales), campo plano,
medianoche fija. Datos: `tools/torneo-data/t2/`, `t3/`, `t3-confirm/`; tablas: `python3 tools/torneo.py confirm …`.

Las respuestas de hoy son etiquetas; aquí cada una lleva el **nombre del comportamiento** que la hace ganar.

## Comportamientos con nombre

| Nombre | Qué hacen | Pieza de la simulación |
|---|---|---|
| **Tiro a quemarropa** | Los tiradores disparan aunque un amigo esté a menos de 5 bloques del blanco. Nuestras flechas ya atraviesan a los nuestros (`RallyFriendlyFireHandler`), así que esa distancia solo les quitaba disparos: con ella dejaban de tirar 2–5 veces por cada disparo. | `quemarropa` (`clearance:1`) |
| **Tirar desde donde están** | Los tiradores no se acercan a su "distancia óptima" (15 bloques): tiran quietos desde su sitio. Contra esqueletos los deja fuera del alcance enemigo. | `quietos` (STILL) y `volea` (VOLLEY corregida) |
| **Escudo arriba** | El escudo sube en cuanto hay blanco y solo baja el instante del golpe (Guard Villagers lo baja mucho más). | `escudoarriba` (`gv:shieldAlways`) |
| **Corro de escudos** | Los escudos forman en círculo. | `circulo` |
| **Creepers: tiradores y escudos atacan, espadas no** | Los tiradores y los escudos atacan a los creepers; la espada sin escudo no se les acerca. | `gv:creepers` |
| **Pantalla de escudos** (escolta) | Los escudos de una escolta se ponen entre los tiradores y el protegido, mirándolos; sin tiradores propios, la mitad va a por los del enemigo. | `escort:defend` (vs `off:screen`), ronda 17 |
| **Tortuga y carga** | Avanzar en tortuga (escudos arriba, sin romper filas) y atacar a 6 bloques del enemigo. | `march:TESTUDO` + `marchCharge:6`, ronda 19 |
| **Escudo contra lo que escupe** | Desde rango 1, escudo arriba y cara a blazes, ghasts y dragones que atacan a los nuestros. | `#rallyguard:shield_threats` (vs `off:threats`), ronda 20 |
| **Apartarse del cuerpo a cuerpo** | Un arquero a pie con un enemigo cuerpo a cuerpo (no tirador, no volador) a menos de 6 bloques corre hasta quedar a 11 y vuelve a tirar (mientras corre, 2 s, no se planta); si no hay camino, se queda y pelea. No en la tortuga ni montado. | `act:kite` (vs `off:kite`), ronda 27 |
| **Pelear contra el enderman enojado** | Los que no son tiradores atacan al enderman que ya se enojó (Guard Villagers lo tenía en su lista negra). Los arqueros no: sus flechas no le dan. | vs `off:enderman`, ronda 27 |

## Filas del manual

| Amenaza | Composición | Hacer | Final (victoria / superv., Δ vs base) | Confirmación (Δ vs base) | Estado |
|---|---|---|---|---|---|
| zombi | arqueros | Tiro a quemarropa | 100 % / 100 % (+17 / +38) | +19 / +42 | **CONFIRMADA** |
| zombi | arqueros | Tirar desde donde están | 100 % / 99 % (+17 / +37) | +19 / +38 | **CONFIRMADA** |
| zombi | línea (4 escudos + 4 arcos) | Tiro a quemarropa | 100 % / 99 % (+18 / +41) | +20 / +40 | **CONFIRMADA** |
| zombi | caballería (4 espadas + 4 arcos) | Tiro a quemarropa | 100 % / 96 % (+37 / +37) | +30 / +36 | **CONFIRMADA** |
| zombi | escudos | Corro de escudos | 70 % / 57 % (+20 / +19) | +47 / +39 | probable (positiva en ambos lotes, > 2 EE solo en la confirmación) |
| zombi | espadas | Corro | 63 % / 47 % (+21 / +15) | +37 / +29 | probable (ídem) |
| esqueleto | escudos | Escudo arriba | 55 % / 66 % (+23 / +32) | +29 / +50 | **CONFIRMADA** |
| esqueleto | escudos | Corro de escudos | 58 % / 54 % (+27 / +21) | +36 / +39 | **CONFIRMADA** |
| esqueleto | espadas | Corro | 87 % / 78 % (+16 / +19) | +13 / +11 | probable |
| esqueleto | arqueros | Tirar desde donde están (quietos) | 100 % / 93 % (+48 / +54) | +19 / +31 | **CONFIRMADA** |
| esqueleto | arqueros | Tirar desde donde están (volea, alcance máximo) | 100 % / 94 % (+48 / +55) | +19 / +34 | **CONFIRMADA** |
| esqueleto | línea | Tirar desde donde están (quietos) | 100 % / 96 % (+48 / +60) | +53 / +72 | **CONFIRMADA** |
| esqueleto | línea | Tirar desde donde están (volea, alcance máximo) | 97 % / 96 % (+45 / +60) | +55 / +72 | **CONFIRMADA** |
| esqueleto | caballería | Tiro a quemarropa | 87 % / 75 % (+47 / +50) | +50 / +50 | **CONFIRMADA** |
| creeper | arqueros / línea / escudos | Atacarlos | supervivientes +40 / +32 / +19 | +36 / +42 / +25 | **CONFIRMADA** |
| creeper | espadas sin escudo | No atacarlos | −11 atacando | −12 atacando | probable (mismo signo en ambos lotes) |
| creeper | caballería | — | +19 atacando | +4 | no confirmada |

Los creepers se midieron como supervivientes azules con 12, 24 y 40 creepers juntos (n = 30 por modo y lote).

## Comer (ronda 4, idea de David; final + confirmación, n = 30 cada uno, semillas 1 y 1001)

Pan ×16 por guardia. **En la mano izquierda** lo usa Guard Villagers (`GuardRunToEatGoal`: se aleja del atacante, come y
se cura); **en la mochila** lo come Rally en el sitio por debajo del 40 % de vida (`GuardPack.consume`). Los escudos no
pueden llevarlo en la mano (la ocupa el escudo). Datos: `tools/torneo-data/t4/`, `t4-confirm/`.

| Amenaza | Composición | Sin comida | Pan en la mano | Pan en la mochila |
|---|---|---|---|---|
| zombi | espadas | 26 % | **92–95 %** (+63–66) CONFIRMADA | 77–90 % (+51–58) CONFIRMADA |
| zombi | escudos | 70 % | — | 90–95 % (+20–35) CONFIRMADA |
| zombi | arqueros | 80 % | **94–97 %** (+13–14; superv. +28–37) CONFIRMADA | 80–88 % no confirmada |
| zombi | línea | 63 % | 93 % (+20–30) CONFIRMADA | 97–100 % (+23–37) CONFIRMADA |
| zombi | caballería | 63 % | **100 %** (+23–37) CONFIRMADA | 97–100 % CONFIRMADA |
| esqueleto | espadas | 84 % | 93–95 % CONFIRMADA | **98–100 %** CONFIRMADA |
| esqueleto | escudos | 13 % | — | **60–63 %, supervivientes 100 %** (+45–47) CONFIRMADA |
| esqueleto | arqueros | 87 % | 100 % CONFIRMADA | 100 % CONFIRMADA |
| esqueleto | línea | 42 % | **100 %** (+55–58) CONFIRMADA | 95–97 % CONFIRMADA |
| esqueleto | caballería | 40 % | **95 %** (+52–55) CONFIRMADA | 93–98 % CONFIRMADA |

(Los porcentajes de "sin comida" son de la final; la base gana más o menos según el lote, por eso cuentan los Δ.)

**Lectura:** la comida es **el efecto más grande medido hasta hoy**, más que cualquier formación o modo de fuego.
- Contra zombis (te rodean), **irse a comer** (mano) le gana a comer en el sitio (mochila): espadas 92 % frente a 77 %;
  los arqueros solo mejoran si se alejan a comer.
- Contra esqueletos, ambas sirven; los escudos con comida en la mochila **no pierden a nadie** (aunque la batalla dure
  más: 90–99 s).
- Dilema real de diseño: **escudo o comida en la mano**. La mochila resuelve el escudo, pero rinde menos contra hordas.
- Consecuencia para el plan de trabajo: la **logística de comida** (fase E3C: reponer, repartir) es oficio de primera,
  no un detalle. Falta medir **cuánta** comida hace falta (16 panes es generoso) y "rotar" de verdad (que alguien cubra
  al que se va a comer y otro tape su hueco).

## Ronda 5: los valores por defecto del juego frente a lo de antes (n = 30 por fila; `tools/torneo-data/t5/`)

Lo que David decidió llevar al juego (`combat/FieldManual`: tiro a quemarropa, creepers para tiradores y escudos, escudo
arriba contra tiradores, mano izquierda escudo > tótem > comida, comer de la mochila), comparado con el comportamiento
anterior (`antes`, con interruptores `off:*`). Victoria / supervivientes:

| Amenaza | Espadas | Escudos | Arqueros | Línea | Caballería |
|---|---|---|---|---|---|
| zombi | 13 / 8 frente a 20 / 13 | 50 / 39 frente a 53 / 40 | **100 / 100** frente a 83 / 62 | **100 / 98** frente a 67 / 45 | **100 / 99** frente a 73 / 68 |
| esqueleto | 90 / 77 frente a 77 / 65 | 7 / 6 frente a 22 / 20 | 70 / 53 frente a 77 / 55 | **78 / 57** frente a 48 / 31 | **87 / 78** frente a 33 / 27 |
| creeper (superv.) | 34 frente a 35 | **74** frente a 40 | **63** frente a 39 | **64** frente a 35 | **48** frente a 26 |

- El tiro a quemarropa por defecto se nota donde el manual lo predijo (todo lo que tira, contra zombis; la caballería
  contra esqueletos), y los creepers ya no matan a tiradores ni escudos.
- **Arqueros contra esqueletos no mejoraban**: lo que ganó ahí fue *tirar desde donde están* (quietos / volea), que no
  estaba entre los valores por defecto del juego. **Llevado al juego el 2026-11-05 y CONFIRMADO** (`holdGroundAgainstShooters`:
  un tirador cuyo blanco dispara tira quieto en vez de acercarse a 15 bloques). Contra esqueletos, victoria /
  supervivientes frente a `antes` (n = 30; semillas 1–30 y 1001–1030): arqueros 100 / 98 % frente a 63–77 / 52–57 %;
  línea 100 / 99–100 % frente a 33 / 21–28 %; caballería 83–90 / 73–74 % frente a 47–53 / 32–37 %. Espadas sin
  cambio (no aplica). Datos: `t5/20261105-070544`, `t5-confirm/20261105-070743`.
- **Escudo arriba, primera versión: no hacía nada.** La sonda `t5-shield-probe` (telemetría por segundo) mostró por qué:
  Guard Villagers ya sube el escudo cuando el blanco es un tirador a ≥ 5 bloques, y la regla "el blanco dispara" decía
  lo mismo (bloqueaban el 43 % del tiempo, igual que antes). El interruptor global de GV que ganó en la ronda 3 lo tenía
  arriba el 89 %, sobre todo en la **mitad del tiempo en que el guardia no tiene blanco pero le siguen disparando**.
- **Segunda versión (bajo fuego = su blanco dispara o un tirador le dio en los últimos 5 s): no confirmada.** El
  mecanismo se repite en los dos lotes (bloquean el 59–60 % del tiempo frente al 41–42 %, paran ≈ 100 golpes frente a
  ≈ 65, reciben ≈ 20 menos con el escudo bajo), pero los supervivientes no: +17 ± 8 con las semillas 1–30 y +3 ± 7 con
  las 1001–1030. Para acercarse al 89 % haría falta subirlo "mientras haya tiradores cerca", no solo 5 s tras un golpe.
  Ojo: lo que ganó en la ronda 3 fue **aguantar, no matar** (batallas hasta el tiempo límite con casi todos vivos).
- **Tercera versión, CONFIRMADA (David, 2026-11-05: contra tiradores, primero sobrevivir).** Bajo fuego = su blanco
  dispara, o un tirador le dio en los últimos 5 s, o **un tirador enemigo que lo ve está a ≤ 24 bloques**; y no está
  rodeado (< 2 enemigos cuerpo a cuerpo a ≤ 3 bloques). Supervivientes frente a `antes`, n = 30 por fila:

  | Contra esqueletos | Semillas 1–30 | Semillas 1001–1030 | Tiempo bloqueando |
  |---|---|---|---|
  | Escudos | 73 % frente a 10 % (+63 ± 6) | 65 % frente a 14 % (+51 ± 7) | 93–94 % frente a 43–44 % |
  | Línea | 60 % frente a 36 % (+23 ± 10) | 64 % frente a 44 % (+20 ± 10) | 90 % frente a 52–53 % |

  Las victorias de los escudos apenas cambian (+13 ± 10 y +3 ± 7: la batalla llega al tiempo límite), pero dejan menos
  esqueletos en pie (≈ 2,5 frente a ≈ 5,8). Contra zombis no cambia nada (bloquean el 18–20 % del tiempo en ambos
  casos; escudos +6 y +16 de supervivientes, sin significación), como se buscaba. Datos: `t5/` y `t5-confirm/`
  (`20261105-054149`, `20261105-054334`).

## Ronda 6: oleadas (David, 2026-11-05; `tools/torneo-data/t6/`, `python3 tools/torneo.py waves …`)

"Un grupo bien organizado debería sobrevivir varias oleadas; el límite es la comida o demasiados a la vez." Hasta 10
olas, una tras otra, con 20 s de pausa entre olas para comer y reagruparse; n ≈ 10 por fila. Olas superadas (media),
manual de campo nuevo (`base`) frente a `antes`:

| Olas | Composición | Sin comida: antes → ahora | Con comida (pan ×16 en la mochila): antes → ahora |
|---|---|---|---|
| 12 zombis | arqueros | 2,0 → **10** (11/11 todas) | 2,6 → **10** (10/10) |
| 12 zombis | línea | 5,4 → **10** (11/11) | 9,0 → **10** (10/10) |
| 12 zombis | escudos | 9,4 → 9,3 | 10 → 10 |
| 10 mezcladas | arqueros | 1,5 → 5,2 | 2,1 → 5,5 |
| 10 mezcladas | línea | 3,9 → 7,8 | 6,3 → **9,9** (9/10 todas) |
| 10 mezcladas | escudos | 5,5 → 6,1 | 8,1 → 8,6 |

- Con el manual nuevo, una línea con comida aguanta **las 10 olas mezcladas** en 9 de cada 10 corridas; antes caía
  hacia la sexta.
- **La comida pesa sobre todo en olas mezcladas** (línea 7,8 → 9,9; escudos 6,1 → 8,6). Contra 12 zombis por ola ya no
  hace falta. En la pausa de 20 s el grupo entero recupera poco (+4 a +11 de vida entre los 8). Comer es lo que sostiene.
- **Fallo encontrado**: en olas mezcladas, 13 de 28 corridas de arqueros con el manual nuevo terminaron atascadas
  contra los últimos 1–4 esqueletos. Los arqueros "tiran desde donde están", los esqueletos se quedan lejos o sin línea
  de tiro, y nadie hace daño en 45 s. Eso también le baja a los arqueros las olas superadas de esta tabla.

## Rondas 15–16: moral en olas grandes (`tools/torneo-data/t15/`, `t16/`)

Ronda 15 (olas mezcladas de 22, con comida): con la moral encendida, una línea de 8 se rompía casi siempre (hasta el
97 % de la unidad rota a la vez) y pasaba 5,8 de 10 olas frente a 10 sin moral. La causa medida
(`act:moraleDown:<causa>`): **"superados en número"** y el **contagio** que lo sigue, que restaban moral todo el rato
aunque la unidad estuviera ganando sin bajas. Ronda 16: **superar en número asusta solo al 20 % mientras la unidad no
tiene bajas**, y vuelve a pesar entero al llegar al 15 % de bajas (`Morale`, factor de intercambio; `off:moraleExchange`
= la regla vieja). Dos lotes por regla, n = 24–35:

| Unidad | Regla vieja | Regla nueva | Sin moral |
|---|---|---|---|
| línea de rango 1 | 7,0 olas, 3,5 en pie, hasta 91 % rota; 17 de 35 corridas se quedan sin pelea (los rotos huyen) | **10 olas**, 7,8 en pie, hasta 6 % rota | 10 olas, 7,9 en pie |
| línea de reclutas (rango 0) | 3,8 olas, 2,8 en pie, 98 % rota | **9,8 olas**, 5,0 en pie, hasta 57 % rota | — |

La moral sigue existiendo: los reclutas aún vacilan y se rompen más que los veteranos; lo que se quitó es el miedo a
un enemigo numeroso que no les está haciendo daño. **Se queda.**

## Rondas 22, 25 y 26: el bestiario (2026-11-08; 8 guardias de rango 1 en línea o arqueros, con pan; GitHub Actions)

"Gana" cuenta el empate como medio. `antes` = el manual de campo apagado; `base` = encendido. n = 4–10 por fila.

- **Vanilla (ronda 25, n = 10)**: con el manual, casi todo es 100 %. Donde el manual se nota: **husks ×40** 40 → 100 %
  (vivos 29 → 98 %), **esqueletos wither ×12** vivos 48 → 100 %, **ahogados ×24** vivos 65 → 100 %. Lo que falla:
  **endermans 0 %** con o sin manual (Guard Villagers los tiene en su lista negra: nadie les pega) y **vindicadores ×12**
  30 % (×6: 100 %).
- **Voladores del modpack** (endermaptera, pájaros estinfálidos, vex, ghasts, phantoms, hasta 8): 88–100 %.
- **Cuerpo a cuerpo y tiradores de Alex's Caves e Ice and Fire** (deeplings, dread thralls, vallumraptores, redcoats,
  disidentes, magos deep one): 100 % hasta 8–16. Se rompen al subir el número: troll ×4 50 %, deepling brute ×4 75 %,
  dread lich ×4 0 %, ignited revenant ×4 12 %, forsaken ×2 0 %, devastador ×4 0 %.
- **Depredadores de Alex's Mobs** (oso, tigre, rinoceronte, komodo, anaconda, cocodrilo, buitre de almas, tusklin):
  100 %, salvo el cocodrilo contra la línea (×4: 67 %, se mete al agua y la pelea se alarga).
- **Dragones de Ice and Fire por edad** (`redNbt:{AgeTicks}`): joven (25 días) 93–100 %; adulto (60 días) 33–67 %;
  **anciano (90 días) 0 %, sin matar ninguno**. Fuego, hielo y rayo igual.
- **Gigantes**: dread queen y cockatrice 100 %; warped mosco 67–83 %; dread knight ×4 50–67 %; gorgona: arqueros 0 %
  (la mirada los petrifica a distancia), línea 83 % con 31 % vivos; tremorsaurio ×2 14–17 %; luxtructosaurio y gusano
  del vacío 33–43 % (empates: no lo matan en 3 minutos); **Tremorzilla 12–28 %, 0 muertes, corridas atascadas**.
- **Jefes de Cataclysm** (Ignis, Harbinger, Monstruosidad de netherita, Guardián del End), **warden** y **cíclope**:
  **0 %, sin hacerles daño que cuente**. La hidra: 40 % (empates).

Lectura: 8 guardias bien llevados ganan a casi todo lo que aparece de noche, en cualquier mod. Lo que no ganan es lo
que el juego pone como jefe. La ronda 28 mide cuántos hacen falta (16 / 32 / 64, mitad escudos y mitad arcos).

**Jefes: calidad, no cantidad (rondas 28 y 30, 2026-11-08).** Con equipo de hierro, ni 64 guardias ganan a Ignis,
Harbinger, Guardián del End, Monstruosidad de netherita o Tremorzilla (0 %): el daño sube mucho menos que el número.
Los **mismos 16** con netherita encantada (Filo V, Poder V, Protección IV) ganan el **100 %** al dragón de fuego
anciano, al Guardián del End, al Harbinger y a la Monstruosidad, con 82–97 % vivos (n = 6 por fila). Diamante o
netherita sin encantar casi no cambian nada. Siguen sin ganar: Ignis (17 %), Tremorzilla (0 %, aunque le hacen seis
veces más daño) y el luxtructosaurio (su vida no baja nunca en el registro: revisar si es inmune o si la lectura falla).
Lectura para el diseño: lo que hace a un guardia capaz contra jefes es la calidad de su equipo, que es justo lo que da
la consagración con tributo (`docs/TRIBUTO.md`).
**Ronda 31 (2026-11-09):** ningún encantamiento solo, sobre hierro, gana a un jefe (Poder V, Filo V o Protección IV:
0–12 % de victorias). Protección es el que más acerca: los guardias viven mucho más (Harbinger: 2 → 72 % vivos) y le
bajan más vida, pero no lo matan. El paquete completo (netherita con los tres) sigue ganando: Monstruosidad y
Guardián del End 100 %, Harbinger 75 %, Ignis 38 % (n = 8). Tremorzilla: 0 % con todo (n = 11–15; con el paquete
completo le dejan 282 de vida en vez de 395). Lectura: lo que gana es aguantar **y** pegar a la vez; la consagración
debería subir las dos cosas juntas. Contra Tremorzilla e Ignis el equipo no basta: toca táctica y armas.
**Ronda 32 (2026-11-09): cada jefe pide otra composición.** Mismos 16 en netherita encantada, solo cambia cómo
pelean (% victorias, n = 8–16):

| Jefe | Línea | Solo arcos | Solo escudos | Solo espadas | Volea a 60 | Tortuga |
|---|---|---|---|---|---|---|
| Harbinger | **100** | 0 | **100** | 67 | 89 | 88 |
| Ignis | 11 | **50** | 0 | 0 | 11 | 38 |
| Tremorzilla | 0 | 0 | 0 (61 % vivos) | 0 | 0 | 0 |

Contra el Harbinger, solo arcos pierde siempre y solo escudos gana siempre; contra Ignis es al revés (los arcos son los
únicos que lo bajan a la mitad; de cerca los queman). No hay una composición buena para todo: elegir la composición
según el enemigo es justamente lo que debe hacer el Battle Planner (y la doctrina por amenaza). Tremorzilla sigue sin
caer con nada de esto: lo que más lo aguanta son los escudos.

**Muñeco y moral (rondas 33–34, 2026-11-09).**
- *La moral les cuesta la mitad contra los jefes.* En las peleas contra jefes se rompe el 79–90 % de los guardias (el
  primero a los ~20 s). Con la moral apagada, la misma táctica: Ignis 44 → **100 %**, y todas las peleas terminan en
  la mitad del tiempo (Harbinger 123 → 55 s); los guardias golpean 2–3 veces más por segundo vivo. Hasta el muñeco de
  1024 de vida los asustaba ("monstruo"). Tremorzilla: 0 → **33 %** sin moral (n = 12–16), lo primero que lo mata.
  **Decisión de David (2026-11-09): la funcionalidad primero.** En el juego, los veteranos (rango 3 o más,
  `[morale] fearlessRank`) nunca temen ni se rompen; las pruebas contra jefes se hacen sin moral (`off:morale`). La
  moral se retomará solo si mejora el mod.

**Cuántos guardias hacen falta (ronda 35, 2026-11-09).** Sin moral, línea mitad escudos y mitad arcos, n = 5–6 por
celda: el grupo más chico probado que gana el 80 % o más.

| Amenaza | Hierro | Netherita encantada |
|---|---|---|
| Dragón de fuego joven | 2 | 2 |
| Dragón de fuego adulto | 8 | 4 |
| Dragón de fuego anciano | más de 32 (60 % con 16 y con 32) | 8 |
| Monstruosidad de netherita | no gana ni con 16 | 4 |
| Guardián del End | no gana ni con 16 | 4 |
| Harbinger | no gana ni con 16 | 4–8 (71–83 %) |
| Ignis | no gana ni con 32 (20 %) | 8 (80 %) |
| Tremorzilla | no gana ni con 32 | 32 (83 %; 16: 20 %) |

La progresión que buscaba David sale sola: con buen equipo el número baja a la mitad o a un cuarto, y contra los jefes
el hierro no alcanza a ningún tamaño (la inmunidad de medio segundo por golpe pone el techo). Son pocas corridas por
celda: es la primera tabla, no la definitiva.
- *Daño máximo de un guardia solo contra el muñeco* (daño por segundo, sin moral, n = 7–9): arco de hierro 5,2;
  arco con Poder V 13,2; espada de hierro 2,4; espada de netherita con Filo V 4,2; escudo + espada 2,1 → 4,6. El arco
  pega 2–3 veces más que la espada.
- *Juntos contra un solo blanco, cada guardia pega mucho menos* (8 contra el muñeco: arcos 5,2 → 1,6 por guardia,
  espadas 2,4 → 0,9). Es una regla de Minecraft: un mob que acaba de recibir un golpe es inmune medio segundo, salvo a
  un golpe más fuerte. Contra un blanco único caben unos 2 golpes completos por segundo, vengan de 3 guardias o de
  300. Por eso más guardias casi no suben el daño contra un jefe y un golpe más fuerte (equipo, consagración,
  maestría) sí. Candidatas que salen de aquí: tiros coordinados que caigan justo cuando termina esa inmunidad, y
  repartir guardias entre varios blancos.

## Ronda 36: armas de fuego (2026-11-10; `tools/torneo-data/t36/`)

Meta de David: que contra pistoleros los guardias saquen resultados parecidos a los de contra esqueletos **sin tocar
el daño de las armas ni la defensa del escudo**: por coordinación. 8 guardias, sin moral, con pan, n = 6.

| Enemigo | Composición | Base (% victorias / % vivos) | Volea empezando a 60 |
|---|---|---|---|
| 16 saqueadores con Pax | arqueros | 0 / 0 | **83 / 71** |
| 16 saqueadores con Pax | línea | 100 / 46 | **100 / 94** |
| 16 saqueadores con Pax | línea con escudo romano | 100 / 50 | **100 / 94** |
| 16 saqueadores con Pax | escudos | 100 / 94 | — |
| 8 saqueadores con Pax | arqueros | 50 / 23 | **100 / 100** |
| 8 vindicadores con Callwell | arqueros / escudos | 100 / 100 · 100 / 73 | — |
| 8–16 casacas rojas, 8 disidentes | todas | 100 / ~100 | — |
| 16 esqueletos (control) | todas | 100 / 100 | — |

Lectura: los pistoleros propios de Scorched Guns no son el problema (su daño llega como flecha y el manual ya sabe
pelear contra tiradores). El problema es el saqueador con Pax: dispara rápido a media distancia. **Mantener la
distancia y disparar en volea** (nuestro alcance es mayor que el suyo) lo lleva a cifras de esqueleto sin cambiar
ningún número. Los escudos paran las balas. La cobertura empeora (los guardias se separan de la línea) y la tortuga no
cambia nada. Es la primera regla del "programa táctico" (`docs/RALLY_TACTICAL_SYSTEM.md` §3). Candidata: confirmar
con otras semillas (ronda 37) antes de ponerla por defecto.

## Noche del 2026-11-11: rondas 37–41 (`tools/torneo-data/night/`)

8 guardias, sin moral, con pan. % victorias / % vivos.

**Ronda 37, distancia contra pistoleros.** Confirmación con otras semillas (n = 20, semilla 1001), 16 saqueadores con
Pax: arqueros 5/1 → **100/81** con volea a 60; línea 80/29 → **100/98**; línea romana 85/35 → **100/96**. Confirmado.
Barrido (n = 8): la volea gana desde 50 bloques (arqueros a 40: 25/8; a 50: 100/67; a 80: 100/91) y **el modo volea
es lo que la sostiene**: "fuego normal empezando a 60" da 0/0 en arqueros (el guardia camina hasta su distancia
óptima y entra en la de ellos). Con volea a 60 una línea de 8 gana a 24 y a 32 Pax (100/83–86). Vindicadores con
Callwell ×16: línea 0/0 → 100/48, arqueros 67/31 → 100/100. Hornlins y piglins con armas: ya 100/≈100.

**Ronda 41, la regla puesta por defecto** (`standOffVsGuns`: un tirador cuyo blanco lleva un arma de Scorched Guns
dispara en volea y se aleja a 48–56 bloques), empezando a 30 bloques como en el juego, n = 10, regla / sin regla:

| Enemigo | arqueros | línea | línea romana | escudos |
|---|---|---|---|---|
| 16 Pax | **100/84** · 0/0 | **100/98** · 90/35 | **100/98** · 80/34 | 100/88 · 100/84 |
| 32 Pax | **80/39** · 0/0 | **100/60** · 40/19 | **100/96** · 100/41 | 90/75 · 100/88 |
| 16 vindicadores con Callwell | **100/100** · 50/8 | **100/50** · 60/24 | **100/54** · 10/4 | 0/0 · 0/0 |
| 16 casacas rojas, 16 esqueletos (control) | sin cambio (100/100) | sin cambio | sin cambio | sin cambio |

Es la meta de David cumplida para los saqueadores: resultados de esqueleto sin tocar daño ni escudos. Pendiente: los
escudos solos contra Callwell (0 %): la regla no los toca (no son tiradores); es un hueco de composición, no de regla.

**Ronda 38, fuego coordinado** (`combat/FireWindows`; dos lotes, semillas 1 y 1001, n = 6–8 cada uno):
- *Contra muchos blancos* (24 zombis, 16 esqueletos): mismo resultado (100/100) con **un tercio menos de flechas**
  (arqueros contra zombis 130 → 80; esqueletos 88 → 52–60) y la pelea **acaba antes** (16 → 12 s; 21 → 17 s). Los
  golpes dentro de la invulnerabilidad bajan de 25–50 por pelea a 2–9. Repartir y sincronizar dan lo mismo aquí.
- *Contra un solo blanco* (muñeco, dragón adulto, Harbinger, Ignis): **sincronizar** gasta 60–75 % menos flechas
  (muñeco 551 → 151, Ignis 388 → 152) con el mismo daño total: confirma que el techo contra jefes es la regla de
  invulnerabilidad, no la puntería. Repartir no cambia nada (no hay a quién repartir). No sube las victorias: las
  diferencias entre filas están dentro del ruido.
- *Contra Pax con volea a 60*: sin efecto (ya gana).
Lectura: es una mejora de **logística** (flechas = tributo y suministro) y de tiempo, no de victorias. Candidata a
regla por defecto ("no gastar flechas en un blanco invulnerable"), pendiente de decidir con David.

**Ronda 39, laboratorio de retirada** (arena de 96 con muro, n = 6; pelear / RETIRARSE):
- Contra 1 dragón joven: pelear 100/100; retirarse 83–100/83–90 y tarda 3 veces más.
- Contra 1 dragón adulto: pelear 100/65–81; retirarse **17/15** (línea) y **0/0** (caballería).
- Contra 3–5 dragones: nadie gana; al minuto 2 siguen vivos más **peleando** (línea contra 3 jóvenes 92 % vivos
  peleando, 64 % retirándose; caballería 69 % → 20 %).
Lectura: hoy la retirada es peor que pelear en todos los casos. Huir de un volador más rápido que tú sin dejar de
disparar ni cubrirse regala tiempo de aliento. Una retirada buena necesita retaguardia que siga tirando, escudos
detrás y un punto seguro (o terreno abierto para medir llegada): primera tarea concreta de la fase 4.

**Ronda 40, techo del cerebro contra jefes** (netherita encantada, n = 5): el oráculo completo (información perfecta)
contra Tremorzilla con 16 guardias pasa de 0 % a 14 %, con 32 de 80 % a 100 %; el fuego sincronizado sin trampas da
lo mismo que el oráculo (16: 25 %; 32: 100/68). Contra Ignis con 4: Rally 40 %, oráculo 60 %, sincro 80 %. El
cerebro tiene margen pequeño contra jefes; el resto es cuerpo y equipo.
Repetición con semillas 1001: igual (Tremorzilla 16: Rally 20 %, oráculo 29 %, sincro 33 %; 32: 100 % los tres).

**Ronda 42, "cuántos guardias" con fuego sincronizado** (la tabla de la ronda 35, n = 5): el número mínimo casi no
cambia (dragón adulto 4/8, anciano 8 en netherita, Ignis 8, Tremorzilla 32); Harbinger con 4 en netherita 71–83 % →
100 %, Tremorzilla con 16 20 % → 50 %. Señal pequeña, a confirmar. Contra los jefes, el fuego sincronizado ahorra
flechas, no guardias.

**Ronda 43 (parcial), techos de la ronda 24 con fuego sincronizado:** fantasmas ×20–35: mismo resultado dentro del
ruido, golpes en invulnerabilidad 22–31 → 2–13. Zombis, esqueletos y olas de quiebre: seguían corriendo al cerrar.

## Rondas 44–45: retirada nueva y distancia aprendida (2026-11-11 tarde; `tools/torneo-data/night/20261111-16*`)

**Ronda 44, retirada.** Causa del fracaso de la ronda 39: escudos y caballería dejaban de pelear al retirarse, y la
orden de retroceso marcaba a la escuadra como "en retirada", así que nadie respondía. La nueva (`RETREAT_FIGHTING`, por
defecto en RETIRARSE) aguanta disparando, retrocede un salto disparando y repite; si el enemigo queda fuera de alcance
sigue alejándose. % vivos al final (n = 6–12), nueva / vieja / pelear sin retirarse:

| Contra | línea | arqueros | caballería |
|---|---|---|---|
| 1 dragón adulto | **94** / 2 / 92 | 96 / 94 / 94 | 75 / 0 / 83 |
| 3 dragones jóvenes | **99** / 33 / 95 | 98 / 99 / 97 | 80 / 12 / 89 |
| 5 dragones jóvenes | **79** / 10 / 74 | 89 / 91 / 91 | **41** / 6 / 19 |
| 32 vindicadores | **48** / 9 / 46 (gana 100 %) | 100 / 88 / 100 | 0 / 0 / 0 |
| 3 dragones adultos | 2 / 0 / 2 | 2 / 7 / 0 | 0 / 0 / 0 |

Lectura: la retirada ya no cuesta vidas (igual o mejor que quedarse a pelear) y gana el doble de distancia al enemigo
(distancia media 20–29 frente a 8–15). Contra lo que no se puede ganar (3 dragones adultos) nadie sobrevive dentro de
una arena con muro: escapar de verdad necesita terreno abierto y un refugio (siguiente versión del laboratorio).

**Ronda 45, distancia contra pistoleros: fija contra aprendida** (n = 8; % victorias / % vivos). La aprendida empieza a
36, se aleja 4 por bala recibida, se acerca 1 cada 2 s sin balas, y dispara directo dentro del alcance del arco:

| Contra | Composición | fija (48–56) | aprendida | sin regla |
|---|---|---|---|---|
| 16 Pax | arqueros / línea / línea romana | 100/95 · 100/89 · 100/92 | 100/89 · 100/95 · 100/88 | 25/8 · 88/41 · 100/42 |
| 32 Pax | arqueros / línea / línea romana | **88/36** · 100/66 · 100/83 | 12/3 · 88/62 · 100/88 | 0/0 · 50/17 · 88/31 |
| 16 Callwell | arqueros / línea / línea romana | 100/100 · 100/55 · 100/66 | 100/100 · 100/61 · 100/66 | 100/91 · 75/39 · 75/38 |
| Pax y Callwell | caballería | 25/11 · 0/0 · 100/47 | 38/8 · 0/0 · 100/47 | 25/12 · 0/0 · 75/38 |

Lectura: para las líneas da lo mismo; para arqueros solos contra muchos Pax, aprender la distancia **cuesta caro**
(cada lección es una bala). Queda la fija por defecto; la aprendida sigue apagada (`standOffAdaptive`). **La caballería
es el hueco contra armas de fuego** (0–38 % contra Pax): la regla no la toca porque el jinete lo conduce la montura.

## Mecánicas de los jefes, leídas en su código fuente (2026-11-11; ficha F4, paso previo)

Fuente: Cataclysm `lender544/new1.20.1` rama `1.21`; Alex's Caves `AlexModGuy/AlexsCaves` rama `1.21.1`. Cambia la
lectura de las rondas 30–42: contra estos jefes el techo **no** es solo la invulnerabilidad de medio segundo.

**Cataclysm (Ignis, Harbinger, Monstruosidad, Guardián del End)**, en `hurt()` y en este orden:
1. Tope por golpe (`damage_cap`): 20–25.
2. **Caída por distancia del TIRADOR** (no de la flecha): daño completo hasta `range_cap`, cae a 0 en 1,5 ×.
   - Ignis: 15 → 22,5.
   - Guardián: 12 → 18.
   - Monstruosidad: 18 → 27.
   - Harbinger: 35 → 52,5.
   - Un arquero a 30 bloques de Ignis no le hace nada.
3. **Tope de daño por segundo compartido** entre todos los atacantes (`dps_cap`, antes de armadura): Ignis 14,
   Harbinger 14, Monstruosidad 20, Guardián 13. Muerte más rápida posible: 26–33 s, la ataque quien la ataque.
4. Luego la armadura (10–20).
5. Se cura 25 de vida por segundo si se queda sin blanco 10 s. Las flechas no reinician ese contador; solo los golpes
   cuerpo a cuerpo.

Extras por jefe:
- **Ignis:** su escudo para todo golpe de frente (flechas incluidas) hasta la fase 3; contraataque inmune; fases
  inmunes.
- **Harbinger:** **inmune a flechas por debajo de la mitad de su vida**; se cura 2/s en combate.
- **Guardián del End:** **inmune a flechas mientras tenga el casco** (hasta la mitad de su vida).
- **Monstruosidad:** la cabeza recibe × 1,5; inmune durante el rugido.

**Alex's Caves:**
- **Tremorzilla:** las flechas hacen 35 %. Golpear sus partes no hace nada desde un mob, solo el cuerpo principal.
  Se regenera en combate: 0,4/s, 3/s bajo la mitad y 10/s bajo el 20 %. Ataque 30, alcance de seguimiento 128.
- **Luxtructosaurio:** flechas al 55 %; tras cada golpe, 10 ticks a × 0,35; **se cura 12/s si no hay ningún jugador a
  150 bloques**. Por eso su vida "nunca bajaba" en la simulación, que no tiene jugadores cerca.

**Qué implica para la coordinación** (hipótesis para la ficha F4, sin medir):
- Contra Cataclysm, más guardias no suben el daño por encima de unos 14/s. La gracia es llegar a ese tope arriesgando
  lo menos posible: pocos golpeadores dentro del radio y el resto fuera.
- Los arqueros tienen que estar **dentro** del `range_cap` del jefe y, contra Ignis, de lado o por detrás.
- Contra el Harbinger, flechas en la primera mitad y cuerpo a cuerpo en la segunda. Contra el Guardián, al revés.
  Es un cambio de roles por fase: justo lo que el motor debe saber hacer.
- Contra Tremorzilla y el Luxtructosaurio, el arco vale un tercio o la mitad. El daño tiene que superar su
  regeneración, sobre todo al final.

## Rondas 46–49: caballería, atracción y reconfirmación con n = 30 (2026-11-11 noche; `tools/torneo-data/t46-48/`)

Norma de David desde hoy: n ≥ 30 por brazo, con las mismas semillas en todos los brazos, para todo valor por defecto.

**F2, caballería (ronda 46, n = 30).** 8 jinetes (4 espadas + 4 arcos), % victorias / % vivos:

| Brazo | 16 Pax | 32 vindicadores |
|---|---|---|
| (a) hoy | 30/10 | 0/0 |
| (b) a pie | 87/28 | 93/37 |
| (c) caballos inmortales | 97/62 | 0/0 |
| (d) solo arqueros montados | 100/66 | 0/0 |
| (e) quietos a 48–56 | 100/47 | 53/12 |
| (f) **alejarse** (pistolero: 48–56; cuerpo a cuerpo a menos de 14 → galopar a 24) | 100/47 | 83/30 |
| (g) solo arqueros montados que se alejan | **100/98** | **100/96** |

Diagnóstico: contra armas de fuego el lastre es el caballo (lo matan; inmortal gana). Contra cuerpo a cuerpo es la
táctica: el jinete arquero se plantaba a 12 bloques y lo rodeaban.

**Ronda 49, la regla "alejarse" contra la de hoy** (n = 30, semillas 3001):

| Enemigo | hoy | alejarse |
|---|---|---|
| 16 Pax | 47/12 | **100/47** |
| 32 Pax | 0/0 | **54/24** |
| 16 Callwell | 93/57 | 100/54 |
| 32 vindicadores | 0/0 | **87/30** |
| 40 zombis | 73/65 | **100/85** |
| 16 esqueletos | 100/100 | 100/100 |

Confirmado y **por defecto** (`riderKite`). Los arqueros montados solos, con la regla, son lo mejor medido contra
ambos (96–98 % vivos). Los jinetes con espada siguen cargando; ahí queda margen.

**F1, atracción (ronda 47, n = 10 por mob, 20 mobs).**
- **Ningún mob cambia de blanco hacia el último que le pegó** (`attrSwitch` 0,4–0,6 con golpes alternos: el valor de
  "se queda con el primero"). Es Minecraft: el objetivo "vengarse" que ya está corriendo no se reinicia con un golpe
  nuevo.
- Al salir del alcance de seguimiento lo suelta en 3–4 ticks. Alcances: esqueleto, araña, bruja y piglin 16;
  vindicador 12; zombi 34; saqueador y devastador 32; Cataclysm 40–50; dragón y Tremorzilla 128.
- Implicaciones:
  - El **"ping-pong" de fuego no funciona** por golpes alternos.
  - El **cebo sí**: quien entra primero se queda con el mob mientras siga vivo y dentro del alcance. El resto dispara
    tranquilo. La pantalla de escudos de la escolta ya ganaba por esto.
  - Contra 12–16 de alcance, retroceder rompe el contacto enseguida. Contra dragones y Tremorzilla (128), nunca.

**Ronda 48, reconfirmación de los valores por defecto de hoy** (n = 30, semillas 2001):
- **Distancia contra pistoleros:** confirmada en llano y en bosque (16 Pax, arqueros 20 → 100 %; 32 Pax, línea 43 → 97 %;
  Callwell en bosque, línea 13 → 100 %). **En la aldea no gana**: los guardias no mueren (100 % vivos contra 44 %)
  pero no rematan a los pistoleros escondidos entre las casas, y la pelea se estanca. La sospecha de Fable era
  cierta: hace falta una regla para terreno cerrado.
- **Fuego sincronizado:** la mejora de victorias de la ronda 43 **no se confirma** (95 esqueletos: 67/31 con, 70/41
  sin). Mismas victorias, menos flechas; algo mejor contra 75 zombis (74 frente a 65 % vivos) e Ignis (44 frente a
  34). Sigue por defecto como ahorro de flechas, no como ventaja de combate.
- **Retirada nueva:** confirmada contra la vieja (línea contra 1 dragón adulto 13 → 95 % vivos; contra 32 vindicadores
  6 → 47). Frente a quedarse a pelear: igual a pie (95 frente a 93; 73 frente a 75) y **peor a caballo** (69 frente
  a 84; 35 frente a 45). Fable tenía razón: hoy es "pelear a más distancia", no una huida. Escapar de verdad queda
  para el laboratorio de retirada real (F5).

## Rondas 50–51: qué mantiene vivo al caballo y los jinetes con espada (2026-11-12; `tools/torneo-data/t50-51/`)

**Ronda 50 (F2b, idea de David), n = 30, con la regla de jinete por defecto.** % de jinetes vivos:

| Caballo | 16 Pax, solo arqueros | 32 Pax, solo arqueros | 16 Pax, mixta | 32 vindicadores, mixta |
|---|---|---|---|---|
| sin armadura | 99 | 65 | 45 | 31 |
| hierro | 98 | 69 | 45 | 30 |
| diamante | 98 | 68 | 48 | 28 |
| "superarmadura" (20 + 8 de dureza) | 100 | 70 | 48 | 26 |
| +50 % de vida | 97 | 75 | 46 | 27 |
| diamante + 50 % de vida | 98 | **77** | 47 | 26 |
| escudo frontal | 98 | 65 | 46 | 30 |
| inmortal (techo) | 98 | **85** | 49 | 26 |

Victorias: 87–100 % en todas las filas.

Lectura:
- **Con la regla de alejarse, el caballo dejó de ser el problema casi siempre.** Solo pesa contra muchos pistoleros
  (32 Pax): ahí el techo es 85 % vivos y diamante con +50 % de vida cierra el 60 % de la diferencia (65 → 77). La
  superarmadura salva caballos (muertes 4,6 → 1,2) pero no tantos jinetes.
- **En la mixta lo que muere son los jinetes con espada**, no los caballos: el caballo inmortal no cambia nada
  (45 → 49; 31 → 26).
- **El escudo frontal no paró casi nada** (0–1,2 bloqueos por pelea): cuando el jinete se aleja, el caballo da la
  espalda a quien dispara. Un escudo trasero o lateral tendría más sentido; o nada.
- Propuesta para el juego: que el **vínculo jinete-caballo** (ya existe, crece con el tiempo montado y hoy solo
  cambia la obediencia) dé **hasta +50 % de vida al caballo**, y que la armadura de diamante tenga sentido. Es una
  mejora real pero chica (+10–12 puntos de vivos solo contra muchos pistoleros); no es la palanca grande.

**Ronda 51 (F2c), jinetes con espada que golpean y salen** (carga, golpe, salen 12 bloques 1,5 s, vuelven), n = 30:
**no sirve**. 32 vindicadores, mixta: 97 → 77 % victorias; 40 zombis, mixta: 88 → 77 % vivos. Ocho espadas montadas
pierden contra todo salvo esqueletos, con y sin la regla. Descartada. La caballería que funciona es la de arqueros.

## Ronda 52 (F5): laboratorio de escape con refugio, primera versión (2026-11-12; `tools/torneo-data/t52-53/`)

Arena de 160, refugio 50 bloques detrás; la orden llega a los 10 s de pelea; n = 30. % victorias / % vivos al final
(2 min):

| Contra | Composición | pelear | retirada que pelea | huida sin pelear |
|---|---|---|---|---|
| 40 zombis | línea / arcos / jinetes | 100/100 · 100/100 · 100/83 | 100/100 · 100/100 · 100/**96** | 0/76 · 0/100 · 100/60 |
| 32 vindicadores | línea / arcos / jinetes | 100/49 · 100/99 · 97/43 | 78/48 · 100/99 · 87/27 | 0/25 · 2/71 · 0/0 |
| 4 devastadores | línea / arcos / jinetes | 100/89 · 100/100 · 100/55 | 100/88 · 100/100 · 100/62 | 66/77 · 73/95 · 73/32 |
| 3 dragones jóvenes | todas | 0/94–96 | 0/94–98 | 0/92–95 |
| 3 dragones adultos | línea / arcos / jinetes | 0/2 · 0/13 · 0/22 | 0/2 · 0/3 · 0/18 | 0/0 · 0/0 · 0/3 |

Lectura:
- **Huir sin pelear es lo peor en todos los casos**; la retirada que pelea iguala a quedarse.
- Contra enemigos de tierra estos grupos ganan de todas formas, así que no miden un escape. Contra 3 dragones
  adultos nadie se salva con ninguna opción.
- **Casi nadie llega al refugio** (10–40 %, en 15–35 s los que llegan). Hay que diagnosticarlo antes de
  sacar conclusiones: posiblemente los guardias responden al que los golpea y se quedan peleando aunque la orden diga
  huir.
- Siguiente versión: enemigos de tierra que no se puedan ganar (80 vindicadores, 8 devastadores) y arreglar el
  movimiento hacia el refugio.

## Ronda 53 (F9): pistoleros entre casas (2026-11-12; `tools/torneo-data/t52-53/`)

Aldea (casas de piedra en dos hileras), n = 30 por brazo (más corridas repetidas por atasco). % victorias / % vivos:

| Contra | Composición | sin regla | regla de distancia | buscar vista (la regla suelta sin línea de visión) |
|---|---|---|---|---|
| 16 Pax | arcos / línea / línea romana | 14/10 · 46/39 · 56/41 | 0/80 · 50/90 · 52/88 | **45/60 · 62/82 · 60/80** |
| 32 Pax | arcos / línea / línea romana | 0/0 · 5/16 · 12/15 | 0/43 · 7/69 · 11/71 | 2/15 · 21/44 · 23/47 |
| 16 Callwell | arcos / línea / línea romana | 0/48 · 16/23 · 11/28 | 0/100 · 0/50 · 0/49 | 0/81 · 7/40 · 2/37 |

Lectura:
- Entre casas ninguna regla actual gana de verdad. La de distancia **salva vidas** (vivos × 2–8) pero casi no gana.
- "Buscar vista" gana más (16 Pax, arcos 0 → 45 %) a cambio de vidas (80 → 60).
- Las peleas duran 70–120 s y muchas se atascan.
- Hace falta una táctica propia de terreno cerrado: escudos delante que paran balas mientras los arcos se acercan
  por cobertura, o tiro alto por encima de las casas. Ficha F9 abierta para Fable.

## Rondas 52 v2 y 54 (F5 v2, F11): huida, supresión, pinza (2026-11-14; `tools/torneo-data/r52v2/`, `r54/`)

- Huida como directiva RETREAT (FALLBACK): sigue sin ganar a pelear (% vivos, arco contra 8 devastadores 100 → 73;
  caballería 0 % contra 32 y 80 vindicadores). Llegan al refugio y se quedan ≤ 5 %. Contra enemigos de tierra no hay
  ventaja en huir.
- Retroceso II no mejora supervivencia donde ya se gana (+0–5 puntos).
- Pinza: la estructura `unit:4` sube vivos de línea contra 32 vindicadores (9,2 → 11,6 de 16); FLANQUEAR no añade nada
  (11,8). Caballería: nada.

## Ronda 52 repetida (F23): la retirada con la orden viva (2026-11-17; `tools/torneo-data/f23/`)

Las rondas 52 v2 y 55 midieron una orden que moría tras un paso (arreglado en F16f). Repetida con las mismas semillas
(n = 30 por brazo, 8 guardias, refugio a 50 bloques):

- **Retirada que pelea**: ya dura toda la pelea, y aun así no salva más que quedarse. Donde se gana, da lo mismo
  (32 vindicadores: línea 50 % vivos con y sin orden); donde el enemigo es mucho, cuesta (arco contra 80 vindicadores
  67 → 49 % vivos; línea contra 8 devastadores 69 → 59 %). Al final hay ≤ 11 % de los guardias en el refugio.
- **Huida sin pelear**: a pie, 38–76 % llega al refugio en unos 20 s, pero no se queda (0–9 % al final; 29 % el mejor
  caso) y mueren 30–52 puntos más que peleando. A caballo no sobrevive nadie (0 %).
- Para el jugador, hoy: contra enemigos de tierra, **no ordenar retirada para salvar gente**; pelear deja igual o más
  vivos. Falta una retaguardia que cubra a los que se van (siguiente paso de O3).

## Ronda 48b (F19): dónde se pierde contra 32 pistoleros Pax en llano (2026-11-16; `tools/torneo-data/t48b/`)

n = 30 por brazo, semillas 5901–5930, 8 guardias contra 32 Pax. Solo medidas; no cambia ninguna regla.
- La ronda 48 se repite: arqueros con la regla de distancia 77 ± 8 % (antes 57 ± 9; juntos 67 ± 6, n = 60); línea
  con regla 97 %; línea con escudo romano 97 % y 6,7 de 8 vivos; sin regla 0 / 33 / 87 %.
- Todas las bajas son por bala. Los impactos perdidos (cancelados 0 %, en ventana de invulnerabilidad 2–3 %) no
  explican nada aquí.
- Arqueros solos: la distancia (39 bloques) baja el daño recibido de 27 a 5,4 de vida por segundo, pero no lo corta,
  y lo paga con la mitad de cadencia (0,73 contra 1,49 tiros por guardia-segundo) y menos puntería (73 contra 91 %;
  la mitad de los fallos son flechas paradas para no dar a un compañero). Las derrotas son por poco: quedan 8 de 32.
- Los escudos son lo que funciona: atraen el 77 % de la atención y paran el 93 % del daño que llega.

## Rondas 61–62 (F21, F22): composición contra jefes, confirmada (2026-11-16; `tools/torneo-data/t61/`, `t62/`)

n = 30 por brazo y lote, dos lotes con semillas distintas (6101–6130 y 6201–6230), sin moral (`off:morale`). No
cambia ninguna regla del juego: es lo que debería proponer el Battle Planner.
- **Tremorzilla, 16 guardias en netherita+: todo escudos (16 espada + escudo) gana 77 ± 5 %** (n = 60; línea 8 + 8:
  18 ± 5 %) y deja 10,0 vivos de 16 en vez de 2,6. El rayo hace la mitad de daño (114 contra 244 por corrida) y mata
  a 3,4 en vez de a 10,1. Cuando gana tarda 94 s (la línea, 70 s).
- **Solo vale con netherita+ (Protección IV y Filo V).** Los mismos 16 escudos con netherita sin encantar o con
  diamante ganan 0 de 30 y quedan igual o peor que la línea netherita+. El escudo para lo mismo en los tres niveles;
  lo que pasa hace 2–3 veces más daño. No está medido cuál de los dos encantamientos lleva el efecto.
- **Ignis, 8 guardias en netherita+: menos cuerpos delante.** 2 escudos + 6 arcos dejan 4,78 vivos de 8 (60 %) y
  8 arcos 4,65 (58 %), frente a 3,13 (39 %) de la línea 4 + 4, con las mismas victorias (98 %, n = 60) y algo más
  rápido (82 y 79 s contra 93). Mueren menos por llamarada (1,1 y 0,7 contra 2,7 por corrida).
- Peor que la línea (F21, sin repetir): 3 escudos por arco contra Ignis; todo espadas sin escudo contra Tremorzilla.
- Aviso: contra Tremorzilla 4–7 corridas de 30 por brazo se cortan con 1–8 guardias vivos y el jefe con casi toda la
  vida tras una pelea larga; cuentan como no victoria. Sustituidas por su reintento, todo escudos da 83 %.

## Ronda 63 (F24): el muro de la arena contra pistoleros (2026-11-17; `tools/torneo-data/f24/`)
8 arqueros contra 32 Pax en llano, n = 30 pareado, semillas 6301–6330. **Candidata, sin confirmar con semillas nuevas.**
- **Sitio para retroceder**: misma separación inicial, arena de 160 en vez de 96: 57 → 100 % victorias (30 de 30),
  1,8 → 6,6 vivos de 8, daño recibido por segundo 6,0 → 1,6 (vida Pax restante −34,9, z −3,7). La distancia media
  solo sube de 39 a 44 bloques (la regla quiere 48–56): los Pax avanzan casi tan rápido como los arqueros retroceden,
  pero esos 5 bloques bastan. Cuesta 26 % más flechas y 14 % más tiempo.
- **Soltar el tiro en vez de retenerlo** (`fire:spread`): +24 impactos por corrida, de los que +22 caen en
  invulnerabilidad; la vida Pax restante no cambia (z −0,1). Indicios en secundarias (menos daño recibido, z −3,0 y
  −2,3; 3–6 s menos), sin confirmar. **No mirar ventanas** (`fire:off`): igual o peor en todo; descartado.

## Lo que **no** funciona (candidatas a quitarse o rehacerse)
- **HOSTIGAR, doctrina HARASS (`hostil`) y AVOID_CASUALTIES (`prudente`)** quedaron entre las peores en casi todas las
  filas del descarte (0–33 % de victorias donde la base ganaba ≈ 50 %).
- **Escudo arriba en grupo contra zombis**: en el duelo es lo mejor (1 contra 3: 97 % frente a 50 %), pero en grupo
  rinde menos que la base (final 33 % frente a 50 %, sin confirmar). Para casi cuatro veces más golpes (79 frente a
  22 por batalla), pero los que le entran con el escudo arriba (por los lados o por detrás) pasan de 17 a 63: rodeado,
  el escudo frontal no basta. Contra esqueletos, que tiran de frente, en cambio gana.
- **El escudo de Guard Villagers tal como viene** apenas aporta: para 2–4 golpes por duelo y recibe 5–15 con el escudo
  bajo; contra esqueletos es peor que el mismo hierro sin escudo.

## Lo que esto pide decidir (no se ha cambiado nada fuera de la simulación)
1. **Tiro a quemarropa por defecto**: bajar la distancia de seguridad de los tiradores (5 → 1 bloque) para todos los
   guardias. Es la ganancia más grande y no tiene coste de fuego amigo, porque las flechas propias ya atraviesan.
2. **Creepers**: para que los tiradores y los escudos los ataquen fuera de la simulación hace falta quitar al creeper
   de la lista negra de Guard Villagers solo para nuestros guardias (un mixin pequeño en `Guard#setTarget`/`canAttack`)
   o cambiar la configuración de Guard Villagers para todos.
3. ~~**Escudo arriba como doctrina**~~: hecho en el juego por guardia (`combat/FieldManual` + `GuardRaiseShieldMixin`) y
   confirmado en la ronda 5 (ver arriba).
4. **Comida**: ¿los guardias de David llevan comida por defecto (mochila de guerra) y en qué cantidad? ¿Rally debería
   mover la comida a la mano izquierda cuando no hay escudo, para que Guard Villagers se aleje a comer?

## Rondas 70–78 (F31–F39): escudo, `kite:big` y `fire:spread` (2026-11-18; `tools/torneo-data/t70/…t78/`)
- Ignis: 8 arcos con `kite:big=14-22` dejan 6,4 de 8 (n=60, 100 % victorias) frente a 4,9; con 18–26 la pelea llega al tope de 240 s.
- Armas de fuego en abierto: `fire:spread` +1,78 ± 0,37 vivos contra 48 Pax (n=60). Aún no es defecto (regresión sin separar de mods).
- Caballería mixta: cambiar 4 espadas por 4 escudos con espada (también grebas) = 7,1 vivos en 23 s frente a 3,4; reserva y flanco empeoran.
- Aldea: espadas sin escudo mueren en ~11 s; 8 escudos vs 32 Pax 83 % / 4,7 frente a 65 % / 3,7; mezclar arcos no ayuda.
- Tremorzilla: en las no victorias los guardias están a 32–45 bloques del jefe; quitar los cráteres no ayuda.

## Rondas 79–88 (F40–F48): escudo contra balas, valores por defecto, tope contra jefes, la puerta (2026-11-19; `docs/equipo/COLA.md`)
- **Contra pistoleros, el cuerpo a cuerpo lleva escudo** (a pie y montado). A caballo, 32 Pax: 4 escudos con espada + 4
  arcos montados = 6,9 ± 0,2 vivos de 8 y 100 % (F40, n=30; con F39 n=60 ≈ 7,0), frente a 3,6 con espadas, en 24 s
  frente a 47. Contra 48 Pax: 100 % / 6,5 en 26 s (8 arcos: 100 % / 6,0 en 57 s). Sin arcos (8 escudos) baja a 83 % / 4,3.
  Ojo: el rol `shield` trae también grebas de hierro; no se ha separado cuál pesa.
- A pie en la aldea, espadas sin escudo mueren en 10–11 s (0 % de victorias, F31, n=30); 8 escudos contra 32 Pax 83 % /
  4,7 frente a 65 % / 3,7 (F38, n=30), pero no se confirmó contra 16 (F42: 78 % / 7,3 frente a 90 % / 6,1, n=30).
- **En abierto a pie, todo arcos**: escudos delante de los arcos contra 48 Pax no mejoran (F41, n=30 por brazo, todas
  las diferencias dentro de ±0,5 vivos).
- **Ya por defecto en el juego** (F48, n=30 por brazo + regresión sin mods con 0 filas rojas): fuego repartido
  (`spreadFire`): 48 Pax 90 % / 4,39 vivos frente a 80 % / 3,20 con el anterior (sincronizado); y **retroceso 14–22
  bloques ante enemigos de ≥ 200 de vida** (`kiteBig`): Ignis 100 % / 6,65 vivos frente a 87 % / 3,97 sin él.
- **Contra jefes, el tope de 8 guardias en el cuerpo a cuerpo es el óptimo medido** (Tremorzilla, 16 guardias).
  Curva de victorias por tope 4-6-8-12-16 = 8-28-69-57-32 % (F47 n=34–40 y F43 n=34–37): más dentro = más muertos por
  el daño de área; menos dentro = el jefe no baja.
- **El techo de la aldea era la puerta cerrada** (F44, n=30): con las puertas abiertas la victoria estricta pasa de
  57–73 % a 97–100 % y la pelea dura ~40 s en vez de ~112 s. La regla "abrir brecha" (`door:breach`, aún solo en la
  simulación) lo recupera casi todo: 16 Pax 65 → 97 % (vivos 70 %), con 8 escudos 67 → 90 %, 32 Pax 60 → 80 % (F46,
  n=30). Falta el 2.º lote y cerrar la puerta al acabar antes de que sea valor por defecto.

## Ronda 105 (F67): techo de la línea mixta en bosque (2026-11-20; `tools/torneo-data/t105/`)

Línea 4 escudos + 4 arcos en bosque (n=30, seed 9800): contra 32 Pax 100 % / 6,33 de 8 vivos; contra 48, 100 % / 5,83;
contra 64, 93 % / 4,30. El techo (≥ 70 % de victorias) está en 64 Pax o más. Proporción: 2+6 (-0,43 ± 0,40) y 6+2
(-0,10 ± 0,40) no mejoran a 4+4 contra 32; 6+2 contra 48 es peor (-1,50 ± 0,44). Receta de bosque: 4 + 4.

## Ronda 108 (F71): `standoff:cover` confirmada en la aldea (2026-11-20; `tools/torneo-data/t108/`)
- 16 Pax en la aldea: arqueros vivos +0,60 de 4 (z 2,6; con F68, n = 60: +0,62, z 3,6), vivos totales +1,27 de 8. 32 Pax: sin empeorar.
- Bosque 48 Pax y llano 32 Pax: vivos totales y victorias sin cambio (+0,33 / −0,20), pelea 3–6 s más corta; arqueros vivos −0,6 y escudos +0,3/+1,0 (la baja se reparte hacia el escudo).

## Ronda 109 (F72): índice de unidades a 800 guardias, 2.º lote (2026-11-20; `tools/torneo-data/f72/`)
- Con `simOnePassUnitXp` + `simUnitIndex`: peor tick de Rally sin GC 17,5 → 10,8 ms (−38 %), p99 11,8 → 7,5 (−36 %), media 2,24 → 1,72; la pelea no cambia. Se repite el efecto de F62.
- No basta para valor por defecto según el criterio escrito: el pico baja ≥ 30 % en 20 de 26 parejas (había un suelo de ~10–12 ms en el brazo con índice). El pico lo pone ahora `STANCE_LOOP` con `LADDER` anidado (~14 ms).

## Ronda 115 (F79): cuánto vale la coordinación sola (2026-11-22; `tools/torneo-data/t115/`)

Rally completo contra Rally "cada uno decide solo" (`off:group`: sin capitán, formación, órdenes de unidad, fuerza,
ventanas de fuego ni blanco compartido), misma cadencia y reglas individuales, 8 guardias, n = 30 parejas.
Contra pistoleros (`t88-pax48`) la capa de grupo **vale +1,6 vivos de 8** (5,2 vs 3,6; z +3,8) y sube la victoria de
87 % a 100 %. Contra la horda cuerpo a cuerpo (`t103-pax32-linea`, arena 160) no separa (6,2 vs 6,8; z −1,3, sin
decidir): las ventanas de fuego recortan un 10 % la cadencia y ahorran impactos en i-frames, y ahí se compensan.

## Ronda 117 (F81): qué pieza de la capa de grupo cuenta (2026-11-22; `tools/torneo-data/t117/`)

Una pieza apagada cada vez, n = 30 parejas, 8 guardias. Contra pistoleros lo que más vale son las **ventanas de fuego**
(sin ellas −1,2 vivos de 8, z −1,9; victoria 93 → 80 %): evitan impactos perdidos en i-frames. El **blanco compartido**
tiende a estorbar (sin él +0,8 vivos, z −1,5, 100 % de victorias); la formación (capitán, MUSTER, ATTACK) no mueve nada.
Ninguna pieza pasa el umbral sola; las tres suman ≈ la capa entera. Con F79 (n = 60) la capa vale **+1,1 vivos** contra
pistoleros (z +2,9) y −0,6 contra la horda (z −1,9, sin decidir). Nada cambia en el juego.

## Ronda 119 (F84): ventanas de fuego confirmadas; contra cuerpo a cuerpo la capa no pesa (2026-11-22; `tools/torneo-data/t119/`)

Contra pistoleros, sumando F81 (n = 60 parejas, 8 guardias): las **ventanas de fuego salvan +1,2 vivos de 8** (z +2,8;
victoria 95 vs 85 %, −33 de daño recibido) porque evitan disparos a rojos en i-frames. El **blanco compartido no
estorba** (−0,3, z −0,7): lo de F81 era ruido. Contra 32 vindicadores (bajas por hacha, no por bala) la capa de grupo
entera y cada pieza son neutras: sin ventanas se doblan los impactos perdidos pero no cambia nada. Nada cambia en el juego.

## Ronda 110 (F73b + F82): la cobertura del arquero no sirve en el juego real (2026-11-22; `tools/torneo-data/f73aldea/`, `f82aldea/`)
- Sin `losRelease` (que es solo de sim) el arquero que recula detrás del escudo **no** se salva: n = 60 pareado, dos
  semillas, arqueros −0,55 de 4 (z −5,1), escudos +0,78, vivos +0,23 (z 1,2) y estricta 82 vs 93 %. `standOffCover`
  vuelve a `false` por defecto; la etiqueta `standoff:cover` queda para pruebas.
- La aldea del juego sin la regla: 93 % estricta, 6,90 vivos de 8, los 4 arqueros vivos en las 60 peleas.

## Ronda 118 (F69): retirarse al ver la masa, no al sangrar (2026-11-22; `tools/torneo-data/t118/`)

- 8 en línea contra 80 vindicadores, casa con puerta a 16 bloques. Ordenar la retirada en cuanto se ven ≥ 4 enemigos
  por guardia a ≤ refugio + 22 bloques salva lo mismo que la orden perfecta (5,29 contra 5,38 de 8 cuando el enemigo
  aparece a 45) y 2,5 guardias más que retirarse al primer daño. Contra 16 (pelea ganable) no salta nunca.
- K = 2 (dos por guardia) salta igual de pronto pero salva menos (−0,83): mejor exigir la masa de 4 a 1.
- Si el enemigo aparece ya a 30, el disparador llega tarde como cualquiera: −1 guardia frente al oráculo.
- Solo simulación; que la escuadra se retire sola lo decide David. n = 24–25 por brazo.

## Ronda 124 (F90): la veteranía de hoy resta (2026-11-22; `tools/torneo-data/t124/`)

- Mismo equipo, moral apagada: rango 5 acaba con 1,2 de 8 vivos donde rango 0 deja 4,7 (pistoleros) y 3,9
  (vindicadores); z −6 a −9. Rango 3 también pierde contra pistoleros (−2,6) y es neutro contra vindicadores.
- Los veteranos aciertan más (65 → 79 %) pero disparan un tercio menos y, a rango 5, se retiran por inferioridad
  numérica una y otra vez (la pelea dura 108 s en vez de 41). Pasa a Fable para leer el código. n = 30.

## Ronda 126 (F92): en espejo, la capa de grupo no decide (2026-11-22; `tools/torneo-data/t126/`)

- 8 v 8 arqueros con el mismo equipo, las cuatro celdas (capa sí/no en cada bando), n = 60 pareadas: el efecto de la
  capa es +0,2 ± 1,3 puntos de vida a los 20 s; sin sesgo de bando. Victorias 47–55 % en todas las celdas.
- La pelea se decide muy pronto: a los 10 s se ha perdido el 90 % de la vida total. Donde la coordinación sí vale es
  contra pistoleros (+1,1 vivos, ronda 81/84), no en el espejo.

## Ronda 129 (F95): en calles y pasillos, la espada a pie (2026-11-22; `tools/torneo-data/t129/`)

- 8 espadas rango 1 contra 12 vindicadores, n = 30 pareadas. En pasillos, montarlas cuesta 1,8 vivos de 8 (z −4,2) y
  pasa de ganar la mitad a no ganar nunca; en la aldea sobreviven igual pero no terminan la pelea. Montadas golpean la
  mitad de veces. Consejo: en espacios pequeños, desmontar a la infantería de espada. Falta saber por qué (Fable).

## Ronda 130 (F96): en inferioridad, la capa de grupo tampoco salva a los arqueros (2026-11-22; `tools/torneo-data/t130/`)

- 8 arqueros rango 1 con capa contra 8, 10 y 12 sin capa, n = 30 pareadas por N: efecto −0,14 ± 0,24 vivos (z −0,6).
  Con 2 arqueros de más el azul ya pierde siempre (0 % en 10 y 12, muere entero en 11–18 s) y causa el mismo daño con o
  sin capa. Consejo: en arco, la superioridad numérica manda; la coordinación de hoy no la compensa. Por qué: Fable.

## Ronda 128 (F94): qué regla de la veteranía resta (2026-11-22; `tools/torneo-data/t128/`)

- Contra vindicadores, toda la pérdida del rango 5 es la retirada por inferioridad (`OUTNUMBERED`): apagada, el r5
  acaba con 3,8 de 8 vivos como el r0 (antes 1,5), en 42 s en vez de 112. n = 30 pareadas, z 8,8.
- Contra pistoleros, la banda que obliga a acercarse (×1,6 de la distancia preferida) explica la mitad: apagada, el r5
  dispara lo mismo que el r0 y gana el 83 % (antes 37 %), pero aún acaba con 2,2 vivos menos que el r0. La otra mitad
  sigue sin causa. Cambiar estas reglas en el juego es decisión de David.

## Ronda 127 (F93): la retirada anticipada, confirmada (2026-11-23; `tools/torneo-data/t127-retirada-conf/`)

- Contra 80 vindicadores, la línea de 8 que se retira a la casa en cuanto el enemigo se acerca (`early-4`) salva
  4,5 de 8, casi como la orden perfecta (5,0) y el doble que esperar al primer daño (2,5). Contra 16 no se dispara
  (6,07 vs 6,10). Dos semillas (F69 y esta). Es regla visible de juego: la decide David.

## Ronda 131 (F97): cuántos guardias aguanta el servidor sin mods (2026-11-23; `tools/torneo-data/t131-tope-*/`)

- 300 v 300 y 350 v 350 cumplen (p99 ≤ 43 ms, < 0,5 % de ticks lentos); 400 v 400 ya no (p99 55). Rally es el 6 %
  del tick en todos: el límite es Minecraft + Guard Villagers, no nuestro código. Sin jugador cerca.

## Ronda 132 (F99): derrotar al enemigo por partes paga (2026-11-23; `tools/torneo-data/t132-por-partes/`)

- 8 guardias contra 16 vindicadores a la vez: 4,4 vivos. Contra 8 y luego 8: 6,5 vivos (z 8,9). La 1.ª tanda muere en
  menos de 15 s, así que separarlas 15, 30 o 60 s da lo mismo. En Minecraft el número pesa: separar al enemigo vale.

## Ronda 133 (F101): la retirada por inferioridad de la veteranía nunca salva (2026-11-23; `tools/torneo-data/t133-outnumbered/`)

- Rango 5 contra 16, 32, 48 y 64 vindicadores, con y sin el paso `OUTNUMBERED`: neutro en 16 y resta en todos los
  demás (−2,5 / −2,1 / −1,0 vivos). Sin el paso, 8 guardias aún ganan el 77 % contra 64. Quitarlo lo decide David.

## Ronda 134 (F102): el paso estrecho no multiplica (2026-11-23; `tools/torneo-data/t134-termopilas/`)

- Con escudos y arcos contra vindicadores, defender el pasillo de `corridors` deja menos vivos que el llano (−0,9 con
  16, −3,2 con 32) y los arcos disparan un 35 % menos. En el llano, 8 ganan siempre contra 32.

## Ronda 137 (F105): separar al enemigo tiene que durar unos 8 s (2026-11-23; `tools/torneo-data/t137/`)

- 8 guardias contra 8 + 8 vindicadores: si la 2.ª tanda llega 4 s después, 5,5 vivos (con 16 a la vez, 4,6); a 8 s,
  6,1; a 12 s, 6,7 (igual que a 15 s o más). A los 4 s la 1.ª tanda sigue viva a medias; a los 8 s ya ha caído. Un
  cebo o un terreno que divida al enemigo tiene que retener a la 2.ª tanda al menos 8 s; 12 s da toda la ganancia.

## Ronda 136 (F104): caballos de velocidad desigual explican un tercio de la flojera de los jinetes con espada (2026-11-23; `tools/torneo-data/t136/`)

- 8 jinetes con espada contra el bestiario: con todos los caballos a la misma velocidad, los impactos por guardia-segundo
  suben de 0,42 a 0,46 en llano (a pie, 0,56) y de 0,26 a 0,30 en pasillos (a pie, 0,42). Pelean algo más cerca, pero
  no más juntos, y siguen 1,5-2 bloques más lejos que a pie. Igualar caballos no basta; queda probar el pánico del
  caballo herido y el ancho del caballo.

## Ronda 61 (F21, leída el 2026-11-23): composición contra jefes sin moral (2026-11-16; `tools/torneo-data/t61/`)

- Repite la ronda 32 sin moral (n = 30 pareadas, netherita+). Tremorzilla (16 guardias): 16 escudos ganan el 90 % y le
  dejan 18 de vida (línea 8 + 8: 23 %, 128); 12 escudos + 4 arcos, 67 %; 16 espadas, 3 %. Los arcos son blanco del
  rayo (231 → 118 de daño de rayo con todo escudos). Ignis (8 guardias): 2 escudos + 6 arcos o 8 arcos dejan 4,8 / 4,5
  vivos frente a 3,0 de la línea 4 + 4; 6 escudos + 2 arcos, 1,1 y 67 % de victorias. Con 8 arcos Ignis no se cura.
- Medido antes de `kiteBig` (F48): con la regla actual las cifras de Ignis son más altas; el orden de composiciones es
  el dato (para el planificador).

## `kiteBig` fuera de Ignis (F110, 2026-11-23, n=30 pareadas)

- Netherite Monstrosity (8 arcos netherite+): recular 14-22 bloques deja 7,27 de 8 vivos frente a 6,03 sin la regla
  (z 5,2), mitad de daño recibido; ambos ganan siempre, con la regla tarda ~4 s más.
- Tremorzilla: la regla no se activa (`act:kiteBig` 0). Ender Guardian: las flechas no le hacen daño; con la regla
  quedan 0,9 vivos menos (z −1,9, sin confirmar).
- Ender Guardian, 2.º lote (F114, t143, n = 30 + 30): la pérdida de vivos no se repite (+0,17; combinado n = 60
  −0,37, z −1,0): `kiteBig` no estorba, solo reduce el daño recibido (−68, z −4,4). Con 4 escudos + 4 arcos netherite+
  se gana siempre (7,4 vivos) con o sin la regla. Tremorzilla no es `Enemy` (es un `TamableAnimal`): por eso no recula.

## La altura no paga (F115-F116, t144/t146, n = 30 pareadas)

- Con MANTENER_LINEA en una meseta de +4 (`hillwide`, el 75 % del azul se queda arriba) contra 16 vindicadores:
  −0,33 vivos de 8 frente al llano (z −0,9): el cuerpo a cuerpo no gana nada por estar arriba.
- Contra 48 pillagers se gana 8/8 en ambos, pero desde arriba se recibe más daño (+16, z 5,3), se tarda 11 s más y la
  precisión de los arcos cae de 96 % a 57 %. No mandes arqueros a una colina esperando ventaja.

## Munición escasa desde la altura (F120, t150, n = 30 pareadas)

- Con 3 objetos de flecha por arquero (12 tiros) en la meseta contra 48 pillagers, callar el arco alto sin línea
  (`fire:noHighAbove`) saca +3,8 impactos de las mismas 48 flechas (z 3,4) y deja +0,37 guardias vivos de 8 (z 3,0).
  Los pillagers mueren igual en todos los brazos (los escudos rematan): sin confirmar que gane peleas más duras.
- F121 (t151, 96 y 144 pillagers, 3 objetos por arquero): los escudos siguen rematando (pillagers vivos en 1/30).
  `fire:noHighAbove` vuelve a sacar +4–5 impactos de las mismas flechas (z 5,6 / 3,7) sin más vivos. La munición
  escasa no es el contexto donde el arco alto decide.

## Por partes contra tiradores (F122, t152, n = 30 pareadas)

- 8 (4 escudos + 4 arcos) contra 32 pillagers en dos tandas de 16: ganan 8/8 en todos los brazos, pero separar las
  tandas baja el daño recibido de 49 a 31 (8 s), 26 (15 s) y 25 (30 s), z −9 a −13. Satura en ~15 s: es cuando la
  1.ª tanda ya está muerta. Falta la cifra en vivos (lote con 48).
- Con 48 y 64 pillagers (F125, t155) siguen 8/8 vivos; el daño recibido baja 86 → 54 (48, 15 s) y 114 → 77 (64,
  15 s), z ≈ −8 a −10; con 8 s ya se logra ~84 % de esa ganancia. Cuesta 4-9 s más de pelea. Cifra en vivos: no medible aquí.

## Coste del tick a 400 v 400: la IA de mob es ≈ 60 % (F127, t157, n = 24 pareadas)

- Sin mods, 400 v 400 cuesta 29,2 ms/tick en régimen; saltar `Mob#serverAiStep` 1 de cada 2 / 4 ticks (solo sim)
  lo baja a 20,1 / 16,3 ms y el p99 de 50,6 a 37,0 / 30,5: la IA de mob es ≈ 60 % del tick; Rally ≈ 6 %.
- Frenar la IA cambia la pelea (−30 a −40 % de bajas con 1 de 4). Frenarla solo lejos de un observador detrás de un
  bando no ahorra tick y da la victoria al bando cercano (12/13): un LOD por distancia tiene que ser simétrico entre
  bandos que se pelean. Nada cambia en el juego.

## Cebo a pie que reparte los golpes (F128 + F130, t158/t160, n = 60 pareadas, 2 semillas)

- 8 (4 escudos + 4 arcos a pie) contra 16 vindicadores: dar a 2 de los arqueros su propia unidad con HOSTIGAR desde
  el segundo 0 deja +1,23 guardias vivos de 8 (z ≈ 6,1 en dos lotes), el 59 % de lo que da recibirlos en dos tandas.
- No separa al enemigo (lo arrastrado vuelve en ~2 s): reparte sus blancos. Los rojos pasan menos tiempo encima de los
  escudos (48 → 38 rojo-segundos) y los escudos sobreviven (0,6 → 1,9). Partir en unidades sin la orden no da nada
  (`pie-sin` = base). Montar el cebo paga menos (+0,70). No es valor por defecto.
- Dosis (F131 + F133, t161/t163, n = 60 pareadas, 2 semillas): **un arquero hostigando basta**. 1 cebo da +1,28
  vivos de 8 (z 6,0); 4 cebos dan lo mismo (+0,03 en F133) y 2 cebos también (+0,87 en n = 60; el bajón de F131 era la
  semilla: con dos unidades de 1 sale igual que con una de 2). Con 1 cebo además se gana antes (15 s frente a 16-17 s).
- Alcance (F135, t165, n = 30 pareadas, 3.ª semilla): el cebo de 1 vale contra 16 vindicadores (+1,50 vivos, z 6,6)
  pero **se apaga** contra 24 (−0,07) y resta contra 32 (−0,40, z −2,1): arrastra más rojos, pero sobran para el grueso
  y la pelea se alarga (33 → 89 s). Úsalo a 2:1, no a 3-4:1.
- Tamaño contra 32 (F150, t178, n = 30 pareadas, 4.ª semilla): **más cebos no lo arreglan**. 1, 2 y 4 arqueros
  hostigando restan −0,40 / −0,77 / −0,20 vivos de 8 (z −3,0 / −3,1 / −2,0). Con 4 el cebo arrastra a casi todo el
  enemigo (31,7 de 32), pero los escudos mueren igual y la pelea dura 90 s en vez de 33. A 4:1 no hay reparto que valga.

## Concentrar el fuego de arco no paga, ni con cupo (F126, t156, n = 30 pareadas)

- Contra 48 pillagers con pistola, el foco de rango 3 cuesta 2 guardias de 8 (réplica de F106). Con un cupo de
  3 tiradores por blanco se pierde la mitad menos de daño en invulnerabilidad (550 → 277). Aun así deja 1 vivo menos
  que no concentrar (z −2,6), con menos disparos.
- A rango 1 (8 contra 8), sembrar el foco en el blanco más herido hace perder: −27 puntos de vida sin cupo y −16 con
  cupo 3. Dispara la mitad.
- Lo que se sabe: con arco, cada uno a su blanco. No es valor por defecto; lo que cuesta el cambio de blanco queda
  para Fable.

## Retirarse por fuerza vista, no por cabezas (F129, t159, n = 30 pareadas, 1 semilla)

- 8 guardias de línea: la orden de retirada salta cuando la fuerza roja vista (Σ vida × daño de ataque) dividida por los
  guardias vivos al primer avistamiento llega a 1500. Contra 48 vindicadores salta al segundo y deja 4,4 vivos de 8 en vez
  de 2,1; contra 80 deja 4,6.
- No salta en las peleas que se ganan: 32 vindicadores (80 % de victorias) y 64 pillagers (100 %, 7,5 vivos). Contar
  cabezas (5 por guardia) sí huye de los pillagers y pierde esa pelea (7 % de victorias).
- Fuerza medida por guardia: 32 vindicadores ≈ 1050, 48 ≈ 1640, 80 ≈ 2680 (72 vistos), 64 pillagers ≈ 880.
  No es valor por defecto: falta la decisión de David.
- 2.ª semilla (F136, t166, seed 14600, n = 30): lo mismo. 0 % de saltos contra 32 vindicadores y 64 pillagers; contra 48
  salta 30/30 y deja 4,6 vivos en vez de 2,7 (z 5,1); contra 80, 5,1. Umbral 1340 no mejora: se queda 1500.
- El precio: contra 48 vindicadores, esperar gana 42 % (25/60 en las dos semillas) y retirarse nunca gana; se cambia
  esa victoria posible por ~2 guardias más vivos.

## A 4:1 la línea de escudos cae en los primeros 5 segundos (F151, t179, n = 30 pareadas)

- 4 escudos + 4 arcos contra 16 o 32 vindicadores: los escudos mueren a los ~5-6 s del contacto, tanto contra 16 como
  contra 32 (a 32 eso es el 15 % de la pelea). Todos a menos de 8 bloques del que los mata. Ningún arquero muere.
- Los 4 arcos ganan solos los ~27 s restantes. La línea no "aguanta el desgaste": se hunde pronto. Hay margen para
  una regla que la salve (relevo o cerrar sobre los arqueros); está por diseñar.

## Contra 32 Pax en la aldea, el reparto intercambia vivos por victoria (F152, t180, n = 30 pareadas, 1 semilla)

- Más escudos (6+2) ganan más (60 % frente a 40 %) y antes, pero sobreviven menos (3,97 de 8). Más arcos (2+6) dejan
  6,13 vivos y casi nunca ganan (7 %): los Pax se quedan dentro de las casas hasta el final.
- Casi todas las bajas son de cuerpo a cuerpo a menos de 8 bloques; los arqueros apenas mueren.
- El scutum (4+4) es el único reparto que mejora las dos cosas (60 %, 5,17 vivos), sin confirmar todavía.
- Consejo provisional: contra muchos Pax en casas, no basta con cambiar quién va; hace falta una forma de entrar.

## Contra 32 Pax en la aldea, 4 scutum + 4 arcos (F156 + F152, t183/t180, n = 60 pareadas, 2 semillas)

- Con el mismo reparto (4 cuerpo a cuerpo + 4 arcos), cambiar el escudo por el scutum gana más y pierde menos:
  victoria estricta 67 % frente a 38 % (+28 puntos, z 3,3), 5,35 vivos de 8 frente a 4,75 (+0,60, z 2,7), 26 s antes.
- La mejora está en los de cuerpo a cuerpo (sobreviven 1,37 de 4 frente a 0,78); los arqueros quedan igual.
- Consejo: contra muchos Pax dentro de casas, lleva scutum en la mitad que entra.

## A 400 contra 320 la pelea se para sola hacia el minuto (F153, t181, n = 20 pareadas)

- Quitando el corte por falta de daño y dejando 240 s, la pelea solo añade ~6 bajas de 720 en 130 s más; ningún bando
  llega a 0. Los bandos quedan a ~44 bloques de media: el problema es volver a buscar blanco, no la regla del corte.

## Guardias contra guardias: la retirada al 30 % es parte de la derrota (F159, t186, n = 30 pareadas, 1 semilla)

- 8 con Rally contra 8 GV nativos en cuerpo a cuerpo: sin la retirada individual por vida baja, el azul quita +20 puntos
  de vida roja (66 → 86 %) y golpea un 27 % más por guardia-segundo, pero gana solo el 23 % (antes 3 %). Contra 12
  sigue perdiendo siempre. Dato de causa, sin cambio en el juego: retirarse sin comida ni refugio cuesta la pelea.

## Guardias contra guardias: sin la retirada, la capa de grupo no cambia el duelo (F162, t189, n = 30 pareadas, 1 semilla)

- 8 Rally rango 1 contra 8 GV nativos, espada de hierro, `off:lowRetreat` en todos: el control gana 27 % y quita el
  87,6 % de la vida roja. Sin blanco compartido: 87,4 % (z −0,07). Sin toda la capa de grupo: 85,7 % (z −0,56).
- Lectura: en una refriega cuerpo a cuerpo pareja, el foco y la formación de Rally no suman ni restan; la derrota
  está en el primer contacto, no en la coordinación. Dato, nada por defecto.

## A escala, no pisar el camino que ya va al blanco abarata mucho (F158, t185, 400 v 400, 1 semilla; candidato)

- Con `perf:closerYield`, el peldaño `REPOSITION_CLOSER` cede en ~95 % de sus llamadas y su coste cae −97 %; Rally p99
  en arcos 5,34 → 3,08 ms. Vivos −9,5 de 200 (z −1,44, no significativo), pero los arcos ganan menos a tiempo (25 → 6 %).
  Pendiente de 2.º lote antes de cualquier valor por defecto.

## Emboscada contra una carga cuerpo a cuerpo: no paga ni con el punto de espera bien puesto (F168, t195, n = 30 pareadas, 1 semilla)

- 12 guardias (6 + 6) contra 24 vindicadores. Con U2 a 20 bloques en el flanco, escondida (descubierta 0/36) y saliendo
  al ver U1 al enemigo: +0,32 vivos frente a mantener la línea (z 0,40). Esperar con disparo "contacto 4 s" es peor:
  los rojos encuentran a U2 sola en el 78 % y se pierden −2,97 vivos (z −3,7).
- Lectura: Minecraft no tiene daño de flanco; dividir 12 en dos frente a una carga no gana nada. Si se embosca, que
  sea saliendo pronto, no esperando el choque. Dato, nada por defecto.

## Ceder el camino en `REPOSITION_CLOSER`: confirmado con la pelea viva (F166, t194, n = 30 pareadas, 2.º lote)

- 400 arcos por bando con `reorder:30` (sin ejércitos congelados): el peldaño cuesta −96 %, Rally p99 4,69 → 2,83 ms,
  vivos 53,7 → 63,1 de 200 (z +0,82), victoria 43 → 50 %. La caída de victorias del 1.er lote (F158) no se repite.
- Regresión en verde (F176, commit ae01ff78: 0 filas rojas, 0 sin datos): **valor por defecto** desde 2026-11-25;
  la etiqueta de simulación `off:closerYield` lo apaga.

## AVANZAR a 100 guardias: avanza igual o más que a 8 (F178, t204, n = 20 pareadas, 1 semilla; solo telemetría)

- Orden AVANZAR, 8 o 100 guardias (mitad escudo, mitad arco) contra 8 vindicadores a 100 bloques: a los 10 s la
  cumplen todos; avance medio 5,3 bloques con 8 y 7,4 con 100; rezagados (menos de media mediana) 7 % y 16 %; nadie
  retrocede de más (3 % los dos). A 100 la línea se estira: los arcos van delante (8,8) y la infantería detrás (6,0).
- Sin dato a 20 s: el rojo carga en vez de esperar (`redwait` no actúa aquí) y la pelea acaba antes de los 20 s.
- A los 20 s, con el rojo quieto de verdad (F179, t205, n = 20 pareadas, misma semilla): la línea de 100 **aguanta**.
  Avance 13,6 bloques con 100 y 11,2 con 8; rezagados 3 % y 6 %: los rezagados de infantería de los 10 s (16 %) se
  recuperan. Los arcos siguen delante (15,1 frente a 12,2): la línea es más honda, no se rompe.
- A los 90 s, sin arcos y con el rojo quieto (F184, t210, n = 20 pareadas, misma semilla): la orden **no caduca**. Sigue
  viva en 20/20 con 8 y con 100 guardias, ninguna terminación en toda la pelea, y la línea avanza al mismo paso de 60 a
  90 s que de 30 a 60 (~0,8 bloques/s).

## Reparto de fuego cuerpo a cuerpo: la mitad del daño se pierde (F186, t212, n = 30 pareadas, 1 semilla; solo medida)

- 8 espadas contra 8 guardias rivales: por cada 100 de daño que entra, se tiran otros ~51 en vano: ~12 de exceso en el
  golpe que mata y ~40 en golpes que caen mientras el blanco aún es invulnerable (unos 10 golpes por pelea).
- La capa de grupo no junta más guardias en el mismo blanco (el más apuntado se lleva ~55 % con o sin capa): por eso
  el blanco compartido no cambió el duelo en F162. Con arcos el desperdicio es ~11 %, casi todo exceso (sin repetir aún).

- F187 (n = 30 pareadas): esos golpes perdidos **siempre** los da otro guardia que se amontona en el mismo blanco,
  nunca el mismo que golpeó antes (su espada no recarga tan rápido). Lo que falta es repartir los golpes en el grupo,
  no frenar a cada guardia. Con arcos (y bestiario, como F96) solo se pierde ~9 %, casi todo en el golpe que mata.

## Golpes escalonados: no golpear a un blanco invulnerable gana el duelo (F193, t217, n = 30 pareadas, 1 semilla; solo sim)

- Si el guardia retiene la espada mientras el blanco está en su ventana de invulnerabilidad (sin cambiar de blanco ni
  moverse), los golpes perdidos pasan de ~10 a 0 y 8 contra 8 guardias sin Rally pasa de 27 % (Rally solo) / 43 % (capa)
  a **70 %** de victorias; vida roja quitada 84 → 96 %. Con superioridad (8 contra 4) gana 2 s antes con más vida.
- 2.º lote con otra semilla (F196, seed 18800): **confirmado**, 13 % → 80 %; unido (n = 60) 75 % de victorias y vida
  roja quitada +11,3 pp sobre la capa (z 6,6). Falta la decisión de David (cambia el ritmo de golpes que se ve). Apagado
  fuera de sim.
- A escala (F197, t220, n = 30 pareadas): con 16 y 32 por bando la capa sola gana el 20-30 % y con la pieza el **100 %**,
  con 12-19 guardias más vivos y 1,5-4,7 s antes; los golpes en ventana por guardia crecen con el tamaño (1,3 → 1,7), y
  el coste del tick no cambia (p99 del servidor −15 % / +0,5 %).
- Contra Rally (F198, F202): en duelo igualado 8 contra 8 no aporta (cada uno pega a uno, no hay golpes perdidos); con
  superioridad (12 y 16 contra 8) sí: los golpes en ventana pasan de 15-23 a 0, el azul acaba con +8,5 / +4,7 pp de
  vida, 1,35 / 0,8 s antes y menos bajas (1,3 → 0,2 muertos a 12). La pieza paga siempre que haya amontonamiento.

## Rendimiento: Alex's Caves y los objetos del suelo (F201, 2026-11-28)

- A 100 contra 100, Alex's Caves sube el tick del servidor de 3,6 a 47 ms (+43,8 ms pareado). El 95 % de ese exceso es
  `MagnetUtil.tickMagnetism` de AC llamado en cada tick de cada **objeto tirado en el suelo** (`ItemEntity`), no los
  guardias ni Rally (Rally ≈ 0,3–0,5 ms con o sin AC). Si caen menos objetos, debería bajar: lo mide F204.
