# Simulación Rally - lote 20261105-071144-t6-waves-mixta-escudo

Preset `t6-waves-mixta-escudo` · velocidad `sprint` · 42 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=20}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-mixta-escudo-sincomida-base | 10 | 5 % ± 5 | 95 % ± 5 | 0.0 ± 0.0 | 5.9 ± 0.9 | 315 ± 42 | 1171 ± 86 | 299 ± 12 | 0 % ± 0 | 6.1 ± 0.8 | 6.8 ± 1.0 | 1 |
| t6-waves-mixta-escudo-sincomida-antes | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 7.9 ± 1.1 | 238 ± 23 | 961 ± 74 | 278 ± 7 | 0 % ± 0 | 6.4 ± 1.1 | 7.5 ± 0.9 | 0 |
| t6-waves-mixta-escudo-comida-base | 10 | 50 % ± 15 | 50 % ± 15 | 1.5 ± 0.6 | 2.6 ± 1.2 | 350 ± 34 | 1493 ± 59 | 378 ± 25 | 0 % ± 0 | 5.3 ± 0.4 | 5.8 ± 0.6 | 0 |
| t6-waves-mixta-escudo-comida-antes | 10 | 40 % ± 16 | 60 % ± 16 | 1.7 ± 0.7 | 3.2 ± 1.1 | 309 ± 44 | 1340 ± 112 | 368 ± 18 | 0 % ± 0 | 6.4 ± 1.5 | 7.1 ± 1.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-mixta-escudo-sincomida-base semilla 1: sprint → draw (0/0 vivos), normal → red (0/8 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 8 de 8 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-mixta-escudo-sincomida-base: enemy:arrow=16 enemy:explosion.player=42 enemy:mob=12
- t6-waves-mixta-escudo-sincomida-antes: enemy:arrow=19 enemy:explosion.player=40 enemy:mob=10
- t6-waves-mixta-escudo-comida-base: enemy:explosion.player=58
- t6-waves-mixta-escudo-comida-antes: enemy:explosion.player=52 enemy:mob=5

## Motivos de decisión (cambios registrados): COUNTERATTACK 43, ENGAGE 2162, LOW_HEALTH_RETREAT 407, MORALE_SHAKEN 631, MORALE_WAVERING 182, NONE 1739, RALLIED 8, REFUSE_ATTACK 125, ROUTED_FLEE 147, SEEK_ENEMY 1001, STUCK 9, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t6-waves-mixta-escudo-sincomida-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t6-waves-mixta-escudo-sincomida-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
