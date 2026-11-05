# Simulación Rally - lote 20261105-070544-t5-check-skeleton

Preset `t5-check-skeleton` · velocidad `sprint` · 301 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-skeleton-mele-base | 30 | 83 % ± 7 | 17 % ± 7 | 5.6 ± 0.5 | 0.8 ± 0.4 | 37 ± 2 | 488 ± 6 | 135 ± 6 | 0 % ± 0 | 7.1 ± 0.2 | 8.8 ± 0.3 | 0 |
| t5-final-skeleton-mele-antes | 30 | 77 % ± 8 | 23 % ± 8 | 5.0 ± 0.5 | 1.2 ± 0.4 | 37 ± 2 | 488 ± 8 | 142 ± 6 | 0 % ± 0 | 6.7 ± 0.2 | 8.7 ± 0.2 | 0 |
| t5-final-skeleton-escudo-base | 30 | 47 % ± 4 | 53 % ± 4 | 4.9 ± 0.5 | 3.5 ± 0.5 | 115 ± 2 | 201 ± 11 | 165 ± 8 | 0 % ± 0 | 7.3 ± 0.2 | 7.1 ± 0.5 | 0 |
| t5-final-skeleton-escudo-antes | 30 | 15 % ± 5 | 85 % ± 5 | 0.7 ± 0.3 | 5.2 ± 0.6 | 93 ± 4 | 205 ± 13 | 195 ± 6 | 0 % ± 0 | 7.8 ± 0.5 | 8.6 ± 0.5 | 0 |
| t5-final-skeleton-arco-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 25 ± 0 | 796 ± 6 | 99 ± 3 | 93 % ± 1 | 14.8 ± 0.1 | 5.4 ± 0.2 | 0 |
| t5-final-skeleton-arco-antes | 30 | 63 % ± 9 | 37 % ± 9 | 4.2 ± 0.7 | 2.1 ± 0.6 | 32 ± 1 | 742 ± 12 | 151 ± 6 | 96 % ± 1 | 10.2 ± 0.2 | 4.3 ± 0.2 | 0 |
| t5-final-skeleton-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 36 ± 2 | 550 ± 5 | 41 ± 3 | 82 % ± 1 | 14.5 ± 0.2 | 9.8 ± 0.2 | 0 |
| t5-final-skeleton-linea-antes | 30 | 38 % ± 9 | 62 % ± 9 | 1.7 ± 0.4 | 3.2 ± 0.6 | 79 ± 4 | 424 ± 12 | 177 ± 6 | 98 % ± 0 | 9.7 ± 0.4 | 9.1 ± 0.6 | 0 |
| t5-final-skeleton-cab-base | 30 | 83 % ± 7 | 17 % ± 7 | 5.9 ± 0.5 | 0.9 ± 0.5 | 35 ± 3 | 658 ± 11 | 119 ± 7 | 94 % ± 1 | 6.9 ± 0.1 | 7.1 ± 0.2 | 0 |
| t5-final-skeleton-cab-antes | 30 | 53 % ± 9 | 47 % ± 9 | 2.6 ± 0.6 | 4.6 ± 1.1 | 48 ± 3 | 559 ± 16 | 162 ± 7 | 97 % ± 1 | 6.6 ± 0.3 | 7.1 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-skeleton-mele-base semilla 1: sprint → blue (6/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 215 de 215 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-skeleton-mele-base: enemy:arrow=72
- t5-final-skeleton-mele-antes: enemy:arrow=91
- t5-final-skeleton-escudo-base: enemy:arrow=94
- t5-final-skeleton-escudo-antes: enemy:arrow=219
- t5-final-skeleton-arco-base: enemy:arrow=5
- t5-final-skeleton-arco-antes: enemy:arrow=114
- t5-final-skeleton-linea-base: enemy:arrow=2
- t5-final-skeleton-linea-antes: enemy:arrow=190
- t5-final-skeleton-cab-base: enemy:arrow=63
- t5-final-skeleton-cab-antes: enemy:arrow=162

## Motivos de decisión (cambios registrados): COUNTERATTACK 64, ENGAGE 8695, FORMATION_HOLD 69, LOW_HEALTH_RETREAT 1037, MORALE_SHAKEN 5675, MORALE_WAVERING 2207, NONE 23, OUT_OF_RANGE 9, RALLIED 34, REFUSE_ATTACK 1443, ROUTED_FLEE 1379, SEEK_ENEMY 578, STUCK 1, TOO_CLOSE 288, UNIT_BROKEN 8

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t5-final-skeleton-escudo-base: 24/30 corridas llegaron al tope de tiempo
