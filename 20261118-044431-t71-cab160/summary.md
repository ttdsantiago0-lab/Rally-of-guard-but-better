# Simulación Rally - lote 20261118-044431-t71-cab160

Preset `t71-cab160` · velocidad `sprint` · 153 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t71-cab | 30 | 93 % ± 5 | 7 % ± 5 | 3.4 ± 0.2 | 0.3 ± 0.2 | 45 ± 1 | 564 ± 12 | 129 ± 5 | 66 % ± 1 | 43.2 ± 0.5 | 16.3 ± 2.0 | 0 |
| t71-cabarcos | 30 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.2 | 0.0 ± 0.0 | 42 ± 1 | 847 ± 6 | 36 ± 6 | 62 % ± 1 | 45.5 ± 0.4 | 22.5 ± 1.7 | 0 |
| t71-reserva | 30 | 23 % ± 8 | 77 % ± 8 | 0.4 ± 0.2 | 8.2 ± 1.3 | 47 ± 3 | 482 ± 30 | 217 ± 4 | 71 % ± 1 | 39.2 ± 0.3 | 12.9 ± 1.1 | 0 |
| t71-flanco | 30 | 23 % ± 8 | 77 % ± 8 | 0.3 ± 0.1 | 6.3 ± 0.9 | 53 ± 3 | 512 ± 22 | 222 ± 5 | 70 % ± 1 | 39.9 ± 0.5 | 12.7 ± 1.0 | 0 |
| t71-pie | 29 | 86 % ± 7 | 14 % ± 7 | 2.5 ± 0.3 | 0.8 ± 0.4 | 52 ± 2 | 660 ± 15 | 151 ± 6 | 73 % ± 1 | 42.6 ± 0.6 | 13.0 ± 2.4 | 3 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t71-cab semilla 7101: sprint → blue (4/0 vivos), normal → blue (1/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 97 de 97 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t71-cab: enemy:scguns.bullet.annihilated=18 enemy:scguns.bullet.decimated=35 enemy:scguns.bullet.eliminated=28 enemy:scguns.bullet.executed=28 enemy:scguns.bullet.killed=30
- t71-cabarcos: enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=4 env:inWall=1
- t71-reserva: enemy:scguns.bullet.annihilated=34 enemy:scguns.bullet.decimated=50 enemy:scguns.bullet.eliminated=62 enemy:scguns.bullet.executed=45 enemy:scguns.bullet.killed=37
- t71-flanco: enemy:scguns.bullet.annihilated=43 enemy:scguns.bullet.decimated=43 enemy:scguns.bullet.eliminated=47 enemy:scguns.bullet.executed=45 enemy:scguns.bullet.killed=50 env:inWall=2
- t71-pie: enemy:scguns.bullet.annihilated=32 enemy:scguns.bullet.decimated=30 enemy:scguns.bullet.eliminated=33 enemy:scguns.bullet.executed=30 enemy:scguns.bullet.killed=33 env:inWall=1

## Motivos de decisión (cambios registrados): ENGAGE 3352, FORMATION_HOLD 275, LOW_HEALTH_RETREAT 122, NONE 141, OUT_OF_RANGE 2847, SEEK_ENEMY 135, UNIT_BROKEN 71

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t71-pie - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
