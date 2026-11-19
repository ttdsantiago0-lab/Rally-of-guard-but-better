# Simulación Rally - lote 20261119-080320-t96-escala-regimen

Preset `t96-escala-regimen` · velocidad `sprint` · 58 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t96-scale-100 | 2 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 15.0 ± 0.0 | 47 ± 9 | 946 ± 11 | 1173 ± 16 | 72 % ± 1 | 29.7 ± 0.5 | 9.1 ± 0.3 | 6 |
| t96-scale-400 | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 79.0 ± 0.0 | 57 ± 0 | 3623 ± 0 | 4803 ± 0 | 73 % ± 0 | 31.5 ± 0.0 | 13.6 ± 0.0 | 8 |
| t96-scale-200 | 1 | 100 % ± 0 | 0 % ± 0 | 41.0 ± 0.0 | 0.0 ± 0.0 | 62 ± 0 | 2344 ± 0 | 1649 ± 0 | 66 % ± 0 | 39.2 ± 0.0 | 14.3 ± 0.0 | 9 |
| t96-400-arcos | 2 | 50 % ± 50 | 50 % ± 50 | 59.5 ± 59.5 | 59.5 ± 59.5 | 58 ± 3 | 3965 ± 826 | 3889 ± 908 | 63 % ± 10 | 44.0 ± 0.5 | 16.2 ± 1.9 | 7 |
| t96-400-melee | 1 | 100 % ± 0 | 0 % ± 0 | 70.0 ± 0.0 | 0.0 ± 0.0 | 53 ± 0 | 4800 ± 0 | 4142 ± 0 | 0 % ± 0 | 13.2 ± 0.0 | 18.6 ± 0.0 | 9 |
| t96-400-sinmoral | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 65.0 ± 0.0 | 58 ± 0 | 3718 ± 0 | 4756 ± 0 | 72 % ± 0 | 30.8 ± 0.0 | 19.8 ± 0.0 | 9 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t96-scale-100 semilla 9400: sprint → red (0/15 vivos), normal → draw (3/14 vivos)
- t96-scale-100 semilla 17177: sprint → ? (0/0 vivos), normal → draw (3/19 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 3 de 3 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t96-scale-100: enemy:arrow=100
- t96-scale-400: enemy:arrow=190 enemy:mob=10
- t96-scale-200: enemy:arrow=57 enemy:mob=2
- t96-400-arcos: enemy:arrow=281
- t96-400-melee: enemy:mob=130
- t96-400-sinmoral: enemy:arrow=191 enemy:mob=9

## Motivos de decisión (cambios registrados): COUNTERATTACK 254, ENGAGE 30241, LOW_HEALTH_RETREAT 15457, MORALE_SHAKEN 10891, MORALE_WAVERING 4794, NONE 8031, ORDER_OVERRIDE 3475, OUT_OF_RANGE 16171, RALLIED 7, REFUSE_ATTACK 1422, ROUTED_FLEE 6346, SEEK_ENEMY 3160, STUCK 12, UNIT_BROKEN 3199

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t96-scale-200 - 9 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t96-400-arcos - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t96-400-melee - 9 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t96-400-sinmoral - 9 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t96-scale-100 - 6 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t96-scale-400 - 8 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t96-scale-100: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
