# Simulación Rally - lote 20261124-073835-t190-emboscada-unidad

Preset `t190-emboscada-unidad` · velocidad `sprint` · 73 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t190-vind-24-base | 30 | 75 % ± 7 | 25 % ± 7 | 5.6 ± 0.7 | 2.5 ± 0.8 | 53 ± 8 | 620 ± 10 | 176 ± 15 | 92 % ± 1 | 11.7 ± 1.1 | 14.2 ± 1.2 | 4 |
| t190-vind-24-ambush | 28 | 57 % ± 8 | 43 % ± 8 | 3.1 ± 0.6 | 3.8 ± 0.8 | 77 ± 8 | 608 ± 10 | 235 ± 14 | 93 % ± 1 | 13.5 ± 1.2 | 16.0 ± 1.0 | 9 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t190-vind-24-base semilla 16900: sprint → blue (7/0 vivos), normal → draw (6/5 vivos)
- t190-vind-24-base semilla 24677: sprint → ? (0/0 vivos), normal → draw (1/3 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 46 de 46 corridas ✓

## Vínculos del plan (segundos ejecutados / saltados y por qué)
- t190-vind-24-ambush: linkSkipped:AMBUSH:missing_cap=35s linkStep:AMBUSH=225s

## Causas de muerte del bando azul (causa:tipo=bajas)
- t190-vind-24-base: enemy:mob=193
- t190-vind-24-ambush: enemy:mob=250

## Motivos de decisión (cambios registrados): ENGAGE 1093, FORMATION_HOLD 8, LOW_HEALTH_RETREAT 95, MORALE_SHAKEN 481, MORALE_WAVERING 244, ORDER_OVERRIDE 1032, OUTNUMBERED 115, OUT_OF_RANGE 4, ROUTED_FLEE 318, SEEK_ENEMY 284, TOO_CLOSE 36, UNIT_BROKEN 17

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t190-vind-24-ambush - 9 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t190-vind-24-base - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t190-vind-24-base: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
