# Simulación Rally - lote 20261108-200017-t25-vanilla-raros-base

Preset `t25-vanilla-raros-base` · velocidad `sprint` · 101 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t25-vanilla-raros-witch-base-r4 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 1 | 133 ± 4 | 0 ± 0 | 81 % ± 2 | 21.4 ± 0.7 | 6.5 ± 0.2 | 0 |
| t25-vanilla-raros-witch-base-r8 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 17 ± 1 | 276 ± 6 | 7 ± 3 | 87 % ± 1 | 17.8 ± 0.3 | 8.0 ± 0.2 | 0 |
| t25-vanilla-raros-vindicator-base-r6 | 10 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.2 | 0.0 ± 0.0 | 9 ± 0 | 155 ± 1 | 27 ± 4 | 92 % ± 2 | 6.6 ± 0.2 | 4.4 ± 0.1 | 0 |
| t25-vanilla-raros-vindicator-base-r12 | 10 | 30 % ± 15 | 70 % ± 15 | 0.8 ± 0.4 | 4.0 ± 1.4 | 16 ± 1 | 263 ± 19 | 209 ± 6 | 94 % ± 2 | 5.2 ± 0.2 | 5.2 ± 0.8 | 0 |
| t25-vanilla-raros-evoker-base-r2 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 8 ± 0 | 94 ± 3 | 8 ± 4 | 76 % ± 3 | 10.6 ± 0.7 | 4.1 ± 0.1 | 0 |
| t25-vanilla-raros-evoker-base-r4 | 10 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 14 ± 0 | 206 ± 6 | 32 ± 5 | 78 % ± 2 | 8.9 ± 0.5 | 5.3 ± 0.2 | 0 |
| t25-vanilla-raros-enderman-base-r3 | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 41 ± 2 | 0 ± 0 | 249 ± 4 | 0 % ± 0 | 10.6 ± 1.0 | 8.6 ± 0.6 | 0 |
| t25-vanilla-raros-enderman-base-r6 | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 6.0 ± 0.0 | 23 ± 1 | 0 ± 0 | 251 ± 5 | 0 % ± 0 | 5.8 ± 0.3 | 7.5 ± 0.5 | 0 |
| t25-vanilla-raros-wither_skeleton-base-r6 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 11 ± 0 | 141 ± 2 | 0 ± 0 | 82 % ± 1 | 16.2 ± 0.2 | 6.1 ± 0.2 | 0 |
| t25-vanilla-raros-wither_skeleton-base-r12 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 0 | 281 ± 2 | 19 ± 2 | 87 % ± 2 | 11.9 ± 0.4 | 6.3 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t25-vanilla-raros-witch-base-r4 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 73 de 73 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t25-vanilla-raros-vindicator-base-r6: enemy:mob=4
- t25-vanilla-raros-vindicator-base-r12: enemy:mob=72
- t25-vanilla-raros-evoker-base-r4: enemy:mob=1
- t25-vanilla-raros-enderman-base-r3: enemy:mob=80
- t25-vanilla-raros-enderman-base-r6: enemy:mob=80

## Motivos de decisión (cambios registrados): COUNTERATTACK 3, ENGAGE 745, LOW_HEALTH_RETREAT 40, MORALE_SHAKEN 238, MORALE_WAVERING 27, NONE 44, REFUSE_ATTACK 4, ROUTED_FLEE 10, SEEK_ENEMY 244, STUCK 5, TOO_CLOSE 114, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
