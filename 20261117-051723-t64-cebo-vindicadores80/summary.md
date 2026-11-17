# Simulación Rally - lote 20261117-051723-t64-cebo-vindicadores80

Preset `t64-cebo-vindicadores80` · velocidad `sprint` · 143 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t64-cebo-todos | 28 | 96 % ± 4 | 4 % ± 4 | 6.1 ± 0.4 | 0.9 ± 0.9 | 97 ± 6 | 2175 ± 15 | 282 ± 10 | 90 % ± 1 | 20.3 ± 1.5 | 21.1 ± 0.9 | 8 |
| t64-cebo-cebo | 28 | 98 % ± 2 | 2 % ± 2 | 6.9 ± 0.3 | 0.2 ± 0.2 | 99 ± 8 | 2190 ± 6 | 265 ± 10 | 90 % ± 1 | 21.0 ± 1.8 | 23.3 ± 1.7 | 7 |
| t64-cebo-pinza | 28 | 55 % ± 9 | 45 % ± 9 | 1.5 ± 0.3 | 24.3 ± 5.9 | 165 ± 13 | 1655 ± 139 | 459 ± 7 | 93 % ± 1 | 12.3 ± 0.9 | 20.3 ± 2.1 | 6 |
| t64-cebo-cebopinza | 27 | 41 % ± 7 | 59 % ± 7 | 1.1 ± 0.2 | 32.1 ± 4.6 | 192 ± 12 | 1558 ± 103 | 475 ± 7 | 94 % ± 1 | 13.1 ± 1.4 | 21.3 ± 2.0 | 10 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t64-cebo-todos semilla 6401: sprint → blue (8/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 90 de 90 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t64-cebo-todos: enemy:mob=278
- t64-cebo-cebo: enemy:mob=254
- t64-cebo-pinza: enemy:mob=407
- t64-cebo-cebopinza: enemy:mob=403

## Motivos de decisión (cambios registrados): ENGAGE 13334, FORMATION_HOLD 1226, INSTRUCTION 21426, LOW_HEALTH_RETREAT 105, ORDER_OVERRIDE 1736, OUT_OF_RANGE 1986, SEEK_ENEMY 8763, TOO_CLOSE 555, UNIT_BROKEN 96

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t64-cebo-cebo - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t64-cebo-cebopinza - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t64-cebo-todos - 8 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t64-cebo-pinza - 6 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
