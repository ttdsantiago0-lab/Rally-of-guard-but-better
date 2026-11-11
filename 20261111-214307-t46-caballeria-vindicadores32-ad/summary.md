# Simulación Rally - lote 20261111-214307-t46-caballeria-vindicadores32-ad

Preset `t46-caballeria-vindicadores32-ad` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t46-caballeria-vindicadores32-a-base | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 30.1 ± 0.3 | 9 ± 0 | 166 ± 5 | 196 ± 0 | 99 % ± 1 | 7.1 ± 0.2 | 4.3 ± 0.1 | 0 |
| t46-caballeria-vindicadores32-b-apie | 30 | 93 % ± 5 | 7 % ± 5 | 2.9 ± 0.2 | 0.4 ± 0.3 | 38 ± 1 | 833 ± 9 | 138 ± 5 | 90 % ± 1 | 14.8 ± 1.0 | 14.2 ± 0.9 | 0 |
| t46-caballeria-vindicadores32-c-caballoinmortal | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 29.6 ± 0.4 | 9 ± 0 | 163 ± 4 | 196 ± 0 | 98 % ± 1 | 7.1 ± 0.2 | 4.4 ± 0.1 | 0 |
| t46-caballeria-vindicadores32-d-soloarqueros | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 31.4 ± 0.1 | 9 ± 0 | 50 ± 2 | 201 ± 1 | 0 % ± 0 | 7.3 ± 0.1 | 4.5 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t46-caballeria-vindicadores32-a-base semilla 1: sprint → red (0/26 vivos), normal → red (0/32 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 28 de 28 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t46-caballeria-vindicadores32-a-base: enemy:mob=240
- t46-caballeria-vindicadores32-b-apie: enemy:mob=149 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t46-caballeria-vindicadores32-c-caballoinmortal: enemy:mob=239 enemy:scguns.bullet.decimated=1
- t46-caballeria-vindicadores32-d-soloarqueros: enemy:mob=237 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=2

## Motivos de decisión (cambios registrados): ENGAGE 1059, FORMATION_HOLD 49, LOW_HEALTH_RETREAT 71, NONE 12, OUT_OF_RANGE 84, SEEK_ENEMY 31, TOO_CLOSE 199

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
