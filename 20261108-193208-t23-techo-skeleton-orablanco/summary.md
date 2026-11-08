# Simulación Rally - lote 20261108-193208-t23-techo-skeleton-orablanco

Preset `t23-techo-skeleton-orablanco` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t23-techo-skeleton-arco-orablanco-r68 | 30 | 37 % ± 9 | 63 % ± 9 | 1.2 ± 0.4 | 4.5 ± 0.9 | 45 ± 2 | 841 ± 13 | 182 ± 3 | 93 % ± 0 | 14.6 ± 0.3 | 7.3 ± 0.4 | 0 |
| t23-techo-skeleton-linea-orablanco-r80 | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 43 ± 1 | 762 ± 9 | 80 ± 3 | 82 % ± 1 | 13.8 ± 0.2 | 9.9 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t23-techo-skeleton-arco-orablanco-r68 semilla 1: sprint → red (0/12 vivos), normal → red (0/12 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 41 de 41 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t23-techo-skeleton-arco-orablanco-r68: enemy:arrow=203
- t23-techo-skeleton-linea-orablanco-r80: enemy:arrow=4

## Motivos de decisión (cambios registrados): ENGAGE 533, LOW_HEALTH_RETREAT 671, MORALE_SHAKEN 153, MORALE_WAVERING 63, NONE 51, OUT_OF_RANGE 13, REFUSE_ATTACK 10, ROUTED_FLEE 16, SEEK_ENEMY 383, TOO_CLOSE 30

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
