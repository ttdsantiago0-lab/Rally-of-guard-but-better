# Simulación Rally - lote 20261124-052650-t179-escudos

Preset `t179-escudos` · velocidad `sprint` · 61 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t179-escudos-once-16 | 30 | 100 % ± 0 | 0 % ± 0 | 4.3 ± 0.1 | 0.0 ± 0.0 | 16 ± 0 | 438 ± 2 | 107 ± 2 | 92 % ± 1 | 8.5 ± 0.2 | 6.6 ± 0.2 | 0 |
| t179-escudos-once-32 | 30 | 100 % ± 0 | 0 % ± 0 | 4.0 ± 0.0 | 0.0 ± 0.0 | 33 ± 0 | 877 ± 3 | 107 ± 1 | 90 % ± 1 | 10.9 ± 0.4 | 13.3 ± 0.9 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t179-escudos-once-16 semilla 16200: sprint → blue (4/0 vivos), normal → blue (4/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t179-escudos-once-16: enemy:mob=111
- t179-escudos-once-32: enemy:mob=120

## Motivos de decisión (cambios registrados): ENGAGE 506, LOW_HEALTH_RETREAT 28, NONE 5, OUT_OF_RANGE 22, TOO_CLOSE 12

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- Ninguna expectativa incumplida ni anomalía detectada.
