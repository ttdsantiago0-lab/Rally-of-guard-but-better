# Simulación Rally - lote 20261123-072500-t152-por-partes-pill

Preset `t152-por-partes-pill` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t152-por-partes-pill-once | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 0 | 747 ± 3 | 50 ± 2 | 94 % ± 0 | 7.4 ± 0.1 | 7.7 ± 0.2 | 0 |
| t152-por-partes-pill-w8 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 31 ± 0 | 802 ± 3 | 31 ± 1 | 90 % ± 0 | 10.7 ± 0.1 | 9.1 ± 0.1 | 0 |
| t152-por-partes-pill-w15 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 35 ± 0 | 829 ± 3 | 26 ± 1 | 85 % ± 1 | 13.1 ± 0.1 | 10.2 ± 0.2 | 0 |
| t152-por-partes-pill-w30 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 51 ± 0 | 829 ± 4 | 25 ± 2 | 84 % ± 1 | 15.0 ± 0.3 | 11.4 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t152-por-partes-pill-once semilla 13400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 1422, LOW_HEALTH_RETREAT 1, NONE 375, OUT_OF_RANGE 50, SEEK_ENEMY 55, TOO_CLOSE 43

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
