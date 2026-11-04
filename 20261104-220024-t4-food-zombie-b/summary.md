# Simulación Rally - lote 20261104-220024-t4-food-zombie-b

Preset `t4-food-zombie-b` · velocidad `sprint` · 211 corridas · 7 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=16}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t4-final-zombie-arco-comidamano | 30 | 97 % ± 2 | 3 % ± 2 | 7.7 ± 0.1 | 0.2 ± 0.2 | 49 ± 4 | 327 ± 2 | 106 ± 17 | 87 % ± 1 | 11.2 ± 0.6 | 9.3 ± 0.8 | 0 |
| t4-final-zombie-linea-base | 30 | 73 % ± 8 | 27 % ± 8 | 4.3 ± 0.5 | 2.1 ± 0.7 | 45 ± 2 | 536 ± 7 | 116 ± 9 | 94 % ± 1 | 5.7 ± 0.3 | 8.1 ± 0.4 | 0 |
| t4-final-zombie-linea-comidamochila | 30 | 97 % ± 2 | 3 % ± 2 | 6.3 ± 0.3 | 0.3 ± 0.2 | 54 ± 5 | 529 ± 5 | 147 ± 15 | 94 % ± 1 | 8.2 ± 0.7 | 12.1 ± 1.1 | 0 |
| t4-final-zombie-linea-comidamano | 30 | 93 % ± 4 | 7 % ± 4 | 7.1 ± 0.3 | 0.5 ± 0.3 | 49 ± 5 | 508 ± 4 | 138 ± 16 | 93 % ± 1 | 8.4 ± 0.7 | 10.2 ± 0.6 | 0 |
| t4-final-zombie-cab-base | 30 | 77 % ± 8 | 23 % ± 8 | 5.9 ± 0.6 | 1.7 ± 0.6 | 28 ± 3 | 295 ± 4 | 104 ± 9 | 90 % ± 2 | 5.8 ± 0.4 | 6.3 ± 0.3 | 0 |
| t4-final-zombie-cab-comidamochila | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 20 ± 0 | 287 ± 1 | 88 ± 5 | 94 % ± 1 | 4.5 ± 0.1 | 5.4 ± 0.2 | 0 |
| t4-final-zombie-cab-comidamano | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 21 ± 0 | 284 ± 1 | 85 ± 3 | 97 % ± 1 | 4.3 ± 0.1 | 5.2 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t4-final-zombie-arco-comidamano semilla 1001: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 194 de 194 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t4-final-zombie-arco-comidamano: enemy:mob=3
- t4-final-zombie-linea-base: enemy:mob=55
- t4-final-zombie-linea-comidamochila: enemy:mob=31
- t4-final-zombie-linea-comidamano: enemy:mob=10
- t4-final-zombie-cab-base: enemy:mob=34
- t4-final-zombie-cab-comidamochila: enemy:mob=2

## Motivos de decisión (cambios registrados): COUNTERATTACK 96, ENGAGE 4067, FORMATION_HOLD 89, LOW_HEALTH_RETREAT 716, MORALE_SHAKEN 1254, MORALE_WAVERING 831, NONE 25, OUT_OF_RANGE 26, RALLIED 36, REFUSE_ATTACK 136, ROUTED_FLEE 441, SEEK_ENEMY 253, TOO_CLOSE 2721, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
