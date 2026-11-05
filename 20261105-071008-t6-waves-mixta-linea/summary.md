# Simulación Rally - lote 20261105-071008-t6-waves-mixta-linea

Preset `t6-waves-mixta-linea` · velocidad `sprint` · 43 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=21}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-mixta-linea-sincomida-base | 10 | 30 % ± 15 | 70 % ± 15 | 1.6 ± 0.9 | 3.5 ± 1.4 | 266 ± 17 | 1469 ± 133 | 255 ± 19 | 75 % ± 2 | 9.0 ± 1.0 | 9.3 ± 0.9 | 1 |
| t6-waves-mixta-linea-sincomida-antes | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 9.9 ± 0.4 | 169 ± 21 | 709 ± 59 | 247 ± 4 | 64 % ± 4 | 7.5 ± 1.3 | 7.4 ± 0.8 | 0 |
| t6-waves-mixta-linea-comida-base | 10 | 90 % ± 10 | 10 % ± 10 | 4.5 ± 0.7 | 0.6 ± 0.6 | 287 ± 9 | 1848 ± 25 | 305 ± 33 | 81 % ± 1 | 7.4 ± 0.6 | 8.5 ± 0.6 | 0 |
| t6-waves-mixta-linea-comida-antes | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 8.1 ± 1.0 | 312 ± 46 | 1101 ± 95 | 472 ± 37 | 71 % ± 3 | 8.2 ± 2.2 | 8.6 ± 1.4 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-mixta-linea-sincomida-base semilla 1: sprint → blue (6/0 vivos), normal → red (0/7 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 12 de 12 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-mixta-linea-sincomida-base: enemy:arrow=12 enemy:explosion.player=27 enemy:mob=14
- t6-waves-mixta-linea-sincomida-antes: enemy:explosion.player=39 enemy:mob=23
- t6-waves-mixta-linea-comida-base: enemy:explosion.player=31 enemy:mob=2
- t6-waves-mixta-linea-comida-antes: enemy:explosion.player=44 enemy:mob=17

## Motivos de decisión (cambios registrados): COUNTERATTACK 65, ENGAGE 2456, LOW_HEALTH_RETREAT 347, MORALE_SHAKEN 385, MORALE_WAVERING 250, NONE 1819, OUT_OF_RANGE 11, RALLIED 15, REFUSE_ATTACK 59, ROUTED_FLEE 183, SEEK_ENEMY 422, STUCK 1, TOO_CLOSE 900

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t6-waves-mixta-linea-sincomida-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t6-waves-mixta-linea-comida-antes - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t6-waves-mixta-linea-sincomida-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
