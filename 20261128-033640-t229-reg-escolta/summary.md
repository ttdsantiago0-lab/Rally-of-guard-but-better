# Simulación Rally - lote 20261128-033640-t229-reg-escolta

Preset `t229-reg-escolta` · velocidad `sprint` · 67 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t229-sync-t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1165 ± 7 | 0 ± 0 | 96 % ± 0 | 17.6 ± 0.3 | 7.4 ± 0.3 | 0 |
| t229-sync-t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 678 ± 7 | 10 ± 2 | 86 % ± 2 | 22.8 ± 0.4 | 3.7 ± 0.1 | 0 |
| t229-sync-t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 63 ± 1 | 829 ± 8 | 4 ± 1 | 89 % ± 1 | 17.3 ± 0.1 | 5.3 ± 0.2 | 0 |
| t229-sync-t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 6.3 ± 1.0 | 34 ± 7 | 118 ± 20 | 15 ± 5 | 0 % ± 0 | 12.0 ± 0.5 | 3.1 ± 0.3 | 0 |
| t229-sync-t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 80 ± 5 | 255 ± 13 | 50 ± 3 | 0 % ± 0 | 13.0 ± 1.5 | 10.0 ± 2.1 | 0 |
| t229-sync-t19-testudo-mezcla-escudo-testudocarga | 7 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 178 ± 13 | 783 ± 17 | 93 ± 10 | 0 % ± 0 | 17.1 ± 1.8 | 12.6 ± 1.6 | 3 |
| t229-sync-t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 2 | 144 ± 0 | 11 ± 2 | 0 % ± 0 | 8.2 ± 0.4 | 4.7 ± 0.2 | 0 |
| t229-sync-t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 31 ± 2 | 240 ± 0 | 14 ± 2 | 0 % ± 0 | 7.9 ± 0.3 | 6.4 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t229-sync-t17-escolta-zombis-linea-defender semilla 21800: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 63 de 63 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 1155, FORMATION_HOLD 888, LOW_HEALTH_RETREAT 13, MORALE_SHAKEN 147, MORALE_WAVERING 8, NONE 240, OUT_OF_RANGE 2, RALLIED 1, ROUTED_FLEE 1, SEEK_ENEMY 880, STUCK 17

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t229-sync-t19-testudo-mezcla-escudo-testudocarga - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
