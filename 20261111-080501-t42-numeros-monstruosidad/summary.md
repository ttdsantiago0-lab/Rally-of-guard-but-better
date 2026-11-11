# Simulación Rally - lote 20261111-080501-t42-numeros-monstruosidad

Preset `t42-numeros-monstruosidad` · velocidad `sprint` · 31 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t42-numeros-monstruosidad-hierro-g4-sincro | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 40 ± 7 | 191 ± 26 | 121 ± 10 | 38 % ± 5 | 14.5 ± 0.9 | 3.4 ± 0.5 | 0 |
| t42-numeros-monstruosidad-netheritamas-g4-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 3.2 ± 0.5 | 0.0 ± 0.0 | 60 ± 7 | 604 ± 3 | 100 ± 15 | 51 % ± 7 | 12.6 ± 0.8 | 8.2 ± 0.6 | 0 |
| t42-numeros-monstruosidad-hierro-g8-sincro | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 45 ± 8 | 310 ± 49 | 233 ± 29 | 52 % ± 3 | 15.1 ± 0.5 | 6.1 ± 1.5 | 0 |
| t42-numeros-monstruosidad-netheritamas-g8-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 7.0 ± 0.4 | 0.0 ± 0.0 | 49 ± 1 | 605 ± 1 | 160 ± 20 | 55 % ± 1 | 12.2 ± 0.4 | 7.8 ± 0.3 | 0 |
| t42-numeros-monstruosidad-hierro-g16-sincro | 5 | 20 % ± 20 | 80 % ± 20 | 0.4 ± 0.4 | 0.8 ± 0.2 | 76 ± 6 | 574 ± 37 | 463 ± 22 | 53 % ± 4 | 16.1 ± 0.3 | 10.2 ± 1.4 | 0 |
| t42-numeros-monstruosidad-netheritamas-g16-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 14.4 ± 0.5 | 0.0 ± 0.0 | 52 ± 1 | 611 ± 8 | 273 ± 19 | 67 % ± 4 | 12.8 ± 0.4 | 8.9 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t42-numeros-monstruosidad-hierro-g4-sincro semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 16 de 16 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t42-numeros-monstruosidad-hierro-g4-sincro: enemy:mob=15 env:lava=4 env:onFire=1
- t42-numeros-monstruosidad-netheritamas-g4-sincro: enemy:mob=4
- t42-numeros-monstruosidad-hierro-g8-sincro: enemy:explosion.player=2 enemy:mob=32 env:lava=5 env:onFire=1
- t42-numeros-monstruosidad-netheritamas-g8-sincro: enemy:mob=5
- t42-numeros-monstruosidad-hierro-g16-sincro: enemy:explosion.player=4 enemy:mob=55 env:lava=14 env:onFire=5
- t42-numeros-monstruosidad-netheritamas-g16-sincro: enemy:mob=7 env:lava=1

## Motivos de decisión (cambios registrados): ENGAGE 410, LOW_HEALTH_RETREAT 236, NONE 12, OUT_OF_RANGE 1, SEEK_ENEMY 105, TOO_CLOSE 10

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: t42-numeros-monstruosidad-hierro-g4-sincro - 25 % de las bajas azules ({enemy:mob=15.0, env:lava=4.0, env:onFire=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t42-numeros-monstruosidad-hierro-g8-sincro - 15 % de las bajas azules ({enemy:explosion.player=2.0, enemy:mob=32.0, env:lava=5.0, env:onFire=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t42-numeros-monstruosidad-hierro-g16-sincro - 24 % de las bajas azules ({enemy:explosion.player=4.0, enemy:mob=55.0, env:lava=14.0, env:onFire=5.0})
- MUERTES QUE NO SON DEL ENEMIGO: t42-numeros-monstruosidad-netheritamas-g16-sincro - 13 % de las bajas azules ({enemy:mob=7.0, env:lava=1.0})
