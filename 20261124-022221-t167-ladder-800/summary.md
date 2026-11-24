# Simulación Rally - lote 20261124-022221-t167-ladder-800

Preset `t167-ladder-800` · velocidad `sprint` · 22 corridas · 0 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t167-tope400 semilla 14700: sprint → draw (100/24 vivos), normal → draw (116/137 vivos)
- t167-tope400 semilla 22477: sprint → draw (90/104 vivos), normal → draw (63/146 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Motivos de decisión (cambios registrados): COUNTERATTACK 2, ENGAGE 31366, LOW_HEALTH_RETREAT 9850, MORALE_SHAKEN 12203, MORALE_WAVERING 6028, NONE 6685, ORDER_OVERRIDE 4995, OUT_OF_RANGE 14921, RALLIED 15, REFUSE_ATTACK 2938, ROUTED_FLEE 8160, SEEK_ENEMY 2046, STUCK 11, TOO_CLOSE 2, UNIT_BROKEN 3010

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t167-tope400 - 20 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
