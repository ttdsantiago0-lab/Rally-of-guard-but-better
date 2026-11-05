# Simulación Rally - lote 20261105-140245-t6-waves-mixta-escudo

Preset `t6-waves-mixta-escudo` · velocidad `sprint` · 50 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=11}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-mixta-escudo-sincomida-antes | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 6.7 ± 1.1 | 364 ± 24 | 1063 ± 64 | 307 ± 9 | 0 % ± 0 | 13.0 ± 1.2 | 12.4 ± 1.1 | 0 |
| t6-waves-mixta-escudo-comida-base | 9 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 539 ± 17 | 1661 ± 10 | 156 ± 6 | 0 % ± 0 | 14.3 ± 1.1 | 15.6 ± 1.0 | 5 |
| t6-waves-mixta-escudo-comida-antes | 10 | 50 % ± 15 | 50 % ± 15 | 2.3 ± 0.8 | 2.0 ± 0.7 | 546 ± 37 | 1389 ± 64 | 442 ± 49 | 0 % ± 0 | 14.9 ± 0.5 | 14.8 ± 0.6 | 0 |
| t6-waves-mixta-escudo-sincomida-base | 8 | 81 % ± 13 | 19 % ± 13 | 5.8 ± 1.2 | 2.3 ± 1.7 | 569 ± 25 | 1565 ± 57 | 197 ± 28 | 0 % ± 0 | 17.7 ± 1.3 | 16.9 ± 1.2 | 7 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-mixta-escudo-sincomida-base semilla 1: sprint → draw (8/1 vivos), normal → blue (7/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 22 de 22 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-mixta-escudo-sincomida-antes: enemy:arrow=21 enemy:explosion.player=34 enemy:mob=16
- t6-waves-mixta-escudo-comida-antes: enemy:arrow=8 enemy:explosion.player=38 enemy:mob=4
- t6-waves-mixta-escudo-sincomida-base: enemy:arrow=4 enemy:explosion.player=2 enemy:mob=6

## Motivos de decisión (cambios registrados): COUNTERATTACK 52, ENGAGE 4481, LOW_HEALTH_RETREAT 375, MORALE_SHAKEN 1623, MORALE_WAVERING 364, NONE 2851, RALLIED 61, REFUSE_ATTACK 146, ROUTED_FLEE 258, SEEK_ENEMY 1844, STUCK 77

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t6-waves-mixta-escudo-sincomida-base - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t6-waves-mixta-escudo-comida-base - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
