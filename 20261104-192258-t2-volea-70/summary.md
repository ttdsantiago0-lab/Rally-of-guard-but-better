# Simulación Rally - lote 20261104-192258-t2-volea-70

Preset `t2-volea-70` · velocidad `sprint` · 241 corridas · 12 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=43}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t2-volea-zombie-arco@70-base | 20 | 100 % ± 0 | 0 % ± 0 | 6.7 ± 0.2 | 0.0 ± 0.0 | 41 ± 2 | 352 ± 5 | 42 ± 5 | 71 % ± 1 | 18.5 ± 0.7 | 6.2 ± 0.4 | 0 |
| t2-volea-zombie-arco@70-quietos | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 24 ± 0 | 325 ± 2 | 0 ± 0 | 64 % ± 1 | 35.2 ± 0.3 | 5.0 ± 0.1 | 0 |
| t2-volea-zombie-arco@70-volea | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 24 ± 0 | 317 ± 2 | 0 ± 0 | 54 % ± 1 | 47.0 ± 0.7 | 5.5 ± 0.1 | 0 |
| t2-volea-zombie-linea@70-base | 20 | 35 % ± 11 | 65 % ± 11 | 1.8 ± 0.6 | 9.0 ± 1.7 | 61 ± 3 | 470 ± 21 | 153 ± 9 | 66 % ± 2 | 12.0 ± 0.3 | 7.8 ± 0.5 | 0 |
| t2-volea-zombie-linea@70-quietos | 20 | 10 % ± 6 | 90 % ± 6 | 0.4 ± 0.3 | 16.0 ± 1.2 | 82 ± 5 | 365 ± 17 | 174 ± 6 | 59 % ± 2 | 19.3 ± 0.8 | 12.5 ± 0.7 | 0 |
| t2-volea-zombie-linea@70-volea | 20 | 23 % ± 9 | 78 % ± 9 | 1.4 ± 0.6 | 11.0 ± 1.5 | 81 ± 5 | 441 ± 16 | 155 ± 11 | 60 % ± 2 | 25.0 ± 0.8 | 15.6 ± 0.8 | 0 |
| t2-volea-skeleton-arco@70-base | 20 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.2 | 0.0 ± 0.0 | 27 ± 0 | 575 ± 5 | 91 ± 3 | 77 % ± 1 | 21.7 ± 0.2 | 5.1 ± 0.2 | 0 |
| t2-volea-skeleton-arco@70-quietos | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 29 ± 0 | 568 ± 8 | 47 ± 3 | 72 % ± 1 | 27.7 ± 0.3 | 4.9 ± 0.1 | 0 |
| t2-volea-skeleton-arco@70-volea | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 29 ± 0 | 612 ± 7 | 27 ± 3 | 69 % ± 1 | 34.2 ± 0.5 | 5.2 ± 0.1 | 0 |
| t2-volea-skeleton-linea@70-base | 20 | 60 % ± 11 | 40 % ± 11 | 3.8 ± 0.7 | 2.5 ± 0.7 | 78 ± 6 | 413 ± 14 | 144 ± 11 | 80 % ± 1 | 17.8 ± 0.6 | 11.4 ± 0.9 | 0 |
| t2-volea-skeleton-linea@70-quietos | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 43 ± 1 | 462 ± 11 | 38 ± 3 | 71 % ± 1 | 22.4 ± 0.2 | 9.3 ± 0.3 | 0 |
| t2-volea-skeleton-linea@70-volea | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 44 ± 1 | 491 ± 6 | 28 ± 3 | 67 % ± 1 | 25.5 ± 0.4 | 11.6 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t2-volea-zombie-arco@70-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 188 de 188 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t2-volea-zombie-arco@70-base: enemy:mob=9
- t2-volea-zombie-linea@70-base: enemy:mob=55
- t2-volea-zombie-linea@70-quietos: enemy:mob=65
- t2-volea-zombie-linea@70-volea: enemy:mob=70
- t2-volea-skeleton-arco@70-base: enemy:arrow=7
- t2-volea-skeleton-linea@70-base: enemy:arrow=84

## Motivos de decisión (cambios registrados): COUNTERATTACK 103, ENGAGE 4223, LOW_HEALTH_RETREAT 516, MORALE_SHAKEN 1654, MORALE_WAVERING 824, NONE 46, OUT_OF_RANGE 1753, RALLIED 17, REFUSE_ATTACK 96, ROUTED_FLEE 618, SEEK_ENEMY 121, TOO_CLOSE 453

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
