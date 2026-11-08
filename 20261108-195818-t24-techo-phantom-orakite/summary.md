# Simulación Rally - lote 20261108-195818-t24-techo-phantom-orakite

Preset `t24-techo-phantom-orakite` · velocidad `sprint` · 142 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t24-techo-phantom-arco-orakite-r20 | 12 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 36 ± 1 | 479 ± 5 | 9 ± 1 | 54 % ± 1 | 16.4 ± 0.8 | 17.8 ± 1.7 | 17 |
| t24-techo-phantom-linea-orakite-r20 | 9 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 49 ± 3 | 477 ± 2 | 17 ± 3 | 56 % ± 3 | 18.5 ± 2.0 | 19.8 ± 2.3 | 26 |
| t24-techo-phantom-arco-orakite-r35 | 3 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.3 | 0.0 ± 0.0 | 61 ± 7 | 843 ± 14 | 32 ± 6 | 54 % ± 5 | 17.8 ± 2.9 | 24.1 ± 1.0 | 34 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t24-techo-phantom-arco-orakite-r20 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 24 de 24 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t24-techo-phantom-arco-orakite-r35: enemy:mob=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 34, ENGAGE 5170, LOW_HEALTH_RETREAT 150, MORALE_SHAKEN 941, MORALE_WAVERING 346, NONE 344, OUT_OF_RANGE 1416, RALLIED 46, REFUSE_ATTACK 92, ROUTED_FLEE 143, SEEK_ENEMY 1013, STUCK 190, TOO_CLOSE 1847

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t24-techo-phantom-arco-orakite-r35 - 34 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t24-techo-phantom-linea-orakite-r20 - 26 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t24-techo-phantom-linea-orakite-r35 - 40 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t24-techo-phantom-arco-orakite-r20 - 17 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
