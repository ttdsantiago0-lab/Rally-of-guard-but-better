# Simulación Rally - lote 20261105-073126-t5-check-skeleton

Preset `t5-check-skeleton` · velocidad `sprint` · 301 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-skeleton-mele-base | 30 | 70 % ± 9 | 30 % ± 9 | 4.7 ± 0.6 | 1.4 ± 0.5 | 39 ± 3 | 485 ± 8 | 142 ± 7 | 0 % ± 0 | 7.1 ± 0.3 | 8.8 ± 0.3 | 0 |
| t5-final-skeleton-mele-antes | 30 | 83 % ± 7 | 17 % ± 7 | 5.8 ± 0.5 | 0.8 ± 0.4 | 36 ± 2 | 488 ± 7 | 129 ± 7 | 0 % ± 0 | 7.0 ± 0.2 | 9.0 ± 0.3 | 0 |
| t5-final-skeleton-escudo-base | 30 | 53 % ± 2 | 47 % ± 2 | 5.2 ± 0.4 | 3.4 ± 0.3 | 117 ± 2 | 200 ± 10 | 169 ± 7 | 0 % ± 0 | 7.3 ± 0.1 | 7.6 ± 0.5 | 0 |
| t5-final-skeleton-escudo-antes | 30 | 18 % ± 6 | 82 % ± 6 | 1.1 ± 0.5 | 4.9 ± 0.5 | 90 ± 4 | 223 ± 14 | 191 ± 7 | 0 % ± 0 | 7.2 ± 0.2 | 8.1 ± 0.4 | 0 |
| t5-final-skeleton-arco-base | 30 | 80 % ± 7 | 20 % ± 7 | 5.1 ± 0.6 | 0.6 ± 0.3 | 31 ± 2 | 762 ± 11 | 137 ± 6 | 96 % ± 0 | 11.6 ± 0.2 | 4.8 ± 0.2 | 0 |
| t5-final-skeleton-arco-antes | 30 | 53 % ± 9 | 47 % ± 9 | 3.5 ± 0.7 | 3.8 ± 0.9 | 32 ± 1 | 701 ± 19 | 156 ± 6 | 96 % ± 1 | 10.1 ± 0.2 | 4.4 ± 0.1 | 0 |
| t5-final-skeleton-linea-base | 30 | 88 % ± 5 | 12 % ± 5 | 5.9 ± 0.4 | 0.5 ± 0.3 | 70 ± 6 | 489 ± 10 | 123 ± 9 | 95 % ± 1 | 11.9 ± 0.6 | 9.8 ± 0.7 | 0 |
| t5-final-skeleton-linea-antes | 30 | 40 % ± 9 | 60 % ± 9 | 2.1 ± 0.5 | 3.1 ± 0.6 | 75 ± 4 | 419 ± 13 | 173 ± 7 | 97 % ± 1 | 9.7 ± 0.5 | 8.8 ± 0.6 | 0 |
| t5-final-skeleton-cab-base | 30 | 93 % ± 5 | 7 % ± 5 | 6.6 ± 0.4 | 0.3 ± 0.2 | 32 ± 2 | 668 ± 7 | 106 ± 6 | 93 % ± 1 | 7.1 ± 0.2 | 7.5 ± 0.2 | 0 |
| t5-final-skeleton-cab-antes | 30 | 27 % ± 8 | 73 % ± 8 | 1.5 ± 0.5 | 5.8 ± 0.9 | 45 ± 2 | 531 ± 16 | 172 ± 6 | 97 % ± 1 | 6.0 ± 0.2 | 6.5 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-skeleton-mele-base semilla 1: sprint → blue (6/0 vivos), normal → red (0/7 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 202 de 202 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-skeleton-mele-base: enemy:arrow=98
- t5-final-skeleton-mele-antes: enemy:arrow=67
- t5-final-skeleton-escudo-base: enemy:arrow=83
- t5-final-skeleton-escudo-antes: enemy:arrow=206
- t5-final-skeleton-arco-base: enemy:arrow=86
- t5-final-skeleton-arco-antes: enemy:arrow=136
- t5-final-skeleton-linea-base: enemy:arrow=64
- t5-final-skeleton-linea-antes: enemy:arrow=176
- t5-final-skeleton-cab-base: enemy:arrow=43
- t5-final-skeleton-cab-antes: enemy:arrow=195

## Motivos de decisión (cambios registrados): COUNTERATTACK 73, ENGAGE 8686, FORMATION_HOLD 37, LOW_HEALTH_RETREAT 1029, MORALE_SHAKEN 5658, MORALE_WAVERING 2488, NONE 20, OUT_OF_RANGE 11, RALLIED 42, REFUSE_ATTACK 1775, ROUTED_FLEE 1656, SEEK_ENEMY 594, STUCK 4, TOO_CLOSE 297, UNIT_BROKEN 5

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t5-final-skeleton-escudo-base: 28/30 corridas llegaron al tope de tiempo
- t5-final-skeleton-mele-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
