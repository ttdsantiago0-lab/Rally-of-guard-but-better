# Simulación Rally - lote 20261123-084706-t149-tope-b200

Preset `t149-tope-b200` · velocidad `sprint` · 18 corridas · 0 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t149-tope-b200 semilla 10700: sprint → draw (19/39 vivos), normal → draw (33/54 vivos)
- t149-tope-b200 semilla 18477: sprint → draw (66/9 vivos), normal → draw (31/58 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Motivos de decisión (cambios registrados): ENGAGE 12860, LOW_HEALTH_RETREAT 4551, MORALE_SHAKEN 5591, MORALE_WAVERING 2358, NONE 2030, ORDER_OVERRIDE 1688, OUT_OF_RANGE 5929, RALLIED 2, REFUSE_ATTACK 939, ROUTED_FLEE 3176, SEEK_ENEMY 735, STUCK 1, TOO_CLOSE 3, UNIT_BROKEN 1292

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t149-tope-b200 - 16 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
