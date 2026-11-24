# Simulación Rally - lote 20261124-014112-t165-cebo-duro

Preset `t165-cebo-duro` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t165-cebo-duro-once-16 | 30 | 100 % ± 0 | 0 % ± 0 | 4.3 ± 0.1 | 0.0 ± 0.0 | 16 ± 0 | 438 ± 2 | 108 ± 2 | 92 % ± 1 | 8.6 ± 0.2 | 6.4 ± 0.2 | 0 |
| t165-cebo-duro-cebo1-16 | 30 | 100 % ± 0 | 0 % ± 0 | 5.8 ± 0.2 | 0.0 ± 0.0 | 15 ± 0 | 431 ± 2 | 84 ± 4 | 91 % ± 1 | 9.2 ± 0.1 | 7.4 ± 0.2 | 0 |
| t165-cebo-duro-once-24 | 30 | 100 % ± 0 | 0 % ± 0 | 4.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 0 | 653 ± 2 | 107 ± 1 | 92 % ± 1 | 9.6 ± 0.4 | 9.6 ± 0.5 | 0 |
| t165-cebo-duro-cebo1-24 | 30 | 100 % ± 0 | 0 % ± 0 | 3.9 ± 0.0 | 0.0 ± 0.0 | 30 ± 3 | 658 ± 2 | 120 ± 2 | 92 % ± 1 | 12.4 ± 1.4 | 10.3 ± 0.9 | 0 |
| t165-cebo-duro-once-32 | 30 | 100 % ± 0 | 0 % ± 0 | 3.9 ± 0.0 | 0.0 ± 0.0 | 33 ± 1 | 875 ± 3 | 107 ± 1 | 89 % ± 1 | 11.2 ± 1.0 | 11.6 ± 0.6 | 0 |
| t165-cebo-duro-cebo1-32 | 30 | 97 % ± 3 | 3 % ± 3 | 3.5 ± 0.2 | 0.7 ± 0.7 | 89 ± 5 | 867 ± 11 | 132 ± 5 | 90 % ± 1 | 23.7 ± 1.5 | 23.6 ± 1.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t165-cebo-duro-once-16 semilla 14400: sprint → blue (4/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 179 de 179 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t165-cebo-duro-once-16: enemy:mob=110
- t165-cebo-duro-cebo1-16: enemy:mob=65
- t165-cebo-duro-once-24: enemy:mob=120
- t165-cebo-duro-cebo1-24: enemy:mob=122
- t165-cebo-duro-once-32: enemy:mob=122
- t165-cebo-duro-cebo1-32: enemy:mob=134

## Motivos de decisión (cambios registrados): ENGAGE 1695, FORMATION_HOLD 200, LOW_HEALTH_RETREAT 138, NONE 1, OUT_OF_RANGE 95, SEEK_ENEMY 293, TOO_CLOSE 58, UNIT_BROKEN 10

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
