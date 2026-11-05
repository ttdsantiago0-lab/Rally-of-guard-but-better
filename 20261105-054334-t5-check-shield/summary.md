# Simulación Rally - lote 20261105-054334-t5-check-shield

Preset `t5-check-shield` · velocidad `sprint` · 241 corridas · 8 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=30}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-zombie-escudo-base | 30 | 77 % ± 8 | 23 % ± 8 | 4.8 ± 0.5 | 3.4 ± 1.2 | 43 ± 3 | 716 ± 11 | 118 ± 9 | 0 % ± 0 | 4.1 ± 0.2 | 5.7 ± 0.3 | 0 |
| t5-final-zombie-escudo-antes | 30 | 57 % ± 9 | 43 % ± 9 | 3.4 ± 0.6 | 4.6 ± 1.1 | 45 ± 2 | 720 ± 11 | 140 ± 9 | 0 % ± 0 | 4.0 ± 0.2 | 6.0 ± 0.3 | 0 |
| t5-final-zombie-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 20 ± 0 | 506 ± 2 | 38 ± 3 | 92 % ± 1 | 5.5 ± 0.2 | 7.1 ± 0.3 | 0 |
| t5-final-zombie-linea-antes | 30 | 77 % ± 8 | 23 % ± 8 | 4.5 ± 0.5 | 2.6 ± 0.9 | 45 ± 2 | 528 ± 8 | 115 ± 8 | 93 % ± 1 | 5.8 ± 0.2 | 9.1 ± 0.3 | 0 |
| t5-final-skeleton-escudo-base | 30 | 55 % ± 3 | 45 % ± 3 | 5.2 ± 0.3 | 2.5 ± 0.3 | 119 ± 1 | 211 ± 10 | 166 ± 6 | 0 % ± 0 | 7.2 ± 0.2 | 6.8 ± 0.6 | 0 |
| t5-final-skeleton-escudo-antes | 30 | 15 % ± 5 | 85 % ± 5 | 1.1 ± 0.4 | 5.7 ± 0.6 | 85 ± 4 | 211 ± 13 | 189 ± 6 | 0 % ± 0 | 7.7 ± 0.5 | 9.7 ± 0.7 | 0 |
| t5-final-skeleton-linea-base | 30 | 82 % ± 6 | 18 % ± 6 | 5.1 ± 0.4 | 0.9 ± 0.3 | 75 ± 6 | 486 ± 13 | 130 ± 8 | 96 % ± 1 | 10.1 ± 0.5 | 8.3 ± 0.6 | 0 |
| t5-final-skeleton-linea-antes | 30 | 55 % ± 9 | 45 % ± 9 | 3.5 ± 0.7 | 2.9 ± 0.6 | 62 ± 4 | 446 ± 12 | 150 ± 9 | 96 % ± 1 | 10.7 ± 0.7 | 9.1 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-zombie-escudo-base semilla 1001: sprint → blue (7/0 vivos), normal → red (0/13 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 175 de 175 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-zombie-escudo-base: enemy:mob=50
- t5-final-zombie-escudo-antes: enemy:mob=64
- t5-final-zombie-linea-base: enemy:mob=1
- t5-final-zombie-linea-antes: enemy:mob=55
- t5-final-skeleton-escudo-base: enemy:arrow=85
- t5-final-skeleton-escudo-antes: enemy:arrow=207
- t5-final-skeleton-linea-base: enemy:arrow=86
- t5-final-skeleton-linea-antes: enemy:arrow=135

## Motivos de decisión (cambios registrados): COUNTERATTACK 130, ENGAGE 7090, LOW_HEALTH_RETREAT 722, MORALE_SHAKEN 4412, MORALE_WAVERING 1790, NONE 21, OUT_OF_RANGE 17, RALLIED 29, REFUSE_ATTACK 1517, ROUTED_FLEE 1297, SEEK_ENEMY 587, STUCK 4, TOO_CLOSE 738

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t5-final-skeleton-escudo-base: 27/30 corridas llegaron al tope de tiempo
- t5-final-zombie-escudo-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
