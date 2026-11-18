# Simulación Rally - lote 20261118-052733-t76-reg-vanilla

Preset `t76-reg-vanilla` · velocidad `sprint` · 49 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t76-spread-t25-vanilla-hordas-husk-base-r40 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 0 | 951 ± 3 | 34 ± 4 | 96 % ± 1 | 6.6 ± 0.2 | 8.3 ± 0.4 | 0 |
| t76-spread-t25-vanilla-raros-wither_skeleton-base-r12 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 12 ± 0 | 282 ± 2 | 10 ± 2 | 87 % ± 2 | 14.6 ± 0.3 | 6.5 ± 0.2 | 0 |
| t76-spread-t25-vanilla-tiradores-drowned-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 18 ± 0 | 556 ± 5 | 14 ± 4 | 94 % ± 1 | 11.4 ± 0.4 | 7.1 ± 0.3 | 0 |
| t76-spread-t25-vanilla-raros-vindicator-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 7 ± 0 | 162 ± 2 | 3 ± 2 | 93 % ± 2 | 10.4 ± 0.8 | 5.4 ± 0.6 | 0 |
| t76-spread-t25-vanilla-hordas-spider-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 0 | 455 ± 5 | 5 ± 1 | 94 % ± 1 | 7.5 ± 0.2 | 6.9 ± 0.3 | 0 |
| t76-spread-t25-vanilla-tiradores-stray-base-r32 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 1 | 566 ± 9 | 25 ± 2 | 91 % ± 1 | 14.9 ± 0.1 | 8.6 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t76-spread-t25-vanilla-hordas-husk-base-r40 semilla 7500: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 48 de 48 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 399, LOW_HEALTH_RETREAT 5, SEEK_ENEMY 5, TOO_CLOSE 6

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
