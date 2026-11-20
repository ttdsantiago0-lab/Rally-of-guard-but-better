# Simulación Rally - lote 20261120-200435-t110-aldea-juego

Preset `t110-aldea-juego` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t110-aldea-juego | 30 | 93 % ± 3 | 7 % ± 3 | 7.1 ± 0.2 | 0.5 ± 0.3 | 73 ± 8 | 349 ± 8 | 97 ± 8 | 24 % ± 2 | 14.4 ± 0.8 | 9.7 ± 0.9 | 0 |
| t110-aldea-juego-sincob | 30 | 98 % ± 2 | 2 % ± 2 | 6.8 ± 0.2 | 0.0 ± 0.0 | 77 ± 5 | 337 ± 6 | 93 ± 4 | 12 % ± 1 | 31.8 ± 0.4 | 25.0 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t110-aldea-juego semilla 10300: sprint → blue (7/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t110-aldea-juego: enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=5 enemy:scguns.bullet.executed=10 enemy:scguns.bullet.killed=3
- t110-aldea-juego-sincob: enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.decimated=8 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=9 enemy:scguns.bullet.killed=5

## Motivos de decisión (cambios registrados): ENGAGE 1184, LOW_HEALTH_RETREAT 130, NONE 66, OUT_OF_RANGE 472, SEEK_ENEMY 172, STUCK 144, TOO_CLOSE 159

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
