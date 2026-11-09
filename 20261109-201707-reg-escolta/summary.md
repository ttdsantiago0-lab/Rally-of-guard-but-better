# Simulación Rally - lote 20261109-201707-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 66 corridas · 8 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=1}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 8.1 ± 1.7 | 65 ± 0 | 762 ± 71 | 40 ± 12 | 92 % ± 1 | 17.4 ± 1.1 | 17.1 ± 0.5 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 63 % ± 8 | 38 % ± 8 | 8.0 ± 0.0 | 3.9 ± 1.0 | 45 ± 5 | 444 ± 60 | 9 ± 2 | 84 % ± 1 | 21.3 ± 0.6 | 8.5 ± 0.8 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 56 % ± 6 | 44 % ± 6 | 8.0 ± 0.0 | 2.9 ± 0.8 | 46 ± 4 | 553 ± 59 | 13 ± 3 | 88 % ± 1 | 18.9 ± 0.6 | 11.5 ± 1.0 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 7.1 ± 0.7 | 24 ± 2 | 87 ± 14 | 10 ± 3 | 0 % ± 0 | 14.1 ± 0.8 | 4.8 ± 0.5 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.3 ± 0.4 | 0.0 ± 0.0 | 103 ± 10 | 220 ± 10 | 66 ± 9 | 0 % ± 0 | 12.9 ± 0.8 | 11.1 ± 1.4 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 75 % ± 13 | 25 % ± 13 | 5.4 ± 0.9 | 1.5 ± 1.0 | 232 ± 32 | 679 ± 65 | 238 ± 36 | 0 % ± 0 | 14.1 ± 1.4 | 16.1 ± 2.0 | 1 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 28 ± 2 | 142 ± 1 | 9 ± 1 | 0 % ± 0 | 13.5 ± 1.2 | 11.3 ± 1.3 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 35 ± 2 | 237 ± 1 | 16 ± 2 | 0 % ± 0 | 10.6 ± 0.8 | 9.8 ± 0.9 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 1: sprint → draw (8/4 vivos), normal → draw (8/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 63 de 63 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t19-testudo-esqueletos-escudo-testudocarga: enemy:arrow=6
- t19-testudo-mezcla-escudo-testudocarga: enemy:arrow=2 enemy:explosion.player=3 enemy:mob=8 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.killed=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 44, ENGAGE 1041, FORMATION_HOLD 565, LOW_HEALTH_RETREAT 50, MORALE_SHAKEN 369, MORALE_WAVERING 94, NONE 230, OUT_OF_RANGE 1, RALLIED 7, REFUSE_ATTACK 4, ROUTED_FLEE 103, SEEK_ENEMY 545, STUCK 61, TOO_CLOSE 12

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t19-testudo-mezcla-escudo-testudocarga - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
