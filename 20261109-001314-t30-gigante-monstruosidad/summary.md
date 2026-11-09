# Simulación Rally - lote 20261109-001314-t30-gigante-monstruosidad

Preset `t30-gigante-monstruosidad` · velocidad `sprint` · 28 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t30-gigante-monstruosidad-hierro | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 152 ± 22 | 418 ± 38 | 645 ± 30 | 40 % ± 3 | 21.9 ± 1.7 | 12.7 ± 1.3 | 0 |
| t30-gigante-monstruosidad-diamante | 6 | 17 % ± 11 | 83 % ± 11 | 1.0 ± 0.8 | 1.0 ± 0.0 | 261 ± 13 | 409 ± 32 | 632 ± 29 | 42 % ± 4 | 27.4 ± 1.8 | 18.4 ± 2.5 | 1 |
| t30-gigante-monstruosidad-netherita | 5 | 20 % ± 20 | 80 % ± 20 | 1.2 ± 1.2 | 0.8 ± 0.2 | 227 ± 26 | 429 ± 64 | 572 ± 41 | 45 % ± 7 | 28.6 ± 2.4 | 21.4 ± 2.3 | 3 |
| t30-gigante-monstruosidad-netheritamas | 6 | 100 % ± 0 | 0 % ± 0 | 13.2 ± 0.6 | 0.0 ± 0.0 | 78 ± 19 | 623 ± 14 | 299 ± 22 | 53 % ± 4 | 19.3 ± 2.4 | 14.3 ± 2.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t30-gigante-monstruosidad-hierro semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 9 de 9 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t30-gigante-monstruosidad-hierro: enemy:explosion.player=2 enemy:mob=76 env:lava=16 env:onFire=2
- t30-gigante-monstruosidad-diamante: enemy:explosion.player=1 enemy:mob=68 env:fall=5 env:lava=11 env:onFire=5
- t30-gigante-monstruosidad-netherita: enemy:explosion.player=1 enemy:mob=55 env:lava=10 env:onFire=8
- t30-gigante-monstruosidad-netheritamas: enemy:mob=14 env:lava=3

## Motivos de decisión (cambios registrados): COUNTERATTACK 109, ENGAGE 958, LOW_HEALTH_RETREAT 239, MORALE_SHAKEN 564, MORALE_WAVERING 407, NONE 311, OUT_OF_RANGE 5, RALLIED 53, REFUSE_ATTACK 36, ROUTED_FLEE 427, SEEK_ENEMY 50, STUCK 14, TOO_CLOSE 15

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t30-gigante-monstruosidad-netherita - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t30-gigante-monstruosidad-diamante - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t30-gigante-monstruosidad-hierro - 19 % de las bajas azules ({enemy:explosion.player=2.0, enemy:mob=76.0, env:lava=16.0, env:onFire=2.0})
- MUERTES QUE NO SON DEL ENEMIGO: t30-gigante-monstruosidad-diamante - 23 % de las bajas azules ({enemy:explosion.player=1.0, enemy:mob=68.0, env:fall=5.0, env:lava=11.0, env:onFire=5.0})
- MUERTES QUE NO SON DEL ENEMIGO: t30-gigante-monstruosidad-netherita - 24 % de las bajas azules ({enemy:explosion.player=1.0, enemy:mob=55.0, env:lava=10.0, env:onFire=8.0})
- MUERTES QUE NO SON DEL ENEMIGO: t30-gigante-monstruosidad-netheritamas - 18 % de las bajas azules ({enemy:mob=14.0, env:lava=3.0})
