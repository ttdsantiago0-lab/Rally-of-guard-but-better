# Simulación Rally - lote 20261119-045928-t88-defecto

Preset `t88-defecto` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t88-pax48 | 30 | 90 % ± 6 | 10 % ± 6 | 4.3 ± 0.4 | 0.9 ± 0.6 | 48 ± 1 | 1210 ± 14 | 136 ± 11 | 70 % ± 1 | 44.4 ± 0.4 | 25.8 ± 1.2 | 0 |
| t88-pax48-sync | 30 | 80 % ± 7 | 20 % ± 7 | 3.2 ± 0.4 | 2.2 ± 0.9 | 53 ± 2 | 1176 ± 25 | 169 ± 10 | 77 % ± 1 | 43.8 ± 0.4 | 22.9 ± 2.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t88-pax48 semilla 8600: sprint → blue (5/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 51 de 51 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t88-pax48: enemy:scguns.bullet.annihilated=19 enemy:scguns.bullet.decimated=19 enemy:scguns.bullet.eliminated=28 enemy:scguns.bullet.executed=15 enemy:scguns.bullet.killed=30
- t88-pax48-sync: enemy:scguns.bullet.annihilated=35 enemy:scguns.bullet.decimated=27 enemy:scguns.bullet.eliminated=19 enemy:scguns.bullet.executed=36 enemy:scguns.bullet.killed=27

## Motivos de decisión (cambios registrados): ENGAGE 2628, LOW_HEALTH_RETREAT 130, NONE 136, OUT_OF_RANGE 2400, SEEK_ENEMY 7

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
