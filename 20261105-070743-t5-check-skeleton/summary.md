# Simulación Rally - lote 20261105-070743-t5-check-skeleton

Preset `t5-check-skeleton` · velocidad `sprint` · 301 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-skeleton-mele-base | 30 | 77 % ± 8 | 23 % ± 8 | 5.0 ± 0.6 | 0.9 ± 0.3 | 43 ± 3 | 487 ± 7 | 143 ± 7 | 0 % ± 0 | 7.2 ± 0.3 | 8.5 ± 0.3 | 0 |
| t5-final-skeleton-mele-antes | 30 | 80 % ± 7 | 20 % ± 7 | 5.3 ± 0.5 | 1.2 ± 0.5 | 38 ± 3 | 482 ± 8 | 139 ± 7 | 0 % ± 0 | 7.1 ± 0.3 | 8.7 ± 0.3 | 0 |
| t5-final-skeleton-escudo-base | 30 | 45 % ± 4 | 55 % ± 4 | 5.0 ± 0.5 | 3.3 ± 0.6 | 115 ± 2 | 202 ± 11 | 164 ± 7 | 0 % ± 0 | 7.2 ± 0.2 | 6.6 ± 0.6 | 0 |
| t5-final-skeleton-escudo-antes | 30 | 17 % ± 4 | 83 % ± 4 | 1.2 ± 0.4 | 4.9 ± 0.5 | 95 ± 4 | 222 ± 12 | 194 ± 4 | 0 % ± 0 | 7.4 ± 0.3 | 7.6 ± 0.3 | 0 |
| t5-final-skeleton-arco-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 26 ± 0 | 794 ± 5 | 108 ± 3 | 93 % ± 0 | 14.8 ± 0.1 | 5.5 ± 0.1 | 0 |
| t5-final-skeleton-arco-antes | 30 | 77 % ± 8 | 23 % ± 8 | 4.6 ± 0.6 | 1.7 ± 0.6 | 33 ± 1 | 744 ± 14 | 146 ± 6 | 96 % ± 0 | 10.2 ± 0.3 | 4.7 ± 0.2 | 0 |
| t5-final-skeleton-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 34 ± 1 | 549 ± 4 | 41 ± 2 | 82 % ± 1 | 14.9 ± 0.2 | 10.0 ± 0.2 | 0 |
| t5-final-skeleton-linea-antes | 30 | 38 % ± 9 | 62 % ± 9 | 2.2 ± 0.5 | 3.7 ± 0.7 | 70 ± 4 | 431 ± 13 | 170 ± 6 | 97 % ± 1 | 9.6 ± 0.5 | 9.1 ± 0.6 | 0 |
| t5-final-skeleton-cab-base | 30 | 90 % ± 6 | 10 % ± 6 | 5.9 ± 0.5 | 0.6 ± 0.4 | 37 ± 3 | 667 ± 8 | 119 ± 7 | 95 % ± 1 | 7.9 ± 0.3 | 8.0 ± 0.3 | 0 |
| t5-final-skeleton-cab-antes | 30 | 47 % ± 9 | 53 % ± 9 | 3.0 ± 0.6 | 5.1 ± 1.1 | 41 ± 2 | 554 ± 18 | 154 ± 8 | 98 % ± 1 | 6.4 ± 0.2 | 7.2 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-skeleton-mele-base semilla 1001: sprint → blue (6/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 219 de 219 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-skeleton-mele-base: enemy:arrow=89
- t5-final-skeleton-mele-antes: enemy:arrow=82
- t5-final-skeleton-escudo-base: enemy:arrow=89
- t5-final-skeleton-escudo-antes: enemy:arrow=204
- t5-final-skeleton-arco-base: enemy:arrow=6
- t5-final-skeleton-arco-antes: enemy:arrow=102
- t5-final-skeleton-linea-antes: enemy:arrow=174
- t5-final-skeleton-cab-base: enemy:arrow=64
- t5-final-skeleton-cab-antes: enemy:arrow=151

## Motivos de decisión (cambios registrados): COUNTERATTACK 47, ENGAGE 8563, FORMATION_HOLD 54, LOW_HEALTH_RETREAT 1162, MORALE_SHAKEN 5655, MORALE_WAVERING 2229, NONE 32, OUT_OF_RANGE 19, RALLIED 30, REFUSE_ATTACK 1443, ROUTED_FLEE 1391, SEEK_ENEMY 636, STUCK 3, TOO_CLOSE 246, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t5-final-skeleton-escudo-base: 23/30 corridas llegaron al tope de tiempo
