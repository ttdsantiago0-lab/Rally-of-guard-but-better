# Simulación Rally - lote 20261104-200303-t3-final-skeleton2

Preset `t3-final-skeleton2` · velocidad `sprint` · 271 corridas · 9 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t3-final-skeleton-arco-base | 30 | 53 % ± 9 | 47 % ± 9 | 3.2 ± 0.6 | 3.2 ± 0.8 | 35 ± 2 | 710 ± 17 | 161 ± 6 | 96 % ± 0 | 9.9 ± 0.2 | 4.7 ± 0.2 | 0 |
| t3-final-skeleton-arco-volea | 30 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.2 | 0.0 ± 0.0 | 27 ± 1 | 794 ± 5 | 108 ± 4 | 94 % ± 0 | 14.4 ± 0.1 | 5.5 ± 0.2 | 0 |
| t3-final-skeleton-arco-quietos | 30 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.2 | 0.0 ± 0.0 | 27 ± 1 | 799 ± 6 | 105 ± 4 | 93 % ± 0 | 14.6 ± 0.2 | 5.9 ± 0.2 | 0 |
| t3-final-skeleton-linea-base | 30 | 52 % ± 9 | 48 % ± 9 | 2.9 ± 0.6 | 2.8 ± 0.7 | 70 ± 4 | 442 ± 11 | 162 ± 8 | 97 % ± 1 | 10.1 ± 0.5 | 9.1 ± 0.5 | 0 |
| t3-final-skeleton-linea-volea | 30 | 97 % ± 3 | 3 % ± 3 | 7.7 ± 0.3 | 0.1 ± 0.1 | 40 ± 1 | 531 ± 8 | 58 ± 6 | 80 % ± 1 | 14.5 ± 0.2 | 10.3 ± 0.2 | 0 |
| t3-final-skeleton-linea-quietos | 30 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.2 | 0.0 ± 0.0 | 41 ± 2 | 535 ± 7 | 59 ± 6 | 82 % ± 1 | 14.8 ± 0.3 | 10.3 ± 0.2 | 0 |
| t3-final-skeleton-cab-base | 30 | 40 % ± 9 | 60 % ± 9 | 2.0 ± 0.5 | 4.5 ± 0.9 | 47 ± 2 | 561 ± 15 | 171 ± 6 | 97 % ± 1 | 6.4 ± 0.2 | 7.1 ± 0.2 | 0 |
| t3-final-skeleton-cab-quemarropa | 30 | 87 % ± 6 | 13 % ± 6 | 6.0 ± 0.5 | 0.9 ± 0.4 | 34 ± 2 | 663 ± 10 | 117 ± 7 | 94 % ± 1 | 7.3 ± 0.4 | 7.9 ± 0.3 | 0 |
| t3-final-skeleton-cab-lineafija | 30 | 50 % ± 9 | 50 % ± 9 | 1.1 ± 0.2 | 2.1 ± 0.5 | 65 ± 2 | 404 ± 9 | 184 ± 2 | 98 % ± 0 | 10.7 ± 0.2 | 5.6 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t3-final-skeleton-arco-base semilla 1: sprint → blue (6/0 vivos), normal → red (0/4 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 204 de 204 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t3-final-skeleton-arco-base: enemy:arrow=143
- t3-final-skeleton-arco-volea: enemy:arrow=14
- t3-final-skeleton-arco-quietos: enemy:arrow=17
- t3-final-skeleton-linea-base: enemy:arrow=154
- t3-final-skeleton-linea-volea: enemy:arrow=9
- t3-final-skeleton-linea-quietos: enemy:arrow=10
- t3-final-skeleton-cab-base: enemy:arrow=179
- t3-final-skeleton-cab-quemarropa: enemy:arrow=60
- t3-final-skeleton-cab-lineafija: enemy:arrow=207

## Motivos de decisión (cambios registrados): COUNTERATTACK 13, ENGAGE 6668, FORMATION_HOLD 82, LOW_HEALTH_RETREAT 846, MORALE_SHAKEN 3780, MORALE_WAVERING 1840, NONE 22, ORDER_OVERRIDE 458, OUT_OF_RANGE 47, RALLIED 30, REFUSE_ATTACK 613, ROUTED_FLEE 1082, SEEK_ENEMY 470, TOO_CLOSE 409, UNIT_BROKEN 25

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t3-final-skeleton-arco-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
