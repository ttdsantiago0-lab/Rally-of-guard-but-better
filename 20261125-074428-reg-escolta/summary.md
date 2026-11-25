# Simulación Rally - lote 20261125-074428-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 65 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1163 ± 7 | 0 ± 0 | 96 % ± 0 | 17.4 ± 0.2 | 7.1 ± 0.2 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 676 ± 7 | 10 ± 1 | 86 % ± 1 | 22.5 ± 0.3 | 3.6 ± 0.1 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 837 ± 10 | 3 ± 1 | 91 % ± 1 | 17.0 ± 0.1 | 5.2 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 5.6 ± 0.7 | 36 ± 6 | 127 ± 22 | 20 ± 4 | 0 % ± 0 | 11.6 ± 0.4 | 3.5 ± 0.3 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 108 ± 14 | 242 ± 9 | 62 ± 8 | 0 % ± 0 | 16.1 ± 1.8 | 13.6 ± 1.8 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.2 | 0.0 ± 0.0 | 195 ± 19 | 767 ± 13 | 131 ± 19 | 0 % ± 0 | 16.5 ± 1.9 | 14.2 ± 1.7 | 0 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 24 ± 1 | 144 ± 0 | 10 ± 2 | 0 % ± 0 | 8.1 ± 0.2 | 4.6 ± 0.2 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 33 ± 2 | 240 ± 0 | 20 ± 3 | 0 % ± 0 | 8.1 ± 0.2 | 6.2 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 17600: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t19-testudo-esqueletos-escudo-testudocarga: enemy:arrow=2
- t19-testudo-mezcla-escudo-testudocarga: enemy:explosion.player=3

## Motivos de decisión (cambios registrados): ENGAGE 1290, FORMATION_HOLD 882, LOW_HEALTH_RETREAT 29, MORALE_SHAKEN 352, MORALE_WAVERING 40, NONE 172, RALLIED 8, REFUSE_ATTACK 2, ROUTED_FLEE 22, SEEK_ENEMY 870, STUCK 11

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
