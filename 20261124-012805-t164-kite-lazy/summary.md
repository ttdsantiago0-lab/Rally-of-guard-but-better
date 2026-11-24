# Simulación Rally - lote 20261124-012805-t164-kite-lazy

Preset `t164-kite-lazy` · velocidad `sprint` · 39 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t164-arcos | 2 | 100 % ± 0 | 0 % ± 0 | 113.5 ± 5.5 | 0.0 ± 0.0 | 59 ± 4 | 4778 ± 0 | 3290 ± 179 | 57 % ± 0 | 47.8 ± 1.5 | 18.2 ± 0.6 | 17 |
| t164-arcos-lazy | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 99.0 ± 0.0 | 59 ± 0 | 3528 ± 0 | 4756 ± 0 | 69 % ± 0 | 42.5 ± 0.0 | 15.7 ± 0.0 | 18 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t164-arcos semilla 14300: sprint → blue (108/0 vivos), normal → blue (133/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 2 de 2 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t164-arcos: enemy:arrow=173
- t164-arcos-lazy: enemy:arrow=200

## Motivos de decisión (cambios registrados): ENGAGE 26214, LOW_HEALTH_RETREAT 13998, MORALE_SHAKEN 7910, MORALE_WAVERING 5163, NONE 14514, ORDER_OVERRIDE 4090, OUT_OF_RANGE 29642, RALLIED 15, REFUSE_ATTACK 1500, ROUTED_FLEE 7074, SEEK_ENEMY 411, STUCK 1, UNIT_BROKEN 2582

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t164-arcos-lazy - 18 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t164-arcos - 17 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
