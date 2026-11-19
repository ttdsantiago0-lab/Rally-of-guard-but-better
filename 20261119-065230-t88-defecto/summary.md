# Simulación Rally - lote 20261119-065230-t88-defecto

Preset `t88-defecto` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t88-pax48 | 30 | 90 % ± 6 | 10 % ± 6 | 4.3 ± 0.4 | 1.8 ± 1.1 | 48 ± 1 | 1199 ± 21 | 130 ± 11 | 71 % ± 1 | 44.8 ± 0.6 | 18.8 ± 1.9 | 0 |
| t88-pax48-sync | 30 | 80 % ± 7 | 20 % ± 7 | 3.3 ± 0.4 | 1.6 ± 0.7 | 55 ± 2 | 1158 ± 22 | 168 ± 9 | 75 % ± 1 | 44.1 ± 0.6 | 21.7 ± 1.7 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t88-pax48 semilla 8900: sprint → blue (4/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 51 de 51 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t88-pax48: enemy:scguns.bullet.annihilated=23 enemy:scguns.bullet.decimated=14 enemy:scguns.bullet.eliminated=26 enemy:scguns.bullet.executed=22 enemy:scguns.bullet.killed=27
- t88-pax48-sync: enemy:scguns.bullet.annihilated=20 enemy:scguns.bullet.decimated=29 enemy:scguns.bullet.eliminated=23 enemy:scguns.bullet.executed=35 enemy:scguns.bullet.killed=33

## Motivos de decisión (cambios registrados): ENGAGE 2561, LOW_HEALTH_RETREAT 134, NONE 140, OUT_OF_RANGE 2342, SEEK_ENEMY 4

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
