# Simulación Rally - lote 20261114-125349-t54-pinza-pax16

Preset `t54-pinza-pax16` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t54-pinza-pax16-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 15.9 ± 0.0 | 0.0 ± 0.0 | 17 ± 0 | 411 ± 5 | 27 ± 3 | 58 % ± 1 | 29.8 ± 0.2 | 20.8 ± 0.2 | 0 |
| t54-pinza-pax16-linea-mantener | 30 | 100 % ± 0 | 0 % ± 0 | 15.9 ± 0.1 | 0.0 ± 0.0 | 17 ± 0 | 411 ± 5 | 67 ± 3 | 59 % ± 1 | 31.2 ± 0.3 | 19.2 ± 0.2 | 0 |
| t54-pinza-pax16-linea-pinza | 30 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 17 ± 0 | 417 ± 4 | 56 ± 2 | 59 % ± 1 | 31.1 ± 0.3 | 19.5 ± 0.2 | 0 |
| t54-pinza-pax16-cab-base | 30 | 100 % ± 0 | 0 % ± 0 | 11.3 ± 0.4 | 0.0 ± 0.0 | 16 ± 1 | 391 ± 5 | 164 ± 8 | 53 % ± 1 | 34.0 ± 0.5 | 22.4 ± 0.4 | 0 |
| t54-pinza-pax16-cab-mantener | 30 | 100 % ± 0 | 0 % ± 0 | 9.1 ± 0.2 | 0.0 ± 0.0 | 20 ± 1 | 413 ± 3 | 212 ± 3 | 55 % ± 1 | 40.9 ± 0.3 | 17.4 ± 0.5 | 0 |
| t54-pinza-pax16-cab-pinza | 30 | 100 % ± 0 | 0 % ± 0 | 9.8 ± 0.4 | 0.0 ± 0.0 | 19 ± 1 | 390 ± 5 | 207 ± 6 | 55 % ± 1 | 39.4 ± 0.5 | 18.4 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t54-pinza-pax16-linea-base semilla 5401: sprint → blue (15/0 vivos), normal → blue (16/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 180 de 180 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t54-pinza-pax16-linea-base: enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1
- t54-pinza-pax16-linea-mantener: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.killed=2
- t54-pinza-pax16-linea-pinza: enemy:scguns.bullet.killed=1
- t54-pinza-pax16-cab-base: enemy:scguns.bullet.annihilated=29 enemy:scguns.bullet.decimated=24 enemy:scguns.bullet.eliminated=27 enemy:scguns.bullet.executed=35 enemy:scguns.bullet.killed=25
- t54-pinza-pax16-cab-mantener: enemy:scguns.bullet.annihilated=45 enemy:scguns.bullet.decimated=34 enemy:scguns.bullet.eliminated=45 enemy:scguns.bullet.executed=39 enemy:scguns.bullet.killed=45
- t54-pinza-pax16-cab-pinza: enemy:scguns.bullet.annihilated=43 enemy:scguns.bullet.decimated=38 enemy:scguns.bullet.eliminated=40 enemy:scguns.bullet.executed=36 enemy:scguns.bullet.killed=29

## Motivos de decisión (cambios registrados): ENGAGE 4427, FORMATION_HOLD 1186, INSTRUCTION 1920, LOW_HEALTH_RETREAT 214, NONE 55, OUT_OF_RANGE 3086, SEEK_ENEMY 181, STUCK 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
