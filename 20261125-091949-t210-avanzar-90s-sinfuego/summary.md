# Simulación Rally - lote 20261125-091949-t210-avanzar-90s-sinfuego

Preset `t210-avanzar-90s-sinfuego` · velocidad `sprint` · 61 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t210-av-b8 | 20 | 100 % ± 0 | 0 % ± 0 | 5.0 ± 0.3 | 0.0 ± 0.0 | 164 ± 0 | 192 ± 0 | 124 ± 8 | 0 % ± 0 | 76.7 ± 0.3 | 3.5 ± 0.0 | 0 |
| t210-av-b100 | 20 | 100 % ± 0 | 0 % ± 0 | 100.0 ± 0.0 | 0.0 ± 0.0 | 159 ± 0 | 192 ± 0 | 15 ± 3 | 0 % ± 0 | 82.2 ± 0.2 | 7.5 ± 0.0 | 0 |
| t210-av-b8-fin | 20 | 50 % ± 0 | 50 % ± 0 | 8.0 ± 0.0 | 8.0 ± 0.0 | 180 ± 0 | 48 ± 1 | 66 ± 1 | 0 % ± 0 | 122.3 ± 0.3 | 5.0 ± 0.0 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t210-av-b8 semilla 17000: sprint → blue (3/0 vivos), normal → blue (5/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 60 de 60 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t210-av-b8: enemy:mob=61
- t210-av-b100: enemy:mob=1

## Motivos de decisión (cambios registrados): ENGAGE 1485, FORMATION_HOLD 41641, INSTRUCTION 43991, LOW_HEALTH_RETREAT 17, SEEK_ENEMY 1638, STUCK 3

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- t210-av-b8-fin: 20/20 corridas llegaron al tope de tiempo
