# Simulación Rally - lote 20261128-035939-t232-ac-suelo2

Preset `t232-ac-suelo2` · velocidad `sprint` · 49 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t232-nodrop | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 44.0 ± 0.0 | 64 ± 0 | 1588 ± 0 | 2341 ± 0 | 66 % ± 0 | 30.6 ± 0.0 | 11.8 ± 0.0 | 14 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t232-base semilla 10700: sprint → draw (1/26 vivos), normal → draw (41/14 vivos)
- t232-base semilla 18477: sprint → draw (39/35 vivos), normal → draw (21/29 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Causas de muerte del bando azul (causa:tipo=bajas)
- t232-nodrop: enemy:arrow=96 enemy:mob=4

## Motivos de decisión (cambios registrados): ENGAGE 18726, LOW_HEALTH_RETREAT 5099, MORALE_SHAKEN 8617, MORALE_WAVERING 3571, NONE 1772, ORDER_OVERRIDE 2503, OUT_OF_RANGE 5860, RALLIED 2, REFUSE_ATTACK 1284, ROUTED_FLEE 4620, SEEK_ENEMY 733, STUCK 1, UNIT_BROKEN 1787

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t232-base - 16 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t232-noloot - 16 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t232-nodrop - 14 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
