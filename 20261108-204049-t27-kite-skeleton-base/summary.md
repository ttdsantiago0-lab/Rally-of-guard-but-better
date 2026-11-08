# Simulación Rally - lote 20261108-204049-t27-kite-skeleton-base

Preset `t27-kite-skeleton-base` · velocidad `sprint` · 41 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-kite-skeleton-arco-base-r68 | 20 | 75 % ± 10 | 25 % ± 10 | 3.8 ± 0.7 | 1.2 ± 0.6 | 41 ± 2 | 950 ± 10 | 165 ± 5 | 96 % ± 0 | 14.8 ± 0.2 | 6.4 ± 0.3 | 0 |
| t27-kite-skeleton-linea-base-r150 | 20 | 85 % ± 8 | 15 % ± 8 | 6.5 ± 0.7 | 0.5 ± 0.3 | 57 ± 5 | 741 ± 21 | 104 ± 10 | 88 % ± 1 | 13.5 ± 0.5 | 9.8 ± 0.7 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-kite-skeleton-arco-base-r68 semilla 1001: sprint → red (0/4 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 32 de 32 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-kite-skeleton-arco-base-r68: enemy:arrow=85
- t27-kite-skeleton-linea-base-r150: enemy:arrow=30

## Motivos de decisión (cambios registrados): ENGAGE 428, LOW_HEALTH_RETREAT 451, MORALE_SHAKEN 156, MORALE_WAVERING 68, NONE 23, OUT_OF_RANGE 14, RALLIED 1, REFUSE_ATTACK 12, ROUTED_FLEE 34, SEEK_ENEMY 266, TOO_CLOSE 18, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t27-kite-skeleton-arco-base-r68: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
