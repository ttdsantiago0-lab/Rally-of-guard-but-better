# Simulación Rally - lote 20261108-202908-t28-jefe-harbinger

Preset `t28-jefe-harbinger` · velocidad `sprint` · 19 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t28-jefe-harbinger-g16 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 210 ± 7 | 464 ± 9 | 609 ± 29 | 88 % ± 2 | 24.6 ± 2.3 | 16.6 ± 2.1 | 0 |
| t28-jefe-harbinger-g32 | 6 | 50 % ± 0 | 50 % ± 0 | 7.3 ± 1.8 | 1.0 ± 0.0 | 300 ± 0 | 671 ± 23 | 1035 ± 42 | 89 % ± 1 | 28.2 ± 1.7 | 22.1 ± 1.4 | 0 |
| t28-jefe-harbinger-g64 | 6 | 50 % ± 0 | 50 % ± 0 | 39.2 ± 1.3 | 1.0 ± 0.0 | 300 ± 0 | 848 ± 8 | 1374 ± 91 | 89 % ± 1 | 30.7 ± 1.8 | 23.2 ± 1.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t28-jefe-harbinger-g16 semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 12 de 12 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t28-jefe-harbinger-g16: enemy:cataclysm.deathlaser=26 enemy:cataclysm.laser=11 enemy:explosion.player=3 enemy:indirectMagic=3 enemy:mob=41 env:inFire=5 env:onFire=4 env:wither=3
- t28-jefe-harbinger-g32: enemy:cataclysm.deathlaser=37 enemy:cataclysm.laser=10 enemy:explosion.player=8 enemy:indirectMagic=13 enemy:mob=53 env:inFire=5 env:onFire=12 env:wither=10
- t28-jefe-harbinger-g64: enemy:cataclysm.deathlaser=43 enemy:cataclysm.laser=16 enemy:explosion.player=5 enemy:indirectMagic=15 enemy:mob=43 env:inFire=7 env:onFire=9 env:wither=11

## Motivos de decisión (cambios registrados): COUNTERATTACK 106, ENGAGE 2109, FORMATION_HOLD 10, LOW_HEALTH_RETREAT 121, MORALE_SHAKEN 1176, MORALE_WAVERING 831, NONE 862, OUT_OF_RANGE 228, RALLIED 70, REFUSE_ATTACK 55, ROUTED_FLEE 1335, SEEK_ENEMY 287, STUCK 658, TOO_CLOSE 72, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t28-jefe-harbinger-g32: 6/6 corridas llegaron al tope de tiempo
- t28-jefe-harbinger-g64: 6/6 corridas llegaron al tope de tiempo
- MUERTES QUE NO SON DEL ENEMIGO: t28-jefe-harbinger-g16 - 13 % de las bajas azules ({enemy:cataclysm.deathlaser=26.0, enemy:cataclysm.laser=11.0, enemy:explosion.player=3.0, enemy:indirectMagic=3.0, enemy:mob=41.0, env:inFire=5.0, env:onFire=4.0, env:wither=3.0})
- MUERTES QUE NO SON DEL ENEMIGO: t28-jefe-harbinger-g32 - 18 % de las bajas azules ({enemy:cataclysm.deathlaser=37.0, enemy:cataclysm.laser=10.0, enemy:explosion.player=8.0, enemy:indirectMagic=13.0, enemy:mob=53.0, env:inFire=5.0, env:onFire=12.0, env:wither=10.0})
- MUERTES QUE NO SON DEL ENEMIGO: t28-jefe-harbinger-g64 - 18 % de las bajas azules ({enemy:cataclysm.deathlaser=43.0, enemy:cataclysm.laser=16.0, enemy:explosion.player=5.0, enemy:indirectMagic=15.0, enemy:mob=43.0, env:inFire=7.0, env:onFire=9.0, env:wither=11.0})
