# Simulación Rally - lote 20261124-001859-t158-por-partes-cebo

Preset `t158-por-partes-cebo` · velocidad `sprint` · 211 corridas · 7 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t158-por-partes-cebo-once | 30 | 100 % ± 0 | 0 % ± 0 | 4.6 ± 0.2 | 0.0 ± 0.0 | 16 ± 0 | 431 ± 2 | 105 ± 3 | 91 % ± 1 | 8.5 ± 0.1 | 6.9 ± 0.2 | 0 |
| t158-por-partes-cebo-w15 | 30 | 100 % ± 0 | 0 % ± 0 | 6.7 ± 0.2 | 0.0 ± 0.0 | 26 ± 0 | 433 ± 2 | 71 ± 4 | 86 % ± 1 | 9.2 ± 0.1 | 7.5 ± 0.2 | 0 |
| t158-por-partes-cebo-mont-sin | 30 | 100 % ± 0 | 0 % ± 0 | 4.3 ± 0.1 | 0.0 ± 0.0 | 16 ± 0 | 437 ± 2 | 111 ± 2 | 90 % ± 1 | 11.1 ± 0.2 | 7.9 ± 0.2 | 0 |
| t158-por-partes-cebo-cebo-pie | 30 | 100 % ± 0 | 0 % ± 0 | 5.8 ± 0.2 | 0.0 ± 0.0 | 15 ± 0 | 429 ± 2 | 87 ± 4 | 90 % ± 1 | 8.9 ± 0.1 | 7.4 ± 0.2 | 0 |
| t158-por-partes-cebo-cebo-mont | 30 | 100 % ± 0 | 0 % ± 0 | 5.3 ± 0.2 | 0.0 ± 0.0 | 17 ± 1 | 435 ± 2 | 96 ± 4 | 88 % ± 1 | 11.7 ± 0.3 | 8.5 ± 0.3 | 0 |
| t158-por-partes-cebo-once-d45 | 30 | 100 % ± 0 | 0 % ± 0 | 4.2 ± 0.1 | 0.0 ± 0.0 | 18 ± 0 | 433 ± 2 | 112 ± 1 | 92 % ± 1 | 11.7 ± 0.1 | 6.5 ± 0.2 | 0 |
| t158-por-partes-cebo-cebo-mont-d45 | 30 | 100 % ± 0 | 0 % ± 0 | 4.7 ± 0.3 | 0.0 ± 0.0 | 24 ± 3 | 432 ± 2 | 99 ± 7 | 89 % ± 1 | 15.9 ± 0.2 | 8.9 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t158-por-partes-cebo-once semilla 13700: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 210 de 210 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t158-por-partes-cebo-once: enemy:mob=103
- t158-por-partes-cebo-w15: enemy:mob=40
- t158-por-partes-cebo-mont-sin: enemy:mob=112
- t158-por-partes-cebo-cebo-pie: enemy:mob=66
- t158-por-partes-cebo-cebo-mont: enemy:mob=82
- t158-por-partes-cebo-once-d45: enemy:mob=114
- t158-por-partes-cebo-cebo-mont-d45: enemy:mob=100

## Motivos de decisión (cambios registrados): ENGAGE 1933, FORMATION_HOLD 28, LOW_HEALTH_RETREAT 170, NONE 216, SEEK_ENEMY 53, TOO_CLOSE 28, UNIT_BROKEN 44

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
