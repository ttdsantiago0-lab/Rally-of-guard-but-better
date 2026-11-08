# Simulación Rally - lote 20261108-202453-t27-kite-zombie-base

Preset `t27-kite-zombie-base` · velocidad `sprint` · 85 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-kite-zombie-arco-base-r60 | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 44 ± 0 | 1475 ± 3 | 0 ± 0 | 96 % ± 0 | 9.0 ± 0.1 | 6.8 ± 0.4 | 0 |
| t27-kite-zombie-arco-base-r85 | 20 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.1 | 0.0 ± 0.0 | 65 ± 1 | 2088 ± 5 | 19 ± 3 | 95 % ± 1 | 11.9 ± 0.8 | 11.1 ± 0.9 | 0 |
| t27-kite-zombie-linea-base-r55 | 20 | 100 % ± 0 | 0 % ± 0 | 5.7 ± 0.3 | 0.0 ± 0.0 | 60 ± 3 | 1377 ± 7 | 73 ± 5 | 94 % ± 1 | 9.2 ± 0.5 | 13.3 ± 0.8 | 0 |
| t27-kite-zombie-linea-base-r75 | 19 | 89 % ± 5 | 11 % ± 5 | 3.4 ± 0.3 | 3.9 ± 2.3 | 111 ± 9 | 1846 ± 34 | 122 ± 6 | 97 % ± 1 | 10.4 ± 0.8 | 18.8 ± 1.5 | 5 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-kite-zombie-arco-base-r60 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 79 de 79 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-kite-zombie-arco-base-r85: enemy:mob=2
- t27-kite-zombie-linea-base-r55: enemy:mob=23
- t27-kite-zombie-linea-base-r75: enemy:mob=40

## Motivos de decisión (cambios registrados): COUNTERATTACK 15, ENGAGE 1302, LOW_HEALTH_RETREAT 320, MORALE_SHAKEN 346, MORALE_WAVERING 267, NONE 14, OUT_OF_RANGE 164, RALLIED 13, REFUSE_ATTACK 21, ROUTED_FLEE 113, SEEK_ENEMY 70, TOO_CLOSE 252

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t27-kite-zombie-linea-base-r75 - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
