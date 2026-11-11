# Simulación Rally - lote 20261111-215320-t48-distancia-pillagerpax32-forest

Preset `t48-distancia-pillagerpax32-forest` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t48-distancia-pillagerpax32-forest-arco-regla | 30 | 33 % ± 9 | 67 % ± 9 | 0.8 ± 0.2 | 7.9 ± 1.4 | 32 ± 1 | 579 ± 21 | 213 ± 5 | 56 % ± 1 | 38.5 ± 0.2 | 14.9 ± 1.0 | 0 |
| t48-distancia-pillagerpax32-forest-arco-sinregla | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 22.5 ± 0.6 | 9 ± 0 | 179 ± 6 | 208 ± 3 | 71 % ± 1 | 21.3 ± 0.3 | 5.2 ± 0.2 | 0 |
| t48-distancia-pillagerpax32-forest-linea-regla | 30 | 100 % ± 0 | 0 % ± 0 | 5.8 ± 0.3 | 0.0 ± 0.0 | 32 ± 1 | 286 ± 9 | 85 ± 5 | 33 % ± 1 | 29.0 ± 0.5 | 18.5 ± 0.7 | 0 |
| t48-distancia-pillagerpax32-forest-linea-sinregla | 30 | 43 % ± 9 | 57 % ± 9 | 1.4 ± 0.3 | 3.2 ± 0.6 | 29 ± 1 | 186 ± 9 | 196 ± 6 | 68 % ± 2 | 10.5 ± 0.3 | 3.4 ± 0.2 | 0 |
| t48-distancia-pillagerpax32-forest-lineascutum-regla | 30 | 100 % ± 0 | 0 % ± 0 | 5.7 ± 0.3 | 0.0 ± 0.0 | 33 ± 2 | 277 ± 10 | 87 ± 5 | 32 % ± 2 | 29.7 ± 0.5 | 18.8 ± 0.5 | 0 |
| t48-distancia-pillagerpax32-forest-lineascutum-sinregla | 30 | 53 % ± 9 | 47 % ± 9 | 1.7 ± 0.3 | 2.3 ± 0.5 | 34 ± 1 | 214 ± 9 | 201 ± 5 | 69 % ± 2 | 9.9 ± 0.2 | 3.9 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t48-distancia-pillagerpax32-forest-arco-regla semilla 2001: sprint → red (0/11 vivos), normal → red (0/8 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 99 de 99 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t48-distancia-pillagerpax32-forest-arco-regla: enemy:scguns.bullet.annihilated=38 enemy:scguns.bullet.decimated=36 enemy:scguns.bullet.eliminated=40 enemy:scguns.bullet.executed=59 enemy:scguns.bullet.killed=44
- t48-distancia-pillagerpax32-forest-arco-sinregla: enemy:scguns.bullet.annihilated=60 enemy:scguns.bullet.decimated=43 enemy:scguns.bullet.eliminated=50 enemy:scguns.bullet.executed=41 enemy:scguns.bullet.killed=46
- t48-distancia-pillagerpax32-forest-linea-regla: enemy:scguns.bullet.annihilated=17 enemy:scguns.bullet.decimated=10 enemy:scguns.bullet.eliminated=15 enemy:scguns.bullet.executed=9 enemy:scguns.bullet.killed=15
- t48-distancia-pillagerpax32-forest-linea-sinregla: enemy:scguns.bullet.annihilated=41 enemy:scguns.bullet.decimated=33 enemy:scguns.bullet.eliminated=41 enemy:scguns.bullet.executed=37 enemy:scguns.bullet.killed=45
- t48-distancia-pillagerpax32-forest-lineascutum-regla: enemy:scguns.bullet.annihilated=12 enemy:scguns.bullet.decimated=15 enemy:scguns.bullet.eliminated=10 enemy:scguns.bullet.executed=19 enemy:scguns.bullet.killed=13
- t48-distancia-pillagerpax32-forest-lineascutum-sinregla: enemy:scguns.bullet.annihilated=48 enemy:scguns.bullet.decimated=34 enemy:scguns.bullet.eliminated=38 enemy:scguns.bullet.executed=36 enemy:scguns.bullet.killed=33

## Motivos de decisión (cambios registrados): ENGAGE 2856, LOW_HEALTH_RETREAT 493, NONE 31, OUT_OF_RANGE 1598, SEEK_ENEMY 81, STUCK 2, TOO_CLOSE 5, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
