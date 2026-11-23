# Simulación Rally - lote 20261123-043826-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 67 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1159 ± 6 | 0 ± 0 | 96 % ± 1 | 17.9 ± 0.4 | 7.1 ± 0.4 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 689 ± 6 | 11 ± 2 | 87 % ± 1 | 22.4 ± 0.3 | 3.8 ± 0.1 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 830 ± 10 | 4 ± 1 | 90 % ± 1 | 17.2 ± 0.1 | 5.4 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 5.0 ± 0.7 | 29 ± 5 | 112 ± 12 | 12 ± 2 | 0 % ± 0 | 12.0 ± 0.2 | 3.3 ± 0.1 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 86 ± 7 | 250 ± 11 | 52 ± 5 | 0 % ± 0 | 12.9 ± 0.9 | 9.4 ± 1.2 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 7 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 167 ± 7 | 763 ± 14 | 89 ± 6 | 0 % ± 0 | 15.8 ± 1.5 | 13.9 ± 2.2 | 3 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 28 ± 2 | 144 ± 0 | 12 ± 1 | 0 % ± 0 | 8.4 ± 0.2 | 5.0 ± 0.2 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 34 ± 2 | 240 ± 0 | 19 ± 3 | 0 % ± 0 | 8.1 ± 0.2 | 6.2 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 12400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 63 de 63 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 1143, FORMATION_HOLD 884, LOW_HEALTH_RETREAT 20, MORALE_SHAKEN 139, MORALE_WAVERING 19, NONE 228, RALLIED 1, REFUSE_ATTACK 13, ROUTED_FLEE 3, SEEK_ENEMY 861, STUCK 20

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t19-testudo-mezcla-escudo-testudocarga - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
