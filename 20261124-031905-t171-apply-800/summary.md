# Simulación Rally - lote 20261124-031905-t171-apply-800

Preset `t171-apply-800` · velocidad `sprint` · 22 corridas · 0 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t171-tope400 semilla 14900: sprint → draw (80/57 vivos), normal → draw (103/135 vivos)
- t171-tope400 semilla 22677: sprint → draw (99/153 vivos), normal → draw (129/97 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 31898, LOW_HEALTH_RETREAT 9751, MORALE_SHAKEN 12240, MORALE_WAVERING 6261, NONE 6964, ORDER_OVERRIDE 5149, OUT_OF_RANGE 14721, RALLIED 23, REFUSE_ATTACK 3094, ROUTED_FLEE 8382, SEEK_ENEMY 2134, STUCK 11, TOO_CLOSE 1, UNIT_BROKEN 3003

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t171-tope400 - 20 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
