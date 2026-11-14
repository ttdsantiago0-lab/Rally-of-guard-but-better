# Simulación Rally - lote 20261114-192512-t57-horda-vindicadores

Preset `t57-horda-vindicadores` · velocidad `sprint` · 26 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t57-horda-vindicadores48-linea-todos | 8 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.2 | 0.0 ± 0.0 | 36 ± 6 | 1306 ± 6 | 246 ± 5 | 92 % ± 1 | 14.6 ± 3.4 | 16.6 ± 2.1 | 0 |
| t57-horda-vindicadores64-linea-todos | 8 | 88 % ± 8 | 13 % ± 8 | 7.3 ± 0.4 | 0.5 ± 0.4 | 95 ± 9 | 1736 ± 8 | 261 ± 13 | 89 % ± 1 | 28.4 ± 3.1 | 26.6 ± 2.0 | 1 |
| t57-horda-vindicadores80-linea-todos | 8 | 81 % ± 9 | 19 % ± 9 | 5.3 ± 0.7 | 5.5 ± 4.8 | 109 ± 5 | 2104 ± 81 | 322 ± 23 | 90 % ± 1 | 19.6 ± 3.2 | 26.8 ± 2.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t57-horda-vindicadores48-linea-todos semilla 5701: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 24 de 24 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t57-horda-vindicadores48-linea-todos: enemy:mob=68
- t57-horda-vindicadores64-linea-todos: enemy:mob=70
- t57-horda-vindicadores80-linea-todos: enemy:mob=86

## Motivos de decisión (cambios registrados): ENGAGE 657, FORMATION_HOLD 242, LOW_HEALTH_RETREAT 43, OUT_OF_RANGE 88, SEEK_ENEMY 278, TOO_CLOSE 21, UNIT_BROKEN 38

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t57-horda-vindicadores64-linea-todos - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
