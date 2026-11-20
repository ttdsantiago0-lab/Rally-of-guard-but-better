# Simulación Rally - lote 20261120-215507-t109-escala-conf

Preset `t109-escala-conf` · velocidad `sprint` · 122 corridas · 0 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t109-800-off semilla 10226: sprint → draw (36/96 vivos), normal → draw (161/69 vivos)
- t109-800-off semilla 18003: sprint → draw (96/141 vivos), normal → draw (100/118 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Motivos de decisión (cambios registrados): COUNTERATTACK 6, ENGAGE 175298, LOW_HEALTH_RETREAT 53470, MORALE_SHAKEN 67539, MORALE_WAVERING 34291, NONE 37109, ORDER_OVERRIDE 28408, OUT_OF_RANGE 80585, RALLIED 89, REFUSE_ATTACK 16993, ROUTED_FLEE 46768, SEEK_ENEMY 11450, STUCK 62, TOO_CLOSE 25, UNIT_BROKEN 17415

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t109-800-off - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t109-800-xp-idx - 60 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
