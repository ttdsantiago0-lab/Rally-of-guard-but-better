# Simulación Rally - lote 20261114-125418-t54-supresion-vindicadores32

Preset `t54-supresion-vindicadores32` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t54-supresion-vindicadores32-arco-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.0 | 0.0 ± 0.0 | 22 ± 0 | 854 ± 5 | 10 ± 2 | 91 % ± 1 | 13.6 ± 0.7 | 8.7 ± 0.6 | 0 |
| t54-supresion-vindicadores32-arco-retroceso | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 1 | 864 ± 5 | 14 ± 2 | 82 % ± 1 | 17.0 ± 0.9 | 9.4 ± 0.6 | 0 |
| t54-supresion-vindicadores32-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 3.5 ± 0.1 | 0.0 ± 0.0 | 36 ± 1 | 833 ± 7 | 122 ± 3 | 89 % ± 1 | 14.2 ± 0.7 | 14.7 ± 0.9 | 0 |
| t54-supresion-vindicadores32-linea-retroceso | 30 | 100 % ± 0 | 0 % ± 0 | 3.7 ± 0.1 | 0.0 ± 0.0 | 37 ± 1 | 825 ± 7 | 125 ± 4 | 83 % ± 1 | 14.5 ± 0.6 | 14.9 ± 1.0 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t54-supresion-vindicadores32-arco-base semilla 5401: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t54-supresion-vindicadores32-arco-base: enemy:mob=1 enemy:scguns.bullet.executed=1
- t54-supresion-vindicadores32-linea-base: enemy:mob=131 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.executed=1
- t54-supresion-vindicadores32-linea-retroceso: enemy:mob=124 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=2

## Motivos de decisión (cambios registrados): ENGAGE 1264, LOW_HEALTH_RETREAT 39, NONE 25, OUT_OF_RANGE 490, SEEK_ENEMY 11, TOO_CLOSE 84

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
