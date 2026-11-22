# Simulación Rally - lote 20261122-072909-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 65 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1162 ± 4 | 0 ± 0 | 96 % ± 1 | 17.8 ± 0.2 | 7.4 ± 0.3 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 677 ± 7 | 10 ± 1 | 85 % ± 1 | 23.1 ± 0.4 | 3.8 ± 0.1 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 838 ± 8 | 4 ± 1 | 89 % ± 0 | 17.3 ± 0.1 | 5.3 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 4.4 ± 0.8 | 35 ± 7 | 123 ± 14 | 17 ± 4 | 0 % ± 0 | 11.4 ± 0.4 | 3.5 ± 0.3 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 80 ± 10 | 214 ± 6 | 59 ± 6 | 0 % ± 0 | 12.7 ± 0.9 | 10.1 ± 1.9 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 161 ± 3 | 789 ± 10 | 100 ± 5 | 0 % ± 0 | 15.7 ± 1.0 | 12.8 ± 1.1 | 0 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 28 ± 1 | 144 ± 0 | 13 ± 2 | 0 % ± 0 | 8.2 ± 0.2 | 4.9 ± 0.2 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 32 ± 1 | 240 ± 0 | 20 ± 2 | 0 % ± 0 | 7.8 ± 0.3 | 6.1 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 10900: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 1065, FORMATION_HOLD 871, LOW_HEALTH_RETREAT 53, MORALE_SHAKEN 122, MORALE_WAVERING 26, NONE 182, RALLIED 1, REFUSE_ATTACK 1, ROUTED_FLEE 7, SEEK_ENEMY 833, STUCK 14

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
