# Simulación Rally - lote 20261119-074306-t95-fuego-horda

Preset `t95-fuego-horda` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t95-horda-defecto | 30 | 100 % ± 0 | 0 % ± 0 | 6.1 ± 0.3 | 0.0 ± 0.0 | 51 ± 1 | 2139 ± 9 | 78 ± 8 | 91 % ± 1 | 15.5 ± 0.6 | 21.2 ± 1.0 | 0 |
| t95-horda-sync | 30 | 100 % ± 0 | 0 % ± 0 | 6.4 ± 0.3 | 0.0 ± 0.0 | 53 ± 1 | 2141 ± 7 | 71 ± 7 | 92 % ± 1 | 19.9 ± 1.3 | 23.0 ± 1.6 | 0 |
| t95-vida-defecto | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 20 ± 0 | 826 ± 1 | 0 ± 0 | 93 % ± 0 | 10.1 ± 0.3 | 11.3 ± 0.5 | 0 |
| t95-vida-sync | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 21 ± 0 | 828 ± 2 | 0 ± 0 | 95 % ± 0 | 10.9 ± 0.4 | 11.9 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t95-horda-defecto semilla 9300: sprint → blue (5/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t95-horda-defecto: enemy:mob=41 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=7 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=1
- t95-horda-sync: enemy:mob=31 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=4

## Motivos de decisión (cambios registrados): ENGAGE 1423, LOW_HEALTH_RETREAT 57, NONE 64, OUT_OF_RANGE 532, SEEK_ENEMY 42, TOO_CLOSE 217

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
