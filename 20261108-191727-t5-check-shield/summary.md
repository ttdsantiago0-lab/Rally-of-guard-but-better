# Simulación Rally - lote 20261108-191727-t5-check-shield

Preset `t5-check-shield` · velocidad `sprint` · 241 corridas · 8 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=20}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-zombie-escudo-base | 30 | 73 % ± 8 | 27 % ± 8 | 4.3 ± 0.5 | 1.5 ± 0.5 | 43 ± 2 | 760 ± 5 | 129 ± 8 | 0 % ± 0 | 3.9 ± 0.1 | 6.1 ± 0.4 | 0 |
| t5-final-zombie-escudo-antes | 30 | 73 % ± 8 | 27 % ± 8 | 4.0 ± 0.5 | 1.7 ± 0.6 | 42 ± 2 | 762 ± 6 | 133 ± 7 | 0 % ± 0 | 4.0 ± 0.2 | 6.7 ± 0.5 | 0 |
| t5-final-zombie-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 19 ± 0 | 508 ± 2 | 39 ± 2 | 93 % ± 1 | 5.5 ± 0.2 | 7.1 ± 0.3 | 0 |
| t5-final-zombie-linea-antes | 30 | 87 % ± 6 | 13 % ± 6 | 4.6 ± 0.4 | 1.0 ± 0.5 | 45 ± 2 | 552 ± 7 | 110 ± 7 | 94 % ± 1 | 6.3 ± 0.3 | 10.0 ± 0.5 | 0 |
| t5-final-skeleton-escudo-base | 30 | 72 % ± 5 | 28 % ± 5 | 7.1 ± 0.2 | 1.6 ± 0.4 | 110 ± 3 | 295 ± 7 | 98 ± 6 | 0 % ± 0 | 11.7 ± 0.7 | 16.9 ± 0.7 | 0 |
| t5-final-skeleton-escudo-antes | 30 | 88 % ± 5 | 12 % ± 5 | 6.9 ± 0.3 | 0.6 ± 0.4 | 91 ± 4 | 332 ± 8 | 86 ± 6 | 0 % ± 0 | 12.4 ± 0.9 | 13.4 ± 0.8 | 0 |
| t5-final-skeleton-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 35 ± 1 | 546 ± 5 | 39 ± 3 | 81 % ± 1 | 15.1 ± 0.2 | 10.1 ± 0.2 | 0 |
| t5-final-skeleton-linea-antes | 30 | 87 % ± 6 | 13 % ± 6 | 5.9 ± 0.5 | 0.6 ± 0.3 | 66 ± 5 | 511 ± 8 | 117 ± 7 | 89 % ± 2 | 14.7 ± 0.9 | 14.5 ± 0.9 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-zombie-escudo-base semilla 1: sprint → blue (7/0 vivos), normal → red (0/5 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 216 de 216 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-zombie-escudo-base: enemy:mob=57
- t5-final-zombie-escudo-antes: enemy:mob=54
- t5-final-zombie-linea-antes: enemy:mob=46
- t5-final-skeleton-escudo-base: enemy:arrow=28
- t5-final-skeleton-escudo-antes: enemy:arrow=33
- t5-final-skeleton-linea-antes: enemy:arrow=64

## Motivos de decisión (cambios registrados): COUNTERATTACK 3, ENGAGE 4578, LOW_HEALTH_RETREAT 1553, MORALE_SHAKEN 2448, MORALE_WAVERING 712, NONE 112, OUT_OF_RANGE 101, RALLIED 22, REFUSE_ATTACK 49, ROUTED_FLEE 239, SEEK_ENEMY 696, STUCK 5, TOO_CLOSE 689

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t5-final-skeleton-escudo-base: 17/30 corridas llegaron al tope de tiempo
- t5-final-zombie-escudo-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
