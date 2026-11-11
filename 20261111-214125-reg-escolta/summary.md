# Simulación Rally - lote 20261111-214125-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 65 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1168 ± 7 | 0 ± 0 | 97 % ± 1 | 17.4 ± 0.1 | 6.8 ± 0.1 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 661 ± 5 | 11 ± 2 | 90 % ± 1 | 23.3 ± 0.2 | 3.7 ± 0.2 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 60 ± 2 | 790 ± 11 | 3 ± 1 | 95 % ± 1 | 17.4 ± 0.2 | 5.3 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 5.8 ± 1.0 | 33 ± 6 | 112 ± 20 | 15 ± 3 | 0 % ± 0 | 12.5 ± 0.3 | 3.4 ± 0.3 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 102 ± 9 | 222 ± 10 | 82 ± 9 | 0 % ± 0 | 12.2 ± 0.9 | 11.3 ± 1.2 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 174 ± 14 | 768 ± 10 | 125 ± 9 | 0 % ± 0 | 14.1 ± 0.7 | 13.8 ± 1.4 | 0 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 24 ± 1 | 144 ± 0 | 10 ± 2 | 0 % ± 0 | 8.0 ± 0.1 | 4.5 ± 0.2 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 34 ± 2 | 240 ± 0 | 19 ± 2 | 0 % ± 0 | 7.8 ± 0.2 | 6.1 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t19-testudo-mezcla-escudo-testudocarga: enemy:explosion.player=2

## Motivos de decisión (cambios registrados): ENGAGE 1223, FORMATION_HOLD 874, LOW_HEALTH_RETREAT 39, MORALE_SHAKEN 274, MORALE_WAVERING 51, NONE 152, RALLIED 5, REFUSE_ATTACK 6, ROUTED_FLEE 24, SEEK_ENEMY 861, STUCK 13, TOO_CLOSE 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
