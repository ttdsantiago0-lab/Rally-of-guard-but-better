# Simulación Rally - lote 20261119-045843-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 65 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1166 ± 5 | 0 ± 0 | 95 % ± 1 | 17.9 ± 0.2 | 7.2 ± 0.3 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 678 ± 5 | 11 ± 2 | 85 % ± 2 | 22.3 ± 0.2 | 3.8 ± 0.1 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 829 ± 10 | 3 ± 1 | 90 % ± 1 | 17.2 ± 0.2 | 5.4 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 5.4 ± 0.9 | 32 ± 6 | 115 ± 16 | 16 ± 3 | 0 % ± 0 | 11.5 ± 0.5 | 3.5 ± 0.3 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.3 | 0.0 ± 0.0 | 98 ± 11 | 234 ± 12 | 64 ± 11 | 0 % ± 0 | 12.4 ± 1.0 | 12.0 ± 2.1 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 178 ± 9 | 796 ± 6 | 106 ± 5 | 0 % ± 0 | 14.1 ± 0.8 | 11.8 ± 0.9 | 0 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 27 ± 2 | 144 ± 0 | 8 ± 2 | 0 % ± 0 | 7.8 ± 0.2 | 5.0 ± 0.2 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 36 ± 2 | 240 ± 0 | 23 ± 3 | 0 % ± 0 | 8.3 ± 0.5 | 6.3 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 8600: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t19-testudo-esqueletos-escudo-testudocarga: enemy:arrow=3

## Motivos de decisión (cambios registrados): ENGAGE 1135, FORMATION_HOLD 866, LOW_HEALTH_RETREAT 40, MORALE_SHAKEN 177, MORALE_WAVERING 34, NONE 165, RALLIED 2, ROUTED_FLEE 9, SEEK_ENEMY 877, STUCK 16, TOO_CLOSE 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
