# Simulación Rally - lote 20261124-072349-t189-gvg-grupo

Preset `t189-gvg-grupo` · velocidad `sprint` · 91 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t189-gv8-nr | 30 | 27 % ± 8 | 73 % ± 8 | 0.8 ± 0.3 | 2.5 ± 0.4 | 16 ± 0 | 164 ± 5 | 185 ± 2 | 0 % ± 0 | 3.3 ± 0.1 | 4.7 ± 0.2 | 0 |
| t189-gv8-nr-sinblanco | 30 | 27 % ± 8 | 73 % ± 8 | 0.9 ± 0.3 | 2.5 ± 0.3 | 16 ± 0 | 164 ± 4 | 185 ± 3 | 0 % ± 0 | 3.5 ± 0.1 | 4.4 ± 0.2 | 0 |
| t189-gv8-nr-solo | 30 | 27 % ± 8 | 73 % ± 8 | 0.8 ± 0.3 | 2.5 ± 0.4 | 16 ± 0 | 161 ± 5 | 185 ± 3 | 0 % ± 0 | 3.3 ± 0.1 | 4.2 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t189-gv8-nr semilla 15100: sprint → blue (1/0 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 24 de 24 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t189-gv8-nr: enemy:mob=217
- t189-gv8-nr-sinblanco: enemy:mob=212
- t189-gv8-nr-solo: enemy:mob=217

## Motivos de decisión (cambios registrados): ENGAGE 772, NONE 50

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t189-gv8-nr: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
