# Simulación Rally - lote 20261119-042715-t85-reg-vanilla

Preset `t85-reg-vanilla` · velocidad `sprint` · 49 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t85-kite-t25-vanilla-hordas-husk-base-r40 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 1 | 969 ± 2 | 37 ± 3 | 97 % ± 1 | 7.2 ± 0.4 | 8.5 ± 0.5 | 0 |
| t85-kite-t25-vanilla-raros-wither_skeleton-base-r12 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 12 ± 0 | 284 ± 3 | 8 ± 3 | 91 % ± 2 | 14.5 ± 0.2 | 6.8 ± 0.2 | 0 |
| t85-kite-t25-vanilla-tiradores-drowned-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 18 ± 1 | 567 ± 6 | 17 ± 2 | 93 % ± 1 | 10.8 ± 0.3 | 6.8 ± 0.2 | 0 |
| t85-kite-t25-vanilla-raros-vindicator-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 8 ± 0 | 162 ± 1 | 5 ± 2 | 87 % ± 4 | 11.2 ± 1.2 | 6.8 ± 1.3 | 0 |
| t85-kite-t25-vanilla-hordas-spider-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 0 | 453 ± 5 | 4 ± 0 | 95 % ± 1 | 7.3 ± 0.1 | 7.1 ± 0.2 | 0 |
| t85-kite-t25-vanilla-tiradores-stray-base-r32 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 27 ± 1 | 580 ± 9 | 22 ± 3 | 92 % ± 1 | 15.4 ± 0.2 | 8.5 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t85-kite-t25-vanilla-hordas-husk-base-r40 semilla 8300: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 48 de 48 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 404, LOW_HEALTH_RETREAT 9, MORALE_SHAKEN 2, MORALE_WAVERING 2, OUT_OF_RANGE 4, SEEK_ENEMY 4, TOO_CLOSE 9

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
