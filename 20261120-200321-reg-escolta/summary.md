# Simulación Rally - lote 20261120-200321-reg-escolta

Preset `reg-escolta` · velocidad `sprint` · 67 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1177 ± 2 | 0 ± 0 | 95 % ± 0 | 17.5 ± 0.1 | 7.0 ± 0.2 | 0 |
| t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 684 ± 8 | 10 ± 2 | 83 % ± 0 | 22.2 ± 0.2 | 3.8 ± 0.2 | 0 |
| t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 831 ± 11 | 5 ± 1 | 90 % ± 1 | 17.4 ± 0.1 | 5.3 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 8 | 56 % ± 6 | 44 % ± 6 | 8.0 ± 0.0 | 4.9 ± 1.3 | 36 ± 7 | 127 ± 18 | 16 ± 3 | 0 % ± 0 | 12.0 ± 0.7 | 3.4 ± 0.2 | 0 |
| t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.3 | 0.0 ± 0.0 | 97 ± 12 | 254 ± 11 | 55 ± 6 | 0 % ± 0 | 13.0 ± 1.2 | 11.3 ± 1.3 | 0 |
| t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 167 ± 11 | 765 ± 7 | 91 ± 5 | 0 % ± 0 | 15.5 ± 0.9 | 13.6 ± 1.6 | 2 |
| t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 26 ± 2 | 144 ± 0 | 12 ± 1 | 0 % ± 0 | 8.2 ± 0.3 | 4.5 ± 0.1 | 0 |
| t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 36 ± 3 | 240 ± 0 | 22 ± 2 | 0 % ± 0 | 7.8 ± 0.3 | 6.4 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-zombis-linea-defender semilla 10300: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t19-testudo-esqueletos-escudo-testudocarga: enemy:arrow=2
- t20-amenaza-blaze-escudo-base-r10: env:onFire=1

## Motivos de decisión (cambios registrados): ENGAGE 1259, FORMATION_HOLD 901, LOW_HEALTH_RETREAT 31, MORALE_SHAKEN 244, MORALE_WAVERING 23, NONE 205, RALLIED 5, REFUSE_ATTACK 8, ROUTED_FLEE 6, SEEK_ENEMY 922, STUCK 5, TOO_CLOSE 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t19-testudo-mezcla-escudo-testudocarga - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t20-amenaza-blaze-escudo-base-r10 - 100 % de las bajas azules ({env:onFire=1.0})
