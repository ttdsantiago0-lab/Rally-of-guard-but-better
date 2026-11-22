# Simulación Rally - lote 20261122-032201-t114-base400

Preset `t114-base400` · velocidad `sprint` · 60 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t96-400-arcos | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 117.0 ± 0.0 | 57 ± 0 | 3016 ± 0 | 4825 ± 0 | 70 % ± 0 | 41.8 ± 0.0 | 17.8 ± 0.0 | 19 |
| t96-400-melee | 3 | 100 % ± 0 | 0 % ± 0 | 105.0 ± 9.2 | 0.0 ± 0.0 | 49 ± 5 | 4800 ± 0 | 3618 ± 127 | 0 % ± 0 | 17.9 ± 3.6 | 20.1 ± 0.7 | 15 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t96-scale-400 semilla 10500: sprint → draw (28/38 vivos), normal → draw (54/44 vivos)
- t96-scale-400 semilla 18277: sprint → draw (51/59 vivos), normal → draw (39/71 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 3 de 3 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t96-400-arcos: enemy:arrow=200
- t96-400-melee: enemy:mob=285

## Motivos de decisión (cambios registrados): COUNTERATTACK 320, ENGAGE 39279, LOW_HEALTH_RETREAT 21196, MORALE_SHAKEN 17376, MORALE_WAVERING 8247, NONE 11266, ORDER_OVERRIDE 5650, OUT_OF_RANGE 21614, RALLIED 8, REFUSE_ATTACK 2279, ROUTED_FLEE 11254, SEEK_ENEMY 5043, STUCK 25, TOO_CLOSE 6, UNIT_BROKEN 4377

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t96-scale-400 - 20 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t96-400-melee - 15 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t96-400-arcos - 19 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
