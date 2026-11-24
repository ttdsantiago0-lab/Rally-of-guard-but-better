# Simulación Rally - lote 20261124-064725-t185-closer

Preset `t185-closer` · velocidad `sprint` · 232 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t185-arcos-yield | 4 | 75 % ± 25 | 25 % ± 25 | 89.3 ± 29.9 | 20.3 ± 20.3 | 57 ± 3 | 4534 ± 263 | 3480 ± 466 | 63 % ± 5 | 45.5 ± 1.7 | 17.4 ± 0.9 | 54 |
| t185-arcos-base | 14 | 93 % ± 7 | 7 % ± 7 | 110.5 ± 8.9 | 6.4 ± 6.4 | 57 ± 1 | 4726 ± 83 | 3267 ± 135 | 60 % ± 2 | 47.2 ± 0.5 | 18.2 ± 0.3 | 38 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t185-arcos-base semilla 15500: sprint → draw (82/9 vivos), normal → draw (84/28 vivos)
- t185-arcos-base semilla 23277: sprint → draw (113/7 vivos), normal → draw (73/31 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 16 de 16 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t185-arcos-yield: enemy:arrow=443
- t185-arcos-base: enemy:arrow=1253

## Motivos de decisión (cambios registrados): ENGAGE 162260, LOW_HEALTH_RETREAT 68827, MORALE_SHAKEN 60402, MORALE_WAVERING 30801, NONE 54582, ORDER_OVERRIDE 22786, OUT_OF_RANGE 124227, RALLIED 74, REFUSE_ATTACK 11356, ROUTED_FLEE 42005, SEEK_ENEMY 5999, STUCK 14, TOO_CLOSE 10, UNIT_BROKEN 16405

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t185-arcos-base - 38 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t185-arcos-yield - 54 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t185-mixto-base - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t185-mixto-yield - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
