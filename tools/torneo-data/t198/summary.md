# Simulación Rally - lote 20261125-064017-t198-retirada-cruce

Preset `t198-retirada-cruce` · velocidad `sprint` · 81 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t198-ret-b8r8 | 20 | 48 % ± 2 | 53 % ± 2 | 2.6 ± 0.3 | 6.9 ± 0.3 | 116 ± 4 | 54 ± 7 | 174 ± 8 | 28 % ± 10 | 21.4 ± 2.1 | 33.0 ± 2.3 | 0 |
| t198-ret-b8r100 | 20 | 43 % ± 4 | 57 % ± 4 | 1.2 ± 0.2 | 99.9 ± 0.1 | 110 ± 5 | 36 ± 5 | 189 ± 5 | 30 % ± 11 | 12.8 ± 0.5 | 15.5 ± 3.2 | 0 |
| t198-ret-b100r8 | 20 | 50 % ± 0 | 50 % ± 0 | 87.8 ± 1.0 | 4.0 ± 0.4 | 120 ± 0 | 113 ± 10 | 417 ± 29 | 68 % ± 10 | 36.3 ± 2.0 | 13.6 ± 0.2 | 0 |
| t198-ret-b100r100 | 20 | 50 % ± 0 | 50 % ± 0 | 12.3 ± 1.3 | 93.9 ± 0.7 | 120 ± 0 | 499 ± 20 | 2556 ± 31 | 97 % ± 1 | 13.0 ± 0.3 | 44.4 ± 0.7 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t198-ret-b8r8 semilla 17000: sprint → draw (6/7 vivos), normal → draw (1/8 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 76 de 76 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t198-ret-b8r8: enemy:mob=108
- t198-ret-b8r100: enemy:mob=137
- t198-ret-b100r8: enemy:mob=244
- t198-ret-b100r100: enemy:mob=1755

## Motivos de decisión (cambios registrados): INSTRUCTION 4328, ORDER_OVERRIDE 4330, STUCK 2, UNIT_BROKEN 11

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t198-ret-b8r8: 19/20 corridas llegaron al tope de tiempo
- t198-ret-b8r100: 17/20 corridas llegaron al tope de tiempo
- t198-ret-b100r8: 20/20 corridas llegaron al tope de tiempo
- t198-ret-b100r100: 20/20 corridas llegaron al tope de tiempo
