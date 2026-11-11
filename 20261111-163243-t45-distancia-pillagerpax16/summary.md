# Simulación Rally - lote 20261111-163243-t45-distancia-pillagerpax16

Preset `t45-distancia-pillagerpax16` · velocidad `sprint` · 97 corridas · 12 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t45-distancia-pillagerpax16-arco-fija | 8 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.3 | 0.0 ± 0.0 | 21 ± 1 | 435 ± 4 | 24 ± 8 | 59 % ± 1 | 42.7 ± 0.5 | 10.8 ± 1.2 | 0 |
| t45-distancia-pillagerpax16-arco-aprendida | 8 | 100 % ± 0 | 0 % ± 0 | 7.1 ± 0.2 | 0.0 ± 0.0 | 15 ± 1 | 429 ± 6 | 61 ± 9 | 74 % ± 1 | 35.7 ± 0.2 | 5.8 ± 0.4 | 0 |
| t45-distancia-pillagerpax16-arco-sinregla | 8 | 25 % ± 16 | 75 % ± 16 | 0.6 ± 0.4 | 3.4 ± 1.0 | 15 ± 2 | 357 ± 18 | 220 ± 8 | 95 % ± 1 | 27.3 ± 0.4 | 3.7 ± 0.7 | 0 |
| t45-distancia-pillagerpax16-linea-fija | 8 | 100 % ± 0 | 0 % ± 0 | 7.1 ± 0.9 | 0.0 ± 0.0 | 25 ± 3 | 320 ± 17 | 47 ± 22 | 60 % ± 2 | 29.9 ± 1.6 | 17.9 ± 0.6 | 0 |
| t45-distancia-pillagerpax16-linea-aprendida | 8 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.2 | 0.0 ± 0.0 | 20 ± 1 | 363 ± 10 | 29 ± 6 | 75 % ± 2 | 25.1 ± 0.4 | 12.4 ± 0.3 | 0 |
| t45-distancia-pillagerpax16-linea-sinregla | 8 | 88 % ± 13 | 13 % ± 13 | 3.3 ± 0.6 | 0.5 ± 0.5 | 25 ± 2 | 278 ± 15 | 166 ± 8 | 96 % ± 1 | 13.6 ± 0.7 | 5.8 ± 0.7 | 0 |
| t45-distancia-pillagerpax16-lineascutum-fija | 8 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.2 | 0.0 ± 0.0 | 23 ± 1 | 309 ± 8 | 47 ± 6 | 59 % ± 1 | 30.0 ± 0.4 | 18.5 ± 0.4 | 0 |
| t45-distancia-pillagerpax16-lineascutum-aprendida | 8 | 100 % ± 0 | 0 % ± 0 | 7.0 ± 0.3 | 0.0 ± 0.0 | 19 ± 1 | 314 ± 16 | 45 ± 9 | 75 % ± 2 | 25.5 ± 0.5 | 11.8 ± 0.3 | 0 |
| t45-distancia-pillagerpax16-lineascutum-sinregla | 8 | 100 % ± 0 | 0 % ± 0 | 3.4 ± 0.5 | 0.0 ± 0.0 | 27 ± 3 | 278 ± 8 | 160 ± 12 | 95 % ± 1 | 13.0 ± 0.7 | 5.3 ± 1.0 | 0 |
| t45-distancia-pillagerpax16-cab-fija | 8 | 25 % ± 16 | 75 % ± 16 | 0.9 ± 0.6 | 4.5 ± 1.4 | 14 ± 1 | 226 ± 15 | 187 ± 12 | 95 % ± 2 | 10.0 ± 0.4 | 6.0 ± 0.8 | 0 |
| t45-distancia-pillagerpax16-cab-aprendida | 8 | 38 % ± 18 | 63 % ± 18 | 0.6 ± 0.4 | 4.3 ± 1.5 | 19 ± 2 | 259 ± 19 | 196 ± 5 | 91 % ± 2 | 16.1 ± 1.4 | 7.3 ± 1.4 | 0 |
| t45-distancia-pillagerpax16-cab-sinregla | 8 | 25 % ± 16 | 75 % ± 16 | 1.0 ± 0.9 | 6.0 ± 1.6 | 13 ± 1 | 208 ± 22 | 191 ± 15 | 95 % ± 1 | 11.5 ± 0.7 | 7.5 ± 0.7 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t45-distancia-pillagerpax16-arco-fija semilla 1: sprint → blue (8/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 72 de 72 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t45-distancia-pillagerpax16-arco-fija: enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.killed=1
- t45-distancia-pillagerpax16-arco-aprendida: enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.killed=3
- t45-distancia-pillagerpax16-arco-sinregla: enemy:scguns.bullet.annihilated=14 enemy:scguns.bullet.decimated=12 enemy:scguns.bullet.eliminated=13 enemy:scguns.bullet.executed=13 enemy:scguns.bullet.killed=7
- t45-distancia-pillagerpax16-linea-fija: enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=3
- t45-distancia-pillagerpax16-linea-aprendida: enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1
- t45-distancia-pillagerpax16-linea-sinregla: enemy:scguns.bullet.annihilated=8 enemy:scguns.bullet.decimated=9 enemy:scguns.bullet.eliminated=10 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=7
- t45-distancia-pillagerpax16-lineascutum-fija: enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t45-distancia-pillagerpax16-lineascutum-aprendida: enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=4
- t45-distancia-pillagerpax16-lineascutum-sinregla: enemy:scguns.bullet.annihilated=10 enemy:scguns.bullet.decimated=8 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=7
- t45-distancia-pillagerpax16-cab-fija: enemy:scguns.bullet.annihilated=12 enemy:scguns.bullet.decimated=6 enemy:scguns.bullet.eliminated=9 enemy:scguns.bullet.executed=13 enemy:scguns.bullet.killed=17
- t45-distancia-pillagerpax16-cab-aprendida: enemy:scguns.bullet.annihilated=13 enemy:scguns.bullet.decimated=14 enemy:scguns.bullet.eliminated=12 enemy:scguns.bullet.executed=14 enemy:scguns.bullet.killed=6
- t45-distancia-pillagerpax16-cab-sinregla: enemy:scguns.bullet.annihilated=15 enemy:scguns.bullet.decimated=11 enemy:scguns.bullet.eliminated=10 enemy:scguns.bullet.executed=11 enemy:scguns.bullet.killed=9

## Motivos de decisión (cambios registrados): ENGAGE 1283, FORMATION_HOLD 34, LOW_HEALTH_RETREAT 173, NONE 13, OUT_OF_RANGE 578, SEEK_ENEMY 34, TOO_CLOSE 8

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
