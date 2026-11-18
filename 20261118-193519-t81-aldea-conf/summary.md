# Simulación Rally - lote 20261118-193519-t81-aldea-conf

Preset `t81-aldea-conf` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t81-pax16 | 30 | 90 % ± 4 | 10 % ± 4 | 6.1 ± 0.2 | 0.4 ± 0.1 | 101 ± 8 | 351 ± 6 | 122 ± 6 | 61 % ± 1 | 11.1 ± 0.6 | 8.9 ± 0.6 | 0 |
| t81-pax16-escudos8 | 30 | 78 % ± 5 | 22 % ± 5 | 7.3 ± 0.1 | 1.4 ± 0.3 | 124 ± 9 | 276 ± 9 | 105 ± 5 | 0 % ± 0 | 5.8 ± 0.3 | 5.7 ± 0.4 | 0 |
| t81-pax32 | 30 | 62 % ± 5 | 38 % ± 5 | 3.7 ± 0.3 | 5.2 ± 0.8 | 158 ± 7 | 411 ± 17 | 208 ± 9 | 63 % ± 1 | 9.0 ± 0.5 | 7.0 ± 0.5 | 0 |
| t81-pax32-escudos8 | 30 | 70 % ± 5 | 30 % ± 5 | 4.4 ± 0.3 | 3.8 ± 0.8 | 150 ± 6 | 367 ± 16 | 197 ± 11 | 0 % ± 0 | 5.5 ± 0.2 | 4.7 ± 0.3 | 0 |
| t81-pax32-escudos8-5min | 30 | 78 % ± 6 | 22 % ± 6 | 4.6 ± 0.4 | 2.1 ± 0.7 | 166 ± 18 | 394 ± 13 | 199 ± 8 | 0 % ± 0 | 5.7 ± 0.3 | 4.6 ± 0.4 | 0 |
| t81-pax32-escudos12 | 30 | 87 % ± 4 | 13 % ± 4 | 8.1 ± 0.4 | 1.1 ± 0.4 | 114 ± 8 | 449 ± 10 | 228 ± 9 | 0 % ± 0 | 5.4 ± 0.2 | 6.2 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t81-pax16 semilla 7900: sprint → blue (6/0 vivos), normal → draw (7/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 176 de 176 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t81-pax16: enemy:scguns.bullet.annihilated=9 enemy:scguns.bullet.decimated=10 enemy:scguns.bullet.eliminated=17 enemy:scguns.bullet.executed=9 enemy:scguns.bullet.killed=11
- t81-pax16-escudos8: enemy:scguns.bullet.annihilated=6 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=7
- t81-pax32: enemy:scguns.bullet.annihilated=26 enemy:scguns.bullet.decimated=26 enemy:scguns.bullet.eliminated=27 enemy:scguns.bullet.executed=23 enemy:scguns.bullet.killed=27
- t81-pax32-escudos8: enemy:scguns.bullet.annihilated=19 enemy:scguns.bullet.decimated=24 enemy:scguns.bullet.eliminated=26 enemy:scguns.bullet.executed=19 enemy:scguns.bullet.killed=19
- t81-pax32-escudos8-5min: enemy:scguns.bullet.annihilated=27 enemy:scguns.bullet.decimated=15 enemy:scguns.bullet.eliminated=16 enemy:scguns.bullet.executed=20 enemy:scguns.bullet.killed=25
- t81-pax32-escudos12: enemy:scguns.bullet.annihilated=17 enemy:scguns.bullet.decimated=27 enemy:scguns.bullet.eliminated=27 enemy:scguns.bullet.executed=22 enemy:scguns.bullet.killed=24

## Motivos de decisión (cambios registrados): ENGAGE 4707, LOW_HEALTH_RETREAT 726, NONE 30, OUT_OF_RANGE 498, SEEK_ENEMY 890, STUCK 1515, TOO_CLOSE 799

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t81-pax32: 21/30 corridas llegaron al tope de tiempo
- t81-pax32-escudos8: 16/30 corridas llegaron al tope de tiempo
- t81-pax16: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
