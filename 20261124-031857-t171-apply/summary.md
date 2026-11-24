# Simulación Rally - lote 20261124-031857-t171-apply

Preset `t171-apply` · velocidad `sprint` · 39 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t171-arcos | 3 | 33 % ± 33 | 67 % ± 33 | 37.3 ± 37.3 | 77.0 ± 38.6 | 60 ± 2 | 3810 ± 547 | 4399 ± 459 | 67 % ± 5 | 44.9 ± 1.5 | 18.3 ± 0.3 | 14 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t171-arcos semilla 14900: sprint → blue (112/0 vivos), normal → draw (86/15 vivos)
- t171-arcos semilla 22677: sprint → ? (0/0 vivos), normal → draw (5/67 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 1 de 1 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t171-arcos: enemy:arrow=488

## Motivos de decisión (cambios registrados): ENGAGE 27393, LOW_HEALTH_RETREAT 11557, MORALE_SHAKEN 10328, MORALE_WAVERING 5266, NONE 9327, ORDER_OVERRIDE 3870, OUT_OF_RANGE 20987, RALLIED 19, REFUSE_ATTACK 2026, ROUTED_FLEE 7061, SEEK_ENEMY 1069, STUCK 2, TOO_CLOSE 4, UNIT_BROKEN 2705

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t171-mixto - 20 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t171-arcos - 14 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t171-arcos: pico de Rally 24 ms en un tick (media de picos)
- t171-arcos: Rally cuesta 2.40 ms/tick de media
- t171-arcos: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
