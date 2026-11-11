# Simulación Rally - lote 20261111-223829-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 67 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1160 ± 5 | 0 ± 0 | 97 % ± 0 | 17.8 ± 0.2 | 7.0 ± 0.2 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 682 ± 10 | 9 ± 1 | 90 % ± 1 | 23.3 ± 0.4 | 3.8 ± 0.2 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 845 ± 6 | 5 ± 1 | 95 % ± 1 | 17.1 ± 0.2 | 5.3 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 56 % ± 6 | 44 % ± 6 | 8.0 ± 0.0 | 4.6 ± 1.3 | 42 ± 7 | 131 ± 14 | 15 ± 3 | 0 % ± 0 | 12.3 ± 0.6 | 3.6 ± 0.2 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 94 % ± 6 | 6 % ± 6 | 7.9 ± 0.1 | 0.1 ± 0.1 | 124 ± 12 | 242 ± 15 | 71 ± 9 | 0 % ± 0 | 19.6 ± 2.4 | 13.5 ± 1.4 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 157 ± 9 | 764 ± 8 | 86 ± 8 | 0 % ± 0 | 15.7 ± 1.2 | 11.4 ± 0.8 | 2 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 28 ± 2 | 144 ± 0 | 10 ± 2 | 0 % ± 0 | 8.2 ± 0.2 | 4.6 ± 0.1 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 34 ± 3 | 240 ± 0 | 21 ± 4 | 0 % ± 0 | 7.8 ± 0.3 | 6.0 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t19-testudo-esqueletos-escudo-testudocarga: enemy:arrow=1
- t20-amenaza-blaze-escudo-base-r10: env:onFire=1

## Motivos de decisión (cambios registrados): ENGAGE 1209, FORMATION_HOLD 902, LOW_HEALTH_RETREAT 63, MORALE_SHAKEN 166, MORALE_WAVERING 39, NONE 218, RALLIED 1, REFUSE_ATTACK 1, ROUTED_FLEE 22, SEEK_ENEMY 976, STUCK 16

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t19-testudo-mezcla-escudo-testudocarga - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t20-amenaza-blaze-escudo-base-r10 - 100 % de las bajas azules ({env:onFire=1.0})
