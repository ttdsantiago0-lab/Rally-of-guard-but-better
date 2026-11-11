# Simulación Rally - lote 20261111-221054-t49-jinete-esqueletos16

Preset `t49-jinete-esqueletos16` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t49-jinete-esqueletos16-hoy | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 13 ± 0 | 359 ± 2 | 31 ± 1 | 95 % ± 1 | 9.8 ± 0.2 | 7.2 ± 0.2 | 0 |
| t49-jinete-esqueletos16-kite | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 13 ± 0 | 359 ± 2 | 29 ± 1 | 95 % ± 1 | 9.6 ± 0.2 | 7.5 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t49-jinete-esqueletos16-hoy semilla 3001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 541, FORMATION_HOLD 66, LOW_HEALTH_RETREAT 1, SEEK_ENEMY 21, TOO_CLOSE 60

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
