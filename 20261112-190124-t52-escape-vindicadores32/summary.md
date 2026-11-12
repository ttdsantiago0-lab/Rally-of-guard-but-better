# Simulación Rally - lote 20261112-190124-t52-escape-vindicadores32

Preset `t52-escape-vindicadores32` · velocidad `sprint` · 298 corridas · 9 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t52-escape-vindicadores32-linea-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 3.9 ± 0.0 | 0.0 ± 0.0 | 38 ± 1 | 836 ± 8 | 110 ± 2 | 84 % ± 1 | 17.1 ± 0.9 | 16.5 ± 0.9 | 0 |
| t52-escape-vindicadores32-linea-retirada | 28 | 100 % ± 0 | 0 % ± 0 | 3.8 ± 0.1 | 0.0 ± 0.0 | 63 ± 4 | 822 ± 9 | 134 ± 6 | 85 % ± 1 | 23.5 ± 1.8 | 21.0 ± 1.2 | 8 |
| t52-escape-vindicadores32-linea-huida | 25 | 44 % ± 3 | 56 % ± 3 | 1.6 ± 0.2 | 18.3 ± 0.6 | 112 ± 4 | 505 ± 9 | 206 ± 5 | 97 % ± 0 | 9.2 ± 0.6 | 29.5 ± 2.5 | 14 |
| t52-escape-vindicadores32-arco-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.0 | 0.0 ± 0.0 | 22 ± 1 | 861 ± 5 | 11 ± 2 | 90 % ± 1 | 14.7 ± 1.2 | 9.5 ± 0.7 | 0 |
| t52-escape-vindicadores32-arco-retirada | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 23 ± 2 | 843 ± 4 | 16 ± 3 | 91 % ± 1 | 16.3 ± 1.5 | 9.7 ± 0.7 | 0 |
| t52-escape-vindicadores32-arco-huida | 25 | 52 % ± 2 | 48 % ± 2 | 5.0 ± 0.4 | 8.6 ± 0.7 | 119 ± 1 | 743 ± 10 | 156 ± 10 | 94 % ± 1 | 14.9 ± 1.5 | 36.7 ± 1.4 | 17 |
| t52-escape-vindicadores32-cab-pelear | 30 | 97 % ± 3 | 3 % ± 3 | 3.5 ± 0.2 | 0.4 ± 0.4 | 42 ± 1 | 824 ± 8 | 124 ± 4 | 84 % ± 1 | 21.2 ± 0.9 | 16.1 ± 1.1 | 0 |
| t52-escape-vindicadores32-cab-retirada | 30 | 87 % ± 6 | 13 % ± 6 | 2.2 ± 0.2 | 2.3 ± 1.1 | 48 ± 2 | 790 ± 19 | 169 ± 6 | 89 % ± 1 | 17.6 ± 0.7 | 12.4 ± 0.9 | 0 |
| t52-escape-vindicadores32-cab-huida | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 21.9 ± 0.7 | 28 ± 1 | 420 ± 8 | 213 ± 3 | 93 % ± 1 | 13.9 ± 0.4 | 7.1 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t52-escape-vindicadores32-linea-pelear semilla 5001: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 220 de 220 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t52-escape-vindicadores32-linea-pelear: enemy:mob=119 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1
- t52-escape-vindicadores32-linea-retirada: enemy:mob=108 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=2
- t52-escape-vindicadores32-linea-huida: enemy:mob=146 enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=2
- t52-escape-vindicadores32-arco-pelear: enemy:mob=1 enemy:scguns.bullet.eliminated=1
- t52-escape-vindicadores32-arco-retirada: enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=2
- t52-escape-vindicadores32-arco-huida: enemy:mob=56 enemy:scguns.bullet.annihilated=4 enemy:scguns.bullet.decimated=5 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=5
- t52-escape-vindicadores32-cab-pelear: enemy:mob=136
- t52-escape-vindicadores32-cab-retirada: enemy:mob=175
- t52-escape-vindicadores32-cab-huida: enemy:mob=237 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.killed=1

## Motivos de decisión (cambios registrados): ENGAGE 4130, FORMATION_HOLD 725, INSTRUCTION 1129, LOW_HEALTH_RETREAT 173, NONE 44, OUT_OF_RANGE 903, SEEK_ENEMY 1327, TOO_CLOSE 230

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t52-escape-vindicadores32-arco-huida - 17 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-vindicadores32-linea-huida - 14 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-vindicadores32-linea-retirada - 8 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t52-escape-vindicadores32-linea-huida: 22/25 corridas llegaron al tope de tiempo
- t52-escape-vindicadores32-arco-huida: 24/25 corridas llegaron al tope de tiempo
