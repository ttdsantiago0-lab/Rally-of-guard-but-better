# Simulación Rally - lote 20261105-075407-t6-waves-mixta-arco

Preset `t6-waves-mixta-arco` · velocidad `sprint` · 42 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=19}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-mixta-arco-sincomida-base | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 277 ± 1 | 2397 ± 11 | 84 ± 6 | 77 % ± 1 | 13.1 ± 0.3 | 11.8 ± 0.3 | 0 |
| t6-waves-mixta-arco-sincomida-antes | 10 | 10 % ± 7 | 90 % ± 7 | 0.0 ± 0.0 | 6.7 ± 1.3 | 207 ± 22 | 776 ± 93 | 223 ± 8 | 84 % ± 1 | 12.8 ± 0.8 | 8.2 ± 0.7 | 0 |
| t6-waves-mixta-arco-comida-base | 10 | 90 % ± 10 | 10 % ± 10 | 7.2 ± 0.8 | 0.9 ± 0.9 | 272 ± 5 | 2256 ± 134 | 123 ± 39 | 76 % ± 1 | 12.9 ± 0.9 | 11.0 ± 0.9 | 1 |
| t6-waves-mixta-arco-comida-antes | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 9.5 ± 0.5 | 246 ± 28 | 849 ± 94 | 318 ± 21 | 81 % ± 4 | 12.8 ± 1.0 | 8.2 ± 0.9 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-mixta-arco-sincomida-base semilla 1: sprint → blue (8/0 vivos), normal → red (0/10 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 19 de 19 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-mixta-arco-sincomida-antes: enemy:arrow=4 enemy:explosion.player=28 enemy:mob=29
- t6-waves-mixta-arco-comida-base: enemy:explosion.player=7 enemy:mob=1
- t6-waves-mixta-arco-comida-antes: enemy:explosion.player=23 enemy:mob=30

## Motivos de decisión (cambios registrados): COUNTERATTACK 26, ENGAGE 2744, LOW_HEALTH_RETREAT 107, MORALE_SHAKEN 251, MORALE_WAVERING 190, NONE 1808, OUT_OF_RANGE 58, RALLIED 22, REFUSE_ATTACK 67, ROUTED_FLEE 117, SEEK_ENEMY 363, STUCK 2, TOO_CLOSE 1174

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t6-waves-mixta-arco-comida-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t6-waves-mixta-arco-sincomida-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
