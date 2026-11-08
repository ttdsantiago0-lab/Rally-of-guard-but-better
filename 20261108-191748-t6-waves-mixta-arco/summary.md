# Simulación Rally - lote 20261108-191748-t6-waves-mixta-arco

Preset `t6-waves-mixta-arco` · velocidad `sprint` · 41 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=14}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-mixta-arco-sincomida-base | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 277 ± 2 | 2393 ± 9 | 77 ± 5 | 77 % ± 1 | 14.2 ± 0.2 | 12.4 ± 0.6 | 0 |
| t6-waves-mixta-arco-sincomida-antes | 10 | 10 % ± 10 | 90 % ± 10 | 0.4 ± 0.4 | 6.7 ± 1.1 | 234 ± 20 | 954 ± 99 | 234 ± 9 | 85 % ± 1 | 15.1 ± 1.0 | 12.0 ± 0.7 | 0 |
| t6-waves-mixta-arco-comida-base | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 275 ± 1 | 2377 ± 11 | 77 ± 3 | 77 % ± 0 | 14.0 ± 0.1 | 11.8 ± 0.3 | 0 |
| t6-waves-mixta-arco-comida-antes | 10 | 15 % ± 11 | 85 % ± 11 | 0.5 ± 0.5 | 7.5 ± 1.8 | 278 ± 14 | 1097 ± 109 | 339 ± 15 | 85 % ± 1 | 13.5 ± 0.4 | 10.6 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-mixta-arco-sincomida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 22 de 22 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-mixta-arco-sincomida-antes: enemy:arrow=4 enemy:explosion.player=27 enemy:mob=21
- t6-waves-mixta-arco-comida-antes: enemy:explosion.player=31 enemy:mob=22

## Motivos de decisión (cambios registrados): COUNTERATTACK 12, ENGAGE 2902, LOW_HEALTH_RETREAT 122, MORALE_SHAKEN 190, MORALE_WAVERING 190, NONE 2128, OUT_OF_RANGE 73, RALLIED 20, REFUSE_ATTACK 48, ROUTED_FLEE 100, SEEK_ENEMY 436, TOO_CLOSE 1063

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
