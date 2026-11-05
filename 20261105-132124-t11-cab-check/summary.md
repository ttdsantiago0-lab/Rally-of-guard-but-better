# Simulación Rally - lote 20261105-132124-t11-cab-check

Preset `t11-cab-check` · velocidad `sprint` · 163 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t11-creeper-cab-base | 19 | 89 % ± 6 | 11 % ± 6 | 5.7 ± 0.6 | 0.5 ± 0.4 | 55 ± 7 | 405 ± 37 | 37 ± 8 | 79 % ± 3 | 14.7 ± 1.6 | 14.3 ± 1.1 | 24 |
| t11-creeper-cab-sinesquiva | 25 | 90 % ± 5 | 10 % ± 5 | 4.3 ± 0.4 | 0.1 ± 0.1 | 27 ± 5 | 155 ± 9 | 127 ± 6 | 79 % ± 4 | 11.4 ± 0.3 | 8.9 ± 0.4 | 14 |
| t11-skeleton-cab-base | 23 | 46 % ± 7 | 54 % ± 7 | 2.6 ± 0.4 | 10.0 ± 2.0 | 96 ± 6 | 485 ± 27 | 132 ± 10 | 81 % ± 2 | 10.6 ± 0.5 | 6.1 ± 0.5 | 21 |
| t11-skeleton-cab-antes | 28 | 32 % ± 9 | 68 % ± 9 | 1.8 ± 0.5 | 12.1 ± 1.9 | 54 ± 5 | 402 ± 34 | 140 ± 7 | 93 % ± 1 | 7.2 ± 0.2 | 6.5 ± 0.4 | 8 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t11-creeper-cab-base semilla 1: sprint → blue (7/0 vivos), normal → red (0/2 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 67 de 67 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t11-creeper-cab-base: enemy:explosion.player=23 env:inWall=20
- t11-creeper-cab-sinesquiva: enemy:explosion.player=82 env:inWall=10
- t11-skeleton-cab-base: enemy:arrow=113 env:inWall=12
- t11-skeleton-cab-antes: enemy:arrow=140 env:inWall=35

## Motivos de decisión (cambios registrados): COUNTERATTACK 32, ENGAGE 2790, FORMATION_HOLD 404, LOW_HEALTH_RETREAT 424, MORALE_SHAKEN 1539, MORALE_WAVERING 982, NONE 126, OUT_OF_RANGE 31, RALLIED 18, REFUSE_ATTACK 209, ROUTED_FLEE 711, SEEK_ENEMY 748, TOO_CLOSE 657

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t11-creeper-cab-base - 24 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t11-skeleton-cab-base - 21 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t11-skeleton-cab-antes - 8 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t11-creeper-cab-sinesquiva - 14 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t11-skeleton-cab-base: 13/23 corridas llegaron al tope de tiempo
- t11-creeper-cab-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
- MUERTES QUE NO SON DEL ENEMIGO: t11-creeper-cab-base - 47 % de las bajas azules ({enemy:explosion.player=23.0, env:inWall=20.0})
- MUERTES QUE NO SON DEL ENEMIGO: t11-creeper-cab-sinesquiva - 11 % de las bajas azules ({enemy:explosion.player=82.0, env:inWall=10.0})
- MUERTES QUE NO SON DEL ENEMIGO: t11-skeleton-cab-antes - 20 % de las bajas azules ({enemy:arrow=140.0, env:inWall=35.0})
