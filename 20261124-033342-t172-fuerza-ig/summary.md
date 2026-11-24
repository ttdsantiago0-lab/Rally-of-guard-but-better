# Simulación Rally - lote 20261124-033342-t172-fuerza-ig

Preset `t172-fuerza-ig` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t172-ig-espera | 30 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.2 | 0.0 ± 0.0 | 105 ± 5 | 509 ± 6 | 151 ± 9 | 84 % ± 1 | 23.7 ± 0.9 | 15.4 ± 0.3 | 0 |
| t172-ig-force | 30 | 100 % ± 0 | 0 % ± 0 | 6.4 ± 0.2 | 0.0 ± 0.0 | 95 ± 4 | 494 ± 4 | 132 ± 8 | 85 % ± 2 | 21.8 ± 0.8 | 15.4 ± 0.3 | 0 |
| t172-pierde-espera | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 31 ± 2 | 56 ± 5 | 272 ± 6 | 0 % ± 0 | 9.7 ± 0.3 | 4.2 ± 0.2 | 0 |
| t172-pierde-force | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 32 ± 2 | 55 ± 4 | 270 ± 6 | 0 % ± 0 | 10.4 ± 0.3 | 4.0 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t172-ig-espera semilla 15300: sprint → blue (5/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t172-ig-espera: enemy:cataclysm.flame_strike=26 enemy:explosion.player=2 enemy:mob=14 env:onFire=2
- t172-ig-force: enemy:cataclysm.flame_strike=37 enemy:explosion.player=1 enemy:mob=5 env:inFire=2 env:onFire=3
- t172-pierde-espera: enemy:mob=229 env:onFire=11
- t172-pierde-force: enemy:explosion.player=1 enemy:mob=227 env:onFire=12

## Motivos de decisión (cambios registrados): ENGAGE 1833, FORMATION_HOLD 383, LOW_HEALTH_RETREAT 557, OUT_OF_RANGE 41, SEEK_ENEMY 696, STUCK 1, TOO_CLOSE 202

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: t172-ig-force - 10 % de las bajas azules ({enemy:cataclysm.flame_strike=37.0, enemy:explosion.player=1.0, enemy:mob=5.0, env:inFire=2.0, env:onFire=3.0})
