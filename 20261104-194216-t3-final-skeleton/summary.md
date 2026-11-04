# Simulación Rally - lote 20261104-194216-t3-final-skeleton

Preset `t3-final-skeleton` · velocidad `sprint` · 151 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t3-final-skeleton-mele-base | 30 | 70 % ± 9 | 30 % ± 9 | 4.8 ± 0.6 | 1.1 ± 0.4 | 42 ± 3 | 485 ± 6 | 142 ± 7 | 0 % ± 0 | 7.2 ± 0.2 | 8.7 ± 0.3 | 0 |
| t3-final-skeleton-mele-circulo | 30 | 87 % ± 6 | 13 % ± 6 | 6.3 ± 0.5 | 0.6 ± 0.3 | 35 ± 3 | 472 ± 4 | 124 ± 7 | 0 % ± 0 | 7.0 ± 0.3 | 7.7 ± 0.3 | 0 |
| t3-final-skeleton-escudo-base | 30 | 32 % ± 7 | 68 % ± 7 | 2.7 ± 0.6 | 4.7 ± 0.7 | 95 ± 4 | 255 ± 15 | 170 ± 9 | 0 % ± 0 | 8.3 ± 0.6 | 9.9 ± 0.7 | 0 |
| t3-final-skeleton-escudo-escudoarriba | 30 | 55 % ± 3 | 45 % ± 3 | 5.3 ± 0.4 | 2.7 ± 0.3 | 115 ± 3 | 218 ± 12 | 161 ± 7 | 0 % ± 0 | 7.5 ± 0.2 | 7.2 ± 0.5 | 0 |
| t3-final-skeleton-escudo-circulo | 30 | 58 % ± 7 | 42 % ± 7 | 4.3 ± 0.6 | 1.6 ± 0.3 | 99 ± 5 | 264 ± 12 | 148 ± 11 | 0 % ± 0 | 9.0 ± 0.3 | 8.5 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t3-final-skeleton-mele-base semilla 1: sprint → blue (4/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 116 de 116 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t3-final-skeleton-mele-base: enemy:arrow=97
- t3-final-skeleton-mele-circulo: enemy:arrow=52
- t3-final-skeleton-escudo-base: enemy:arrow=160
- t3-final-skeleton-escudo-escudoarriba: enemy:arrow=82
- t3-final-skeleton-escudo-circulo: enemy:arrow=110

## Motivos de decisión (cambios registrados): COUNTERATTACK 51, ENGAGE 5326, LOW_HEALTH_RETREAT 435, MORALE_SHAKEN 4057, MORALE_WAVERING 1262, NONE 22, RALLIED 38, REFUSE_ATTACK 1482, ROUTED_FLEE 860, SEEK_ENEMY 760, UNIT_BROKEN 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t3-final-skeleton-escudo-escudoarriba: 27/30 corridas llegaron al tope de tiempo
