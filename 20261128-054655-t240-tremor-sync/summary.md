# Simulación Rally - lote 20261128-054655-t240-tremor-sync

Preset `t240-tremor-sync` · velocidad `sprint` · 67 corridas · 2 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t240-escudos | 29 | 90 % ± 6 | 10 % ± 6 | 11.5 ± 0.8 | 0.1 ± 0.1 | 106 ± 10 | 1009 ± 54 | 203 ± 14 | 0 % ± 0 | 26.8 ± 0.7 | 23.2 ± 0.7 | 7 |
| t240-escudos-sync | 30 | 100 % ± 0 | 0 % ± 0 | 13.0 ± 0.3 | 0.0 ± 0.0 | 55 ± 1 | 711 ± 12 | 134 ± 6 | 0 % ± 0 | 29.5 ± 0.6 | 23.6 ± 0.5 | 0 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t240-escudos semilla 23600: sprint → blue (15/0 vivos), normal → blue (12/0 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 56 de 56 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t240-escudos: enemy:mob=46 enemy:tremorzilla_beam=76 env:radiation=9
- t240-escudos-sync: enemy:mob=26 enemy:tremorzilla_beam=62 env:radiation=2

## Motivos de decisión (cambios registrados): ENGAGE 2144, FORMATION_HOLD 2, LOW_HEALTH_RETREAT 1537, NONE 1920, SEEK_ENEMY 2299, STUCK 385, UNIT_BROKEN 3

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t240-escudos - 7 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
