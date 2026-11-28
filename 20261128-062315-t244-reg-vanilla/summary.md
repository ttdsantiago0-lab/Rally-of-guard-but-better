# Simulación Rally - lote 20261128-062315-t244-reg-vanilla

Preset `t244-reg-vanilla` · velocidad `sprint` · 49 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t244-sync-t25-vanilla-hordas-husk-base-r40 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 0 | 951 ± 5 | 43 ± 6 | 97 % ± 1 | 6.7 ± 0.3 | 7.5 ± 0.2 | 0 |
| t244-sync-t25-vanilla-raros-wither_skeleton-base-r12 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 12 ± 0 | 283 ± 3 | 9 ± 2 | 91 % ± 2 | 14.1 ± 0.2 | 6.5 ± 0.2 | 0 |
| t244-sync-t25-vanilla-tiradores-drowned-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 18 ± 0 | 566 ± 5 | 13 ± 2 | 92 % ± 2 | 11.4 ± 0.2 | 7.5 ± 0.2 | 0 |
| t244-sync-t25-vanilla-raros-vindicator-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 8 ± 0 | 163 ± 2 | 8 ± 3 | 91 % ± 1 | 9.8 ± 0.7 | 4.5 ± 0.2 | 0 |
| t244-sync-t25-vanilla-hordas-spider-base-r24 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 0 | 458 ± 5 | 7 ± 1 | 93 % ± 1 | 7.4 ± 0.2 | 6.6 ± 0.3 | 0 |
| t244-sync-t25-vanilla-tiradores-stray-base-r32 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 27 ± 1 | 593 ± 9 | 26 ± 4 | 90 % ± 1 | 15.4 ± 0.1 | 8.2 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t244-sync-t25-vanilla-hordas-husk-base-r40 semilla 24400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 48 de 48 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 398, LOW_HEALTH_RETREAT 15, MORALE_SHAKEN 5, MORALE_WAVERING 3, REFUSE_ATTACK 1, SEEK_ENEMY 9, TOO_CLOSE 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
