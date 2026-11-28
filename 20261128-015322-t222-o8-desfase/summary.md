# Simulación Rally - lote 20261128-015322-t222-o8-desfase

Preset `t222-o8-desfase` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t222-base | 30 | 75 % ± 7 | 25 % ± 7 | 70.5 ± 7.0 | 15.2 ± 5.0 | 114 ± 8 | 4743 ± 63 | 4001 ± 114 | 65 % ± 1 | 47.5 ± 0.6 | 18.3 ± 0.3 | 0 |
| t222-desfase | 30 | 50 % ± 6 | 50 % ± 6 | 46.9 ± 7.7 | 49.9 ± 7.0 | 146 ± 9 | 4267 ± 108 | 4317 ± 128 | 69 % ± 1 | 43.1 ± 0.7 | 18.4 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t222-base semilla 17800: sprint → blue (83/0 vivos), normal → blue (122/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 50 de 50 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t222-base: enemy:arrow=3884
- t222-desfase: enemy:arrow=4594

## Motivos de decisión (cambios registrados): ENGAGE 43182, LOW_HEALTH_RETREAT 20989, MORALE_SHAKEN 12512, MORALE_WAVERING 8017, NONE 21401, ORDER_OVERRIDE 958, OUT_OF_RANGE 46946, RALLIED 32, REFUSE_ATTACK 2528, ROUTED_FLEE 10906, SEEK_ENEMY 723, STUCK 1, TOO_CLOSE 4, UNIT_BROKEN 4054

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t222-desfase: 18/30 corridas llegaron al tope de tiempo
