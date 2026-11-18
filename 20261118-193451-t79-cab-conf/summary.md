# Simulación Rally - lote 20261118-193451-t79-cab-conf

Preset `t79-cab-conf` · velocidad `sprint` · 186 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t79-cab | 30 | 100 % ± 0 | 0 % ± 0 | 3.6 ± 0.1 | 0.0 ± 0.0 | 47 ± 2 | 561 ± 9 | 125 ± 5 | 66 % ± 1 | 43.8 ± 0.6 | 18.7 ± 2.1 | 1 |
| t79-cab-escudos | 30 | 100 % ± 0 | 0 % ± 0 | 6.9 ± 0.2 | 0.0 ± 0.0 | 24 ± 1 | 323 ± 9 | 53 ± 4 | 60 % ± 1 | 32.3 ± 0.4 | 21.5 ± 0.3 | 0 |
| t79-p48-cab | 30 | 93 % ± 5 | 7 % ± 5 | 2.8 ± 0.2 | 0.5 ± 0.5 | 53 ± 2 | 681 ± 13 | 150 ± 6 | 72 % ± 1 | 43.5 ± 0.6 | 16.0 ± 1.8 | 3 |
| t79-p48-cabarcos | 30 | 100 % ± 0 | 0 % ± 0 | 6.0 ± 0.3 | 0.0 ± 0.0 | 57 ± 3 | 1204 ± 14 | 86 ± 9 | 71 % ± 1 | 46.5 ± 1.2 | 23.4 ± 2.1 | 1 |
| t79-p48-cab-escudos | 30 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.2 | 0.0 ± 0.0 | 26 ± 1 | 352 ± 7 | 58 ± 4 | 64 % ± 1 | 33.6 ± 0.5 | 20.4 ± 0.4 | 0 |
| t79-p48-escudos8 | 30 | 83 % ± 7 | 17 % ± 7 | 4.3 ± 0.5 | 1.0 ± 0.5 | 33 ± 1 | 191 ± 8 | 153 ± 8 | 0 % ± 0 | 8.3 ± 0.1 | 5.7 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t79-cab semilla 7900: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 173 de 173 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t79-cab: enemy:scguns.bullet.annihilated=20 enemy:scguns.bullet.decimated=22 enemy:scguns.bullet.eliminated=26 enemy:scguns.bullet.executed=28 enemy:scguns.bullet.killed=34 env:inWall=1
- t79-cab-escudos: enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.decimated=7 enemy:scguns.bullet.eliminated=11 enemy:scguns.bullet.executed=5 enemy:scguns.bullet.killed=6
- t79-p48-cab: enemy:scguns.bullet.annihilated=26 enemy:scguns.bullet.decimated=38 enemy:scguns.bullet.eliminated=30 enemy:scguns.bullet.executed=31 enemy:scguns.bullet.killed=29 env:inWall=1
- t79-p48-cabarcos: enemy:scguns.bullet.annihilated=11 enemy:scguns.bullet.decimated=10 enemy:scguns.bullet.eliminated=9 enemy:scguns.bullet.executed=13 enemy:scguns.bullet.killed=16
- t79-p48-cab-escudos: enemy:scguns.bullet.annihilated=7 enemy:scguns.bullet.decimated=10 enemy:scguns.bullet.eliminated=10 enemy:scguns.bullet.executed=10 enemy:scguns.bullet.killed=8
- t79-p48-escudos8: enemy:scguns.bullet.annihilated=23 enemy:scguns.bullet.decimated=24 enemy:scguns.bullet.eliminated=16 enemy:scguns.bullet.executed=29 enemy:scguns.bullet.killed=19

## Motivos de decisión (cambios registrados): ENGAGE 3434, FORMATION_HOLD 211, LOW_HEALTH_RETREAT 265, NONE 202, OUT_OF_RANGE 2505, SEEK_ENEMY 111, STUCK 8

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t79-p48-cab - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t79-p48-cabarcos - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t79-cab - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
