# Simulación Rally - lote 20261108-195802-t24-techo-phantom-oracadencia

Preset `t24-techo-phantom-oracadencia` · velocidad `sprint` · 101 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t24-techo-phantom-arco-oracadencia-r20 | 19 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 41 ± 2 | 481 ± 3 | 30 ± 1 | 43 % ± 2 | 19.7 ± 1.5 | 5.4 ± 0.3 | 7 |
| t24-techo-phantom-arco-oracadencia-r35 | 19 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.1 | 0.0 ± 0.0 | 58 ± 3 | 846 ± 3 | 73 ± 3 | 51 % ± 1 | 15.7 ± 1.2 | 6.4 ± 0.4 | 2 |
| t24-techo-phantom-linea-oracadencia-r20 | 18 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.1 | 0.0 ± 0.0 | 46 ± 2 | 477 ± 3 | 33 ± 1 | 51 % ± 2 | 17.4 ± 1.4 | 9.4 ± 1.2 | 7 |
| t24-techo-phantom-linea-oracadencia-r35 | 18 | 100 % ± 0 | 0 % ± 0 | 6.6 ± 0.1 | 0.0 ± 0.0 | 66 ± 3 | 826 ± 3 | 76 ± 2 | 57 % ± 2 | 13.8 ± 1.1 | 10.7 ± 0.7 | 10 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t24-techo-phantom-arco-oracadencia-r20 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 74 de 74 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t24-techo-phantom-arco-oracadencia-r20: enemy:mob=2
- t24-techo-phantom-arco-oracadencia-r35: enemy:mob=29
- t24-techo-phantom-linea-oracadencia-r20: enemy:mob=8
- t24-techo-phantom-linea-oracadencia-r35: enemy:mob=26

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 3377, LOW_HEALTH_RETREAT 323, MORALE_SHAKEN 309, MORALE_WAVERING 133, NONE 99, OUT_OF_RANGE 729, RALLIED 18, REFUSE_ATTACK 44, ROUTED_FLEE 39, SEEK_ENEMY 317, STUCK 127, TOO_CLOSE 1773

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t24-techo-phantom-arco-oracadencia-r20 - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t24-techo-phantom-linea-oracadencia-r20 - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t24-techo-phantom-linea-oracadencia-r35 - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t24-techo-phantom-arco-oracadencia-r35 - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
