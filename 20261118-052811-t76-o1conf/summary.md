# Simulación Rally - lote 20261118-052811-t76-o1conf

Preset `t76-o1conf` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t76-pax48 | 30 | 83 % ± 7 | 17 % ± 7 | 3.3 ± 0.4 | 0.9 ± 0.5 | 56 ± 2 | 1195 ± 18 | 171 ± 10 | 75 % ± 1 | 44.8 ± 0.7 | 20.7 ± 2.1 | 0 |
| t76-pax48-spread | 30 | 90 % ± 6 | 10 % ± 6 | 4.6 ± 0.4 | 1.1 ± 0.7 | 48 ± 2 | 1205 ± 17 | 126 ± 13 | 70 % ± 1 | 44.4 ± 0.6 | 19.5 ± 1.8 | 0 |
| t76-pax32 | 30 | 97 % ± 3 | 3 % ± 3 | 5.8 ± 0.4 | 0.1 ± 0.1 | 42 ± 1 | 843 ± 6 | 84 ± 10 | 65 % ± 1 | 44.7 ± 0.4 | 18.6 ± 1.6 | 0 |
| t76-pax32-spread | 30 | 100 % ± 0 | 0 % ± 0 | 7.2 ± 0.2 | 0.0 ± 0.0 | 33 ± 1 | 852 ± 5 | 35 ± 6 | 61 % ± 1 | 44.2 ± 0.2 | 14.3 ± 0.9 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t76-pax48 semilla 7500: sprint → blue (3/0 vivos), normal → blue (3/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 111 de 111 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t76-pax48: enemy:scguns.bullet.annihilated=18 enemy:scguns.bullet.decimated=24 enemy:scguns.bullet.eliminated=40 enemy:scguns.bullet.executed=28 enemy:scguns.bullet.killed=30
- t76-pax48-spread: enemy:scguns.bullet.annihilated=28 enemy:scguns.bullet.decimated=21 enemy:scguns.bullet.eliminated=21 enemy:scguns.bullet.executed=18 enemy:scguns.bullet.killed=15
- t76-pax32: enemy:scguns.bullet.annihilated=14 enemy:scguns.bullet.decimated=15 enemy:scguns.bullet.eliminated=15 enemy:scguns.bullet.executed=13 enemy:scguns.bullet.killed=8
- t76-pax32-spread: enemy:scguns.bullet.annihilated=7 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=4

## Motivos de decisión (cambios registrados): ENGAGE 5063, LOW_HEALTH_RETREAT 174, NONE 351, OUT_OF_RANGE 4760, SEEK_ENEMY 4

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
