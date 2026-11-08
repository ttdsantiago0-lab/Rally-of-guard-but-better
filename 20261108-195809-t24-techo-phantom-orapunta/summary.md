# Simulación Rally - lote 20261108-195809-t24-techo-phantom-orapunta

Preset `t24-techo-phantom-orapunta` · velocidad `sprint` · 113 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t24-techo-phantom-arco-orapunta-r20 | 18 | 100 % ± 0 | 0 % ± 0 | 7.2 ± 0.1 | 0.0 ± 0.0 | 45 ± 2 | 488 ± 3 | 43 ± 2 | 45 % ± 2 | 15.9 ± 1.9 | 5.5 ± 0.3 | 8 |
| t24-techo-phantom-arco-orapunta-r35 | 19 | 100 % ± 0 | 0 % ± 0 | 5.6 ± 0.1 | 0.0 ± 0.0 | 62 ± 2 | 847 ± 3 | 99 ± 3 | 54 % ± 2 | 11.6 ± 0.9 | 7.3 ± 0.5 | 5 |
| t24-techo-phantom-linea-orapunta-r20 | 17 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.1 | 0.0 ± 0.0 | 52 ± 1 | 471 ± 3 | 43 ± 1 | 46 % ± 2 | 14.6 ± 1.0 | 8.5 ± 0.7 | 12 |
| t24-techo-phantom-linea-orapunta-r35 | 15 | 100 % ± 0 | 0 % ± 0 | 5.9 ± 0.3 | 0.0 ± 0.0 | 76 ± 3 | 824 ± 3 | 93 ± 5 | 52 % ± 3 | 13.5 ± 1.3 | 11.7 ± 0.9 | 18 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t24-techo-phantom-arco-orapunta-r20 semilla 1: sprint → blue (8/0 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 69 de 69 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t24-techo-phantom-arco-orapunta-r20: enemy:mob=15
- t24-techo-phantom-arco-orapunta-r35: enemy:mob=45
- t24-techo-phantom-linea-orapunta-r20: enemy:mob=10
- t24-techo-phantom-linea-orapunta-r35: enemy:mob=31

## Motivos de decisión (cambios registrados): COUNTERATTACK 21, ENGAGE 4017, LOW_HEALTH_RETREAT 490, MORALE_SHAKEN 596, MORALE_WAVERING 299, NONE 95, OUT_OF_RANGE 519, RALLIED 22, REFUSE_ATTACK 80, ROUTED_FLEE 117, SEEK_ENEMY 327, STUCK 74, TOO_CLOSE 2225

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t24-techo-phantom-linea-orapunta-r20 - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t24-techo-phantom-linea-orapunta-r35 - 18 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t24-techo-phantom-arco-orapunta-r20 - 8 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t24-techo-phantom-arco-orapunta-r35 - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
