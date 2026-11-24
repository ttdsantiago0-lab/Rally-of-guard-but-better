# Simulación Rally - lote 20261123-225929-t157-lod

Preset `t157-lod` · velocidad `sprint` · 87 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t157-lod-d32 | 12 | 100 % ± 0 | 0 % ± 0 | 297.5 ± 2.1 | 0.0 ± 0.0 | 77 ± 4 | 9523 ± 20 | 3507 ± 29 | 57 % ± 1 | 36.3 ± 0.6 | 28.2 ± 0.1 | 1 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t157-lod-ctl semilla 13600: sprint → draw (40/91 vivos), normal → draw (122/125 vivos)
- t157-lod-ctl semilla 21377: sprint → draw (147/108 vivos), normal → draw (130/113 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 12 de 12 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t157-lod-d32: enemy:arrow=1141 enemy:mob=89

## Motivos de decisión (cambios registrados): COUNTERATTACK 613, ENGAGE 147135, LOW_HEALTH_RETREAT 37202, MORALE_SHAKEN 58724, MORALE_WAVERING 32601, NONE 21415, ORDER_OVERRIDE 27610, OUT_OF_RANGE 56181, RALLIED 95, REFUSE_ATTACK 10855, ROUTED_FLEE 38213, SEEK_ENEMY 10657, STUCK 114, TOO_CLOSE 188, UNIT_BROKEN 10343

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t157-lod-ctl - 24 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t157-lod-n2 - 24 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t157-lod-n4 - 24 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t157-lod-d32 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t157-lod-d32: Rally cuesta 2.61 ms/tick de media
