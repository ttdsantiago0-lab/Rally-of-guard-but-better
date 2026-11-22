# Simulación Rally - lote 20261122-075114-t118-retirada-disparador

Preset `t118-retirada-disparador` · velocidad `sprint` · 318 corridas · 13 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t118-d30-pelear | 25 | 26 % ± 6 | 74 % ± 6 | 0.8 ± 0.2 | 27.1 ± 3.9 | 95 ± 6 | 1553 ± 77 | 194 ± 5 | 92 % ± 2 | 14.8 ± 1.9 | 19.8 ± 2.6 | 0 |
| t118-d30-casa-0 | 25 | 50 % ± 0 | 50 % ± 0 | 5.2 ± 0.3 | 79.5 ± 0.2 | 120 ± 0 | 31 ± 5 | 84 ± 9 | 36 % ± 10 | 6.4 ± 0.4 | 14.9 ± 1.5 | 0 |
| t118-d30-dano | 25 | 40 % ± 4 | 60 % ± 4 | 2.6 ± 0.4 | 77.2 ± 0.4 | 113 ± 4 | 222 ± 14 | 156 ± 10 | 96 % ± 1 | 6.3 ± 0.3 | 17.6 ± 2.0 | 0 |
| t118-d30-early-4 | 25 | 48 % ± 2 | 52 % ± 2 | 4.2 ± 0.3 | 79.0 ± 0.2 | 119 ± 1 | 140 ± 5 | 109 ± 9 | 97 % ± 1 | 5.4 ± 0.1 | 17.1 ± 1.6 | 0 |
| t118-d30-early-2 | 25 | 50 % ± 0 | 50 % ± 0 | 4.4 ± 0.3 | 79.1 ± 0.2 | 120 ± 0 | 141 ± 6 | 106 ± 9 | 96 % ± 2 | 5.4 ± 0.2 | 16.9 ± 1.8 | 0 |
| t118-d45-pelear | 25 | 20 % ± 5 | 80 % ± 5 | 0.5 ± 0.1 | 39.2 ± 3.9 | 88 ± 7 | 1334 ± 80 | 198 ± 4 | 94 % ± 1 | 13.1 ± 1.0 | 17.6 ± 2.2 | 0 |
| t118-d45-casa-0 | 24 | 50 % ± 0 | 50 % ± 0 | 5.4 ± 0.2 | 79.7 ± 0.2 | 120 ± 0 | 30 ± 4 | 78 ± 6 | 29 % ± 9 | 8.0 ± 0.4 | 15.4 ± 1.6 | 0 |
| t118-d45-dano | 24 | 50 % ± 0 | 50 % ± 0 | 2.8 ± 0.2 | 77.8 ± 0.3 | 120 ± 0 | 238 ± 11 | 151 ± 7 | 98 % ± 1 | 7.3 ± 0.3 | 22.5 ± 2.3 | 0 |
| t118-d45-early-4 | 24 | 48 % ± 2 | 52 % ± 2 | 5.3 ± 0.3 | 79.8 ± 0.1 | 116 ± 4 | 39 ± 4 | 77 ± 7 | 89 % ± 6 | 6.8 ± 0.4 | 14.6 ± 1.4 | 0 |
| t118-d45-early-2 | 24 | 48 % ± 2 | 52 % ± 2 | 4.5 ± 0.3 | 79.6 ± 0.1 | 119 ± 1 | 51 ± 4 | 96 ± 7 | 96 % ± 4 | 6.8 ± 0.2 | 15.2 ± 1.6 | 0 |
| t118-d30-r16-pelear | 24 | 100 % ± 0 | 0 % ± 0 | 4.7 ± 0.2 | 0.0 ± 0.0 | 19 ± 1 | 411 ± 4 | 105 ± 4 | 75 % ± 3 | 14.1 ± 0.9 | 10.1 ± 0.6 | 0 |
| t118-d30-r16-espera | 24 | 100 % ± 0 | 0 % ± 0 | 6.0 ± 0.3 | 0.0 ± 0.0 | 21 ± 2 | 409 ± 4 | 80 ± 6 | 72 % ± 3 | 16.0 ± 1.3 | 11.3 ± 0.6 | 0 |
| t118-d30-r16-early-4 | 24 | 100 % ± 0 | 0 % ± 0 | 5.7 ± 0.3 | 0.0 ± 0.0 | 18 ± 1 | 413 ± 5 | 87 ± 6 | 75 % ± 3 | 12.5 ± 0.7 | 9.7 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 282 de 282 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t118-d30-pelear: enemy:mob=177 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.executed=2
- t118-d30-casa-0: enemy:mob=66 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1
- t118-d30-dano: enemy:mob=122 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=3
- t118-d30-early-4: enemy:mob=90 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t118-d30-early-2: enemy:mob=82 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=3
- t118-d45-pelear: enemy:mob=179 enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1
- t118-d45-casa-0: enemy:mob=57 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.killed=1
- t118-d45-dano: enemy:mob=121 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t118-d45-early-4: enemy:mob=61 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=2
- t118-d45-early-2: enemy:mob=75 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.killed=2
- t118-d30-r16-pelear: enemy:mob=76 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=2
- t118-d30-r16-espera: enemy:mob=39 enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=3
- t118-d30-r16-early-4: enemy:mob=53 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=2

## Motivos de decisión (cambios registrados): ENGAGE 1955, FORMATION_HOLD 45, INSTRUCTION 1560, LOW_HEALTH_RETREAT 104, NONE 8, ORDER_OVERRIDE 1592, OUT_OF_RANGE 329, SEEK_ENEMY 53, STUCK 38, TOO_CLOSE 210

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t118-d30-casa-0: 25/25 corridas llegaron al tope de tiempo
- t118-d30-dano: 20/25 corridas llegaron al tope de tiempo
- t118-d30-early-4: 24/25 corridas llegaron al tope de tiempo
- t118-d30-early-2: 25/25 corridas llegaron al tope de tiempo
- t118-d45-casa-0: 24/24 corridas llegaron al tope de tiempo
- t118-d45-dano: 24/24 corridas llegaron al tope de tiempo
- t118-d45-early-4: 23/24 corridas llegaron al tope de tiempo
- t118-d45-early-2: 23/24 corridas llegaron al tope de tiempo
