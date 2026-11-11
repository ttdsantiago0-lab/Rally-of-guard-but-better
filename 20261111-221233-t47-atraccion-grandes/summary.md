# Simulación Rally - lote 20261111-221233-t47-atraccion-grandes

Preset `t47-atraccion-grandes` · velocidad `sprint` · 61 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t47-atraccion-fire_dragon | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 8 ± 0 | 0 ± 0 | 0 % ± 0 | 90.8 ± 1.0 | 90.2 ± 1.1 | 0 |
| t47-atraccion-ignis | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 4 ± 0 | 0 ± 0 | 0 % ± 0 | 37.6 ± 0.5 | 37.0 ± 0.5 | 0 |
| t47-atraccion-the_harbinger | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 5 ± 0 | 17 ± 1 | 0 % ± 0 | 32.9 ± 0.4 | 31.2 ± 0.6 | 0 |
| t47-atraccion-netherite_monstrosity | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 5 ± 0 | 0 ± 0 | 0 % ± 0 | 38.6 ± 0.6 | 37.4 ± 0.5 | 0 |
| t47-atraccion-ender_guardian | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 2 ± 0 | 0 ± 0 | 0 % ± 0 | 38.4 ± 0.6 | 37.8 ± 0.5 | 0 |
| t47-atraccion-tremorzilla | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 6 ± 0 | 0 ± 0 | 0 % ± 0 | 87.4 ± 1.2 | 87.3 ± 1.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t47-atraccion-fire_dragon semilla 1: sprint → draw (2/1 vivos), normal → draw (2/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 208, OUT_OF_RANGE 95, TOO_CLOSE 123

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t47-atraccion-fire_dragon: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-ignis: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-the_harbinger: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-netherite_monstrosity: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-ender_guardian: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-tremorzilla: 10/10 corridas llegaron al tope de tiempo
