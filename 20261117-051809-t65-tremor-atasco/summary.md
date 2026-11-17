# Simulación Rally - lote 20261117-051809-t65-tremor-atasco

Preset `t65-tremor-atasco` · velocidad `sprint` · 69 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t65-tremor-escudos | 28 | 77 % ± 7 | 23 % ± 7 | 9.4 ± 1.0 | 0.3 ± 0.1 | 132 ± 14 | 1079 ± 61 | 239 ± 18 | 0 % ± 0 | 28.1 ± 0.9 | 22.6 ± 1.1 | 6 |
| t65-tremor-14a2 | 30 | 72 % ± 8 | 28 % ± 8 | 7.5 ± 1.0 | 0.3 ± 0.1 | 118 ± 12 | 1028 ± 48 | 265 ± 15 | 85 % ± 2 | 24.6 ± 0.7 | 19.7 ± 0.7 | 4 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t65-tremor-escudos semilla 6201: sprint → blue (15/0 vivos), normal → blue (11/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 45 de 45 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t65-tremor-escudos: enemy:mob=75 enemy:tremorzilla_beam=101 env:radiation=8
- t65-tremor-14a2: enemy:mob=76 enemy:tremorzilla_beam=165 env:fall=1 env:radiation=12

## Motivos de decisión (cambios registrados): ENGAGE 2633, FORMATION_HOLD 1, LOW_HEALTH_RETREAT 2926, NONE 2265, OUT_OF_RANGE 58, SEEK_ENEMY 3207, STUCK 700, TOO_CLOSE 10

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t65-tremor-14a2 - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t65-tremor-escudos - 6 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
