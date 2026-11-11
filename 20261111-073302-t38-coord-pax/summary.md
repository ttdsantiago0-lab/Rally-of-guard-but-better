# Simulación Rally - lote 20261111-073302-t38-coord-pax

Preset `t38-coord-pax` · velocidad `sprint` · 49 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t38-coord-pax16-linea-volea60-base | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 24 ± 1 | 338 ± 16 | 57 ± 9 | 61 % ± 2 | 32.7 ± 0.9 | 8.1 ± 0.1 | 0 |
| t38-coord-pax16-arco-volea60-base | 8 | 88 % ± 13 | 13 % ± 13 | 5.4 ± 1.0 | 0.4 ± 0.4 | 19 ± 1 | 422 ± 9 | 96 ± 25 | 57 % ± 2 | 43.0 ± 0.5 | 4.9 ± 0.2 | 0 |
| t38-coord-pax16-linea-volea60-reparto | 8 | 100 % ± 0 | 0 % ± 0 | 6.8 ± 0.5 | 0.0 ± 0.0 | 24 ± 1 | 326 ± 12 | 71 ± 17 | 60 % ± 1 | 33.6 ± 0.6 | 8.0 ± 0.3 | 0 |
| t38-coord-pax16-arco-volea60-reparto | 8 | 63 % ± 18 | 38 % ± 18 | 3.5 ± 1.2 | 1.3 ± 0.8 | 20 ± 1 | 405 ± 12 | 138 ± 23 | 57 % ± 2 | 43.5 ± 0.5 | 4.9 ± 0.2 | 0 |
| t38-coord-pax16-linea-volea60-sincro | 8 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.3 | 0.0 ± 0.0 | 23 ± 1 | 337 ± 13 | 48 ± 13 | 63 % ± 1 | 34.4 ± 0.6 | 7.9 ± 0.2 | 0 |
| t38-coord-pax16-arco-volea60-sincro | 8 | 75 % ± 16 | 25 % ± 16 | 4.3 ± 1.1 | 1.8 ± 1.2 | 20 ± 1 | 396 ± 23 | 133 ± 21 | 63 % ± 2 | 43.1 ± 0.4 | 5.4 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t38-coord-pax16-linea-volea60-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 42 de 42 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t38-coord-pax16-linea-volea60-base: enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.killed=1
- t38-coord-pax16-arco-volea60-base: enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=5 enemy:scguns.bullet.killed=8
- t38-coord-pax16-linea-volea60-reparto: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=4
- t38-coord-pax16-arco-volea60-reparto: enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=11
- t38-coord-pax16-linea-volea60-sincro: enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.eliminated=2
- t38-coord-pax16-arco-volea60-sincro: enemy:scguns.bullet.annihilated=10 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=6 enemy:scguns.bullet.killed=7

## Motivos de decisión (cambios registrados): ENGAGE 413, FORMATION_HOLD 1, LOW_HEALTH_RETREAT 68, NONE 19, OUT_OF_RANGE 296, SEEK_ENEMY 6

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
