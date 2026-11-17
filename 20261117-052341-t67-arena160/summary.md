# Simulación Rally - lote 20261117-052341-t67-arena160

Preset `t67-arena160` · velocidad `sprint` · 151 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t67-a160-o1 | 30 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.3 | 0.0 ± 0.0 | 40 ± 1 | 848 ± 5 | 69 ± 8 | 66 % ± 1 | 44.5 ± 0.3 | 20.6 ± 1.2 | 0 |
| t67-a160-o1spread | 30 | 100 % ± 0 | 0 % ± 0 | 7.1 ± 0.2 | 0.0 ± 0.0 | 34 ± 1 | 847 ± 6 | 38 ± 6 | 59 % ± 1 | 45.0 ± 0.4 | 14.8 ± 0.7 | 0 |
| t67-a160-o1pax48 | 30 | 83 % ± 7 | 17 % ± 7 | 3.2 ± 0.4 | 1.4 ± 0.8 | 55 ± 2 | 1175 ± 18 | 174 ± 9 | 76 % ± 1 | 44.4 ± 0.6 | 22.0 ± 1.9 | 0 |
| t67-a160-cab | 30 | 100 % ± 0 | 0 % ± 0 | 3.5 ± 0.2 | 0.0 ± 0.0 | 48 ± 1 | 573 ± 11 | 125 ± 5 | 66 % ± 1 | 43.8 ± 0.4 | 16.2 ± 1.9 | 0 |
| t67-a160-cabarcos | 30 | 100 % ± 0 | 0 % ± 0 | 7.0 ± 0.3 | 0.0 ± 0.0 | 44 ± 1 | 851 ± 5 | 45 ± 9 | 65 % ± 1 | 46.6 ± 0.5 | 18.7 ± 1.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t67-a160-o1 semilla 6701: sprint → blue (5/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 145 de 145 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t67-a160-o1: enemy:scguns.bullet.annihilated=8 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=15 enemy:scguns.bullet.executed=7 enemy:scguns.bullet.killed=10
- t67-a160-o1spread: enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=5 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=8
- t67-a160-o1pax48: enemy:scguns.bullet.annihilated=29 enemy:scguns.bullet.decimated=32 enemy:scguns.bullet.eliminated=26 enemy:scguns.bullet.executed=30 enemy:scguns.bullet.killed=27
- t67-a160-cab: enemy:scguns.bullet.annihilated=34 enemy:scguns.bullet.decimated=25 enemy:scguns.bullet.eliminated=18 enemy:scguns.bullet.executed=32 enemy:scguns.bullet.killed=26 env:inWall=1
- t67-a160-cabarcos: enemy:scguns.bullet.annihilated=6 enemy:scguns.bullet.decimated=7 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=9 enemy:scguns.bullet.killed=8

## Motivos de decisión (cambios registrados): ENGAGE 5491, FORMATION_HOLD 46, LOW_HEALTH_RETREAT 170, NONE 351, OUT_OF_RANGE 5039, SEEK_ENEMY 9

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
