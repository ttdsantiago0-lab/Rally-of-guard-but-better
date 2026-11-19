# Simulación Rally - lote 20261119-042755-t76-reg-escolta

Preset `t76-reg-escolta` · velocidad `sprint` · 65 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t76-spread-t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1175 ± 5 | 0 ± 0 | 94 % ± 0 | 17.6 ± 0.3 | 7.4 ± 0.4 | 0 |
| t76-spread-t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 671 ± 10 | 7 ± 1 | 84 % ± 1 | 22.5 ± 0.4 | 3.9 ± 0.1 | 0 |
| t76-spread-t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 63 ± 1 | 847 ± 6 | 4 ± 1 | 91 % ± 1 | 17.2 ± 0.1 | 5.4 ± 0.2 | 0 |
| t76-spread-t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 5.6 ± 0.7 | 30 ± 5 | 117 ± 10 | 12 ± 1 | 0 % ± 0 | 12.0 ± 0.1 | 3.2 ± 0.2 | 0 |
| t76-spread-t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 81 ± 7 | 242 ± 11 | 53 ± 6 | 0 % ± 0 | 15.3 ± 1.7 | 11.2 ± 2.0 | 0 |
| t76-spread-t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.2 | 0.0 ± 0.0 | 181 ± 19 | 755 ± 17 | 106 ± 15 | 0 % ± 0 | 15.4 ± 1.3 | 13.4 ± 1.7 | 0 |
| t76-spread-t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 27 ± 1 | 144 ± 0 | 11 ± 1 | 0 % ± 0 | 7.7 ± 0.3 | 4.1 ± 0.4 | 0 |
| t76-spread-t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 35 ± 2 | 240 ± 0 | 24 ± 2 | 0 % ± 0 | 7.8 ± 0.3 | 6.4 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t76-spread-t17-escolta-zombis-linea-defender semilla 8300: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t76-spread-t19-testudo-mezcla-escudo-testudocarga: enemy:explosion.player=2
- t76-spread-t20-amenaza-blaze-escudo-base-r10: env:onFire=1

## Motivos de decisión (cambios registrados): ENGAGE 1086, FORMATION_HOLD 856, LOW_HEALTH_RETREAT 27, MORALE_SHAKEN 127, MORALE_WAVERING 31, NONE 146, OUT_OF_RANGE 2, RALLIED 3, REFUSE_ATTACK 17, ROUTED_FLEE 12, SEEK_ENEMY 818, STUCK 7

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- MUERTES QUE NO SON DEL ENEMIGO: t76-spread-t20-amenaza-blaze-escudo-base-r10 - 100 % de las bajas azules ({env:onFire=1.0})
