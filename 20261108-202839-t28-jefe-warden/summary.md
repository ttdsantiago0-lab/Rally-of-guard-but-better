# Simulación Rally - lote 20261108-202839-t28-jefe-warden

Preset `t28-jefe-warden` · velocidad `sprint` · 19 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t28-jefe-warden-g16 | 6 | 100 % ± 0 | 0 % ± 0 | 14.8 ± 0.5 | 0.0 ± 0.0 | 45 ± 3 | 503 ± 1 | 78 ± 12 | 89 % ± 1 | 19.6 ± 1.4 | 16.1 ± 1.4 | 0 |
| t28-jefe-warden-g32 | 6 | 100 % ± 0 | 0 % ± 0 | 31.0 ± 0.7 | 0.0 ± 0.0 | 33 ± 1 | 505 ± 1 | 59 ± 15 | 88 % ± 1 | 25.2 ± 0.8 | 18.1 ± 0.6 | 0 |
| t28-jefe-warden-g64 | 6 | 100 % ± 0 | 0 % ± 0 | 62.2 ± 0.5 | 0.0 ± 0.0 | 29 ± 1 | 505 ± 1 | 68 ± 11 | 88 % ± 1 | 24.9 ± 0.8 | 15.3 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t28-jefe-warden-g16 semilla 1: sprint → blue (13/0 vivos), normal → blue (11/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 18 de 18 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t28-jefe-warden-g16: enemy:mob=7
- t28-jefe-warden-g32: enemy:mob=6
- t28-jefe-warden-g64: enemy:mob=11

## Motivos de decisión (cambios registrados): ENGAGE 832, FORMATION_HOLD 2, LOW_HEALTH_RETREAT 11, MORALE_SHAKEN 320, MORALE_WAVERING 94, NONE 308, OUT_OF_RANGE 8, RALLIED 1, ROUTED_FLEE 42, SEEK_ENEMY 150, STUCK 12, TOO_CLOSE 43

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
