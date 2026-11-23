# Simulación Rally - lote 20261123-070157-t149-tope-b100

Preset `t149-tope-b100` · velocidad `sprint` · 17 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t149-tope-b100 | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 43.0 ± 0.0 | 53 ± 0 | 1604 ± 0 | 2359 ± 0 | 73 % ± 0 | 31.0 ± 0.0 | 12.0 ± 0.0 | 14 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t149-tope-b100 semilla 10700: sprint → draw (11/21 vivos), normal → draw (31/28 vivos)
- t149-tope-b100 semilla 18477: sprint → draw (31/31 vivos), normal → draw (48/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Causas de muerte del bando azul (causa:tipo=bajas)
- t149-tope-b100: enemy:arrow=98 enemy:mob=2

## Motivos de decisión (cambios registrados): ENGAGE 6541, LOW_HEALTH_RETREAT 1782, MORALE_SHAKEN 3022, MORALE_WAVERING 1292, NONE 730, ORDER_OVERRIDE 803, OUT_OF_RANGE 2216, REFUSE_ATTACK 488, ROUTED_FLEE 1585, SEEK_ENEMY 259, TOO_CLOSE 1, UNIT_BROKEN 565

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t149-tope-b100 - 14 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
