# Simulación Rally - lote 20261128-063611-t245-duelo-arcos

Preset `t245-duelo-arcos` · velocidad `sprint` · 181 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t245-32-espejo | 30 | 53 % ± 9 | 47 % ± 9 | 2.7 ± 0.6 | 2.0 ± 0.5 | 14 ± 0 | 726 ± 5 | 721 ± 5 | 90 % ± 1 | 27.4 ± 0.2 | 8.7 ± 0.3 | 0 |
| t245-32-sync-solo | 30 | 100 % ± 0 | 0 % ± 0 | 10.0 ± 0.4 | 0.0 ± 0.0 | 13 ± 0 | 746 ± 3 | 593 ± 5 | 86 % ± 0 | 27.5 ± 0.1 | 10.0 ± 0.1 | 0 |
| t245-32-solo-sync | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 9.2 ± 0.4 | 13 ± 0 | 605 ± 7 | 745 ± 2 | 90 % ± 0 | 26.1 ± 0.1 | 8.1 ± 0.2 | 0 |
| t245-64-espejo | 30 | 70 % ± 9 | 30 % ± 9 | 6.5 ± 1.2 | 2.2 ± 0.7 | 24 ± 1 | 1477 ± 7 | 1416 ± 16 | 81 % ± 1 | 32.4 ± 0.1 | 9.5 ± 0.2 | 0 |
| t245-64-sync-solo | 30 | 100 % ± 0 | 0 % ± 0 | 20.5 ± 1.1 | 0.0 ± 0.0 | 20 ± 0 | 1495 ± 3 | 1207 ± 18 | 75 % ± 1 | 32.8 ± 0.1 | 10.1 ± 0.1 | 0 |
| t245-64-solo-sync | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 17.7 ± 1.1 | 22 ± 0 | 1262 ± 17 | 1493 ± 4 | 88 % ± 0 | 30.6 ± 0.1 | 8.9 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t245-32-espejo semilla 24600: sprint → red (0/3 vivos), normal → red (0/2 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 97 de 97 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t245-32-espejo: enemy:arrow=879
- t245-32-sync-solo: enemy:arrow=661
- t245-32-solo-sync: enemy:arrow=960
- t245-64-espejo: enemy:arrow=1726
- t245-64-sync-solo: enemy:arrow=1304
- t245-64-solo-sync: enemy:arrow=1920

## Motivos de decisión (cambios registrados): ENGAGE 17660, NONE 183, OUT_OF_RANGE 715

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
