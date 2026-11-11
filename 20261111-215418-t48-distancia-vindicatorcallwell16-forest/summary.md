# Simulación Rally - lote 20261111-215418-t48-distancia-vindicatorcallwell16-forest

Preset `t48-distancia-vindicatorcallwell16-forest` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t48-distancia-vindicatorcallwell16-forest-arco-regla | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 27 ± 0 | 435 ± 2 | 1 ± 1 | 38 % ± 1 | 40.5 ± 0.4 | 17.2 ± 0.6 | 0 |
| t48-distancia-vindicatorcallwell16-forest-arco-sinregla | 30 | 17 % ± 7 | 83 % ± 7 | 0.3 ± 0.1 | 7.6 ± 0.8 | 16 ± 1 | 309 ± 13 | 174 ± 2 | 74 % ± 2 | 14.4 ± 0.1 | 4.1 ± 0.2 | 0 |
| t48-distancia-vindicatorcallwell16-forest-linea-regla | 30 | 100 % ± 0 | 0 % ± 0 | 3.6 ± 0.1 | 0.0 ± 0.0 | 40 ± 1 | 382 ± 9 | 100 ± 3 | 42 % ± 1 | 35.7 ± 0.6 | 18.9 ± 1.2 | 0 |
| t48-distancia-vindicatorcallwell16-forest-linea-sinregla | 30 | 13 % ± 6 | 87 % ± 6 | 0.5 ± 0.3 | 7.3 ± 0.7 | 18 ± 1 | 236 ± 13 | 184 ± 4 | 77 % ± 2 | 11.8 ± 0.2 | 5.3 ± 0.2 | 0 |
| t48-distancia-vindicatorcallwell16-forest-lineascutum-regla | 30 | 100 % ± 0 | 0 % ± 0 | 3.3 ± 0.2 | 0.0 ± 0.0 | 42 ± 1 | 389 ± 7 | 106 ± 3 | 42 % ± 1 | 37.6 ± 0.7 | 18.3 ± 1.1 | 0 |
| t48-distancia-vindicatorcallwell16-forest-lineascutum-sinregla | 30 | 17 % ± 7 | 83 % ± 7 | 0.6 ± 0.2 | 6.3 ± 0.8 | 18 ± 1 | 241 ± 11 | 179 ± 3 | 80 % ± 1 | 11.6 ± 0.3 | 5.0 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t48-distancia-vindicatorcallwell16-forest-arco-regla semilla 2001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 104 de 104 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t48-distancia-vindicatorcallwell16-forest-arco-regla: enemy:scguns.bullet.executed=1
- t48-distancia-vindicatorcallwell16-forest-arco-sinregla: enemy:scguns.bullet.annihilated=51 enemy:scguns.bullet.decimated=55 enemy:scguns.bullet.eliminated=43 enemy:scguns.bullet.executed=43 enemy:scguns.bullet.killed=39
- t48-distancia-vindicatorcallwell16-forest-linea-regla: enemy:scguns.bullet.annihilated=18 enemy:scguns.bullet.decimated=31 enemy:scguns.bullet.eliminated=34 enemy:scguns.bullet.executed=27 enemy:scguns.bullet.killed=21
- t48-distancia-vindicatorcallwell16-forest-linea-sinregla: enemy:scguns.bullet.annihilated=64 enemy:scguns.bullet.decimated=41 enemy:scguns.bullet.eliminated=34 enemy:scguns.bullet.executed=47 enemy:scguns.bullet.killed=39
- t48-distancia-vindicatorcallwell16-forest-lineascutum-regla: enemy:scguns.bullet.annihilated=26 enemy:scguns.bullet.decimated=30 enemy:scguns.bullet.eliminated=29 enemy:scguns.bullet.executed=26 enemy:scguns.bullet.killed=29
- t48-distancia-vindicatorcallwell16-forest-lineascutum-sinregla: enemy:scguns.bullet.annihilated=38 enemy:scguns.bullet.decimated=54 enemy:scguns.bullet.eliminated=43 enemy:scguns.bullet.executed=46 enemy:scguns.bullet.killed=42

## Motivos de decisión (cambios registrados): ENGAGE 2548, FORMATION_HOLD 1, LOW_HEALTH_RETREAT 231, NONE 41, OUT_OF_RANGE 1264, SEEK_ENEMY 76, TOO_CLOSE 5

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
