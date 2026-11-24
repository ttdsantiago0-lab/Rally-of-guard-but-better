# Simulación Rally - lote 20261124-040927-t174-zombie-mele

Preset `t174-zombie-mele` · velocidad `sprint` · 31 corridas · 1 escenarios.
> Mobs ajenos retirados del campo entre corridas (tercer bando oculto): {minecraft:zombie_villager=25}

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t5-final-zombie-mele-base | 30 | 23 % ± 8 | 77 % ± 8 | 1.2 ± 0.4 | 7.8 ± 1.1 | 32 ± 1 | 566 ± 14 | 172 ± 6 | 0 % ± 0 | 3.7 ± 0.1 | 5.3 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t5-final-zombie-mele-base semilla 1: sprint → blue (6/0 vivos), normal → blue (6/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 7 de 7 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t5-final-zombie-mele-base: enemy:mob=104

## Motivos de decisión (cambios registrados): ENGAGE 287, LOW_HEALTH_RETREAT 293, MORALE_SHAKEN 162, MORALE_WAVERING 20, REFUSE_ATTACK 6, ROUTED_FLEE 2, SEEK_ENEMY 55

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
