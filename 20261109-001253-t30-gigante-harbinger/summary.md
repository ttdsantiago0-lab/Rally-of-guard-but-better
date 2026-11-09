# Simulación Rally - lote 20261109-001253-t30-gigante-harbinger

Preset `t30-gigante-harbinger` · velocidad `sprint` · 25 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t30-gigante-harbinger-hierro | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 180 ± 13 | 450 ± 27 | 572 ± 30 | 91 % ± 1 | 19.0 ± 1.5 | 13.1 ± 0.4 | 0 |
| t30-gigante-harbinger-diamante | 6 | 33 % ± 11 | 67 % ± 11 | 1.2 ± 0.4 | 1.0 ± 0.0 | 289 ± 11 | 543 ± 29 | 596 ± 26 | 90 % ± 2 | 29.1 ± 1.7 | 23.4 ± 2.1 | 0 |
| t30-gigante-harbinger-netherita | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 255 ± 12 | 617 ± 39 | 619 ± 13 | 90 % ± 1 | 27.3 ± 1.5 | 19.8 ± 1.0 | 0 |
| t30-gigante-harbinger-netheritamas | 6 | 100 % ± 0 | 0 % ± 0 | 15.5 ± 0.3 | 0.0 ± 0.0 | 103 ± 26 | 596 ± 53 | 146 ± 34 | 93 % ± 1 | 21.7 ± 2.9 | 14.9 ± 1.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t30-gigante-harbinger-hierro semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 10 de 10 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t30-gigante-harbinger-hierro: enemy:cataclysm.deathlaser=31 enemy:cataclysm.laser=9 enemy:explosion.player=2 enemy:indirectMagic=3 enemy:mob=34 env:inFire=5 env:onFire=5 env:wither=7
- t30-gigante-harbinger-diamante: enemy:cataclysm.deathlaser=41 enemy:cataclysm.laser=10 enemy:explosion.player=1 enemy:indirectMagic=2 enemy:mob=23 env:inFire=3 env:onFire=1 env:wither=8
- t30-gigante-harbinger-netherita: enemy:cataclysm.deathlaser=39 enemy:cataclysm.laser=9 enemy:explosion.player=2 enemy:indirectMagic=8 enemy:mob=21 env:inFire=7 env:onFire=5 env:wither=5
- t30-gigante-harbinger-netheritamas: enemy:cataclysm.laser=1 enemy:mob=1 env:inFire=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 98, ENGAGE 1300, FORMATION_HOLD 4, LOW_HEALTH_RETREAT 94, MORALE_SHAKEN 782, MORALE_WAVERING 497, NONE 466, OUT_OF_RANGE 109, RALLIED 79, REFUSE_ATTACK 13, ROUTED_FLEE 623, SEEK_ENEMY 13, STUCK 181, TOO_CLOSE 54, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t30-gigante-harbinger-diamante: 4/6 corridas llegaron al tope de tiempo
- MUERTES QUE NO SON DEL ENEMIGO: t30-gigante-harbinger-hierro - 18 % de las bajas azules ({enemy:cataclysm.deathlaser=31.0, enemy:cataclysm.laser=9.0, enemy:explosion.player=2.0, enemy:indirectMagic=3.0, enemy:mob=34.0, env:inFire=5.0, env:onFire=5.0, env:wither=7.0})
- MUERTES QUE NO SON DEL ENEMIGO: t30-gigante-harbinger-diamante - 13 % de las bajas azules ({enemy:cataclysm.deathlaser=41.0, enemy:cataclysm.laser=10.0, enemy:explosion.player=1.0, enemy:indirectMagic=2.0, enemy:mob=23.0, env:inFire=3.0, env:onFire=1.0, env:wither=8.0})
- MUERTES QUE NO SON DEL ENEMIGO: t30-gigante-harbinger-netherita - 18 % de las bajas azules ({enemy:cataclysm.deathlaser=39.0, enemy:cataclysm.laser=9.0, enemy:explosion.player=2.0, enemy:indirectMagic=8.0, enemy:mob=21.0, env:inFire=7.0, env:onFire=5.0, env:wither=5.0})
- MUERTES QUE NO SON DEL ENEMIGO: t30-gigante-harbinger-netheritamas - 33 % de las bajas azules ({enemy:cataclysm.laser=1.0, enemy:mob=1.0, env:inFire=1.0})
