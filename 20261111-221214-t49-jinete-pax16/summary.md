# Simulación Rally - lote 20261111-221214-t49-jinete-pax16

Preset `t49-jinete-pax16` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t49-jinete-pax16-hoy | 30 | 47 % ± 9 | 53 % ± 9 | 1.0 ± 0.3 | 3.4 ± 0.6 | 19 ± 1 | 261 ± 11 | 196 ± 4 | 90 % ± 2 | 15.2 ± 1.0 | 8.4 ± 1.0 | 0 |
| t49-jinete-pax16-kite | 30 | 100 % ± 0 | 0 % ± 0 | 3.8 ± 0.1 | 0.0 ± 0.0 | 27 ± 1 | 360 ± 5 | 113 ± 4 | 65 % ± 1 | 38.4 ± 0.4 | 12.8 ± 1.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t49-jinete-pax16-hoy semilla 3001: sprint → blue (1/0 vivos), normal → red (0/10 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 44 de 44 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t49-jinete-pax16-hoy: enemy:scguns.bullet.annihilated=41 enemy:scguns.bullet.decimated=49 enemy:scguns.bullet.eliminated=42 enemy:scguns.bullet.executed=29 enemy:scguns.bullet.killed=50
- t49-jinete-pax16-kite: enemy:scguns.bullet.annihilated=35 enemy:scguns.bullet.decimated=24 enemy:scguns.bullet.eliminated=29 enemy:scguns.bullet.executed=16 enemy:scguns.bullet.killed=23

## Motivos de decisión (cambios registrados): ENGAGE 797, FORMATION_HOLD 66, LOW_HEALTH_RETREAT 141, NONE 11, OUT_OF_RANGE 348, SEEK_ENEMY 26, STUCK 1, TOO_CLOSE 10

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t49-jinete-pax16-hoy: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
