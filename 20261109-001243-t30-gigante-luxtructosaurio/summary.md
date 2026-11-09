# Simulación Rally - lote 20261109-001243-t30-gigante-luxtructosaurio

Preset `t30-gigante-luxtructosaurio` · velocidad `sprint` · 29 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t30-gigante-luxtructosaurio-hierro | 6 | 33 % ± 11 | 67 % ± 11 | 4.0 ± 1.3 | 1.0 ± 0.0 | 270 ± 24 | 113 ± 14 | 580 ± 30 | 83 % ± 2 | 45.0 ± 2.4 | 30.7 ± 2.1 | 0 |
| t30-gigante-luxtructosaurio-diamante | 6 | 50 % ± 0 | 50 % ± 0 | 8.7 ± 0.5 | 1.0 ± 0.0 | 300 ± 0 | 139 ± 18 | 478 ± 25 | 87 % ± 1 | 46.3 ± 2.3 | 33.9 ± 1.2 | 2 |
| t30-gigante-luxtructosaurio-netherita | 6 | 50 % ± 0 | 50 % ± 0 | 7.3 ± 1.2 | 1.0 ± 0.0 | 300 ± 0 | 188 ± 40 | 565 ± 51 | 82 % ± 2 | 39.9 ± 2.5 | 31.1 ± 2.0 | 1 |
| t30-gigante-luxtructosaurio-netheritamas | 6 | 50 % ± 0 | 50 % ± 0 | 13.0 ± 0.5 | 1.0 ± 0.0 | 300 ± 0 | 499 ± 53 | 408 ± 67 | 82 % ± 3 | 43.6 ± 2.8 | 30.4 ± 1.0 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t30-gigante-luxtructosaurio-hierro semilla 1: sprint → red (0/1 vivos), normal → draw (2/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 22 de 22 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t30-gigante-luxtructosaurio-hierro: enemy:explosion.player=4 enemy:mob=57 env:inFire=1 env:onFire=10
- t30-gigante-luxtructosaurio-diamante: enemy:explosion.player=5 enemy:mob=29 env:onFire=10
- t30-gigante-luxtructosaurio-netherita: enemy:explosion.player=3 enemy:mob=38 env:inFire=3 env:onFire=8
- t30-gigante-luxtructosaurio-netheritamas: enemy:explosion.player=3 enemy:mob=13 env:onFire=2

## Motivos de decisión (cambios registrados): COUNTERATTACK 9, ENGAGE 1609, LOW_HEALTH_RETREAT 229, MORALE_SHAKEN 777, MORALE_WAVERING 591, NONE 612, OUT_OF_RANGE 68, RALLIED 110, REFUSE_ATTACK 8, ROUTED_FLEE 518, SEEK_ENEMY 308, STUCK 35, TOO_CLOSE 17, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t30-gigante-luxtructosaurio-diamante - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t30-gigante-luxtructosaurio-netherita - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t30-gigante-luxtructosaurio-netheritamas - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t30-gigante-luxtructosaurio-hierro: 4/6 corridas llegaron al tope de tiempo
- t30-gigante-luxtructosaurio-diamante: 6/6 corridas llegaron al tope de tiempo
- t30-gigante-luxtructosaurio-netherita: 6/6 corridas llegaron al tope de tiempo
- t30-gigante-luxtructosaurio-netheritamas: 6/6 corridas llegaron al tope de tiempo
- t30-gigante-luxtructosaurio-hierro: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
- MUERTES QUE NO SON DEL ENEMIGO: t30-gigante-luxtructosaurio-hierro - 15 % de las bajas azules ({enemy:explosion.player=4.0, enemy:mob=57.0, env:inFire=1.0, env:onFire=10.0})
- MUERTES QUE NO SON DEL ENEMIGO: t30-gigante-luxtructosaurio-diamante - 23 % de las bajas azules ({enemy:explosion.player=5.0, enemy:mob=29.0, env:onFire=10.0})
- MUERTES QUE NO SON DEL ENEMIGO: t30-gigante-luxtructosaurio-netherita - 21 % de las bajas azules ({enemy:explosion.player=3.0, enemy:mob=38.0, env:inFire=3.0, env:onFire=8.0})
- MUERTES QUE NO SON DEL ENEMIGO: t30-gigante-luxtructosaurio-netheritamas - 11 % de las bajas azules ({enemy:explosion.player=3.0, enemy:mob=13.0, env:onFire=2.0})
