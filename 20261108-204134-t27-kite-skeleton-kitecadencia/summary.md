# Simulación Rally - lote 20261108-204134-t27-kite-skeleton-kitecadencia

Preset `t27-kite-skeleton-kitecadencia` · velocidad `sprint` · 41 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-kite-skeleton-arco-kitecadencia-r68 | 20 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.2 | 0.0 ± 0.0 | 26 ± 0 | 1111 ± 6 | 111 ± 4 | 93 % ± 0 | 15.2 ± 0.1 | 5.3 ± 0.2 | 0 |
| t27-kite-skeleton-linea-kitecadencia-r150 | 20 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 42 ± 1 | 923 ± 12 | 80 ± 5 | 86 % ± 1 | 13.9 ± 0.4 | 9.7 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-kite-skeleton-arco-kitecadencia-r68 semilla 1001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-kite-skeleton-arco-kitecadencia-r68: enemy:arrow=7
- t27-kite-skeleton-linea-kitecadencia-r150: enemy:arrow=3

## Motivos de decisión (cambios registrados): ENGAGE 354, LOW_HEALTH_RETREAT 193, MORALE_SHAKEN 37, MORALE_WAVERING 23, NONE 5, OUT_OF_RANGE 22, ROUTED_FLEE 3, SEEK_ENEMY 101, TOO_CLOSE 4

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
