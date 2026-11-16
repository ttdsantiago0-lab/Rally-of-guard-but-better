# Simulación Rally - lote 20261116-213910-t63-o1-arena160

Preset `t63-o1-arena160` · velocidad `sprint` · 91 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t63-o1-a160-sync | 30 | 100 % ± 0 | 0 % ± 0 | 6.6 ± 0.3 | 0.0 ± 0.0 | 39 ± 1 | 854 ± 6 | 65 ± 8 | 67 % ± 1 | 44.3 ± 0.4 | 18.0 ± 1.4 | 0 |
| t63-o1-a160-spread | 30 | 100 % ± 0 | 0 % ± 0 | 7.0 ± 0.2 | 0.0 ± 0.0 | 33 ± 1 | 852 ± 5 | 43 ± 6 | 61 % ± 1 | 44.8 ± 0.4 | 15.0 ± 0.9 | 0 |
| t63-o1-a160-off | 30 | 100 % ± 0 | 0 % ± 0 | 6.0 ± 0.3 | 0.0 ± 0.0 | 37 ± 1 | 823 ± 7 | 83 ± 10 | 60 % ± 1 | 44.1 ± 0.4 | 19.8 ± 1.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t63-o1-a160-sync semilla 6301: sprint → blue (4/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 90 de 90 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t63-o1-a160-sync: enemy:scguns.bullet.annihilated=9 enemy:scguns.bullet.decimated=6 enemy:scguns.bullet.eliminated=10 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=10
- t63-o1-a160-spread: enemy:scguns.bullet.annihilated=6 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=6 enemy:scguns.bullet.executed=5 enemy:scguns.bullet.killed=8
- t63-o1-a160-off: enemy:scguns.bullet.annihilated=9 enemy:scguns.bullet.decimated=11 enemy:scguns.bullet.eliminated=17 enemy:scguns.bullet.executed=10 enemy:scguns.bullet.killed=13

## Motivos de decisión (cambios registrados): ENGAGE 3688, LOW_HEALTH_RETREAT 73, NONE 231, OUT_OF_RANGE 3514, SEEK_ENEMY 11

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
