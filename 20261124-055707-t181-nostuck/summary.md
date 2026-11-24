# Simulación Rally - lote 20261124-055707-t181-nostuck

Preset `t181-nostuck` · velocidad `sprint` · 21 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t181-ctl-nostuck | 20 | 50 % ± 0 | 50 % ± 0 | 164.3 ± 3.8 | 31.8 ± 5.5 | 240 ± 0 | 7156 ± 81 | 6329 ± 107 | 59 % ± 1 | 43.3 ± 0.5 | 19.7 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t181-ctl-nostuck semilla 16000: sprint → draw (136/3 vivos), normal → draw (174/16 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 20 de 20 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t181-ctl-nostuck: enemy:arrow=4433 enemy:mob=280 other(minecraft:arrow):arrow=1

## Motivos de decisión (cambios registrados): ENGAGE 30457, LOW_HEALTH_RETREAT 8844, MORALE_SHAKEN 10961, MORALE_WAVERING 5473, NONE 7567, ORDER_OVERRIDE 4254, OUT_OF_RANGE 15842, RALLIED 13, REFUSE_ATTACK 2511, ROUTED_FLEE 6862, SEEK_ENEMY 1968, STUCK 10, TOO_CLOSE 3, UNIT_BROKEN 2178

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t181-ctl-nostuck: 20/20 corridas llegaron al tope de tiempo
