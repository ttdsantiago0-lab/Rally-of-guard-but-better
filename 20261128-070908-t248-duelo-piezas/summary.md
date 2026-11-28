# Simulación Rally - lote 20261128-070908-t248-duelo-piezas

Preset `t248-duelo-piezas` · velocidad `sprint` · 151 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t248-espejo | 30 | 63 % ± 9 | 37 % ± 9 | 4.7 ± 1.0 | 2.8 ± 0.8 | 29 ± 1 | 1467 ± 12 | 1438 ± 15 | 79 % ± 1 | 17.2 ± 0.3 | 10.5 ± 0.2 | 0 |
| t248-todo | 30 | 67 % ± 9 | 33 % ± 9 | 6.0 ± 1.0 | 1.7 ± 0.6 | 30 ± 1 | 1483 ± 8 | 1410 ± 15 | 78 % ± 1 | 18.7 ± 0.2 | 10.3 ± 0.2 | 0 |
| t248-fuego | 30 | 63 % ± 9 | 37 % ± 9 | 6.5 ± 1.1 | 1.9 ± 0.6 | 29 ± 1 | 1472 ± 7 | 1404 ± 15 | 79 % ± 1 | 18.5 ± 0.3 | 10.4 ± 0.2 | 0 |
| t248-blanco | 30 | 53 % ± 9 | 47 % ± 9 | 4.1 ± 0.9 | 3.3 ± 0.9 | 29 ± 1 | 1457 ± 12 | 1450 ± 12 | 79 % ± 1 | 16.9 ± 0.3 | 10.3 ± 0.2 | 0 |
| t248-forma | 30 | 70 % ± 9 | 30 % ± 9 | 7.2 ± 1.1 | 2.1 ± 0.7 | 28 ± 1 | 1480 ± 12 | 1403 ± 17 | 78 % ± 1 | 17.8 ± 0.3 | 10.7 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t248-espejo semilla 24800: sprint → red (0/3 vivos), normal → red (0/3 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 95 de 95 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t248-espejo: enemy:arrow=1675 enemy:mob=103
- t248-todo: enemy:arrow=1665 enemy:mob=74
- t248-fuego: enemy:arrow=1661 enemy:mob=65
- t248-blanco: enemy:arrow=1696 enemy:mob=101
- t248-forma: enemy:arrow=1607 enemy:mob=97

## Motivos de decisión (cambios registrados): ENGAGE 28312, NONE 289, OUT_OF_RANGE 1708, SEEK_ENEMY 4, TOO_CLOSE 8331, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
