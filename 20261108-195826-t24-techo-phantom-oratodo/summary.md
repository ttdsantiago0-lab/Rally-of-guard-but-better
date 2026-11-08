# Simulación Rally - lote 20261108-195826-t24-techo-phantom-oratodo

Preset `t24-techo-phantom-oratodo` · velocidad `sprint` · 148 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t24-techo-phantom-arco-oratodo-r20 | 11 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 45 ± 2 | 487 ± 4 | 13 ± 1 | 30 % ± 1 | 20.1 ± 2.9 | 21.0 ± 1.7 | 23 |
| t24-techo-phantom-linea-oratodo-r20 | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 61 ± 5 | 474 ± 3 | 16 ± 2 | 33 % ± 2 | 19.4 ± 1.7 | 20.4 ± 1.9 | 23 |
| t24-techo-phantom-linea-oratodo-r35 | 1 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 87 ± 0 | 825 ± 0 | 41 ± 0 | 31 % ± 0 | 12.0 ± 0.0 | 18.6 ± 0.0 | 38 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t24-techo-phantom-arco-oratodo-r20 semilla 1: sprint → draw (8/1 vivos), normal → draw (8/2 vivos)
- t24-techo-phantom-arco-oratodo-r20 semilla 7778: sprint → draw (8/2 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 22 de 22 corridas ✓

## Motivos de decisión (cambios registrados): COUNTERATTACK 34, ENGAGE 6127, LOW_HEALTH_RETREAT 187, MORALE_SHAKEN 838, MORALE_WAVERING 469, NONE 291, OUT_OF_RANGE 1836, RALLIED 52, REFUSE_ATTACK 154, ROUTED_FLEE 200, SEEK_ENEMY 1924, STUCK 268, TOO_CLOSE 2081

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t24-techo-phantom-arco-oratodo-r20 - 23 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t24-techo-phantom-arco-oratodo-r35 - 40 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t24-techo-phantom-linea-oratodo-r20 - 23 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t24-techo-phantom-linea-oratodo-r35 - 38 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
