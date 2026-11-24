# Simulación Rally - lote 20261124-054242-t180-aldea-pax32

Preset `t180-aldea-pax32` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t180-aldea32-e4a4 | 30 | 70 % ± 5 | 30 % ± 5 | 4.7 ± 0.2 | 3.9 ± 0.7 | 151 ± 8 | 405 ± 15 | 142 ± 5 | 10 % ± 1 | 37.4 ± 0.9 | 19.7 ± 1.1 | 0 |
| t180-aldea32-e6a2 | 30 | 80 % ± 5 | 20 % ± 5 | 4.0 ± 0.4 | 2.6 ± 0.7 | 126 ± 9 | 415 ± 15 | 192 ± 7 | 14 % ± 1 | 26.1 ± 1.2 | 19.1 ± 1.2 | 0 |
| t180-aldea32-e2a6 | 30 | 53 % ± 2 | 47 % ± 2 | 6.1 ± 0.1 | 8.9 ± 0.7 | 175 ± 4 | 335 ± 13 | 71 ± 4 | 8 % ± 1 | 48.4 ± 0.7 | 16.9 ± 1.5 | 0 |
| t180-aldea32-s4a4 | 30 | 80 % ± 5 | 20 % ± 5 | 5.2 ± 0.2 | 2.4 ± 0.7 | 129 ± 8 | 452 ± 13 | 141 ± 5 | 14 % ± 1 | 37.0 ± 1.1 | 21.8 ± 0.9 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t180-aldea32-e4a4 semilla 16400: sprint → draw (4/3 vivos), normal → draw (5/4 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t180-aldea32-e4a4: enemy:scguns.bullet.annihilated=30 enemy:scguns.bullet.decimated=17 enemy:scguns.bullet.eliminated=14 enemy:scguns.bullet.executed=21 enemy:scguns.bullet.killed=17
- t180-aldea32-e6a2: enemy:scguns.bullet.annihilated=23 enemy:scguns.bullet.decimated=20 enemy:scguns.bullet.eliminated=38 enemy:scguns.bullet.executed=21 enemy:scguns.bullet.killed=19
- t180-aldea32-e2a6: enemy:scguns.bullet.annihilated=13 enemy:scguns.bullet.decimated=12 enemy:scguns.bullet.eliminated=13 enemy:scguns.bullet.executed=14 enemy:scguns.bullet.killed=4
- t180-aldea32-s4a4: enemy:scguns.bullet.annihilated=16 enemy:scguns.bullet.decimated=21 enemy:scguns.bullet.eliminated=14 enemy:scguns.bullet.executed=19 enemy:scguns.bullet.killed=15

## Motivos de decisión (cambios registrados): ENGAGE 2823, LOW_HEALTH_RETREAT 482, NONE 283, OUT_OF_RANGE 2037, SEEK_ENEMY 348, STUCK 289

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t180-aldea32-e4a4: 18/30 corridas llegaron al tope de tiempo
- t180-aldea32-e2a6: 28/30 corridas llegaron al tope de tiempo
