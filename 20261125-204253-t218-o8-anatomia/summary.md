# Simulación Rally - lote 20261125-204253-t218-o8-anatomia

Preset `t218-o8-anatomia` · velocidad `sprint` · 31 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t218-400-arcos | 30 | 77 % ± 6 | 23 % ± 6 | 68.8 ± 7.3 | 17.6 ± 5.6 | 120 ± 8 | 4710 ± 70 | 4021 ± 115 | 66 % ± 1 | 48.5 ± 0.7 | 18.7 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t218-400-arcos semilla 17800: sprint → blue (100/0 vivos), normal → red (0/109 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 27 de 27 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t218-400-arcos: enemy:arrow=3937

## Motivos de decisión (cambios registrados): ENGAGE 20848, LOW_HEALTH_RETREAT 10825, MORALE_SHAKEN 6125, MORALE_WAVERING 3971, NONE 10963, ORDER_OVERRIDE 565, OUT_OF_RANGE 23399, RALLIED 12, REFUSE_ATTACK 1218, ROUTED_FLEE 5549, SEEK_ENEMY 260, UNIT_BROKEN 2144

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t218-400-arcos: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
