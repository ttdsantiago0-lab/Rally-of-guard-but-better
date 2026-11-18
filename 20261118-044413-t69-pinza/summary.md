# Simulación Rally - lote 20261118-044413-t69-pinza

Preset `t69-pinza` · velocidad `sprint` · 135 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t69-todos | 30 | 98 % ± 2 | 2 % ± 2 | 6.2 ± 0.4 | 0.5 ± 0.5 | 105 ± 8 | 2178 ± 7 | 276 ± 11 | 90 % ± 1 | 21.1 ± 1.7 | 23.4 ± 1.6 | 2 |
| t69-pinza0 | 29 | 100 % ± 0 | 0 % ± 0 | 6.9 ± 0.3 | 0.0 ± 0.0 | 96 ± 6 | 2182 ± 4 | 259 ± 9 | 89 % ± 1 | 21.5 ± 1.7 | 20.5 ± 1.5 | 4 |
| t69-pinzafija | 30 | 93 % ± 5 | 7 % ± 5 | 6.3 ± 0.4 | 2.5 ± 1.8 | 88 ± 5 | 2145 ± 32 | 276 ± 11 | 90 % ± 1 | 18.8 ± 1.8 | 21.5 ± 1.6 | 4 |
| t69-cebopinzafija | 30 | 100 % ± 0 | 0 % ± 0 | 7.3 ± 0.2 | 0.0 ± 0.0 | 85 ± 4 | 2192 ± 5 | 252 ± 5 | 90 % ± 1 | 19.9 ± 1.6 | 21.1 ± 1.7 | 5 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t69-todos semilla 6901: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 117 de 117 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t69-todos: enemy:mob=293
- t69-pinza0: enemy:mob=264
- t69-pinzafija: enemy:mob=291
- t69-cebopinzafija: enemy:mob=262

## Motivos de decisión (cambios registrados): ENGAGE 5753, FORMATION_HOLD 1469, INSTRUCTION 2876, LOW_HEALTH_RETREAT 113, ORDER_OVERRIDE 1053, OUT_OF_RANGE 1063, SEEK_ENEMY 1980, TOO_CLOSE 150, UNIT_BROKEN 109

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t69-pinzafija - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t69-todos - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t69-pinza0 - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t69-cebopinzafija - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
