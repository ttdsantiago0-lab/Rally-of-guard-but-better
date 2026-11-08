# Simulación Rally - lote 20261108-202929-t28-jefe-ciclope

Preset `t28-jefe-ciclope` · velocidad `sprint` · 19 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t28-jefe-ciclope-g16 | 6 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 31 ± 1 | 151 ± 0 | 0 ± 0 | 97 % ± 1 | 19.0 ± 1.7 | 12.7 ± 1.5 | 0 |
| t28-jefe-ciclope-g32 | 6 | 100 % ± 0 | 0 % ± 0 | 32.0 ± 0.0 | 0.0 ± 0.0 | 27 ± 1 | 151 ± 0 | 0 ± 0 | 96 % ± 1 | 27.9 ± 0.9 | 19.0 ± 0.8 | 0 |
| t28-jefe-ciclope-g64 | 6 | 100 % ± 0 | 0 % ± 0 | 64.0 ± 0.0 | 0.0 ± 0.0 | 24 ± 1 | 151 ± 0 | 0 ± 0 | 93 % ± 1 | 38.2 ± 1.2 | 23.0 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t28-jefe-ciclope-g16 semilla 1: sprint → blue (16/0 vivos), normal → blue (16/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 18 de 18 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 623, FORMATION_HOLD 13, MORALE_SHAKEN 153, MORALE_WAVERING 1, NONE 271, OUT_OF_RANGE 63, SEEK_ENEMY 116, STUCK 9, TOO_CLOSE 14

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
