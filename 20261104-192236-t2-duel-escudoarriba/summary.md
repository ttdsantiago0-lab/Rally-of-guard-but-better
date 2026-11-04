# Simulación Rally - lote 20261104-192236-t2-duel-escudoarriba

Preset `t2-duel-escudoarriba` · velocidad `sprint` · 273 corridas · 9 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=18}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t2-duel-zombie-escudoarriba-r1 | 30 | 100 % ± 0 | 0 % ± 0 | 1.0 ± 0.0 | 0.0 ± 0.0 | 7 ± 0 | 24 ± 0 | 1 ± 0 | 0 % ± 0 | 3.1 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-escudoarriba-r2 | 30 | 100 % ± 0 | 0 % ± 0 | 1.0 ± 0.0 | 0.0 ± 0.0 | 11 ± 0 | 47 ± 0 | 4 ± 0 | 0 % ± 0 | 2.1 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-escudoarriba-r3 | 30 | 97 % ± 3 | 3 % ± 3 | 1.0 ± 0.0 | 0.1 ± 0.1 | 15 ± 0 | 70 ± 1 | 10 ± 1 | 0 % ± 0 | 1.8 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-zombie-escudoarriba-r4 | 30 | 30 % ± 9 | 70 % ± 9 | 0.3 ± 0.1 | 1.4 ± 0.2 | 20 ± 1 | 82 ± 2 | 20 ± 1 | 0 % ± 0 | 1.5 ± 0.1 | 0.0 ± 0.0 | 1 |
| t2-duel-skeleton-escudoarriba-r1 | 30 | 98 % ± 2 | 2 % ± 2 | 1.0 ± 0.0 | 0.0 ± 0.0 | 19 ± 4 | 23 ± 0 | 2 ± 0 | 0 % ± 0 | 4.5 ± 0.2 | 0.0 ± 0.0 | 0 |
| t2-duel-skeleton-escudoarriba-r2 | 30 | 72 % ± 7 | 28 % ± 7 | 0.9 ± 0.1 | 0.5 ± 0.1 | 61 ± 8 | 36 ± 2 | 14 ± 2 | 0 % ± 0 | 5.5 ± 0.4 | 0.0 ± 0.0 | 0 |
| t2-duel-skeleton-escudoarriba-r3 | 30 | 27 % ± 5 | 73 % ± 5 | 0.5 ± 0.1 | 1.7 ± 0.2 | 94 ± 5 | 32 ± 3 | 25 ± 1 | 0 % ± 0 | 7.0 ± 0.2 | 0.0 ± 0.0 | 1 |
| t2-duel-creeper-escudoarriba-r1 | 30 | 100 % ± 0 | 0 % ± 0 | 1.0 ± 0.0 | 0.0 ± 0.0 | 6 ± 0 | 11 ± 1 | 6 ± 1 | 0 % ± 0 | 3.2 ± 0.1 | 0.0 ± 0.0 | 0 |
| t2-duel-creeper-escudoarriba-r2 | 30 | 85 % ± 6 | 15 % ± 6 | 0.8 ± 0.1 | 0.1 ± 0.1 | 6 ± 0 | 12 ± 0 | 15 ± 1 | 0 % ± 0 | 2.4 ± 0.1 | 0.0 ± 0.0 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t2-duel-zombie-escudoarriba-r1 semilla 1: sprint → blue (1/0 vivos), normal → blue (1/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 223 de 223 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t2-duel-zombie-escudoarriba-r3: enemy:mob=1
- t2-duel-zombie-escudoarriba-r4: enemy:mob=11
- t2-duel-skeleton-escudoarriba-r2: enemy:arrow=4
- t2-duel-skeleton-escudoarriba-r3: enemy:arrow=15
- t2-duel-creeper-escudoarriba-r2: enemy:explosion.player=6

## Motivos de decisión (cambios registrados): COUNTERATTACK 20, ENGAGE 475, LOW_HEALTH_RETREAT 17, MORALE_SHAKEN 156, MORALE_WAVERING 116, REFUSE_ATTACK 108, ROUTED_FLEE 101, SEEK_ENEMY 1, STUCK 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t2-duel-zombie-escudoarriba-r4 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t2-duel-skeleton-escudoarriba-r3 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
