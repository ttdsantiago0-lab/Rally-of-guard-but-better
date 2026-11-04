# Simulación Rally - lote 20261104-213310-t4-food-skeleton-b

Preset `t4-food-skeleton-b` · velocidad `sprint` · 211 corridas · 7 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t4-final-skeleton-arco-comidamano | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 35 ± 3 | 748 ± 11 | 161 ± 11 | 94 % ± 1 | 10.0 ± 0.2 | 5.1 ± 0.2 | 0 |
| t4-final-skeleton-linea-base | 30 | 42 % ± 9 | 58 % ± 9 | 2.7 ± 0.6 | 3.0 ± 0.6 | 71 ± 4 | 424 ± 15 | 166 ± 7 | 98 % ± 1 | 10.4 ± 0.7 | 9.3 ± 0.7 | 0 |
| t4-final-skeleton-linea-comidamochila | 30 | 97 % ± 2 | 3 % ± 2 | 7.9 ± 0.1 | 0.1 ± 0.1 | 64 ± 5 | 500 ± 7 | 154 ± 12 | 96 % ± 1 | 12.0 ± 0.8 | 11.0 ± 0.8 | 0 |
| t4-final-skeleton-linea-comidamano | 30 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.2 | 0.0 ± 0.0 | 50 ± 4 | 523 ± 6 | 130 ± 11 | 93 % ± 1 | 10.3 ± 0.4 | 9.3 ± 0.5 | 0 |
| t4-final-skeleton-cab-base | 30 | 40 % ± 9 | 60 % ± 9 | 2.2 ± 0.6 | 6.1 ± 1.0 | 42 ± 2 | 533 ± 17 | 161 ± 7 | 98 % ± 0 | 6.1 ± 0.2 | 7.0 ± 0.3 | 0 |
| t4-final-skeleton-cab-comidamochila | 30 | 93 % ± 4 | 7 % ± 4 | 7.2 ± 0.3 | 0.3 ± 0.2 | 56 ± 5 | 609 ± 10 | 193 ± 18 | 96 % ± 1 | 8.0 ± 0.4 | 8.2 ± 0.4 | 0 |
| t4-final-skeleton-cab-comidamano | 30 | 95 % ± 3 | 5 % ± 3 | 7.6 ± 0.1 | 0.2 ± 0.1 | 65 ± 6 | 590 ± 9 | 218 ± 18 | 97 % ± 1 | 8.0 ± 0.4 | 8.2 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t4-final-skeleton-arco-comidamano semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 175 de 175 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t4-final-skeleton-arco-comidamano: enemy:arrow=1
- t4-final-skeleton-linea-base: enemy:arrow=160
- t4-final-skeleton-linea-comidamochila: enemy:arrow=3
- t4-final-skeleton-linea-comidamano: enemy:arrow=15
- t4-final-skeleton-cab-base: enemy:arrow=174
- t4-final-skeleton-cab-comidamochila: enemy:arrow=23
- t4-final-skeleton-cab-comidamano: enemy:arrow=12

## Motivos de decisión (cambios registrados): COUNTERATTACK 29, ENGAGE 6368, FORMATION_HOLD 85, LOW_HEALTH_RETREAT 392, MORALE_SHAKEN 3618, MORALE_WAVERING 1791, NONE 3, OUT_OF_RANGE 9, RALLIED 85, REFUSE_ATTACK 1033, ROUTED_FLEE 1185, SEEK_ENEMY 165, TOO_CLOSE 479

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
