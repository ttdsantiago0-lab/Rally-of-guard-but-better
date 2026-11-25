# Simulación Rally - lote 20261125-101042-t213-o8-partes

Preset `t213-o8-partes` · velocidad `sprint` · 31 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t213-400-arcos | 30 | 58 % ± 8 | 42 % ± 8 | 52.1 ± 7.7 | 29.8 ± 6.9 | 123 ± 9 | 4580 ± 97 | 4273 ± 117 | 67 % ± 1 | 46.6 ± 0.6 | 17.8 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t213-400-arcos semilla 17800: sprint → blue (103/0 vivos), normal → blue (77/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 22 de 22 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t213-400-arcos: enemy:arrow=4438

## Motivos de decisión (cambios registrados): ENGAGE 20845, LOW_HEALTH_RETREAT 11110, MORALE_SHAKEN 6191, MORALE_WAVERING 3970, NONE 10831, ORDER_OVERRIDE 523, OUT_OF_RANGE 23019, RALLIED 14, REFUSE_ATTACK 1182, ROUTED_FLEE 5460, SEEK_ENEMY 323, UNIT_BROKEN 2084

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
