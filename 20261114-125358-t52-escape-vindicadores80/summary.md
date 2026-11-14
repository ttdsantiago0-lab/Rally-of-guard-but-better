# Simulación Rally - lote 20261114-125358-t52-escape-vindicadores80

Preset `t52-escape-vindicadores80` · velocidad `sprint` · 306 corridas · 9 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t52-escape-vindicadores80-linea-pelear | 30 | 38 % ± 6 | 62 % ± 6 | 1.1 ± 0.2 | 18.9 ± 3.1 | 102 ± 5 | 1732 ± 56 | 187 ± 5 | 91 % ± 2 | 17.2 ± 2.1 | 22.2 ± 2.5 | 0 |
| t52-escape-vindicadores80-linea-retirada | 30 | 40 % ± 4 | 60 % ± 4 | 1.6 ± 0.2 | 44.1 ± 2.4 | 107 ± 5 | 1230 ± 54 | 197 ± 5 | 93 % ± 1 | 10.4 ± 0.7 | 23.9 ± 2.0 | 1 |
| t52-escape-vindicadores80-linea-huida | 22 | 32 % ± 5 | 68 % ± 5 | 1.0 ± 0.2 | 69.8 ± 0.8 | 104 ± 6 | 502 ± 11 | 216 ± 5 | 98 % ± 1 | 7.3 ± 0.2 | 26.6 ± 2.6 | 24 |
| t52-escape-vindicadores80-arco-pelear | 30 | 100 % ± 0 | 0 % ± 0 | 6.0 ± 0.3 | 0.0 ± 0.0 | 52 ± 2 | 2134 ± 8 | 85 ± 7 | 93 % ± 1 | 16.0 ± 0.8 | 21.0 ± 1.4 | 0 |
| t52-escape-vindicadores80-arco-retirada | 30 | 60 % ± 6 | 40 % ± 6 | 3.3 ± 0.4 | 10.5 ± 2.2 | 111 ± 3 | 1962 ± 37 | 180 ± 9 | 90 % ± 1 | 12.7 ± 0.7 | 28.0 ± 1.3 | 0 |
| t52-escape-vindicadores80-arco-huida | 23 | 43 % ± 4 | 57 % ± 4 | 1.7 ± 0.2 | 61.7 ± 0.8 | 114 ± 4 | 874 ± 13 | 212 ± 8 | 98 % ± 0 | 7.3 ± 0.2 | 33.0 ± 1.8 | 24 |
| t52-escape-vindicadores80-cab-pelear | 30 | 7 % ± 3 | 93 % ± 3 | 0.1 ± 0.1 | 55.3 ± 2.3 | 62 ± 6 | 1033 ± 55 | 223 ± 3 | 96 % ± 1 | 13.2 ± 0.6 | 12.1 ± 1.4 | 0 |
| t52-escape-vindicadores80-cab-retirada | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 65.9 ± 0.9 | 41 ± 3 | 731 ± 28 | 220 ± 3 | 96 % ± 1 | 13.1 ± 0.3 | 8.6 ± 0.6 | 0 |
| t52-escape-vindicadores80-cab-huida | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 73.8 ± 0.5 | 21 ± 1 | 411 ± 8 | 206 ± 2 | 95 % ± 1 | 14.2 ± 0.5 | 7.1 ± 0.4 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t52-escape-vindicadores80-linea-pelear semilla 5201: sprint → blue (3/0 vivos), normal → draw (2/2 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 138 de 138 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t52-escape-vindicadores80-linea-pelear: enemy:mob=199 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=3 enemy:scguns.bullet.killed=3
- t52-escape-vindicadores80-linea-retirada: enemy:mob=177 enemy:scguns.bullet.annihilated=5 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.executed=2 enemy:scguns.bullet.killed=3
- t52-escape-vindicadores80-linea-huida: enemy:mob=141 enemy:scguns.bullet.annihilated=6 enemy:scguns.bullet.decimated=2 enemy:scguns.bullet.eliminated=2 enemy:scguns.bullet.killed=2
- t52-escape-vindicadores80-arco-pelear: enemy:mob=47 enemy:scguns.bullet.annihilated=3 enemy:scguns.bullet.decimated=4 enemy:scguns.bullet.eliminated=3 enemy:scguns.bullet.executed=1 enemy:scguns.bullet.killed=1
- t52-escape-vindicadores80-arco-retirada: enemy:mob=111 enemy:scguns.bullet.annihilated=6 enemy:scguns.bullet.decimated=6 enemy:scguns.bullet.eliminated=1 enemy:scguns.bullet.executed=8 enemy:scguns.bullet.killed=9
- t52-escape-vindicadores80-arco-huida: enemy:mob=124 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=3 enemy:scguns.bullet.eliminated=5 enemy:scguns.bullet.executed=5 enemy:scguns.bullet.killed=7
- t52-escape-vindicadores80-cab-pelear: enemy:mob=236
- t52-escape-vindicadores80-cab-retirada: enemy:mob=236 enemy:scguns.bullet.annihilated=1 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=2
- t52-escape-vindicadores80-cab-huida: enemy:mob=236 enemy:scguns.bullet.annihilated=2 enemy:scguns.bullet.decimated=1 enemy:scguns.bullet.eliminated=1

## Motivos de decisión (cambios registrados): ENGAGE 4795, FORMATION_HOLD 274, INSTRUCTION 1148, LOW_HEALTH_RETREAT 203, NONE 41, ORDER_OVERRIDE 117, OUT_OF_RANGE 1348, SEEK_ENEMY 1192, TOO_CLOSE 478, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t52-escape-vindicadores80-linea-huida - 24 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-vindicadores80-arco-huida - 24 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-vindicadores80-cab-huida - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-vindicadores80-linea-retirada - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t52-escape-vindicadores80-linea-retirada: 24/30 corridas llegaron al tope de tiempo
- t52-escape-vindicadores80-linea-huida: 14/22 corridas llegaron al tope de tiempo
- t52-escape-vindicadores80-arco-retirada: 18/30 corridas llegaron al tope de tiempo
- t52-escape-vindicadores80-arco-huida: 20/23 corridas llegaron al tope de tiempo
- t52-escape-vindicadores80-linea-pelear: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
