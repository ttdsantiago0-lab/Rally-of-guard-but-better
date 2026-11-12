# Simulación Rally - lote 20261112-055133-t50-caballo-pax16-arqueros

Preset `t50-caballo-pax16-arqueros` · velocidad `sprint` · 241 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t50-caballo-pax16-arqueros-1-sinarmadura | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.0 | 0.0 ± 0.0 | 23 ± 1 | 436 ± 2 | 8 ± 2 | 54 % ± 1 | 44.3 ± 0.4 | 14.8 ± 1.1 | 0 |
| t50-caballo-pax16-arqueros-2-hierro | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 23 ± 1 | 433 ± 2 | 14 ± 4 | 55 % ± 1 | 44.1 ± 0.4 | 13.5 ± 1.2 | 0 |
| t50-caballo-pax16-arqueros-3-diamante | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 22 ± 1 | 433 ± 2 | 13 ± 3 | 56 % ± 1 | 44.2 ± 0.4 | 15.6 ± 1.1 | 0 |
| t50-caballo-pax16-arqueros-4-superarmadura | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 23 ± 1 | 432 ± 2 | 9 ± 3 | 55 % ± 1 | 44.5 ± 0.4 | 14.6 ± 1.3 | 0 |
| t50-caballo-pax16-arqueros-5-vida50 | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 24 ± 1 | 432 ± 3 | 13 ± 3 | 54 % ± 1 | 44.1 ± 0.4 | 16.5 ± 1.3 | 0 |
| t50-caballo-pax16-arqueros-6-diamantevida50 | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 25 ± 1 | 435 ± 2 | 14 ± 3 | 54 % ± 1 | 44.7 ± 0.4 | 15.9 ± 1.5 | 0 |
| t50-caballo-pax16-arqueros-7-escudofrontal | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 23 ± 1 | 435 ± 3 | 13 ± 2 | 55 % ± 1 | 43.2 ± 0.4 | 15.7 ± 1.5 | 0 |
| t50-caballo-pax16-arqueros-8-inmortal | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 23 ± 1 | 430 ± 2 | 14 ± 3 | 54 % ± 1 | 44.1 ± 0.4 | 15.6 ± 1.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t50-caballo-pax16-arqueros-1-sinarmadura semilla 4001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 240 de 240 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t50-caballo-pax16-arqueros-1-sinarmadura: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.eliminated=1
- t50-caballo-pax16-arqueros-2-hierro: enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.killed=3
- t50-caballo-pax16-arqueros-3-diamante: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=2
- t50-caballo-pax16-arqueros-4-superarmadura: enemy:scguns.bullet.eliminated=1
- t50-caballo-pax16-arqueros-5-vida50: enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.killed=2
- t50-caballo-pax16-arqueros-6-diamantevida50: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.killed=1
- t50-caballo-pax16-arqueros-7-escudofrontal: enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=3
- t50-caballo-pax16-arqueros-8-inmortal: enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.killed=1

## Motivos de decisión (cambios registrados): ENGAGE 6271, LOW_HEALTH_RETREAT 32, NONE 157, OUT_OF_RANGE 5465, SEEK_ENEMY 6

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
