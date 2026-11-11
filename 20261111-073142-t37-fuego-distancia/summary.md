# Simulación Rally - lote 20261111-073142-t37-fuego-distancia

Preset `t37-fuego-distancia` · velocidad `sprint` · 97 corridas · 12 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t37-dist-pax16-linea-volea40 | 8 | 100 % ± 0 | 0 % ± 0 | 6.4 ± 0.3 | 0.0 ± 0.0 | 21 ± 1 | 330 ± 12 | 92 ± 13 | 74 % ± 1 | 23.3 ± 0.4 | 7.0 ± 0.3 | 0 |
| t37-dist-pax16-linea-volea50 | 8 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.2 | 0.0 ± 0.0 | 21 ± 1 | 325 ± 13 | 61 ± 9 | 68 % ± 2 | 29.4 ± 0.7 | 7.5 ± 0.4 | 0 |
| t37-dist-pax16-linea-volea60 | 8 | 100 % ± 0 | 0 % ± 0 | 7.3 ± 0.2 | 0.0 ± 0.0 | 25 ± 1 | 343 ± 14 | 62 ± 8 | 61 % ± 1 | 31.8 ± 0.4 | 8.0 ± 0.2 | 0 |
| t37-dist-pax16-linea-volea70 | 8 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.4 | 0.0 ± 0.0 | 27 ± 1 | 336 ± 7 | 41 ± 11 | 54 % ± 2 | 37.5 ± 0.5 | 8.5 ± 0.5 | 0 |
| t37-dist-pax16-linea-volea80 | 8 | 100 % ± 0 | 0 % ± 0 | 7.3 ± 0.3 | 0.0 ± 0.0 | 30 ± 0 | 347 ± 7 | 61 ± 10 | 50 % ± 1 | 41.4 ± 0.5 | 9.0 ± 0.2 | 0 |
| t37-dist-pax16-linea-normal60 | 8 | 63 % ± 18 | 38 % ± 18 | 2.4 ± 0.8 | 0.9 ± 0.5 | 39 ± 3 | 229 ± 21 | 181 ± 13 | 59 % ± 3 | 26.1 ± 0.5 | 5.4 ± 0.5 | 0 |
| t37-dist-pax16-arco-volea40 | 8 | 25 % ± 16 | 75 % ± 16 | 0.6 ± 0.5 | 3.3 ± 0.9 | 18 ± 2 | 348 ± 17 | 210 ± 7 | 70 % ± 1 | 33.1 ± 0.3 | 5.3 ± 0.6 | 0 |
| t37-dist-pax16-arco-volea50 | 8 | 100 % ± 0 | 0 % ± 0 | 5.4 ± 0.7 | 0.0 ± 0.0 | 17 ± 1 | 419 ± 5 | 98 ± 19 | 61 % ± 1 | 37.9 ± 0.6 | 4.8 ± 0.1 | 0 |
| t37-dist-pax16-arco-volea60 | 8 | 100 % ± 0 | 0 % ± 0 | 5.8 ± 0.4 | 0.0 ± 0.0 | 20 ± 1 | 429 ± 4 | 97 ± 11 | 56 % ± 1 | 43.6 ± 0.4 | 5.5 ± 0.1 | 0 |
| t37-dist-pax16-arco-volea70 | 8 | 100 % ± 0 | 0 % ± 0 | 5.9 ± 0.8 | 0.0 ± 0.0 | 22 ± 1 | 420 ± 6 | 77 ± 20 | 47 % ± 1 | 49.5 ± 0.9 | 5.3 ± 0.1 | 0 |
| t37-dist-pax16-arco-volea80 | 8 | 100 % ± 0 | 0 % ± 0 | 7.3 ± 0.4 | 0.0 ± 0.0 | 24 ± 0 | 423 ± 3 | 47 ± 12 | 42 % ± 1 | 53.6 ± 0.6 | 5.3 ± 0.1 | 0 |
| t37-dist-pax16-arco-normal60 | 8 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 8.9 ± 0.9 | 16 ± 0 | 235 ± 19 | 193 ± 2 | 54 % ± 1 | 40.9 ± 0.2 | 4.5 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t37-dist-pax16-linea-volea40 semilla 1: sprint → blue (7/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 79 de 79 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t37-dist-pax16-linea-volea40: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=5
- t37-dist-pax16-linea-volea50: enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t37-dist-pax16-linea-volea60: enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t37-dist-pax16-linea-volea70: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=1
- t37-dist-pax16-linea-volea80: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.executed=2
- t37-dist-pax16-linea-normal60: enemy:scguns.bullet.annihilated=6 enemy:scguns.bullet.decimated=12 enemy:scguns.bullet.eliminated=6 enemy:scguns.bullet.executed=11 enemy:scguns.bullet.killed=10
- t37-dist-pax16-arco-volea40: enemy:scguns.bullet.annihilated=17 enemy:scguns.bullet.decimated=6 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=19 enemy:scguns.bullet.killed=9
- t37-dist-pax16-arco-volea50: enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=6 enemy:scguns.bullet.killed=4
- t37-dist-pax16-arco-volea60: enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=5 enemy:scguns.bullet.killed=3
- t37-dist-pax16-arco-volea70: enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=4
- t37-dist-pax16-arco-volea80: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.killed=3
- t37-dist-pax16-arco-normal60: enemy:scguns.bullet.annihilated=17 enemy:scguns.bullet.decimated=12 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=12 enemy:scguns.bullet.killed=15

## Motivos de decisión (cambios registrados): ENGAGE 866, LOW_HEALTH_RETREAT 166, NONE 25, OUT_OF_RANGE 551, SEEK_ENEMY 38

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
