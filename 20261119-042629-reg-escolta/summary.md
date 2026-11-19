# Simulación Rally - lote 20261119-042629-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 67 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1169 ± 6 | 1 ± 1 | 98 % ± 0 | 17.9 ± 0.2 | 7.4 ± 0.4 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 680 ± 6 | 10 ± 1 | 88 % ± 1 | 22.5 ± 0.2 | 4.1 ± 0.2 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 94 % ± 6 | 6 % ± 6 | 8.0 ± 0.0 | 0.4 ± 0.4 | 59 ± 4 | 778 ± 37 | 6 ± 1 | 95 % ± 1 | 17.9 ± 0.6 | 5.4 ± 0.2 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 6.1 ± 1.0 | 40 ± 7 | 134 ± 15 | 17 ± 4 | 0 % ± 0 | 12.0 ± 0.3 | 3.9 ± 0.3 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 77 ± 6 | 252 ± 17 | 49 ± 5 | 0 % ± 0 | 12.5 ± 0.7 | 9.1 ± 1.0 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 7 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 165 ± 20 | 757 ± 19 | 94 ± 10 | 0 % ± 0 | 15.5 ± 1.2 | 13.0 ± 1.5 | 3 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 1 | 144 ± 0 | 11 ± 1 | 0 % ± 0 | 8.2 ± 0.2 | 5.1 ± 0.3 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 36 ± 2 | 240 ± 0 | 20 ± 2 | 0 % ± 0 | 8.4 ± 0.4 | 6.7 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 8300: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 63 de 63 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 1158, FORMATION_HOLD 869, LOW_HEALTH_RETREAT 24, MORALE_SHAKEN 119, MORALE_WAVERING 8, NONE 208, OUT_OF_RANGE 1, REFUSE_ATTACK 2, SEEK_ENEMY 924, STUCK 10, TOO_CLOSE 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t19-testudo-mezcla-escudo-testudocarga - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
