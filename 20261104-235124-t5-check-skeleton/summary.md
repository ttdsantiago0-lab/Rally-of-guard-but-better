# Simulación Rally - lote 20261104-235124-t5-check-skeleton

Preset `t5-check-skeleton` · velocidad `sprint` · 301 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-skeleton-mele-base | 30 | 90 % ± 6 | 10 % ± 6 | 6.1 ± 0.5 | 0.8 ± 0.4 | 34 ± 2 | 488 ± 6 | 126 ± 6 | 0 % ± 0 | 7.0 ± 0.3 | 8.9 ± 0.3 | 0 |
| t5-final-skeleton-mele-antes | 30 | 77 % ± 8 | 23 % ± 8 | 5.2 ± 0.6 | 1.2 ± 0.5 | 39 ± 3 | 480 ± 7 | 138 ± 7 | 0 % ± 0 | 7.1 ± 0.4 | 7.9 ± 0.2 | 0 |
| t5-final-skeleton-escudo-base | 30 | 7 % ± 4 | 93 % ± 4 | 0.5 ± 0.3 | 6.7 ± 0.6 | 85 ± 3 | 157 ± 11 | 198 ± 3 | 0 % ± 0 | 7.0 ± 0.2 | 7.6 ± 0.4 | 0 |
| t5-final-skeleton-escudo-antes | 30 | 22 % ± 6 | 78 % ± 6 | 1.6 ± 0.5 | 5.4 ± 0.6 | 92 ± 3 | 215 ± 13 | 187 ± 7 | 0 % ± 0 | 7.7 ± 0.3 | 8.4 ± 0.5 | 0 |
| t5-final-skeleton-arco-base | 30 | 70 % ± 9 | 30 % ± 9 | 4.2 ± 0.6 | 2.5 ± 0.8 | 31 ± 2 | 741 ± 15 | 150 ± 6 | 96 % ± 0 | 10.8 ± 0.2 | 4.8 ± 0.3 | 0 |
| t5-final-skeleton-arco-antes | 30 | 77 % ± 8 | 23 % ± 8 | 4.4 ± 0.6 | 1.5 ± 0.5 | 33 ± 2 | 743 ± 11 | 147 ± 6 | 96 % ± 0 | 10.2 ± 0.2 | 4.7 ± 0.2 | 0 |
| t5-final-skeleton-linea-base | 30 | 78 % ± 7 | 22 % ± 7 | 4.5 ± 0.6 | 0.7 ± 0.2 | 73 ± 6 | 486 ± 9 | 146 ± 9 | 97 % ± 1 | 11.2 ± 0.4 | 10.1 ± 0.5 | 0 |
| t5-final-skeleton-linea-antes | 30 | 48 % ± 9 | 52 % ± 9 | 2.5 ± 0.5 | 2.7 ± 0.6 | 74 ± 3 | 450 ± 12 | 169 ± 7 | 96 % ± 1 | 10.2 ± 0.7 | 8.5 ± 0.6 | 0 |
| t5-final-skeleton-cab-base | 30 | 87 % ± 6 | 13 % ± 6 | 6.3 ± 0.5 | 0.9 ± 0.4 | 33 ± 1 | 655 ± 10 | 115 ± 6 | 95 % ± 1 | 7.2 ± 0.2 | 7.5 ± 0.2 | 0 |
| t5-final-skeleton-cab-antes | 30 | 33 % ± 9 | 67 % ± 9 | 2.1 ± 0.6 | 7.0 ± 1.1 | 44 ± 2 | 521 ± 19 | 167 ± 7 | 96 % ± 1 | 6.5 ± 0.3 | 7.6 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-skeleton-mele-base semilla 1: sprint → red (0/10 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 183 de 183 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-skeleton-mele-base: enemy:arrow=57
- t5-final-skeleton-mele-antes: enemy:arrow=84
- t5-final-skeleton-escudo-base: enemy:arrow=226
- t5-final-skeleton-escudo-antes: enemy:arrow=192
- t5-final-skeleton-arco-base: enemy:arrow=113
- t5-final-skeleton-arco-antes: enemy:arrow=107
- t5-final-skeleton-linea-base: enemy:arrow=104
- t5-final-skeleton-linea-antes: enemy:arrow=165
- t5-final-skeleton-cab-base: enemy:arrow=52
- t5-final-skeleton-cab-antes: enemy:arrow=176

## Motivos de decisión (cambios registrados): COUNTERATTACK 54, ENGAGE 8640, FORMATION_HOLD 54, LOW_HEALTH_RETREAT 1037, MORALE_SHAKEN 5651, MORALE_WAVERING 2479, NONE 22, OUT_OF_RANGE 18, RALLIED 50, REFUSE_ATTACK 1829, ROUTED_FLEE 1620, SEEK_ENEMY 627, TOO_CLOSE 307, UNIT_BROKEN 4

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t5-final-skeleton-mele-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
