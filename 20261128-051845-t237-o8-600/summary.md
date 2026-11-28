# Simulación Rally - lote 20261128-051845-t237-o8-600

Preset `t237-o8-600` · velocidad `sprint` · 31 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t237-600-mixto | 15 | 53 % ± 3 | 47 % ± 3 | 63.3 ± 7.4 | 39.6 ± 7.8 | 179 ± 1 | 6660 ± 146 | 6262 ± 148 | 67 % ± 1 | 41.3 ± 0.8 | 21.8 ± 0.5 | 0 |
| t237-600-mixto-sync | 15 | 40 % ± 5 | 60 % ± 5 | 28.5 ± 6.9 | 83.7 ± 7.1 | 173 ± 5 | 5830 ± 162 | 6819 ± 106 | 67 % ± 1 | 37.7 ± 0.6 | 20.0 ± 0.7 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t237-600-mixto semilla 23300: sprint → draw (18/29 vivos), normal → draw (58/77 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 27 de 27 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t237-600-mixto: enemy:arrow=3317 enemy:mob=233
- t237-600-mixto-sync: enemy:arrow=3719 enemy:mob=353

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 35826, LOW_HEALTH_RETREAT 9103, MORALE_SHAKEN 14436, MORALE_WAVERING 6920, NONE 6651, ORDER_OVERRIDE 1352, OUT_OF_RANGE 16797, RALLIED 16, REFUSE_ATTACK 4287, ROUTED_FLEE 8690, SEEK_ENEMY 1192, STUCK 6, UNIT_BROKEN 3211

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t237-600-mixto: 14/15 corridas llegaron al tope de tiempo
- t237-600-mixto-sync: 12/15 corridas llegaron al tope de tiempo
