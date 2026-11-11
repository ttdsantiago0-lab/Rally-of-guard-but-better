# Simulación Rally - lote 20261111-080351-t42-numeros-harbinger

Preset `t42-numeros-harbinger` · velocidad `sprint` · 34 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t42-numeros-harbinger-hierro-g4-sincro | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 40 ± 3 | 167 ± 22 | 116 ± 10 | 94 % ± 3 | 10.1 ± 0.8 | 7.2 ± 1.3 | 0 |
| t42-numeros-harbinger-netheritamas-g4-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 4.0 ± 0.0 | 0.0 ± 0.0 | 64 ± 2 | 520 ± 3 | 64 ± 6 | 91 % ± 1 | 16.9 ± 1.2 | 14.0 ± 0.7 | 0 |
| t42-numeros-harbinger-hierro-g8-sincro | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 75 ± 5 | 357 ± 15 | 257 ± 16 | 87 % ± 3 | 17.4 ± 0.8 | 12.1 ± 1.1 | 0 |
| t42-numeros-harbinger-netheritamas-g8-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.2 | 0.0 ± 0.0 | 69 ± 6 | 530 ± 13 | 96 ± 17 | 88 % ± 3 | 17.6 ± 1.8 | 12.2 ± 1.0 | 0 |
| t42-numeros-harbinger-hierro-g16-sincro | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 130 ± 12 | 475 ± 24 | 495 ± 44 | 83 % ± 1 | 20.7 ± 0.8 | 13.9 ± 0.4 | 2 |
| t42-numeros-harbinger-netheritamas-g16-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 58 ± 1 | 508 ± 1 | 87 ± 14 | 89 % ± 1 | 18.5 ± 1.6 | 13.7 ± 1.0 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t42-numeros-harbinger-hierro-g4-sincro semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 15 de 15 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t42-numeros-harbinger-hierro-g4-sincro: enemy:cataclysm.deathlaser=1 enemy:cataclysm.laser=3 enemy:explosion.player=3 enemy:mob=10 env:inFire=1 env:onFire=1 env:wither=1
- t42-numeros-harbinger-hierro-g8-sincro: enemy:cataclysm.deathlaser=5 enemy:cataclysm.laser=6 enemy:explosion.player=3 enemy:indirectMagic=1 enemy:mob=16 env:inFire=2 env:onFire=2 env:wither=5
- t42-numeros-harbinger-netheritamas-g8-sincro: enemy:cataclysm.deathlaser=1 enemy:mob=1 env:onFire=1
- t42-numeros-harbinger-hierro-g16-sincro: enemy:cataclysm.deathlaser=22 enemy:cataclysm.laser=11 enemy:explosion.player=1 enemy:indirectMagic=9 enemy:mob=18 env:inFire=2 env:onFire=10 env:wither=7

## Motivos de decisión (cambios registrados): ENGAGE 940, FORMATION_HOLD 1, LOW_HEALTH_RETREAT 337, NONE 46, OUT_OF_RANGE 270, SEEK_ENEMY 143, STUCK 91, TOO_CLOSE 192, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t42-numeros-harbinger-hierro-g16-sincro - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t42-numeros-harbinger-netheritamas-g16-sincro - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t42-numeros-harbinger-hierro-g4-sincro - 15 % de las bajas azules ({enemy:cataclysm.deathlaser=1.0, enemy:cataclysm.laser=3.0, enemy:explosion.player=3.0, enemy:mob=10.0, env:inFire=1.0, env:onFire=1.0, env:wither=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t42-numeros-harbinger-hierro-g8-sincro - 23 % de las bajas azules ({enemy:cataclysm.deathlaser=5.0, enemy:cataclysm.laser=6.0, enemy:explosion.player=3.0, enemy:indirectMagic=1.0, enemy:mob=16.0, env:inFire=2.0, env:onFire=2.0, env:wither=5.0})
- MUERTES QUE NO SON DEL ENEMIGO: t42-numeros-harbinger-netheritamas-g8-sincro - 33 % de las bajas azules ({enemy:cataclysm.deathlaser=1.0, enemy:mob=1.0, env:onFire=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t42-numeros-harbinger-hierro-g16-sincro - 24 % de las bajas azules ({enemy:cataclysm.deathlaser=22.0, enemy:cataclysm.laser=11.0, enemy:explosion.player=1.0, enemy:indirectMagic=9.0, enemy:mob=18.0, env:inFire=2.0, env:onFire=10.0, env:wither=7.0})
