# Simulación Rally - lote 20261109-212018-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 66 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 56 % ± 6 | 44 % ± 6 | 8.0 ± 0.0 | 3.6 ± 0.9 | 65 ± 0 | 1119 ± 21 | 2 ± 1 | 91 % ± 1 | 15.4 ± 0.3 | 7.4 ± 0.3 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 94 % ± 6 | 6 % ± 6 | 8.0 ± 0.0 | 0.1 ± 0.1 | 59 ± 5 | 599 ± 57 | 11 ± 2 | 77 % ± 2 | 21.1 ± 0.5 | 4.5 ± 0.2 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 94 % ± 6 | 6 % ± 6 | 8.0 ± 0.0 | 0.5 ± 0.5 | 61 ± 4 | 795 ± 40 | 9 ± 2 | 86 % ± 1 | 16.1 ± 0.3 | 5.7 ± 0.2 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 6.0 ± 0.7 | 25 ± 1 | 102 ± 15 | 10 ± 2 | 0 % ± 0 | 11.9 ± 0.6 | 3.6 ± 0.3 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 82 ± 9 | 228 ± 13 | 58 ± 4 | 0 % ± 0 | 12.4 ± 1.1 | 7.5 ± 1.4 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 7 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 187 ± 12 | 768 ± 16 | 99 ± 8 | 0 % ± 0 | 16.0 ± 1.6 | 11.9 ± 1.2 | 2 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 2 | 144 ± 0 | 10 ± 2 | 0 % ± 0 | 8.1 ± 0.2 | 4.7 ± 0.4 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 34 ± 1 | 240 ± 0 | 18 ± 3 | 0 % ± 0 | 7.7 ± 0.2 | 6.2 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 1: sprint → draw (8/5 vivos), normal → draw (8/5 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 63 de 63 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t19-testudo-mezcla-escudo-testudocarga: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): ENGAGE 1146, FORMATION_HOLD 779, LOW_HEALTH_RETREAT 34, MORALE_SHAKEN 122, MORALE_WAVERING 33, NONE 182, OUT_OF_RANGE 4, RALLIED 3, REFUSE_ATTACK 7, ROUTED_FLEE 8, SEEK_ENEMY 876, STUCK 15, TOO_CLOSE 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t19-testudo-mezcla-escudo-testudocarga - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
