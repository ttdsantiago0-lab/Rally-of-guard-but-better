# Simulación Rally - lote 20261125-204308-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 66 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1159 ± 6 | 0 ± 0 | 96 % ± 1 | 17.7 ± 0.2 | 6.8 ± 0.3 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 678 ± 5 | 10 ± 1 | 84 % ± 1 | 22.4 ± 0.3 | 3.9 ± 0.2 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 63 ± 2 | 846 ± 8 | 5 ± 1 | 90 % ± 1 | 17.2 ± 0.2 | 5.4 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 4.8 ± 0.7 | 31 ± 5 | 113 ± 9 | 11 ± 4 | 0 % ± 0 | 11.5 ± 0.3 | 3.3 ± 0.1 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 86 ± 11 | 226 ± 5 | 53 ± 4 | 0 % ± 0 | 15.1 ± 1.5 | 10.6 ± 1.9 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 177 ± 8 | 779 ± 10 | 99 ± 6 | 0 % ± 0 | 15.6 ± 1.5 | 12.1 ± 1.4 | 1 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 2 | 144 ± 0 | 9 ± 2 | 0 % ± 0 | 8.1 ± 0.2 | 5.0 ± 0.2 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.3 | 0.0 ± 0.0 | 34 ± 2 | 240 ± 0 | 23 ± 3 | 0 % ± 0 | 7.6 ± 0.2 | 6.0 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 17400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t19-testudo-mezcla-escudo-testudocarga: enemy:explosion.player=1
- t20-amenaza-blaze-escudo-base-r10: env:onFire=2

## Motivos de decisión (cambios registrados): ENGAGE 1083, FORMATION_HOLD 876, LOW_HEALTH_RETREAT 21, MORALE_SHAKEN 101, MORALE_WAVERING 12, NONE 197, OUT_OF_RANGE 2, REFUSE_ATTACK 5, ROUTED_FLEE 1, SEEK_ENEMY 856, STUCK 14

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t19-testudo-mezcla-escudo-testudocarga - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t20-amenaza-blaze-escudo-base-r10 - 100 % de las bajas azules ({env:onFire=2.0})
