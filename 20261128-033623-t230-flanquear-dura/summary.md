# Simulación Rally - lote 20261128-033623-t230-flanquear-dura

Preset `t230-flanquear-dura` · velocidad `sprint` · 41 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t230-fl-r16 | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 19 ± 0 | 434 ± 2 | 3 ± 1 | 82 % ± 1 | 17.6 ± 0.3 | 11.8 ± 0.1 | 0 |
| t230-fl-r24 | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.1 | 0.0 ± 0.0 | 23 ± 0 | 643 ± 3 | 16 ± 3 | 85 % ± 1 | 15.9 ± 0.3 | 12.1 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t230-fl-r16 semilla 17400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t230-fl-r24: enemy:mob=1

## Motivos de decisión (cambios registrados): ENGAGE 2141, FORMATION_HOLD 91, INSTRUCTION 2139, LOW_HEALTH_RETREAT 3, SEEK_ENEMY 55

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
