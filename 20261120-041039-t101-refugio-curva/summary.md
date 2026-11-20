# Simulación Rally - lote 20261120-041039-t101-refugio-curva

Preset `t101-refugio-curva` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t101-pelear | 30 | 33 % ± 6 | 67 % ± 6 | 0.9 ± 0.2 | 27.9 ± 3.4 | 98 ± 5 | 1524 ± 67 | 192 ± 5 | 89 % ± 2 | 13.2 ± 1.1 | 16.4 ± 1.9 | 0 |
| t101-casa-0 | 30 | 50 % ± 0 | 50 % ± 0 | 5.2 ± 0.3 | 79.6 ± 0.1 | 120 ± 0 | 28 ± 4 | 84 ± 8 | 45 % ± 9 | 6.3 ± 0.2 | 17.0 ± 1.2 | 0 |
| t101-casa-3 | 30 | 50 % ± 0 | 50 % ± 0 | 2.8 ± 0.3 | 77.8 ± 0.4 | 120 ± 0 | 207 ± 6 | 159 ± 8 | 95 % ± 1 | 6.2 ± 0.2 | 24.5 ± 1.9 | 0 |
| t101-casa-6 | 30 | 45 % ± 3 | 55 % ± 3 | 2.0 ± 0.2 | 75.3 ± 0.4 | 117 ± 2 | 308 ± 4 | 183 ± 5 | 95 % ± 1 | 6.7 ± 0.2 | 29.4 ± 2.4 | 0 |
| t101-casa-9 | 30 | 45 % ± 3 | 55 % ± 3 | 1.7 ± 0.2 | 74.0 ± 0.5 | 119 ± 1 | 380 ± 7 | 186 ± 5 | 91 % ± 2 | 6.9 ± 0.1 | 30.3 ± 2.2 | 0 |
| t101-casa-12 | 30 | 47 % ± 2 | 53 % ± 2 | 1.6 ± 0.2 | 72.5 ± 0.5 | 119 ± 1 | 445 ± 9 | 191 ± 5 | 88 % ± 2 | 7.0 ± 0.2 | 21.5 ± 2.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t101-pelear semilla 9700: sprint → draw (1/41 vivos), normal → draw (3/16 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 158 de 158 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t101-pelear: enemy:mob=207 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t101-casa-0: enemy:mob=79 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t101-casa-3: enemy:mob=141 enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=1
- t101-casa-6: enemy:mob=165 enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=3
- t101-casa-9: enemy:mob=175 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=5 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=1
- t101-casa-12: enemy:mob=174 enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=3

## Motivos de decisión (cambios registrados): ENGAGE 1426, FORMATION_HOLD 1, INSTRUCTION 917, LOW_HEALTH_RETREAT 65, NONE 3, ORDER_OVERRIDE 956, OUT_OF_RANGE 190, SEEK_ENEMY 7, STUCK 55, TOO_CLOSE 176

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t101-casa-0: 30/30 corridas llegaron al tope de tiempo
- t101-casa-3: 30/30 corridas llegaron al tope de tiempo
- t101-casa-6: 27/30 corridas llegaron al tope de tiempo
- t101-casa-9: 27/30 corridas llegaron al tope de tiempo
- t101-casa-12: 28/30 corridas llegaron al tope de tiempo
