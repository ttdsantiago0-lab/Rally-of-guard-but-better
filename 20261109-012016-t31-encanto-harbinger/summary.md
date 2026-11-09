# Simulación Rally - lote 20261109-012016-t31-encanto-harbinger

Preset `t31-encanto-harbinger` · velocidad `sprint` · 41 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t31-encanto-harbinger-hierro | 8 | 6 % ± 6 | 94 % ± 6 | 0.3 ± 0.3 | 1.0 ± 0.0 | 220 ± 19 | 453 ± 20 | 585 ± 18 | 90 % ± 1 | 25.5 ± 2.1 | 17.7 ± 1.6 | 0 |
| t31-encanto-harbinger-poder | 8 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 178 ± 10 | 488 ± 25 | 551 ± 18 | 91 % ± 1 | 20.7 ± 1.4 | 14.6 ± 1.4 | 0 |
| t31-encanto-harbinger-filo | 8 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 200 ± 10 | 521 ± 15 | 567 ± 16 | 92 % ± 1 | 19.3 ± 1.0 | 13.6 ± 0.9 | 0 |
| t31-encanto-harbinger-proteccion | 8 | 50 % ± 0 | 50 % ± 0 | 11.5 ± 0.7 | 1.0 ± 0.0 | 300 ± 0 | 797 ± 30 | 456 ± 17 | 91 % ± 1 | 29.4 ± 1.9 | 23.3 ± 1.1 | 0 |
| t31-encanto-harbinger-netheritamas | 8 | 88 % ± 8 | 13 % ± 8 | 14.4 ± 0.6 | 0.3 ± 0.2 | 159 ± 34 | 692 ± 55 | 196 ± 28 | 90 % ± 1 | 24.9 ± 3.4 | 17.9 ± 1.9 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t31-encanto-harbinger-hierro semilla 101: sprint → draw (2/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 17 de 17 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t31-encanto-harbinger-hierro: enemy:cataclysm.deathlaser=37 enemy:cataclysm.laser=20 enemy:explosion.player=6 enemy:indirectMagic=8 enemy:mob=41 env:inFire=3 env:onFire=1 env:wither=10
- t31-encanto-harbinger-poder: enemy:cataclysm.deathlaser=33 enemy:cataclysm.laser=6 enemy:explosion.player=4 enemy:indirectMagic=11 enemy:mob=54 env:inFire=4 env:onFire=4 env:wither=12
- t31-encanto-harbinger-filo: enemy:cataclysm.deathlaser=38 enemy:cataclysm.laser=11 enemy:explosion.player=6 enemy:indirectMagic=7 enemy:mob=48 env:inFire=4 env:onFire=7 env:wither=7
- t31-encanto-harbinger-proteccion: enemy:cataclysm.deathlaser=7 enemy:cataclysm.laser=4 enemy:explosion.player=1 enemy:indirectMagic=4 enemy:mob=12 env:onFire=1 env:wither=7
- t31-encanto-harbinger-netheritamas: enemy:cataclysm.deathlaser=1 enemy:cataclysm.laser=2 enemy:mob=7 env:onFire=2 env:wither=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 146, ENGAGE 2051, FORMATION_HOLD 3, LOW_HEALTH_RETREAT 185, MORALE_SHAKEN 1216, MORALE_WAVERING 834, NONE 667, OUT_OF_RANGE 93, RALLIED 134, REFUSE_ATTACK 53, ROUTED_FLEE 1095, SEEK_ENEMY 28, STUCK 409, TOO_CLOSE 121, UNIT_BROKEN 8

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t31-encanto-harbinger-proteccion: 8/8 corridas llegaron al tope de tiempo
- t31-encanto-harbinger-hierro: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
- MUERTES QUE NO SON DEL ENEMIGO: t31-encanto-harbinger-hierro - 11 % de las bajas azules ({enemy:cataclysm.deathlaser=37.0, enemy:cataclysm.laser=20.0, enemy:explosion.player=6.0, enemy:indirectMagic=8.0, enemy:mob=41.0, env:inFire=3.0, env:onFire=1.0, env:wither=10.0})
- MUERTES QUE NO SON DEL ENEMIGO: t31-encanto-harbinger-poder - 16 % de las bajas azules ({enemy:cataclysm.deathlaser=33.0, enemy:cataclysm.laser=6.0, enemy:explosion.player=4.0, enemy:indirectMagic=11.0, enemy:mob=54.0, env:inFire=4.0, env:onFire=4.0, env:wither=12.0})
- MUERTES QUE NO SON DEL ENEMIGO: t31-encanto-harbinger-filo - 14 % de las bajas azules ({enemy:cataclysm.deathlaser=38.0, enemy:cataclysm.laser=11.0, enemy:explosion.player=6.0, enemy:indirectMagic=7.0, enemy:mob=48.0, env:inFire=4.0, env:onFire=7.0, env:wither=7.0})
- MUERTES QUE NO SON DEL ENEMIGO: t31-encanto-harbinger-proteccion - 22 % de las bajas azules ({enemy:cataclysm.deathlaser=7.0, enemy:cataclysm.laser=4.0, enemy:explosion.player=1.0, enemy:indirectMagic=4.0, enemy:mob=12.0, env:onFire=1.0, env:wither=7.0})
- MUERTES QUE NO SON DEL ENEMIGO: t31-encanto-harbinger-netheritamas - 23 % de las bajas azules ({enemy:cataclysm.deathlaser=1.0, enemy:cataclysm.laser=2.0, enemy:mob=7.0, env:onFire=2.0, env:wither=1.0})
