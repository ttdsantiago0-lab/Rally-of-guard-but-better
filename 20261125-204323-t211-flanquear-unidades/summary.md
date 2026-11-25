# Simulación Rally - lote 20261125-204323-t211-flanquear-unidades

Preset `t211-flanquear-unidades` · velocidad `sprint` · 41 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t211-fl-b8 | 20 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.1 | 0.0 ± 0.0 | 64 ± 0 | 214 ± 1 | 38 ± 4 | 77 % ± 2 | 76.7 ± 0.2 | 9.9 ± 0.0 | 0 |
| t211-fl-b100 | 20 | 100 % ± 0 | 0 % ± 0 | 100.0 ± 0.0 | 0.0 ± 0.0 | 61 ± 0 | 214 ± 1 | 0 ± 0 | 48 % ± 2 | 89.0 ± 0.3 | 12.7 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t211-fl-b8 semilla 17400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t211-fl-b8: enemy:mob=8

## Motivos de decisión (cambios registrados): ENGAGE 3745, FORMATION_HOLD 2434, INSTRUCTION 7450, LOW_HEALTH_RETREAT 4, OUT_OF_RANGE 78, SEEK_ENEMY 797, STUCK 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
