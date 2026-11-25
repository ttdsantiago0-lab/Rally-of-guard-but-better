# Simulación Rally - lote 20261125-090922-t209-o8-hoy

Preset `t209-o8-hoy` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t209-400-mixto | 30 | 50 % ± 5 | 50 % ± 5 | 27.3 ± 4.5 | 33.1 ± 4.5 | 163 ± 6 | 4371 ± 80 | 4507 ± 78 | 70 % ± 1 | 39.2 ± 0.7 | 17.3 ± 0.4 | 0 |
| t209-400-arcos | 30 | 62 % ± 7 | 38 % ± 7 | 50.8 ± 7.0 | 26.4 ± 6.1 | 135 ± 8 | 4651 ± 82 | 4300 ± 106 | 68 % ± 1 | 47.3 ± 0.7 | 18.4 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t209-400-mixto semilla 17800: sprint → blue (62/0 vivos), normal → blue (69/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 49 de 49 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t209-400-mixto: enemy:arrow=4945 enemy:mob=236
- t209-400-arcos: enemy:arrow=4477

## Motivos de decisión (cambios registrados): ENGAGE 42123, LOW_HEALTH_RETREAT 18424, MORALE_SHAKEN 15744, MORALE_WAVERING 7933, NONE 13759, ORDER_OVERRIDE 1338, OUT_OF_RANGE 31664, RALLIED 25, REFUSE_ATTACK 3011, ROUTED_FLEE 10815, SEEK_ENEMY 1650, STUCK 1, TOO_CLOSE 6, UNIT_BROKEN 4412

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t209-400-mixto: 20/30 corridas llegaron al tope de tiempo
