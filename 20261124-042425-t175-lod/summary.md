# Simulación Rally - lote 20261124-042425-t175-lod

Preset `t175-lod` · velocidad `sprint` · 122 corridas · 0 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t175-lod-ctl semilla 16000: sprint → draw (137/2 vivos), normal → draw (185/45 vivos)
- t175-lod-ctl semilla 23777: sprint → draw (169/31 vivos), normal → draw (159/42 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Motivos de decisión (cambios registrados): COUNTERATTACK 743, ENGAGE 181254, LOW_HEALTH_RETREAT 47331, MORALE_SHAKEN 74160, MORALE_WAVERING 40955, NONE 24321, ORDER_OVERRIDE 34671, OUT_OF_RANGE 69151, RALLIED 52, REFUSE_ATTACK 14647, ROUTED_FLEE 48667, SEEK_ENEMY 10816, STUCK 101, TOO_CLOSE 261, UNIT_BROKEN 13259

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t175-lod-ctl - 40 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t175-lod-n2 - 40 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t175-lod-n3 - 40 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
