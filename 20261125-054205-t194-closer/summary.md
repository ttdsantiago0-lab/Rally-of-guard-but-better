# Simulación Rally - lote 20261125-054205-t194-closer

Preset `t194-closer` · velocidad `sprint` · 178 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t194-arcos-base-ro | 30 | 63 % ± 7 | 37 % ± 7 | 53.7 ± 7.4 | 25.7 ± 6.6 | 135 ± 9 | 4663 ± 97 | 4284 ± 114 | 67 % ± 1 | 47.2 ± 0.7 | 18.3 ± 0.4 | 0 |
| t194-arcos-yield-ro | 30 | 67 % ± 7 | 33 % ± 7 | 63.1 ± 8.1 | 21.5 ± 5.9 | 125 ± 9 | 4689 ± 70 | 4111 ± 129 | 66 % ± 1 | 47.6 ± 0.6 | 18.7 ± 0.4 | 0 |
| t194-arcos-base | 4 | 75 % ± 25 | 25 % ± 25 | 80.5 ± 27.7 | 24.5 ± 24.5 | 61 ± 3 | 4480 ± 344 | 3823 ± 366 | 62 % ± 4 | 46.1 ± 1.6 | 17.3 ± 0.8 | 54 |
| t194-arcos-yield | 4 | 100 % ± 0 | 0 % ± 0 | 106.3 ± 8.0 | 0.0 ± 0.0 | 57 ± 2 | 4820 ± 14 | 3319 ± 103 | 59 % ± 2 | 46.8 ± 0.2 | 18.7 ± 1.0 | 54 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t194-arcos-base semilla 17300: sprint → draw (79/10 vivos), normal → draw (98/11 vivos)
- t194-arcos-base semilla 25077: sprint → draw (90/14 vivos), normal → draw (4/98 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 57 de 57 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t194-arcos-base-ro: enemy:arrow=4388
- t194-arcos-yield-ro: enemy:arrow=4108
- t194-arcos-base: enemy:arrow=478
- t194-arcos-yield: enemy:arrow=375

## Motivos de decisión (cambios registrados): ENGAGE 118680, LOW_HEALTH_RETREAT 63495, MORALE_SHAKEN 35890, MORALE_WAVERING 23238, NONE 63142, ORDER_OVERRIDE 13723, OUT_OF_RANGE 131333, RALLIED 98, REFUSE_ATTACK 6902, ROUTED_FLEE 32697, SEEK_ENEMY 1548, STUCK 7, UNIT_BROKEN 12445

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t194-arcos-base - 54 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t194-arcos-yield - 54 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
