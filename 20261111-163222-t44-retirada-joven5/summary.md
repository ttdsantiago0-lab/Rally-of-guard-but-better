# Simulación Rally - lote 20261111-163222-t44-retirada-joven5

Preset `t44-retirada-joven5` · velocidad `sprint` · 91 corridas · 7 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t44-retirada-joven5-linea-vieja | 6 | 17 % ± 11 | 83 % ± 11 | 0.8 ± 0.5 | 5.0 ± 0.0 | 85 ± 12 | 549 ± 51 | 210 ± 17 | 13 % ± 2 | 10.2 ± 0.7 | 7.3 ± 0.9 | 0 |
| t44-retirada-joven5-arco-vieja | 1 | 50 % ± 0 | 50 % ± 0 | 7.0 ± 0.0 | 5.0 ± 0.0 | 120 ± 0 | 698 ± 0 | 159 ± 0 | 13 % ± 0 | 8.8 ± 0.0 | 7.8 ± 0.0 | 11 |
| t44-retirada-joven5-cab-pelear | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 5.0 ± 0.0 | 67 ± 11 | 568 ± 34 | 240 ± 22 | 24 % ± 1 | 7.8 ± 0.6 | 6.1 ± 0.9 | 4 |
| t44-retirada-joven5-cab-vieja | 6 | 17 % ± 11 | 83 % ± 11 | 0.5 ± 0.3 | 5.0 ± 0.0 | 79 ± 13 | 475 ± 69 | 282 ± 19 | 16 % ± 3 | 9.5 ± 0.6 | 6.4 ± 1.1 | 0 |
| t44-retirada-joven5-linea-pelear | 2 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 5.0 ± 0.0 | 59 ± 20 | 620 ± 50 | 193 ± 6 | 17 % ± 1 | 10.0 ± 0.5 | 8.0 ± 1.9 | 10 |
| t44-retirada-joven5-linea-nueva | 1 | 50 % ± 0 | 50 % ± 0 | 7.0 ± 0.0 | 5.0 ± 0.0 | 120 ± 0 | 676 ± 0 | 80 ± 0 | 2 % ± 0 | 13.0 ± 0.0 | 13.1 ± 0.0 | 11 |
| t44-retirada-joven5-cab-nueva | 4 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 5.0 ± 0.0 | 69 ± 11 | 510 ± 56 | 217 ± 14 | 13 % ± 1 | 19.2 ± 1.6 | 10.0 ± 0.6 | 4 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t44-retirada-joven5-linea-pelear semilla 1: sprint → draw (7/5 vivos), normal → draw (7/5 vivos)
- t44-retirada-joven5-linea-pelear semilla 7778: sprint → draw (7/5 vivos), normal → draw (2/5 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 6 de 6 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t44-retirada-joven5-linea-vieja: enemy:dragon_fire=18 enemy:mob=8 env:inFire=12 env:onFire=5
- t44-retirada-joven5-arco-vieja: enemy:dragon_fire=1
- t44-retirada-joven5-cab-pelear: enemy:dragon_fire=20 enemy:mob=8 env:inFire=8 env:onFire=4
- t44-retirada-joven5-cab-vieja: enemy:dragon_fire=25 enemy:mob=12 env:inFire=5 env:onFire=3
- t44-retirada-joven5-linea-pelear: enemy:dragon_fire=11 enemy:mob=1 env:inFire=3 env:onFire=1
- t44-retirada-joven5-linea-nueva: env:onFire=1
- t44-retirada-joven5-cab-nueva: enemy:dragon_fire=11 enemy:mob=10 env:inFire=6 env:onFire=5

## Motivos de decisión (cambios registrados): ENGAGE 1485, FORMATION_HOLD 212, INSTRUCTION 448, LOW_HEALTH_RETREAT 756, NONE 2, ORDER_OVERRIDE 12, OUT_OF_RANGE 15, SEEK_ENEMY 1292, STUCK 188, TOO_CLOSE 531

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t44-retirada-joven5-linea-pelear - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t44-retirada-joven5-linea-nueva - 11 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t44-retirada-joven5-arco-pelear - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t44-retirada-joven5-arco-vieja - 11 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t44-retirada-joven5-arco-nueva - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t44-retirada-joven5-cab-nueva - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t44-retirada-joven5-cab-pelear - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t44-retirada-joven5-arco-vieja: 1/1 corridas llegaron al tope de tiempo
- t44-retirada-joven5-linea-nueva: 1/1 corridas llegaron al tope de tiempo
- MUERTES QUE NO SON DEL ENEMIGO: t44-retirada-joven5-linea-vieja - 40 % de las bajas azules ({enemy:dragon_fire=18.0, enemy:mob=8.0, env:inFire=12.0, env:onFire=5.0})
- MUERTES QUE NO SON DEL ENEMIGO: t44-retirada-joven5-cab-pelear - 30 % de las bajas azules ({enemy:dragon_fire=20.0, enemy:mob=8.0, env:inFire=8.0, env:onFire=4.0})
- MUERTES QUE NO SON DEL ENEMIGO: t44-retirada-joven5-cab-vieja - 18 % de las bajas azules ({enemy:dragon_fire=25.0, enemy:mob=12.0, env:inFire=5.0, env:onFire=3.0})
- MUERTES QUE NO SON DEL ENEMIGO: t44-retirada-joven5-linea-pelear - 25 % de las bajas azules ({enemy:dragon_fire=11.0, enemy:mob=1.0, env:inFire=3.0, env:onFire=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t44-retirada-joven5-linea-nueva - 100 % de las bajas azules ({env:onFire=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t44-retirada-joven5-cab-nueva - 34 % de las bajas azules ({enemy:dragon_fire=11.0, enemy:mob=10.0, env:inFire=6.0, env:onFire=5.0})
