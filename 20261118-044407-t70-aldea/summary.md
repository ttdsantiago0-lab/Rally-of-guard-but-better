# Simulación Rally - lote 20261118-044407-t70-aldea

Preset `t70-aldea` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t70-pax16 | 30 | 85 % ± 4 | 15 % ± 4 | 6.4 ± 0.2 | 0.9 ± 0.3 | 110 ± 10 | 345 ± 8 | 122 ± 6 | 63 % ± 1 | 11.0 ± 0.8 | 9.3 ± 0.7 | 0 |
| t70-pax16-sinregla | 30 | 63 % ± 6 | 37 % ± 6 | 3.1 ± 0.3 | 3.4 ± 0.6 | 128 ± 10 | 269 ± 16 | 195 ± 7 | 69 % ± 2 | 6.6 ± 0.4 | 3.5 ± 0.4 | 0 |
| t70-pax16-espadas8 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 13.9 ± 0.2 | 11 ± 0 | 52 ± 3 | 201 ± 2 | 0 % ± 0 | 10.1 ± 0.2 | 5.1 ± 0.2 | 0 |
| t70-pax16-escudos8 | 30 | 87 % ± 4 | 13 % ± 4 | 7.0 ± 0.2 | 0.7 ± 0.3 | 118 ± 8 | 284 ± 8 | 123 ± 6 | 0 % ± 0 | 6.3 ± 0.3 | 5.8 ± 0.3 | 0 |
| t70-pax32 | 30 | 57 % ± 6 | 43 % ± 6 | 3.6 ± 0.4 | 5.9 ± 0.8 | 154 ± 7 | 419 ± 16 | 201 ± 13 | 65 % ± 1 | 10.1 ± 0.7 | 6.7 ± 0.5 | 0 |
| t70-pax32-espadas8 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 29.1 ± 0.4 | 10 ± 0 | 37 ± 1 | 196 ± 1 | 0 % ± 0 | 11.2 ± 0.1 | 5.3 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t70-pax16 semilla 7001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 112 de 112 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t70-pax16: enemy:scguns.bullet.annihilated=10 enemy:scguns.bullet.decimated=10 enemy:scguns.bullet.eliminated=7 enemy:scguns.bullet.executed=9 enemy:scguns.bullet.killed=12
- t70-pax16-sinregla: enemy:scguns.bullet.annihilated=23 enemy:scguns.bullet.decimated=33 enemy:scguns.bullet.eliminated=29 enemy:scguns.bullet.executed=26 enemy:scguns.bullet.killed=37
- t70-pax16-espadas8: enemy:scguns.bullet.annihilated=52 enemy:scguns.bullet.decimated=51 enemy:scguns.bullet.eliminated=46 enemy:scguns.bullet.executed=39 enemy:scguns.bullet.killed=52
- t70-pax16-escudos8: enemy:scguns.bullet.annihilated=10 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=7 enemy:scguns.bullet.executed=6 enemy:scguns.bullet.killed=3
- t70-pax32: enemy:scguns.bullet.annihilated=28 enemy:scguns.bullet.decimated=29 enemy:scguns.bullet.eliminated=24 enemy:scguns.bullet.executed=30 enemy:scguns.bullet.killed=22
- t70-pax32-espadas8: enemy:scguns.bullet.annihilated=51 enemy:scguns.bullet.decimated=39 enemy:scguns.bullet.eliminated=48 enemy:scguns.bullet.executed=62 enemy:scguns.bullet.killed=40

## Motivos de decisión (cambios registrados): ENGAGE 3410, LOW_HEALTH_RETREAT 653, NONE 37, OUT_OF_RANGE 519, SEEK_ENEMY 454, STUCK 539, TOO_CLOSE 812

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t70-pax32: 18/30 corridas llegaron al tope de tiempo
