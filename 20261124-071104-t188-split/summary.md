# Simulación Rally - lote 20261124-071104-t188-split

Preset `t188-split` · velocidad `sprint` · 21 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t188-split | 20 | 55 % ± 3 | 45 % ± 3 | 162.1 ± 4.4 | 30.3 ± 5.2 | 168 ± 8 | 7173 ± 75 | 6340 ± 116 | 60 % ± 1 | 42.7 ± 0.3 | 20.6 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t188-split semilla 16000: sprint → blue (155/0 vivos), normal → draw (182/12 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 20 de 20 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t188-split: enemy:arrow=4486 enemy:mob=272

## Motivos de decisión (cambios registrados): ENGAGE 30419, LOW_HEALTH_RETREAT 8623, MORALE_SHAKEN 10834, MORALE_WAVERING 5287, NONE 7492, ORDER_OVERRIDE 3888, OUT_OF_RANGE 16154, RALLIED 16, REFUSE_ATTACK 2539, ROUTED_FLEE 6673, SEEK_ENEMY 1952, STUCK 7, TOO_CLOSE 10, UNIT_BROKEN 2208

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t188-split: 18/20 corridas llegaron al tope de tiempo
- t188-split: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
