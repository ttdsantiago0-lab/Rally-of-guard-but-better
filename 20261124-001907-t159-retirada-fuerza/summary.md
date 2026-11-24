# Simulación Rally - lote 20261124-001907-t159-retirada-fuerza

Preset `t159-retirada-fuerza` · velocidad `sprint` · 301 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t159-r32-espera | 30 | 92 % ± 3 | 8 % ± 3 | 3.3 ± 0.1 | 0.9 ± 0.4 | 78 ± 5 | 797 ± 10 | 157 ± 5 | 69 % ± 2 | 23.9 ± 1.5 | 22.3 ± 1.1 | 0 |
| t159-r32-force | 30 | 88 % ± 5 | 12 % ± 5 | 3.1 ± 0.2 | 0.8 ± 0.3 | 79 ± 5 | 801 ± 10 | 154 ± 7 | 66 % ± 2 | 21.4 ± 1.3 | 22.2 ± 1.1 | 0 |
| t159-r48-espera | 30 | 57 % ± 6 | 43 % ± 6 | 2.1 ± 0.2 | 7.6 ± 1.7 | 105 ± 4 | 1080 ± 31 | 182 ± 6 | 75 % ± 3 | 14.9 ± 1.2 | 23.2 ± 1.6 | 0 |
| t159-r48-force | 30 | 48 % ± 2 | 52 % ± 2 | 4.4 ± 0.3 | 45.2 ± 1.5 | 119 ± 1 | 182 ± 33 | 111 ± 9 | 96 % ± 1 | 6.3 ± 0.3 | 19.0 ± 1.5 | 0 |
| t159-r48-early0-5 | 30 | 50 % ± 0 | 50 % ± 0 | 4.6 ± 0.2 | 46.8 ± 0.3 | 120 ± 0 | 144 ± 5 | 95 ± 5 | 97 % ± 1 | 5.5 ± 0.2 | 17.2 ± 1.4 | 0 |
| t159-d45-force | 30 | 50 % ± 0 | 50 % ± 0 | 4.6 ± 0.2 | 79.8 ± 0.1 | 120 ± 0 | 48 ± 4 | 97 ± 7 | 93 % ± 4 | 6.8 ± 0.1 | 16.1 ± 1.6 | 0 |
| t159-d45-early0-5 | 30 | 50 % ± 0 | 50 % ± 0 | 5.2 ± 0.2 | 79.7 ± 0.1 | 120 ± 0 | 41 ± 4 | 84 ± 6 | 93 % ± 5 | 6.3 ± 0.1 | 13.3 ± 1.2 | 0 |
| t159-p64-espera | 30 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.1 | 0.0 ± 0.0 | 47 ± 3 | 952 ± 21 | 133 ± 6 | 66 % ± 3 | 13.7 ± 1.0 | 14.1 ± 0.6 | 0 |
| t159-p64-force | 30 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.2 | 0.0 ± 0.0 | 45 ± 2 | 977 ± 21 | 136 ± 7 | 72 % ± 3 | 12.5 ± 0.8 | 13.4 ± 0.7 | 0 |
| t159-p64-early0-5 | 30 | 53 % ± 2 | 47 % ± 2 | 7.8 ± 0.1 | 11.1 ± 1.8 | 118 ± 1 | 409 ± 21 | 146 ± 7 | 95 % ± 1 | 6.7 ± 0.5 | 8.8 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t159-r32-espera semilla 12000: sprint → blue (3/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 293 de 293 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t159-r32-espera: enemy:mob=128 enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=5
- t159-r32-force: enemy:mob=126 enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=5
- t159-r48-espera: enemy:mob=153 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=5 enemy:scguns.bullet.killed=10
- t159-r48-force: enemy:mob=96 enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=5 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=1
- t159-r48-early0-5: enemy:mob=88 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=5
- t159-d45-force: enemy:mob=96 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=3
- t159-d45-early0-5: enemy:mob=77 enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.killed=4
- t159-p64-espera: enemy:arrow=2 enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=2
- t159-p64-force: enemy:arrow=4 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=5 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=2
- t159-p64-early0-5: enemy:arrow=2 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1

## Motivos de decisión (cambios registrados): ENGAGE 3131, FORMATION_HOLD 552, INSTRUCTION 1191, LOW_HEALTH_RETREAT 298, ORDER_OVERRIDE 1441, OUT_OF_RANGE 1127, SEEK_ENEMY 617, STUCK 285, TOO_CLOSE 378

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t159-r48-espera: 16/30 corridas llegaron al tope de tiempo
- t159-r48-force: 29/30 corridas llegaron al tope de tiempo
- t159-r48-early0-5: 30/30 corridas llegaron al tope de tiempo
- t159-d45-force: 30/30 corridas llegaron al tope de tiempo
- t159-d45-early0-5: 30/30 corridas llegaron al tope de tiempo
- t159-p64-early0-5: 28/30 corridas llegaron al tope de tiempo
