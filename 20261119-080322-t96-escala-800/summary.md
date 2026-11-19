# Simulación Rally - lote 20261119-080322-t96-escala-800

Preset `t96-escala-800` · velocidad `sprint` · 22 corridas · 0 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t96-scale-800 semilla 9400: sprint → draw (97/30 vivos), normal → draw (24/156 vivos)
- t96-scale-800 semilla 17177: sprint → draw (134/86 vivos), normal → draw (55/171 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Motivos de decisión (cambios registrados): ENGAGE 24925, LOW_HEALTH_RETREAT 8112, MORALE_SHAKEN 9775, MORALE_WAVERING 4619, NONE 5251, ORDER_OVERRIDE 3652, OUT_OF_RANGE 12046, RALLIED 8, REFUSE_ATTACK 2148, ROUTED_FLEE 5937, SEEK_ENEMY 1490, STUCK 3, TOO_CLOSE 1, UNIT_BROKEN 2048

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t96-scale-800 - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t96-400-arena160 - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
