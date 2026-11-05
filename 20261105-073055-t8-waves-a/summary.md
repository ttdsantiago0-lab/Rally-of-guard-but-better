# Simulación Rally - lote 20261105-073055-t8-waves-a

Preset `t8-waves-a` · velocidad `sprint` · 41 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=6}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 10 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.3 | 0.0 ± 0.0 | 279 ± 4 | 1983 ± 52 | 188 ± 25 | 82 % ± 1 | 6.8 ± 0.6 | 8.1 ± 0.7 | 0 |
| t8-waves-mixta-linea-comida-sinesquiva | 10 | 50 % ± 17 | 50 % ± 17 | 2.4 ± 0.9 | 3.6 ± 1.5 | 328 ± 27 | 1712 ± 70 | 397 ± 39 | 83 % ± 1 | 6.7 ± 0.7 | 7.1 ± 0.7 | 0 |
| t8-waves-mixta-arco-comida-base | 10 | 80 % ± 13 | 20 % ± 13 | 5.7 ± 1.0 | 0.8 ± 0.7 | 274 ± 13 | 2015 ± 139 | 293 ± 47 | 78 % ± 1 | 6.8 ± 0.7 | 7.0 ± 0.6 | 0 |
| t8-waves-mixta-arco-comida-sinesquiva | 10 | 80 % ± 13 | 20 % ± 13 | 4.4 ± 0.9 | 1.3 ± 0.9 | 282 ± 7 | 1924 ± 26 | 373 ± 33 | 79 % ± 1 | 6.9 ± 0.8 | 6.9 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 31 de 31 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t8-waves-mixta-linea-comida-base: enemy:explosion.player=6
- t8-waves-mixta-linea-comida-sinesquiva: enemy:arrow=2 enemy:explosion.player=43 enemy:mob=7
- t8-waves-mixta-arco-comida-base: enemy:arrow=2 enemy:explosion.player=15 enemy:mob=3
- t8-waves-mixta-arco-comida-sinesquiva: enemy:explosion.player=27 enemy:mob=5

## Motivos de decisión (cambios registrados): COUNTERATTACK 27, ENGAGE 4076, LOW_HEALTH_RETREAT 72, MORALE_SHAKEN 197, MORALE_WAVERING 88, NONE 2499, OUT_OF_RANGE 1, RALLIED 17, REFUSE_ATTACK 82, ROUTED_FLEE 80, SEEK_ENEMY 617, STUCK 10, TOO_CLOSE 2352

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
