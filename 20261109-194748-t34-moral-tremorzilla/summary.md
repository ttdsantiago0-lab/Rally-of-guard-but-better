# Simulación Rally - lote 20261109-194748-t34-moral-tremorzilla

Preset `t34-moral-tremorzilla` · velocidad `sprint` · 30 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t34-moral-tremorzilla-sinmoral | 6 | 83 % ± 11 | 17 % ± 11 | 11.2 ± 2.2 | 0.3 ± 0.2 | 174 ± 43 | 1227 ± 177 | 205 ± 41 | 0 % ± 0 | 30.4 ± 2.4 | 27.5 ± 2.4 | 6 |
| t34-moral-tremorzilla-conmoral | 1 | 50 % ± 0 | 50 % ± 0 | 16.0 ± 0.0 | 1.0 ± 0.0 | 300 ± 0 | 1009 ± 0 | 118 ± 0 | 0 % ± 0 | 44.4 ± 0.0 | 34.4 ± 0.0 | 15 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t34-moral-tremorzilla-conmoral semilla 1: sprint → draw (7/1 vivos), normal → draw (3/1 vivos)
- t34-moral-tremorzilla-conmoral semilla 7778: sprint → draw (8/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 7 de 7 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t34-moral-tremorzilla-sinmoral: enemy:mob=11 enemy:tremorzilla_beam=16 env:radiation=2

## Motivos de decisión (cambios registrados): COUNTERATTACK 32, ENGAGE 1676, LOW_HEALTH_RETREAT 869, MORALE_SHAKEN 620, MORALE_WAVERING 347, NONE 1041, RALLIED 35, REFUSE_ATTACK 2, ROUTED_FLEE 280, SEEK_ENEMY 641, STUCK 569, TOO_CLOSE 9

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t34-moral-tremorzilla-conmoral - 15 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t34-moral-tremorzilla-sinmoral - 6 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t34-moral-tremorzilla-conmoral: 1/1 corridas llegaron al tope de tiempo
