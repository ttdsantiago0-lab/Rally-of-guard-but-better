# Simulación Rally - lote 20261118-052828-t75-ignis-conf

Preset `t75-ignis-conf` · velocidad `sprint` · 227 corridas · 7 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t75-arcos | 30 | 93 % ± 5 | 7 % ± 5 | 4.9 ± 0.4 | 0.1 ± 0.0 | 80 ± 3 | 520 ± 6 | 240 ± 14 | 86 % ± 1 | 13.6 ± 0.2 | 9.5 ± 0.2 | 0 |
| t75-arcos-k14 | 30 | 100 % ± 0 | 0 % ± 0 | 6.4 ± 0.2 | 0.0 ± 0.0 | 104 ± 2 | 521 ± 5 | 124 ± 8 | 91 % ± 1 | 18.4 ± 0.1 | 14.2 ± 0.3 | 0 |
| t75-arcos-k18 | 30 | 50 % ± 0 | 50 % ± 0 | 6.5 ± 0.3 | 1.0 ± 0.0 | 240 ± 0 | 316 ± 8 | 171 ± 7 | 82 % ± 1 | 24.8 ± 0.4 | 20.3 ± 0.4 | 3 |
| t75-2a6 | 30 | 100 % ± 0 | 0 % ± 0 | 4.3 ± 0.2 | 0.0 ± 0.0 | 83 ± 2 | 649 ± 19 | 233 ± 11 | 89 % ± 1 | 11.3 ± 0.1 | 9.7 ± 0.2 | 0 |
| t75-2a6-k14 | 30 | 100 % ± 0 | 0 % ± 0 | 5.7 ± 0.1 | 0.0 ± 0.0 | 89 ± 3 | 635 ± 15 | 142 ± 8 | 91 % ± 0 | 15.2 ± 0.1 | 12.2 ± 0.2 | 0 |
| t75-tremor-linea | 28 | 41 % ± 9 | 59 % ± 9 | 4.8 ± 1.1 | 0.6 ± 0.1 | 117 ± 9 | 929 ± 61 | 332 ± 16 | 90 % ± 1 | 20.1 ± 0.9 | 14.3 ± 0.7 | 8 |
| t75-tremor-linea-k14 | 29 | 40 % ± 9 | 60 % ± 9 | 5.1 ± 1.2 | 0.6 ± 0.1 | 114 ± 11 | 958 ± 72 | 312 ± 19 | 89 % ± 1 | 19.5 ± 0.6 | 13.6 ± 0.6 | 8 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t75-arcos semilla 7500: sprint → blue (7/0 vivos), normal → blue (3/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 172 de 172 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t75-arcos: enemy:cataclysm.flame_strike=18 enemy:explosion.player=1 enemy:mob=72 env:onFire=2
- t75-arcos-k14: enemy:cataclysm.flame_strike=26 enemy:mob=20 env:onFire=2 other(cataclysm:ignis):cataclysm.flame_strike=1
- t75-arcos-k18: enemy:mob=37 env:inFire=3 env:onFire=2 other(cataclysm:ignis):cataclysm.flame_strike=2
- t75-2a6: enemy:cataclysm.flame_strike=38 enemy:explosion.player=1 enemy:mob=65 env:fall=2 env:inFire=1 env:onFire=3
- t75-2a6-k14: enemy:cataclysm.flame_strike=31 enemy:explosion.player=1 enemy:mob=34 env:fall=1 env:onFire=1
- t75-tremor-linea: enemy:mob=75 enemy:tremorzilla_beam=217 env:radiation=22 other(cataclysm:ignis):explosion.player=1
- t75-tremor-linea-k14: enemy:mob=65 enemy:tremorzilla_beam=226 env:radiation=24

## Motivos de decisión (cambios registrados): ENGAGE 5512, FORMATION_HOLD 20, LOW_HEALTH_RETREAT 3938, NONE 2081, OUT_OF_RANGE 965, SEEK_ENEMY 3230, STUCK 543, TOO_CLOSE 809, UNIT_BROKEN 3

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t75-tremor-linea - 8 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t75-tremor-linea-k14 - 8 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t75-arcos-k18 - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t75-arcos-k18: 30/30 corridas llegaron al tope de tiempo
- MUERTES QUE NO SON DEL ENEMIGO: t75-arcos-k18 - 16 % de las bajas azules ({enemy:mob=37.0, env:inFire=3.0, env:onFire=2.0, other(cataclysm:ignis):cataclysm.flame_strike=2.0})
