# Simulación Rally - lote 20261109-012040-t20-amenaza-blaze

Preset `t20-amenaza-blaze` · velocidad `sprint` · 361 corridas · 12 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t20-amenaza-blaze-escudo-base-r6 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 29 ± 1 | 143 ± 0 | 12 ± 1 | 0 % ± 0 | 13.2 ± 0.6 | 10.8 ± 0.6 | 0 |
| t20-amenaza-blaze-escudo-sinamenazas-r6 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 19 ± 1 | 142 ± 1 | 17 ± 1 | 0 % ± 0 | 12.3 ± 0.6 | 10.3 ± 0.6 | 0 |
| t20-amenaza-blaze-scutum-base-r6 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 28 ± 1 | 142 ± 1 | 11 ± 1 | 0 % ± 0 | 12.8 ± 0.8 | 9.5 ± 0.6 | 0 |
| t20-amenaza-blaze-scutum-sinamenazas-r6 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 20 ± 1 | 142 ± 0 | 17 ± 1 | 0 % ± 0 | 12.9 ± 0.7 | 10.5 ± 0.5 | 0 |
| t20-amenaza-blaze-linea-base-r6 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 9 ± 0 | 143 ± 1 | 17 ± 1 | 85 % ± 1 | 19.3 ± 0.4 | 7.0 ± 0.3 | 0 |
| t20-amenaza-blaze-linea-sinamenazas-r6 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 10 ± 0 | 143 ± 1 | 18 ± 1 | 86 % ± 1 | 18.1 ± 0.5 | 8.9 ± 0.4 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 43 ± 2 | 238 ± 0 | 21 ± 1 | 0 % ± 0 | 13.3 ± 0.6 | 12.1 ± 0.7 | 0 |
| t20-amenaza-blaze-escudo-sinamenazas-r10 | 30 | 98 % ± 2 | 2 % ± 2 | 7.5 ± 0.3 | 0.1 ± 0.1 | 31 ± 5 | 234 ± 3 | 33 ± 2 | 0 % ± 0 | 13.0 ± 0.8 | 12.1 ± 0.6 | 0 |
| t20-amenaza-blaze-scutum-base-r10 | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 38 ± 1 | 239 ± 0 | 22 ± 1 | 0 % ± 0 | 12.9 ± 0.5 | 11.7 ± 0.5 | 0 |
| t20-amenaza-blaze-scutum-sinamenazas-r10 | 30 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.1 | 0.0 ± 0.0 | 28 ± 1 | 238 ± 1 | 36 ± 1 | 0 % ± 0 | 11.9 ± 0.5 | 11.7 ± 0.5 | 0 |
| t20-amenaza-blaze-linea-base-r10 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 1 | 239 ± 1 | 23 ± 1 | 88 % ± 1 | 18.5 ± 0.5 | 8.6 ± 0.4 | 0 |
| t20-amenaza-blaze-linea-sinamenazas-r10 | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 14 ± 1 | 237 ± 1 | 25 ± 1 | 89 % ± 1 | 17.2 ± 0.4 | 10.5 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t20-amenaza-blaze-escudo-base-r6 semilla 101: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 360 de 360 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t20-amenaza-blaze-scutum-sinamenazas-r6: env:onFire=1
- t20-amenaza-blaze-escudo-base-r10: env:onFire=3
- t20-amenaza-blaze-escudo-sinamenazas-r10: enemy:fireball=5 enemy:mob=1 env:inFire=1 env:onFire=9
- t20-amenaza-blaze-scutum-base-r10: env:onFire=3
- t20-amenaza-blaze-scutum-sinamenazas-r10: enemy:fireball=2 env:inFire=2 env:onFire=8
- t20-amenaza-blaze-linea-base-r10: env:onFire=1
- t20-amenaza-blaze-linea-sinamenazas-r10: enemy:fireball=1 env:onFire=2

## Motivos de decisión (cambios registrados): ENGAGE 2407, LOW_HEALTH_RETREAT 184, MORALE_SHAKEN 124, MORALE_WAVERING 26, NONE 974, ROUTED_FLEE 4, SEEK_ENEMY 420, STUCK 77, TOO_CLOSE 46

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: t20-amenaza-blaze-scutum-sinamenazas-r6 - 100 % de las bajas azules ({env:onFire=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t20-amenaza-blaze-escudo-base-r10 - 100 % de las bajas azules ({env:onFire=3.0})
- MUERTES QUE NO SON DEL ENEMIGO: t20-amenaza-blaze-escudo-sinamenazas-r10 - 63 % de las bajas azules ({enemy:fireball=5.0, enemy:mob=1.0, env:inFire=1.0, env:onFire=9.0})
- MUERTES QUE NO SON DEL ENEMIGO: t20-amenaza-blaze-scutum-base-r10 - 100 % de las bajas azules ({env:onFire=3.0})
- MUERTES QUE NO SON DEL ENEMIGO: t20-amenaza-blaze-scutum-sinamenazas-r10 - 83 % de las bajas azules ({enemy:fireball=2.0, env:inFire=2.0, env:onFire=8.0})
- MUERTES QUE NO SON DEL ENEMIGO: t20-amenaza-blaze-linea-base-r10 - 100 % de las bajas azules ({env:onFire=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t20-amenaza-blaze-linea-sinamenazas-r10 - 67 % de las bajas azules ({enemy:fireball=1.0, env:onFire=2.0})
