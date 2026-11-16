# Simulación Rally - lote 20261116-213838-t52-escape-devastadores8

Preset `t52-escape-devastadores8` · velocidad `sprint` · 292 corridas · 9 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t52-escape-devastadores8-linea-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 5.5 ± 0.2 | 0.0 ± 0.0 | 33 ± 1 | 829 ± 1 | 90 ± 6 | 91 % ± 1 | 15.8 ± 1.1 | 18.3 ± 0.6 | 0 |
| t52-escape-devastadores8-linea-retirada | 30 | 100 % ± 0 | 0 % ± 0 | 4.7 ± 0.2 | 0.0 ± 0.0 | 53 ± 4 | 828 ± 1 | 108 ± 6 | 93 % ± 1 | 24.7 ± 2.9 | 15.9 ± 0.6 | 2 |
| t52-escape-devastadores8-linea-huida | 26 | 33 % ± 5 | 67 % ± 5 | 1.5 ± 0.3 | 7.2 ± 0.2 | 108 ± 4 | 452 ± 8 | 214 ± 9 | 96 % ± 0 | 15.3 ± 2.3 | 29.2 ± 2.3 | 12 |
| t52-escape-devastadores8-arco-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 22 ± 0 | 829 ± 1 | 0 ± 0 | 95 % ± 0 | 11.1 ± 0.5 | 10.8 ± 0.5 | 0 |
| t52-escape-devastadores8-arco-retirada | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 2 | 828 ± 1 | 1 ± 1 | 94 % ± 0 | 14.1 ± 2.1 | 12.0 ± 0.4 | 1 |
| t52-escape-devastadores8-arco-huida | 28 | 48 % ± 2 | 52 % ± 2 | 4.1 ± 0.3 | 5.7 ± 0.2 | 120 ± 0 | 668 ± 8 | 154 ± 14 | 97 % ± 0 | 31.3 ± 2.8 | 34.8 ± 1.8 | 12 |
| t52-escape-devastadores8-cab-pelear | 30 | 97 % ± 3 | 3 % ± 3 | 3.6 ± 0.1 | 0.1 ± 0.1 | 41 ± 1 | 823 ± 5 | 133 ± 4 | 92 % ± 1 | 16.4 ± 0.7 | 15.7 ± 1.0 | 0 |
| t52-escape-devastadores8-cab-retirada | 30 | 100 % ± 0 | 0 % ± 0 | 3.8 ± 0.1 | 0.0 ± 0.0 | 39 ± 1 | 829 ± 1 | 143 ± 5 | 94 % ± 0 | 14.4 ± 0.3 | 13.2 ± 0.6 | 0 |
| t52-escape-devastadores8-cab-huida | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 5.9 ± 0.2 | 36 ± 2 | 464 ± 15 | 263 ± 8 | 93 % ± 1 | 9.2 ± 0.2 | 7.3 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t52-escape-devastadores8-linea-pelear semilla 5201: sprint → blue (7/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 223 de 223 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t52-escape-devastadores8-linea-pelear: enemy:mob=74
- t52-escape-devastadores8-linea-retirada: enemy:mob=98
- t52-escape-devastadores8-linea-huida: enemy:mob=170
- t52-escape-devastadores8-arco-retirada: enemy:mob=1
- t52-escape-devastadores8-arco-huida: enemy:mob=109
- t52-escape-devastadores8-cab-pelear: enemy:mob=133
- t52-escape-devastadores8-cab-retirada: enemy:mob=125
- t52-escape-devastadores8-cab-huida: enemy:mob=240

## Motivos de decisión (cambios registrados): ENGAGE 7454, FORMATION_HOLD 1384, INSTRUCTION 7861, LOW_HEALTH_RETREAT 437, NONE 15, ORDER_OVERRIDE 706, OUT_OF_RANGE 461, SEEK_ENEMY 981, TOO_CLOSE 258, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t52-escape-devastadores8-linea-huida - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-devastadores8-arco-huida - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-devastadores8-linea-retirada - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-devastadores8-arco-retirada - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t52-escape-devastadores8-linea-huida: 17/26 corridas llegaron al tope de tiempo
- t52-escape-devastadores8-arco-huida: 27/28 corridas llegaron al tope de tiempo
