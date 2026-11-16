# Simulación Rally - lote 20261116-213936-t63-o1-arena96

Preset `t63-o1-arena96` · velocidad `sprint` · 91 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t63-o1-a96-sync | 30 | 57 % ± 9 | 43 % ± 9 | 1.8 ± 0.4 | 3.5 ± 1.0 | 34 ± 1 | 739 ± 17 | 200 ± 8 | 74 % ± 1 | 39.2 ± 0.2 | 14.0 ± 1.2 | 0 |
| t63-o1-a96-spread | 30 | 70 % ± 9 | 30 % ± 9 | 3.0 ± 0.5 | 3.0 ± 1.0 | 31 ± 1 | 767 ± 17 | 161 ± 11 | 70 % ± 1 | 39.8 ± 0.3 | 13.8 ± 1.1 | 0 |
| t63-o1-a96-off | 30 | 47 % ± 9 | 53 % ± 9 | 1.6 ± 0.4 | 4.8 ± 1.1 | 31 ± 1 | 689 ± 20 | 201 ± 7 | 70 % ± 1 | 38.0 ± 0.3 | 11.4 ± 1.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t63-o1-a96-sync semilla 6301: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 52 de 52 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t63-o1-a96-sync: enemy:scguns.bullet.annihilated=33 enemy:scguns.bullet.decimated=38 enemy:scguns.bullet.eliminated=40 enemy:scguns.bullet.executed=41 enemy:scguns.bullet.killed=33
- t63-o1-a96-spread: enemy:scguns.bullet.annihilated=30 enemy:scguns.bullet.decimated=29 enemy:scguns.bullet.eliminated=25 enemy:scguns.bullet.executed=39 enemy:scguns.bullet.killed=26
- t63-o1-a96-off: enemy:scguns.bullet.annihilated=34 enemy:scguns.bullet.decimated=37 enemy:scguns.bullet.eliminated=39 enemy:scguns.bullet.executed=37 enemy:scguns.bullet.killed=44

## Motivos de decisión (cambios registrados): ENGAGE 2746, LOW_HEALTH_RETREAT 257, NONE 75, OUT_OF_RANGE 2176, SEEK_ENEMY 8

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
