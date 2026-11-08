# Simulación Rally - lote 20261108-202437-t27-kite-vindicator-kitecadencia

Preset `t27-kite-vindicator-kitecadencia` · velocidad `sprint` · 41 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t27-kite-vindicator-arco-kitecadencia-r8 | 20 | 100 % ± 0 | 0 % ± 0 | 8.0 ± 0.0 | 0.0 ± 0.0 | 7 ± 0 | 214 ± 1 | 0 ± 0 | 73 % ± 2 | 13.2 ± 0.5 | 5.0 ± 0.1 | 0 |
| t27-kite-vindicator-linea-kitecadencia-r12 | 20 | 100 % ± 0 | 0 % ± 0 | 6.4 ± 0.2 | 0.0 ± 0.0 | 12 ± 0 | 323 ± 2 | 58 ± 5 | 89 % ± 1 | 7.7 ± 0.2 | 6.8 ± 0.2 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t27-kite-vindicator-arco-kitecadencia-r8 semilla 1: sprint → blue (8/0 vivos), normal → blue (8/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 40 de 40 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t27-kite-vindicator-linea-kitecadencia-r12: enemy:mob=33

## Motivos de decisión (cambios registrados): ENGAGE 332, LOW_HEALTH_RETREAT 31, MORALE_SHAKEN 29, TOO_CLOSE 2

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
