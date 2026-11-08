# Simulación Rally - lote 20261108-202330-t27-kite-phantom-sinkite

Preset `t27-kite-phantom-sinkite` · velocidad `sprint` · 57 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-kite-phantom-arco-sinkite-r35 | 19 | 100 % ± 0 | 0 % ± 0 | 6.0 ± 0.2 | 0.0 ± 0.0 | 61 ± 2 | 846 ± 3 | 91 ± 3 | 58 % ± 1 | 15.0 ± 1.1 | 7.1 ± 0.3 | 6 |
| t27-kite-phantom-linea-sinkite-r35 | 12 | 96 % ± 4 | 4 % ± 4 | 5.6 ± 0.3 | 0.1 ± 0.1 | 82 ± 10 | 828 ± 3 | 94 ± 5 | 68 % ± 3 | 10.6 ± 1.0 | 9.7 ± 0.7 | 19 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-kite-phantom-arco-sinkite-r35 semilla 1: sprint → blue (7/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 31 de 31 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-kite-phantom-arco-sinkite-r35: enemy:mob=38
- t27-kite-phantom-linea-sinkite-r35: enemy:mob=29

## Motivos de decisión (cambios registrados): COUNTERATTACK 14, ENGAGE 2263, LOW_HEALTH_RETREAT 321, MORALE_SHAKEN 398, MORALE_WAVERING 174, NONE 64, OUT_OF_RANGE 233, RALLIED 11, REFUSE_ATTACK 50, ROUTED_FLEE 51, SEEK_ENEMY 199, STUCK 15, TOO_CLOSE 1258

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t27-kite-phantom-linea-sinkite-r35 - 19 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t27-kite-phantom-arco-sinkite-r35 - 6 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
