# Manual de campo v0 (torneo de doctrinas, 2026-11-04)

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
| esqueleto | arqueros | Tirar desde donde están | 100 % / 93–94 % (+48 / +54–55) | pendiente | final ganada |
| esqueleto | línea | Tirar desde donde están | 97–100 % / 96 % (+45–48 / +60) | pendiente | final ganada |
| esqueleto | caballería | Tiro a quemarropa | 87 % / 75 % (+47 / +50) | pendiente | final ganada |
| creeper | arqueros / línea / escudos | Atacarlos | supervivientes +40 / +32 / +19 | +36 / +42 / +25 | **CONFIRMADA** |
| creeper | espadas sin escudo | No atacarlos | −11 atacando | −12 atacando | probable (mismo signo en ambos lotes) |
| creeper | caballería | — | +19 atacando | +4 | no confirmada |

Los creepers se midieron como supervivientes azules con 12, 24 y 40 creepers juntos (n = 30 por modo y lote).

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
3. **Escudo arriba como doctrina**: hoy es una opción global de Guard Villagers; como doctrina por unidad (contra
   esqueletos sí, contra hordas de zombis no) hace falta una pieza propia de Rally.
