# Simulación Rally - lote 20261128-053212-t238-tremor-sync

Preset `t238-tremor-sync` · velocidad `sprint` · 67 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t238-escudos | 29 | 88 % ± 6 | 12 % ± 6 | 10.3 ± 0.8 | 0.1 ± 0.1 | 114 ± 11 | 987 ± 67 | 228 ± 12 | 0 % ± 0 | 27.1 ± 0.8 | 21.9 ± 0.9 | 7 |
| t238-escudos-sync | 30 | 97 % ± 3 | 3 % ± 3 | 13.0 ± 0.5 | 0.0 ± 0.0 | 58 ± 3 | 700 ± 13 | 144 ± 10 | 0 % ± 0 | 28.6 ± 0.8 | 22.1 ± 0.8 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t238-escudos semilla 23400: sprint → blue (9/0 vivos), normal → blue (10/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 55 de 55 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t238-escudos: enemy:mob=45 enemy:tremorzilla_beam=111 env:radiation=9
- t238-escudos-sync: enemy:mob=20 enemy:tremorzilla_beam=70

## Motivos de decisión (cambios registrados): ENGAGE 2143, FORMATION_HOLD 4, LOW_HEALTH_RETREAT 1589, NONE 2153, SEEK_ENEMY 2229, STUCK 187, UNIT_BROKEN 4

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t238-escudos - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
