# Simulación Rally - lote 20261108-195928-t24-techo-skeleton-orakite

Preset `t24-techo-skeleton-orakite` · velocidad `sprint` · 81 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t24-techo-skeleton-arco-orakite-r68 | 20 | 80 % ± 9 | 20 % ± 9 | 3.3 ± 0.6 | 1.5 ± 0.8 | 43 ± 3 | 925 ± 17 | 168 ± 5 | 96 % ± 0 | 14.7 ± 0.3 | 6.5 ± 0.3 | 0 |
| t24-techo-skeleton-arco-orakite-r95 | 20 | 25 % ± 10 | 75 % ± 10 | 1.2 ± 0.5 | 7.2 ± 1.3 | 42 ± 1 | 886 ± 26 | 182 ± 3 | 98 % ± 0 | 14.9 ± 0.2 | 6.9 ± 0.6 | 0 |
| t24-techo-skeleton-linea-orakite-r110 | 20 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 46 ± 3 | 733 ± 10 | 79 ± 5 | 86 % ± 1 | 14.0 ± 0.5 | 10.1 ± 0.6 | 0 |
| t24-techo-skeleton-linea-orakite-r150 | 20 | 95 % ± 5 | 5 % ± 5 | 7.2 ± 0.4 | 0.2 ± 0.2 | 53 ± 6 | 795 ± 11 | 101 ± 8 | 90 % ± 1 | 14.0 ± 0.9 | 10.7 ± 0.9 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t24-techo-skeleton-arco-orakite-r68 semilla 1: sprint → blue (5/0 vivos), normal → red (0/5 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t24-techo-skeleton-arco-orakite-r68: enemy:arrow=95
- t24-techo-skeleton-arco-orakite-r95: enemy:arrow=137
- t24-techo-skeleton-linea-orakite-r110: enemy:arrow=4
- t24-techo-skeleton-linea-orakite-r150: enemy:arrow=16

## Motivos de decisión (cambios registrados): ENGAGE 817, LOW_HEALTH_RETREAT 974, MORALE_SHAKEN 296, MORALE_WAVERING 135, NONE 31, OUT_OF_RANGE 35, RALLIED 4, REFUSE_ATTACK 13, ROUTED_FLEE 35, SEEK_ENEMY 569, TOO_CLOSE 26

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t24-techo-skeleton-arco-orakite-r68: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
