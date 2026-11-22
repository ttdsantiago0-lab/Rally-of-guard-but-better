# Simulación Rally - lote 20261122-072939-t117-piezas

Preset `t117-piezas` · velocidad `sprint` · 301 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t117-horda-rally8 | 30 | 100 % ± 0 | 0 % ± 0 | 6.1 ± 0.3 | 0.0 ± 0.0 | 31 ± 1 | 289 ± 8 | 81 ± 5 | 30 % ± 1 | 31.1 ± 0.5 | 19.7 ± 0.5 | 0 |
| t117-horda-indiv8 | 30 | 100 % ± 0 | 0 % ± 0 | 6.6 ± 0.2 | 0.0 ± 0.0 | 29 ± 1 | 273 ± 8 | 74 ± 5 | 29 % ± 2 | 30.1 ± 0.3 | 20.9 ± 0.4 | 0 |
| t117-horda-sinfuego | 30 | 100 % ± 0 | 0 % ± 0 | 6.4 ± 0.3 | 0.0 ± 0.0 | 31 ± 1 | 277 ± 7 | 77 ± 6 | 29 % ± 2 | 30.0 ± 0.5 | 19.9 ± 0.4 | 0 |
| t117-horda-sinblanco | 30 | 100 % ± 0 | 0 % ± 0 | 6.3 ± 0.3 | 0.0 ± 0.0 | 29 ± 1 | 276 ± 8 | 77 ± 5 | 30 % ± 1 | 30.5 ± 0.4 | 20.1 ± 0.4 | 0 |
| t117-horda-sinforma | 30 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.3 | 0.0 ± 0.0 | 30 ± 1 | 283 ± 6 | 76 ± 6 | 31 % ± 1 | 30.4 ± 0.5 | 19.7 ± 0.4 | 0 |
| t117-pistoleros-rally8 | 30 | 93 % ± 5 | 7 % ± 5 | 4.4 ± 0.4 | 0.5 ± 0.4 | 49 ± 1 | 1222 ± 15 | 133 ± 11 | 70 % ± 1 | 44.3 ± 0.7 | 20.5 ± 2.0 | 0 |
| t117-pistoleros-indiv8 | 30 | 90 % ± 6 | 10 % ± 6 | 3.8 ± 0.4 | 1.6 ± 0.9 | 49 ± 2 | 1173 ± 22 | 145 ± 11 | 73 % ± 1 | 43.4 ± 0.6 | 18.2 ± 2.0 | 0 |
| t117-pistoleros-sinfuego | 30 | 80 % ± 7 | 20 % ± 7 | 3.1 ± 0.4 | 2.3 ± 1.2 | 51 ± 2 | 1133 ± 30 | 162 ± 11 | 72 % ± 1 | 43.7 ± 0.6 | 21.7 ± 2.0 | 0 |
| t117-pistoleros-sinblanco | 30 | 100 % ± 0 | 0 % ± 0 | 5.1 ± 0.3 | 0.0 ± 0.0 | 48 ± 2 | 1240 ± 9 | 112 ± 9 | 70 % ± 1 | 44.6 ± 0.6 | 20.4 ± 1.7 | 0 |
| t117-pistoleros-sinforma | 30 | 93 % ± 5 | 7 % ± 5 | 4.4 ± 0.3 | 0.3 ± 0.3 | 47 ± 1 | 1232 ± 15 | 130 ± 10 | 71 % ± 1 | 44.5 ± 0.6 | 20.8 ± 1.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t117-horda-rally8 semilla 10800: sprint → blue (5/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 287 de 287 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t117-horda-rally8: enemy:scguns.bullet.annihilated=13 enemy:scguns.bullet.decimated=13 enemy:scguns.bullet.eliminated=6 enemy:scguns.bullet.executed=12 enemy:scguns.bullet.killed=13
- t117-horda-indiv8: enemy:scguns.bullet.annihilated=7 enemy:scguns.bullet.decimated=8 enemy:scguns.bullet.eliminated=13 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=6
- t117-horda-sinfuego: enemy:scguns.bullet.annihilated=16 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=11 enemy:scguns.bullet.executed=10 enemy:scguns.bullet.killed=7
- t117-horda-sinblanco: enemy:scguns.bullet.annihilated=13 enemy:scguns.bullet.decimated=8 enemy:scguns.bullet.eliminated=11 enemy:scguns.bullet.executed=10 enemy:scguns.bullet.killed=9
- t117-horda-sinforma: enemy:scguns.bullet.annihilated=11 enemy:scguns.bullet.decimated=8 enemy:scguns.bullet.eliminated=11 enemy:scguns.bullet.executed=11 enemy:scguns.bullet.killed=5
- t117-pistoleros-rally8: enemy:scguns.bullet.annihilated=25 enemy:scguns.bullet.decimated=22 enemy:scguns.bullet.eliminated=15 enemy:scguns.bullet.executed=31 enemy:scguns.bullet.killed=16
- t117-pistoleros-indiv8: enemy:scguns.bullet.annihilated=22 enemy:scguns.bullet.decimated=25 enemy:scguns.bullet.eliminated=24 enemy:scguns.bullet.executed=22 enemy:scguns.bullet.killed=32
- t117-pistoleros-sinfuego: enemy:scguns.bullet.annihilated=33 enemy:scguns.bullet.decimated=32 enemy:scguns.bullet.eliminated=24 enemy:scguns.bullet.executed=22 enemy:scguns.bullet.killed=35
- t117-pistoleros-sinblanco: enemy:scguns.bullet.annihilated=14 enemy:scguns.bullet.decimated=12 enemy:scguns.bullet.eliminated=18 enemy:scguns.bullet.executed=22 enemy:scguns.bullet.killed=20
- t117-pistoleros-sinforma: enemy:scguns.bullet.annihilated=26 enemy:scguns.bullet.decimated=18 enemy:scguns.bullet.eliminated=24 enemy:scguns.bullet.executed=17 enemy:scguns.bullet.killed=22

## Motivos de decisión (cambios registrados): ENGAGE 8756, FORMATION_HOLD 1, LOW_HEALTH_RETREAT 568, NONE 582, OUT_OF_RANGE 7761, SEEK_ENEMY 147, STUCK 3

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
