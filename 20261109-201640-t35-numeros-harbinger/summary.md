# Simulación Rally - lote 20261109-201640-t35-numeros-harbinger

Preset `t35-numeros-harbinger` · velocidad `sprint` · 36 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t35-numeros-harbinger-hierro-g4 | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 50 ± 4 | 103 ± 28 | 108 ± 14 | 71 % ± 18 | 10.8 ± 2.6 | 5.5 ± 2.4 | 0 |
| t35-numeros-harbinger-netheritamas-g4 | 5 | 100 % ± 0 | 0 % ± 0 | 3.6 ± 0.4 | 0.0 ± 0.0 | 89 ± 7 | 566 ± 14 | 100 ± 18 | 52 % ± 21 | 18.3 ± 1.4 | 14.7 ± 1.7 | 1 |
| t35-numeros-harbinger-hierro-g8 | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 94 ± 10 | 295 ± 27 | 252 ± 8 | 90 % ± 2 | 17.7 ± 0.7 | 11.9 ± 0.5 | 0 |
| t35-numeros-harbinger-netheritamas-g8 | 5 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 65 ± 3 | 522 ± 8 | 82 ± 11 | 90 % ± 2 | 22.1 ± 2.5 | 12.9 ± 1.2 | 2 |
| t35-numeros-harbinger-hierro-g16 | 5 | 10 % ± 10 | 90 % ± 10 | 0.2 ± 0.2 | 1.0 ± 0.0 | 195 ± 18 | 441 ± 25 | 544 ± 19 | 89 % ± 1 | 24.4 ± 1.4 | 17.3 ± 2.2 | 0 |
| t35-numeros-harbinger-netheritamas-g16 | 5 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 61 ± 3 | 513 ± 5 | 111 ± 23 | 89 % ± 3 | 21.6 ± 2.5 | 15.3 ± 1.4 | 2 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t35-numeros-harbinger-hierro-g4 semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 16 de 16 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t35-numeros-harbinger-hierro-g4: enemy:cataclysm.deathlaser=2 enemy:cataclysm.laser=2 enemy:explosion.player=1 enemy:indirectMagic=3 enemy:mob=8 env:inFire=2 env:onFire=1 env:wither=1
- t35-numeros-harbinger-netheritamas-g4: enemy:cataclysm.deathlaser=1 env:wither=1
- t35-numeros-harbinger-hierro-g8: enemy:cataclysm.deathlaser=12 enemy:cataclysm.laser=3 enemy:explosion.player=1 enemy:indirectMagic=5 enemy:mob=14 env:onFire=1 env:wither=4
- t35-numeros-harbinger-netheritamas-g8: enemy:mob=1
- t35-numeros-harbinger-hierro-g16: enemy:cataclysm.deathlaser=24 enemy:cataclysm.laser=7 enemy:explosion.player=7 enemy:indirectMagic=2 enemy:mob=28 env:inFire=4 env:onFire=2 env:wither=5

## Motivos de decisión (cambios registrados): ENGAGE 661, FORMATION_HOLD 3, LOW_HEALTH_RETREAT 240, NONE 189, OUT_OF_RANGE 116, SEEK_ENEMY 96, STUCK 192, TOO_CLOSE 160

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t35-numeros-harbinger-netheritamas-g4 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t35-numeros-harbinger-netheritamas-g16 - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t35-numeros-harbinger-netheritamas-g8 - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t35-numeros-harbinger-hierro-g4 - 20 % de las bajas azules ({enemy:cataclysm.deathlaser=2.0, enemy:cataclysm.laser=2.0, enemy:explosion.player=1.0, enemy:indirectMagic=3.0, enemy:mob=8.0, env:inFire=2.0, env:onFire=1.0, env:wither=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t35-numeros-harbinger-netheritamas-g4 - 50 % de las bajas azules ({enemy:cataclysm.deathlaser=1.0, env:wither=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t35-numeros-harbinger-hierro-g8 - 13 % de las bajas azules ({enemy:cataclysm.deathlaser=12.0, enemy:cataclysm.laser=3.0, enemy:explosion.player=1.0, enemy:indirectMagic=5.0, enemy:mob=14.0, env:onFire=1.0, env:wither=4.0})
- MUERTES QUE NO SON DEL ENEMIGO: t35-numeros-harbinger-hierro-g16 - 14 % de las bajas azules ({enemy:cataclysm.deathlaser=24.0, enemy:cataclysm.laser=7.0, enemy:explosion.player=7.0, enemy:indirectMagic=2.0, enemy:mob=28.0, env:inFire=4.0, env:onFire=2.0, env:wither=5.0})
