# Simulación Rally - lote 20261125-075559-t203-mantener-tarde

Preset `t203-mantener-tarde` · velocidad `sprint` · 81 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t203-man-b100r8-d30 | 20 | 100 % ± 0 | 0 % ± 0 | 100.0 ± 0.0 | 0.0 ± 0.0 | 5 ± 0 | 213 ± 1 | 0 ± 0 | 56 % ± 2 | 28.4 ± 0.4 | 9.3 ± 0.0 | 0 |
| t203-man-b100r8-d100 | 20 | 100 % ± 0 | 0 % ± 0 | 100.0 ± 0.0 | 0.0 ± 0.0 | 20 ± 0 | 215 ± 1 | 0 ± 0 | 65 % ± 2 | 58.2 ± 0.2 | 7.6 ± 0.1 | 0 |
| t203-man-b8r8-d100 | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.1 | 0.0 ± 0.0 | 24 ± 0 | 214 ± 1 | 5 ± 2 | 93 % ± 1 | 44.3 ± 0.4 | 4.6 ± 0.1 | 0 |
| t203-man-b100r100-d30 | 20 | 100 % ± 0 | 0 % ± 0 | 94.7 ± 0.4 | 0.0 ± 0.0 | 15 ± 0 | 2673 ± 5 | 380 ± 18 | 88 % ± 1 | 10.7 ± 0.1 | 9.5 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t203-man-b100r8-d30 semilla 17000: sprint → blue (100/0 vivos), normal → blue (100/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 80 de 80 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t203-man-b8r8-d100: enemy:mob=1
- t203-man-b100r100-d30: enemy:mob=107

## Motivos de decisión (cambios registrados): ENGAGE 4222, FORMATION_HOLD 4205, INSTRUCTION 6260, LOW_HEALTH_RETREAT 50, OUT_OF_RANGE 164, SEEK_ENEMY 1292, TOO_CLOSE 10

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
