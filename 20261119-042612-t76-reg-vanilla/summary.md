# Simulación Rally - lote 20261119-042612-t76-reg-vanilla

Preset `t76-reg-vanilla` · velocidad `sprint` · 49 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t76-spread-t25-vanilla-hordas-husk-base-r40 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 0 | 953 ± 4 | 28 ± 2 | 97 % ± 0 | 6.5 ± 0.1 | 8.4 ± 0.4 | 0 |
| t76-spread-t25-vanilla-raros-wither_skeleton-base-r12 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 12 ± 0 | 281 ± 2 | 9 ± 2 | 89 % ± 2 | 14.3 ± 0.3 | 6.7 ± 0.2 | 0 |
| t76-spread-t25-vanilla-tiradores-drowned-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 18 ± 0 | 562 ± 6 | 19 ± 2 | 94 % ± 1 | 11.1 ± 0.2 | 7.3 ± 0.2 | 0 |
| t76-spread-t25-vanilla-raros-vindicator-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 8 ± 0 | 162 ± 2 | 11 ± 2 | 89 % ± 2 | 11.4 ± 1.2 | 6.3 ± 0.9 | 0 |
| t76-spread-t25-vanilla-hordas-spider-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 15 ± 0 | 452 ± 5 | 4 ± 1 | 92 % ± 1 | 7.7 ± 0.3 | 7.0 ± 0.4 | 0 |
| t76-spread-t25-vanilla-tiradores-stray-base-r32 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 27 ± 1 | 583 ± 11 | 21 ± 2 | 88 % ± 1 | 15.8 ± 0.3 | 8.9 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t76-spread-t25-vanilla-hordas-husk-base-r40 semilla 8300: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 48 de 48 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 397, LOW_HEALTH_RETREAT 3, MORALE_SHAKEN 1, OUT_OF_RANGE 4, SEEK_ENEMY 6

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
