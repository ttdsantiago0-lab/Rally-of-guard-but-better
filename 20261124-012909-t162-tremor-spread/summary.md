# Simulación Rally - lote 20261124-012909-t162-tremor-spread

Preset `t162-tremor-spread` · velocidad `sprint` · 104 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t162-mixto-defecto | 28 | 5 % ± 3 | 95 % ± 3 | 0.3 ± 0.1 | 1.0 ± 0.0 | 107 ± 10 | 618 ± 61 | 187 ± 4 | 94 % ± 1 | 20.1 ± 0.9 | 13.1 ± 1.0 | 5 |
| t162-mixto-spread4 | 29 | 12 % ± 4 | 88 % ± 4 | 0.7 ± 0.2 | 1.0 ± 0.0 | 143 ± 11 | 762 ± 69 | 195 ± 7 | 94 % ± 1 | 19.5 ± 0.8 | 13.1 ± 0.8 | 4 |
| t162-mixto-spread7 | 30 | 5 % ± 3 | 95 % ± 3 | 0.3 ± 0.2 | 1.0 ± 0.0 | 137 ± 8 | 809 ± 64 | 203 ± 8 | 94 % ± 1 | 18.6 ± 0.6 | 11.5 ± 0.6 | 7 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t162-mixto-defecto semilla 14100: sprint → red (0/1 vivos), normal → red (0/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 13 de 13 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t162-mixto-defecto: enemy:mob=45 enemy:tremorzilla_beam=156 env:radiation=16
- t162-mixto-spread4: enemy:mob=40 enemy:tremorzilla_beam=159 env:radiation=14
- t162-mixto-spread7: enemy:mob=49 enemy:tremorzilla_beam=172 env:radiation=10

## Motivos de decisión (cambios registrados): ENGAGE 2327, FORMATION_HOLD 1, LOW_HEALTH_RETREAT 2166, NONE 1306, OUT_OF_RANGE 375, SEEK_ENEMY 1939, STUCK 498, TOO_CLOSE 115, UNIT_BROKEN 4

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t162-mixto-spread7 - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t162-mixto-spread4 - 4 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t162-mixto-defecto - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
