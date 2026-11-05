# Simulación Rally - lote 20261105-140020-t10-moral

Preset `t10-moral` · velocidad `sprint` · 41 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t10-moral-linea-conmoral | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 291 ± 3 | 2127 ± 23 | 82 ± 3 | 81 % ± 1 | 12.0 ± 0.2 | 12.3 ± 0.2 | 0 |
| t10-moral-linea-sinmoral | 10 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 294 ± 3 | 2125 ± 25 | 83 ± 5 | 80 % ± 1 | 12.2 ± 0.2 | 12.4 ± 0.2 | 0 |
| t10-moral-arco-conmoral | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 279 ± 2 | 2412 ± 6 | 82 ± 4 | 77 % ± 1 | 13.8 ± 0.2 | 11.9 ± 0.3 | 0 |
| t10-moral-arco-sinmoral | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 277 ± 1 | 2405 ± 8 | 79 ± 3 | 78 % ± 1 | 13.9 ± 0.2 | 11.8 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t10-moral-linea-conmoral semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t10-moral-linea-sinmoral: enemy:explosion.player=1

## Motivos de decisión (cambios registrados): ENGAGE 4093, LOW_HEALTH_RETREAT 2, MORALE_SHAKEN 85, MORALE_WAVERING 17, NONE 2957, OUT_OF_RANGE 41, RALLIED 2, REFUSE_ATTACK 2, ROUTED_FLEE 3, SEEK_ENEMY 626, TOO_CLOSE 874

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
