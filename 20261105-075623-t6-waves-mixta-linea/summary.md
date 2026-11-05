# Simulación Rally - lote 20261105-075623-t6-waves-mixta-linea

Preset `t6-waves-mixta-linea` · velocidad `sprint` · 41 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=17}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-mixta-linea-sincomida-base | 10 | 60 % ± 16 | 40 % ± 16 | 3.7 ± 1.1 | 2.1 ± 1.0 | 283 ± 14 | 1716 ± 99 | 222 ± 31 | 80 % ± 1 | 7.6 ± 0.9 | 8.7 ± 0.9 | 0 |
| t6-waves-mixta-linea-sincomida-antes | 10 | 15 % ± 11 | 85 % ± 11 | 0.6 ± 0.6 | 6.1 ± 1.4 | 163 ± 29 | 700 ± 120 | 249 ± 10 | 56 % ± 9 | 6.6 ± 1.1 | 6.7 ± 0.8 | 0 |
| t6-waves-mixta-linea-comida-base | 10 | 90 % ± 10 | 10 % ± 10 | 6.8 ± 0.8 | 1.0 ± 1.0 | 278 ± 9 | 1874 ± 105 | 202 ± 30 | 80 % ± 2 | 7.4 ± 1.0 | 8.1 ± 0.9 | 0 |
| t6-waves-mixta-linea-comida-antes | 10 | 5 % ± 5 | 95 % ± 5 | 0.2 ± 0.2 | 4.0 ± 0.8 | 321 ± 54 | 980 ± 79 | 443 ± 35 | 66 % ± 7 | 8.4 ± 2.1 | 8.3 ± 2.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-mixta-linea-sincomida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 17 de 17 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-mixta-linea-sincomida-base: enemy:arrow=5 enemy:explosion.player=21 enemy:mob=8
- t6-waves-mixta-linea-sincomida-antes: enemy:arrow=6 enemy:explosion.player=35 enemy:mob=15
- t6-waves-mixta-linea-comida-base: enemy:explosion.player=11 enemy:mob=1
- t6-waves-mixta-linea-comida-antes: enemy:arrow=5 enemy:explosion.player=48 enemy:mob=9

## Motivos de decisión (cambios registrados): COUNTERATTACK 35, ENGAGE 2639, LOW_HEALTH_RETREAT 271, MORALE_SHAKEN 264, MORALE_WAVERING 132, NONE 1820, OUT_OF_RANGE 15, RALLIED 12, REFUSE_ATTACK 65, ROUTED_FLEE 102, SEEK_ENEMY 758, STUCK 3, TOO_CLOSE 978

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
