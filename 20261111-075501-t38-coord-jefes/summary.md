# Simulación Rally - lote 20261111-075501-t38-coord-jefes

Preset `t38-coord-jefes` · velocidad `sprint` · 77 corridas · 12 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t38-coord-muneco-arco-g8-base | 6 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 68 ± 6 | 1027 ± 1 | 0 ± 0 | 100 % ± 0 | 13.6 ± 0.2 | 5.4 ± 0.1 | 0 |
| t38-coord-dragonadulto-g8-base | 6 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.8 | 0.0 ± 0.0 | 34 ± 4 | 253 ± 2 | 56 ± 28 | 30 % ± 4 | 12.6 ± 0.6 | 6.1 ± 0.1 | 0 |
| t38-coord-ignis-g8-base | 6 | 100 % ± 0 | 0 % ± 0 | 3.0 ± 0.5 | 0.0 ± 0.0 | 108 ± 6 | 888 ± 62 | 323 ± 23 | 88 % ± 1 | 8.9 ± 0.2 | 8.0 ± 0.3 | 0 |
| t38-coord-harbinger-g8-base | 6 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 61 ± 2 | 512 ± 4 | 86 ± 7 | 91 % ± 2 | 17.8 ± 0.9 | 14.2 ± 0.8 | 2 |
| t38-coord-muneco-arco-g8-reparto | 6 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 71 ± 5 | 1027 ± 1 | 0 ± 0 | 100 % ± 0 | 13.4 ± 0.2 | 5.3 ± 0.1 | 0 |
| t38-coord-dragonadulto-g8-reparto | 6 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.5 | 0.0 ± 0.0 | 32 ± 3 | 255 ± 1 | 78 ± 20 | 36 % ± 3 | 12.9 ± 1.0 | 6.5 ± 0.4 | 1 |
| t38-coord-ignis-g8-reparto | 6 | 100 % ± 0 | 0 % ± 0 | 3.5 ± 0.3 | 0.0 ± 0.0 | 98 ± 8 | 825 ± 76 | 306 ± 27 | 87 % ± 2 | 8.9 ± 0.2 | 8.4 ± 0.2 | 0 |
| t38-coord-harbinger-g8-reparto | 6 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.2 | 0.0 ± 0.0 | 59 ± 1 | 507 ± 2 | 89 ± 11 | 93 % ± 2 | 16.0 ± 2.1 | 11.7 ± 1.6 | 0 |
| t38-coord-muneco-arco-g8-sincro | 6 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 78 ± 3 | 1027 ± 1 | 0 ± 0 | 100 % ± 0 | 13.3 ± 0.3 | 5.7 ± 0.2 | 0 |
| t38-coord-dragonadulto-g8-sincro | 6 | 100 % ± 0 | 0 % ± 0 | 7.3 ± 0.3 | 0.0 ± 0.0 | 29 ± 2 | 254 ± 1 | 24 ± 9 | 25 % ± 4 | 11.9 ± 0.5 | 5.5 ± 0.2 | 0 |
| t38-coord-ignis-g8-sincro | 6 | 100 % ± 0 | 0 % ± 0 | 2.8 ± 0.3 | 0.0 ± 0.0 | 102 ± 7 | 860 ± 67 | 305 ± 23 | 90 % ± 1 | 9.0 ± 0.3 | 7.8 ± 0.3 | 0 |
| t38-coord-harbinger-g8-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 65 ± 4 | 520 ± 9 | 106 ± 14 | 91 % ± 2 | 17.2 ± 1.7 | 13.4 ± 0.9 | 2 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t38-coord-muneco-arco-g8-base semilla 1001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 71 de 71 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t38-coord-dragonadulto-g8-base: enemy:dragon_fire=6 enemy:mob=3
- t38-coord-ignis-g8-base: enemy:cataclysm.flame_strike=13 enemy:mob=15 env:onFire=2
- t38-coord-harbinger-g8-base: enemy:cataclysm.laser=1
- t38-coord-dragonadulto-g8-reparto: enemy:dragon_fire=6 enemy:mob=2 env:inFire=1
- t38-coord-ignis-g8-reparto: enemy:cataclysm.flame_strike=15 enemy:indirectMagic=1 enemy:mob=11
- t38-coord-harbinger-g8-reparto: enemy:indirectMagic=1 env:inFire=1
- t38-coord-dragonadulto-g8-sincro: enemy:dragon_fire=3 enemy:mob=1
- t38-coord-ignis-g8-sincro: enemy:cataclysm.flame_strike=13 enemy:mob=15 env:fall=2 env:onFire=1
- t38-coord-harbinger-g8-sincro: enemy:cataclysm.deathlaser=1

## Motivos de decisión (cambios registrados): ENGAGE 1083, LOW_HEALTH_RETREAT 287, NONE 13, OUT_OF_RANGE 73, SEEK_ENEMY 158, STUCK 74, TOO_CLOSE 203

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t38-coord-harbinger-g8-base - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t38-coord-dragonadulto-g8-reparto - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t38-coord-harbinger-g8-sincro - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t38-coord-dragonadulto-g8-reparto - 11 % de las bajas azules ({enemy:dragon_fire=6.0, enemy:mob=2.0, env:inFire=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t38-coord-harbinger-g8-reparto - 50 % de las bajas azules ({enemy:indirectMagic=1.0, env:inFire=1.0})
