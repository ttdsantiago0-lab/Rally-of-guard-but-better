# Simulación Rally - lote 20261104-184503-t2-duel-zombie

Preset `t2-duel-zombie` · velocidad `sprint` · 361 corridas · 12 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=233}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t2-duel-zombie-espada-r1 | 30 | 60 % ± 9 | 40 % ± 9 | 0.6 ± 0.1 | 0.5 ± 0.1 | 9 ± 1 | 14 ± 2 | 9 ± 2 | 0 % ± 0 | 2.6 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-espada-r2 | 30 | 60 % ± 9 | 40 % ± 9 | 0.6 ± 0.1 | 0.8 ± 0.2 | 13 ± 1 | 30 ± 4 | 16 ± 2 | 0 % ± 0 | 2.2 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-espada-r3 | 30 | 3 % ± 3 | 97 % ± 3 | 0.0 ± 0.0 | 2.8 ± 0.1 | 15 ± 1 | 25 ± 4 | 23 ± 1 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-espada-r4 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 4.0 ± 0.0 | 12 ± 0 | 22 ± 3 | 23 ± 1 | 0 % ± 0 | 2.0 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-pesado-r1 | 30 | 50 % ± 9 | 50 % ± 9 | 0.5 ± 0.1 | 0.5 ± 0.1 | 13 ± 2 | 12 ± 2 | 11 ± 2 | 0 % ± 0 | 2.6 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-pesado-r2 | 30 | 57 % ± 9 | 43 % ± 9 | 0.6 ± 0.1 | 0.9 ± 0.2 | 15 ± 1 | 27 ± 4 | 14 ± 1 | 0 % ± 0 | 2.1 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-pesado-r3 | 30 | 30 % ± 9 | 70 % ± 9 | 0.3 ± 0.1 | 1.9 ± 0.2 | 21 ± 1 | 36 ± 6 | 22 ± 2 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-pesado-r4 | 30 | 3 % ± 3 | 97 % ± 3 | 0.0 ± 0.0 | 3.6 ± 0.2 | 19 ± 1 | 23 ± 6 | 23 ± 1 | 0 % ± 0 | 1.9 ± 0.0 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-escudo-r1 | 30 | 40 % ± 9 | 60 % ± 9 | 0.4 ± 0.1 | 0.6 ± 0.1 | 17 ± 2 | 9 ± 2 | 14 ± 2 | 0 % ± 0 | 2.6 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-escudo-r2 | 30 | 73 % ± 8 | 27 % ± 8 | 0.7 ± 0.1 | 0.5 ± 0.2 | 13 ± 1 | 35 ± 4 | 12 ± 2 | 0 % ± 0 | 2.3 ± 0.0 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-escudo-r3 | 30 | 13 % ± 6 | 87 % ± 6 | 0.1 ± 0.1 | 2.4 ± 0.2 | 19 ± 1 | 24 ± 5 | 22 ± 1 | 0 % ± 0 | 2.1 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-escudo-r4 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.8 ± 0.1 | 20 ± 1 | 21 ± 5 | 24 ± 1 | 0 % ± 0 | 1.9 ± 0.0 | 0.0 ± 0.0 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t2-duel-zombie-espada-r1 semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 117 de 117 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t2-duel-zombie-espada-r1: enemy:mob=5
- t2-duel-zombie-espada-r2: enemy:mob=6
- t2-duel-zombie-espada-r3: enemy:mob=16
- t2-duel-zombie-espada-r4: enemy:mob=16
- t2-duel-zombie-pesado-r1: enemy:mob=10
- t2-duel-zombie-pesado-r2: enemy:mob=8
- t2-duel-zombie-pesado-r3: enemy:mob=11
- t2-duel-zombie-pesado-r4: enemy:mob=13
- t2-duel-zombie-escudo-r1: enemy:mob=10
- t2-duel-zombie-escudo-r2: enemy:mob=7
- t2-duel-zombie-escudo-r3: enemy:mob=12
- t2-duel-zombie-escudo-r4: enemy:mob=17

## Motivos de decisión (cambios registrados): COUNTERATTACK 151, ENGAGE 360, LOW_HEALTH_RETREAT 91, MORALE_SHAKEN 315, MORALE_WAVERING 247, NONE 316, REFUSE_ATTACK 64, ROUTED_FLEE 263, SEEK_ENEMY 6

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
