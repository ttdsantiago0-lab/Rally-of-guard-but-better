# Simulación Rally - lote 20261108-203945-t29-estructura-pasillos

Preset `t29-estructura-pasillos` · velocidad `sprint` · 61 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t29-estructura-pasillos-zombie-linea | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 24 ± 1 | 376 ± 3 | 20 ± 3 | 71 % ± 2 | 9.9 ± 0.3 | 6.8 ± 0.5 | 0 |
| t29-estructura-pasillos-zombie-arco | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 1 | 399 ± 3 | 10 ± 3 | 80 % ± 1 | 10.4 ± 0.2 | 5.0 ± 0.4 | 0 |
| t29-estructura-pasillos-skeleton-linea | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 32 ± 2 | 268 ± 3 | 25 ± 2 | 76 % ± 2 | 14.2 ± 0.2 | 8.8 ± 0.6 | 0 |
| t29-estructura-pasillos-skeleton-arco | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 27 ± 3 | 268 ± 4 | 27 ± 2 | 65 % ± 2 | 12.7 ± 0.3 | 4.9 ± 0.5 | 0 |
| t29-estructura-pasillos-vindicator-linea | 10 | 100 % ± 0 | 0 % ± 0 | 7.2 ± 0.4 | 0.0 ± 0.0 | 32 ± 5 | 155 ± 1 | 43 ± 9 | 57 % ± 2 | 12.6 ± 0.3 | 4.0 ± 0.4 | 0 |
| t29-estructura-pasillos-vindicator-arco | 10 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.2 | 0.0 ± 0.0 | 35 ± 3 | 163 ± 3 | 15 ± 6 | 65 % ± 1 | 13.5 ± 0.2 | 3.1 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t29-estructura-pasillos-zombie-linea semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t29-estructura-pasillos-vindicator-linea: enemy:mob=8
- t29-estructura-pasillos-vindicator-arco: enemy:mob=3

## Motivos de decisión (cambios registrados): ENGAGE 733, LOW_HEALTH_RETREAT 11, MORALE_SHAKEN 33, MORALE_WAVERING 2, NONE 2, REFUSE_ATTACK 2, SEEK_ENEMY 22, STUCK 2, TOO_CLOSE 241

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
