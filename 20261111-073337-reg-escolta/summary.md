# Simulación Rally - lote 20261111-073337-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 66 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 56 % ± 6 | 44 % ± 6 | 8.0 ± 0.0 | 2.8 ± 0.6 | 65 ± 0 | 1142 ± 9 | 1 ± 0 | 92 % ± 1 | 15.7 ± 0.2 | 6.7 ± 0.2 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 88 % ± 8 | 13 % ± 8 | 8.0 ± 0.0 | 1.4 ± 1.0 | 57 ± 5 | 587 ± 46 | 13 ± 2 | 78 % ± 1 | 20.6 ± 0.3 | 4.1 ± 0.2 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 94 % ± 6 | 6 % ± 6 | 8.0 ± 0.0 | 0.4 ± 0.4 | 61 ± 4 | 804 ± 42 | 5 ± 1 | 87 % ± 1 | 16.1 ± 0.2 | 5.8 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 4.3 ± 0.9 | 46 ± 7 | 130 ± 11 | 26 ± 6 | 0 % ± 0 | 11.2 ± 0.4 | 3.7 ± 0.2 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 94 % ± 6 | 6 % ± 6 | 7.9 ± 0.1 | 0.1 ± 0.1 | 104 ± 14 | 233 ± 12 | 61 ± 5 | 0 % ± 0 | 13.7 ± 1.1 | 13.3 ± 2.1 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 183 ± 16 | 768 ± 24 | 104 ± 13 | 0 % ± 0 | 16.0 ± 1.1 | 14.5 ± 1.8 | 1 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 1 | 144 ± 0 | 10 ± 1 | 0 % ± 0 | 7.9 ± 0.2 | 5.1 ± 0.4 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 36 ± 3 | 240 ± 0 | 19 ± 3 | 0 % ± 0 | 7.6 ± 0.3 | 6.3 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 1: sprint → draw (8/3 vivos), normal → draw (8/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t19-testudo-esqueletos-escudo-testudocarga: enemy:arrow=1

## Motivos de decisión (cambios registrados): ENGAGE 1405, FORMATION_HOLD 757, LOW_HEALTH_RETREAT 52, MORALE_SHAKEN 368, MORALE_WAVERING 48, NONE 197, OUT_OF_RANGE 1, RALLIED 3, REFUSE_ATTACK 1, ROUTED_FLEE 7, SEEK_ENEMY 929, STUCK 17, TOO_CLOSE 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t19-testudo-mezcla-escudo-testudocarga - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
