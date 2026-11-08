# Simulación Rally - lote 20261108-195857-t24-techo-skeleton-base

Preset `t24-techo-skeleton-base` · velocidad `sprint` · 81 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t24-techo-skeleton-arco-base-r68 | 20 | 80 % ± 9 | 20 % ± 9 | 3.7 ± 0.7 | 1.1 ± 0.5 | 39 ± 2 | 945 ± 10 | 165 ± 5 | 96 % ± 0 | 14.7 ± 0.2 | 6.7 ± 0.6 | 0 |
| t24-techo-skeleton-arco-base-r95 | 20 | 20 % ± 9 | 80 % ± 9 | 0.5 ± 0.3 | 9.8 ± 1.7 | 41 ± 2 | 880 ± 22 | 186 ± 2 | 97 % ± 0 | 14.5 ± 0.3 | 6.8 ± 0.6 | 0 |
| t24-techo-skeleton-linea-base-r110 | 20 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.3 | 0.0 ± 0.0 | 46 ± 2 | 725 ± 8 | 90 ± 7 | 88 % ± 1 | 13.8 ± 0.3 | 10.9 ± 0.6 | 0 |
| t24-techo-skeleton-linea-base-r150 | 20 | 93 % ± 5 | 8 % ± 5 | 6.8 ± 0.5 | 0.2 ± 0.1 | 54 ± 7 | 756 ± 20 | 105 ± 10 | 89 % ± 1 | 13.2 ± 0.4 | 10.3 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t24-techo-skeleton-arco-base-r68 semilla 1: sprint → blue (1/0 vivos), normal → blue (3/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 59 de 59 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t24-techo-skeleton-arco-base-r68: enemy:arrow=87
- t24-techo-skeleton-arco-base-r95: enemy:arrow=150
- t24-techo-skeleton-linea-base-r110: enemy:arrow=9
- t24-techo-skeleton-linea-base-r150: enemy:arrow=25

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 864, LOW_HEALTH_RETREAT 1008, MORALE_SHAKEN 352, MORALE_WAVERING 182, NONE 33, OUT_OF_RANGE 45, RALLIED 4, REFUSE_ATTACK 10, ROUTED_FLEE 47, SEEK_ENEMY 523, TOO_CLOSE 27

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
