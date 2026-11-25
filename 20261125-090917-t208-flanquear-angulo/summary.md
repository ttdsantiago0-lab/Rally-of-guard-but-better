# Simulación Rally - lote 20261125-090917-t208-flanquear-angulo

Preset `t208-flanquear-angulo` · velocidad `sprint` · 41 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t208-fl-b8 | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 63 ± 0 | 216 ± 1 | 5 ± 1 | 91 % ± 1 | 76.9 ± 0.2 | 3.7 ± 0.1 | 0 |
| t208-fl-b100 | 20 | 100 % ± 0 | 0 % ± 0 | 100.0 ± 0.0 | 0.0 ± 0.0 | 59 ± 0 | 216 ± 1 | 0 ± 0 | 66 % ± 2 | 85.7 ± 0.3 | 7.4 ± 0.0 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t208-fl-b8 semilla 17400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 4432, FORMATION_HOLD 2609, INSTRUCTION 7976, OUT_OF_RANGE 3, SEEK_ENEMY 326, TOO_CLOSE 5

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
