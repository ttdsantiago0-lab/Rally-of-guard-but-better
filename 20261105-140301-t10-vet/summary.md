# Simulación Rally - lote 20261105-140301-t10-vet

Preset `t10-vet` · velocidad `sprint` · 41 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t10-vet-linea-r0 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 298 ± 2 | 2198 ± 23 | 90 ± 7 | 80 % ± 1 | 11.9 ± 0.2 | 12.0 ± 0.3 | 0 |
| t10-vet-linea-r1 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 294 ± 4 | 2126 ± 39 | 83 ± 4 | 79 % ± 1 | 12.2 ± 0.2 | 12.5 ± 0.2 | 0 |
| t10-vet-linea-r3 | 10 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 290 ± 2 | 2114 ± 32 | 86 ± 6 | 80 % ± 1 | 11.9 ± 0.3 | 12.2 ± 0.3 | 0 |
| t10-vet-linea-r5 | 10 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 298 ± 2 | 2126 ± 20 | 82 ± 4 | 79 % ± 1 | 13.0 ± 0.2 | 12.9 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t10-vet-linea-r0 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t10-vet-linea-r3: enemy:explosion.player=1
- t10-vet-linea-r5: enemy:explosion.player=2

## Motivos de decisión (cambios registrados): COUNTERATTACK 4, ENGAGE 5837, FOCUS_FIRE 841, FORMATION_HOLD 28, LOW_HEALTH_RETREAT 18, MORALE_SHAKEN 185, MORALE_WAVERING 24, NONE 2986, OUTNUMBERED 29, OUT_OF_RANGE 330, PAIR_BOUND 499, RALLIED 1, REFUSE_ATTACK 4, ROUTED_FLEE 1, SEEK_ENEMY 844, SHOOT_AND_MOVE 267, TARGET_PRIORITY 2172, THREAT_MEMORY 11, TOO_CLOSE 378

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
