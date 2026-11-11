# Simulación Rally - lote 20261111-215324-t48-distancia-pillagerpax32-flat

Preset `t48-distancia-pillagerpax32-flat` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t48-distancia-pillagerpax32-flat-arco-regla | 30 | 57 % ± 9 | 43 % ± 9 | 2.4 ± 0.5 | 4.4 ± 1.0 | 33 ± 1 | 715 ± 21 | 182 ± 8 | 73 % ± 1 | 39.3 ± 0.3 | 13.9 ± 1.2 | 0 |
| t48-distancia-pillagerpax32-flat-arco-sinregla | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 24.6 ± 0.3 | 7 ± 0 | 227 ± 4 | 200 ± 2 | 92 % ± 1 | 27.3 ± 0.1 | 3.4 ± 0.1 | 0 |
| t48-distancia-pillagerpax32-flat-linea-regla | 30 | 97 % ± 3 | 3 % ± 3 | 5.9 ± 0.4 | 0.1 ± 0.1 | 27 ± 1 | 368 ± 11 | 75 ± 6 | 66 % ± 1 | 32.4 ± 0.6 | 16.8 ± 0.7 | 0 |
| t48-distancia-pillagerpax32-flat-linea-sinregla | 30 | 43 % ± 9 | 57 % ± 9 | 1.5 ± 0.3 | 2.9 ± 0.6 | 27 ± 1 | 202 ± 10 | 198 ± 7 | 92 % ± 1 | 15.6 ± 0.6 | 4.0 ± 0.3 | 0 |
| t48-distancia-pillagerpax32-flat-lineascutum-regla | 30 | 100 % ± 0 | 0 % ± 0 | 6.7 ± 0.2 | 0.0 ± 0.0 | 25 ± 1 | 366 ± 8 | 56 ± 5 | 69 % ± 1 | 32.3 ± 0.6 | 17.0 ± 0.4 | 0 |
| t48-distancia-pillagerpax32-flat-lineascutum-sinregla | 30 | 87 % ± 6 | 13 % ± 6 | 3.1 ± 0.3 | 0.4 ± 0.2 | 29 ± 1 | 224 ± 7 | 162 ± 5 | 92 % ± 1 | 13.7 ± 0.4 | 3.5 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t48-distancia-pillagerpax32-flat-arco-regla semilla 2001: sprint → blue (8/0 vivos), normal → blue (3/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 115 de 115 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t48-distancia-pillagerpax32-flat-arco-regla: enemy:scguns.bullet.annihilated=34 enemy:scguns.bullet.decimated=33 enemy:scguns.bullet.eliminated=27 enemy:scguns.bullet.executed=27 enemy:scguns.bullet.killed=47
- t48-distancia-pillagerpax32-flat-arco-sinregla: enemy:scguns.bullet.annihilated=52 enemy:scguns.bullet.decimated=48 enemy:scguns.bullet.eliminated=44 enemy:scguns.bullet.executed=48 enemy:scguns.bullet.killed=48
- t48-distancia-pillagerpax32-flat-linea-regla: enemy:scguns.bullet.annihilated=13 enemy:scguns.bullet.decimated=14 enemy:scguns.bullet.eliminated=14 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=12 other(minecraft:pillager):scguns.bullet.eliminated=1 other(minecraft:pillager):scguns.bullet.killed=1
- t48-distancia-pillagerpax32-flat-linea-sinregla: enemy:scguns.bullet.annihilated=46 enemy:scguns.bullet.decimated=34 enemy:scguns.bullet.eliminated=42 enemy:scguns.bullet.executed=36 enemy:scguns.bullet.killed=38
- t48-distancia-pillagerpax32-flat-lineascutum-regla: enemy:scguns.bullet.annihilated=8 enemy:scguns.bullet.decimated=10 enemy:scguns.bullet.eliminated=7 enemy:scguns.bullet.executed=5 enemy:scguns.bullet.killed=10
- t48-distancia-pillagerpax32-flat-lineascutum-sinregla: enemy:scguns.bullet.annihilated=29 enemy:scguns.bullet.decimated=37 enemy:scguns.bullet.eliminated=39 enemy:scguns.bullet.executed=21 enemy:scguns.bullet.killed=21

## Motivos de decisión (cambios registrados): ENGAGE 2842, FORMATION_HOLD 5, LOW_HEALTH_RETREAT 417, NONE 41, OUT_OF_RANGE 1605, SEEK_ENEMY 74, STUCK 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
