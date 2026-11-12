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
