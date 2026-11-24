# Simulación Rally - lote 20261124-044441-t176-closer

Preset `t176-closer` · velocidad `sprint` · 42 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t176-arcos-reuse | 2 | 100 % ± 0 | 0 % ± 0 | 121.0 ± 8.0 | 0.0 ± 0.0 | 56 ± 3 | 4792 ± 6 | 3067 ± 261 | 57 % ± 4 | 46.5 ± 0.8 | 18.2 ± 0.1 | 18 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t176-arcos-reuse semilla 15500: sprint → draw (1/80 vivos), normal → draw (58/49 vivos)
- t176-arcos-reuse semilla 23277: sprint → draw (24/67 vivos), normal → draw (51/70 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 2 de 2 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t176-arcos-reuse: enemy:arrow=158

## Motivos de decisión (cambios registrados): ENGAGE 29252, LOW_HEALTH_RETREAT 12820, MORALE_SHAKEN 10593, MORALE_WAVERING 5520, NONE 10269, ORDER_OVERRIDE 4421, OUT_OF_RANGE 22976, RALLIED 17, REFUSE_ATTACK 1888, ROUTED_FLEE 7548, SEEK_ENEMY 1037, UNIT_BROKEN 2915

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t176-arcos-reuse - 18 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t176-mixto-reuse - 20 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
