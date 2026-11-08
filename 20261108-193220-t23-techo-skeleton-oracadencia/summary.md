# Simulación Rally - lote 20261108-193220-t23-techo-skeleton-oracadencia

Preset `t23-techo-skeleton-oracadencia` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t23-techo-skeleton-arco-oracadencia-r68 | 30 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.2 | 0.0 ± 0.0 | 27 ± 1 | 1113 ± 6 | 117 ± 4 | 94 % ± 0 | 15.3 ± 0.1 | 5.7 ± 0.2 | 0 |
| t23-techo-skeleton-linea-oracadencia-r80 | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 38 ± 1 | 840 ± 9 | 61 ± 3 | 84 % ± 1 | 14.1 ± 0.4 | 10.1 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t23-techo-skeleton-arco-oracadencia-r68 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t23-techo-skeleton-arco-oracadencia-r68: enemy:arrow=13

## Motivos de decisión (cambios registrados): ENGAGE 552, LOW_HEALTH_RETREAT 247, MORALE_SHAKEN 69, MORALE_WAVERING 14, NONE 7, OUT_OF_RANGE 17, SEEK_ENEMY 153, TOO_CLOSE 3

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
