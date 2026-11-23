# Simulación Rally - lote 20261123-055058-t146-altura-hold

Preset `t146-altura-hold` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t146-altura-vind-flat-hold | 30 | 100 % ± 0 | 0 % ± 0 | 6.1 ± 0.2 | 0.0 ± 0.0 | 14 ± 0 | 428 ± 2 | 80 ± 4 | 92 % ± 1 | 8.8 ± 0.2 | 6.8 ± 0.2 | 0 |
| t146-altura-vind-hill-hold | 30 | 100 % ± 0 | 0 % ± 0 | 5.8 ± 0.3 | 0.0 ± 0.0 | 17 ± 1 | 428 ± 1 | 84 ± 6 | 72 % ± 2 | 9.3 ± 0.1 | 6.8 ± 0.2 | 0 |
| t146-altura-pill48-flat-hold | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 31 ± 0 | 979 ± 5 | 84 ± 2 | 96 % ± 0 | 6.4 ± 0.1 | 6.9 ± 0.2 | 0 |
| t146-altura-pill48-hill-hold | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 43 ± 2 | 915 ± 8 | 100 ± 3 | 59 % ± 2 | 7.3 ± 0.2 | 8.3 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t146-altura-vind-flat-hold semilla 13300: sprint → blue (7/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t146-altura-vind-flat-hold: enemy:mob=56
- t146-altura-vind-hill-hold: enemy:mob=66
- t146-altura-pill48-hill-hold: enemy:arrow=1

## Motivos de decisión (cambios registrados): ENGAGE 1164, FORMATION_HOLD 37, INSTRUCTION 968, LOW_HEALTH_RETREAT 61, SEEK_ENEMY 413, STUCK 10, TOO_CLOSE 186

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
