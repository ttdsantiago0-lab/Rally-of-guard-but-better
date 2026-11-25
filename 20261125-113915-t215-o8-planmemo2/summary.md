# Simulación Rally - lote 20261125-113915-t215-o8-planmemo2

Preset `t215-o8-planmemo2` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t215-400-arcos-nomemo | 30 | 50 % ± 7 | 50 % ± 7 | 43.8 ± 7.4 | 34.1 ± 6.9 | 135 ± 9 | 4550 ± 99 | 4418 ± 112 | 67 % ± 1 | 46.3 ± 0.6 | 18.4 ± 0.4 | 0 |
| t215-400-arcos | 30 | 65 % ± 8 | 35 % ± 8 | 58.8 ± 8.5 | 27.7 ± 6.7 | 110 ± 8 | 4557 ± 90 | 4121 ± 135 | 66 % ± 1 | 46.3 ± 0.6 | 18.3 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t215-400-arcos-nomemo semilla 18400: sprint → draw (26/13 vivos), normal → draw (44/5 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 44 de 44 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t215-400-arcos-nomemo: enemy:arrow=4684 other(minecraft:arrow):arrow=1
- t215-400-arcos: enemy:arrow=4236

## Motivos de decisión (cambios registrados): ENGAGE 41489, LOW_HEALTH_RETREAT 21901, MORALE_SHAKEN 12388, MORALE_WAVERING 7861, NONE 22335, ORDER_OVERRIDE 991, OUT_OF_RANGE 46481, RALLIED 33, REFUSE_ATTACK 2276, ROUTED_FLEE 10901, SEEK_ENEMY 550, STUCK 1, UNIT_BROKEN 4251

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
