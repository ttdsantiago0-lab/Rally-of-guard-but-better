# Simulación Rally - lote 20261108-203449-reg-vanilla

Preset `reg-vanilla` · velocidad `sprint` · 49 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t25-vanilla-hordas-husk-base-r40 | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 34 ± 1 | 970 ± 4 | 64 ± 4 | 94 % ± 1 | 7.3 ± 0.4 | 11.8 ± 0.7 | 0 |
| t25-vanilla-raros-wither_skeleton-base-r12 | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 14 ± 0 | 284 ± 2 | 18 ± 3 | 87 % ± 2 | 11.8 ± 0.5 | 6.6 ± 0.1 | 0 |
| t25-vanilla-tiradores-drowned-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 24 ± 1 | 564 ± 7 | 34 ± 7 | 91 % ± 1 | 9.4 ± 0.5 | 7.2 ± 0.4 | 0 |
| t25-vanilla-raros-vindicator-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 9 ± 0 | 159 ± 1 | 14 ± 5 | 85 % ± 1 | 7.8 ± 0.2 | 5.5 ± 0.2 | 0 |
| t25-vanilla-hordas-spider-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 19 ± 1 | 461 ± 5 | 6 ± 1 | 92 % ± 2 | 8.0 ± 0.6 | 8.5 ± 0.6 | 0 |
| t25-vanilla-tiradores-stray-base-r32 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 31 ± 1 | 527 ± 5 | 30 ± 3 | 82 % ± 2 | 14.4 ± 0.4 | 8.7 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t25-vanilla-hordas-husk-base-r40 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 48 de 48 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t25-vanilla-hordas-husk-base-r40: enemy:mob=1
- t25-vanilla-raros-wither_skeleton-base-r12: enemy:mob=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 2, ENGAGE 476, LOW_HEALTH_RETREAT 30, MORALE_SHAKEN 35, MORALE_WAVERING 16, NONE 2, OUT_OF_RANGE 8, ROUTED_FLEE 6, SEEK_ENEMY 12, TOO_CLOSE 51

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
