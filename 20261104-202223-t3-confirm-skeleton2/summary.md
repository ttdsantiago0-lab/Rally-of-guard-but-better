# Simulación Rally - lote 20261104-202223-t3-confirm-skeleton2

Preset `t3-confirm-skeleton2` · velocidad `sprint` · 241 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t3-final-skeleton-arco-base | 30 | 80 % ± 7 | 20 % ± 7 | 5.1 ± 0.5 | 1.6 ± 0.6 | 31 ± 1 | 755 ± 13 | 142 ± 6 | 95 % ± 1 | 10.8 ± 0.2 | 4.8 ± 0.2 | 0 |
| t3-final-skeleton-arco-quietos | 30 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.1 | 0.0 ± 0.0 | 26 ± 1 | 803 ± 4 | 100 ± 3 | 93 % ± 0 | 14.6 ± 0.1 | 5.3 ± 0.1 | 0 |
| t3-final-skeleton-arco-volea | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 24 ± 0 | 800 ± 6 | 97 ± 3 | 93 % ± 1 | 14.8 ± 0.1 | 5.2 ± 0.1 | 0 |
| t3-final-skeleton-linea-base | 30 | 43 % ± 9 | 57 % ± 9 | 1.8 ± 0.5 | 3.3 ± 0.7 | 70 ± 4 | 431 ± 14 | 174 ± 7 | 98 % ± 1 | 9.2 ± 0.4 | 8.5 ± 0.5 | 0 |
| t3-final-skeleton-linea-quietos | 30 | 97 % ± 3 | 3 % ± 3 | 7.6 ± 0.3 | 0.1 ± 0.1 | 40 ± 3 | 532 ± 10 | 62 ± 6 | 83 % ± 1 | 14.4 ± 0.3 | 10.1 ± 0.3 | 0 |
| t3-final-skeleton-linea-volea | 30 | 98 % ± 2 | 2 % ± 2 | 7.5 ± 0.3 | 0.0 ± 0.0 | 45 ± 3 | 524 ± 7 | 67 ± 7 | 84 % ± 1 | 14.2 ± 0.3 | 10.2 ± 0.3 | 0 |
| t3-final-skeleton-cab-base | 30 | 37 % ± 9 | 63 % ± 9 | 2.0 ± 0.5 | 5.4 ± 1.0 | 46 ± 2 | 543 ± 17 | 169 ± 6 | 97 % ± 1 | 6.5 ± 0.2 | 6.9 ± 0.2 | 0 |
| t3-final-skeleton-cab-quemarropa | 30 | 87 % ± 6 | 13 % ± 6 | 6.0 ± 0.5 | 1.0 ± 0.5 | 34 ± 2 | 668 ± 11 | 117 ± 7 | 95 % ± 1 | 7.1 ± 0.3 | 8.0 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t3-final-skeleton-arco-base semilla 1001: sprint → blue (4/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 193 de 193 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t3-final-skeleton-arco-base: enemy:arrow=88
- t3-final-skeleton-arco-quietos: enemy:arrow=11
- t3-final-skeleton-arco-volea: enemy:arrow=5
- t3-final-skeleton-linea-base: enemy:arrow=186
- t3-final-skeleton-linea-quietos: enemy:arrow=13
- t3-final-skeleton-linea-volea: enemy:arrow=14
- t3-final-skeleton-cab-base: enemy:arrow=179
- t3-final-skeleton-cab-quemarropa: enemy:arrow=59

## Motivos de decisión (cambios registrados): COUNTERATTACK 18, ENGAGE 5928, FORMATION_HOLD 59, LOW_HEALTH_RETREAT 695, MORALE_SHAKEN 3259, MORALE_WAVERING 1444, NONE 13, OUT_OF_RANGE 34, RALLIED 33, REFUSE_ATTACK 639, ROUTED_FLEE 808, SEEK_ENEMY 265, TOO_CLOSE 315, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
