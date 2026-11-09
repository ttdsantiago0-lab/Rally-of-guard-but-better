# Simulación Rally - lote 20261109-194823-t34-moral-monstruosidad

Preset `t34-moral-monstruosidad` · velocidad `sprint` · 17 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t34-moral-monstruosidad-conmoral | 8 | 100 % ± 0 | 0 % ± 0 | 13.8 ± 0.6 | 0.0 ± 0.0 | 69 ± 6 | 612 ± 5 | 305 ± 38 | 54 % ± 6 | 18.1 ± 0.6 | 13.8 ± 0.8 | 0 |
| t34-moral-monstruosidad-sinmoral | 8 | 100 % ± 0 | 0 % ± 0 | 14.9 ± 0.5 | 0.0 ± 0.0 | 52 ± 1 | 604 ± 1 | 244 ± 24 | 60 % ± 6 | 17.4 ± 0.9 | 12.1 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t34-moral-monstruosidad-conmoral semilla 1: sprint → blue (15/0 vivos), normal → blue (16/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 16 de 16 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t34-moral-monstruosidad-conmoral: enemy:mob=16 env:lava=2
- t34-moral-monstruosidad-sinmoral: enemy:mob=7 env:lava=2

## Motivos de decisión (cambios registrados): ENGAGE 542, LOW_HEALTH_RETREAT 145, MORALE_SHAKEN 247, MORALE_WAVERING 144, NONE 116, RALLIED 4, REFUSE_ATTACK 20, ROUTED_FLEE 112, SEEK_ENEMY 51, STUCK 9, TOO_CLOSE 7

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: t34-moral-monstruosidad-conmoral - 11 % de las bajas azules ({enemy:mob=16.0, env:lava=2.0})
- MUERTES QUE NO SON DEL ENEMIGO: t34-moral-monstruosidad-sinmoral - 22 % de las bajas azules ({enemy:mob=7.0, env:lava=2.0})
