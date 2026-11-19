# Simulación Rally - lote 20261119-065147-t90-refugio-casa

Preset `t90-refugio-casa` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t90-vindicadores80-pelear | 30 | 95 % ± 3 | 5 % ± 3 | 5.6 ± 0.3 | 1.3 ± 1.2 | 80 ± 4 | 2109 ± 25 | 87 ± 7 | 77 % ± 2 | 23.2 ± 1.5 | 30.6 ± 1.3 | 0 |
| t90-vindicadores80-campo | 30 | 40 % ± 4 | 60 % ± 4 | 1.5 ± 0.2 | 79.5 ± 0.1 | 109 ± 5 | 25 ± 3 | 208 ± 5 | 89 % ± 6 | 9.7 ± 0.4 | 27.2 ± 2.6 | 0 |
| t90-vindicadores80-casa | 30 | 48 % ± 2 | 52 % ± 2 | 5.5 ± 0.3 | 79.9 ± 0.0 | 120 ± 0 | 11 ± 2 | 73 ± 8 | 63 % ± 9 | 6.1 ± 0.2 | 21.2 ± 1.2 | 0 |
| t90-devastadores8-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 1 | 830 ± 1 | 3 ± 1 | 93 % ± 0 | 11.7 ± 0.7 | 12.7 ± 0.5 | 0 |
| t90-devastadores8-campo | 30 | 50 % ± 0 | 50 % ± 0 | 4.0 ± 0.3 | 8.0 ± 0.0 | 120 ± 0 | 31 ± 6 | 152 ± 9 | 69 % ± 8 | 30.8 ± 1.7 | 28.3 ± 1.4 | 0 |
| t90-devastadores8-casa | 30 | 50 % ± 0 | 50 % ± 0 | 6.4 ± 0.2 | 8.0 ± 0.0 | 120 ± 0 | 2 ± 1 | 49 ± 8 | 11 % ± 5 | 7.6 ± 0.2 | 17.9 ± 1.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t90-vindicadores80-pelear semilla 8900: sprint → blue (6/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 173 de 173 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t90-vindicadores80-pelear: enemy:mob=60 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=5
- t90-vindicadores80-campo: enemy:mob=181 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=4
- t90-vindicadores80-casa: enemy:mob=64 enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=2
- t90-devastadores8-pelear: enemy:mob=1
- t90-devastadores8-campo: enemy:mob=119
- t90-devastadores8-casa: enemy:mob=49

## Motivos de decisión (cambios registrados): ENGAGE 1054, INSTRUCTION 960, LOW_HEALTH_RETREAT 25, NONE 49, ORDER_OVERRIDE 964, OUT_OF_RANGE 469, SEEK_ENEMY 37, STUCK 6, TOO_CLOSE 343, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t90-vindicadores80-campo: 24/30 corridas llegaron al tope de tiempo
- t90-vindicadores80-casa: 29/30 corridas llegaron al tope de tiempo
- t90-devastadores8-campo: 30/30 corridas llegaron al tope de tiempo
- t90-devastadores8-casa: 30/30 corridas llegaron al tope de tiempo
