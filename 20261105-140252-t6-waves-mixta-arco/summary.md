# Simulación Rally - lote 20261105-140252-t6-waves-mixta-arco

Preset `t6-waves-mixta-arco` · velocidad `sprint` · 41 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=16}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-mixta-arco-sincomida-base | 10 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 279 ± 2 | 2396 ± 12 | 76 ± 3 | 77 % ± 1 | 14.7 ± 0.3 | 11.9 ± 0.3 | 0 |
| t6-waves-mixta-arco-sincomida-antes | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 8.8 ± 0.7 | 182 ± 15 | 723 ± 44 | 218 ± 5 | 84 % ± 1 | 12.9 ± 0.9 | 10.7 ± 0.9 | 0 |
| t6-waves-mixta-arco-comida-base | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 275 ± 2 | 2416 ± 7 | 73 ± 3 | 79 % ± 1 | 13.8 ± 0.2 | 11.7 ± 0.3 | 0 |
| t6-waves-mixta-arco-comida-antes | 10 | 5 % ± 5 | 95 % ± 5 | 0.0 ± 0.0 | 8.6 ± 1.2 | 266 ± 19 | 1001 ± 73 | 326 ± 14 | 84 % ± 1 | 13.9 ± 0.7 | 9.4 ± 0.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-mixta-arco-sincomida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 20 de 20 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-mixta-arco-sincomida-base: enemy:arrow=1
- t6-waves-mixta-arco-sincomida-antes: enemy:arrow=6 enemy:explosion.player=22 enemy:mob=23
- t6-waves-mixta-arco-comida-antes: enemy:arrow=1 enemy:explosion.player=34 enemy:mob=20

## Motivos de decisión (cambios registrados): COUNTERATTACK 8, ENGAGE 2823, LOW_HEALTH_RETREAT 85, MORALE_SHAKEN 226, MORALE_WAVERING 177, NONE 2029, OUT_OF_RANGE 65, RALLIED 15, REFUSE_ATTACK 67, ROUTED_FLEE 106, SEEK_ENEMY 400, TOO_CLOSE 923, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
