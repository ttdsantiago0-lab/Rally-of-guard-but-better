# Simulación Rally - lote 20261115-025641-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 66 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1168 ± 5 | 0 ± 0 | 98 % ± 0 | 17.8 ± 0.3 | 7.6 ± 0.4 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 683 ± 7 | 10 ± 2 | 90 % ± 1 | 22.9 ± 0.5 | 3.5 ± 0.1 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 94 % ± 6 | 6 % ± 6 | 8.0 ± 0.0 | 0.3 ± 0.3 | 63 ± 2 | 821 ± 12 | 4 ± 1 | 96 % ± 1 | 17.3 ± 0.2 | 5.2 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 56 % ± 6 | 44 % ± 6 | 8.0 ± 0.0 | 5.6 ± 1.0 | 35 ± 7 | 120 ± 24 | 17 ± 4 | 0 % ± 0 | 11.6 ± 0.4 | 3.5 ± 0.2 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 87 ± 9 | 227 ± 10 | 52 ± 2 | 0 % ± 0 | 16.8 ± 1.3 | 10.7 ± 1.3 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 94 % ± 6 | 6 % ± 6 | 7.8 ± 0.3 | 0.6 ± 0.6 | 185 ± 27 | 747 ± 21 | 113 ± 12 | 0 % ± 0 | 15.8 ± 1.3 | 14.9 ± 2.1 | 1 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 27 ± 2 | 144 ± 0 | 13 ± 2 | 0 % ± 0 | 8.0 ± 0.2 | 4.8 ± 0.3 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 30 ± 2 | 240 ± 0 | 16 ± 2 | 0 % ± 0 | 7.6 ± 0.2 | 6.3 ± 0.2 | 0 |

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

## Motivos de decisión (cambios registrados): ENGAGE 1244, FORMATION_HOLD 888, LOW_HEALTH_RETREAT 61, MORALE_SHAKEN 254, MORALE_WAVERING 56, NONE 184, OUT_OF_RANGE 2, RALLIED 4, REFUSE_ATTACK 6, ROUTED_FLEE 19, SEEK_ENEMY 861, STUCK 7, TOO_CLOSE 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t19-testudo-mezcla-escudo-testudocarga - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
