# Simulación Rally - lote 20261108-193040-t23-techo-phantom-oracadencia

Preset `t23-techo-phantom-oracadencia` · velocidad `sprint` · 33 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t23-techo-phantom-arco-oracadencia-r10 | 15 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 28 ± 2 | 241 ± 1 | 9 ± 0 | 43 % ± 3 | 24.2 ± 2.4 | 3.6 ± 0.2 | 0 |
| t23-techo-phantom-linea-oracadencia-r10 | 14 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 33 ± 2 | 239 ± 2 | 9 ± 0 | 46 % ± 3 | 24.4 ± 2.3 | 7.0 ± 0.5 | 3 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t23-techo-phantom-arco-oracadencia-r10 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 29 de 29 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 775, MORALE_SHAKEN 1, NONE 14, OUT_OF_RANGE 209, SEEK_ENEMY 49, TOO_CLOSE 297

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t23-techo-phantom-linea-oracadencia-r10 - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
