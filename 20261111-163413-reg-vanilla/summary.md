# Simulación Rally - lote 20261111-163413-reg-vanilla

Preset `reg-vanilla` · velocidad `sprint` · 49 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t25-vanilla-hordas-husk-base-r40 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 0 | 966 ± 7 | 36 ± 5 | 98 % ± 0 | 6.6 ± 0.1 | 9.3 ± 0.4 | 0 |
| t25-vanilla-raros-wither_skeleton-base-r12 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 12 ± 0 | 287 ± 3 | 8 ± 3 | 90 % ± 1 | 14.8 ± 0.4 | 6.9 ± 0.2 | 0 |
| t25-vanilla-tiradores-drowned-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 18 ± 0 | 556 ± 6 | 17 ± 2 | 96 % ± 1 | 10.7 ± 0.3 | 7.2 ± 0.2 | 0 |
| t25-vanilla-raros-vindicator-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 7 ± 0 | 162 ± 3 | 3 ± 2 | 94 % ± 2 | 10.3 ± 0.4 | 4.9 ± 0.2 | 0 |
| t25-vanilla-hordas-spider-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 1 | 461 ± 6 | 7 ± 1 | 94 % ± 1 | 7.4 ± 0.1 | 6.6 ± 0.2 | 0 |
| t25-vanilla-tiradores-stray-base-r32 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 1 | 570 ± 10 | 28 ± 3 | 92 % ± 1 | 15.2 ± 0.3 | 8.5 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t25-vanilla-hordas-husk-base-r40 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 48 de 48 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 411, LOW_HEALTH_RETREAT 7, MORALE_SHAKEN 13, MORALE_WAVERING 4, NONE 2, SEEK_ENEMY 7, TOO_CLOSE 6

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
