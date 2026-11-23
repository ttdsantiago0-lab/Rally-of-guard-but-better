# Simulación Rally - lote 20261123-040855-t137-por-partes-corto

Preset `t137-por-partes-corto` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t137-por-partes-corto-once | 30 | 100 % ± 0 | 0 % ± 0 | 4.6 ± 0.2 | 0.0 ± 0.0 | 16 ± 0 | 431 ± 2 | 103 ± 3 | 92 % ± 1 | 8.5 ± 0.2 | 6.8 ± 0.2 | 0 |
| t137-por-partes-corto-w4 | 30 | 100 % ± 0 | 0 % ± 0 | 5.5 ± 0.2 | 0.0 ± 0.0 | 17 ± 0 | 431 ± 2 | 95 ± 4 | 87 % ± 1 | 8.9 ± 0.1 | 6.3 ± 0.1 | 0 |
| t137-por-partes-corto-w8 | 30 | 100 % ± 0 | 0 % ± 0 | 6.1 ± 0.2 | 0.0 ± 0.0 | 18 ± 0 | 430 ± 2 | 71 ± 4 | 88 % ± 1 | 9.0 ± 0.1 | 6.6 ± 0.1 | 0 |
| t137-por-partes-corto-w12 | 30 | 100 % ± 0 | 0 % ± 0 | 6.7 ± 0.1 | 0.0 ± 0.0 | 22 ± 0 | 432 ± 1 | 70 ± 4 | 86 % ± 1 | 9.1 ± 0.1 | 7.0 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t137-por-partes-corto-once semilla 12400: sprint → blue (5/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t137-por-partes-corto-once: enemy:mob=101
- t137-por-partes-corto-w4: enemy:mob=74
- t137-por-partes-corto-w8: enemy:mob=58
- t137-por-partes-corto-w12: enemy:mob=40

## Motivos de decisión (cambios registrados): ENGAGE 1268, LOW_HEALTH_RETREAT 95, NONE 285, SEEK_ENEMY 42, TOO_CLOSE 10

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
