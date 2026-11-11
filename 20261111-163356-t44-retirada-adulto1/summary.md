# Simulación Rally - lote 20261111-163356-t44-retirada-adulto1

Preset `t44-retirada-adulto1` · velocidad `sprint` · 55 corridas · 9 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t44-retirada-adulto1-linea-pelear | 6 | 100 % ± 0 | 0 % ± 0 | 7.3 ± 0.3 | 0.0 ± 0.0 | 29 ± 2 | 253 ± 1 | 42 ± 15 | 20 % ± 4 | 14.4 ± 0.7 | 6.1 ± 0.5 | 0 |
| t44-retirada-adulto1-linea-vieja | 6 | 17 % ± 17 | 83 % ± 17 | 0.2 ± 0.2 | 0.8 ± 0.2 | 79 ± 4 | 192 ± 18 | 231 ± 11 | 26 % ± 5 | 14.6 ± 1.3 | 5.1 ± 0.5 | 0 |
| t44-retirada-adulto1-linea-nueva | 6 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.3 | 0.0 ± 0.0 | 24 ± 1 | 257 ± 1 | 14 ± 10 | 11 % ± 4 | 28.5 ± 1.6 | 11.4 ± 0.3 | 0 |
| t44-retirada-adulto1-arco-pelear | 6 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.5 | 0.0 ± 0.0 | 28 ± 2 | 254 ± 2 | 21 ± 13 | 20 % ± 2 | 14.8 ± 0.4 | 3.6 ± 0.1 | 0 |
| t44-retirada-adulto1-arco-vieja | 6 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.3 | 0.0 ± 0.0 | 31 ± 2 | 254 ± 1 | 21 ± 14 | 25 % ± 2 | 19.0 ± 1.1 | 8.6 ± 0.3 | 0 |
| t44-retirada-adulto1-arco-nueva | 6 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.2 | 0.0 ± 0.0 | 31 ± 2 | 253 ± 1 | 25 ± 8 | 28 % ± 4 | 21.8 ± 1.8 | 9.4 ± 0.8 | 0 |
| t44-retirada-adulto1-cab-pelear | 6 | 100 % ± 0 | 0 % ± 0 | 6.7 ± 0.8 | 0.0 ± 0.0 | 30 ± 3 | 252 ± 1 | 60 ± 27 | 17 % ± 5 | 13.3 ± 2.2 | 6.8 ± 1.5 | 0 |
| t44-retirada-adulto1-cab-vieja | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 44 ± 4 | 92 ± 9 | 209 ± 13 | 47 % ± 11 | 13.3 ± 1.3 | 4.6 ± 0.4 | 0 |
| t44-retirada-adulto1-cab-nueva | 6 | 100 % ± 0 | 0 % ± 0 | 6.0 ± 0.5 | 0.0 ± 0.0 | 34 ± 3 | 254 ± 1 | 74 ± 17 | 17 % ± 4 | 28.7 ± 2.7 | 12.4 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t44-retirada-adulto1-linea-pelear semilla 1: sprint → blue (7/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 43 de 43 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t44-retirada-adulto1-linea-pelear: enemy:dragon_fire=3 enemy:mob=1
- t44-retirada-adulto1-linea-vieja: enemy:dragon_fire=17 enemy:mob=22 env:inFire=4 env:onFire=4
- t44-retirada-adulto1-linea-nueva: enemy:dragon_fire=2 enemy:mob=1
- t44-retirada-adulto1-arco-pelear: enemy:dragon_fire=1 enemy:mob=2
- t44-retirada-adulto1-arco-vieja: enemy:dragon_fire=2 enemy:mob=1
- t44-retirada-adulto1-arco-nueva: enemy:dragon_fire=2
- t44-retirada-adulto1-cab-pelear: enemy:dragon_fire=4 enemy:mob=4
- t44-retirada-adulto1-cab-vieja: enemy:dragon_fire=19 enemy:mob=26 env:inFire=2 env:onFire=1
- t44-retirada-adulto1-cab-nueva: enemy:dragon_fire=8 enemy:mob=4

## Motivos de decisión (cambios registrados): ENGAGE 604, FORMATION_HOLD 239, INSTRUCTION 288, LOW_HEALTH_RETREAT 124, NONE 4, ORDER_OVERRIDE 6, OUT_OF_RANGE 1, SEEK_ENEMY 217, STUCK 2, TOO_CLOSE 117

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: t44-retirada-adulto1-linea-vieja - 17 % de las bajas azules ({enemy:dragon_fire=17.0, enemy:mob=22.0, env:inFire=4.0, env:onFire=4.0})
