# Simulación Rally - lote 20261119-090520-t99-escala-unitxp

Preset `t99-escala-unitxp` · velocidad `sprint` · 22 corridas · 0 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t99-800-off semilla 9600: sprint → draw (8/118 vivos), normal → draw (79/157 vivos)
- t99-800-off semilla 17377: sprint → draw (108/129 vivos), normal → draw (119/61 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Motivos de decisión (cambios registrados): COUNTERATTACK 2, ENGAGE 31823, LOW_HEALTH_RETREAT 9931, MORALE_SHAKEN 12220, MORALE_WAVERING 6119, NONE 6948, ORDER_OVERRIDE 4945, OUT_OF_RANGE 15368, RALLIED 18, REFUSE_ATTACK 2940, ROUTED_FLEE 8076, SEEK_ENEMY 2047, STUCK 11, TOO_CLOSE 4, UNIT_BROKEN 2981

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t99-800-off - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t99-800-on - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
