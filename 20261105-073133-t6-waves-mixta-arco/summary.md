# Simulación Rally - lote 20261105-073133-t6-waves-mixta-arco

Preset `t6-waves-mixta-arco` · velocidad `sprint` · 45 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=26}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-mixta-arco-sincomida-base | 10 | 25 % ± 13 | 75 % ± 13 | 1.1 ± 0.8 | 6.0 ± 1.7 | 193 ± 25 | 1294 ± 191 | 243 ± 15 | 73 % ± 2 | 8.4 ± 1.1 | 7.9 ± 0.8 | 1 |
| t6-waves-mixta-arco-sincomida-antes | 9 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 14.1 ± 0.9 | 145 ± 36 | 415 ± 157 | 215 ± 15 | 48 % ± 10 | 8.7 ± 1.9 | 6.8 ± 1.2 | 3 |
| t6-waves-mixta-arco-comida-base | 10 | 70 % ± 15 | 30 % ± 15 | 3.8 ± 0.9 | 1.4 ± 0.8 | 284 ± 14 | 1905 ± 76 | 369 ± 41 | 78 % ± 1 | 7.7 ± 0.9 | 7.9 ± 0.6 | 0 |
| t6-waves-mixta-arco-comida-antes | 9 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 12.9 ± 1.7 | 134 ± 34 | 326 ± 147 | 295 ± 32 | 41 % ± 11 | 6.6 ± 1.5 | 5.4 ± 0.8 | 2 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-mixta-arco-sincomida-base semilla 1: sprint → red (0/3 vivos), normal → red (0/13 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 9 de 9 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-mixta-arco-sincomida-base: enemy:arrow=2 enemy:explosion.player=38 enemy:mob=13
- t6-waves-mixta-arco-sincomida-antes: enemy:arrow=1 enemy:explosion.player=15 enemy:mob=28
- t6-waves-mixta-arco-comida-base: enemy:explosion.player=33 enemy:mob=3
- t6-waves-mixta-arco-comida-antes: enemy:explosion.player=15 enemy:mob=30

## Motivos de decisión (cambios registrados): COUNTERATTACK 51, ENGAGE 2383, LOW_HEALTH_RETREAT 255, MORALE_SHAKEN 313, MORALE_WAVERING 261, NONE 1209, OUT_OF_RANGE 19, RALLIED 20, REFUSE_ATTACK 129, ROUTED_FLEE 193, SEEK_ENEMY 205, STUCK 1, TOO_CLOSE 1853

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t6-waves-mixta-arco-comida-antes - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t6-waves-mixta-arco-sincomida-base - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t6-waves-mixta-arco-sincomida-antes - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
