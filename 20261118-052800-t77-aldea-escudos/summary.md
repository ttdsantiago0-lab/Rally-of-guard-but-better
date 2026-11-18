# Simulación Rally - lote 20261118-052800-t77-aldea-escudos

Preset `t77-aldea-escudos` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t77-pax32 | 30 | 65 % ± 6 | 35 % ± 6 | 3.7 ± 0.3 | 4.2 ± 0.8 | 150 ± 7 | 466 ± 15 | 206 ± 7 | 66 % ± 1 | 10.1 ± 0.5 | 8.0 ± 0.5 | 0 |
| t77-pax32-escudos8 | 30 | 83 % ± 4 | 17 % ± 4 | 4.7 ± 0.3 | 2.2 ± 0.6 | 133 ± 7 | 407 ± 13 | 207 ± 7 | 0 % ± 0 | 5.8 ± 0.2 | 4.9 ± 0.3 | 0 |
| t77-pax32-4e4a | 30 | 57 % ± 6 | 43 % ± 6 | 3.2 ± 0.4 | 5.8 ± 0.9 | 149 ± 8 | 424 ± 18 | 201 ± 8 | 65 % ± 1 | 10.3 ± 0.7 | 7.0 ± 0.5 | 0 |
| t77-pax32-2e6a | 30 | 57 % ± 6 | 43 % ± 6 | 3.6 ± 0.4 | 6.0 ± 0.8 | 146 ± 9 | 449 ± 21 | 203 ± 8 | 67 % ± 1 | 12.7 ± 1.0 | 8.3 ± 0.6 | 0 |
| t77-pax16 | 30 | 80 % ± 5 | 20 % ± 5 | 6.3 ± 0.3 | 1.0 ± 0.3 | 124 ± 10 | 338 ± 7 | 120 ± 5 | 63 % ± 1 | 9.8 ± 0.7 | 8.0 ± 0.6 | 0 |
| t77-pax16-4e4a | 30 | 83 % ± 4 | 17 % ± 4 | 6.3 ± 0.3 | 0.9 ± 0.2 | 117 ± 9 | 355 ± 7 | 131 ± 10 | 61 % ± 2 | 10.5 ± 0.7 | 8.7 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t77-pax32 semilla 7500: sprint → draw (6/8 vivos), normal → blue (3/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 169 de 169 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t77-pax32: enemy:scguns.bullet.annihilated=30 enemy:scguns.bullet.decimated=23 enemy:scguns.bullet.eliminated=24 enemy:scguns.bullet.executed=26 enemy:scguns.bullet.killed=25
- t77-pax32-escudos8: enemy:scguns.bullet.annihilated=18 enemy:scguns.bullet.decimated=22 enemy:scguns.bullet.eliminated=17 enemy:scguns.bullet.executed=21 enemy:scguns.bullet.killed=20
- t77-pax32-4e4a: enemy:scguns.bullet.annihilated=24 enemy:scguns.bullet.decimated=25 enemy:scguns.bullet.eliminated=37 enemy:scguns.bullet.executed=28 enemy:scguns.bullet.killed=29
- t77-pax32-2e6a: enemy:scguns.bullet.annihilated=21 enemy:scguns.bullet.decimated=26 enemy:scguns.bullet.eliminated=22 enemy:scguns.bullet.executed=20 enemy:scguns.bullet.killed=44
- t77-pax16: enemy:scguns.bullet.annihilated=7 enemy:scguns.bullet.decimated=18 enemy:scguns.bullet.eliminated=10 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=12
- t77-pax16-4e4a: enemy:scguns.bullet.annihilated=8 enemy:scguns.bullet.decimated=12 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=15 enemy:scguns.bullet.killed=7

## Motivos de decisión (cambios registrados): ENGAGE 5820, LOW_HEALTH_RETREAT 712, NONE 65, OUT_OF_RANGE 1496, SEEK_ENEMY 596, STUCK 538, TOO_CLOSE 2397, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t77-pax32-4e4a: 18/30 corridas llegaron al tope de tiempo
- t77-pax32-2e6a: 18/30 corridas llegaron al tope de tiempo
- t77-pax32: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
