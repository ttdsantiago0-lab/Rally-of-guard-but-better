# Simulación Rally - lote 20261120-043241-t105-bosque-techo

Preset `t105-bosque-techo` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t105-pax32-linea | 30 | 100 % ± 0 | 0 % ± 0 | 6.3 ± 0.3 | 0.0 ± 0.0 | 32 ± 1 | 291 ± 10 | 82 ± 6 | 29 % ± 2 | 29.0 ± 0.5 | 19.7 ± 0.7 | 0 |
| t105-pax32-2e6a | 30 | 97 % ± 3 | 3 % ± 3 | 5.9 ± 0.3 | 0.3 ± 0.3 | 30 ± 1 | 378 ± 16 | 72 ± 9 | 36 % ± 2 | 38.4 ± 0.5 | 18.2 ± 1.1 | 0 |
| t105-pax32-6e2a | 30 | 100 % ± 0 | 0 % ± 0 | 6.2 ± 0.3 | 0.0 ± 0.0 | 30 ± 1 | 254 ± 6 | 104 ± 7 | 29 % ± 2 | 18.6 ± 0.3 | 16.4 ± 0.4 | 0 |
| t105-pax48-linea | 30 | 100 % ± 0 | 0 % ± 0 | 5.8 ± 0.3 | 0.0 ± 0.0 | 33 ± 1 | 330 ± 11 | 88 ± 5 | 33 % ± 2 | 29.9 ± 0.5 | 19.7 ± 0.6 | 0 |
| t105-pax48-6e2a | 30 | 93 % ± 5 | 7 % ± 5 | 4.3 ± 0.4 | 0.4 ± 0.3 | 37 ± 2 | 278 ± 9 | 147 ± 7 | 28 % ± 2 | 20.2 ± 0.3 | 17.4 ± 0.5 | 0 |
| t105-pax64-linea | 30 | 93 % ± 5 | 7 % ± 5 | 4.3 ± 0.4 | 0.6 ± 0.4 | 36 ± 1 | 365 ± 9 | 117 ± 8 | 39 % ± 2 | 31.0 ± 0.4 | 18.8 ± 1.0 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t105-pax32-linea semilla 9800: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 175 de 175 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t105-pax32-linea: enemy:scguns.bullet.annihilated=9 enemy:scguns.bullet.decimated=9 enemy:scguns.bullet.eliminated=9 enemy:scguns.bullet.executed=9 enemy:scguns.bullet.killed=14
- t105-pax32-2e6a: enemy:scguns.bullet.annihilated=13 enemy:scguns.bullet.decimated=9 enemy:scguns.bullet.eliminated=15 enemy:scguns.bullet.executed=14 enemy:scguns.bullet.killed=12
- t105-pax32-6e2a: enemy:scguns.bullet.annihilated=10 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=18 enemy:scguns.bullet.executed=10 enemy:scguns.bullet.killed=12
- t105-pax48-linea: enemy:scguns.bullet.annihilated=15 enemy:scguns.bullet.decimated=13 enemy:scguns.bullet.eliminated=13 enemy:scguns.bullet.executed=12 enemy:scguns.bullet.killed=12
- t105-pax48-6e2a: enemy:scguns.bullet.annihilated=33 enemy:scguns.bullet.decimated=23 enemy:scguns.bullet.eliminated=12 enemy:scguns.bullet.executed=23 enemy:scguns.bullet.killed=19
- t105-pax64-linea: enemy:scguns.bullet.annihilated=25 enemy:scguns.bullet.decimated=12 enemy:scguns.bullet.eliminated=27 enemy:scguns.bullet.executed=25 enemy:scguns.bullet.killed=22

## Motivos de decisión (cambios registrados): ENGAGE 3521, FORMATION_HOLD 1, LOW_HEALTH_RETREAT 429, NONE 101, OUT_OF_RANGE 2561, SEEK_ENEMY 145, STUCK 4

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
