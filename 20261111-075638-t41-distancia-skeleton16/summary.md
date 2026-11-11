# Simulación Rally - lote 20261111-075638-t41-distancia-skeleton16

Preset `t41-distancia-skeleton16` · velocidad `sprint` · 81 corridas · 8 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t41-distancia-skeleton16-linea-regla | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 21 ± 1 | 342 ± 5 | 8 ± 2 | 77 % ± 1 | 16.7 ± 0.3 | 8.5 ± 0.2 | 0 |
| t41-distancia-skeleton16-linea-sinregla | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 19 ± 1 | 347 ± 4 | 6 ± 2 | 80 % ± 2 | 17.0 ± 0.2 | 8.1 ± 0.2 | 0 |
| t41-distancia-skeleton16-arco-regla | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 13 ± 0 | 375 ± 4 | 14 ± 2 | 77 % ± 2 | 21.3 ± 0.5 | 5.1 ± 0.1 | 0 |
| t41-distancia-skeleton16-arco-sinregla | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 13 ± 0 | 367 ± 3 | 11 ± 2 | 76 % ± 2 | 21.0 ± 0.5 | 5.1 ± 0.1 | 0 |
| t41-distancia-skeleton16-lineascutum-regla | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 22 ± 1 | 349 ± 3 | 10 ± 2 | 77 % ± 1 | 16.7 ± 0.2 | 8.5 ± 0.2 | 0 |
| t41-distancia-skeleton16-lineascutum-sinregla | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 22 ± 1 | 351 ± 3 | 8 ± 2 | 79 % ± 2 | 17.4 ± 0.3 | 8.6 ± 0.3 | 0 |
| t41-distancia-skeleton16-escudo-regla | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 106 ± 9 | 259 ± 4 | 68 ± 12 | 0 % ± 0 | 11.9 ± 0.8 | 17.8 ± 2.2 | 0 |
| t41-distancia-skeleton16-escudo-sinregla | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 112 ± 8 | 268 ± 5 | 67 ± 10 | 0 % ± 0 | 15.2 ± 1.9 | 18.6 ± 2.0 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t41-distancia-skeleton16-linea-regla semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 80 de 80 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 713, NONE 65, SEEK_ENEMY 133, STUCK 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
