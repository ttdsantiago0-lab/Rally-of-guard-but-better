# Simulación Rally - lote 20261105-140233-t13-rodeados-b

Preset `t13-rodeados-b` · velocidad `sprint` · 44 corridas · 3 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=1}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t13-mixta16-linea-base | 12 | 96 % ± 4 | 4 % ± 4 | 7.5 ± 0.5 | 1.3 ± 1.3 | 388 ± 30 | 3281 ± 73 | 202 ± 38 | 81 % ± 1 | 13.1 ± 0.6 | 13.3 ± 0.8 | 0 |
| t13-mixta16-linea-circulo | 12 | 96 % ± 4 | 4 % ± 4 | 7.3 ± 0.6 | 0.3 ± 0.3 | 399 ± 35 | 3309 ± 34 | 213 ± 36 | 82 % ± 1 | 14.7 ± 1.8 | 13.5 ± 1.0 | 2 |
| t13-mixta16-linea-retroceder | 10 | 85 % ± 11 | 15 % ± 11 | 5.5 ± 0.8 | 2.6 ± 1.8 | 517 ± 35 | 3107 ± 61 | 326 ± 44 | 74 % ± 1 | 22.5 ± 1.2 | 20.5 ± 1.1 | 7 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t13-mixta16-linea-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 33 de 33 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t13-mixta16-linea-base: enemy:arrow=4 enemy:mob=1
- t13-mixta16-linea-circulo: enemy:explosion.player=2 enemy:mob=6
- t13-mixta16-linea-retroceder: enemy:arrow=1 enemy:explosion.player=8 enemy:mob=10

## Motivos de decisión (cambios registrados): COUNTERATTACK 235, ENGAGE 5832, FORMATION_HOLD 2, LOW_HEALTH_RETREAT 74, MORALE_SHAKEN 1694, MORALE_WAVERING 512, NONE 2854, OUT_OF_RANGE 453, RALLIED 99, REFUSE_ATTACK 11, ROUTED_FLEE 505, SEEK_ENEMY 829, STUCK 33, TOO_CLOSE 643

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t13-mixta16-linea-circulo - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t13-mixta16-linea-retroceder - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
