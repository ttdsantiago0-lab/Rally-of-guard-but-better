# Simulación Rally - lote 20261124-030654-t169-multiplicador-gvg

Preset `t169-multiplicador-gvg` · velocidad `sprint` · 151 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t169-gvg-espejo-8 | 30 | 43 % ± 9 | 57 % ± 9 | 2.5 ± 0.6 | 3.1 ± 0.6 | 33 ± 4 | 169 ± 5 | 172 ± 5 | 0 % ± 0 | 6.8 ± 0.7 | 6.2 ± 0.4 | 0 |
| t169-gvg-gv-8 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 5.6 ± 0.3 | 15 ± 0 | 119 ± 3 | 192 ± 0 | 0 % ± 0 | 3.5 ± 0.1 | 4.2 ± 0.2 | 0 |
| t169-gvg-gv-12 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 10.9 ± 0.2 | 12 ± 0 | 92 ± 4 | 192 ± 0 | 0 % ± 0 | 3.0 ± 0.1 | 4.8 ± 0.2 | 0 |
| t169-gvg-gv-16 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 15.4 ± 0.1 | 11 ± 0 | 84 ± 3 | 192 ± 0 | 0 % ± 0 | 2.9 ± 0.1 | 4.5 ± 0.3 | 0 |
| t169-gvg-gv-24 | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 23.9 ± 0.1 | 9 ± 0 | 58 ± 2 | 192 ± 0 | 0 % ± 0 | 2.9 ± 0.1 | 4.9 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t169-gvg-espejo-8 semilla 15100: sprint → red (0/4 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 13 de 13 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t169-gvg-espejo-8: enemy:mob=164
- t169-gvg-gv-8: enemy:mob=240
- t169-gvg-gv-12: enemy:mob=240
- t169-gvg-gv-16: enemy:mob=240
- t169-gvg-gv-24: enemy:mob=240

## Motivos de decisión (cambios registrados): ENGAGE 1476, LOW_HEALTH_RETREAT 1973, NONE 180, SEEK_ENEMY 652, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t169-gvg-espejo-8: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
