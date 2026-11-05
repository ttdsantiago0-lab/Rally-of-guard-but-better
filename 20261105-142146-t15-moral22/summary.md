# Simulación Rally - lote 20261105-142146-t15-moral22

Preset `t15-moral22` · velocidad `sprint` · 57 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=7}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t15-moral22-linea-conmoral | 10 | 30 % ± 15 | 70 % ± 15 | 1.7 ± 0.9 | 8.4 ± 2.4 | 517 ± 38 | 3199 ± 297 | 533 ± 34 | 87 % ± 1 | 20.1 ± 1.6 | 17.7 ± 1.3 | 7 |
| t15-moral22-linea-sinmoral | 12 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.1 | 0.0 ± 0.0 | 388 ± 4 | 4208 ± 47 | 267 ± 13 | 82 % ± 1 | 12.8 ± 0.3 | 13.1 ± 0.3 | 0 |
| t15-moral22-arco-conmoral | 12 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 371 ± 5 | 5086 ± 17 | 176 ± 8 | 83 % ± 1 | 14.6 ± 0.6 | 11.2 ± 0.6 | 2 |
| t15-moral22-arco-sinmoral | 12 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 357 ± 2 | 5129 ± 18 | 194 ± 11 | 85 % ± 0 | 13.7 ± 0.3 | 10.6 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t15-moral22-linea-conmoral semilla 1: sprint → red (0/4 vivos), normal → draw (7/17 vivos)
- t15-moral22-linea-conmoral semilla 7778: sprint → ? (0/0 vivos), normal → draw (1/16 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 39 de 39 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t15-moral22-linea-conmoral: enemy:arrow=2 enemy:explosion.player=9 enemy:mob=24
- t15-moral22-linea-sinmoral: enemy:explosion.player=3
- t15-moral22-arco-conmoral: enemy:explosion.player=1 enemy:mob=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 137, ENGAGE 7124, LOW_HEALTH_RETREAT 98, MORALE_SHAKEN 960, MORALE_WAVERING 448, NONE 3395, OUT_OF_RANGE 379, RALLIED 99, REFUSE_ATTACK 50, ROUTED_FLEE 407, SEEK_ENEMY 853, STUCK 13, TOO_CLOSE 2134

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t15-moral22-linea-conmoral - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t15-moral22-arco-conmoral - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t15-moral22-linea-conmoral: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
