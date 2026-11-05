# Simulación Rally - lote 20261105-071100-t6-waves-zombie-linea

Preset `t6-waves-zombie-linea` · velocidad `sprint` · 41 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=13}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-zombie-linea-sincomida-base | 10 | 100 % ± 0 | 0 % ± 0 | 7.7 ± 0.2 | 0.0 ± 0.0 | 290 ± 3 | 2859 ± 8 | 132 ± 6 | 86 % ± 1 | 8.4 ± 0.2 | 10.2 ± 0.3 | 0 |
| t6-waves-zombie-linea-sincomida-antes | 10 | 10 % ± 10 | 90 % ± 10 | 0.3 ± 0.3 | 8.9 ± 1.6 | 281 ± 30 | 1766 ± 199 | 258 ± 12 | 91 % ± 1 | 9.6 ± 0.6 | 10.2 ± 0.5 | 0 |
| t6-waves-zombie-linea-comida-base | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 286 ± 1 | 2858 ± 12 | 136 ± 6 | 85 % ± 1 | 8.0 ± 0.1 | 10.1 ± 0.2 | 0 |
| t6-waves-zombie-linea-comida-antes | 10 | 60 % ± 16 | 40 % ± 16 | 4.0 ± 1.1 | 2.3 ± 1.1 | 426 ± 14 | 2706 ± 90 | 377 ± 44 | 91 % ± 1 | 10.6 ± 0.6 | 9.6 ± 0.4 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-zombie-linea-sincomida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 27 de 27 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-zombie-linea-sincomida-base: enemy:mob=2
- t6-waves-zombie-linea-sincomida-antes: enemy:mob=32
- t6-waves-zombie-linea-comida-antes: enemy:mob=23

## Motivos de decisión (cambios registrados): COUNTERATTACK 40, ENGAGE 3664, LOW_HEALTH_RETREAT 329, MORALE_SHAKEN 304, MORALE_WAVERING 223, NONE 2491, OUT_OF_RANGE 51, RALLIED 10, REFUSE_ATTACK 42, ROUTED_FLEE 137, SEEK_ENEMY 309, TOO_CLOSE 1216

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
