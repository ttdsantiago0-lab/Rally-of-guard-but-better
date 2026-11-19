# Simulación Rally - lote 20261119-083021-t98-escala-rebanada

Preset `t98-escala-rebanada` · velocidad `sprint` · 22 corridas · 0 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t98-400-off semilla 9500: sprint → draw (17/46 vivos), normal → draw (53/39 vivos)
- t98-400-off semilla 17277: sprint → draw (49/40 vivos), normal → draw (55/53 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Motivos de decisión (cambios registrados): ENGAGE 15865, LOW_HEALTH_RETREAT 5569, MORALE_SHAKEN 6964, MORALE_WAVERING 2971, NONE 2539, ORDER_OVERRIDE 2131, OUT_OF_RANGE 7062, RALLIED 4, REFUSE_ATTACK 1239, ROUTED_FLEE 3874, SEEK_ENEMY 948, TOO_CLOSE 4, UNIT_BROKEN 1536

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t98-400-off - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t98-400-on - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
