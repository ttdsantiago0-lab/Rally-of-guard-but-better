# Simulación Rally - lote 20261111-214403-t46-caballeria-vindicadores32-eg

Preset `t46-caballeria-vindicadores32-eg` · velocidad `sprint` · 91 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t46-caballeria-vindicadores32-e-quietos48 | 30 | 53 % ± 9 | 47 % ± 9 | 1.0 ± 0.2 | 7.4 ± 1.7 | 56 ± 4 | 721 ± 27 | 179 ± 5 | 77 % ± 1 | 27.5 ± 1.0 | 15.0 ± 1.6 | 0 |
| t46-caballeria-vindicadores32-f-kite | 30 | 83 % ± 7 | 17 % ± 7 | 2.4 ± 0.3 | 1.2 ± 0.5 | 45 ± 2 | 806 ± 10 | 159 ± 7 | 88 % ± 1 | 17.0 ± 1.0 | 14.5 ± 1.4 | 0 |
| t46-caballeria-vindicadores32-g-arqueroskite | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 31.4 ± 0.2 | 9 ± 0 | 49 ± 2 | 200 ± 1 | 0 % ± 0 | 7.6 ± 0.1 | 4.5 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t46-caballeria-vindicadores32-e-quietos48 semilla 1: sprint → blue (2/0 vivos), normal → blue (1/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 41 de 41 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t46-caballeria-vindicadores32-e-quietos48: enemy:mob=210 enemy:scguns.bullet.decimated=1
- t46-caballeria-vindicadores32-f-kite: enemy:mob=166 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=1
- t46-caballeria-vindicadores32-g-arqueroskite: enemy:mob=236 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=2

## Motivos de decisión (cambios registrados): ENGAGE 1212, FORMATION_HOLD 78, LOW_HEALTH_RETREAT 66, NONE 13, OUT_OF_RANGE 437, SEEK_ENEMY 11, TOO_CLOSE 175

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
