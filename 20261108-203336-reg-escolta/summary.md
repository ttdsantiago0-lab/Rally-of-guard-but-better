# Simulación Rally - lote 20261108-203336-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 65 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 11.4 ± 0.9 | 65 ± 0 | 934 ± 18 | 4 ± 1 | 89 % ± 1 | 14.0 ± 0.2 | 6.4 ± 0.2 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 69 % ± 9 | 31 % ± 9 | 8.0 ± 0.0 | 2.9 ± 1.0 | 55 ± 5 | 553 ± 43 | 15 ± 3 | 83 % ± 1 | 19.1 ± 0.3 | 4.9 ± 0.3 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 75 % ± 9 | 25 % ± 9 | 8.0 ± 0.0 | 1.9 ± 0.8 | 64 ± 1 | 786 ± 15 | 10 ± 2 | 86 % ± 1 | 15.2 ± 0.3 | 6.5 ± 0.3 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 5.5 ± 1.0 | 35 ± 7 | 108 ± 13 | 14 ± 4 | 0 % ± 0 | 11.7 ± 0.7 | 3.4 ± 0.3 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 83 ± 10 | 220 ± 11 | 60 ± 6 | 0 % ± 0 | 14.7 ± 1.6 | 10.2 ± 1.7 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 157 ± 7 | 769 ± 19 | 98 ± 8 | 0 % ± 0 | 15.1 ± 0.8 | 11.3 ± 1.0 | 0 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 1 | 144 ± 0 | 11 ± 2 | 0 % ± 0 | 8.0 ± 0.2 | 4.4 ± 0.2 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 35 ± 1 | 240 ± 0 | 19 ± 1 | 0 % ± 0 | 8.0 ± 0.2 | 6.5 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 1: sprint → draw (8/13 vivos), normal → draw (8/9 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t19-testudo-esqueletos-escudo-testudocarga: enemy:arrow=2

## Motivos de decisión (cambios registrados): ENGAGE 1156, FORMATION_HOLD 694, LOW_HEALTH_RETREAT 35, MORALE_SHAKEN 121, MORALE_WAVERING 11, NONE 155, ROUTED_FLEE 1, SEEK_ENEMY 885, STUCK 6, TOO_CLOSE 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
