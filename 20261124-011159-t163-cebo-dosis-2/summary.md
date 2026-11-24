# Simulación Rally - lote 20261124-011159-t163-cebo-dosis-2

Preset `t163-cebo-dosis-2` · velocidad `sprint` · 151 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t163-cebo-dosis-2-once | 30 | 100 % ± 0 | 0 % ± 0 | 4.7 ± 0.2 | 0.0 ± 0.0 | 15 ± 0 | 431 ± 2 | 102 ± 3 | 91 % ± 1 | 8.6 ± 0.2 | 6.6 ± 0.2 | 0 |
| t163-cebo-dosis-2-cebo-1 | 30 | 100 % ± 0 | 0 % ± 0 | 5.8 ± 0.2 | 0.0 ± 0.0 | 15 ± 0 | 430 ± 2 | 82 ± 5 | 92 % ± 1 | 9.0 ± 0.1 | 7.4 ± 0.2 | 0 |
| t163-cebo-dosis-2-cebo-2 | 30 | 100 % ± 0 | 0 % ± 0 | 5.8 ± 0.3 | 0.0 ± 0.0 | 17 ± 2 | 429 ± 2 | 82 ± 6 | 90 % ± 1 | 9.9 ± 0.9 | 8.6 ± 0.7 | 0 |
| t163-cebo-dosis-2-cebo-4 | 30 | 100 % ± 0 | 0 % ± 0 | 5.8 ± 0.2 | 0.0 ± 0.0 | 17 ± 2 | 435 ± 2 | 82 ± 5 | 93 % ± 1 | 10.3 ± 0.8 | 8.7 ± 0.9 | 0 |
| t163-cebo-dosis-2-cebo-2u1 | 30 | 100 % ± 0 | 0 % ± 0 | 5.9 ± 0.2 | 0.0 ± 0.0 | 16 ± 1 | 432 ± 1 | 83 ± 5 | 91 % ± 1 | 9.7 ± 0.5 | 7.9 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t163-cebo-dosis-2-once semilla 14200: sprint → blue (4/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 150 de 150 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t163-cebo-dosis-2-once: enemy:mob=99
- t163-cebo-dosis-2-cebo-1: enemy:mob=66
- t163-cebo-dosis-2-cebo-2: enemy:mob=66
- t163-cebo-dosis-2-cebo-4: enemy:mob=65
- t163-cebo-dosis-2-cebo-2u1: enemy:mob=62

## Motivos de decisión (cambios registrados): ENGAGE 1220, FORMATION_HOLD 8, INSTRUCTION 10, LOW_HEALTH_RETREAT 114, ORDER_OVERRIDE 9, OUT_OF_RANGE 1, SEEK_ENEMY 43, TOO_CLOSE 14, UNIT_BROKEN 24

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
