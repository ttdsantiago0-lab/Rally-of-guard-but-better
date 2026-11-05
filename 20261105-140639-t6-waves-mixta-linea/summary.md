# Simulación Rally - lote 20261105-140639-t6-waves-mixta-linea

Preset `t6-waves-mixta-linea` · velocidad `sprint` · 41 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=11}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-mixta-linea-sincomida-base | 10 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 299 ± 3 | 2178 ± 31 | 92 ± 5 | 80 % ± 1 | 13.0 ± 0.4 | 12.6 ± 0.3 | 0 |
| t6-waves-mixta-linea-sincomida-antes | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 6.7 ± 1.1 | 250 ± 14 | 1014 ± 71 | 257 ± 8 | 86 % ± 2 | 12.9 ± 0.5 | 11.2 ± 0.6 | 0 |
| t6-waves-mixta-linea-comida-base | 10 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 295 ± 2 | 2134 ± 27 | 88 ± 4 | 79 % ± 1 | 12.3 ± 0.2 | 12.6 ± 0.3 | 0 |
| t6-waves-mixta-linea-comida-antes | 10 | 35 % ± 15 | 65 % ± 15 | 1.5 ± 0.8 | 4.0 ± 1.4 | 407 ± 37 | 1446 ± 59 | 398 ± 32 | 87 % ± 1 | 12.7 ± 0.4 | 10.6 ± 0.3 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-mixta-linea-sincomida-base semilla 1: sprint → blue (7/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 23 de 23 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-mixta-linea-sincomida-base: enemy:mob=1
- t6-waves-mixta-linea-sincomida-antes: enemy:arrow=16 enemy:explosion.player=32 enemy:mob=18
- t6-waves-mixta-linea-comida-base: enemy:explosion.player=1
- t6-waves-mixta-linea-comida-antes: enemy:arrow=3 enemy:explosion.player=43 enemy:mob=10

## Motivos de decisión (cambios registrados): COUNTERATTACK 24, ENGAGE 3241, LOW_HEALTH_RETREAT 135, MORALE_SHAKEN 419, MORALE_WAVERING 182, NONE 2438, OUT_OF_RANGE 47, RALLIED 14, REFUSE_ATTACK 50, ROUTED_FLEE 104, SEEK_ENEMY 865, TOO_CLOSE 518, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
