# Simulación Rally - lote 20261128-054632-t241-cab-sync

Preset `t241-cab-sync` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t241-flat-jinete | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 10.6 ± 0.2 | 10 ± 0 | 98 ± 3 | 197 ± 1 | 0 % ± 0 | 8.1 ± 0.2 | 4.2 ± 0.2 | 0 |
| t241-flat-jinete-sync | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 9.8 ± 0.3 | 11 ± 0 | 115 ± 4 | 197 ± 1 | 0 % ± 0 | 7.9 ± 0.1 | 4.4 ± 0.2 | 0 |
| t241-corr-jinete | 30 | 3 % ± 2 | 97 % ± 2 | 0.1 ± 0.1 | 8.2 ± 0.4 | 34 ± 8 | 124 ± 7 | 193 ± 2 | 0 % ± 0 | 10.0 ± 0.2 | 4.8 ± 0.5 | 0 |
| t241-corr-jinete-sync | 30 | 10 % ± 5 | 90 % ± 5 | 0.3 ± 0.2 | 6.0 ± 0.6 | 39 ± 8 | 171 ± 9 | 189 ± 4 | 0 % ± 0 | 10.1 ± 0.1 | 4.8 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t241-flat-jinete semilla 23700: sprint → red (0/12 vivos), normal → red (0/12 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 6 de 6 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t241-flat-jinete: enemy:mob=238 enemy:scguns.bullet.executed=2
- t241-flat-jinete-sync: enemy:mob=240
- t241-corr-jinete: enemy:mob=236 enemy:scguns.bullet.executed=1
- t241-corr-jinete-sync: enemy:mob=228 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.killed=1

## Motivos de decisión (cambios registrados): ENGAGE 969, FORMATION_HOLD 291, LOW_HEALTH_RETREAT 65, SEEK_ENEMY 11

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
