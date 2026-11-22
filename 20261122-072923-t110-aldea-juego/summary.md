# Simulación Rally - lote 20261122-072923-t110-aldea-juego

Preset `t110-aldea-juego` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t110-aldea-juego | 30 | 88 % ± 4 | 12 % ± 4 | 7.2 ± 0.2 | 1.2 ± 0.4 | 82 ± 10 | 338 ± 11 | 99 ± 7 | 29 % ± 2 | 13.6 ± 0.8 | 9.7 ± 0.8 | 0 |
| t110-aldea-juego-sincob | 30 | 95 % ± 3 | 5 % ± 3 | 7.0 ± 0.2 | 0.5 ± 0.3 | 82 ± 7 | 315 ± 7 | 90 ± 4 | 14 % ± 1 | 31.5 ± 0.7 | 24.4 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t110-aldea-juego semilla 10900: sprint → draw (7/6 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t110-aldea-juego: enemy:scguns.bullet.annihilated=9 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=5 enemy:scguns.bullet.killed=6
- t110-aldea-juego-sincob: enemy:scguns.bullet.annihilated=8 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=7

## Motivos de decisión (cambios registrados): ENGAGE 1132, LOW_HEALTH_RETREAT 112, NONE 51, OUT_OF_RANGE 444, SEEK_ENEMY 165, STUCK 116, TOO_CLOSE 164

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t110-aldea-juego: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
