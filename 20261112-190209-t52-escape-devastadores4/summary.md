# Simulación Rally - lote 20261112-190209-t52-escape-devastadores4

Preset `t52-escape-devastadores4` · velocidad `sprint` · 282 corridas · 9 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t52-escape-devastadores4-linea-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 7.1 ± 0.1 | 0.0 ± 0.0 | 18 ± 0 | 415 ± 1 | 42 ± 4 | 96 % ± 1 | 9.6 ± 0.4 | 9.9 ± 0.2 | 0 |
| t52-escape-devastadores4-linea-retirada | 30 | 100 % ± 0 | 0 % ± 0 | 7.1 ± 0.1 | 0.0 ± 0.0 | 29 ± 4 | 414 ± 1 | 38 ± 4 | 95 % ± 1 | 18.8 ± 3.4 | 10.7 ± 0.4 | 0 |
| t52-escape-devastadores4-linea-huida | 29 | 93 % ± 3 | 7 % ± 3 | 5.8 ± 0.3 | 0.2 ± 0.1 | 73 ± 7 | 411 ± 2 | 85 ± 8 | 96 % ± 0 | 37.2 ± 3.8 | 15.5 ± 0.8 | 9 |
| t52-escape-devastadores4-arco-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 16 ± 0 | 414 ± 1 | 0 ± 0 | 95 % ± 1 | 10.5 ± 0.4 | 10.7 ± 0.3 | 0 |
| t52-escape-devastadores4-arco-retirada | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 16 ± 1 | 412 ± 1 | 0 ± 0 | 97 % ± 1 | 11.2 ± 0.6 | 10.5 ± 0.4 | 0 |
| t52-escape-devastadores4-arco-huida | 30 | 90 % ± 4 | 10 % ± 4 | 7.7 ± 0.1 | 0.2 ± 0.1 | 39 ± 8 | 411 ± 2 | 12 ± 4 | 97 % ± 0 | 21.3 ± 3.6 | 12.3 ± 0.7 | 3 |
| t52-escape-devastadores4-cab-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 4.4 ± 0.2 | 0.0 ± 0.0 | 23 ± 1 | 413 ± 1 | 102 ± 5 | 88 % ± 1 | 13.5 ± 0.4 | 12.4 ± 0.3 | 0 |
| t52-escape-devastadores4-cab-retirada | 30 | 100 % ± 0 | 0 % ± 0 | 4.9 ± 0.3 | 0.0 ± 0.0 | 23 ± 0 | 415 ± 1 | 98 ± 5 | 88 % ± 1 | 13.9 ± 0.3 | 11.9 ± 0.3 | 0 |
| t52-escape-devastadores4-cab-huida | 30 | 73 % ± 8 | 27 % ± 8 | 2.6 ± 0.4 | 0.4 ± 0.1 | 43 ± 3 | 401 ± 5 | 172 ± 10 | 91 % ± 1 | 13.6 ± 0.4 | 12.9 ± 0.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t52-escape-devastadores4-linea-pelear semilla 5001: sprint → blue (7/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 261 de 261 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t52-escape-devastadores4-linea-pelear: enemy:mob=27
- t52-escape-devastadores4-linea-retirada: enemy:mob=28
- t52-escape-devastadores4-linea-huida: enemy:mob=64
- t52-escape-devastadores4-arco-huida: enemy:mob=10
- t52-escape-devastadores4-cab-pelear: enemy:mob=108
- t52-escape-devastadores4-cab-retirada: enemy:mob=92
- t52-escape-devastadores4-cab-huida: enemy:mob=162

## Motivos de decisión (cambios registrados): ENGAGE 3515, FORMATION_HOLD 604, INSTRUCTION 1318, LOW_HEALTH_RETREAT 266, NONE 2, OUT_OF_RANGE 29, SEEK_ENEMY 1122, TOO_CLOSE 127

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t52-escape-devastadores4-linea-huida - 9 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-devastadores4-arco-huida - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
