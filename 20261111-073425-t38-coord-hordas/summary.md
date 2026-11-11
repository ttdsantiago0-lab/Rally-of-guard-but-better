# Simulación Rally - lote 20261111-073425-t38-coord-hordas

Preset `t38-coord-hordas` · velocidad `sprint` · 97 corridas · 12 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t38-coord-zombie24-arco-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 16 ± 0 | 592 ± 3 | 0 ± 0 | 93 % ± 1 | 9.4 ± 0.2 | 3.3 ± 0.3 | 0 |
| t38-coord-zombie24-linea-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 19 ± 0 | 576 ± 4 | 15 ± 2 | 91 % ± 1 | 7.4 ± 0.3 | 8.0 ± 0.6 | 0 |
| t38-coord-skeleton16-arco-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 13 ± 1 | 370 ± 5 | 11 ± 2 | 80 % ± 2 | 21.6 ± 0.6 | 5.1 ± 0.1 | 0 |
| t38-coord-skeleton16-linea-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 21 ± 1 | 340 ± 5 | 12 ± 1 | 74 % ± 2 | 16.5 ± 0.2 | 8.5 ± 0.3 | 0 |
| t38-coord-zombie24-arco-reparto | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 12 ± 0 | 584 ± 4 | 0 ± 0 | 96 % ± 1 | 10.1 ± 0.1 | 4.7 ± 0.3 | 0 |
| t38-coord-zombie24-linea-reparto | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 17 ± 0 | 579 ± 4 | 18 ± 3 | 95 % ± 1 | 7.4 ± 0.2 | 8.0 ± 0.3 | 0 |
| t38-coord-skeleton16-arco-reparto | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 11 ± 0 | 374 ± 3 | 6 ± 1 | 86 % ± 1 | 23.5 ± 0.4 | 5.1 ± 0.1 | 0 |
| t38-coord-skeleton16-linea-reparto | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 17 ± 0 | 361 ± 6 | 5 ± 1 | 89 % ± 1 | 17.2 ± 0.2 | 7.9 ± 0.2 | 0 |
| t38-coord-zombie24-arco-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 13 ± 0 | 585 ± 2 | 0 ± 0 | 99 % ± 0 | 10.4 ± 0.2 | 5.0 ± 0.2 | 0 |
| t38-coord-zombie24-linea-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 17 ± 0 | 576 ± 4 | 19 ± 2 | 95 % ± 1 | 7.0 ± 0.1 | 7.1 ± 0.3 | 0 |
| t38-coord-skeleton16-arco-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 11 ± 0 | 375 ± 6 | 6 ± 1 | 94 % ± 1 | 23.1 ± 0.4 | 5.0 ± 0.1 | 0 |
| t38-coord-skeleton16-linea-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 17 ± 1 | 355 ± 3 | 5 ± 1 | 94 % ± 1 | 17.7 ± 0.2 | 7.5 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t38-coord-zombie24-arco-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 96 de 96 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 780, LOW_HEALTH_RETREAT 2, SEEK_ENEMY 7

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
