# Simulación Rally - lote 20261105-131954-t11-cab-check

Preset `t11-cab-check` · velocidad `sprint` · 163 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t11-creeper-cab-base | 25 | 76 % ± 9 | 24 % ± 9 | 4.6 ± 0.7 | 2.1 ± 0.9 | 38 ± 2 | 343 ± 35 | 40 ± 8 | 88 % ± 1 | 11.8 ± 1.0 | 12.0 ± 0.9 | 15 |
| t11-creeper-cab-sinesquiva | 29 | 100 % ± 0 | 0 % ± 0 | 4.2 ± 0.3 | 0.0 ± 0.0 | 21 ± 1 | 158 ± 8 | 121 ± 6 | 86 % ± 2 | 11.2 ± 0.2 | 8.9 ± 0.3 | 12 |
| t11-skeleton-cab-base | 18 | 50 % ± 9 | 50 % ± 9 | 2.6 ± 0.5 | 5.1 ± 1.3 | 88 ± 7 | 550 ± 20 | 146 ± 9 | 81 % ± 2 | 10.7 ± 0.6 | 6.0 ± 0.6 | 26 |
| t11-skeleton-cab-antes | 26 | 27 % ± 8 | 73 % ± 8 | 1.5 ± 0.5 | 13.3 ± 2.2 | 56 ± 5 | 388 ± 36 | 144 ± 8 | 91 % ± 2 | 6.9 ± 0.3 | 5.8 ± 0.4 | 10 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t11-creeper-cab-base semilla 1001: sprint → blue (8/0 vivos), normal → draw (3/12 vivos)
- t11-creeper-cab-base semilla 8778: sprint → ? (0/0 vivos), normal → red (0/24 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 69 de 69 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t11-creeper-cab-base: enemy:explosion.player=37 env:inWall=49
- t11-creeper-cab-sinesquiva: enemy:explosion.player=101 env:inWall=10
- t11-skeleton-cab-base: enemy:arrow=95 env:inWall=3
- t11-skeleton-cab-antes: enemy:arrow=140 env:inWall=28

## Motivos de decisión (cambios registrados): COUNTERATTACK 30, ENGAGE 2691, FORMATION_HOLD 645, LOW_HEALTH_RETREAT 448, MORALE_SHAKEN 1494, MORALE_WAVERING 952, NONE 124, OUT_OF_RANGE 20, RALLIED 8, REFUSE_ATTACK 220, ROUTED_FLEE 632, SEEK_ENEMY 764, TOO_CLOSE 618

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t11-creeper-cab-sinesquiva - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t11-creeper-cab-base - 15 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t11-skeleton-cab-base - 26 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t11-skeleton-cab-antes - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t11-creeper-cab-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
- MUERTES QUE NO SON DEL ENEMIGO: t11-creeper-cab-base - 57 % de las bajas azules ({enemy:explosion.player=37.0, env:inWall=49.0})
- MUERTES QUE NO SON DEL ENEMIGO: t11-skeleton-cab-antes - 17 % de las bajas azules ({enemy:arrow=140.0, env:inWall=28.0})
