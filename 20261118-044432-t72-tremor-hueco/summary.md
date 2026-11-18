# Simulación Rally - lote 20261118-044432-t72-tremor-hueco

Preset `t72-tremor-hueco` · velocidad `sprint` · 62 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t72-escudos | 30 | 90 % ± 6 | 10 % ± 6 | 11.1 ± 0.8 | 0.1 ± 0.1 | 98 ± 6 | 944 ± 42 | 220 ± 15 | 0 % ± 0 | 26.8 ± 0.7 | 21.6 ± 0.8 | 1 |
| t72-escudos-nogrief | 30 | 73 % ± 8 | 27 % ± 8 | 9.5 ± 1.1 | 0.3 ± 0.1 | 125 ± 13 | 1011 ± 62 | 224 ± 16 | 0 % ± 0 | 30.6 ± 0.9 | 25.9 ± 1.0 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t72-escudos semilla 7201: sprint → blue (13/0 vivos), normal → blue (13/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 49 de 49 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t72-escudos: enemy:mob=36 enemy:tremorzilla_beam=97 env:radiation=13
- t72-escudos-nogrief: enemy:mob=64 enemy:tremorzilla_beam=118 env:fall=1 env:radiation=13

## Motivos de decisión (cambios registrados): ENGAGE 2038, FORMATION_HOLD 2, LOW_HEALTH_RETREAT 1936, NONE 2075, SEEK_ENEMY 2636, STUCK 19

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t72-escudos - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
