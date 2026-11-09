# Simulación Rally - lote 20261109-194821-t34-moral-guardianender

Preset `t34-moral-guardianender` · velocidad `sprint` · 17 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t34-moral-guardianender-conmoral | 8 | 100 % ± 0 | 0 % ± 0 | 15.4 ± 0.3 | 0.0 ± 0.0 | 83 ± 10 | 337 ± 1 | 237 ± 22 | 84 % ± 3 | 17.5 ± 2.7 | 13.5 ± 2.0 | 0 |
| t34-moral-guardianender-sinmoral | 8 | 100 % ± 0 | 0 % ± 0 | 14.3 ± 0.6 | 0.0 ± 0.0 | 65 ± 2 | 334 ± 0 | 262 ± 22 | 77 % ± 3 | 13.5 ± 1.0 | 11.1 ± 0.9 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t34-moral-guardianender-conmoral semilla 1: sprint → blue (15/0 vivos), normal → blue (16/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 16 de 16 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t34-moral-guardianender-conmoral: enemy:indirectMagic=3 enemy:mob=2
- t34-moral-guardianender-sinmoral: enemy:indirectMagic=6 enemy:mob=8

## Motivos de decisión (cambios registrados): COUNTERATTACK 7, ENGAGE 511, LOW_HEALTH_RETREAT 73, MORALE_SHAKEN 194, MORALE_WAVERING 151, NONE 106, RALLIED 14, REFUSE_ATTACK 54, ROUTED_FLEE 135, SEEK_ENEMY 26, STUCK 9, TOO_CLOSE 15

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
