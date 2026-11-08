# Simulación Rally - lote 20261108-193439-t23-techo-phantom-oratodo

Preset `t23-techo-phantom-oratodo` · velocidad `sprint` · 41 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t23-techo-phantom-arco-oratodo-r10 | 13 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 31 ± 2 | 239 ± 2 | 3 ± 1 | 24 % ± 1 | 19.4 ± 2.1 | 12.1 ± 1.2 | 7 |
| t23-techo-phantom-linea-oratodo-r10 | 14 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 34 ± 3 | 239 ± 2 | 4 ± 1 | 30 % ± 2 | 20.2 ± 1.6 | 12.7 ± 1.0 | 5 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t23-techo-phantom-arco-oratodo-r10 semilla 1: sprint → draw (8/1 vivos), normal → draw (8/1 vivos)
- t23-techo-phantom-arco-oratodo-r10 semilla 7778: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 27 de 27 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 900, MORALE_SHAKEN 10, NONE 26, OUT_OF_RANGE 346, SEEK_ENEMY 238, STUCK 60, TOO_CLOSE 157

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t23-techo-phantom-arco-oratodo-r10 - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t23-techo-phantom-linea-oratodo-r10 - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
