# Simulación Rally - lote 20261124-072340-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 65 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1166 ± 8 | 0 ± 0 | 97 % ± 0 | 17.9 ± 0.1 | 7.4 ± 0.3 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 685 ± 6 | 10 ± 1 | 85 % ± 1 | 22.6 ± 0.2 | 3.9 ± 0.1 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 833 ± 11 | 2 ± 1 | 90 % ± 1 | 17.2 ± 0.1 | 5.2 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 6.1 ± 0.5 | 26 ± 1 | 100 ± 6 | 12 ± 2 | 0 % ± 0 | 12.0 ± 0.5 | 3.1 ± 0.2 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 88 ± 10 | 223 ± 9 | 60 ± 8 | 0 % ± 0 | 13.5 ± 0.7 | 12.0 ± 1.9 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 174 ± 16 | 761 ± 10 | 83 ± 10 | 0 % ± 0 | 17.2 ± 2.1 | 14.8 ± 2.3 | 0 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 1 | 144 ± 0 | 12 ± 2 | 0 % ± 0 | 8.1 ± 0.2 | 4.6 ± 0.2 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 36 ± 2 | 240 ± 0 | 17 ± 2 | 0 % ± 0 | 8.5 ± 0.4 | 6.5 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 15600: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t19-testudo-esqueletos-escudo-testudocarga: enemy:arrow=1

## Motivos de decisión (cambios registrados): ENGAGE 1066, FORMATION_HOLD 885, LOW_HEALTH_RETREAT 45, MORALE_SHAKEN 126, MORALE_WAVERING 37, NONE 191, RALLIED 5, ROUTED_FLEE 11, SEEK_ENEMY 754, STUCK 9

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
