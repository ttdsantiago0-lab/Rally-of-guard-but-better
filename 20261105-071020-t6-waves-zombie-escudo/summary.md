# Simulación Rally - lote 20261105-071020-t6-waves-zombie-escudo

Preset `t6-waves-zombie-escudo` · velocidad `sprint` · 41 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=5}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-zombie-escudo-sincomida-base | 10 | 70 % ± 15 | 30 % ± 15 | 4.4 ± 1.0 | 2.5 ± 1.4 | 330 ± 9 | 2742 ± 86 | 244 ± 14 | 0 % ± 0 | 5.3 ± 0.2 | 7.5 ± 0.3 | 0 |
| t6-waves-zombie-escudo-sincomida-antes | 10 | 80 % ± 13 | 20 % ± 13 | 5.1 ± 0.9 | 1.4 ± 1.1 | 318 ± 8 | 2757 ± 90 | 241 ± 15 | 0 % ± 0 | 5.3 ± 0.3 | 7.7 ± 0.3 | 0 |
| t6-waves-zombie-escudo-comida-base | 10 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 307 ± 3 | 2836 ± 2 | 209 ± 7 | 0 % ± 0 | 4.6 ± 0.1 | 7.3 ± 0.3 | 0 |
| t6-waves-zombie-escudo-comida-antes | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 309 ± 2 | 2834 ± 0 | 204 ± 6 | 0 % ± 0 | 4.7 ± 0.1 | 7.3 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-zombie-escudo-sincomida-base semilla 1: sprint → blue (5/0 vivos), normal → red (0/13 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 35 de 35 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-zombie-escudo-sincomida-base: enemy:mob=20
- t6-waves-zombie-escudo-sincomida-antes: enemy:mob=12
- t6-waves-zombie-escudo-comida-base: enemy:mob=1

## Motivos de decisión (cambios registrados): COUNTERATTACK 8, ENGAGE 3023, LOW_HEALTH_RETREAT 475, MORALE_SHAKEN 99, MORALE_WAVERING 51, NONE 2759, REFUSE_ATTACK 19, ROUTED_FLEE 25, SEEK_ENEMY 1130

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t6-waves-zombie-escudo-sincomida-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
