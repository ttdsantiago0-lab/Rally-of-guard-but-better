# Simulación Rally - lote 20261108-193206-t23-techo-skeleton-orapunta

Preset `t23-techo-skeleton-orapunta` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t23-techo-skeleton-arco-orapunta-r68 | 30 | 93 % ± 5 | 7 % ± 5 | 5.0 ± 0.5 | 0.2 ± 0.2 | 39 ± 2 | 962 ± 9 | 154 ± 4 | 96 % ± 0 | 14.9 ± 0.3 | 7.2 ± 0.5 | 0 |
| t23-techo-skeleton-linea-orapunta-r80 | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.0 | 0.0 ± 0.0 | 43 ± 1 | 717 ± 8 | 74 ± 4 | 85 % ± 1 | 14.1 ± 0.3 | 10.7 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t23-techo-skeleton-arco-orapunta-r68 semilla 1: sprint → blue (7/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 58 de 58 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t23-techo-skeleton-arco-orapunta-r68: enemy:arrow=89
- t23-techo-skeleton-linea-orapunta-r80: enemy:arrow=2

## Motivos de decisión (cambios registrados): ENGAGE 664, LOW_HEALTH_RETREAT 532, MORALE_SHAKEN 220, MORALE_WAVERING 85, NONE 25, OUT_OF_RANGE 37, RALLIED 1, REFUSE_ATTACK 6, ROUTED_FLEE 14, SEEK_ENEMY 308, TOO_CLOSE 21, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
