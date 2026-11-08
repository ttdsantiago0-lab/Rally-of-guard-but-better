# Simulación Rally - lote 20261108-191725-t5-check-skeleton

Preset `t5-check-skeleton` · velocidad `sprint` · 301 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-skeleton-mele-base | 30 | 90 % ± 6 | 10 % ± 6 | 6.4 ± 0.4 | 0.7 ± 0.4 | 31 ± 1 | 494 ± 6 | 126 ± 5 | 0 % ± 0 | 7.1 ± 0.3 | 9.7 ± 0.3 | 0 |
| t5-final-skeleton-mele-antes | 30 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.3 | 0.0 ± 0.0 | 32 ± 2 | 496 ± 4 | 121 ± 5 | 0 % ± 0 | 6.6 ± 0.2 | 8.5 ± 0.3 | 0 |
| t5-final-skeleton-escudo-base | 30 | 78 % ± 5 | 22 % ± 5 | 7.4 ± 0.2 | 1.5 ± 0.5 | 110 ± 2 | 304 ± 9 | 97 ± 6 | 0 % ± 0 | 11.7 ± 0.6 | 15.4 ± 0.8 | 0 |
| t5-final-skeleton-escudo-antes | 30 | 92 % ± 3 | 8 % ± 3 | 7.3 ± 0.2 | 0.5 ± 0.2 | 88 ± 4 | 346 ± 5 | 84 ± 5 | 0 % ± 0 | 11.7 ± 0.7 | 12.8 ± 0.8 | 0 |
| t5-final-skeleton-arco-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.0 | 0.0 ± 0.0 | 25 ± 0 | 795 ± 5 | 99 ± 3 | 92 % ± 0 | 14.5 ± 0.1 | 5.2 ± 0.1 | 0 |
| t5-final-skeleton-arco-antes | 30 | 93 % ± 5 | 7 % ± 5 | 6.3 ± 0.4 | 0.2 ± 0.1 | 30 ± 1 | 774 ± 7 | 133 ± 4 | 95 % ± 0 | 11.4 ± 0.2 | 6.1 ± 0.2 | 0 |
| t5-final-skeleton-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 35 ± 1 | 542 ± 5 | 39 ± 2 | 81 % ± 1 | 14.8 ± 0.3 | 10.0 ± 0.3 | 0 |
| t5-final-skeleton-linea-antes | 30 | 85 % ± 6 | 15 % ± 6 | 5.7 ± 0.5 | 0.7 ± 0.3 | 60 ± 5 | 507 ± 10 | 124 ± 7 | 89 % ± 2 | 14.3 ± 0.9 | 13.9 ± 1.1 | 0 |
| t5-final-skeleton-cab-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.1 | 0.0 ± 0.0 | 27 ± 0 | 677 ± 5 | 94 ± 2 | 93 % ± 1 | 7.3 ± 0.2 | 8.0 ± 0.2 | 0 |
| t5-final-skeleton-cab-antes | 30 | 97 % ± 3 | 3 % ± 3 | 5.8 ± 0.4 | 0.0 ± 0.0 | 36 ± 2 | 639 ± 6 | 125 ± 5 | 95 % ± 1 | 7.1 ± 0.3 | 8.5 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-skeleton-mele-base semilla 1: sprint → blue (7/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 290 de 290 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-skeleton-mele-base: enemy:arrow=48
- t5-final-skeleton-mele-antes: enemy:arrow=44
- t5-final-skeleton-escudo-base: enemy:arrow=19
- t5-final-skeleton-escudo-antes: enemy:arrow=21
- t5-final-skeleton-arco-base: enemy:arrow=2
- t5-final-skeleton-arco-antes: enemy:arrow=50
- t5-final-skeleton-linea-antes: enemy:arrow=69
- t5-final-skeleton-cab-base: enemy:arrow=15
- t5-final-skeleton-cab-antes: enemy:arrow=65

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 4962, FORMATION_HOLD 60, LOW_HEALTH_RETREAT 1998, MORALE_SHAKEN 2454, MORALE_WAVERING 653, NONE 120, OUT_OF_RANGE 99, RALLIED 18, REFUSE_ATTACK 37, ROUTED_FLEE 225, SEEK_ENEMY 1177, STUCK 11, TOO_CLOSE 396

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
