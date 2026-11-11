# Simulación Rally - lote 20261111-073215-t37-fuego-escala

Preset `t37-fuego-escala` · velocidad `sprint` · 33 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t37-escala-pax24-linea-volea60 | 8 | 100 % ± 0 | 0 % ± 0 | 6.6 ± 0.3 | 0.0 ± 0.0 | 25 ± 1 | 403 ± 10 | 69 ± 11 | 68 % ± 2 | 32.2 ± 0.6 | 7.5 ± 0.2 | 0 |
| t37-escala-pax24-lineascutum-volea60 | 8 | 100 % ± 0 | 0 % ± 0 | 6.9 ± 0.5 | 0.0 ± 0.0 | 26 ± 0 | 421 ± 7 | 77 ± 16 | 70 % ± 2 | 31.2 ± 0.5 | 7.3 ± 0.3 | 0 |
| t37-escala-pax32-linea-volea60 | 8 | 100 % ± 0 | 0 % ± 0 | 6.9 ± 0.3 | 0.0 ± 0.0 | 27 ± 0 | 441 ± 14 | 89 ± 9 | 73 % ± 1 | 31.8 ± 0.5 | 7.7 ± 0.2 | 0 |
| t37-escala-pax32-lineascutum-volea60 | 8 | 100 % ± 0 | 0 % ± 0 | 6.8 ± 0.8 | 0.0 ± 0.0 | 28 ± 2 | 399 ± 14 | 71 ± 17 | 66 % ± 2 | 32.0 ± 0.6 | 7.1 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t37-escala-pax24-linea-volea60 semilla 1: sprint → blue (7/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 32 de 32 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t37-escala-pax24-linea-volea60: enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.killed=3
- t37-escala-pax24-lineascutum-volea60: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=1
- t37-escala-pax32-linea-volea60: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=1
- t37-escala-pax32-lineascutum-volea60: enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.killed=3

## Motivos de decisión (cambios registrados): ENGAGE 269, LOW_HEALTH_RETREAT 35, NONE 3, OUT_OF_RANGE 132, SEEK_ENEMY 11

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
