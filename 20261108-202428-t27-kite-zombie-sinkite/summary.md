# Simulación Rally - lote 20261108-202428-t27-kite-zombie-sinkite

Preset `t27-kite-zombie-sinkite` · velocidad `sprint` · 81 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=75}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-kite-zombie-arco-sinkite-r60 | 20 | 20 % ± 9 | 80 % ± 9 | 1.1 ± 0.5 | 19.4 ± 3.1 | 65 ± 4 | 1285 ± 46 | 172 ± 7 | 98 % ± 0 | 6.7 ± 0.5 | 8.7 ± 0.5 | 0 |
| t27-kite-zombie-arco-sinkite-r85 | 20 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 55.1 ± 3.0 | 62 ± 5 | 1154 ± 65 | 186 ± 2 | 99 % ± 0 | 5.8 ± 0.4 | 8.8 ± 0.6 | 0 |
| t27-kite-zombie-linea-sinkite-r55 | 20 | 10 % ± 7 | 90 % ± 7 | 0.4 ± 0.3 | 23.6 ± 2.5 | 60 ± 4 | 1052 ± 45 | 185 ± 5 | 99 % ± 0 | 5.0 ± 0.4 | 10.3 ± 0.5 | 0 |
| t27-kite-zombie-linea-sinkite-r75 | 20 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 51.6 ± 1.6 | 50 ± 2 | 978 ± 29 | 186 ± 1 | 99 % ± 0 | 4.1 ± 0.2 | 9.6 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-kite-zombie-arco-sinkite-r60 semilla 1: sprint → red (0/34 vivos), normal → red (0/36 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 6 de 6 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-kite-zombie-arco-sinkite-r60: enemy:mob=65
- t27-kite-zombie-arco-sinkite-r85: enemy:mob=88
- t27-kite-zombie-linea-sinkite-r55: enemy:mob=76
- t27-kite-zombie-linea-sinkite-r75: enemy:mob=87

## Motivos de decisión (cambios registrados): COUNTERATTACK 29, ENGAGE 2142, LOW_HEALTH_RETREAT 1044, MORALE_SHAKEN 633, MORALE_WAVERING 475, NONE 14, OUT_OF_RANGE 11, RALLIED 4, REFUSE_ATTACK 43, ROUTED_FLEE 234, SEEK_ENEMY 192, TOO_CLOSE 1911

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
