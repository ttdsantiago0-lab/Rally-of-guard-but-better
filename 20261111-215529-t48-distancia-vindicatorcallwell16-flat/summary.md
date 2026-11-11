# Simulación Rally - lote 20261111-215529-t48-distancia-vindicatorcallwell16-flat

Preset `t48-distancia-vindicatorcallwell16-flat` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t48-distancia-vindicatorcallwell16-flat-arco-regla | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 22 ± 1 | 438 ± 2 | 0 ± 0 | 57 % ± 1 | 42.5 ± 0.3 | 10.3 ± 0.7 | 0 |
| t48-distancia-vindicatorcallwell16-flat-arco-sinregla | 30 | 100 % ± 0 | 0 % ± 0 | 7.2 ± 0.1 | 0.0 ± 0.0 | 10 ± 0 | 438 ± 2 | 27 ± 3 | 99 % ± 0 | 16.6 ± 0.1 | 6.7 ± 0.1 | 0 |
| t48-distancia-vindicatorcallwell16-flat-linea-regla | 30 | 100 % ± 0 | 0 % ± 0 | 4.2 ± 0.1 | 0.0 ± 0.0 | 32 ± 1 | 394 ± 6 | 99 ± 1 | 58 % ± 1 | 35.4 ± 0.6 | 17.9 ± 1.2 | 0 |
| t48-distancia-vindicatorcallwell16-flat-linea-sinregla | 30 | 57 % ± 9 | 43 % ± 9 | 2.5 ± 0.5 | 2.0 ± 0.5 | 17 ± 0 | 355 ± 7 | 144 ± 9 | 97 % ± 0 | 13.0 ± 0.2 | 5.8 ± 0.2 | 0 |
| t48-distancia-vindicatorcallwell16-flat-lineascutum-regla | 30 | 100 % ± 0 | 0 % ± 0 | 4.2 ± 0.1 | 0.0 ± 0.0 | 32 ± 1 | 388 ± 5 | 100 ± 1 | 57 % ± 1 | 35.7 ± 0.6 | 17.4 ± 1.2 | 0 |
| t48-distancia-vindicatorcallwell16-flat-lineascutum-sinregla | 30 | 80 % ± 7 | 20 % ± 7 | 3.3 ± 0.4 | 1.3 ± 0.5 | 17 ± 0 | 367 ± 6 | 141 ± 6 | 98 % ± 0 | 12.8 ± 0.2 | 6.8 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t48-distancia-vindicatorcallwell16-flat-arco-regla semilla 2001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 161 de 161 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t48-distancia-vindicatorcallwell16-flat-arco-sinregla: enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=6 enemy:scguns.bullet.eliminated=7 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=5
- t48-distancia-vindicatorcallwell16-flat-linea-regla: enemy:scguns.bullet.annihilated=11 enemy:scguns.bullet.decimated=20 enemy:scguns.bullet.eliminated=24 enemy:scguns.bullet.executed=35 enemy:scguns.bullet.killed=23
- t48-distancia-vindicatorcallwell16-flat-linea-sinregla: enemy:scguns.bullet.annihilated=40 enemy:scguns.bullet.decimated=31 enemy:scguns.bullet.eliminated=30 enemy:scguns.bullet.executed=23 enemy:scguns.bullet.killed=41
- t48-distancia-vindicatorcallwell16-flat-lineascutum-regla: enemy:scguns.bullet.annihilated=18 enemy:scguns.bullet.decimated=22 enemy:scguns.bullet.eliminated=29 enemy:scguns.bullet.executed=19 enemy:scguns.bullet.killed=25
- t48-distancia-vindicatorcallwell16-flat-lineascutum-sinregla: enemy:scguns.bullet.annihilated=30 enemy:scguns.bullet.decimated=25 enemy:scguns.bullet.eliminated=34 enemy:scguns.bullet.executed=22 enemy:scguns.bullet.killed=31

## Motivos de decisión (cambios registrados): ENGAGE 2607, LOW_HEALTH_RETREAT 222, NONE 77, OUT_OF_RANGE 1393, SEEK_ENEMY 87

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
