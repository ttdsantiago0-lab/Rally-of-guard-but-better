# Simulación Rally - lote 20261112-190249-t52-escape-zombis40

Preset `t52-escape-zombis40` · velocidad `sprint` · 286 corridas · 9 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t52-escape-zombis40-linea-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 0 | 957 ± 2 | 33 ± 2 | 97 % ± 0 | 6.8 ± 0.2 | 8.7 ± 0.3 | 0 |
| t52-escape-zombis40-linea-retirada | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 27 ± 0 | 961 ± 2 | 24 ± 1 | 98 % ± 0 | 7.9 ± 0.2 | 7.7 ± 0.1 | 0 |
| t52-escape-zombis40-linea-huida | 29 | 50 % ± 0 | 50 % ± 0 | 5.9 ± 0.3 | 15.0 ± 0.9 | 120 ± 0 | 739 ± 18 | 102 ± 8 | 98 % ± 0 | 10.4 ± 0.5 | 24.8 ± 0.8 | 5 |
| t52-escape-zombis40-arco-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 20 ± 0 | 971 ± 3 | 0 ± 0 | 99 % ± 0 | 9.0 ± 0.0 | 3.6 ± 0.1 | 0 |
| t52-escape-zombis40-arco-retirada | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 20 ± 0 | 968 ± 2 | 0 ± 0 | 99 % ± 0 | 9.0 ± 0.0 | 3.7 ± 0.1 | 0 |
| t52-escape-zombis40-arco-huida | 22 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 6.7 ± 0.6 | 120 ± 0 | 878 ± 11 | 20 ± 4 | 99 % ± 0 | 15.0 ± 0.9 | 22.6 ± 0.6 | 19 |
| t52-escape-zombis40-cab-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 6.7 ± 0.3 | 0.0 ± 0.0 | 33 ± 1 | 991 ± 8 | 112 ± 5 | 89 % ± 1 | 12.2 ± 0.3 | 12.0 ± 0.3 | 0 |
| t52-escape-zombis40-cab-retirada | 30 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.2 | 0.0 ± 0.0 | 30 ± 1 | 968 ± 4 | 87 ± 5 | 93 % ± 1 | 11.6 ± 0.2 | 10.5 ± 0.1 | 0 |
| t52-escape-zombis40-cab-huida | 30 | 100 % ± 0 | 0 % ± 0 | 4.8 ± 0.2 | 0.0 ± 0.0 | 80 ± 3 | 1046 ± 6 | 208 ± 8 | 91 % ± 1 | 13.0 ± 0.3 | 15.8 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t52-escape-zombis40-linea-pelear semilla 5001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 261 de 261 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t52-escape-zombis40-linea-huida: enemy:mob=30
- t52-escape-zombis40-arco-huida: enemy:mob=1
- t52-escape-zombis40-cab-pelear: enemy:mob=18
- t52-escape-zombis40-cab-retirada: enemy:mob=5
- t52-escape-zombis40-cab-huida: enemy:mob=50

## Motivos de decisión (cambios registrados): ENGAGE 4660, FORMATION_HOLD 405, INSTRUCTION 1560, LOW_HEALTH_RETREAT 418, NONE 6, OUT_OF_RANGE 31, SEEK_ENEMY 2104, STUCK 3, TOO_CLOSE 505

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t52-escape-zombis40-linea-huida - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-zombis40-arco-huida - 19 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t52-escape-zombis40-linea-huida: 29/29 corridas llegaron al tope de tiempo
- t52-escape-zombis40-arco-huida: 22/22 corridas llegaron al tope de tiempo
