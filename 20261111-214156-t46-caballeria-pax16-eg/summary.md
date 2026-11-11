# Simulación Rally - lote 20261111-214156-t46-caballeria-pax16-eg

Preset `t46-caballeria-pax16-eg` · velocidad `sprint` · 91 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t46-caballeria-pax16-e-quietos48 | 30 | 100 % ± 0 | 0 % ± 0 | 3.7 ± 0.1 | 0.0 ± 0.0 | 28 ± 1 | 359 ± 6 | 115 ± 4 | 64 % ± 1 | 38.9 ± 0.4 | 12.7 ± 1.7 | 0 |
| t46-caballeria-pax16-f-kite | 30 | 100 % ± 0 | 0 % ± 0 | 3.7 ± 0.1 | 0.0 ± 0.0 | 27 ± 1 | 344 ± 6 | 119 ± 5 | 64 % ± 1 | 38.7 ± 0.4 | 12.3 ± 1.7 | 0 |
| t46-caballeria-pax16-g-arqueroskite | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 14.2 ± 0.3 | 7 ± 0 | 1 ± 0 | 193 ± 1 | 0 % ± 0 | 17.1 ± 0.2 | 4.0 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t46-caballeria-pax16-e-quietos48 semilla 1: sprint → blue (3/0 vivos), normal → blue (3/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t46-caballeria-pax16-e-quietos48: enemy:scguns.bullet.annihilated=28 enemy:scguns.bullet.decimated=20 enemy:scguns.bullet.eliminated=30 enemy:scguns.bullet.executed=25 enemy:scguns.bullet.killed=25
- t46-caballeria-pax16-f-kite: enemy:scguns.bullet.annihilated=27 enemy:scguns.bullet.decimated=34 enemy:scguns.bullet.eliminated=26 enemy:scguns.bullet.executed=22 enemy:scguns.bullet.killed=19
- t46-caballeria-pax16-g-arqueroskite: enemy:scguns.bullet.annihilated=46 enemy:scguns.bullet.decimated=42 enemy:scguns.bullet.eliminated=54 enemy:scguns.bullet.executed=41 enemy:scguns.bullet.killed=57

## Motivos de decisión (cambios registrados): ENGAGE 1304, FORMATION_HOLD 72, LOW_HEALTH_RETREAT 143, NONE 17, OUT_OF_RANGE 640, SEEK_ENEMY 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
