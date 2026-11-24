# Simulación Rally - lote 20261124-055648-reg-vanilla

Preset `reg-vanilla` · velocidad `sprint` · 49 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t25-vanilla-hordas-husk-base-r40 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 0 | 968 ± 4 | 30 ± 1 | 95 % ± 1 | 6.9 ± 0.5 | 8.5 ± 0.7 | 0 |
| t25-vanilla-raros-wither_skeleton-base-r12 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 12 ± 0 | 282 ± 2 | 7 ± 1 | 90 % ± 2 | 14.3 ± 0.3 | 6.4 ± 0.1 | 0 |
| t25-vanilla-tiradores-drowned-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 19 ± 0 | 560 ± 11 | 25 ± 6 | 90 % ± 1 | 11.0 ± 0.3 | 7.6 ± 0.1 | 0 |
| t25-vanilla-raros-vindicator-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 7 ± 0 | 161 ± 2 | 0 ± 0 | 88 % ± 2 | 10.1 ± 0.6 | 5.0 ± 0.2 | 0 |
| t25-vanilla-hordas-spider-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 15 ± 0 | 468 ± 5 | 4 ± 1 | 95 % ± 1 | 7.2 ± 0.2 | 6.6 ± 0.3 | 0 |
| t25-vanilla-tiradores-stray-base-r32 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 1 | 578 ± 7 | 23 ± 3 | 90 % ± 1 | 15.5 ± 0.2 | 8.7 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t25-vanilla-hordas-husk-base-r40 semilla 15600: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 48 de 48 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 408, LOW_HEALTH_RETREAT 5, MORALE_SHAKEN 3, NONE 1, OUT_OF_RANGE 3, SEEK_ENEMY 6, TOO_CLOSE 6

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
