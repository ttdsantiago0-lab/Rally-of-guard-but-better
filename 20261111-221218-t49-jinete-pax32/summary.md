# Simulación Rally - lote 20261111-221218-t49-jinete-pax32

Preset `t49-jinete-pax32` · velocidad `sprint` · 70 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t49-jinete-pax32-hoy | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 14.0 ± 0.5 | 11 ± 1 | 162 ± 5 | 196 ± 2 | 98 % ± 1 | 13.4 ± 0.6 | 5.6 ± 0.3 | 0 |
| t49-jinete-pax32-kite | 27 | 78 % ± 8 | 22 % ± 8 | 2.0 ± 0.3 | 1.9 ± 0.8 | 38 ± 2 | 499 ± 11 | 169 ± 6 | 73 % ± 1 | 37.8 ± 0.4 | 12.6 ± 1.8 | 12 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t49-jinete-pax32-hoy semilla 3001: sprint → red (0/11 vivos), normal → red (0/14 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 21 de 21 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t49-jinete-pax32-hoy: enemy:scguns.bullet.annihilated=53 enemy:scguns.bullet.decimated=47 enemy:scguns.bullet.eliminated=55 enemy:scguns.bullet.executed=41 enemy:scguns.bullet.killed=44
- t49-jinete-pax32-kite: enemy:scguns.bullet.annihilated=30 enemy:scguns.bullet.decimated=34 enemy:scguns.bullet.eliminated=30 enemy:scguns.bullet.executed=31 enemy:scguns.bullet.killed=35 env:inWall=1

## Motivos de decisión (cambios registrados): ENGAGE 934, FORMATION_HOLD 82, LOW_HEALTH_RETREAT 149, NONE 12, OUT_OF_RANGE 394, SEEK_ENEMY 7, STUCK 4

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t49-jinete-pax32-kite - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
