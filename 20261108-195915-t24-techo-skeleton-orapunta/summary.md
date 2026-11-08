# Simulación Rally - lote 20261108-195915-t24-techo-skeleton-orapunta

Preset `t24-techo-skeleton-orapunta` · velocidad `sprint` · 81 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t24-techo-skeleton-arco-orapunta-r68 | 20 | 80 % ± 9 | 20 % ± 9 | 4.5 ± 0.7 | 1.0 ± 0.5 | 38 ± 2 | 940 ± 14 | 158 ± 6 | 97 % ± 0 | 14.7 ± 0.2 | 6.7 ± 0.3 | 0 |
| t24-techo-skeleton-arco-orapunta-r95 | 20 | 20 % ± 9 | 80 % ± 9 | 0.6 ± 0.4 | 6.4 ± 1.4 | 45 ± 3 | 918 ± 13 | 186 ± 2 | 98 % ± 0 | 14.4 ± 0.2 | 6.4 ± 0.4 | 0 |
| t24-techo-skeleton-linea-orapunta-r110 | 20 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.3 | 0.0 ± 0.0 | 52 ± 6 | 734 ± 12 | 91 ± 7 | 87 % ± 1 | 14.9 ± 1.2 | 10.8 ± 0.9 | 0 |
| t24-techo-skeleton-linea-orapunta-r150 | 20 | 90 % ± 7 | 10 % ± 7 | 6.7 ± 0.6 | 0.5 ± 0.4 | 53 ± 6 | 774 ± 21 | 97 ± 10 | 89 % ± 1 | 13.1 ± 0.4 | 9.8 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t24-techo-skeleton-arco-orapunta-r68 semilla 1: sprint → red (0/4 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 58 de 58 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t24-techo-skeleton-arco-orapunta-r68: enemy:arrow=71
- t24-techo-skeleton-arco-orapunta-r95: enemy:arrow=148
- t24-techo-skeleton-linea-orapunta-r110: enemy:arrow=8
- t24-techo-skeleton-linea-orapunta-r150: enemy:arrow=27

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 848, LOW_HEALTH_RETREAT 861, MORALE_SHAKEN 313, MORALE_WAVERING 126, NONE 21, OUT_OF_RANGE 34, RALLIED 6, REFUSE_ATTACK 10, ROUTED_FLEE 50, SEEK_ENEMY 490, TOO_CLOSE 31

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t24-techo-skeleton-arco-orapunta-r68: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
