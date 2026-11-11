# Simulación Rally - lote 20261111-073341-t37-fuego-confirma

Preset `t37-fuego-confirma` · velocidad `sprint` · 121 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t37-confirma-pax16-linea-base | 20 | 80 % ± 9 | 20 % ± 9 | 2.4 ± 0.4 | 0.6 ± 0.3 | 28 ± 1 | 194 ± 8 | 180 ± 8 | 86 % ± 1 | 12.5 ± 0.3 | 3.7 ± 0.3 | 0 |
| t37-confirma-pax16-linea-volea60 | 20 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 23 ± 0 | 331 ± 8 | 39 ± 4 | 63 % ± 1 | 33.8 ± 0.4 | 7.8 ± 0.2 | 0 |
| t37-confirma-pax16-arco-base | 20 | 5 % ± 5 | 95 % ± 5 | 0.1 ± 0.1 | 12.4 ± 0.7 | 8 ± 1 | 145 ± 13 | 199 ± 2 | 81 % ± 1 | 26.8 ± 0.2 | 4.3 ± 0.4 | 0 |
| t37-confirma-pax16-arco-volea60 | 20 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.2 | 0.0 ± 0.0 | 19 ± 0 | 428 ± 2 | 74 ± 8 | 53 % ± 1 | 43.9 ± 0.3 | 5.2 ± 0.1 | 0 |
| t37-confirma-pax16-lineascutum-base | 20 | 85 % ± 8 | 15 % ± 8 | 2.8 ± 0.4 | 0.6 ± 0.3 | 29 ± 1 | 203 ± 8 | 172 ± 7 | 85 % ± 1 | 12.7 ± 0.5 | 4.6 ± 0.5 | 0 |
| t37-confirma-pax16-lineascutum-volea60 | 20 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.1 | 0.0 ± 0.0 | 24 ± 0 | 324 ± 6 | 42 ± 5 | 61 % ± 1 | 33.1 ± 0.5 | 8.0 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t37-confirma-pax16-linea-base semilla 1001: sprint → blue (3/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 94 de 94 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t37-confirma-pax16-linea-base: enemy:scguns.bullet.annihilated=16 enemy:scguns.bullet.decimated=25 enemy:scguns.bullet.eliminated=22 enemy:scguns.bullet.executed=23 enemy:scguns.bullet.killed=27
- t37-confirma-pax16-linea-volea60: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1
- t37-confirma-pax16-arco-base: enemy:scguns.bullet.annihilated=25 enemy:scguns.bullet.decimated=29 enemy:scguns.bullet.eliminated=38 enemy:scguns.bullet.executed=33 enemy:scguns.bullet.killed=33
- t37-confirma-pax16-arco-volea60: enemy:scguns.bullet.annihilated=7 enemy:scguns.bullet.decimated=7 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=6 enemy:scguns.bullet.killed=7
- t37-confirma-pax16-lineascutum-base: enemy:scguns.bullet.annihilated=22 enemy:scguns.bullet.decimated=15 enemy:scguns.bullet.eliminated=20 enemy:scguns.bullet.executed=23 enemy:scguns.bullet.killed=24
- t37-confirma-pax16-lineascutum-volea60: enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1

## Motivos de decisión (cambios registrados): ENGAGE 1005, LOW_HEALTH_RETREAT 209, NONE 11, OUT_OF_RANGE 325, SEEK_ENEMY 37

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
