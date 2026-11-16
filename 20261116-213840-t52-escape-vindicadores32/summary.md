# Simulación Rally - lote 20261116-213840-t52-escape-vindicadores32

Preset `t52-escape-vindicadores32` · velocidad `sprint` · 305 corridas · 9 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t52-escape-vindicadores32-linea-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 4.0 ± 0.0 | 0.0 ± 0.0 | 39 ± 1 | 827 ± 9 | 110 ± 2 | 83 % ± 1 | 18.7 ± 1.0 | 16.8 ± 0.8 | 0 |
| t52-escape-vindicadores32-linea-retirada | 30 | 100 % ± 0 | 0 % ± 0 | 4.0 ± 0.0 | 0.0 ± 0.0 | 39 ± 1 | 826 ± 6 | 121 ± 2 | 85 % ± 1 | 17.3 ± 1.0 | 13.5 ± 0.9 | 0 |
| t52-escape-vindicadores32-arco-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 24 ± 1 | 854 ± 4 | 10 ± 2 | 89 % ± 1 | 17.5 ± 1.1 | 9.3 ± 0.6 | 0 |
| t52-escape-vindicadores32-arco-retirada | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 22 ± 1 | 864 ± 4 | 9 ± 2 | 92 % ± 1 | 14.1 ± 0.8 | 8.1 ± 0.5 | 0 |
| t52-escape-vindicadores32-arco-huida | 25 | 46 % ± 3 | 54 % ± 3 | 3.3 ± 0.4 | 11.7 ± 0.8 | 118 ± 1 | 693 ± 8 | 202 ± 8 | 95 % ± 1 | 10.7 ± 1.1 | 34.9 ± 1.9 | 17 |
| t52-escape-vindicadores32-cab-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 3.2 ± 0.2 | 0.0 ± 0.0 | 45 ± 1 | 840 ± 7 | 133 ± 5 | 85 % ± 1 | 20.5 ± 0.8 | 17.7 ± 1.1 | 0 |
| t52-escape-vindicadores32-cab-retirada | 30 | 93 % ± 5 | 7 % ± 5 | 2.9 ± 0.2 | 0.4 ± 0.3 | 42 ± 1 | 828 ± 9 | 139 ± 6 | 90 % ± 1 | 16.7 ± 0.7 | 10.2 ± 0.9 | 0 |
| t52-escape-vindicadores32-cab-huida | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 25.4 ± 0.4 | 27 ± 1 | 364 ± 5 | 213 ± 2 | 94 % ± 1 | 13.6 ± 0.4 | 5.6 ± 0.3 | 0 |
| t52-escape-vindicadores32-linea-huida | 11 | 27 % ± 8 | 73 % ± 8 | 0.8 ± 0.3 | 21.2 ± 1.2 | 101 ± 9 | 418 ± 13 | 229 ± 8 | 91 % ± 2 | 9.9 ± 1.0 | 26.2 ± 3.9 | 41 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t52-escape-vindicadores32-linea-pelear semilla 5201: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 207 de 207 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t52-escape-vindicadores32-linea-pelear: enemy:mob=119 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.killed=1
- t52-escape-vindicadores32-linea-retirada: enemy:mob=116 enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.executed=1
- t52-escape-vindicadores32-arco-pelear: enemy:mob=1
- t52-escape-vindicadores32-arco-huida: enemy:mob=107 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=1
- t52-escape-vindicadores32-cab-pelear: enemy:mob=144
- t52-escape-vindicadores32-cab-retirada: enemy:mob=152 enemy:scguns.bullet.decimated=1
- t52-escape-vindicadores32-cab-huida: enemy:mob=234 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.killed=1
- t52-escape-vindicadores32-linea-huida: enemy:mob=76 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1

## Motivos de decisión (cambios registrados): ENGAGE 7734, FORMATION_HOLD 276, INSTRUCTION 6321, LOW_HEALTH_RETREAT 125, NONE 32, ORDER_OVERRIDE 666, OUT_OF_RANGE 1266, SEEK_ENEMY 67, TOO_CLOSE 163

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t52-escape-vindicadores32-linea-huida - 41 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-vindicadores32-arco-huida - 17 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t52-escape-vindicadores32-arco-huida: 23/25 corridas llegaron al tope de tiempo
- t52-escape-vindicadores32-linea-huida: 6/11 corridas llegaron al tope de tiempo
