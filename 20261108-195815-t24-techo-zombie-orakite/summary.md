# Simulación Rally - lote 20261108-195815-t24-techo-zombie-orakite

Preset `t24-techo-zombie-orakite` · velocidad `sprint` · 83 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t24-techo-zombie-arco-orakite-r60 | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 44 ± 0 | 1466 ± 4 | 1 ± 0 | 96 % ± 0 | 9.4 ± 0.3 | 7.3 ± 0.5 | 0 |
| t24-techo-zombie-arco-orakite-r85 | 20 | 100 % ± 0 | 0 % ± 0 | 7.5 ± 0.1 | 0.0 ± 0.0 | 66 ± 1 | 2083 ± 4 | 26 ± 5 | 95 % ± 1 | 11.3 ± 0.9 | 10.5 ± 1.0 | 0 |
| t24-techo-zombie-linea-orakite-r55 | 20 | 100 % ± 0 | 0 % ± 0 | 5.4 ± 0.3 | 0.0 ± 0.0 | 59 ± 2 | 1393 ± 8 | 82 ± 5 | 95 % ± 0 | 8.8 ± 0.3 | 14.1 ± 0.6 | 0 |
| t24-techo-zombie-linea-orakite-r75 | 19 | 87 % ± 5 | 13 % ± 5 | 4.1 ± 0.4 | 2.5 ± 1.0 | 111 ± 10 | 1863 ± 10 | 113 ± 7 | 96 % ± 1 | 10.4 ± 0.8 | 17.1 ± 1.7 | 3 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t24-techo-zombie-arco-orakite-r60 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 79 de 79 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t24-techo-zombie-arco-orakite-r85: enemy:mob=6
- t24-techo-zombie-linea-orakite-r55: enemy:mob=24
- t24-techo-zombie-linea-orakite-r75: enemy:mob=38

## Motivos de decisión (cambios registrados): COUNTERATTACK 13, ENGAGE 1189, LOW_HEALTH_RETREAT 335, MORALE_SHAKEN 318, MORALE_WAVERING 220, NONE 29, OUT_OF_RANGE 153, RALLIED 12, REFUSE_ATTACK 19, ROUTED_FLEE 87, SEEK_ENEMY 80, TOO_CLOSE 227

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t24-techo-zombie-linea-orakite-r75 - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
