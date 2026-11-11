# Simulación Rally - lote 20261111-215350-t48-distancia-pillagerpax16-flat

Preset `t48-distancia-pillagerpax16-flat` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t48-distancia-pillagerpax16-flat-arco-regla | 30 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.2 | 0.0 ± 0.0 | 22 ± 1 | 436 ± 2 | 25 ± 7 | 59 % ± 1 | 43.3 ± 0.2 | 12.6 ± 0.7 | 0 |
| t48-distancia-pillagerpax16-flat-arco-sinregla | 30 | 20 % ± 7 | 80 % ± 7 | 0.4 ± 0.2 | 4.4 ± 0.5 | 12 ± 1 | 331 ± 7 | 210 ± 3 | 94 % ± 1 | 27.0 ± 0.2 | 4.1 ± 0.4 | 0 |
| t48-distancia-pillagerpax16-flat-linea-regla | 30 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.1 | 0.0 ± 0.0 | 23 ± 1 | 318 ± 6 | 35 ± 4 | 59 % ± 1 | 29.6 ± 0.3 | 18.5 ± 0.2 | 0 |
| t48-distancia-pillagerpax16-flat-linea-sinregla | 30 | 80 % ± 7 | 20 % ± 7 | 3.0 ± 0.3 | 0.5 ± 0.2 | 28 ± 1 | 284 ± 5 | 174 ± 7 | 95 % ± 1 | 13.6 ± 0.4 | 6.1 ± 0.5 | 0 |
| t48-distancia-pillagerpax16-flat-lineascutum-regla | 30 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.1 | 0.0 ± 0.0 | 23 ± 0 | 323 ± 4 | 45 ± 4 | 61 % ± 1 | 29.7 ± 0.3 | 18.2 ± 0.2 | 0 |
| t48-distancia-pillagerpax16-flat-lineascutum-sinregla | 30 | 93 % ± 5 | 7 % ± 5 | 3.7 ± 0.3 | 0.1 ± 0.1 | 26 ± 1 | 283 ± 4 | 156 ± 5 | 95 % ± 1 | 13.3 ± 0.4 | 5.3 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t48-distancia-pillagerpax16-flat-arco-regla semilla 2001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 148 de 148 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t48-distancia-pillagerpax16-flat-arco-regla: enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=5
- t48-distancia-pillagerpax16-flat-arco-sinregla: enemy:scguns.bullet.annihilated=43 enemy:scguns.bullet.decimated=36 enemy:scguns.bullet.eliminated=49 enemy:scguns.bullet.executed=58 enemy:scguns.bullet.killed=41
- t48-distancia-pillagerpax16-flat-linea-regla: enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=3
- t48-distancia-pillagerpax16-flat-linea-sinregla: enemy:scguns.bullet.annihilated=25 enemy:scguns.bullet.decimated=35 enemy:scguns.bullet.eliminated=33 enemy:scguns.bullet.executed=26 enemy:scguns.bullet.killed=31
- t48-distancia-pillagerpax16-flat-lineascutum-regla: enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=4
- t48-distancia-pillagerpax16-flat-lineascutum-sinregla: enemy:scguns.bullet.annihilated=21 enemy:scguns.bullet.decimated=22 enemy:scguns.bullet.eliminated=22 enemy:scguns.bullet.executed=26 enemy:scguns.bullet.killed=37

## Motivos de decisión (cambios registrados): ENGAGE 2735, LOW_HEALTH_RETREAT 330, NONE 46, OUT_OF_RANGE 1599, SEEK_ENEMY 78

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
