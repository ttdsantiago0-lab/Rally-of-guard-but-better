# Simulación Rally - lote 20261108-204245-t27-kite-vindicator-base

Preset `t27-kite-vindicator-base` · velocidad `sprint` · 41 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-kite-vindicator-arco-base-r8 | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 9 ± 0 | 216 ± 2 | 0 ± 0 | 77 % ± 1 | 10.6 ± 0.3 | 5.6 ± 0.1 | 0 |
| t27-kite-vindicator-linea-base-r12 | 20 | 100 % ± 0 | 0 % ± 0 | 5.4 ± 0.3 | 0.0 ± 0.0 | 16 ± 0 | 322 ± 2 | 79 ± 6 | 88 % ± 1 | 8.3 ± 0.1 | 8.3 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-kite-vindicator-arco-base-r8 semilla 1001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-kite-vindicator-linea-base-r12: enemy:mob=53

## Motivos de decisión (cambios registrados): ENGAGE 340, LOW_HEALTH_RETREAT 55, MORALE_SHAKEN 57, MORALE_WAVERING 1, OUT_OF_RANGE 1, SEEK_ENEMY 6, TOO_CLOSE 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
