# Simulación Rally - lote 20261104-200109-t3-confirm-zombie

Preset `t3-confirm-zombie` · velocidad `sprint` · 331 corridas · 11 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=76}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t3-final-zombie-arco-base | 30 | 83 % ± 7 | 17 % ± 7 | 4.8 ± 0.5 | 1.6 ± 0.7 | 47 ± 4 | 367 ± 10 | 83 ± 10 | 89 % ± 1 | 9.0 ± 0.3 | 7.1 ± 0.4 | 0 |
| t3-final-zombie-arco-quemarropa | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 0 | 326 ± 1 | 2 ± 1 | 87 % ± 1 | 8.0 ± 0.2 | 2.9 ± 0.1 | 0 |
| t3-final-zombie-arco-quietos | 30 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.1 | 0.0 ± 0.0 | 18 ± 2 | 330 ± 4 | 7 ± 3 | 85 % ± 1 | 15.4 ± 0.3 | 5.3 ± 0.1 | 0 |
| t3-final-zombie-linea-base | 30 | 80 % ± 7 | 20 % ± 7 | 4.8 ± 0.5 | 1.5 ± 0.6 | 44 ± 2 | 536 ± 8 | 107 ± 9 | 94 % ± 1 | 5.8 ± 0.2 | 8.5 ± 0.3 | 0 |
| t3-final-zombie-linea-quemarropa | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 19 ± 0 | 505 ± 2 | 39 ± 2 | 92 % ± 1 | 5.2 ± 0.1 | 6.8 ± 0.3 | 0 |
| t3-final-zombie-cab-base | 30 | 70 % ± 9 | 30 % ± 9 | 5.1 ± 0.6 | 1.8 ± 0.5 | 32 ± 4 | 311 ± 5 | 118 ± 10 | 89 % ± 3 | 5.9 ± 0.5 | 6.2 ± 0.4 | 0 |
| t3-final-zombie-cab-quemarropa | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 0 | 290 ± 1 | 41 ± 2 | 83 % ± 1 | 5.4 ± 0.1 | 5.9 ± 0.2 | 0 |
| t3-final-zombie-mele-base | 30 | 20 % ± 7 | 80 % ± 7 | 0.9 ± 0.4 | 10.3 ± 1.3 | 33 ± 1 | 535 ± 16 | 176 ± 5 | 0 % ± 0 | 3.8 ± 0.1 | 5.2 ± 0.2 | 0 |
| t3-final-zombie-mele-circulo | 30 | 57 % ± 9 | 43 % ± 9 | 3.2 ± 0.5 | 4.4 ± 1.0 | 32 ± 1 | 592 ± 10 | 141 ± 8 | 0 % ± 0 | 3.7 ± 0.1 | 4.1 ± 0.2 | 0 |
| t3-final-zombie-escudo-base | 30 | 43 % ± 9 | 57 % ± 9 | 2.7 ± 0.6 | 6.8 ± 1.3 | 48 ± 3 | 696 ± 14 | 149 ± 10 | 0 % ± 0 | 4.0 ± 0.2 | 5.9 ± 0.3 | 0 |
| t3-final-zombie-escudo-circulo | 30 | 90 % ± 6 | 10 % ± 6 | 5.9 ± 0.4 | 0.8 ± 0.5 | 39 ± 1 | 738 ± 5 | 102 ± 6 | 0 % ± 0 | 3.7 ± 0.1 | 4.4 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t3-final-zombie-arco-base semilla 1001: sprint → blue (4/0 vivos), normal → red (0/6 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 253 de 253 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t3-final-zombie-arco-base: enemy:mob=51
- t3-final-zombie-arco-quietos: enemy:mob=2 other(minecraft:zombie_villager):mob=1
- t3-final-zombie-linea-base: enemy:mob=53
- t3-final-zombie-cab-base: enemy:mob=45
- t3-final-zombie-cab-quemarropa: enemy:mob=1
- t3-final-zombie-mele-base: enemy:mob=113
- t3-final-zombie-mele-circulo: enemy:mob=73 other(minecraft:zombie_villager):mob=2
- t3-final-zombie-escudo-base: enemy:mob=86
- t3-final-zombie-escudo-circulo: enemy:mob=33

## Motivos de decisión (cambios registrados): COUNTERATTACK 83, ENGAGE 6557, FORMATION_HOLD 47, LOW_HEALTH_RETREAT 1359, MORALE_SHAKEN 2867, MORALE_WAVERING 1146, NONE 17, OUT_OF_RANGE 10, RALLIED 8, REFUSE_ATTACK 826, ROUTED_FLEE 497, SEEK_ENEMY 471, TOO_CLOSE 1902

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t3-final-zombie-arco-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
- MUERTES QUE NO SON DEL ENEMIGO: t3-final-zombie-arco-quietos - 33 % de las bajas azules ({enemy:mob=2.0, other(minecraft:zombie_villager):mob=1.0})
