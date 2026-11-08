# Simulación Rally - lote 20261108-193200-t23-techo-skeleton-orakite

Preset `t23-techo-skeleton-orakite` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t23-techo-skeleton-arco-orakite-r68 | 30 | 83 % ± 7 | 17 % ± 7 | 4.2 ± 0.5 | 0.7 ± 0.4 | 41 ± 2 | 929 ± 12 | 162 ± 4 | 96 % ± 0 | 15.2 ± 0.3 | 7.1 ± 0.7 | 0 |
| t23-techo-skeleton-linea-orakite-r80 | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.0 | 0.0 ± 0.0 | 42 ± 1 | 732 ± 9 | 73 ± 3 | 85 % ± 1 | 13.8 ± 0.2 | 10.5 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t23-techo-skeleton-arco-orakite-r68 semilla 1: sprint → blue (5/0 vivos), normal → blue (1/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 55 de 55 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t23-techo-skeleton-arco-orakite-r68: enemy:arrow=113
- t23-techo-skeleton-linea-orakite-r80: enemy:arrow=2

## Motivos de decisión (cambios registrados): ENGAGE 683, LOW_HEALTH_RETREAT 598, MORALE_SHAKEN 239, MORALE_WAVERING 71, NONE 54, OUT_OF_RANGE 32, RALLIED 1, REFUSE_ATTACK 6, ROUTED_FLEE 12, SEEK_ENEMY 344, TOO_CLOSE 17

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
