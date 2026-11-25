# Simulación Rally - lote 20261125-090929-t209-o8-hoy-800

Preset `t209-o8-hoy-800` · velocidad `sprint` · 11 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t209-800-mixto | 10 | 55 % ± 5 | 45 % ± 5 | 55.4 ± 13.4 | 84.9 ± 18.4 | 174 ± 6 | 8268 ± 374 | 8977 ± 209 | 67 % ± 1 | 38.5 ± 1.3 | 24.1 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t209-800-mixto semilla 17800: sprint → blue (94/0 vivos), normal → draw (69/96 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 10 de 10 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t209-800-mixto: enemy:arrow=3286 enemy:mob=160

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 15949, LOW_HEALTH_RETREAT 5080, MORALE_SHAKEN 6042, MORALE_WAVERING 3033, NONE 3820, ORDER_OVERRIDE 555, OUT_OF_RANGE 7627, RALLIED 7, REFUSE_ATTACK 1524, ROUTED_FLEE 4056, SEEK_ENEMY 1043, STUCK 8, TOO_CLOSE 2, UNIT_BROKEN 1567

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t209-800-mixto: 9/10 corridas llegaron al tope de tiempo
- t209-800-mixto: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
