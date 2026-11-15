# Simulación Rally - lote 20261115-030952-t56b-retira-vindicadores32

Preset `t56b-retira-vindicadores32` · velocidad `sprint` · 9 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t56b-unidades-vindicadores32-linea-retira12 | 8 | 100 % ± 0 | 0 % ± 0 | 11.6 ± 0.7 | 0.0 ± 0.0 | 17 ± 1 | 866 ± 4 | 160 ± 19 | 91 % ± 2 | 10.7 ± 0.8 | 11.9 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t56b-unidades-vindicadores32-linea-retira12 semilla 5601: sprint → blue (14/0 vivos), normal → blue (9/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 8 de 8 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t56b-unidades-vindicadores32-linea-retira12: enemy:mob=35

## Motivos de decisión (cambios registrados): ENGAGE 214, FORMATION_HOLD 10, INSTRUCTION 71, LOW_HEALTH_RETREAT 14, ORDER_OVERRIDE 4, SEEK_ENEMY 14, TOO_CLOSE 2, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
