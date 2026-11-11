# Simulación Rally - lote 20261111-215306-t48-sincro-skeleton

Preset `t48-sincro-skeleton` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t48-sincro-techo-skeleton-arco-base-r95-base-sincro | 30 | 67 % ± 9 | 33 % ± 9 | 2.5 ± 0.5 | 1.8 ± 0.6 | 44 ± 2 | 1222 ± 16 | 173 ± 3 | 98 % ± 0 | 15.3 ± 0.2 | 6.2 ± 0.2 | 0 |
| t48-sincro-techo-skeleton-arco-base-r95-base-sinsincro | 30 | 70 % ± 9 | 30 % ± 9 | 3.3 ± 0.5 | 1.1 ± 0.3 | 43 ± 2 | 1222 ± 11 | 168 ± 4 | 97 % ± 0 | 15.8 ± 0.3 | 6.7 ± 0.4 | 0 |
| t48-sincro-techo-skeleton-arco-base-r68-base-sincro | 30 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.1 | 0.0 ± 0.0 | 26 ± 1 | 1111 ± 5 | 112 ± 3 | 98 % ± 0 | 15.1 ± 0.1 | 5.4 ± 0.1 | 0 |
| t48-sincro-techo-skeleton-arco-base-r68-base-sinsincro | 30 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.1 | 0.0 ± 0.0 | 26 ± 0 | 1107 ± 7 | 115 ± 2 | 93 % ± 0 | 15.1 ± 0.1 | 5.5 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t48-sincro-techo-skeleton-arco-base-r95-base-sincro semilla 2001: sprint → blue (4/0 vivos), normal → blue (2/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 101 de 101 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t48-sincro-techo-skeleton-arco-base-r95-base-sincro: enemy:arrow=165
- t48-sincro-techo-skeleton-arco-base-r95-base-sinsincro: enemy:arrow=141
- t48-sincro-techo-skeleton-arco-base-r68-base-sincro: enemy:arrow=8
- t48-sincro-techo-skeleton-arco-base-r68-base-sinsincro: enemy:arrow=9

## Motivos de decisión (cambios registrados): ENGAGE 987, LOW_HEALTH_RETREAT 1296, MORALE_SHAKEN 46, MORALE_WAVERING 16, NONE 81, OUT_OF_RANGE 1, REFUSE_ATTACK 10, ROUTED_FLEE 4, SEEK_ENEMY 715, TOO_CLOSE 32

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
