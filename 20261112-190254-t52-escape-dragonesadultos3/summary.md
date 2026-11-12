# Simulación Rally - lote 20261112-190254-t52-escape-dragonesadultos3

Preset `t52-escape-dragonesadultos3` · velocidad `sprint` · 304 corridas · 9 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t52-escape-dragonesadultos3-linea-pelear | 29 | 2 % ± 2 | 98 % ± 2 | 0.1 ± 0.1 | 3.0 ± 0.0 | 47 ± 4 | 433 ± 25 | 218 ± 6 | 18 % ± 1 | 9.7 ± 0.3 | 7.6 ± 0.5 | 2 |
| t52-escape-dragonesadultos3-linea-retirada | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 42 ± 2 | 349 ± 28 | 217 ± 5 | 18 % ± 1 | 10.9 ± 0.2 | 7.4 ± 0.3 | 1 |
| t52-escape-dragonesadultos3-linea-huida | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 46 ± 2 | 276 ± 20 | 214 ± 5 | 16 % ± 1 | 12.0 ± 0.3 | 8.1 ± 0.6 | 0 |
| t52-escape-dragonesadultos3-arco-pelear | 27 | 2 % ± 2 | 98 % ± 2 | 0.0 ± 0.0 | 3.0 ± 0.0 | 44 ± 4 | 504 ± 27 | 241 ± 7 | 18 % ± 1 | 12.5 ± 0.4 | 8.3 ± 0.7 | 10 |
| t52-escape-dragonesadultos3-arco-retirada | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 41 ± 2 | 470 ± 25 | 256 ± 8 | 19 % ± 1 | 11.2 ± 0.3 | 7.7 ± 0.4 | 1 |
| t52-escape-dragonesadultos3-arco-huida | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 42 ± 2 | 395 ± 23 | 251 ± 7 | 21 % ± 1 | 11.0 ± 0.4 | 7.6 ± 0.4 | 0 |
| t52-escape-dragonesadultos3-cab-pelear | 23 | 9 % ± 4 | 91 % ± 4 | 0.3 ± 0.2 | 3.0 ± 0.0 | 55 ± 7 | 452 ± 46 | 219 ± 8 | 18 % ± 1 | 12.8 ± 0.6 | 11.9 ± 1.4 | 19 |
| t52-escape-dragonesadultos3-cab-retirada | 28 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 49 ± 4 | 422 ± 40 | 233 ± 6 | 18 % ± 1 | 12.7 ± 0.6 | 9.1 ± 0.6 | 12 |
| t52-escape-dragonesadultos3-cab-huida | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 52 ± 3 | 434 ± 30 | 243 ± 6 | 16 % ± 1 | 14.0 ± 0.5 | 12.8 ± 0.8 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t52-escape-dragonesadultos3-linea-pelear semilla 5001: sprint → red (0/3 vivos), normal → red (0/3 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 6 de 6 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t52-escape-dragonesadultos3-linea-pelear: enemy:dragon_fire=88 enemy:mob=118 env:inFire=10 env:onFire=14
- t52-escape-dragonesadultos3-linea-retirada: enemy:dragon_fire=93 enemy:mob=119 env:inFire=10 env:onFire=18
- t52-escape-dragonesadultos3-linea-huida: enemy:dragon_fire=100 enemy:mob=112 env:inFire=18 env:onFire=10
- t52-escape-dragonesadultos3-arco-pelear: enemy:dragon_fire=98 enemy:mob=102 env:inFire=11 env:onFire=4
- t52-escape-dragonesadultos3-arco-retirada: enemy:dragon_fire=95 enemy:mob=130 env:inFire=7 env:onFire=8
- t52-escape-dragonesadultos3-arco-huida: enemy:dragon_fire=106 enemy:mob=110 env:inFire=11 env:onFire=13
- t52-escape-dragonesadultos3-cab-pelear: enemy:dragon_fire=73 enemy:mob=87 env:inFire=9 env:onFire=7
- t52-escape-dragonesadultos3-cab-retirada: enemy:dragon_fire=112 enemy:mob=93 env:inFire=14 env:onFire=5
- t52-escape-dragonesadultos3-cab-huida: enemy:dragon_fire=116 enemy:mob=106 env:fall=1 env:inFire=11 env:onFire=6

## Motivos de decisión (cambios registrados): ENGAGE 5234, FORMATION_HOLD 697, INSTRUCTION 1309, LOW_HEALTH_RETREAT 2298, NONE 40, OUT_OF_RANGE 66, SEEK_ENEMY 2184, STUCK 21, TOO_CLOSE 1531, UNIT_BROKEN 3

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t52-escape-dragonesadultos3-cab-retirada - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-dragonesadultos3-arco-pelear - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-dragonesadultos3-linea-pelear - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-dragonesadultos3-cab-pelear - 19 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-dragonesadultos3-cab-huida - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-dragonesadultos3-linea-retirada - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t52-escape-dragonesadultos3-arco-retirada - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t52-escape-dragonesadultos3-linea-pelear - 10 % de las bajas azules ({enemy:dragon_fire=88.0, enemy:mob=118.0, env:inFire=10.0, env:onFire=14.0})
- MUERTES QUE NO SON DEL ENEMIGO: t52-escape-dragonesadultos3-linea-retirada - 12 % de las bajas azules ({enemy:dragon_fire=93.0, enemy:mob=119.0, env:inFire=10.0, env:onFire=18.0})
- MUERTES QUE NO SON DEL ENEMIGO: t52-escape-dragonesadultos3-linea-huida - 12 % de las bajas azules ({enemy:dragon_fire=100.0, enemy:mob=112.0, env:inFire=18.0, env:onFire=10.0})
