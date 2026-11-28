# Simulación Rally - lote 20261128-053232-t239-aldea-sync

Preset `t239-aldea-sync` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t239-e4a4 | 30 | 68 % ± 4 | 32 % ± 4 | 4.8 ± 0.3 | 3.2 ± 0.6 | 152 ± 8 | 424 ± 15 | 147 ± 6 | 9 % ± 1 | 38.0 ± 1.0 | 20.2 ± 0.9 | 0 |
| t239-e4a4-sync | 30 | 73 % ± 5 | 27 % ± 5 | 5.1 ± 0.3 | 3.7 ± 0.8 | 137 ± 9 | 407 ± 13 | 136 ± 5 | 9 % ± 1 | 37.6 ± 1.2 | 20.3 ± 1.2 | 0 |
| t239-s4a4 | 30 | 67 % ± 4 | 33 % ± 4 | 4.9 ± 0.3 | 3.2 ± 0.6 | 148 ± 8 | 423 ± 13 | 141 ± 6 | 10 % ± 1 | 36.7 ± 0.9 | 19.9 ± 0.9 | 0 |
| t239-s4a4-sync | 30 | 77 % ± 5 | 23 % ± 5 | 5.1 ± 0.2 | 2.3 ± 0.6 | 136 ± 8 | 431 ± 14 | 137 ± 5 | 10 % ± 1 | 37.0 ± 1.1 | 20.3 ± 1.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t239-e4a4 semilla 23500: sprint → blue (8/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t239-e4a4: enemy:scguns.bullet.annihilated=14 enemy:scguns.bullet.decimated=29 enemy:scguns.bullet.eliminated=17 enemy:scguns.bullet.executed=18 enemy:scguns.bullet.killed=18
- t239-e4a4-sync: enemy:scguns.bullet.annihilated=19 enemy:scguns.bullet.decimated=25 enemy:scguns.bullet.eliminated=12 enemy:scguns.bullet.executed=17 enemy:scguns.bullet.killed=14
- t239-s4a4: enemy:scguns.bullet.annihilated=12 enemy:scguns.bullet.decimated=16 enemy:scguns.bullet.eliminated=20 enemy:scguns.bullet.executed=27 enemy:scguns.bullet.killed=19
- t239-s4a4-sync: enemy:scguns.bullet.annihilated=18 enemy:scguns.bullet.decimated=21 enemy:scguns.bullet.eliminated=17 enemy:scguns.bullet.executed=12 enemy:scguns.bullet.killed=19

## Motivos de decisión (cambios registrados): ENGAGE 2896, LOW_HEALTH_RETREAT 514, NONE 275, OUT_OF_RANGE 2033, SEEK_ENEMY 354, STUCK 323

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t239-e4a4: 19/30 corridas llegaron al tope de tiempo
- t239-e4a4-sync: 16/30 corridas llegaron al tope de tiempo
- t239-s4a4: 20/30 corridas llegaron al tope de tiempo
