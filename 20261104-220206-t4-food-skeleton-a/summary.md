# Simulación Rally - lote 20261104-220206-t4-food-skeleton-a

Preset `t4-food-skeleton-a` · velocidad `sprint` · 211 corridas · 7 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t4-final-skeleton-mele-base | 30 | 70 % ± 9 | 30 % ± 9 | 4.7 ± 0.6 | 1.9 ± 0.6 | 38 ± 2 | 472 ± 10 | 142 ± 7 | 0 % ± 0 | 7.0 ± 0.2 | 9.1 ± 0.3 | 0 |
| t4-final-skeleton-mele-comidamochila | 30 | 98 % ± 2 | 2 % ± 2 | 7.8 ± 0.2 | 0.1 ± 0.1 | 35 ± 3 | 498 ± 6 | 129 ± 9 | 0 % ± 0 | 7.4 ± 0.3 | 9.4 ± 0.3 | 0 |
| t4-final-skeleton-mele-comidamano | 30 | 93 % ± 3 | 7 % ± 3 | 7.7 ± 0.1 | 0.2 ± 0.1 | 57 ± 6 | 482 ± 6 | 207 ± 20 | 0 % ± 0 | 8.4 ± 0.4 | 10.2 ± 0.4 | 0 |
| t4-final-skeleton-escudo-base | 30 | 18 % ± 6 | 82 % ± 6 | 0.9 ± 0.3 | 5.5 ± 0.6 | 91 ± 4 | 199 ± 11 | 195 ± 5 | 0 % ± 0 | 7.4 ± 0.3 | 8.0 ± 0.4 | 0 |
| t4-final-skeleton-escudo-comidamochila | 30 | 63 % ± 4 | 37 % ± 4 | 8.0 ± 0.0 | 1.9 ± 0.3 | 112 ± 3 | 274 ± 12 | 207 ± 13 | 0 % ± 0 | 10.1 ± 0.4 | 11.3 ± 0.6 | 0 |
| t4-final-skeleton-arco-base | 30 | 67 % ± 9 | 33 % ± 9 | 4.4 ± 0.7 | 2.2 ± 0.7 | 32 ± 2 | 743 ± 12 | 147 ± 7 | 95 % ± 1 | 10.3 ± 0.2 | 4.4 ± 0.1 | 0 |
| t4-final-skeleton-arco-comidamochila | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.0 | 0.0 ± 0.0 | 33 ± 2 | 778 ± 7 | 148 ± 9 | 95 % ± 1 | 10.1 ± 0.2 | 5.1 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t4-final-skeleton-mele-base semilla 1001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 170 de 170 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t4-final-skeleton-mele-base: enemy:arrow=99
- t4-final-skeleton-mele-comidamochila: enemy:arrow=5
- t4-final-skeleton-mele-comidamano: enemy:arrow=9
- t4-final-skeleton-escudo-base: enemy:arrow=213
- t4-final-skeleton-arco-base: enemy:arrow=109
- t4-final-skeleton-arco-comidamochila: enemy:arrow=2

## Motivos de decisión (cambios registrados): COUNTERATTACK 34, ENGAGE 6467, LOW_HEALTH_RETREAT 523, MORALE_SHAKEN 4410, MORALE_WAVERING 1686, NONE 34, RALLIED 50, REFUSE_ATTACK 1380, ROUTED_FLEE 959, SEEK_ENEMY 502, TOO_CLOSE 48, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t4-final-skeleton-escudo-comidamochila: 22/30 corridas llegaron al tope de tiempo
