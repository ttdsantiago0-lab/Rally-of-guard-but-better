# Simulación Rally - lote 20261118-045632-t73-ignis-kite

Preset `t73-ignis-kite` · velocidad `sprint` · 151 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t73-2a6 | 30 | 100 % ± 0 | 0 % ± 0 | 4.4 ± 0.2 | 0.0 ± 0.0 | 80 ± 3 | 630 ± 17 | 225 ± 12 | 90 % ± 1 | 11.0 ± 0.1 | 9.4 ± 0.2 | 0 |
| t73-2a6-k10 | 30 | 100 % ± 0 | 0 % ± 0 | 5.3 ± 0.2 | 0.0 ± 0.0 | 82 ± 3 | 640 ± 21 | 155 ± 8 | 90 % ± 1 | 13.4 ± 0.1 | 11.3 ± 0.2 | 0 |
| t73-2a6-k14 | 30 | 100 % ± 0 | 0 % ± 0 | 5.6 ± 0.1 | 0.0 ± 0.0 | 91 ± 3 | 648 ± 15 | 141 ± 9 | 91 % ± 1 | 15.2 ± 0.1 | 12.2 ± 0.2 | 0 |
| t73-arcos | 30 | 100 % ± 0 | 0 % ± 0 | 4.3 ± 0.3 | 0.0 ± 0.0 | 77 ± 1 | 523 ± 5 | 240 ± 12 | 86 % ± 0 | 13.5 ± 0.1 | 9.6 ± 0.2 | 0 |
| t73-arcos-k14 | 30 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.2 | 0.0 ± 0.0 | 107 ± 2 | 525 ± 5 | 142 ± 7 | 88 % ± 0 | 19.1 ± 0.3 | 14.5 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t73-2a6 semilla 7301: sprint → blue (6/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 150 de 150 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t73-2a6: enemy:cataclysm.flame_strike=41 enemy:explosion.player=1 enemy:mob=64 env:fall=1 env:onFire=1
- t73-2a6-k10: enemy:cataclysm.flame_strike=33 enemy:mob=46 env:onFire=2
- t73-2a6-k14: enemy:cataclysm.flame_strike=26 enemy:mob=43 env:onFire=3 other(cataclysm:ignis):cataclysm.flame_strike=1
- t73-arcos: enemy:cataclysm.flame_strike=26 enemy:explosion.player=1 enemy:mob=80 env:fall=2 env:inFire=1
- t73-arcos-k14: enemy:cataclysm.flame_strike=23 enemy:mob=17 env:inFire=3 env:onFire=3

## Motivos de decisión (cambios registrados): ENGAGE 2259, FORMATION_HOLD 2, LOW_HEALTH_RETREAT 971, NONE 13, OUT_OF_RANGE 56, SEEK_ENEMY 445, STUCK 54, TOO_CLOSE 611

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: t73-arcos-k14 - 13 % de las bajas azules ({enemy:cataclysm.flame_strike=23.0, enemy:mob=17.0, env:inFire=3.0, env:onFire=3.0})
