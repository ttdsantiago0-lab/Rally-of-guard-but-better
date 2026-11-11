# Simulación Rally - lote 20261111-073246-t38-coord-jefes

Preset `t38-coord-jefes` · velocidad `sprint` · 74 corridas · 12 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t38-coord-muneco-arco-g8-base | 6 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 70 ± 4 | 1026 ± 1 | 0 ± 0 | 100 % ± 0 | 13.2 ± 0.2 | 5.4 ± 0.1 | 0 |
| t38-coord-dragonadulto-g8-base | 6 | 100 % ± 0 | 0 % ± 0 | 6.8 ± 0.5 | 0.0 ± 0.0 | 34 ± 3 | 253 ± 1 | 58 ± 16 | 37 % ± 2 | 11.7 ± 0.6 | 6.2 ± 0.4 | 0 |
| t38-coord-ignis-g8-base | 6 | 100 % ± 0 | 0 % ± 0 | 3.8 ± 0.3 | 0.0 ± 0.0 | 86 ± 8 | 704 ± 81 | 246 ± 22 | 87 % ± 2 | 8.9 ± 0.3 | 8.0 ± 0.3 | 0 |
| t38-coord-harbinger-g8-base | 6 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 58 ± 2 | 504 ± 3 | 81 ± 10 | 93 % ± 2 | 15.1 ± 1.0 | 12.1 ± 0.8 | 0 |
| t38-coord-muneco-arco-g8-reparto | 6 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 68 ± 3 | 1025 ± 1 | 0 ± 0 | 100 % ± 0 | 13.5 ± 0.2 | 5.2 ± 0.1 | 0 |
| t38-coord-dragonadulto-g8-reparto | 6 | 83 % ± 17 | 17 % ± 17 | 5.3 ± 1.1 | 0.2 ± 0.2 | 35 ± 3 | 242 ± 10 | 86 ± 22 | 33 % ± 6 | 13.1 ± 0.9 | 6.1 ± 0.4 | 0 |
| t38-coord-ignis-g8-reparto | 6 | 100 % ± 0 | 0 % ± 0 | 3.0 ± 0.4 | 0.0 ± 0.0 | 102 ± 7 | 790 ± 52 | 339 ± 19 | 86 % ± 2 | 8.7 ± 0.2 | 7.5 ± 0.4 | 0 |
| t38-coord-harbinger-g8-reparto | 6 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 57 ± 2 | 504 ± 3 | 71 ± 8 | 93 % ± 1 | 15.0 ± 0.7 | 11.8 ± 0.5 | 0 |
| t38-coord-muneco-arco-g8-sincro | 6 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 73 ± 1 | 1028 ± 1 | 0 ± 0 | 100 % ± 0 | 13.3 ± 0.1 | 5.4 ± 0.1 | 0 |
| t38-coord-dragonadulto-g8-sincro | 6 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 29 ± 2 | 253 ± 1 | 16 ± 11 | 17 % ± 3 | 13.7 ± 0.8 | 5.9 ± 0.4 | 1 |
| t38-coord-ignis-g8-sincro | 6 | 100 % ± 0 | 0 % ± 0 | 3.7 ± 0.5 | 0.0 ± 0.0 | 105 ± 11 | 876 ± 79 | 291 ± 42 | 86 % ± 2 | 9.3 ± 0.3 | 8.2 ± 0.3 | 0 |
| t38-coord-harbinger-g8-sincro | 6 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 63 ± 4 | 517 ± 9 | 76 ± 4 | 87 % ± 1 | 19.2 ± 1.3 | 14.6 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t38-coord-muneco-arco-g8-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 71 de 71 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t38-coord-dragonadulto-g8-base: enemy:dragon_fire=3 enemy:mob=3 env:onFire=1
- t38-coord-ignis-g8-base: enemy:cataclysm.flame_strike=15 enemy:mob=9 env:onFire=1
- t38-coord-harbinger-g8-base: enemy:cataclysm.laser=1
- t38-coord-dragonadulto-g8-reparto: enemy:dragon_fire=7 enemy:mob=6 env:inFire=2 env:onFire=1
- t38-coord-ignis-g8-reparto: enemy:cataclysm.flame_strike=17 enemy:mob=13
- t38-coord-dragonadulto-g8-sincro: enemy:dragon_fire=1
- t38-coord-ignis-g8-sincro: enemy:cataclysm.flame_strike=8 enemy:mob=17 env:onFire=1

## Motivos de decisión (cambios registrados): ENGAGE 904, FORMATION_HOLD 2, LOW_HEALTH_RETREAT 292, NONE 4, OUT_OF_RANGE 43, SEEK_ENEMY 158, STUCK 2, TOO_CLOSE 167

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t38-coord-dragonadulto-g8-sincro - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t38-coord-dragonadulto-g8-base - 14 % de las bajas azules ({enemy:dragon_fire=3.0, enemy:mob=3.0, env:onFire=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t38-coord-dragonadulto-g8-reparto - 19 % de las bajas azules ({enemy:dragon_fire=7.0, enemy:mob=6.0, env:inFire=2.0, env:onFire=1.0})
