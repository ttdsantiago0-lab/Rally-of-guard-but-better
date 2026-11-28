# Simulación Rally - lote 20261128-060004-t242-o8-borde

Preset `t242-o8-borde` · velocidad `sprint` · 31 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t242-700-mixto | 15 | 60 % ± 5 | 40 % ± 5 | 89.7 ± 6.7 | 26.8 ± 6.8 | 176 ± 2 | 8187 ± 113 | 7137 ± 131 | 67 % ± 1 | 42.7 ± 0.9 | 23.1 ± 0.3 | 0 |
| t242-800-mixto | 15 | 50 % ± 0 | 50 % ± 0 | 60.9 ± 9.8 | 71.3 ± 10.5 | 180 ± 0 | 8645 ± 205 | 8881 ± 161 | 68 % ± 1 | 39.2 ± 0.8 | 24.2 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t242-700-mixto semilla 23800: sprint → blue (74/0 vivos), normal → draw (36/66 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 30 de 30 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t242-700-mixto: enemy:arrow=3725 enemy:mob=180
- t242-800-mixto: enemy:arrow=4830 enemy:mob=257

## Motivos de decisión (cambios registrados): ENGAGE 42411, LOW_HEALTH_RETREAT 13424, MORALE_SHAKEN 16591, MORALE_WAVERING 8179, NONE 9213, ORDER_OVERRIDE 1414, OUT_OF_RANGE 20726, RALLIED 22, REFUSE_ATTACK 4120, ROUTED_FLEE 10446, SEEK_ENEMY 2586, STUCK 3, TOO_CLOSE 5, UNIT_BROKEN 3894

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t242-700-mixto: 12/15 corridas llegaron al tope de tiempo
- t242-800-mixto: 15/15 corridas llegaron al tope de tiempo
- t242-700-mixto: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
