# Simulación Rally - lote 20261114-125352-t54-supresion-devastadores4

Preset `t54-supresion-devastadores4` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t54-supresion-devastadores4-arco-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 15 ± 0 | 413 ± 1 | 0 ± 0 | 97 % ± 0 | 10.2 ± 0.3 | 9.6 ± 0.2 | 0 |
| t54-supresion-devastadores4-arco-retroceso | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 15 ± 0 | 416 ± 1 | 0 ± 0 | 97 % ± 0 | 9.4 ± 0.1 | 6.2 ± 0.3 | 0 |
| t54-supresion-devastadores4-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.1 | 0.0 ± 0.0 | 18 ± 0 | 414 ± 1 | 30 ± 3 | 94 % ± 1 | 9.6 ± 0.4 | 10.0 ± 0.2 | 0 |
| t54-supresion-devastadores4-linea-retroceso | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 17 ± 0 | 416 ± 1 | 23 ± 3 | 95 % ± 1 | 8.5 ± 0.2 | 7.5 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t54-supresion-devastadores4-arco-base semilla 5401: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t54-supresion-devastadores4-linea-base: enemy:mob=17
- t54-supresion-devastadores4-linea-retroceso: enemy:mob=7

## Motivos de decisión (cambios registrados): ENGAGE 997, LOW_HEALTH_RETREAT 45, OUT_OF_RANGE 3, SEEK_ENEMY 31, TOO_CLOSE 30

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
