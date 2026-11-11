# Simulación Rally - lote 20261111-075518-t41-distancia-vindicatorcallwell16

Preset `t41-distancia-vindicatorcallwell16` · velocidad `sprint` · 81 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t41-distancia-vindicatorcallwell16-linea-regla | 10 | 100 % ± 0 | 0 % ± 0 | 4.0 ± 0.0 | 0.0 ± 0.0 | 32 ± 1 | 398 ± 10 | 95 ± 2 | 53 % ± 2 | 35.7 ± 0.7 | 17.2 ± 1.8 | 0 |
| t41-distancia-vindicatorcallwell16-linea-sinregla | 10 | 60 % ± 16 | 40 % ± 16 | 1.9 ± 0.6 | 2.3 ± 1.1 | 19 ± 1 | 328 ± 20 | 161 ± 9 | 93 % ± 1 | 12.8 ± 0.4 | 5.9 ± 0.6 | 0 |
| t41-distancia-vindicatorcallwell16-arco-regla | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 21 ± 1 | 431 ± 3 | 0 ± 0 | 52 % ± 1 | 42.7 ± 0.6 | 7.3 ± 0.7 | 0 |
| t41-distancia-vindicatorcallwell16-arco-sinregla | 10 | 50 % ± 17 | 50 % ± 17 | 0.6 ± 0.2 | 3.7 ± 1.4 | 16 ± 2 | 374 ± 19 | 162 ± 3 | 89 % ± 1 | 14.3 ± 0.1 | 4.2 ± 0.5 | 0 |
| t41-distancia-vindicatorcallwell16-lineascutum-regla | 10 | 100 % ± 0 | 0 % ± 0 | 4.3 ± 0.3 | 0.0 ± 0.0 | 30 ± 1 | 372 ± 9 | 99 ± 3 | 50 % ± 1 | 35.5 ± 0.9 | 17.8 ± 1.8 | 0 |
| t41-distancia-vindicatorcallwell16-lineascutum-sinregla | 10 | 10 % ± 10 | 90 % ± 10 | 0.3 ± 0.3 | 7.9 ± 1.6 | 16 ± 2 | 257 ± 24 | 179 ± 8 | 93 % ± 1 | 13.1 ± 0.4 | 4.4 ± 0.3 | 0 |
| t41-distancia-vindicatorcallwell16-escudo-regla | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 9.7 ± 1.4 | 16 ± 1 | 70 ± 17 | 187 ± 3 | 0 % ± 0 | 10.7 ± 0.6 | 4.1 ± 0.3 | 0 |
| t41-distancia-vindicatorcallwell16-escudo-sinregla | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 9.4 ± 0.6 | 15 ± 1 | 63 ± 7 | 181 ± 3 | 0 % ± 0 | 9.8 ± 0.4 | 4.8 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t41-distancia-vindicatorcallwell16-linea-regla semilla 1: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 42 de 42 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t41-distancia-vindicatorcallwell16-linea-regla: enemy:scguns.bullet.annihilated=6 enemy:scguns.bullet.decimated=9 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=7 enemy:scguns.bullet.killed=10
- t41-distancia-vindicatorcallwell16-linea-sinregla: enemy:scguns.bullet.annihilated=17 enemy:scguns.bullet.decimated=16 enemy:scguns.bullet.eliminated=13 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=11
- t41-distancia-vindicatorcallwell16-arco-sinregla: enemy:scguns.bullet.annihilated=11 enemy:scguns.bullet.decimated=7 enemy:scguns.bullet.eliminated=19 enemy:scguns.bullet.executed=21 enemy:scguns.bullet.killed=16
- t41-distancia-vindicatorcallwell16-lineascutum-regla: enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=16 enemy:scguns.bullet.eliminated=6 enemy:scguns.bullet.executed=5 enemy:scguns.bullet.killed=8
- t41-distancia-vindicatorcallwell16-lineascutum-sinregla: enemy:scguns.bullet.annihilated=14 enemy:scguns.bullet.decimated=14 enemy:scguns.bullet.eliminated=22 enemy:scguns.bullet.executed=9 enemy:scguns.bullet.killed=18
- t41-distancia-vindicatorcallwell16-escudo-regla: enemy:scguns.bullet.annihilated=18 enemy:scguns.bullet.decimated=15 enemy:scguns.bullet.eliminated=20 enemy:scguns.bullet.executed=15 enemy:scguns.bullet.killed=12
- t41-distancia-vindicatorcallwell16-escudo-sinregla: enemy:scguns.bullet.annihilated=18 enemy:scguns.bullet.decimated=24 enemy:scguns.bullet.eliminated=14 enemy:scguns.bullet.executed=14 enemy:scguns.bullet.killed=10

## Motivos de decisión (cambios registrados): ENGAGE 1076, FORMATION_HOLD 2, LOW_HEALTH_RETREAT 123, NONE 51, OUT_OF_RANGE 496, SEEK_ENEMY 57

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
