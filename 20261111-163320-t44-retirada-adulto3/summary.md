# Simulación Rally - lote 20261111-163320-t44-retirada-adulto3

Preset `t44-retirada-adulto3` · velocidad `sprint` · 57 corridas · 9 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t44-retirada-adulto3-linea-pelear | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 36 ± 3 | 346 ± 27 | 214 ± 6 | 14 % ± 2 | 11.3 ± 0.6 | 7.6 ± 1.4 | 1 |
| t44-retirada-adulto3-linea-vieja | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 38 ± 3 | 156 ± 24 | 229 ± 14 | 24 % ± 7 | 14.2 ± 0.7 | 5.9 ± 0.3 | 0 |
| t44-retirada-adulto3-linea-nueva | 6 | 8 % ± 8 | 92 % ± 8 | 0.2 ± 0.2 | 3.0 ± 0.0 | 63 ± 12 | 497 ± 76 | 233 ± 14 | 17 % ± 2 | 14.3 ± 0.5 | 9.4 ± 1.0 | 0 |
| t44-retirada-adulto3-arco-pelear | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 46 ± 4 | 589 ± 69 | 238 ± 16 | 14 % ± 1 | 13.6 ± 1.2 | 6.9 ± 0.8 | 0 |
| t44-retirada-adulto3-arco-vieja | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 57 ± 7 | 509 ± 69 | 286 ± 16 | 19 % ± 2 | 14.6 ± 1.1 | 10.8 ± 1.2 | 1 |
| t44-retirada-adulto3-arco-nueva | 6 | 8 % ± 8 | 92 % ± 8 | 0.2 ± 0.2 | 3.0 ± 0.0 | 59 ± 13 | 533 ± 64 | 238 ± 12 | 20 % ± 1 | 14.4 ± 1.1 | 7.4 ± 0.9 | 0 |
| t44-retirada-adulto3-cab-pelear | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 30 ± 4 | 208 ± 19 | 231 ± 16 | 23 % ± 4 | 10.2 ± 1.3 | 5.0 ± 1.1 | 0 |
| t44-retirada-adulto3-cab-vieja | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 26 ± 2 | 63 ± 6 | 207 ± 16 | 38 % ± 11 | 11.5 ± 2.3 | 4.9 ± 0.4 | 0 |
| t44-retirada-adulto3-cab-nueva | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 30 ± 1 | 182 ± 26 | 207 ± 10 | 19 % ± 5 | 16.9 ± 2.6 | 8.5 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t44-retirada-adulto3-linea-pelear semilla 1: sprint → red (0/3 vivos), normal → red (0/3 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 2 de 2 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t44-retirada-adulto3-linea-pelear: enemy:dragon_fire=20 enemy:mob=23 env:inFire=2 env:onFire=3
- t44-retirada-adulto3-linea-vieja: enemy:dragon_fire=22 enemy:mob=22 env:inFire=3 env:onFire=1
- t44-retirada-adulto3-linea-nueva: enemy:dragon_fire=16 enemy:mob=28 env:fall=1 env:inFire=2
- t44-retirada-adulto3-arco-pelear: enemy:dragon_fire=22 enemy:mob=23 env:inFire=1 env:onFire=2
- t44-retirada-adulto3-arco-vieja: enemy:dragon_fire=20 enemy:mob=23 env:inFire=4 env:onFire=1
- t44-retirada-adulto3-arco-nueva: enemy:dragon_fire=24 enemy:mob=19 env:inFire=2 env:onFire=2
- t44-retirada-adulto3-cab-pelear: enemy:dragon_fire=25 enemy:mob=19 env:inFire=3 env:onFire=1
- t44-retirada-adulto3-cab-vieja: enemy:dragon_fire=14 enemy:mob=31 env:inFire=3
- t44-retirada-adulto3-cab-nueva: enemy:dragon_fire=17 enemy:mob=28 env:inFire=1 env:onFire=2

## Motivos de decisión (cambios registrados): ENGAGE 671, FORMATION_HOLD 142, INSTRUCTION 296, LOW_HEALTH_RETREAT 517, NONE 5, ORDER_OVERRIDE 7, OUT_OF_RANGE 7, SEEK_ENEMY 354, STUCK 3, TOO_CLOSE 282

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t44-retirada-adulto3-arco-vieja - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t44-retirada-adulto3-linea-pelear - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t44-retirada-adulto3-linea-pelear - 10 % de las bajas azules ({enemy:dragon_fire=20.0, enemy:mob=23.0, env:inFire=2.0, env:onFire=3.0})
- MUERTES QUE NO SON DEL ENEMIGO: t44-retirada-adulto3-arco-vieja - 10 % de las bajas azules ({enemy:dragon_fire=20.0, enemy:mob=23.0, env:inFire=4.0, env:onFire=1.0})
