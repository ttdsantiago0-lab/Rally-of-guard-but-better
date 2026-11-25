# Simulación Rally - lote 20261125-070516-t200-mantener-escala

Preset `t200-mantener-escala` · velocidad `sprint` · 81 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t200-man-b8 | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 9 ± 0 | 219 ± 2 | 2 ± 1 | 94 % ± 1 | 11.7 ± 0.3 | 5.8 ± 0.1 | 0 |
| t200-man-b25 | 20 | 100 % ± 0 | 0 % ± 0 | 24.6 ± 0.2 | 0.0 ± 0.0 | 11 ± 0 | 675 ± 2 | 46 ± 6 | 88 % ± 1 | 10.2 ± 0.2 | 7.7 ± 0.1 | 0 |
| t200-man-b50 | 20 | 100 % ± 0 | 0 % ± 0 | 49.0 ± 0.2 | 0.0 ± 0.0 | 12 ± 0 | 1343 ± 4 | 113 ± 9 | 88 % ± 1 | 10.8 ± 0.1 | 8.9 ± 0.1 | 0 |
| t200-man-b100 | 20 | 100 % ± 0 | 0 % ± 0 | 95.6 ± 0.5 | 0.0 ± 0.0 | 15 ± 0 | 2682 ± 4 | 329 ± 15 | 86 % ± 1 | 10.9 ± 0.1 | 9.4 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t200-man-b8 semilla 17000: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 80 de 80 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t200-man-b25: enemy:mob=9
- t200-man-b50: enemy:mob=21
- t200-man-b100: enemy:mob=89

## Motivos de decisión (cambios registrados): ENGAGE 3688, FORMATION_HOLD 1709, INSTRUCTION 3668, LOW_HEALTH_RETREAT 53, OUT_OF_RANGE 47, SEEK_ENEMY 1860, TOO_CLOSE 16

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
