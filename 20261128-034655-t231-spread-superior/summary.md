# Simulación Rally - lote 20261128-034655-t231-spread-superior

Preset `t231-spread-superior` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t231-12-sync | 30 | 100 % ± 0 | 0 % ± 0 | 11.9 ± 0.1 | 0.0 ± 0.0 | 10 ± 0 | 192 ± 0 | 114 ± 1 | 0 % ± 0 | 4.2 ± 0.0 | 6.2 ± 0.0 | 0 |
| t231-12-spread | 30 | 100 % ± 0 | 0 % ± 0 | 11.8 ± 0.1 | 0.0 ± 0.0 | 9 ± 0 | 192 ± 0 | 116 ± 2 | 0 % ± 0 | 4.3 ± 0.0 | 6.1 ± 0.0 | 0 |
| t231-16-sync | 30 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 9 ± 0 | 192 ± 0 | 104 ± 1 | 0 % ± 0 | 4.4 ± 0.0 | 6.6 ± 0.1 | 0 |
| t231-16-spread | 30 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 9 ± 0 | 192 ± 0 | 101 ± 2 | 0 % ± 0 | 4.5 ± 0.0 | 6.3 ± 0.0 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t231-12-sync semilla 22600: sprint → blue (12/0 vivos), normal → blue (12/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t231-12-sync: enemy:mob=4
- t231-12-spread: enemy:mob=6
- t231-16-spread: enemy:mob=1

## Motivos de decisión (cambios registrados): ENGAGE 2661, NONE 4, SEEK_ENEMY 94

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
