# Simulación Rally - lote 20261111-163312-t44-retirada-joven3

Preset `t44-retirada-joven3` · velocidad `sprint` · 101 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t44-retirada-joven3-linea-vieja | 6 | 50 % ± 0 | 50 % ± 0 | 2.7 ± 0.6 | 3.0 ± 0.0 | 120 ± 0 | 402 ± 6 | 220 ± 9 | 14 % ± 5 | 11.9 ± 1.3 | 7.3 ± 1.0 | 0 |
| t44-retirada-joven3-cab-vieja | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 52 ± 7 | 269 ± 35 | 229 ± 21 | 42 % ± 7 | 9.3 ± 0.9 | 6.6 ± 0.9 | 4 |
| t44-retirada-joven3-cab-nueva | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 3.0 ± 0.0 | 93 ± 0 | 387 ± 0 | 265 ± 0 | 23 % ± 0 | 16.4 ± 0.0 | 9.7 ± 0.0 | 11 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t44-retirada-joven3-linea-pelear semilla 1: sprint → draw (7/3 vivos), normal → draw (8/3 vivos)
- t44-retirada-joven3-linea-pelear semilla 7778: sprint → draw (8/3 vivos), normal → draw (8/3 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 6 de 6 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t44-retirada-joven3-linea-vieja: enemy:dragon_fire=12 enemy:mob=7 env:inFire=3 env:onFire=10
- t44-retirada-joven3-cab-vieja: enemy:dragon_fire=21 enemy:mob=13 env:inFire=5 env:onFire=1
- t44-retirada-joven3-cab-nueva: enemy:dragon_fire=6 enemy:mob=1 env:inFire=1

## Motivos de decisión (cambios registrados): ENGAGE 1167, FORMATION_HOLD 344, INSTRUCTION 504, LOW_HEALTH_RETREAT 374, NONE 36, ORDER_OVERRIDE 12, OUT_OF_RANGE 3, SEEK_ENEMY 1174, STUCK 114, TOO_CLOSE 278

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t44-retirada-joven3-linea-pelear - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t44-retirada-joven3-linea-nueva - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t44-retirada-joven3-arco-pelear - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t44-retirada-joven3-arco-vieja - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t44-retirada-joven3-arco-nueva - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t44-retirada-joven3-cab-pelear - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t44-retirada-joven3-cab-nueva - 11 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t44-retirada-joven3-cab-vieja - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t44-retirada-joven3-linea-vieja: 6/6 corridas llegaron al tope de tiempo
- MUERTES QUE NO SON DEL ENEMIGO: t44-retirada-joven3-linea-vieja - 41 % de las bajas azules ({enemy:dragon_fire=12.0, enemy:mob=7.0, env:inFire=3.0, env:onFire=10.0})
- MUERTES QUE NO SON DEL ENEMIGO: t44-retirada-joven3-cab-vieja - 15 % de las bajas azules ({enemy:dragon_fire=21.0, enemy:mob=13.0, env:inFire=5.0, env:onFire=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t44-retirada-joven3-cab-nueva - 13 % de las bajas azules ({enemy:dragon_fire=6.0, enemy:mob=1.0, env:inFire=1.0})
