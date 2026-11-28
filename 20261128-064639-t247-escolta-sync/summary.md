# Simulación Rally - lote 20261128-064639-t247-escolta-sync

Preset `t247-escolta-sync` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t247-ctl | 30 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 5.6 ± 0.5 | 36 ± 3 | 121 ± 9 | 15 ± 2 | 0 % ± 0 | 12.2 ± 0.3 | 3.3 ± 0.1 | 0 |
| t247-sync | 30 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 5.1 ± 0.6 | 37 ± 3 | 123 ± 10 | 15 ± 2 | 0 % ± 0 | 12.1 ± 0.2 | 3.5 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t247-ctl semilla 24700: sprint → draw (8/9 vivos), normal → draw (8/7 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 699, FORMATION_HOLD 562, SEEK_ENEMY 835, STUCK 13

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
