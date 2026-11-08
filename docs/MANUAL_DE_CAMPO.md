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
