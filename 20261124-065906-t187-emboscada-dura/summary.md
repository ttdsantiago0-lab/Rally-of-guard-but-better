# Simulación Rally - lote 20261124-065906-t187-emboscada-dura

Preset `t187-emboscada-dura` · velocidad `sprint` · 200 corridas · 6 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t187-base-zombi-48 | 30 | 97 % ± 2 | 3 % ± 2 | 10.0 ± 0.3 | 0.6 ± 0.4 | 34 ± 4 | 1143 ± 6 | 80 ± 7 | 96 % ± 0 | 8.6 ± 0.1 | 8.6 ± 0.3 | 3 |
| t187-ambush-zombi-48 | 30 | 97 % ± 2 | 3 % ± 2 | 9.9 ± 0.4 | 0.6 ± 0.4 | 37 ± 4 | 1143 ± 5 | 70 ± 8 | 96 % ± 0 | 10.3 ± 0.3 | 9.8 ± 0.2 | 1 |
| t187-base-vind-16 | 30 | 100 % ± 0 | 0 % ± 0 | 11.6 ± 0.1 | 0.0 ± 0.0 | 11 ± 0 | 432 ± 2 | 25 ± 4 | 89 % ± 1 | 10.0 ± 0.1 | 8.9 ± 0.1 | 0 |
| t187-ambush-vind-16 | 30 | 100 % ± 0 | 0 % ± 0 | 11.5 ± 0.1 | 0.0 ± 0.0 | 12 ± 0 | 433 ± 1 | 28 ± 4 | 87 % ± 1 | 9.6 ± 0.1 | 8.5 ± 0.1 | 0 |
| t187-base-vind-24 | 27 | 76 % ± 6 | 24 % ± 6 | 5.1 ± 0.7 | 1.3 ± 0.4 | 63 ± 9 | 632 ± 5 | 187 ± 16 | 91 % ± 1 | 14.4 ± 1.2 | 14.8 ± 1.3 | 10 |
| t187-ambush-vind-24 | 29 | 57 % ± 8 | 43 % ± 8 | 3.5 ± 0.6 | 4.1 ± 0.9 | 68 ± 8 | 608 ± 10 | 220 ± 15 | 93 % ± 0 | 13.1 ± 1.0 | 15.3 ± 1.0 | 9 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t187-base-zombi-48 semilla 16700: sprint → draw (5/8 vivos), normal → blue (10/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 165 de 165 corridas ✓

## Vínculos del plan (segundos ejecutados / saltados y por qué)
- t187-ambush-zombi-48: linkSkipped:AMBUSH:missing_cap=97s linkStep:AMBUSH=661s
- t187-ambush-vind-16: linkSkipped:AMBUSH:missing_cap=5s linkStep:AMBUSH=214s
- t187-ambush-vind-24: linkSkipped:AMBUSH:missing_cap=56s linkStep:AMBUSH=207s

## Causas de muerte del bando azul (causa:tipo=bajas)
- t187-base-zombi-48: enemy:mob=27
- t187-ambush-zombi-48: enemy:mob=28
- t187-base-vind-16: enemy:mob=13
- t187-ambush-vind-16: enemy:mob=15
- t187-base-vind-24: enemy:mob=186
- t187-ambush-vind-24: enemy:mob=246

## Motivos de decisión (cambios registrados): ENGAGE 2811, FORMATION_HOLD 38, LOW_HEALTH_RETREAT 386, MORALE_SHAKEN 679, MORALE_WAVERING 348, ORDER_OVERRIDE 1336, OUTNUMBERED 224, OUT_OF_RANGE 5, REFUSE_ATTACK 1, ROUTED_FLEE 397, SEEK_ENEMY 1022, TOO_CLOSE 61, UNIT_BROKEN 16

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t187-ambush-vind-24 - 9 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t187-base-zombi-48 - 3 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t187-base-vind-24 - 10 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t187-ambush-zombi-48 - 1 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t187-base-zombi-48: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
