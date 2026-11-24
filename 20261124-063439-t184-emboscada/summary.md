# Simulación Rally - lote 20261124-063439-t184-emboscada

Preset `t184-emboscada` · velocidad `sprint` · 121 corridas · 4 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t184-base-15 | 30 | 100 % ± 0 | 0 % ± 0 | 12.0 ± 0.0 | 0.0 ± 0.0 | 12 ± 0 | 349 ± 2 | 2 ± 1 | 91 % ± 1 | 15.9 ± 0.2 | 8.7 ± 0.1 | 0 |
| t184-ambush-15 | 30 | 100 % ± 0 | 0 % ± 0 | 12.0 ± 0.0 | 0.0 ± 0.0 | 12 ± 0 | 346 ± 1 | 1 ± 1 | 92 % ± 1 | 15.1 ± 0.2 | 8.3 ± 0.1 | 0 |
| t184-base-22 | 30 | 100 % ± 0 | 0 % ± 0 | 12.0 ± 0.0 | 0.0 ± 0.0 | 15 ± 0 | 510 ± 2 | 7 ± 1 | 92 % ± 1 | 11.4 ± 0.1 | 7.9 ± 0.1 | 0 |
| t184-ambush-22 | 30 | 100 % ± 0 | 0 % ± 0 | 11.9 ± 0.0 | 0.0 ± 0.0 | 16 ± 0 | 508 ± 2 | 8 ± 2 | 92 % ± 1 | 11.5 ± 0.1 | 7.9 ± 0.1 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t184-base-15 semilla 16600: sprint → blue (12/0 vivos), normal → blue (12/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 120 de 120 corridas ✓

## Vínculos del plan (segundos ejecutados / saltados y por qué)
- t184-ambush-15: linkStep:AMBUSH=215s
- t184-ambush-22: linkStep:AMBUSH=320s

## Causas de muerte del bando azul (causa:tipo=bajas)
- t184-ambush-22: enemy:mob=1

## Motivos de decisión (cambios registrados): ENGAGE 1475, FORMATION_HOLD 6, LOW_HEALTH_RETREAT 12, MORALE_SHAKEN 3, OUTNUMBERED 61, SEEK_ENEMY 751

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
