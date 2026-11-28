# Simulación Rally - lote 20261128-043524-t235-duelo-lados

Preset `t235-duelo-lados` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t235-32-espejo | 30 | 97 % ± 3 | 3 % ± 3 | 15.0 ± 0.9 | 0.1 ± 0.1 | 16 ± 0 | 768 ± 0 | 616 ± 9 | 0 % ± 0 | 4.9 ± 0.2 | 10.4 ± 0.1 | 0 |
| t235-32-sync-solo | 30 | 100 % ± 0 | 0 % ± 0 | 23.3 ± 0.5 | 0.0 ± 0.0 | 13 ± 0 | 768 ± 0 | 500 ± 6 | 0 % ± 0 | 4.5 ± 0.1 | 10.5 ± 0.0 | 0 |
| t235-32-solo-sync | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 17.8 ± 0.9 | 14 ± 0 | 595 ± 9 | 768 ± 0 | 0 % ± 0 | 3.7 ± 0.0 | 8.9 ± 0.2 | 0 |
| t235-64-espejo | 30 | 80 % ± 7 | 20 % ± 7 | 12.2 ± 1.8 | 2.0 ± 1.0 | 20 ± 0 | 1520 ± 8 | 1424 ± 17 | 0 % ± 0 | 4.0 ± 0.1 | 9.7 ± 0.2 | 0 |
| t235-64-sync-solo | 30 | 100 % ± 0 | 0 % ± 0 | 39.6 ± 0.8 | 0.0 ± 0.0 | 15 ± 0 | 1536 ± 0 | 1109 ± 9 | 0 % ± 0 | 4.4 ± 0.1 | 10.5 ± 0.1 | 0 |
| t235-64-solo-sync | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 41.3 ± 0.6 | 14 ± 0 | 1091 ± 7 | 1536 ± 0 | 0 % ± 0 | 3.7 ± 0.0 | 9.7 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t235-32-espejo semilla 23100: sprint → blue (18/0 vivos), normal → blue (18/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 113 de 113 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t235-32-espejo: enemy:mob=511
- t235-32-sync-solo: enemy:mob=262
- t235-32-solo-sync: enemy:mob=960
- t235-64-espejo: enemy:mob=1553
- t235-64-sync-solo: enemy:mob=731
- t235-64-solo-sync: enemy:mob=1920

## Motivos de decisión (cambios registrados): ENGAGE 17381, NONE 133, SEEK_ENEMY 714

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
