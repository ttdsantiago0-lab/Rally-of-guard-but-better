# Simulación Rally - lote 20261112-055028-t51-espadas-esqueletos16

Preset `t51-espadas-esqueletos16` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t51-espadas-esqueletos16-mixta-hoy | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 13 ± 0 | 359 ± 2 | 29 ± 1 | 95 % ± 1 | 9.4 ± 0.1 | 7.0 ± 0.2 | 0 |
| t51-espadas-esqueletos16-mixta-golpearysalir | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 13 ± 0 | 360 ± 2 | 31 ± 1 | 94 % ± 1 | 9.3 ± 0.2 | 7.3 ± 0.2 | 0 |
| t51-espadas-esqueletos16-espadas-hoy | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 23 ± 0 | 334 ± 3 | 52 ± 2 | 0 % ± 0 | 7.9 ± 0.2 | 8.5 ± 0.3 | 0 |
| t51-espadas-esqueletos16-espadas-golpearysalir | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 28 ± 0 | 328 ± 2 | 74 ± 2 | 0 % ± 0 | 7.9 ± 0.2 | 8.9 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t51-espadas-esqueletos16-mixta-hoy semilla 4001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 1032, FORMATION_HOLD 202, LOW_HEALTH_RETREAT 7, NONE 6, SEEK_ENEMY 118, TOO_CLOSE 57

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
