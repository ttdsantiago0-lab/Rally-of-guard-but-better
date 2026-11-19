# Simulación Rally - lote 20261119-042629-t85-reg-escolta

Preset `t85-reg-escolta` · velocidad `sprint` · 67 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t85-kite-t17-escolta-zombis-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 1162 ± 4 | 0 ± 0 | 97 % ± 1 | 18.3 ± 0.1 | 7.7 ± 0.2 | 0 |
| t85-kite-t17-escolta-esqueletos-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 668 ± 8 | 7 ± 2 | 90 % ± 2 | 23.4 ± 0.2 | 3.7 ± 0.1 | 0 |
| t85-kite-t17-escolta-mezcla-linea-defender | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 65 ± 0 | 830 ± 9 | 6 ± 1 | 95 % ± 1 | 17.3 ± 0.2 | 5.3 ± 0.1 | 0 |
| t85-kite-t17-escolta-esqueletos-escudo-defender | 8 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 5.1 ± 1.0 | 30 ± 5 | 107 ± 8 | 15 ± 3 | 0 % ± 0 | 12.2 ± 0.2 | 3.5 ± 0.3 | 0 |
| t85-kite-t19-testudo-esqueletos-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 82 ± 6 | 247 ± 8 | 56 ± 3 | 0 % ± 0 | 12.7 ± 0.7 | 8.4 ± 1.3 | 0 |
| t85-kite-t19-testudo-mezcla-escudo-testudocarga | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 166 ± 10 | 756 ± 10 | 96 ± 6 | 0 % ± 0 | 15.1 ± 0.7 | 11.9 ± 1.4 | 2 |
| t85-kite-t20-amenaza-blaze-escudo-base-r6 | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 25 ± 1 | 144 ± 0 | 10 ± 1 | 0 % ± 0 | 8.2 ± 0.2 | 4.5 ± 0.2 | 0 |
| t85-kite-t20-amenaza-blaze-escudo-base-r10 | 8 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 35 ± 1 | 240 ± 0 | 23 ± 2 | 0 % ± 0 | 8.1 ± 0.3 | 6.7 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t85-kite-t17-escolta-zombis-linea-defender semilla 8300: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 64 de 64 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t85-kite-t19-testudo-esqueletos-escudo-testudocarga: enemy:arrow=1
- t85-kite-t20-amenaza-blaze-escudo-base-r10: env:onFire=1

## Motivos de decisión (cambios registrados): ENGAGE 1155, FORMATION_HOLD 884, LOW_HEALTH_RETREAT 37, MORALE_SHAKEN 162, MORALE_WAVERING 14, NONE 210, OUT_OF_RANGE 2, REFUSE_ATTACK 3, ROUTED_FLEE 1, SEEK_ENEMY 917, STUCK 20

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t85-kite-t19-testudo-mezcla-escudo-testudocarga - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t85-kite-t20-amenaza-blaze-escudo-base-r10 - 100 % de las bajas azules ({env:onFire=1.0})
