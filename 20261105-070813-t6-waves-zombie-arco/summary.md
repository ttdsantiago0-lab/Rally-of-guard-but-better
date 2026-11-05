# Simulación Rally - lote 20261105-070813-t6-waves-zombie-arco

Preset `t6-waves-zombie-arco` · velocidad `sprint` · 41 corridas · 4 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=21}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t6-waves-zombie-arco-sincomida-base | 10 | 100 % ± 0 | 0 % ± 0 | 7.9 ± 0.1 | 0.0 ± 0.0 | 287 ± 2 | 2951 ± 10 | 77 ± 5 | 84 % ± 1 | 11.6 ± 0.2 | 10.8 ± 0.4 | 0 |
| t6-waves-zombie-arco-sincomida-antes | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 9.6 ± 1.2 | 159 ± 15 | 865 ± 121 | 195 ± 5 | 90 % ± 1 | 10.7 ± 0.9 | 9.0 ± 1.0 | 0 |
| t6-waves-zombie-arco-comida-base | 10 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 284 ± 2 | 2949 ± 9 | 67 ± 6 | 85 % ± 0 | 10.8 ± 0.1 | 9.9 ± 0.3 | 0 |
| t6-waves-zombie-arco-comida-antes | 10 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 10.7 ± 1.1 | 225 ± 21 | 999 ± 144 | 328 ± 16 | 85 % ± 1 | 12.2 ± 0.6 | 9.3 ± 0.9 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t6-waves-zombie-arco-sincomida-base semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 20 de 20 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t6-waves-zombie-arco-sincomida-antes: enemy:mob=40
- t6-waves-zombie-arco-comida-antes: enemy:mob=41

## Motivos de decisión (cambios registrados): COUNTERATTACK 27, ENGAGE 3328, LOW_HEALTH_RETREAT 177, MORALE_SHAKEN 248, MORALE_WAVERING 250, NONE 1786, OUT_OF_RANGE 76, RALLIED 22, REFUSE_ATTACK 72, ROUTED_FLEE 147, SEEK_ENEMY 149, TOO_CLOSE 1896

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
