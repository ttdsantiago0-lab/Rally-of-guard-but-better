# Simulación Rally - lote 20261119-045919-t88-defecto-ignis

Preset `t88-defecto-ignis` · velocidad `sprint` · 91 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t88-ignis | 30 | 100 % ± 0 | 0 % ± 0 | 6.6 ± 0.2 | 0.0 ± 0.0 | 83 ± 1 | 488 ± 4 | 121 ± 8 | 89 % ± 0 | 18.4 ± 0.1 | 14.2 ± 0.3 | 0 |
| t88-ignis-sync | 30 | 100 % ± 0 | 0 % ± 0 | 6.6 ± 0.2 | 0.0 ± 0.0 | 105 ± 2 | 523 ± 4 | 130 ± 8 | 89 % ± 1 | 18.7 ± 0.2 | 14.6 ± 0.3 | 0 |
| t88-ignis-kiteoff | 30 | 87 % ± 6 | 13 % ± 6 | 4.0 ± 0.4 | 0.1 ± 0.1 | 75 ± 2 | 511 ± 13 | 239 ± 11 | 87 % ± 1 | 13.2 ± 0.2 | 9.3 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t88-ignis semilla 8600: sprint → blue (7/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 86 de 86 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t88-ignis: enemy:cataclysm.flame_strike=32 enemy:explosion.player=1 enemy:mob=5 env:onFire=3
- t88-ignis-sync: enemy:cataclysm.flame_strike=19 enemy:mob=22 env:inFire=1 env:onFire=1
- t88-ignis-kiteoff: enemy:cataclysm.flame_strike=15 enemy:explosion.player=2 enemy:mob=97 env:fall=4 env:inFire=1 env:onFire=1 other(cataclysm:ignis):cataclysm.flame_strike=1

## Motivos de decisión (cambios registrados): ENGAGE 1453, FORMATION_HOLD 6, LOW_HEALTH_RETREAT 537, NONE 12, OUT_OF_RANGE 39, SEEK_ENEMY 226, STUCK 36, TOO_CLOSE 485

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
