# Simulación Rally - lote 20261111-080300-t42-numeros-dragonadulto

Preset `t42-numeros-dragonadulto` · velocidad `sprint` · 41 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t42-numeros-dragonadulto-hierro-g4-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 3.4 ± 0.4 | 0.0 ± 0.0 | 32 ± 4 | 255 ± 1 | 28 ± 15 | 22 % ± 2 | 12.8 ± 0.7 | 6.4 ± 0.7 | 0 |
| t42-numeros-dragonadulto-netheritamas-g4-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 4.0 ± 0.0 | 0.0 ± 0.0 | 16 ± 2 | 258 ± 3 | 8 ± 6 | 20 % ± 6 | 16.2 ± 2.2 | 4.5 ± 0.3 | 0 |
| t42-numeros-dragonadulto-hierro-g8-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 6.8 ± 0.5 | 0.0 ± 0.0 | 34 ± 4 | 253 ± 1 | 65 ± 19 | 24 % ± 2 | 12.0 ± 1.1 | 6.3 ± 0.4 | 0 |
| t42-numeros-dragonadulto-netheritamas-g8-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 1 | 261 ± 2 | 21 ± 9 | 19 % ± 5 | 19.2 ± 2.0 | 5.6 ± 0.3 | 0 |
| t42-numeros-dragonadulto-hierro-g16-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 15.6 ± 0.4 | 0.0 ± 0.0 | 32 ± 3 | 254 ± 2 | 53 ± 33 | 30 % ± 3 | 13.1 ± 0.4 | 6.7 ± 0.4 | 0 |
| t42-numeros-dragonadulto-netheritamas-g16-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 15 ± 1 | 256 ± 5 | 72 ± 30 | 19 % ± 2 | 20.2 ± 2.0 | 6.4 ± 0.3 | 0 |
| t42-numeros-dragonadulto-hierro-g32-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 30.8 ± 1.0 | 0.0 ± 0.0 | 31 ± 3 | 253 ± 1 | 74 ± 58 | 27 % ± 8 | 22.3 ± 2.4 | 14.0 ± 1.3 | 0 |
| t42-numeros-dragonadulto-netheritamas-g32-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 32.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 1 | 259 ± 2 | 20 ± 20 | 30 % ± 5 | 26.7 ± 1.4 | 11.7 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t42-numeros-dragonadulto-hierro-g4-sincro semilla 1: sprint → blue (3/0 vivos), normal → blue (3/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t42-numeros-dragonadulto-hierro-g4-sincro: enemy:dragon_fire=1 enemy:mob=2
- t42-numeros-dragonadulto-hierro-g8-sincro: enemy:dragon_fire=4 enemy:mob=2
- t42-numeros-dragonadulto-hierro-g16-sincro: enemy:dragon_fire=1 enemy:mob=1
- t42-numeros-dragonadulto-hierro-g32-sincro: enemy:dragon_fire=3 enemy:mob=1 env:inFire=1 env:onFire=1

## Motivos de decisión (cambios registrados): ENGAGE 611, LOW_HEALTH_RETREAT 37, NONE 69, OUT_OF_RANGE 1, SEEK_ENEMY 109, STUCK 2, TOO_CLOSE 67

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: t42-numeros-dragonadulto-hierro-g32-sincro - 33 % de las bajas azules ({enemy:dragon_fire=3.0, enemy:mob=1.0, env:inFire=1.0, env:onFire=1.0})
