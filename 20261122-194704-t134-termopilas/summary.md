# Simulación Rally - lote 20261122-194704-t134-termopilas

Preset `t134-termopilas` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t134-termopilas-flat-16 | 30 | 100 % ± 0 | 0 % ± 0 | 4.8 ± 0.2 | 0.0 ± 0.0 | 15 ± 0 | 430 ± 2 | 101 ± 3 | 91 % ± 1 | 8.7 ± 0.1 | 6.8 ± 0.2 | 0 |
| t134-termopilas-corr-16 | 30 | 82 % ± 6 | 18 % ± 6 | 3.9 ± 0.4 | 1.0 ± 0.4 | 61 ± 11 | 407 ± 9 | 154 ± 9 | 76 % ± 1 | 9.5 ± 0.2 | 4.4 ± 0.5 | 0 |
| t134-termopilas-flat-32 | 30 | 100 % ± 0 | 0 % ± 0 | 3.9 ± 0.0 | 0.0 ± 0.0 | 33 ± 1 | 876 ± 3 | 107 ± 1 | 90 % ± 1 | 10.6 ± 0.5 | 12.9 ± 0.5 | 0 |
| t134-termopilas-corr-32 | 30 | 33 % ± 7 | 67 % ± 7 | 0.8 ± 0.2 | 13.6 ± 2.3 | 90 ± 13 | 571 ± 54 | 201 ± 5 | 80 % ± 2 | 11.8 ± 0.7 | 5.5 ± 1.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t134-termopilas-flat-16 semilla 12600: sprint → blue (6/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 101 de 101 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t134-termopilas-flat-16: enemy:mob=97
- t134-termopilas-corr-16: enemy:mob=124
- t134-termopilas-flat-32: enemy:mob=122
- t134-termopilas-corr-32: enemy:mob=217

## Motivos de decisión (cambios registrados): ENGAGE 1194, LOW_HEALTH_RETREAT 133, NONE 3, OUT_OF_RANGE 57, SEEK_ENEMY 29, TOO_CLOSE 295

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
