# Simulación Rally - lote 20261109-012047-t31-encanto-ignis

Preset `t31-encanto-ignis` · velocidad `sprint` · 41 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t31-encanto-ignis-hierro | 8 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 99 ± 10 | 183 ± 16 | 558 ± 23 | 83 % ± 2 | 17.3 ± 1.3 | 12.5 ± 1.3 | 0 |
| t31-encanto-ignis-poder | 8 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 111 ± 6 | 275 ± 30 | 536 ± 17 | 85 % ± 1 | 22.5 ± 1.5 | 16.5 ± 1.0 | 0 |
| t31-encanto-ignis-filo | 8 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 101 ± 10 | 202 ± 15 | 528 ± 25 | 88 % ± 2 | 17.9 ± 1.9 | 13.9 ± 1.7 | 0 |
| t31-encanto-ignis-proteccion | 8 | 25 % ± 9 | 75 % ± 9 | 1.1 ± 0.4 | 1.0 ± 0.0 | 271 ± 15 | 458 ± 54 | 845 ± 22 | 83 % ± 2 | 24.2 ± 1.8 | 21.2 ± 1.6 | 0 |
| t31-encanto-ignis-netheritamas | 8 | 50 % ± 16 | 50 % ± 16 | 4.6 ± 1.7 | 0.6 ± 0.2 | 222 ± 27 | 1093 ± 136 | 758 ± 76 | 82 % ± 3 | 18.9 ± 2.2 | 17.0 ± 2.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t31-encanto-ignis-hierro semilla 101: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 9 de 9 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t31-encanto-ignis-hierro: enemy:mob=123 env:inFire=1 env:onFire=4
- t31-encanto-ignis-poder: enemy:cataclysm.flame_strike=1 enemy:mob=124 env:onFire=3
- t31-encanto-ignis-filo: enemy:mob=127 env:onFire=1
- t31-encanto-ignis-proteccion: enemy:mob=112 env:fall=2 env:inFire=1 env:onFire=4
- t31-encanto-ignis-netheritamas: enemy:cataclysm.flame_strike=9 enemy:mob=77 env:inFire=3 env:onFire=2

## Motivos de decisión (cambios registrados): COUNTERATTACK 84, ENGAGE 1466, FORMATION_HOLD 2, LOW_HEALTH_RETREAT 185, MORALE_SHAKEN 795, MORALE_WAVERING 628, NONE 466, OUT_OF_RANGE 6, RALLIED 74, REFUSE_ATTACK 103, ROUTED_FLEE 569, SEEK_ENEMY 34, STUCK 8, TOO_CLOSE 89, UNIT_BROKEN 3

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
