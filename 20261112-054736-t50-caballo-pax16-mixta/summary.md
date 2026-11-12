# Simulación Rally - lote 20261112-054736-t50-caballo-pax16-mixta

Preset `t50-caballo-pax16-mixta` · velocidad `sprint` · 246 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t50-caballo-pax16-mixta-1-sinarmadura | 30 | 100 % ± 0 | 0 % ± 0 | 3.8 ± 0.1 | 0.0 ± 0.0 | 27 ± 1 | 353 ± 5 | 112 ± 3 | 64 % ± 1 | 39.3 ± 0.4 | 14.1 ± 1.7 | 2 |
| t50-caballo-pax16-mixta-2-hierro | 30 | 100 % ± 0 | 0 % ± 0 | 3.7 ± 0.1 | 0.0 ± 0.0 | 29 ± 1 | 360 ± 6 | 115 ± 3 | 62 % ± 1 | 38.7 ± 0.5 | 15.7 ± 1.6 | 2 |
| t50-caballo-pax16-mixta-3-diamante | 30 | 100 % ± 0 | 0 % ± 0 | 3.8 ± 0.1 | 0.0 ± 0.0 | 27 ± 1 | 355 ± 6 | 110 ± 3 | 64 % ± 1 | 38.9 ± 0.5 | 13.1 ± 1.6 | 0 |
| t50-caballo-pax16-mixta-4-superarmadura | 30 | 100 % ± 0 | 0 % ± 0 | 3.9 ± 0.1 | 0.0 ± 0.0 | 28 ± 1 | 336 ± 7 | 112 ± 3 | 61 % ± 1 | 39.3 ± 0.4 | 13.3 ± 1.4 | 0 |
| t50-caballo-pax16-mixta-5-vida50 | 30 | 100 % ± 0 | 0 % ± 0 | 3.7 ± 0.1 | 0.0 ± 0.0 | 28 ± 1 | 356 ± 5 | 113 ± 3 | 63 % ± 1 | 38.7 ± 0.5 | 11.7 ± 1.4 | 0 |
| t50-caballo-pax16-mixta-6-diamantevida50 | 30 | 100 % ± 0 | 0 % ± 0 | 3.7 ± 0.1 | 0.0 ± 0.0 | 29 ± 1 | 348 ± 6 | 117 ± 4 | 62 % ± 1 | 39.2 ± 0.3 | 15.3 ± 1.5 | 0 |
| t50-caballo-pax16-mixta-7-escudofrontal | 30 | 100 % ± 0 | 0 % ± 0 | 3.7 ± 0.1 | 0.0 ± 0.0 | 27 ± 1 | 358 ± 5 | 117 ± 4 | 63 % ± 1 | 38.9 ± 0.4 | 12.4 ± 1.4 | 1 |
| t50-caballo-pax16-mixta-8-inmortal | 30 | 100 % ± 0 | 0 % ± 0 | 3.9 ± 0.1 | 0.0 ± 0.0 | 27 ± 1 | 323 ± 8 | 109 ± 4 | 56 % ± 1 | 39.4 ± 0.4 | 16.4 ± 1.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t50-caballo-pax16-mixta-1-sinarmadura semilla 4001: sprint → blue (3/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 240 de 240 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t50-caballo-pax16-mixta-1-sinarmadura: enemy:scguns.bullet.annihilated=24 enemy:scguns.bullet.decimated=20 enemy:scguns.bullet.eliminated=23 enemy:scguns.bullet.executed=26 enemy:scguns.bullet.killed=33
- t50-caballo-pax16-mixta-2-hierro: enemy:scguns.bullet.annihilated=29 enemy:scguns.bullet.decimated=25 enemy:scguns.bullet.eliminated=22 enemy:scguns.bullet.executed=26 enemy:scguns.bullet.killed=26 env:inWall=1
- t50-caballo-pax16-mixta-3-diamante: enemy:scguns.bullet.annihilated=24 enemy:scguns.bullet.decimated=29 enemy:scguns.bullet.eliminated=25 enemy:scguns.bullet.executed=29 enemy:scguns.bullet.killed=18
- t50-caballo-pax16-mixta-4-superarmadura: enemy:scguns.bullet.annihilated=19 enemy:scguns.bullet.decimated=29 enemy:scguns.bullet.eliminated=31 enemy:scguns.bullet.executed=23 enemy:scguns.bullet.killed=22
- t50-caballo-pax16-mixta-5-vida50: enemy:scguns.bullet.annihilated=22 enemy:scguns.bullet.decimated=28 enemy:scguns.bullet.eliminated=21 enemy:scguns.bullet.executed=25 enemy:scguns.bullet.killed=34
- t50-caballo-pax16-mixta-6-diamantevida50: enemy:scguns.bullet.annihilated=26 enemy:scguns.bullet.decimated=22 enemy:scguns.bullet.eliminated=28 enemy:scguns.bullet.executed=25 enemy:scguns.bullet.killed=26 env:inWall=1
- t50-caballo-pax16-mixta-7-escudofrontal: enemy:scguns.bullet.annihilated=19 enemy:scguns.bullet.decimated=29 enemy:scguns.bullet.eliminated=29 enemy:scguns.bullet.executed=23 enemy:scguns.bullet.killed=28
- t50-caballo-pax16-mixta-8-inmortal: enemy:scguns.bullet.annihilated=30 enemy:scguns.bullet.decimated=12 enemy:scguns.bullet.eliminated=22 enemy:scguns.bullet.executed=29 enemy:scguns.bullet.killed=29

## Motivos de decisión (cambios registrados): ENGAGE 4221, FORMATION_HOLD 294, LOW_HEALTH_RETREAT 342, NONE 68, OUT_OF_RANGE 2592, SEEK_ENEMY 22, STUCK 4

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t50-caballo-pax16-mixta-1-sinarmadura - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t50-caballo-pax16-mixta-7-escudofrontal - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t50-caballo-pax16-mixta-2-hierro - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
