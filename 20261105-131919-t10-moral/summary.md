# Simulación Rally - lote 20261105-131919-t10-moral

Preset `t10-moral` · velocidad `sprint` · 41 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=2}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t10-moral-linea-conmoral | 10 | 90 % ± 10 | 10 % ± 10 | 6.3 ± 0.8 | 0.6 ± 0.6 | 284 ± 7 | 1895 ± 74 | 218 ± 30 | 84 % ± 1 | 6.9 ± 0.8 | 7.5 ± 0.8 | 0 |
| t10-moral-linea-sinmoral | 10 | 95 % ± 5 | 5 % ± 5 | 6.7 ± 0.8 | 0.0 ± 0.0 | 285 ± 1 | 1951 ± 71 | 215 ± 40 | 83 % ± 1 | 6.8 ± 0.9 | 7.2 ± 1.0 | 0 |
| t10-moral-arco-conmoral | 10 | 90 % ± 10 | 10 % ± 10 | 5.5 ± 0.8 | 0.8 ± 0.8 | 274 ± 7 | 2076 ± 117 | 267 ± 42 | 81 % ± 1 | 7.7 ± 1.0 | 7.8 ± 1.0 | 0 |
| t10-moral-arco-sinmoral | 10 | 100 % ± 0 | 0 % ± 0 | 7.3 ± 0.3 | 0.0 ± 0.0 | 275 ± 2 | 2197 ± 57 | 211 ± 30 | 81 % ± 1 | 7.7 ± 1.0 | 7.8 ± 0.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t10-moral-linea-conmoral semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 37 de 37 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t10-moral-linea-conmoral: enemy:explosion.player=14 enemy:mob=1
- t10-moral-linea-sinmoral: enemy:explosion.player=13
- t10-moral-arco-conmoral: enemy:explosion.player=23
- t10-moral-arco-sinmoral: enemy:explosion.player=7

## Motivos de decisión (cambios registrados): COUNTERATTACK 10, ENGAGE 4334, LOW_HEALTH_RETREAT 37, MORALE_SHAKEN 76, MORALE_WAVERING 22, NONE 2676, OUT_OF_RANGE 5, RALLIED 3, REFUSE_ATTACK 16, ROUTED_FLEE 21, SEEK_ENEMY 931, STUCK 6, TOO_CLOSE 2045

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
