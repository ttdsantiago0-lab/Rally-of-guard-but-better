# Simulación Rally - lote 20261108-202430-t27-kite-skeleton-base

Preset `t27-kite-skeleton-base` · velocidad `sprint` · 41 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-kite-skeleton-arco-base-r68 | 20 | 85 % ± 8 | 15 % ± 8 | 4.2 ± 0.6 | 0.6 ± 0.3 | 41 ± 2 | 932 ± 13 | 163 ± 5 | 96 % ± 0 | 14.7 ± 0.2 | 6.7 ± 0.5 | 0 |
| t27-kite-skeleton-linea-base-r150 | 20 | 100 % ± 0 | 0 % ± 0 | 7.3 ± 0.3 | 0.0 ± 0.0 | 55 ± 5 | 756 ± 19 | 104 ± 9 | 90 % ± 1 | 14.1 ± 0.7 | 11.2 ± 1.0 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-kite-skeleton-arco-base-r68 semilla 1: sprint → blue (6/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 37 de 37 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-kite-skeleton-arco-base-r68: enemy:arrow=76
- t27-kite-skeleton-linea-base-r150: enemy:arrow=15

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 439, LOW_HEALTH_RETREAT 523, MORALE_SHAKEN 154, MORALE_WAVERING 108, NONE 18, OUT_OF_RANGE 20, RALLIED 4, REFUSE_ATTACK 2, ROUTED_FLEE 41, SEEK_ENEMY 274, TOO_CLOSE 19, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
