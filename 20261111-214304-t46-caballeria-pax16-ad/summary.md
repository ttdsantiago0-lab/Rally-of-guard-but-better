# Simulación Rally - lote 20261111-214304-t46-caballeria-pax16-ad

Preset `t46-caballeria-pax16-ad` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t46-caballeria-pax16-a-base | 30 | 30 % ± 9 | 70 % ± 9 | 0.8 ± 0.3 | 4.4 ± 0.7 | 17 ± 1 | 249 ± 11 | 195 ± 5 | 92 % ± 2 | 14.4 ± 1.2 | 7.9 ± 0.8 | 0 |
| t46-caballeria-pax16-b-apie | 30 | 87 % ± 6 | 13 % ± 6 | 2.2 ± 0.2 | 0.6 ± 0.3 | 30 ± 1 | 376 ± 6 | 164 ± 5 | 68 % ± 1 | 38.4 ± 0.3 | 12.5 ± 1.4 | 0 |
| t46-caballeria-pax16-c-caballoinmortal | 30 | 97 % ± 3 | 3 % ± 3 | 4.9 ± 0.4 | 0.2 ± 0.2 | 16 ± 1 | 332 ± 5 | 128 ± 8 | 91 % ± 1 | 8.5 ± 0.1 | 6.6 ± 0.2 | 0 |
| t46-caballeria-pax16-d-soloarqueros | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 14.6 ± 0.2 | 7 ± 0 | 1 ± 0 | 191 ± 1 | 0 % ± 0 | 17.0 ± 0.2 | 4.0 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t46-caballeria-pax16-a-base semilla 1: sprint → red (0/3 vivos), normal → red (0/4 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t46-caballeria-pax16-a-base: enemy:scguns.bullet.annihilated=41 enemy:scguns.bullet.decimated=41 enemy:scguns.bullet.eliminated=38 enemy:scguns.bullet.executed=41 enemy:scguns.bullet.killed=54
- t46-caballeria-pax16-b-apie: enemy:scguns.bullet.annihilated=34 enemy:scguns.bullet.decimated=34 enemy:scguns.bullet.eliminated=32 enemy:scguns.bullet.executed=31 enemy:scguns.bullet.killed=43
- t46-caballeria-pax16-c-caballoinmortal: enemy:scguns.bullet.annihilated=22 enemy:scguns.bullet.decimated=17 enemy:scguns.bullet.eliminated=19 enemy:scguns.bullet.executed=17 enemy:scguns.bullet.killed=17
- t46-caballeria-pax16-d-soloarqueros: enemy:scguns.bullet.annihilated=46 enemy:scguns.bullet.decimated=52 enemy:scguns.bullet.eliminated=50 enemy:scguns.bullet.executed=49 enemy:scguns.bullet.killed=43

## Motivos de decisión (cambios registrados): ENGAGE 1409, FORMATION_HOLD 56, LOW_HEALTH_RETREAT 311, NONE 10, OUT_OF_RANGE 417, SEEK_ENEMY 57, TOO_CLOSE 37

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
