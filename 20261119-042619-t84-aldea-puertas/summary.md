# Simulación Rally - lote 20261119-042619-t84-aldea-puertas

Preset `t84-aldea-puertas` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t84-pax16 | 30 | 87 % ± 4 | 13 % ± 4 | 6.6 ± 0.2 | 0.5 ± 0.2 | 112 ± 10 | 348 ± 5 | 118 ± 7 | 63 % ± 1 | 10.8 ± 0.7 | 9.0 ± 0.7 | 0 |
| t84-pax16-abierta | 30 | 100 % ± 0 | 0 % ± 0 | 6.2 ± 0.3 | 0.0 ± 0.0 | 38 ± 2 | 285 ± 8 | 108 ± 7 | 58 % ± 1 | 21.1 ± 0.7 | 16.0 ± 0.4 | 0 |
| t84-pax16-escudos8 | 30 | 85 % ± 4 | 15 % ± 4 | 7.0 ± 0.2 | 0.9 ± 0.3 | 112 ± 9 | 280 ± 8 | 120 ± 6 | 0 % ± 0 | 6.2 ± 0.3 | 5.9 ± 0.4 | 0 |
| t84-pax16-escudos8-abierta | 30 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.1 | 0.0 ± 0.0 | 38 ± 4 | 174 ± 6 | 63 ± 4 | 0 % ± 0 | 9.8 ± 0.2 | 5.6 ± 0.1 | 0 |
| t84-pax32-escudos8 | 30 | 73 % ± 6 | 27 % ± 6 | 4.0 ± 0.4 | 2.9 ± 0.8 | 141 ± 7 | 385 ± 16 | 214 ± 9 | 0 % ± 0 | 5.9 ± 0.2 | 4.6 ± 0.3 | 0 |
| t84-pax32-escudos8-abierta | 30 | 98 % ± 2 | 2 % ± 2 | 6.6 ± 0.2 | 0.1 ± 0.1 | 47 ± 5 | 221 ± 8 | 114 ± 8 | 0 % ± 0 | 9.5 ± 0.2 | 5.2 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t84-pax16 semilla 8300: sprint → blue (5/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 177 de 177 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t84-pax16: enemy:scguns.bullet.annihilated=12 enemy:scguns.bullet.decimated=9 enemy:scguns.bullet.eliminated=10 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=8
- t84-pax16-abierta: enemy:scguns.bullet.annihilated=10 enemy:scguns.bullet.decimated=12 enemy:scguns.bullet.eliminated=13 enemy:scguns.bullet.executed=14 enemy:scguns.bullet.killed=5
- t84-pax16-escudos8: enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=9 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=10
- t84-pax16-escudos8-abierta: enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=2
- t84-pax32-escudos8: enemy:scguns.bullet.annihilated=26 enemy:scguns.bullet.decimated=20 enemy:scguns.bullet.eliminated=26 enemy:scguns.bullet.executed=27 enemy:scguns.bullet.killed=22
- t84-pax32-escudos8-abierta: enemy:scguns.bullet.annihilated=11 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=6 enemy:scguns.bullet.executed=6 enemy:scguns.bullet.killed=15

## Motivos de decisión (cambios registrados): ENGAGE 3274, LOW_HEALTH_RETREAT 448, NONE 21, OUT_OF_RANGE 534, SEEK_ENEMY 693, STUCK 710, TOO_CLOSE 429

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
