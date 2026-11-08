# Simulación Rally - lote 20261108-195844-t25-vanilla-tiradores-antes

Preset `t25-vanilla-tiradores-antes` · velocidad `sprint` · 101 corridas · 10 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=2}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t25-vanilla-tiradores-stray-antes-r16 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 21 ± 1 | 334 ± 4 | 49 ± 3 | 91 % ± 2 | 10.6 ± 0.3 | 6.5 ± 0.2 | 0 |
| t25-vanilla-tiradores-stray-antes-r32 | 10 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 33 ± 1 | 523 ± 9 | 89 ± 4 | 89 % ± 2 | 9.9 ± 0.3 | 8.1 ± 0.6 | 0 |
| t25-vanilla-tiradores-bogged-antes-r16 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 17 ± 1 | 286 ± 3 | 30 ± 1 | 87 % ± 1 | 11.6 ± 0.2 | 6.2 ± 0.2 | 0 |
| t25-vanilla-tiradores-bogged-antes-r32 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 30 ± 2 | 478 ± 4 | 60 ± 5 | 91 % ± 1 | 10.3 ± 0.4 | 8.7 ± 0.8 | 0 |
| t25-vanilla-tiradores-pillager-antes-r8 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 1 | 207 ± 2 | 12 ± 1 | 94 % ± 1 | 6.8 ± 0.2 | 5.1 ± 0.3 | 0 |
| t25-vanilla-tiradores-pillager-antes-r16 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 1 | 382 ± 6 | 54 ± 3 | 94 % ± 2 | 6.5 ± 0.3 | 6.7 ± 0.4 | 0 |
| t25-vanilla-tiradores-drowned-antes-r12 | 10 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.2 | 0.0 ± 0.0 | 31 ± 3 | 296 ± 4 | 43 ± 10 | 93 % ± 1 | 8.8 ± 0.6 | 6.5 ± 0.6 | 0 |
| t25-vanilla-tiradores-drowned-antes-r24 | 10 | 90 % ± 10 | 10 % ± 10 | 5.2 ± 0.7 | 0.4 ± 0.4 | 71 ± 12 | 572 ± 11 | 201 ± 23 | 96 % ± 2 | 10.4 ± 1.7 | 12.3 ± 1.0 | 0 |
| t25-vanilla-tiradores-breeze-antes-r2 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 16 ± 1 | 60 ± 0 | 0 ± 0 | 88 % ± 3 | 13.2 ± 0.4 | 7.3 ± 0.2 | 0 |
| t25-vanilla-tiradores-breeze-antes-r4 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 22 ± 1 | 120 ± 0 | 0 ± 0 | 87 % ± 2 | 12.3 ± 0.4 | 8.5 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t25-vanilla-tiradores-stray-antes-r16 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 99 de 99 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t25-vanilla-tiradores-stray-antes-r32: enemy:arrow=1
- t25-vanilla-tiradores-drowned-antes-r12: enemy:mob=5
- t25-vanilla-tiradores-drowned-antes-r24: enemy:mob=14 enemy:trident=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 10, ENGAGE 1134, LOW_HEALTH_RETREAT 146, MORALE_SHAKEN 124, MORALE_WAVERING 114, NONE 5, OUT_OF_RANGE 7, RALLIED 3, REFUSE_ATTACK 11, ROUTED_FLEE 49, SEEK_ENEMY 72, TOO_CLOSE 342

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
