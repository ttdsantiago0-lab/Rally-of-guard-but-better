# Simulación Rally - lote 20261111-080208-t42-numeros-dragonjoven

Preset `t42-numeros-dragonjoven` · velocidad `sprint` · 41 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t42-numeros-dragonjoven-hierro-g2-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 18 ± 1 | 139 ± 2 | 2 ± 1 | 13 % ± 3 | 15.8 ± 0.6 | 3.3 ± 0.2 | 0 |
| t42-numeros-dragonjoven-netheritamas-g2-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 2.0 ± 0.0 | 0.0 ± 0.0 | 10 ± 0 | 148 ± 4 | 0 ± 0 | 11 % ± 3 | 19.3 ± 0.2 | 2.5 ± 0.1 | 0 |
| t42-numeros-dragonjoven-hierro-g4-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 4.0 ± 0.0 | 0.0 ± 0.0 | 15 ± 1 | 140 ± 1 | 0 ± 0 | 43 % ± 4 | 16.0 ± 0.3 | 4.6 ± 0.3 | 0 |
| t42-numeros-dragonjoven-netheritamas-g4-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 4.0 ± 0.0 | 0.0 ± 0.0 | 9 ± 0 | 145 ± 4 | 0 ± 0 | 52 % ± 10 | 20.4 ± 0.9 | 3.3 ± 0.2 | 0 |
| t42-numeros-dragonjoven-hierro-g8-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 17 ± 1 | 139 ± 1 | 2 ± 2 | 58 % ± 7 | 17.1 ± 0.7 | 4.3 ± 0.2 | 0 |
| t42-numeros-dragonjoven-netheritamas-g8-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 10 ± 0 | 140 ± 1 | 5 ± 5 | 50 % ± 8 | 20.7 ± 0.6 | 4.5 ± 0.2 | 0 |
| t42-numeros-dragonjoven-hierro-g16-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 16 ± 1 | 139 ± 1 | 0 ± 0 | 64 % ± 8 | 20.7 ± 0.4 | 7.9 ± 0.3 | 0 |
| t42-numeros-dragonjoven-netheritamas-g16-sincro | 5 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 10 ± 0 | 144 ± 3 | 2 ± 2 | 41 % ± 9 | 23.6 ± 0.5 | 7.9 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t42-numeros-dragonjoven-hierro-g2-sincro semilla 1: sprint → blue (2/0 vivos), normal → blue (2/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 284, NONE 7, SEEK_ENEMY 18

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
