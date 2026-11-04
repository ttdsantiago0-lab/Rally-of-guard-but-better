# Simulación Rally - lote 20261104-184131-t1-final-zombie-a

Preset `t1-final-zombie-a` · velocidad `sprint` · 241 corridas · 8 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=61}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t1-final-zombie-mele-base | 30 | 43 % ± 9 | 57 % ± 9 | 2.9 ± 0.6 | 6.8 ± 1.2 | 46 ± 3 | 277 ± 16 | 138 ± 13 | 0 % ± 0 | 8.0 ± 0.3 | 9.1 ± 0.4 | 0 |
| t1-final-zombie-mele-mantener | 30 | 70 % ± 9 | 30 % ± 9 | 5.0 ± 0.6 | 4.2 ± 1.2 | 32 ± 1 | 290 ± 12 | 107 ± 12 | 0 % ± 0 | 7.0 ± 0.1 | 5.1 ± 0.2 | 0 |
| t1-final-zombie-mele-flanquear | 30 | 63 % ± 9 | 37 % ± 9 | 4.4 ± 0.6 | 4.6 ± 1.3 | 35 ± 2 | 285 ± 19 | 110 ± 11 | 0 % ± 0 | 7.4 ± 0.3 | 5.8 ± 0.2 | 0 |
| t1-final-zombie-escudo-base | 30 | 93 % ± 5 | 7 % ± 5 | 6.9 ± 0.4 | 0.7 ± 0.5 | 35 ± 3 | 245 ± 6 | 57 ± 9 | 0 % ± 0 | 9.4 ± 0.4 | 10.8 ± 0.5 | 0 |
| t1-final-zombie-escudo-flanquear | 30 | 90 % ± 6 | 10 % ± 6 | 6.9 ± 0.4 | 1.1 ± 0.6 | 32 ± 2 | 234 ± 6 | 54 ± 9 | 0 % ± 0 | 8.6 ± 0.4 | 6.3 ± 0.2 | 0 |
| t1-final-zombie-escudo-circulo | 30 | 95 % ± 4 | 5 % ± 4 | 7.2 ± 0.3 | 0.6 ± 0.5 | 30 ± 4 | 240 ± 4 | 36 ± 7 | 0 % ± 0 | 9.9 ± 0.6 | 8.7 ± 0.4 | 0 |
| t1-final-zombie-arco-base | 30 | 57 % ± 9 | 43 % ± 9 | 4.1 ± 0.7 | 4.9 ± 1.2 | 44 ± 4 | 512 ± 11 | 129 ± 12 | 97 % ± 1 | 5.7 ± 0.3 | 6.8 ± 0.3 | 0 |
| t1-final-zombie-arco-flanquear | 30 | 73 % ± 8 | 27 % ± 8 | 5.1 ± 0.6 | 3.2 ± 1.1 | 42 ± 3 | 529 ± 10 | 107 ± 11 | 96 % ± 1 | 6.9 ± 0.4 | 6.9 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t1-final-zombie-mele-base semilla 1: sprint → blue (6/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 176 de 176 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t1-final-zombie-mele-base: enemy:mob=73
- t1-final-zombie-mele-mantener: enemy:mob=44
- t1-final-zombie-mele-flanquear: enemy:mob=50
- t1-final-zombie-escudo-base: enemy:mob=18
- t1-final-zombie-escudo-flanquear: enemy:mob=17
- t1-final-zombie-escudo-circulo: enemy:mob=10
- t1-final-zombie-arco-base: enemy:mob=55
- t1-final-zombie-arco-flanquear: enemy:mob=54

## Motivos de decisión (cambios registrados): COUNTERATTACK 210, ENGAGE 2326, FORMATION_HOLD 571, INSTRUCTION 960, LOW_HEALTH_RETREAT 444, MORALE_SHAKEN 1139, MORALE_WAVERING 687, NONE 506, RALLIED 8, REFUSE_ATTACK 162, ROUTED_FLEE 523, SEEK_ENEMY 378, STUCK 9, TOO_CLOSE 631

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
