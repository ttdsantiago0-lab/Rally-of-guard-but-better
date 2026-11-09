# Simulación Rally - lote 20261109-201815-t35-numeros-dragonanciano

Preset `t35-numeros-dragonanciano` · velocidad `sprint` · 31 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t35-numeros-dragonanciano-hierro-g8 | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 56 ± 5 | 145 ± 24 | 205 ± 7 | 62 % ± 3 | 20.6 ± 0.6 | 12.8 ± 1.1 | 0 |
| t35-numeros-dragonanciano-netheritamas-g8 | 5 | 100 % ± 0 | 0 % ± 0 | 7.2 ± 0.6 | 0.0 ± 0.0 | 31 ± 1 | 372 ± 2 | 63 ± 23 | 75 % ± 5 | 20.3 ± 3.0 | 12.3 ± 1.3 | 0 |
| t35-numeros-dragonanciano-hierro-g16 | 5 | 60 % ± 24 | 40 % ± 24 | 8.0 ± 3.3 | 0.4 ± 0.2 | 71 ± 9 | 311 ± 37 | 221 ± 86 | 52 % ± 6 | 21.2 ± 2.5 | 14.1 ± 2.2 | 0 |
| t35-numeros-dragonanciano-netheritamas-g16 | 5 | 100 % ± 0 | 0 % ± 0 | 15.8 ± 0.2 | 0.0 ± 0.0 | 23 ± 3 | 372 ± 2 | 70 ± 41 | 57 % ± 6 | 24.4 ± 3.2 | 12.0 ± 1.6 | 0 |
| t35-numeros-dragonanciano-hierro-g32 | 5 | 60 % ± 24 | 40 % ± 24 | 12.8 ± 6.2 | 0.4 ± 0.2 | 99 ± 25 | 348 ± 19 | 531 ± 157 | 64 % ± 3 | 25.9 ± 2.3 | 19.0 ± 2.1 | 0 |
| t35-numeros-dragonanciano-netheritamas-g32 | 5 | 100 % ± 0 | 0 % ± 0 | 31.2 ± 0.6 | 0.0 ± 0.0 | 23 ± 2 | 373 ± 1 | 166 ± 83 | 60 % ± 7 | 31.1 ± 3.4 | 15.4 ± 1.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t35-numeros-dragonanciano-hierro-g8 semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 21 de 21 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t35-numeros-dragonanciano-hierro-g8: enemy:dragon_fire=16 enemy:mob=20 env:inFire=4
- t35-numeros-dragonanciano-netheritamas-g8: enemy:dragon_fire=2 enemy:mob=2
- t35-numeros-dragonanciano-hierro-g16: enemy:dragon_fire=12 enemy:mob=20 env:inFire=3 env:onFire=5
- t35-numeros-dragonanciano-netheritamas-g16: enemy:dragon_fire=1
- t35-numeros-dragonanciano-hierro-g32: enemy:dragon_fire=52 enemy:mob=34 env:inFire=5 env:onFire=5
- t35-numeros-dragonanciano-netheritamas-g32: enemy:dragon_fire=4

## Motivos de decisión (cambios registrados): ENGAGE 540, LOW_HEALTH_RETREAT 148, NONE 246, OUT_OF_RANGE 13, SEEK_ENEMY 120, STUCK 7, TOO_CLOSE 53

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: t35-numeros-dragonanciano-hierro-g16 - 20 % de las bajas azules ({enemy:dragon_fire=12.0, enemy:mob=20.0, env:inFire=3.0, env:onFire=5.0})
- MUERTES QUE NO SON DEL ENEMIGO: t35-numeros-dragonanciano-hierro-g32 - 10 % de las bajas azules ({enemy:dragon_fire=52.0, enemy:mob=34.0, env:inFire=5.0, env:onFire=5.0})
