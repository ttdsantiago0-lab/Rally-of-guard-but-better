# Simulación Rally - lote 20261122-190805-t128-veterania-ablacion

Preset `t128-veterania-ablacion` · velocidad `sprint` · 211 corridas · 7 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t128-veterania-pist-r0 | 30 | 100 % ± 0 | 0 % ± 0 | 5.4 ± 0.3 | 0.0 ± 0.0 | 50 ± 1 | 1257 ± 7 | 99 ± 10 | 67 % ± 1 | 45.6 ± 0.6 | 22.6 ± 2.0 | 0 |
| t128-veterania-pist-r5 | 30 | 37 % ± 9 | 63 % ± 9 | 1.0 ± 0.3 | 7.6 ± 1.5 | 52 ± 2 | 1026 ± 32 | 232 ± 8 | 78 % ± 1 | 41.6 ± 0.6 | 16.2 ± 1.5 | 0 |
| t128-veterania-pist-r5-offBand | 30 | 83 % ± 7 | 17 % ± 7 | 3.1 ± 0.4 | 2.3 ± 1.1 | 56 ± 2 | 1092 ± 21 | 182 ± 11 | 72 % ± 1 | 44.4 ± 0.8 | 19.9 ± 2.0 | 0 |
| t128-veterania-pist-r3-offBand | 30 | 73 % ± 8 | 27 % ± 8 | 2.8 ± 0.4 | 3.7 ± 1.4 | 57 ± 2 | 1106 ± 28 | 164 ± 13 | 70 % ± 1 | 45.2 ± 0.8 | 18.4 ± 1.8 | 0 |
| t128-veterania-vind-r0 | 30 | 100 % ± 0 | 0 % ± 0 | 3.9 ± 0.1 | 0.0 ± 0.0 | 40 ± 1 | 843 ± 6 | 113 ± 2 | 77 % ± 1 | 17.2 ± 0.9 | 18.4 ± 1.2 | 0 |
| t128-veterania-vind-r5 | 30 | 53 % ± 9 | 47 % ± 9 | 1.5 ± 0.3 | 10.5 ± 2.1 | 112 ± 7 | 649 ± 40 | 183 ± 7 | 80 % ± 2 | 20.5 ± 2.0 | 28.4 ± 2.4 | 0 |
| t128-veterania-vind-r5-offOutnumbered | 30 | 100 % ± 0 | 0 % ± 0 | 3.8 ± 0.1 | 0.0 ± 0.0 | 42 ± 1 | 845 ± 5 | 112 ± 2 | 79 % ± 1 | 17.0 ± 1.1 | 20.2 ± 1.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t128-veterania-pist-r0 semilla 12100: sprint → blue (2/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 166 de 166 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t128-veterania-pist-r0: enemy:scguns.bullet.annihilated=10 enemy:scguns.bullet.decimated=15 enemy:scguns.bullet.eliminated=23 enemy:scguns.bullet.executed=17 enemy:scguns.bullet.killed=14
- t128-veterania-pist-r5: enemy:scguns.bullet.annihilated=53 enemy:scguns.bullet.decimated=47 enemy:scguns.bullet.eliminated=36 enemy:scguns.bullet.executed=28 enemy:scguns.bullet.killed=45
- t128-veterania-pist-r5-offBand: enemy:scguns.bullet.annihilated=25 enemy:scguns.bullet.decimated=29 enemy:scguns.bullet.eliminated=28 enemy:scguns.bullet.executed=33 enemy:scguns.bullet.killed=31
- t128-veterania-pist-r3-offBand: enemy:scguns.bullet.annihilated=27 enemy:scguns.bullet.decimated=29 enemy:scguns.bullet.eliminated=34 enemy:scguns.bullet.executed=33 enemy:scguns.bullet.killed=32
- t128-veterania-vind-r0: enemy:mob=121 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1
- t128-veterania-vind-r5: enemy:mob=188 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t128-veterania-vind-r5-offOutnumbered: enemy:mob=120 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 16, COVER_AVAILABLE 120, ENGAGE 6741, FOCUS_FIRE 3836, FORMATION_HOLD 24, LOW_HEALTH_RETREAT 654, NONE 358, OUTNUMBERED 1171, OUT_OF_RANGE 1630, PAIR_BOUND 714, SEEK_ENEMY 946, SHOOT_AND_MOVE 1915, TARGET_PRIORITY 252, THREAT_MEMORY 67, TOO_CLOSE 27

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
