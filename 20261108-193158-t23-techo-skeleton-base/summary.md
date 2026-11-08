# Simulación Rally - lote 20261108-193158-t23-techo-skeleton-base

Preset `t23-techo-skeleton-base` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t23-techo-skeleton-arco-base-r68 | 30 | 70 % ± 9 | 30 % ± 9 | 3.8 ± 0.6 | 1.2 ± 0.4 | 41 ± 2 | 927 ± 12 | 163 ± 4 | 96 % ± 0 | 15.1 ± 0.3 | 7.1 ± 0.6 | 0 |
| t23-techo-skeleton-linea-base-r80 | 30 | 97 % ± 3 | 3 % ± 3 | 7.5 ± 0.3 | 0.1 ± 0.1 | 44 ± 2 | 717 ± 9 | 75 ± 7 | 86 % ± 1 | 13.6 ± 0.2 | 10.1 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t23-techo-skeleton-arco-base-r68 semilla 1: sprint → red (0/4 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 50 de 50 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t23-techo-skeleton-arco-base-r68: enemy:arrow=127
- t23-techo-skeleton-linea-base-r80: enemy:arrow=15

## Motivos de decisión (cambios registrados): ENGAGE 602, LOW_HEALTH_RETREAT 555, MORALE_SHAKEN 177, MORALE_WAVERING 69, NONE 30, OUT_OF_RANGE 16, RALLIED 1, REFUSE_ATTACK 9, ROUTED_FLEE 24, SEEK_ENEMY 299, STUCK 1, TOO_CLOSE 18

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t23-techo-skeleton-arco-base-r68: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
