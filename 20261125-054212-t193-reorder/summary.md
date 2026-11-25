# Simulación Rally - lote 20261125-054212-t193-reorder

Preset `t193-reorder` · velocidad `sprint` · 41 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t193-split-ctl | 20 | 53 % ± 2 | 48 % ± 2 | 171.1 ± 4.9 | 29.4 ± 5.0 | 174 ± 6 | 7189 ± 76 | 6107 ± 131 | 59 % ± 1 | 42.5 ± 0.3 | 20.3 ± 0.2 | 0 |
| t193-split-reorder | 20 | 80 % ± 6 | 20 % ± 6 | 158.4 ± 8.6 | 6.7 ± 4.9 | 150 ± 8 | 7647 ± 74 | 6463 ± 207 | 60 % ± 1 | 49.1 ± 1.1 | 24.9 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t193-split-ctl semilla 16000: sprint → blue (161/0 vivos), normal → draw (186/32 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t193-split-ctl: enemy:arrow=4312 enemy:mob=267
- t193-split-reorder: enemy:arrow=4579 enemy:mob=254

## Motivos de decisión (cambios registrados): COUNTERATTACK 2, ENGAGE 59488, LOW_HEALTH_RETREAT 16199, MORALE_SHAKEN 21299, MORALE_WAVERING 10697, NONE 14369, ORDER_OVERRIDE 4971, OUT_OF_RANGE 29684, RALLIED 34, REFUSE_ATTACK 5246, ROUTED_FLEE 13876, SEEK_ENEMY 3781, STUCK 19, TOO_CLOSE 5, UNIT_BROKEN 4690

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t193-split-ctl: 19/20 corridas llegaron al tope de tiempo
- t193-split-ctl: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
