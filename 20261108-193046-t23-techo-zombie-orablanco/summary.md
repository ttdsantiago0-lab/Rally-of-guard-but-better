# Simulación Rally - lote 20261108-193046-t23-techo-zombie-orablanco

Preset `t23-techo-zombie-orablanco` · velocidad `sprint` · 61 corridas · 2 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=9}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t23-techo-zombie-arco-orablanco-r40 | 30 | 70 % ± 9 | 30 % ± 9 | 4.0 ± 0.5 | 4.5 ± 1.4 | 50 ± 2 | 1000 ± 17 | 130 ± 8 | 94 % ± 1 | 8.0 ± 0.5 | 9.1 ± 0.6 | 0 |
| t23-techo-zombie-linea-orablanco-r38 | 30 | 100 % ± 0 | 0 % ± 0 | 5.6 ± 0.2 | 0.0 ± 0.0 | 42 ± 1 | 966 ± 5 | 82 ± 3 | 97 % ± 1 | 5.8 ± 0.3 | 8.8 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t23-techo-zombie-arco-orablanco-r40 semilla 1: sprint → red (0/15 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 51 de 51 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t23-techo-zombie-arco-orablanco-r40: enemy:mob=53
- t23-techo-zombie-linea-orablanco-r38: enemy:mob=39

## Motivos de decisión (cambios registrados): COUNTERATTACK 2, ENGAGE 1451, LOW_HEALTH_RETREAT 584, MORALE_SHAKEN 261, MORALE_WAVERING 183, NONE 12, OUT_OF_RANGE 8, RALLIED 3, REFUSE_ATTACK 11, ROUTED_FLEE 68, SEEK_ENEMY 187, TOO_CLOSE 1127

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t23-techo-zombie-arco-orablanco-r40: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
