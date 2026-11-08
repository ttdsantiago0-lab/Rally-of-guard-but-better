# Simulación Rally - lote 20261108-202904-t28-jefe-tremorzilla

Preset `t28-jefe-tremorzilla` · velocidad `sprint` · 35 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t28-jefe-tremorzilla-g16 | 3 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 70 ± 23 | 99 ± 19 | 499 ± 45 | 86 % ± 3 | 29.1 ± 3.9 | 18.2 ± 5.0 | 7 |
| t28-jefe-tremorzilla-g64 | 1 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 166 ± 0 | 391 ± 0 | 2158 ± 0 | 96 % ± 0 | 33.3 ± 0.0 | 29.0 ± 0.0 | 10 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t28-jefe-tremorzilla-g16 semilla 1: sprint → red (0/1 vivos), normal → draw (1/1 vivos)
- t28-jefe-tremorzilla-g16 semilla 7778: sprint → ? (0/0 vivos), normal → draw (7/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Causas de muerte del bando azul (causa:tipo=bajas)
- t28-jefe-tremorzilla-g16: enemy:tremorzilla_beam=43 env:radiation=5
- t28-jefe-tremorzilla-g64: enemy:mob=12 enemy:tremorzilla_beam=44 env:fall=1 env:radiation=7

## Motivos de decisión (cambios registrados): COUNTERATTACK 25, ENGAGE 2584, FORMATION_HOLD 4, LOW_HEALTH_RETREAT 624, MORALE_SHAKEN 1181, MORALE_WAVERING 608, NONE 2043, OUT_OF_RANGE 330, RALLIED 31, REFUSE_ATTACK 33, ROUTED_FLEE 393, SEEK_ENEMY 467, STUCK 232, TOO_CLOSE 130

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t28-jefe-tremorzilla-g32 - 12 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t28-jefe-tremorzilla-g64 - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t28-jefe-tremorzilla-g16 - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t28-jefe-tremorzilla-g16: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
- MUERTES QUE NO SON DEL ENEMIGO: t28-jefe-tremorzilla-g16 - 10 % de las bajas azules ({enemy:tremorzilla_beam=43.0, env:radiation=5.0})
- MUERTES QUE NO SON DEL ENEMIGO: t28-jefe-tremorzilla-g64 - 13 % de las bajas azules ({enemy:mob=12.0, enemy:tremorzilla_beam=44.0, env:fall=1.0, env:radiation=7.0})
