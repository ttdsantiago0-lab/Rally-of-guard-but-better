# Simulación Rally - lote 20261105-013012-t5-check-shield

Preset `t5-check-shield` · velocidad `sprint` · 241 corridas · 8 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=32}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-zombie-escudo-base | 30 | 67 % ± 9 | 33 % ± 9 | 4.1 ± 0.6 | 4.4 ± 1.2 | 43 ± 3 | 711 ± 12 | 131 ± 9 | 0 % ± 0 | 4.1 ± 0.2 | 6.0 ± 0.2 | 0 |
| t5-final-zombie-escudo-antes | 30 | 67 % ± 9 | 33 % ± 9 | 4.2 ± 0.6 | 4.2 ± 1.2 | 40 ± 2 | 713 ± 13 | 128 ± 8 | 0 % ± 0 | 3.9 ± 0.1 | 6.1 ± 0.4 | 0 |
| t5-final-zombie-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 20 ± 0 | 509 ± 1 | 35 ± 1 | 92 % ± 1 | 5.5 ± 0.2 | 7.7 ± 0.3 | 0 |
| t5-final-zombie-linea-antes | 30 | 70 % ± 9 | 30 % ± 9 | 4.0 ± 0.5 | 2.4 ± 0.7 | 50 ± 4 | 540 ± 7 | 125 ± 9 | 95 % ± 1 | 5.9 ± 0.4 | 8.2 ± 0.4 | 0 |
| t5-final-skeleton-escudo-base | 30 | 45 % ± 6 | 55 % ± 6 | 2.4 ± 0.5 | 3.2 ± 0.5 | 110 ± 3 | 204 ± 14 | 181 ± 8 | 0 % ± 0 | 8.7 ± 0.6 | 9.9 ± 0.8 | 0 |
| t5-final-skeleton-escudo-antes | 30 | 18 % ± 6 | 82 % ± 6 | 1.1 ± 0.4 | 5.5 ± 0.6 | 90 ± 4 | 220 ± 14 | 193 ± 6 | 0 % ± 0 | 7.6 ± 0.4 | 8.1 ± 0.5 | 0 |
| t5-final-skeleton-linea-base | 30 | 67 % ± 8 | 33 % ± 8 | 3.8 ± 0.5 | 1.9 ± 0.6 | 86 ± 5 | 461 ± 14 | 159 ± 8 | 97 % ± 1 | 12.1 ± 0.6 | 12.4 ± 0.7 | 0 |
| t5-final-skeleton-linea-antes | 30 | 58 % ± 9 | 42 % ± 9 | 3.1 ± 0.6 | 2.4 ± 0.6 | 69 ± 4 | 455 ± 17 | 160 ± 8 | 96 % ± 1 | 10.7 ± 0.6 | 10.3 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-zombie-escudo-base semilla 1: sprint → red (0/12 vivos), normal → red (0/10 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 163 de 163 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-zombie-escudo-base: enemy:mob=53
- t5-final-zombie-escudo-antes: enemy:mob=56
- t5-final-zombie-linea-base: enemy:mob=1
- t5-final-zombie-linea-antes: enemy:mob=55
- t5-final-skeleton-escudo-base: enemy:arrow=167
- t5-final-skeleton-escudo-antes: enemy:arrow=208
- t5-final-skeleton-linea-base: enemy:arrow=125
- t5-final-skeleton-linea-antes: enemy:arrow=147

## Motivos de decisión (cambios registrados): COUNTERATTACK 117, ENGAGE 7128, LOW_HEALTH_RETREAT 826, MORALE_SHAKEN 4392, MORALE_WAVERING 1836, NONE 27, OUT_OF_RANGE 18, RALLIED 42, REFUSE_ATTACK 1465, ROUTED_FLEE 1297, SEEK_ENEMY 549, TOO_CLOSE 732, UNIT_BROKEN 4

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t5-final-skeleton-escudo-base: 19/30 corridas llegaron al tope de tiempo
