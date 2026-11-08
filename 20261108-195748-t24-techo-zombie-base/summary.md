# Simulación Rally - lote 20261108-195748-t24-techo-zombie-base

Preset `t24-techo-zombie-base` · velocidad `sprint` · 81 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=75}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t24-techo-zombie-arco-base-r60 | 20 | 30 % ± 11 | 70 % ± 11 | 1.4 ± 0.5 | 17.7 ± 3.4 | 62 ± 3 | 1293 ± 55 | 167 ± 7 | 98 % ± 1 | 6.6 ± 0.5 | 9.2 ± 0.7 | 0 |
| t24-techo-zombie-arco-base-r85 | 20 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 52.2 ± 3.0 | 61 ± 4 | 1216 ± 66 | 187 ± 2 | 98 % ± 0 | 5.5 ± 0.3 | 9.4 ± 0.5 | 0 |
| t24-techo-zombie-linea-base-r55 | 20 | 5 % ± 5 | 95 % ± 5 | 0.2 ± 0.2 | 23.6 ± 2.4 | 58 ± 3 | 1063 ± 43 | 186 ± 2 | 99 % ± 0 | 4.5 ± 0.2 | 10.6 ± 0.7 | 0 |
| t24-techo-zombie-linea-base-r75 | 20 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 49.1 ± 2.0 | 50 ± 3 | 1005 ± 43 | 186 ± 1 | 99 % ± 0 | 3.9 ± 0.1 | 9.4 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t24-techo-zombie-arco-base-r60 semilla 1: sprint → blue (2/0 vivos), normal → blue (2/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 7 de 7 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t24-techo-zombie-arco-base-r60: enemy:mob=69
- t24-techo-zombie-arco-base-r85: enemy:mob=83
- t24-techo-zombie-linea-base-r55: enemy:mob=79
- t24-techo-zombie-linea-base-r75: enemy:mob=93

## Motivos de decisión (cambios registrados): COUNTERATTACK 19, ENGAGE 2219, LOW_HEALTH_RETREAT 1020, MORALE_SHAKEN 652, MORALE_WAVERING 478, NONE 2, OUT_OF_RANGE 10, RALLIED 5, REFUSE_ATTACK 51, ROUTED_FLEE 232, SEEK_ENEMY 206, TOO_CLOSE 1975

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
