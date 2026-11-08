# Simulación Rally - lote 20261108-193517-t23-techo-phantom-orakite

Preset `t23-techo-phantom-orakite` · velocidad `sprint` · 37 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t23-techo-phantom-arco-orakite-r10 | 14 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 31 ± 2 | 243 ± 2 | 2 ± 0 | 37 % ± 2 | 27.7 ± 2.4 | 10.0 ± 0.7 | 4 |
| t23-techo-phantom-linea-orakite-r10 | 14 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 36 ± 4 | 235 ± 2 | 3 ± 1 | 50 % ± 3 | 23.0 ± 1.6 | 14.9 ± 1.1 | 4 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t23-techo-phantom-arco-orakite-r10 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 28 de 28 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 811, MORALE_SHAKEN 21, MORALE_WAVERING 1, NONE 41, OUT_OF_RANGE 335, REFUSE_ATTACK 1, SEEK_ENEMY 148, STUCK 33, TOO_CLOSE 147

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t23-techo-phantom-linea-orakite-r10 - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t23-techo-phantom-arco-orakite-r10 - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
