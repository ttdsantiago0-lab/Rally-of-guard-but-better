# Simulación Rally - lote 20261123-043908-t139-por-partes-calma

Preset `t139-por-partes-calma` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t139-por-partes-calma-w8 | 30 | 100 % ± 0 | 0 % ± 0 | 6.3 ± 0.2 | 0.0 ± 0.0 | 18 ± 0 | 430 ± 2 | 73 ± 5 | 88 % ± 1 | 8.9 ± 0.1 | 6.6 ± 0.1 | 0 |
| t139-por-partes-calma-w8-sincalma | 30 | 100 % ± 0 | 0 % ± 0 | 6.3 ± 0.2 | 0.0 ± 0.0 | 18 ± 0 | 430 ± 2 | 71 ± 4 | 86 % ± 1 | 8.8 ± 0.1 | 6.7 ± 0.1 | 0 |
| t139-por-partes-calma-w12 | 30 | 100 % ± 0 | 0 % ± 0 | 6.1 ± 0.2 | 0.0 ± 0.0 | 23 ± 0 | 430 ± 2 | 74 ± 4 | 87 % ± 1 | 9.3 ± 0.1 | 7.0 ± 0.1 | 0 |
| t139-por-partes-calma-w12-sincalma | 30 | 100 % ± 0 | 0 % ± 0 | 6.0 ± 0.2 | 0.0 ± 0.0 | 23 ± 0 | 437 ± 2 | 82 ± 5 | 86 % ± 1 | 9.1 ± 0.1 | 7.0 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t139-por-partes-calma-w8 semilla 12400: sprint → blue (7/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t139-por-partes-calma-w8: enemy:mob=51
- t139-por-partes-calma-w8-sincalma: enemy:mob=50
- t139-por-partes-calma-w12: enemy:mob=56
- t139-por-partes-calma-w12-sincalma: enemy:mob=60

## Motivos de decisión (cambios registrados): ENGAGE 1555, LOW_HEALTH_RETREAT 77, NONE 574, SEEK_ENEMY 64, TOO_CLOSE 11

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
