# Simulación Rally - lote 20261125-093619-t212-reparto-cac

Preset `t212-reparto-cac` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t212-rep-cac-capa | 30 | 27 % ± 8 | 73 % ± 8 | 0.8 ± 0.3 | 2.7 ± 0.4 | 16 ± 0 | 160 ± 5 | 183 ± 4 | 0 % ± 0 | 3.4 ± 0.1 | 4.0 ± 0.2 | 0 |
| t212-rep-cac-solo | 30 | 20 % ± 7 | 80 % ± 7 | 0.5 ± 0.2 | 2.6 ± 0.3 | 16 ± 0 | 166 ± 4 | 187 ± 2 | 0 % ± 0 | 3.4 ± 0.1 | 3.9 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t212-rep-cac-capa semilla 15100: sprint → red (0/4 vivos), normal → red (0/7 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 14 de 14 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t212-rep-cac-capa: enemy:mob=215
- t212-rep-cac-solo: enemy:mob=225

## Motivos de decisión (cambios registrados): ENGAGE 509, NONE 32

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
