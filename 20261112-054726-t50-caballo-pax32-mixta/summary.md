# Simulación Rally - lote 20261112-054726-t50-caballo-pax32-mixta

Preset `t50-caballo-pax32-mixta` · velocidad `sprint` · 290 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t50-caballo-pax32-mixta-1-sinarmadura | 28 | 64 % ± 9 | 36 % ± 9 | 1.7 ± 0.3 | 2.9 ± 1.0 | 40 ± 3 | 469 ± 15 | 175 ± 6 | 70 % ± 1 | 37.5 ± 0.4 | 14.8 ± 1.8 | 9 |
| t50-caballo-pax32-mixta-2-hierro | 26 | 77 % ± 8 | 23 % ± 8 | 2.2 ± 0.3 | 1.5 ± 0.6 | 37 ± 2 | 479 ± 15 | 161 ± 7 | 72 % ± 1 | 38.7 ± 0.5 | 14.2 ± 1.9 | 12 |
| t50-caballo-pax32-mixta-3-diamante | 29 | 62 % ± 9 | 38 % ± 9 | 1.8 ± 0.3 | 2.5 ± 0.9 | 36 ± 1 | 477 ± 13 | 179 ± 7 | 73 % ± 1 | 37.5 ± 0.4 | 14.5 ± 1.7 | 7 |
| t50-caballo-pax32-mixta-4-superarmadura | 30 | 93 % ± 5 | 7 % ± 5 | 3.0 ± 0.2 | 0.7 ± 0.5 | 36 ± 2 | 466 ± 12 | 139 ± 5 | 69 % ± 2 | 37.8 ± 0.5 | 15.1 ± 1.7 | 3 |
| t50-caballo-pax32-mixta-5-vida50 | 27 | 78 % ± 8 | 22 % ± 8 | 2.1 ± 0.3 | 1.8 ± 0.8 | 42 ± 3 | 476 ± 12 | 171 ± 7 | 69 % ± 2 | 37.5 ± 0.5 | 16.6 ± 1.9 | 11 |
| t50-caballo-pax32-mixta-6-diamantevida50 | 29 | 93 % ± 5 | 7 % ± 5 | 3.1 ± 0.2 | 0.5 ± 0.3 | 33 ± 1 | 478 ± 10 | 138 ± 6 | 73 % ± 1 | 36.9 ± 0.4 | 10.4 ± 1.7 | 7 |
| t50-caballo-pax32-mixta-7-escudofrontal | 25 | 60 % ± 10 | 40 % ± 10 | 1.6 ± 0.3 | 3.9 ± 1.1 | 38 ± 4 | 452 ± 12 | 178 ± 6 | 71 % ± 2 | 38.1 ± 0.6 | 15.0 ± 2.3 | 15 |
| t50-caballo-pax32-mixta-8-inmortal | 30 | 93 % ± 5 | 7 % ± 5 | 3.2 ± 0.2 | 0.6 ± 0.4 | 33 ± 1 | 459 ± 13 | 140 ± 6 | 69 % ± 2 | 37.2 ± 0.4 | 13.9 ± 1.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t50-caballo-pax32-mixta-1-sinarmadura semilla 4001: sprint → red (0/8 vivos), normal → draw (1/11 vivos)
- t50-caballo-pax32-mixta-1-sinarmadura semilla 11778: sprint → ? (0/0 vivos), normal → draw (2/9 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 175 de 175 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t50-caballo-pax32-mixta-1-sinarmadura: enemy:scguns.bullet.annihilated=40 enemy:scguns.bullet.decimated=41 enemy:scguns.bullet.eliminated=35 enemy:scguns.bullet.executed=26 enemy:scguns.bullet.killed=32 env:inWall=2
- t50-caballo-pax32-mixta-2-hierro: enemy:scguns.bullet.annihilated=27 enemy:scguns.bullet.decimated=23 enemy:scguns.bullet.eliminated=37 enemy:scguns.bullet.executed=43 enemy:scguns.bullet.killed=22
- t50-caballo-pax32-mixta-3-diamante: enemy:scguns.bullet.annihilated=44 enemy:scguns.bullet.decimated=42 enemy:scguns.bullet.eliminated=36 enemy:scguns.bullet.executed=25 enemy:scguns.bullet.killed=33
- t50-caballo-pax32-mixta-4-superarmadura: enemy:scguns.bullet.annihilated=24 enemy:scguns.bullet.decimated=24 enemy:scguns.bullet.eliminated=39 enemy:scguns.bullet.executed=32 enemy:scguns.bullet.killed=29 env:inWall=1
- t50-caballo-pax32-mixta-5-vida50: enemy:scguns.bullet.annihilated=30 enemy:scguns.bullet.decimated=33 enemy:scguns.bullet.eliminated=43 enemy:scguns.bullet.executed=32 enemy:scguns.bullet.killed=21 env:inWall=1
- t50-caballo-pax32-mixta-6-diamantevida50: enemy:scguns.bullet.annihilated=32 enemy:scguns.bullet.decimated=27 enemy:scguns.bullet.eliminated=32 enemy:scguns.bullet.executed=25 enemy:scguns.bullet.killed=26
- t50-caballo-pax32-mixta-7-escudofrontal: enemy:scguns.bullet.annihilated=27 enemy:scguns.bullet.decimated=29 enemy:scguns.bullet.eliminated=35 enemy:scguns.bullet.executed=42 enemy:scguns.bullet.killed=28
- t50-caballo-pax32-mixta-8-inmortal: enemy:scguns.bullet.annihilated=34 enemy:scguns.bullet.decimated=25 enemy:scguns.bullet.eliminated=30 enemy:scguns.bullet.executed=25 enemy:scguns.bullet.killed=29

## Motivos de decisión (cambios registrados): ENGAGE 4871, FORMATION_HOLD 364, LOW_HEALTH_RETREAT 580, NONE 86, OUT_OF_RANGE 2750, SEEK_ENEMY 48, STUCK 31

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t50-caballo-pax32-mixta-3-diamante - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t50-caballo-pax32-mixta-2-hierro - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t50-caballo-pax32-mixta-4-superarmadura - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t50-caballo-pax32-mixta-7-escudofrontal - 15 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t50-caballo-pax32-mixta-1-sinarmadura - 9 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t50-caballo-pax32-mixta-5-vida50 - 11 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t50-caballo-pax32-mixta-6-diamantevida50 - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t50-caballo-pax32-mixta-1-sinarmadura: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
