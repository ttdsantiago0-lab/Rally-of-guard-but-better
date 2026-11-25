# Simulación Rally - lote 20261125-062306-t197-retirada-escala

Preset `t197-retirada-escala` · velocidad `sprint` · 81 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t197-ret-b8 | 20 | 43 % ± 4 | 57 % ± 4 | 2.6 ± 0.5 | 6.7 ± 0.3 | 116 ± 2 | 63 ± 7 | 174 ± 11 | 63 % ± 11 | 19.9 ± 1.9 | 28.3 ± 2.7 | 0 |
| t197-ret-b25 | 20 | 50 % ± 0 | 50 % ± 0 | 4.1 ± 0.4 | 22.2 ± 0.6 | 120 ± 0 | 156 ± 14 | 623 ± 9 | 92 % ± 5 | 13.5 ± 0.4 | 37.6 ± 1.6 | 0 |
| t197-ret-b50 | 20 | 50 % ± 0 | 50 % ± 0 | 7.0 ± 0.8 | 45.9 ± 0.4 | 120 ± 0 | 278 ± 13 | 1260 ± 18 | 95 % ± 1 | 12.4 ± 0.4 | 43.4 ± 1.1 | 0 |
| t197-ret-b100 | 20 | 50 % ± 0 | 50 % ± 0 | 11.0 ± 1.2 | 92.9 ± 0.8 | 120 ± 0 | 526 ± 23 | 2579 ± 24 | 96 % ± 1 | 12.8 ± 0.2 | 43.4 ± 0.7 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t197-ret-b8 semilla 17000: sprint → draw (6/5 vivos), normal → red (0/8 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 77 de 77 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t197-ret-b8: enemy:mob=108
- t197-ret-b25: enemy:mob=419
- t197-ret-b50: enemy:mob=860
- t197-ret-b100: enemy:mob=1780

## Motivos de decisión (cambios registrados): INSTRUCTION 3668, ORDER_OVERRIDE 3668, UNIT_BROKEN 22

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t197-ret-b8: 17/20 corridas llegaron al tope de tiempo
- t197-ret-b25: 20/20 corridas llegaron al tope de tiempo
- t197-ret-b50: 20/20 corridas llegaron al tope de tiempo
- t197-ret-b100: 20/20 corridas llegaron al tope de tiempo
- t197-ret-b8: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
