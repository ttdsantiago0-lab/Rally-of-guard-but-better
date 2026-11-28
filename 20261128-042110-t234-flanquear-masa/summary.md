# Simulación Rally - lote 20261128-042110-t234-flanquear-masa

Preset `t234-flanquear-masa` · velocidad `sprint` · 41 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t234-fl-r40 | 20 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.1 | 0.0 ± 0.0 | 33 ± 1 | 1071 ± 3 | 53 ± 5 | 89 % ± 1 | 15.1 ± 0.2 | 12.6 ± 0.1 | 0 |
| t234-fl-r56 | 20 | 100 % ± 0 | 0 % ± 0 | 6.7 ± 0.3 | 0.0 ± 0.0 | 43 ± 1 | 1492 ± 4 | 89 ± 5 | 91 % ± 1 | 15.4 ± 0.2 | 12.7 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t234-fl-r40 semilla 17400: sprint → blue (8/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t234-fl-r40: enemy:mob=7
- t234-fl-r56: enemy:mob=27

## Motivos de decisión (cambios registrados): ENGAGE 2245, FORMATION_HOLD 77, INSTRUCTION 2165, LOW_HEALTH_RETREAT 25, SEEK_ENEMY 50, UNIT_BROKEN 4

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
