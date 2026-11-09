# Simulación Rally - lote 20261109-201700-t35-numeros-tremorzilla

Preset `t35-numeros-tremorzilla` · velocidad `sprint` · 47 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t35-numeros-tremorzilla-hierro-g8 | 5 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 58 ± 16 | 110 ± 32 | 256 ± 8 | 90 % ± 3 | 27.2 ± 3.5 | 9.3 ± 3.8 | 2 |
| t35-numeros-tremorzilla-netheritamas-g8 | 4 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 122 ± 8 | 460 ± 186 | 207 ± 8 | 92 % ± 3 | 25.5 ± 3.4 | 18.7 ± 3.2 | 2 |
| t35-numeros-tremorzilla-hierro-g16 | 3 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 83 ± 34 | 90 ± 19 | 513 ± 21 | 89 % ± 4 | 33.4 ± 6.0 | 22.2 ± 8.9 | 5 |
| t35-numeros-tremorzilla-netheritamas-g32 | 5 | 100 % ± 0 | 0 % ± 0 | 20.0 ± 2.0 | 0.0 ± 0.0 | 95 ± 24 | 1072 ± 195 | 462 ± 35 | 94 % ± 1 | 29.4 ± 5.1 | 24.4 ± 5.1 | 1 |
| t35-numeros-tremorzilla-netheritamas-g16 | 3 | 67 % ± 33 | 33 % ± 33 | 10.0 ± 5.0 | 0.3 ± 0.3 | 106 ± 24 | 948 ± 26 | 226 ± 71 | 94 % ± 3 | 25.9 ± 2.4 | 22.9 ± 1.5 | 7 |
| t35-numeros-tremorzilla-hierro-g32 | 2 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 136 ± 46 | 275 ± 20 | 1138 ± 18 | 93 % ± 1 | 37.5 ± 10.4 | 25.9 ± 1.8 | 7 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t35-numeros-tremorzilla-hierro-g8 semilla 1: sprint → draw (1/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 7 de 7 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t35-numeros-tremorzilla-hierro-g8: enemy:mob=2 enemy:tremorzilla_beam=32 env:radiation=6
- t35-numeros-tremorzilla-netheritamas-g8: enemy:mob=2 enemy:tremorzilla_beam=30
- t35-numeros-tremorzilla-hierro-g16: enemy:mob=2 enemy:tremorzilla_beam=43 env:radiation=3
- t35-numeros-tremorzilla-netheritamas-g32: enemy:mob=3 enemy:tremorzilla_beam=53 env:radiation=4
- t35-numeros-tremorzilla-netheritamas-g16: enemy:mob=3 enemy:tremorzilla_beam=15
- t35-numeros-tremorzilla-hierro-g32: enemy:mob=10 enemy:tremorzilla_beam=52 env:radiation=2

## Motivos de decisión (cambios registrados): ENGAGE 1577, FORMATION_HOLD 1, LOW_HEALTH_RETREAT 762, NONE 1347, OUT_OF_RANGE 273, SEEK_ENEMY 725, STUCK 216, TOO_CLOSE 77

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t35-numeros-tremorzilla-hierro-g8 - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t35-numeros-tremorzilla-hierro-g16 - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t35-numeros-tremorzilla-netheritamas-g16 - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t35-numeros-tremorzilla-hierro-g32 - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t35-numeros-tremorzilla-netheritamas-g8 - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t35-numeros-tremorzilla-netheritamas-g32 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- MUERTES QUE NO SON DEL ENEMIGO: t35-numeros-tremorzilla-hierro-g8 - 15 % de las bajas azules ({enemy:mob=2.0, enemy:tremorzilla_beam=32.0, env:radiation=6.0})
