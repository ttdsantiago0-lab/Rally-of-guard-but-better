# Simulación Rally - lote 20261112-190104-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 65 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1159 ± 4 | 0 ± 0 | 97 % ± 0 | 17.7 ± 0.1 | 7.1 ± 0.3 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 684 ± 6 | 9 ± 1 | 91 % ± 1 | 22.3 ± 0.3 | 3.8 ± 0.1 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 94 % ± 6 | 6 % ± 6 | 8.0 ± 0.0 | 0.3 ± 0.3 | 57 ± 4 | 797 ± 38 | 3 ± 1 | 95 % ± 1 | 18.0 ± 0.4 | 5.4 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 5.9 ± 1.2 | 39 ± 7 | 130 ± 15 | 14 ± 3 | 0 % ± 0 | 12.0 ± 0.7 | 3.7 ± 0.2 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 86 ± 9 | 235 ± 8 | 53 ± 7 | 0 % ± 0 | 14.0 ± 1.2 | 9.4 ± 1.3 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 157 ± 11 | 798 ± 16 | 86 ± 4 | 0 % ± 0 | 14.1 ± 1.1 | 11.1 ± 1.7 | 0 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 27 ± 1 | 144 ± 0 | 7 ± 1 | 0 % ± 0 | 8.2 ± 0.2 | 4.8 ± 0.2 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 32 ± 1 | 240 ± 0 | 18 ± 3 | 0 % ± 0 | 7.9 ± 0.2 | 6.3 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 1021, FORMATION_HOLD 857, LOW_HEALTH_RETREAT 37, MORALE_SHAKEN 91, MORALE_WAVERING 18, NONE 183, OUT_OF_RANGE 1, RALLIED 3, REFUSE_ATTACK 6, ROUTED_FLEE 4, SEEK_ENEMY 856, STUCK 10

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
