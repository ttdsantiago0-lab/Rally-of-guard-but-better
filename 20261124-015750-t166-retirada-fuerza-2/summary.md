# Simulación Rally - lote 20261124-015750-t166-retirada-fuerza-2

Preset `t166-retirada-fuerza-2` · velocidad `sprint` · 301 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t166-r32-espera | 30 | 88 % ± 5 | 12 % ± 5 | 3.4 ± 0.2 | 1.1 ± 0.6 | 95 ± 4 | 798 ± 13 | 149 ± 8 | 66 % ± 3 | 27.6 ± 1.8 | 25.3 ± 1.2 | 0 |
| t166-r32-force | 30 | 92 % ± 3 | 8 % ± 3 | 3.4 ± 0.2 | 0.5 ± 0.3 | 77 ± 5 | 807 ± 10 | 144 ± 5 | 69 % ± 3 | 20.3 ± 1.2 | 21.6 ± 1.4 | 0 |
| t166-r32-force1340 | 30 | 90 % ± 4 | 10 % ± 4 | 3.3 ± 0.2 | 0.6 ± 0.3 | 86 ± 5 | 809 ± 11 | 147 ± 8 | 66 % ± 3 | 24.1 ± 1.6 | 22.5 ± 1.2 | 0 |
| t166-r48-espera | 30 | 73 % ± 6 | 27 % ± 6 | 2.7 ± 0.2 | 6.1 ± 1.6 | 104 ± 3 | 1120 ± 29 | 166 ± 7 | 74 % ± 3 | 16.2 ± 1.5 | 24.1 ± 1.4 | 0 |
| t166-r48-force | 30 | 50 % ± 0 | 50 % ± 0 | 4.6 ± 0.3 | 46.9 ± 0.3 | 120 ± 0 | 152 ± 6 | 104 ± 8 | 98 % ± 1 | 6.1 ± 0.3 | 18.3 ± 1.4 | 0 |
| t166-r48-force1340 | 30 | 50 % ± 0 | 50 % ± 0 | 4.5 ± 0.2 | 47.2 ± 0.2 | 120 ± 0 | 150 ± 5 | 106 ± 7 | 94 % ± 2 | 5.7 ± 0.2 | 19.1 ± 1.4 | 0 |
| t166-d45-force | 30 | 50 % ± 0 | 50 % ± 0 | 5.1 ± 0.2 | 79.8 ± 0.1 | 120 ± 0 | 48 ± 4 | 86 ± 6 | 95 % ± 3 | 6.7 ± 0.2 | 15.0 ± 1.2 | 0 |
| t166-p64-espera | 30 | 98 % ± 2 | 2 % ± 2 | 7.7 ± 0.1 | 0.0 ± 0.0 | 44 ± 3 | 952 ± 25 | 124 ± 6 | 68 % ± 3 | 12.9 ± 0.8 | 14.0 ± 0.6 | 0 |
| t166-p64-force | 30 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.1 | 0.0 ± 0.0 | 40 ± 1 | 950 ± 17 | 128 ± 5 | 72 % ± 3 | 11.5 ± 0.6 | 13.3 ± 0.6 | 0 |
| t166-p64-force1340 | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 43 ± 1 | 955 ± 21 | 129 ± 6 | 68 % ± 3 | 12.2 ± 0.7 | 12.5 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t166-r32-espera semilla 14600: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 296 de 296 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t166-r32-espera: enemy:mob=129 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=1
- t166-r32-force: enemy:mob=125 enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=2
- t166-r32-force1340: enemy:mob=127 enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=1
- t166-r48-espera: enemy:mob=144 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=6 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=3
- t166-r48-force: enemy:mob=94 enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t166-r48-force1340: enemy:mob=102 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=1
- t166-d45-force: enemy:mob=79 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=3
- t166-p64-espera: enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t166-p64-force: enemy:arrow=1 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=3
- t166-p64-force1340: enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.killed=2

## Motivos de decisión (cambios registrados): ENGAGE 3628, FORMATION_HOLD 779, INSTRUCTION 720, LOW_HEALTH_RETREAT 328, ORDER_OVERRIDE 730, OUT_OF_RANGE 1332, SEEK_ENEMY 901, STUCK 41, TOO_CLOSE 418

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t166-r48-force: 30/30 corridas llegaron al tope de tiempo
- t166-r48-force1340: 30/30 corridas llegaron al tope de tiempo
- t166-d45-force: 30/30 corridas llegaron al tope de tiempo
