# Simulación Rally - lote 20261120-041207-t103-bosque-comp

Preset `t103-bosque-comp` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t103-pax32-arco | 30 | 30 % ± 9 | 70 % ± 9 | 1.0 ± 0.3 | 7.5 ± 1.2 | 32 ± 1 | 610 ± 19 | 215 ± 7 | 51 % ± 1 | 38.2 ± 0.2 | 13.6 ± 1.1 | 0 |
| t103-pax32-linea | 30 | 100 % ± 0 | 0 % ± 0 | 6.3 ± 0.3 | 0.0 ± 0.0 | 30 ± 1 | 274 ± 8 | 74 ± 6 | 29 % ± 1 | 28.7 ± 0.4 | 20.8 ± 0.5 | 0 |
| t103-pax32-escudo | 30 | 83 % ± 7 | 17 % ± 7 | 5.1 ± 0.5 | 1.0 ± 0.4 | 32 ± 1 | 224 ± 7 | 152 ± 9 | 0 % ± 0 | 8.1 ± 0.1 | 4.9 ± 0.1 | 0 |
| t103-pax16-arco | 30 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.2 | 0.0 ± 0.0 | 25 ± 1 | 412 ± 4 | 68 ± 6 | 39 % ± 1 | 41.4 ± 0.3 | 17.7 ± 1.2 | 0 |
| t103-pax16-linea | 30 | 100 % ± 0 | 0 % ± 0 | 7.0 ± 0.3 | 0.0 ± 0.0 | 27 ± 1 | 258 ± 9 | 65 ± 6 | 28 % ± 1 | 28.1 ± 0.4 | 20.5 ± 0.4 | 0 |
| t103-pax16-escudo | 30 | 93 % ± 5 | 7 % ± 5 | 6.4 ± 0.4 | 0.3 ± 0.2 | 28 ± 1 | 207 ± 7 | 110 ± 9 | 0 % ± 0 | 8.5 ± 0.1 | 5.2 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t103-pax32-arco semilla 9700: sprint → blue (2/0 vivos), normal → red (0/5 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 152 de 152 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t103-pax32-arco: enemy:scguns.bullet.annihilated=43 enemy:scguns.bullet.decimated=36 enemy:scguns.bullet.eliminated=42 enemy:scguns.bullet.executed=42 enemy:scguns.bullet.killed=46
- t103-pax32-linea: enemy:scguns.bullet.annihilated=9 enemy:scguns.bullet.decimated=8 enemy:scguns.bullet.eliminated=11 enemy:scguns.bullet.executed=11 enemy:scguns.bullet.killed=12
- t103-pax32-escudo: enemy:scguns.bullet.annihilated=14 enemy:scguns.bullet.decimated=13 enemy:scguns.bullet.eliminated=16 enemy:scguns.bullet.executed=24 enemy:scguns.bullet.killed=19
- t103-pax16-arco: enemy:scguns.bullet.annihilated=9 enemy:scguns.bullet.decimated=10 enemy:scguns.bullet.eliminated=6 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=13
- t103-pax16-linea: enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=6 enemy:scguns.bullet.executed=13 enemy:scguns.bullet.killed=5
- t103-pax16-escudo: enemy:scguns.bullet.annihilated=8 enemy:scguns.bullet.decimated=9 enemy:scguns.bullet.eliminated=9 enemy:scguns.bullet.executed=14 enemy:scguns.bullet.killed=8

## Motivos de decisión (cambios registrados): ENGAGE 3481, LOW_HEALTH_RETREAT 383, NONE 71, OUT_OF_RANGE 2338, SEEK_ENEMY 208, STUCK 10

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t103-pax32-arco: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
