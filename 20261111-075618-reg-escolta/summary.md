# Simulación Rally - lote 20261111-075618-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 65 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 56 % ± 6 | 44 % ± 6 | 8.0 ± 0.0 | 4.1 ± 1.0 | 65 ± 0 | 1109 ± 19 | 1 ± 1 | 92 % ± 1 | 15.7 ± 0.1 | 7.2 ± 0.4 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 672 ± 8 | 21 ± 3 | 80 % ± 1 | 19.9 ± 0.4 | 4.1 ± 0.2 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 63 ± 1 | 826 ± 9 | 7 ± 2 | 87 % ± 1 | 15.8 ± 0.2 | 5.6 ± 0.2 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 7.3 ± 0.3 | 24 ± 1 | 87 ± 7 | 11 ± 3 | 0 % ± 0 | 12.4 ± 0.4 | 3.1 ± 0.2 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 88 ± 5 | 224 ± 10 | 50 ± 5 | 0 % ± 0 | 15.6 ± 0.8 | 11.6 ± 1.5 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 169 ± 18 | 788 ± 24 | 106 ± 13 | 0 % ± 0 | 13.4 ± 1.0 | 10.8 ± 1.7 | 0 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 1 | 144 ± 0 | 12 ± 2 | 0 % ± 0 | 8.4 ± 0.1 | 4.5 ± 0.1 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 33 ± 2 | 240 ± 0 | 22 ± 1 | 0 % ± 0 | 8.4 ± 0.4 | 6.4 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 1: sprint → draw (8/2 vivos), normal → draw (8/4 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 1169, FORMATION_HOLD 794, LOW_HEALTH_RETREAT 40, MORALE_SHAKEN 139, MORALE_WAVERING 30, NONE 165, RALLIED 5, REFUSE_ATTACK 9, ROUTED_FLEE 13, SEEK_ENEMY 924, STUCK 4

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
