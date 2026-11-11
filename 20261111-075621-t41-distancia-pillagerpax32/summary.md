# Simulación Rally - lote 20261111-075621-t41-distancia-pillagerpax32

Preset `t41-distancia-pillagerpax32` · velocidad `sprint` · 81 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t41-distancia-pillagerpax32-linea-regla | 10 | 100 % ± 0 | 0 % ± 0 | 4.8 ± 0.8 | 0.0 ± 0.0 | 29 ± 2 | 390 ± 15 | 104 ± 16 | 62 % ± 2 | 33.9 ± 1.2 | 16.1 ± 1.2 | 0 |
| t41-distancia-pillagerpax32-linea-sinregla | 10 | 40 % ± 16 | 60 % ± 16 | 1.5 ± 0.6 | 4.3 ± 1.7 | 25 ± 2 | 157 ± 22 | 191 ± 6 | 85 % ± 3 | 16.5 ± 1.3 | 4.3 ± 1.1 | 0 |
| t41-distancia-pillagerpax32-arco-regla | 10 | 80 % ± 13 | 20 % ± 13 | 3.1 ± 0.7 | 0.7 ± 0.5 | 33 ± 1 | 721 ± 20 | 168 ± 17 | 69 % ± 1 | 39.2 ± 0.8 | 11.4 ± 2.1 | 0 |
| t41-distancia-pillagerpax32-arco-sinregla | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 26.6 ± 0.7 | 7 ± 0 | 131 ± 7 | 199 ± 4 | 84 % ± 3 | 26.7 ± 0.2 | 3.8 ± 0.5 | 0 |
| t41-distancia-pillagerpax32-lineascutum-regla | 10 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.2 | 0.0 ± 0.0 | 24 ± 1 | 327 ± 11 | 37 ± 8 | 58 % ± 2 | 29.6 ± 0.4 | 16.8 ± 0.5 | 0 |
| t41-distancia-pillagerpax32-lineascutum-sinregla | 10 | 100 % ± 0 | 0 % ± 0 | 3.3 ± 0.3 | 0.0 ± 0.0 | 32 ± 1 | 191 ± 16 | 165 ± 7 | 85 % ± 5 | 12.7 ± 0.6 | 3.9 ± 0.8 | 0 |
| t41-distancia-pillagerpax32-escudo-regla | 10 | 90 % ± 10 | 10 % ± 10 | 6.0 ± 0.8 | 0.4 ± 0.4 | 30 ± 1 | 190 ± 12 | 120 ± 14 | 0 % ± 0 | 10.2 ± 0.2 | 5.1 ± 0.3 | 0 |
| t41-distancia-pillagerpax32-escudo-sinregla | 10 | 100 % ± 0 | 0 % ± 0 | 7.0 ± 0.2 | 0.0 ± 0.0 | 29 ± 1 | 194 ± 9 | 105 ± 12 | 0 % ± 0 | 10.0 ± 0.2 | 5.0 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t41-distancia-pillagerpax32-linea-regla semilla 1: sprint → blue (3/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 61 de 61 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t41-distancia-pillagerpax32-linea-regla: enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=9 enemy:scguns.bullet.killed=6
- t41-distancia-pillagerpax32-linea-sinregla: enemy:scguns.bullet.annihilated=14 enemy:scguns.bullet.decimated=11 enemy:scguns.bullet.eliminated=11 enemy:scguns.bullet.executed=13 enemy:scguns.bullet.killed=16
- t41-distancia-pillagerpax32-arco-regla: enemy:scguns.bullet.annihilated=11 enemy:scguns.bullet.decimated=9 enemy:scguns.bullet.eliminated=10 enemy:scguns.bullet.executed=13 enemy:scguns.bullet.killed=6
- t41-distancia-pillagerpax32-arco-sinregla: enemy:scguns.bullet.annihilated=14 enemy:scguns.bullet.decimated=17 enemy:scguns.bullet.eliminated=14 enemy:scguns.bullet.executed=17 enemy:scguns.bullet.killed=18
- t41-distancia-pillagerpax32-lineascutum-regla: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.executed=1
- t41-distancia-pillagerpax32-lineascutum-sinregla: enemy:scguns.bullet.annihilated=11 enemy:scguns.bullet.decimated=14 enemy:scguns.bullet.eliminated=9 enemy:scguns.bullet.executed=9 enemy:scguns.bullet.killed=4
- t41-distancia-pillagerpax32-escudo-regla: enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=5 enemy:scguns.bullet.executed=6 enemy:scguns.bullet.killed=2
- t41-distancia-pillagerpax32-escudo-sinregla: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=4

## Motivos de decisión (cambios registrados): ENGAGE 1160, LOW_HEALTH_RETREAT 160, NONE 19, OUT_OF_RANGE 586, SEEK_ENEMY 84, STUCK 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
