# Simulación Rally - lote 20261105-134507-t11-cab-check

Preset `t11-cab-check` · velocidad `sprint` · 148 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t11-creeper-cab-base | 25 | 100 % ± 0 | 0 % ± 0 | 4.1 ± 0.4 | 0.0 ± 0.0 | 25 ± 4 | 161 ± 13 | 128 ± 6 | 81 % ± 2 | 11.1 ± 0.2 | 8.6 ± 0.4 | 15 |
| t11-creeper-cab-sinesquiva | 27 | 98 % ± 2 | 2 % ± 2 | 4.2 ± 0.4 | 0.0 ± 0.0 | 20 ± 1 | 149 ± 8 | 119 ± 5 | 81 % ± 3 | 11.4 ± 0.3 | 9.1 ± 0.3 | 15 |
| t11-skeleton-cab-base | 30 | 40 % ± 9 | 60 % ± 9 | 1.9 ± 0.5 | 7.3 ± 1.3 | 37 ± 2 | 532 ± 24 | 117 ± 6 | 87 % ± 1 | 6.2 ± 0.2 | 5.6 ± 0.3 | 3 |
| t11-skeleton-cab-antes | 30 | 12 % ± 6 | 88 % ± 6 | 0.6 ± 0.3 | 13.2 ± 1.5 | 41 ± 3 | 402 ± 23 | 136 ± 5 | 92 % ± 1 | 5.6 ± 0.3 | 5.5 ± 0.3 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t11-creeper-cab-base semilla 2001: sprint → blue (8/0 vivos), normal → draw (2/2 vivos)
- t11-creeper-cab-base semilla 9778: sprint → ? (0/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 67 de 67 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t11-creeper-cab-base: enemy:explosion.player=81 env:inWall=16
- t11-creeper-cab-sinesquiva: enemy:explosion.player=93 env:inWall=9
- t11-skeleton-cab-base: enemy:arrow=115 env:inWall=68
- t11-skeleton-cab-antes: enemy:arrow=170 env:inWall=53

## Motivos de decisión (cambios registrados): COUNTERATTACK 23, ENGAGE 1984, FORMATION_HOLD 455, LOW_HEALTH_RETREAT 488, MORALE_SHAKEN 1244, MORALE_WAVERING 705, NONE 86, RALLIED 18, REFUSE_ATTACK 278, ROUTED_FLEE 440, SEEK_ENEMY 618, TOO_CLOSE 675

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t11-creeper-cab-base - 15 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t11-creeper-cab-sinesquiva - 15 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t11-skeleton-cab-antes - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t11-skeleton-cab-base - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t11-creeper-cab-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
- MUERTES QUE NO SON DEL ENEMIGO: t11-creeper-cab-base - 16 % de las bajas azules ({enemy:explosion.player=81.0, env:inWall=16.0})
- MUERTES QUE NO SON DEL ENEMIGO: t11-skeleton-cab-base - 37 % de las bajas azules ({enemy:arrow=115.0, env:inWall=68.0})
- MUERTES QUE NO SON DEL ENEMIGO: t11-skeleton-cab-antes - 24 % de las bajas azules ({enemy:arrow=170.0, env:inWall=53.0})
