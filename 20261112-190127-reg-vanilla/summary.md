# Simulación Rally - lote 20261112-190127-reg-vanilla

Preset `reg-vanilla` · velocidad `sprint` · 49 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t25-vanilla-hordas-husk-base-r40 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 27 ± 1 | 966 ± 4 | 40 ± 3 | 98 % ± 1 | 7.1 ± 0.3 | 8.7 ± 0.5 | 0 |
| t25-vanilla-raros-wither_skeleton-base-r12 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 12 ± 0 | 284 ± 3 | 4 ± 1 | 91 % ± 3 | 14.3 ± 0.3 | 6.5 ± 0.1 | 0 |
| t25-vanilla-tiradores-drowned-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 18 ± 0 | 551 ± 8 | 15 ± 4 | 93 % ± 1 | 10.9 ± 0.2 | 7.0 ± 0.2 | 0 |
| t25-vanilla-raros-vindicator-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 7 ± 0 | 163 ± 2 | 1 ± 1 | 96 % ± 1 | 10.3 ± 0.5 | 4.8 ± 0.2 | 0 |
| t25-vanilla-hordas-spider-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 15 ± 0 | 470 ± 4 | 5 ± 1 | 95 % ± 1 | 7.8 ± 0.2 | 7.4 ± 0.3 | 0 |
| t25-vanilla-tiradores-stray-base-r32 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 0 | 575 ± 7 | 25 ± 3 | 93 % ± 0 | 15.5 ± 0.2 | 8.7 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t25-vanilla-hordas-husk-base-r40 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 48 de 48 corridas ✓

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 397, LOW_HEALTH_RETREAT 6, MORALE_SHAKEN 2, MORALE_WAVERING 1, NONE 1, OUT_OF_RANGE 2, ROUTED_FLEE 1, SEEK_ENEMY 3, TOO_CLOSE 3

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
