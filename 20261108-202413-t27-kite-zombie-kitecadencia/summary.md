# Simulación Rally - lote 20261108-202413-t27-kite-zombie-kitecadencia

Preset `t27-kite-zombie-kitecadencia` · velocidad `sprint` · 81 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-kite-zombie-arco-kitecadencia-r60 | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 34 ± 0 | 1477 ± 3 | 0 ± 0 | 95 % ± 1 | 8.9 ± 0.1 | 4.4 ± 0.2 | 0 |
| t27-kite-zombie-arco-kitecadencia-r85 | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 45 ± 1 | 2092 ± 5 | 1 ± 1 | 96 % ± 0 | 9.0 ± 0.2 | 6.3 ± 0.3 | 0 |
| t27-kite-zombie-linea-kitecadencia-r55 | 20 | 100 % ± 0 | 0 % ± 0 | 6.0 ± 0.3 | 0.0 ± 0.0 | 43 ± 1 | 1375 ± 7 | 67 ± 4 | 94 % ± 1 | 8.1 ± 0.3 | 11.4 ± 0.5 | 0 |
| t27-kite-zombie-linea-kitecadencia-r75 | 20 | 100 % ± 0 | 0 % ± 0 | 5.2 ± 0.3 | 0.0 ± 0.0 | 58 ± 1 | 1876 ± 8 | 83 ± 5 | 96 % ± 1 | 8.0 ± 0.2 | 13.3 ± 0.7 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-kite-zombie-arco-kitecadencia-r60 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 80 de 80 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-kite-zombie-linea-kitecadencia-r55: enemy:mob=17
- t27-kite-zombie-linea-kitecadencia-r75: enemy:mob=39

## Motivos de decisión (cambios registrados): ENGAGE 805, LOW_HEALTH_RETREAT 217, MORALE_SHAKEN 146, MORALE_WAVERING 84, NONE 2, OUT_OF_RANGE 51, RALLIED 4, REFUSE_ATTACK 5, ROUTED_FLEE 19, SEEK_ENEMY 35, TOO_CLOSE 44

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
