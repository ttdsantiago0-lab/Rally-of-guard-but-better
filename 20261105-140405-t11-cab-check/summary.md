# Simulación Rally - lote 20261105-140405-t11-cab-check

Preset `t11-cab-check` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t11-creeper-cab-base | 30 | 98 % ± 2 | 2 % ± 2 | 3.8 ± 0.3 | 0.0 ± 0.0 | 16 ± 0 | 110 ± 6 | 152 ± 6 | 88 % ± 1 | 11.7 ± 0.1 | 8.4 ± 0.3 | 0 |
| t11-creeper-cab-sinesquiva | 30 | 100 % ± 0 | 0 % ± 0 | 5.1 ± 0.3 | 0.0 ± 0.0 | 16 ± 0 | 140 ± 4 | 122 ± 5 | 89 % ± 1 | 12.1 ± 0.2 | 9.2 ± 0.3 | 0 |
| t11-skeleton-cab-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.0 ± 0.3 | 0.0 ± 0.0 | 30 ± 1 | 676 ± 5 | 101 ± 5 | 96 % ± 1 | 7.3 ± 0.2 | 8.0 ± 0.2 | 0 |
| t11-skeleton-cab-antes | 30 | 40 % ± 9 | 60 % ± 9 | 2.5 ± 0.6 | 5.0 ± 1.0 | 44 ± 3 | 561 ± 17 | 157 ± 8 | 96 % ± 1 | 6.4 ± 0.2 | 7.4 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t11-creeper-cab-base semilla 1: sprint → blue (2/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 101 de 101 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t11-creeper-cab-base: enemy:explosion.player=126
- t11-creeper-cab-sinesquiva: enemy:explosion.player=88
- t11-skeleton-cab-base: enemy:arrow=30
- t11-skeleton-cab-antes: enemy:arrow=165

## Motivos de decisión (cambios registrados): COUNTERATTACK 3, ENGAGE 2116, FORMATION_HOLD 183, LOW_HEALTH_RETREAT 348, MORALE_SHAKEN 1261, MORALE_WAVERING 549, NONE 73, RALLIED 13, REFUSE_ATTACK 263, ROUTED_FLEE 264, SEEK_ENEMY 416, TOO_CLOSE 465, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
