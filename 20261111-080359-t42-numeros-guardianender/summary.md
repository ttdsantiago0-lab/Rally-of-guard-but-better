# Simulación Rally - lote 20261111-080359-t42-numeros-guardianender

Preset `t42-numeros-guardianender` · velocidad `sprint` · 31 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t42-numeros-guardianender-hierro-g4-sincro | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 26 ± 5 | 15 ± 4 | 115 ± 11 | 96 % ± 3 | 11.1 ± 0.6 | 2.6 ± 0.3 | 0 |
| t42-numeros-guardianender-netheritamas-g4-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 3.6 ± 0.4 | 0.0 ± 0.0 | 70 ± 4 | 335 ± 1 | 104 ± 12 | 93 % ± 3 | 9.2 ± 0.5 | 6.7 ± 0.6 | 0 |
| t42-numeros-guardianender-hierro-g8-sincro | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 55 ± 12 | 39 ± 6 | 230 ± 11 | 97 % ± 2 | 10.4 ± 0.6 | 4.4 ± 0.9 | 0 |
| t42-numeros-guardianender-netheritamas-g8-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 64 ± 2 | 334 ± 0 | 125 ± 18 | 89 % ± 4 | 8.6 ± 0.5 | 7.2 ± 0.6 | 0 |
| t42-numeros-guardianender-hierro-g16-sincro | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 105 ± 16 | 56 ± 14 | 463 ± 9 | 94 % ± 1 | 10.7 ± 0.3 | 5.1 ± 0.8 | 0 |
| t42-numeros-guardianender-netheritamas-g16-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 15.8 ± 0.2 | 0.0 ± 0.0 | 66 ± 2 | 335 ± 1 | 235 ± 42 | 78 % ± 2 | 8.4 ± 0.4 | 6.4 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t42-numeros-guardianender-hierro-g4-sincro semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 15 de 15 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t42-numeros-guardianender-hierro-g4-sincro: enemy:indirectMagic=2 enemy:mob=18
- t42-numeros-guardianender-netheritamas-g4-sincro: enemy:indirectMagic=1 enemy:mob=1
- t42-numeros-guardianender-hierro-g8-sincro: enemy:explosion.player=2 enemy:indirectMagic=3 enemy:mob=34 other(cataclysm:ender_guardian):explosion.player=1
- t42-numeros-guardianender-hierro-g16-sincro: enemy:indirectMagic=6 enemy:mob=74
- t42-numeros-guardianender-netheritamas-g16-sincro: enemy:indirectMagic=1

## Motivos de decisión (cambios registrados): ENGAGE 368, LOW_HEALTH_RETREAT 95, NONE 1, SEEK_ENEMY 47, STUCK 5, TOO_CLOSE 33

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
