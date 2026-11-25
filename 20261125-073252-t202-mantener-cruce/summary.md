# Simulación Rally - lote 20261125-073252-t202-mantener-cruce

Preset `t202-mantener-cruce` · velocidad `sprint` · 81 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t202-man-b8r8 | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 9 ± 0 | 216 ± 2 | 2 ± 1 | 93 % ± 1 | 11.8 ± 0.5 | 6.0 ± 0.1 | 0 |
| t202-man-b8r100 | 20 | 48 % ± 2 | 53 % ± 2 | 2.3 ± 0.3 | 64.1 ± 2.7 | 119 ± 1 | 1380 ± 73 | 160 ± 7 | 96 % ± 1 | 9.3 ± 0.6 | 23.4 ± 2.5 | 0 |
| t202-man-b100r8 | 20 | 100 % ± 0 | 0 % ± 0 | 100.0 ± 0.0 | 0.0 ± 0.0 | 5 ± 0 | 212 ± 1 | 0 ± 0 | 54 % ± 1 | 27.5 ± 1.5 | 9.0 ± 0.5 | 0 |
| t202-man-b100r100 | 20 | 100 % ± 0 | 0 % ± 0 | 95.1 ± 0.3 | 0.0 ± 0.0 | 15 ± 0 | 2683 ± 4 | 345 ± 18 | 88 % ± 1 | 11.0 ± 0.1 | 9.4 ± 0.0 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t202-man-b8r8 semilla 17000: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 79 de 79 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t202-man-b8r100: enemy:mob=115
- t202-man-b100r100: enemy:mob=99

## Motivos de decisión (cambios registrados): ENGAGE 3505, FORMATION_HOLD 2076, INSTRUCTION 4328, LOW_HEALTH_RETREAT 54, OUT_OF_RANGE 230, SEEK_ENEMY 1447, TOO_CLOSE 34

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t202-man-b8r100: 19/20 corridas llegaron al tope de tiempo
