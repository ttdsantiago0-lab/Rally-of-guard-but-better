# Simulación Rally - lote 20261105-015335-t5-check-shield

Preset `t5-check-shield` · velocidad `sprint` · 241 corridas · 8 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=25}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-zombie-escudo-base | 30 | 53 % ± 9 | 47 % ± 9 | 3.5 ± 0.6 | 5.4 ± 1.2 | 46 ± 3 | 705 ± 12 | 143 ± 10 | 0 % ± 0 | 4.2 ± 0.3 | 6.1 ± 0.3 | 0 |
| t5-final-zombie-escudo-antes | 30 | 77 % ± 8 | 23 % ± 8 | 4.7 ± 0.5 | 2.2 ± 0.8 | 41 ± 2 | 736 ± 8 | 117 ± 9 | 0 % ± 0 | 3.8 ± 0.1 | 6.1 ± 0.4 | 0 |
| t5-final-zombie-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.0 | 0.0 ± 0.0 | 20 ± 0 | 508 ± 2 | 36 ± 2 | 92 % ± 1 | 5.3 ± 0.2 | 7.1 ± 0.3 | 0 |
| t5-final-zombie-linea-antes | 30 | 83 % ± 7 | 17 % ± 7 | 5.0 ± 0.5 | 1.2 ± 0.6 | 46 ± 3 | 536 ± 7 | 112 ± 8 | 95 % ± 1 | 6.3 ± 0.3 | 8.7 ± 0.4 | 0 |
| t5-final-skeleton-escudo-base | 30 | 37 % ± 5 | 63 % ± 5 | 1.9 ± 0.3 | 4.0 ± 0.3 | 113 ± 2 | 185 ± 11 | 196 ± 4 | 0 % ± 0 | 8.3 ± 0.3 | 10.5 ± 0.6 | 0 |
| t5-final-skeleton-escudo-antes | 30 | 28 % ± 7 | 72 % ± 7 | 1.7 ± 0.5 | 4.1 ± 0.5 | 99 ± 4 | 225 ± 14 | 183 ± 8 | 0 % ± 0 | 8.6 ± 0.5 | 9.5 ± 0.5 | 0 |
| t5-final-skeleton-linea-base | 30 | 72 % ± 7 | 28 % ± 7 | 4.5 ± 0.6 | 1.2 ± 0.3 | 74 ± 6 | 475 ± 12 | 141 ± 8 | 96 % ± 1 | 11.1 ± 0.6 | 9.9 ± 0.7 | 0 |
| t5-final-skeleton-linea-antes | 30 | 38 % ± 9 | 62 % ± 9 | 2.0 ± 0.5 | 3.1 ± 0.6 | 75 ± 5 | 434 ± 11 | 176 ± 8 | 98 % ± 1 | 9.0 ± 0.4 | 8.6 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-zombie-escudo-base semilla 1001: sprint → blue (6/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 165 de 165 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-zombie-escudo-base: enemy:mob=57
- t5-final-zombie-escudo-antes: enemy:mob=50
- t5-final-zombie-linea-base: enemy:mob=1
- t5-final-zombie-linea-antes: enemy:mob=50
- t5-final-skeleton-escudo-base: enemy:arrow=182
- t5-final-skeleton-escudo-antes: enemy:arrow=190
- t5-final-skeleton-linea-base: enemy:arrow=105
- t5-final-skeleton-linea-antes: enemy:arrow=179

## Motivos de decisión (cambios registrados): COUNTERATTACK 123, ENGAGE 7161, LOW_HEALTH_RETREAT 730, MORALE_SHAKEN 4326, MORALE_WAVERING 1791, NONE 8, OUT_OF_RANGE 14, RALLIED 35, REFUSE_ATTACK 1448, ROUTED_FLEE 1285, SEEK_ENEMY 573, TOO_CLOSE 757, UNIT_BROKEN 7

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t5-final-skeleton-escudo-base: 20/30 corridas llegaron al tope de tiempo
