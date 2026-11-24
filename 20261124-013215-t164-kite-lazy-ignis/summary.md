# Simulación Rally - lote 20261124-013215-t164-kite-lazy-ignis

Preset `t164-kite-lazy-ignis` · velocidad `sprint` · 21 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t164-ignis | 10 | 100 % ± 0 | 0 % ± 0 | 6.4 ± 0.4 | 0.0 ± 0.0 | 81 ± 3 | 499 ± 9 | 128 ± 12 | 89 % ± 1 | 18.4 ± 0.3 | 14.5 ± 0.6 | 0 |
| t164-ignis-lazy | 10 | 100 % ± 0 | 0 % ± 0 | 6.6 ± 0.3 | 0.0 ± 0.0 | 84 ± 2 | 484 ± 7 | 129 ± 9 | 89 % ± 1 | 18.3 ± 0.1 | 14.0 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t164-ignis semilla 14300: sprint → blue (7/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 20 de 20 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t164-ignis: enemy:cataclysm.flame_strike=11 enemy:mob=5
- t164-ignis-lazy: enemy:cataclysm.flame_strike=11 enemy:mob=3

## Motivos de decisión (cambios registrados): ENGAGE 275, FORMATION_HOLD 1, LOW_HEALTH_RETREAT 67, NONE 4, OUT_OF_RANGE 11, SEEK_ENEMY 31, STUCK 11, TOO_CLOSE 74

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
