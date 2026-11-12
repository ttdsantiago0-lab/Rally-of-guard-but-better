# Simulación Rally - lote 20261112-055114-t50-caballo-pax32-arqueros

Preset `t50-caballo-pax32-arqueros` · velocidad `sprint` · 245 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t50-caballo-pax32-arqueros-1-sinarmadura | 30 | 97 % ± 3 | 3 % ± 3 | 5.2 ± 0.3 | 0.1 ± 0.1 | 35 ± 1 | 777 ± 10 | 118 ± 8 | 70 % ± 1 | 39.2 ± 0.5 | 16.1 ± 1.5 | 0 |
| t50-caballo-pax32-arqueros-2-hierro | 30 | 100 % ± 0 | 0 % ± 0 | 5.6 ± 0.3 | 0.0 ± 0.0 | 39 ± 2 | 772 ± 10 | 96 ± 7 | 70 % ± 1 | 40.0 ± 0.4 | 17.4 ± 1.7 | 1 |
| t50-caballo-pax32-arqueros-3-diamante | 30 | 100 % ± 0 | 0 % ± 0 | 5.4 ± 0.2 | 0.0 ± 0.0 | 39 ± 1 | 788 ± 8 | 97 ± 7 | 71 % ± 1 | 39.6 ± 0.4 | 14.8 ± 1.9 | 0 |
| t50-caballo-pax32-arqueros-4-superarmadura | 30 | 100 % ± 0 | 0 % ± 0 | 5.6 ± 0.3 | 0.0 ± 0.0 | 38 ± 1 | 776 ± 10 | 89 ± 7 | 72 % ± 1 | 39.3 ± 0.5 | 13.9 ± 1.4 | 0 |
| t50-caballo-pax32-arqueros-5-vida50 | 30 | 100 % ± 0 | 0 % ± 0 | 6.0 ± 0.2 | 0.0 ± 0.0 | 35 ± 1 | 799 ± 8 | 89 ± 7 | 70 % ± 1 | 39.9 ± 0.4 | 19.0 ± 1.6 | 0 |
| t50-caballo-pax32-arqueros-6-diamantevida50 | 30 | 97 % ± 3 | 3 % ± 3 | 6.1 ± 0.3 | 0.1 ± 0.1 | 38 ± 1 | 785 ± 12 | 80 ± 8 | 69 % ± 1 | 39.8 ± 0.5 | 15.8 ± 1.5 | 0 |
| t50-caballo-pax32-arqueros-7-escudofrontal | 30 | 100 % ± 0 | 0 % ± 0 | 5.5 ± 0.3 | 0.0 ± 0.0 | 37 ± 2 | 796 ± 8 | 110 ± 8 | 70 % ± 1 | 39.7 ± 0.5 | 19.6 ± 1.7 | 3 |
| t50-caballo-pax32-arqueros-8-inmortal | 30 | 100 % ± 0 | 0 % ± 0 | 6.8 ± 0.2 | 0.0 ± 0.0 | 33 ± 1 | 809 ± 8 | 65 ± 7 | 71 % ± 1 | 39.3 ± 0.4 | 19.1 ± 1.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t50-caballo-pax32-arqueros-1-sinarmadura semilla 4001: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 238 de 238 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t50-caballo-pax32-arqueros-1-sinarmadura: enemy:scguns.bullet.annihilated=11 enemy:scguns.bullet.decimated=15 enemy:scguns.bullet.eliminated=17 enemy:scguns.bullet.executed=19 enemy:scguns.bullet.killed=19 env:inWall=2
- t50-caballo-pax32-arqueros-2-hierro: enemy:scguns.bullet.annihilated=16 enemy:scguns.bullet.decimated=10 enemy:scguns.bullet.eliminated=9 enemy:scguns.bullet.executed=12 enemy:scguns.bullet.killed=22 env:inWall=2
- t50-caballo-pax32-arqueros-3-diamante: enemy:scguns.bullet.annihilated=13 enemy:scguns.bullet.decimated=25 enemy:scguns.bullet.eliminated=12 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=18 env:inWall=1
- t50-caballo-pax32-arqueros-4-superarmadura: enemy:scguns.bullet.annihilated=10 enemy:scguns.bullet.decimated=15 enemy:scguns.bullet.eliminated=11 enemy:scguns.bullet.executed=18 enemy:scguns.bullet.killed=19
- t50-caballo-pax32-arqueros-5-vida50: enemy:scguns.bullet.annihilated=13 enemy:scguns.bullet.decimated=8 enemy:scguns.bullet.eliminated=11 enemy:scguns.bullet.executed=17 enemy:scguns.bullet.killed=12
- t50-caballo-pax32-arqueros-6-diamantevida50: enemy:scguns.bullet.annihilated=10 enemy:scguns.bullet.decimated=12 enemy:scguns.bullet.eliminated=17 enemy:scguns.bullet.executed=9 enemy:scguns.bullet.killed=7 env:inWall=1
- t50-caballo-pax32-arqueros-7-escudofrontal: enemy:scguns.bullet.annihilated=16 enemy:scguns.bullet.decimated=16 enemy:scguns.bullet.eliminated=15 enemy:scguns.bullet.executed=16 enemy:scguns.bullet.killed=11 env:inWall=1
- t50-caballo-pax32-arqueros-8-inmortal: enemy:scguns.bullet.annihilated=7 enemy:scguns.bullet.decimated=7 enemy:scguns.bullet.eliminated=7 enemy:scguns.bullet.executed=7 enemy:scguns.bullet.killed=7

## Motivos de decisión (cambios registrados): ENGAGE 6693, LOW_HEALTH_RETREAT 372, NONE 174, OUT_OF_RANGE 5384, SEEK_ENEMY 67, STUCK 12

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t50-caballo-pax32-arqueros-7-escudofrontal - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t50-caballo-pax32-arqueros-2-hierro - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
