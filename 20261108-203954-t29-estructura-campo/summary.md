# Simulación Rally - lote 20261108-203954-t29-estructura-campo

Preset `t29-estructura-campo` · velocidad `sprint` · 61 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t29-estructura-campo-zombie-linea | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 17 ± 1 | 385 ± 2 | 4 ± 1 | 89 % ± 1 | 7.5 ± 0.1 | 6.7 ± 0.2 | 0 |
| t29-estructura-campo-zombie-arco | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 17 ± 0 | 400 ± 3 | 0 ± 0 | 87 % ± 1 | 9.7 ± 0.1 | 4.2 ± 0.6 | 0 |
| t29-estructura-campo-skeleton-linea | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 23 ± 1 | 277 ± 5 | 6 ± 1 | 74 % ± 2 | 18.8 ± 0.3 | 9.0 ± 0.4 | 0 |
| t29-estructura-campo-skeleton-arco | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 12 ± 0 | 291 ± 2 | 4 ± 1 | 76 % ± 2 | 25.8 ± 0.6 | 5.0 ± 0.1 | 0 |
| t29-estructura-campo-vindicator-linea | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 10 ± 0 | 158 ± 2 | 15 ± 3 | 88 % ± 2 | 9.1 ± 0.3 | 5.9 ± 0.2 | 0 |
| t29-estructura-campo-vindicator-arco | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 7 ± 0 | 161 ± 1 | 0 ± 0 | 72 % ± 3 | 14.0 ± 0.5 | 4.9 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t29-estructura-campo-zombie-linea semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 495, MORALE_SHAKEN 2, SEEK_ENEMY 8, TOO_CLOSE 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
