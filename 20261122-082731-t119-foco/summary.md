# Simulación Rally - lote 20261122-082731-t119-foco

Preset `t119-foco` · velocidad `sprint` · 212 corridas · 7 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t119-pistoleros-rally8 | 30 | 97 % ± 3 | 3 % ± 3 | 5.0 ± 0.4 | 0.4 ± 0.4 | 49 ± 2 | 1228 ± 12 | 121 ± 12 | 69 % ± 1 | 45.0 ± 0.7 | 23.9 ± 1.6 | 0 |
| t119-pistoleros-sinblanco | 30 | 100 % ± 0 | 0 % ± 0 | 4.8 ± 0.3 | 0.0 ± 0.0 | 48 ± 1 | 1228 ± 11 | 126 ± 11 | 68 % ± 1 | 45.7 ± 0.7 | 24.1 ± 1.8 | 0 |
| t119-pistoleros-sinfuego | 30 | 90 % ± 6 | 10 % ± 6 | 3.9 ± 0.4 | 0.5 ± 0.4 | 50 ± 1 | 1162 ± 16 | 157 ± 13 | 71 % ± 1 | 43.1 ± 0.6 | 18.9 ± 2.2 | 0 |
| t119-vind-rally8 | 30 | 100 % ± 0 | 0 % ± 0 | 3.8 ± 0.1 | 0.0 ± 0.0 | 41 ± 1 | 839 ± 9 | 112 ± 3 | 76 % ± 1 | 17.7 ± 0.7 | 19.1 ± 1.2 | 0 |
| t119-vind-indiv8 | 30 | 100 % ± 0 | 0 % ± 0 | 3.8 ± 0.1 | 0.0 ± 0.0 | 39 ± 1 | 841 ± 8 | 113 ± 2 | 78 % ± 1 | 16.5 ± 0.9 | 16.9 ± 0.9 | 0 |
| t119-vind-sinfuego | 30 | 100 % ± 0 | 0 % ± 0 | 3.8 ± 0.1 | 0.0 ± 0.0 | 41 ± 1 | 837 ± 7 | 115 ± 3 | 76 % ± 1 | 17.9 ± 1.0 | 18.7 ± 1.1 | 1 |
| t119-vind-sinblanco | 30 | 100 % ± 0 | 0 % ± 0 | 3.9 ± 0.0 | 0.0 ± 0.0 | 39 ± 1 | 843 ± 6 | 110 ± 2 | 77 % ± 1 | 16.2 ± 0.8 | 17.7 ± 0.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t119-pistoleros-rally8 semilla 11100: sprint → blue (6/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 206 de 206 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t119-pistoleros-rally8: enemy:scguns.bullet.annihilated=13 enemy:scguns.bullet.decimated=20 enemy:scguns.bullet.eliminated=24 enemy:scguns.bullet.executed=13 enemy:scguns.bullet.killed=20
- t119-pistoleros-sinblanco: enemy:scguns.bullet.annihilated=23 enemy:scguns.bullet.decimated=17 enemy:scguns.bullet.eliminated=19 enemy:scguns.bullet.executed=22 enemy:scguns.bullet.killed=16
- t119-pistoleros-sinfuego: enemy:scguns.bullet.annihilated=25 enemy:scguns.bullet.decimated=21 enemy:scguns.bullet.eliminated=23 enemy:scguns.bullet.executed=33 enemy:scguns.bullet.killed=22
- t119-vind-rally8: enemy:mob=117 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.killed=1
- t119-vind-indiv8: enemy:mob=118 enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=1
- t119-vind-sinfuego: enemy:mob=120 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.killed=2
- t119-vind-sinblanco: enemy:mob=121 enemy:scguns.bullet.eliminated=1

## Motivos de decisión (cambios registrados): ENGAGE 5316, FORMATION_HOLD 1, LOW_HEALTH_RETREAT 208, NONE 372, OUT_OF_RANGE 4368, SEEK_ENEMY 24, TOO_CLOSE 70

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t119-vind-sinfuego - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
