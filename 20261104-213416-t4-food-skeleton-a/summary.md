# Simulación Rally - lote 20261104-213416-t4-food-skeleton-a

Preset `t4-food-skeleton-a` · velocidad `sprint` · 211 corridas · 7 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t4-final-skeleton-mele-base | 30 | 83 % ± 7 | 17 % ± 7 | 5.9 ± 0.5 | 0.9 ± 0.4 | 37 ± 2 | 487 ± 8 | 133 ± 6 | 0 % ± 0 | 7.4 ± 0.4 | 9.5 ± 0.3 | 0 |
| t4-final-skeleton-mele-comidamochila | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 33 ± 2 | 508 ± 3 | 124 ± 6 | 0 % ± 0 | 7.1 ± 0.2 | 9.3 ± 0.3 | 0 |
| t4-final-skeleton-mele-comidamano | 30 | 95 % ± 3 | 5 % ± 3 | 7.5 ± 0.2 | 0.3 ± 0.2 | 49 ± 6 | 479 ± 7 | 184 ± 19 | 0 % ± 0 | 7.3 ± 0.3 | 10.0 ± 0.5 | 0 |
| t4-final-skeleton-escudo-base | 30 | 13 % ± 5 | 87 % ± 5 | 1.0 ± 0.4 | 5.7 ± 0.5 | 89 ± 4 | 205 ± 12 | 193 ± 7 | 0 % ± 0 | 7.4 ± 0.3 | 8.7 ± 0.5 | 0 |
| t4-final-skeleton-escudo-comidamochila | 30 | 60 % ± 4 | 40 % ± 4 | 8.0 ± 0.0 | 2.6 ± 0.4 | 116 ± 2 | 258 ± 13 | 226 ± 13 | 0 % ± 0 | 9.8 ± 0.7 | 11.7 ± 0.5 | 0 |
| t4-final-skeleton-arco-base | 30 | 87 % ± 6 | 13 % ± 6 | 5.3 ± 0.5 | 1.1 ± 0.5 | 31 ± 1 | 760 ± 10 | 142 ± 5 | 96 % ± 0 | 10.7 ± 0.1 | 4.9 ± 0.1 | 0 |
| t4-final-skeleton-arco-comidamochila | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 36 ± 3 | 756 ± 8 | 157 ± 8 | 95 % ± 0 | 10.3 ± 0.2 | 5.2 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t4-final-skeleton-mele-base semilla 1: sprint → red (0/8 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 177 de 177 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t4-final-skeleton-mele-base: enemy:arrow=63
- t4-final-skeleton-mele-comidamochila: enemy:arrow=1
- t4-final-skeleton-mele-comidamano: enemy:arrow=14
- t4-final-skeleton-escudo-base: enemy:arrow=209
- t4-final-skeleton-arco-base: enemy:arrow=80
- t4-final-skeleton-arco-comidamochila: enemy:arrow=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 42, ENGAGE 6629, LOW_HEALTH_RETREAT 566, MORALE_SHAKEN 4596, MORALE_WAVERING 1636, NONE 19, RALLIED 65, REFUSE_ATTACK 1379, ROUTED_FLEE 948, SEEK_ENEMY 530, TOO_CLOSE 59

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t4-final-skeleton-escudo-comidamochila: 24/30 corridas llegaron al tope de tiempo
- t4-final-skeleton-mele-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
