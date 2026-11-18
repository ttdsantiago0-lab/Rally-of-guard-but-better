# Simulación Rally - lote 20261118-193452-t80-abierto-escudos

Preset `t80-abierto-escudos` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t80-arcos8 | 30 | 93 % ± 5 | 7 % ± 5 | 4.6 ± 0.4 | 0.1 ± 0.1 | 47 ± 1 | 1213 ± 15 | 125 ± 9 | 69 % ± 1 | 45.1 ± 0.6 | 23.1 ± 1.5 | 0 |
| t80-2e6a | 30 | 90 % ± 6 | 10 % ± 6 | 4.1 ± 0.3 | 0.9 ± 0.7 | 47 ± 1 | 919 ± 17 | 140 ± 10 | 65 % ± 1 | 44.2 ± 0.6 | 19.0 ± 1.6 | 0 |
| t80-4e4a | 30 | 97 % ± 3 | 3 % ± 3 | 4.2 ± 0.3 | 0.5 ± 0.5 | 37 ± 2 | 505 ± 18 | 118 ± 8 | 60 % ± 1 | 39.1 ± 0.8 | 17.7 ± 1.0 | 0 |
| t80-escudos8 | 30 | 90 % ± 6 | 10 % ± 6 | 4.9 ± 0.5 | 0.7 ± 0.4 | 34 ± 1 | 177 ± 6 | 148 ± 10 | 0 % ± 0 | 10.6 ± 0.2 | 5.3 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t80-arcos8 semilla 7900: sprint → blue (7/0 vivos), normal → blue (3/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 111 de 111 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t80-arcos8: enemy:scguns.bullet.annihilated=28 enemy:scguns.bullet.decimated=20 enemy:scguns.bullet.eliminated=19 enemy:scguns.bullet.executed=14 enemy:scguns.bullet.killed=20
- t80-2e6a: enemy:scguns.bullet.annihilated=25 enemy:scguns.bullet.decimated=17 enemy:scguns.bullet.eliminated=29 enemy:scguns.bullet.executed=23 enemy:scguns.bullet.killed=24
- t80-4e4a: enemy:scguns.bullet.annihilated=23 enemy:scguns.bullet.decimated=22 enemy:scguns.bullet.eliminated=26 enemy:scguns.bullet.executed=20 enemy:scguns.bullet.killed=23
- t80-escudos8: enemy:scguns.bullet.annihilated=19 enemy:scguns.bullet.decimated=16 enemy:scguns.bullet.eliminated=22 enemy:scguns.bullet.executed=23 enemy:scguns.bullet.killed=14

## Motivos de decisión (cambios registrados): ENGAGE 3226, LOW_HEALTH_RETREAT 253, NONE 240, OUT_OF_RANGE 2664, SEEK_ENEMY 95, STUCK 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
