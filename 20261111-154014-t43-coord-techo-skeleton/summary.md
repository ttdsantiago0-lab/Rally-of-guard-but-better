# Simulación Rally - lote 20261111-154014-t43-coord-techo-skeleton

Preset `t43-coord-techo-skeleton` · velocidad `sprint` · 65 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t43-techo-skeleton-arco-base-r68-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.0 ± 0.9 | 0.0 ± 0.0 | 29 ± 3 | 1084 ± 13 | 118 ± 10 | 94 % ± 1 | 15.4 ± 0.2 | 5.9 ± 0.3 | 0 |
| t43-techo-skeleton-arco-base-r68-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 26 ± 0 | 1101 ± 13 | 107 ± 3 | 98 % ± 0 | 15.3 ± 0.2 | 5.5 ± 0.3 | 0 |
| t43-techo-skeleton-arco-base-r95-base | 8 | 38 % ± 18 | 63 % ± 18 | 1.8 ± 1.0 | 3.8 ± 1.5 | 41 ± 2 | 1184 ± 45 | 180 ± 6 | 96 % ± 0 | 15.0 ± 0.1 | 6.1 ± 0.7 | 0 |
| t43-techo-skeleton-arco-base-r95-sincro | 8 | 75 % ± 16 | 25 % ± 16 | 4.1 ± 1.1 | 0.3 ± 0.2 | 45 ± 3 | 1220 ± 27 | 163 ± 8 | 99 % ± 0 | 15.6 ± 0.5 | 7.0 ± 1.0 | 0 |
| t43-techo-skeleton-linea-base-r110-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 38 ± 1 | 858 ± 16 | 75 ± 5 | 85 % ± 1 | 13.2 ± 0.2 | 9.8 ± 0.3 | 0 |
| t43-techo-skeleton-linea-base-r110-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 45 ± 6 | 905 ± 13 | 73 ± 9 | 93 % ± 2 | 14.6 ± 0.8 | 11.1 ± 1.5 | 0 |
| t43-techo-skeleton-linea-base-r150-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 42 ± 1 | 951 ± 17 | 70 ± 5 | 84 % ± 2 | 13.1 ± 0.2 | 9.6 ± 0.4 | 0 |
| t43-techo-skeleton-linea-base-r150-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 40 ± 1 | 933 ± 22 | 77 ± 5 | 96 % ± 1 | 13.1 ± 0.2 | 8.9 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t43-techo-skeleton-arco-base-r68-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 57 de 57 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t43-techo-skeleton-arco-base-r68-base: enemy:arrow=8
- t43-techo-skeleton-arco-base-r68-sincro: enemy:arrow=2
- t43-techo-skeleton-arco-base-r95-base: enemy:arrow=50
- t43-techo-skeleton-arco-base-r95-sincro: enemy:arrow=31
- t43-techo-skeleton-linea-base-r150-base: enemy:arrow=1

## Motivos de decisión (cambios registrados): ENGAGE 590, LOW_HEALTH_RETREAT 466, MORALE_SHAKEN 98, MORALE_WAVERING 32, NONE 18, OUT_OF_RANGE 21, RALLIED 1, REFUSE_ATTACK 1, ROUTED_FLEE 6, SEEK_ENEMY 269, TOO_CLOSE 6

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
