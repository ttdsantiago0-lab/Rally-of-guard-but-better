# Simulación Rally - lote 20261111-214248-t47-atraccion-vanilla2

Preset `t47-atraccion-vanilla2` · velocidad `sprint` · 81 corridas · 8 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombified_piglin=10}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t47-atraccion-pillager | 10 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 5 ± 1 | 27 ± 1 | 0 ± 0 | 77 % ± 1 | 1.6 ± 1.1 | 1.6 ± 1.1 | 0 |
| t47-atraccion-vindicator | 10 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 5 ± 1 | 27 ± 1 | 0 ± 0 | 96 % ± 3 | 1.8 ± 1.2 | 1.6 ± 1.1 | 0 |
| t47-atraccion-piglin | 10 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 3 ± 0 | 18 ± 1 | 0 ± 0 | 94 % ± 4 | 0.0 ± 0.0 | 0.0 ± 0.0 | 0 |
| t47-atraccion-piglin_brute | 10 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 15 ± 0 | 36 ± 2 | 0 ± 0 | 93 % ± 4 | 9.0 ± 0.3 | 8.0 ± 0.0 | 0 |
| t47-atraccion-ravager | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 41 ± 1 | 0 ± 0 | 100 % ± 0 | 26.6 ± 0.2 | 26.2 ± 0.2 | 0 |
| t47-atraccion-witch | 10 | 85 % ± 8 | 15 % ± 8 | 2.0 ± 0.0 | 0.3 ± 0.2 | 20 ± 9 | 31 ± 1 | 0 ± 0 | 98 % ± 2 | 5.4 ± 2.7 | 4.7 ± 2.4 | 0 |
| t47-atraccion-blaze | 10 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 3 ± 0 | 24 ± 1 | 0 ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 0.0 ± 0.0 | 0 |
| t47-atraccion-enderman | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 10 ± 0 | 0 ± 0 | 0 % ± 0 | 48.8 ± 0.9 | 46.5 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t47-atraccion-pillager semilla 1: sprint → blue (2/0 vivos), normal → blue (2/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 80 de 80 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 150, FORMATION_HOLD 23, NONE 4, OUT_OF_RANGE 14, SEEK_ENEMY 17, STUCK 25, TOO_CLOSE 7

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t47-atraccion-ravager: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-enderman: 10/10 corridas llegaron al tope de tiempo
