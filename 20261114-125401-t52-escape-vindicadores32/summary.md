# Simulación Rally - lote 20261114-125401-t52-escape-vindicadores32

Preset `t52-escape-vindicadores32` · velocidad `sprint` · 303 corridas · 9 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t52-escape-vindicadores32-linea-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 4.0 ± 0.0 | 0.0 ± 0.0 | 38 ± 1 | 834 ± 8 | 110 ± 1 | 84 % ± 1 | 17.0 ± 0.9 | 17.0 ± 0.9 | 0 |
| t52-escape-vindicadores32-linea-retirada | 30 | 98 % ± 2 | 2 % ± 2 | 3.5 ± 0.1 | 0.1 ± 0.1 | 66 ± 4 | 814 ± 10 | 141 ± 6 | 85 % ± 1 | 23.7 ± 1.7 | 20.5 ± 1.3 | 2 |
| t52-escape-vindicadores32-linea-huida | 15 | 40 % ± 5 | 60 % ± 5 | 1.5 ± 0.3 | 18.8 ± 1.2 | 112 ± 6 | 495 ± 16 | 215 ± 8 | 97 % ± 1 | 10.7 ± 1.0 | 31.5 ± 2.5 | 35 |
| t52-escape-vindicadores32-arco-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 22 ± 1 | 850 ± 4 | 8 ± 2 | 91 % ± 1 | 16.0 ± 0.9 | 8.4 ± 0.6 | 0 |
| t52-escape-vindicadores32-arco-retirada | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 22 ± 0 | 855 ± 5 | 11 ± 3 | 90 % ± 1 | 15.6 ± 0.9 | 9.6 ± 0.5 | 0 |
| t52-escape-vindicadores32-arco-huida | 28 | 50 % ± 0 | 50 % ± 0 | 4.7 ± 0.3 | 8.3 ± 0.6 | 120 ± 0 | 745 ± 9 | 164 ± 9 | 95 % ± 1 | 13.1 ± 1.1 | 38.9 ± 1.1 | 12 |
| t52-escape-vindicadores32-cab-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 3.5 ± 0.1 | 0.0 ± 0.0 | 41 ± 1 | 826 ± 8 | 126 ± 5 | 85 % ± 1 | 20.9 ± 0.8 | 16.8 ± 1.1 | 0 |
| t52-escape-vindicadores32-cab-retirada | 30 | 87 % ± 6 | 13 % ± 6 | 2.0 ± 0.2 | 1.2 ± 0.6 | 56 ± 3 | 814 ± 12 | 175 ± 6 | 90 % ± 1 | 17.9 ± 0.9 | 10.6 ± 1.1 | 0 |
| t52-escape-vindicadores32-cab-huida | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 23.9 ± 0.5 | 22 ± 1 | 388 ± 6 | 205 ± 2 | 93 % ± 1 | 13.2 ± 0.5 | 6.6 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t52-escape-vindicadores32-linea-pelear semilla 5201: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 216 de 216 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t52-escape-vindicadores32-linea-pelear: enemy:mob=118 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.killed=1
- t52-escape-vindicadores32-linea-retirada: enemy:mob=120 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=2
- t52-escape-vindicadores32-linea-huida: enemy:mob=90 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.killed=2
- t52-escape-vindicadores32-arco-pelear: enemy:mob=1
- t52-escape-vindicadores32-arco-retirada: enemy:mob=3
- t52-escape-vindicadores32-arco-huida: enemy:mob=70 enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=8 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=3
- t52-escape-vindicadores32-cab-pelear: enemy:mob=134
- t52-escape-vindicadores32-cab-retirada: enemy:mob=176 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=1
- t52-escape-vindicadores32-cab-huida: enemy:mob=235 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=2

## Motivos de decisión (cambios registrados): ENGAGE 4110, FORMATION_HOLD 580, INSTRUCTION 1124, LOW_HEALTH_RETREAT 170, NONE 25, ORDER_OVERRIDE 109, OUT_OF_RANGE 979, SEEK_ENEMY 1175, TOO_CLOSE 208

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t52-escape-vindicadores32-linea-huida - 35 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-vindicadores32-arco-huida - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-vindicadores32-linea-retirada - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t52-escape-vindicadores32-linea-huida: 12/15 corridas llegaron al tope de tiempo
- t52-escape-vindicadores32-arco-huida: 28/28 corridas llegaron al tope de tiempo
