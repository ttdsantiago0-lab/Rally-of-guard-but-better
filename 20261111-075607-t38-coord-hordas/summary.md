# Simulación Rally - lote 20261111-075607-t38-coord-hordas

Preset `t38-coord-hordas` · velocidad `sprint` · 97 corridas · 12 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t38-coord-zombie24-arco-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 16 ± 1 | 597 ± 1 | 0 ± 0 | 92 % ± 1 | 9.5 ± 0.2 | 3.6 ± 0.2 | 0 |
| t38-coord-zombie24-linea-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 18 ± 1 | 581 ± 3 | 20 ± 3 | 90 % ± 1 | 7.2 ± 0.3 | 7.8 ± 0.4 | 0 |
| t38-coord-skeleton16-arco-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 13 ± 0 | 372 ± 4 | 12 ± 1 | 82 % ± 2 | 21.3 ± 0.4 | 5.0 ± 0.1 | 0 |
| t38-coord-skeleton16-linea-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 20 ± 1 | 358 ± 4 | 5 ± 1 | 77 % ± 2 | 17.2 ± 0.3 | 8.5 ± 0.3 | 0 |
| t38-coord-zombie24-arco-reparto | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 12 ± 0 | 584 ± 4 | 0 ± 0 | 97 % ± 1 | 10.4 ± 0.1 | 4.5 ± 0.2 | 0 |
| t38-coord-zombie24-linea-reparto | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 16 ± 0 | 571 ± 5 | 17 ± 2 | 95 % ± 1 | 7.0 ± 0.1 | 6.8 ± 0.2 | 0 |
| t38-coord-skeleton16-arco-reparto | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 11 ± 0 | 372 ± 2 | 3 ± 1 | 85 % ± 2 | 24.1 ± 0.4 | 5.0 ± 0.0 | 0 |
| t38-coord-skeleton16-linea-reparto | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 17 ± 0 | 360 ± 4 | 4 ± 1 | 90 % ± 1 | 17.4 ± 0.2 | 7.4 ± 0.1 | 0 |
| t38-coord-zombie24-arco-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 13 ± 0 | 585 ± 2 | 0 ± 0 | 97 % ± 1 | 10.1 ± 0.1 | 4.5 ± 0.1 | 0 |
| t38-coord-zombie24-linea-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 17 ± 1 | 585 ± 7 | 17 ± 3 | 94 % ± 2 | 7.4 ± 0.2 | 7.9 ± 0.3 | 0 |
| t38-coord-skeleton16-arco-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 10 ± 0 | 375 ± 4 | 4 ± 1 | 94 % ± 1 | 24.2 ± 0.6 | 5.0 ± 0.1 | 0 |
| t38-coord-skeleton16-linea-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 17 ± 0 | 360 ± 6 | 3 ± 0 | 92 % ± 1 | 18.1 ± 0.2 | 8.1 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t38-coord-zombie24-arco-base semilla 1001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 96 de 96 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t38-coord-zombie24-linea-sincro: enemy:mob=1

## Motivos de decisión (cambios registrados): ENGAGE 777, LOW_HEALTH_RETREAT 1, SEEK_ENEMY 2, TOO_CLOSE 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
