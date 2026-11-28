# Simulación Rally - lote 20261128-024612-t224-ac-muestra

Preset `t224-ac-muestra` · velocidad `sprint` · 18 corridas · 0 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t224-ac-muestra semilla 10700: sprint → draw (22/1 vivos), normal → draw (26/24 vivos)
- t224-ac-muestra semilla 18477: sprint → draw (13/16 vivos), normal → draw (24/19 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Motivos de decisión (cambios registrados): ENGAGE 6814, LOW_HEALTH_RETREAT 1951, MORALE_SHAKEN 3175, MORALE_WAVERING 1314, NONE 677, ORDER_OVERRIDE 844, OUT_OF_RANGE 2199, REFUSE_ATTACK 485, ROUTED_FLEE 1659, SEEK_ENEMY 295, UNIT_BROKEN 622

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t224-ac-muestra - 16 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
