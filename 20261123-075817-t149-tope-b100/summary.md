# Simulación Rally - lote 20261123-075817-t149-tope-b100

Preset `t149-tope-b100` · velocidad `sprint` · 18 corridas · 0 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t149-tope-b100 semilla 10700: sprint → draw (4/24 vivos), normal → draw (29/16 vivos)
- t149-tope-b100 semilla 18477: sprint → draw (9/29 vivos), normal → draw (9/32 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Motivos de decisión (cambios registrados): ENGAGE 6801, LOW_HEALTH_RETREAT 1886, MORALE_SHAKEN 3260, MORALE_WAVERING 1293, NONE 563, ORDER_OVERRIDE 858, OUT_OF_RANGE 2171, REFUSE_ATTACK 475, ROUTED_FLEE 1594, SEEK_ENEMY 261, STUCK 1, TOO_CLOSE 1, UNIT_BROKEN 583

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t149-tope-b100 - 16 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
