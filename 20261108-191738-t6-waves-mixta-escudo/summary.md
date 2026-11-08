# Simulación Rally - lote 20261108-191738-t6-waves-mixta-escudo

Preset `t6-waves-mixta-escudo` · velocidad `sprint` · 47 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=6}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-mixta-escudo-sincomida-base | 10 | 85 % ± 11 | 15 % ± 11 | 6.3 ± 1.0 | 1.5 ± 1.0 | 538 ± 24 | 1573 ± 47 | 187 ± 26 | 0 % ± 0 | 15.9 ± 0.7 | 16.0 ± 1.3 | 1 |
| t6-waves-mixta-escudo-sincomida-antes | 10 | 10 % ± 10 | 90 % ± 10 | 0.2 ± 0.2 | 7.7 ± 1.2 | 379 ± 33 | 1099 ± 76 | 282 ± 10 | 0 % ± 0 | 12.3 ± 0.9 | 11.2 ± 0.8 | 0 |
| t6-waves-mixta-escudo-comida-base | 10 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 506 ± 15 | 1640 ± 13 | 149 ± 9 | 0 % ± 0 | 15.4 ± 0.9 | 14.5 ± 0.8 | 4 |
| t6-waves-mixta-escudo-comida-antes | 10 | 95 % ± 5 | 5 % ± 5 | 3.7 ± 0.4 | 1.0 ± 1.0 | 489 ± 27 | 1502 ± 27 | 325 ± 27 | 0 % ± 0 | 14.3 ± 1.0 | 14.3 ± 1.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-mixta-escudo-sincomida-base semilla 1: sprint → draw (1/8 vivos), normal → draw (8/1 vivos)
- t6-waves-mixta-escudo-sincomida-base semilla 7778: sprint → ? (0/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 30 de 30 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-mixta-escudo-sincomida-base: enemy:arrow=1 enemy:explosion.player=2 enemy:mob=5
- t6-waves-mixta-escudo-sincomida-antes: enemy:arrow=16 enemy:explosion.player=44 enemy:mob=11
- t6-waves-mixta-escudo-comida-base: enemy:explosion.player=1
- t6-waves-mixta-escudo-comida-antes: enemy:explosion.player=42

## Motivos de decisión (cambios registrados): COUNTERATTACK 10, ENGAGE 4038, LOW_HEALTH_RETREAT 289, MORALE_SHAKEN 979, MORALE_WAVERING 192, NONE 2926, RALLIED 30, REFUSE_ATTACK 72, ROUTED_FLEE 95, SEEK_ENEMY 1875, STUCK 40, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t6-waves-mixta-escudo-comida-base - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t6-waves-mixta-escudo-sincomida-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
