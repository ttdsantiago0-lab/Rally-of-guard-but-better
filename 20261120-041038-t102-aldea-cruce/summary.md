# Simulación Rally - lote 20261120-041038-t102-aldea-cruce

Preset `t102-aldea-cruce` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t102-pax16-defecto | 30 | 100 % ± 0 | 0 % ± 0 | 6.1 ± 0.3 | 0.0 ± 0.0 | 50 ± 2 | 364 ± 4 | 137 ± 8 | 58 % ± 1 | 17.5 ± 0.5 | 13.5 ± 0.5 | 0 |
| t102-pax16-escudoprimero | 30 | 100 % ± 0 | 0 % ± 0 | 6.0 ± 0.2 | 0.0 ± 0.0 | 51 ± 1 | 362 ± 3 | 136 ± 7 | 56 % ± 1 | 17.2 ± 0.4 | 13.8 ± 0.4 | 0 |
| t102-pax32-defecto | 30 | 70 % ± 9 | 30 % ± 9 | 2.5 ± 0.4 | 1.6 ± 0.5 | 73 ± 2 | 486 ± 11 | 244 ± 9 | 60 % ± 1 | 16.9 ± 0.6 | 12.6 ± 0.5 | 0 |
| t102-pax32-escudoprimero | 30 | 68 % ± 8 | 32 % ± 8 | 2.9 ± 0.4 | 3.4 ± 0.9 | 87 ± 7 | 460 ± 17 | 228 ± 7 | 64 % ± 1 | 15.4 ± 0.6 | 11.0 ± 0.7 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t102-pax16-defecto semilla 9700: sprint → blue (6/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 103 de 103 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t102-pax16-defecto: enemy:scguns.bullet.annihilated=14 enemy:scguns.bullet.decimated=6 enemy:scguns.bullet.eliminated=16 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=14
- t102-pax16-escudoprimero: enemy:scguns.bullet.annihilated=8 enemy:scguns.bullet.decimated=13 enemy:scguns.bullet.eliminated=15 enemy:scguns.bullet.executed=12 enemy:scguns.bullet.killed=13
- t102-pax32-defecto: enemy:scguns.bullet.annihilated=35 enemy:scguns.bullet.decimated=28 enemy:scguns.bullet.eliminated=33 enemy:scguns.bullet.executed=36 enemy:scguns.bullet.killed=34
- t102-pax32-escudoprimero: enemy:scguns.bullet.annihilated=26 enemy:scguns.bullet.decimated=41 enemy:scguns.bullet.eliminated=33 enemy:scguns.bullet.executed=24 enemy:scguns.bullet.killed=30

## Motivos de decisión (cambios registrados): ENGAGE 3087, LOW_HEALTH_RETREAT 511, NONE 41, OUT_OF_RANGE 1058, SEEK_ENEMY 375, STUCK 151, TOO_CLOSE 841

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
