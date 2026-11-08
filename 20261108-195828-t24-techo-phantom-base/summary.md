# Simulación Rally - lote 20261108-195828-t24-techo-phantom-base

Preset `t24-techo-phantom-base` · velocidad `sprint` · 105 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t24-techo-phantom-arco-base-r20 | 18 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.1 | 0.0 ± 0.0 | 48 ± 2 | 486 ± 2 | 37 ± 1 | 46 % ± 2 | 18.5 ± 1.8 | 5.6 ± 0.3 | 11 |
| t24-techo-phantom-arco-base-r35 | 20 | 100 % ± 0 | 0 % ± 0 | 6.1 ± 0.2 | 0.0 ± 0.0 | 65 ± 2 | 843 ± 3 | 88 ± 2 | 57 % ± 1 | 14.8 ± 1.1 | 7.3 ± 0.4 | 2 |
| t24-techo-phantom-linea-base-r20 | 19 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.1 | 0.0 ± 0.0 | 48 ± 2 | 476 ± 2 | 36 ± 1 | 56 % ± 2 | 15.0 ± 1.2 | 10.1 ± 1.0 | 6 |
| t24-techo-phantom-linea-base-r35 | 15 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.2 | 0.0 ± 0.0 | 72 ± 2 | 823 ± 2 | 87 ± 3 | 57 % ± 2 | 11.5 ± 0.9 | 10.6 ± 0.8 | 13 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t24-techo-phantom-arco-base-r20 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 72 de 72 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t24-techo-phantom-arco-base-r20: enemy:mob=6
- t24-techo-phantom-arco-base-r35: enemy:mob=39
- t24-techo-phantom-linea-base-r20: enemy:mob=12
- t24-techo-phantom-linea-base-r35: enemy:mob=23

## Motivos de decisión (cambios registrados): COUNTERATTACK 13, ENGAGE 3708, LOW_HEALTH_RETREAT 413, MORALE_SHAKEN 488, MORALE_WAVERING 222, NONE 92, OUT_OF_RANGE 675, RALLIED 26, REFUSE_ATTACK 103, ROUTED_FLEE 87, SEEK_ENEMY 367, STUCK 165, TOO_CLOSE 2016

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t24-techo-phantom-arco-base-r35 - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t24-techo-phantom-linea-base-r20 - 6 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t24-techo-phantom-arco-base-r20 - 11 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t24-techo-phantom-linea-base-r35 - 13 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
