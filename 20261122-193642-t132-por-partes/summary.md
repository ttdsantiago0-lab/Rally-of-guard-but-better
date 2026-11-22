# Simulación Rally - lote 20261122-193642-t132-por-partes

Preset `t132-por-partes` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t132-por-partes-once | 30 | 100 % ± 0 | 0 % ± 0 | 4.4 ± 0.1 | 0.0 ± 0.0 | 16 ± 0 | 434 ± 2 | 109 ± 2 | 92 % ± 1 | 8.4 ± 0.1 | 6.7 ± 0.3 | 0 |
| t132-por-partes-w15 | 30 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.2 | 0.0 ± 0.0 | 26 ± 0 | 431 ± 2 | 80 ± 4 | 87 % ± 1 | 9.2 ± 0.1 | 7.4 ± 0.2 | 0 |
| t132-por-partes-w30 | 30 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.2 | 0.0 ± 0.0 | 41 ± 0 | 431 ± 2 | 69 ± 4 | 85 % ± 1 | 10.0 ± 0.2 | 9.4 ± 0.2 | 0 |
| t132-por-partes-w60 | 30 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.2 | 0.0 ± 0.0 | 71 ± 0 | 432 ± 2 | 68 ± 5 | 85 % ± 1 | 10.9 ± 0.2 | 12.9 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t132-por-partes-once semilla 12400: sprint → blue (5/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t132-por-partes-once: enemy:mob=108
- t132-por-partes-w15: enemy:mob=46
- t132-por-partes-w30: enemy:mob=44
- t132-por-partes-w60: enemy:mob=45

## Motivos de decisión (cambios registrados): ENGAGE 1655, LOW_HEALTH_RETREAT 61, NONE 679, OUT_OF_RANGE 14, SEEK_ENEMY 44, TOO_CLOSE 19

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
