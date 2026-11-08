# Simulación Rally - lote 20261108-200019-t25-vanilla-raros-antes

Preset `t25-vanilla-raros-antes` · velocidad `sprint` · 101 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t25-vanilla-raros-witch-antes-r4 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 11 ± 1 | 129 ± 2 | 0 ± 0 | 91 % ± 1 | 19.0 ± 0.7 | 6.2 ± 0.3 | 0 |
| t25-vanilla-raros-witch-antes-r8 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 19 ± 1 | 285 ± 7 | 9 ± 2 | 93 % ± 1 | 13.0 ± 0.4 | 6.0 ± 0.1 | 0 |
| t25-vanilla-raros-vindicator-antes-r6 | 10 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.3 | 0.0 ± 0.0 | 12 ± 1 | 156 ± 1 | 79 ± 10 | 94 % ± 2 | 6.0 ± 0.3 | 4.4 ± 0.2 | 0 |
| t25-vanilla-raros-vindicator-antes-r12 | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 5.5 ± 0.9 | 17 ± 1 | 234 ± 10 | 255 ± 4 | 96 % ± 1 | 5.0 ± 0.2 | 4.5 ± 0.3 | 0 |
| t25-vanilla-raros-evoker-antes-r2 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 10 ± 0 | 97 ± 3 | 16 ± 4 | 80 % ± 3 | 10.6 ± 0.6 | 4.1 ± 0.2 | 0 |
| t25-vanilla-raros-evoker-antes-r4 | 10 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 16 ± 1 | 209 ± 6 | 55 ± 5 | 80 % ± 2 | 7.2 ± 0.4 | 5.1 ± 0.3 | 0 |
| t25-vanilla-raros-enderman-antes-r3 | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 39 ± 3 | 0 ± 0 | 219 ± 1 | 0 % ± 0 | 9.1 ± 0.9 | 7.5 ± 0.6 | 0 |
| t25-vanilla-raros-enderman-antes-r6 | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 6.0 ± 0.0 | 22 ± 1 | 0 ± 0 | 218 ± 1 | 0 % ± 0 | 5.9 ± 0.3 | 5.2 ± 0.4 | 0 |
| t25-vanilla-raros-wither_skeleton-antes-r6 | 10 | 100 % ± 0 | 0 % ± 0 | 7.1 ± 0.3 | 0.0 ± 0.0 | 15 ± 2 | 145 ± 1 | 33 ± 10 | 93 % ± 3 | 10.2 ± 0.7 | 6.1 ± 0.4 | 0 |
| t25-vanilla-raros-wither_skeleton-antes-r12 | 10 | 100 % ± 0 | 0 % ± 0 | 3.8 ± 0.4 | 0.0 ± 0.0 | 24 ± 1 | 281 ± 2 | 132 ± 12 | 92 % ± 2 | 7.0 ± 0.2 | 5.0 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t25-vanilla-raros-witch-antes-r4 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 70 de 70 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t25-vanilla-raros-vindicator-antes-r6: enemy:mob=15
- t25-vanilla-raros-vindicator-antes-r12: enemy:mob=80
- t25-vanilla-raros-evoker-antes-r4: enemy:indirectMagic=1
- t25-vanilla-raros-enderman-antes-r3: enemy:mob=80
- t25-vanilla-raros-enderman-antes-r6: enemy:mob=80
- t25-vanilla-raros-wither_skeleton-antes-r6: enemy:mob=9
- t25-vanilla-raros-wither_skeleton-antes-r12: enemy:mob=42

## Motivos de decisión (cambios registrados): ENGAGE 834, LOW_HEALTH_RETREAT 118, MORALE_SHAKEN 326, MORALE_WAVERING 16, NONE 16, OUT_OF_RANGE 1, REFUSE_ATTACK 3, ROUTED_FLEE 4, SEEK_ENEMY 258, STUCK 2, TOO_CLOSE 293, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
