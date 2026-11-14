# Simulación Rally - lote 20261114-125416-t54-supresion-zombis60

Preset `t54-supresion-zombis60` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t54-supresion-zombis60-arco-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 30 ± 0 | 1461 ± 3 | 0 ± 0 | 99 % ± 0 | 8.8 ± 0.1 | 4.6 ± 0.2 | 0 |
| t54-supresion-zombis60-arco-retroceso | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 29 ± 0 | 1456 ± 3 | 0 ± 0 | 97 % ± 0 | 9.0 ± 0.0 | 3.9 ± 0.2 | 0 |
| t54-supresion-zombis60-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 38 ± 0 | 1440 ± 4 | 56 ± 3 | 98 % ± 0 | 6.8 ± 0.1 | 10.2 ± 0.3 | 0 |
| t54-supresion-zombis60-linea-retroceso | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 37 ± 1 | 1433 ± 4 | 55 ± 3 | 96 % ± 0 | 6.9 ± 0.2 | 10.0 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t54-supresion-zombis60-arco-base semilla 5401: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t54-supresion-zombis60-linea-base: enemy:mob=1
- t54-supresion-zombis60-linea-retroceso: enemy:mob=5

## Motivos de decisión (cambios registrados): ENGAGE 1009, LOW_HEALTH_RETREAT 74, OUT_OF_RANGE 24, SEEK_ENEMY 47, TOO_CLOSE 5

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
