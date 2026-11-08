# Simulación Rally - lote 20261108-193142-t23-techo-zombie-orakite

Preset `t23-techo-zombie-orakite` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t23-techo-zombie-arco-orakite-r40 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 32 ± 0 | 983 ± 2 | 0 ± 0 | 94 % ± 0 | 8.7 ± 0.1 | 4.3 ± 0.2 | 0 |
| t23-techo-zombie-linea-orakite-r38 | 30 | 100 % ± 0 | 0 % ± 0 | 6.7 ± 0.2 | 0.0 ± 0.0 | 37 ± 1 | 945 ± 5 | 53 ± 3 | 93 % ± 1 | 7.7 ± 0.2 | 10.4 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t23-techo-zombie-arco-orakite-r40 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t23-techo-zombie-linea-orakite-r38: enemy:mob=20

## Motivos de decisión (cambios registrados): ENGAGE 561, LOW_HEALTH_RETREAT 91, MORALE_SHAKEN 82, MORALE_WAVERING 35, NONE 1, OUT_OF_RANGE 13, REFUSE_ATTACK 1, ROUTED_FLEE 5, SEEK_ENEMY 15, TOO_CLOSE 5

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
