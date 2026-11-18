# Simulación Rally - lote 20261118-052744-t76-reg-escolta

Preset `t76-reg-escolta` · velocidad `sprint` · 67 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t76-spread-t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1159 ± 3 | 0 ± 0 | 95 % ± 1 | 17.9 ± 0.2 | 7.3 ± 0.2 | 0 |
| t76-spread-t17-escolta-esqueletos-linea-defender | 8 | 94 % ± 6 | 6 % ± 6 | 8.0 ± 0.0 | 0.3 ± 0.3 | 61 ± 4 | 644 ± 30 | 7 ± 1 | 85 % ± 1 | 23.0 ± 0.4 | 3.8 ± 0.1 | 0 |
| t76-spread-t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 62 ± 2 | 833 ± 8 | 5 ± 1 | 91 % ± 1 | 17.4 ± 0.2 | 5.5 ± 0.1 | 0 |
| t76-spread-t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 6.9 ± 0.7 | 30 ± 5 | 115 ± 15 | 16 ± 2 | 0 % ± 0 | 12.1 ± 0.5 | 3.3 ± 0.2 | 0 |
| t76-spread-t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 98 ± 10 | 255 ± 11 | 54 ± 2 | 0 % ± 0 | 17.0 ± 2.5 | 12.2 ± 1.9 | 0 |
| t76-spread-t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 155 ± 8 | 755 ± 14 | 95 ± 7 | 0 % ± 0 | 14.7 ± 0.5 | 9.7 ± 1.2 | 2 |
| t76-spread-t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 1 | 144 ± 0 | 8 ± 1 | 0 % ± 0 | 8.2 ± 0.3 | 5.3 ± 0.2 | 0 |
| t76-spread-t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 31 ± 2 | 240 ± 0 | 22 ± 2 | 0 % ± 0 | 7.6 ± 0.2 | 6.1 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t76-spread-t17-escolta-zombis-linea-defender semilla 7500: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t76-spread-t19-testudo-mezcla-escudo-testudocarga: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): ENGAGE 1169, FORMATION_HOLD 860, LOW_HEALTH_RETREAT 42, MORALE_SHAKEN 180, MORALE_WAVERING 45, NONE 198, RALLIED 5, REFUSE_ATTACK 2, ROUTED_FLEE 18, SEEK_ENEMY 836, STUCK 7

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t76-spread-t19-testudo-mezcla-escudo-testudocarga - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
