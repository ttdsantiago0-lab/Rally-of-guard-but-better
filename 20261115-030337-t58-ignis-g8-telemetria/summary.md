# Simulación Rally - lote 20261115-030337-t58-ignis-g8-telemetria

Preset `t58-ignis-g8-telemetria` · velocidad `sprint` · 9 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t58-ignis-g8-telemetria | 8 | 100 % ± 0 | 0 % ± 0 | 3.5 ± 0.4 | 0.0 ± 0.0 | 104 ± 8 | 863 ± 71 | 316 ± 26 | 89 % ± 2 | 9.5 ± 0.2 | 8.6 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t58-ignis-g8-telemetria semilla 5801: sprint → blue (5/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 8 de 8 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t58-ignis-g8-telemetria: enemy:cataclysm.flame_strike=17 enemy:mob=18 env:fall=1

## Motivos de decisión (cambios registrados): ENGAGE 154, LOW_HEALTH_RETREAT 97, SEEK_ENEMY 46, TOO_CLOSE 38

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
