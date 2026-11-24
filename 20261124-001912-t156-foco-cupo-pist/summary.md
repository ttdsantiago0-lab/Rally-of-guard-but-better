# Simulación Rally - lote 20261124-001912-t156-foco-cupo-pist

Preset `t156-foco-cupo-pist` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t156-foco-cupo-pist-r3-foco | 30 | 83 % ± 7 | 17 % ± 7 | 3.1 ± 0.4 | 1.9 ± 0.9 | 59 ± 2 | 1123 ± 19 | 177 ± 11 | 72 % ± 1 | 45.2 ± 0.9 | 16.5 ± 1.7 | 0 |
| t156-foco-cupo-pist-r3-sinfoco | 30 | 100 % ± 0 | 0 % ± 0 | 5.2 ± 0.3 | 0.0 ± 0.0 | 49 ± 1 | 1264 ± 6 | 110 ± 10 | 68 % ± 1 | 47.0 ± 0.6 | 21.4 ± 1.7 | 0 |
| t156-foco-cupo-pist-r3-cupo2 | 30 | 100 % ± 0 | 0 % ± 0 | 5.7 ± 0.4 | 0.0 ± 0.0 | 47 ± 1 | 1265 ± 6 | 87 ± 10 | 69 % ± 1 | 46.8 ± 0.5 | 23.2 ± 1.6 | 0 |
| t156-foco-cupo-pist-r3-cupo3 | 30 | 90 % ± 6 | 10 % ± 6 | 4.2 ± 0.4 | 0.9 ± 0.6 | 50 ± 1 | 1214 ± 19 | 136 ± 13 | 70 % ± 1 | 45.4 ± 0.7 | 20.7 ± 1.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t156-foco-cupo-pist-r3-foco semilla 13500: sprint → blue (1/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 112 de 112 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t156-foco-cupo-pist-r3-foco: enemy:scguns.bullet.annihilated=28 enemy:scguns.bullet.decimated=40 enemy:scguns.bullet.eliminated=23 enemy:scguns.bullet.executed=24 enemy:scguns.bullet.killed=32
- t156-foco-cupo-pist-r3-sinfoco: enemy:scguns.bullet.annihilated=19 enemy:scguns.bullet.decimated=18 enemy:scguns.bullet.eliminated=16 enemy:scguns.bullet.executed=13 enemy:scguns.bullet.killed=17
- t156-foco-cupo-pist-r3-cupo2: enemy:scguns.bullet.annihilated=13 enemy:scguns.bullet.decimated=15 enemy:scguns.bullet.eliminated=17 enemy:scguns.bullet.executed=10 enemy:scguns.bullet.killed=13
- t156-foco-cupo-pist-r3-cupo3: enemy:scguns.bullet.annihilated=22 enemy:scguns.bullet.decimated=25 enemy:scguns.bullet.eliminated=18 enemy:scguns.bullet.executed=23 enemy:scguns.bullet.killed=27

## Motivos de decisión (cambios registrados): COVER_AVAILABLE 40, ENGAGE 3092, FOCUS_FIRE 1626, LOW_HEALTH_RETREAT 382, NONE 180, OUT_OF_RANGE 1105, SEEK_ENEMY 6, TARGET_PRIORITY 219

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
