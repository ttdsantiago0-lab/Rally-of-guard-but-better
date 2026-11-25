# Simulación Rally - lote 20261125-205211-t220-cac-escala

Preset `t220-cac-escala` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t220-16-capa | 30 | 20 % ± 7 | 80 % ± 7 | 1.0 ± 0.4 | 5.0 ± 0.7 | 17 ± 1 | 327 ± 9 | 375 ± 4 | 0 % ± 0 | 3.5 ± 0.1 | 8.4 ± 0.3 | 0 |
| t220-16-sync | 30 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.4 | 0.0 ± 0.0 | 16 ± 0 | 384 ± 0 | 299 ± 6 | 0 % ± 0 | 4.4 ± 0.1 | 9.1 ± 0.2 | 0 |
| t220-32-capa | 30 | 30 % ± 9 | 70 % ± 9 | 2.6 ± 0.9 | 5.0 ± 0.9 | 21 ± 1 | 712 ± 11 | 739 ± 11 | 0 % ± 0 | 3.4 ± 0.1 | 8.4 ± 0.3 | 0 |
| t220-32-sync | 30 | 100 % ± 0 | 0 % ± 0 | 17.1 ± 0.7 | 0.0 ± 0.0 | 16 ± 0 | 768 ± 0 | 556 ± 9 | 0 % ± 0 | 4.6 ± 0.1 | 9.9 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t220-16-capa semilla 19000: sprint → red (0/8 vivos), normal → red (0/9 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 75 de 75 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t220-16-capa: enemy:mob=451
- t220-16-sync: enemy:mob=247
- t220-32-capa: enemy:mob=881
- t220-32-sync: enemy:mob=446

## Motivos de decisión (cambios registrados): ENGAGE 3129, NONE 403

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
