# Simulación Rally - lote 20261128-041124-t233-duelo-escala

Preset `t233-duelo-escala` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t233-32-espejo | 30 | 100 % ± 0 | 0 % ± 0 | 15.0 ± 0.8 | 0.0 ± 0.0 | 16 ± 0 | 768 ± 0 | 615 ± 10 | 0 % ± 0 | 4.5 ± 0.1 | 10.1 ± 0.1 | 0 |
| t233-32-sync-solo | 30 | 100 % ± 0 | 0 % ± 0 | 22.6 ± 0.5 | 0.0 ± 0.0 | 13 ± 0 | 768 ± 0 | 511 ± 7 | 0 % ± 0 | 4.7 ± 0.1 | 10.4 ± 0.1 | 0 |
| t233-64-espejo | 30 | 77 % ± 8 | 23 % ± 8 | 11.9 ± 1.7 | 2.6 ± 1.1 | 20 ± 0 | 1516 ± 9 | 1430 ± 15 | 0 % ± 0 | 3.8 ± 0.1 | 9.6 ± 0.2 | 0 |
| t233-64-sync-solo | 30 | 100 % ± 0 | 0 % ± 0 | 39.9 ± 0.8 | 0.0 ± 0.0 | 15 ± 0 | 1536 ± 0 | 1104 ± 11 | 0 % ± 0 | 4.3 ± 0.1 | 10.5 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t233-32-espejo semilla 23000: sprint → blue (19/0 vivos), normal → blue (18/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 113 de 113 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t233-32-espejo: enemy:mob=511
- t233-32-sync-solo: enemy:mob=283
- t233-64-espejo: enemy:mob=1562
- t233-64-sync-solo: enemy:mob=724

## Motivos de decisión (cambios registrados): ENGAGE 11614, NONE 76, SEEK_ENEMY 485

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
