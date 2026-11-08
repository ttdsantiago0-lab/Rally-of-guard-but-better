# Simulación Rally - lote 20261108-202421-t27-ender-base

Preset `t27-ender-base` · velocidad `sprint` · 81 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-ender-linea-base-r3 | 20 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.1 | 0.0 ± 0.0 | 15 ± 1 | 126 ± 0 | 27 ± 2 | 0 % ± 0 | 10.1 ± 1.0 | 8.1 ± 0.7 | 0 |
| t27-ender-linea-base-r6 | 20 | 60 % ± 11 | 40 % ± 11 | 3.0 ± 0.6 | 1.4 ± 0.4 | 63 ± 6 | 219 ± 10 | 136 ± 10 | 0 % ± 0 | 22.4 ± 1.6 | 21.6 ± 0.9 | 0 |
| t27-ender-escudo-base-r3 | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 11 ± 0 | 126 ± 0 | 12 ± 1 | 0 % ± 0 | 4.9 ± 0.1 | 3.4 ± 0.2 | 0 |
| t27-ender-arco-base-r3 | 20 | 35 % ± 5 | 65 % ± 5 | 1.3 ± 0.2 | 3.0 ± 0.0 | 168 ± 5 | 0 ± 0 | 166 ± 4 | 0 % ± 0 | 32.3 ± 1.3 | 22.7 ± 0.9 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-ender-linea-base-r3 semilla 1: sprint → blue (7/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 66 de 66 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-ender-linea-base-r3: enemy:mob=7
- t27-ender-linea-base-r6: enemy:mob=101
- t27-ender-arco-base-r3: enemy:mob=135

## Motivos de decisión (cambios registrados): COUNTERATTACK 13, ENGAGE 386, LOW_HEALTH_RETREAT 105, MORALE_SHAKEN 304, MORALE_WAVERING 132, NONE 762, RALLIED 3, ROUTED_FLEE 62, SEEK_ENEMY 1046, STUCK 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t27-ender-arco-base-r3: 14/20 corridas llegaron al tope de tiempo
