# Simulación Rally - lote 20261111-080228-t42-numeros-dragonanciano

Preset `t42-numeros-dragonanciano` · velocidad `sprint` · 31 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t42-numeros-dragonanciano-hierro-g8-sincro | 5 | 20 % ± 20 | 80 % ± 20 | 0.8 ± 0.8 | 0.8 ± 0.2 | 64 ± 6 | 257 ± 40 | 208 ± 15 | 49 % ± 5 | 11.7 ± 0.7 | 5.8 ± 0.4 | 0 |
| t42-numeros-dragonanciano-netheritamas-g8-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.4 | 0.0 ± 0.0 | 27 ± 2 | 377 ± 4 | 50 ± 17 | 44 % ± 3 | 14.9 ± 1.4 | 6.1 ± 0.5 | 0 |
| t42-numeros-dragonanciano-hierro-g16-sincro | 5 | 60 % ± 24 | 40 % ± 24 | 7.6 ± 3.1 | 0.4 ± 0.2 | 70 ± 5 | 326 ± 30 | 232 ± 70 | 46 % ± 5 | 12.8 ± 0.5 | 6.2 ± 0.1 | 0 |
| t42-numeros-dragonanciano-netheritamas-g16-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 28 ± 2 | 374 ± 3 | 9 ± 4 | 47 % ± 7 | 13.7 ± 0.5 | 5.5 ± 0.1 | 0 |
| t42-numeros-dragonanciano-hierro-g32-sincro | 5 | 80 % ± 20 | 20 % ± 20 | 22.0 ± 5.6 | 0.2 ± 0.2 | 69 ± 9 | 367 ± 4 | 323 ± 133 | 49 % ± 4 | 19.0 ± 0.8 | 12.4 ± 1.0 | 0 |
| t42-numeros-dragonanciano-netheritamas-g32-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 31.4 ± 0.6 | 0.0 ± 0.0 | 25 ± 1 | 373 ± 2 | 121 ± 44 | 50 % ± 8 | 21.6 ± 1.4 | 12.8 ± 0.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t42-numeros-dragonanciano-hierro-g8-sincro semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 23 de 23 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t42-numeros-dragonanciano-hierro-g8-sincro: enemy:dragon_fire=15 enemy:mob=19 env:inFire=1 env:onFire=1
- t42-numeros-dragonanciano-netheritamas-g8-sincro: enemy:dragon_fire=1 enemy:mob=2
- t42-numeros-dragonanciano-hierro-g16-sincro: enemy:dragon_fire=16 enemy:mob=25 env:onFire=1
- t42-numeros-dragonanciano-hierro-g32-sincro: enemy:dragon_fire=20 enemy:mob=20 env:inFire=2 env:onFire=8
- t42-numeros-dragonanciano-netheritamas-g32-sincro: enemy:dragon_fire=3

## Motivos de decisión (cambios registrados): ENGAGE 689, LOW_HEALTH_RETREAT 160, NONE 111, OUT_OF_RANGE 2, SEEK_ENEMY 240, STUCK 5, TOO_CLOSE 119, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: t42-numeros-dragonanciano-hierro-g32-sincro - 20 % de las bajas azules ({enemy:dragon_fire=20.0, enemy:mob=20.0, env:inFire=2.0, env:onFire=8.0})
