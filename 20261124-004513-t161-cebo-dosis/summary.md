# Simulación Rally - lote 20261124-004513-t161-cebo-dosis

Preset `t161-cebo-dosis` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t161-cebo-dosis-once | 30 | 100 % ± 0 | 0 % ± 0 | 4.5 ± 0.2 | 0.0 ± 0.0 | 16 ± 0 | 435 ± 2 | 105 ± 3 | 91 % ± 1 | 8.6 ± 0.2 | 6.7 ± 0.2 | 0 |
| t161-cebo-dosis-cebo-1 | 30 | 100 % ± 0 | 0 % ± 0 | 6.0 ± 0.2 | 0.0 ± 0.0 | 15 ± 0 | 428 ± 1 | 77 ± 4 | 93 % ± 1 | 8.8 ± 0.1 | 7.2 ± 0.2 | 0 |
| t161-cebo-dosis-cebo-2 | 30 | 100 % ± 0 | 0 % ± 0 | 5.2 ± 0.2 | 0.0 ± 0.0 | 17 ± 1 | 435 ± 2 | 93 ± 5 | 93 % ± 1 | 10.3 ± 0.6 | 8.4 ± 0.5 | 0 |
| t161-cebo-dosis-cebo-4 | 30 | 100 % ± 0 | 0 % ± 0 | 6.0 ± 0.3 | 0.0 ± 0.0 | 16 ± 1 | 432 ± 2 | 80 ± 6 | 93 % ± 1 | 9.4 ± 0.3 | 7.8 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t161-cebo-dosis-once semilla 14000: sprint → blue (5/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t161-cebo-dosis-once: enemy:mob=104
- t161-cebo-dosis-cebo-1: enemy:mob=60
- t161-cebo-dosis-cebo-2: enemy:mob=85
- t161-cebo-dosis-cebo-4: enemy:mob=61

## Motivos de decisión (cambios registrados): ENGAGE 980, FORMATION_HOLD 5, INSTRUCTION 35, LOW_HEALTH_RETREAT 95, ORDER_OVERRIDE 29, OUT_OF_RANGE 1, SEEK_ENEMY 26, TOO_CLOSE 10, UNIT_BROKEN 22

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
