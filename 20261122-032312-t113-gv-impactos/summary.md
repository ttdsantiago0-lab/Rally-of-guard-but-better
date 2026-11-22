# Simulación Rally - lote 20261122-032312-t113-gv-impactos

Preset `t113-gv-impactos` · velocidad `sprint` · 91 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t113-horda-rally8 | 30 | 100 % ± 0 | 0 % ± 0 | 7.0 ± 0.2 | 0.0 ± 0.0 | 24 ± 1 | 343 ± 9 | 104 ± 8 | 52 % ± 2 | 14.6 ± 0.4 | 6.1 ± 0.4 | 0 |
| t113-horda-sin8 | 30 | 3 % ± 3 | 97 % ± 3 | 0.1 ± 0.1 | 8.2 ± 0.5 | 21 ± 1 | 62 ± 6 | 186 ± 2 | 0 % ± 0 | 9.6 ± 0.2 | 2.9 ± 0.2 | 0 |
| t113-horda-sin16 | 30 | 20 % ± 7 | 80 % ± 7 | 0.7 ± 0.3 | 5.3 ± 0.7 | 25 ± 1 | 200 ± 10 | 369 ± 4 | 0 % ± 0 | 9.1 ± 0.1 | 6.2 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t113-horda-rally8 semilla 10500: sprint → blue (7/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 30 de 30 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t113-horda-rally8: enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.decimated=8 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=5 enemy:scguns.bullet.killed=9
- t113-horda-sin8: enemy:scguns.bullet.annihilated=46 enemy:scguns.bullet.decimated=49 enemy:scguns.bullet.eliminated=43 enemy:scguns.bullet.executed=51 enemy:scguns.bullet.killed=49
- t113-horda-sin16: enemy:scguns.bullet.annihilated=81 enemy:scguns.bullet.decimated=99 enemy:scguns.bullet.eliminated=90 enemy:scguns.bullet.executed=97 enemy:scguns.bullet.killed=91

## Motivos de decisión (cambios registrados): ENGAGE 285, LOW_HEALTH_RETREAT 60, OUT_OF_RANGE 22, SEEK_ENEMY 32, TOO_CLOSE 18

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
