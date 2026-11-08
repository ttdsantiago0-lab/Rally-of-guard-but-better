# Simulación Rally - lote 20261108-191623-t6-waves-mixta-linea

Preset `t6-waves-mixta-linea` · velocidad `sprint` · 41 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=14}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-mixta-linea-sincomida-base | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 296 ± 3 | 2156 ± 28 | 79 ± 4 | 80 % ± 1 | 12.6 ± 0.5 | 12.6 ± 0.4 | 0 |
| t6-waves-mixta-linea-sincomida-antes | 10 | 5 % ± 5 | 95 % ± 5 | 0.0 ± 0.0 | 6.5 ± 1.4 | 265 ± 20 | 1023 ± 59 | 257 ± 10 | 87 % ± 1 | 11.7 ± 1.0 | 9.9 ± 0.5 | 0 |
| t6-waves-mixta-linea-comida-base | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 295 ± 3 | 2143 ± 28 | 82 ± 5 | 79 % ± 1 | 12.1 ± 0.3 | 12.2 ± 0.3 | 0 |
| t6-waves-mixta-linea-comida-antes | 10 | 20 % ± 13 | 80 % ± 13 | 0.7 ± 0.5 | 6.3 ± 1.2 | 402 ± 20 | 1431 ± 53 | 408 ± 29 | 89 % ± 1 | 12.2 ± 0.5 | 10.6 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-mixta-linea-sincomida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 22 de 22 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-mixta-linea-sincomida-antes: enemy:arrow=7 enemy:explosion.player=30 enemy:mob=18
- t6-waves-mixta-linea-comida-antes: enemy:arrow=3 enemy:explosion.player=49 enemy:mob=12

## Motivos de decisión (cambios registrados): COUNTERATTACK 22, ENGAGE 3070, LOW_HEALTH_RETREAT 172, MORALE_SHAKEN 278, MORALE_WAVERING 154, NONE 2324, OUT_OF_RANGE 35, RALLIED 13, REFUSE_ATTACK 34, ROUTED_FLEE 81, SEEK_ENEMY 752, TOO_CLOSE 574

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
