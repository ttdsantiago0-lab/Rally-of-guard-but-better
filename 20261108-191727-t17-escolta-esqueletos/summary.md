# Simulación Rally - lote 20261108-191727-t17-escolta-esqueletos

Preset `t17-escolta-esqueletos` · velocidad `sprint` · 361 corridas · 12 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-esqueletos-linea-defender | 30 | 77 % ± 5 | 23 % ± 5 | 8.0 ± 0.0 | 2.2 ± 0.6 | 56 ± 2 | 554 ± 25 | 16 ± 2 | 82 % ± 1 | 19.3 ± 0.2 | 4.6 ± 0.1 | 0 |
| t17-escolta-esqueletos-linea-sinpantalla | 30 | 53 % ± 2 | 47 % ± 2 | 8.0 ± 0.0 | 5.4 ± 0.5 | 35 ± 2 | 329 ± 23 | 12 ± 1 | 82 % ± 1 | 21.5 ± 0.2 | 4.7 ± 0.2 | 0 |
| t17-escolta-esqueletos-linea-antes | 30 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 6.5 ± 0.4 | 19 ± 2 | 153 ± 16 | 1 ± 0 | 73 % ± 2 | 27.5 ± 0.7 | 3.9 ± 0.2 | 0 |
| t17-escolta-esqueletos-linea-atacar | 30 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 6.3 ± 0.4 | 15 ± 1 | 122 ± 11 | 3 ± 1 | 75 % ± 1 | 26.1 ± 0.7 | 5.1 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-defender | 30 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 6.1 ± 0.6 | 38 ± 3 | 120 ± 9 | 15 ± 2 | 0 % ± 0 | 12.0 ± 0.2 | 3.6 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-sinpantalla | 30 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 7.6 ± 0.4 | 25 ± 1 | 62 ± 6 | 13 ± 2 | 0 % ± 0 | 14.3 ± 0.3 | 3.5 ± 0.0 | 0 |
| t17-escolta-esqueletos-escudo-antes | 30 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 9.2 ± 0.2 | 21 ± 1 | 37 ± 4 | 5 ± 1 | 0 % ± 0 | 15.5 ± 0.4 | 3.1 ± 0.1 | 0 |
| t17-escolta-esqueletos-escudo-atacar | 30 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 7.6 ± 0.4 | 22 ± 1 | 71 ± 6 | 5 ± 1 | 0 % ± 0 | 12.3 ± 0.2 | 4.5 ± 0.1 | 0 |
| t17-escolta-esqueletos-arco-defender | 30 | 68 % ± 4 | 32 % ± 4 | 8.0 ± 0.0 | 3.2 ± 0.6 | 44 ± 3 | 499 ± 32 | 10 ± 1 | 73 % ± 1 | 25.6 ± 0.3 | 3.6 ± 0.1 | 0 |
| t17-escolta-esqueletos-arco-sinpantalla | 30 | 65 % ± 4 | 35 % ± 4 | 8.0 ± 0.0 | 3.8 ± 0.6 | 45 ± 3 | 504 ± 27 | 10 ± 1 | 74 % ± 1 | 25.8 ± 0.2 | 3.6 ± 0.1 | 0 |
| t17-escolta-esqueletos-arco-antes | 30 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 6.6 ± 0.5 | 15 ± 2 | 149 ± 20 | 1 ± 0 | 63 % ± 3 | 30.1 ± 1.7 | 3.3 ± 0.2 | 0 |
| t17-escolta-esqueletos-arco-atacar | 30 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 7.4 ± 0.3 | 9 ± 1 | 80 ± 11 | 1 ± 0 | 57 % ± 2 | 30.6 ± 1.9 | 4.9 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-esqueletos-linea-defender semilla 1: sprint → blue (8/0 vivos), normal → draw (8/6 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 360 de 360 corridas ✓

## Motivos de decisión (cambios registrados): ENGAGE 4634, FORMATION_HOLD 3322, NONE 43, OUT_OF_RANGE 92, SEEK_ENEMY 2196, STUCK 16, TOO_CLOSE 10

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t17-escolta-esqueletos-linea-defender: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
