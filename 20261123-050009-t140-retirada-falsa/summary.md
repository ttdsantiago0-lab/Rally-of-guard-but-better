# Simulación Rally - lote 20261123-050009-t140-retirada-falsa

Preset `t140-retirada-falsa` · velocidad `sprint` · 241 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t140-r32-espera | 30 | 88 % ± 5 | 12 % ± 5 | 3.5 ± 0.1 | 0.7 ± 0.3 | 80 ± 5 | 802 ± 12 | 135 ± 5 | 62 % ± 3 | 23.6 ± 1.6 | 22.8 ± 1.2 | 0 |
| t140-r32-early-4 | 30 | 50 % ± 0 | 50 % ± 0 | 4.6 ± 0.2 | 30.8 ± 0.3 | 120 ± 0 | 152 ± 9 | 100 ± 7 | 94 % ± 1 | 6.2 ± 0.3 | 16.6 ± 1.6 | 0 |
| t140-r32-early-8 | 30 | 92 % ± 3 | 8 % ± 3 | 3.1 ± 0.2 | 1.7 ± 1.0 | 81 ± 5 | 787 ± 17 | 157 ± 9 | 66 % ± 3 | 23.8 ± 1.6 | 22.9 ± 1.0 | 0 |
| t140-r48-espera | 30 | 60 % ± 6 | 40 % ± 6 | 2.4 ± 0.2 | 9.4 ± 1.6 | 107 ± 4 | 1078 ± 28 | 177 ± 7 | 75 % ± 2 | 15.5 ± 1.1 | 24.9 ± 1.6 | 0 |
| t140-r48-early-4 | 30 | 50 % ± 0 | 50 % ± 0 | 5.1 ± 0.2 | 47.0 ± 0.2 | 120 ± 0 | 152 ± 4 | 93 ± 7 | 98 % ± 1 | 5.9 ± 0.3 | 19.3 ± 1.6 | 0 |
| t140-r48-early-8 | 30 | 50 % ± 0 | 50 % ± 0 | 2.1 ± 0.2 | 42.9 ± 0.4 | 120 ± 0 | 342 ± 9 | 177 ± 6 | 85 % ± 2 | 6.9 ± 0.2 | 26.5 ± 2.5 | 0 |
| t140-d45-early-4 | 30 | 50 % ± 0 | 50 % ± 0 | 4.6 ± 0.3 | 79.8 ± 0.1 | 120 ± 0 | 43 ± 4 | 97 ± 8 | 87 % ± 6 | 6.9 ± 0.2 | 14.9 ± 1.4 | 0 |
| t140-d45-early-8 | 30 | 50 % ± 0 | 50 % ± 0 | 4.5 ± 0.2 | 79.6 ± 0.1 | 120 ± 0 | 53 ± 5 | 104 ± 6 | 94 % ± 4 | 6.9 ± 0.2 | 17.3 ± 1.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t140-r32-espera semilla 12000: sprint → blue (4/0 vivos), normal → blue (2/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 236 de 236 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t140-r32-espera: enemy:mob=120 enemy:scguns.bullet.annihilated=6 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=1
- t140-r32-early-4: enemy:mob=91 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=1
- t140-r32-early-8: enemy:mob=126 enemy:scguns.bullet.annihilated=6 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=6 enemy:scguns.bullet.executed=5 enemy:scguns.bullet.killed=1
- t140-r48-espera: enemy:mob=154 enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=5
- t140-r48-early-4: enemy:mob=81 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=3
- t140-r48-early-8: enemy:mob=171 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=2
- t140-d45-early-4: enemy:mob=98 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=2
- t140-d45-early-8: enemy:mob=100 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=2

## Motivos de decisión (cambios registrados): ENGAGE 2134, FORMATION_HOLD 380, INSTRUCTION 1095, LOW_HEALTH_RETREAT 117, ORDER_OVERRIDE 1113, OUT_OF_RANGE 713, SEEK_ENEMY 445, STUCK 20, TOO_CLOSE 142

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t140-r32-early-4: 30/30 corridas llegaron al tope de tiempo
- t140-r48-espera: 18/30 corridas llegaron al tope de tiempo
- t140-r48-early-4: 30/30 corridas llegaron al tope de tiempo
- t140-r48-early-8: 30/30 corridas llegaron al tope de tiempo
- t140-d45-early-4: 30/30 corridas llegaron al tope de tiempo
- t140-d45-early-8: 30/30 corridas llegaron al tope de tiempo
