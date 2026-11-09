# Simulación Rally - lote 20261109-012000-t31-encanto-monstruosidad

Preset `t31-encanto-monstruosidad` · velocidad `sprint` · 44 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t31-encanto-monstruosidad-hierro | 8 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 135 ± 14 | 270 ± 19 | 550 ± 24 | 46 % ± 4 | 23.9 ± 1.2 | 13.8 ± 1.2 | 1 |
| t31-encanto-monstruosidad-poder | 8 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 162 ± 16 | 415 ± 45 | 600 ± 28 | 42 % ± 5 | 25.8 ± 2.0 | 15.8 ± 1.7 | 0 |
| t31-encanto-monstruosidad-filo | 8 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 139 ± 21 | 306 ± 29 | 572 ± 31 | 38 % ± 3 | 23.9 ± 1.9 | 13.3 ± 2.3 | 0 |
| t31-encanto-monstruosidad-proteccion | 7 | 50 % ± 11 | 50 % ± 11 | 5.9 ± 1.6 | 0.9 ± 0.1 | 269 ± 24 | 487 ± 46 | 637 ± 44 | 51 % ± 5 | 29.8 ± 2.1 | 22.3 ± 2.1 | 3 |
| t31-encanto-monstruosidad-netheritamas | 8 | 100 % ± 0 | 0 % ± 0 | 14.6 ± 0.5 | 0.0 ± 0.0 | 62 ± 4 | 607 ± 2 | 228 ± 21 | 50 % ± 6 | 20.8 ± 1.1 | 14.3 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t31-encanto-monstruosidad-hierro semilla 101: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 14 de 14 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t31-encanto-monstruosidad-hierro: enemy:explosion.player=4 enemy:mob=107 env:lava=15 env:onFire=2
- t31-encanto-monstruosidad-poder: enemy:explosion.player=3 enemy:mob=100 env:lava=19 env:onFire=6
- t31-encanto-monstruosidad-filo: enemy:explosion.player=8 enemy:mob=106 env:lava=8 env:onFire=6
- t31-encanto-monstruosidad-proteccion: enemy:explosion.player=1 enemy:mob=51 env:fall=1 env:lava=16 env:onFire=2
- t31-encanto-monstruosidad-netheritamas: enemy:mob=10 env:lava=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 56, ENGAGE 1384, LOW_HEALTH_RETREAT 328, MORALE_SHAKEN 850, MORALE_WAVERING 578, NONE 483, OUT_OF_RANGE 2, RALLIED 47, REFUSE_ATTACK 40, ROUTED_FLEE 454, SEEK_ENEMY 60, STUCK 10, TOO_CLOSE 21, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t31-encanto-monstruosidad-proteccion - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t31-encanto-monstruosidad-hierro - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t31-encanto-monstruosidad-proteccion: 5/7 corridas llegaron al tope de tiempo
- MUERTES QUE NO SON DEL ENEMIGO: t31-encanto-monstruosidad-hierro - 13 % de las bajas azules ({enemy:explosion.player=4.0, enemy:mob=107.0, env:lava=15.0, env:onFire=2.0})
- MUERTES QUE NO SON DEL ENEMIGO: t31-encanto-monstruosidad-poder - 20 % de las bajas azules ({enemy:explosion.player=3.0, enemy:mob=100.0, env:lava=19.0, env:onFire=6.0})
- MUERTES QUE NO SON DEL ENEMIGO: t31-encanto-monstruosidad-filo - 11 % de las bajas azules ({enemy:explosion.player=8.0, enemy:mob=106.0, env:lava=8.0, env:onFire=6.0})
- MUERTES QUE NO SON DEL ENEMIGO: t31-encanto-monstruosidad-proteccion - 27 % de las bajas azules ({enemy:explosion.player=1.0, enemy:mob=51.0, env:fall=1.0, env:lava=16.0, env:onFire=2.0})
