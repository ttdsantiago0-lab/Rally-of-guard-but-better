# Simulación Rally - lote 20261108-191750-t20-amenaza-blaze

Preset `t20-amenaza-blaze` · velocidad `sprint` · 361 corridas · 12 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t20-amenaza-blaze-escudo-base-r6 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 1 | 144 ± 0 | 9 ± 1 | 0 % ± 0 | 8.0 ± 0.1 | 4.7 ± 0.1 | 0 |
| t20-amenaza-blaze-escudo-sinamenazas-r6 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 18 ± 1 | 144 ± 0 | 19 ± 1 | 0 % ± 0 | 7.8 ± 0.1 | 4.8 ± 0.1 | 0 |
| t20-amenaza-blaze-scutum-base-r6 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 27 ± 1 | 144 ± 0 | 12 ± 1 | 0 % ± 0 | 8.1 ± 0.1 | 4.7 ± 0.1 | 0 |
| t20-amenaza-blaze-scutum-sinamenazas-r6 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 18 ± 0 | 144 ± 0 | 20 ± 1 | 0 % ± 0 | 7.7 ± 0.1 | 4.7 ± 0.1 | 0 |
| t20-amenaza-blaze-linea-base-r6 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 11 ± 0 | 145 ± 1 | 16 ± 1 | 85 % ± 1 | 16.2 ± 0.2 | 5.1 ± 0.1 | 0 |
| t20-amenaza-blaze-linea-sinamenazas-r6 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 10 ± 0 | 143 ± 1 | 19 ± 1 | 84 % ± 2 | 14.4 ± 0.1 | 6.6 ± 0.1 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 33 ± 1 | 240 ± 0 | 22 ± 2 | 0 % ± 0 | 7.9 ± 0.2 | 6.3 ± 0.2 | 0 |
| t20-amenaza-blaze-escudo-sinamenazas-r10 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 23 ± 1 | 240 ± 0 | 33 ± 1 | 0 % ± 0 | 7.4 ± 0.1 | 6.6 ± 0.2 | 0 |
| t20-amenaza-blaze-scutum-base-r10 | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 34 ± 1 | 240 ± 0 | 22 ± 1 | 0 % ± 0 | 8.0 ± 0.1 | 6.4 ± 0.2 | 0 |
| t20-amenaza-blaze-scutum-sinamenazas-r10 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 22 ± 1 | 240 ± 0 | 31 ± 1 | 0 % ± 0 | 7.6 ± 0.2 | 6.7 ± 0.2 | 0 |
| t20-amenaza-blaze-linea-base-r10 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 15 ± 0 | 239 ± 1 | 25 ± 1 | 87 % ± 1 | 14.3 ± 0.2 | 6.0 ± 0.1 | 0 |
| t20-amenaza-blaze-linea-sinamenazas-r10 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 13 ± 0 | 237 ± 1 | 26 ± 1 | 87 % ± 1 | 13.2 ± 0.1 | 7.7 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t20-amenaza-blaze-escudo-base-r6 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 360 de 360 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t20-amenaza-blaze-escudo-base-r10: env:onFire=1
- t20-amenaza-blaze-escudo-sinamenazas-r10: env:onFire=1
- t20-amenaza-blaze-scutum-base-r10: env:onFire=5

## Motivos de decisión (cambios registrados): ENGAGE 2982, LOW_HEALTH_RETREAT 101, MORALE_SHAKEN 1, NONE 16, SEEK_ENEMY 833, STUCK 26

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: t20-amenaza-blaze-escudo-base-r10 - 100 % de las bajas azules ({env:onFire=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t20-amenaza-blaze-escudo-sinamenazas-r10 - 100 % de las bajas azules ({env:onFire=1.0})
- MUERTES QUE NO SON DEL ENEMIGO: t20-amenaza-blaze-scutum-base-r10 - 100 % de las bajas azules ({env:onFire=5.0})
