# Simulación Rally - lote 20261105-131941-t10-quiebre

Preset `t10-quiebre` · velocidad `sprint` · 47 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=33}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t10-quiebre-mixta-r16 | 10 | 50 % ± 17 | 50 % ± 17 | 3.5 ± 1.2 | 2.8 ± 1.0 | 328 ± 34 | 2681 ± 219 | 407 ± 41 | 86 % ± 1 | 7.6 ± 1.6 | 8.7 ± 1.7 | 0 |
| t10-quiebre-zombie-r24 | 10 | 10 % ± 10 | 90 % ± 10 | 0.7 ± 0.7 | 8.3 ± 2.6 | 293 ± 24 | 3775 ± 453 | 637 ± 48 | 90 % ± 1 | 5.5 ± 1.0 | 7.6 ± 1.4 | 0 |
| t10-quiebre-zombie-r32 | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 14.3 ± 2.9 | 165 ± 23 | 1617 ± 126 | 533 ± 25 | 95 % ± 1 | 7.4 ± 1.8 | 9.0 ± 1.3 | 2 |
| t10-quiebre-mixta-r22 | 9 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 16.6 ± 2.0 | 263 ± 34 | 2063 ± 193 | 510 ± 28 | 88 % ± 1 | 6.3 ± 1.3 | 6.6 ± 0.8 | 5 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t10-quiebre-mixta-r16 semilla 1: sprint → blue (8/0 vivos), normal → blue (2/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 6 de 6 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t10-quiebre-mixta-r16: enemy:explosion.player=20 enemy:mob=11
- t10-quiebre-zombie-r24: enemy:mob=36
- t10-quiebre-zombie-r32: enemy:mob=41
- t10-quiebre-mixta-r22: enemy:arrow=1 enemy:explosion.player=29 enemy:mob=21

## Motivos de decisión (cambios registrados): COUNTERATTACK 399, ENGAGE 3733, LOW_HEALTH_RETREAT 216, MORALE_SHAKEN 1037, MORALE_WAVERING 535, NONE 1552, OUT_OF_RANGE 37, RALLIED 78, REFUSE_ATTACK 272, ROUTED_FLEE 747, SEEK_ENEMY 583, STUCK 12, TOO_CLOSE 1554

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t10-quiebre-mixta-r22 - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t10-quiebre-zombie-r32 - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
