# Simulación Rally - lote 20261104-184350-t2-volea-30

Preset `t2-volea-30` · velocidad `sprint` · 210 corridas · 12 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t2-volea-zombie-arco@30-base | 18 | 56 % ± 12 | 44 % ± 12 | 4.1 ± 0.9 | 5.3 ± 1.7 | 44 ± 4 | 505 ± 17 | 126 ± 15 | 98 % ± 1 | 5.5 ± 0.4 | 7.0 ± 0.3 | 0 |
| t2-volea-zombie-arco@30-quietos | 18 | 72 % ± 11 | 28 % ± 11 | 4.8 ± 0.8 | 3.1 ± 1.3 | 43 ± 5 | 539 ± 14 | 105 ± 17 | 95 % ± 1 | 7.8 ± 0.4 | 8.5 ± 0.5 | 0 |
| t2-volea-zombie-arco@30-volea | 18 | 89 % ± 8 | 11 % ± 8 | 6.0 ± 0.6 | 0.9 ± 0.7 | 34 ± 3 | 548 ± 8 | 81 ± 12 | 97 % ± 1 | 7.5 ± 0.4 | 8.6 ± 0.3 | 0 |
| t2-volea-zombie-linea@30-base | 18 | 39 % ± 12 | 61 % ± 12 | 2.8 ± 0.9 | 8.3 ± 2.0 | 56 ± 5 | 469 ± 28 | 148 ± 13 | 87 % ± 7 | 6.6 ± 0.5 | 8.5 ± 0.5 | 0 |
| t2-volea-zombie-linea@30-quietos | 18 | 44 % ± 12 | 56 % ± 12 | 3.1 ± 0.8 | 8.1 ± 2.0 | 52 ± 6 | 472 ± 25 | 145 ± 15 | 97 % ± 1 | 6.9 ± 0.6 | 8.8 ± 0.5 | 0 |
| t2-volea-zombie-linea@30-volea | 18 | 33 % ± 11 | 67 % ± 11 | 2.5 ± 0.9 | 11.5 ± 2.1 | 46 ± 4 | 417 ± 24 | 151 ± 14 | 98 % ± 1 | 6.4 ± 0.3 | 9.1 ± 0.5 | 0 |
| t2-volea-skeleton-arco@30-base | 17 | 59 % ± 12 | 41 % ± 12 | 3.2 ± 0.8 | 2.1 ± 0.7 | 48 ± 5 | 479 ± 23 | 149 ± 14 | 94 % ± 1 | 11.0 ± 0.5 | 6.6 ± 0.3 | 0 |
| t2-volea-skeleton-arco@30-quietos | 17 | 41 % ± 12 | 59 % ± 12 | 2.5 ± 0.8 | 4.6 ± 1.2 | 48 ± 4 | 401 ± 31 | 153 ± 14 | 90 % ± 3 | 12.7 ± 0.7 | 9.9 ± 0.8 | 0 |
| t2-volea-skeleton-arco@30-volea | 17 | 47 % ± 12 | 53 % ± 12 | 2.1 ± 0.6 | 3.6 ± 1.0 | 55 ± 5 | 402 ± 31 | 161 ± 14 | 84 % ± 6 | 12.7 ± 0.8 | 8.4 ± 0.5 | 0 |
| t2-volea-skeleton-linea@30-base | 17 | 94 % ± 6 | 6 % ± 6 | 7.4 ± 0.5 | 0.3 ± 0.3 | 37 ± 3 | 351 ± 8 | 69 ± 10 | 90 % ± 1 | 12.7 ± 0.4 | 9.1 ± 0.4 | 0 |
| t2-volea-skeleton-linea@30-quietos | 17 | 91 % ± 6 | 9 % ± 6 | 6.5 ± 0.6 | 0.5 ± 0.4 | 46 ± 6 | 326 ± 13 | 77 ± 12 | 82 % ± 6 | 13.7 ± 0.8 | 10.8 ± 0.6 | 0 |
| t2-volea-skeleton-linea@30-volea | 17 | 79 % ± 9 | 21 % ± 9 | 5.6 ± 0.8 | 1.1 ± 0.5 | 61 ± 8 | 310 ± 15 | 98 ± 16 | 72 % ± 9 | 14.9 ± 0.8 | 12.6 ± 0.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 132 de 132 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t2-volea-zombie-arco@30-base: enemy:mob=41
- t2-volea-zombie-arco@30-quietos: enemy:mob=28
- t2-volea-zombie-arco@30-volea: enemy:mob=23
- t2-volea-zombie-linea@30-base: enemy:mob=50
- t2-volea-zombie-linea@30-quietos: enemy:mob=50
- t2-volea-zombie-linea@30-volea: enemy:mob=51
- t2-volea-skeleton-arco@30-base: enemy:arrow=81
- t2-volea-skeleton-arco@30-quietos: enemy:arrow=94
- t2-volea-skeleton-arco@30-volea: enemy:arrow=100
- t2-volea-skeleton-linea@30-base: enemy:arrow=11
- t2-volea-skeleton-linea@30-quietos: enemy:arrow=25
- t2-volea-skeleton-linea@30-volea: enemy:arrow=40

## Motivos de decisión (cambios registrados): COUNTERATTACK 236, ENGAGE 4002, FORMATION_HOLD 4, LOW_HEALTH_RETREAT 735, MORALE_SHAKEN 2187, MORALE_WAVERING 1200, NONE 383, OUT_OF_RANGE 2, RALLIED 21, REFUSE_ATTACK 162, ROUTED_FLEE 902, SEEK_ENEMY 159, STUCK 4, TOO_CLOSE 811, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
