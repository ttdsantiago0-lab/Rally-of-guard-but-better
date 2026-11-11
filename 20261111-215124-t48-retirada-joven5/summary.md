# Simulación Rally - lote 20261111-215124-t48-retirada-joven5

Preset `t48-retirada-joven5` · velocidad `sprint` · 275 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t48-retirada-joven5-linea-vieja | 30 | 17 % ± 4 | 83 % ± 4 | 0.6 ± 0.2 | 5.0 ± 0.0 | 89 ± 5 | 515 ± 22 | 237 ± 7 | 14 % ± 1 | 10.4 ± 0.3 | 7.4 ± 0.4 | 1 |
| t48-retirada-joven5-linea-nueva | 8 | 6 % ± 6 | 94 % ± 6 | 0.1 ± 0.1 | 5.0 ± 0.0 | 70 ± 8 | 519 ± 50 | 204 ± 8 | 18 % ± 2 | 13.6 ± 1.0 | 7.7 ± 0.5 | 46 |
| t48-retirada-joven5-cab-vieja | 29 | 7 % ± 3 | 93 % ± 3 | 0.4 ± 0.2 | 5.0 ± 0.0 | 68 ± 5 | 432 ± 32 | 278 ± 9 | 26 % ± 3 | 8.6 ± 0.3 | 7.2 ± 0.4 | 2 |
| t48-retirada-joven5-cab-nueva | 21 | 5 % ± 3 | 95 % ± 3 | 0.1 ± 0.1 | 5.0 ± 0.0 | 62 ± 5 | 508 ± 20 | 223 ± 9 | 22 % ± 2 | 15.7 ± 0.6 | 9.6 ± 0.5 | 26 |
| t48-retirada-joven5-cab-pelear | 14 | 7 % ± 5 | 93 % ± 5 | 0.4 ± 0.4 | 5.0 ± 0.0 | 60 ± 9 | 505 ± 30 | 239 ± 13 | 23 % ± 2 | 8.0 ± 0.4 | 6.7 ± 0.6 | 37 |
| t48-retirada-joven5-linea-pelear | 2 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 5.0 ± 0.0 | 52 ± 13 | 515 ± 84 | 184 ± 18 | 12 % ± 9 | 9.5 ± 0.6 | 6.6 ± 1.1 | 57 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t48-retirada-joven5-linea-pelear semilla 2001: sprint → draw (6/5 vivos), normal → draw (7/5 vivos)
- t48-retirada-joven5-linea-pelear semilla 9778: sprint → draw (8/5 vivos), normal → draw (6/5 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 19 de 19 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t48-retirada-joven5-linea-vieja: enemy:dragon_fire=118 enemy:mob=35 env:inFire=42 env:onFire=27
- t48-retirada-joven5-linea-nueva: enemy:dragon_fire=34 enemy:mob=9 env:inFire=9 env:onFire=11
- t48-retirada-joven5-cab-vieja: enemy:dragon_fire=102 enemy:mob=62 env:inFire=42 env:onFire=14
- t48-retirada-joven5-cab-nueva: enemy:dragon_fire=72 enemy:mob=60 env:inFire=30 env:onFire=4
- t48-retirada-joven5-cab-pelear: enemy:dragon_fire=56 enemy:mob=32 env:inFire=15 env:onFire=3
- t48-retirada-joven5-linea-pelear: enemy:dragon_fire=8 env:inFire=3 env:onFire=5

## Motivos de decisión (cambios registrados): ENGAGE 4400, FORMATION_HOLD 685, INSTRUCTION 1304, LOW_HEALTH_RETREAT 2597, NONE 22, OUT_OF_RANGE 16, SEEK_ENEMY 4074, STUCK 703, TOO_CLOSE 1397

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t48-retirada-joven5-linea-pelear - 57 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-retirada-joven5-cab-pelear - 37 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-retirada-joven5-cab-nueva - 26 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-retirada-joven5-linea-nueva - 46 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-retirada-joven5-linea-vieja - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t48-retirada-joven5-cab-vieja - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t48-retirada-joven5-linea-vieja - 31 % de las bajas azules ({enemy:dragon_fire=118.0, enemy:mob=35.0, env:inFire=42.0, env:onFire=27.0})
- MUERTES QUE NO SON DEL ENEMIGO: t48-retirada-joven5-linea-nueva - 32 % de las bajas azules ({enemy:dragon_fire=34.0, enemy:mob=9.0, env:inFire=9.0, env:onFire=11.0})
- MUERTES QUE NO SON DEL ENEMIGO: t48-retirada-joven5-cab-vieja - 25 % de las bajas azules ({enemy:dragon_fire=102.0, enemy:mob=62.0, env:inFire=42.0, env:onFire=14.0})
- MUERTES QUE NO SON DEL ENEMIGO: t48-retirada-joven5-cab-nueva - 20 % de las bajas azules ({enemy:dragon_fire=72.0, enemy:mob=60.0, env:inFire=30.0, env:onFire=4.0})
- MUERTES QUE NO SON DEL ENEMIGO: t48-retirada-joven5-cab-pelear - 17 % de las bajas azules ({enemy:dragon_fire=56.0, enemy:mob=32.0, env:inFire=15.0, env:onFire=3.0})
- MUERTES QUE NO SON DEL ENEMIGO: t48-retirada-joven5-linea-pelear - 50 % de las bajas azules ({enemy:dragon_fire=8.0, env:inFire=3.0, env:onFire=5.0})
