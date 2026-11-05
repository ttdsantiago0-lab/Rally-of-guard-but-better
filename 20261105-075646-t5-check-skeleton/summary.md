# Simulación Rally - lote 20261105-075646-t5-check-skeleton

Preset `t5-check-skeleton` · velocidad `sprint` · 301 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-skeleton-mele-base | 30 | 80 % ± 7 | 20 % ± 7 | 5.0 ± 0.5 | 1.2 ± 0.5 | 38 ± 2 | 487 ± 7 | 141 ± 6 | 0 % ± 0 | 6.9 ± 0.2 | 9.1 ± 0.3 | 0 |
| t5-final-skeleton-mele-antes | 30 | 73 % ± 8 | 27 % ± 8 | 4.8 ± 0.6 | 1.8 ± 0.6 | 37 ± 2 | 471 ± 10 | 139 ± 7 | 0 % ± 0 | 7.0 ± 0.3 | 8.8 ± 0.3 | 0 |
| t5-final-skeleton-escudo-base | 30 | 47 % ± 2 | 53 % ± 2 | 5.0 ± 0.4 | 3.1 ± 0.3 | 119 ± 1 | 203 ± 9 | 169 ± 6 | 0 % ± 0 | 7.4 ± 0.3 | 7.0 ± 0.5 | 0 |
| t5-final-skeleton-escudo-antes | 30 | 25 % ± 6 | 75 % ± 6 | 1.8 ± 0.5 | 4.7 ± 0.5 | 100 ± 3 | 223 ± 13 | 190 ± 5 | 0 % ± 0 | 7.9 ± 0.3 | 9.3 ± 0.6 | 0 |
| t5-final-skeleton-arco-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 25 ± 0 | 795 ± 6 | 101 ± 3 | 94 % ± 0 | 14.7 ± 0.1 | 5.3 ± 0.1 | 0 |
| t5-final-skeleton-arco-antes | 30 | 53 % ± 9 | 47 % ± 9 | 3.1 ± 0.6 | 3.0 ± 0.7 | 34 ± 1 | 715 ± 18 | 160 ± 6 | 97 % ± 0 | 10.0 ± 0.2 | 4.7 ± 0.2 | 0 |
| t5-final-skeleton-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 34 ± 1 | 540 ± 5 | 42 ± 2 | 82 % ± 1 | 14.5 ± 0.2 | 9.6 ± 0.2 | 0 |
| t5-final-skeleton-linea-antes | 30 | 48 % ± 9 | 52 % ± 9 | 2.5 ± 0.5 | 2.8 ± 0.6 | 79 ± 3 | 439 ± 13 | 175 ± 5 | 97 % ± 1 | 10.9 ± 0.8 | 10.3 ± 0.7 | 0 |
| t5-final-skeleton-cab-base | 30 | 77 % ± 8 | 23 % ± 8 | 5.4 ± 0.6 | 1.6 ± 0.6 | 35 ± 2 | 640 ± 12 | 124 ± 7 | 95 % ± 1 | 7.4 ± 0.3 | 7.8 ± 0.3 | 0 |
| t5-final-skeleton-cab-antes | 30 | 32 % ± 8 | 68 % ± 8 | 2.2 ± 0.6 | 5.2 ± 0.9 | 47 ± 3 | 549 ± 18 | 164 ± 8 | 97 % ± 1 | 6.5 ± 0.2 | 6.9 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-skeleton-mele-base semilla 1: sprint → red (0/3 vivos), normal → red (0/6 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 211 de 211 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-skeleton-mele-base: enemy:arrow=90
- t5-final-skeleton-mele-antes: enemy:arrow=95
- t5-final-skeleton-escudo-base: enemy:arrow=91
- t5-final-skeleton-escudo-antes: enemy:arrow=187
- t5-final-skeleton-arco-base: enemy:arrow=7
- t5-final-skeleton-arco-antes: enemy:arrow=147
- t5-final-skeleton-linea-antes: enemy:arrow=164
- t5-final-skeleton-cab-base: enemy:arrow=77
- t5-final-skeleton-cab-antes: enemy:arrow=174

## Motivos de decisión (cambios registrados): COUNTERATTACK 72, ENGAGE 8474, FORMATION_HOLD 62, LOW_HEALTH_RETREAT 1087, MORALE_SHAKEN 5501, MORALE_WAVERING 2231, NONE 16, OUT_OF_RANGE 16, RALLIED 45, REFUSE_ATTACK 1463, ROUTED_FLEE 1437, SEEK_ENEMY 648, STUCK 1, TOO_CLOSE 221

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t5-final-skeleton-escudo-base: 28/30 corridas llegaron al tope de tiempo
