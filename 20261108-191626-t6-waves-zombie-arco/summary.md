# Simulación Rally - lote 20261108-191626-t6-waves-zombie-arco

Preset `t6-waves-zombie-arco` · velocidad `sprint` · 41 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=20}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-zombie-arco-sincomida-base | 10 | 100 % ± 0 | 0 % ± 0 | 7.8 ± 0.1 | 0.0 ± 0.0 | 286 ± 2 | 2951 ± 8 | 78 ± 7 | 85 % ± 1 | 11.5 ± 0.2 | 10.6 ± 0.4 | 0 |
| t6-waves-zombie-arco-sincomida-antes | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 8.8 ± 1.1 | 182 ± 18 | 913 ± 126 | 191 ± 2 | 88 % ± 2 | 12.1 ± 0.8 | 9.9 ± 0.9 | 0 |
| t6-waves-zombie-arco-comida-base | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 282 ± 2 | 2959 ± 8 | 64 ± 9 | 83 % ± 0 | 10.9 ± 0.1 | 10.4 ± 0.4 | 0 |
| t6-waves-zombie-arco-comida-antes | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 10.3 ± 1.0 | 216 ± 29 | 903 ± 156 | 332 ± 11 | 88 % ± 1 | 10.6 ± 0.5 | 8.9 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-zombie-arco-sincomida-base semilla 1: sprint → blue (7/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 20 de 20 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-zombie-arco-sincomida-base: enemy:mob=1
- t6-waves-zombie-arco-sincomida-antes: enemy:mob=43
- t6-waves-zombie-arco-comida-antes: enemy:mob=47

## Motivos de decisión (cambios registrados): COUNTERATTACK 16, ENGAGE 3315, LOW_HEALTH_RETREAT 214, MORALE_SHAKEN 201, MORALE_WAVERING 238, NONE 1726, OUT_OF_RANGE 85, RALLIED 16, REFUSE_ATTACK 51, ROUTED_FLEE 115, SEEK_ENEMY 153, TOO_CLOSE 1943

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
