# Simulación Rally - lote 20261122-070015-t115-ablacion

Preset `t115-ablacion` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t115-horda-rally8 | 30 | 100 % ± 0 | 0 % ± 0 | 6.2 ± 0.4 | 0.0 ± 0.0 | 26 ± 2 | 330 ± 8 | 115 ± 10 | 50 % ± 2 | 15.4 ± 0.4 | 6.6 ± 0.5 | 0 |
| t115-horda-indiv8 | 30 | 100 % ± 0 | 0 % ± 0 | 6.8 ± 0.3 | 0.0 ± 0.0 | 24 ± 1 | 318 ± 7 | 101 ± 7 | 50 % ± 2 | 14.6 ± 0.6 | 5.6 ± 0.4 | 0 |
| t115-pistoleros-rally8 | 30 | 100 % ± 0 | 0 % ± 0 | 5.2 ± 0.2 | 0.0 ± 0.0 | 47 ± 2 | 1225 ± 10 | 108 ± 8 | 69 % ± 1 | 45.4 ± 0.6 | 21.8 ± 1.6 | 0 |
| t115-pistoleros-indiv8 | 30 | 87 % ± 6 | 13 % ± 6 | 3.6 ± 0.4 | 2.1 ± 1.1 | 49 ± 1 | 1158 ± 18 | 157 ± 10 | 71 % ± 1 | 43.6 ± 0.5 | 20.3 ± 1.9 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t115-horda-rally8 semilla 10600: sprint → blue (8/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 116 de 116 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t115-horda-rally8: enemy:scguns.bullet.annihilated=7 enemy:scguns.bullet.decimated=14 enemy:scguns.bullet.eliminated=17 enemy:scguns.bullet.executed=6 enemy:scguns.bullet.killed=10
- t115-horda-indiv8: enemy:scguns.bullet.annihilated=6 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=10 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=7
- t115-pistoleros-rally8: enemy:scguns.bullet.annihilated=11 enemy:scguns.bullet.decimated=19 enemy:scguns.bullet.eliminated=20 enemy:scguns.bullet.executed=17 enemy:scguns.bullet.killed=17
- t115-pistoleros-indiv8: enemy:scguns.bullet.annihilated=32 enemy:scguns.bullet.decimated=35 enemy:scguns.bullet.eliminated=24 enemy:scguns.bullet.executed=19 enemy:scguns.bullet.killed=23

## Motivos de decisión (cambios registrados): ENGAGE 3158, LOW_HEALTH_RETREAT 245, NONE 171, OUT_OF_RANGE 2481, SEEK_ENEMY 55, TOO_CLOSE 22

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
