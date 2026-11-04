# Simulación Rally - lote 20261104-185811-t2-duel-zombie

Preset `t2-duel-zombie` · velocidad `sprint` · 361 corridas · 12 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=139}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t2-duel-zombie-espada-r1 | 30 | 100 % ± 0 | 0 % ± 0 | 1.0 ± 0.0 | 0.0 ± 0.0 | 5 ± 0 | 24 ± 0 | 0 ± 0 | 0 % ± 0 | 2.9 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-espada-r2 | 30 | 83 % ± 7 | 17 % ± 7 | 0.8 ± 0.1 | 0.3 ± 0.1 | 11 ± 0 | 45 ± 1 | 13 ± 1 | 0 % ± 0 | 2.3 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-espada-r3 | 30 | 13 % ± 6 | 87 % ± 6 | 0.1 ± 0.1 | 2.2 ± 0.2 | 14 ± 0 | 43 ± 3 | 22 ± 0 | 0 % ± 0 | 2.3 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-espada-r4 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 4.0 ± 0.1 | 12 ± 0 | 35 ± 2 | 22 ± 0 | 0 % ± 0 | 2.1 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-pesado-r1 | 30 | 100 % ± 0 | 0 % ± 0 | 1.0 ± 0.0 | 0.0 ± 0.0 | 5 ± 0 | 24 ± 0 | 0 ± 0 | 0 % ± 0 | 2.9 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-pesado-r2 | 30 | 100 % ± 0 | 0 % ± 0 | 1.0 ± 0.0 | 0.0 ± 0.0 | 10 ± 0 | 47 ± 0 | 8 ± 1 | 0 % ± 0 | 2.3 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-pesado-r3 | 30 | 57 % ± 9 | 43 % ± 9 | 0.6 ± 0.1 | 1.0 ± 0.2 | 17 ± 1 | 62 ± 2 | 18 ± 1 | 0 % ± 0 | 2.2 ± 0.0 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-pesado-r4 | 30 | 3 % ± 3 | 97 % ± 3 | 0.0 ± 0.0 | 3.6 ± 0.2 | 18 ± 1 | 49 ± 3 | 22 ± 0 | 0 % ± 0 | 1.9 ± 0.0 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-escudo-r1 | 30 | 100 % ± 0 | 0 % ± 0 | 1.0 ± 0.0 | 0.0 ± 0.0 | 5 ± 0 | 24 ± 0 | 0 ± 0 | 0 % ± 0 | 3.0 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-escudo-r2 | 30 | 100 % ± 0 | 0 % ± 0 | 1.0 ± 0.0 | 0.0 ± 0.0 | 10 ± 0 | 47 ± 0 | 7 ± 1 | 0 % ± 0 | 2.3 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-escudo-r3 | 30 | 50 % ± 9 | 50 % ± 9 | 0.5 ± 0.1 | 1.1 ± 0.2 | 19 ± 1 | 59 ± 3 | 18 ± 1 | 0 % ± 0 | 2.1 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-escudo-r4 | 30 | 7 % ± 5 | 93 % ± 5 | 0.1 ± 0.0 | 3.3 ± 0.2 | 19 ± 0 | 54 ± 3 | 22 ± 0 | 0 % ± 0 | 1.9 ± 0.0 | 0.0 ± 0.0 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t2-duel-zombie-espada-r1 semilla 1: sprint → blue (1/0 vivos), normal → blue (1/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 214 de 214 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t2-duel-zombie-espada-r2: enemy:mob=2
- t2-duel-zombie-espada-r3: enemy:mob=9
- t2-duel-zombie-espada-r4: enemy:mob=18
- t2-duel-zombie-pesado-r3: enemy:mob=4
- t2-duel-zombie-pesado-r4: enemy:mob=13
- t2-duel-zombie-escudo-r3: enemy:mob=5
- t2-duel-zombie-escudo-r4: enemy:mob=17

## Motivos de decisión (cambios registrados): COUNTERATTACK 32, ENGAGE 674, LOW_HEALTH_RETREAT 185, MORALE_SHAKEN 270, MORALE_WAVERING 200, REFUSE_ATTACK 143, ROUTED_FLEE 178, SEEK_ENEMY 14

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
