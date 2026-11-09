# Simulación Rally - lote 20261109-001238-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 65 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 56 % ± 6 | 44 % ± 6 | 8.0 ± 0.0 | 4.3 ± 0.7 | 65 ± 0 | 1098 ± 15 | 1 ± 0 | 91 % ± 1 | 15.2 ± 0.2 | 6.9 ± 0.2 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 94 % ± 6 | 6 % ± 6 | 8.0 ± 0.0 | 0.5 ± 0.5 | 64 ± 1 | 655 ± 8 | 11 ± 1 | 75 % ± 2 | 21.9 ± 0.4 | 4.4 ± 0.3 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 88 % ± 8 | 13 % ± 8 | 8.0 ± 0.0 | 0.8 ± 0.5 | 60 ± 4 | 783 ± 40 | 8 ± 2 | 86 % ± 1 | 16.4 ± 0.4 | 5.7 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 6.8 ± 1.0 | 33 ± 7 | 113 ± 20 | 11 ± 2 | 0 % ± 0 | 12.5 ± 0.4 | 3.3 ± 0.2 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.4 | 0.0 ± 0.0 | 88 ± 10 | 229 ± 9 | 62 ± 8 | 0 % ± 0 | 11.8 ± 0.7 | 9.5 ± 1.3 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 186 ± 15 | 782 ± 16 | 106 ± 12 | 0 % ± 0 | 15.7 ± 1.2 | 13.8 ± 2.2 | 0 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 2 | 144 ± 0 | 15 ± 2 | 0 % ± 0 | 8.0 ± 0.3 | 4.9 ± 0.2 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 34 ± 2 | 240 ± 0 | 20 ± 2 | 0 % ± 0 | 8.2 ± 0.3 | 6.8 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 1: sprint → draw (8/6 vivos), normal → draw (8/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t19-testudo-esqueletos-escudo-testudocarga: enemy:arrow=4
- t19-testudo-mezcla-escudo-testudocarga: enemy:explosion.player=1
- t20-amenaza-blaze-escudo-base-r10: env:onFire=1

## Motivos de decisión (cambios registrados): ENGAGE 1175, FORMATION_HOLD 797, LOW_HEALTH_RETREAT 34, MORALE_SHAKEN 173, MORALE_WAVERING 25, NONE 176, RALLIED 4, REFUSE_ATTACK 5, ROUTED_FLEE 14, SEEK_ENEMY 876, STUCK 12, TOO_CLOSE 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: t20-amenaza-blaze-escudo-base-r10 - 100 % de las bajas azules ({env:onFire=1.0})
