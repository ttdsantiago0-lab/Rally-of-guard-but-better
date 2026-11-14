# Simulación Rally - lote 20261114-191336-t56-unidades-vindicadores32

Preset `t56-unidades-vindicadores32` · velocidad `sprint` · 25 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t56-unidades-vindicadores32-linea-todos | 8 | 100 % ± 0 | 0 % ± 0 | 13.0 ± 0.6 | 0.0 ± 0.0 | 16 ± 1 | 861 ± 4 | 148 ± 10 | 92 % ± 2 | 9.3 ± 0.4 | 11.9 ± 0.3 | 0 |
| t56-unidades-vindicadores32-linea-retira12 | 8 | 100 % ± 0 | 0 % ± 0 | 12.8 ± 0.4 | 0.0 ± 0.0 | 16 ± 0 | 861 ± 4 | 152 ± 15 | 90 % ± 1 | 9.5 ± 0.3 | 12.2 ± 0.4 | 0 |
| t56-unidades-vindicadores32-linea-flanco34 | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.7 | 0.0 ± 0.0 | 60 ± 7 | 877 ± 5 | 260 ± 12 | 88 % ± 2 | 31.7 ± 4.6 | 19.8 ± 1.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t56-unidades-vindicadores32-linea-todos semilla 5601: sprint → blue (10/0 vivos), normal → blue (13/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 24 de 24 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t56-unidades-vindicadores32-linea-todos: enemy:mob=24
- t56-unidades-vindicadores32-linea-retira12: enemy:mob=26
- t56-unidades-vindicadores32-linea-flanco34: enemy:mob=66

## Motivos de decisión (cambios registrados): ENGAGE 426, FORMATION_HOLD 74, INSTRUCTION 28, LOW_HEALTH_RETREAT 42, OUT_OF_RANGE 15, SEEK_ENEMY 114, TOO_CLOSE 14, UNIT_BROKEN 16

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
