# Simulación Rally - lote 20261108-193202-t23-techo-skeleton-oratodo

Preset `t23-techo-skeleton-oratodo` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t23-techo-skeleton-arco-oratodo-r68 | 30 | 93 % ± 5 | 7 % ± 5 | 5.6 ± 0.5 | 0.2 ± 0.1 | 38 ± 2 | 1012 ± 8 | 147 ± 4 | 88 % ± 1 | 14.7 ± 0.2 | 7.2 ± 0.3 | 0 |
| t23-techo-skeleton-linea-oratodo-r80 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 40 ± 1 | 804 ± 7 | 76 ± 3 | 82 % ± 1 | 13.7 ± 0.3 | 10.4 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t23-techo-skeleton-arco-oratodo-r68 semilla 1: sprint → blue (8/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 58 de 58 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t23-techo-skeleton-arco-oratodo-r68: enemy:arrow=73
- t23-techo-skeleton-linea-oratodo-r80: enemy:arrow=1

## Motivos de decisión (cambios registrados): ENGAGE 529, LOW_HEALTH_RETREAT 537, MORALE_SHAKEN 59, MORALE_WAVERING 28, NONE 26, OUT_OF_RANGE 22, RALLIED 1, REFUSE_ATTACK 3, ROUTED_FLEE 5, SEEK_ENEMY 353, TOO_CLOSE 25

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
