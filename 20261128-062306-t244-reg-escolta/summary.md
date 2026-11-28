# Simulación Rally - lote 20261128-062306-t244-reg-escolta

Preset `t244-reg-escolta` · velocidad `sprint` · 66 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t244-sync-t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1160 ± 5 | 0 ± 0 | 97 % ± 0 | 17.7 ± 0.2 | 7.0 ± 0.3 | 0 |
| t244-sync-t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 676 ± 11 | 9 ± 1 | 84 % ± 2 | 23.2 ± 0.4 | 3.6 ± 0.1 | 0 |
| t244-sync-t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 62 ± 2 | 824 ± 13 | 5 ± 1 | 90 % ± 1 | 17.4 ± 0.2 | 5.4 ± 0.1 | 0 |
| t244-sync-t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 4.9 ± 0.9 | 34 ± 7 | 117 ± 11 | 18 ± 8 | 0 % ± 0 | 12.0 ± 0.5 | 3.4 ± 0.3 | 0 |
| t244-sync-t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.3 | 0.0 ± 0.0 | 85 ± 9 | 248 ± 10 | 62 ± 10 | 0 % ± 0 | 13.9 ± 1.0 | 11.0 ± 1.9 | 0 |
| t244-sync-t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 168 ± 12 | 771 ± 15 | 90 ± 7 | 0 % ± 0 | 16.9 ± 1.5 | 15.9 ± 2.2 | 1 |
| t244-sync-t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 24 ± 1 | 144 ± 0 | 11 ± 1 | 0 % ± 0 | 8.3 ± 0.1 | 5.0 ± 0.1 | 0 |
| t244-sync-t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 35 ± 2 | 240 ± 0 | 15 ± 2 | 0 % ± 0 | 8.7 ± 0.6 | 6.3 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t244-sync-t17-escolta-zombis-linea-defender semilla 24400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t244-sync-t19-testudo-esqueletos-escudo-testudocarga: enemy:arrow=2

## Motivos de decisión (cambios registrados): ENGAGE 1122, FORMATION_HOLD 851, LOW_HEALTH_RETREAT 22, MORALE_SHAKEN 174, MORALE_WAVERING 36, NONE 193, RALLIED 2, REFUSE_ATTACK 1, ROUTED_FLEE 6, SEEK_ENEMY 805, STUCK 8

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t244-sync-t19-testudo-mezcla-escudo-testudocarga - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
