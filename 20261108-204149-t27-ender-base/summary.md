# Simulación Rally - lote 20261108-204149-t27-ender-base

Preset `t27-ender-base` · velocidad `sprint` · 81 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-ender-linea-base-r3 | 20 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.1 | 0.0 ± 0.0 | 20 ± 2 | 126 ± 0 | 31 ± 2 | 0 % ± 0 | 15.0 ± 1.9 | 11.2 ± 1.3 | 0 |
| t27-ender-linea-base-r6 | 20 | 45 % ± 11 | 55 % ± 11 | 2.1 ± 0.6 | 1.7 ± 0.4 | 78 ± 9 | 213 ± 11 | 155 ± 10 | 0 % ± 0 | 18.4 ± 1.5 | 16.9 ± 1.1 | 0 |
| t27-ender-escudo-base-r3 | 20 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 12 ± 0 | 126 ± 0 | 15 ± 2 | 0 % ± 0 | 4.8 ± 0.1 | 3.3 ± 0.1 | 0 |
| t27-ender-arco-base-r3 | 20 | 18 % ± 5 | 83 % ± 5 | 0.7 ± 0.2 | 3.0 ± 0.0 | 155 ± 6 | 0 ± 0 | 179 ± 4 | 0 % ± 0 | 28.8 ± 1.4 | 21.8 ± 0.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-ender-linea-base-r3 semilla 1001: sprint → blue (7/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 56 de 56 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-ender-linea-base-r3: enemy:mob=10
- t27-ender-linea-base-r6: enemy:mob=118
- t27-ender-escudo-base-r3: enemy:mob=2
- t27-ender-arco-base-r3: enemy:mob=147

## Motivos de decisión (cambios registrados): COUNTERATTACK 16, ENGAGE 400, LOW_HEALTH_RETREAT 147, MORALE_SHAKEN 348, MORALE_WAVERING 186, NONE 694, RALLIED 7, REFUSE_ATTACK 1, ROUTED_FLEE 75, SEEK_ENEMY 1052, STUCK 3

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
