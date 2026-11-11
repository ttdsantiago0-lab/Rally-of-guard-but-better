# Simulación Rally - lote 20261111-221214-t46-caballeria-dg

Preset `t46-caballeria-dg` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t46-caballeria-pax16-d-soloarqueros | 30 | 100 % ± 0 | 0 % ± 0 | 5.3 ± 0.2 | 0.0 ± 0.0 | 11 ± 0 | 388 ± 5 | 100 ± 7 | 94 % ± 1 | 13.1 ± 0.6 | 8.9 ± 0.5 | 0 |
| t46-caballeria-pax16-g-arqueroskite | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 24 ± 1 | 434 ± 2 | 23 ± 3 | 53 % ± 1 | 43.0 ± 0.4 | 17.6 ± 1.0 | 0 |
| t46-caballeria-vindicadores32-d-soloarqueros | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 27.9 ± 0.5 | 11 ± 1 | 295 ± 13 | 199 ± 1 | 95 % ± 1 | 4.4 ± 0.2 | 3.8 ± 0.3 | 0 |
| t46-caballeria-vindicadores32-g-arqueroskite | 30 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.1 | 0.0 ± 0.0 | 24 ± 1 | 862 ± 4 | 19 ± 3 | 86 % ± 1 | 20.8 ± 0.8 | 12.6 ± 0.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t46-caballeria-pax16-d-soloarqueros semilla 1: sprint → blue (7/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 90 de 90 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t46-caballeria-pax16-d-soloarqueros: enemy:scguns.bullet.annihilated=15 enemy:scguns.bullet.decimated=14 enemy:scguns.bullet.eliminated=18 enemy:scguns.bullet.executed=16 enemy:scguns.bullet.killed=19
- t46-caballeria-pax16-g-arqueroskite: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.killed=1
- t46-caballeria-vindicadores32-d-soloarqueros: enemy:mob=240
- t46-caballeria-vindicadores32-g-arqueroskite: enemy:mob=9

## Motivos de decisión (cambios registrados): ENGAGE 1629, LOW_HEALTH_RETREAT 81, NONE 28, OUT_OF_RANGE 838, SEEK_ENEMY 19, TOO_CLOSE 206

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
