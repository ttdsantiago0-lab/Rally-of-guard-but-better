# Simulación Rally - lote 20261108-193301-t23-techo-zombie-base

Preset `t23-techo-zombie-base` · velocidad `sprint` · 61 corridas · 2 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=3}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t23-techo-zombie-arco-base-r40 | 30 | 97 % ± 3 | 3 % ± 3 | 6.6 ± 0.3 | 0.2 ± 0.2 | 36 ± 2 | 1018 ± 6 | 92 ± 6 | 95 % ± 0 | 7.4 ± 0.3 | 7.4 ± 0.3 | 0 |
| t23-techo-zombie-linea-base-r38 | 30 | 93 % ± 5 | 7 % ± 5 | 5.5 ± 0.4 | 0.7 ± 0.5 | 43 ± 2 | 959 ± 8 | 110 ± 6 | 96 % ± 1 | 6.4 ± 0.3 | 9.5 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t23-techo-zombie-arco-base-r40 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 57 de 57 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t23-techo-zombie-arco-base-r40: enemy:mob=22
- t23-techo-zombie-linea-base-r38: enemy:mob=44

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 1525, LOW_HEALTH_RETREAT 437, MORALE_SHAKEN 192, MORALE_WAVERING 115, NONE 9, OUT_OF_RANGE 9, REFUSE_ATTACK 6, ROUTED_FLEE 27, SEEK_ENEMY 166, TOO_CLOSE 1183

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
