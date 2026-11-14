# Simulación Rally - lote 20261114-192814-t55b-relevo-vindicadores32

Preset `t55b-relevo-vindicadores32` · velocidad `sprint` · 117 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t55b-relevo-vindicadores32-linea-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 4.0 ± 0.0 | 0.0 ± 0.0 | 37 ± 1 | 821 ± 9 | 110 ± 2 | 85 % ± 1 | 16.7 ± 0.8 | 15.7 ± 0.9 | 0 |
| t55b-relevo-vindicadores32-linea-huida | 20 | 35 % ± 5 | 65 % ± 5 | 1.4 ± 0.2 | 19.6 ± 0.6 | 98 ± 8 | 477 ± 11 | 207 ± 4 | 96 % ± 1 | 8.0 ± 0.3 | 28.2 ± 3.1 | 26 |
| t55b-relevo-vindicadores32-linea-relevonocarga | 27 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 21.8 ± 0.8 | 28 ± 4 | 420 ± 9 | 221 ± 3 | 94 % ± 1 | 9.7 ± 0.6 | 8.1 ± 0.8 | 13 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t55b-relevo-vindicadores32-linea-pelear semilla 5501: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 44 de 44 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t55b-relevo-vindicadores32-linea-pelear: enemy:mob=118 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.executed=1
- t55b-relevo-vindicadores32-linea-huida: enemy:mob=127 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.killed=3
- t55b-relevo-vindicadores32-linea-relevonocarga: enemy:mob=211 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=1

## Motivos de decisión (cambios registrados): ENGAGE 1262, FORMATION_HOLD 31, INSTRUCTION 366, LOW_HEALTH_RETREAT 117, NONE 8, ORDER_OVERRIDE 62, OUT_OF_RANGE 197, SEEK_ENEMY 349, TOO_CLOSE 71

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t55b-relevo-vindicadores32-linea-relevonocarga - 13 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t55b-relevo-vindicadores32-linea-huida - 26 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t55b-relevo-vindicadores32-linea-huida: 14/20 corridas llegaron al tope de tiempo
