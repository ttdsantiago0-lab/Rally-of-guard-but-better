# Simulación Rally - lote 20261108-202411-t27-kite-phantom-kitecadencia

Preset `t27-kite-phantom-kitecadencia` · velocidad `sprint` · 55 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-kite-phantom-arco-kitecadencia-r35 | 19 | 100 % ± 0 | 0 % ± 0 | 6.8 ± 0.1 | 0.0 ± 0.0 | 55 ± 2 | 845 ± 3 | 68 ± 1 | 52 % ± 2 | 16.3 ± 1.5 | 6.6 ± 0.5 | 5 |
| t27-kite-phantom-linea-kitecadencia-r35 | 15 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.2 | 0.0 ± 0.0 | 69 ± 4 | 824 ± 3 | 73 ± 3 | 55 % ± 2 | 11.2 ± 0.8 | 9.9 ± 0.5 | 15 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-kite-phantom-arco-kitecadencia-r35 semilla 1: sprint → blue (7/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 34 de 34 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-kite-phantom-arco-kitecadencia-r35: enemy:mob=23
- t27-kite-phantom-linea-kitecadencia-r35: enemy:mob=22

## Motivos de decisión (cambios registrados): COUNTERATTACK 5, ENGAGE 2061, LOW_HEALTH_RETREAT 245, MORALE_SHAKEN 275, MORALE_WAVERING 117, NONE 41, OUT_OF_RANGE 347, RALLIED 10, REFUSE_ATTACK 43, ROUTED_FLEE 32, SEEK_ENEMY 215, STUCK 37, TOO_CLOSE 1124

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t27-kite-phantom-linea-kitecadencia-r35 - 15 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t27-kite-phantom-arco-kitecadencia-r35 - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
