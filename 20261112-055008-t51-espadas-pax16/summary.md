# Simulación Rally - lote 20261112-055008-t51-espadas-pax16

Preset `t51-espadas-pax16` · velocidad `sprint` · 122 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t51-espadas-pax16-mixta-hoy | 30 | 100 % ± 0 | 0 % ± 0 | 3.7 ± 0.1 | 0.0 ± 0.0 | 28 ± 1 | 352 ± 5 | 120 ± 3 | 62 % ± 1 | 39.1 ± 0.5 | 15.1 ± 1.5 | 0 |
| t51-espadas-pax16-mixta-golpearysalir | 30 | 100 % ± 0 | 0 % ± 0 | 3.7 ± 0.1 | 0.0 ± 0.0 | 28 ± 1 | 353 ± 5 | 113 ± 4 | 63 % ± 1 | 39.0 ± 0.5 | 13.7 ± 1.7 | 0 |
| t51-espadas-pax16-espadas-hoy | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 10.4 ± 0.4 | 12 ± 0 | 20 ± 3 | 212 ± 3 | 0 % ± 0 | 12.8 ± 0.2 | 5.4 ± 0.3 | 0 |
| t51-espadas-pax16-espadas-golpearysalir | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 10.2 ± 0.4 | 12 ± 0 | 13 ± 2 | 208 ± 3 | 0 % ± 0 | 13.6 ± 0.4 | 5.0 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t51-espadas-pax16-mixta-hoy semilla 4001: sprint → blue (3/0 vivos), normal → draw (3/3 vivos)
- t51-espadas-pax16-mixta-hoy semilla 11778: sprint → ? (0/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t51-espadas-pax16-mixta-hoy: enemy:scguns.bullet.annihilated=29 enemy:scguns.bullet.decimated=23 enemy:scguns.bullet.eliminated=35 enemy:scguns.bullet.executed=14 enemy:scguns.bullet.killed=27
- t51-espadas-pax16-mixta-golpearysalir: enemy:scguns.bullet.annihilated=21 enemy:scguns.bullet.decimated=33 enemy:scguns.bullet.eliminated=28 enemy:scguns.bullet.executed=22 enemy:scguns.bullet.killed=25
- t51-espadas-pax16-espadas-hoy: enemy:scguns.bullet.annihilated=59 enemy:scguns.bullet.decimated=36 enemy:scguns.bullet.eliminated=44 enemy:scguns.bullet.executed=59 enemy:scguns.bullet.killed=42
- t51-espadas-pax16-espadas-golpearysalir: enemy:scguns.bullet.annihilated=53 enemy:scguns.bullet.decimated=47 enemy:scguns.bullet.eliminated=44 enemy:scguns.bullet.executed=47 enemy:scguns.bullet.killed=49

## Motivos de decisión (cambios registrados): ENGAGE 1587, FORMATION_HOLD 234, LOW_HEALTH_RETREAT 277, NONE 19, OUT_OF_RANGE 681, SEEK_ENEMY 30, STUCK 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t51-espadas-pax16-mixta-hoy: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
