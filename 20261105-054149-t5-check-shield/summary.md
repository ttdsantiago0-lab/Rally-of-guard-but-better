# Simulación Rally - lote 20261105-054149-t5-check-shield

Preset `t5-check-shield` · velocidad `sprint` · 241 corridas · 8 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=37}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-zombie-escudo-base | 30 | 53 % ± 9 | 47 % ± 9 | 3.5 ± 0.6 | 4.4 ± 1.0 | 47 ± 3 | 724 ± 9 | 144 ± 9 | 0 % ± 0 | 4.1 ± 0.2 | 5.8 ± 0.3 | 0 |
| t5-final-zombie-escudo-antes | 30 | 47 % ± 9 | 53 % ± 9 | 2.9 ± 0.6 | 5.4 ± 1.1 | 48 ± 3 | 713 ± 12 | 147 ± 10 | 0 % ± 0 | 4.2 ± 0.2 | 6.2 ± 0.4 | 0 |
| t5-final-zombie-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 20 ± 0 | 504 ± 2 | 37 ± 2 | 91 % ± 1 | 5.6 ± 0.2 | 7.2 ± 0.4 | 0 |
| t5-final-zombie-linea-antes | 30 | 80 % ± 7 | 20 % ± 7 | 4.4 ± 0.5 | 1.6 ± 0.6 | 48 ± 3 | 545 ± 7 | 117 ± 8 | 93 % ± 1 | 6.2 ± 0.3 | 8.2 ± 0.4 | 0 |
| t5-final-skeleton-escudo-base | 30 | 62 % ± 4 | 38 % ± 4 | 5.9 ± 0.3 | 2.4 ± 0.4 | 117 ± 1 | 212 ± 14 | 152 ± 7 | 0 % ± 0 | 7.5 ± 0.2 | 7.7 ± 0.7 | 0 |
| t5-final-skeleton-escudo-antes | 30 | 15 % ± 6 | 85 % ± 6 | 0.8 ± 0.4 | 5.9 ± 0.6 | 86 ± 3 | 201 ± 15 | 193 ± 6 | 0 % ± 0 | 7.3 ± 0.3 | 8.4 ± 0.5 | 0 |
| t5-final-skeleton-linea-base | 30 | 78 % ± 5 | 22 % ± 5 | 4.8 ± 0.5 | 1.1 ± 0.3 | 82 ± 6 | 467 ± 12 | 138 ± 8 | 97 % ± 1 | 11.0 ± 0.6 | 10.4 ± 0.8 | 0 |
| t5-final-skeleton-linea-antes | 30 | 43 % ± 9 | 57 % ± 9 | 2.9 ± 0.6 | 2.7 ± 0.5 | 71 ± 4 | 449 ± 12 | 160 ± 9 | 97 % ± 1 | 9.6 ± 0.5 | 8.7 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-zombie-escudo-base semilla 1: sprint → blue (6/0 vivos), normal → red (0/4 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 163 de 163 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-zombie-escudo-base: enemy:mob=66
- t5-final-zombie-escudo-antes: enemy:mob=81
- t5-final-zombie-linea-antes: enemy:mob=55
- t5-final-skeleton-escudo-base: enemy:arrow=64
- t5-final-skeleton-escudo-antes: enemy:arrow=215
- t5-final-skeleton-linea-base: enemy:arrow=97
- t5-final-skeleton-linea-antes: enemy:arrow=153

## Motivos de decisión (cambios registrados): COUNTERATTACK 114, ENGAGE 7195, LOW_HEALTH_RETREAT 763, MORALE_SHAKEN 4464, MORALE_WAVERING 1861, NONE 13, OUT_OF_RANGE 15, RALLIED 42, REFUSE_ATTACK 1472, ROUTED_FLEE 1352, SEEK_ENEMY 598, STUCK 1, TOO_CLOSE 705, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t5-final-skeleton-escudo-base: 23/30 corridas llegaron al tope de tiempo
- t5-final-zombie-escudo-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
