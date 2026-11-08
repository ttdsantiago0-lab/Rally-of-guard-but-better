# Simulación Rally - lote 20261108-191745-t17-escolta-mezcla

Preset `t17-escolta-mezcla` · velocidad `sprint` · 361 corridas · 12 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t17-escolta-mezcla-linea-defender | 30 | 67 % ± 4 | 33 % ± 4 | 8.0 ± 0.0 | 1.6 ± 0.3 | 64 ± 1 | 786 ± 12 | 11 ± 1 | 88 % ± 0 | 14.5 ± 0.1 | 6.2 ± 0.1 | 0 |
| t17-escolta-mezcla-linea-sinpantalla | 30 | 65 % ± 4 | 35 % ± 4 | 8.0 ± 0.0 | 1.8 ± 0.3 | 58 ± 2 | 728 ± 24 | 12 ± 1 | 89 % ± 1 | 14.8 ± 0.1 | 6.5 ± 0.1 | 0 |
| t17-escolta-mezcla-linea-antes | 30 | 67 % ± 4 | 33 % ± 4 | 8.0 ± 0.0 | 1.8 ± 0.3 | 49 ± 2 | 648 ± 27 | 5 ± 1 | 87 % ± 0 | 14.7 ± 0.2 | 7.0 ± 0.1 | 0 |
| t17-escolta-mezcla-linea-atacar | 30 | 68 % ± 4 | 32 % ± 4 | 8.0 ± 0.0 | 1.7 ± 0.3 | 50 ± 2 | 649 ± 16 | 11 ± 1 | 81 % ± 1 | 11.6 ± 0.2 | 7.5 ± 0.1 | 0 |
| t17-escolta-mezcla-escudo-defender | 30 | 52 % ± 2 | 48 % ± 2 | 8.0 ± 0.0 | 5.0 ± 0.5 | 56 ± 2 | 417 ± 5 | 32 ± 2 | 0 % ± 0 | 10.5 ± 0.1 | 3.8 ± 0.1 | 0 |
| t17-escolta-mezcla-escudo-sinpantalla | 30 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 8.3 ± 0.4 | 54 ± 2 | 346 ± 5 | 34 ± 2 | 0 % ± 0 | 12.2 ± 0.2 | 3.9 ± 0.1 | 0 |
| t17-escolta-mezcla-escudo-antes | 30 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 10.1 ± 0.5 | 46 ± 1 | 316 ± 5 | 27 ± 2 | 0 % ± 0 | 11.3 ± 0.5 | 4.3 ± 0.2 | 0 |
| t17-escolta-mezcla-escudo-atacar | 30 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 9.4 ± 0.3 | 39 ± 0 | 368 ± 6 | 17 ± 1 | 0 % ± 0 | 6.2 ± 0.1 | 4.3 ± 0.1 | 0 |
| t17-escolta-mezcla-arco-defender | 30 | 95 % ± 3 | 5 % ± 3 | 8.0 ± 0.0 | 0.1 ± 0.1 | 61 ± 2 | 830 ± 14 | 8 ± 1 | 80 % ± 0 | 14.2 ± 0.2 | 3.3 ± 0.1 | 0 |
| t17-escolta-mezcla-arco-sinpantalla | 30 | 93 % ± 3 | 7 % ± 3 | 8.0 ± 0.0 | 0.3 ± 0.2 | 60 ± 2 | 821 ± 16 | 8 ± 1 | 80 % ± 1 | 14.2 ± 0.2 | 3.4 ± 0.1 | 0 |
| t17-escolta-mezcla-arco-antes | 30 | 88 % ± 4 | 12 % ± 4 | 8.0 ± 0.0 | 0.5 ± 0.2 | 56 ± 2 | 795 ± 22 | 4 ± 1 | 80 % ± 1 | 14.9 ± 0.1 | 4.7 ± 0.2 | 0 |
| t17-escolta-mezcla-arco-atacar | 30 | 63 % ± 4 | 37 % ± 4 | 8.0 ± 0.0 | 1.8 ± 0.3 | 49 ± 2 | 643 ± 14 | 9 ± 1 | 75 % ± 1 | 15.7 ± 0.2 | 3.5 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t17-escolta-mezcla-linea-defender semilla 1: sprint → blue (8/0 vivos), normal → draw (8/4 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 360 de 360 corridas ✓

## Motivos de decisión (cambios registrados): COUNTERATTACK 5, ENGAGE 8996, FORMATION_HOLD 5304, LOW_HEALTH_RETREAT 1, MORALE_SHAKEN 16, MORALE_WAVERING 4, NONE 658, OUT_OF_RANGE 23, RALLIED 1, ROUTED_FLEE 7, SEEK_ENEMY 5731, STUCK 8, TOO_CLOSE 1324

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t17-escolta-mezcla-linea-defender: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
