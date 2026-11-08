# Simulación Rally - lote 20261108-202447-t27-kite-skeleton-kitecadencia

Preset `t27-kite-skeleton-kitecadencia` · velocidad `sprint` · 41 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-kite-skeleton-arco-kitecadencia-r68 | 20 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.2 | 0.0 ± 0.0 | 27 ± 1 | 1113 ± 9 | 121 ± 4 | 95 % ± 0 | 15.1 ± 0.2 | 5.9 ± 0.3 | 0 |
| t27-kite-skeleton-linea-kitecadencia-r150 | 20 | 95 % ± 5 | 5 % ± 5 | 7.2 ± 0.5 | 0.1 ± 0.1 | 48 ± 3 | 901 ± 15 | 91 ± 9 | 86 % ± 1 | 13.7 ± 0.3 | 10.5 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-kite-skeleton-arco-kitecadencia-r68 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 39 de 39 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-kite-skeleton-arco-kitecadencia-r68: enemy:arrow=10
- t27-kite-skeleton-linea-kitecadencia-r150: enemy:arrow=16

## Motivos de decisión (cambios registrados): ENGAGE 430, LOW_HEALTH_RETREAT 271, MORALE_SHAKEN 126, MORALE_WAVERING 32, NONE 8, OUT_OF_RANGE 20, RALLIED 1, ROUTED_FLEE 14, SEEK_ENEMY 155, TOO_CLOSE 9

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
