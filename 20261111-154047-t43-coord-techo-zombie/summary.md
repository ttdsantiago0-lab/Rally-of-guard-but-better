# Simulación Rally - lote 20261111-154047-t43-coord-techo-zombie

Preset `t43-coord-techo-zombie` · velocidad `sprint` · 65 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t43-techo-zombie-arco-base-r60-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 33 ± 0 | 1483 ± 4 | 0 ± 0 | 96 % ± 0 | 9.0 ± 0.1 | 5.2 ± 0.4 | 0 |
| t43-techo-zombie-arco-base-r60-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 30 ± 0 | 1465 ± 7 | 0 ± 0 | 99 % ± 0 | 8.8 ± 0.2 | 4.1 ± 0.2 | 0 |
| t43-techo-zombie-arco-base-r85-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 45 ± 1 | 2087 ± 5 | 0 ± 0 | 96 % ± 1 | 9.1 ± 0.3 | 6.9 ± 0.5 | 0 |
| t43-techo-zombie-arco-base-r85-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 41 ± 1 | 2066 ± 5 | 0 ± 0 | 99 % ± 0 | 8.7 ± 0.2 | 5.3 ± 0.6 | 0 |
| t43-techo-zombie-linea-base-r55-base | 8 | 100 % ± 0 | 0 % ± 0 | 6.6 ± 0.5 | 0.0 ± 0.0 | 40 ± 1 | 1356 ± 10 | 57 ± 7 | 96 % ± 1 | 7.6 ± 0.3 | 11.4 ± 0.7 | 0 |
| t43-techo-zombie-linea-base-r55-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 6.9 ± 0.4 | 0.0 ± 0.0 | 39 ± 1 | 1348 ± 9 | 57 ± 7 | 98 % ± 1 | 7.7 ± 0.4 | 10.8 ± 0.6 | 0 |
| t43-techo-zombie-linea-base-r75-base | 8 | 100 % ± 0 | 0 % ± 0 | 4.0 ± 0.2 | 0.0 ± 0.0 | 62 ± 2 | 1899 ± 9 | 95 ± 3 | 96 % ± 1 | 8.7 ± 0.8 | 12.0 ± 1.4 | 0 |
| t43-techo-zombie-linea-base-r75-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 5.4 ± 0.5 | 0.0 ± 0.0 | 57 ± 2 | 1860 ± 16 | 79 ± 7 | 98 % ± 0 | 8.4 ± 0.5 | 14.3 ± 1.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t43-techo-zombie-arco-base-r60-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t43-techo-zombie-linea-base-r55-base: enemy:mob=8
- t43-techo-zombie-linea-base-r55-sincro: enemy:mob=8
- t43-techo-zombie-linea-base-r75-base: enemy:mob=14
- t43-techo-zombie-linea-base-r75-sincro: enemy:mob=12

## Motivos de decisión (cambios registrados): ENGAGE 625, LOW_HEALTH_RETREAT 172, MORALE_SHAKEN 111, MORALE_WAVERING 58, NONE 1, OUT_OF_RANGE 30, RALLIED 2, ROUTED_FLEE 11, SEEK_ENEMY 30, TOO_CLOSE 27

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
