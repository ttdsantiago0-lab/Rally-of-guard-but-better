# Simulación Rally - lote 20261124-051616-t177-closer

Preset `t177-closer` · velocidad `sprint` · 40 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t177-arcos-reuse | 3 | 100 % ± 0 | 0 % ± 0 | 114.7 ± 4.7 | 0.0 ± 0.0 | 55 ± 3 | 4805 ± 19 | 3219 ± 42 | 58 % ± 1 | 47.8 ± 0.1 | 18.4 ± 0.2 | 15 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t177-arcos-reuse semilla 15500: sprint → blue (110/0 vivos), normal → draw (16/77 vivos)
- t177-arcos-reuse semilla 23277: sprint → ? (0/0 vivos), normal → draw (48/38 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 3 de 3 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t177-arcos-reuse: enemy:arrow=256

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 28128, LOW_HEALTH_RETREAT 11745, MORALE_SHAKEN 10155, MORALE_WAVERING 5317, NONE 9464, ORDER_OVERRIDE 3948, OUT_OF_RANGE 21687, RALLIED 11, REFUSE_ATTACK 1916, ROUTED_FLEE 7283, SEEK_ENEMY 1008, STUCK 5, TOO_CLOSE 6, UNIT_BROKEN 2853

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t177-mixto-reuse - 20 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t177-arcos-reuse - 15 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t177-arcos-reuse: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
