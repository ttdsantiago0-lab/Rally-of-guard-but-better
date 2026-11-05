# Simulación Rally - lote 20261105-073125-t5-check-skeleton

Preset `t5-check-skeleton` · velocidad `sprint` · 301 corridas · 10 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-skeleton-mele-base | 30 | 83 % ± 7 | 17 % ± 7 | 4.9 ± 0.5 | 1.0 ± 0.5 | 40 ± 3 | 484 ± 8 | 143 ± 6 | 0 % ± 0 | 7.1 ± 0.3 | 8.5 ± 0.3 | 0 |
| t5-final-skeleton-mele-antes | 30 | 77 % ± 8 | 23 % ± 8 | 5.4 ± 0.6 | 1.8 ± 0.6 | 35 ± 1 | 477 ± 10 | 138 ± 6 | 0 % ± 0 | 6.7 ± 0.2 | 8.5 ± 0.3 | 0 |
| t5-final-skeleton-escudo-base | 30 | 53 % ± 3 | 47 % ± 3 | 5.1 ± 0.4 | 2.8 ± 0.3 | 118 ± 2 | 206 ± 11 | 164 ± 8 | 0 % ± 0 | 7.6 ± 0.2 | 7.8 ± 0.5 | 0 |
| t5-final-skeleton-escudo-antes | 30 | 20 % ± 6 | 80 % ± 6 | 1.5 ± 0.5 | 5.3 ± 0.7 | 90 ± 4 | 213 ± 16 | 184 ± 8 | 0 % ± 0 | 8.1 ± 0.6 | 8.5 ± 0.6 | 0 |
| t5-final-skeleton-arco-base | 30 | 87 % ± 6 | 13 % ± 6 | 5.9 ± 0.5 | 0.6 ± 0.3 | 30 ± 2 | 769 ± 9 | 131 ± 6 | 95 % ± 0 | 11.7 ± 0.2 | 4.7 ± 0.2 | 0 |
| t5-final-skeleton-arco-antes | 30 | 63 % ± 9 | 37 % ± 9 | 3.5 ± 0.6 | 2.7 ± 0.7 | 36 ± 2 | 717 ± 18 | 157 ± 6 | 96 % ± 0 | 10.0 ± 0.2 | 4.7 ± 0.2 | 0 |
| t5-final-skeleton-linea-base | 30 | 82 % ± 5 | 18 % ± 5 | 5.1 ± 0.4 | 0.9 ± 0.3 | 78 ± 6 | 483 ± 9 | 136 ± 8 | 96 % ± 1 | 10.5 ± 0.5 | 9.0 ± 0.7 | 0 |
| t5-final-skeleton-linea-antes | 30 | 38 % ± 9 | 62 % ± 9 | 2.0 ± 0.5 | 3.4 ± 0.6 | 73 ± 4 | 431 ± 12 | 174 ± 7 | 97 % ± 1 | 9.1 ± 0.4 | 9.0 ± 0.7 | 0 |
| t5-final-skeleton-cab-base | 30 | 93 % ± 5 | 7 % ± 5 | 6.6 ± 0.4 | 0.5 ± 0.3 | 32 ± 1 | 663 ± 9 | 110 ± 5 | 94 % ± 1 | 7.0 ± 0.2 | 8.0 ± 0.2 | 0 |
| t5-final-skeleton-cab-antes | 30 | 33 % ± 9 | 67 % ± 9 | 1.8 ± 0.5 | 4.7 ± 0.8 | 49 ± 2 | 555 ± 16 | 173 ± 6 | 97 % ± 1 | 6.4 ± 0.2 | 6.3 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-skeleton-mele-base semilla 1001: sprint → blue (6/0 vivos), normal → red (0/12 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 210 de 210 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-skeleton-mele-base: enemy:arrow=92
- t5-final-skeleton-mele-antes: enemy:arrow=79
- t5-final-skeleton-escudo-base: enemy:arrow=86
- t5-final-skeleton-escudo-antes: enemy:arrow=194
- t5-final-skeleton-arco-base: enemy:arrow=62
- t5-final-skeleton-arco-antes: enemy:arrow=134
- t5-final-skeleton-linea-base: enemy:arrow=86
- t5-final-skeleton-linea-antes: enemy:arrow=179
- t5-final-skeleton-cab-base: enemy:arrow=42
- t5-final-skeleton-cab-antes: enemy:arrow=186

## Motivos de decisión (cambios registrados): COUNTERATTACK 70, ENGAGE 8779, FORMATION_HOLD 60, LOW_HEALTH_RETREAT 1098, MORALE_SHAKEN 5624, MORALE_WAVERING 2597, NONE 27, OUT_OF_RANGE 6, RALLIED 46, REFUSE_ATTACK 1874, ROUTED_FLEE 1692, SEEK_ENEMY 602, STUCK 1, TOO_CLOSE 305

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t5-final-skeleton-escudo-base: 26/30 corridas llegaron al tope de tiempo
- t5-final-skeleton-mele-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
