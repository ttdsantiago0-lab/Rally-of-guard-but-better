# Simulación Rally - lote 20261114-125441-t52-escape-devastadores8

Preset `t52-escape-devastadores8` · velocidad `sprint` · 293 corridas · 9 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t52-escape-devastadores8-linea-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 5.4 ± 0.3 | 0.0 ± 0.0 | 33 ± 1 | 827 ± 1 | 94 ± 8 | 93 % ± 1 | 15.3 ± 1.0 | 17.5 ± 0.8 | 0 |
| t52-escape-devastadores8-linea-retirada | 30 | 97 % ± 2 | 3 % ± 2 | 3.5 ± 0.2 | 0.2 ± 0.1 | 87 ± 3 | 827 ± 4 | 153 ± 8 | 95 % ± 0 | 36.0 ± 1.9 | 21.8 ± 0.9 | 0 |
| t52-escape-devastadores8-linea-huida | 24 | 50 % ± 0 | 50 % ± 0 | 1.5 ± 0.1 | 5.3 ± 0.2 | 120 ± 0 | 556 ± 12 | 209 ± 4 | 97 % ± 0 | 19.8 ± 1.5 | 24.4 ± 1.7 | 18 |
| t52-escape-devastadores8-arco-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 21 ± 0 | 827 ± 1 | 0 ± 0 | 96 % ± 1 | 10.3 ± 0.5 | 10.4 ± 0.6 | 0 |
| t52-escape-devastadores8-arco-retirada | 30 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.1 | 0.0 ± 0.0 | 43 ± 5 | 830 ± 1 | 10 ± 3 | 96 % ± 0 | 30.3 ± 4.1 | 13.7 ± 0.7 | 1 |
| t52-escape-devastadores8-arco-huida | 27 | 52 % ± 2 | 48 % ± 2 | 5.6 ± 0.2 | 2.7 ± 0.3 | 119 ± 1 | 755 ± 9 | 86 ± 6 | 96 % ± 0 | 44.3 ± 1.8 | 23.6 ± 0.9 | 12 |
| t52-escape-devastadores8-cab-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 3.9 ± 0.1 | 0.0 ± 0.0 | 39 ± 1 | 828 ± 1 | 121 ± 4 | 91 % ± 1 | 17.4 ± 0.8 | 17.3 ± 1.3 | 0 |
| t52-escape-devastadores8-cab-retirada | 30 | 93 % ± 5 | 7 % ± 5 | 3.0 ± 0.2 | 0.3 ± 0.2 | 47 ± 2 | 814 ± 12 | 174 ± 7 | 94 % ± 0 | 14.9 ± 0.5 | 12.7 ± 0.8 | 0 |
| t52-escape-devastadores8-cab-huida | 30 | 3 % ± 3 | 97 % ± 3 | 0.0 ± 0.0 | 4.7 ± 0.3 | 53 ± 3 | 582 ± 21 | 261 ± 6 | 95 % ± 0 | 10.8 ± 0.5 | 10.5 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t52-escape-devastadores8-linea-pelear semilla 5201: sprint → blue (5/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 230 de 230 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t52-escape-devastadores8-linea-pelear: enemy:mob=77
- t52-escape-devastadores8-linea-retirada: enemy:mob=136
- t52-escape-devastadores8-linea-huida: enemy:mob=157
- t52-escape-devastadores8-arco-retirada: enemy:mob=11
- t52-escape-devastadores8-arco-huida: enemy:mob=65
- t52-escape-devastadores8-cab-pelear: enemy:mob=123
- t52-escape-devastadores8-cab-retirada: enemy:mob=151
- t52-escape-devastadores8-cab-huida: enemy:mob=239

## Motivos de decisión (cambios registrados): ENGAGE 4150, FORMATION_HOLD 1273, INSTRUCTION 1298, LOW_HEALTH_RETREAT 516, NONE 18, ORDER_OVERRIDE 97, OUT_OF_RANGE 253, SEEK_ENEMY 2036, TOO_CLOSE 284

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t52-escape-devastadores8-linea-huida - 18 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-devastadores8-arco-huida - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-devastadores8-arco-retirada - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t52-escape-devastadores8-linea-huida: 24/24 corridas llegaron al tope de tiempo
- t52-escape-devastadores8-arco-huida: 26/27 corridas llegaron al tope de tiempo
