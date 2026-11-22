# Simulación Rally - lote 20261122-090807-t123-persecucion

Preset `t123-persecucion` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t123-persecucion-pie-fuga | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.0 | 0.0 ± 0.0 | 21 ± 1 | 144 ± 0 | 93 ± 2 | 0 % ± 0 | 16.2 ± 1.0 | 10.1 ± 0.5 | 0 |
| t123-persecucion-jinete-fuga | 30 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.1 | 0.0 ± 0.0 | 21 ± 1 | 144 ± 0 | 81 ± 2 | 0 % ± 0 | 20.9 ± 1.1 | 10.7 ± 0.4 | 0 |
| t123-persecucion-pie-sinfuga | 30 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.1 | 0.0 ± 0.0 | 13 ± 0 | 144 ± 0 | 93 ± 3 | 0 % ± 0 | 4.5 ± 0.1 | 4.9 ± 0.1 | 0 |
| t123-persecucion-jinete-sinfuga | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 13 ± 1 | 144 ± 0 | 84 ± 2 | 0 % ± 0 | 5.8 ± 0.3 | 5.2 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t123-persecucion-pie-fuga semilla 11400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t123-persecucion-pie-fuga: enemy:mob=2
- t123-persecucion-jinete-fuga: enemy:mob=8
- t123-persecucion-pie-sinfuga: enemy:mob=17
- t123-persecucion-jinete-sinfuga: enemy:mob=6

## Motivos de decisión (cambios registrados): ENGAGE 1751, FORMATION_HOLD 8, LOW_HEALTH_RETREAT 1848, NONE 151, SEEK_ENEMY 1094

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
