# Simulación Rally - lote 20261104-215957-t4-food-skeleton-b

Preset `t4-food-skeleton-b` · velocidad `sprint` · 211 corridas · 7 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t4-final-skeleton-arco-comidamano | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 33 ± 3 | 765 ± 9 | 146 ± 9 | 94 % ± 1 | 10.2 ± 0.2 | 5.2 ± 0.2 | 0 |
| t4-final-skeleton-linea-base | 30 | 45 % ± 9 | 55 % ± 9 | 2.4 ± 0.5 | 2.1 ± 0.5 | 73 ± 4 | 444 ± 13 | 170 ± 7 | 98 % ± 0 | 9.5 ± 0.3 | 9.0 ± 0.6 | 0 |
| t4-final-skeleton-linea-comidamochila | 30 | 95 % ± 3 | 5 % ± 3 | 8.0 ± 0.0 | 0.2 ± 0.1 | 65 ± 5 | 503 ± 6 | 159 ± 10 | 95 % ± 1 | 11.5 ± 0.7 | 11.4 ± 0.7 | 0 |
| t4-final-skeleton-linea-comidamano | 30 | 100 % ± 0 | 0 % ± 0 | 7.2 ± 0.2 | 0.0 ± 0.0 | 60 ± 4 | 489 ± 8 | 156 ± 9 | 95 % ± 1 | 10.5 ± 0.7 | 9.1 ± 0.6 | 0 |
| t4-final-skeleton-cab-base | 30 | 43 % ± 9 | 57 % ± 9 | 2.6 ± 0.6 | 4.8 ± 0.9 | 45 ± 3 | 554 ± 14 | 160 ± 7 | 98 % ± 0 | 6.7 ± 0.4 | 7.1 ± 0.2 | 0 |
| t4-final-skeleton-cab-comidamochila | 30 | 98 % ± 2 | 2 % ± 2 | 7.5 ± 0.2 | 0.1 ± 0.1 | 56 ± 5 | 619 ± 10 | 183 ± 18 | 96 % ± 1 | 8.1 ± 0.6 | 8.0 ± 0.3 | 0 |
| t4-final-skeleton-cab-comidamano | 30 | 95 % ± 3 | 5 % ± 3 | 7.7 ± 0.1 | 0.2 ± 0.1 | 64 ± 6 | 608 ± 9 | 211 ± 17 | 97 % ± 1 | 7.9 ± 0.4 | 7.9 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t4-final-skeleton-arco-comidamano semilla 1001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 177 de 177 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t4-final-skeleton-linea-base: enemy:arrow=168
- t4-final-skeleton-linea-comidamochila: enemy:arrow=1
- t4-final-skeleton-linea-comidamano: enemy:arrow=24
- t4-final-skeleton-cab-base: enemy:arrow=162
- t4-final-skeleton-cab-comidamochila: enemy:arrow=14
- t4-final-skeleton-cab-comidamano: enemy:arrow=10

## Motivos de decisión (cambios registrados): COUNTERATTACK 24, ENGAGE 6440, FORMATION_HOLD 76, LOW_HEALTH_RETREAT 411, MORALE_SHAKEN 3618, MORALE_WAVERING 1840, NONE 13, OUT_OF_RANGE 12, RALLIED 96, REFUSE_ATTACK 1136, ROUTED_FLEE 1238, SEEK_ENEMY 181, TOO_CLOSE 463

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
