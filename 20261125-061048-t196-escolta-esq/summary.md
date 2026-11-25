# Simulación Rally - lote 20261125-061048-t196-escolta-esq

Preset `t196-escolta-esq` · velocidad `sprint` · 31 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-esqueletos-escudo-defender | 30 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 6.2 ± 0.5 | 35 ± 3 | 110 ± 8 | 14 ± 2 | 0 % ± 0 | 12.5 ± 0.2 | 3.4 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-esqueletos-escudo-defender semilla 31: sprint → draw (8/9 vivos), normal → draw (8/6 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 30 de 30 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 338, FORMATION_HOLD 306, MORALE_SHAKEN 1, MORALE_WAVERING 1, SEEK_ENEMY 423, STUCK 12

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
