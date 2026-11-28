# Simulación Rally - lote 20261128-015258-t224-ac-muestra

Preset `t224-ac-muestra` · velocidad `sprint` · 17 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t224-ac-muestra | 1 | 100 % ± 0 | 0 % ± 0 | 42.0 ± 0.0 | 0.0 ± 0.0 | 55 ± 0 | 2345 ± 0 | 1634 ± 0 | 59 % ± 0 | 39.5 ± 0.0 | 12.9 ± 0.0 | 14 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t224-ac-muestra semilla 10700: sprint → draw (33/2 vivos), normal → draw (18/31 vivos)
- t224-ac-muestra semilla 18477: sprint → draw (38/13 vivos), normal → draw (34/21 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 1 de 1 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t224-ac-muestra: enemy:arrow=55 enemy:mob=3

## Motivos de decisión (cambios registrados): ENGAGE 6560, LOW_HEALTH_RETREAT 1774, MORALE_SHAKEN 3013, MORALE_WAVERING 1261, NONE 585, ORDER_OVERRIDE 782, OUT_OF_RANGE 2076, REFUSE_ATTACK 459, ROUTED_FLEE 1580, SEEK_ENEMY 257, UNIT_BROKEN 573

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t224-ac-muestra - 14 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
