# Simulación Rally - lote 20261104-213440-t4-food-zombie-a

Preset `t4-food-zombie-a` · velocidad `sprint` · 211 corridas · 7 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=53}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t4-final-zombie-mele-base | 30 | 27 % ± 8 | 73 % ± 8 | 1.5 ± 0.5 | 8.1 ± 1.2 | 31 ± 1 | 558 ± 14 | 165 ± 7 | 0 % ± 0 | 3.8 ± 0.1 | 5.3 ± 0.2 | 0 |
| t4-final-zombie-mele-comidamochila | 30 | 77 % ± 8 | 23 % ± 8 | 4.8 ± 0.5 | 1.7 ± 0.6 | 34 ± 2 | 605 ± 4 | 166 ± 12 | 0 % ± 0 | 4.6 ± 0.3 | 6.5 ± 0.4 | 0 |
| t4-final-zombie-mele-comidamano | 30 | 92 % ± 4 | 8 % ± 4 | 7.0 ± 0.4 | 1.0 ± 0.5 | 42 ± 6 | 572 ± 3 | 188 ± 28 | 0 % ± 0 | 7.6 ± 1.0 | 10.1 ± 0.9 | 0 |
| t4-final-zombie-escudo-base | 30 | 70 % ± 9 | 30 % ± 9 | 4.5 ± 0.6 | 3.4 ± 1.1 | 41 ± 2 | 722 ± 10 | 124 ± 9 | 0 % ± 0 | 3.9 ± 0.2 | 5.8 ± 0.3 | 0 |
| t4-final-zombie-escudo-comidamochila | 30 | 90 % ± 4 | 10 % ± 4 | 6.7 ± 0.5 | 1.0 ± 0.4 | 52 ± 6 | 718 ± 3 | 137 ± 15 | 0 % ± 0 | 7.5 ± 0.8 | 8.6 ± 0.6 | 0 |
| t4-final-zombie-arco-base | 30 | 80 % ± 7 | 20 % ± 7 | 5.1 ± 0.5 | 1.9 ± 0.7 | 44 ± 4 | 351 ± 8 | 78 ± 11 | 89 % ± 1 | 9.1 ± 0.3 | 7.0 ± 0.4 | 0 |
| t4-final-zombie-arco-comidamochila | 30 | 80 % ± 7 | 20 % ± 7 | 5.6 ± 0.5 | 1.8 ± 0.7 | 53 ± 4 | 342 ± 6 | 112 ± 17 | 87 % ± 1 | 9.8 ± 0.4 | 7.5 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t4-final-zombie-mele-base semilla 1: sprint → red (0/11 vivos), normal → red (0/8 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 158 de 158 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t4-final-zombie-mele-base: enemy:mob=94
- t4-final-zombie-mele-comidamochila: enemy:mob=48
- t4-final-zombie-mele-comidamano: enemy:mob=10
- t4-final-zombie-escudo-base: enemy:mob=57
- t4-final-zombie-escudo-comidamochila: enemy:mob=20
- t4-final-zombie-arco-base: enemy:mob=37
- t4-final-zombie-arco-comidamochila: enemy:mob=37

## Motivos de decisión (cambios registrados): COUNTERATTACK 141, ENGAGE 5726, LOW_HEALTH_RETREAT 1037, MORALE_SHAKEN 2996, MORALE_WAVERING 1237, NONE 22, OUT_OF_RANGE 31, RALLIED 40, REFUSE_ATTACK 832, ROUTED_FLEE 641, SEEK_ENEMY 348, TOO_CLOSE 1270

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
