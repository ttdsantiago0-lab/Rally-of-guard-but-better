# Simulación Rally - lote 20261111-221114-t49-jinete-vindicadores32

Preset `t49-jinete-vindicadores32` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t49-jinete-vindicadores32-hoy | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 30.4 ± 0.2 | 10 ± 1 | 166 ± 6 | 197 ± 1 | 98 % ± 1 | 7.3 ± 0.2 | 4.4 ± 0.2 | 0 |
| t49-jinete-vindicadores32-kite | 30 | 87 % ± 6 | 13 % ± 6 | 2.4 ± 0.3 | 1.1 ± 0.5 | 43 ± 1 | 819 ± 8 | 160 ± 6 | 89 % ± 1 | 17.2 ± 0.8 | 12.9 ± 1.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t49-jinete-vindicadores32-hoy semilla 3001: sprint → red (0/31 vivos), normal → red (0/29 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 26 de 26 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t49-jinete-vindicadores32-hoy: enemy:mob=240
- t49-jinete-vindicadores32-kite: enemy:mob=166 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1

## Motivos de decisión (cambios registrados): ENGAGE 604, FORMATION_HOLD 57, LOW_HEALTH_RETREAT 34, NONE 4, OUT_OF_RANGE 73, SEEK_ENEMY 19, TOO_CLOSE 149

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
