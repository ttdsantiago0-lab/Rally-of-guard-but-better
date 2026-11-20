# Simulación Rally - lote 20261120-050942-t106-aldea-cobertura

Preset `t106-aldea-cobertura` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t106-pax16-defecto | 30 | 100 % ± 0 | 0 % ± 0 | 5.7 ± 0.2 | 0.0 ± 0.0 | 50 ± 1 | 355 ± 5 | 141 ± 6 | 58 % ± 1 | 17.5 ± 0.4 | 14.0 ± 0.3 | 0 |
| t106-pax16-cobertura | 30 | 100 % ± 0 | 0 % ± 0 | 6.9 ± 0.2 | 0.0 ± 0.0 | 45 ± 1 | 381 ± 4 | 114 ± 7 | 73 % ± 1 | 10.5 ± 0.3 | 5.3 ± 0.3 | 0 |
| t106-pax32-defecto | 30 | 53 % ± 9 | 47 % ± 9 | 2.1 ± 0.4 | 3.1 ± 0.8 | 75 ± 3 | 457 ± 14 | 244 ± 8 | 60 % ± 1 | 16.6 ± 0.7 | 12.0 ± 0.8 | 0 |
| t106-pax32-cobertura | 30 | 70 % ± 9 | 30 % ± 9 | 2.7 ± 0.5 | 2.6 ± 0.9 | 74 ± 5 | 508 ± 21 | 250 ± 10 | 72 % ± 1 | 10.2 ± 0.4 | 5.3 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t106-pax16-defecto semilla 9900: sprint → blue (7/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 97 de 97 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t106-pax16-defecto: enemy:scguns.bullet.annihilated=14 enemy:scguns.bullet.decimated=23 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=15 enemy:scguns.bullet.killed=10
- t106-pax16-cobertura: enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.decimated=8 enemy:scguns.bullet.eliminated=9 enemy:scguns.bullet.executed=9 enemy:scguns.bullet.killed=3
- t106-pax32-defecto: enemy:scguns.bullet.annihilated=35 enemy:scguns.bullet.decimated=30 enemy:scguns.bullet.eliminated=36 enemy:scguns.bullet.executed=36 enemy:scguns.bullet.killed=39
- t106-pax32-cobertura: enemy:scguns.bullet.annihilated=29 enemy:scguns.bullet.decimated=37 enemy:scguns.bullet.eliminated=36 enemy:scguns.bullet.executed=24 enemy:scguns.bullet.killed=32

## Motivos de decisión (cambios registrados): ENGAGE 2819, LOW_HEALTH_RETREAT 532, NONE 42, OUT_OF_RANGE 542, SEEK_ENEMY 408, STUCK 143, TOO_CLOSE 1087

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
