# Simulación Rally - lote 20261108-202332-t27-kite-phantom-base

Preset `t27-kite-phantom-base` · velocidad `sprint` · 56 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-kite-phantom-arco-base-r35 | 19 | 100 % ± 0 | 0 % ± 0 | 5.9 ± 0.2 | 0.0 ± 0.0 | 64 ± 3 | 845 ± 4 | 91 ± 3 | 58 % ± 1 | 14.9 ± 1.4 | 7.9 ± 0.4 | 6 |
| t27-kite-phantom-linea-base-r35 | 16 | 100 % ± 0 | 0 % ± 0 | 6.3 ± 0.1 | 0.0 ± 0.0 | 69 ± 4 | 829 ± 3 | 86 ± 3 | 65 % ± 2 | 10.4 ± 0.9 | 10.2 ± 0.7 | 14 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-kite-phantom-arco-base-r35 semilla 1: sprint → blue (7/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 35 de 35 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-kite-phantom-arco-base-r35: enemy:mob=39
- t27-kite-phantom-linea-base-r35: enemy:mob=28

## Motivos de decisión (cambios registrados): COUNTERATTACK 11, ENGAGE 2395, LOW_HEALTH_RETREAT 306, MORALE_SHAKEN 496, MORALE_WAVERING 174, NONE 53, OUT_OF_RANGE 249, RALLIED 13, REFUSE_ATTACK 45, ROUTED_FLEE 63, SEEK_ENEMY 175, STUCK 30, TOO_CLOSE 1328

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t27-kite-phantom-arco-base-r35 - 6 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t27-kite-phantom-linea-base-r35 - 14 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
