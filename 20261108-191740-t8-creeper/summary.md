# Simulación Rally - lote 20261108-191740-t8-creeper

Preset `t8-creeper` · velocidad `sprint` · 315 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-creeper-arco-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 0 | 580 ± 3 | 0 ± 0 | 86 % ± 1 | 9.4 ± 0.1 | 6.9 ± 0.2 | 0 |
| t8-creeper-arco-sinesquiva | 30 | 100 % ± 0 | 0 % ± 0 | 6.1 ± 0.2 | 0.0 ± 0.0 | 13 ± 0 | 282 ± 6 | 92 ± 6 | 89 % ± 1 | 9.8 ± 0.1 | 5.3 ± 0.2 | 0 |
| t8-creeper-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 1 | 349 ± 14 | 5 ± 1 | 87 % ± 1 | 10.1 ± 0.2 | 7.9 ± 0.3 | 0 |
| t8-creeper-linea-sinesquiva | 30 | 100 % ± 0 | 0 % ± 0 | 5.7 ± 0.2 | 0.0 ± 0.0 | 12 ± 0 | 179 ± 6 | 97 ± 5 | 93 % ± 1 | 10.9 ± 0.1 | 5.8 ± 0.2 | 0 |
| t8-creeper-escudo-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 32 ± 2 | 76 ± 4 | 12 ± 2 | 0 % ± 0 | 12.3 ± 0.7 | 8.6 ± 0.5 | 5 |
| t8-creeper-escudo-sinesquiva | 30 | 100 % ± 0 | 0 % ± 0 | 6.3 ± 0.2 | 0.0 ± 0.0 | 12 ± 0 | 54 ± 3 | 87 ± 6 | 0 % ± 0 | 11.7 ± 0.2 | 5.5 ± 0.2 | 0 |
| t8-creeper-cab-base | 30 | 100 % ± 0 | 0 % ± 0 | 4.6 ± 0.3 | 0.0 ± 0.0 | 16 ± 0 | 131 ± 7 | 126 ± 6 | 89 % ± 1 | 12.4 ± 0.2 | 9.5 ± 0.3 | 0 |
| t8-creeper-cab-sinesquiva | 30 | 98 % ± 2 | 2 % ± 2 | 4.3 ± 0.3 | 0.0 ± 0.0 | 17 ± 1 | 131 ± 5 | 134 ± 6 | 92 % ± 1 | 12.0 ± 0.1 | 8.7 ± 0.3 | 0 |
| t8-creeper-mele-base | 28 | 57 % ± 3 | 43 % ± 3 | 4.4 ± 0.3 | 5.0 ± 0.7 | 117 ± 2 | 0 ± 0 | 124 ± 6 | 0 % ± 0 | 16.0 ± 1.1 | 35.8 ± 1.3 | 11 |
| t8-creeper-mele-sinesquiva | 30 | 100 % ± 0 | 0 % ± 0 | 3.9 ± 0.3 | 0.0 ± 0.0 | 20 ± 0 | 0 ± 0 | 150 ± 5 | 0 % ± 0 | 13.0 ± 0.1 | 8.0 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-creeper-arco-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 297 de 297 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t8-creeper-arco-sinesquiva: enemy:explosion.player=56
- t8-creeper-linea-base: enemy:explosion.player=1
- t8-creeper-linea-sinesquiva: enemy:explosion.player=68
- t8-creeper-escudo-sinesquiva: enemy:explosion.player=51
- t8-creeper-cab-base: enemy:explosion.player=102
- t8-creeper-cab-sinesquiva: enemy:explosion.player=110
- t8-creeper-mele-base: enemy:explosion.player=100
- t8-creeper-mele-sinesquiva: enemy:explosion.player=122

## Motivos de decisión (cambios registrados): ENGAGE 3227, FORMATION_HOLD 169, LOW_HEALTH_RETREAT 213, MORALE_SHAKEN 796, MORALE_WAVERING 223, NONE 544, RALLIED 15, REFUSE_ATTACK 2, ROUTED_FLEE 183, SEEK_ENEMY 2502, STUCK 192, TOO_CLOSE 642

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t8-creeper-mele-base - 11 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t8-creeper-escudo-base - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t8-creeper-mele-base: 24/28 corridas llegaron al tope de tiempo
