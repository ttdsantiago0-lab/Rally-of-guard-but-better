# Simulación Rally - lote 20261108-195914-t25-vanilla-hordas-base

Preset `t25-vanilla-hordas-base` · velocidad `sprint` · 125 corridas · 12 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:magma_cube=67, minecraft:silverfish=4, minecraft:slime=8}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t25-vanilla-hordas-spider-base-r12 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 11 ± 0 | 229 ± 2 | 4 ± 1 | 85 % ± 2 | 7.4 ± 0.2 | 5.4 ± 0.2 | 0 |
| t25-vanilla-hordas-spider-base-r24 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 17 ± 0 | 449 ± 4 | 22 ± 1 | 91 % ± 1 | 5.6 ± 0.1 | 5.8 ± 0.2 | 0 |
| t25-vanilla-hordas-cave_spider-base-r12 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 9 ± 0 | 180 ± 2 | 2 ± 0 | 77 % ± 2 | 8.4 ± 0.3 | 5.4 ± 0.1 | 0 |
| t25-vanilla-hordas-cave_spider-base-r24 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 0 | 346 ± 2 | 19 ± 2 | 87 % ± 2 | 5.5 ± 0.2 | 5.2 ± 0.2 | 0 |
| t25-vanilla-hordas-husk-base-r24 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 21 ± 0 | 575 ± 2 | 45 ± 3 | 93 % ± 1 | 6.0 ± 0.5 | 8.3 ± 0.5 | 0 |
| t25-vanilla-hordas-husk-base-r40 | 10 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 35 ± 1 | 972 ± 4 | 138 ± 15 | 97 % ± 0 | 5.3 ± 0.2 | 9.8 ± 0.5 | 0 |
| t25-vanilla-hordas-silverfish-base-r16 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 9 ± 1 | 118 ± 9 | 0 ± 0 | 52 % ± 3 | 13.2 ± 0.7 | 6.7 ± 0.2 | 0 |
| t25-vanilla-hordas-silverfish-base-r32 | 8 | 94 % ± 6 | 6 % ± 6 | 7.9 ± 0.1 | 0.1 ± 0.1 | 35 ± 21 | 294 ± 11 | 15 ± 14 | 64 % ± 2 | 7.8 ± 0.7 | 5.7 ± 0.4 | 6 |
| t25-vanilla-hordas-slime-base-r8 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 18 ± 1 | 379 ± 37 | 0 ± 0 | 55 % ± 3 | 11.8 ± 0.2 | 8.6 ± 0.3 | 0 |
| t25-vanilla-hordas-slime-base-r16 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 2 | 646 ± 81 | 0 ± 0 | 67 % ± 2 | 11.6 ± 0.4 | 10.0 ± 0.3 | 0 |
| t25-vanilla-hordas-magma_cube-base-r8 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 2 | 218 ± 45 | 0 ± 0 | 62 % ± 4 | 13.7 ± 0.5 | 7.7 ± 0.2 | 0 |
| t25-vanilla-hordas-magma_cube-base-r16 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 27 ± 2 | 617 ± 54 | 0 ± 0 | 68 % ± 2 | 12.3 ± 0.3 | 10.6 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t25-vanilla-hordas-spider-base-r12 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 118 de 118 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t25-vanilla-hordas-silverfish-base-r32: enemy:mob=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 3, ENGAGE 1591, LOW_HEALTH_RETREAT 34, MORALE_SHAKEN 86, MORALE_WAVERING 37, NONE 12, OUT_OF_RANGE 1, RALLIED 1, REFUSE_ATTACK 8, ROUTED_FLEE 20, SEEK_ENEMY 216, STUCK 5, TOO_CLOSE 483

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t25-vanilla-hordas-silverfish-base-r32 - 6 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
