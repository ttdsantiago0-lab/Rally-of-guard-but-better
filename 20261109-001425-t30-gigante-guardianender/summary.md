# Simulación Rally - lote 20261109-001425-t30-gigante-guardianender

Preset `t30-gigante-guardianender` · velocidad `sprint` · 25 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t30-gigante-guardianender-hierro | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 155 ± 10 | 51 ± 9 | 512 ± 27 | 90 % ± 3 | 21.2 ± 1.5 | 15.1 ± 1.2 | 0 |
| t30-gigante-guardianender-diamante | 6 | 17 % ± 11 | 83 % ± 11 | 1.2 ± 0.7 | 1.0 ± 0.0 | 256 ± 21 | 152 ± 37 | 790 ± 48 | 87 % ± 2 | 23.3 ± 1.2 | 18.9 ± 1.1 | 0 |
| t30-gigante-guardianender-netherita | 6 | 17 % ± 11 | 83 % ± 11 | 1.0 ± 0.7 | 1.0 ± 0.0 | 261 ± 21 | 127 ± 29 | 719 ± 20 | 83 % ± 3 | 24.0 ± 2.0 | 18.9 ± 1.6 | 0 |
| t30-gigante-guardianender-netheritamas | 6 | 100 % ± 0 | 0 % ± 0 | 15.0 ± 0.4 | 0.0 ± 0.0 | 84 ± 6 | 342 ± 4 | 259 ± 22 | 84 % ± 3 | 14.0 ± 1.2 | 10.9 ± 1.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t30-gigante-guardianender-hierro semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 10 de 10 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t30-gigante-guardianender-hierro: enemy:explosion.player=1 enemy:indirectMagic=5 enemy:mob=90
- t30-gigante-guardianender-diamante: enemy:explosion.player=8 enemy:indirectMagic=15 enemy:mob=63 env:fall=1 other(cataclysm:ender_guardian):explosion.player=2
- t30-gigante-guardianender-netherita: enemy:explosion.player=6 enemy:indirectMagic=22 enemy:mob=62
- t30-gigante-guardianender-netheritamas: enemy:explosion.player=2 enemy:indirectMagic=2 enemy:mob=2

## Motivos de decisión (cambios registrados): COUNTERATTACK 30, ENGAGE 901, FORMATION_HOLD 5, LOW_HEALTH_RETREAT 100, MORALE_SHAKEN 475, MORALE_WAVERING 370, NONE 219, OUT_OF_RANGE 3, RALLIED 43, REFUSE_ATTACK 124, ROUTED_FLEE 351, SEEK_ENEMY 20, STUCK 13, TOO_CLOSE 23, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
