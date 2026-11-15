# Simulación Rally - lote 20261115-030949-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 65 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1170 ± 8 | 0 ± 0 | 98 % ± 0 | 18.0 ± 0.2 | 6.7 ± 0.1 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 681 ± 7 | 9 ± 2 | 91 % ± 1 | 23.1 ± 0.5 | 3.8 ± 0.1 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 94 % ± 6 | 6 % ± 6 | 8.0 ± 0.0 | 0.4 ± 0.4 | 56 ± 4 | 798 ± 40 | 9 ± 2 | 95 % ± 1 | 17.3 ± 0.4 | 5.6 ± 0.2 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 6.6 ± 1.0 | 34 ± 6 | 123 ± 12 | 14 ± 2 | 0 % ± 0 | 12.1 ± 0.6 | 3.4 ± 0.2 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 93 ± 11 | 215 ± 10 | 61 ± 5 | 0 % ± 0 | 14.1 ± 1.0 | 11.1 ± 2.0 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 161 ± 9 | 749 ± 13 | 97 ± 6 | 0 % ± 0 | 13.6 ± 1.0 | 10.8 ± 1.2 | 0 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 24 ± 1 | 144 ± 0 | 10 ± 1 | 0 % ± 0 | 8.0 ± 0.2 | 4.8 ± 0.3 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 34 ± 2 | 240 ± 0 | 19 ± 4 | 0 % ± 0 | 7.5 ± 0.2 | 6.6 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 5601: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 1187, FORMATION_HOLD 860, LOW_HEALTH_RETREAT 48, MORALE_SHAKEN 213, MORALE_WAVERING 43, NONE 173, RALLIED 2, ROUTED_FLEE 4, SEEK_ENEMY 883, STUCK 13

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
