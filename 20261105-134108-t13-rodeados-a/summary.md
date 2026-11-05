# Simulación Rally - lote 20261105-134108-t13-rodeados-a

Preset `t13-rodeados-a` · velocidad `sprint` · 37 corridas · 3 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=27}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t13-zombie24-linea-base | 12 | 25 % ± 13 | 75 % ± 13 | 2.0 ± 1.0 | 9.1 ± 2.2 | 460 ± 36 | 4799 ± 354 | 605 ± 59 | 93 % ± 1 | 13.0 ± 1.4 | 13.4 ± 1.1 | 0 |
| t13-zombie24-linea-circulo | 12 | 42 % ± 15 | 58 % ± 15 | 3.3 ± 1.2 | 8.5 ± 2.8 | 428 ± 24 | 4922 ± 314 | 549 ± 58 | 93 % ± 0 | 10.8 ± 1.1 | 11.4 ± 0.9 | 0 |
| t13-zombie24-linea-retroceder | 12 | 4 % ± 4 | 96 % ± 4 | 0.1 ± 0.1 | 12.8 ± 2.5 | 528 ± 37 | 3092 ± 250 | 639 ± 12 | 87 % ± 1 | 22.9 ± 1.0 | 19.5 ± 0.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t13-zombie24-linea-base semilla 1: sprint → blue (8/0 vivos), normal → red (0/23 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 9 de 9 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t13-zombie24-linea-base: enemy:mob=34
- t13-zombie24-linea-circulo: enemy:mob=30
- t13-zombie24-linea-retroceder: enemy:mob=50

## Motivos de decisión (cambios registrados): COUNTERATTACK 322, ENGAGE 4161, LOW_HEALTH_RETREAT 188, MORALE_SHAKEN 1249, MORALE_WAVERING 619, NONE 1664, OUT_OF_RANGE 156, RALLIED 94, REFUSE_ATTACK 106, ROUTED_FLEE 635, SEEK_ENEMY 141, TOO_CLOSE 1230

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t13-zombie24-linea-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
