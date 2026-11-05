# Simulación Rally - lote 20261105-075615-t5-check-skeleton

Preset `t5-check-skeleton` · velocidad `sprint` · 301 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-skeleton-mele-base | 30 | 70 % ± 9 | 30 % ± 9 | 4.5 ± 0.6 | 1.8 ± 0.6 | 41 ± 3 | 478 ± 9 | 148 ± 7 | 0 % ± 0 | 7.0 ± 0.3 | 8.9 ± 0.3 | 0 |
| t5-final-skeleton-mele-antes | 30 | 77 % ± 8 | 23 % ± 8 | 5.1 ± 0.6 | 1.4 ± 0.5 | 38 ± 2 | 481 ± 9 | 142 ± 6 | 0 % ± 0 | 6.7 ± 0.1 | 8.6 ± 0.3 | 0 |
| t5-final-skeleton-escudo-base | 30 | 50 % ± 2 | 50 % ± 2 | 5.5 ± 0.3 | 2.5 ± 0.2 | 119 ± 1 | 202 ± 11 | 161 ± 6 | 0 % ± 0 | 7.5 ± 0.2 | 7.6 ± 0.6 | 0 |
| t5-final-skeleton-escudo-antes | 30 | 15 % ± 5 | 85 % ± 5 | 0.9 ± 0.3 | 5.2 ± 0.6 | 97 ± 3 | 193 ± 10 | 200 ± 3 | 0 % ± 0 | 7.7 ± 0.4 | 8.4 ± 0.4 | 0 |
| t5-final-skeleton-arco-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 24 ± 0 | 796 ± 6 | 90 ± 3 | 93 % ± 1 | 14.9 ± 0.1 | 5.2 ± 0.1 | 0 |
| t5-final-skeleton-arco-antes | 30 | 60 % ± 9 | 40 % ± 9 | 3.7 ± 0.6 | 2.6 ± 0.7 | 34 ± 2 | 736 ± 15 | 155 ± 6 | 96 % ± 0 | 10.3 ± 0.3 | 4.9 ± 0.2 | 0 |
| t5-final-skeleton-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 36 ± 1 | 555 ± 6 | 43 ± 3 | 82 % ± 1 | 15.1 ± 0.3 | 10.0 ± 0.3 | 0 |
| t5-final-skeleton-linea-antes | 30 | 52 % ± 9 | 48 % ± 9 | 2.8 ± 0.6 | 2.6 ± 0.6 | 73 ± 4 | 443 ± 14 | 165 ± 7 | 97 % ± 0 | 9.7 ± 0.4 | 9.5 ± 0.6 | 0 |
| t5-final-skeleton-cab-base | 30 | 87 % ± 6 | 13 % ± 6 | 6.1 ± 0.5 | 0.8 ± 0.4 | 34 ± 2 | 655 ± 9 | 115 ± 7 | 94 % ± 1 | 7.6 ± 0.2 | 7.8 ± 0.2 | 0 |
| t5-final-skeleton-cab-antes | 30 | 43 % ± 9 | 57 % ± 9 | 2.6 ± 0.6 | 3.8 ± 0.8 | 48 ± 3 | 572 ± 13 | 160 ± 8 | 98 % ± 1 | 6.6 ± 0.3 | 7.2 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-skeleton-mele-base semilla 1001: sprint → red (0/5 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 214 de 214 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-skeleton-mele-base: enemy:arrow=105
- t5-final-skeleton-mele-antes: enemy:arrow=87
- t5-final-skeleton-escudo-base: enemy:arrow=75
- t5-final-skeleton-escudo-antes: enemy:arrow=214
- t5-final-skeleton-arco-base: enemy:arrow=1
- t5-final-skeleton-arco-antes: enemy:arrow=128
- t5-final-skeleton-linea-antes: enemy:arrow=157
- t5-final-skeleton-cab-base: enemy:arrow=57
- t5-final-skeleton-cab-antes: enemy:arrow=161

## Motivos de decisión (cambios registrados): COUNTERATTACK 46, ENGAGE 8502, FORMATION_HOLD 59, LOW_HEALTH_RETREAT 1020, MORALE_SHAKEN 5490, MORALE_WAVERING 2237, NONE 22, OUT_OF_RANGE 18, RALLIED 42, REFUSE_ATTACK 1448, ROUTED_FLEE 1407, SEEK_ENEMY 593, TOO_CLOSE 261, UNIT_BROKEN 4

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t5-final-skeleton-escudo-base: 28/30 corridas llegaron al tope de tiempo
- t5-final-skeleton-mele-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
