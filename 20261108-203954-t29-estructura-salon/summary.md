# Simulación Rally - lote 20261108-203954-t29-estructura-salon

Preset `t29-estructura-salon` · velocidad `sprint` · 61 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t29-estructura-salon-zombie-linea | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 18 ± 0 | 381 ± 3 | 7 ± 2 | 81 % ± 2 | 8.2 ± 0.2 | 6.8 ± 0.2 | 0 |
| t29-estructura-salon-zombie-arco | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 16 ± 1 | 390 ± 4 | 0 ± 0 | 78 % ± 1 | 11.0 ± 0.1 | 4.2 ± 0.3 | 0 |
| t29-estructura-salon-skeleton-linea | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 18 ± 0 | 274 ± 3 | 13 ± 2 | 84 % ± 1 | 13.9 ± 0.4 | 6.0 ± 0.2 | 0 |
| t29-estructura-salon-skeleton-arco | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 12 ± 0 | 283 ± 4 | 12 ± 1 | 74 % ± 1 | 19.7 ± 0.6 | 6.8 ± 0.2 | 0 |
| t29-estructura-salon-vindicator-linea | 10 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 10 ± 0 | 159 ± 2 | 21 ± 5 | 79 % ± 3 | 8.8 ± 0.2 | 6.1 ± 0.1 | 0 |
| t29-estructura-salon-vindicator-arco | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 7 ± 0 | 161 ± 2 | 0 ± 0 | 70 % ± 2 | 13.6 ± 0.5 | 5.9 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t29-estructura-salon-zombie-linea semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t29-estructura-salon-vindicator-linea: enemy:mob=2

## Motivos de decisión (cambios registrados): ENGAGE 515, LOW_HEALTH_RETREAT 3, MORALE_SHAKEN 4, SEEK_ENEMY 2, TOO_CLOSE 35

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
