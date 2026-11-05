# Simulación Rally - lote 20261105-142323-t15-vet22

Preset `t15-vet22` · velocidad `sprint` · 69 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=10}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t15-vet22-linea-r3 | 12 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 393 ± 4 | 4217 ± 36 | 276 ± 17 | 85 % ± 0 | 12.6 ± 0.3 | 12.7 ± 0.2 | 1 |
| t15-vet22-linea-r5 | 11 | 95 % ± 5 | 5 % ± 5 | 7.4 ± 0.3 | 2.0 ± 2.0 | 475 ± 29 | 3867 ± 153 | 337 ± 25 | 83 % ± 1 | 20.2 ± 2.3 | 18.9 ± 2.0 | 4 |
| t15-vet22-linea-r0 | 5 | 10 % ± 10 | 90 % ± 10 | 0.6 ± 0.6 | 13.6 ± 2.5 | 482 ± 65 | 1849 ± 192 | 539 ± 13 | 88 % ± 1 | 24.8 ± 3.0 | 21.0 ± 3.9 | 16 |
| t15-vet22-linea-r1 | 9 | 39 % ± 16 | 61 % ± 16 | 2.4 ± 1.1 | 6.9 ± 2.5 | 497 ± 46 | 3297 ± 298 | 479 ± 43 | 87 % ± 1 | 19.7 ± 1.7 | 16.5 ± 1.1 | 10 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t15-vet22-linea-r0 semilla 1: sprint → draw (2/18 vivos), normal → red (0/9 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 28 de 28 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t15-vet22-linea-r3: enemy:mob=2
- t15-vet22-linea-r5: enemy:arrow=2 enemy:explosion.player=2 enemy:mob=2
- t15-vet22-linea-r0: enemy:arrow=2 enemy:explosion.player=10 enemy:mob=14
- t15-vet22-linea-r1: enemy:arrow=4 enemy:explosion.player=9 enemy:mob=16

## Motivos de decisión (cambios registrados): COUNTERATTACK 428, COVER_AVAILABLE 3, ENGAGE 10713, FOCUS_FIRE 1483, FORMATION_HOLD 43, LOW_HEALTH_RETREAT 413, MORALE_SHAKEN 2454, MORALE_WAVERING 1152, NONE 3243, OUTNUMBERED 984, OUT_OF_RANGE 992, PAIR_BOUND 651, RALLIED 148, REFUSE_ATTACK 63, ROUTED_FLEE 995, SEEK_ENEMY 1412, SHOOT_AND_MOVE 588, STUCK 95, TARGET_PRIORITY 4274, THREAT_MEMORY 124, TOO_CLOSE 957

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t15-vet22-linea-r0 - 16 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t15-vet22-linea-r1 - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t15-vet22-linea-r3 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t15-vet22-linea-r5 - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
