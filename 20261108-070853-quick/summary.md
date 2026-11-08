# Simulación Rally - lote 20261108-070853-quick

Preset `quick` · velocidad `sprint` · 15 corridas · 14 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| mirror-melee-2 | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 2.0 ± 0.0 | 10 ± 0 | 29 ± 0 | 48 ± 0 | 0 % ± 0 | 3.9 ± 0.0 | 1.0 ± 0.0 | 0 |
| mirror-melee-5 | 1 | 100 % ± 0 | 0 % ± 0 | 5.0 ± 0.0 | 0.0 ± 0.0 | 13 ± 0 | 120 ± 0 | 77 ± 0 | 0 % ± 0 | 5.0 ± 0.0 | 3.5 ± 0.0 | 0 |
| mirror-archer-5 | 1 | 100 % ± 0 | 0 % ± 0 | 5.0 ± 0.0 | 0.0 ± 0.0 | 13 ± 0 | 113 ± 0 | 67 ± 0 | 95 % ± 0 | 30.5 ± 0.0 | 6.5 ± 0.0 | 0 |
| rank3v0-melee-5 | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 29 ± 0 | 91 ± 0 | 120 ± 0 | 0 % ± 0 | 3.4 ± 0.0 | 1.3 ± 0.0 | 0 |
| rank3v0-archer-5 | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 5.0 ± 0.0 | 10 ± 0 | 50 ± 0 | 115 ± 0 | 100 % ± 0 | 26.8 ± 0.0 | 4.5 ± 0.0 | 0 |
| A | 1 | 100 % ± 0 | 0 % ± 0 | 19.0 ± 0.0 | 0.0 ± 0.0 | 15 ± 0 | 502 ± 0 | 77 ± 0 | 0 % ± 0 | 5.5 ± 0.0 | 7.6 ± 0.0 | 0 |
| C | 1 | 100 % ± 0 | 0 % ± 0 | 20.0 ± 0.0 | 0.0 ± 0.0 | 13 ± 0 | 463 ± 0 | 0 ± 0 | 76 % ± 0 | 10.7 ± 0.0 | 5.7 ± 0.0 | 0 |
| zombie-5 | 1 | 100 % ± 0 | 0 % ± 0 | 5.0 ± 0.0 | 0.0 ± 0.0 | 10 ± 0 | 120 ± 0 | 10 ± 0 | 0 % ± 0 | 2.8 ± 0.0 | 3.0 ± 0.0 | 0 |
| skeleton-5 | 1 | 100 % ± 0 | 0 % ± 0 | 5.0 ± 0.0 | 0.0 ± 0.0 | 8 ± 0 | 121 ± 0 | 13 ± 0 | 85 % ± 0 | 16.4 ± 0.0 | 3.5 ± 0.0 | 0 |
| mirror-mounted-5 | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 21 ± 0 | 101 ± 0 | 120 ± 0 | 0 % ± 0 | 4.1 ± 0.0 | 3.0 ± 0.0 | 0 |
| precedence-player-vs-force | 1 | 100 % ± 0 | 0 % ± 0 | 10.0 ± 0.0 | 0.0 ± 0.0 | 11 ± 0 | 96 ± 0 | 0 ± 0 | 0 % ± 0 | 11.4 ± 0.0 | 5.0 ± 0.0 | 0 |
| picture-live-cover | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 2.0 ± 0.0 | 57 ± 0 | 228 ± 0 | 250 ± 0 | 69 % ± 0 | 32.0 ± 0.0 | 8.0 ± 0.0 | 0 |
| rank5-core-live-5 | 1 | 100 % ± 0 | 0 % ± 0 | 5.0 ± 0.0 | 0.0 ± 0.0 | 9 ± 0 | 120 ± 0 | 0 ± 0 | 0 % ± 0 | 3.8 ± 0.0 | 3.2 ± 0.0 | 0 |
| no-pingpong-archer-8 | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 6.0 ± 0.0 | 16 ± 0 | 141 ± 0 | 199 ± 0 | 83 % ± 0 | 35.7 ± 0.0 | 5.8 ± 0.0 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- mirror-melee-2 semilla 1: sprint → red (0/2 vivos), normal → red (0/2 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 8 de 8 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- mirror-melee-2: enemy:mob=2
- rank3v0-melee-5: enemy:mob=5
- rank3v0-archer-5: enemy:arrow=5
- A: enemy:mob=1
- mirror-mounted-5: enemy:mob=5
- picture-live-cover: enemy:arrow=10
- no-pingpong-archer-8: enemy:arrow=8

## Motivos de decisión (cambios registrados): COVER_AVAILABLE 8, ENGAGE 234, LOW_HEALTH_RETREAT 122, MORALE_SHAKEN 65, MORALE_WAVERING 19, NONE 35, OUT_OF_RANGE 16, REFUSE_ATTACK 11, ROUTED_FLEE 3, SEEK_ENEMY 42, SHOOT_AND_MOVE 11, TARGET_PRIORITY 56, THREAT_MEMORY 4, TOO_CLOSE 4

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- mirror-melee-2: pico de Rally 25 ms en un tick (media de picos)
