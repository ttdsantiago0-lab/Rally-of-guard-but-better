# Simulación Rally - lote 20261120-041038-t100-escala-indice

Preset `t100-escala-indice` · velocidad `sprint` · 62 corridas · 0 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t100-800-off semilla 9700: sprint → draw (60/42 vivos), normal → draw (105/142 vivos)
- t100-800-off semilla 17477: sprint → draw (131/141 vivos), normal → draw (94/123 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Motivos de decisión (cambios registrados): COUNTERATTACK 3, ENGAGE 88597, LOW_HEALTH_RETREAT 27185, MORALE_SHAKEN 34464, MORALE_WAVERING 17360, NONE 18527, ORDER_OVERRIDE 14416, OUT_OF_RANGE 40651, RALLIED 47, REFUSE_ATTACK 8665, ROUTED_FLEE 23649, SEEK_ENEMY 5760, STUCK 28, TOO_CLOSE 7, UNIT_BROKEN 8824

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t100-800-off - 20 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t100-800-xp - 20 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t100-800-xp-idx - 20 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
