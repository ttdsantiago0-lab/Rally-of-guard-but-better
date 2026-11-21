# Simulación Rally - lote 20261121-034303-t111-multiplicador

Preset `t111-multiplicador` · velocidad `sprint` · 301 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t111-horda-rally8 | 30 | 100 % ± 0 | 0 % ± 0 | 6.2 ± 0.3 | 0.0 ± 0.0 | 26 ± 1 | 334 ± 8 | 117 ± 8 | 48 % ± 2 | 13.4 ± 0.5 | 5.6 ± 0.4 | 0 |
| t111-horda-sin8 | 30 | 10 % ± 6 | 90 % ± 6 | 0.2 ± 0.1 | 8.0 ± 0.8 | 21 ± 1 | 80 ± 9 | 185 ± 2 | 0 % ± 0 | 9.8 ± 0.2 | 3.1 ± 0.1 | 0 |
| t111-horda-sin12 | 30 | 33 % ± 9 | 67 % ± 9 | 0.9 ± 0.3 | 5.1 ± 0.8 | 26 ± 1 | 191 ± 10 | 269 ± 4 | 0 % ± 0 | 8.5 ± 0.1 | 5.7 ± 0.2 | 0 |
| t111-horda-sin16 | 30 | 37 % ± 9 | 63 % ± 9 | 1.6 ± 0.4 | 5.3 ± 0.9 | 26 ± 1 | 203 ± 11 | 355 ± 6 | 0 % ± 0 | 9.1 ± 0.1 | 6.5 ± 0.2 | 0 |
| t111-horda-sin24 | 30 | 90 % ± 6 | 10 % ± 6 | 7.0 ± 0.6 | 0.7 ± 0.4 | 27 ± 1 | 338 ± 8 | 466 ± 10 | 0 % ± 0 | 9.1 ± 0.1 | 6.3 ± 0.2 | 0 |
| t111-pistoleros-rally8 | 30 | 90 % ± 6 | 10 % ± 6 | 4.8 ± 0.4 | 0.8 ± 0.5 | 48 ± 1 | 1226 ± 16 | 119 ± 11 | 69 % ± 1 | 45.1 ± 0.7 | 21.4 ± 1.5 | 0 |
| t111-pistoleros-sin8 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 42.2 ± 0.6 | 6 ± 0 | 7 ± 1 | 191 ± 1 | 0 % ± 0 | 17.9 ± 0.2 | 2.7 ± 0.1 | 0 |
| t111-pistoleros-sin12 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 38.0 ± 0.5 | 6 ± 0 | 19 ± 1 | 276 ± 3 | 0 % ± 0 | 17.2 ± 0.2 | 3.8 ± 0.1 | 0 |
| t111-pistoleros-sin16 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 35.1 ± 0.8 | 7 ± 0 | 29 ± 2 | 370 ± 3 | 0 % ± 0 | 16.7 ± 0.1 | 6.2 ± 0.3 | 0 |
| t111-pistoleros-sin24 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 25.8 ± 0.7 | 9 ± 0 | 41 ± 2 | 562 ± 3 | 0 % ± 0 | 15.9 ± 0.1 | 4.2 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t111-horda-rally8 semilla 10400: sprint → blue (8/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 57 de 57 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t111-horda-rally8: enemy:scguns.bullet.annihilated=14 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=11 enemy:scguns.bullet.executed=14 enemy:scguns.bullet.killed=10
- t111-horda-sin8: enemy:scguns.bullet.annihilated=44 enemy:scguns.bullet.decimated=58 enemy:scguns.bullet.eliminated=41 enemy:scguns.bullet.executed=52 enemy:scguns.bullet.killed=38
- t111-horda-sin12: enemy:scguns.bullet.annihilated=79 enemy:scguns.bullet.decimated=61 enemy:scguns.bullet.eliminated=59 enemy:scguns.bullet.executed=67 enemy:scguns.bullet.killed=67
- t111-horda-sin16: enemy:scguns.bullet.annihilated=86 enemy:scguns.bullet.decimated=93 enemy:scguns.bullet.eliminated=77 enemy:scguns.bullet.executed=84 enemy:scguns.bullet.killed=92 friend:arrow=1
- t111-horda-sin24: enemy:scguns.bullet.annihilated=114 enemy:scguns.bullet.decimated=98 enemy:scguns.bullet.eliminated=102 enemy:scguns.bullet.executed=100 enemy:scguns.bullet.killed=94 friend:arrow=3
- t111-pistoleros-rally8: enemy:scguns.bullet.annihilated=15 enemy:scguns.bullet.decimated=23 enemy:scguns.bullet.eliminated=23 enemy:scguns.bullet.executed=16 enemy:scguns.bullet.killed=19
- t111-pistoleros-sin8: enemy:scguns.bullet.annihilated=52 enemy:scguns.bullet.decimated=44 enemy:scguns.bullet.eliminated=44 enemy:scguns.bullet.executed=46 enemy:scguns.bullet.killed=53 friend:arrow=1
- t111-pistoleros-sin12: enemy:scguns.bullet.annihilated=80 enemy:scguns.bullet.decimated=70 enemy:scguns.bullet.eliminated=69 enemy:scguns.bullet.executed=64 enemy:scguns.bullet.killed=77
- t111-pistoleros-sin16: enemy:scguns.bullet.annihilated=95 enemy:scguns.bullet.decimated=88 enemy:scguns.bullet.eliminated=103 enemy:scguns.bullet.executed=104 enemy:scguns.bullet.killed=87 friend:arrow=2 other(minecraft:pillager):scguns.bullet.executed=1
- t111-pistoleros-sin24: enemy:scguns.bullet.annihilated=132 enemy:scguns.bullet.decimated=147 enemy:scguns.bullet.eliminated=158 enemy:scguns.bullet.executed=135 enemy:scguns.bullet.killed=147 friend:arrow=1

## Motivos de decisión (cambios registrados): ENGAGE 1576, LOW_HEALTH_RETREAT 115, NONE 96, OUT_OF_RANGE 1246, SEEK_ENEMY 43, TOO_CLOSE 25

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
