# Simulación Rally - lote 20261108-195949-t25-vanilla-tiradores-base

Preset `t25-vanilla-tiradores-base` · velocidad `sprint` · 101 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t25-vanilla-tiradores-stray-base-r16 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 27 ± 1 | 347 ± 4 | 15 ± 2 | 74 % ± 2 | 16.8 ± 0.2 | 9.5 ± 0.1 | 0 |
| t25-vanilla-tiradores-stray-base-r32 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 32 ± 1 | 531 ± 8 | 32 ± 4 | 82 % ± 1 | 14.8 ± 0.3 | 9.2 ± 0.3 | 0 |
| t25-vanilla-tiradores-bogged-base-r16 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 22 ± 1 | 283 ± 5 | 8 ± 1 | 72 % ± 3 | 17.0 ± 0.3 | 9.0 ± 0.3 | 0 |
| t25-vanilla-tiradores-bogged-base-r32 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 33 ± 1 | 479 ± 4 | 22 ± 2 | 76 % ± 1 | 15.7 ± 0.5 | 10.8 ± 0.4 | 0 |
| t25-vanilla-tiradores-pillager-base-r8 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 13 ± 0 | 213 ± 3 | 10 ± 1 | 86 % ± 2 | 8.4 ± 0.2 | 6.5 ± 0.3 | 0 |
| t25-vanilla-tiradores-pillager-base-r16 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 21 ± 0 | 397 ± 4 | 28 ± 2 | 89 % ± 1 | 8.8 ± 0.2 | 7.2 ± 0.2 | 0 |
| t25-vanilla-tiradores-drowned-base-r12 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 15 ± 0 | 288 ± 3 | 10 ± 2 | 88 % ± 2 | 11.6 ± 0.3 | 6.8 ± 0.1 | 0 |
| t25-vanilla-tiradores-drowned-base-r24 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 23 ± 0 | 554 ± 8 | 37 ± 4 | 92 % ± 1 | 8.8 ± 0.3 | 7.0 ± 0.3 | 0 |
| t25-vanilla-tiradores-breeze-base-r2 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 15 ± 1 | 60 ± 0 | 0 ± 0 | 82 % ± 3 | 12.7 ± 0.6 | 6.6 ± 0.1 | 0 |
| t25-vanilla-tiradores-breeze-base-r4 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 23 ± 1 | 120 ± 0 | 0 ± 0 | 91 % ± 2 | 11.0 ± 0.4 | 8.0 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t25-vanilla-tiradores-stray-base-r16 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 100 de 100 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 914, LOW_HEALTH_RETREAT 2, MORALE_SHAKEN 9, MORALE_WAVERING 2, NONE 6, OUT_OF_RANGE 13, ROUTED_FLEE 1, SEEK_ENEMY 19, TOO_CLOSE 85

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
