# Simulación Rally - lote 20261105-140509-t14-arriba

Preset `t14-arriba` · velocidad `sprint` · 49 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=37}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t14-zombie24-linea-base | 12 | 75 % ± 13 | 25 % ± 13 | 5.7 ± 1.0 | 5.2 ± 2.7 | 417 ± 12 | 5316 ± 252 | 431 ± 55 | 92 % ± 0 | 10.3 ± 1.0 | 10.5 ± 0.9 | 0 |
| t14-zombie24-linea-escudoarriba | 12 | 8 % ± 8 | 92 % ± 8 | 0.7 ± 0.7 | 10.3 ± 2.4 | 388 ± 18 | 3038 ± 358 | 575 ± 35 | 94 % ± 1 | 14.5 ± 1.0 | 12.3 ± 0.9 | 0 |
| t14-zombie24-escudo-base | 12 | 8 % ± 8 | 92 % ± 8 | 0.5 ± 0.5 | 14.6 ± 1.8 | 350 ± 25 | 3107 ± 395 | 594 ± 23 | 0 % ± 0 | 9.2 ± 1.2 | 10.6 ± 0.8 | 0 |
| t14-zombie24-escudo-escudoarriba | 12 | 8 % ± 8 | 92 % ± 8 | 0.4 ± 0.4 | 7.4 ± 1.4 | 337 ± 29 | 2257 ± 353 | 532 ± 35 | 0 % ± 0 | 13.3 ± 1.1 | 11.3 ± 1.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t14-zombie24-linea-base semilla 1: sprint → blue (8/0 vivos), normal → red (0/17 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 12 de 12 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t14-zombie24-linea-base: enemy:mob=13
- t14-zombie24-linea-escudoarriba: enemy:mob=41
- t14-zombie24-escudo-base: enemy:mob=57
- t14-zombie24-escudo-escudoarriba: enemy:mob=49

## Motivos de decisión (cambios registrados): COUNTERATTACK 525, ENGAGE 4933, LOW_HEALTH_RETREAT 281, MORALE_SHAKEN 2147, MORALE_WAVERING 672, NONE 1819, OUT_OF_RANGE 25, RALLIED 97, REFUSE_ATTACK 413, ROUTED_FLEE 917, SEEK_ENEMY 380, TOO_CLOSE 976, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t14-zombie24-linea-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
