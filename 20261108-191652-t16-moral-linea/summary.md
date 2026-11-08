# Simulación Rally - lote 20261108-191652-t16-moral-linea

Preset `t16-moral-linea` · velocidad `sprint` · 44 corridas · 3 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=6}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t16-moral-linea-nueva | 12 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 384 ± 3 | 4147 ± 23 | 245 ± 10 | 84 % ± 1 | 12.7 ± 0.2 | 13.1 ± 0.3 | 0 |
| t16-moral-linea-sinmoral | 12 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 388 ± 4 | 4279 ± 52 | 260 ± 9 | 83 % ± 0 | 12.5 ± 0.3 | 12.6 ± 0.4 | 0 |
| t16-moral-linea-vieja | 10 | 20 % ± 11 | 80 % ± 11 | 1.3 ± 0.8 | 7.5 ± 1.9 | 496 ± 50 | 2824 ± 323 | 549 ± 32 | 87 % ± 1 | 21.0 ± 2.3 | 19.3 ± 2.0 | 8 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t16-moral-linea-vieja semilla 1: sprint → draw (1/6 vivos), normal → draw (7/1 vivos)
- t16-moral-linea-vieja semilla 7778: sprint → draw (6/5 vivos), normal → draw (6/6 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 27 de 27 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t16-moral-linea-sinmoral: enemy:explosion.player=2 enemy:mob=1
- t16-moral-linea-vieja: enemy:arrow=7 enemy:explosion.player=9 enemy:mob=28

## Motivos de decisión (cambios registrados): COUNTERATTACK 135, ENGAGE 5456, LOW_HEALTH_RETREAT 100, MORALE_SHAKEN 1112, MORALE_WAVERING 437, NONE 2573, OUT_OF_RANGE 310, RALLIED 81, REFUSE_ATTACK 26, ROUTED_FLEE 407, SEEK_ENEMY 845, STUCK 44, TOO_CLOSE 1048

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t16-moral-linea-vieja - 8 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
