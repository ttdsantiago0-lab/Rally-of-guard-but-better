# Simulación Rally - lote 20261114-190626-t55-relevo-vindicadores32

Preset `t55-relevo-vindicadores32` · velocidad `sprint` · 114 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t55-relevo-vindicadores32-linea-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 4.0 ± 0.0 | 0.0 ± 0.0 | 38 ± 1 | 816 ± 9 | 106 ± 1 | 84 % ± 1 | 17.8 ± 0.9 | 16.0 ± 1.1 | 0 |
| t55-relevo-vindicadores32-linea-relevo | 26 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 18.8 ± 0.6 | 36 ± 4 | 467 ± 10 | 230 ± 2 | 94 % ± 1 | 10.1 ± 0.5 | 8.8 ± 0.9 | 13 |
| t55-relevo-vindicadores32-linea-huida | 22 | 39 % ± 5 | 61 % ± 5 | 1.4 ± 0.2 | 20.2 ± 0.7 | 115 ± 3 | 481 ± 12 | 224 ± 7 | 96 % ± 1 | 9.2 ± 0.8 | 34.1 ± 2.1 | 22 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t55-relevo-vindicadores32-linea-pelear semilla 5501: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 47 de 47 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t55-relevo-vindicadores32-linea-pelear: enemy:mob=117 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=1
- t55-relevo-vindicadores32-linea-relevo: enemy:mob=204 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.killed=1
- t55-relevo-vindicadores32-linea-huida: enemy:mob=136 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1

## Motivos de decisión (cambios registrados): ENGAGE 1300, FORMATION_HOLD 51, INSTRUCTION 349, LOW_HEALTH_RETREAT 109, NONE 4, ORDER_OVERRIDE 60, OUT_OF_RANGE 204, SEEK_ENEMY 390, TOO_CLOSE 83

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t55-relevo-vindicadores32-linea-huida - 22 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t55-relevo-vindicadores32-linea-relevo - 13 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t55-relevo-vindicadores32-linea-huida: 17/22 corridas llegaron al tope de tiempo
