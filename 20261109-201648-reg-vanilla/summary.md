# Simulación Rally - lote 20261109-201648-reg-vanilla

Preset `reg-vanilla` · velocidad `sprint` · 49 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t25-vanilla-hordas-husk-base-r40 | 8 | 100 % ± 0 | 0 % ± 0 | 7.1 ± 0.2 | 0.0 ± 0.0 | 47 ± 4 | 975 ± 7 | 119 ± 24 | 93 % ± 2 | 9.9 ± 1.2 | 12.5 ± 1.0 | 0 |
| t25-vanilla-raros-wither_skeleton-base-r12 | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 15 ± 1 | 283 ± 3 | 21 ± 5 | 88 % ± 3 | 12.2 ± 0.4 | 6.5 ± 0.2 | 0 |
| t25-vanilla-tiradores-drowned-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 2 | 571 ± 6 | 45 ± 13 | 91 % ± 1 | 9.1 ± 0.5 | 7.1 ± 0.2 | 0 |
| t25-vanilla-raros-vindicator-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.2 | 0.0 ± 0.0 | 9 ± 0 | 165 ± 3 | 14 ± 6 | 90 % ± 3 | 10.8 ± 0.8 | 6.8 ± 0.5 | 0 |
| t25-vanilla-hordas-spider-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 20 ± 2 | 460 ± 4 | 12 ± 4 | 91 % ± 3 | 9.8 ± 0.6 | 9.7 ± 0.8 | 0 |
| t25-vanilla-tiradores-stray-base-r32 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 33 ± 4 | 524 ± 16 | 35 ± 10 | 84 % ± 2 | 15.3 ± 0.5 | 9.0 ± 0.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t25-vanilla-hordas-husk-base-r40 semilla 1: sprint → blue (7/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 48 de 48 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t25-vanilla-hordas-husk-base-r40: enemy:mob=3
- t25-vanilla-raros-wither_skeleton-base-r12: enemy:mob=2
- t25-vanilla-raros-vindicator-base-r6: enemy:mob=3
- t25-vanilla-hordas-spider-base-r24: enemy:mob=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 41, ENGAGE 404, LOW_HEALTH_RETREAT 17, MORALE_SHAKEN 91, MORALE_WAVERING 41, NONE 135, OUT_OF_RANGE 7, RALLIED 3, ROUTED_FLEE 60, SEEK_ENEMY 4, STUCK 8, TOO_CLOSE 33

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
