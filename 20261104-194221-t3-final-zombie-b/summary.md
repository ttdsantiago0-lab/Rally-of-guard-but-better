# Simulación Rally - lote 20261104-194221-t3-final-zombie-b

Preset `t3-final-zombie-b` · velocidad `sprint` · 211 corridas · 7 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=29}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t3-final-zombie-arco-quietos | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 15 ± 1 | 325 ± 2 | 3 ± 2 | 85 % ± 1 | 16.0 ± 0.3 | 5.3 ± 0.1 | 0 |
| t3-final-zombie-linea-base | 30 | 82 % ± 7 | 18 % ± 7 | 4.6 ± 0.5 | 1.8 ± 0.7 | 49 ± 3 | 539 ± 5 | 117 ± 8 | 95 % ± 1 | 6.2 ± 0.4 | 9.0 ± 0.4 | 0 |
| t3-final-zombie-linea-quemarropa | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.0 | 0.0 ± 0.0 | 20 ± 0 | 507 ± 2 | 37 ± 2 | 92 % ± 1 | 5.5 ± 0.1 | 7.1 ± 0.3 | 0 |
| t3-final-zombie-linea-quietos | 30 | 87 % ± 6 | 13 % ± 6 | 5.7 ± 0.5 | 0.6 ± 0.3 | 42 ± 4 | 537 ± 6 | 90 ± 9 | 93 % ± 1 | 8.1 ± 0.3 | 9.4 ± 0.3 | 0 |
| t3-final-zombie-cab-base | 30 | 63 % ± 9 | 37 % ± 9 | 4.8 ± 0.7 | 2.1 ± 0.5 | 29 ± 2 | 305 ± 5 | 118 ± 10 | 86 % ± 2 | 5.2 ± 0.2 | 5.4 ± 0.2 | 0 |
| t3-final-zombie-cab-quemarropa | 30 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.2 | 0.0 ± 0.0 | 14 ± 1 | 295 ± 3 | 40 ± 4 | 84 % ± 2 | 5.8 ± 0.1 | 5.8 ± 0.2 | 0 |
| t3-final-zombie-cab-lineafija | 30 | 73 % ± 8 | 27 % ± 8 | 5.7 ± 0.6 | 2.9 ± 1.0 | 27 ± 1 | 283 ± 6 | 101 ± 10 | 95 % ± 1 | 7.5 ± 0.2 | 6.9 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t3-final-zombie-arco-quietos semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 182 de 182 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t3-final-zombie-arco-quietos: enemy:mob=1
- t3-final-zombie-linea-base: enemy:mob=44
- t3-final-zombie-linea-quemarropa: enemy:mob=1
- t3-final-zombie-linea-quietos: enemy:mob=41
- t3-final-zombie-cab-base: enemy:mob=48
- t3-final-zombie-cab-quemarropa: enemy:mob=6
- t3-final-zombie-cab-lineafija: enemy:mob=36

## Motivos de decisión (cambios registrados): COUNTERATTACK 20, ENGAGE 3202, FORMATION_HOLD 86, LOW_HEALTH_RETREAT 703, MORALE_SHAKEN 938, MORALE_WAVERING 450, NONE 7, ORDER_OVERRIDE 58, OUT_OF_RANGE 5, RALLIED 6, REFUSE_ATTACK 91, ROUTED_FLEE 183, SEEK_ENEMY 405, TOO_CLOSE 1582

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
