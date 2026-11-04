# Simulación Rally - lote 20261104-213424-t4-food-zombie-b

Preset `t4-food-zombie-b` · velocidad `sprint` · 211 corridas · 7 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=27}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t4-final-zombie-arco-comidamano | 30 | 93 % ± 3 | 7 % ± 3 | 7.3 ± 0.2 | 0.5 ± 0.3 | 58 ± 6 | 332 ± 3 | 146 ± 26 | 85 % ± 1 | 11.9 ± 0.6 | 10.5 ± 1.1 | 0 |
| t4-final-zombie-linea-base | 30 | 63 % ± 9 | 37 % ± 9 | 3.9 ± 0.6 | 3.7 ± 1.0 | 51 ± 3 | 518 ± 8 | 126 ± 9 | 94 % ± 1 | 5.8 ± 0.3 | 8.9 ± 0.5 | 0 |
| t4-final-zombie-linea-comidamochila | 30 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.2 | 0.0 ± 0.0 | 47 ± 3 | 531 ± 4 | 127 ± 10 | 94 % ± 1 | 7.4 ± 0.6 | 10.3 ± 0.6 | 0 |
| t4-final-zombie-linea-comidamano | 30 | 93 % ± 5 | 7 % ± 5 | 7.0 ± 0.4 | 0.7 ± 0.5 | 45 ± 3 | 506 ± 4 | 136 ± 15 | 94 % ± 1 | 7.8 ± 0.8 | 10.2 ± 0.6 | 0 |
| t4-final-zombie-cab-base | 30 | 63 % ± 9 | 37 % ± 9 | 4.7 ± 0.7 | 2.4 ± 0.6 | 31 ± 3 | 306 ± 4 | 118 ± 11 | 88 % ± 3 | 6.1 ± 0.7 | 6.1 ± 0.5 | 0 |
| t4-final-zombie-cab-comidamochila | 30 | 97 % ± 3 | 3 % ± 3 | 7.7 ± 0.3 | 0.2 ± 0.2 | 20 ± 1 | 286 ± 2 | 86 ± 8 | 95 % ± 1 | 4.6 ± 0.2 | 5.3 ± 0.2 | 0 |
| t4-final-zombie-cab-comidamano | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 21 ± 0 | 286 ± 1 | 88 ± 4 | 96 % ± 1 | 4.5 ± 0.1 | 5.6 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t4-final-zombie-arco-comidamano semilla 1: sprint → blue (7/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 185 de 185 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t4-final-zombie-arco-comidamano: enemy:mob=8
- t4-final-zombie-linea-base: enemy:mob=64
- t4-final-zombie-linea-comidamochila: enemy:mob=26
- t4-final-zombie-linea-comidamano: enemy:mob=14
- t4-final-zombie-cab-base: enemy:mob=54
- t4-final-zombie-cab-comidamochila: enemy:mob=6
- t4-final-zombie-cab-comidamano: enemy:mob=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 70, ENGAGE 4210, FORMATION_HOLD 74, LOW_HEALTH_RETREAT 712, MORALE_SHAKEN 1365, MORALE_WAVERING 890, NONE 43, OUT_OF_RANGE 50, RALLIED 32, REFUSE_ATTACK 151, ROUTED_FLEE 451, SEEK_ENEMY 221, TOO_CLOSE 2752, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
