# Simulación Rally - lote 20261104-103645-coord

Preset `coord` · velocidad `sprint` · 91 corridas · 9 escenarios.
> Precedencia: 0/5 con orden del jugador obedecieron la directiva de fuerza (debe ser 0); 5/5 del resto la recibieron
> Precedencia: 0/5 con orden del jugador obedecieron la directiva de fuerza (debe ser 0); 5/5 del resto la recibieron
> Precedencia: 0/5 con orden del jugador obedecieron la directiva de fuerza (debe ser 0); 5/5 del resto la recibieron
> Precedencia: 0/5 con orden del jugador obedecieron la directiva de fuerza (debe ser 0); 5/5 del resto la recibieron
> Precedencia: 0/5 con orden del jugador obedecieron la directiva de fuerza (debe ser 0); 5/5 del resto la recibieron
> Precedencia: 0/5 con orden del jugador obedecieron la directiva de fuerza (debe ser 0); 5/5 del resto la recibieron
> Precedencia: 0/5 con orden del jugador obedecieron la directiva de fuerza (debe ser 0); 5/5 del resto la recibieron
> Precedencia: 0/5 con orden del jugador obedecieron la directiva de fuerza (debe ser 0); 5/5 del resto la recibieron
> Precedencia: 0/5 con orden del jugador obedecieron la directiva de fuerza (debe ser 0); 5/5 del resto la recibieron
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=39}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| planner2-fill | 10 | 70 % ± 15 | 30 % ± 15 | 7.6 ± 1.7 | 3.5 ± 2.0 | 37 ± 7 | 561 ± 18 | 182 ± 42 | 93 % ± 3 | 8.3 ± 0.3 | 7.6 ± 0.5 | 0 |
| planner2-fill-base | 10 | 50 % ± 17 | 50 % ± 17 | 5.3 ± 1.8 | 6.5 ± 2.3 | 48 ± 8 | 555 ± 18 | 225 ± 34 | 91 % ± 3 | 7.8 ± 0.5 | 7.3 ± 0.3 | 0 |
| planner2-ambush | 10 | 60 % ± 16 | 40 % ± 16 | 6.2 ± 1.7 | 4.5 ± 2.0 | 44 ± 7 | 393 ± 16 | 171 ± 35 | 77 % ± 9 | 8.4 ± 0.5 | 7.0 ± 0.4 | 0 |
| planner2-ambush-base | 10 | 90 % ± 10 | 10 % ± 10 | 9.6 ± 1.1 | 1.3 ± 1.3 | 28 ± 2 | 376 ± 6 | 92 ± 22 | 91 % ± 3 | 8.6 ± 0.5 | 6.9 ± 0.3 | 0 |
| planner2-delay | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 22.8 ± 1.0 | 39 ± 2 | 282 ± 19 | 295 ± 5 | 93 % ± 2 | 6.9 ± 0.1 | 9.2 ± 0.6 | 0 |
| planner2-delay-base | 10 | 40 % ± 16 | 60 % ± 16 | 4.2 ± 1.7 | 10.3 ± 3.1 | 38 ± 4 | 395 ± 29 | 215 ± 33 | 92 % ± 3 | 7.4 ± 0.6 | 6.0 ± 0.4 | 0 |
| scenario-defensa | 10 | 50 % ± 17 | 50 % ± 17 | 4.8 ± 1.6 | 6.8 ± 2.4 | 37 ± 4 | 425 ± 19 | 204 ± 29 | 80 % ± 4 | 7.2 ± 0.4 | 6.5 ± 0.4 | 0 |
| scenario-defensa-noplan | 10 | 80 % ± 13 | 20 % ± 13 | 8.5 ± 1.4 | 3.0 ± 2.0 | 31 ± 3 | 424 ± 10 | 118 ± 29 | 95 % ± 2 | 8.3 ± 0.4 | 8.0 ± 0.5 | 0 |
| precedence-player-vs-force | 10 | 55 % ± 5 | 45 % ± 5 | 10.0 ± 0.0 | 2.9 ± 0.4 | 12 ± 0 | 49 ± 7 | 13 ± 3 | 0 % ± 0 | 22.3 ± 1.0 | 11.5 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- planner2-fill semilla 1: sprint → red (0/18 vivos), normal → red (0/18 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓
- precedence-player-vs-force: precedencia correcta en 100 % ± 0 de las corridas

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 54 de 54 corridas ✓

## Vínculos del plan (segundos ejecutados / saltados y por qué)
- planner2-ambush: linkSkipped:AMBUSH:missing_cap=80s linkStep:AMBUSH=156s
- planner2-delay: linkStep:DELAY=190s

## Causas de muerte del bando azul (causa:tipo=bajas)
- planner2-fill: enemy:mob=26 env:genericKill=53
- planner2-fill-base: enemy:mob=34 env:genericKill=56
- planner2-ambush: enemy:mob=37
- planner2-ambush-base: enemy:mob=10
- planner2-delay: enemy:mob=60
- planner2-delay-base: enemy:mob=39
- scenario-defensa: enemy:mob=33
- scenario-defensa-noplan: enemy:mob=14

## Motivos de decisión (cambios registrados): COUNTERATTACK 7, ENGAGE 1554, FORMATION_HOLD 325, LOW_HEALTH_RETREAT 309, MORALE_SHAKEN 795, MORALE_WAVERING 449, NONE 44, ORDER_OVERRIDE 887, OUTNUMBERED 24, REFUSE_ATTACK 125, RELAXED 228, ROUTED_FLEE 283, SEEK_ENEMY 423, TOO_CLOSE 508

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: planner2-fill - 67 % de las bajas azules ({enemy:mob=26.0, env:genericKill=53.0})
- MUERTES QUE NO SON DEL ENEMIGO: planner2-fill-base - 62 % de las bajas azules ({enemy:mob=34.0, env:genericKill=56.0})
