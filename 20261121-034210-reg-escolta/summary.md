# Simulación Rally - lote 20261121-034210-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 66 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1152 ± 4 | 0 ± 0 | 97 % ± 0 | 17.7 ± 0.2 | 7.1 ± 0.3 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 668 ± 8 | 6 ± 2 | 86 % ± 1 | 22.9 ± 0.3 | 3.7 ± 0.1 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 94 % ± 6 | 6 % ± 6 | 8.0 ± 0.0 | 0.4 ± 0.4 | 59 ± 4 | 806 ± 39 | 5 ± 1 | 91 % ± 1 | 17.5 ± 0.3 | 5.4 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 6.1 ± 0.6 | 23 ± 1 | 86 ± 7 | 8 ± 1 | 0 % ± 0 | 12.7 ± 0.4 | 3.0 ± 0.3 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 94 % ± 6 | 6 % ± 6 | 7.8 ± 0.3 | 0.4 ± 0.4 | 90 ± 17 | 251 ± 14 | 70 ± 10 | 0 % ± 0 | 11.7 ± 0.9 | 8.6 ± 2.0 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 7 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 184 ± 10 | 749 ± 25 | 110 ± 11 | 0 % ± 0 | 18.4 ± 1.4 | 15.1 ± 1.4 | 2 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 2 | 144 ± 0 | 12 ± 1 | 0 % ± 0 | 7.8 ± 0.1 | 4.7 ± 0.2 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 34 ± 2 | 240 ± 0 | 25 ± 2 | 0 % ± 0 | 7.8 ± 0.2 | 6.4 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 10400: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 63 de 63 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t19-testudo-esqueletos-escudo-testudocarga: enemy:arrow=2
- t19-testudo-mezcla-escudo-testudocarga: enemy:explosion.player=1
- t20-amenaza-blaze-escudo-base-r10: enemy:mob=1 env:onFire=1

## Motivos de decisión (cambios registrados): ENGAGE 1127, FORMATION_HOLD 857, LOW_HEALTH_RETREAT 53, MORALE_SHAKEN 188, MORALE_WAVERING 34, NONE 181, RALLIED 5, REFUSE_ATTACK 4, ROUTED_FLEE 16, SEEK_ENEMY 821, STUCK 6, TOO_CLOSE 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t19-testudo-mezcla-escudo-testudocarga - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t20-amenaza-blaze-escudo-base-r10 - 50 % de las bajas azules ({enemy:mob=1.0, env:onFire=1.0})
