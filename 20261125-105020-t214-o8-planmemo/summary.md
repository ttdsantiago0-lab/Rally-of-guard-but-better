# Simulación Rally - lote 20261125-105020-t214-o8-planmemo

Preset `t214-o8-planmemo` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t214-400-arcos | 30 | 53 % ± 8 | 47 % ± 8 | 47.6 ± 8.0 | 34.9 ± 7.4 | 120 ± 9 | 4483 ± 109 | 4305 ± 125 | 68 % ± 1 | 46.1 ± 0.6 | 17.8 ± 0.4 | 0 |
| t214-400-arcos-memo | 30 | 52 % ± 7 | 48 % ± 7 | 41.2 ± 7.2 | 37.5 ± 7.1 | 136 ± 8 | 4511 ± 105 | 4458 ± 107 | 70 % ± 1 | 46.3 ± 0.6 | 19.2 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t214-400-arcos semilla 17800: sprint → red (0/49 vivos), normal → red (0/75 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 42 de 42 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t214-400-arcos: enemy:arrow=4571
- t214-400-arcos-memo: enemy:arrow=4763

## Motivos de decisión (cambios registrados): ENGAGE 41356, LOW_HEALTH_RETREAT 21453, MORALE_SHAKEN 12359, MORALE_WAVERING 7940, NONE 21250, ORDER_OVERRIDE 1063, OUT_OF_RANGE 45368, RALLIED 34, REFUSE_ATTACK 2411, ROUTED_FLEE 11145, SEEK_ENEMY 540, STUCK 3, UNIT_BROKEN 4405

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
