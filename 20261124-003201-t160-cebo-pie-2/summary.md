# Simulación Rally - lote 20261124-003201-t160-cebo-pie-2

Preset `t160-cebo-pie-2` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t160-cebo-pie-2-once | 30 | 100 % ± 0 | 0 % ± 0 | 4.6 ± 0.2 | 0.0 ± 0.0 | 15 ± 0 | 430 ± 2 | 103 ± 3 | 92 % ± 1 | 8.7 ± 0.1 | 6.7 ± 0.3 | 0 |
| t160-cebo-pie-2-w15 | 30 | 100 % ± 0 | 0 % ± 0 | 6.7 ± 0.2 | 0.0 ± 0.0 | 25 ± 0 | 431 ± 2 | 67 ± 4 | 86 % ± 1 | 9.4 ± 0.1 | 7.8 ± 0.1 | 0 |
| t160-cebo-pie-2-cebo-pie | 30 | 100 % ± 0 | 0 % ± 0 | 5.9 ± 0.3 | 0.0 ± 0.0 | 17 ± 2 | 433 ± 1 | 80 ± 5 | 92 % ± 1 | 10.2 ± 0.9 | 8.6 ± 0.6 | 0 |
| t160-cebo-pie-2-pie-sin | 30 | 100 % ± 0 | 0 % ± 0 | 4.6 ± 0.2 | 0.0 ± 0.0 | 16 ± 0 | 433 ± 2 | 106 ± 2 | 90 % ± 1 | 8.9 ± 0.2 | 7.1 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t160-cebo-pie-2-once semilla 13900: sprint → blue (7/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t160-cebo-pie-2-once: enemy:mob=101
- t160-cebo-pie-2-w15: enemy:mob=39
- t160-cebo-pie-2-cebo-pie: enemy:mob=64
- t160-cebo-pie-2-pie-sin: enemy:mob=101

## Motivos de decisión (cambios registrados): ENGAGE 1188, FORMATION_HOLD 4, LOW_HEALTH_RETREAT 102, NONE 214, SEEK_ENEMY 32, TOO_CLOSE 7, UNIT_BROKEN 8

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
