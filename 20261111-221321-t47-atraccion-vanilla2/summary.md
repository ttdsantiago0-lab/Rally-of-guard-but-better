# Simulación Rally - lote 20261111-221321-t47-atraccion-vanilla2

Preset `t47-atraccion-vanilla2` · velocidad `sprint` · 81 corridas · 8 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombified_piglin=20}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t47-atraccion-pillager | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 10 ± 0 | 0 ± 0 | 0 % ± 0 | 25.9 ± 0.4 | 25.8 ± 0.3 | 0 |
| t47-atraccion-vindicator | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 10 ± 0 | 0 ± 0 | 0 % ± 0 | 13.8 ± 0.2 | 13.1 ± 0.1 | 0 |
| t47-atraccion-piglin | 10 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 15 ± 0 | 5 ± 0 | 0 ± 0 | 0 % ± 0 | 10.2 ± 1.3 | 8.0 ± 0.0 | 0 |
| t47-atraccion-piglin_brute | 10 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 15 ± 0 | 5 ± 0 | 0 ± 0 | 0 % ± 0 | 8.5 ± 0.1 | 8.0 ± 0.0 | 0 |
| t47-atraccion-ravager | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 10 ± 0 | 0 ± 0 | 0 % ± 0 | 26.1 ± 0.3 | 25.9 ± 0.2 | 0 |
| t47-atraccion-witch | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 10 ± 0 | 0 ± 0 | 0 % ± 0 | 16.4 ± 0.4 | 15.6 ± 0.2 | 0 |
| t47-atraccion-blaze | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 10 ± 0 | 0 ± 0 | 0 % ± 0 | 37.7 ± 0.5 | 36.6 ± 0.5 | 0 |
| t47-atraccion-enderman | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 10 ± 0 | 0 ± 0 | 0 % ± 0 | 47.5 ± 0.6 | 47.0 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t47-atraccion-pillager semilla 1: sprint → draw (2/1 vivos), normal → draw (2/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 80 de 80 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 229, OUT_OF_RANGE 36, TOO_CLOSE 109

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t47-atraccion-pillager: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-vindicator: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-ravager: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-witch: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-blaze: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-enderman: 10/10 corridas llegaron al tope de tiempo
