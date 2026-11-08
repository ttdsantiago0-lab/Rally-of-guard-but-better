# Simulación Rally - lote 20261108-195901-t24-techo-zombie-oracadencia

Preset `t24-techo-zombie-oracadencia` · velocidad `sprint` · 81 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=36}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t24-techo-zombie-arco-oracadencia-r60 | 20 | 95 % ± 5 | 5 % ± 5 | 6.4 ± 0.4 | 0.5 ± 0.5 | 37 ± 1 | 1508 ± 6 | 91 ± 7 | 95 % ± 0 | 7.3 ± 0.4 | 7.3 ± 0.4 | 0 |
| t24-techo-zombie-arco-oracadencia-r85 | 20 | 40 % ± 11 | 60 % ± 11 | 1.6 ± 0.5 | 19.9 ± 3.9 | 69 ± 4 | 1867 ± 64 | 170 ± 6 | 98 % ± 0 | 7.4 ± 0.6 | 11.2 ± 0.7 | 0 |
| t24-techo-zombie-linea-oracadencia-r55 | 20 | 70 % ± 11 | 30 % ± 11 | 3.4 ± 0.6 | 4.7 ± 1.9 | 54 ± 3 | 1351 ± 25 | 147 ± 8 | 97 % ± 0 | 6.4 ± 0.5 | 10.4 ± 0.6 | 0 |
| t24-techo-zombie-linea-oracadencia-r75 | 20 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 36.3 ± 2.1 | 59 ± 3 | 1292 ± 42 | 190 ± 1 | 99 % ± 0 | 4.5 ± 0.2 | 10.1 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t24-techo-zombie-arco-oracadencia-r60 semilla 1: sprint → blue (7/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 41 de 41 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t24-techo-zombie-arco-oracadencia-r60: enemy:mob=22
- t24-techo-zombie-arco-oracadencia-r85: enemy:mob=53
- t24-techo-zombie-linea-oracadencia-r55: enemy:mob=55
- t24-techo-zombie-linea-oracadencia-r75: enemy:mob=76

## Motivos de decisión (cambios registrados): COUNTERATTACK 7, ENGAGE 2190, LOW_HEALTH_RETREAT 931, MORALE_SHAKEN 391, MORALE_WAVERING 259, NONE 15, OUT_OF_RANGE 22, RALLIED 4, REFUSE_ATTACK 21, ROUTED_FLEE 113, SEEK_ENEMY 303, TOO_CLOSE 1840

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
