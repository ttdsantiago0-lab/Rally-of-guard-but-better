# Simulación Rally - lote 20261111-214054-t47-atraccion-grandes

Preset `t47-atraccion-grandes` · velocidad `sprint` · 61 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t47-atraccion-fire_dragon | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 8 ± 0 | 0 ± 0 | 0 % ± 0 | 88.5 ± 1.6 | 88.5 ± 1.6 | 0 |
| t47-atraccion-ignis | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 12 ± 1 | 0 ± 0 | 100 % ± 0 | 39.0 ± 0.7 | 37.9 ± 0.6 | 0 |
| t47-atraccion-the_harbinger | 10 | 45 % ± 5 | 55 % ± 5 | 1.8 ± 0.2 | 1.0 ± 0.0 | 55 ± 5 | 25 ± 2 | 19 ± 3 | 98 % ± 3 | 30.0 ± 2.8 | 28.0 ± 3.1 | 0 |
| t47-atraccion-netherite_monstrosity | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 32 ± 2 | 5 ± 2 | 45 % ± 9 | 38.8 ± 0.5 | 37.7 ± 0.5 | 0 |
| t47-atraccion-ender_guardian | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 2 ± 0 | 0 ± 0 | 100 % ± 0 | 38.6 ± 0.5 | 38.1 ± 0.4 | 0 |
| t47-atraccion-tremorzilla | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 6 ± 0 | 0 ± 0 | 0 % ± 0 | 94.0 ± 1.4 | 89.0 ± 1.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t47-atraccion-fire_dragon semilla 1: sprint → draw (2/1 vivos), normal → draw (2/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 59 de 59 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t47-atraccion-the_harbinger: enemy:mob=2

## Motivos de decisión (cambios registrados): ENGAGE 126, LOW_HEALTH_RETREAT 29, NONE 84, OUT_OF_RANGE 210, SEEK_ENEMY 15, STUCK 249, TOO_CLOSE 27

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t47-atraccion-fire_dragon: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-ignis: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-the_harbinger: 9/10 corridas llegaron al tope de tiempo
- t47-atraccion-netherite_monstrosity: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-ender_guardian: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-tremorzilla: 10/10 corridas llegaron al tope de tiempo
