# Simulación Rally - lote 20261108-193242-t23-techo-zombie-oracadencia

Preset `t23-techo-zombie-oracadencia` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t23-techo-zombie-arco-oracadencia-r40 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 22 ± 0 | 989 ± 3 | 38 ± 3 | 95 % ± 0 | 5.9 ± 0.1 | 4.3 ± 0.2 | 0 |
| t23-techo-zombie-linea-oracadencia-r38 | 30 | 100 % ± 0 | 0 % ± 0 | 7.0 ± 0.2 | 0.0 ± 0.0 | 29 ± 1 | 941 ± 5 | 75 ± 4 | 94 % ± 1 | 5.6 ± 0.2 | 7.7 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t23-techo-zombie-arco-oracadencia-r40 semilla 1: sprint → blue (8/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t23-techo-zombie-linea-oracadencia-r38: enemy:mob=16

## Motivos de decisión (cambios registrados): ENGAGE 1347, LOW_HEALTH_RETREAT 106, MORALE_SHAKEN 29, MORALE_WAVERING 9, NONE 1, OUT_OF_RANGE 1, SEEK_ENEMY 44, TOO_CLOSE 917

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
