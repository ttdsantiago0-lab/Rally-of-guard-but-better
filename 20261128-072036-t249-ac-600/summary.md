# Simulación Rally - lote 20261128-072036-t249-ac-600

Preset `t249-ac-600` · velocidad `sprint` · 17 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t249-600-ac | 8 | 44 % ± 6 | 56 % ± 6 | 28.4 ± 10.0 | 77.5 ± 7.4 | 179 ± 1 | 5959 ± 200 | 6872 ± 184 | 66 % ± 1 | 37.9 ± 0.7 | 22.9 ± 0.8 | 0 |
| t249-600-ac-nodrop | 8 | 50 % ± 9 | 50 % ± 9 | 50.0 ± 14.1 | 52.6 ± 13.7 | 174 ± 4 | 6473 ± 248 | 6458 ± 261 | 68 % ± 2 | 40.6 ± 1.5 | 21.0 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t249-600-ac semilla 24900: sprint → draw (2/65 vivos), normal → draw (105/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 14 de 14 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t249-600-ac: enemy:arrow=2030 enemy:mob=143
- t249-600-ac-nodrop: enemy:arrow=1859 enemy:mob=141

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 20083, LOW_HEALTH_RETREAT 5237, MORALE_SHAKEN 8022, MORALE_WAVERING 3792, NONE 3721, ORDER_OVERRIDE 724, OUT_OF_RANGE 9496, RALLIED 7, REFUSE_ATTACK 2213, ROUTED_FLEE 4685, SEEK_ENEMY 656, STUCK 1, UNIT_BROKEN 1713

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t249-600-ac: 7/8 corridas llegaron al tope de tiempo
- t249-600-ac-nodrop: 6/8 corridas llegaron al tope de tiempo
