# Simulación Rally - lote 20261105-073419-t8-creeper

Preset `t8-creeper` · velocidad `sprint` · 327 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-creeper-arco-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.1 | 0.0 ± 0.0 | 25 ± 0 | 512 ± 12 | 26 ± 5 | 86 % ± 1 | 9.0 ± 0.2 | 6.8 ± 0.3 | 0 |
| t8-creeper-arco-sinesquiva | 30 | 98 % ± 2 | 2 % ± 2 | 5.6 ± 0.4 | 0.0 ± 0.0 | 15 ± 0 | 268 ± 10 | 102 ± 9 | 84 % ± 1 | 9.6 ± 0.1 | 5.4 ± 0.2 | 0 |
| t8-creeper-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.2 | 0.0 ± 0.0 | 25 ± 1 | 321 ± 10 | 14 ± 5 | 90 % ± 1 | 9.3 ± 0.1 | 7.3 ± 0.2 | 0 |
| t8-creeper-linea-sinesquiva | 30 | 100 % ± 0 | 0 % ± 0 | 5.8 ± 0.3 | 0.0 ± 0.0 | 14 ± 0 | 178 ± 7 | 94 ± 7 | 90 % ± 1 | 10.8 ± 0.1 | 5.6 ± 0.2 | 0 |
| t8-creeper-escudo-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 28 ± 2 | 72 ± 4 | 19 ± 3 | 0 % ± 0 | 10.8 ± 0.5 | 7.3 ± 0.3 | 1 |
| t8-creeper-escudo-sinesquiva | 30 | 100 % ± 0 | 0 % ± 0 | 6.1 ± 0.2 | 0.0 ± 0.0 | 14 ± 0 | 68 ± 4 | 98 ± 5 | 0 % ± 0 | 11.6 ± 0.1 | 5.8 ± 0.2 | 0 |
| t8-creeper-cab-base | 29 | 98 % ± 2 | 2 % ± 2 | 3.9 ± 0.3 | 0.0 ± 0.0 | 18 ± 0 | 142 ± 7 | 133 ± 6 | 85 % ± 2 | 11.7 ± 0.1 | 8.7 ± 0.3 | 2 |
| t8-creeper-cab-sinesquiva | 29 | 100 % ± 0 | 0 % ± 0 | 4.7 ± 0.3 | 0.0 ± 0.0 | 18 ± 1 | 155 ± 8 | 117 ± 6 | 87 % ± 1 | 11.9 ± 0.2 | 8.9 ± 0.3 | 2 |
| t8-creeper-mele-base | 21 | 81 % ± 5 | 19 % ± 5 | 4.0 ± 0.4 | 1.4 ± 0.5 | 75 ± 8 | 0 ± 0 | 142 ± 8 | 0 % ± 0 | 14.4 ± 1.0 | 16.7 ± 2.1 | 22 |
| t8-creeper-mele-sinesquiva | 22 | 95 % ± 3 | 5 % ± 3 | 2.8 ± 0.3 | 0.0 ± 0.0 | 25 ± 1 | 0 ± 0 | 181 ± 8 | 0 % ± 0 | 12.9 ± 0.2 | 7.0 ± 0.3 | 18 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-creeper-arco-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 276 de 276 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t8-creeper-arco-base: enemy:explosion.player=11
- t8-creeper-arco-sinesquiva: enemy:explosion.player=71
- t8-creeper-linea-base: enemy:explosion.player=8
- t8-creeper-linea-sinesquiva: enemy:explosion.player=64 env:fall=1
- t8-creeper-escudo-base: enemy:explosion.player=5
- t8-creeper-escudo-sinesquiva: enemy:explosion.player=58
- t8-creeper-cab-base: enemy:explosion.player=111 env:inWall=7
- t8-creeper-cab-sinesquiva: enemy:explosion.player=84 env:inWall=12
- t8-creeper-mele-base: enemy:explosion.player=83
- t8-creeper-mele-sinesquiva: enemy:explosion.player=114

## Motivos de decisión (cambios registrados): ENGAGE 3809, FORMATION_HOLD 155, LOW_HEALTH_RETREAT 308, MORALE_SHAKEN 2443, MORALE_WAVERING 650, NONE 280, OUT_OF_RANGE 2, RALLIED 26, REFUSE_ATTACK 41, ROUTED_FLEE 380, SEEK_ENEMY 2635, STUCK 62, TOO_CLOSE 879

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-creeper-escudo-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t8-creeper-cab-base - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t8-creeper-mele-sinesquiva - 18 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t8-creeper-cab-sinesquiva - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t8-creeper-mele-base - 22 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t8-creeper-cab-sinesquiva - 13 % de las bajas azules ({enemy:explosion.player=84.0, env:inWall=12.0})
