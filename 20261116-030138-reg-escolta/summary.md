# Simulación Rally - lote 20261116-030138-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 65 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1162 ± 5 | 0 ± 0 | 98 % ± 0 | 17.5 ± 0.2 | 7.0 ± 0.3 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 680 ± 6 | 12 ± 3 | 93 % ± 1 | 22.7 ± 0.2 | 3.9 ± 0.1 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 94 % ± 6 | 6 % ± 6 | 8.0 ± 0.0 | 0.1 ± 0.1 | 62 ± 2 | 826 ± 11 | 5 ± 1 | 96 % ± 1 | 17.2 ± 0.1 | 5.4 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 6.0 ± 0.9 | 31 ± 5 | 92 ± 14 | 17 ± 5 | 0 % ± 0 | 11.8 ± 0.5 | 3.4 ± 0.2 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 90 ± 10 | 223 ± 11 | 58 ± 4 | 0 % ± 0 | 13.8 ± 1.2 | 11.2 ± 1.5 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 166 ± 12 | 753 ± 13 | 106 ± 11 | 0 % ± 0 | 15.3 ± 1.0 | 10.8 ± 1.2 | 0 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 24 ± 1 | 144 ± 0 | 10 ± 2 | 0 % ± 0 | 8.1 ± 0.2 | 4.9 ± 0.2 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 35 ± 1 | 240 ± 0 | 17 ± 2 | 0 % ± 0 | 7.8 ± 0.1 | 5.9 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 5601: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t19-testudo-mezcla-escudo-testudocarga: enemy:explosion.player=2

## Motivos de decisión (cambios registrados): ENGAGE 1120, FORMATION_HOLD 853, LOW_HEALTH_RETREAT 42, MORALE_SHAKEN 192, MORALE_WAVERING 26, NONE 164, RALLIED 1, ROUTED_FLEE 6, SEEK_ENEMY 867, STUCK 13, TOO_CLOSE 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
