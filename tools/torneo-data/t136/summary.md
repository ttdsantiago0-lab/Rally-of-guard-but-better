# Simulación Rally - lote 20261123-040925-t136-cab-velocidad

Preset `t136-cab-velocidad` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t136-cab-velocidad-flat-pie | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 10.7 ± 0.2 | 10 ± 0 | 110 ± 4 | 198 ± 1 | 0 % ± 0 | 6.5 ± 0.1 | 4.1 ± 0.2 | 0 |
| t136-cab-velocidad-flat-jinete | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 10.6 ± 0.2 | 11 ± 0 | 91 ± 2 | 196 ± 1 | 0 % ± 0 | 8.4 ± 0.1 | 4.7 ± 0.2 | 0 |
| t136-cab-velocidad-flat-jinete-igual | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 10.6 ± 0.2 | 10 ± 0 | 93 ± 3 | 196 ± 0 | 0 % ± 0 | 8.0 ± 0.1 | 4.5 ± 0.2 | 0 |
| t136-cab-velocidad-corridors-pie | 30 | 33 % ± 8 | 67 % ± 8 | 1.3 ± 0.4 | 3.0 ± 0.5 | 39 ± 7 | 225 ± 9 | 176 ± 7 | 0 % ± 0 | 7.9 ± 0.2 | 3.9 ± 0.4 | 0 |
| t136-cab-velocidad-corridors-jinete | 30 | 5 % ± 3 | 95 % ± 3 | 0.2 ± 0.1 | 8.4 ± 0.3 | 40 ± 9 | 110 ± 5 | 190 ± 3 | 0 % ± 0 | 10.7 ± 0.2 | 5.2 ± 0.4 | 0 |
| t136-cab-velocidad-corridors-jinete-igual | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 8.8 ± 0.4 | 25 ± 4 | 120 ± 6 | 196 ± 0 | 0 % ± 0 | 10.1 ± 0.2 | 5.3 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t136-cab-velocidad-flat-pie semilla 12200: sprint → red (0/11 vivos), normal → red (0/7 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 14 de 14 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t136-cab-velocidad-flat-pie: enemy:mob=228 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=3
- t136-cab-velocidad-flat-jinete: enemy:mob=240
- t136-cab-velocidad-flat-jinete-igual: enemy:mob=240
- t136-cab-velocidad-corridors-pie: enemy:mob=178 enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.decimated=10 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=1
- t136-cab-velocidad-corridors-jinete: enemy:mob=233 enemy:scguns.bullet.eliminated=1
- t136-cab-velocidad-corridors-jinete-igual: enemy:mob=240

## Motivos de decisión (cambios registrados): ENGAGE 1450, FORMATION_HOLD 283, LOW_HEALTH_RETREAT 124, SEEK_ENEMY 21, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
