# Simulación Rally - lote 20261104-215937-t4-food-zombie-a

Preset `t4-food-zombie-a` · velocidad `sprint` · 211 corridas · 7 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=47}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t4-final-zombie-mele-base | 30 | 33 % ± 9 | 67 % ± 9 | 1.9 ± 0.5 | 7.7 ± 1.3 | 32 ± 1 | 558 ± 16 | 163 ± 7 | 0 % ± 0 | 3.7 ± 0.1 | 5.1 ± 0.2 | 0 |
| t4-final-zombie-mele-comidamochila | 30 | 90 % ± 6 | 10 % ± 6 | 6.3 ± 0.4 | 1.1 ± 0.6 | 29 ± 1 | 582 ± 6 | 135 ± 10 | 0 % ± 0 | 4.6 ± 0.3 | 6.4 ± 0.4 | 0 |
| t4-final-zombie-mele-comidamano | 30 | 95 % ± 4 | 5 % ± 4 | 7.4 ± 0.3 | 0.7 ± 0.5 | 39 ± 4 | 569 ± 2 | 172 ± 19 | 0 % ± 0 | 6.5 ± 0.8 | 8.9 ± 0.8 | 0 |
| t4-final-zombie-escudo-base | 30 | 60 % ± 9 | 40 % ± 9 | 3.6 ± 0.6 | 5.8 ± 1.5 | 44 ± 2 | 697 ± 16 | 140 ± 9 | 0 % ± 0 | 3.9 ± 0.1 | 6.4 ± 0.4 | 0 |
| t4-final-zombie-escudo-comidamochila | 30 | 95 % ± 4 | 5 % ± 4 | 7.5 ± 0.3 | 0.5 ± 0.3 | 37 ± 4 | 711 ± 3 | 104 ± 10 | 0 % ± 0 | 5.1 ± 0.5 | 6.7 ± 0.5 | 0 |
| t4-final-zombie-arco-base | 30 | 83 % ± 7 | 17 % ± 7 | 4.7 ± 0.4 | 1.7 ± 0.8 | 48 ± 3 | 362 ± 11 | 85 ± 9 | 88 % ± 1 | 9.3 ± 0.4 | 7.3 ± 0.5 | 0 |
| t4-final-zombie-arco-comidamochila | 30 | 88 % ± 6 | 12 % ± 6 | 5.8 ± 0.4 | 1.5 ± 0.7 | 45 ± 4 | 344 ± 6 | 92 ± 15 | 87 % ± 1 | 9.8 ± 0.3 | 6.9 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t4-final-zombie-mele-base semilla 1001: sprint → blue (5/0 vivos), normal → red (0/9 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 165 de 165 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t4-final-zombie-mele-base: enemy:mob=93
- t4-final-zombie-mele-comidamochila: enemy:mob=27
- t4-final-zombie-mele-comidamano: enemy:mob=8
- t4-final-zombie-escudo-base: enemy:mob=70
- t4-final-zombie-escudo-comidamochila: enemy:mob=8
- t4-final-zombie-arco-base: enemy:mob=47
- t4-final-zombie-arco-comidamochila: enemy:mob=34

## Motivos de decisión (cambios registrados): COUNTERATTACK 113, ENGAGE 5543, LOW_HEALTH_RETREAT 1008, MORALE_SHAKEN 2939, MORALE_WAVERING 1124, NONE 21, OUT_OF_RANGE 20, RALLIED 25, REFUSE_ATTACK 757, ROUTED_FLEE 548, SEEK_ENEMY 293, TOO_CLOSE 1238

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t4-final-zombie-mele-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
