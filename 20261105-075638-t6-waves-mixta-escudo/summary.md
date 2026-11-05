# Simulación Rally - lote 20261105-075638-t6-waves-mixta-escudo

Preset `t6-waves-mixta-escudo` · velocidad `sprint` · 44 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=13}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-mixta-escudo-sincomida-base | 10 | 65 % ± 15 | 35 % ± 15 | 4.3 ± 1.0 | 4.1 ± 1.7 | 374 ± 55 | 1435 ± 120 | 232 ± 16 | 0 % ± 0 | 10.4 ± 2.8 | 11.2 ± 2.6 | 2 |
| t6-waves-mixta-escudo-sincomida-antes | 10 | 20 % ± 11 | 80 % ± 11 | 0.3 ± 0.3 | 5.8 ± 1.5 | 276 ± 38 | 1096 ± 104 | 288 ± 10 | 0 % ± 0 | 7.6 ± 1.3 | 7.9 ± 1.2 | 1 |
| t6-waves-mixta-escudo-comida-base | 10 | 100 % ± 0 | 0 % ± 0 | 7.4 ± 0.3 | 0.0 ± 0.0 | 371 ± 25 | 1691 ± 11 | 172 ± 15 | 0 % ± 0 | 10.1 ± 2.2 | 10.2 ± 1.8 | 0 |
| t6-waves-mixta-escudo-comida-antes | 10 | 30 % ± 13 | 70 % ± 13 | 1.5 ± 0.9 | 5.0 ± 1.3 | 398 ± 55 | 1313 ± 67 | 448 ± 41 | 0 % ± 0 | 9.0 ± 2.3 | 10.2 ± 2.6 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-mixta-escudo-sincomida-base semilla 1: sprint → draw (1/2 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 21 de 21 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-mixta-escudo-sincomida-base: enemy:arrow=2 enemy:explosion.player=19 enemy:mob=10
- t6-waves-mixta-escudo-sincomida-antes: enemy:arrow=8 enemy:explosion.player=51 enemy:mob=8
- t6-waves-mixta-escudo-comida-base: enemy:explosion.player=6
- t6-waves-mixta-escudo-comida-antes: enemy:arrow=5 enemy:explosion.player=50 enemy:mob=5

## Motivos de decisión (cambios registrados): COUNTERATTACK 26, ENGAGE 2996, LOW_HEALTH_RETREAT 268, MORALE_SHAKEN 640, MORALE_WAVERING 199, NONE 2316, RALLIED 10, REFUSE_ATTACK 70, ROUTED_FLEE 110, SEEK_ENEMY 1595, STUCK 34

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t6-waves-mixta-escudo-sincomida-base - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t6-waves-mixta-escudo-sincomida-antes - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
