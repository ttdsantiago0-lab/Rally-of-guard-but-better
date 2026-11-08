# Simulación Rally - lote 20261108-202902-t28-jefe-ignis

Preset `t28-jefe-ignis` · velocidad `sprint` · 19 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t28-jefe-ignis-g16 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 110 ± 14 | 165 ± 16 | 528 ± 14 | 87 % ± 1 | 20.1 ± 1.6 | 14.7 ± 1.0 | 0 |
| t28-jefe-ignis-g32 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 174 ± 17 | 255 ± 10 | 1157 ± 31 | 82 % ± 2 | 21.9 ± 2.0 | 18.7 ± 1.9 | 0 |
| t28-jefe-ignis-g64 | 6 | 17 % ± 11 | 83 % ± 11 | 0.7 ± 0.5 | 1.0 ± 0.0 | 285 ± 6 | 466 ± 40 | 2478 ± 70 | 79 % ± 2 | 26.4 ± 1.3 | 24.1 ± 1.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t28-jefe-ignis-g16 semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 2 de 2 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t28-jefe-ignis-g16: enemy:explosion.player=1 enemy:mob=94 env:onFire=1
- t28-jefe-ignis-g32: enemy:mob=187 env:inFire=2 env:onFire=3
- t28-jefe-ignis-g64: enemy:mob=373 env:inFire=2 env:onFire=5

## Motivos de decisión (cambios registrados): COUNTERATTACK 38, ENGAGE 1380, LOW_HEALTH_RETREAT 344, MORALE_SHAKEN 790, MORALE_WAVERING 608, NONE 552, OUT_OF_RANGE 5, RALLIED 41, REFUSE_ATTACK 62, ROUTED_FLEE 461, SEEK_ENEMY 283, STUCK 8, TOO_CLOSE 55, UNIT_BROKEN 3

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
