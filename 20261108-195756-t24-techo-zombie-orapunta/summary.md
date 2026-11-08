# Simulación Rally - lote 20261108-195756-t24-techo-zombie-orapunta

Preset `t24-techo-zombie-orapunta` · velocidad `sprint` · 81 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=69}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t24-techo-zombie-arco-orapunta-r60 | 20 | 55 % ± 11 | 45 % ± 11 | 2.7 ± 0.6 | 11.9 ± 3.4 | 61 ± 3 | 1369 ± 55 | 150 ± 8 | 98 % ± 0 | 7.1 ± 0.6 | 9.0 ± 0.7 | 0 |
| t24-techo-zombie-arco-orapunta-r85 | 20 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 52.6 ± 2.7 | 61 ± 4 | 1193 ± 59 | 186 ± 2 | 99 % ± 0 | 5.6 ± 0.3 | 10.0 ± 0.7 | 0 |
| t24-techo-zombie-linea-orapunta-r55 | 20 | 10 % ± 7 | 90 % ± 7 | 0.6 ± 0.4 | 25.7 ± 2.6 | 50 ± 2 | 1041 ± 40 | 179 ± 5 | 99 % ± 0 | 4.3 ± 0.3 | 9.8 ± 0.5 | 0 |
| t24-techo-zombie-linea-orapunta-r75 | 20 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 53.6 ± 2.1 | 46 ± 3 | 914 ± 40 | 184 ± 2 | 99 % ± 0 | 4.1 ± 0.2 | 10.6 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t24-techo-zombie-arco-orapunta-r60 semilla 1: sprint → blue (5/0 vivos), normal → red (0/36 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 13 de 13 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t24-techo-zombie-arco-orapunta-r60: enemy:mob=54
- t24-techo-zombie-arco-orapunta-r85: enemy:mob=89
- t24-techo-zombie-linea-orapunta-r55: enemy:mob=71
- t24-techo-zombie-linea-orapunta-r75: enemy:mob=86

## Motivos de decisión (cambios registrados): COUNTERATTACK 19, ENGAGE 2340, LOW_HEALTH_RETREAT 986, MORALE_SHAKEN 625, MORALE_WAVERING 468, NONE 4, OUT_OF_RANGE 17, RALLIED 4, REFUSE_ATTACK 34, ROUTED_FLEE 202, SEEK_ENEMY 193, TOO_CLOSE 2058

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t24-techo-zombie-arco-orapunta-r60: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
