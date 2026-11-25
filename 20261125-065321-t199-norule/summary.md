# Simulación Rally - lote 20261125-065321-t199-norule

Preset `t199-norule` · velocidad `sprint` · 61 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t199-ctl | 20 | 50 % ± 0 | 50 % ± 0 | 166.5 ± 3.7 | 36.7 ± 3.9 | 180 ± 0 | 7107 ± 51 | 6214 ± 105 | 60 % ± 1 | 42.4 ± 0.4 | 20.0 ± 0.2 | 0 |
| t199-nofb | 20 | 53 % ± 2 | 48 % ± 2 | 169.6 ± 4.2 | 33.3 ± 6.7 | 174 ± 6 | 7112 ± 96 | 6160 ± 115 | 59 % ± 1 | 42.5 ± 0.6 | 20.5 ± 0.3 | 0 |
| t199-nomor | 20 | 50 % ± 0 | 50 % ± 0 | 169.5 ± 4.1 | 26.3 ± 4.0 | 180 ± 0 | 7220 ± 60 | 6140 ± 114 | 60 % ± 1 | 39.9 ± 1.1 | 28.8 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t199-ctl semilla 16000: sprint → draw (168/1 vivos), normal → draw (192/19 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t199-ctl: enemy:arrow=4431 enemy:mob=239
- t199-nofb: enemy:arrow=4353 enemy:mob=255
- t199-nomor: enemy:arrow=4367 enemy:mob=243

## Motivos de decisión (cambios registrados): COUNTERATTACK 3, ENGAGE 89017, LOW_HEALTH_RETREAT 24306, MORALE_SHAKEN 31726, MORALE_WAVERING 16255, NONE 21529, ORDER_OVERRIDE 12575, OUT_OF_RANGE 45157, RALLIED 41, REFUSE_ATTACK 7612, ROUTED_FLEE 20637, SEEK_ENEMY 5500, STUCK 29, TOO_CLOSE 9, UNIT_BROKEN 6714

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t199-ctl: 20/20 corridas llegaron al tope de tiempo
- t199-nofb: 19/20 corridas llegaron al tope de tiempo
- t199-nomor: 20/20 corridas llegaron al tope de tiempo
