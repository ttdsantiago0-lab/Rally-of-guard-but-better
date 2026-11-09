# Simulación Rally - lote 20261109-190948-t32-tactica-harbinger

Preset `t32-tactica-harbinger` · velocidad `sprint` · 52 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t32-tactica-harbinger-linea | 8 | 100 % ± 0 | 0 % ± 0 | 15.9 ± 0.1 | 0.0 ± 0.0 | 117 ± 22 | 625 ± 43 | 131 ± 16 | 93 % ± 1 | 23.2 ± 2.3 | 17.3 ± 1.4 | 0 |
| t32-tactica-harbinger-arcos | 8 | 19 % ± 9 | 81 % ± 9 | 0.5 ± 0.3 | 1.0 ± 0.0 | 275 ± 10 | 719 ± 13 | 825 ± 17 | 91 % ± 1 | 28.0 ± 1.4 | 21.0 ± 1.1 | 0 |
| t32-tactica-harbinger-escudos | 8 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 92 ± 5 | 571 ± 11 | 105 ± 16 | 0 % ± 0 | 17.8 ± 0.8 | 13.5 ± 0.9 | 0 |
| t32-tactica-harbinger-espadas | 8 | 81 % ± 13 | 19 % ± 13 | 10.8 ± 2.3 | 0.3 ± 0.2 | 141 ± 36 | 590 ± 28 | 508 ± 84 | 0 % ± 0 | 24.2 ± 2.7 | 19.9 ± 2.1 | 1 |
| t32-tactica-harbinger-volea60 | 8 | 100 % ± 0 | 0 % ± 0 | 15.6 ± 0.3 | 0.0 ± 0.0 | 107 ± 16 | 599 ± 32 | 146 ± 27 | 84 % ± 1 | 22.5 ± 0.8 | 16.4 ± 0.7 | 1 |
| t32-tactica-harbinger-tortuga | 8 | 94 % ± 6 | 6 % ± 6 | 15.1 ± 0.4 | 0.1 ± 0.1 | 167 ± 30 | 699 ± 48 | 185 ± 22 | 90 % ± 1 | 24.3 ± 2.4 | 17.3 ± 1.7 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t32-tactica-harbinger-linea semilla 1: sprint → blue (16/0 vivos), normal → draw (16/1 vivos)
- t32-tactica-harbinger-linea semilla 7778: sprint → ? (0/0 vivos), normal → blue (15/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 42 de 42 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t32-tactica-harbinger-linea: env:wither=1
- t32-tactica-harbinger-arcos: enemy:cataclysm.deathlaser=29 enemy:cataclysm.laser=23 enemy:explosion.player=1 enemy:indirectMagic=6 enemy:mob=39 env:inFire=7 env:onFire=5 env:wither=14
- t32-tactica-harbinger-espadas: enemy:cataclysm.deathlaser=8 enemy:cataclysm.laser=6 enemy:explosion.player=2 enemy:indirectMagic=1 enemy:mob=13 env:inFire=3 env:onFire=3 env:wither=6
- t32-tactica-harbinger-volea60: enemy:mob=1 env:onFire=1 env:wither=1
- t32-tactica-harbinger-tortuga: enemy:cataclysm.deathlaser=2 enemy:explosion.player=2 enemy:indirectMagic=1 enemy:mob=1 env:inFire=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 90, ENGAGE 2754, FORMATION_HOLD 73, LOW_HEALTH_RETREAT 158, MORALE_SHAKEN 1589, MORALE_WAVERING 945, NONE 677, OUT_OF_RANGE 151, RALLIED 141, REFUSE_ATTACK 107, ROUTED_FLEE 1143, SEEK_ENEMY 244, STUCK 438, TOO_CLOSE 146, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t32-tactica-harbinger-volea60 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t32-tactica-harbinger-espadas - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t32-tactica-harbinger-tortuga: pico de Rally 23 ms en un tick (media de picos)
- t32-tactica-harbinger-linea: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
- MUERTES QUE NO SON DEL ENEMIGO: t32-tactica-harbinger-linea - 100 % de las bajas azules ({env:wither=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t32-tactica-harbinger-arcos - 21 % de las bajas azules ({enemy:cataclysm.deathlaser=29.0, enemy:cataclysm.laser=23.0, enemy:explosion.player=1.0, enemy:indirectMagic=6.0, enemy:mob=39.0, env:inFire=7.0, env:onFire=5.0, env:wither=14.0})
- MUERTES QUE NO SON DEL ENEMIGO: t32-tactica-harbinger-espadas - 29 % de las bajas azules ({enemy:cataclysm.deathlaser=8.0, enemy:cataclysm.laser=6.0, enemy:explosion.player=2.0, enemy:indirectMagic=1.0, enemy:mob=13.0, env:inFire=3.0, env:onFire=3.0, env:wither=6.0})
- MUERTES QUE NO SON DEL ENEMIGO: t32-tactica-harbinger-volea60 - 67 % de las bajas azules ({enemy:mob=1.0, env:onFire=1.0, env:wither=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t32-tactica-harbinger-tortuga - 14 % de las bajas azules ({enemy:cataclysm.deathlaser=2.0, enemy:explosion.player=2.0, enemy:indirectMagic=1.0, enemy:mob=1.0, env:inFire=1.0})
