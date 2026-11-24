# Simulación Rally - lote 20261124-034419-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 67 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1168 ± 4 | 0 ± 0 | 96 % ± 0 | 17.6 ± 0.2 | 7.1 ± 0.2 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 682 ± 7 | 10 ± 2 | 85 % ± 1 | 22.4 ± 0.3 | 3.8 ± 0.2 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 836 ± 8 | 3 ± 1 | 91 % ± 1 | 17.3 ± 0.1 | 5.2 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 5.8 ± 0.8 | 30 ± 5 | 98 ± 9 | 13 ± 2 | 0 % ± 0 | 11.4 ± 0.3 | 3.4 ± 0.2 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 93 ± 7 | 237 ± 12 | 70 ± 9 | 0 % ± 0 | 13.7 ± 1.1 | 11.1 ± 1.1 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 7 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 182 ± 16 | 764 ± 9 | 83 ± 5 | 0 % ± 0 | 16.9 ± 1.2 | 14.8 ± 1.7 | 3 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 1 | 144 ± 0 | 10 ± 1 | 0 % ± 0 | 8.1 ± 0.1 | 4.5 ± 0.2 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 33 ± 1 | 240 ± 0 | 18 ± 3 | 0 % ± 0 | 7.9 ± 0.2 | 6.2 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 15400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 63 de 63 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 1168, FORMATION_HOLD 907, LOW_HEALTH_RETREAT 34, MORALE_SHAKEN 146, MORALE_WAVERING 21, NONE 224, OUT_OF_RANGE 1, RALLIED 2, REFUSE_ATTACK 2, ROUTED_FLEE 10, SEEK_ENEMY 897, STUCK 14

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t19-testudo-mezcla-escudo-testudocarga - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
