# Simulación Rally - lote 20261119-042621-t83-tremor-cap

Preset `t83-tremor-cap` · velocidad `sprint` · 107 corridas · 3 escenarios.

| Escenario | n | Victoria azul | Victoria rojo | Superv. azul | Superv. rojo | Tiempo (s) | Daño azul | Daño rojo | Precisión azul | Dist. | Cohesión | Sin contacto |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| t83-escudos | 29 | 97 % ± 3 | 3 % ± 3 | 12.7 ± 0.5 | 0.0 ± 0.0 | 88 ± 5 | 867 ± 25 | 189 ± 11 | 0 % ± 0 | 28.2 ± 0.7 | 23.7 ± 0.8 | 5 |
| t83-cap12 | 30 | 68 % ± 8 | 32 % ± 8 | 8.4 ± 1.1 | 0.3 ± 0.1 | 115 ± 12 | 971 ± 45 | 272 ± 13 | 0 % ± 0 | 19.5 ± 0.5 | 15.8 ± 0.5 | 5 |
| t83-cap16 | 28 | 43 % ± 10 | 57 % ± 10 | 5.8 ± 1.3 | 0.6 ± 0.1 | 113 ± 9 | 850 ± 32 | 294 ± 10 | 0 % ± 0 | 17.5 ± 0.8 | 11.9 ± 0.8 | 9 |

## Espejo (control: debe dar ≈50 %)

## Control de velocidad (misma semilla a velocidad normal)
- t83-escudos semilla 8300: sprint → blue (14/0 vivos), normal → draw (4/1 vivos)

## Jerarquía de órdenes
- Reglas con histéresis (39↔41 %, entrada 40 %, salida 20 %): 1 activación, 0 desactivaciones -> sin alternancia ✓

## Alarma anti-inflado: puntería y alcance (decisiones C1/C2, sin cambiar)
- Alcance por rango +15 % (tope +75 %, R5 = +75 %); dispersión −12 % por rango (suelo 30 %, R5 = 40 %); maestría: cada efecto ≤ 50 % (desde rango 0)

## Comprobación global: vida/daño/velocidad iguales al aparecer y al terminar en 61 de 61 corridas ✓

## Causas de muerte del bando azul (causa:tipo=bajas)
- t83-escudos: enemy:mob=40 enemy:tremorzilla_beam=54 env:radiation=2
- t83-cap12: enemy:mob=83 enemy:tremorzilla_beam=134 env:radiation=12
- t83-cap16: enemy:mob=113 enemy:tremorzilla_beam=152 env:fall=1 env:radiation=21

## Motivos de decisión (cambios registrados): ENGAGE 3959, FORMATION_HOLD 1, LOW_HEALTH_RETREAT 5713, NONE 3630, SEEK_ENEMY 5683, STUCK 1118, UNIT_BROKEN 1

## Qué aprendimos
- Sin diferencias significativas con el lote anterior y ninguna improvisación candidata.

## Hallazgos
- SIN ENFRENTAMIENTO: t83-cap16 - 9 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t83-cap12 - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- SIN ENFRENTAMIENTO: t83-escudos - 5 corrida(s) sin daño entre bandos (excluidas de las tasas; revisar hostilidad/objetivos)
- t83-escudos: el ganador cambia entre sprint y velocidad normal con la misma semilla (la IA no es determinista; si se repite en varios lotes, revisar)
