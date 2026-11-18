# Simulación Rally - lote 20261118-045652-t74-o1duro

Preset `t74-o1duro` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t74-pax48 | 30 | 67 % ± 9 | 33 % ± 9 | 2.9 ± 0.5 | 1.6 ± 0.6 | 55 ± 2 | 1132 ± 19 | 177 ± 11 | 75 % ± 1 | 44.5 ± 0.7 | 21.9 ± 2.0 | 0 |
| t74-pax48-spread | 30 | 100 % ± 0 | 0 % ± 0 | 5.2 ± 0.3 | 0.0 ± 0.0 | 48 ± 1 | 1244 ± 8 | 109 ± 7 | 68 % ± 1 | 45.8 ± 0.7 | 20.1 ± 1.5 | 0 |
| t74-pax48-dist | 30 | 3 % ± 3 | 97 % ± 3 | 0.1 ± 0.1 | 25.9 ± 1.8 | 34 ± 2 | 622 ± 31 | 242 ± 6 | 87 % ± 1 | 36.1 ± 0.2 | 6.9 ± 1.2 | 0 |
| t74-pax48-spread-dist | 30 | 10 % ± 6 | 90 % ± 6 | 0.2 ± 0.1 | 22.5 ± 2.0 | 34 ± 2 | 680 ± 38 | 249 ± 7 | 86 % ± 1 | 35.9 ± 0.2 | 8.0 ± 1.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t74-pax48 semilla 7401: sprint → blue (5/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 54 de 54 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t74-pax48: enemy:scguns.bullet.annihilated=18 enemy:scguns.bullet.decimated=42 enemy:scguns.bullet.eliminated=32 enemy:scguns.bullet.executed=32 enemy:scguns.bullet.killed=30
- t74-pax48-spread: enemy:scguns.bullet.annihilated=17 enemy:scguns.bullet.decimated=22 enemy:scguns.bullet.eliminated=10 enemy:scguns.bullet.executed=20 enemy:scguns.bullet.killed=15
- t74-pax48-dist: enemy:scguns.bullet.annihilated=48 enemy:scguns.bullet.decimated=43 enemy:scguns.bullet.eliminated=37 enemy:scguns.bullet.executed=39 enemy:scguns.bullet.killed=71
- t74-pax48-spread-dist: enemy:scguns.bullet.annihilated=44 enemy:scguns.bullet.decimated=39 enemy:scguns.bullet.eliminated=52 enemy:scguns.bullet.executed=50 enemy:scguns.bullet.killed=49

## Motivos de decisión (cambios registrados): ENGAGE 3456, LOW_HEALTH_RETREAT 322, NONE 289, OUT_OF_RANGE 2806, SEEK_ENEMY 6

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
