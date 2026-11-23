# Simulación Rally - lote 20261122-194746-t133-outnumbered

Preset `t133-outnumbered` · velocidad `sprint` · 241 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t133-outnumbered-16-r5 | 30 | 100 % ± 0 | 0 % ± 0 | 4.0 ± 0.0 | 0.0 ± 0.0 | 29 ± 1 | 416 ± 4 | 108 ± 1 | 70 % ± 2 | 14.9 ± 0.9 | 14.1 ± 1.5 | 0 |
| t133-outnumbered-16-r5-offOutnumbered | 30 | 100 % ± 0 | 0 % ± 0 | 4.1 ± 0.0 | 0.0 ± 0.0 | 23 ± 1 | 418 ± 5 | 110 ± 1 | 72 % ± 2 | 13.2 ± 0.9 | 9.8 ± 0.7 | 0 |
| t133-outnumbered-32-r5 | 30 | 50 % ± 9 | 50 % ± 9 | 1.3 ± 0.3 | 12.8 ± 2.3 | 109 ± 6 | 600 ± 45 | 184 ± 7 | 79 % ± 2 | 18.5 ± 1.6 | 24.5 ± 2.7 | 0 |
| t133-outnumbered-32-r5-offOutnumbered | 30 | 100 % ± 0 | 0 % ± 0 | 3.9 ± 0.1 | 0.0 ± 0.0 | 41 ± 1 | 833 ± 5 | 114 ± 3 | 78 % ± 1 | 17.0 ± 1.0 | 18.7 ± 1.1 | 0 |
| t133-outnumbered-48-r5 | 30 | 20 % ± 7 | 80 % ± 7 | 0.5 ± 0.2 | 32.1 ± 2.7 | 125 ± 7 | 610 ± 58 | 210 ± 5 | 81 % ± 1 | 14.8 ± 1.1 | 18.0 ± 2.2 | 0 |
| t133-outnumbered-48-r5-offOutnumbered | 30 | 90 % ± 6 | 10 % ± 6 | 2.7 ± 0.2 | 2.3 ± 1.3 | 63 ± 2 | 1213 ± 22 | 150 ± 5 | 84 % ± 1 | 17.7 ± 1.3 | 23.6 ± 2.0 | 0 |
| t133-outnumbered-64-r5 | 30 | 12 % ± 5 | 88 % ± 5 | 0.4 ± 0.2 | 51.0 ± 3.4 | 100 ± 9 | 529 ± 80 | 205 ± 4 | 78 % ± 2 | 16.5 ± 2.0 | 21.8 ± 2.3 | 0 |
| t133-outnumbered-64-r5-offOutnumbered | 30 | 77 % ± 8 | 23 % ± 8 | 1.4 ± 0.2 | 7.9 ± 2.7 | 99 ± 6 | 1513 ± 52 | 179 ± 5 | 88 % ± 1 | 17.4 ± 1.8 | 20.9 ± 2.7 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t133-outnumbered-16-r5 semilla 12500: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 169 de 169 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t133-outnumbered-16-r5: enemy:mob=113 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=2
- t133-outnumbered-16-r5-offOutnumbered: enemy:mob=111 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t133-outnumbered-32-r5: enemy:mob=193 enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=1
- t133-outnumbered-32-r5-offOutnumbered: enemy:mob=122 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.executed=1
- t133-outnumbered-48-r5: enemy:mob=217 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=1
- t133-outnumbered-48-r5-offOutnumbered: enemy:mob=152 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t133-outnumbered-64-r5: enemy:mob=223 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=1
- t133-outnumbered-64-r5-offOutnumbered: enemy:mob=183 enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=4

## Motivos de decisión (cambios registrados): COUNTERATTACK 86, COVER_AVAILABLE 611, ENGAGE 7865, FOCUS_FIRE 2602, FORMATION_HOLD 42, LOW_HEALTH_RETREAT 1093, NONE 229, OUTNUMBERED 3197, OUT_OF_RANGE 1406, PAIR_BOUND 2977, SEEK_ENEMY 2770, SHOOT_AND_MOVE 2302, THREAT_MEMORY 291, TOO_CLOSE 180

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
