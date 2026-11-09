# Simulación Rally - lote 20261109-201917-reg-olas

Preset `reg-olas` · velocidad `sprint` · 47 corridas · 5 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=2}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t8-waves-mixta-linea-comida-base | 8 | 94 % ± 6 | 6 % ± 6 | 7.4 ± 0.5 | 1.3 ± 1.3 | 363 ± 52 | 2150 ± 52 | 131 ± 59 | 81 % ± 1 | 22.4 ± 2.5 | 21.1 ± 1.4 | 0 |
| t8-waves-mixta-arco-comida-base | 8 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 281 ± 6 | 2414 ± 12 | 65 ± 3 | 79 % ± 1 | 20.7 ± 1.3 | 17.5 ± 1.0 | 0 |
| t8-waves-mixta-escudo-comida-base | 7 | 93 % ± 7 | 7 % ± 7 | 7.3 ± 0.3 | 0.1 ± 0.1 | 587 ± 39 | 1547 ± 71 | 203 ± 25 | 0 % ± 0 | 21.3 ± 2.6 | 23.9 ± 3.2 | 4 |
| t16-moral-linea-nueva | 8 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.3 | 0.0 ± 0.0 | 419 ± 19 | 4384 ± 83 | 228 ± 22 | 81 % ± 1 | 20.1 ± 1.3 | 19.2 ± 1.1 | 0 |
| t16-moral-r0-nueva | 7 | 64 % ± 18 | 36 % ± 18 | 4.0 ± 1.4 | 6.3 ± 3.3 | 471 ± 57 | 3518 ± 383 | 360 ± 60 | 83 % ± 1 | 20.9 ± 1.6 | 21.1 ± 1.9 | 4 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t8-waves-mixta-linea-comida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 36 de 36 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t8-waves-mixta-linea-comida-base: enemy:explosion.player=1 enemy:mob=2
- t8-waves-mixta-escudo-comida-base: enemy:arrow=1 enemy:explosion.player=3 enemy:mob=1
- t16-moral-linea-nueva: enemy:explosion.player=2 enemy:mob=2
- t16-moral-r0-nueva: enemy:explosion.player=4 enemy:mob=8 enemy:scguns.bullet.decimated=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 153, ENGAGE 3782, FORMATION_HOLD 3, LOW_HEALTH_RETREAT 69, MORALE_SHAKEN 1125, MORALE_WAVERING 359, NONE 2504, OUT_OF_RANGE 151, RALLIED 56, REFUSE_ATTACK 6, ROUTED_FLEE 308, SEEK_ENEMY 682, STUCK 75, TOO_CLOSE 70

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t16-moral-r0-nueva - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t8-waves-mixta-escudo-comida-base - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
