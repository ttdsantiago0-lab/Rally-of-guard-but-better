# Simulación Rally - lote 20261124-022234-t167-ladder

Preset `t167-ladder` · velocidad `sprint` · 41 corridas · 1 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t167-arcos | 2 | 100 % ± 0 | 0 % ± 0 | 124.5 ± 3.5 | 0.0 ± 0.0 | 58 ± 3 | 4814 ± 13 | 3176 ± 73 | 58 % ± 0 | 47.8 ± 0.1 | 19.0 ± 0.1 | 18 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t167-arcos semilla 14700: sprint → draw (93/13 vivos), normal → blue (107/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 2 de 2 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t167-arcos: enemy:arrow=151

## Motivos de decisión (cambios registrados): ENGAGE 28841, LOW_HEALTH_RETREAT 12442, MORALE_SHAKEN 10607, MORALE_WAVERING 5646, NONE 10519, ORDER_OVERRIDE 4381, OUT_OF_RANGE 22542, RALLIED 10, REFUSE_ATTACK 2068, ROUTED_FLEE 7650, SEEK_ENEMY 977, STUCK 2, TOO_CLOSE 2, UNIT_BROKEN 2865

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t167-arcos - 18 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t167-mixto - 20 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
