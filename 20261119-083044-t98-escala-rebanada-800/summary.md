# Simulación Rally - lote 20261119-083044-t98-escala-rebanada-800

Preset `t98-escala-rebanada-800` · velocidad `sprint` · 22 corridas · 0 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t98-800-off semilla 9500: sprint → draw (121/24 vivos), normal → draw (75/142 vivos)
- t98-800-off semilla 17277: sprint → draw (145/72 vivos), normal → draw (120/108 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 31931, LOW_HEALTH_RETREAT 10111, MORALE_SHAKEN 12328, MORALE_WAVERING 6138, NONE 7313, ORDER_OVERRIDE 4974, OUT_OF_RANGE 15607, RALLIED 19, REFUSE_ATTACK 3003, ROUTED_FLEE 8181, SEEK_ENEMY 2078, STUCK 7, TOO_CLOSE 4, UNIT_BROKEN 2942

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t98-800-off - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t98-800-on - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
