# Simulación Rally - lote 20261124-055702-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 65 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1166 ± 5 | 0 ± 0 | 95 % ± 1 | 17.9 ± 0.1 | 7.1 ± 0.3 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 679 ± 11 | 12 ± 2 | 84 % ± 2 | 22.5 ± 0.3 | 3.8 ± 0.1 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 845 ± 7 | 4 ± 1 | 89 % ± 1 | 17.1 ± 0.2 | 5.2 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 5.9 ± 1.2 | 39 ± 7 | 123 ± 20 | 19 ± 4 | 0 % ± 0 | 12.0 ± 0.5 | 3.5 ± 0.1 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 96 ± 8 | 227 ± 9 | 65 ± 8 | 0 % ± 0 | 15.0 ± 1.6 | 12.3 ± 1.8 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 155 ± 5 | 779 ± 10 | 98 ± 8 | 0 % ± 0 | 14.1 ± 0.4 | 13.1 ± 1.3 | 0 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 2 | 144 ± 0 | 11 ± 2 | 0 % ± 0 | 8.0 ± 0.2 | 4.7 ± 0.3 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 32 ± 2 | 240 ± 0 | 23 ± 2 | 0 % ± 0 | 7.8 ± 0.2 | 6.8 ± 0.3 | 0 |

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

## Motivos de decisión (cambios registrados): ENGAGE 1143, FORMATION_HOLD 895, LOW_HEALTH_RETREAT 49, MORALE_SHAKEN 184, MORALE_WAVERING 20, NONE 180, RALLIED 4, REFUSE_ATTACK 3, ROUTED_FLEE 10, SEEK_ENEMY 862, STUCK 12, TOO_CLOSE 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
