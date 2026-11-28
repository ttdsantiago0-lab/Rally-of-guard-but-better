# Simulación Rally - lote 20261128-074710-t252-duelo-sync

Preset `t252-duelo-sync` · velocidad `sprint` · 241 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t252-espejo | 60 | 53 % ± 6 | 47 % ± 6 | 5.1 ± 0.7 | 3.5 ± 0.7 | 29 ± 1 | 1459 ± 10 | 1440 ± 10 | 79 % ± 1 | 17.1 ± 0.2 | 10.6 ± 0.2 | 0 |
| t252-todo | 60 | 60 % ± 6 | 40 % ± 6 | 5.9 ± 0.8 | 2.6 ± 0.5 | 29 ± 0 | 1470 ± 8 | 1412 ± 12 | 80 % ± 0 | 18.3 ± 0.2 | 10.4 ± 0.1 | 0 |
| t252-sync-espejo | 60 | 55 % ± 6 | 45 % ± 6 | 4.8 ± 0.7 | 3.9 ± 0.7 | 28 ± 0 | 1456 ± 10 | 1445 ± 10 | 79 % ± 1 | 16.7 ± 0.2 | 10.7 ± 0.1 | 0 |
| t252-sync-todo | 60 | 70 % ± 6 | 30 % ± 6 | 6.7 ± 0.8 | 1.8 ± 0.4 | 29 ± 0 | 1480 ± 6 | 1403 ± 12 | 79 % ± 0 | 18.3 ± 0.2 | 10.6 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t252-espejo semilla 25200: sprint → blue (12/0 vivos), normal → blue (10/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 143 de 143 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t252-espejo: enemy:arrow=3332 enemy:mob=204
- t252-todo: enemy:arrow=3344 enemy:mob=145
- t252-sync-espejo: enemy:arrow=3257 enemy:mob=294
- t252-sync-todo: enemy:arrow=3298 enemy:mob=142

## Motivos de decisión (cambios registrados): ENGAGE 44906, NONE 416, OUT_OF_RANGE 2519, SEEK_ENEMY 10, TOO_CLOSE 13214

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
