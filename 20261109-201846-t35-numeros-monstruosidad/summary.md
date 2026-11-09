# Simulación Rally - lote 20261109-201846-t35-numeros-monstruosidad

Preset `t35-numeros-monstruosidad` · velocidad `sprint` · 31 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t35-numeros-monstruosidad-hierro-g4 | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 55 ± 16 | 140 ± 48 | 135 ± 16 | 26 % ± 9 | 18.8 ± 1.7 | 4.4 ± 0.7 | 0 |
| t35-numeros-monstruosidad-netheritamas-g4 | 5 | 100 % ± 0 | 0 % ± 0 | 3.6 ± 0.2 | 0.0 ± 0.0 | 52 ± 1 | 603 ± 2 | 83 ± 10 | 61 % ± 4 | 15.4 ± 1.3 | 11.3 ± 1.5 | 0 |
| t35-numeros-monstruosidad-hierro-g8 | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 68 ± 8 | 215 ± 19 | 237 ± 14 | 35 % ± 2 | 20.9 ± 1.6 | 7.8 ± 0.7 | 0 |
| t35-numeros-monstruosidad-netheritamas-g8 | 5 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.4 | 0.0 ± 0.0 | 53 ± 2 | 604 ± 2 | 145 ± 15 | 64 % ± 9 | 15.0 ± 0.8 | 10.9 ± 0.7 | 0 |
| t35-numeros-monstruosidad-hierro-g16 | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 100 ± 15 | 345 ± 57 | 464 ± 13 | 46 % ± 2 | 21.9 ± 2.3 | 11.8 ± 2.5 | 0 |
| t35-numeros-monstruosidad-netheritamas-g16 | 5 | 100 % ± 0 | 0 % ± 0 | 13.8 ± 0.9 | 0.0 ± 0.0 | 53 ± 2 | 603 ± 1 | 240 ± 24 | 57 % ± 6 | 17.4 ± 1.0 | 12.0 ± 1.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t35-numeros-monstruosidad-hierro-g4 semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 15 de 15 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t35-numeros-monstruosidad-hierro-g4: enemy:explosion.player=3 enemy:mob=15 env:lava=1 env:onFire=1
- t35-numeros-monstruosidad-netheritamas-g4: enemy:explosion.player=1 enemy:mob=1
- t35-numeros-monstruosidad-hierro-g8: enemy:explosion.player=4 enemy:mob=26 env:lava=5 env:onFire=5
- t35-numeros-monstruosidad-netheritamas-g8: enemy:mob=3
- t35-numeros-monstruosidad-hierro-g16: enemy:explosion.player=1 enemy:mob=65 env:lava=8 env:onFire=6
- t35-numeros-monstruosidad-netheritamas-g16: enemy:mob=11

## Motivos de decisión (cambios registrados): ENGAGE 315, FORMATION_HOLD 2, LOW_HEALTH_RETREAT 137, NONE 80, SEEK_ENEMY 44, STUCK 4, TOO_CLOSE 12

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: t35-numeros-monstruosidad-hierro-g8 - 25 % de las bajas azules ({enemy:explosion.player=4.0, enemy:mob=26.0, env:lava=5.0, env:onFire=5.0})
- MUERTES QUE NO SON DEL ENEMIGO: t35-numeros-monstruosidad-hierro-g16 - 18 % de las bajas azules ({enemy:explosion.player=1.0, enemy:mob=65.0, env:lava=8.0, env:onFire=6.0})
