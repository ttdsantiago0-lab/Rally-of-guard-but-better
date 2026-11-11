# Simulación Rally - lote 20261111-153942-t43-coord-quiebre

Preset `t43-coord-quiebre` · velocidad `sprint` · 49 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t43-quiebre-mixta-r16-base | 6 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.2 | 0.0 ± 0.0 | 321 ± 5 | 3358 ± 59 | 112 ± 8 | 79 % ± 1 | 12.6 ± 0.5 | 12.7 ± 0.3 | 0 |
| t43-quiebre-mixta-r16-sincro | 6 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 317 ± 4 | 3505 ± 49 | 116 ± 10 | 88 % ± 1 | 12.7 ± 0.5 | 13.1 ± 0.4 | 0 |
| t43-quiebre-mixta-r22-base | 6 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 356 ± 8 | 4385 ± 91 | 187 ± 10 | 81 % ± 1 | 13.2 ± 0.6 | 13.3 ± 0.5 | 0 |
| t43-quiebre-mixta-r22-sincro | 6 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 347 ± 5 | 4497 ± 101 | 177 ± 17 | 90 % ± 1 | 13.1 ± 0.5 | 13.2 ± 0.4 | 0 |
| t43-quiebre-zombie-r24-base | 6 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 354 ± 2 | 5729 ± 10 | 201 ± 18 | 89 % ± 0 | 9.4 ± 0.3 | 10.0 ± 0.3 | 0 |
| t43-quiebre-zombie-r24-sincro | 6 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 344 ± 2 | 5740 ± 9 | 222 ± 18 | 93 % ± 0 | 8.9 ± 0.2 | 10.6 ± 0.5 | 0 |
| t43-quiebre-zombie-r32-base | 6 | 100 % ± 0 | 0 % ± 0 | 7.2 ± 0.2 | 0.0 ± 0.0 | 408 ± 6 | 7659 ± 13 | 319 ± 20 | 90 % ± 1 | 9.2 ± 0.2 | 10.1 ± 0.4 | 0 |
| t43-quiebre-zombie-r32-sincro | 6 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.3 | 0.0 ± 0.0 | 402 ± 6 | 7679 ± 8 | 306 ± 28 | 95 % ± 0 | 9.7 ± 0.4 | 10.4 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t43-quiebre-mixta-r16-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 48 de 48 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t43-quiebre-mixta-r16-base: enemy:explosion.player=2
- t43-quiebre-mixta-r22-sincro: enemy:mob=1
- t43-quiebre-zombie-r32-base: enemy:mob=3
- t43-quiebre-zombie-r32-sincro: enemy:mob=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 5, ENGAGE 4651, LOW_HEALTH_RETREAT 135, MORALE_SHAKEN 392, MORALE_WAVERING 87, NONE 3519, OUT_OF_RANGE 220, RALLIED 8, REFUSE_ATTACK 12, ROUTED_FLEE 21, SEEK_ENEMY 721, STUCK 1, TOO_CLOSE 132

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
