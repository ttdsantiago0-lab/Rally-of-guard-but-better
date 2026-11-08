# Simulación Rally - lote 20261108-193229-t23-techo-phantom-base

Preset `t23-techo-phantom-base` · velocidad `sprint` · 32 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t23-techo-phantom-arco-base-r10 | 15 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 34 ± 3 | 239 ± 2 | 11 ± 1 | 40 % ± 3 | 28.4 ± 2.7 | 4.4 ± 0.5 | 0 |
| t23-techo-phantom-linea-base-r10 | 15 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 37 ± 2 | 238 ± 2 | 11 ± 1 | 45 % ± 2 | 24.0 ± 2.2 | 5.7 ± 0.4 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t23-techo-phantom-arco-base-r10 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 30 de 30 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 830, NONE 8, OUT_OF_RANGE 219, SEEK_ENEMY 36, TOO_CLOSE 358

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t23-techo-phantom-linea-base-r10 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
