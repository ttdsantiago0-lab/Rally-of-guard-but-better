# Simulación Rally - lote 20261123-053157-t144-altura

Preset `t144-altura` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t144-altura-vind-flat | 30 | 100 % ± 0 | 0 % ± 0 | 4.5 ± 0.1 | 0.0 ± 0.0 | 16 ± 0 | 434 ± 2 | 107 ± 2 | 91 % ± 1 | 8.5 ± 0.1 | 6.9 ± 0.3 | 0 |
| t144-altura-vind-hill | 30 | 100 % ± 0 | 0 % ± 0 | 4.8 ± 0.2 | 0.0 ± 0.0 | 16 ± 0 | 433 ± 2 | 101 ± 4 | 87 % ± 1 | 8.0 ± 0.1 | 6.7 ± 0.3 | 0 |
| t144-altura-pill-flat | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 16 ± 0 | 421 ± 2 | 14 ± 1 | 91 % ± 1 | 9.5 ± 0.1 | 7.2 ± 0.1 | 0 |
| t144-altura-pill-hill | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 17 ± 0 | 421 ± 2 | 14 ± 1 | 87 % ± 1 | 9.4 ± 0.1 | 7.0 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t144-altura-vind-flat semilla 13100: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t144-altura-vind-flat: enemy:mob=106
- t144-altura-vind-hill: enemy:mob=95

## Motivos de decisión (cambios registrados): ENGAGE 984, LOW_HEALTH_RETREAT 48, NONE 1, SEEK_ENEMY 5, TOO_CLOSE 16

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
