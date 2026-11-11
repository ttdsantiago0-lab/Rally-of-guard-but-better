# Simulación Rally - lote 20261111-221113-t49-jinete-callwell16

Preset `t49-jinete-callwell16` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t49-jinete-callwell16-hoy | 30 | 93 % ± 5 | 7 % ± 5 | 4.5 ± 0.4 | 0.4 ± 0.3 | 19 ± 1 | 371 ± 6 | 107 ± 7 | 87 % ± 1 | 15.0 ± 0.7 | 12.1 ± 0.8 | 0 |
| t49-jinete-callwell16-kite | 30 | 100 % ± 0 | 0 % ± 0 | 4.3 ± 0.2 | 0.0 ± 0.0 | 33 ± 1 | 398 ± 6 | 87 ± 2 | 56 % ± 1 | 36.4 ± 0.6 | 17.5 ± 1.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t49-jinete-callwell16-hoy semilla 3001: sprint → blue (6/0 vivos), normal → blue (3/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 58 de 58 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t49-jinete-callwell16-hoy: enemy:scguns.bullet.annihilated=17 enemy:scguns.bullet.decimated=19 enemy:scguns.bullet.eliminated=24 enemy:scguns.bullet.executed=21 enemy:scguns.bullet.killed=23
- t49-jinete-callwell16-kite: enemy:scguns.bullet.annihilated=19 enemy:scguns.bullet.decimated=28 enemy:scguns.bullet.eliminated=21 enemy:scguns.bullet.executed=20 enemy:scguns.bullet.killed=23

## Motivos de decisión (cambios registrados): ENGAGE 799, FORMATION_HOLD 72, LOW_HEALTH_RETREAT 80, NONE 31, OUT_OF_RANGE 377, SEEK_ENEMY 30, TOO_CLOSE 5

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
