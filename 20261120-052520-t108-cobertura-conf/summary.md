# Simulación Rally - lote 20261120-052520-t108-cobertura-conf

Preset `t108-cobertura-conf` · velocidad `sprint` · 241 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t108-aldea-pax16-defecto | 30 | 100 % ± 0 | 0 % ± 0 | 5.6 ± 0.2 | 0.0 ± 0.0 | 47 ± 1 | 350 ± 5 | 136 ± 6 | 57 % ± 1 | 18.0 ± 0.4 | 14.1 ± 0.3 | 0 |
| t108-aldea-pax16-cobertura | 30 | 100 % ± 0 | 0 % ± 0 | 6.9 ± 0.2 | 0.0 ± 0.0 | 44 ± 1 | 380 ± 5 | 110 ± 6 | 73 % ± 1 | 10.4 ± 0.2 | 5.3 ± 0.2 | 0 |
| t108-aldea-pax32-defecto | 30 | 63 % ± 9 | 37 % ± 9 | 2.6 ± 0.4 | 3.5 ± 1.0 | 85 ± 6 | 461 ± 20 | 234 ± 10 | 60 % ± 1 | 15.9 ± 0.8 | 10.7 ± 0.7 | 0 |
| t108-aldea-pax32-cobertura | 30 | 75 % ± 7 | 25 % ± 7 | 3.2 ± 0.5 | 2.9 ± 1.0 | 83 ± 7 | 516 ± 24 | 241 ± 11 | 73 % ± 1 | 9.4 ± 0.5 | 4.3 ± 0.4 | 0 |
| t108-bosque-pax48-defecto | 30 | 93 % ± 5 | 7 % ± 5 | 5.5 ± 0.4 | 0.3 ± 0.2 | 34 ± 2 | 317 ± 9 | 94 ± 8 | 33 % ± 2 | 29.7 ± 0.6 | 18.3 ± 0.8 | 0 |
| t108-bosque-pax48-cobertura | 30 | 93 % ± 5 | 7 % ± 5 | 5.8 ± 0.4 | 0.8 ± 0.6 | 27 ± 1 | 352 ± 13 | 131 ± 9 | 50 % ± 2 | 14.8 ± 0.7 | 6.7 ± 0.7 | 0 |
| t108-llano-pax32-defecto | 30 | 97 % ± 3 | 3 % ± 3 | 6.1 ± 0.4 | 0.1 ± 0.1 | 26 ± 1 | 383 ± 11 | 69 ± 10 | 62 % ± 1 | 32.2 ± 0.6 | 15.6 ± 0.6 | 0 |
| t108-llano-pax32-cobertura | 30 | 100 % ± 0 | 0 % ± 0 | 5.9 ± 0.3 | 0.0 ± 0.0 | 22 ± 1 | 408 ± 6 | 118 ± 9 | 85 % ± 1 | 22.5 ± 0.6 | 6.3 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t108-aldea-pax16-defecto semilla 10100: sprint → blue (7/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 219 de 219 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t108-aldea-pax16-defecto: enemy:scguns.bullet.annihilated=15 enemy:scguns.bullet.decimated=13 enemy:scguns.bullet.eliminated=13 enemy:scguns.bullet.executed=17 enemy:scguns.bullet.killed=13
- t108-aldea-pax16-cobertura: enemy:scguns.bullet.annihilated=11 enemy:scguns.bullet.decimated=6 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=7 enemy:scguns.bullet.killed=1
- t108-aldea-pax32-defecto: enemy:scguns.bullet.annihilated=29 enemy:scguns.bullet.decimated=34 enemy:scguns.bullet.eliminated=32 enemy:scguns.bullet.executed=37 enemy:scguns.bullet.killed=31
- t108-aldea-pax32-cobertura: enemy:scguns.bullet.annihilated=26 enemy:scguns.bullet.decimated=31 enemy:scguns.bullet.eliminated=35 enemy:scguns.bullet.executed=28 enemy:scguns.bullet.killed=23
- t108-bosque-pax48-defecto: enemy:scguns.bullet.annihilated=19 enemy:scguns.bullet.decimated=11 enemy:scguns.bullet.eliminated=12 enemy:scguns.bullet.executed=16 enemy:scguns.bullet.killed=17
- t108-bosque-pax48-cobertura: enemy:scguns.bullet.annihilated=17 enemy:scguns.bullet.decimated=7 enemy:scguns.bullet.eliminated=14 enemy:scguns.bullet.executed=11 enemy:scguns.bullet.killed=16
- t108-llano-pax32-defecto: enemy:scguns.bullet.annihilated=9 enemy:scguns.bullet.decimated=11 enemy:scguns.bullet.eliminated=13 enemy:scguns.bullet.executed=13 enemy:scguns.bullet.killed=10
- t108-llano-pax32-cobertura: enemy:scguns.bullet.annihilated=11 enemy:scguns.bullet.decimated=12 enemy:scguns.bullet.eliminated=11 enemy:scguns.bullet.executed=12 enemy:scguns.bullet.killed=16

## Motivos de decisión (cambios registrados): ENGAGE 4708, LOW_HEALTH_RETREAT 728, NONE 67, OUT_OF_RANGE 1541, SEEK_ENEMY 474, STUCK 166, TOO_CLOSE 1191, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
