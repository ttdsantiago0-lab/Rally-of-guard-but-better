# Simulación Rally - lote 20261119-072609-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 67 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1165 ± 7 | 0 ± 0 | 96 % ± 0 | 18.0 ± 0.2 | 6.8 ± 0.1 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 685 ± 8 | 11 ± 2 | 86 % ± 2 | 22.3 ± 0.4 | 3.8 ± 0.2 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 63 ± 2 | 823 ± 10 | 4 ± 1 | 88 % ± 1 | 17.4 ± 0.2 | 5.4 ± 0.2 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 4.5 ± 0.9 | 40 ± 7 | 138 ± 15 | 14 ± 3 | 0 % ± 0 | 11.3 ± 0.4 | 3.7 ± 0.3 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 91 ± 8 | 227 ± 10 | 66 ± 5 | 0 % ± 0 | 15.4 ± 1.7 | 10.8 ± 1.3 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 172 ± 10 | 775 ± 9 | 101 ± 8 | 0 % ± 0 | 16.0 ± 0.9 | 13.2 ± 1.3 | 2 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 28 ± 2 | 144 ± 0 | 13 ± 2 | 0 % ± 0 | 8.1 ± 0.2 | 5.0 ± 0.3 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 35 ± 1 | 240 ± 0 | 21 ± 2 | 0 % ± 0 | 8.0 ± 0.3 | 6.5 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 9100: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t19-testudo-mezcla-escudo-testudocarga: enemy:mob=1

## Motivos de decisión (cambios registrados): ENGAGE 1187, FORMATION_HOLD 893, LOW_HEALTH_RETREAT 54, MORALE_SHAKEN 167, MORALE_WAVERING 33, NONE 225, RALLIED 7, REFUSE_ATTACK 7, ROUTED_FLEE 9, SEEK_ENEMY 919, STUCK 11

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t19-testudo-mezcla-escudo-testudocarga - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
