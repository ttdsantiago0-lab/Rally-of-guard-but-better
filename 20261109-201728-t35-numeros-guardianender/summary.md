# Simulación Rally - lote 20261109-201728-t35-numeros-guardianender

Preset `t35-numeros-guardianender` · velocidad `sprint` · 31 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t35-numeros-guardianender-hierro-g4 | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 25 ± 6 | 13 ± 3 | 113 ± 5 | 76 % ± 19 | 14.8 ± 1.5 | 4.2 ± 1.2 | 0 |
| t35-numeros-guardianender-netheritamas-g4 | 5 | 100 % ± 0 | 0 % ± 0 | 3.2 ± 0.4 | 0.0 ± 0.0 | 83 ± 6 | 343 ± 5 | 111 ± 12 | 93 % ± 2 | 13.3 ± 1.9 | 10.5 ± 2.5 | 0 |
| t35-numeros-guardianender-hierro-g8 | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 69 ± 15 | 12 ± 2 | 253 ± 10 | 101 % ± 8 | 15.3 ± 2.9 | 7.5 ± 1.9 | 0 |
| t35-numeros-guardianender-netheritamas-g8 | 5 | 100 % ± 0 | 0 % ± 0 | 7.2 ± 0.4 | 0.0 ± 0.0 | 66 ± 2 | 335 ± 1 | 137 ± 10 | 88 % ± 3 | 11.0 ± 1.1 | 9.0 ± 1.2 | 0 |
| t35-numeros-guardianender-hierro-g16 | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 107 ± 5 | 30 ± 4 | 454 ± 17 | 94 % ± 1 | 19.5 ± 1.4 | 12.5 ± 1.4 | 0 |
| t35-numeros-guardianender-netheritamas-g16 | 5 | 100 % ± 0 | 0 % ± 0 | 13.0 ± 0.5 | 0.0 ± 0.0 | 63 ± 1 | 335 ± 1 | 285 ± 26 | 78 % ± 5 | 15.4 ± 2.0 | 12.3 ± 1.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t35-numeros-guardianender-hierro-g4 semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 15 de 15 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t35-numeros-guardianender-hierro-g4: enemy:indirectMagic=3 enemy:mob=17
- t35-numeros-guardianender-netheritamas-g4: enemy:indirectMagic=3 enemy:mob=1
- t35-numeros-guardianender-hierro-g8: enemy:explosion.player=2 enemy:indirectMagic=6 enemy:mob=32
- t35-numeros-guardianender-netheritamas-g8: enemy:indirectMagic=3 enemy:mob=1
- t35-numeros-guardianender-hierro-g16: enemy:indirectMagic=3 enemy:mob=77
- t35-numeros-guardianender-netheritamas-g16: enemy:explosion.player=2 enemy:indirectMagic=11 enemy:mob=2

## Motivos de decisión (cambios registrados): ENGAGE 303, FORMATION_HOLD 7, LOW_HEALTH_RETREAT 80, NONE 75, SEEK_ENEMY 33, STUCK 5, TOO_CLOSE 46

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
