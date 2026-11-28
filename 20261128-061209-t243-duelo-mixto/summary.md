# Simulación Rally - lote 20261128-061209-t243-duelo-mixto

Preset `t243-duelo-mixto` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t243-32-espejo | 30 | 50 % ± 9 | 50 % ± 9 | 2.3 ± 0.5 | 2.2 ± 0.5 | 20 ± 0 | 722 ± 6 | 714 ± 7 | 88 % ± 1 | 16.4 ± 0.3 | 8.8 ± 0.2 | 0 |
| t243-32-sync-solo | 30 | 100 % ± 0 | 0 % ± 0 | 8.3 ± 0.4 | 0.0 ± 0.0 | 18 ± 0 | 751 ± 2 | 612 ± 7 | 86 % ± 1 | 17.0 ± 0.2 | 10.7 ± 0.1 | 0 |
| t243-32-solo-sync | 30 | 7 % ± 5 | 93 % ± 5 | 0.2 ± 0.1 | 6.2 ± 0.6 | 19 ± 1 | 651 ± 10 | 744 ± 3 | 86 % ± 1 | 15.5 ± 0.3 | 9.0 ± 0.2 | 0 |
| t243-64-espejo | 30 | 57 % ± 9 | 43 % ± 9 | 3.4 ± 0.7 | 2.9 ± 0.6 | 30 ± 1 | 1465 ± 9 | 1456 ± 11 | 79 % ± 1 | 17.1 ± 0.3 | 10.0 ± 0.2 | 0 |
| t243-64-sync-solo | 30 | 67 % ± 9 | 33 % ± 9 | 7.6 ± 1.4 | 1.9 ± 0.6 | 28 ± 1 | 1483 ± 7 | 1380 ± 21 | 78 % ± 1 | 18.4 ± 0.3 | 10.4 ± 0.2 | 0 |
| t243-64-solo-sync | 30 | 35 % ± 9 | 65 % ± 9 | 2.1 ± 0.6 | 6.4 ± 1.2 | 30 ± 1 | 1408 ± 17 | 1477 ± 10 | 81 % ± 1 | 17.7 ± 0.3 | 9.9 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t243-32-espejo semilla 24000: sprint → blue (3/0 vivos), normal → red (0/2 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 94 de 94 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t243-32-espejo: enemy:arrow=879 enemy:mob=12
- t243-32-sync-solo: enemy:arrow=711
- t243-32-solo-sync: enemy:arrow=938 enemy:mob=17
- t243-64-espejo: enemy:arrow=1727 enemy:mob=90
- t243-64-sync-solo: enemy:arrow=1625 enemy:mob=68
- t243-64-solo-sync: enemy:arrow=1790 enemy:mob=68

## Motivos de decisión (cambios registrados): ENGAGE 24159, NONE 173, OUT_OF_RANGE 1197, SEEK_ENEMY 3, TOO_CLOSE 6346

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t243-32-espejo: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
