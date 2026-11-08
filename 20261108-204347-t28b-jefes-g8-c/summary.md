# Simulación Rally - lote 20261108-204347-t28b-jefes-g8-c

Preset `t28b-jefes-g8-c` · velocidad `sprint` · 32 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t28b-jefe-ciclope-g8 | 6 | 100 % ± 0 | 0 % ± 0 | 6.5 ± 0.8 | 0.0 ± 0.0 | 59 ± 8 | 150 ± 0 | 54 ± 30 | 95 % ± 2 | 18.5 ± 2.0 | 14.0 ± 2.1 | 0 |
| t28b-jefe-tremorzilla-g8 | 3 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 26 ± 4 | 89 ± 22 | 258 ± 18 | 97 % ± 2 | 18.6 ± 0.2 | 8.4 ± 0.6 | 8 |
| t28b-jefe-luxtructosaurio-g8 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 208 ± 17 | 66 ± 11 | 342 ± 28 | 74 % ± 4 | 34.8 ± 1.5 | 19.0 ± 3.1 | 0 |
| t28b-jefe-gusanovacio-g8 | 6 | 0 % ± 0 | 100 % ± 0 | 0.0 ± 0.0 | 1.0 ± 0.0 | 200 ± 13 | 38 ± 6 | 520 ± 33 | 40 % ± 13 | 34.1 ± 1.7 | 11.6 ± 1.8 | 2 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t28b-jefe-ciclope-g8 semilla 1: sprint → blue (3/0 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 6 de 6 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t28b-jefe-ciclope-g8: enemy:mob=9
- t28b-jefe-tremorzilla-g8: enemy:tremorzilla_beam=24
- t28b-jefe-luxtructosaurio-g8: enemy:explosion.player=1 enemy:mob=43 env:onFire=4
- t28b-jefe-gusanovacio-g8: enemy:mob=48

## Motivos de decisión (cambios registrados): COUNTERATTACK 1, ENGAGE 734, FORMATION_HOLD 7, LOW_HEALTH_RETREAT 40, MORALE_SHAKEN 354, MORALE_WAVERING 232, NONE 238, OUT_OF_RANGE 41, RALLIED 18, REFUSE_ATTACK 45, ROUTED_FLEE 170, SEEK_ENEMY 64, STUCK 14, TOO_CLOSE 11

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t28b-jefe-tremorzilla-g8 - 8 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t28b-jefe-gusanovacio-g8 - 2 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t28b-jefe-ciclope-g8: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
