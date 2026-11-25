# Simulación Rally - lote 20261125-123036-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 67 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1164 ± 5 | 0 ± 0 | 96 % ± 1 | 18.0 ± 0.2 | 7.1 ± 0.4 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 668 ± 6 | 12 ± 1 | 85 % ± 1 | 23.1 ± 0.5 | 3.8 ± 0.1 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 862 ± 5 | 5 ± 1 | 91 % ± 1 | 17.3 ± 0.2 | 5.2 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 5.1 ± 1.0 | 36 ± 6 | 121 ± 11 | 14 ± 3 | 0 % ± 0 | 11.3 ± 0.5 | 3.7 ± 0.3 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 88 ± 7 | 242 ± 16 | 54 ± 4 | 0 % ± 0 | 14.1 ± 1.3 | 10.1 ± 1.5 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 184 ± 8 | 764 ± 16 | 94 ± 9 | 0 % ± 0 | 16.9 ± 1.9 | 13.4 ± 1.1 | 2 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 2 | 144 ± 0 | 11 ± 1 | 0 % ± 0 | 8.4 ± 0.2 | 4.8 ± 0.2 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 34 ± 2 | 240 ± 0 | 14 ± 1 | 0 % ± 0 | 8.7 ± 0.5 | 6.5 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 1131, FORMATION_HOLD 895, LOW_HEALTH_RETREAT 21, MORALE_SHAKEN 73, MORALE_WAVERING 9, NONE 236, OUT_OF_RANGE 1, RALLIED 2, REFUSE_ATTACK 3, ROUTED_FLEE 3, SEEK_ENEMY 946, STUCK 18

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t19-testudo-mezcla-escudo-testudocarga - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
