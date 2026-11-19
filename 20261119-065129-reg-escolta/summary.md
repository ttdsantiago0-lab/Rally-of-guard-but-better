# Simulación Rally - lote 20261119-065129-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 66 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1176 ± 5 | 0 ± 0 | 97 % ± 0 | 18.0 ± 0.3 | 7.4 ± 0.3 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 681 ± 6 | 9 ± 2 | 84 % ± 1 | 22.2 ± 0.3 | 3.7 ± 0.1 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 63 ± 2 | 824 ± 10 | 5 ± 2 | 90 % ± 1 | 17.1 ± 0.2 | 5.4 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 5.0 ± 1.2 | 40 ± 7 | 110 ± 14 | 14 ± 4 | 0 % ± 0 | 11.9 ± 0.5 | 3.8 ± 0.5 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.3 | 0.0 ± 0.0 | 79 ± 8 | 226 ± 8 | 62 ± 6 | 0 % ± 0 | 11.3 ± 0.4 | 7.8 ± 1.1 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 153 ± 11 | 746 ± 13 | 95 ± 3 | 0 % ± 0 | 15.3 ± 1.2 | 13.7 ± 1.9 | 1 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 2 | 144 ± 0 | 11 ± 1 | 0 % ± 0 | 8.0 ± 0.1 | 4.8 ± 0.1 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 31 ± 3 | 240 ± 0 | 21 ± 2 | 0 % ± 0 | 7.8 ± 0.2 | 6.2 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 8900: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t19-testudo-esqueletos-escudo-testudocarga: enemy:arrow=2
- t19-testudo-mezcla-escudo-testudocarga: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): ENGAGE 1055, FORMATION_HOLD 878, LOW_HEALTH_RETREAT 28, MORALE_SHAKEN 86, MORALE_WAVERING 21, NONE 196, OUT_OF_RANGE 1, RALLIED 1, REFUSE_ATTACK 1, ROUTED_FLEE 2, SEEK_ENEMY 860, STUCK 14

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t19-testudo-mezcla-escudo-testudocarga - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
