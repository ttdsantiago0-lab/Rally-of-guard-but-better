# Simulación Rally - lote 20261124-065838-t186-gvg-retirada

Preset `t186-gvg-retirada` · velocidad `sprint` · 91 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t186-gv8-rally | 30 | 3 % ± 3 | 97 % ± 3 | 0.2 ± 0.2 | 5.3 ± 0.3 | 15 ± 0 | 120 ± 5 | 191 ± 1 | 0 % ± 0 | 3.4 ± 0.1 | 4.4 ± 0.2 | 0 |
| t186-gv8-sinretirada | 30 | 23 % ± 8 | 77 % ± 8 | 0.5 ± 0.2 | 3.1 ± 0.4 | 15 ± 0 | 160 ± 6 | 187 ± 3 | 0 % ± 0 | 3.4 ± 0.1 | 4.3 ± 0.3 | 0 |
| t186-gv12-sinretirada | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 10.2 ± 0.2 | 12 ± 0 | 119 ± 3 | 192 ± 0 | 0 % ± 0 | 3.0 ± 0.1 | 4.9 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t186-gv8-rally semilla 15100: sprint → red (0/5 vivos), normal → red (0/3 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 8 de 8 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t186-gv8-rally: enemy:mob=235
- t186-gv8-sinretirada: enemy:mob=224
- t186-gv12-sinretirada: enemy:mob=240

## Motivos de decisión (cambios registrados): ENGAGE 744, LOW_HEALTH_RETREAT 245, NONE 68

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
