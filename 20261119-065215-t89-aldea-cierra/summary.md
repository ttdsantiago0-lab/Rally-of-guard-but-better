# Simulación Rally - lote 20261119-065215-t89-aldea-cierra

Preset `t89-aldea-cierra` · velocidad `sprint` · 271 corridas · 9 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t89-pax16 | 30 | 78 % ± 5 | 22 % ± 5 | 6.5 ± 0.2 | 0.9 ± 0.2 | 125 ± 10 | 347 ± 5 | 111 ± 7 | 59 % ± 1 | 9.3 ± 0.6 | 7.4 ± 0.5 | 0 |
| t89-pax16-brecha | 30 | 100 % ± 0 | 0 % ± 0 | 6.1 ± 0.2 | 0.0 ± 0.0 | 46 ± 1 | 361 ± 5 | 129 ± 6 | 57 % ± 1 | 18.5 ± 0.6 | 14.4 ± 0.5 | 0 |
| t89-pax16-cierra | 30 | 98 % ± 2 | 2 % ± 2 | 6.2 ± 0.2 | 0.2 ± 0.2 | 52 ± 5 | 350 ± 7 | 125 ± 7 | 57 % ± 1 | 18.3 ± 0.6 | 14.2 ± 0.6 | 0 |
| t89-pax16-escudos8 | 30 | 82 % ± 4 | 18 % ± 4 | 7.0 ± 0.2 | 0.9 ± 0.2 | 120 ± 9 | 284 ± 6 | 121 ± 6 | 0 % ± 0 | 6.1 ± 0.3 | 5.1 ± 0.3 | 0 |
| t89-pax16-escudos8-brecha | 30 | 98 % ± 2 | 2 % ± 2 | 6.5 ± 0.2 | 0.1 ± 0.1 | 64 ± 5 | 300 ± 5 | 133 ± 6 | 0 % ± 0 | 8.0 ± 0.2 | 5.4 ± 0.2 | 0 |
| t89-pax16-escudos8-cierra | 30 | 100 % ± 0 | 0 % ± 0 | 6.6 ± 0.2 | 0.0 ± 0.0 | 60 ± 2 | 299 ± 5 | 122 ± 5 | 0 % ± 0 | 8.0 ± 0.1 | 5.7 ± 0.2 | 0 |
| t89-pax32-escudos8 | 30 | 65 % ± 5 | 35 % ± 5 | 4.2 ± 0.5 | 4.5 ± 0.8 | 155 ± 6 | 350 ± 17 | 198 ± 13 | 0 % ± 0 | 5.3 ± 0.2 | 5.2 ± 0.4 | 0 |
| t89-pax32-escudos8-brecha | 30 | 97 % ± 3 | 3 % ± 3 | 4.4 ± 0.4 | 0.4 ± 0.4 | 76 ± 3 | 411 ± 8 | 211 ± 8 | 0 % ± 0 | 7.6 ± 0.1 | 5.4 ± 0.3 | 0 |
| t89-pax32-escudos8-cierra | 30 | 82 % ± 7 | 18 % ± 7 | 3.4 ± 0.4 | 0.9 ± 0.4 | 88 ± 5 | 404 ± 8 | 224 ± 8 | 0 % ± 0 | 7.3 ± 0.1 | 4.8 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t89-pax16 semilla 8900: sprint → blue (7/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 262 de 262 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t89-pax16: enemy:scguns.bullet.annihilated=8 enemy:scguns.bullet.decimated=6 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=16 enemy:scguns.bullet.killed=11
- t89-pax16-brecha: enemy:scguns.bullet.annihilated=10 enemy:scguns.bullet.decimated=12 enemy:scguns.bullet.eliminated=10 enemy:scguns.bullet.executed=14 enemy:scguns.bullet.killed=11
- t89-pax16-cierra: enemy:scguns.bullet.annihilated=12 enemy:scguns.bullet.decimated=6 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=17 enemy:scguns.bullet.killed=12
- t89-pax16-escudos8: enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.decimated=6 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=5 enemy:scguns.bullet.killed=6
- t89-pax16-escudos8-brecha: enemy:scguns.bullet.annihilated=11 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=11 enemy:scguns.bullet.executed=9 enemy:scguns.bullet.killed=9
- t89-pax16-escudos8-cierra: enemy:scguns.bullet.annihilated=6 enemy:scguns.bullet.decimated=11 enemy:scguns.bullet.eliminated=9 enemy:scguns.bullet.executed=7 enemy:scguns.bullet.killed=9
- t89-pax32-escudos8: enemy:scguns.bullet.annihilated=11 enemy:scguns.bullet.decimated=27 enemy:scguns.bullet.eliminated=24 enemy:scguns.bullet.executed=24 enemy:scguns.bullet.killed=29
- t89-pax32-escudos8-brecha: enemy:scguns.bullet.annihilated=17 enemy:scguns.bullet.decimated=24 enemy:scguns.bullet.eliminated=23 enemy:scguns.bullet.executed=22 enemy:scguns.bullet.killed=21
- t89-pax32-escudos8-cierra: enemy:scguns.bullet.annihilated=25 enemy:scguns.bullet.decimated=30 enemy:scguns.bullet.eliminated=28 enemy:scguns.bullet.executed=30 enemy:scguns.bullet.killed=26

## Motivos de decisión (cambios registrados): ENGAGE 5404, LOW_HEALTH_RETREAT 958, NONE 42, OUT_OF_RANGE 788, SEEK_ENEMY 1212, STUCK 1234, TOO_CLOSE 779

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t89-pax32-escudos8: 17/30 corridas llegaron al tope de tiempo
