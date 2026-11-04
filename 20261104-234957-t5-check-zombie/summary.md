# Simulación Rally - lote 20261104-234957-t5-check-zombie

Preset `t5-check-zombie` · velocidad `sprint` · 301 corridas · 10 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=111}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-zombie-mele-base | 30 | 10 % ± 6 | 90 % ± 6 | 0.5 ± 0.3 | 10.1 ± 1.0 | 34 ± 1 | 542 ± 13 | 180 ± 4 | 0 % ± 0 | 3.9 ± 0.2 | 5.4 ± 0.2 | 0 |
| t5-final-zombie-mele-antes | 30 | 20 % ± 7 | 80 % ± 7 | 1.1 ± 0.4 | 9.1 ± 1.2 | 33 ± 1 | 550 ± 15 | 172 ± 5 | 0 % ± 0 | 3.7 ± 0.1 | 4.9 ± 0.2 | 0 |
| t5-final-zombie-escudo-base | 30 | 50 % ± 9 | 50 % ± 9 | 3.1 ± 0.6 | 5.4 ± 1.1 | 47 ± 3 | 711 ± 11 | 145 ± 10 | 0 % ± 0 | 4.1 ± 0.3 | 6.1 ± 0.4 | 0 |
| t5-final-zombie-escudo-antes | 30 | 53 % ± 9 | 47 % ± 9 | 3.2 ± 0.6 | 5.2 ± 1.2 | 46 ± 2 | 716 ± 12 | 144 ± 9 | 0 % ± 0 | 3.9 ± 0.2 | 5.8 ± 0.4 | 0 |
| t5-final-zombie-arco-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 0 | 323 ± 1 | 2 ± 0 | 87 % ± 1 | 8.0 ± 0.2 | 3.2 ± 0.1 | 0 |
| t5-final-zombie-arco-antes | 30 | 83 % ± 7 | 17 % ± 7 | 5.0 ± 0.5 | 1.3 ± 0.6 | 44 ± 3 | 367 ± 6 | 81 ± 10 | 89 % ± 1 | 9.3 ± 0.3 | 7.1 ± 0.4 | 0 |
| t5-final-zombie-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 20 ± 0 | 509 ± 2 | 37 ± 2 | 92 % ± 1 | 5.6 ± 0.2 | 7.0 ± 0.3 | 0 |
| t5-final-zombie-linea-antes | 30 | 67 % ± 9 | 33 % ± 9 | 3.6 ± 0.5 | 2.9 ± 0.9 | 50 ± 2 | 539 ± 9 | 130 ± 9 | 94 % ± 1 | 5.9 ± 0.3 | 8.8 ± 0.4 | 0 |
| t5-final-zombie-cab-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 14 ± 0 | 292 ± 2 | 42 ± 3 | 85 % ± 1 | 5.4 ± 0.2 | 5.9 ± 0.2 | 0 |
| t5-final-zombie-cab-antes | 30 | 73 % ± 8 | 27 % ± 8 | 5.5 ± 0.6 | 1.6 ± 0.5 | 30 ± 3 | 305 ± 5 | 110 ± 10 | 89 % ± 2 | 5.8 ± 0.4 | 6.4 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-zombie-mele-base semilla 1: sprint → red (0/15 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 197 de 197 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-zombie-mele-base: enemy:mob=113 other(minecraft:zombie_villager):mob=1
- t5-final-zombie-mele-antes: enemy:mob=94
- t5-final-zombie-escudo-base: enemy:mob=76
- t5-final-zombie-escudo-antes: enemy:mob=75 other(minecraft:zombie_villager):mob=1
- t5-final-zombie-arco-antes: enemy:mob=43
- t5-final-zombie-linea-base: enemy:mob=3
- t5-final-zombie-linea-antes: enemy:mob=65
- t5-final-zombie-cab-base: enemy:mob=1
- t5-final-zombie-cab-antes: enemy:mob=33

## Motivos de decisión (cambios registrados): COUNTERATTACK 97, ENGAGE 6214, FORMATION_HOLD 66, LOW_HEALTH_RETREAT 1378, MORALE_SHAKEN 2766, MORALE_WAVERING 1263, NONE 26, OUT_OF_RANGE 12, RALLIED 9, REFUSE_ATTACK 797, ROUTED_FLEE 589, SEEK_ENEMY 466, TOO_CLOSE 1782

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t5-final-zombie-mele-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
