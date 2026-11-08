# Simulación Rally - lote 20261108-195738-t24-techo-zombie-oratodo

Preset `t24-techo-zombie-oratodo` · velocidad `sprint` · 81 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t24-techo-zombie-arco-oratodo-r60 | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 42 ± 1 | 1480 ± 4 | 0 ± 0 | 91 % ± 1 | 9.2 ± 0.3 | 5.5 ± 0.3 | 0 |
| t24-techo-zombie-arco-oratodo-r85 | 20 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 58 ± 1 | 2104 ± 6 | 10 ± 3 | 93 % ± 1 | 9.4 ± 0.4 | 6.4 ± 0.5 | 0 |
| t24-techo-zombie-linea-oratodo-r55 | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.1 | 0.0 ± 0.0 | 41 ± 1 | 1332 ± 2 | 10 ± 2 | 93 % ± 1 | 6.7 ± 0.1 | 9.7 ± 0.3 | 0 |
| t24-techo-zombie-linea-oratodo-r75 | 20 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.1 | 0.0 ± 0.0 | 55 ± 1 | 1815 ± 5 | 23 ± 3 | 93 % ± 1 | 7.7 ± 0.4 | 11.0 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t24-techo-zombie-arco-oratodo-r60 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 80 de 80 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t24-techo-zombie-arco-oratodo-r85: enemy:mob=4
- t24-techo-zombie-linea-oratodo-r55: enemy:mob=1
- t24-techo-zombie-linea-oratodo-r75: enemy:mob=3

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 842, LOW_HEALTH_RETREAT 20, MORALE_SHAKEN 34, MORALE_WAVERING 13, OUT_OF_RANGE 50, RALLIED 1, REFUSE_ATTACK 5, ROUTED_FLEE 3, SEEK_ENEMY 9, TOO_CLOSE 159

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
