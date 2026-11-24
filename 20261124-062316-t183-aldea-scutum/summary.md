# Simulación Rally - lote 20261124-062316-t183-aldea-scutum

Preset `t183-aldea-scutum` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t183-aldea32-e4a4 | 30 | 68 % ± 4 | 32 % ± 4 | 4.8 ± 0.2 | 3.8 ± 0.7 | 146 ± 9 | 416 ± 14 | 144 ± 4 | 11 % ± 1 | 38.0 ± 1.0 | 19.1 ± 1.1 | 0 |
| t183-aldea32-s4a4 | 30 | 87 % ± 4 | 13 % ± 4 | 5.5 ± 0.2 | 1.7 ± 0.6 | 117 ± 8 | 449 ± 10 | 136 ± 6 | 13 % ± 1 | 35.0 ± 1.0 | 22.4 ± 1.0 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t183-aldea32-e4a4 semilla 16500: sprint → blue (6/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t183-aldea32-e4a4: enemy:scguns.bullet.annihilated=21 enemy:scguns.bullet.decimated=17 enemy:scguns.bullet.eliminated=17 enemy:scguns.bullet.executed=21 enemy:scguns.bullet.killed=19
- t183-aldea32-s4a4: enemy:scguns.bullet.annihilated=10 enemy:scguns.bullet.decimated=20 enemy:scguns.bullet.eliminated=16 enemy:scguns.bullet.executed=15 enemy:scguns.bullet.killed=13

## Motivos de decisión (cambios registrados): ENGAGE 1430, LOW_HEALTH_RETREAT 284, NONE 160, OUT_OF_RANGE 1028, SEEK_ENEMY 239, STUCK 131

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t183-aldea32-e4a4: 19/30 corridas llegaron al tope de tiempo
