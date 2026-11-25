# Simulación Rally - lote 20261125-083138-t205-avanzar-20s

Preset `t205-avanzar-20s` · velocidad `sprint` · 41 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t205-av-b8 | 20 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 56 ± 0 | 215 ± 1 | 17 ± 3 | 89 % ± 1 | 68.6 ± 0.3 | 3.8 ± 0.1 | 0 |
| t205-av-b100 | 20 | 100 % ± 0 | 0 % ± 0 | 100.0 ± 0.0 | 0.0 ± 0.0 | 53 ± 0 | 214 ± 1 | 0 ± 0 | 64 % ± 2 | 77.2 ± 0.3 | 7.5 ± 0.0 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t205-av-b8 semilla 17000: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t205-av-b8: enemy:mob=2

## Motivos de decisión (cambios registrados): ENGAGE 1482, FORMATION_HOLD 14041, INSTRUCTION 15259, OUT_OF_RANGE 37, SEEK_ENEMY 244, STUCK 1, TOO_CLOSE 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
