# Simulación Rally - lote 20261111-215130-t48-sincro-zombie

Preset `t48-sincro-zombie` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t48-sincro-techo-zombie-linea-base-r75-base-sincro | 30 | 100 % ± 0 | 0 % ± 0 | 5.9 ± 0.3 | 0.0 ± 0.0 | 54 ± 1 | 1849 ± 8 | 68 ± 4 | 98 % ± 0 | 8.5 ± 0.4 | 13.0 ± 0.5 | 0 |
| t48-sincro-techo-zombie-linea-base-r75-base-sinsincro | 30 | 100 % ± 0 | 0 % ± 0 | 5.2 ± 0.3 | 0.0 ± 0.0 | 59 ± 1 | 1875 ± 8 | 82 ± 4 | 95 % ± 0 | 8.6 ± 0.3 | 13.8 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t48-sincro-techo-zombie-linea-base-r75-base-sincro semilla 2001: sprint → blue (5/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t48-sincro-techo-zombie-linea-base-r75-base-sincro: enemy:mob=35
- t48-sincro-techo-zombie-linea-base-r75-base-sinsincro: enemy:mob=37

## Motivos de decisión (cambios registrados): COUNTERATTACK 2, ENGAGE 745, LOW_HEALTH_RETREAT 356, MORALE_SHAKEN 264, MORALE_WAVERING 142, NONE 11, OUT_OF_RANGE 91, RALLIED 7, REFUSE_ATTACK 4, ROUTED_FLEE 45, SEEK_ENEMY 80, TOO_CLOSE 46

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
