# Simulación Rally - lote 20261111-215554-t48-distancia-pillagerpax16-forest

Preset `t48-distancia-pillagerpax16-forest` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t48-distancia-pillagerpax16-forest-arco-regla | 30 | 100 % ± 0 | 0 % ± 0 | 6.0 ± 0.2 | 0.0 ± 0.0 | 27 ± 1 | 410 ± 5 | 89 ± 7 | 40 % ± 1 | 41.6 ± 0.4 | 19.0 ± 1.0 | 0 |
| t48-distancia-pillagerpax16-forest-arco-sinregla | 30 | 17 % ± 7 | 83 % ± 7 | 0.5 ± 0.2 | 6.1 ± 0.6 | 13 ± 1 | 286 ± 10 | 222 ± 7 | 81 % ± 1 | 18.4 ± 0.5 | 5.0 ± 0.3 | 0 |
| t48-distancia-pillagerpax16-forest-linea-regla | 30 | 100 % ± 0 | 0 % ± 0 | 7.1 ± 0.2 | 0.0 ± 0.0 | 26 ± 1 | 243 ± 7 | 63 ± 5 | 28 % ± 2 | 27.3 ± 0.3 | 19.8 ± 0.3 | 0 |
| t48-distancia-pillagerpax16-forest-linea-sinregla | 30 | 80 % ± 7 | 20 % ± 7 | 2.9 ± 0.3 | 0.9 ± 0.4 | 28 ± 1 | 243 ± 7 | 177 ± 6 | 76 % ± 1 | 10.6 ± 0.3 | 4.5 ± 0.3 | 0 |
| t48-distancia-pillagerpax16-forest-lineascutum-regla | 30 | 100 % ± 0 | 0 % ± 0 | 6.7 ± 0.3 | 0.0 ± 0.0 | 28 ± 1 | 249 ± 4 | 70 ± 6 | 30 % ± 2 | 28.1 ± 0.5 | 19.4 ± 0.5 | 0 |
| t48-distancia-pillagerpax16-forest-lineascutum-sinregla | 30 | 80 % ± 7 | 20 % ± 7 | 3.0 ± 0.3 | 0.9 ± 0.4 | 27 ± 2 | 245 ± 7 | 171 ± 7 | 77 % ± 1 | 10.7 ± 0.3 | 4.4 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t48-distancia-pillagerpax16-forest-arco-regla semilla 2001: sprint → blue (6/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 143 de 143 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t48-distancia-pillagerpax16-forest-arco-regla: enemy:scguns.bullet.annihilated=10 enemy:scguns.bullet.decimated=13 enemy:scguns.bullet.eliminated=11 enemy:scguns.bullet.executed=15 enemy:scguns.bullet.killed=10
- t48-distancia-pillagerpax16-forest-arco-sinregla: enemy:scguns.bullet.annihilated=54 enemy:scguns.bullet.decimated=33 enemy:scguns.bullet.eliminated=43 enemy:scguns.bullet.executed=48 enemy:scguns.bullet.killed=47
- t48-distancia-pillagerpax16-forest-linea-regla: enemy:scguns.bullet.annihilated=7 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=6 enemy:scguns.bullet.executed=7 enemy:scguns.bullet.killed=3
- t48-distancia-pillagerpax16-forest-linea-sinregla: enemy:scguns.bullet.annihilated=28 enemy:scguns.bullet.decimated=33 enemy:scguns.bullet.eliminated=32 enemy:scguns.bullet.executed=28 enemy:scguns.bullet.killed=31
- t48-distancia-pillagerpax16-forest-lineascutum-regla: enemy:scguns.bullet.annihilated=9 enemy:scguns.bullet.decimated=6 enemy:scguns.bullet.eliminated=7 enemy:scguns.bullet.executed=9 enemy:scguns.bullet.killed=9
- t48-distancia-pillagerpax16-forest-lineascutum-sinregla: enemy:scguns.bullet.annihilated=30 enemy:scguns.bullet.decimated=26 enemy:scguns.bullet.eliminated=32 enemy:scguns.bullet.executed=32 enemy:scguns.bullet.killed=30

## Motivos de decisión (cambios registrados): ENGAGE 2744, FORMATION_HOLD 3, LOW_HEALTH_RETREAT 460, NONE 43, OUT_OF_RANGE 1522, SEEK_ENEMY 109, STUCK 2, TOO_CLOSE 42

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
