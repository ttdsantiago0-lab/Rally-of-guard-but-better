# Simulación Rally - lote 20261128-023516-t225-sync-superior

Preset `t225-sync-superior` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t225-12-base | 30 | 100 % ± 0 | 0 % ± 0 | 10.7 ± 0.2 | 0.0 ± 0.0 | 11 ± 0 | 192 ± 0 | 142 ± 2 | 0 % ± 0 | 4.1 ± 0.0 | 6.5 ± 0.1 | 0 |
| t225-12-sync | 30 | 100 % ± 0 | 0 % ± 0 | 11.9 ± 0.1 | 0.0 ± 0.0 | 10 ± 0 | 192 ± 0 | 113 ± 1 | 0 % ± 0 | 4.2 ± 0.0 | 6.1 ± 0.0 | 0 |
| t225-16-base | 30 | 100 % ± 0 | 0 % ± 0 | 15.7 ± 0.1 | 0.0 ± 0.0 | 10 ± 0 | 192 ± 0 | 123 ± 2 | 0 % ± 0 | 4.2 ± 0.0 | 6.6 ± 0.0 | 0 |
| t225-16-sync | 30 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 9 ± 0 | 192 ± 0 | 103 ± 1 | 0 % ± 0 | 4.4 ± 0.0 | 6.6 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t225-12-base semilla 20600: sprint → blue (11/0 vivos), normal → blue (11/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t225-12-base: enemy:mob=39
- t225-12-sync: enemy:mob=3
- t225-16-base: enemy:mob=10

## Motivos de decisión (cambios registrados): ENGAGE 2660, NONE 6, SEEK_ENEMY 115

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
