# Simulación Rally - lote 20261125-113910-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 67 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1152 ± 5 | 0 ± 0 | 96 % ± 1 | 17.8 ± 0.2 | 6.9 ± 0.2 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 94 % ± 6 | 6 % ± 6 | 8.0 ± 0.0 | 0.4 ± 0.4 | 61 ± 4 | 645 ± 36 | 9 ± 1 | 85 % ± 1 | 22.0 ± 0.2 | 3.7 ± 0.2 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 63 ± 2 | 833 ± 11 | 3 ± 1 | 90 % ± 0 | 17.1 ± 0.2 | 5.4 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 4.6 ± 0.7 | 34 ± 5 | 136 ± 15 | 16 ± 4 | 0 % ± 0 | 11.0 ± 0.3 | 3.3 ± 0.3 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 79 ± 5 | 237 ± 16 | 74 ± 6 | 0 % ± 0 | 11.8 ± 0.8 | 9.8 ± 2.0 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 162 ± 10 | 747 ± 12 | 95 ± 5 | 0 % ± 0 | 14.9 ± 1.3 | 12.6 ± 1.6 | 2 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 1 | 144 ± 0 | 11 ± 1 | 0 % ± 0 | 8.1 ± 0.2 | 4.8 ± 0.2 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 34 ± 1 | 240 ± 0 | 20 ± 2 | 0 % ± 0 | 8.0 ± 0.3 | 6.1 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 18400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t19-testudo-esqueletos-escudo-testudocarga: enemy:arrow=1

## Motivos de decisión (cambios registrados): ENGAGE 1262, FORMATION_HOLD 874, LOW_HEALTH_RETREAT 62, MORALE_SHAKEN 254, MORALE_WAVERING 48, NONE 230, RALLIED 1, REFUSE_ATTACK 12, ROUTED_FLEE 9, SEEK_ENEMY 907, STUCK 11

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t19-testudo-mezcla-escudo-testudocarga - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
