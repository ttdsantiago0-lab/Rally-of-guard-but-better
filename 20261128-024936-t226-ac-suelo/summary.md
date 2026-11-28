# Simulación Rally - lote 20261128-024936-t226-ac-suelo

Preset `t226-ac-suelo` · velocidad `sprint` · 34 corridas · 0 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t226-base semilla 10700: sprint → draw (7/22 vivos), normal → draw (38/23 vivos)
- t226-base semilla 18477: sprint → draw (29/11 vivos), normal → draw (9/40 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Motivos de decisión (cambios registrados): ENGAGE 12949, LOW_HEALTH_RETREAT 3534, MORALE_SHAKEN 6002, MORALE_WAVERING 2495, NONE 1216, ORDER_OVERRIDE 1756, OUT_OF_RANGE 3986, RALLIED 1, REFUSE_ATTACK 950, ROUTED_FLEE 3198, SEEK_ENEMY 483, STUCK 2, TOO_CLOSE 2, UNIT_BROKEN 1216

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t226-base - 16 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t226-noloot - 16 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
