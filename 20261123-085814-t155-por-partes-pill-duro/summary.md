# Simulación Rally - lote 20261123-085814-t155-por-partes-pill-duro

Preset `t155-por-partes-pill-duro` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t155-por-partes-pill-duro-pill48-once | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 33 ± 0 | 1000 ± 5 | 86 ± 3 | 96 % ± 0 | 7.1 ± 0.1 | 8.5 ± 0.2 | 0 |
| t155-por-partes-pill-duro-pill48-w8 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 37 ± 0 | 1071 ± 5 | 59 ± 1 | 94 % ± 0 | 9.3 ± 0.1 | 8.8 ± 0.2 | 0 |
| t155-por-partes-pill-duro-pill48-w15 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 40 ± 0 | 1136 ± 4 | 54 ± 2 | 91 % ± 1 | 11.0 ± 0.1 | 10.4 ± 0.2 | 0 |
| t155-por-partes-pill-duro-pill64-once | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 36 ± 0 | 1153 ± 7 | 114 ± 3 | 96 % ± 0 | 6.3 ± 0.1 | 8.6 ± 0.3 | 0 |
| t155-por-partes-pill-duro-pill64-w8 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 41 ± 0 | 1264 ± 8 | 84 ± 2 | 95 % ± 0 | 8.0 ± 0.2 | 8.3 ± 0.2 | 0 |
| t155-por-partes-pill-duro-pill64-w15 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 45 ± 0 | 1352 ± 5 | 77 ± 2 | 95 % ± 0 | 9.4 ± 0.1 | 9.2 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t155-por-partes-pill-duro-pill48-once semilla 13400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 180 de 180 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 1799, LOW_HEALTH_RETREAT 61, OUT_OF_RANGE 1, SEEK_ENEMY 35, TOO_CLOSE 297

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
