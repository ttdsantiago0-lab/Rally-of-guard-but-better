# Simulación Rally - lote 20261108-202454-t27-kite-skeleton-sinkite

Preset `t27-kite-skeleton-sinkite` · velocidad `sprint` · 41 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-kite-skeleton-arco-sinkite-r68 | 20 | 90 % ± 7 | 10 % ± 7 | 4.8 ± 0.6 | 0.6 ± 0.5 | 39 ± 2 | 953 ± 11 | 158 ± 5 | 97 % ± 0 | 15.1 ± 0.3 | 7.2 ± 0.6 | 0 |
| t27-kite-skeleton-linea-sinkite-r150 | 20 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.1 | 0.0 ± 0.0 | 47 ± 2 | 774 ± 14 | 91 ± 5 | 87 % ± 1 | 14.0 ± 0.5 | 10.7 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-kite-skeleton-arco-sinkite-r68 semilla 1: sprint → red (0/9 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 38 de 38 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-kite-skeleton-arco-sinkite-r68: enemy:arrow=65
- t27-kite-skeleton-linea-sinkite-r150: enemy:arrow=8

## Motivos de decisión (cambios registrados): ENGAGE 469, LOW_HEALTH_RETREAT 453, MORALE_SHAKEN 168, MORALE_WAVERING 73, NONE 18, OUT_OF_RANGE 26, REFUSE_ATTACK 1, ROUTED_FLEE 18, SEEK_ENEMY 258, TOO_CLOSE 18

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t27-kite-skeleton-arco-sinkite-r68: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
