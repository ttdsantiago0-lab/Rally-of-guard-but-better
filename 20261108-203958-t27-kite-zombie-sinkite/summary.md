# Simulación Rally - lote 20261108-203958-t27-kite-zombie-sinkite

Preset `t27-kite-zombie-sinkite` · velocidad `sprint` · 81 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=76}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-kite-zombie-arco-sinkite-r60 | 20 | 15 % ± 8 | 85 % ± 8 | 0.8 ± 0.4 | 24.6 ± 3.4 | 64 ± 5 | 1190 ± 58 | 178 ± 6 | 98 % ± 0 | 7.2 ± 0.7 | 9.9 ± 0.7 | 0 |
| t27-kite-zombie-arco-sinkite-r85 | 20 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 53.8 ± 2.5 | 63 ± 5 | 1173 ± 51 | 184 ± 1 | 99 % ± 0 | 5.9 ± 0.4 | 9.8 ± 0.6 | 0 |
| t27-kite-zombie-linea-sinkite-r55 | 20 | 5 % ± 5 | 95 % ± 5 | 0.4 ± 0.4 | 28.3 ± 2.5 | 50 ± 3 | 964 ± 41 | 184 ± 3 | 99 % ± 0 | 4.5 ± 0.4 | 10.7 ± 0.6 | 0 |
| t27-kite-zombie-linea-sinkite-r75 | 20 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 46.9 ± 2.3 | 51 ± 3 | 1034 ± 50 | 186 ± 2 | 99 % ± 0 | 4.1 ± 0.2 | 10.2 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-kite-zombie-arco-sinkite-r60 semilla 1001: sprint → blue (6/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 4 de 4 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-kite-zombie-arco-sinkite-r60: enemy:mob=65
- t27-kite-zombie-arco-sinkite-r85: enemy:mob=83
- t27-kite-zombie-linea-sinkite-r55: enemy:mob=77
- t27-kite-zombie-linea-sinkite-r75: enemy:mob=76

## Motivos de decisión (cambios registrados): COUNTERATTACK 27, ENGAGE 2235, LOW_HEALTH_RETREAT 1058, MORALE_SHAKEN 649, MORALE_WAVERING 466, NONE 9, OUT_OF_RANGE 9, RALLIED 2, REFUSE_ATTACK 35, ROUTED_FLEE 227, SEEK_ENEMY 194, TOO_CLOSE 2008

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
