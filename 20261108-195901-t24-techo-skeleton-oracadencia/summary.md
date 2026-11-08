# Simulación Rally - lote 20261108-195901-t24-techo-skeleton-oracadencia

Preset `t24-techo-skeleton-oracadencia` · velocidad `sprint` · 81 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t24-techo-skeleton-arco-oracadencia-r68 | 20 | 100 % ± 0 | 0 % ± 0 | 7.1 ± 0.4 | 0.0 ± 0.0 | 28 ± 1 | 1120 ± 8 | 122 ± 5 | 94 % ± 1 | 15.4 ± 0.2 | 5.7 ± 0.2 | 0 |
| t24-techo-skeleton-arco-oracadencia-r95 | 20 | 70 % ± 11 | 30 % ± 11 | 3.1 ± 0.7 | 2.8 ± 1.1 | 42 ± 3 | 1220 ± 19 | 170 ± 4 | 97 % ± 0 | 15.6 ± 0.3 | 6.8 ± 0.5 | 0 |
| t24-techo-skeleton-linea-oracadencia-r110 | 20 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 39 ± 1 | 878 ± 12 | 70 ± 4 | 87 % ± 1 | 13.2 ± 0.2 | 9.1 ± 0.3 | 0 |
| t24-techo-skeleton-linea-oracadencia-r150 | 20 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.3 | 0.0 ± 0.0 | 45 ± 3 | 930 ± 13 | 87 ± 9 | 88 % ± 1 | 13.2 ± 0.2 | 9.8 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t24-techo-skeleton-arco-oracadencia-r68 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 74 de 74 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t24-techo-skeleton-arco-oracadencia-r68: enemy:arrow=18
- t24-techo-skeleton-arco-oracadencia-r95: enemy:arrow=98
- t24-techo-skeleton-linea-oracadencia-r110: enemy:arrow=2
- t24-techo-skeleton-linea-oracadencia-r150: enemy:arrow=13

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 714, LOW_HEALTH_RETREAT 623, MORALE_SHAKEN 108, MORALE_WAVERING 49, NONE 19, OUT_OF_RANGE 18, REFUSE_ATTACK 4, ROUTED_FLEE 18, SEEK_ENEMY 373, TOO_CLOSE 17

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
