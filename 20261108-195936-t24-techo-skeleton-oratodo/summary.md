# Simulación Rally - lote 20261108-195936-t24-techo-skeleton-oratodo

Preset `t24-techo-skeleton-oratodo` · velocidad `sprint` · 81 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t24-techo-skeleton-arco-oratodo-r68 | 20 | 80 % ± 9 | 20 % ± 9 | 4.4 ± 0.7 | 1.4 ± 0.6 | 39 ± 2 | 996 ± 16 | 159 ± 5 | 92 % ± 1 | 14.6 ± 0.3 | 6.7 ± 0.3 | 0 |
| t24-techo-skeleton-arco-oratodo-r95 | 20 | 15 % ± 8 | 85 % ± 8 | 0.5 ± 0.3 | 9.4 ± 1.4 | 40 ± 2 | 990 ± 26 | 186 ± 2 | 94 % ± 1 | 13.9 ± 0.2 | 5.9 ± 0.3 | 0 |
| t24-techo-skeleton-linea-oratodo-r110 | 20 | 85 % ± 8 | 15 % ± 8 | 6.5 ± 0.6 | 0.7 ± 0.4 | 55 ± 4 | 858 ± 19 | 111 ± 10 | 86 % ± 1 | 13.5 ± 0.6 | 11.1 ± 0.8 | 0 |
| t24-techo-skeleton-linea-oratodo-r150 | 20 | 90 % ± 7 | 10 % ± 7 | 6.2 ± 0.6 | 0.6 ± 0.4 | 59 ± 6 | 861 ± 16 | 125 ± 9 | 89 % ± 2 | 14.4 ± 1.1 | 12.0 ± 1.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t24-techo-skeleton-arco-oratodo-r68 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 54 de 54 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t24-techo-skeleton-arco-oratodo-r68: enemy:arrow=73
- t24-techo-skeleton-arco-oratodo-r95: enemy:arrow=151
- t24-techo-skeleton-linea-oratodo-r110: enemy:arrow=30
- t24-techo-skeleton-linea-oratodo-r150: enemy:arrow=36

## Motivos de decisión (cambios registrados): ENGAGE 810, LOW_HEALTH_RETREAT 1065, MORALE_SHAKEN 270, MORALE_WAVERING 161, NONE 45, OUT_OF_RANGE 61, RALLIED 8, REFUSE_ATTACK 24, ROUTED_FLEE 60, SEEK_ENEMY 590, STUCK 2, TOO_CLOSE 41

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
