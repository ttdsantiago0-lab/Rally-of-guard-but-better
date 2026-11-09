# Simulación Rally - lote 20261109-201820-t35-numeros-dragonadulto

Preset `t35-numeros-dragonadulto` · velocidad `sprint` · 41 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t35-numeros-dragonadulto-hierro-g4 | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 50 ± 7 | 128 ± 31 | 122 ± 9 | 22 % ± 9 | 18.3 ± 2.8 | 9.8 ± 2.7 | 0 |
| t35-numeros-dragonadulto-netheritamas-g4 | 5 | 100 % ± 0 | 0 % ± 0 | 3.6 ± 0.2 | 0.0 ± 0.0 | 38 ± 8 | 254 ± 1 | 40 ± 19 | 9 % ± 6 | 17.4 ± 2.1 | 11.4 ± 2.8 | 0 |
| t35-numeros-dragonadulto-hierro-g8 | 5 | 100 % ± 0 | 0 % ± 0 | 6.4 ± 0.9 | 0.0 ± 0.0 | 36 ± 3 | 252 ± 0 | 61 ± 20 | 38 % ± 6 | 14.5 ± 2.4 | 9.8 ± 2.1 | 0 |
| t35-numeros-dragonadulto-netheritamas-g8 | 5 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 0 | 261 ± 2 | 4 ± 2 | 46 % ± 5 | 19.7 ± 2.0 | 8.2 ± 1.3 | 0 |
| t35-numeros-dragonadulto-hierro-g16 | 5 | 100 % ± 0 | 0 % ± 0 | 15.4 ± 0.4 | 0.0 ± 0.0 | 26 ± 1 | 254 ± 2 | 54 ± 20 | 37 % ± 3 | 21.1 ± 1.5 | 12.7 ± 1.1 | 0 |
| t35-numeros-dragonadulto-netheritamas-g16 | 5 | 100 % ± 0 | 0 % ± 0 | 16.0 ± 0.0 | 0.0 ± 0.0 | 17 ± 4 | 253 ± 1 | 1 ± 0 | 39 % ± 5 | 20.3 ± 2.4 | 10.2 ± 0.9 | 0 |
| t35-numeros-dragonadulto-hierro-g32 | 5 | 100 % ± 0 | 0 % ± 0 | 31.0 ± 0.5 | 0.0 ± 0.0 | 23 ± 1 | 253 ± 2 | 96 ± 41 | 40 % ± 2 | 26.1 ± 3.1 | 14.1 ± 1.4 | 0 |
| t35-numeros-dragonadulto-netheritamas-g32 | 5 | 100 % ± 0 | 0 % ± 0 | 32.0 ± 0.0 | 0.0 ± 0.0 | 12 ± 1 | 259 ± 3 | 0 ± 0 | 40 % ± 4 | 26.3 ± 0.6 | 11.4 ± 0.7 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t35-numeros-dragonadulto-hierro-g4 semilla 1: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 35 de 35 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t35-numeros-dragonadulto-hierro-g4: enemy:dragon_fire=13 enemy:mob=6 env:onFire=1
- t35-numeros-dragonadulto-netheritamas-g4: enemy:dragon_fire=1 enemy:mob=1
- t35-numeros-dragonadulto-hierro-g8: enemy:dragon_fire=4 enemy:mob=4
- t35-numeros-dragonadulto-hierro-g16: enemy:dragon_fire=3
- t35-numeros-dragonadulto-hierro-g32: enemy:dragon_fire=5

## Motivos de decisión (cambios registrados): ENGAGE 507, LOW_HEALTH_RETREAT 60, NONE 215, OUT_OF_RANGE 9, SEEK_ENEMY 56, STUCK 5, TOO_CLOSE 42

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
