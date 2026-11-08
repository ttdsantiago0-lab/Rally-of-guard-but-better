# Simulación Rally - lote 20261108-193124-t23-techo-zombie-orapunta

Preset `t23-techo-zombie-orapunta` · velocidad `sprint` · 61 corridas · 2 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=7}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t23-techo-zombie-arco-orapunta-r40 | 30 | 97 % ± 3 | 3 % ± 3 | 6.6 ± 0.3 | 0.7 ± 0.7 | 35 ± 1 | 1007 ± 9 | 88 ± 6 | 95 % ± 0 | 7.6 ± 0.3 | 7.6 ± 0.5 | 0 |
| t23-techo-zombie-linea-orapunta-r38 | 30 | 87 % ± 6 | 13 % ± 6 | 5.1 ± 0.4 | 1.1 ± 0.5 | 44 ± 2 | 957 ± 6 | 115 ± 7 | 96 % ± 0 | 6.4 ± 0.3 | 10.0 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t23-techo-zombie-arco-orapunta-r40 semilla 1: sprint → blue (7/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 55 de 55 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t23-techo-zombie-arco-orapunta-r40: enemy:mob=19
- t23-techo-zombie-linea-orapunta-r38: enemy:mob=40

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 1584, LOW_HEALTH_RETREAT 415, MORALE_SHAKEN 208, MORALE_WAVERING 135, NONE 13, OUT_OF_RANGE 12, REFUSE_ATTACK 6, ROUTED_FLEE 42, SEEK_ENEMY 136, TOO_CLOSE 1247

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
