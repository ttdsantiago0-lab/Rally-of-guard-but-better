# Simulación Rally - lote 20261108-191810-t11-cab-check

Preset `t11-cab-check` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t11-creeper-cab-base | 30 | 100 % ± 0 | 0 % ± 0 | 4.7 ± 0.3 | 0.0 ± 0.0 | 17 ± 0 | 126 ± 7 | 134 ± 6 | 88 % ± 1 | 12.2 ± 0.2 | 9.0 ± 0.3 | 0 |
| t11-creeper-cab-sinesquiva | 30 | 100 % ± 0 | 0 % ± 0 | 4.3 ± 0.3 | 0.0 ± 0.0 | 17 ± 0 | 133 ± 7 | 136 ± 5 | 87 % ± 2 | 12.0 ± 0.2 | 9.2 ± 0.3 | 0 |
| t11-skeleton-cab-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.2 | 0.0 ± 0.0 | 28 ± 0 | 674 ± 6 | 97 ± 3 | 93 % ± 1 | 7.2 ± 0.2 | 7.8 ± 0.2 | 0 |
| t11-skeleton-cab-antes | 30 | 93 % ± 5 | 7 % ± 5 | 6.0 ± 0.4 | 0.3 ± 0.2 | 34 ± 1 | 636 ± 6 | 121 ± 5 | 95 % ± 1 | 6.9 ± 0.2 | 8.8 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t11-creeper-cab-base semilla 1: sprint → blue (7/0 vivos), normal → blue (2/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 118 de 118 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t11-creeper-cab-base: enemy:explosion.player=100
- t11-creeper-cab-sinesquiva: enemy:explosion.player=111
- t11-skeleton-cab-base: enemy:arrow=19
- t11-skeleton-cab-antes: enemy:arrow=60

## Motivos de decisión (cambios registrados): ENGAGE 1063, FORMATION_HOLD 211, LOW_HEALTH_RETREAT 533, MORALE_SHAKEN 277, MORALE_WAVERING 67, NONE 90, OUT_OF_RANGE 3, REFUSE_ATTACK 1, ROUTED_FLEE 7, SEEK_ENEMY 528, TOO_CLOSE 479

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
