# Simulación Rally - lote 20261125-124517-t216-o8-rgpartes

Preset `t216-o8-rgpartes` · velocidad `sprint` · 31 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t216-400-arcos | 30 | 68 % ± 8 | 32 % ± 8 | 64.3 ± 8.2 | 23.6 ± 7.4 | 114 ± 8 | 4632 ± 101 | 4036 ± 128 | 66 % ± 1 | 47.8 ± 0.6 | 18.3 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t216-400-arcos semilla 17800: sprint → red (0/90 vivos), normal → red (0/102 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 22 de 22 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t216-400-arcos: enemy:arrow=4072

## Motivos de decisión (cambios registrados): ENGAGE 22024, LOW_HEALTH_RETREAT 10833, MORALE_SHAKEN 6424, MORALE_WAVERING 4075, NONE 11754, ORDER_OVERRIDE 442, OUT_OF_RANGE 24615, RALLIED 32, REFUSE_ATTACK 1375, ROUTED_FLEE 5535, SEEK_ENEMY 229, UNIT_BROKEN 2087

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
