# Simulación Rally - lote 20261123-042253-t138-veterania-foco

Preset `t138-veterania-foco` · velocidad `sprint` · 151 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t138-veterania-pist-r0 | 30 | 93 % ± 5 | 7 % ± 5 | 4.9 ± 0.3 | 1.0 ± 0.8 | 50 ± 2 | 1229 ± 17 | 116 ± 10 | 65 % ± 1 | 46.5 ± 0.8 | 23.7 ± 1.9 | 0 |
| t138-veterania-pist-r5-offBand | 30 | 87 % ± 6 | 13 % ± 6 | 3.3 ± 0.4 | 1.3 ± 0.7 | 60 ± 2 | 1127 ± 19 | 160 ± 10 | 72 % ± 1 | 46.3 ± 0.7 | 19.6 ± 1.9 | 0 |
| t138-veterania-pist-r5-offBand-offFocus | 30 | 93 % ± 5 | 7 % ± 5 | 4.3 ± 0.4 | 0.3 ± 0.2 | 51 ± 2 | 1246 ± 11 | 123 ± 11 | 70 % ± 1 | 46.6 ± 0.7 | 20.9 ± 1.7 | 0 |
| t138-veterania-pist-r5-offBand-offScoot | 30 | 87 % ± 6 | 13 % ± 6 | 3.5 ± 0.4 | 1.3 ± 0.8 | 59 ± 2 | 1125 ± 21 | 155 ± 11 | 72 % ± 1 | 45.7 ± 0.7 | 20.3 ± 1.8 | 0 |
| t138-veterania-pist-r3-offBand-offFocus | 30 | 100 % ± 0 | 0 % ± 0 | 5.3 ± 0.3 | 0.0 ± 0.0 | 48 ± 1 | 1248 ± 7 | 102 ± 11 | 69 % ± 1 | 46.5 ± 0.8 | 21.4 ± 1.7 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t138-veterania-pist-r0 semilla 12100: sprint → blue (2/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 138 de 138 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t138-veterania-pist-r0: enemy:scguns.bullet.annihilated=19 enemy:scguns.bullet.decimated=21 enemy:scguns.bullet.eliminated=13 enemy:scguns.bullet.executed=19 enemy:scguns.bullet.killed=20
- t138-veterania-pist-r5-offBand: enemy:scguns.bullet.annihilated=26 enemy:scguns.bullet.decimated=22 enemy:scguns.bullet.eliminated=35 enemy:scguns.bullet.executed=26 enemy:scguns.bullet.killed=31
- t138-veterania-pist-r5-offBand-offFocus: enemy:scguns.bullet.annihilated=17 enemy:scguns.bullet.decimated=25 enemy:scguns.bullet.eliminated=22 enemy:scguns.bullet.executed=20 enemy:scguns.bullet.killed=27
- t138-veterania-pist-r5-offBand-offScoot: enemy:scguns.bullet.annihilated=29 enemy:scguns.bullet.decimated=21 enemy:scguns.bullet.eliminated=29 enemy:scguns.bullet.executed=32 enemy:scguns.bullet.killed=25
- t138-veterania-pist-r3-offBand-offFocus: enemy:scguns.bullet.annihilated=24 enemy:scguns.bullet.decimated=17 enemy:scguns.bullet.eliminated=17 enemy:scguns.bullet.executed=15 enemy:scguns.bullet.killed=8

## Motivos de decisión (cambios registrados): COVER_AVAILABLE 32, ENGAGE 6385, FOCUS_FIRE 2915, LOW_HEALTH_RETREAT 429, NONE 262, OUTNUMBERED 5, OUT_OF_RANGE 641, PAIR_BOUND 10, SEEK_ENEMY 8, SHOOT_AND_MOVE 2812, TARGET_PRIORITY 184, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
