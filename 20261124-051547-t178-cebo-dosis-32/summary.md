# Simulación Rally - lote 20261124-051547-t178-cebo-dosis-32

Preset `t178-cebo-dosis-32` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t178-cebo-dosis-32-once-32 | 30 | 100 % ± 0 | 0 % ± 0 | 4.0 ± 0.0 | 0.0 ± 0.0 | 33 ± 0 | 878 ± 2 | 105 ± 1 | 91 % ± 1 | 11.0 ± 0.5 | 13.2 ± 0.6 | 0 |
| t178-cebo-dosis-32-cebo1-32 | 30 | 97 % ± 2 | 3 % ± 2 | 3.6 ± 0.1 | 0.1 ± 0.0 | 90 ± 7 | 876 ± 3 | 126 ± 4 | 89 % ± 1 | 24.3 ± 2.0 | 23.5 ± 1.3 | 0 |
| t178-cebo-dosis-32-cebo2-32 | 30 | 88 % ± 6 | 12 % ± 6 | 3.2 ± 0.2 | 1.9 ± 1.0 | 81 ± 6 | 852 ± 15 | 141 ± 7 | 91 % ± 1 | 22.5 ± 1.7 | 21.5 ± 1.2 | 0 |
| t178-cebo-dosis-32-cebo4-32 | 30 | 100 % ± 0 | 0 % ± 0 | 3.8 ± 0.1 | 0.0 ± 0.0 | 90 ± 5 | 876 ± 3 | 123 ± 3 | 90 % ± 1 | 26.8 ± 1.8 | 23.1 ± 0.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t178-cebo-dosis-32-once-32 semilla 16200: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 117 de 117 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t178-cebo-dosis-32-once-32: enemy:mob=120
- t178-cebo-dosis-32-cebo1-32: enemy:mob=132
- t178-cebo-dosis-32-cebo2-32: enemy:mob=143
- t178-cebo-dosis-32-cebo4-32: enemy:mob=126

## Motivos de decisión (cambios registrados): ENGAGE 1527, FORMATION_HOLD 530, INSTRUCTION 214, LOW_HEALTH_RETREAT 75, NONE 2, ORDER_OVERRIDE 185, OUT_OF_RANGE 192, SEEK_ENEMY 665, TOO_CLOSE 54, UNIT_BROKEN 19

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
