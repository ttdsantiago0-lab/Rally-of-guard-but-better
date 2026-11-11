# Simulación Rally - lote 20261111-214155-t47-atraccion-vanilla1

Preset `t47-atraccion-vanilla1` · velocidad `sprint` · 62 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t47-atraccion-zombie | 10 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 3 ± 0 | 24 ± 1 | 0 ± 0 | 98 % ± 3 | 0.0 ± 0.0 | 0.0 ± 0.0 | 0 |
| t47-atraccion-husk | 10 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 3 ± 0 | 23 ± 1 | 0 ± 0 | 98 % ± 3 | 0.8 ± 0.8 | 0.8 ± 0.8 | 0 |
| t47-atraccion-skeleton | 10 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 3 ± 0 | 23 ± 1 | 0 ± 0 | 96 % ± 3 | 0.8 ± 0.8 | 0.8 ± 0.8 | 0 |
| t47-atraccion-stray | 10 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 3 ± 0 | 23 ± 1 | 0 ± 0 | 100 % ± 0 | 1.0 ± 1.0 | 0.8 ± 0.8 | 0 |
| t47-atraccion-spider | 10 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 4 ± 1 | 17 ± 0 | 0 ± 0 | 70 % ± 5 | 1.8 ± 1.2 | 1.6 ± 1.1 | 1 |
| t47-atraccion-wither_skeleton | 10 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 3 ± 0 | 23 ± 1 | 0 ± 0 | 100 % ± 0 | 0.8 ± 0.8 | 0.8 ± 0.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t47-atraccion-zombie semilla 1: sprint → blue (2/0 vivos), normal → blue (2/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 126, FORMATION_HOLD 16, NONE 2, TOO_CLOSE 3

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t47-atraccion-spider - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
