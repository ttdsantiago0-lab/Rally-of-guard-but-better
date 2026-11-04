# Simulación Rally - lote 20261104-184440-t2-volea-70

Preset `t2-volea-70` · velocidad `sprint` · 80 corridas · 12 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t2-volea-zombie-arco@70-base | 7 | 86 % ± 14 | 14 % ± 14 | 5.4 ± 1.1 | 1.3 ± 1.3 | 46 ± 6 | 547 ± 16 | 103 ± 21 | 97 % ± 1 | 10.8 ± 0.6 | 7.9 ± 0.9 | 0 |
| t2-volea-zombie-arco@70-quietos | 7 | 29 % ± 18 | 71 % ± 18 | 1.4 ± 0.9 | 7.7 ± 2.6 | 64 ± 8 | 535 ± 30 | 166 ± 20 | 95 % ± 1 | 13.7 ± 0.8 | 10.3 ± 1.1 | 0 |
| t2-volea-zombie-arco@70-volea | 7 | 71 % ± 18 | 29 % ± 18 | 4.0 ± 1.1 | 5.3 ± 3.6 | 52 ± 7 | 525 ± 44 | 119 ± 25 | 90 % ± 4 | 19.0 ± 2.3 | 10.0 ± 0.7 | 0 |
| t2-volea-zombie-linea@70-base | 7 | 21 % ± 15 | 79 % ± 15 | 1.3 ± 1.0 | 14.6 ± 3.2 | 87 ± 12 | 381 ± 47 | 178 ± 18 | 65 % ± 17 | 18.8 ± 0.8 | 14.4 ± 0.8 | 0 |
| t2-volea-zombie-linea@70-quietos | 7 | 14 % ± 9 | 86 % ± 9 | 0.4 ± 0.3 | 20.6 ± 1.0 | 91 ± 10 | 299 ± 20 | 180 ± 5 | 90 % ± 4 | 21.3 ± 1.2 | 12.8 ± 1.5 | 0 |
| t2-volea-zombie-linea@70-volea | 7 | 14 % ± 14 | 86 % ± 14 | 0.9 ± 0.9 | 16.6 ± 3.1 | 80 ± 8 | 371 ± 43 | 169 ± 17 | 80 % ± 7 | 20.8 ± 1.4 | 12.4 ± 0.7 | 0 |
| t2-volea-skeleton-arco@70-base | 7 | 43 % ± 20 | 57 % ± 20 | 2.1 ± 1.0 | 5.1 ± 1.9 | 50 ± 3 | 399 ± 53 | 156 ± 16 | 95 % ± 2 | 15.9 ± 0.8 | 9.3 ± 0.7 | 0 |
| t2-volea-skeleton-arco@70-quietos | 7 | 86 % ± 14 | 14 % ± 14 | 5.7 ± 1.0 | 0.6 ± 0.6 | 39 ± 4 | 463 ± 53 | 92 ± 19 | 85 % ± 3 | 22.0 ± 1.3 | 9.4 ± 0.5 | 0 |
| t2-volea-skeleton-arco@70-volea | 6 | 83 % ± 17 | 17 % ± 17 | 5.7 ± 1.2 | 0.3 ± 0.3 | 45 ± 4 | 448 ± 32 | 86 ± 23 | 84 % ± 3 | 21.5 ± 1.2 | 9.0 ± 0.9 | 0 |
| t2-volea-skeleton-linea@70-base | 6 | 83 % ± 17 | 17 % ± 17 | 5.3 ± 1.2 | 1.7 ± 1.7 | 59 ± 8 | 306 ± 36 | 96 ± 23 | 94 % ± 1 | 22.3 ± 2.0 | 13.9 ± 1.0 | 0 |
| t2-volea-skeleton-linea@70-quietos | 6 | 83 % ± 17 | 17 % ± 17 | 6.2 ± 1.2 | 0.3 ± 0.3 | 52 ± 11 | 327 ± 24 | 82 ± 30 | 90 % ± 2 | 21.9 ± 1.2 | 12.8 ± 0.4 | 0 |
| t2-volea-skeleton-linea@70-volea | 6 | 75 % ± 17 | 25 % ± 17 | 5.7 ± 1.3 | 0.8 ± 0.5 | 66 ± 17 | 297 ± 26 | 87 ± 30 | 93 % ± 2 | 23.4 ± 2.1 | 11.4 ± 1.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 47 de 47 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t2-volea-zombie-arco@70-base: enemy:mob=8
- t2-volea-zombie-arco@70-quietos: enemy:mob=17
- t2-volea-zombie-arco@70-volea: enemy:mob=11
- t2-volea-zombie-linea@70-base: enemy:mob=26
- t2-volea-zombie-linea@70-quietos: enemy:mob=25
- t2-volea-zombie-linea@70-volea: enemy:mob=22
- t2-volea-skeleton-arco@70-base: enemy:arrow=41
- t2-volea-skeleton-arco@70-quietos: enemy:arrow=16
- t2-volea-skeleton-arco@70-volea: enemy:arrow=14
- t2-volea-skeleton-linea@70-base: enemy:arrow=16
- t2-volea-skeleton-linea@70-quietos: enemy:arrow=11
- t2-volea-skeleton-linea@70-volea: enemy:arrow=14

## Motivos de decisión (cambios registrados): COUNTERATTACK 187, ENGAGE 1389, LOW_HEALTH_RETREAT 310, MORALE_SHAKEN 724, MORALE_WAVERING 497, NONE 178, OUT_OF_RANGE 264, RALLIED 16, REFUSE_ATTACK 31, ROUTED_FLEE 472, SEEK_ENEMY 59, STUCK 4, TOO_CLOSE 183

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
