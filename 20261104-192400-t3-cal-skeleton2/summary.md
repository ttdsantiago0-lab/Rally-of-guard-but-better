# Simulación Rally - lote 20261104-192400-t3-cal-skeleton2

Preset `t3-cal-skeleton2` · velocidad `sprint` · 73 corridas · 12 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t3-cal-skeleton-arco-base-r40 | 6 | 100 % ± 0 | 0 % ± 0 | 7.2 ± 0.3 | 0.0 ± 0.0 | 25 ± 2 | 714 ± 7 | 113 ± 8 | 95 % ± 1 | 11.1 ± 0.3 | 4.8 ± 0.2 | 0 |
| t3-cal-skeleton-arco-base-r52 | 6 | 17 % ± 17 | 83 % ± 17 | 1.2 ± 1.2 | 3.8 ± 1.3 | 36 ± 4 | 741 ± 22 | 182 ± 9 | 97 % ± 1 | 10.0 ± 0.3 | 4.4 ± 0.1 | 0 |
| t3-cal-skeleton-arco-base-r64 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 15.8 ± 1.9 | 30 ± 1 | 640 ± 43 | 189 ± 1 | 99 % ± 0 | 9.0 ± 0.3 | 3.8 ± 0.2 | 0 |
| t3-cal-skeleton-arco-base-r80 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 20.3 ± 0.9 | 29 ± 1 | 653 ± 32 | 188 ± 1 | 98 % ± 1 | 9.4 ± 0.2 | 3.9 ± 0.2 | 0 |
| t3-cal-skeleton-linea-base-r40 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 7.2 ± 0.9 | 62 ± 5 | 409 ± 16 | 194 ± 2 | 98 % ± 0 | 7.7 ± 0.5 | 8.1 ± 1.3 | 0 |
| t3-cal-skeleton-linea-base-r52 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 6.3 ± 1.1 | 58 ± 3 | 419 ± 14 | 191 ± 2 | 99 % ± 1 | 7.4 ± 0.1 | 7.0 ± 0.8 | 0 |
| t3-cal-skeleton-linea-base-r64 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 13.0 ± 1.6 | 45 ± 3 | 345 ± 16 | 185 ± 2 | 100 % ± 0 | 7.6 ± 0.4 | 6.5 ± 1.6 | 0 |
| t3-cal-skeleton-linea-base-r80 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 14.2 ± 0.9 | 45 ± 1 | 370 ± 18 | 185 ± 1 | 100 % ± 0 | 7.2 ± 0.2 | 5.9 ± 0.5 | 0 |
| t3-cal-skeleton-cab-base-r40 | 6 | 50 % ± 22 | 50 % ± 22 | 3.0 ± 1.3 | 4.0 ± 1.9 | 40 ± 2 | 530 ± 40 | 146 ± 17 | 98 % ± 1 | 5.9 ± 0.5 | 6.4 ± 0.5 | 0 |
| t3-cal-skeleton-cab-base-r52 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 11.8 ± 2.4 | 46 ± 3 | 484 ± 30 | 189 ± 2 | 99 % ± 1 | 5.4 ± 0.3 | 6.1 ± 0.4 | 0 |
| t3-cal-skeleton-cab-base-r64 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 20.3 ± 3.5 | 38 ± 2 | 486 ± 31 | 185 ± 1 | 99 % ± 1 | 5.1 ± 0.3 | 8.2 ± 0.3 | 0 |
| t3-cal-skeleton-cab-base-r80 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 25.2 ± 3.6 | 35 ± 2 | 436 ± 33 | 186 ± 2 | 98 % ± 1 | 4.9 ± 0.1 | 6.6 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t3-cal-skeleton-arco-base-r40 semilla 1: sprint → blue (7/0 vivos), normal → red (0/8 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 10 de 10 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t3-cal-skeleton-arco-base-r40: enemy:arrow=5
- t3-cal-skeleton-arco-base-r52: enemy:arrow=41
- t3-cal-skeleton-arco-base-r64: enemy:arrow=48
- t3-cal-skeleton-arco-base-r80: enemy:arrow=48
- t3-cal-skeleton-linea-base-r40: enemy:arrow=48
- t3-cal-skeleton-linea-base-r52: enemy:arrow=48
- t3-cal-skeleton-linea-base-r64: enemy:arrow=48
- t3-cal-skeleton-linea-base-r80: enemy:arrow=48
- t3-cal-skeleton-cab-base-r40: enemy:arrow=30
- t3-cal-skeleton-cab-base-r52: enemy:arrow=48
- t3-cal-skeleton-cab-base-r64: enemy:arrow=48
- t3-cal-skeleton-cab-base-r80: enemy:arrow=48

## Motivos de decisión (cambios registrados): COUNTERATTACK 22, ENGAGE 1644, FORMATION_HOLD 20, LOW_HEALTH_RETREAT 156, MORALE_SHAKEN 835, MORALE_WAVERING 606, NONE 2, REFUSE_ATTACK 426, ROUTED_FLEE 528, SEEK_ENEMY 78, TOO_CLOSE 106, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t3-cal-skeleton-arco-base-r40: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
