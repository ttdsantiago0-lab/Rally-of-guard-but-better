# Simulación Rally - lote 20261109-012000-t31-encanto-tremorzilla

Preset `t31-encanto-tremorzilla` · velocidad `sprint` · 68 corridas · 5 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t31-encanto-tremorzilla-poder | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 85 ± 13 | 157 ± 27 | 554 ± 14 | 83 % ± 7 | 34.5 ± 2.9 | 27.7 ± 3.3 | 5 |
| t31-encanto-tremorzilla-netheritamas | 2 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 171 ± 33 | 356 ± 57 | 338 ± 23 | 96 % ± 1 | 36.2 ± 3.2 | 25.3 ± 3.0 | 13 |
| t31-encanto-tremorzilla-hierro | 3 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 103 ± 7 | 156 ± 32 | 555 ± 24 | 93 % ± 1 | 33.2 ± 2.6 | 21.3 ± 2.9 | 10 |
| t31-encanto-tremorzilla-filo | 3 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 71 ± 25 | 124 ± 27 | 517 ± 23 | 92 % ± 4 | 33.5 ± 1.6 | 20.1 ± 4.8 | 11 |
| t31-encanto-tremorzilla-proteccion | 3 | 17 % ± 17 | 83 % ± 17 | 3.3 ± 3.3 | 1.0 ± 0.0 | 190 ± 58 | 239 ± 51 | 385 ± 36 | 90 % ± 1 | 43.2 ± 10.5 | 27.7 ± 8.7 | 10 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t31-encanto-tremorzilla-hierro semilla 101: sprint → draw (1/1 vivos), normal → draw (7/1 vivos)
- t31-encanto-tremorzilla-hierro semilla 7878: sprint → draw (2/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 1 de 1 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t31-encanto-tremorzilla-poder: enemy:mob=6 enemy:tremorzilla_beam=86 env:radiation=4
- t31-encanto-tremorzilla-netheritamas: enemy:mob=8 enemy:tremorzilla_beam=20 env:radiation=4
- t31-encanto-tremorzilla-hierro: enemy:mob=5 enemy:tremorzilla_beam=38 env:radiation=5
- t31-encanto-tremorzilla-filo: enemy:mob=2 enemy:tremorzilla_beam=41 env:radiation=5
- t31-encanto-tremorzilla-proteccion: enemy:mob=4 enemy:tremorzilla_beam=31 env:radiation=3

## Motivos de decisión (cambios registrados): COUNTERATTACK 25, ENGAGE 2522, LOW_HEALTH_RETREAT 581, MORALE_SHAKEN 1182, MORALE_WAVERING 699, NONE 1605, OUT_OF_RANGE 292, RALLIED 55, REFUSE_ATTACK 26, ROUTED_FLEE 385, SEEK_ENEMY 113, STUCK 126, TOO_CLOSE 88

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t31-encanto-tremorzilla-hierro - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t31-encanto-tremorzilla-poder - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t31-encanto-tremorzilla-filo - 11 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t31-encanto-tremorzilla-proteccion - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t31-encanto-tremorzilla-netheritamas - 13 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t31-encanto-tremorzilla-netheritamas - 13 % de las bajas azules ({enemy:mob=8.0, enemy:tremorzilla_beam=20.0, env:radiation=4.0})
- MUERTES QUE NO SON DEL ENEMIGO: t31-encanto-tremorzilla-hierro - 10 % de las bajas azules ({enemy:mob=5.0, enemy:tremorzilla_beam=38.0, env:radiation=5.0})
- MUERTES QUE NO SON DEL ENEMIGO: t31-encanto-tremorzilla-filo - 10 % de las bajas azules ({enemy:mob=2.0, enemy:tremorzilla_beam=41.0, env:radiation=5.0})
