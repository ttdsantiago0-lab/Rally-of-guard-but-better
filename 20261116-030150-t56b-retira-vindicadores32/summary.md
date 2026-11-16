# Simulación Rally - lote 20261116-030150-t56b-retira-vindicadores32

Preset `t56b-retira-vindicadores32` · velocidad `sprint` · 9 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t56b-unidades-vindicadores32-linea-retira12 | 8 | 100 % ± 0 | 0 % ± 0 | 11.8 ± 0.5 | 0.0 ± 0.0 | 21 ± 2 | 860 ± 4 | 165 ± 12 | 90 % ± 1 | 13.5 ± 1.9 | 12.0 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t56b-unidades-vindicadores32-linea-retira12 semilla 5601: sprint → blue (10/0 vivos), normal → blue (10/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 8 de 8 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t56b-unidades-vindicadores32-linea-retira12: enemy:mob=34

## Motivos de decisión (cambios registrados): ENGAGE 311, FORMATION_HOLD 45, INSTRUCTION 494, LOW_HEALTH_RETREAT 12, ORDER_OVERRIDE 266, OUT_OF_RANGE 1, SEEK_ENEMY 56, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
