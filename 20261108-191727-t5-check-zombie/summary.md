# Simulación Rally - lote 20261108-191727-t5-check-zombie

Preset `t5-check-zombie` · velocidad `sprint` · 301 corridas · 10 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=59}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-zombie-mele-base | 30 | 37 % ± 9 | 63 % ± 9 | 2.2 ± 0.5 | 7.2 ± 1.1 | 29 ± 1 | 556 ± 12 | 157 ± 7 | 0 % ± 0 | 3.8 ± 0.1 | 5.0 ± 0.2 | 0 |
| t5-final-zombie-mele-antes | 30 | 40 % ± 9 | 60 % ± 9 | 2.1 ± 0.5 | 6.4 ± 1.1 | 33 ± 1 | 578 ± 12 | 160 ± 7 | 0 % ± 0 | 4.0 ± 0.1 | 5.3 ± 0.3 | 0 |
| t5-final-zombie-escudo-base | 30 | 90 % ± 6 | 10 % ± 6 | 5.2 ± 0.4 | 0.5 ± 0.3 | 39 ± 1 | 757 ± 4 | 112 ± 6 | 0 % ± 0 | 4.0 ± 0.2 | 6.0 ± 0.4 | 0 |
| t5-final-zombie-escudo-antes | 30 | 87 % ± 6 | 13 % ± 6 | 5.0 ± 0.4 | 0.8 ± 0.4 | 41 ± 1 | 759 ± 5 | 120 ± 6 | 0 % ± 0 | 4.0 ± 0.1 | 6.1 ± 0.3 | 0 |
| t5-final-zombie-arco-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 14 ± 0 | 328 ± 2 | 2 ± 1 | 86 % ± 1 | 7.9 ± 0.2 | 3.1 ± 0.1 | 0 |
| t5-final-zombie-arco-antes | 30 | 87 % ± 6 | 13 % ± 6 | 5.0 ± 0.4 | 1.5 ± 0.7 | 47 ± 4 | 362 ± 9 | 78 ± 9 | 87 % ± 1 | 9.5 ± 0.4 | 7.5 ± 0.5 | 0 |
| t5-final-zombie-linea-base | 30 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 19 ± 0 | 507 ± 2 | 35 ± 2 | 92 % ± 1 | 5.4 ± 0.2 | 6.9 ± 0.3 | 0 |
| t5-final-zombie-linea-antes | 30 | 93 % ± 5 | 7 % ± 5 | 5.4 ± 0.3 | 0.7 ± 0.5 | 42 ± 2 | 543 ± 6 | 100 ± 6 | 95 % ± 1 | 6.4 ± 0.3 | 9.1 ± 0.4 | 0 |
| t5-final-zombie-cab-base | 30 | 100 % ± 0 | 0 % ± 0 | 7.6 ± 0.3 | 0.0 ± 0.0 | 15 ± 1 | 299 ± 6 | 44 ± 5 | 84 % ± 2 | 5.7 ± 0.1 | 5.7 ± 0.2 | 0 |
| t5-final-zombie-cab-antes | 30 | 83 % ± 7 | 17 % ± 7 | 6.3 ± 0.5 | 1.2 ± 0.5 | 25 ± 2 | 297 ± 4 | 95 ± 8 | 93 % ± 2 | 5.0 ± 0.3 | 6.0 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-zombie-mele-base semilla 1: sprint → blue (6/0 vivos), normal → red (0/15 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 245 de 245 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-zombie-mele-base: enemy:mob=80 other(minecraft:zombie_villager):mob=1
- t5-final-zombie-mele-antes: enemy:mob=90
- t5-final-zombie-escudo-base: enemy:mob=46
- t5-final-zombie-escudo-antes: enemy:mob=44
- t5-final-zombie-arco-antes: enemy:mob=46
- t5-final-zombie-linea-antes: enemy:mob=39
- t5-final-zombie-cab-base: enemy:mob=7
- t5-final-zombie-cab-antes: enemy:mob=21 other(minecraft:zombie_villager):mob=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 4, ENGAGE 3783, FORMATION_HOLD 59, LOW_HEALTH_RETREAT 1486, MORALE_SHAKEN 778, MORALE_WAVERING 261, NONE 18, OUT_OF_RANGE 21, RALLIED 4, REFUSE_ATTACK 34, ROUTED_FLEE 57, SEEK_ENEMY 531, TOO_CLOSE 1710

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t5-final-zombie-mele-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
