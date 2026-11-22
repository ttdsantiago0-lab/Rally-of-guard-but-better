# Simulación Rally - lote 20261122-092209-t124-veterania

Preset `t124-veterania` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t124-veterania-pist-r0 | 30 | 90 % ± 6 | 10 % ± 6 | 4.7 ± 0.4 | 0.8 ± 0.5 | 51 ± 2 | 1223 ± 12 | 117 ± 12 | 65 % ± 1 | 46.5 ± 0.7 | 24.3 ± 2.0 | 0 |
| t124-veterania-pist-r3 | 30 | 67 % ± 9 | 33 % ± 9 | 2.0 ± 0.3 | 3.2 ± 1.0 | 53 ± 2 | 1110 ± 21 | 211 ± 8 | 75 % ± 1 | 42.0 ± 0.5 | 18.6 ± 1.7 | 0 |
| t124-veterania-pist-r5 | 30 | 47 % ± 9 | 53 % ± 9 | 1.2 ± 0.3 | 9.4 ± 2.4 | 50 ± 3 | 962 ± 54 | 221 ± 7 | 80 % ± 1 | 41.3 ± 0.7 | 13.4 ± 1.5 | 0 |
| t124-veterania-vind-r0 | 30 | 100 % ± 0 | 0 % ± 0 | 3.9 ± 0.1 | 0.0 ± 0.0 | 41 ± 1 | 832 ± 8 | 115 ± 2 | 75 % ± 1 | 18.6 ± 1.2 | 20.3 ± 1.1 | 0 |
| t124-veterania-vind-r3 | 30 | 100 % ± 0 | 0 % ± 0 | 4.0 ± 0.0 | 0.0 ± 0.0 | 41 ± 1 | 849 ± 5 | 111 ± 2 | 79 % ± 1 | 16.3 ± 1.2 | 17.8 ± 1.2 | 0 |
| t124-veterania-vind-r5 | 30 | 43 % ± 9 | 57 % ± 9 | 1.2 ± 0.3 | 12.3 ± 2.1 | 108 ± 7 | 614 ± 41 | 190 ± 7 | 81 % ± 2 | 21.3 ± 2.3 | 26.2 ± 2.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t124-veterania-pist-r0 semilla 11500: sprint → blue (6/0 vivos), normal → blue (3/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 135 de 135 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t124-veterania-pist-r0: enemy:scguns.bullet.annihilated=22 enemy:scguns.bullet.decimated=17 enemy:scguns.bullet.eliminated=27 enemy:scguns.bullet.executed=15 enemy:scguns.bullet.killed=18
- t124-veterania-pist-r3: enemy:scguns.bullet.annihilated=29 enemy:scguns.bullet.decimated=30 enemy:scguns.bullet.eliminated=50 enemy:scguns.bullet.executed=41 enemy:scguns.bullet.killed=30
- t124-veterania-pist-r5: enemy:scguns.bullet.annihilated=45 enemy:scguns.bullet.decimated=42 enemy:scguns.bullet.eliminated=35 enemy:scguns.bullet.executed=42 enemy:scguns.bullet.killed=39
- t124-veterania-vind-r0: enemy:mob=124
- t124-veterania-vind-r3: enemy:mob=118 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1
- t124-veterania-vind-r5: enemy:mob=201 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.executed=1

## Motivos de decisión (cambios registrados): COVER_AVAILABLE 140, ENGAGE 2720, FOCUS_FIRE 1157, FORMATION_HOLD 9, LOW_HEALTH_RETREAT 526, NONE 289, OUTNUMBERED 1040, OUT_OF_RANGE 1694, PAIR_BOUND 83, SEEK_ENEMY 848, SHOOT_AND_MOVE 273, TARGET_PRIORITY 187, THREAT_MEMORY 84, TOO_CLOSE 41

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
