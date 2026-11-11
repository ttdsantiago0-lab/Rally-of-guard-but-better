# Simulación Rally - lote 20261111-221241-t47-atraccion-vanilla1

Preset `t47-atraccion-vanilla1` · velocidad `sprint` · 61 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t47-atraccion-zombie | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 9 ± 0 | 0 ± 0 | 0 % ± 0 | 27.8 ± 0.2 | 27.4 ± 0.2 | 0 |
| t47-atraccion-husk | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 9 ± 0 | 0 ± 0 | 0 % ± 0 | 29.4 ± 0.3 | 28.8 ± 0.3 | 0 |
| t47-atraccion-skeleton | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 10 ± 0 | 0 ± 0 | 0 % ± 0 | 18.1 ± 0.4 | 15.8 ± 0.1 | 0 |
| t47-atraccion-stray | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 10 ± 0 | 0 ± 0 | 0 % ± 0 | 17.8 ± 0.4 | 15.7 ± 0.2 | 0 |
| t47-atraccion-spider | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.1 ± 0.1 | 60 ± 0 | 10 ± 0 | 0 ± 0 | 0 % ± 0 | 18.2 ± 0.7 | 15.9 ± 0.1 | 0 |
| t47-atraccion-wither_skeleton | 10 | 50 % ± 0 | 50 % ± 0 | 2.0 ± 0.0 | 1.0 ± 0.0 | 60 ± 0 | 10 ± 0 | 0 ± 0 | 0 % ± 0 | 16.9 ± 0.3 | 15.9 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t47-atraccion-zombie semilla 1: sprint → draw (2/1 vivos), normal → draw (2/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 187, OUT_OF_RANGE 18, TOO_CLOSE 79

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t47-atraccion-zombie: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-husk: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-skeleton: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-stray: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-spider: 10/10 corridas llegaron al tope de tiempo
- t47-atraccion-wither_skeleton: 10/10 corridas llegaron al tope de tiempo
