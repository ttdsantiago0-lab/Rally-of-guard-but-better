# Simulación Rally - lote 20261116-213852-t52-escape-vindicadores80

Preset `t52-escape-vindicadores80` · velocidad `sprint` · 310 corridas · 9 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t52-escape-vindicadores80-linea-pelear | 30 | 42 % ± 6 | 58 % ± 6 | 1.1 ± 0.2 | 15.1 ± 2.8 | 108 ± 4 | 1791 ± 55 | 183 ± 4 | 91 % ± 1 | 16.5 ± 1.7 | 15.7 ± 2.0 | 0 |
| t52-escape-vindicadores80-linea-retirada | 30 | 32 % ± 7 | 68 % ± 7 | 0.7 ± 0.2 | 33.1 ± 3.5 | 85 ± 6 | 1425 ± 66 | 207 ± 4 | 97 % ± 0 | 9.3 ± 0.4 | 12.0 ± 1.1 | 2 |
| t52-escape-vindicadores80-linea-huida | 11 | 23 % ± 8 | 77 % ± 8 | 0.5 ± 0.2 | 72.0 ± 1.0 | 92 ± 11 | 448 ± 11 | 217 ± 5 | 98 % ± 1 | 7.8 ± 0.3 | 17.9 ± 3.2 | 41 |
| t52-escape-vindicadores80-arco-pelear | 30 | 98 % ± 2 | 2 % ± 2 | 5.4 ± 0.4 | 0.1 ± 0.1 | 56 ± 3 | 2133 ± 9 | 98 ± 9 | 91 % ± 1 | 19.4 ± 1.2 | 23.2 ± 1.3 | 0 |
| t52-escape-vindicadores80-arco-retirada | 30 | 93 % ± 5 | 7 % ± 5 | 3.9 ± 0.4 | 0.8 ± 0.6 | 69 ± 3 | 2110 ± 12 | 137 ± 11 | 94 % ± 1 | 16.4 ± 1.0 | 15.2 ± 1.4 | 0 |
| t52-escape-vindicadores80-arco-huida | 20 | 45 % ± 3 | 55 % ± 3 | 1.2 ± 0.1 | 69.0 ± 0.6 | 117 ± 2 | 734 ± 9 | 226 ± 7 | 98 % ± 0 | 7.3 ± 0.1 | 29.9 ± 2.3 | 25 |
| t52-escape-vindicadores80-cab-pelear | 30 | 12 % ± 5 | 88 % ± 5 | 0.3 ± 0.1 | 51.0 ± 3.1 | 71 ± 6 | 1114 ± 63 | 220 ± 4 | 95 % ± 1 | 13.2 ± 0.5 | 11.0 ± 1.4 | 0 |
| t52-escape-vindicadores80-cab-retirada | 30 | 2 % ± 2 | 98 % ± 2 | 0.0 ± 0.0 | 62.0 ± 1.8 | 46 ± 3 | 852 ± 41 | 219 ± 2 | 96 % ± 1 | 12.7 ± 0.4 | 8.1 ± 0.8 | 0 |
| t52-escape-vindicadores80-cab-huida | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 74.7 ± 0.4 | 20 ± 1 | 372 ± 4 | 210 ± 3 | 95 % ± 1 | 12.7 ± 0.4 | 5.7 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t52-escape-vindicadores80-linea-pelear semilla 5201: sprint → draw (1/4 vivos), normal → draw (1/13 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 123 de 123 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t52-escape-vindicadores80-linea-pelear: enemy:mob=200 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=3
- t52-escape-vindicadores80-linea-retirada: enemy:mob=199 enemy:scguns.bullet.annihilated=7 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=4 enemy:scguns.bullet.executed=4 enemy:scguns.bullet.killed=2
- t52-escape-vindicadores80-linea-huida: enemy:mob=78 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t52-escape-vindicadores80-arco-pelear: enemy:mob=56 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=8 enemy:scguns.bullet.eliminated=9 enemy:scguns.bullet.executed=4
- t52-escape-vindicadores80-arco-retirada: enemy:mob=85 enemy:scguns.bullet.annihilated=9 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=9 enemy:scguns.bullet.executed=7 enemy:scguns.bullet.killed=8
- t52-escape-vindicadores80-arco-huida: enemy:mob=123 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=7 enemy:scguns.bullet.killed=4
- t52-escape-vindicadores80-cab-pelear: enemy:mob=231 enemy:scguns.bullet.executed=1
- t52-escape-vindicadores80-cab-retirada: enemy:mob=239
- t52-escape-vindicadores80-cab-huida: enemy:mob=238 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.eliminated=1

## Motivos de decisión (cambios registrados): ENGAGE 12905, FORMATION_HOLD 424, INSTRUCTION 12458, LOW_HEALTH_RETREAT 242, NONE 48, ORDER_OVERRIDE 676, OUT_OF_RANGE 1546, SEEK_ENEMY 614, TOO_CLOSE 692, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t52-escape-vindicadores80-linea-retirada - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-vindicadores80-linea-huida - 41 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-vindicadores80-arco-huida - 25 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t52-escape-vindicadores80-linea-pelear: 17/30 corridas llegaron al tope de tiempo
- t52-escape-vindicadores80-arco-huida: 18/20 corridas llegaron al tope de tiempo
