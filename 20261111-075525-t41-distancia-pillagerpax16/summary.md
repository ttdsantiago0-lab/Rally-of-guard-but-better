# Simulación Rally - lote 20261111-075525-t41-distancia-pillagerpax16

Preset `t41-distancia-pillagerpax16` · velocidad `sprint` · 81 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t41-distancia-pillagerpax16-linea-regla | 10 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 21 ± 1 | 266 ± 11 | 32 ± 6 | 53 % ± 2 | 30.1 ± 0.4 | 17.9 ± 0.4 | 0 |
| t41-distancia-pillagerpax16-linea-sinregla | 10 | 90 % ± 10 | 10 % ± 10 | 2.8 ± 0.5 | 0.6 ± 0.6 | 29 ± 1 | 208 ± 12 | 185 ± 8 | 85 % ± 1 | 12.2 ± 0.4 | 4.4 ± 0.6 | 0 |
| t41-distancia-pillagerpax16-arco-regla | 10 | 100 % ± 0 | 0 % ± 0 | 6.7 ± 0.3 | 0.0 ± 0.0 | 24 ± 1 | 422 ± 3 | 50 ± 6 | 55 % ± 1 | 41.3 ± 0.8 | 11.7 ± 1.3 | 0 |
| t41-distancia-pillagerpax16-arco-sinregla | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 12.9 ± 0.4 | 7 ± 0 | 144 ± 9 | 203 ± 4 | 87 % ± 1 | 26.2 ± 0.2 | 4.0 ± 0.3 | 0 |
| t41-distancia-pillagerpax16-lineascutum-regla | 10 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 21 ± 0 | 282 ± 12 | 34 ± 5 | 53 % ± 2 | 29.2 ± 0.4 | 17.6 ± 0.3 | 0 |
| t41-distancia-pillagerpax16-lineascutum-sinregla | 10 | 80 % ± 13 | 20 % ± 13 | 2.7 ± 0.5 | 0.7 ± 0.5 | 32 ± 2 | 196 ± 11 | 179 ± 10 | 82 % ± 2 | 11.9 ± 0.4 | 4.3 ± 0.6 | 0 |
| t41-distancia-pillagerpax16-escudo-regla | 10 | 100 % ± 0 | 0 % ± 0 | 7.0 ± 0.4 | 0.0 ± 0.0 | 31 ± 1 | 215 ± 6 | 94 ± 12 | 0 % ± 0 | 9.5 ± 0.1 | 5.6 ± 0.2 | 0 |
| t41-distancia-pillagerpax16-escudo-sinregla | 10 | 100 % ± 0 | 0 % ± 0 | 6.7 ± 0.4 | 0.0 ± 0.0 | 30 ± 1 | 205 ± 5 | 112 ± 10 | 0 % ± 0 | 9.4 ± 0.2 | 5.2 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t41-distancia-pillagerpax16-linea-regla semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 67 de 67 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t41-distancia-pillagerpax16-linea-regla: enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1
- t41-distancia-pillagerpax16-linea-sinregla: enemy:scguns.bullet.annihilated=10 enemy:scguns.bullet.decimated=13 enemy:scguns.bullet.eliminated=15 enemy:scguns.bullet.executed=6 enemy:scguns.bullet.killed=8
- t41-distancia-pillagerpax16-arco-regla: enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=1
- t41-distancia-pillagerpax16-arco-sinregla: enemy:scguns.bullet.annihilated=11 enemy:scguns.bullet.decimated=22 enemy:scguns.bullet.eliminated=17 enemy:scguns.bullet.executed=15 enemy:scguns.bullet.killed=15
- t41-distancia-pillagerpax16-lineascutum-regla: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.eliminated=1
- t41-distancia-pillagerpax16-lineascutum-sinregla: enemy:scguns.bullet.annihilated=12 enemy:scguns.bullet.decimated=11 enemy:scguns.bullet.eliminated=11 enemy:scguns.bullet.executed=12 enemy:scguns.bullet.killed=7
- t41-distancia-pillagerpax16-escudo-regla: enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=1
- t41-distancia-pillagerpax16-escudo-sinregla: enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=2

## Motivos de decisión (cambios registrados): ENGAGE 1113, LOW_HEALTH_RETREAT 138, NONE 13, OUT_OF_RANGE 543, SEEK_ENEMY 84, STUCK 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
