# Simulación Rally - lote 20261125-071844-t201-mantener-medida

Preset `t201-mantener-medida` · velocidad `sprint` · 41 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t201-man-b8 | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.1 | 0.0 ± 0.0 | 9 ± 0 | 217 ± 1 | 2 ± 2 | 94 % ± 1 | 12.1 ± 0.3 | 5.8 ± 0.1 | 0 |
| t201-man-b100 | 20 | 100 % ± 0 | 0 % ± 0 | 94.2 ± 0.6 | 0.0 ± 0.0 | 15 ± 0 | 2683 ± 5 | 372 ± 24 | 87 % ± 1 | 10.9 ± 0.1 | 9.4 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t201-man-b8 semilla 17000: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t201-man-b8: enemy:mob=1
- t201-man-b100: enemy:mob=117

## Motivos de decisión (cambios registrados): ENGAGE 2190, FORMATION_HOLD 939, INSTRUCTION 2168, LOW_HEALTH_RETREAT 44, OUT_OF_RANGE 38, SEEK_ENEMY 1234, TOO_CLOSE 12

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
