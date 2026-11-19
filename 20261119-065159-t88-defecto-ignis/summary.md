# Simulación Rally - lote 20261119-065159-t88-defecto-ignis

Preset `t88-defecto-ignis` · velocidad `sprint` · 91 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t88-ignis | 30 | 100 % ± 0 | 0 % ± 0 | 6.8 ± 0.2 | 0.0 ± 0.0 | 84 ± 1 | 494 ± 4 | 124 ± 7 | 89 % ± 0 | 18.3 ± 0.1 | 14.6 ± 0.2 | 0 |
| t88-ignis-sync | 30 | 100 % ± 0 | 0 % ± 0 | 6.0 ± 0.2 | 0.0 ± 0.0 | 110 ± 2 | 534 ± 5 | 134 ± 6 | 89 % ± 0 | 18.3 ± 0.1 | 13.6 ± 0.3 | 0 |
| t88-ignis-kiteoff | 30 | 100 % ± 0 | 0 % ± 0 | 4.6 ± 0.3 | 0.0 ± 0.0 | 74 ± 1 | 513 ± 8 | 234 ± 9 | 87 % ± 0 | 13.5 ± 0.1 | 9.1 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t88-ignis semilla 8900: sprint → blue (8/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 90 de 90 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t88-ignis: enemy:cataclysm.flame_strike=21 enemy:mob=15 env:inFire=1
- t88-ignis-sync: enemy:cataclysm.flame_strike=19 enemy:explosion.player=1 enemy:mob=35 env:onFire=4
- t88-ignis-kiteoff: enemy:cataclysm.flame_strike=22 enemy:mob=71 env:fall=1 env:inFire=1 env:onFire=2 other(cataclysm:ignis):cataclysm.flame_strike=4

## Motivos de decisión (cambios registrados): ENGAGE 1410, FORMATION_HOLD 1, LOW_HEALTH_RETREAT 537, NONE 14, OUT_OF_RANGE 4, SEEK_ENEMY 238, STUCK 11, TOO_CLOSE 476

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
