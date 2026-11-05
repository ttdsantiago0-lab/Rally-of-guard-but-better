# Simulación Rally - lote 20261105-132115-t12-calma-b

Preset `t12-calma-b` · velocidad `sprint` · 21 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t12-waves-mixta-arco-comida-base | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 275 ± 1 | 2407 ± 6 | 70 ± 5 | 78 % ± 1 | 14.0 ± 0.2 | 11.2 ± 0.4 | 0 |
| t12-waves-mixta-arco-comida-sincalma | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 273 ± 1 | 2397 ± 8 | 80 ± 4 | 79 % ± 1 | 13.9 ± 0.3 | 11.9 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t12-waves-mixta-arco-comida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 20 de 20 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 2032, LOW_HEALTH_RETREAT 3, MORALE_SHAKEN 55, MORALE_WAVERING 27, NONE 1525, OUT_OF_RANGE 17, RALLIED 4, REFUSE_ATTACK 4, ROUTED_FLEE 5, SEEK_ENEMY 213, TOO_CLOSE 517

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
