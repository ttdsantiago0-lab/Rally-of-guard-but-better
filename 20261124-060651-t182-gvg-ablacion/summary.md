# Simulación Rally - lote 20261124-060651-t182-gvg-ablacion

Preset `t182-gvg-ablacion` · velocidad `sprint` · 91 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t182-gv8-rally | 30 | 3 % ± 3 | 97 % ± 3 | 0.0 ± 0.0 | 5.4 ± 0.4 | 15 ± 1 | 121 ± 5 | 191 ± 1 | 0 % ± 0 | 3.5 ± 0.1 | 4.8 ± 0.2 | 0 |
| t182-gv8-sinblanco | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 4.9 ± 0.3 | 16 ± 1 | 127 ± 5 | 192 ± 0 | 0 % ± 0 | 3.4 ± 0.1 | 4.5 ± 0.2 | 0 |
| t182-gv8-solo | 30 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 5.4 ± 0.3 | 15 ± 0 | 118 ± 4 | 192 ± 0 | 0 % ± 0 | 3.4 ± 0.1 | 5.0 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t182-gv8-rally semilla 15100: sprint → red (0/5 vivos), normal → red (0/7 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 1 de 1 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t182-gv8-rally: enemy:mob=239
- t182-gv8-sinblanco: enemy:mob=240
- t182-gv8-solo: enemy:mob=240

## Motivos de decisión (cambios registrados): ENGAGE 741, LOW_HEALTH_RETREAT 737, NONE 186

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
