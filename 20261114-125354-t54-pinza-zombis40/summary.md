# Simulación Rally - lote 20261114-125354-t54-pinza-zombis40

Preset `t54-pinza-zombis40` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t54-pinza-zombis40-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 15 ± 0 | 954 ± 2 | 10 ± 1 | 96 % ± 0 | 7.4 ± 0.0 | 9.7 ± 0.1 | 0 |
| t54-pinza-zombis40-linea-mantener | 30 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 16 ± 0 | 957 ± 2 | 6 ± 1 | 96 % ± 0 | 9.9 ± 0.1 | 9.6 ± 0.1 | 0 |
| t54-pinza-zombis40-linea-pinza | 30 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 16 ± 0 | 958 ± 3 | 7 ± 1 | 96 % ± 0 | 9.8 ± 0.1 | 9.8 ± 0.1 | 0 |
| t54-pinza-zombis40-cab-base | 30 | 100 % ± 0 | 0 % ± 0 | 15.7 ± 0.2 | 0.0 ± 0.0 | 17 ± 0 | 957 ± 4 | 61 ± 5 | 90 % ± 1 | 12.3 ± 0.2 | 12.8 ± 0.1 | 0 |
| t54-pinza-zombis40-cab-mantener | 30 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 18 ± 0 | 957 ± 2 | 25 ± 2 | 92 % ± 0 | 15.0 ± 0.1 | 11.4 ± 0.1 | 0 |
| t54-pinza-zombis40-cab-pinza | 30 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 20 ± 0 | 954 ± 2 | 26 ± 2 | 89 % ± 0 | 16.3 ± 0.4 | 12.0 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t54-pinza-zombis40-linea-base semilla 5401: sprint → blue (16/0 vivos), normal → blue (16/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 180 de 180 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t54-pinza-zombis40-cab-base: enemy:mob=4

## Motivos de decisión (cambios registrados): ENGAGE 2956, FORMATION_HOLD 422, INSTRUCTION 1920, LOW_HEALTH_RETREAT 51, NONE 4, OUT_OF_RANGE 41, SEEK_ENEMY 614, TOO_CLOSE 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
