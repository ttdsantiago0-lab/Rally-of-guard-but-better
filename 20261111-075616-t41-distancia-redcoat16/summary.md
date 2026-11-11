# Simulación Rally - lote 20261111-075616-t41-distancia-redcoat16

Preset `t41-distancia-redcoat16` · velocidad `sprint` · 81 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t41-distancia-redcoat16-linea-regla | 10 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 22 ± 1 | 351 ± 6 | 32 ± 5 | 75 % ± 2 | 17.4 ± 0.3 | 8.5 ± 0.1 | 0 |
| t41-distancia-redcoat16-linea-sinregla | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 23 ± 1 | 358 ± 6 | 37 ± 4 | 75 % ± 1 | 18.1 ± 0.3 | 9.4 ± 0.3 | 0 |
| t41-distancia-redcoat16-arco-regla | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 16 ± 1 | 377 ± 3 | 27 ± 8 | 73 % ± 1 | 24.6 ± 0.7 | 5.1 ± 0.1 | 0 |
| t41-distancia-redcoat16-arco-sinregla | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 17 ± 1 | 382 ± 4 | 22 ± 6 | 71 % ± 2 | 25.4 ± 0.8 | 5.1 ± 0.1 | 0 |
| t41-distancia-redcoat16-lineascutum-regla | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 24 ± 1 | 361 ± 9 | 36 ± 4 | 78 % ± 2 | 18.1 ± 0.5 | 8.6 ± 0.4 | 0 |
| t41-distancia-redcoat16-lineascutum-sinregla | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 22 ± 1 | 349 ± 7 | 37 ± 4 | 79 % ± 1 | 17.4 ± 0.3 | 8.2 ± 0.3 | 0 |
| t41-distancia-redcoat16-escudo-regla | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 48 ± 2 | 270 ± 9 | 59 ± 8 | 0 % ± 0 | 9.8 ± 0.7 | 8.8 ± 0.7 | 0 |
| t41-distancia-redcoat16-escudo-sinregla | 10 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 46 ± 2 | 275 ± 10 | 66 ± 6 | 0 % ± 0 | 8.6 ± 0.3 | 7.1 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t41-distancia-redcoat16-linea-regla semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 80 de 80 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t41-distancia-redcoat16-linea-regla: enemy:arrow=2
- t41-distancia-redcoat16-escudo-sinregla: enemy:arrow=2

## Motivos de decisión (cambios registrados): ENGAGE 713, LOW_HEALTH_RETREAT 41, NONE 12, OUT_OF_RANGE 11, SEEK_ENEMY 135, STUCK 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
