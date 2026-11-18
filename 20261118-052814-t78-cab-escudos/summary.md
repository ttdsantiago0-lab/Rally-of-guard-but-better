# Simulación Rally - lote 20261118-052814-t78-cab-escudos

Preset `t78-cab-escudos` · velocidad `sprint` · 122 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t78-cab | 30 | 100 % ± 0 | 0 % ± 0 | 3.4 ± 0.1 | 0.0 ± 0.0 | 46 ± 1 | 564 ± 9 | 130 ± 4 | 66 % ± 1 | 44.4 ± 0.6 | 15.4 ± 1.8 | 0 |
| t78-cabarcos | 30 | 100 % ± 0 | 0 % ± 0 | 7.2 ± 0.2 | 0.0 ± 0.0 | 43 ± 1 | 858 ± 4 | 39 ± 5 | 62 % ± 1 | 46.1 ± 0.3 | 21.3 ± 1.7 | 0 |
| t78-cab-escudos | 30 | 100 % ± 0 | 0 % ± 0 | 7.1 ± 0.2 | 0.0 ± 0.0 | 23 ± 0 | 317 ± 7 | 47 ± 4 | 61 % ± 1 | 31.3 ± 0.3 | 21.4 ± 0.2 | 0 |
| t78-escudospie | 30 | 100 % ± 0 | 0 % ± 0 | 6.3 ± 0.3 | 0.0 ± 0.0 | 29 ± 2 | 340 ± 12 | 65 ± 7 | 60 % ± 1 | 34.6 ± 0.9 | 18.4 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t78-cab semilla 7500: sprint → blue (3/0 vivos), normal → draw (1/9 vivos)
- t78-cab semilla 15277: sprint → ? (0/0 vivos), normal → blue (3/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t78-cab: enemy:scguns.bullet.annihilated=36 enemy:scguns.bullet.decimated=30 enemy:scguns.bullet.eliminated=27 enemy:scguns.bullet.executed=24 enemy:scguns.bullet.killed=22
- t78-cabarcos: enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=6 enemy:scguns.bullet.executed=7 enemy:scguns.bullet.killed=2 env:inWall=1
- t78-cab-escudos: enemy:scguns.bullet.annihilated=8 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=7 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=6
- t78-escudospie: enemy:scguns.bullet.annihilated=14 enemy:scguns.bullet.decimated=7 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=11 enemy:scguns.bullet.killed=11

## Motivos de decisión (cambios registrados): ENGAGE 2501, FORMATION_HOLD 59, LOW_HEALTH_RETREAT 129, NONE 162, OUT_OF_RANGE 2039, SEEK_ENEMY 35, STUCK 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t78-cab: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
